package cn.rivamed.device.ClientHandler.eth002Handler;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Date;

import cn.rivamed.FunctionCode;
import cn.rivamed.Utils.Transfer;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.DeviceType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;

import static cn.rivamed.device.ClientHandler.eth002Handler.Eth002V2Handler.DataProtocol.BEGIN_FLAG;


/***
 * ETH002 设备操作接口
 * V2 系列是针对2.0版本的耗材柜，eht002模块，上未接灯，而且锁方面自带逻辑
 *
 */
public class Eth002V2Handler extends NettyDeviceClientHandler implements Eth002ClientHandler {


    private static final String LOG_TAG = "DEV_ETH002_C";
    /***
     * 指纹是否处于注册模式
     */
    boolean fingerRegisterModel = false;

    boolean waitFingerReg = false;

    //记录最后一次指纹返回的数据，超过30秒，则表示需要重新发送指纹采集
    //超过5分中，则应该发送指纹错误消息
    Date lastFingerData = new Date();


    boolean doorOpened = true;
    //存储指纹数据
    ByteBuffer fingerData = ByteBuffer.allocate(1024);


    //指纹注册监控线程；防止指纹注册出现超时等
    int fingerRegisterTimer = 0;
    Thread fingerRegStateThread = new Thread(() -> {
        fingerRegisterTimer = 0;
        Log.i(LOG_TAG, "指纹注册监控线程启动");
        while (fingerRegisterTimer < 40) {  //指纹注册最长等待时间为30秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            fingerRegisterTimer++;
        }
        if (fingerRegisterModel)
            fingerRegisterModel = false;
    });


    @Override
    public DeviceType getDeviceType() {
        return DeviceType.Eth002;
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
        return "Eth";
    }

    @Override
    public String getVersion() {
        return "V2.0";
    }

    int continueIdleCount = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        continueIdleCount = 0;
        ByteBuf in = (ByteBuf) msg;
        if (in.getByte(0) != BEGIN_FLAG) {
            in.readByte();
            return;
        }

        if (in.readableBytes() <= 4) {
            return;
        }


        int bufLen = in.getByte(1) * 256 + in.getByte(2);
        int needBufLen = bufLen + 3;

        if ((bufLen + 3) < in.readableBytes()) {
            return;
        }

        byte[] buf = new byte[needBufLen];

        in.readBytes(buf);
        String s = Transfer.Byte2String(buf);
        Log.d(LOG_TAG, "接收到客户端" + getIdentification() + "发送的消息,总长度为" + needBufLen + "消息内容为" + s);
        if (buf[0] != BEGIN_FLAG || (buf[buf.length - 1] & 0xff) != DataProtocol.CheckSum(buf, 0, buf.length - 1)) {
            Log.w(LOG_TAG, "对客户端" + getIdentification() + "的消息校验不通过，消息体=" + s + "\r 将强制断开设备");
            Close();
            return;
        }

        switch (buf[3]) {  //对应  tar / src  位，协议中第4位 （头和len（2位）之后第一位
            case 0x00: //来自模块本身
                ProcessModelMessage(buf);
                break;
            case 0x01://IO
                ProcessIOData(buf);
                break;
            case 0x02://串口1
                ProcessIdCard(buf);
                break;
            case 0x03: //串口2
                ProcessFingerData(buf);
                break;
        }

