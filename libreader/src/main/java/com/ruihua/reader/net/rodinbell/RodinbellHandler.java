package com.ruihua.reader.net.rodinbell;

import android.os.Handler;
import android.os.HandlerThread;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.reader.net.bean.EpcInfo;
import com.ruihua.reader.net.callback.ReaderHandler;
import com.ruihua.reader.net.callback.ReaderMessageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * describe ： 罗丹贝尔通道数据处理类（发出，收到数据处理等）
 *
 * @author : Yich
 * date: 2019/2/22
 */
public class RodinbellHandler extends BaseRodinbellHandler implements ReaderHandler {


    /**
     * currentAntIndex 当前天线数（第几根）
     * scanModel 是否在扫描状态（扫描是单任务操作，防止重复操作）
     * locker , 锁，保证数据处理；
     * epcList扫描的标签的结果集合
     * scanTime 扫描时间（持续扫描，多长时间没有新标签了就返回结果）
     * lastTipTime  收到上一个新标签的时间，用来判断持续扫描是否超时？
     * lastCmd  上一条指令（用来判断是什么操作：锁或者灯，开或者关），因为都是操作gpio
     * checkWhich 检测的是什么设备的状态 ， 0 没有操作的状态， 1 操作灯，，2操作操作锁
     */
    private byte currentAntIndex = 0;
    private volatile boolean scanModel = false;
    private final Object locker = new Object();
    private Map<String, List<EpcInfo>> epcList = new HashMap<>();
    private int scanTime = 1500;
    private long lastTipTime;
    private byte[] lastCmd = null;
    private int checkWhich = 0;
    private ReaderMessageListener mListener;
    private Handler mHandler;

