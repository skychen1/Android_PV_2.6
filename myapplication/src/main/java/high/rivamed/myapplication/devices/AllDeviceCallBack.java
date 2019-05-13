package high.rivamed.myapplication.devices;

import android.util.Log;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.activity.LoginActivity.mConfigType;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.fragment.TimelyAllFrag.mTimelyOnResume;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/8/22 12:17
 * 描述:        硬件回调
 * 包名:        high.rivamed.myapplication.devices
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class AllDeviceCallBack {

   private static final String TAG = "AllDeviceCallBack";
   private        List<BoxIdBean>   mBoxIdBeans;
   // 设置本类为单例模式
   private static AllDeviceCallBack instances;
   public static  ArrayList<String> mEthDeviceIdBack;
   public static  ArrayList<String> mEthDeviceIdBack2;
   public static  ArrayList<String> mEthDeviceIdBack3;
   public static  List<String>      mReaderIdList;
   public static  List<String>      mReaderDeviceId;
   public static  List<String>      eth002DeviceIdList;
   public static  String            sReaderType;
   List<String> ss = new ArrayList<>();

   public static AllDeviceCallBack getInstance() {
	//	sReaderType = UIUtils.getConfigReaderType(UIUtils.getContext(), CONFIG_000);
	if (instances == null) {
	   synchronized (NetRequest.class) {
		if (instances == null) {
		   instances = new AllDeviceCallBack();
		   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
		   Log.i(TAG,"mReaderDeviceId    "+mReaderDeviceId.size());
		   eth002DeviceIdList = DevicesUtils.getEthDeviceId();
		   mEthDeviceIdBack = new ArrayList<>();
		   mEthDeviceIdBack3 = new ArrayList<>();
		   mEthDeviceIdBack2 = new ArrayList<>();
		}
	   }
	}
	return instances;
   }

   public void initCallBack() {
	DeviceManager.getInstance().UnRegisterDeviceCallBack();
	DeviceManager.getInstance().RegisterDeviceCallBack(new cn.rivamed.callback.DeviceCallBack() {
	   @Override
	   public void OnDeviceConnected(
		   DeviceType deviceType, String deviceIndentify) {

	   }

	   @Override
	   public void OnDeviceDisConnected(
		   DeviceType deviceType, String deviceIndentify) {
		LogUtils.i(TAG, "设备已断开：" + deviceType + ":::ID=" + deviceIndentify);
	   }

	   @Override
	   public void OnCheckState(
		   DeviceType deviceType, String deviceId, Integer code) {
		LogUtils.i(TAG, "检查门锁开关：" + deviceType + ":::ID=" + deviceId + ":::RET=" + code);
	   }

	   @Override
	   public void OnIDCard(String deviceId, String idCard) {
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mConfigType = 1;//IC卡
		   EventBusUtils.post(new Event.EventICAndFinger(deviceId,idCard,mConfigType));
		}
	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mConfigType = 2;//指纹登录
		   EventBusUtils.post(new Event.EventICAndFinger(deviceId,fingerFea,mConfigType));

		}
	   }

	   @Override
	   public void OnFingerRegExcuted(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {

	   }

	   @Override
	   public void OnDoorOpened(String deviceIndentify, boolean success) {
		//目前设备监控开门success有可能出现错误   都设置成true
		EventBusUtils.post(new Event.EventOppenDoor("true"));
		if (success) {
		   EventBusUtils.post(new Event.PopupEvent(true, "柜门已开"));
		} else {

		}
		if (mEthDeviceIdBack.size() > 0) {
		   for (String s : mEthDeviceIdBack) {
			if (!deviceIndentify.equals(s)) {
			   mEthDeviceIdBack.add(deviceIndentify);
			}
		   }
		} else {
		   mEthDeviceIdBack.add(deviceIndentify);
		}

		//筛选相同的锁
		ArrayList<String> strings = StringUtils.removeDuplicteUsers(mEthDeviceIdBack);
		for (String s : strings) {
		   Log.i(TAG, "  开门strings     " + s);
		}
		mEthDeviceIdBack.clear();
		mEthDeviceIdBack.addAll(strings);

		mEthDeviceIdBack2.clear();
		mEthDeviceIdBack2.addAll(mEthDeviceIdBack);
		mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
		strings.clear();
		EventBusUtils.postSticky(
			new Event.HomeNoClickEvent(true, deviceIndentify));//禁止桌面左边菜单栏点击
	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
		   ss.clear();
		   mBoxIdBeans = LitePal.where("name = ?", UHF_TYPE).find(BoxIdBean.class);
		   for (BoxIdBean s : mBoxIdBeans) {
			DeviceManager.getInstance().CheckDoorState(s.getDevice_id());
		   }
		   Log.i("FAFAS", "OnDoorClosed   " + deviceIndentify);
		} else {//正常开门
		   for (int i = 0; i < mEthDeviceIdBack2.size(); i++) {
			if (mEthDeviceIdBack2.get(i).equals(deviceIndentify)) {
			   mEthDeviceIdBack2.remove(i);
			}
		   }
		   if (mEthDeviceIdBack2 == null || mEthDeviceIdBack2.size() == 0) {
			EventBusUtils.postSticky(new Event.HomeNoClickEvent(false, ""));//开启桌面左边菜单栏点击
			EventBusUtils.postSticky(new Event.EventGoneBtn("显示"));
		   }
		   clossDoorStartScan(deviceIndentify);
		}
	   }

	   @Override
	   public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
		   for (BoxIdBean s : mBoxIdBeans) {//记录强开后未关门
			if (s.getDevice_id().equals(deviceIndentify) && opened) {
			   ss.add(deviceIndentify);
			}
		   }
		   Log.i("FAFAS", "ss   " + ss.size());
		   if (ss.size() == 0) {//强开后门关完了
			StrongOpenScanStart();
		   }
		}else {

		}
	   }

	   @Override
	   public void OnUhfScanRet(
		   boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
		LogUtils.i(TAG, "扫描完成   " + success + "   deviceId   " + deviceId);
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0&&!mTimelyOnResume) {//强开
		   Log.e(TAG,"扫描强开");
		   EventBusUtils.post(new Event.EventStrongOpenDeviceCallBack(deviceId, epcs));
		} else {
		   Log.e(TAG,"正常扫描");
		   EventBusUtils.postSticky(new Event.EventDeviceCallBack(deviceId, epcs));
		}
	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
		} else {
//		   EventBusUtils.postSticky(new Event.EventLoading(false));
		}
		LogUtils.i(TAG, "RFID扫描结束：" + deviceId + ":::success=" + success);

	   }

	   @Override
	   public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

	   }

	   @Override
	   public void OnUhfSetPowerRet(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

	   }
	});
   }

   /**
    * 强开的扫描
    */
   private void StrongOpenScanStart() {
	List<String> deviceId = DevicesUtils.getReaderDeviceId();
	for (String s : deviceId) {//记录强开后未关门
	   Log.i("FAFAS", "StrongOpenScanStart   " + s);
	   int i = DeviceManager.getInstance().StartUhfScan(s, READER_TIME);
	   Log.i("FAFAS", "xxx   " + i);
	}
   }

   /**
    * 柜门检测关闭后进行扫描
    *
    * @param deviceIndentify
    */
   private void clossDoorStartScan(String deviceIndentify) {
	if (mReaderIdList.size() == 0) {
	   new Thread(new Runnable() {
		@Override
		public void run() {
		   try {
			Thread.currentThread().sleep(4000);
			setReaderList(null);
			if (mReaderIdList.size() == 0) {
			   LogUtils.i(TAG, "走了");
			   EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
			   mEthDeviceIdBack.clear();
			   mEthDeviceIdBack3.clear();
			   mEthDeviceIdBack2.clear();
			   EventBusUtils.postSticky(new Event.EventToast("reader未启动，请重新开关柜门"));
			} else {
			   startScan(deviceIndentify);
			}
		   } catch (Exception e) {
			e.printStackTrace();
		   }
		}

	   }).start();

	} else {
	   startScan(deviceIndentify);
	}
   }

   public void setReaderList(String mDeviceCode) {
	if (mDeviceCode == null) {
	   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	   for (int i = 0; i < mReaderDeviceId.size(); i++) {
		String DeviceId = (String) mReaderDeviceId.get(i);
		mReaderIdList.add(DeviceId);
	   }
	} else {
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    READER_TYPE).find(BoxIdBean.class);

	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		LogUtils.i(TAG, "device_id   " + device_id);
		LogUtils.i(TAG, "mReaderDeviceId.size   " + mReaderDeviceId.size());
		for (int i = 0; i < mReaderDeviceId.size(); i++) {
		   LogUtils.i(TAG, "mReaderDeviceId.get(i)   " + mReaderDeviceId.get(i));
		   if (mReaderDeviceId.get(i).equals(device_id)) {
			mReaderIdList.add(device_id);
			LogUtils.i(TAG, " eth002DeviceIdList.size   " + eth002DeviceIdList.size());
		   }
		}
	   }
	}
   }

   /**
    * 开柜,开柜子获得reader的标识
    *
    * @param position
    * @param mTbaseDevices
    */
   public void openDoor(
	   int position, List<BoxSizeBean.DevicesBean> mTbaseDevices) {
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();

	BoxSizeBean.DevicesBean devicesBean = mTbaseDevices.get(position);
	String mDeviceCode = devicesBean.getDeviceId();

	if (mReaderIdList != null) {
	   mReaderIdList.clear();
	} else {
	   mReaderIdList = new ArrayList<>();
	}
	LogUtils.i(TAG, " eth002DeviceIdList.size   " + eth002DeviceIdList.size());
	setReaderList(mDeviceCode);

	if (mTbaseDevices.size() > 1 && eth002DeviceIdList.size() > 1) {
	   if (position == 0) {//第一个为全部开柜
		LogUtils.i(TAG, " position   " + position);
		initCallBack();
		for (int i = 0; i < eth002DeviceIdList.size(); i++) {
		   LogUtils.i(TAG,
				  " eth002DeviceIdList.get(i)   " + (String) eth002DeviceIdList.get(i));
		   DeviceManager.getInstance().OpenDoor((String) eth002DeviceIdList.get(i));
		}
	   } else {
		LogUtils.i(TAG, " DDD  mDeviceCode 1   " + mDeviceCode);
		queryDoorId(mDeviceCode);
	   }
	} else {
	   LogUtils.i(TAG, " DDD  mDeviceCode 2   " + mDeviceCode);
	   queryDoorId(mDeviceCode);
	}

   }

   /**
    * 获取设备门锁ID，并开柜
    */
   private void queryDoorId(String mDeviceCode) {
	LogUtils.i(TAG, " eth002DeviceIdList.size  " +  eth002DeviceIdList.size());
	for (int i = 0; i < eth002DeviceIdList.size(); i++) {
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    UHF_TYPE).find(BoxIdBean.class);
	   LogUtils.i(TAG, " boxIdBeans.size  " +  boxIdBeans.size());
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		if (device_id.equals(eth002DeviceIdList.get(i))) {
		   LogUtils.i(TAG,
				  " eth002DeviceIdList.get(i)   " + (String) eth002DeviceIdList.get(i));
		   DeviceManager.getInstance().OpenDoor((String) eth002DeviceIdList.get(i));
		   EventBusUtils.post(new Event.EventBoolean(true, (String) eth002DeviceIdList.get(i)));
		}
	   }
	}
   }

   /**
    * 正式开始扫描
    *
    * @param deviceIndentify
    */
   private void startScan(String deviceIndentify) {
	LogUtils.i(TAG, "startScan   ");
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	EventBusUtils.postSticky(new Event.EventLoading(true));
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   //	   if (sReaderType.equals(READER_2)) {
	   //		new Thread() {
	   //		   public void run() {
	   //			for (BoxIdBean deviceid : deviceBean) {
	   //			   String device_id = deviceid.getDevice_id();
	   //			   int i = DeviceManager.getInstance().StartUhfScan(device_id, 3000);
	   //			   LogUtils.i(TAG, "开始扫描了状态 罗丹贝尔   " + i + "    " + device_id);
	   //			   try {
	   //				Thread.sleep(3000);
	   //			   } catch (InterruptedException e) {
	   //				e.printStackTrace();
	   //			   }
	   //			}
	   //		   }
	   //		}.start();
	   //	   } else {
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
		if (i==1){
		   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
		  DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
		}
		if (i == 2) {
		   DeviceManager.getInstance().StopUhfScan(device_id);
		   DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
		}
		LogUtils.i(TAG, "开始扫描了状态    " + i + "    " + device_id);
	   }
	   //	   }
	}
   }
}
