package com.ruihua.reader.local;

import android.support.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.ReaderProducerType;
import com.ruihua.reader.local.callback.LocalReaderOperate;
import com.ruihua.reader.local.corelinks.CoreLinksManager;
import com.ruihua.reader.local.raylinks.RaylinksManager;
import com.ruihua.reader.ReaderCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ： 本地连接（usb或者串口）reader管理类(单例模式)
 *
 * @author : Yich
 * date: 2019/4/2
 */
public class LocalReaderManager {
    private static LocalReaderManager manager;

    private LocalReaderManager() {
    }

    public static synchronized LocalReaderManager getManager() {
        if (manager == null) {
            manager = new LocalReaderManager();
        }
        return manager;
    }

    /**
     * 保存各个已连接的Reader的集合
     */
    private Map<String, LocalReaderOperate> connectReader = new HashMap<>();
    private ReaderCallback mCallback;

    /**
     * 添加连接好的设备到集合中
     *
     * @param readerId 设备id，唯一标识
     * @param operate  设备通用接口
     */
    public void addConnectReader(String readerId, LocalReaderOperate operate) {
        //如果集合中有该设备，删除该设备
        if (connectReader.containsKey(readerId)) {
            connectReader.remove(readerId);
        }
        connectReader.put(readerId, operate);
    }


    /**
     * 删除断开连接的设备
     *
     * @param readerId 设备标识
     */
    public void delDisConnectReader(String readerId) {
        if (connectReader.containsKey(readerId)) {
            connectReader.remove(readerId);
        }
    }

    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备的集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, LocalReaderOperate> d : connectReader.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), "", d.getValue().getProducer(), d.getValue().getVersion()));
            }
        }
        return deviceInfos;
    }

    /**
     * 连接本地设备， 传入设备类型
     *
     * @param type 设备厂家的类型，LocalReaderType类中列出
     */
    public void connect(int type) {
        //根据传入的类型，连接对应的设备
        switch (type) {
            case ReaderProducerType.TYPE_LOCAL_RAYLINKS:
                RaylinksManager raylinksManager = new RaylinksManager(this);
                raylinksManager.connect();
                break;
            case ReaderProducerType.TYPE_LOCAL_CORELINKS:
                CoreLinksManager coreLinksManager = new CoreLinksManager(this);
                coreLinksManager.connect();
                //nothing
                break;
            default:
                break;
        }
    }

    /**
     * 断开指定设备连接
     *
     * @param readerId ，设备的id值
     */
    public int disConnect(String readerId) {
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).disConnect();
    }

    /**
     * 开始扫描
     *
     * @param readerId 设备id
     * @param timeOut  超时时间
     * @return 操作返回码
     */
    public int startScan(String readerId, @IntRange(from = 1000, to = 10 * 1000) int timeOut) {
        //如果没有指定的设备，返回没有该设备的码
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).startScan();
    }

    /**
     * 重新扫描，实际是清空下部去重集合数据，能够重新扫描返回数据
     *
     * @param readerId 设备唯一标识
     * @return
     */
    public int reScan(String readerId) {
        //如果没有指定的设备，返回没有该设备的码
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).reScan();
    }

    /**
     * 停止扫描
     *
     * @param readerId 设备唯一标识
     * @return 返回码
     */
    public int stopScan(String readerId) {
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).stopScan();
    }

    /**
     * 设置设备的功率
     *
     * @param readerId 设备唯一标识
     * @param power    功率（限制输入范围是1-30）
     * @return 返回码
     */
    public int setPower(String readerId, @IntRange(from = 1, to = 30) int power) {
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).setPower(power);
    }

    /**
     * 查询设备现在的输出功率
     *
     * @param readerId 设备唯一标识
     * @return 返回码
     */
    public int getPower(String readerId) {
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).getPower();
    }

    /**
     * 重置设备
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int restDevice(String deviceId) {
        if (!connectReader.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(deviceId).reset();
    }

    /**
     * 获取设备频率
     *
     * @param readerId 设备id
     * @return 返回码
     */
    public int getFrequency(String readerId) {
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).getFrequency();
    }

    /**
     * 删除下部去重结合中的某个epc，使其能够再次返回这个标签
     *
     * @param readerId 设备id
     * @param epc      epc
     * @return 返回码
     */
    public int delOneEpc(String readerId, String epc) {
        if (!connectReader.containsKey(readerId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectReader.get(readerId).delOneEpc(epc);
    }

    /**
     * 获取回调
     *
     * @return 回调接口实例
     */
    public ReaderCallback getCallback() {
        return mCallback;
    }

    /**
     * 注册监听回调
     *
     * @param callback 回调
     */
    public void registerCallback(ReaderCallback callback) {
        this.mCallback = null;
        this.mCallback = callback;
    }

    /**
     * 反注册回调
     */
    public void unRegisterCallback() {
        this.mCallback = null;
    }

}
