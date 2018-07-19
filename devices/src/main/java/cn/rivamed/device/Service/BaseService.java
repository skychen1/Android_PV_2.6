package cn.rivamed.device.Service;

import cn.rivamed.DeviceManager;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:34
 * @Modyfied By :
 */
public abstract class BaseService {

    public abstract boolean isAlive();

    private DeviceManager deviceManager;

    protected DeviceManager getDeviceManager(){
        return this.deviceManager;
    }


    public boolean StartService(DeviceManager deviceManager){
        this.deviceManager=deviceManager;
        return true;
    }

    public abstract boolean StopService();
}
