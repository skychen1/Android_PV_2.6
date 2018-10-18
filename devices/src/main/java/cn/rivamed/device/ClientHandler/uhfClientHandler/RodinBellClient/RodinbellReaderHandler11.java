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

    public RodinbellReaderHandler11() {
        super();
    }

    /**
     * 485地址，默认设置位0x01
     */
    static byte address = 0x01;

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        continueIdleCount = 0;
        ByteBuf in = (ByteBuf) msg;
        byte[] buf = new byte[in.readableBytes()];
        in.readBytes(buf);

        Log.d(log_tag, "接收到消息" + Transfer.Byte2String(buf));

        int check = DataProtocol.CheckSum(buf, 0, buf.length - 1);
        if ((check & 0xff) != (0xff & buf[buf.length - 1])) {
            Log.e(log_tag, "效验码验证未通过  数据为" + Transfer.Byte2String(buf) + "计算校验码为" + check);
            return;
        }
        //是否已完成当前指令


        boolean completed = false;

        switch (buf[3]) {
            case DataProtocol.CMD_RESET:
//                completed = processReset(buf);
                break;
            case DataProtocol.CMD_SETDEVICEID:
                completed = processSetDeviceId(buf);
                break;
            case DataProtocol.CMD_GETDEVICEID:
                completed = processGetDeviceId(buf);
                break;
            case DataProtocol.CMD_SET_OUTPUTPOWER:
                completed = processSetPower(buf);
                break;
            case DataProtocol.CMD_GET_OUTPUTPOWER:
                completed = processGetPower(buf);
                break;
            case DataProtocol.CMD_SET_WORK_ANT:
                completed = processSetWorkAnt(buf);
                break;
            case DataProtocol.CMD_GET_WORK_ANT:
                completed = processGetWorkAnt(buf);
                break;
            case DataProtocol.CMD_REALTIME_INVENTORY:
                completed = processRealTimeInventory(buf);
                break;
            case DataProtocol.CMD_FAST_SWITCH_ANT_INVENTORY:
                completed = processSwitchFastInventory(buf);
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
                    if(success){
                        SendRtInventory();
                        setNextWorkAnt = false;
                    }else{
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
        byte[] data = new byte[]{RTINVENTORY_SESSION,RTINVENTORY_TARGET,RTINVENTORY_REPEAT};
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
