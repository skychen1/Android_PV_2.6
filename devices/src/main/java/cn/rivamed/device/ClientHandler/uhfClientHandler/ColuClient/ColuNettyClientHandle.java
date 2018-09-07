package cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
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

public class ColuNettyClientHandle extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {

    private static final String LOG_TAG = "DEV_COLU_NC";
    Map<String, List<TagInfo>> epcs = new HashMap<>();
    private int MAX_ANT = 8;
    private int MAX_POWER = 30;

    private long queryConnIndex = 0l;

    boolean scanMode = false;

    int antByte = 0x0;  //用数位标识天线，从低位开始 第一位为1 标识天线 1存在，0 则不存在，以此类推


    /**
     * 持续扫描的时间，默认3秒，实际由 StartScan 方法进行赋值
     */
    int scanTime = 3000;

    long startScanTime = new Date().getTime();

    /**
     * 扫描过程中检测已扫描的时间是否超过预定时间
     */
    private boolean ScanTimeCompleted() {
        return new Date().getTime() - startScanTime > scanTime;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // super.channelRead(ctx, msg);
        try {
            ByteBuf in = (ByteBuf) msg;
            byte[] buf = new byte[in.readableBytes()];
            in.readBytes(buf);
            Log.d(LOG_TAG, "接收到消息" + Transfer.Byte2String(buf));

            //计算校验码  判断校验码
            byte[] crcCheckBuf = new byte[buf.length - 3];
            System.arraycopy(buf, 1, crcCheckBuf, 0, crcCheckBuf.length);
            byte[] crc = DataProtocol.CalcCRC16(crcCheckBuf, crcCheckBuf.length);
            if (crc[0] != buf[buf.length - 2] || crc[1] != buf[buf.length - 1]) {
                Log.w(LOG_TAG, "接收到的信息有误，校验码未通过  origin=" + Transfer.Byte2String(buf) + "||ji算的校验码=" + Transfer.Byte2String(crc));
                return;
            }
            if (buf.length < 7) {
                Log.e(LOG_TAG, "根据数据协议,消息长度最少为7,当前数据长度有误;origindata=" + Transfer.Byte2String(buf));
                return;
            }
            switch (buf[1] & 0xf)  //  8-11位为消息类型
            {
                case DataProtocol.MSG_TYPE_READER_ERROR:
                    break;
                case DataProtocol.MSG_TYPE_READER_OPTION:
                    ProcessReaderOption(buf);
                    break;
                case DataProtocol.MSG_TYPE_RFID_OPERATION:
                    ProcessRfidOperation(buf);
                    break;
                case DataProtocol.MSG_TYPE_READER_LOG:
                    break;
                case DataProtocol.MSG_TYPE_READER_UPDATE:
                    break;
                case DataProtocol.MSG_TYPE_READER_TEST:
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            Log.e(LOG_TAG,e.getMessage());
        }
    }

    private byte[] getData(byte[] buf) {
        if (buf.length < 7) return null;
        int lenPre = buf[3] & 0xff;
        int lenAft = buf[4] & 0xff;
        int len = lenPre * 256 + lenAft;
        if (buf.length != len + 7) {
            Log.e(LOG_TAG, "获取数据部分错误,数据长度校验不正确 len=" + len + ";Origin=" + Transfer.Byte2String(buf));
        }
        if (len > 0) {
            byte[] data = new byte[len];
            System.arraycopy(buf, 5, data, 0, data.length);
            return data;
        } else {
            return new byte[0];
        }
    }

    private void ProcessMsgError(byte[] buf) {

    }

    /**
     * 处理阅读器配置和管理信息
     *
     * @param buf
     */
    private void ProcessReaderOption(byte[] buf) {
        switch (buf[2]) {
            case 0x01:
                break;
            case DataProtocol.MID_READER_OPTION_QUERYCONN:
                ProcessConnQuery(buf);
                break;
            case DataProtocol.MID_READER_OPTION_QUERY_MAC:
                ProcessQueryMac(buf);
                break;
        }
    }

    /**
     * 处理阅读器配置和管理信息  获取MAC
     *
     * @param buf
     */
    private void ProcessQueryMac(byte[] buf) {
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length <= 0) return;
        String mac = Transfer.Byte2String(data);
        setIdentification(mac);
        //通知
        Log.i(LOG_TAG, "获取到MAC地址:" + mac);
        if (this.messageListener != null) {
            this.messageListener.OnConnected();
        }
    }


