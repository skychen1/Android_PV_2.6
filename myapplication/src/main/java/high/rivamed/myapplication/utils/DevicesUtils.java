package high.rivamed.myapplication.utils;

import android.content.Context;
import android.view.WindowManager;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.ruihua.reader.ReaderManager;

import java.util.ArrayList;
import java.util.List;

import cn.rivamed.Eth002Manager;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/21 15:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class DevicesUtils {
   public static  List<DeviceInfo>  arrayList = new ArrayList();
   /**
    * 获取设备连接设备
    */
   public static List<DeviceInfo> QueryConnectedDevice() {
	arrayList.clear();
	List<DeviceInfo> deviceInfos = Eth002Manager.getEth002Manager().getConnectedDevice();
	for (int i = 0; i < deviceInfos.size(); i++) {
	   deviceInfos.get(i).setDeviceType(DeviceInfo.DeviceType.Eth002);
	}
	List<DeviceInfo> connectedDevice = ReaderManager.getManager().getConnectedDevice();
	for (int i = 0; i < connectedDevice.size(); i++) {
	   connectedDevice.get(i).setDeviceType(DeviceInfo.DeviceType.UHFREADER);
	}
	arrayList.addAll(deviceInfos);
	arrayList.addAll(connectedDevice);
	return  arrayList;
   }
   /**
    * 获取锁
    * @return
    */
   public static List<String> getEthDeviceId() {
	List<DeviceInfo> deviceInfos = Eth002Manager.getEth002Manager().getConnectedDevice();
	//	String s = "";
	List<String> identifition = new ArrayList<>();
	for (DeviceInfo d : deviceInfos) {
	   String eth002DeviceId = d.getIdentification();
	   identifition.add(eth002DeviceId);
	   //	   s += d.getIdentification() + "|||";
	}
	return identifition;
   }
   /**
    * 获取reader
    * @return
    */
   public static List<String> getReaderDeviceId() {
	List<DeviceInfo> connectedDevice = ReaderManager.getManager().getConnectedDevice();
	List<String> identifition = new ArrayList<>();
	for (DeviceInfo d : connectedDevice) {
	   String uhfDeviceId = d.getIdentification();
	   identifition.add(uhfDeviceId);
	}
	return identifition;
   }
   /**
    * 获取当前屏幕宽度px(像素)
    */
   public static int getScreenWidth(Context context) {
	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	int width = wm.getDefaultDisplay().getWidth();
	return width;
   }
   /**
    * 检测所有门锁是否关闭 true 关闭，false未关闭
    *
    * @return
    */
   public static void getDoorStatus() {
	List<DeviceInfo> connectedDevice = Eth002Manager.getEth002Manager().getConnectedDevice();
	for (DeviceInfo s : connectedDevice) {
	   Eth002Manager.getEth002Manager().checkDoorState(s.getIdentification());
	}
   }
}