    /**
     * 构造函数，初始化handler 用来执行定时任务
     */
    public RodinbellHandler() {
        HandlerThread mHandlerThread = new HandlerThread("delay_ant_thread::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }


    /**
     * 收到一条条标准的协议数据，进行对应的处理
     *
     * @param buf 协议数据
     */
    @Override
    public void processData(byte[] buf) {
        //根据协议，第三个字节流是类型数据，根据不同类型的数据做不同的处理
        switch (buf[3]) {
            case DataProtocol.CMD_RESET:
                //重置没有回复的消息
                break;
            //获取读写器的识别码
            case DataProtocol.CMD_GET_DEVICE_ID:
                processGetDeviceId(buf);
                break;
            //设置读写器射频输出功率
            case DataProtocol.CMD_SET_OUTPUT_POWER:
                processSetPower(buf);
                break;
            //获取读写器射频输出功率
            case DataProtocol.CMD_GET_OUTPUT_POWER:
                processGetPower(buf);
                break;
            //设置读写器工作天线
            case DataProtocol.CMD_SET_WORK_ANT:
                processSetWorkAnt(buf);
                break;
            //获取读写器工作天线
            case DataProtocol.CMD_GET_WORK_ANT:
                //获取工作天线，在该项目中用作心跳包处理，所以不不需要处理
                break;
            //自定义session和target盘存
            case DataProtocol.CMD_REALTIME_INVENTORY:
                processRealTimeInventory(buf);
                break;
            case DataProtocol.CMD_GPIO_READ:
                //读取gpio的情况（查看锁或者灯的开光情况）
                processDoorAndLockState(buf);
                break;
            case DataProtocol.CMD_GPIO_WRITE:
                //写入gpio,控制灯和锁的情况
                processIOData(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 解析获取设备id号结果
     *
     * @param buf 数据
     */
    private void processGetDeviceId(byte[] buf) {
        //如果获取数据出错，重新发送获取指令
        if (buf.length != 17) {
            sendGetDeviceId();
            return;
        }
        //获取成功解析数据，保存并回调
        byte[] idBuf = new byte[12];
        System.arraycopy(buf, 4, idBuf, 0, 12);
        setIdentification(TransferUtils.Byte2String(idBuf));
        //否则提醒用户设备连接成功
        if (mListener != null) {
            mListener.onConnectState(RodinbellHandler.this, getIdentification(), true);
        }
    }

    /**
     * 设置功率
     *
     * @param buf 协议数据
     */
    private void processSetPower(byte[] buf) {
        //回调设置功率结果，成功或者失败
        if (this.mListener != null) {
            mListener.onSetPowerRet(this.getIdentification(), buf[4] == DataProtocol.CMD_SUCCESS);
        }
    }

    /**
     * 获取功率
     *
     * @param buf 协议数据
     */
    private void processGetPower(byte[] buf) {
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
        if (this.mListener != null) {
            mListener.onQueryPowerRet(this.getIdentification(), allPower);
        }
    }

    /**
     * 解析设置天线数据包
     *
     * @param buf 协议数据
     */
    private void processSetWorkAnt(byte[] buf) {
        //收到设置天线包就取消延时发送设置天线包
        cancelSendNextWorkAntDelay();
        //拿到设置成功或者失败
        boolean success = buf[4] == DataProtocol.CMD_SUCCESS;
        //如果成功
        if (success) {
            //设置天线成功就发送盘存指令
            sendRtInventory();
            sendNextWorkAntDelay();
        } else {
            //如果失败并且在扫描状态下就设置下一根天线
            if (scanModel) {
                currentAntIndex++;
                sendNextWorkAnt();
            }
        }
    }

    /**
     * 盘存的结果解析
     *
     * @param buf 协议数据
     */
    private void processRealTimeInventory(byte[] buf) {
        //收到扫描数据结果包取消开启下一个天线的延时操作；
        cancelSendNextWorkAntDelay();
        //如果没在扫描中就丢掉包
        if (!scanModel) {
            return;
        }
        //读取扫描数据出错的话直接跳下一根天线继续读
        if (buf.length == 6) {
//            Log.e("解析数据出错", currentAntIndex + "继续扫描下一根天线");
            //继续扫描，设置天线
            currentAntIndex++;
            sendNextWorkAnt();
            return;
        }
        //如果是一根天先扫描完成的结束包（一根正确的天线扫描结束）
        if (buf.length == 12 && buf[4] >= 0x00 && buf[4] <= 0x07) {
            if (System.currentTimeMillis() - lastTipTime > scanTime) {
//                Log.e("对比是否有新标签了", "没有新标签了，返回最后结果");
                //超过设置时间间隔就发送扫描结果发送扫描结果，
                processScanCompleted();
                //设置扫描表示为（没有扫描）
                scanModel = false;
                currentAntIndex = 0;
            } else {
                //如果时间不够，继续扫描
                currentAntIndex++;
                sendNextWorkAnt();
            }
        } else {
            //返回的数据正确，就解析数据
            processEpcData(buf);
        }
    }

    /**
     * 解析读取的标签数据
     *
     * @param buf 标签数据
     */
    private void processEpcData(byte[] buf) {
        //延时开启下一根天线运行，防止丢包引起流程中断
        sendNextWorkAntDelay();
        //因为前面数据已经处理过(每一条数据就是一条标准的协议标签数据，所以不需要再验证标签数据)，
        byte ant = (byte) ((buf[4] & 0x03) & 0xff);
        byte[] bPc = new byte[2];
        //解析pc值
        System.arraycopy(buf, 5, bPc, 0, 2);
        String pc = TransferUtils.Byte2String(bPc);
        int lenEpc = buf.length - 9;
        byte[] bEpc = new byte[lenEpc];
        System.arraycopy(buf, 7, bEpc, 0, lenEpc);
        String epc = TransferUtils.Byte2String(bEpc);
        //转变成真实的RSSI,参见开发文档第五节
        byte brssi = buf[5 + lenEpc];
        int rssi = 0 - (129 - (brssi & 0xff));
        EpcInfo info = new EpcInfo(rssi, ant, pc);
        //判断以前是否有这个标签，如果有，就直接添加本次扫描的状态到集合中
        if (epcList.containsKey(epc)) {
            epcList.get(epc).add(info);
        } else {
            //如果是新标签，就重新记录上一个新标签的时间，用于结束使用；
            lastTipTime = System.currentTimeMillis();
            if (mListener != null) {
                mListener.onScanNewEpc(this.getIdentification(), epc, currentAntIndex + 1);
            }
            //创建一个新的集合，保存数据，并存入list中
            ArrayList<EpcInfo> list = new ArrayList<>();
            list.add(info);
            epcList.put(epc, list);
        }
    }

    /**
     * 扫描天线完成，回调扫描结果
     */
    private void processScanCompleted() {
        synchronized (locker) {
            //回调扫描完成结果
            if (this.mListener != null) {
                //扫描结果
                this.mListener.onScanResult(this.getIdentification(), epcList);
            }
        }
        epcList.clear();
    }

    /**
     * 解析查询gpio口的状态（锁和灯的状态）
     *
     * @param buf 协议数据
     */
    private void processDoorAndLockState(byte[] buf) {
        //如果没有监听者就不用处理数据了，直接丢弃
        if (mListener == null) {
            checkWhich = 0;
            return;
        }
        if (buf.length == 7) {
            if (checkWhich == 1) {
                mListener.onLightState(this.getIdentification(), buf[5] == 0x01);
            } else if (checkWhich == 2) {
                mListener.onLockState(this.getIdentification(), buf[4] == 0x01);
            }
        }
        checkWhich = 0;
    }

    /**
     * 处理串口数据(开关门，开关灯)指令
     *
     * @param buf 协议数据
     */
    private void processIOData(byte[] buf) {
        if (lastCmd[0] == DataProtocol.GPIO4_WRITE && lastCmd[1] == DataProtocol.GPIO_WRITE_LOW) {
            processOpenLock(buf);
        } else if (lastCmd[0] == DataProtocol.GPIO4_WRITE && lastCmd[1] == DataProtocol.GPIO_WRITE_HIGH) {
            processCloseLock(buf);
        } else if (lastCmd[0] == DataProtocol.GPIO3_WRITE && lastCmd[1] == DataProtocol.GPIO_WRITE_LOW) {
            processOpenLight(buf);
        } else if (lastCmd[0] == DataProtocol.GPIO3_WRITE && lastCmd[1] == DataProtocol.GPIO_WRITE_HIGH) {
            processCloseLight(buf);
        }
        lastCmd = null;
    }

    /**
     * 解析开门指令
     *
     * @param buf 协议数据
     */
    private void processOpenLock(byte[] buf) {
        if (buf[4] == DataProtocol.CMD_FAILED) {
            //开门错误，就在发送开门指令？以前的死循环发送是什么情况？
            sendOpenLock();
            return;
        }
        //操作成功，回调不为空就回调
        if (buf[4] == DataProtocol.CMD_SUCCESS && mListener != null) {
            mListener.onLockOpen(this.getIdentification(), true);
        }
    }

    /**
     * 解析关门指令
     *
     * @param buf 协议数据
     */
    private void processCloseLock(byte[] buf) {
        if (buf[4] == DataProtocol.CMD_FAILED) {
            //开门错误，就在发送开门指令？以前的死循环发送是什么情况？
            sendCloseLock();
            return;
        }
        //操作成功，回调不为空就回调
        if (buf[4] == DataProtocol.CMD_SUCCESS && mListener != null) {
            mListener.onLockClose(this.getIdentification(), true);
        }
    }

    /**
     * 解析开灯
     *
     * @param buf 协议数据
     */
    private void processOpenLight(byte[] buf) {
        if (buf[4] == DataProtocol.CMD_FAILED) {
            //开门错误，就在发送开门指令？以前的死循环发送是什么情况？
            sendOpenLight();
            return;
        }
        //操作成功，回调不为空就回调
        if (buf[4] == DataProtocol.CMD_SUCCESS && mListener != null) {
            mListener.onLightOpen(this.getIdentification(), true);
        }
    }

    /**
     * 解析关灯
     *
     * @param buf 协议数据
     */
    private void processCloseLight(byte[] buf) {
        if (buf[4] == DataProtocol.CMD_FAILED) {
            //开门错误，就在发送开门指令？以前的死循环发送是什么情况？
            sendCloseLight();
            return;
        }
        //操作成功，回调不为空就回调
        if (buf[4] == DataProtocol.CMD_SUCCESS && mListener != null) {
            mListener.onLightClose(this.getIdentification(), true);
        }
    }

    /**
     * 设置下一根天线
     *
     * @return 是否发出
     */
    private boolean sendNextWorkAnt() {
        //加同步锁
        synchronized (locker) {
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

    /**
     * 没有收到扫描最终结果包，导致流程中断
     * 使用延时操作发送操作下一根天线，让流程继续运行
     */
    private void sendNextWorkAntDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.e("延时重置天线了", "延时重置天线了");
                currentAntIndex++;
                sendNextWorkAnt();
            }
        }, 800);
    }

    /**
     * 取消延时发送设置天线协议
     */
    private void cancelSendNextWorkAntDelay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 发送盘存的命令
     */
    private void sendRtInventory() {
        //准备0x8B盘存指令
        byte[] data = new byte[]{DataProtocol.RTINVENTORY_SESSION, DataProtocol.RTINVENTORY_TARGET, DataProtocol.RTINVENTORY_REPEAT};
        //将盘存的全部指令发送出去
        sendPacket(DataProtocol.CMD_REALTIME_INVENTORY, data);
    }

    //    提供给外部的方法，发起某个操作的方法

    @Override
    public int closeChannel() {
        //调用父类的关闭方法
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    /**
     * 开始扫描的方法，
     *
     * @param timeout 没有新标签的时间
     * @return 成功失败
     */
    @Override
    public int startScan(int timeout) {
        //先查看是否是在扫描中，因为只能单任务操作，所以需要加标识来处理
        if (scanModel) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果没有在扫描，就进入扫描，并且设置标识,
        scanModel = true;
        //记录没有新标签的时间
        scanTime = timeout;
        //如果是点击开始扫描的话就设置上次收到新标签的时间为当前时间
        lastTipTime = System.currentTimeMillis();
        //数据回到初始状态
        epcList.clear();
        //开始扫描默认是第1根天线
        currentAntIndex = 0;
        //调用开始盘点方法，根据结果返回成功或者失败
        return sendNextWorkAnt() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    /**
     * 停止扫描 （该方法只能在此通道服务端停止扫描操作，不能直接停止底层扫描返回）
     * 所以调用改方法以后需要延时2s以后才能进行下一次扫描
     *
     * @return 无
     */
    @Override
    public int stopScan() {
        scanModel = false;
        return FunctionCode.SUCCESS;
    }

    /**
     * 设置设备功率
     *
     * @param power 功率值
     * @return 返回码
     */
    @Override
    public int setPower(byte power) {
        byte[] data = new byte[]{power};
        return sendPacket(DataProtocol.CMD_SET_OUTPUT_POWER, data) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    /**
     * 查询功率
     *
     * @return 返回码
     */
    @Override
    public int getPower() {
        byte[] data = new byte[]{};
        return sendPacket(DataProtocol.CMD_GET_OUTPUT_POWER, data) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int checkAnts() {
        // TODO: 2019/1/29 还未实现，需要编写代码
        return -10;
    }

    /**
     * 重置的命令
     *
     * @return 返回码
     */
    @Override
    public int reset() {
        boolean ret = sendPacket(DataProtocol.CMD_RESET, null);
        //断开连接
        closeChannel();
        return ret ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    /**
     * 开锁的操作
     *
     * @return 返回码
     */
    @Override
    public int openLock() {
        if (lastCmd == null) {
            return sendOpenLock() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
        } else {
            return FunctionCode.DEVICE_BUSY;
        }
    }

    /**
     * 关锁操作
     *
     * @return 返回码
     */
    @Override
    public int closeLock() {
        if (lastCmd == null) {
            return sendCloseLock() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
        } else {
            return FunctionCode.DEVICE_BUSY;
        }
    }

    /**
     * 开灯操作
     *
     * @return 返回码
     */
    @Override
    public int openLight() {
        if (lastCmd == null) {
            sendOpenLight();
            return sendOpenLight() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
        } else {
            return FunctionCode.DEVICE_BUSY;
        }
    }

    /**
     * 关灯操作
     *
     * @return 返回码
     */
    @Override
    public int closeLight() {
        if (lastCmd == null) {
            return sendCloseLight() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
        } else {
            return FunctionCode.DEVICE_BUSY;
        }
    }

    /**
     * 检测锁的状态
     *
     * @return 返回码
     */
    @Override
    public int checkLockState() {
        if (lastCmd == null) {
            checkWhich = 2;
            return sendGPIOState() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
        } else {
            return FunctionCode.DEVICE_BUSY;
        }
    }

    /**
     * 检测灯的状态
     *
     * @return 返回码
     */
    @Override
    public int checkLightState() {
        if (lastCmd == null) {
            checkWhich = 1;
            return sendGPIOState() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
        } else {
            return FunctionCode.DEVICE_BUSY;
        }
    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "RodinBell";
    }

    @Override
    public String getVersion() {
        return "V1.0";
    }

    private boolean sendOpenLock() {
        byte[] data = new byte[]{DataProtocol.GPIO4_WRITE, DataProtocol.GPIO_WRITE_HIGH};
        lastCmd = data;
        return sendPacket(DataProtocol.CMD_GPIO_WRITE, data);
    }

    /**
     * create by 孙朝阳 on 2018-12-12
     * 发送关锁命令
     */
    private boolean sendCloseLock() {
        byte[] data = new byte[]{DataProtocol.GPIO4_WRITE, DataProtocol.GPIO_WRITE_LOW};
        lastCmd = data;
        return sendPacket(DataProtocol.CMD_GPIO_WRITE, data);
    }

    /**
     * create by 孙朝阳 on 2018-12-12
     * 发送开灯命令
     */
    private boolean sendOpenLight() {
        byte[] data = new byte[]{DataProtocol.GPIO3_WRITE, DataProtocol.GPIO_WRITE_LOW};
        lastCmd = data;
        return sendPacket(DataProtocol.CMD_GPIO_WRITE, data);
    }

    /**
     * create by 孙朝阳 on 2018-12-12
     * 发送关灯命令
     */
    private boolean sendCloseLight() {
        byte[] data = new byte[]{DataProtocol.GPIO3_WRITE, DataProtocol.GPIO_WRITE_HIGH};
        lastCmd = data;
        return sendPacket(DataProtocol.CMD_GPIO_WRITE, data);
    }

    /**
     * create by 孙朝阳 on 2018-12-12
     * 发送检测门、锁的状态，二者是同一指令
     */
    private boolean sendGPIOState() {
        return sendPacket(DataProtocol.CMD_GPIO_READ, null);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.e("连接断开了");
        mHandler.removeCallbacksAndMessages(null);
        mHandler=null;
        if (mListener != null) {
            mListener.onConnectState(this, getIdentification(), false);
        }
        super.channelInactive(ctx);
    }


    /**
     * 注册监听回调
     *
     * @param listener 回调
     */
    public void registerRodinbellMessageListener(ReaderMessageListener listener) {
        this.mListener = listener;
    }
}
