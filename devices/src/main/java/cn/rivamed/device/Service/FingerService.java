package cn.rivamed.device.Service;

import cn.rivamed.DeviceManager;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:35
 * @Modyfied By :
 */
public class FingerService extends BaseService {

    boolean regModel = false;

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean StartService(DeviceManager deviceManager) {
        return false;
    }

    @Override
    public boolean StopService() {
        return false;
    }
}
