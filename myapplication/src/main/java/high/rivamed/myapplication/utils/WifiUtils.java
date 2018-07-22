package high.rivamed.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.WIFI_SERVICE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/20 14:04
 * 描述:        网络工具类，判断当前网络链接类型
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class WifiUtils {
   public static int isWifi(Context con) {
	ConnectivityManager connectMgr = (ConnectivityManager) con
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo info = connectMgr.getActiveNetworkInfo();
	if (info == null) {
	   // 没网
	   return 0;
	} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
	   return 1;
	} else {
	   return 2;
	}
   }
   //获取wifi本地IP和主机名
   public static String getLocalIpAddress(Context context) {
	WifiManager wifiManager = (WifiManager) context.getSystemService(
		WIFI_SERVICE);
	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	// 获取32位整型IP地址
	int ipAddress = wifiInfo.getIpAddress();
	//返回整型地址转换成“*.*.*.*”地址
	return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				   (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
   }

   /**
    * 获取本地以太网IP
    * @return
    */
   public static String getHostIP() {
	String hostIp = null;
	try {
	   Enumeration nis = NetworkInterface.getNetworkInterfaces();
	   InetAddress ia = null;
	   while (nis.hasMoreElements()) {
		NetworkInterface ni = (NetworkInterface) nis.nextElement();
		Enumeration<InetAddress> ias = ni.getInetAddresses();
		while (ias.hasMoreElements()) {
		   ia = ias.nextElement();
		   if (ia instanceof Inet6Address) {
			continue;// skip ipv6
		   }
		   String ip = ia.getHostAddress();
		   if (!"127.0.0.1".equals(ip)) {
			hostIp = ia.getHostAddress();
			Log.i("MainActivity", "hostIp   " + hostIp);
			break;
		   }
		}
	   }
	} catch (SocketException e) {
	   Log.i("MainActivity", "SocketException");
	   e.printStackTrace();
	}
	return hostIp;
   }
}
