package high.rivamed.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ruihua.reader.net.bean.EpcInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.entity.Inventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.RxUtils;
import high.rivamed.myapplication.utils.StringUtils;

import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;
import static high.rivamed.myapplication.utils.LyDateUtils.startScan;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/25 14:16
 * 描述:        后台强开控制扫描提交数据
 * 包名:        high.rivamed.myapplication.service
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ScanService2 extends Service {

   String TAG = "ScanService";
   private List<BoxIdBean> mBoxIdBeans;
   int k    = 0;
   int size = 0;
   private       Map<String, List<EpcInfo>> mEPCDate        = new TreeMap<>();
   private       Map<String, String>        mEPCDatess      = new TreeMap<>();
   public static boolean                    mDoorStatusType = false;//false 没关  true已经关了
   private       List<String>               mDeviceSizeList = new ArrayList<>();
   private ArrayList<String> mListDevices;
   private ArrayList<String> mEthDevices = new ArrayList<>();
   private RxUtils.BaseEpcObservable mObs;
   public List<InventoryVo> mBoxInventoryVos = new ArrayList<>(); //在柜epc信息

   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventDoorStatus(Event.EventDoorStatus event) {
	//	Log.i("FAFAS", "EventDoorStatus   "+event.type);
	if (event.type) {//门没关
	   mDoorStatusType = false;
	   mEthDevices.clear();
	   if (mListDevices != null) {
		mListDevices.clear();
	   }

	   return;
	}
	if (!event.type) {//门关了
	   for (Object o : mDeviceSizeList) {
		String s = (String) o;
		if (s.equals(event.id) && !event.type) {
		   mEthDevices.add(s);
		   mListDevices = StringUtils.removeDuplicteUsers(mEthDevices);
		}
	   }
	   if (mDeviceSizeList.size() == mListDevices.size()) {
		mDoorStatusType = true;
		mListDevices.clear();
		mEthDevices.clear();
	   }
	}
   }

   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartScan(Event.EventStartScan event) {
	if (event.type) {
	   startScan(mBoxInventoryVos, mObs, event.deviceIndentify);
	}
   }
   //   /**
   //    * 扫描后EPC准备传值(定时)
   //    * @param event
   //    */
   //   @Subscribe(threadMode = ThreadMode.MAIN)
   //   public void onCallBackEvent(Event.EventStrongOpenDeviceCallBack event) {
   //
   //	List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
   //		.find(BoxIdBean.class);
   //	for (BoxIdBean boxIdBean : boxIdBeanss) {
   //	   String box_id = boxIdBean.getBox_id();
   //	   if (box_id != null) {
   //		size++;
   //		List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
   //									   READER_TYPE).find(BoxIdBean.class);
   //		if (boxIdBeansss.size() > 1) {
   //		   for (BoxIdBean BoxIdBean : boxIdBeansss) {
   //			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
   //			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
   //			   mEPCDate.putAll(event.epcs);
   //			   k++;
   //			   LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
   //			}
   //		   }
   //		   LogUtils.i(TAG, "mEPCDate  k  " + k);
   //		   if (k == boxIdBeansss.size()) {
   //			k = 0;
   //			if (mEPCDate.size() == 0) {
   //			   mEPCDatess.put("", box_id);//没有空格
   //			}
   //			for (Map.Entry<String, List<EpcInfo>> v : mEPCDate.entrySet()) {
   //			   mEPCDatess.put(v.getKey(), box_id);
   //			}
   //			LogUtils.i(TAG, "mEPCDates.mEPCDates()多reader  " + mEPCDatess.size());
   //		   } else {
   //			return;
   //		   }
   //		} else {
   //		   if (event.epcs.size() == 0) {
   //			mEPCDatess.put(" ", box_id);//1个空格
   //		   }
   //		   for (Map.Entry<String, List<EpcInfo>> v : event.epcs.entrySet()) {
   //			mEPCDatess.put(v.getKey(), box_id);
   //		   }
   //		   LogUtils.i(TAG, "mEPCDates.mEPCDates()单reader  " + mEPCDatess.size());
   //		}
   //	   }
   //	}
   //	List<BoxIdBean> boxIdBeansss = LitePal.where("name = ?", READER_TYPE).find(BoxIdBean.class);
   //	if (boxIdBeansss.size() != size) {
   //	   return;
   //	}
   //	size = 0;
   //	LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size());
   //	putEPC(mEPCDatess);
   //   }

   /**
    * EPC扫描返回数据（单个返回）
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventOneEpcStrongOpenDeviceCallBack event) {
	if (getVosType(mBoxInventoryVos, event.epc)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		if (mBoxInventoryVos.get(i).getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   mBoxInventoryVos.remove(i);
		}
	   }

	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理
	   mObs.getScanEpc(event.deviceId, event.epc);
	}
   }

//   /**
//    * 提交epc数据
//    */
//   private void putEPC() {
//
//		if (v.getKey() != null && !v.getKey().toString().trim().equals("") &&
//		    v.getValue() != null) {
//		   InventoryVo inventory = new InventoryVo();
//		   inventory.setEpc(v.getKey());
//		   inventory.setDeviceId(v.getValue());
//		   inventory.setRenewTime(getDates());
//		   inventory.setStatus("2");//入柜
//		   inventory.setOperationStatus(99);
//		   inventory.save();
//		}
//
//
//	for (InventoryVo s : mBoxInventoryVos) {
//	   ContentValues values = new ContentValues();
//	   values.put("status", "3");//出柜
//	   values.put("operationstatus", 98);
//	   values.put("renewtime", getDates());
//	   //	   values.put("accountid", SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
//	   //	   values.put("username", SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
//	   LitePal.updateAll(InventoryVo.class, values, "epc = ?", s.getEpc());
//	}
//   }

   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
	return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
	return START_STICKY;
   }

   @Override
   public void onCreate() {
	super.onCreate();
	List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean idBean : boxIdBeans) {
	   mDeviceSizeList.add(idBean.getDevice_id());
	}
	new Thread(() -> {
	   AllDeviceCallBack.getInstance().initCallBack();
	   initRxJavaSearch();
	}).start();

	EventBusUtils.register(this);
   }

   /**
    * 500ms进行网络请求一次RXJAVA的处理
    */
   private void initRxJavaSearch() {
	mObs = new RxUtils.BaseEpcObservable() {};
	RxUtils.getInstance().setEpcResultListener(mObs, new RxUtils.EpcDebounceResultListener() {
	   @Override
	   public void goEpcSearch(List<DeviceInventoryVo> vos) {
		new Thread(() -> setScanDateInBoxVo(vos)).start();
	   }
	});
   }

   /**
    * 放入的EPC
    * @param vos
    */
   private void setScanDateInBoxVo(List<DeviceInventoryVo> vos) {
	if (mBoxInventoryVos.size() > 0) {
	   for (int x = 0; x < vos.size(); x++) {
		String deviceId = vos.get(x).getDeviceId();
		for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		   String id = mBoxInventoryVos.get(i).getDeviceId();
		   if (id.equals(deviceId)) {
			setSaveVo(vos, x, deviceId);
		   }
		}
	   }
	} else {
	   for (int x = 0; x < vos.size(); x++) {
		setSaveVo(vos, x, vos.get(x).getDeviceId());
	   }
	}
   }

   private void setSaveVo(List<DeviceInventoryVo> vos, int x, String deviceId) {
	List<Inventory> list = vos.get(x).getInventories();
	for (int y = 0; y < list.size(); y++) {
	   if (!getVosType(mBoxInventoryVos, list.get(y).getEpc())) {
		InventoryVo inventory = new InventoryVo();
		inventory.setEpc(list.get(y).getEpc());
		inventory.setDeviceId(deviceId);
		inventory.setRenewTime(getDates());
		inventory.setStatus("2");//入柜
		inventory.setOperationStatus(99);
		boolean save = inventory.save();
		LogUtils.i(TAG,"ScanService 存入入柜数据  "+save);
	   }
	}
   }

   @Override
   public void onDestroy() {
	EventBusUtils.unregister(this);
	super.onDestroy();

   }
}
