package org.androidpn.client;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

/**
 * @ProjectName: new2.6.3
 * @Package: org.androidpn.client
 * @ClassName: AppServiceManager
 * @Description: java类作用描述
 * @Author: Amos_Bo
 * @CreateDate: 2018/11/6 10:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/6 10:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AppBroadcastReceiverManager {

    public static final String ACTION_NOTIFICATION_NETSTATE = "org.androidpn.client.NOTIFICATION_NETSTATE";
    public static final String DATE_NETLINKED = "org.androidpn.client.NOTIFICATION_NETLINKED";
    private static NetLinkReceiver mNetLinkReceiver = new NetLinkReceiver();

    public static void registerNetLinkReceiver(Context application) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NOTIFICATION_NETSTATE);
        application.registerReceiver(mNetLinkReceiver, filter);
    }

    public static void addNetLinkListener(NetLinkReceiver.NetLinkListener linkListener) {
        if (mNetLinkReceiver != null) {
            mNetLinkReceiver.addNetLinkListener(linkListener);
        } else {
            Log.e("ABRM", "NetLinkReceiver没有初始化");
        }
    }

    public static void removeNetLinkListener(NetLinkReceiver.NetLinkListener linkListener) {
        if (mNetLinkReceiver != null) {
            mNetLinkReceiver.removeNetLinkListener(linkListener);
        } else {
            Log.e("ABRM", "NetLinkReceiver没有初始化");
        }
    }

    public static void unregisterNetLinkReceiver(Context application) {
        if (mNetLinkReceiver != null) {
            application.unregisterReceiver(mNetLinkReceiver);
            mNetLinkReceiver.removeAllNetLinkListener();
        }
    }
}
