package high.rivamed.myapplication.utils;

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

   static String eth002DeviceId = "";

   public static String getDeviceId() {
	List<DeviceManager.DeviceInfo> deviceInfos = DeviceManager.getInstance()
		.QueryConnectedDevice();
	String s = "";
	for (DeviceManager.DeviceInfo d : deviceInfos) {
	   if (d.getDeviceType() == DeviceType.Eth002V2) {
		eth002DeviceId = d.getIdentifition();
	   }
	   s += d.getIdentifition() + "|||";

	}
	return eth002DeviceId;
   }

}
