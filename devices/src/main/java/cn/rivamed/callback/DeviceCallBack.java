package cn.rivamed.callback;

import cn.rivamed.model.TagInfo;
import cn.rivamed.device.DeviceType;

import java.util.List;
import java.util.Map;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:00
 * @Modyfied By :
 */
public interface DeviceCallBack {

    /**
     * 设备连接通知
     *
     * @param deviceType
     * @param deviceIndentify
     */
    void OnDeviceConnected(DeviceType deviceType, String deviceIndentify);

    /***
     * 设备断开通知
     * @param deviceType
     * @param deviceIndentify
     */
    void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify);

    /***
     * 检查设备状态结果通知
     * @param deviceType
     * @param deviceId
     * @param code 详情见 functionCode
     */
    void OnCheckState(DeviceType deviceType, String deviceId, Integer code);


    /***
     * 刷卡器刷卡通知
     * 每组柜子仅有一个读卡器，故不做设备标识
     * @param  deviceId  刷卡器连接的Eth002模块的设备ID，每个柜子一个ETH002模块
     * @param idCard  卡片信息
     */
    void OnIDCard(String deviceId, String idCard);


    /***
     * 指纹采集通知
     * 每组柜子仅有一个指纹仪，故不作设备标识
     * @param deviceId 指纹仪连接的EHT020模块的设备ID
     * @param fingerFea  指纹数据
     */
    void OnFingerFea(String deviceId, String fingerFea);

    /***
     * 指纹注册指令执行通知，该指令执行结果为success,则表示 指纹仪已处于指纹注册模式，
     *
     * 需要提示用户连续按下3次指纹，然后会接收到OnFingerRegisterRet 回调
     * 每组柜子仅有一个指纹仪，故不作设备标识
     * @param deviceId 指纹仪连接的EHT020模块的设备ID
     * @param success 是否注册成功
     *
     */
    void OnFingerRegExcuted(String deviceId, boolean success);


    /***
     * 指纹注册结果通知
     * 每组柜子仅有一个指纹仪，故不作设备标识
     * @param deviceId 指纹仪连接的EHT020模块的设备ID
     * @param success 是否注册成功
     * @param fingerData 指纹注册数据
     *
     */
    void OnFingerRegisterRet(String deviceId, boolean success, String fingerData);


    /***
     * 柜门打开通知
     * @param success  是否开门成功  true 门以打开 ，false 门未打开
     * @param deviceIndentify
     */
    void OnDoorOpened(String deviceIndentify, boolean success);


    /***
     * 柜门关闭通知
     * @param success  是否开门成功  true 门已关闭 ，false 门为关闭
     * @param deviceIndentify
     */
    void OnDoorClosed(String deviceIndentify, boolean success);


    /***
     * 柜门状态变通知
     * @param deviceIndentify
     */
    void OnDoorCheckedState(String deviceIndentify,boolean opened);

    /***
     * RFID UHF RFID READER  扫描结果通知，每次扫描可能有多个通知；通知结束后，会有OnUhfScanCompleted通知
     * @param deviceId 设备标识
     * @param epcs      epc扫描结果，其中Key为EPC，value 为每个epc被多次扫描的信息
     */
    void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs);

    /***
     * UHF READER 扫描结束通知
     * RFID UHF RFID READER  扫描结束通知
     * @param deviceId 设备标识
     */
    void OnUhfScanComplete(boolean success, String deviceId);


    /**
     * UHF READER 设置功率结果通知
     *
     * @param success  是否成功
     * @param deviceId 设备ID
     */
    void OnUhfSetPowerRet(String deviceId, boolean success);

    /**
     * UHF READER 获取功率结果通知
     *
     * @param success  是否成功
     * @param deviceId 设备ID
     * @param power    功率
     */
    void OnUhfQueryPowerRet(String deviceId, boolean success, int power);


}
