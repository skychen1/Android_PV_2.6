package cn.rivamed.device.ClientHandler.uhfClientHandler.RodinBellClient;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.FunctionCode;
import cn.rivamed.Utils.JsonTools;
import cn.rivamed.Utils.Transfer;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfClientMessage;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfHandler;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-05-17 10:23
 * @Modyfied By :
 */

public class RodinbellReaderHandler11 extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {


    private static final String log_tag = "DEV_RDBL_C";
    /**
     * 发送实时盘点（0x89）时的参数
     */
    private static final byte RTINVENTORY_SESSION = (byte) 0X01;//此命令是0X8A方式盘存的时候必须发送的指令
    private static final byte RTINVENTORY_TARGET = (byte) 0X00;//此命令是0X8A方式盘存的时候必须发送的指令
    private static final byte RTINVENTORY_REPEAT = (byte) 0X01;//注：此命令为为0X01的时候，盘存最快，用的时间最短
    /**
     * 预制天线列表，总共是8根天线
     */
    byte[] ants = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};


    /**
     * 当前工作的天线序号
     */
    byte currentAntIndex = 0;

    /**
     * 持续扫描的时间，默认3秒，实际由 StartScan 方法进行赋值
     */
    int scanTime = 3000;

    long startScanTime = new Date().getTime();

    /**
     * 用于记录已扫描到的标签信息
     * <p>
     * 当 扫描完成并触发事件后，进行清空；
     * <p>
     * 开始扫描时，即repeatIndex==0 && currentAntIndex ==0 时，进行清空
     */
    Map<String, List<TagInfo>> epcList = new HashMap<>();

    //是否处于扫描模式
    boolean scanModel = false;

    //
    Object locker = new Object();

    int continueIdleCount = 0;
    private byte[] m_btAryBuffer = new byte[4096];
    private int m_nLength = 0;

    public RodinbellReaderHandler11() {
        super();
    }

    /**
     * 485地址，默认设置位0x01
     */
    static byte address = 0x01;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只要收到包，就认为设备连接正常的，重置发包次数
        continueIdleCount = 0;
        //收到的所有数据都为ByteBuf类型，转换数据
        ByteBuf in = (ByteBuf) msg;
        byte[] buf = new byte[in.readableBytes()];
        in.readBytes(buf);
        //转换数据为byte【】
        Log.d(log_tag, "接收到消息" + Transfer.Byte2String(buf));
        receiveData(buf);
        //以下代码是必须的，释放管道数据，防止内存泄露；
        in.retain();
        ctx.write(in);
        super.channelRead(ctx, msg);
    }

    /**
     * 将收到的数据按照罗丹贝尔的规则进行解析
     */
    private void receiveData(byte[] buf) {
        try {
            int nCount = buf.length;
            byte[] btAryBuffer = new byte[nCount + m_nLength];
            System.arraycopy(m_btAryBuffer, 0, btAryBuffer, 0, m_nLength);
            System.arraycopy(buf, 0, btAryBuffer, m_nLength,
                    buf.length);
            Log.d(log_tag, "需要解析的数据" + Transfer.Byte2String(btAryBuffer));
            int nIndex = 0;
            int nMarkIndex = 0;
            for (int nLoop = 0; nLoop < btAryBuffer.length; nLoop++) {
                if (btAryBuffer.length > nLoop + 1) {
                    if (btAryBuffer[nLoop] == DataProtocol.BEGIN_FLAG) {
                        int nLen = btAryBuffer[nLoop + 1] & 0xFF;
                        Log.d(log_tag, "数据长度为" + nLen);
                        if (nLen > 39) {
                            continue;
                        }
                        if (nLoop + 1 + nLen < btAryBuffer.length) {
                            byte[] btAryAnaly = new byte[nLen + 2];
                            System.arraycopy(btAryBuffer, nLoop, btAryAnaly, 0,
                                    nLen + 2);
                            processData(btAryAnaly);
                            nLoop += 1 + nLen;
                            nIndex = nLoop + 1;
                        } else {
                            nLoop += 1 + nLen;
                        }
                    } else {
                        Log.e(log_tag, "出现标志位异常的情况：：");
                        nMarkIndex = nLoop;
                    }
                }
            }
            if (nIndex < nMarkIndex) {
                nIndex = nMarkIndex + 1;
            }
            if (nIndex < btAryBuffer.length) {
                m_nLength = btAryBuffer.length - nIndex;
                m_btAryBuffer = new byte[btAryBuffer.length - nIndex];
                System.arraycopy(btAryBuffer, nIndex, m_btAryBuffer, 0,
                        btAryBuffer.length - nIndex);
                Log.e(log_tag, "解析完一个包剩余的数据：：" + Transfer.Byte2String(m_btAryBuffer));
            } else {
                m_nLength = 0;
            }
        } catch (Exception e) {
            Log.e(log_tag, e.toString());
        }
    }

    private void processData(byte[] buf) {
        //协议最小数据是6，如果数据小于6就直接退出，防止错误数据崩溃
        //如果数据不合法，直接丢掉数据包,判断上一条协议如果是扫描，就要重置设备
        if (buf.length < 6) {
            Log.e(log_tag, "数据出错，数据长度小于6：：" + Transfer.Byte2String(buf));
            return;
        }
        //检测数据的合法性
        int check = DataProtocol.CheckSum(buf, 0, buf.length - 1);
        //如果数据不合法，直接丢掉数据包,判断上一条协议如果是扫描，就要重置设备
        if ((check & 0xff) != (0xff & buf[buf.length - 1])) {
            Log.e(log_tag, "效验码验证未通过  数据为" + Transfer.Byte2String(buf) + "计算校验码为" + check);
            return;
        }
        //根据协议，第三个字节流是类型数据，根据不同类型的数据做不同的处理
        switch (buf[3]) {
            //复位的回包，不处理？
            case DataProtocol.CMD_RESET:
                //重置没有回复的消息
                break;
            //获取读写器的识别码
            case DataProtocol.CMD_GETDEVICEID:
                processGetDeviceId(buf);
                break;
            //设置读写器射频输出功率
            case DataProtocol.CMD_SET_OUTPUTPOWER:
                processSetPower(buf);
                break;
            //获取读写器射频输出功率
            case DataProtocol.CMD_GET_OUTPUTPOWER:
                processGetPower(buf);
                break;
            //设置读写器工作天线
            case DataProtocol.CMD_SET_WORK_ANT:
                processSetWorkAnt(buf);
                break;
            //获取读写器工作天线
            case DataProtocol.CMD_GET_WORK_ANT:
                //本服务器模式将获取工作天线作为心跳包，所以此处不处理
                break;
            //自定义session和target盘存
            case DataProtocol.CMD_REALTIME_INVENTORY:
                processRealTimeInventory(buf);
                break;
            //快速轮询多个天线盘存标签
            case DataProtocol.CMD_FAST_SWITCH_ANT_INVENTORY:
                //暂时没有使用该指令，所以没有这样的回复，不处理
                break;
            default:
                break;
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            switch (stateEvent.state()) {
                case READER_IDLE:
                case ALL_IDLE:
                case WRITER_IDLE:
                    continueIdleCount++;
                    if (continueIdleCount > 5) {
                        Close();
                        return;
                    }
                    SendGetWorkAnt();
                    break;
            }
        } else
            super.userEventTriggered(ctx, evt);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

        Log.d(log_tag, "channelRegistered 事件发生");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Log.d(log_tag, "channelActive 事件发生");
        super.channelActive(ctx);
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            SendGetDeviceId();
        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.d(log_tag, "channelInactive 事件发生");
        super.channelInactive(ctx);
    }

    private boolean processGetDeviceId(byte[] buf) {

        if (buf.length != 17 || buf[3] != DataProtocol.CMD_GETDEVICEID) {
            SendGetDeviceId();
            return false;
        }
        byte[] idBuf = new byte[12];
        System.arraycopy(buf, 4, idBuf, 0, 12);
        super.setIdentification(Transfer.Byte2String(idBuf));
        if (this.messageListener != null) {
            messageListener.OnConnected();
        }
        return true;
    }

    private boolean processSetDeviceId(byte[] buf) {
        if (buf[3] != DataProtocol.CMD_SETDEVICEID) {
            Log.e(log_tag, "处理RodinBell 设置设备ID信息错误，信息内容为 " + Transfer.Byte2String(buf));
            return true;
        }

        if (buf.length != 17 || buf[3] != DataProtocol.CMD_GETDEVICEID) {
            SendGetDeviceId();
            return false;
        }
        byte[] idBuf = new byte[12];
        System.arraycopy(buf, 4, idBuf, 0, 12);
        super.setIdentification(Transfer.Byte2String(idBuf));

        if (this.messageListener != null) {
            messageListener.OnConnected();
        }
        return true;
    }

    private boolean processSetPower(byte[] buf) {
        if (buf[3] != DataProtocol.CMD_SET_OUTPUTPOWER) {
            Log.e(log_tag, "处理RodinBell 设置设备ID信息错误，信息内容为 " + Transfer.Byte2String(buf));
            return true;
        }
        if (this.messageListener != null) {
            messageListener.OnUhfSetPowerRet(this.getIdentification(), buf[4] == DataProtocol.CMD_SUCCESS);
        }
        return true;
    }

    private boolean processGetPower(byte[] buf) {
        if (buf[3] != DataProtocol.CMD_GET_OUTPUTPOWER) {
            Log.e(log_tag, "处理RodinBell 设置设备ID信息错误，信息内容为 " + Transfer.Byte2String(buf));
            return false;
        }

        if (this.messageListener != null) {
            int allPower = -1;
            if (buf.length == 6) {
                allPower = buf[4];
            } else {
                allPower = -1;
                for (int i = 0; i < 4; i++) {
                    if (allPower < buf[4 + i]) {
                        allPower = buf[4 + i];
                    }
                }
            }
            if (this.messageListener != null)
                messageListener.OnUhfQueryPowerRet(this.getIdentification(), true, allPower);
            return true;
        }
        return false;
    }

    private boolean processSetWorkAnt(byte[] buf) {

        if (buf[3] != DataProtocol.CMD_SET_WORK_ANT) {
            Log.e(log_tag, "处理RodinBell 设置设备ID信息错误，信息内容为 " + Transfer.Byte2String(buf));
            return true;
        }

        boolean success = buf[4] == DataProtocol.CMD_SUCCESS;
        Log.d(log_tag, "设备" + getIdentification() + " 设置天线" + ants[currentAntIndex] + (success ? "成功" : "失败"));
        boolean setNextWorkAnt = false;  //是否需要重新发送设置天线命令，该变量能防死锁
        boolean sendScanRet = false;
        synchronized (locker) {
            if (scanModel) {
                if (ScanTimeCompleted()) { //已扫描到最大值
                    sendScanRet = true;
                    setNextWorkAnt = false;
                } else {
                    if (success) {
                        SendRtInventory();
                        setNextWorkAnt = false;
                    } else {
                        setNextWorkAnt = true;
                    }
                    sendScanRet = false;
                }
            }
        }

        if (scanModel && !sendScanRet && setNextWorkAnt) {
            currentAntIndex++;
            SendNextWorkAnt();
        } else if (sendScanRet)
            ProcessScanCompleted();
        return true;
    }

    private boolean processGetWorkAnt(byte[] buf) {
        if (buf[3] != DataProtocol.CMD_GET_WORK_ANT) {
            Log.e(log_tag, "处理RodinBell 设置设备ID信息错误，信息内容为 " + Transfer.Byte2String(buf));
            return true;
        }

        if (this.messageListener != null) {
            //      messageListener.OnGetWorkAnt(true, buf[4]);
        }
        return true;
    }

    private boolean processRealTimeInventory(byte[] buf) {
        synchronized (locker) {
            if (!scanModel) return true;
        }
        //(buf.length    ==6 标识执行错误   ==12 标识一个天线扫描执行结束    ==修改了天线的范围
        if ((buf.length == 6 || (buf.length == 12 && buf[4] >= 0x00 && buf[4] <= 0x07))) {
            boolean sentScanRet = false; //是否发送结果到MQ

            synchronized (locker) {  //判断当前是否为最后一次扫描
                if (ScanTimeCompleted()) { //已扫描到最大值
                    sentScanRet = true;
                }
            }
            if (!sentScanRet) {
                currentAntIndex++;
                SendNextWorkAnt();
                return false;
            } else
                ProcessScanCompleted();
            return true;
        } else {  //解析标签
            int position = 0;
            while (position < buf.length) {
                if (buf[position] != DataProtocol.BEGIN_FLAG) {
                    position += 1;
                    continue;
                }
                if ((position + 2 + buf[position + 1] & 0xff) > buf.length) {
                    break;
                }

                //计算校验码
                int len = (buf[position + 1] & 0xff) + 2;
                if ((buf[position + len - 1] & 0xff) != DataProtocol.CheckSum(buf, position, len - 1)) {
                    position += len;
                    continue;
                }
                byte ant = (byte) ((buf[position + 4] & 0x03) & 0xff);
                byte[] bPc = new byte[2];
                System.arraycopy(buf, position + 5, bPc, 0, 2);
                String pc = Transfer.Byte2String(bPc);
                int lenEpc = len - 9;
                byte[] bEpc = new byte[lenEpc];
                System.arraycopy(buf, position + 7, bEpc, 0, lenEpc);
                String epc = Transfer.Byte2String(bEpc);
                byte brssi = buf[position + 5 + lenEpc];
                int rssi = 0 - (129 - (brssi & 0xff));  //转变成真实的RSSI,参见开发文档第五节
                TagInfo lbinfo = new TagInfo(rssi, ant, pc);

                position += len;

                if (epcList.containsKey(epc)) {
                    epcList.get(epc).add(lbinfo);
                } else {
                    epcList.put(epc, new ArrayList<>());
                    epcList.get(epc).add(lbinfo);
                }
            }
            return false;
        }
    }

    private boolean processSwitchFastInventory(byte[] buf) {
        return false;
    }

    private boolean ProcessScanCompleted() {
        synchronized (locker) {
            scanModel = false;
            Log.i(log_tag, "扫描完成,发送扫描结果:EpcCount=" + epcList.size() + ",列表=" + JsonTools.getJsonString("EPCLIST", epcList));

            if (this.messageListener != null)
                messageListener.OnUhfScanRet(true, this.getIdentification(), "", epcList);
            scanModel = false;
            currentAntIndex = 0;
        }
        return true;
    }

    /**
     * 扫描过程中检测已扫描的时间是否超过预定时间
     */
    private boolean ScanTimeCompleted() {
        return new Date().getTime() - startScanTime > scanTime;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.UHFREADER;
    }

    @Override
    public String getRemoteIP() {
        String address = this.getCtx() == null ? "" : this.getCtx().pipeline().channel().remoteAddress().toString();
        if (!StringUtil.isNullOrEmpty(address)) {
            address = address.replace("/", "");
            address = address.substring(0, address.indexOf(":"));
        }
        return address;
    }

    @Override
    public String getProducer() {
        return "RodinBell";
    }

    @Override
    public String getVersion() {
        return "V1.0";
    }

    private boolean SendBuf(byte cmd, byte[] buf) {
        if (super.getCtx() != null) {
            try {
                byte[] bufSend = DataProtocol.PieceCommond(address, cmd, buf);
                ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, bufSend.length, bufSend.length);
                byteBuf.writeBytes(bufSend);
                super.getCtx().write(byteBuf);
                super.getCtx().flush();
                Log.d(log_tag, "向设备" + getIdentification() + "发送命令" + Transfer.Byte2String(bufSend));
                return true;
            } catch (Throwable e) {
                Log.e(log_tag, "发送数据发生错误" + e.getMessage());
            }
        }
        return false;
    }

    private boolean SendSetDeviceId(byte[] buf) {
        if (buf.length != 0) {
            byte[] bufOrg = new byte[buf.length];
            System.arraycopy(buf, 0, bufOrg, 0, buf.length);
            buf = new byte[12];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = 0;
            }
            System.arraycopy(bufOrg, 0, buf, 0, bufOrg.length);
        }
        return SendBuf(DataProtocol.CMD_SETDEVICEID, buf);
    }

    private boolean SendGetDeviceId() {
        return SendBuf(DataProtocol.CMD_GETDEVICEID, null);
    }

    private boolean SendSetWorkAnt(byte workAnt) {
        synchronized (locker) {
            if (scanModel) return false;
        }
        if (workAnt < 0 || workAnt > 3) {
            workAnt = ants[0];
        }
        if (workAnt > ants[ants.length - 1]) {
            workAnt = ants[ants.length - 1];
        }
        byte[] data = new byte[]{workAnt};
        return SendBuf(DataProtocol.CMD_SET_WORK_ANT, data);
    }

    private boolean SendNextWorkAnt() {
        synchronized (locker) {
            if (!scanModel) {
                return false;
            }
        }
        synchronized (locker) {
            if (scanModel) {
                if (currentAntIndex > ants.length - 1) {
                    currentAntIndex = 0;
                }
            }
            return SendBuf(DataProtocol.CMD_SET_WORK_ANT, new byte[]{ants[currentAntIndex]});
        }
    }


    private boolean SendGetWorkAnt() {
        byte[] data = new byte[]{};
        return SendBuf(DataProtocol.CMD_GET_WORK_ANT, data);
    }

    /**
     * 仅发送盘点命令，不做他用。开始盘点请参照 StartInventory
     */
    private boolean SendRtInventory() {
        //data是0X8A盘存要发送的命令
        byte[] data = new byte[]{RTINVENTORY_SESSION, RTINVENTORY_TARGET, RTINVENTORY_REPEAT};
        Log.d(log_tag, "发送实时盘点命令，Ant=" + currentAntIndex);
        return SendBuf(DataProtocol.CMD_REALTIME_INVENTORY, data);//将盘存的全部指令发送出去
    }

    private boolean SendSetPower(byte power) {
        byte[] data = new byte[]{power};
        return SendBuf(DataProtocol.CMD_SET_OUTPUTPOWER, data);
    }

    /**
     * 正式的盘点开始，调用该方法
     */
    private boolean StartInventory() {
        synchronized (locker) {
            if (scanModel) return false;
            scanModel = true;
            epcList.clear();
            currentAntIndex = 0;
            startScanTime = new Date().getTime();
        }
        return SendNextWorkAnt();
    }

    /**
     * 获取功率
     */
    private boolean sendreset() {
        boolean ret = SendBuf(DataProtocol.CMD_RESET, null);
        Close();
        return ret;
    }

    /**
     * 获取功率
     */
    private boolean SendGetPower() {
        byte[] data = new byte[]{};
        return SendBuf(DataProtocol.CMD_GET_OUTPUTPOWER, data);
    }


    @Override
    public int StartScan() {
        return StartScan(3000);
    }

    @Override
    public int StartScan(int timeout) {
        if (scanModel) return FunctionCode.DEVICE_BUSY;
        if (timeout <= 0)
            timeout = 3000;
        this.scanTime = timeout;
        this.startScanTime = new Date().getTime();
        return StartInventory() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int StopScan() {
        scanModel = false;
        return 0;
    }

    @Override
    public int SetPower(byte power) {
        return SendSetPower(power) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int QueryPower() {
        return SendGetPower() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public List<Integer> getUhfAnts() {
        return null;
    }

    @Override
    public int Reset() {
        return sendreset() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int Close() {
        try {
            Log.e(log_tag, "已断开与设备 DeviceId=" + getIdentification() + "的连接");
            getCtx().close();
        } catch (Exception ex) {
            Log.e(log_tag, ex.getMessage());
        } finally {
            if (!StringUtil.isNullOrEmpty(getIdentification()) && this.messageListener != null) {
                this.messageListener.OnDisconnected();
            }
        }
        return FunctionCode.SUCCESS;
    }

    /**
     * 以下为监听器部分
     */

    protected UhfClientMessage messageListener;

    public void RegisterMessageListener(UhfClientMessage messageListener) {
        this.messageListener = messageListener;
        this.messageListener.setDeviceHandler(RodinbellReaderHandler11.this);
    }

    /**
     * 数据协议部分（仅包含部分数据协议）
     * 在进行开发时，仍然需要参照厂家的开发文档
     */

    static class DataProtocol {

        public static final byte BEGIN_FLAG = (byte) 0xA0;

        public static final byte CMD_SETDEVICEID = (byte) 0x67;

        public static final byte CMD_GETDEVICEID = (byte) 0x68;

        public static final byte CMD_SET_OUTPUTPOWER = (byte) 0x76;
        public static final byte CMD_GET_OUTPUTPOWER = (byte) 0x77;

        public static final byte CMD_RESET = (byte) 0x70;

        public static final byte CMD_SET_WORK_ANT = 0X74;

        public static final byte CMD_GET_WORK_ANT = 0X75;

        /**
         * 指定天线扫描，与CMD_SET_WORK_ANT 配合
         */
        public static final byte CMD_REALTIME_INVENTORY = (byte) 0x8B;

        /**
         * 快速多天线盘点，适用于快速切换天线进行盘点
         */
        public static final byte CMD_FAST_SWITCH_ANT_INVENTORY = (byte) 0x8A;


        public static final byte CMD_SUCCESS = 0X10;


        public static byte[] PieceCommond(byte address, byte cmd, byte[] data) {
            /**
             *  head |  Len   |   Address |  cmd    |  data    | check
             *  --------------------------------------------------------
             *  1byte|  1byte |  1byte    |  1byte  |  N byte  |  1Byte
             *
             * */
            byte[] picecBuf = new byte[(data == null ? 0 : data.length) + 5]; //前4 后1
            picecBuf[0] = BEGIN_FLAG;
            picecBuf[1] = (byte) ((picecBuf.length - 2) & 0xff);  //len  从address开始计算直至结尾，不含len
            picecBuf[2] = (byte) (address & 0xff);
            picecBuf[3] = cmd;
            if (data != null) {
                System.arraycopy(data, 0, picecBuf, 4, data.length);
            }
            picecBuf[picecBuf.length - 1] = (byte) CheckSum(picecBuf, 0, picecBuf.length - 1);
            return picecBuf;
        }

        public static int CheckSum(byte[] buf, int offset, int len) {
            int i = 0, uSum = 0;
            for (i = 0; i < len; i++) {
                int v = buf[i + offset];
                uSum += 0xff & v;
            }
            uSum = (~(uSum & 0xff)) + 1;
            return (uSum) & 0xff;
        }
    }

}