        //计算时间差
        {
            long intenval = ((new Date()).getTime() - lastFingerData.getTime()) / 1000;

            if (intenval > 5 * 60) {
                if (messageListener != null) {
                    messageListener.FingerDeviceError(true);
                }
                fingerData.clear();
                if (waitFingerReg) {
                    SendFingerRegister();
                } else {
                    SendFingerGetImage();
                }
            }
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
                    SendFingerGetImage();
                    break;
            }

        } else
            super.userEventTriggered(ctx, evt);

    }

    private void ProcessModelMessage(byte[] buffer) {
        switch (buffer[4] & 0xff) {
            case 0x67://心跳
                //log.trace("判断消息为心跳消息 ");
                ResponseHearBear(buffer);
                //    SendUnLock();
                break;
            case 0x68://读取模块id

                Log.d(LOG_TAG, "判断消息为读取ID消息");
                ProcessDeviceId(buffer);
                break;
            case 0x69://修改模块ID返回接口
                Log.d(LOG_TAG, "判断消息为修改ID确认消息");
                break;
        }
    }


    private void ResponseHearBear(byte[] buffer) {
        if (buffer.length > 0 && buffer[4] == 0x67 && buffer[5] == 0x05) {
            Log.w(LOG_TAG, "心跳计算错误");
            return;
        }
        byte[] buf = new byte[]{0x00, 0x67, 0x01};
        SendBuf(buf);
    }

    private void ProcessDeviceId(byte[] buf) {
        String id = "";
        if (buf != null) {
            if (buf.length == 14 && buf[4] == 0x68) {
                byte[] bufId = new byte[8];
                System.arraycopy(buf, 5, bufId, 0, 8);
                id = Transfer.Byte2String(bufId);
            }
        }
        if (id.length() == 16) {
            super.setIdentification(id);
            if (this.messageListener != null) {
                this.messageListener.OnConnected();
            }
            SendFingerGetImage();
        } else {
            SendGetId();
        }
    }

    private void ProcessIOData(byte[] buf) {
        switch (buf[4] & 0xff) {
            case 0x70://开锁
                Log.d(LOG_TAG, "判断消息为开锁结果返回");
                ProcessOpenDoor(buf);
                break;
            case 0x71://关锁
                Log.d(LOG_TAG, "判断消息为关锁");
                ProcessDoorClosed(buf);
                break;
            case 0x72://查看锁状态
                Log.d(LOG_TAG, "判断消息为检查锁状态 opened=" + (buf[5] == 0x01));
                ProcessDoorState(buf);
                break;
        }
    }

    private void ProcessOpenDoor(byte[] buf) {
        //B00004017000DB
        if (buf != null) {
            doorOpened = buf[5] == 0x01;
            if (buf.length == 7 && buf[4] == 0x70 && buf[5] == 0x01) {
                if (messageListener != null) {
                    messageListener.DoorOpenRet(true);
                }
            }
        }
        SendCheckLockState();
    }

    private void ProcessDoorClosed(byte[] buf) {
        //B00004017101D9
        if (buf != null) {
            doorOpened = buf[5] == 0x01;
            if (buf.length == 7 && buf[4] == 0x71) {
                if (messageListener != null) {
                    messageListener.DoorClosed(buf[5] == 0x01);
                }
            }
        }
        SendCheckLockState();
    }

    private void ProcessDoorState(byte[] buf) {
        if (buf != null) {
            if (buf.length == 7 && buf[4] == 0x72) {
                doorOpened = buf[5] == 0x01;
                if (messageListener != null) {
                    messageListener.DoorState(buf[5] == 0x01);
                }
            }
        }
    }

    /**
     * 串口1 连接HF读卡器
     */
    private void ProcessIdCard(byte[] buf) {
        if (buf != null) {
            if (buf.length == 14) {
                byte[] bufId = new byte[8];
                System.arraycopy(buf, 5, bufId, 0, 8);
                String id = Transfer.Byte2StringAscII(bufId);
                if (this.messageListener != null) {
                    this.messageListener.ReciveIDCard(id);
                }
            }
        }
    }

    /**
     * 串口2 解析指纹仪数据
     */
    private void ProcessFingerData(byte[] buf) {

        boolean completed = false;
        boolean error = false;

        //计算校验码
        int retCheck = DataProtocol.CheckSum(buf, 0, buf.length - 1);
        if (retCheck != (buf[buf.length - 1] & 0xff)) {
            Log.e(LOG_TAG, "客户端" + this.getIdentification() + "接收指纹消息错误，校验码未通过；消息=" + Transfer.Byte2String(buf));
            error = true;
        }
        //判断指令
        if (buf[4] != 0x6b) {
            Log.e(LOG_TAG, "客户端" + this.getIdentification() + "接收指纹消息指令错误，需要指令为 0x6b,实际指令为" + buf[4] + " 消息=" + Transfer.Byte2String(buf));
            error = true;
        }
        lastFingerData = new Date();  //重置时间
        if (!error) {
            /**
             * evt002 协议；按照协议进行拆包
             *
             * head  |  len   | dst/src |cmd    |  data  | check
             * 1byte |  2byte | 1byte   | 1byte | N byte | 1byte
             *
             * len = dst ~ check  包含 data,check
             * */
            int len = buf[1] * 256 + buf[2];
            byte[] fingerOrg = new byte[len - 3];
            System.arraycopy(buf, 5, fingerOrg, 0, len - 3);
            Log.d(LOG_TAG, "对指纹数据进行etv002拆包得到原始指纹数据: len=" + fingerOrg.length + " data=" + Transfer.Byte2String(fingerOrg));

            /**
             * 为保证数据能正常通过终端传输，所有上传、下发数据都拆解为2字节可见字符。
             * 如 0x23 拆分为0x32 0x33,及高4位+0x30 低4位+0x30.
             * 拆包时需要反其道而行之
             *
             * */
            byte[] finger = new byte[fingerOrg.length / 2];
            for (int i = 0, index = 0; i < fingerOrg.length && index < finger.length; i += 2, index++) {  //解析原始数据
                Integer b = ((fingerOrg[i] % 0x10) * 0x10 + (fingerOrg[i + 1] % 0x10));
                finger[index] = (byte) (b & 0xff);
            }
            Log.d(LOG_TAG, "去除指纹数据中 0x3 后的指纹数据为 len=" + finger.length + "data=" + Transfer.Byte2String(finger));

            /**
             *
             *  指纹协议 （返回数据）
             *  头  0xEF01 | 	芯片地址  |	包标识 03/ 07/08  |	包长度（从确认码第1位开始计算）  |	确认码  |	返回参数 |	校验和
             *  2  bytes   |    4bytes	  |   1  byte	      | 2  byte                        |   1  bytes |	N  bytes |	2  bytes
             *
             *  说明：返回包每包数据大小最大为128字节，
             *  超过128字节的数据拆分为多包传输，
             *  包标识：07表示返回结果，包标识 03代表还有后续包传输，包标识 08表示数据传输结束。
             *  确认码：	00h：表示指令执行完毕或 OK 	01h：表示数据包接收错误
             *  校验位：  命令 从包标识到参数最后1个字节计算   返回 从包标识到返回参数最后1个字节计算
             *  当包标识为 02 或 08 时，没有确认码
             * */

            //头部必须为 0xef 0x01
            if (((0xff & finger[0]) != 0xef) || (0xff & finger[1]) != 0x01) {
                Log.e(LOG_TAG, "指纹原始数据格式错误，数据为" + Transfer.Byte2String(finger));
                error = true;
            }

            //计算校验码
            int postion = 0;   //每个数据包的开始

            while (!error && postion < finger.length) {
                len = (0xff & finger[postion + 7]) * 256 + (0xff & finger[postion + 8]);   //从len后开始计算，
                byte[] check = DataProtocol.FingerCheckSum(finger, postion + 6, len - 2 + 3);
                if (finger[postion + 9 + len - 2] != check[0] || finger[postion + 9 + len - 1] != check[1]) {  //确认码前有9个字节未计入len
                    //校验未通过
                    Log.e(LOG_TAG, "指纹校验码计算失败，Postion=" + postion + ",data=" + Transfer.Byte2String(finger));
                    completed = true;
                    error = true;
                }

                if (finger[postion + 6] == 0x07)  //判断包标识  执行结果   0x07 表示执行结果，目前的指令中，07之后都还有数据
                {
                    if ((0xff & finger[postion + 9]) != 0x00 && len == 3) {
                        Log.d(LOG_TAG, "指纹执行结果:采集失败  errorCode=" + finger[postion + 9]);
                        error = true;
                    } else if (len > 3) {  //有实际数据
                        fingerData.put(finger, postion + 10, len - 3);
                    }
                } else if (finger[postion + 6] == 0x08) {
                    //结束
                    fingerData.put(finger, postion + 9, len - 2);
                    completed = true;
                } else {
                    fingerData.put(finger, postion + 9, len - 2);
                }
                postion = postion + 9 + len;
            }
            if (!error && completed) {   //无错误，并读取完成
                fingerData.flip();
                byte[] fingerSData = new byte[fingerData.limit()];
                fingerData.get(fingerSData);
                Log.d(LOG_TAG, "设备" + getIdentification() + "指纹采集净数据 LEN=" + fingerSData.length + " DATA=" + Transfer.Byte2String(fingerSData));
                String fingStr = Transfer.byte2Base64StringFun(fingerSData);
                if (fingerRegisterModel) {
                    fingerRegisterModel = false;
                    Log.d(LOG_TAG, "设备" + getIdentification() + "指纹注册成功 ,指纹数据为" + fingStr);
                    if (this.messageListener != null) {
                        //todo  fingerRegister userid
                        this.messageListener.FingerRegisterRet(true, fingStr, null);
                    }
                } else {
                    Log.d(LOG_TAG, "设备" + getIdentification() + " 指纹采集成功{}" + fingStr);
                    if (this.messageListener != null) {
                        this.messageListener.FingerGetImage(fingStr);
                    }
                }

            }
        }
        if (error || completed) {
            if (fingerRegisterModel) {    //如果是注册模式，注册失败，则发送MQ消息。
                fingerRegisterModel = false;
                if (error) {
                    if (this.messageListener != null) {
                        this.messageListener.FingerRegisterRet(false, null, null);
                    }
                }
            }
            fingerData.clear();
            if (waitFingerReg) {
                SendFingerRegister();
            } else {
                SendFingerGetImage();
            }
        }
    }

    private synchronized boolean SendBuf(byte[] buf) {
        try {
            if (getCtx() != null) {
                byte[] bufSend = DataProtocol.PieceCommond(buf);
                ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, bufSend.length, bufSend.length);
                byteBuf.writeBytes(bufSend);
                ChannelFuture cf = getCtx().writeAndFlush(byteBuf);
                Log.d(LOG_TAG, "向客户端" + getIdentification() + "发送命令 " + Transfer.Byte2String(bufSend));
            }
        } catch (Throwable ex) {
            return false;
        }
        return true;
    }

    Object fingerLocker = new Object();

    private boolean SendFingerRegister() {
        byte[] buf = new byte[]{0x03, 0x6d, 0x3e, 0x3f, 0x30, 0x31, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x30, 0x31, 0x30, 0x30, 0x30, 0x33, 0x32, 0x31, 0x30, 0x30, 0x32, 0x35};

        fingerRegisterModel = true;
        waitFingerReg = false;
        if (this.messageListener != null) {
            this.messageListener.FingerRegisgerCmdExcuted();
        }
        if (fingerRegStateThread.isAlive()) {
            fingerRegisterTimer = 0;
        } else {
            fingerRegStateThread.start();
        }

        SendBuf(buf);
        Log.d(LOG_TAG, "设备" + getIdentification() + "发送指纹注册消息 userid=");
        return true;
    }

    private boolean SendFingerGetImage() {
        byte[] buf = new byte[]{0x03, 0x6d, 0x3e, 0x3f, 0x30, 0x31, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x30, 0x31, 0x30, 0x30, 0x30, 0x33, 0x32, 0x32, 0x30, 0x30, 0x32, 0x36};
        SendBuf(buf);
        Log.d(LOG_TAG, "设备" + getIdentification() + "发送指纹采集消息");
        return true;
    }

    /***
     * 发送开门指令，该指令存疑，是检测门锁状态，还是检测继电器状态
     */
    private boolean SendUnLock() {
        //todo 该指令存疑，是检测门锁状态，还是检测继电器状态
        byte[] buf = new byte[]{0x01, 0x70};
        SendBuf(buf);
        return true;
    }

    /***
     * 发送检测门锁状态  IO 1
     */
    private void SendCheckLockState() {
        byte[] buf = new byte[]{0x01, 0x72};
        SendBuf(buf);
    }

    private void SendGetId() {
        byte[] buf = new byte[]{0x00, 0x68};
        SendBuf(buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.d(LOG_TAG, "channelRegistered 事件发生");
        super.channelRegistered(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Log.d(LOG_TAG, "channelActive 事件发生");
        try {
            super.channelActive(ctx);
        } catch (Exception e) {

        }
        this.setCtx(ctx);
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SendGetId();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
            SendCheckLockState();
        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.d(LOG_TAG, "channelInactive 事件发生");
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
        Close();
    }

    @Override
    public int Close() {
        int ret = super.Close();
        if (this.messageListener != null) {
            this.messageListener.OnDisconnected();
        }
        return ret;
    }

    @Override
    public int OpenDoor() {
        try {
            SendUnLock();
            return FunctionCode.SUCCESS;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "发送关门消息发生错误,message=", ex);
            return FunctionCode.OPERATION_FAIL;
        }
    }

    @Override
    public int FingerReg() {
        if (fingerRegisterModel || waitFingerReg) {
            return FunctionCode.DEVICE_BUSY;
        }
        try {
            waitFingerReg=true;
            return FunctionCode.SUCCESS;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "发送关门消息发生错误,message=", ex);
            return FunctionCode.OPERATION_FAIL;
        }
    }

    @Override
    public int CheckLockState() {
        try {
            SendCheckLockState();
            return FunctionCode.SUCCESS;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "发送关门消息发生错误,message=", ex);
            return FunctionCode.OPERATION_FAIL;
        }
    }

    @Override
    public int OpenLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int CloseLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }


    /***
     * 回调函数与注册
     *
     *
     */
    protected Eth002Message messageListener;

    public void RegisterMessageListener(Eth002Message messageListener) {
        this.messageListener = messageListener;
        this.messageListener.setDeviceHandler(this);
    }


    /***
     * 数据协议
     */
    static class DataProtocol {
        public static final byte BEGIN_FLAG = (byte) 0xB0;

        //设备自身通道
        public static final byte CHAN_DEVICESELF = (byte) 0x00;
        //IO 模块通道,Mensuo通道
        public static final byte CHAN_IO = (byte) 0x01;
        //串口1通道，高频读卡器
        public static final byte CHAN_SER1_HFREADER = (byte) 0x02;
        //串口2通道，指纹仪
        public static final byte CHAN_SER2_FINGER = (byte) 0x03;
        //成功状态
        public static final byte SUCCESS_CODE = (byte) 0x01;
        //锁已经开启
        public static final byte LOCK_ALREADY_OPEN = (byte) 0x02;
        //锁状态: 关闭
        public static final byte LOCK_CLOSED = (byte) 0x00;
        //锁状态: 开启
        public static final byte LOCK_OPENED = (byte) 0x01;
        //错误码表
        public static final byte ERROR_SUCCESS = 0X10;

        public static byte[] PieceCommond(byte[] buf) {
            byte[] picecBuf = new byte[buf.length + 4]; //前三 后1
            int len1 = (buf.length + 1) / 256;
            int len2 = (buf.length + 1) % 256;
            picecBuf[0] = BEGIN_FLAG;
            picecBuf[1] = (byte) (len1 & 0xff);
            picecBuf[2] = (byte) (len2 & 0xff);
            System.arraycopy(buf, 0, picecBuf, 3, buf.length);
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

        public static byte[] FingerCheckSum(byte[] buf, int offset, int len) {
            int uSum = 0;
            for (int i = 0; i < len; i++) {
                int v = buf[i + offset];
                uSum += 0xff & v;
            }
            byte[] ret = new byte[]{0x00, 0x00};
            ret[0] = (byte) ((uSum / 256) & 0xff);
            ret[1] = (byte) ((uSum % 256) & 0xff);
            return ret;
        }
    }
}
