package high.rivamed.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.rivamed.FingerManager;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.reader.ReaderManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dbmodel.ChildrenBean;
import high.rivamed.myapplication.dbmodel.ChildrenBeanX;
import high.rivamed.myapplication.dbmodel.OperationRoomsBean;
import high.rivamed.myapplication.dbmodel.RoomsBean;
import high.rivamed.myapplication.dbmodel.UserBean;
import high.rivamed.myapplication.dbmodel.UserFeatureInfosBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetApi;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.receiver.NetWorkReceiver;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.RxUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.getAppContext;
import static high.rivamed.myapplication.base.App.mAppContext;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.CONSUMABLE_TYPE;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;
import static high.rivamed.myapplication.utils.LyDateUtils.setAllBoxVosDate;
import static high.rivamed.myapplication.utils.UnNetCstUtils.deleteVo;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getAllCstDate;
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
   private       List<String>              mEPCDate         = new ArrayList<>();
   private       Map<String, String>       mEPCDatess       = new TreeMap<>();
   public static boolean                   mDoorStatusType  = false;//false 没关  true已经关了
   private       List<String>              mDeviceSizeList  = new ArrayList<>();
   private       ArrayList<String>         mListDevices;
   private       ArrayList<String>         mEthDevices      = new ArrayList<>();
   private       RxUtils.BaseEpcObservable mObs;
   public        List<InventoryVo>         mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   private       NetWorkReceiver           mWorkReceiver;
   private       Gson                      mGson            = new Gson();
   private       Thread                    mThread;
   private       Thread                    mThread1;
   private       Thread                    mThread2;
   private       long                      lastClickTime    = 0L;
   private       long                      mTime;
   private       ScheduledExecutorService  scheduled;
   private       TimerTask                 mTask;

   /**
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onServerEvent(Event.EventServer event) {
	mTime = System.currentTimeMillis();
	if (event.type == 1) {//登录界面每次显示更新数据
	   if (mThread != null) {
		if (mTime - lastClickTime >= 2000) {
		   mThread.start();
		   lastClickTime = mTime;
		}
	   } else {
		mThread = new Thread(new Runnable() {
		   @Override
		   public void run() {
			getAllCstDate(this);//重新获取在库耗材数据
			getUnNetUseDate();
			getUnEntFindOperation();
		   }
		});
		lastClickTime = mTime;
		mThread.start();
	   }
	} else if (event.type == 2) {

	   if (mThread1 != null) {
		if (mTime - lastClickTime >= 2000) {
//		   mThread1.start();
//		   lastClickTime = mTime;
		}
	   } else {
		mThread1 = new Thread(new Runnable() {
		   @Override
		   public void run() {
			List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
			if (voList == null || voList.size() == 0) {
			   getAllCstDate(this);
			}
			UnNetCstUtils.putUnNetOperateYes(this);//提交离线耗材和重新获取在库耗材数据
		   }
		});
		lastClickTime = mTime;
		mThread1.start();
	   }
	}
   }

   /**
    * 所有用户的本地数据
    */
   private void getUnNetUseDate() {
	NetRequest.getInstance().getUnNetUseDate(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		UserBean userBean = mGson.fromJson(result, UserBean.class);
		List<AccountVosBean> accountVos = userBean.getAccountVos();
		for (AccountVosBean allUser : accountVos) {
		   AccountVosBean vo = LitePal.where("accountId = ?", allUser.getAccountId())
			   .findFirst(AccountVosBean.class);
		   if (vo == null) {
			new Thread(new Runnable() {
			   @Override
			   public void run() {
				//				setLitePalUseBean(allUser);//存入
				saveUserData(allUser);
			   }
			}).start();
		   }
		}
	   }
	});
   }

   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventDoorStatus(Event.EventDoorStatus event) {
	Log.i("onDoorState","onEventDoorStatus：：" + event.type);
	if (event.type) {//门没关
	   mDoorStatusType = false;
	   mEthDevices.clear();
	   if (mListDevices != null) {
		mListDevices.clear();
	   }
	   EventBusUtils.post(new Event.EventDoorV(false));
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
	   if (mDeviceSizeList != null && mListDevices != null &&
		 (mDeviceSizeList.size() == mListDevices.size())) {
		//	   if (mDeviceSizeList.size() == mListDevices.size()) {
		mDoorStatusType = true;
		mListDevices.clear();
		mEthDevices.clear();
		EventBusUtils.post(new Event.EventDoorV(true));
	   }
	   if (mListDevices == null) {
		mListDevices = new ArrayList<>();
		mDoorStatusType = true;
		EventBusUtils.post(new Event.EventDoorV(true));
	   }
	   if (mDeviceSizeList == null) {
		mDeviceSizeList = new ArrayList<>();
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
	LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size() + "   box_id   " + box_id);
	putEPC(mEPCDatess, box_id);
   }

   /**
    * 提交epc数据
    *
    * @param epcDatess
    */
   private void putEPC(Map<String, String> epcDatess, String boxid) {
	List<InventoryVo> mInVo = new ArrayList<>();
	List<InventoryVo> mBoxInventoryVos = new ArrayList<>();
	List<InventoryVo> vos = setAllBoxVosDate(mBoxInventoryVos, boxid);
	mInVo.addAll(vos);
	Iterator<Map.Entry<String, String>> iterator = epcDatess.entrySet().iterator();
	while (iterator.hasNext()) {
	   Map.Entry<String, String> next = iterator.next();
	   String key = next.getKey();
	   String value = next.getValue();
	   InventoryVo vo = LitePal.where("epc = ?", key).findFirst(InventoryVo.class);
	   if (vo != null) {
		mInVo.remove(vo);
	   } else {
		if (key != null && !key.trim().equals("") && value != null) {
		   if (!getVosType(mBoxInventoryVos, key)) {
			boolean saveError = saveErrorVo(key, value, true, false, false);//放入，存入error流水表
			Log.i(TAG, "     Scan 存入error流水表  " + saveError);
		   }
		   if (!mTitleConn) {
			boolean save = saveErrorVo(key, value, false, false, false);//放入，存入库存表
			Log.i(TAG, "Scan 存入库存表    " + save);
		   }
		}
	   }
	}
	Iterator<InventoryVo> iteratorX = mInVo.iterator();
	while (iteratorX.hasNext()) {
	   InventoryVo next = iteratorX.next();
	   deleteVo(mBoxInventoryVos, next.getEpc());//拿出时，删除库存表内的该条数据
	   boolean save = saveErrorVo(next.getEpc(), next.getDeviceId(), true, true,
						false);//拿出时，存入到error流水表
	   Log.i(TAG, "Scan 出柜存入 并删除   " + save);
	}
	if (mTitleConn) {
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

	setConnectType();

	EventBusUtils.register(this);
	List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", CONSUMABLE_TYPE).find(BoxIdBean.class);
	if (boxIdBeans!=null){
	   for (BoxIdBean idBean : boxIdBeans) {
		if (idBean.getCabinetType().equals("0")||idBean.getCabinetType().equals("1")){
		   mDeviceSizeList.add(idBean.getDevice_id()+"0");
		}else if (idBean.getCabinetType().equals("2")){
		   mDeviceSizeList.add(idBean.getDevice_id()+"1");
		}
	   }
	}

	initReceiver();
	AllDeviceCallBack.getInstance().initCallBack();
	//	new Thread(() -> AllDeviceCallBack.getInstance().initCallBack()).start();

   }

   private void setConnectType() {
	mTask = new TimerTask() {
	   @Override
	   public void run() {
		MAIN_URL = SPUtils.getString(mAppContext, SAVE_SEVER_IP);
		String urls = MAIN_URL + NetApi.URL_CONNECT;
		if (MAIN_URL != null) {
		   OkGo.<String>get(urls).tag(this).execute(new StringCallback() {
			@Override
			public void onSuccess(Response<String> response) {
			   EventBusUtils.post(new Event.XmmppConnect(true));
			}

			@Override
			public void onError(Response<String> response) {
			   EventBusUtils.post(new Event.XmmppConnect(false));
			}
		   });
		} else {
		   EventBusUtils.post(new Event.XmmppConnect(false));
		}
	   }
	};
	if (scheduled == null) {
	   scheduled = Executors.newScheduledThreadPool(1);
	}
	scheduled.scheduleAtFixedRate(mTask, 0, 15000, TimeUnit.MILLISECONDS);

   }

   @Override
   public void onDestroy() {
	EventBusUtils.unregister(this);
	if (mWorkReceiver != null) {
	   unregisterReceiver(mWorkReceiver);
	   mWorkReceiver = null;
	}
	ReaderManager.getManager().unRegisterCallback();
	FingerManager.getManager().unRegisterCallback();
	IdCardManager.getIdCardManager().unRegisterCallBack();
	ConsumableManager.getManager().unRegisterCallback();
	Log.i("FADDDD", "管了   ");
	super.onDestroy();

   }

   /**
    * 注册网络监听的广播
    */
   private void initReceiver() {
	mWorkReceiver = new NetWorkReceiver();
	IntentFilter timeFilter = new IntentFilter();
	timeFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
	timeFilter.addAction("android.net.ethernet.STATE_CHANGE");
	timeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
	timeFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
	timeFilter.addAction("android.net.wifi.STATE_CHANGE");
	timeFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	registerReceiver(mWorkReceiver, timeFilter);
   }

   /**
    * 数据存储
    *
    * @param accountVosBean
    */
   private void setLitePalUseBean(AccountVosBean accountVosBean) {

	AccountVosBean vosBean = new AccountVosBean();
	vosBean.setAccountId(accountVosBean.getAccountId());
	vosBean.setUserId(accountVosBean.getUserId());
	vosBean.setAccountName(accountVosBean.getAccountName());
	vosBean.setTenantId(accountVosBean.getTenantId());
	vosBean.setUseState(accountVosBean.getUseState());
	vosBean.setPassword(accountVosBean.getPassword());
	vosBean.setSalt(accountVosBean.getSalt());
	vosBean.setSex(accountVosBean.getSex());
	vosBean.setUserName(accountVosBean.getUserName());
	for (UserFeatureInfosBean userFeatureInfosBean : accountVosBean.getUserFeatureInfos()) {
	   UserFeatureInfosBean infosBean = new UserFeatureInfosBean();
	   infosBean.setFeatureId(userFeatureInfosBean.getFeatureId());
	   infosBean.setUserId(userFeatureInfosBean.getUserId());
	   infosBean.setType(userFeatureInfosBean.getType());
	   infosBean.setData(userFeatureInfosBean.getData());
	   infosBean.setAccountName(accountVosBean.getAccountName());
	   infosBean.save();
	   vosBean.getUserFeatureInfos().add(infosBean);
	}
	for (HomeAuthorityMenuBean homeAuthorityMenuBean : accountVosBean.getMenus()) {
	   HomeAuthorityMenuBean mhomeAuthorityMenuBean = new HomeAuthorityMenuBean();
	   mhomeAuthorityMenuBean.setTitle(homeAuthorityMenuBean.getTitle());
	   mhomeAuthorityMenuBean.setAccountName(accountVosBean.getAccountName());
	   if (homeAuthorityMenuBean.getTitle().equals("耗材操作") &&
		 null != homeAuthorityMenuBean.getChildren() &&
		 homeAuthorityMenuBean.getChildren().size() > 0) {
		ChildrenBeanX mChildrenBeanX = new ChildrenBeanX();
		mChildrenBeanX.setTitle(homeAuthorityMenuBean.getChildren().get(0).getTitle());
		mChildrenBeanX.setAccountName(accountVosBean.getAccountName());
		if (null != homeAuthorityMenuBean.getChildren().get(0).getChildren()) {
		   for (int x = 0;
			  x < homeAuthorityMenuBean.getChildren().get(0).getChildren().size(); x++) {
			ChildrenBean childrenBean = homeAuthorityMenuBean.getChildren()
				.get(0)
				.getChildren()
				.get(x);
			ChildrenBean mChildrenBean = new ChildrenBean();
			mChildrenBean.setTitle(childrenBean.getTitle());
			mChildrenBean.setAccountName(accountVosBean.getAccountName());
			mChildrenBean.save();
			mChildrenBeanX.getChildren().add(mChildrenBean);
		   }
		}
		mChildrenBeanX.save();
		mhomeAuthorityMenuBean.getChildren().add(mChildrenBeanX);
	   }
	   mhomeAuthorityMenuBean.save();
	   vosBean.getMenus().add(mhomeAuthorityMenuBean);
	}
	boolean save = vosBean.save();
	Log.i("ddefad", "存入名字结束  " + save + "    " + getDates());
   }

   /**
    * 本地手术室
    */
   private void getUnEntFindOperation() {

	NetRequest.getInstance().getUnEntFindOperation(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "getUnEntFindOperation    " + result);
		RoomsBean roomsBean = mGson.fromJson(result, RoomsBean.class);
		List<OperationRoomsBean> operationRooms = roomsBean.getOperationRooms();

		for (OperationRoomsBean allRoom : operationRooms) {
		   OperationRoomsBean vo = LitePal.where("optRoomId = ?", allRoom.getOptRoomId())
			   .findFirst(OperationRoomsBean.class);
		   if (vo == null) {
			boolean save = allRoom.save();//存入
			Log.i("ddefad", "存入手术间  " + save);
		   }
		}
	   }
	});
   }

   private void saveUserData(AccountVosBean accountVosBean) {

	List<UserFeatureInfosBean> userFeatureInfosList = new ArrayList<>();
	for (UserFeatureInfosBean userFeatureInfosBean : accountVosBean.getUserFeatureInfos()) {
	   userFeatureInfosBean.setAccountName(accountVosBean.getAccountName());
	   userFeatureInfosList.add(userFeatureInfosBean);
	}
	LitePal.saveAll(userFeatureInfosList);

	List<HomeAuthorityMenuBean> homeAuthorityMenuBeanS = new ArrayList<>();
	List<ChildrenBeanX> mChildrenBeanXS = new ArrayList<>();

	for (HomeAuthorityMenuBean homeAuthorityMenuBean : accountVosBean.getMenus()) {
	   homeAuthorityMenuBean.setAccountName(accountVosBean.getAccountName());
	   if (homeAuthorityMenuBean.getTitle().equals("耗材操作") &&
		 null != homeAuthorityMenuBean.getChildren() &&
		 homeAuthorityMenuBean.getChildren().size() > 0) {

		ChildrenBeanX childrenBeanX = homeAuthorityMenuBean.getChildren().get(0);
		childrenBeanX.setAccountName(accountVosBean.getAccountName());
		if (null != homeAuthorityMenuBean.getChildren().get(0).getChildren()) {
		   List<ChildrenBean> children = homeAuthorityMenuBean.getChildren()
			   .get(0)
			   .getChildren();
		   for (ChildrenBean child : children) {
			child.setAccountName(accountVosBean.getAccountName());
		   }
		   LitePal.saveAll(children);
		}
		mChildrenBeanXS.add(childrenBeanX);
	   }
	   LitePal.saveAll(mChildrenBeanXS);
	   homeAuthorityMenuBeanS.add(homeAuthorityMenuBean);
	}
	accountVosBean.setMenus(homeAuthorityMenuBeanS);
	LitePal.saveAll(homeAuthorityMenuBeanS);
	boolean save = accountVosBean.save();
	Log.i("ddefad", "userBean完成  " + save + "     " + getDates());
   }
}
