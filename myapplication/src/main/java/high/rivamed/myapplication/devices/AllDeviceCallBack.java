package high.rivamed.myapplication.devices;

import android.util.Log;

import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.net.bean.AntInfo;
import com.ruihua.reader.net.bean.EpcInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.rivamed.Eth002Manager;
import cn.rivamed.callback.Eth002CallBack;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.activity.LoginActivity.mConfigType;
import static high.rivamed.myapplication.activity.LoginActivity.sTCstConfigVos;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.CONFIG_043;
import static high.rivamed.myapplication.cont.Constants.CONFIG_044;
import static high.rivamed.myapplication.cont.Constants.CONFIG_045;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.fragment.TimelyAllFrag.mTimelyOnResume;
import static high.rivamed.myapplication.utils.DevicesUtils.getDoorStatus;
import static high.rivamed.myapplication.utils.LoginUtils.getEpcFilte;
import static high.rivamed.myapplication.utils.LyDateUtils.stopScan;

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

   private static final String            TAG = "AllDeviceCallBack";
   // 设置本类为单例模式
   private static       AllDeviceCallBack instances;
   public static        ArrayList<String> mEthDeviceIdBack;
   public static        ArrayList<String> mEthDeviceIdBack2;
   public static        ArrayList<String> mEthDeviceIdBack3;
   public static        List<String>      mReaderIdList;
   public static        List<String>      mReaderDeviceId;
   public static        List<String>      eth002DeviceIdList;

   public static AllDeviceCallBack getInstance() {
	//	sReaderType = UIUtils.getConfigReaderType(UIUtils.getContext(), CONFIG_000);
	if (instances == null) {
	   synchronized (NetRequest.class) {
		if (instances == null) {
		   instances = new AllDeviceCallBack();
		   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
		   Log.i(TAG, "mReaderDeviceId    " + mReaderDeviceId.size());
		   eth002DeviceIdList = DevicesUtils.getEthDeviceId();
		   mEthDeviceIdBack = new ArrayList<>();
		   mEthDeviceIdBack3 = new ArrayList<>();
		   mEthDeviceIdBack2 = new ArrayList<>();

		}
	   }
	}
	return instances;
   }

   //   /**
   //    * 强开的扫描
   //    */
   //   private void StrongOpenScanStart() {
   //	List<String> deviceId = DevicesUtils.getReaderDeviceId();
   //	for (String s : deviceId) {//记录强开后未关门
   //	   int i =  ReaderManager.getManager().startScan(s, READER_TIME);
   //	}
   //   }

   //   /**
   //    * 柜门检测关闭后进行扫描
   //    *
   //    * @param deviceIndentify
   //    */
   //   private void clossDoorStartScan(String deviceIndentify) {
   //	if (mReaderIdList.size() == 0) {
   //	   new Thread(new Runnable() {
   //		@Override
   //		public void run() {
   //		   try {
   //			Thread.currentThread().sleep(4000);
   //			setReaderList(null);
   //			if (mReaderIdList.size() == 0) {
   //			   LogUtils.i(TAG, "走了");
   //			   mEthDeviceIdBack.clear();
   //			   EventBusUtils.postSticky(new Event.EventToast("reader未启动，请重新开关柜门"));
   //			} else {
   //			   startScan(deviceIndentify);
   //			}
   //		   } catch (Exception e) {
   //			e.printStackTrace();
   //		   }
   //		}
   //
   //	   }).start();
   //
   //	} else {
   //	   startScan(deviceIndentify);
   //	}
   //   }

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
	stopScan();
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
		//		initCallBack();
		for (int i = 0; i < eth002DeviceIdList.size(); i++) {
		   LogUtils.i(TAG,
				  " eth002DeviceIdList.get(i)   " + (String) eth002DeviceIdList.get(i));
		   Eth002Manager.getEth002Manager().openDoor((String) eth002DeviceIdList.get(i));
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
	LogUtils.i(TAG, " eth002DeviceIdList.size  " + eth002DeviceIdList.size());
	for (int i = 0; i < eth002DeviceIdList.size(); i++) {
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    UHF_TYPE).find(BoxIdBean.class);
	   LogUtils.i(TAG, " boxIdBeans.size  " + boxIdBeans.size());
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		if (device_id.equals(eth002DeviceIdList.get(i))) {
		   LogUtils.i(TAG,
				  " eth002DeviceIdList.get(i)   " + (String) eth002DeviceIdList.get(i));
		   Eth002Manager.getEth002Manager().openDoor((String) eth002DeviceIdList.get(i));
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
   public void startScan(String deviceIndentify) {
	LogUtils.i(TAG, "deviceIndentify    " + deviceIndentify + "    ");
	BoxIdBean boxIdBean = LitePal.where("device_id = ? and name = ?", deviceIndentify, UHF_TYPE)
		.findFirst(BoxIdBean.class);

	String box_id = boxIdBean.getBox_id();
	BoxIdBean deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		.findFirst(BoxIdBean.class);

	String device_id = deviceBean.getDevice_id();
	int i = ReaderManager.getManager().startScan(device_id, READER_TIME);
	if (i == 1) {
	   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	   ReaderManager.getManager().startScan(device_id, READER_TIME);
	}
	if (i == 2) {
	   ReaderManager.getManager().stopScan(device_id);
	   ReaderManager.getManager().startScan(device_id, READER_TIME);
	}
	LogUtils.i(TAG, "开始扫描了状态    " + i + "    " + device_id);

   }

   public void initCallBack() {
	initReader();
	initEth002();
   }

   /**
    * 初始化罗丹贝尔回调
    */
   public void initReader() {
	//设置回调
	ReaderManager.getManager().registerCallback(new ReaderCallback() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {

	   }

	   @Override
	   public void onScanResult(String deviceId, Map<String, List<EpcInfo>> result) {
		List<String> epcs = new ArrayList<>();
		for (Map.Entry<String, List<EpcInfo>> v : result.entrySet()) {
		   String epc = filteListEpc(v.getKey());
		   epcs.add(epc);
		}

		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0 &&
		    !mTimelyOnResume) {//强开
		   EventBusUtils.post(new Event.EventStrongOpenDeviceCallBack(deviceId, epcs));
		} else {
		   EventBusUtils.post(new Event.EventDeviceCallBack(deviceId, epcs));
		}
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
		} else {
		   EventBusUtils.postSticky(new Event.EventLoading(false));
		}
		if (result==null||result.size()==0){
		   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, "0"));
		}
	   }

	   @Override
	   public void onScanNewEpc(String deviceId, String epc, int ant) {
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0 &&
		    !mTimelyOnResume) {//强开
		   //		   EventBusUtils.post(new Event.EventOneEpcStrongOpenDeviceCallBack(deviceId, epc));
		} else {
		   Log.i("FAFAFAFAD", "FDFDF   " + deviceId + "      EPC    " + epc);
		   filteEpc(deviceId, epc);
		}
	   }

	   @Override
	   public void onSetPower(String deviceId, boolean success) {

	   }

	   @Override
	   public void onGetPower(String deviceId, int power) {
	   }

	   @Override
	   public void onGetFrequency(String deviceId, String frequency) {
	   }

	   @Override
	   public void onCheckAnt(String deviceId, List<AntInfo> ant) {
	   }

	   @Override
	   public void onLockOpen(String deviceId, boolean isSuccess) {
	   }

	   @Override
	   public void onLockClose(String deviceId, boolean isSuccess) {
	   }

	   @Override
	   public void onLightOpen(String deviceId, boolean isSuccess) {
	   }

	   @Override
	   public void onLightClose(String deviceId, boolean isSuccess) {
	   }

	   @Override
	   public void onLockState(String deviceId, boolean isOpened) {
	   }

	   @Override
	   public void onLightState(String deviceId, boolean isOpened) {
	   }
	});
   }

   /**
    * 屏蔽单个EPC
    *
    * @param deviceId
    * @param epc
    */
   private void filteEpc(String deviceId, String epc) {
	if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
	    !UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
	    !UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数
	   ConfigBean.ThingConfigVosBean epcFilte = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   int integer = Integer.parseInt(epcFilte.getValue());
	   if (epc.length() == integer) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	} else if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     !UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数和前X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   try {
		int integer = Integer.parseInt(epc43.getValue());
		String value44 = epc44.getValue();
		if (epc.length() == integer && !epc.startsWith(value44)) {
		   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
		}
	   } catch (Exception e) {
		e.printStackTrace();
	   }
	} else if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);

	   try {
		int integer = Integer.parseInt(epc43.getValue());
		String value44 = epc44.getValue();
		String value45 = epc45.getValue();
		Log.i("FAFAFAFAD", "integer   " + integer + "      value44    " + epc.startsWith(value44) + "      value45    " + value45);
		if (epc.length() == integer &&( !epc.startsWith(value44) ||!epc.endsWith(value45))) {
		   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
		}
	   } catch (NumberFormatException  e) {
		e.printStackTrace();
	   }

	} else if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     !UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value45 = epc45.getValue();
	   if (epc.length() == integer && !epc.endsWith(value45)) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	} else if (!UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   String value44 = epc44.getValue();
	   String value45 = epc45.getValue();
	   if (!epc.startsWith(value44) && !epc.endsWith(value45)) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	} else {
	   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	}
   }

   /**
    * 屏蔽List里面EPC
    *
    * @param epc
    */
   private String filteListEpc(String epc) {
	if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
	    !UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
	    !UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数
	   ConfigBean.ThingConfigVosBean epcFilte = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   int integer = Integer.parseInt(epcFilte.getValue());
	   if (epc.length() == integer) {
		return epc;
	   }
	} else if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     !UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数和前X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value44 = epc44.getValue();
	   if (epc.length() == integer && !epc.startsWith(value44)) {
		return epc;
	   }
	} else if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value44 = epc44.getValue();
	   String value45 = epc45.getValue();
	   if (epc.length() == integer && (!epc.startsWith(value44) || !epc.endsWith(value45))) {
		return epc;
	   }
	} else if (UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     !UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽位数和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value45 = epc45.getValue();
	   if (epc.length() == integer && !epc.endsWith(value45)) {
		return epc;
	   }
	} else if (!UIUtils.getConfigType(App.getAppContext(), CONFIG_043) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_044) &&
		     UIUtils.getConfigType(App.getAppContext(), CONFIG_045)) {//屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   String value44 = epc44.getValue();
	   String value45 = epc45.getValue();
	   if (!epc.startsWith(value44) && !epc.endsWith(value45)) {
		return epc;
	   }
	}
	return epc;
   }

   public void initEth002() {
	Eth002Manager.getEth002Manager().registerCallBack(new Eth002CallBack() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {

	   }

	   @Override
	   public void onFingerFea(String deviceId, String fingerFea) {
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mConfigType = 2;//指纹登录
		   EventBusUtils.post(new Event.EventICAndFinger(deviceId, fingerFea, mConfigType));

		}
	   }

	   @Override
	   public void onFingerRegExcuted(String deviceId, boolean success) {

	   }

	   @Override
	   public void onFingerRegisterRet(String deviceId, boolean success, String fingerData) {

	   }

	   @Override
	   public void onIDCard(String deviceId, String idCard) {
		Log.i("FADDDD", "deviceId" + deviceId);
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mConfigType = 1;//IC卡
		   EventBusUtils.post(new Event.EventICAndFinger(deviceId, idCard, mConfigType));
		}
	   }

	   @Override
	   public void onDoorOpened(String deviceIndentify, boolean success) {
		//目前设备监控开门success有可能出现错误   都设置成true
		getDoorStatus();
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
		if (success) {
		   EventBusUtils.post(new Event.PopupEvent(true, "柜门已开", deviceIndentify));
		}
	   }

	   @Override
	   public void onDoorClosed(String deviceIndentify, boolean success) {
		getDoorStatus();
		EventBusUtils.post(new Event.PopupEvent(false, "关闭", deviceIndentify));
		LogUtils.i(TAG, "onDoorClosed  " + mEthDeviceIdBack2.size() + "   " +
				    mEthDeviceIdBack.size());
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
		   startScan(deviceIndentify);
		} else {//正常开门
		   for (int i = 0; i < mEthDeviceIdBack2.size(); i++) {
			if (mEthDeviceIdBack2.get(i).equals(deviceIndentify)) {
			   mEthDeviceIdBack2.remove(i);
			}
		   }
		}
	   }

	   @Override
	   public void onDoorCheckedState(String deviceIndentify, boolean opened) {
		EventBusUtils.postSticky(new Event.EventDoorStatus(deviceIndentify, opened));
	   }
	});
   }
}
