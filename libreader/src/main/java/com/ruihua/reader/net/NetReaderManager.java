package com.ruihua.reader.net;

import android.support.annotation.IntRange;
import android.util.Log;
import android.widget.Toast;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.net.callback.ReaderHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ：  扫描设备 管理类(包括罗丹贝尔，和鸿陆的)
 * 提供给用户各种设备操作方法，回调各种数据（详情见文档）
 *
 * @author : Yich
 * date: 2019/2/22
 */

public class NetReaderManager {
    private static NetReaderManager manager;

    private NetReaderManager() {
    }

    /**
     * 单例模式，一个种设备只需要一个地方操作
     *
     * @return 实例
     */
    public static synchronized NetReaderManager getManager() {
        if (manager == null) {
            manager = new NetReaderManager();
        }
        return manager;
    }

    /**
     * callback 回调。给外部使用的地方调用
     * connectHandler 所有连接设备通道的的集合，用于对设备的操作
     * mService 服务
     * isSimulation 是否是模拟形式；模拟形式下不需要连接设备，所有数据都是假数据返回  默认是正常模式
     * testHandler 测试数据的类，返回假数据
     */
    private ReaderCallback callback;
    private Map<String, ReaderHandler> connectHandler = new HashMap<>();
    private ReaderService mService;


    /**
     * 添加一个设备的通道进行维护
     *
     * @param identification 通道唯一标识
     * @param handler        通道
     */
    public void addConnectHandler(String identification, ReaderHandler handler) {
        if (connectHandler.containsKey(identification) && connectHandler.get(identification).equals(handler)) {
            //查看如果以前有这个相同的设备通道，就关闭以前的那个通道
            connectHandler.get(identification).closeChannel();
        }
        //加入到集合中维护
        connectHandler.put(identification, handler);
    }

    /**
     * 当一个设备断开的时候就删除 掉集合中维护的对应的通道
     *
     * @param identification 标识
     */
    public void delDisConnectHandler(String identification) {
        if (connectHandler.containsKey(identification)) {
            connectHandler.remove(identification);
        }
    }

    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, ReaderHandler> d : connectHandler.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getRemoteIP(), d.getValue().getProducer(), d.getValue().getVersion()));
            }
        }
        return deviceInfos;
    }


    /**
     * 开启服务
     *
     * @param port 端口号。限制输入范围是0-65536
     * @param type 设备的类型（罗丹贝尔或者鸿陆）
     */
    public void startService(@IntRange(from = 1, to = 65536) int port, int type) {
        //如果服务部存在创建该服务，只能有一个服务
        if (mService == null) {
            mService = new ReaderService(this);
            mService.startService(port, type);
        }else {
            Log.i("reader", "启动失败");
        }
    }

    /**
     * 关闭服务，不再使用扫描设备的时候可以关闭服务
     */
    public void stopService() {
        if (mService == null) {
            return;
        }
        mService.stopService();
        mService=null;
    }

    /**
     * 断开某个设备的连接
     *
     * @param deviceId
     * @return
     */
    public int closeChannel(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //根据id，调用对应通道id进行扫描
        return connectHandler.get(deviceId).closeChannel();
    }

    /**
     * 开始扫描的方法
     *
     * @param deviceId 设备的唯一标识id
     * @param timeOut  持续扫描的时候（多久没有新标签就借宿扫描）限制输入范围是1-10
     * @return 返回码
     */
    public int startScan(String deviceId, @IntRange(from = 1000, to = 10 * 1000) int timeOut) {
        //如果没有指定的设备，返回没有该设备的码
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //根据id，调用对应通道id进行扫描
        return connectHandler.get(deviceId).startScan(timeOut);
    }

    /**
     * 停止扫描 （该方法只能在此通道服务端停止扫描操作，不能直接停止底层扫描返回）
     * 所以调用改方法以后需要延时2秒以后才能进行下一次扫描
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int stopScan(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).stopScan();
    }

    /**
     * 设置设备的功率
     *
     * @param deviceId 设备唯一标识
     * @param power    功率（限制输入范围是1-30）
     * @return 返回码
     */
    public int setPower(String deviceId, @IntRange(from = 1, to = 30) int power) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).setPower((byte) power);
    }

    /**
     * 查询设备现在的输出功率
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int getPower(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).getPower();
    }

    /**
     * 查询所有天线的状态  （暂未实现，后期处理）
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int checkAnt(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return -1;
    }

    /**
     * 重置设备
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int restDevice(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).reset();
    }

    /**
     * 开锁
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int openLock(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).openLock();
    }

    /**
     * 关锁
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int closeLock(String deviceId) {
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).closeLock();
    }

    /**
     * 检测锁的状态
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int checkLockState(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).checkLockState();
    }

    /**
     * 开灯
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int openLight(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).openLight();
    }

    /**
     * 关灯
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int checkLightState(String deviceId) {
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).checkLightState();
    }

    /**
     * 检测等的状态
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int closeLight(String deviceId) {

        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).closeLight();
    }

    /**
     * 拿到回调
     *
     * @return 用户层回调
     */
    public ReaderCallback getCallback() {
        return callback;
    }

    /**
     * 注册监听回调
     *
     * @param callback 回调
     */
    public void registerCallback(ReaderCallback callback) {
        this.callback = null;
        this.callback = callback;
    }

    /**
     * 反注册注册监听回调、
     * 在页面销毁的之前必须反注册，防止内存泄露
     */
    public void unRegisterCallback() {
        this.callback = null;
    }

}
