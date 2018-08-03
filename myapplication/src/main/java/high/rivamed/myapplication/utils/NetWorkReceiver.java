package high.rivamed.myapplication.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 项目名称:    TCP_SC_Demo
 * 创建者:      Danniu
 * 创建时间:    2018/5/31 13:24
 * 描述:        监听网线、WIFI、无网
 * 包名:        com.danniu.myapp.receiver
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NetWorkReceiver extends BroadcastReceiver {
   public IntAction  intAction;
   public int NET_ETHERNET = 1;
   public int NET_WIFI = 2;
   public int NET_NOCONNECT = 0;

   @Override
   public void onReceive(Context context, Intent intent) {
	String action = intent.getAction();

	if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {

	   switch (isNetworkAvailable(context)) {
		case 1:
//		   intAction.setText("有线");
		   intAction.setInt(1);
		   break;
		case 2:
//		   intAction.setText("wifi");
		   intAction.setInt(2);
		   break;
		case 0:
//		   intAction.setText("无网络");
		   intAction.setInt(0);
		   break;
		default:
		   break;
	   }
	}
   }
   private int isNetworkAvailable(Context context) {
	ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ethNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	if (ethNetInfo != null && ethNetInfo.isConnected()) {
	   return NET_ETHERNET;
	} else if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
	   return NET_WIFI;
	} else {
	   return NET_NOCONNECT;
	}
   }
   public interface IntAction{
       void setText(String d);
       void setInt(int k);
   }
   public void setInteractionListener(IntAction intAction){
      this.intAction=intAction;
   }
}
