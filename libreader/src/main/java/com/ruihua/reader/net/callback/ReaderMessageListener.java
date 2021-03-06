package com.ruihua.reader.net.callback;

import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;

import java.util.List;
import java.util.Map;

/**
 * describe ：设备操作的回调
 *
 * @author : Yich
 * data: 2019/2/27
 */
public interface ReaderMessageListener {


    /**
     * 设备连接状态返回，
     *
     * @param mHandler  通道
     * @param deviceId  设备的唯一标识号
     * @param isConnect 连接的装态，，true为已连接，false为已断开
     */
    void onConnectState(ReaderHandler mHandler, String deviceId, boolean isConnect);

    /**
     * 扫描完成结果回调
     *
     * @param deviceId 设备的唯一标识号
     * @param result   扫描到的结果数据 (如果扫描失败结果为null) ，没有标签为空集合， 有标签就是标签的数据
     *                 map的key值为标签，list是标签扫描的信息（扫到一次为一个数据，多次为多个数据）
     */
    void onScanResult(String deviceId, Map<String, List<EpcInfo>> result);

    /**
     * 扫描过程中有一个新标签的回调
     *
     * @param deviceId 设备的唯一标识号
     * @param epc      单个的epc数据
     * @param ant      天线
     */
    void onScanNewEpc(String deviceId, String epc, int ant);

    /**
     * 设置功率结果通知
     *
     * @param success  是否成功
     * @param deviceId 设备ID
     */
    void onSetPowerRet(String deviceId, boolean success);

    /**
     * 获取功率结果通知
     *
     * @param deviceId 设备ID
     * @param power    功率
     */
    void onQueryPowerRet(String deviceId, int power);

    /**
     * 获取功率结果通知(所有天线)
     *
     * @param deviceId 设备ID
     * @param power    功率
     */
    void onQueryPowerRet(String deviceId, int[] power);

    /**
     * 检查天线是否可用
     *
     * @param deviceId 设备ID
     * @param ant      天线是否可用的集合，具体信息参看实例类
     */
    void onCheckAnt(String deviceId, List<AntInfo> ant);

    /**
     * 开锁回调
     *
     * @param deviceId  设备id
     * @param isSuccess 是否成功
     */
    void onLockOpen(String deviceId, boolean isSuccess);

    /**
     * 关锁回调
     *
     * @param deviceId  设备id
     * @param isSuccess 是否成功
     */
    void onLockClose(String deviceId, boolean isSuccess);

    /**
     * 开灯回调
     *
     * @param deviceId  设备id
     * @param isSuccess 是否成功
     */
    void onLightOpen(String deviceId, boolean isSuccess);

    /**
     * 关灯回调
     *
     * @param deviceId  设备id
     * @param isSuccess 是否成功
     */
    void onLightClose(String deviceId, boolean isSuccess);

    /**
     * 检查锁状态的回调
     *
     * @param deviceId 设备id
     * @param isOpened 锁的状态 true 为打开， false 为关闭
     */
    void onLockState(String deviceId, boolean isOpened);

    /**
     * 检查灯状态的回调
     *
     * @param deviceId 设备id
     * @param isOpened 锁的状态 true 为打开， false 为关闭
     */
    void onLightState(String deviceId, boolean isOpened);


}
