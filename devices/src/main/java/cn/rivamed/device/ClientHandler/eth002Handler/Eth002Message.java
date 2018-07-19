package cn.rivamed.device.ClientHandler.eth002Handler;

import cn.rivamed.device.ClientHandler.DeviceHandler;


/***
 *  ETH002 设备回调函数
 *  @author 郝小鹏
 *
 *
 */
public interface Eth002Message {

    public DeviceHandler getDeviceHandler();

    public void setDeviceHandler(DeviceHandler handler);

    void ReciveIDCard(String cardNo);

    void DoorClosed(boolean success);

    void DoorOpenRet(boolean opened);

    void DoorState(boolean opened);

    void FingerRegisterRet(boolean regestedSuccess, String fingerData, String userid);

    void FingerGetImage(String fingerData);

    void FingerRegisgerCmdExcuted();

    void FingerDeviceError(boolean disconnected);

    /**
     * 设备断开连接时触发的事件
     */
    void OnDisconnected();
    /**
     * 获取到设备ID时触发的事件
     */
    void OnConnected();

}
