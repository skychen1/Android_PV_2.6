package com.ruihua.reader;

import android.support.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.net.NetReaderManager;
import com.ruihua.reader.test.TestHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * describe ：  扫描设备 管理类(包括罗丹贝尔，和鸿陆的)
 * 提供给用户各种设备操作方法，回调各种数据（详情见文档）
 *
 * @author : Yich
 * date: 2019/2/22
 */

public class ReaderManager {
    private static ReaderManager manager;

    private ReaderManager() {
    }

    /**
     * 单例模式，一个种设备只需要一个地方操作
     *
     * @return 实例
     */
    public static synchronized ReaderManager getManager() {
        if (manager == null) {
            manager = new ReaderManager();
        }
        return manager;
    }

    /**
     * mType  设备类型，网络的或者本地的(默认是网路的)
     * callBack 回调。给外部使用的地方调用
     * connectHandler 所有连接设备通道的的集合，用于对设备的操作
     * mService 服务
     * isSimulation 是否是模拟形式；模拟形式下不需要连接设备，所有数据都是假数据返回  默认是正常模式
     * testHandler 测试数据的类，返回假数据
     */
    private int mConnectType = ReaderConnectType.TYPE_NET;
    private ReaderCallback callBack;
    private boolean isSimulation = false;
    private TestHandler testHandler;


    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备的集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> connectedDevice = new ArrayList<>();
        //如果是测试模式
        if (isSimulation) {
            connectedDevice.add(new DeviceInfo(TestHandler.MID, "", testHandler.getProducer(), testHandler.getVersion()));
            return connectedDevice;
        }
        //根据类型返回对应的设备集合
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                connectedDevice = NetReaderManager.getManager().getConnectedDevice();
                break;
            case ReaderConnectType.TYPE_LOCAL:
                connectedDevice = LocalReaderManager.getManager().getConnectedDevice();
                break;
            default:

                break;
        }
        return connectedDevice;
    }

    /**
     * 设置连接方式
     *
     * @param type 连接方式
     */
    public void setConnectType(int type) {
        this.mConnectType = type;
    }

    /**
     * 设置是否是模拟形式
     *
     * @param isSimulation 是否是模拟形式，true 就是模拟形式，false就是连接设备
     */
    public void setSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
        if (isSimulation) {
            //准备测试通道
            testHandler = new TestHandler(this);
        }
    }

    /**
     * 连接设备,根据设备厂家类型，启用对应的连接
     */
    public void connectReader(int producerType) {
        switch (producerType) {
            //网络连接
            case ReaderProducerType.TYPE_NET_RODINBELL:
                NetReaderManager.getManager().startService(8014, ReaderProducerType.TYPE_NET_RODINBELL);
                break;
            case ReaderProducerType.TYPE_NET_COLU:
                NetReaderManager.getManager().startService(8010, ReaderProducerType.TYPE_NET_COLU);
                break;
            //本地连接
            case ReaderProducerType.TYPE_LOCAL_RAYLINKS:
                LocalReaderManager.getManager().connect(producerType);
                break;
            default:
                break;
        }
    }

    /**
     * 断开设备连接，主要用于本地，因为不断开，下次可能会连不上
     *
     * @param readerId 设备的id
     * @return 是否成功
     */
    public int disConnectReader(String readerId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().closeChannel(readerId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().disConnect(readerId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }


    /**
     * 开始扫描的方法
     *
     * @param deviceId 设备的唯一标识id
     * @param timeOut  持续扫描的时候（多久没有新标签就借宿扫描）限制输入范围是1-10
     * @return 返回码
     */
    public int startScan(String deviceId, @IntRange(from = 1000, to = 10 * 1000) int timeOut) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.startScan(timeOut);
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().startScan(deviceId, timeOut);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().startScan(deviceId, timeOut);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 停止扫描 （该方法只能在此通道服务端停止扫描操作，不能直接停止底层扫描返回）
     * 所以调用改方法以后需要延时2秒以后才能进行下一次扫描
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int stopScan(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.stopScan();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().stopScan(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().stopScan(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 设置设备的功率
     *
     * @param deviceId 设备唯一标识
     * @param power    功率（限制输入范围是1-30）
     * @return 返回码
     */
    public int setPower(String deviceId, @IntRange(from = 1, to = 30) int power) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.setPower((byte) power);
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().setPower(deviceId, power);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().setPower(deviceId, power);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 查询设备现在的输出功率
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int getPower(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.getPower();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().getPower(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().getPower(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 查询所有天线的状态  （暂未实现，后期处理）
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int checkAnt(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.checkAnts();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().checkAnt(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                // TODO: 2019/4/4
                code = -1;
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 重置设备
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int restDevice(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.reset();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().restDevice(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().restDevice(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }


    /**
     * 拿到回调
     *
     * @return 用户层回调
     */
    public ReaderCallback getCallBack() {
        return callBack;
    }

    /**
     * 注册监听回调
     *
     * @param callBack 回调
     */
    public void registerCallback(ReaderCallback callBack) {
        this.callBack = null;
        this.callBack = callBack;
        //测试模式就不需要了
        if (isSimulation) {
            return;
        }
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                NetReaderManager.getManager().registerCallback(callBack);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                LocalReaderManager.getManager().registerCallback(callBack);
                break;
            default:
                break;
        }
    }

    /**
     * 反注册注册监听回调、
     * 在页面销毁的之前必须反注册，防止内存泄露
     */
    public void unRegisterCallback() {
        this.callBack = null;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                NetReaderManager.getManager().unRegisterCallback();
                break;
            case ReaderConnectType.TYPE_LOCAL:
                LocalReaderManager.getManager().unRegisterCallback();
                break;
            default:
                break;
        }
    }

}
