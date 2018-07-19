package cn.rivamed.device.ClientHandler.uhfClientHandler;

import java.util.List;
import java.util.Map;

import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.model.TagInfo;


/***
 *  ETH002 设备回调函数
 *  @author 郝小鹏
 *
 *
 */
public interface UhfClientMessage {

    public DeviceHandler getDeviceHandler();

    void setDeviceHandler(DeviceHandler handler);


    /**
     * 设备断开连接时触发的事件
     */
    void OnDisconnected();
    /**
     * 获取到设备ID时触发的事件
     */
    void OnConnected();

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
