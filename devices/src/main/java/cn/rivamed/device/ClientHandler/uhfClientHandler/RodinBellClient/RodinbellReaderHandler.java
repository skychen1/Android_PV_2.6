package cn.rivamed.device.ClientHandler.uhfClientHandler.RodinBellClient;

import android.os.Handler;
import android.os.HandlerThread;
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
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-05-17 10:23
 * @Modyfied By :
 */

public class RodinbellReaderHandler extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {

    private static final String LOG_TAG = "DEV_RDBL_C";

    /**
     * currentAntIndex 当前工作的天线序号
     * scanTime 持续扫描多久没有新标签了就结束扫描（默认2秒）
     * scanModel 是否在扫描状态（标识，防止重复操作）
     * LOCKER 同步锁
     * continueIdleCount 当前连续发送多少次数据没有接受到返回（心跳机制，超过5次就认为断链）
     * m_btAryBuffer 上一条数据包解析后剩下的数据 m_nLength 剩下的数据长度
     * HEAD 标识头
     * scheduled 线程池处理延时操作  lastTipTime 收到上一个新标签的时间
     */
    private byte currentAntIndex = 0;
    private int scanTime = 2000;

    private volatile boolean scanModel = false;
    private static final Object LOCKER = new Object();
    private int continueIdleCount = 0;
    private byte[] m_btAryBuffer = new byte[4096];
    private int m_nLength = 0;
    private static final byte HEAD = (byte) 0xA0;
    private long lastTipTime;
    /**
     * 用于记录已扫描到的标签信息
     * <p>
     * 当 扫描完成并触发事件后，进行清空；
     * <p>
     * 开始扫描时，即repeatIndex==0 && currentAntIndex ==0 时，进行清空
     */
    private Map<String, List<TagInfo>> epcList = new HashMap<>();
    private Handler mHandler;

