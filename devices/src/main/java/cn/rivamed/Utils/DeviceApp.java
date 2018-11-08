package cn.rivamed.Utils;

import android.app.Application;
import android.content.Context;

public class DeviceApp extends Application {

    private static DeviceApp instance;


    public static synchronized DeviceApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
