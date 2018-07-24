package cn.rivamed.device.ClientHandler.uhfClientHandler.RodinBellClient;

import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONStringer;

import cn.rivamed.FunctionCode;
import cn.rivamed.Utils.JsonTools;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfClientMessage;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfHandler;
import cn.rivamed.Utils.Transfer;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;


import java.util.*;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-05-17 10:23
 * @Modyfied By :
 */

public class RodinbellReaderHandler extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {


    private static final String log_tag = "DEV_RDBL_C";
    /**
     * 预制天线列表
     */
    byte[] ants = {0x00, 0x01, 0x02, 0x03};

    boolean bStopInventory = false;

    /**
     * 当前工作的天线序号
     */
    byte currentAntIndex = 0;

    /**
     * 用于标记一次扫描过程中当前是第几轮扫描
     */
    int repeatIndex = 0;

    /**
     * 用于记录扫描过程中的扫描重复次数，是repeatIndex所能达到的最大值
     */
    int repeatCount = 1;


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

    public RodinbellReaderHandler() {
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
            return true;
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

                messageListener.OnUhfQueryPowerRet(this.getIdentification(), true, allPower);
            }
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
            if (!scanModel) {
                if (bStopInventory) return true; //已停止
                if (success) {  //如果设置成功，则发送扫描指令
                    SendRtInventory((byte) repeatCount);
                    return false;
                }
                if (repeatIndex >= repeatCount && currentAntIndex >= ants.length - 1) { //已扫描到最大值
                    sendScanRet = true;
                    setNextWorkAnt = false;
                } else {
                    sendScanRet = false;
                    setNextWorkAnt = true;
                }
            }
        }

        if (!sendScanRet && setNextWorkAnt && !bStopInventory) {
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
        //(buf.length    ==6 标识执行错误   ==12 标识一个天线扫描执行结束
        if ((buf.length == 6 || (buf.length == 12 && buf[4] >= 0x00 && buf[4] <= 0x03))) {
            boolean setNextWorkAnt = false;  //是否继续扫描
            boolean sentScanRet = false; //是否发送结果到MQ

            synchronized (locker) {  //判断当前是否为最后一次扫描
                if (repeatIndex >= repeatCount && currentAntIndex >= ants.length - 1) { //已扫描到最大值
                    sentScanRet = true;
                    setNextWorkAnt = false;
                } else if (repeatIndex <= repeatCount) {
                    setNextWorkAnt = true;
                    sentScanRet = false;
                }
            }
            if (!bStopInventory) {
                if (!sentScanRet && setNextWorkAnt && !bStopInventory) {
                    currentAntIndex++;
                    SendNextWorkAnt();
                    return false;
                } else if (sentScanRet)
                    ProcessScanCompleted();
            } else {
                synchronized (locker) {
                    scanModel = false;
                }
            }
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
                messageListener.OnUhfScanComplete(true, this.getIdentification());
            scanModel = false;
            repeatIndex = 0;
            repeatCount = 0;
            currentAntIndex = 0;
            epcList.clear();
        }
        return true;
    }


    @Override
    public DeviceType getDeviceType() {
        return DeviceType.RodinBellReader;
    }

    @Override
    public String getRemoteIP() {
        String address = this.getCtx() == null ? "" : this.getCtx().pipeline().channel().remoteAddress().toString();
        if (StringUtil.isNullOrEmpty(address)) {
            address = address.replace("/", "");
            address = address.substring(0, address.indexOf(":"));
        }
        return address;
    }

    private void SendBuf(byte cmd, byte[] buf) {
        if (super.getCtx() != null) {
            byte[] bufSend = DataProtocol.PieceCommond(address, cmd, buf);
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, bufSend.length, bufSend.length);
            byteBuf.writeBytes(bufSend);
            super.getCtx().write(byteBuf);
            super.getCtx().flush();
            Log.d(log_tag, "向设备" + getIdentification() + "发送命令" + Transfer.Byte2String(bufSend));
        }
    }

    private void SendSetDeviceId(byte[] buf) {
        if (buf.length != 0) {
            byte[] bufOrg = new byte[buf.length];
            System.arraycopy(buf, 0, bufOrg, 0, buf.length);
            buf = new byte[12];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = 0;
            }
            System.arraycopy(bufOrg, 0, buf, 0, bufOrg.length);
        }
        SendBuf(DataProtocol.CMD_SETDEVICEID, buf);
    }

    private void SendGetDeviceId() {
        SendBuf(DataProtocol.CMD_GETDEVICEID, null);
    }

    private void SendSetWorkAnt(byte workAnt) {
        synchronized (locker) {
            if (scanModel) return;
        }
        if (workAnt < 0 || workAnt > 3) {
            workAnt = ants[0];
        }
        if (workAnt > ants[ants.length - 1]) {
            workAnt = ants[ants.length - 1];
        }
        byte[] data = new byte[]{workAnt};
        SendBuf(DataProtocol.CMD_SET_WORK_ANT, data);
    }

    private void SendNextWorkAnt() {
        synchronized (locker) {
            if (!scanModel) {
                return;
            }
        }
        synchronized (locker) {
            if (scanModel) {
                if (currentAntIndex > ants.length - 1) {
                    currentAntIndex = 0;
                }
                if (currentAntIndex == 0) repeatIndex++;
            }
            SendBuf(DataProtocol.CMD_SET_WORK_ANT, new byte[]{ants[currentAntIndex]});
        }
    }


    private void SendGetWorkAnt() {
        byte[] data = new byte[]{};
        SendBuf(DataProtocol.CMD_GET_WORK_ANT, data);
    }

    /**
     * 仅发送盘点命令，不做他用。开始盘点请参照 StartInventory
     */
    private void SendRtInventory(byte repeat) {
        if (repeat <= 0) repeat = 0x10;
        byte[] data = new byte[]{repeat};
        Log.d(log_tag, "发送实时盘点命令，Ant=" + currentAntIndex + " ;RepeatIndex=" + repeatIndex + " ;RepeatCount=" + repeatCount);
        SendBuf(DataProtocol.CMD_REALTIME_INVENTORY, data);
    }

    private void SendSetPower(byte power) {
        byte[] data = new byte[]{power};
        SendBuf(DataProtocol.CMD_SET_OUTPUTPOWER, data);
    }

    /**
     * 正式的盘点开始，调用该方法
     */
    private boolean StartInventory(byte repeat) {
        synchronized (locker) {
            if (scanModel) return false;
            scanModel = true;
            bStopInventory = false;
            epcList.clear();
            currentAntIndex = 0;
            repeatCount = repeat;
            repeatIndex = 0;
        }
        SendNextWorkAnt();
        return true;
    }

    /**
     * 获取功率
     */
    private void SendGetPower() {
        byte[] data = new byte[]{};
        SendBuf(DataProtocol.CMD_GET_OUTPUTPOWER, data);
    }


    @Override
    public int StartScan() {
        if (scanModel) return FunctionCode.DEVICE_BUSY;
        return StartInventory((byte) 3) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int StopScan() {
        scanModel=false;
        return 0;
    }

    @Override
    public int SetPower(int power) {
        return 0;
    }

    @Override
    public int QueryPower() {
        return 0;
    }

    @Override
    public int Reset() {
        return 0;
    }

    @Override
    public int Close() {
        return 0;
    }

    /**
     * 以下为监听器部分
     */

    protected UhfClientMessage messageListener;

    public void RegisterMessageListener(UhfClientMessage messageListener) {
        this.messageListener = messageListener;
        this.messageListener.setDeviceHandler(RodinbellReaderHandler.this);
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
        public static final byte CMD_REALTIME_INVENTORY = (byte) 0x89;

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