    /**
     * 构造函数，舒适化handler 用来执行定时任务
     */
    public RodinbellReaderHandler() {
        HandlerThread mHandlerThread = new HandlerThread("delay_ant_thread::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        super.channelActive(ctx);
        //当有设备连接上的时候就发送获取设备id的指令
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            //有设备连接了就发送获取设备id的指令
            sendGetDeviceId();
        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.e(LOG_TAG, "设备断开时间发生了" + getIdentification());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.d(LOG_TAG, "channelRegistered 事件发生");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Log.d(LOG_TAG, "channelUnregistered 事件发生");
        super.channelUnregistered(ctx);
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
                    sendGetWorkAnt();
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception   {
        //只要收到包，就认为设备连接正常的，重置发包次数
        continueIdleCount = 0;
        //收到的所有数据都为ByteBuf类型，转换数据
        ByteBuf in = (ByteBuf) msg;
        byte[] buf = new byte[in.readableBytes()];
        in.readBytes(buf);
        //转换数据为byte【】
        Log.d(LOG_TAG, "接收到消息" + Transfer.Byte2String(buf));
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
            Log.d(LOG_TAG, "需要解析的数据" + Transfer.Byte2String(btAryBuffer));
            int nIndex = 0;
            int nMarkIndex = 0;
            for (int nLoop = 0; nLoop < btAryBuffer.length; nLoop++) {
                if (btAryBuffer.length > nLoop + 1) {
                    if (btAryBuffer[nLoop] == HEAD) {
                        int nLen = btAryBuffer[nLoop + 1] & 0xFF;
                        Log.d(LOG_TAG, "数据长度为" + nLen);
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
                        Log.e(LOG_TAG, "出现标志位异常的情况：：");
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
                Log.e(LOG_TAG, "解析完一个包剩余的数据：：" + Transfer.Byte2String(m_btAryBuffer));
            } else {
                m_nLength = 0;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void processData(byte[] buf) {
        //协议最小数据是6，如果数据小于6就直接退出，防止错误数据崩溃
        //如果数据不合法，直接丢掉数据包,判断上一条协议如果是扫描，就要重置设备
        if (buf.length < 6) {
            Log.e(LOG_TAG, "数据出错，数据长度小于6：：" + Transfer.Byte2String(buf));
            return;
        }
        //检测数据的合法性
        int check = DataProtocol.checkSum(buf, 0, buf.length - 1);
        //如果数据不合法，直接丢掉数据包,判断上一条协议如果是扫描，就要重置设备
        if ((check & 0xff) != (0xff & buf[buf.length - 1])) {
            Log.e(LOG_TAG, "效验码验证未通过  数据为" + Transfer.Byte2String(buf) + "计算校验码为" + check);
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

    /**
     * 解析获取设备标识
     *
     * @param buf
     * @return
     */
    private boolean processGetDeviceId(byte[] buf) {
        //如果获取失败，重新发送获取指令
        if (buf.length != 17) {
            sendGetDeviceId();
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
        if (this.messageListener != null) {
            messageListener.OnUhfSetPowerRet(this.getIdentification(), buf[4] == DataProtocol.CMD_SUCCESS);
        }
        return true;
    }

    private boolean processGetPower(byte[] buf) {
        //解析数据
        int allPower;
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
        if (this.messageListener != null) {
            messageListener.OnUhfQueryPowerRet(this.getIdentification(), true, allPower);
            return true;
        }
        return false;
    }

    private boolean processSetWorkAnt(byte[] buf) {
        //收到设置天线包就取消延时发送设置天线包
        cancelSendNextWorkAntDelay();
        //拿到设置成功或者失败
        boolean success = buf[4] == DataProtocol.CMD_SUCCESS;
        Log.d(LOG_TAG, "设备" + getIdentification() + " 设置天线" + DataProtocol.ANTS[currentAntIndex] + (success ? "成功" : "失败"));
        //是否需要发送设置下一根天线命令
        boolean setNextWorkAnt;
        //如果成功
        if (success) {
            //设置天线成功就发送盘存指令
            sendRtInventory();
            sendNextWorkAntDelay();
            setNextWorkAnt = false;
        } else {
            //如果失败就设置天线
            setNextWorkAnt = true;
        }
        //如果需要设置下一根天线（并且要在scanModel状态下），就继续设置天线
        if (scanModel && setNextWorkAnt) {
            //发送设置一下跟天线的结果
            currentAntIndex++;
            sendNextWorkAnt();
        }
        return true;
    }


    private boolean processRealTimeInventory(byte[] buf) {
        //收到扫描数据结果包取消开启下一个天线的延时操作；
        cancelSendNextWorkAntDelay();
        //如果没在扫描中就丢掉包
        if (!scanModel) {
            return false;
        }
        //读取扫描数据出错的话直接跳下一根天线继续读
        if (buf.length == 6) {
            Log.e("解析数据出错", currentAntIndex + "继续扫描下一根天线");
            //继续扫描，设置天线
            currentAntIndex++;
            sendNextWorkAnt();
            return false;
            //如果是一根天先扫描完成的结束包（一根正确的天线扫描结束）
        } else if (buf.length == 12 && buf[4] >= 0x00 && buf[4] <= 0x07) {
            if (System.currentTimeMillis() - lastTipTime > scanTime) {
                Log.e("对比是否有新标签了", "没有新标签了，返回最后结果");
                //超过设置时间间隔就发送扫描结果发送扫描结果，
                processScanCompleted();
                return true;
            } else {
                //如果时间不够，继续扫描
                currentAntIndex++;
                sendNextWorkAnt();
                return false;
            }
        } else {
            //返回的数据正确，就解析数据
            processEpcData(buf);
            return false;
        }
    }

    /**
     * 解析读取的标签数据
     *
     * @param buf
     */
    private void processEpcData(byte[] buf) {
        //延时开启下一根天线运行，防止丢包引起流程中断
        sendNextWorkAntDelay();
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
            if ((buf[position + len - 1] & 0xff) != DataProtocol.checkSum(buf, position, len - 1)) {
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
            //转变成真实的RSSI,参见开发文档第五节
            byte brssi = buf[position + 5 + lenEpc];
            int rssi = 0 - (129 - (brssi & 0xff));
            TagInfo lbinfo = new TagInfo(rssi, ant, pc);
            position += len;
            if (epcList.containsKey(epc)) {
                epcList.get(epc).add(lbinfo);
            } else {
                lastTipTime = System.currentTimeMillis();
                epcList.put(epc, new ArrayList<>());
                epcList.get(epc).add(lbinfo);
            }
        }
    }

    private boolean processScanCompleted() {
        synchronized (LOCKER) {
            if (this.messageListener != null) {
                messageListener.OnUhfScanRet(true, this.getIdentification(), "", epcList);
            }
            scanModel = false;
            currentAntIndex = 0;
        }
        return true;
    }

    /**
     * 没有收到扫描最终结果包，导致流程中断
     * 使用延时操作发送操作下一根天线，让流程继续运行
     */
    private void sendNextWorkAntDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("延时重置天线了", "延时重置天线了");
                currentAntIndex++;
                sendNextWorkAnt();
            }
        }, 1000);
    }

    /**
     * 取消延时发送设置天线协议
     */
    private void cancelSendNextWorkAntDelay() {
        mHandler.removeCallbacksAndMessages(null);
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

    /**
     * 发送指令
     *
     * @param cmd
     * @param buf
     * @return
     */
    private boolean sendPacket(byte cmd, byte[] buf) {
        if (getCtx() != null) {
            try {
                //按照协议拼装数据，（地址为默认值，指令，和）
                byte[] bufSend = DataProtocol.pieceCommond(DataProtocol.ADDRESS, cmd, buf);
                //按照netty的数据方式组装数据
                ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, bufSend.length, bufSend.length);
                //复制数据
                byteBuf.writeBytes(bufSend);
                //真正的发送数据
                getCtx().write(byteBuf);
                //flush缓冲区
                getCtx().flush();
                return true;
            } catch (Exception e) {
                //nothing
            }
            return false;
        }
        return false;
    }

    /**
     * 发送获取设备id的方法
     */
    private void sendGetDeviceId() {
        sendPacket(DataProtocol.CMD_GETDEVICEID, null);
    }

    private boolean sendNextWorkAnt() {
        //加同步锁
        synchronized (LOCKER) {
            //如果正在扫描，直接返货错误
            if (!scanModel) {
                return false;
            }
            //超过天线的最大值就设为第一根天线
            if ((currentAntIndex > DataProtocol.ANTS.length - 1)) {
                currentAntIndex = 0;
            }
            //设置天线的时候，开启一个延时设置天线，防止设置天线收不到回复包导致扫描流程中断。并且标志位无法重置；
            cancelSendNextWorkAntDelay();
            sendNextWorkAntDelay();
            return sendPacket(DataProtocol.CMD_SET_WORK_ANT, new byte[]{DataProtocol.ANTS[currentAntIndex]});
        }
    }


    private boolean sendGetWorkAnt() {
        byte[] data = new byte[]{};
        return sendPacket(DataProtocol.CMD_GET_WORK_ANT, data);
    }

    /**
     * 仅发送盘点命令，不做他用。开始盘点请参照 startInventory
     */
    private boolean sendRtInventory() {
        //准备0x8B盘存指令
        byte[] data = new byte[]{DataProtocol.RTINVENTORY_SESSION, DataProtocol.RTINVENTORY_TARGET, DataProtocol.RTINVENTORY_REPEAT};
        //将盘存的全部指令发送出去
        return sendPacket(DataProtocol.CMD_REALTIME_INVENTORY, data);
    }

    private boolean sendSetPower(byte power) {
        byte[] data = new byte[]{power};
        return sendPacket(DataProtocol.CMD_SET_OUTPUTPOWER, data);
    }

    /**
     * 正式的盘点开始，调用该方法
     */
    private boolean startInventory() {
        if (scanModel) {
            return false;
        }
        scanModel = true;
        epcList.clear();
        currentAntIndex = 0;
        return sendNextWorkAnt();
    }

    /**
     * 获取功率
     */
    private boolean sendReset() {
        boolean ret = sendPacket(DataProtocol.CMD_RESET, null);
        Close();
        return ret;
    }

    /**
     * 获取功率
     */
    private boolean sendGetPower() {
        byte[] data = new byte[]{};
        return sendPacket(DataProtocol.CMD_GET_OUTPUTPOWER, data);
    }


    @Override
    public int StartScan() {
        return StartScan(2000);
    }

    @Override
    public int StartScan(int timeout) {
        if (scanModel) {
            return FunctionCode.DEVICE_BUSY;
        }
        if (timeout <= 0) {
            timeout = 2000;
        }
        //如果是点击开始扫描的话就设置上次收到新标签的时间为当前时间
        lastTipTime = System.currentTimeMillis();
        this.scanTime = timeout;
        return startInventory() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int StopScan() {
        scanModel = false;
        return 0;
    }

    @Override
    public int SetPower(byte power) {
        return sendSetPower(power) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int QueryPower() {
        return sendGetPower() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public List<Integer> getUhfAnts() {
        return null;
    }

    @Override
    public int Reset() {
        return sendReset() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
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

    /**
     * 以下为监听器部分
     */

    private UhfClientMessage messageListener;

    public void registerMessageListener(UhfClientMessage messageListener) {
        this.messageListener = messageListener;
        this.messageListener.setDeviceHandler(RodinbellReaderHandler.this);
    }


}
