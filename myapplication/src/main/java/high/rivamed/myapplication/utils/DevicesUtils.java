package high.rivamed.myapplication.utils;

import android.content.Context;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.rivamed.DeviceManager;
import cn.rivamed.device.DeviceType;

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

   /**
    * 获取锁
    * @return
    */
   public static List<String> getEthDeviceId() {
	List<DeviceManager.DeviceInfo> deviceInfos = DeviceManager.getInstance()
		.QueryConnectedDevice();
	String s = "";
	List<String> identifition = new ArrayList<>();
	for (DeviceManager.DeviceInfo d : deviceInfos) {
	   if (d.getDeviceType() == DeviceType.Eth002) {
		String eth002DeviceId = d.getIdentifition();
		identifition.add(eth002DeviceId);
	   }
	   s += d.getIdentifition() + "|||";

	}
	return identifition;
   }
   /**
    * 获取reader
    * @return
    */
   public static List<String> getReaderDeviceId() {
	List<DeviceManager.DeviceInfo> deviceInfos = DeviceManager.getInstance()
		.QueryConnectedDevice();
	List<String> identifition = new ArrayList<>();
	for (DeviceManager.DeviceInfo d : deviceInfos) {
	   if (d.getDeviceType() == DeviceType.UHFREADER) {
		String uhfDeviceId = d.getIdentifition();
		identifition.add(uhfDeviceId);
	   }
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

}
