package high.rivamed.myapplication.utils;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.reader.ReaderManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.dbmodel.BoxIdBean;

import static high.rivamed.myapplication.cont.Constants.CONSUMABLE_TYPE;

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
	List<DeviceInfo> deviceInfos = IdCardManager.getIdCardManager().getConnectedDevice();
	List<DeviceInfo> connectedDeviceb = ConsumableManager.getManager().getConnectedDevice();
	List<DeviceInfo> connectedDeviceR = ReaderManager.getManager().getConnectedReader();
	arrayList.addAll(deviceInfos);
	arrayList.addAll(connectedDeviceb);
	arrayList.addAll(connectedDeviceR);
	return  arrayList;
   }
   /**
    * 获取锁、指纹仪、bom主板
    * @return
    */
   public static List<String> getBomDeviceId() {
	List<DeviceInfo> deviceInfos = ConsumableManager.getManager().getConnectedDevice();
	List<String> identifition = new ArrayList<>();
	for (DeviceInfo d : deviceInfos) {
	   String bomDeviceId = d.getIdentification();
	   identifition.add(bomDeviceId);
	}
	return identifition;
   }
   /**
    * 获取reader
    * @return
    */
   public static List<String> getReaderDeviceId() {
	List<DeviceInfo> connectedDevice = ReaderManager.getManager().getConnectedReader();
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
    * @return
    */
   public static void getDoorStatus() {
	List<DeviceInfo> deviceInfos = ConsumableManager.getManager().getConnectedDevice();
	for (DeviceInfo s : deviceInfos) {
	   List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", s.getIdentification(),
								    CONSUMABLE_TYPE).find(BoxIdBean.class);
	   if (boxIdBeans.size()>1){
		ConsumableManager.getManager().checkDoorState(s.getIdentification());
		Log.i("ssffff", "检测的全部门");
	   }else {
		ConsumableManager.getManager().checkDoorState(s.getIdentification(),0);
		Log.i("ssffff", "检测1个门-");
	   }

	}
   }
}
