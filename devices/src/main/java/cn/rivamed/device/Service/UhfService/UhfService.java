package cn.rivamed.device.Service.UhfService;

import android.util.Log;

import com.clou.uhf.G3Lib.CLReader;

import cn.rivamed.DeviceManager;

public interface UhfService {

    public boolean StartService(DeviceManager deviceManager);

    public boolean StopService();

    public boolean isAlive();


}
