package cn.rivamed.device.ClientHandler;

import cn.rivamed.FunctionCode;
import cn.rivamed.device.DeviceType;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:00
 * @Modyfied By :
 */
public interface DeviceHandler {

    String getIdentification();

    DeviceType getDeviceType();

    String getRemoteIP();

    int Close();

}
