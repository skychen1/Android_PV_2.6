package high.rivamed.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.RxUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;

import static high.rivamed.myapplication.base.App.getAppContext;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;
import static high.rivamed.myapplication.utils.LyDateUtils.setAllBoxVosDate;
import static high.rivamed.myapplication.utils.UnNetCstUtils.deleteVo;
import static high.rivamed.myapplication.utils.UnNetCstUtils.saveErrorVo;

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
public class ScanService extends Service {

   String TAG = "ScanService";
   private List<BoxIdBean> mBoxIdBeans;
   int k    = 0;
   int size = 0;
   private       List<String>        mEPCDate        = new ArrayList<>();
   private       Map<String, String> mEPCDatess      = new TreeMap<>();
   public static boolean             mDoorStatusType = false;//false 没关  true已经关了
   private       List<String>        mDeviceSizeList = new ArrayList<>();
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
    * 扫描后EPC准备传值(定时)
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventStrongOpenDeviceCallBack event) {
	mEPCDatess.clear();

	LogUtils.i(TAG, "EventStrongOpenDeviceCallBack    " + event.epcs.size());
	BoxIdBean boxIdBean = LitePal.where("device_id = ?", event.deviceId)
		.findFirst(BoxIdBean.class);
	String box_id = boxIdBean.getBox_id();
	if (box_id != null) {
	   if (event.epcs.size() == 0) {
		mEPCDatess.put(" ", box_id);//1个空格
	   }
	   for (String epc : event.epcs) {
		mEPCDatess.put(epc, box_id);
	   }
	}
	LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size());
	putEPC(mEPCDatess, box_id);
   }

   //   /**
   //    * 提交epc数据
   //    *
   //    * @param epcDatess
   //    */
   //   private void putEPC(Map<String, String> epcDatess,String boxid) {
   //	List<InventoryVo> mInVo = new ArrayList<>();
   //	List<InventoryVo> vos = setAllBoxVosDate(LitePal.findAll(InventoryVo.class), boxid);
   //	mInVo.addAll(vos);
   //	for (Map.Entry<String, String> v : epcDatess.entrySet()) {
   //	   InventoryVo vo = LitePal.where("epc = ?", v.getKey()).findFirst(InventoryVo.class);
   //	   if (vo != null) {
   //		mInVo.remove(vo);
   //	   } else {
   //		if (v.getKey() != null && !v.getKey().toString().trim().equals("") &&
   //		    v.getValue() != null) {
   //		   InventoryVo inventory = new InventoryVo();
   //		   inventory.setEpc(v.getKey());
   //		   inventory.setDeviceId(v.getValue());
   //		   inventory.setRenewTime(getDates());
   //		   inventory.setStatus("2");//入柜
   //		   inventory.setOperationStatus(99);
   //		   boolean save = inventory.save();
   //		   LogUtils.i(TAG, "Scan 入柜存入 " + save);
   //		}
   //	   }
   //	}
   //	for (InventoryVo s : mInVo) {
   //	   ContentValues values = new ContentValues();
   //	   values.put("status", "3");//出柜
   //	   values.put("operationstatus", 98);
   //	   values.put("renewtime", getDates());
   //	   //	   values.put("accountid", SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
   //	   //	   values.put("username", SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
   //	   LitePal.updateAll(InventoryVo.class, values, "epc = ?", s.getEpc());
   //	}
   //   }

   /**
    * 提交epc数据
    *
    * @param epcDatess
    */
   private void putEPC(Map<String, String> epcDatess, String boxid) {
	List<InventoryVo> mInVo = new ArrayList<>();
	List<InventoryVo> allVo = LitePal.findAll(InventoryVo.class);
	List<InventoryVo> vos = setAllBoxVosDate(allVo, boxid);
	mInVo.addAll(vos);
	for (Map.Entry<String, String> v : epcDatess.entrySet()) {
	   InventoryVo vo = LitePal.where("epc = ?", v.getKey()).findFirst(InventoryVo.class);
	   if (vo != null) {
		mInVo.remove(vo);
	   } else {
		if (v.getKey() != null && !v.getKey().toString().trim().equals("") &&
		    v.getValue() != null) {
		   if (!getVosType(allVo,v.getKey())){
			boolean saveError = saveErrorVo(v.getKey(),v.getValue(),true,false,false);//放入，存入error流水表
			LogUtils.i(TAG, "     Scan 入柜存入error  "+saveError);
		   }
		   boolean save = saveErrorVo(v.getKey(),v.getValue(),false,false,false);//放入，存入库存表
		   LogUtils.i(TAG, "Scan 入柜存入库存    " + save);
		}
	   }
	}
	for (InventoryVo s : mInVo) {
	   deleteVo(allVo,s.getEpc());//拿出时，删除库存表内的该条数据
	   boolean save = saveErrorVo(s.getEpc(),s.getDeviceId(),true,true,false);//拿出时，存入到error流水表
	   LogUtils.i(TAG, "Scan 出柜存入 并删除   " + save);
	}
	if (mTitleConn){
	   UnNetCstUtils.putUnNetOperateYes(getAppContext());//提交离线耗材和重新获取在库耗材数据
	}
   }

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
	EventBusUtils.register(this);
	List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean idBean : boxIdBeans) {
	   mDeviceSizeList.add(idBean.getDevice_id());
	}
	new Thread(() -> AllDeviceCallBack.getInstance().initCallBack()).start();

   }

   @Override
   public void onDestroy() {
	EventBusUtils.unregister(this);
	super.onDestroy();

   }
}