    /**
     * 处理阅读器配置和管理信息 心跳
     *
     * @param buf
     */
    private void ProcessConnQuery(byte[] buf) {
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length <= 0) {
            Log.d(LOG_TAG, "接收到心跳回复确认消息");
            return;
        } else {
            if (SendConnQuery(data)) {
                Log.d(LOG_TAG, "发送心跳回复成功");
            }
        }
    }

    private void ProcessRfidOperation(byte[] buf) {
        int dst = buf[1] & 0x10;  //第12位，1标识阅读器主动上传，0 标识上位机指令或阅读器响应上位机指令
        switch (buf[2]) {
            case 0x00:// DataProtocol.MID_RFID_UPDATE_TAG,
                if (dst == 0X10) {   //阅读器主动上传  ox00 EPC 数据上传信息
                    ProcessRfidEpc(buf);
                } else {    //==0 阅读器响应
                    ProccessRfidInfo(buf);
                }
                break;
            case DataProtocol.MID_RFID_QUERY_ANT:
                ProcessRfidQueryAnt(buf);
                break;
            case 0x01:  //设定功率  和 读取结束  依赖上传表示判断
                if (dst != 0x10) {
                    ProcessRfidOptionPower(buf);
                } else {
                    ProccessInventoryEnd(buf);
                }
                break;
            case DataProtocol.MID_RFID_QUERY_POWER:
                ProcessRfidQueryPower(buf);
                break;
            case DataProtocol.MID_RFID_INVENTORY_EPC:
                Log.i(LOG_TAG, "读取EPC开始结果:" + Transfer.Byte2String(buf));
                break;
        }
    }

    private void ProccessInventoryEnd(byte[] buf) {
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length != 1) return;
        if (data[0] == 0x00) {
            Log.i(LOG_TAG, "RFID扫描结束");
            scanMode = false;
            if (this.messageListener != null) {   //发送回调
                Log.d(LOG_TAG, "触发扫描完成回调");
                this.messageListener.OnUhfScanRet(true, this.getIdentification(), "", epcs);
                this.messageListener.OnUhfScanComplete(true, this.getIdentification());
            }
        }
    }

    private void ProcessRfidEpc(byte[] buf) {
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length < 6) return;
        String epc = "";
        int rssi = 0;
        String pc = "";
        int antIndex = 0;
        int ret = 0x00;

        boolean getRet = false;
        boolean getRssi = false;
        for (int i = data.length - 1; i >= 0; i--) {
            if (!getRssi && i < 7) return;   //数据长度不够

            if (!getRssi) {
                if ((data[i - 1] != 0x01 && data[i - 1] != 0x02)) {
                    i--;
                    continue;
                } else {
                    if (data[i - 1] == 0x01) {
                        getRssi = true;
                        getRet = true;
                        ret = 0;
                        rssi = data[i];
                        antIndex = data[i - 2];

                        byte[] bpc = new byte[2];
                        System.arraycopy(data, i - 4, bpc, 0, 2);
                        pc = Transfer.Byte2String(bpc).toUpperCase();
                        int epcLen = data.length - 5 - 2;
                        byte[] bEpc = new byte[epcLen];
                        System.arraycopy(data, 2, bEpc, 0, epcLen);
                        epc = Transfer.Byte2String(bEpc);
                        Log.i(LOG_TAG, "获取到EPC信息:EPC=" + epc + ";rssi=" + rssi + ";ant=" + antIndex + ";pc=" + pc);
                        TagInfo tagInfo = new TagInfo();
                        tagInfo.setAnt(antIndex);
                        tagInfo.setPc(pc);
                        tagInfo.setRssi(0 - rssi);
                        AppendNewTag(epc, tagInfo);
                    } else if (data[-1] == 0x02) {
                        getRet = true;
                        ret = data[i];
                        if (ret != 0) {
                            Log.e(LOG_TAG, "接收到错误的EPC读取结果:RET=" + data[i]);
                            getRet = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * 内部方法，用于和 Colu Service 互动；
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
        if(ScanTimeCompleted()){
            StopScan();
            scanMode = false;
            if (this.messageListener != null) {   //发送回调
                Log.d(LOG_TAG, "触发扫描完成回调");
                this.messageListener.OnUhfScanRet(true, this.getIdentification(), "", epcs);
                this.messageListener.OnUhfScanComplete(true, this.getIdentification());
            }
        }
    }

    private void ProccessRfidInfo(byte[] buf) {
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length < 2) return;
        MAX_POWER = data[1];
        MAX_ANT = data[2];
        Log.i(LOG_TAG, "获取RFID信息:最大天线数目：" + MAX_ANT + ";最大支持功率:" + MAX_POWER);
    }

    private void ProcessRfidOptionPower(byte[] buf) {
        // AA020100010092F3
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length != 1) return;
        Log.i(LOG_TAG, "设置功率结果=" + (data[0] == 0x00));
        if (messageListener != null) {
            messageListener.OnUhfSetPowerRet(this.getIdentification(), data[0] == 0);
        }
    }

    private void ProcessRfidQueryPower(byte[] buf) {
        byte[] data = getData(buf);
        if (data == null) return;
        if (data.length <= 0) return;
        if (data.length % 2 != 0) return;
        int power = 100;
        int tmpPower = 100;
        int index = 0;
        for (int i = 0; i < MAX_ANT; i++) {
            int ant = (int) Math.pow(2, data[i * 2]);
            if ((antByte & ant) == ant) {
                tmpPower = data[2 * i + 1] & 0xff;
                if (tmpPower < power) {
                    power = tmpPower;
                }
            }
        }
        if (this.messageListener != null) {
            this.messageListener.OnUhfQueryPowerRet(this.getIdentification(), true, power);
        }
    }

    private void ProcessRfidQueryAnt(byte[] buf) {
        int len = (0xff & buf[3]) * 256 + buf[4] & 0xff;
        if (len == 0) {
            antByte = 0;
        } else if (len == 1) {
            antByte = buf[5] & 0xff;
        } else if (len == 2) {
            antByte = (buf[5] & 0xff) * 256 + (buf[6] & 0xff);
        }

        Log.i(LOG_TAG, "获取到天线数据:" + Integer.toHexString(antByte));
    }

    private void ProcessReadLog(byte[] buf) {

    }

    private void ProcessReadUpdate(byte[] buf) {

    }

    private void ProcessReadTest(byte[] buf) {

    }

    int sendQueryMac = 0;  //累计获取MAC地址，3次未获取到，则主动断开

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
            sendQueryMac = 0;
            while (StringUtil.isNullOrEmpty(getIdentification())) {
                if (sendQueryMac > 5) {
                    Log.e(LOG_TAG, "超时未获取到MAC地址，将强制断开");
                    Close();
                    return;
                }

                SendQueryMac();
                sendQueryMac++;

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {

                }
            }
            SendQueryAnt();
            SendRfidQueryInfo();
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
        return SendStartInventory(null) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int StartScan(int timeout) {
        if (timeout <= 0) timeout = 3000;
        this.scanTime = timeout;
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
        return SendOptionPower(power) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int QueryPower() {
        return SendQueryPower() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
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
        return SendReset() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    private boolean SendStop() {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_RFID_STOP;
        return SendBuf(msgType, mid, null);
    }

    private boolean SendStartInventory(byte[] password) {
        byte msttype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_INVENTORY_EPC;
        byte[] data;
        if (password == null || password.length != 4) {
            data = new byte[2];
            data[0] = (byte) antByte; //天线
            data[1] = 0X01;              //连续扫描 1 单次扫描 0
        } else {
            data = new byte[7];
            data[0] = (byte) antByte;
            data[1] = 0x01;
            data[2] = 0x05; //密码
            System.arraycopy(password, 0, data, 3, password.length);
        }

        return SendBuf(msttype, mid, data);
    }

    private boolean SendReset() {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_RESET;
        return SendBuf(msgType, mid, null);
    }

    private boolean SendQueryPower() {
        byte msgtype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_QUERY_POWER;
        return SendBuf(msgtype, mid, null);
    }

    private boolean SendOptionPower(byte power) {
        // todo 发送设置功率
        byte msgtype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_OPTION_POWER;
        byte[] datatmp = new byte[1024];
        int pos = 0;
        for (byte antIndex = 0; antIndex < MAX_ANT; antIndex++) {
            int antNum = (int) Math.pow(2, antIndex);
            if ((antNum & antByte) == antNum) {
                datatmp[pos++] = (byte) (antIndex + 1);
                datatmp[pos++] = power;
            }
        }
        if (pos <= 0) return false;
        byte[] data = new byte[pos];
        System.arraycopy(datatmp, 0, data, 0, pos);
        return SendBuf(msgtype, mid, data);

    }

    private boolean SendQueryAnt() {
        byte msgtype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_QUERY_ANT;
        return SendBuf(msgtype, mid, null);
    }

    private boolean SendOptionAnt() {
        //todo 发送设置天线
        //        return true;

        return true;
    }

    private boolean SendInventory(int ant) {
        // todo 发送扫描EPC
        //        return true;

        return true;
    }

    private boolean SendRfidStop() {
        //todo  停止读取

        return true;
    }

    private boolean SendRfidQueryInfo() {
        byte mstType = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_QUERY_RFIDINFO;
        return SendBuf(mstType, mid, null);
    }

    private boolean SendConnQuery(byte[] data) {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_QUERYCONN;
        // byte[] data = DataProtocol.ReverseLongToU32Bytes(queryConnIndex);
        return SendBuf(msgType, mid, data);
    }

    public boolean SendConnQuery() {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_QUERYCONN;
        byte[] data = DataProtocol.ReverseLongToU32Bytes(queryConnIndex);
        return SendBuf(msgType, mid, data);
    }


    public boolean SendQueryMac() {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_QUERY_MAC;
        return SendBuf(msgType, mid, null);
    }


    private synchronized boolean SendBuf(byte msgType, byte mid, byte[] data) {
        byte[] buf = new byte[(data == null ? 0 : data.length) + 7];
        buf[0] = DataProtocol.HEAD_BEGIN;
        buf[1] = 0;
        {
            //485 和 上传标识 默认为0；
            buf[1] |= msgType;
            //    buf[1] |= 0x10;
        }
        buf[2] = mid;
        if (data == null) {
            buf[3] = 0x00;
            buf[4] = 0x00;
        } else {
            byte[] buflen = DataProtocol.ReverseIntToU16Bytes(data.length);
            System.arraycopy(buflen, 0, buf, 3, buflen.length);
            System.arraycopy(data, 0, buf, 5, data.length);
        }

        byte[] checkBuf = new byte[buf.length - 3];
        System.arraycopy(buf, 1, checkBuf, 0, checkBuf.length);
        byte[] crc = DataProtocol.CalcCRC16(checkBuf, checkBuf.length);
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
        return "鸿陆";
    }

    @Override
    public String getVersion() {
        return "V1.0_Netty";
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
        public static final byte HEAD_BEGIN = (byte) 0xAA;

        /**
         * 协议控制字 长度2
         */
        public static final byte PROTOCOL_CONTROL_LEN = 2;

        /**
         * 消息类型
         * <p>
         * 协议控制的 第 8-11位
         * <p>
         * 读写器错误或警告信息
         */
        public static final byte MSG_TYPE_READER_ERROR = 0X00;

        /**
         * 消息类型
         * <p>
         * <p>
         * 协议控制的 第 8-11位
         * <p>
         * 读写器配置和管理消息
         */
        public static final byte MSG_TYPE_READER_OPTION = 0X01;

        /**
         * 消息类型
         * <p>
         * 协议控制的 第 8-11位
         * <p>
         * RFID配置与操作信息
         */
        public static final byte MSG_TYPE_RFID_OPERATION = 0X02;

        /**
         * 消息类型
         * <p>
         * 协议控制的 第 8-11位
         * <p>
         * <p>
         * 读写器 日子消息
         */
        public static final byte MSG_TYPE_READER_LOG = 0X03;

        /**
         * 消息类型
         * <p>
         * 协议控制的 第 8-11位
         * <p>
         * 读写器应用处理及基带升级信息
         */
        public static final byte MSG_TYPE_READER_UPDATE = 0X04;


        /**
         * 消息类型
         * <p>
         * 协议控制的 第 8-11位
         * <p>
         * 读写器测试指令
         */
        public static final byte MSG_TYPE_READER_TEST = 0X05;

        /**
         * 读写器配置和管理信息  -- 查询读写器基本信息
         */
        public static final byte MID_READER_OPTION_QUERY_INFO = 0X00;
        /**
         * 读写器配置和管理信息  -- 查询读写器
         */
        public static final byte MID_READER_OPTION_QUERY_BASEBAND = 0X01;
        /**
         * 读写器配置和管理信息  -- 配置串口参数
         */
        public static final byte MID_READER_OPTION_SET_UARTPARAM = 0X02;

        /**
         * 读写器配置和管理信息  --查询串口参数
         */
        public static final byte MID_READER_OPTION_QUERY_UARTPARAM = 0X03;
        /**
         * 读写器配置和管理信息  --查询mac地址
         */
        public static final byte MID_READER_OPTION_QUERY_MAC = 0X06;

        /**
         * 读写器主动上传
         * <p>
         * 触发开始消息
         */
        public static final byte MDI_READER_OPTION_UPDATE_START = 0X00;

        /**
         * 读写器主动上传
         * <p>
         * 触发停止消息
         */
        public static final byte MDI_READER_OPTION_UPDATE_STOP = 0X01;


        /**
         * 读写器配置和管理信息  -- 连接状态确认
         */
        public static final byte MID_READER_OPTION_QUERYCONN = 0X12;
        /**
         * 读写器配置和管理信息  -- 重启
         */
        public static final byte MID_READER_OPTION_RESET = 0X0F;
        /**
         * RFID设置，获取RFID信息
         */
        public static final byte MID_RFID_QUERY_RFIDINFO = 0X00;
        /**
         * RFID设置，配置读写器功率
         */
        public static final byte MID_RFID_OPTION_POWER = 0X01;

        /**
         * RFID设置，查询读写器功率
         */
        public static final byte MID_RFID_QUERY_POWER = 0X02;

        /***
         * rfid 设置，查询读写器天线
         */
        public static final byte MID_RFID_QUERY_ANT = 0X08;
        /**
         * rfid 设置，设置读写器天线
         */
        public static final byte MID_RFID_OPTION_ANT = 0X07;
        /**
         * rfid 设置，读取EPC
         */
        public static final byte MID_RFID_INVENTORY_EPC = 0X10;

        /***
         * RFID设置，停止一切活动
         */
        public static final byte MID_RFID_STOP = (byte) 0XFF;
        /**
         * RFID  阅读器主动上传，标签信息
         */
        public static final byte MID_RFID_UPDATE_TAG = 0X00;


        /**
         * RFID  阅读器主动上传，EPC读卡结束
         */
        public static final byte MID_RFID_UPDATE_READ_END = 0X01;


        /**
         * CRC
         */

        static int CRCtable[] = {
                0x0, 0x8005, 0x800f, 0xa, 0x801b, 0x1e, 0x14, 0x8011, 0x8033, 0x36,
                0x3c, 0x8039, 0x28, 0x802d, 0x8027, 0x22, 0x8063, 0x66, 0x6c, 0x8069,
                0x78, 0x807d, 0x8077, 0x72, 0x50, 0x8055, 0x805f, 0x5a, 0x804b, 0x4e,
                0x44, 0x8041, 0x80c3, 0xc6, 0xcc, 0x80c9, 0xd8, 0x80dd, 0x80d7, 0xd2,
                0xf0, 0x80f5, 0x80ff, 0xfa, 0x80eb, 0xee, 0xe4, 0x80e1, 0xa0, 0x80a5,
                0x80af, 0xaa, 0x80bb, 0xbe, 0xb4, 0x80b1, 0x8093, 0x96, 0x9c, 0x8099,
                0x88, 0x808d, 0x8087, 0x82, 0x8183, 0x186, 0x18c, 0x8189, 0x198, 0x819d,
                0x8197, 0x192, 0x1b0, 0x81b5, 0x81bf, 0x1ba, 0x81ab, 0x1ae, 0x1a4, 0x81a1,
                0x1e0, 0x81e5, 0x81ef, 0x1ea, 0x81fb, 0x1fe, 0x1f4, 0x81f1, 0x81d3, 0x1d6,
                0x1dc, 0x81d9, 0x1c8, 0x81cd, 0x81c7, 0x1c2, 0x140, 0x8145, 0x814f, 0x14a,
                0x815b, 0x15e, 0x154, 0x8151, 0x8173, 0x176, 0x17c, 0x8179, 0x168, 0x816d,
                0x8167, 0x162, 0x8123, 0x126, 0x12c, 0x8129, 0x138, 0x813d, 0x8137, 0x132,
                0x110, 0x8115, 0x811f, 0x11a, 0x810b, 0x10e, 0x104, 0x8101, 0x8303, 0x306,
                0x30c, 0x8309, 0x318, 0x831d, 0x8317, 0x312, 0x330, 0x8335, 0x833f, 0x33a,
                0x832b, 0x32e, 0x324, 0x8321, 0x360, 0x8365, 0x836f, 0x36a, 0x837b, 0x37e,
                0x374, 0x8371, 0x8353, 0x356, 0x35c, 0x8359, 0x348, 0x834d, 0x8347, 0x342,
                0x3c0, 0x83c5, 0x83cf, 0x3ca, 0x83db, 0x3de, 0x3d4, 0x83d1, 0x83f3, 0x3f6,
                0x3fc, 0x83f9, 0x3e8, 0x83ed, 0x83e7, 0x3e2, 0x83a3, 0x3a6, 0x3ac, 0x83a9,
                0x3b8, 0x83bd, 0x83b7, 0x3b2, 0x390, 0x8395, 0x839f, 0x39a, 0x838b, 0x38e,
                0x384, 0x8381, 0x280, 0x8285, 0x828f, 0x28a, 0x829b, 0x29e, 0x294, 0x8291,
                0x82b3, 0x2b6, 0x2bc, 0x82b9, 0x2a8, 0x82ad, 0x82a7, 0x2a2, 0x82e3, 0x2e6,
                0x2ec, 0x82e9, 0x2f8, 0x82fd, 0x82f7, 0x2f2, 0x2d0, 0x82d5, 0x82df, 0x2da,
                0x82cb, 0x2ce, 0x2c4, 0x82c1, 0x8243, 0x246, 0x24c, 0x8249, 0x258, 0x825d,
                0x8257, 0x252, 0x270, 0x8275, 0x827f, 0x27a, 0x826b, 0x26e, 0x264, 0x8261,
                0x220, 0x8225, 0x822f, 0x22a, 0x823b, 0x23e, 0x234, 0x8231, 0x8213, 0x216,
                0x21c, 0x8219, 0x208, 0x820d, 0x8207, 0x202};


        private static int CRC16_CalateByte(byte CheckByte, int LastCRC) {
//            int crcIndex = ((LastCRC & '\uff00') >> 8 ^ CheckByte) & 255;
//            return (LastCRC & 255) << 8 ^ CRCtable[crcIndex];
            int crcIndex = ((LastCRC >> 8) ^ (CheckByte)) & 0xff;
            return (LastCRC << 8) ^ CRCtable[crcIndex];
        }

        /***
         * 计算 byte数组的 crc校验码
         * @param data  需要计算的数组
         * @param len   需要计算的截至长度
         * @return 计算结果
         */
        public static byte[] CalcCRC16(byte[] data, int len) {
            int crc_result = 0;

            for (int i = 0; i < len; i++) {
                crc_result = CRC16_CalateByte(data[i], crc_result);
            }

            byte[] rt = ReverseIntToU16Bytes(crc_result);
            return rt;
        }

        public static byte[] ReverseIntToU16Bytes(int i) {
            i &= 0xffff;
            byte[] ret = new byte[]{0x00, 0x00};
            ret[0] = (byte) ((i / 256) & 0xff);
            ret[1] = (byte) ((i % 256) & 0xff);
            // ret = Reverse(rt);
            return ret;
        }

        private static byte[] Reverse(byte[] b) {
            byte[] temp = new byte[b.length];

            for (int i = 0; i < b.length; ++i) {
                temp[i] = b[b.length - 1 - i];
            }

            return temp;
        }

        public static byte[] ReverseLongToU32Bytes(long i) {
            byte[] rt = new byte[]{(byte) ((int) (255L & i)), (byte) ((int) ((65280L & i) >> 8)), (byte) ((int) ((16711680L & i) >> 16)), (byte) ((int) ((-16777216L & i) >> 24))};
            rt = Reverse(rt);
            return rt;
        }
    }
}
