package cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient;


import android.util.Log;

import java.util.List;

import cn.rivamed.Utils.Transfer;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfHandler;
import cn.rivamed.device.DeviceType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ColuNettyClientHandle extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {

    private static final String LOG_TAG = "DEV_COLU_NC";

    private long queryConnIndex = 0l;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        ByteBuf in = (ByteBuf) msg;
        byte[] buf = new byte[in.readableBytes()];
        in.readBytes(buf);
        Log.d(LOG_TAG, "接收到消息" + Transfer.Byte2String(buf));

        //计算校验码  判断校验码
        byte[] crc=DataProtocol.CalcCRC16(buf,buf.length-2);
        if(crc[0]!=buf[buf.length-2] ||crc[1]!=buf[buf.length-1]){
            Log.w(LOG_TAG,"接收到的信息有误，校验码未通过  origin="+Transfer.Byte2String(buf)+"||ji算的校验码="+Transfer.Byte2String(crc));
            return;
        }


        //判断消息类型

        //分别枚举处理


    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        super.channelActive(ctx);

        //发送获取MAC
        new Thread(()->{
            SendQueryMac();
        }).start();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public DeviceType getDeviceType() {
        return null;
    }

    @Override
    public String getRemoteIP() {
        return null;
    }

    @Override
    public String getProducer() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public int StartScan() {
        return 0;
    }

    @Override
    public int StopScan() {
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
    public List<Integer> getUhfAnts() {
        return null;
    }

    @Override
    public int Reset() {
        return 0;
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



    private boolean SendBuf(byte msgType, byte mid, byte[] data) {
        byte[] buf = new byte[(data == null ? 0 : data.length) + 7];
        buf[0] = DataProtocol.HEAD_BEGIN;
        buf[1] = 0;
        {
            //485 和 上传标识 默认为0；
            buf[1] |= msgType;
        }
        buf[2] = mid;
        byte[] buflen = DataProtocol.ReverseIntToU16Bytes(buf.length);
        System.arraycopy(buflen, 0, buf, 3, buflen.length);
        if (data != null)
            System.arraycopy(data, 0, buf, 5, data.length);
        byte[] crc = DataProtocol.CalcCRC16(buf, buf.length - 2);
        System.arraycopy(crc, 0, buf, buf.length - 2, crc.length);
        if (getCtx() != null) {
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, buf.length, buf.length);
            getCtx().writeAndFlush(byteBuf);
            Log.d(LOG_TAG, "向客户端发送信息:" + Transfer.Byte2String(buf));
            return true;
        }
        return false;
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
         * CRC
         */

        static int[] CRCtable = new int[]{0, 32773, 32783, 10, 32795, 30, 20, 32785, 32819, 54, 60, 32825, 40, 32813, 32807, 34, 32867, 102, 108, 32873, 120, 32893, 32887, 114, 80, 32853, 32863, 90, 32843, 78, 68, 32833, 32963, 198, 204, 32969, 216, 32989, 32983, 210, 240, 33013, 33023, 250, 33003, 238, 228, 32993, 160, 32933, 32943, 170, 32955, 190, 180, 32945, 32915, 150, 156, 32921, 136, 32909, 32903, 130, 33155, 390, 396, 33161, 408, 33181, 33175, 402, 432, 33205, 33215, 442, 33195, 430, 420, 33185, 480, 33253, 33263, 490, 33275, 510, 500, 33265, 33235, 470, 476, 33241, 456, 33229, 33223, 450, 320, 33093, 33103, 330, 33115, 350, 340, 33105, 33139, 374, 380, 33145, 360, 33133, 33127, 354, 33059, 294, 300, 33065, 312, 33085, 33079, 306, 272, 33045, 33055, 282, 33035, 270, 260, 33025, 33539, 774, 780, 33545, 792, 33565, 33559, 786, 816, 33589, 33599, 826, 33579, 814, 804, 33569, 864, 33637, 33647, 874, 33659, 894, 884, 33649, 33619, 854, 860, 33625, 840, 33613, 33607, 834, 960, 33733, 33743, 970, 33755, 990, 980, 33745, 33779, 1014, 1020, 33785, 1000, 33773, 33767, 994, 33699, 934, 940, 33705, 952, 33725, 33719, 946, 912, 33685, 33695, 922, 33675, 910, 900, 33665, 640, 33413, 33423, 650, 33435, 670, 660, 33425, 33459, 694, 700, 33465, 680, 33453, 33447, 674, 33507, 742, 748, 33513, 760, 33533, 33527, 754, 720, 33493, 33503, 730, 33483, 718, 708, 33473, 33347, 582, 588, 33353, 600, 33373, 33367, 594, 624, 33397, 33407, 634, 33387, 622, 612, 33377, 544, 33317, 33327, 554, 33339, 574, 564, 33329, 33299, 534, 540, 33305, 520, 33293, 33287, 514};

        private static int CRC16_CalateByte(byte CheckByte, int LastCRC) {
            int crcIndex = ((LastCRC & '\uff00') >> 8 ^ CheckByte) & 255;
            return (LastCRC & 255) << 8 ^ CRCtable[crcIndex];
        }

        /***
         * 计算 byte数组的 crc校验码
         * @param data  需要计算的数组
         * @param len   需要计算的截至长度
         * @return 计算结果
         */
        public static byte[] CalcCRC16(byte[] data, int len) {
            int crc_result = 0;

            for (int i = 0; i < len; ++i) {
                crc_result = CRC16_CalateByte(data[i], crc_result);
            }

            byte[] rt = ReverseIntToU16Bytes(crc_result);
            return rt;
        }

        public static byte[] ReverseIntToU16Bytes(int i) {
            byte[] rt = new byte[]{(byte) (255 & i), (byte) (('\uff00' & i) >> 8)};
            rt = Reverse(rt);
            return rt;
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
