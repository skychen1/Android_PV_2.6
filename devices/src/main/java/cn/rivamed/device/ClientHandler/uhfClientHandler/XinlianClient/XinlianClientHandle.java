package cn.rivamed.device.ClientHandler.uhfClientHandler.XinlianClient;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.FunctionCode;
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
import io.netty.util.internal.StringUtil;
import sun.java2d.pipe.BufferedTextPipe;

public class XinlianClientHandle extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {

    private static final String LOG_TAG = "DEV_XL_NC";
    Map<String, List<TagInfo>> epcs = new HashMap<>();
    private int MAX_ANT = 8;
    private int MAX_POWER = 30;

    private long queryConnIndex = 0l;

    /**
     * 扫描超时时间，单位  毫秒
     */
    private int inventoryTimeOut = 4000;

    boolean scanMode = false;

    int antByte = 0x0;  //用数位标识天线，从低位开始 第一位为1 标识天线 1存在，0 则不存在，以此类推

    int repeat = 1;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // super.channelRead(ctx, msg);
        try {
            ByteBuf in = (ByteBuf) msg;//消息的数据格式（包含了具体的什么数据）
            byte[] buf = new byte[in.readableBytes()];
            in.readBytes(buf);
            Log.d(LOG_TAG, "接收到消息" + Transfer.Byte2String(buf));

            //计算校验码  判断校验码
            byte[] crcCheckBuf = new byte[buf.length - 3];//这个是什么意思？
            System.arraycopy(buf, 1, crcCheckBuf, 0, crcCheckBuf.length);
            byte[] crc = DataProtocol.CalcCRC(crcCheckBuf, crcCheckBuf.length);
            if (crc[0] != buf[buf.length - 2] || crc[1] != buf[buf.length - 1]) {
                Log.w(LOG_TAG, "接收到的信息有误，校验码未通过  origin=" + Transfer.Byte2String(buf) + "||ji算的校验码=" + Transfer.Byte2String(crc));
                return;
            }

            switch (buf[2]) {
                case DataProtocol.CMD_QUERY_SERIAL_NUM:
                    ProcessQuerySerialNum(buf);
                    break;
                case DataProtocol.CMD_EPC_INVENTORY:
                    ProcessInventory(buf);
                    break;
                case DataProtocol.CMD_QUERY_INVENTORY_RET:
                    break;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void ProcessPullScanRet(byte[] buf){
        int unUpdateTagCount=buf[];


    }

    /**
     * 处理 0x22的执行结果返回信息
     * */
    private void ProcessInventory(byte[] buf) {
        /**
         *  主机到模块：
         *   FF	 |    05	|     22   |    00   |   	00  00	 |       00  C8	  |    ? ?
         * ---------------------------------------------------------------------------------
         *  SOH  | Length   |  Opcode  | Option  |  Search Flags |     Timeout    |    CRC
         *
         *  模块到主机(本处解析)
         *   FF	 |    04	|    22   |	  00  00 |	  00	  |   00  00	 |    00	  |  ??
         * ------------------------------------------------------------------------------------------
         *   SOH |  Length  | Opcode  |  Status  |    Option  | Search Flags | Tag Found  |  CRC
         **/
        int status = buf[3] * 256 + buf[4];
        if (status == 0x0000) { //成功
            boolean more255 = (buf[7] & 0x10) == 0x10;   //0x0   0001 0000  bit4 ==1 表示标签数量大于255
            int tagCount = more255 ? (255 + buf[8] & 0xff) : (buf[8] & 0xff);
            Log.i(LOG_TAG, "扫描完成，共扫描到标签数量为" + tagCount);
            SendPullScanRet();
        }
    }

    /**
     *  处理返回回来的序列号
     *
     * @param buf
     */
    private void ProcessQuerySerialNum(byte[] buf) {
        byte[] bStatus = new byte[2];
        int len = buf[1] & 0xff;
        System.arraycopy(buf, 3, bStatus, 0, bStatus.length);
        int status = (bStatus[0] & 0xff) * 256 + bStatus[1] & 0xff;
        if (status == 0x0000) {
            //成功
            byte[] bufSn = new byte[len];
            System.arraycopy(buf, 5, bufSn, 0, bufSn.length);
            setIdentification(Transfer.Byte2String(bufSn));
            Log.i(LOG_TAG, "获取到设备ID=" + getIdentification());
            if (messageListener != null) {
                messageListener.OnConnected();
            }
        } else {
            Log.e(LOG_TAG, "芯联阅读器获取序列号解析失败 buf=" + Transfer.Byte2String(buf));
        }
    }

    /**
     * 内部方法,用于添加新的EPC
     */
    public synchronized void AppendNewTag(String epc, TagInfo tagInfo) {
        if (!scanMode) {
            return;
        }
        if (this.epcs.containsKey(epc)) {
            this.epcs.get(epc).add(tagInfo);
        } else {
            List<TagInfo> tagInfos = new ArrayList<>();
            tagInfos.add(tagInfo);
            this.epcs.put(epc, tagInfos);
        }
    }


    int sendQuerySN = 0;  //累计获取序列号，3次未获取到，则主动断开

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        super.channelActive(ctx);
        Log.d(LOG_TAG, "设备已连接" + ctx.channel().remoteAddress());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        //发送获取MAC
        new Thread(() -> {
            sendQuerySN = 0;
            while (StringUtil.isNullOrEmpty(getIdentification())) {
                if (sendQuerySN > 5) {
                    Log.e(LOG_TAG, "超时未获取到M序列号，将强制断开");
                    Close();
                    return;
                }

                SendQuerySerialNum();
                sendQuerySN++;

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {

                }
            }
        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        Log.w(LOG_TAG, "通道已关闭，Reader已断开");
        Close();
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
    public int StartScan() {
        if (scanMode) return FunctionCode.DEVICE_BUSY;
        epcs.clear();
        scanMode = true;
        return SendStartInventory() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int StartScan(int repeat) {
        if (repeat <= 0) repeat = 1;
        this.repeat = repeat;
        return StartScan();
    }

    @Override
    public int StopScan() {
        scanMode = false;
        return SendStop() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int SetPower(byte power) {
        if (power <= 0 || power > 30) return FunctionCode.PARAM_ERROR;
        return FunctionCode.DEVICE_NOT_SUPPORT;
//        return SendOptionPower(power) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int QueryPower() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
//        return SendQueryPower() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public List<Integer> getUhfAnts() {
        List<Integer> ants = new ArrayList<>();
        for (int i = 0; i < MAX_ANT; i++) {
            ants.add(i);
        }
        return ants;
    }

    @Override
    public int Reset() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
//        return SendReset() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    private boolean SendStop() {
        return false;
    }

    /**
     * 发送 0x29 指令，获取 0x22的扫描结果
     * */
    private boolean SendPullScanRet() {
        byte cmd = DataProtocol.CMD_QUERY_INVENTORY_RET;
        byte[] data = new byte[3];

        int metaFlag = 0x0000 | 0x0002 | 0x0004;
        data[0] = (byte) (metaFlag / 256);
        data[1] = (byte) (metaFlag % 256);
        data[2] = 0x00; //返回剩余的标签数量
       return SendBuf(cmd, data);
    }

    /**
     * 发送 0x22 盘点指令
     */
    private boolean SendStartInventory() {
        byte cmd = DataProtocol.CMD_EPC_INVENTORY;
        byte[] data = new byte[5];
        data[0] = 0x00; // option
        data[1] = 0x00; // search flag 0
        data[2] = 0x00; //search flag 1
        data[3] = (byte) (inventoryTimeOut / 256);
        data[4] = (byte) (inventoryTimeOut % 256);
        return SendBuf(cmd, data);
    }


    /**
     * 向Reader硬件发送获取序列号指令
     * 发送序列号
     */
    private boolean SendQuerySerialNum() {
        byte cmd = DataProtocol.CMD_QUERY_SERIAL_NUM;
        byte[] data = new byte[2];
        data[0] = 0x00;
        data[1] = 0x00;
        return SendBuf(cmd, data);
    }

    /**
     * 向Reader硬件设备发送数据的总入口
     */
    private synchronized boolean SendBuf(byte cmd, byte[] data) {
        byte[] buf = new byte[(data == null ? 0 : data.length) + 5];
        buf[0] = DataProtocol.HEAD_BEGIN;
        buf[1] = (byte) (data == null ? 0 : data.length);
        buf[2] = cmd;
        if (data != null) {
            System.arraycopy(data, 0, buf, 3, data.length);
        }

        byte[] checkBuf = new byte[buf.length - 3];
        System.arraycopy(buf, 1, checkBuf, 0, checkBuf.length);
        byte[] crc = DataProtocol.CalcCRC(checkBuf, checkBuf.length);
        System.arraycopy(crc, 0, buf, buf.length - 2, crc.length);
        if (getCtx() != null) {
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, buf.length, buf.length);
            byteBuf.writeBytes(buf);
            getCtx().writeAndFlush(byteBuf);
            Log.d(LOG_TAG, "向客户端发送信息成功:" + Transfer.Byte2String(buf));
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {

            }
            return true;
        }
        Log.e(LOG_TAG, "向客户端发送信息失败:" + Transfer.Byte2String(buf));
        return false;
    }

    @Override
    public String getProducer() {
        return "芯联";
    }

    @Override
    public String getVersion() {
        return "V1.0";
    }

    @Override
    public int Close() {
        try {
            Log.e(LOG_TAG, "已断开与设备 DeviceId=" + getIdentification() + "的连接");
            getCtx().close();

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        } finally {
            if (!StringUtil.isNullOrEmpty(getIdentification()) && this.messageListener != null) {
                this.messageListener.OnDisconnected();
            }
        }
        return FunctionCode.SUCCESS;
    }

    protected UhfClientMessage messageListener;

    public void RegisterMessageListener(UhfClientMessage messageListener) {
        this.messageListener = messageListener;
        this.messageListener.setDeviceHandler(this);
    }

    private static class DataProtocol {
        public static final byte HEAD_BEGIN = (byte) 0xFF;
        /**
         * 获取序列号
         */
        public static final byte CMD_QUERY_SERIAL_NUM = 0X10;
        /**
         * 多标签盘存
         */
        public static final byte CMD_EPC_INVENTORY = 0X22;
        /**
         * 获取盘点结果；在接收到0X22的执行结果之后发送
         */
        public static final byte CMD_QUERY_INVENTORY_RET = 0X29;


        /***
         * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         *
         * 以下部分为校验码计算，详情参考厂家文档
         *
         */

        private static final Integer MSG_CRC_INIT = 0xFFFF;
        private static final Integer MSG_CCITT_CRC_POLY = 0x1021;

        static short CRC_calcCrc8(int crcReg, int poly, int u8Data) {
            short i;
            int xorFlag;
            int bit;
            int dcdBitMask = 0x80;
            for (i = 0; i < 8; i++) {
                xorFlag = crcReg & 0x8000;
                crcReg <<= 1;
                bit = ((u8Data & dcdBitMask) == dcdBitMask) ? 0x01 : 0x00;
                crcReg |= bit;
                if (xorFlag != 0) {
                    crcReg = crcReg ^ poly;
                }
                dcdBitMask >>= 1;
            }
            return (short) (crcReg & 0xff);
        }

        /**
         * 计算校验码主方法
         */
        public static byte[] CalcCRC(byte[] msgbuf, int msglen) {
            int calcCrc = MSG_CRC_INIT;
            int i;
            for (i = 1; i < msglen; ++i)
                calcCrc = CRC_calcCrc8(calcCrc, MSG_CCITT_CRC_POLY, msgbuf[i]);

            byte[] buf = new byte[2];

            for (i = 1; i >= 0; i--) {
                buf[i] = (byte) (calcCrc % 256);
                calcCrc >>= 8;
            }
            return buf;
        }
    }
}
