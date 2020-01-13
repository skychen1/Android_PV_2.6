package high.rivamed.myapplication.devices;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.rivamed.FingerCallback;
import com.rivamed.FingerManager;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libconsumables.callback.ConsumableCallBack;
import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.service.ScanService;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.activity.LoginActivity.mConfigType;
import static high.rivamed.myapplication.activity.LoginActivity.mConfigType043;
import static high.rivamed.myapplication.activity.LoginActivity.mConfigType044;
import static high.rivamed.myapplication.activity.LoginActivity.mConfigType045;
import static high.rivamed.myapplication.activity.LoginActivity.sTCstConfigVos;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.base.App.mAppContext;
import static high.rivamed.myapplication.cont.Constants.CONFIG_043;
import static high.rivamed.myapplication.cont.Constants.CONFIG_044;
import static high.rivamed.myapplication.cont.Constants.CONFIG_045;
import static high.rivamed.myapplication.cont.Constants.CONSUMABLE_TYPE;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.fragment.TimelyAllFrag.mTimelyOnResume;
import static high.rivamed.myapplication.utils.DevicesUtils.getDoorStatus;
import static high.rivamed.myapplication.utils.LoginUtils.getEpcFilte;
import static high.rivamed.myapplication.utils.LyDateUtils.initReaderUtil;
import static high.rivamed.myapplication.utils.LyDateUtils.stopScan;
import static high.rivamed.myapplication.utils.StringUtils.getStringType;

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
   public static        List<String>      mBomDoorDeviceIdList;
  public static Gson                      mGson;

   public static AllDeviceCallBack getInstance() {
	//	sReaderType = UIUtils.getConfigReaderType(UIUtils.getContext(), CONFIG_000);
	if (instances == null) {
	   synchronized (NetRequest.class) {
		if (instances == null) {
		   mGson = new Gson();
		   instances = new AllDeviceCallBack();
		   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
		   Log.i(TAG, "mReaderDeviceId    " + mReaderDeviceId.size());
		   mBomDoorDeviceIdList = DevicesUtils.getBomDeviceId();
		   mEthDeviceIdBack = new ArrayList<>();
		   mEthDeviceIdBack3 = new ArrayList<>();
		   mEthDeviceIdBack2 = new ArrayList<>();

		}
	   }
	}
	return instances;
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
			LogUtils.i(TAG, " mBomDoorDeviceIdList.size   " + mBomDoorDeviceIdList.size());
		   }
		}
	   }
	}
   }

   /**
    * 开柜,开柜子获得reader的标识
    *
    * @param deviceId
    * @param mTbaseDevices
    */
   public void openDoor(
	   String deviceId, List<BoxSizeBean.DevicesBean> mTbaseDevices) {
	mBomDoorDeviceIdList = DevicesUtils.getBomDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	stopScan();
//	BoxSizeBean.DevicesBean devicesBean = mTbaseDevices.get(position);
//	String mDeviceCode = devicesBean.getDeviceId();

	if (mReaderIdList != null) {
	   mReaderIdList.clear();
	} else {
	   mReaderIdList = new ArrayList<>();
	}
	LogUtils.i(TAG, " mBomDoorDeviceIdList.size   " + mBomDoorDeviceIdList.size());
	setReaderList(deviceId);

	if (mTbaseDevices.size() > 1 && mBomDoorDeviceIdList.size() >= 1) {
	   if (deviceId.equals("")) {//第一个为全部开柜
		//		initCallBack();
		for (int i = 0; i < mBomDoorDeviceIdList.size(); i++) {
		   LogUtils.i(TAG, " mBomDoorDeviceIdList.get(i)   " + (String) mBomDoorDeviceIdList.get(i));
		   List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", mBomDoorDeviceIdList.get(i),
									    CONSUMABLE_TYPE).find(BoxIdBean.class);
		   if (boxIdBeans.size()>1){
			ConsumableManager.getManager().openDoor((String) mBomDoorDeviceIdList.get(i));
			Log.i("ssffff", "开门全部ddddd");
		   }else {
			ConsumableManager.getManager().openDoor((String) mBomDoorDeviceIdList.get(i),0);
			Log.i("ssffff", "开门1个");
		   }

		}
	   } else {
//		LogUtils.i(TAG, " DDD  mDeviceCode 1   " + mDeviceCode);
		queryDoorId(deviceId);
	   }
	} else {
//	   LogUtils.i(TAG, " DDD  mDeviceCode 2   " + mDeviceCode);
	   queryDoorId(deviceId);
	}

   }

   /**
    * 获取设备门锁ID，并开柜
    */
   private void queryDoorId(String mDeviceCode) {
	Log.i("ffaer", " mDeviceCode  " + mDeviceCode);
	LogUtils.i(TAG, " mBomDoorDeviceIdList.size  " + mBomDoorDeviceIdList.size());
	for (int i = 0; i < mBomDoorDeviceIdList.size(); i++) {
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    CONSUMABLE_TYPE).find(BoxIdBean.class);
	   Log.i("ffaer", " boxIdBeans  " + boxIdBeans);
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		Log.i("ffaer", " device_id  " + device_id);
		if (device_id.equals(mBomDoorDeviceIdList.get(i))) {
		   Log.i("ffaer", " mBomDoorDeviceIdList.get(i)  " + mBomDoorDeviceIdList.get(i));
			if ( boxIdBean.getCabinetType() != null) {
				if (boxIdBean.getCabinetType().equals("1") || boxIdBean.getCabinetType().equals("0")) {//上柜或者单柜   J24
					ConsumableManager.getManager().openDoor((String) mBomDoorDeviceIdList.get(i), 0);
				} else if (boxIdBean.getCabinetType().equals("2")) {                                                  //J25
					ConsumableManager.getManager().openDoor((String) mBomDoorDeviceIdList.get(i), 1);
				}
			}
		}
	   }
	}
   }

   /**
    * 开灯
    */
   public void openLightStart(){
	List<String> bomDeviceId = DevicesUtils.getBomDeviceId();
	for (String s : bomDeviceId) {
	   ConsumableManager.getManager().openLight(s);
	}
   }

   /**
    * 关灯
    */
   public void closeLightStart(){
	List<String> bomDeviceId = DevicesUtils.getBomDeviceId();
	for (String s : bomDeviceId) {
	   ConsumableManager.getManager().closeLight(s);
	}
   }

   /**
    * 检测灯
    */
   public void StateLightStart(){
	List<String> bomDeviceId = DevicesUtils.getBomDeviceId();
	for (String s : bomDeviceId) {
	   ConsumableManager.getManager().checkLightState(s);
	}
   }
   /**
    * 正式开始扫描
    *
    * @param deviceIndentify
    */
   public void startScan(String deviceIndentify,int which) {
	LogUtils.i(TAG, "deviceIndentify    " + deviceIndentify + "    ");
	BoxIdBean boxIdBean = LitePal.where("device_id = ? and name = ?", deviceIndentify,
							CONSUMABLE_TYPE)
		.findFirst(BoxIdBean.class);

	String box_id = boxIdBean.getBox_id();
	BoxIdBean deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		.findFirst(BoxIdBean.class);

	String device_id = deviceBean.getDevice_id();
	int i = ReaderManager.getManager().startScan(device_id, READER_TIME);
	if (i == 1) {
	   ReaderManager.getManager().restDevice(device_id);
	   mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	   ReaderManager.getManager().startScan(device_id, READER_TIME);
	}

	if (i == 2) {
//	   ReaderManager.getManager().stopScan(device_id);
//	   ReaderManager.getManager().startScan(device_id, READER_TIME);
	}
	LogUtils.i(TAG, "开始扫描了状态    " + i + "    " + device_id);

   }

   public void initCallBack() {

	initReader();
	initBom();
	initFinger();
	initIC();
   }

   /**
    * IC卡
    */
   private void initIC() {
	IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {

	   }

	   @Override
	   public void onReceiveCardNum(String cardNo) {
		int appSatus = App.getInstance().getAppSatus();
		Log.i("appSatus","appSatus   "+appSatus);
		if (appSatus!=3){
		   if (!UIUtils.isFastDoubleClick()) {
			mConfigType = 1;//IC卡
			EventBusUtils.post(new Event.EventICAndFinger(cardNo, mConfigType));
		   }
		}else {
		   Intent intent = new Intent(mAppContext, ScanService.class);
		   mAppContext.stopService(intent);
		}
	   }
	});
   }

   /**
    * 指纹仪
    */
   private void initFinger() {
	FingerManager.getManager().registerCallback(new FingerCallback() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {
		Log.i("appSatus","isConnectdeviceId     "+isConnect+deviceId);
	   }

	   @Override
	   public void onFingerFeatures(String deviceId, String features) {
		int appSatus = App.getInstance().getAppSatus();
		Log.i("appSatus","appSatus   "+appSatus);
		if (appSatus!=3){
		   if (!UIUtils.isFastDoubleClick()) {
			mConfigType = 2;//指纹登录
			EventBusUtils.post(new Event.EventICAndFinger(features, mConfigType));
		   }
		}else {
		   Intent intent = new Intent(mAppContext, ScanService.class);
		   mAppContext.stopService(intent);
		}
	   }

	   @Override
	   public void onRegisterResult(String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
		Log.i("appSatus","deviceId   "+deviceId);
		EventBusUtils.post(new Event.EventFingerRegEnter(code,features,msg));
//		appendLog("设备：：" + deviceId + "注册结果码是：：" + code + "\n>>>>>>>" + msg
//			    + "\n指纹照片数据：：" + (fingerPicPath == null ? 0 : fingerPicPath.size()) + "\n特征值是：：：" + features);
		//收到注册结果标识注册完成就开启读取

	   }

	   @Override
	   public void onFingerUp(String deviceId) {
		Log.i("appSatus","请抬起手指   "+deviceId);
//		appendLog("设备：：" + deviceId + "请抬起手指：");
		EventBusUtils.post(new Event.EventFingerReg(0));
	   }

	   @Override
	   public void onRegisterTimeLeft(String deviceId, long time) {
		Log.i("appSatus","请抬起手指   "+time);
//		setLog("设备：：" + deviceId + "剩余注册时间：：" + time + "\n");
		EventBusUtils.post(new Event.EventFingerTime(time));
		EventBusUtils.post(new Event.EventFingerReg(1));
	   }
	});
   }

   /**
    * 主板回调
    */
   private void initBom() {
	ConsumableManager.getManager().registerCallback(new ConsumableCallBack() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {

	   }

	   @Override
	   public void onOpenDoor(String deviceId, int which, boolean isSuccess) {
//		appendLog("设备：：" + deviceId + "开锁：：：" + which + ":::的结果是：：：" + isSuccess);
		Log.i("onDoorState","onOpenDoor 开锁：：" + deviceId + "状态：：：" + which + ":::的结果是：：：" + isSuccess);
		if (isSuccess) {
		   Log.i("outtccc", "柜门已开    " );
		   EventBusUtils.post(new Event.PopupEvent(true, "柜门已开", deviceId+which));
		}
		if (mEthDeviceIdBack.size() > 0) {
		   if (!getStringType(mEthDeviceIdBack,deviceId+which)){
			mEthDeviceIdBack.add(deviceId+which);
		   }
		} else {
		   mEthDeviceIdBack.add(deviceId+which);
		}
		mEthDeviceIdBack2.clear();
		mEthDeviceIdBack2.addAll(mEthDeviceIdBack);
		mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
		getDoorStatus();
	   }

	   @Override
	   public void onCloseDoor(String deviceId, int which, boolean isSuccess) {

		Log.i("onDoorState","onCloseDoor设备：：" + deviceId + "状态：：：" + which + ":::的结果是：：：" + isSuccess);
		EventBusUtils.post(new Event.PopupEvent(false, "关闭", deviceId+which));
		LogUtils.i("onDoorState", "onDoorClosed  " + mEthDeviceIdBack2.size() + "   " + mEthDeviceIdBack.size());
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
		   startScan(deviceId,which);
		} else {//正常开门
		   for (int i = 0; i < mEthDeviceIdBack2.size(); i++) {
			if (mEthDeviceIdBack2.get(i).equals(deviceId+which)) {
			   mEthDeviceIdBack2.remove(i);
			}
		   }
		}
		getDoorStatus();
	   }

	   @Override
	   public void onDoorState(String deviceId, int which, boolean state) {
		Log.i("ssffff","onDoorState设备：：" + deviceId + "检测锁的状态：：：" + which + ":::的结果是：：：" + state);
		EventBusUtils.postSticky(new Event.EventDoorStatus(deviceId+which, state));
	   }

	   @Override
	   public void onOpenLight(String deviceId, int which, boolean isSuccess) {
		Log.i("onDoorState","设备：：" + deviceId + "开灯：：：" + which + ":::的结果是：：：" + isSuccess);
	   }

	   @Override
	   public void onCloseLight(String deviceId, int which, boolean isSuccess) {
		Log.i("onDoorState","设备：：" + deviceId + "关灯：：：" + which + ":::的结果是：：：" + isSuccess);
	   }

	   @Override
	   public void onLightState(String deviceId, int which, boolean state) {
		if (state){
		   EventBusUtils.post(new Event.EventLightCloss(true));
		}
		Log.i("onDoorState","设备：：" + deviceId + "检测灯：：：" + which + ":::的结果是：：：" + state);
	   }


	   @Override
	   public void onFirmwareVersion(String deviceId, String version) {
//		appendLog("设备：：" + deviceId + "获取到版本号：：：" + version);
	   }

	   @Override
	   public void onNeedUpdateFile(String deviceId) {
//		appendLog("设备：：" + deviceId + "需要升级文件！！！！");
//		String filePath = FilesUtils.getFilePath(mContext) + "/boot.bin";
//		com.rivamed.libdevicesbase.utils.LogUtils.e("文件路径是：：：" + filePath);
//		int i = ConsumableManager.getManager().sendUpdateFile(deviceId, filePath);
//		if (i != FunctionCode.SUCCESS) {
//		   com.rivamed.libdevicesbase.utils.LogUtils.e("发送升级文件出错" + i);
//		}
	   }

	   @Override
	   public void onUpdateProgress(String deviceId, int percent) {
//		appendLog("设备：：" + deviceId + "已升级的进度：：：" + percent + "%");
	   }

	   @Override
	   public void onUpdateResult(String deviceId, boolean isSuccess) {
//		appendLog("设备：：" + deviceId + "升级" + (isSuccess ? "成功！！！！！" : "失败！！！！"));
	   }
	});
   }

   /**
    * 初始化罗丹贝尔回调
    */
   public void initReader() {
	//设置回调
	ReaderManager.getManager().registerCallback(new ReaderCallback() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {
		if (isConnect) {
		   EventBusUtils.post(new Event.ConnectReaderState(true));
		} else {
		   EventBusUtils.post(new Event.ConnectReaderState(false));
		   initReaderUtil();
		}
	   }

	   @Override
	   public void onScanResult(String deviceId, Map<String, List<EpcInfo>> result) {
		Log.i("resultddd","result：：onScanResult" );
		List<String> epcs = new ArrayList<>();
		for (Map.Entry<String, List<EpcInfo>> v : result.entrySet()) {
		   String epc = filteListEpc(v.getKey());
		   if (epc!=null){
			epcs.add(epc);
		   }
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
		if (result == null || result.size() == 0) {
		   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, "0"));
		}
		if (result.size() >= 1) {
		   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, "-1"));
		}
	   }

	   @Override
	   public void onScanNewEpc(String deviceId, String epc, int ant) {
		if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0 &&
		    !mTimelyOnResume) {//强开
		   //		   EventBusUtils.post(new Event.EventOneEpcStrongOpenDeviceCallBack(deviceId, epc));
		} else {
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
	   public void onGetPower(String deviceId, int[] power) {

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
	if (mConfigType043 && !mConfigType044 && !mConfigType045) {//屏蔽位数
	   ConfigBean.ThingConfigVosBean epcFilte = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   int integer = Integer.parseInt(epcFilte.getValue());
	   if (epc.length() == integer) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	} else if (mConfigType043 && mConfigType044 && !mConfigType045) {//屏蔽位数和前X位
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
	} else if (mConfigType043 && mConfigType044 && mConfigType045) {//屏蔽位数屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);

	   try {
		int integer = Integer.parseInt(epc43.getValue());
		String value44 = epc44.getValue();
		String value45 = epc45.getValue();

		if (epc.length() == integer) {
		   if (epc.startsWith(value44)&&epc.endsWith(value45)){
		   }else {
			EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
		   }
		}
	   } catch (NumberFormatException e) {
		e.printStackTrace();
	   }
	} else if (mConfigType043 && !mConfigType044 && mConfigType045) {//屏蔽位数和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value45 = epc45.getValue();

	   if (epc.length() == integer && !epc.endsWith(value45)) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	} else if (!mConfigType043 && mConfigType044 && mConfigType045) {//屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   String value44 = epc44.getValue();
	   String value45 = epc45.getValue();
	   if (epc.startsWith(value44) && epc.endsWith(value45)) {
	   }else {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	} else if (!mConfigType043 && mConfigType044 && !mConfigType045){
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   String value44 = epc44.getValue();
	   if (!epc.startsWith(value44)) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	}else if (!mConfigType043 && !mConfigType044 && mConfigType045){
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   String value45 = epc45.getValue();
	   if (!epc.endsWith(value45)) {
		EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	   }
	}else {
	   EventBusUtils.post(new Event.EventOneEpcDeviceCallBack(deviceId, epc));
	}
   }

   /**
    * 屏蔽List里面EPC
    *
    * @param epc
    */
   private String filteListEpc(String epc) {
	if (mConfigType043 && !mConfigType044 && !mConfigType045) {//屏蔽位数
	   ConfigBean.ThingConfigVosBean epcFilte = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   int integer = Integer.parseInt(epcFilte.getValue());
	   if (epc.length() == integer) {
		return epc;
	   }
	} else if (mConfigType043 && mConfigType044 && !mConfigType045) {//屏蔽位数和前X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value44 = epc44.getValue();
	   if (epc.length() == integer && !epc.startsWith(value44)) {
		return epc;
	   }
	} else if (mConfigType043 && mConfigType044 && mConfigType045) {//屏蔽位数屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value44 = epc44.getValue();
	   String value45 = epc45.getValue();
	   if (epc.length() == integer) {
		if (epc.startsWith(value44)&&epc.endsWith(value45)){
		}else {
		   return epc;
		}
	   }
	} else if (mConfigType043 && !mConfigType044 && mConfigType045) {//屏蔽位数和后X位
	   ConfigBean.ThingConfigVosBean epc43 = getEpcFilte(sTCstConfigVos, CONFIG_043);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   int integer = Integer.parseInt(epc43.getValue());
	   String value45 = epc45.getValue();
	   if (epc.length() == integer && !epc.endsWith(value45)) {
		return epc;
	   }
	} else if (!mConfigType043 && mConfigType044 && mConfigType045) {//屏蔽前X位和后X位
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   String value44 = epc44.getValue();
	   String value45 = epc45.getValue();
	   if (epc.startsWith(value44) && epc.endsWith(value45)) {
	   }else {
		return epc;
	   }
	}else if (!mConfigType043 && mConfigType044 && !mConfigType045){
	   ConfigBean.ThingConfigVosBean epc44 = getEpcFilte(sTCstConfigVos, CONFIG_044);
	   String value44 = epc44.getValue();
	   if (!epc.startsWith(value44)) {
		return epc;
	   }
	}else if (!mConfigType043 && !mConfigType044 && mConfigType045) {
	   ConfigBean.ThingConfigVosBean epc45 = getEpcFilte(sTCstConfigVos, CONFIG_045);
	   String value45 = epc45.getValue();
	   if (!epc.endsWith(value45)) {
		return epc;
	   }
	}else if (!mConfigType043 && !mConfigType044 && !mConfigType045){
	   return epc;
	}
	return null;
   }

}
