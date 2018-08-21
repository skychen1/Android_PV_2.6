package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.InOutBoxTwoActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutBoxFoutActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.CONFIG_0011;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.views.RvDialog.sTableTypeView;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材操作主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentConsumeOperateFrag extends BaseSimpleFragment {

   String TAG = "ContentConsumeOperateFrag";
   @BindView(R.id.consume_openall_rv)
   RecyclerView mConsumeOpenallRv;
   @BindView(R.id.consume_openall_top)
   LinearLayout mConsumeOpenallTop;
   @BindView(R.id.function_title_meal)
   TextView     mFunctionTitleMeal;
   @BindView(R.id.function_cardview_meal)
   CardView     mFunctionCardviewMeal;
   @BindView(R.id.fastopen_title_form)
   TextView     mFastopenTitleForm;
   @BindView(R.id.function_cardview_form)
   CardView     mFunctionCardviewForm;
   @BindView(R.id.consume_openall_middle)
   LinearLayout mConsumeOpenallMiddle;
   @BindView(R.id.content_rb_ly)
   RadioButton  mContentRbLy;
   @BindView(R.id.content_rb_rk)
   RadioButton  mContentRbRk;
   @BindView(R.id.content_rb_yc)
   RadioButton  mContentRbYc;
   @BindView(R.id.content_rb_tb)
   RadioButton  mContentRbTb;
   @BindView(R.id.content_rb_yr)
   RadioButton  mContentRbYr;
   @BindView(R.id.content_rb_tuihui)
   RadioButton  mContentRbTuihui;
   @BindView(R.id.content_rb_tuihuo)
   RadioButton  mContentRbTuihuo;
   @BindView(R.id.content_rg)
   RadioGroup   mContentRg;
   @BindView(R.id.consume_down_rv)
   RecyclerView mConsumeDownRv;
   @BindView(R.id.consume_down)
   LinearLayout mConsumeDown;
   private LoadingDialog.Builder mShowLoading;
   private HomeFastOpenAdapter   mHomeFastOpenTopAdapter;
   private HomeFastOpenAdapter   mHomeFastOpenDownAdapter;

   private String                             eth002DeviceId;
   private String                             uhfDeviceId;
   private NoDialog.Builder                   mBuilder;
   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;
   //   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices2;
   private HashMap<String, String>            mReaderMap;

   public static List<String>                                 mReaderIdList;
   private       int                                          mRbKey;
   private       BoxSizeBean                                  mBoxSizeBean;
   private       BoxSizeBean                                  mBoxSizeBean2;
   private       List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos;
   private       String                                       mRvEventString;
   private       String                                       mPatientName;
   private       String                                       mPatientId;
   private       String                                       mFirstBind;
   private       String                                       mDeviceCode;
   private       RvDialog.Builder                             mShowRvDialog;
   private       Handler                                      mHandler;
   private String mOppenDoor = null;
   private List<String> mEthDeviceId;
   private TCstInventoryDto  mTCstInventoryDtoAll = new TCstInventoryDto();;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (event.isMute) {
	   if (mBuilder == null) {
		mBuilder = DialogUtils.showNoDialog(mContext, event.mString, 2, "in", null);
	   }
	} else {
	   if (mBuilder != null) {
		mBuilder.mDialog.dismiss();
		mBuilder = null;

	   }
	}
   }
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onOppenDoorEvent(Event.EventOppenDoor event) {
	mOppenDoor = event.mString;

   }
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onBooleanEvent(Event.EventBoolean event) {
//
	if (event.mBoolean){
	   new Thread(new Runnable() {
		@Override
		public void run() {
		   LogUtils.i(TAG,"EventBoolean   "+mOppenDoor);
		   if (mOppenDoor==null&&eth002DeviceId!=null){
			DeviceManager.getInstance().UnRegisterDeviceCallBack();
			initCallBack();
			DeviceManager.getInstance().OpenDoor(event.mId);
			//			EventBusUtils.post(new Event.EventBoolean(true,event.mId));
		   } else {
			LogUtils.i(TAG, "EventBoolean  进来2");
			DeviceManager.getInstance().UnRegisterDeviceCallBack();
			initCallBack();
			if (mShowLoading != null) {
			   mShowLoading.mDialog.dismiss();
			}
			mOppenDoor=null;
		   }
		}
	   }).start();

	}
   }
   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START1")) {
	   initCallBack();
	   initData();
	}else {
	   LogUtils.i(TAG,"UnRegisterDeviceCallBack");
	   DeviceManager.getInstance().UnRegisterDeviceCallBack();
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	mRvEventString = event.mString;
	LogUtils.i(TAG, "mRvEventString   " + mRvEventString);
	loadBingDate(mRvEventString, -2, null);
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onToast(Event.EventToast event) {
	ToastUtils.showUiToast(mContext, event.mString);
	if (mShowLoading != null) {
	   mShowLoading.mDialog.dismiss();
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvCheckBindEvent(Event.EventCheckbox event) {
	mFirstBind = event.type;
	if (event.type.equals("firstBind")) {
	   mPatientName = event.mString;
	   mPatientId = event.id;
	   LogUtils.i(TAG, "mPatientName   " + mPatientName);
	   LogUtils.i(TAG, "mPatientId   " + mPatientId);
	   openDoor(event.position, event.mTbaseDevices);
	} else {
	   String type = event.type;
	   String mString = event.mString;
	   int position = event.position;
	   List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices = event.mTbaseDevices;
	   mRvEventString = event.mString;
	   loadBingDate(mRvEventString, -1, null);
	}

   }

   public static ContentConsumeOperateFrag newInstance() {

	Bundle args = new Bundle();
	ContentConsumeOperateFrag fragment = new ContentConsumeOperateFrag();
	fragment.setArguments(args);
	LogUtils.i("ContentConsumeOperateFrag", "newInstance");
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.ctconsumeoperate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	EventBusUtils.register(this);
//	mShowLoading = DialogUtils.showLoading(mContext);
	LogUtils.i(TAG, "initDataAndEvent");
	initCallBack();
	mContentRbTb.setVisibility(View.GONE);
	initData();

   }


   private void initCallBack() {
	LogUtils.i(TAG, "initCallBack 进入  "+(DeviceManager.getInstance()==null ));

	App.InitDeviceService();
	DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
	   @Override
	   public void OnDeviceConnected(
		   DeviceType deviceType, String deviceIndentify) {
		LogUtils.i(TAG, "设备已连接：" + deviceType + ":::ID=" + deviceIndentify);
		if (deviceType == DeviceType.UHFREADER) {
		   uhfDeviceId = deviceIndentify;
		   LogUtils.i(TAG, "uhfDevice   " + uhfDeviceId);
		} else if (deviceType == DeviceType.Eth002) {
		   eth002DeviceId = deviceIndentify;
		}
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

	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {

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
		LogUtils.i(TAG, "开门结果  开门     " + success);
		if (success) {
		   EventBusUtils.post(new Event.PopupEvent(true, "柜门已开"));
		} else {
		   //		   if (mShowLoading != null) {
		   //			mShowLoading.mDialog.dismiss();
		   //		   }
		   //		   ToastUtils.showShort("开门异常，请重试！");
		}
	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {
		mEthDeviceId = DevicesUtils.getEthDeviceId();
		//遍历已连接的设备，查看是否都是关闭状态
		for (String deviceId : mEthDeviceId) {
		   DeviceManager.getInstance().CheckDoorState(deviceId);
		}
		LogUtils.i(TAG, "门锁已关闭：    " + success);

	   }

	   @Override
	   public void OnDoorCheckedState(String deviceIndentify, boolean opened) {


	      for (String deviceId : mEthDeviceId) {
		   if (!opened && deviceIndentify.equals(deviceId)) {
			clossDoorStartScan(deviceIndentify);
		   }
		}
	   }

	   @Override
	   public void OnUhfScanRet(
		   boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
		LogUtils.i(TAG, "扫描完成   " + success + "   deviceId   " + deviceId);
		if (!success) {
		   ToastUtils.showUiToast(_mActivity, "扫描失败，请重试！");
		}
		getDeviceDate(deviceId, epcs);

	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {
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
    * 柜门检测关闭后进行扫描
    * @param deviceIndentify
    */
   private void clossDoorStartScan(String deviceIndentify) {
	if (mReaderIdList.size() == 0) {

	   new Thread(new Runnable() {
		@Override
		public void run() {

		   try {
			Thread.currentThread().sleep(4000);
			setReaderList();
			if (mReaderIdList.size() == 0) {
			   LogUtils.i(TAG, "走了");
			   if (mShowLoading != null) {
				mShowLoading.mDialog.dismiss();
			   }
			   if (mBuilder != null) {
				mBuilder.mDialog.dismiss();
			   }
			   EventBusUtils.postSticky(
				   new Event.EventToast("reader未启动，请重新开关柜门"));
			} else {
			   startScan(deviceIndentify);
			}
		   } catch (InterruptedException e) {
			e.printStackTrace();
		   }
		}

	   }).start();

	} else {
	   if (mShowLoading != null) {
		mShowLoading.mDialog.dismiss();
	   }
	   startScan(deviceIndentify);
	}
   }

   /**
    * 正式开始扫描
    * @param deviceIndentify
    */
   private void startScan(String deviceIndentify) {

	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = DeviceManager.getInstance().StartUhfScan(device_id);
		LogUtils.i(TAG, "开始扫描了状态    " + i);
	   }
	}
   }

   /**
    * 扫描后传值
    */

   private void getDeviceDate(String deviceId, Map<String, List<TagInfo>> epcs) {

	List<TCstInventory> epcList = new ArrayList<>();

	for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
	   TCstInventory tCstInventory = new TCstInventory();
	   tCstInventory.setEpc(v.getKey());
	   epcList.add(tCstInventory);
	}

	DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
	List<DeviceInventoryVo> deviceList = new ArrayList<>();

	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   Log.i(TAG, "device_id   " + box_id);
	   if (box_id != null) {
		deviceInventoryVo.setDeviceCode(box_id);
	   }
	}
	deviceInventoryVo.settCstInventories(epcList);
	deviceList.add(deviceInventoryVo);

	tCstInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
	tCstInventoryDto.setDeviceInventoryVos(deviceList);
	tCstInventoryDto.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	LogUtils.i(TAG, "mRbKey    " + mRbKey);
	if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 || mRbKey == 10 ||
	    mRbKey == 7 || mRbKey == 8) {
	   tCstInventoryDto.setOperation(mRbKey);
	} else {
	   tCstInventoryDto.setOperation(mRbKey);
	}
	if (mFirstBind != null && mFirstBind.equals("firstBind") && mRbKey == 3) {
	   tCstInventoryDto.setPatientName(mPatientName);
	   tCstInventoryDto.setPatientId(mPatientId);
	}
	String toJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);

	NetRequest.getInstance().putEPCDate(toJson, _mActivity, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		TCstInventoryDto cstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
		String string = null;
		if (cstInventoryDto.getErrorEpcs() != null &&
		    cstInventoryDto.getErrorEpcs().size() > 0) {
		   string = StringUtils.listToString(cstInventoryDto.getErrorEpcs());
		   ToastUtils.showLong(string);
		   return;
		}
		LogUtils.i(TAG, "我跳转    " + (cstInventoryDto.gettCstInventoryVos() == null));
		//先绑定患者
		if (mFirstBind != null && mFirstBind.equals("firstBind") && mRbKey == 3) {
		   for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {
			tCstInventoryVo.setPatientName(cstInventoryDto.getPatientName());
			tCstInventoryVo.setPatientId(cstInventoryDto.getPatientId());
		   }
		   cstInventoryDto.setBindType("firstBind");
		   mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
		   EventBusUtils.postSticky(cstInventoryDto);

		} else {//正常的领用或者其他正常操作
		   mShowLoading.mDialog.dismiss();
		   if (cstInventoryDto.gettCstInventoryVos() == null ||
			 cstInventoryDto.gettCstInventoryVos().size() < 1) {
			if (mBuilder != null) {
			   mBuilder.mDialog.dismiss();
			}
			ToastUtils.showShort("未扫描到操作的耗材");
		   } else {
			LogUtils.i(TAG, "我跳转    " + cstInventoryDto.getType());
			EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
			if (cstInventoryDto.getType() == 0) {//放入
			   if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 ||
				 mRbKey == 10 || mRbKey == 7 || mRbKey == 8) {
				Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
				mContext.startActivity(intent2);
				EventBusUtils.postSticky(new Event.EventAct("inout"));
				cstInventoryDto.setOperation(mRbKey);
				EventBusUtils.postSticky(cstInventoryDto);
			   } else {
				Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
				mContext.startActivity(intent2);
				EventBusUtils.postSticky(new Event.EventAct("all"));
				EventBusUtils.postSticky(cstInventoryDto);
			   }
			} else {//拿出
			   if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 ||
				 mRbKey == 10 || mRbKey == 7 || mRbKey == 8) {
				Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
				mContext.startActivity(intent2);
				EventBusUtils.postSticky(new Event.EventAct("inout"));
				cstInventoryDto.setOperation(mRbKey);
				EventBusUtils.postSticky(cstInventoryDto);
			   } else {
				mContext.startActivity(new Intent(mContext, OutBoxFoutActivity.class));
				EventBusUtils.postSticky(cstInventoryDto);
			   }
			}
		   }
		}

	   }
	});
   }

   private void initData() {
	if (UIUtils.getConfigType(mContext, CONFIG_0011)) {
	   mConsumeOpenallTop.setVisibility(View.VISIBLE);
	} else {
	   mConsumeOpenallTop.setVisibility(View.GONE);
	}
	loadDate();

   }

   //数据加载
   private void loadDate() {
	LogUtils.i(TAG, "loadDate");
	NetRequest.getInstance().loadBoxSize(mContext, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		mBoxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
		//		mBoxSizeBean2 = mGson.fromJson(result, BoxSizeBean.class);
		//		mTbaseDevices2 = mBoxSizeBean2.getTbaseDevices();//顶部数据
		LogUtils.i(TAG, "result  "+result);
		mTbaseDevices = mBoxSizeBean.getTbaseDevices();
		if (mTbaseDevices.size() > 1) {
		   BoxSizeBean.TbaseDevicesBean tbaseDevicesBean = new BoxSizeBean.TbaseDevicesBean();
		   tbaseDevicesBean.setDeviceName("全部开柜");
		   mTbaseDevices.add(0, tbaseDevicesBean);
		}
		onSucceedDate();
	   }
	});
   }

   //赋值
   private void onSucceedDate() {
	LogUtils.i(TAG, "onSucceedDate");
	mShowLoading.mDialog.dismiss();
	//	mConsumeOpenallMiddle.setVisibility(View.GONE);//此处部分医院不需要可以隐藏  根据接口来
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材操作");
	mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME));

	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

	//	if (mTbaseDevices2.size() <= 1) {
	//	   mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
	//									     mTbaseDevices2);
	//	} else {
	mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									  mTbaseDevices);
	//	}

	mConsumeOpenallRv.setLayoutManager(layoutManager);
	mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

	mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		mRbKey = -1;
		//		if (mTbaseDevices2.size() <= 1) {
		//		   openDoor(position, "top", mTbaseDevices2);
		//		} else {
		openDoor(position, mTbaseDevices);
		//		}

		//		DeviceManager.getInstance().OpenDoor("xxx");
		//		if (position == 0){
		//
		//
		//		}
		// else if (position == 1){
		//		   DialogUtils.showNoDialog(mContext, title, 2,"out",null);
		//		}else if (position ==2){
		//		   DialogUtils.showNoDialog(mContext, title, 2,"out","bing");
		//		}else if (position == 3){
		//		   ToastUtils.showShort("按套餐领用-绑定患者！");
		//		   mContext.startActivity(new Intent(mContext,OutMealActivity.class));
		//		   EventBusUtils.postSticky(new Event.EventAct("BING_MEAL"));
		//
		//		}else if (position ==4){
		//
		//		}

		//		if (position == 0) {
		//		   int mType = 1;//1.8.3未绑定
		//		   showTwoDialog(mType);
		//		} else if (position == 1) {
		//		   int mType = 1;//1.6移出
		//		   showStoreDialog(3, mType);
		//		} else if (position == 2) {
		//		   int mType = 2;//1.7退货
		//		   showStoreDialog(2, mType);
		//		} else if (position == 3) {
		//		   int mType = 3;//1.8调拨
		//		   showStoreDialog(2, mType);
		//		} else if (position == 4) {
		//		   int mType = 2;//1.9.3请领单
		//		   showTwoDialog(mType);
		//		} else if (position == 5) {//1.2错误
		//		   String title = "耗材中包含过期耗材，请查看！";
		//		   showNoDialog(title, 1);
		//		} else if (position == 6) {//1.8.1选择患者
		//		   showRvDialog();
		//		} else {
		//		   String title = "柜门已开";
		//		   showNoDialog(title, 2);
		//		}
	   }
	});
	LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
	layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

	mHomeFastOpenDownAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									   mTbaseDevices);
	mConsumeDownRv.setLayoutManager(layoutManager2);
	mConsumeDownRv.setAdapter(mHomeFastOpenDownAdapter);
	mHomeFastOpenDownAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		int id = mContentRg.getCheckedRadioButtonId();
		if (id == -1) {
		   ToastUtils.showShort("请选择操作方式！");
		} else {
		   switch (id) {
			case R.id.content_rb_ly:
			   mRbKey = 3;
			   ToastUtils.showShort("领用！");//拿出
			   if (UIUtils.getConfigType(mContext, CONFIG_007) &&
				 UIUtils.getConfigType(mContext, CONFIG_010)) {
				//先绑定患者再开柜
				loadBingDate("", position, mTbaseDevices);
			   } else {
				//不绑定患者
				openDoor(position, mTbaseDevices);
			   }
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_rk:
			   mRbKey = 2;
			   ToastUtils.showShort("入库！");//拿入
			   openDoor(position, mTbaseDevices);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_yc:
			   mRbKey = 9;
			   ToastUtils.showShort("移出！");//拿出
			   openDoor(position, mTbaseDevices);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_tb:
			   mRbKey = 11;
			   ToastUtils.showShort("调拨！");//拿出
			   openDoor(position, mTbaseDevices);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_yr:
			   mRbKey = 10;
			   ToastUtils.showShort("移入！");//拿入
			   openDoor(position, mTbaseDevices);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_tuihui:
			   mRbKey = 7;
			   ToastUtils.showShort("退回！");//拿入
			   openDoor(position, mTbaseDevices);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_tuihuo:
			   mRbKey = 8;
			   ToastUtils.showShort("退货！");//拿出
			   openDoor(position, mTbaseDevices);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
		   }
		}
	   }
	});
   }

   @Override
   public void onDestroy() {
	if (mShowLoading != null) {
	   mShowLoading.mDialog.dismiss();
	}
	LogUtils.i(TAG,"onDestroy");
	super.onDestroy();
   }

   /**
    * 开柜
    * @param position
    * @param mTbaseDevices
    */
   private void openDoor(
	   int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	mShowLoading = DialogUtils.showLoading(mContext);
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	if (mTbaseDevices.size() > 1) {//第一个为全部开柜
	   if (position == 0) {
		List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", READER_TYPE)
			.find(BoxIdBean.class);
		mReaderMap = new HashMap<>();
		for (BoxIdBean boxIdBean : boxIdBeans) {
		   String device_id = boxIdBean.getDevice_id();
		   String box_id = boxIdBean.getBox_id();
		   mReaderMap.put(device_id, box_id);
		}
		for (int i = 0; i < eth002DeviceIdList.size(); i++) {
		   DeviceManager.getInstance().OpenDoor((String) eth002DeviceIdList.get(i));
		}
	   } else {
		oneOpenBox(position, mTbaseDevices);
	   }
	} else {//只有一个柜子
	   oneOpenBox(position, mTbaseDevices);
	}

   }

   /**
    * 开单个柜子获得reader的标识
    * @param position
    * @param mTbaseDevices
    */
   private void oneOpenBox(
	   int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	BoxSizeBean.TbaseDevicesBean devicesBean = mTbaseDevices.get(position);
	mDeviceCode = devicesBean.getDeviceCode();
	if (mReaderIdList != null) {
	   mReaderIdList.clear();
	} else {
	   mReaderIdList = new ArrayList<>();
	}
	LogUtils.i(TAG, "deviceCode   " + mDeviceCode + " READER_TYPE  " + READER_TYPE);
	setReaderList();
	LogUtils.i(TAG, " eth002DeviceIdList.size   " + eth002DeviceIdList.size());

	if (mTbaseDevices.size() > 1 && eth002DeviceIdList.size() > 1) {
	   if (position == 0) {//第一个为全部开柜
		LogUtils.i(TAG, " position   " + position);
		for (int i = 0; i < eth002DeviceIdList.size(); i++) {
		   LogUtils.i(TAG,
				  " eth002DeviceIdList.get(i)   " + (String) eth002DeviceIdList.get(i));
		   DeviceManager.getInstance().OpenDoor((String) eth002DeviceIdList.get(i));
		}
	   } else {
		queryDoorId();
	   }
	} else {
	   queryDoorId();
	}
   }

   /**
    * 获取设备门锁ID，并开柜
    */
   private void queryDoorId() {
	for (int i = 0; i < eth002DeviceIdList.size(); i++) {
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    UHF_TYPE).find(BoxIdBean.class);
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

   private void setReaderList() {
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

   @Override
   public void onPause() {
      LogUtils.i(TAG,"onPause");
	super.onPause();

   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.function_title_meal, R.id.fastopen_title_form})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);
		popupClick();
		break;
	   case R.id.base_tab_btn_msg:

		break;
	   case R.id.function_title_meal:
		if (UIUtils.getConfigType(mContext, CONFIG_007) &&
		    UIUtils.getConfigType(mContext, CONFIG_010)) {
		   mContext.startActivity(new Intent(mContext, OutMealActivity.class));
		   EventBusUtils.postSticky(new Event.EventAct("NOBING_MEAL"));
		} else {
		   ToastUtils.showShort("此功能暂未开放");
		}

		break;
	   case R.id.fastopen_title_form:
		if (UIUtils.getConfigType(mContext, CONFIG_007) &&
		    UIUtils.getConfigType(mContext, CONFIG_010)) {
		   mContext.startActivity(new Intent(mContext, OutFormActivity.class));
		} else {
		   ToastUtils.showShort("此功能暂未开放");
		   //		   DialogUtils.showRegisteDialog(mContext, _mActivity);
		}

		break;
	   case R.id.fastopen_title_guanlian://患者关联
		mContext.startActivity(new Intent(mContext, PatientConnActivity.class));
		break;
	}
   }


   /**
    * 获取需要绑定的患者
    */
   private void loadBingDate(
	   String optienNameOrId, int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	LogUtils.i(TAG, "optienNameOrId   " + optienNameOrId);
	NetRequest.getInstance().findSchedulesDate(optienNameOrId, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		if (mPatientInfos != null) {
		   mPatientInfos.clear();
		   List<BingFindSchedulesBean.PatientInfosBean> patientInfos = bingFindSchedulesBean.getPatientInfos();
		   mPatientInfos.addAll(patientInfos);
		   if (mShowRvDialog.mDialog.isShowing()) {
			sTableTypeView.mBingOutAdapter.notifyDataSetChanged();
		   } else {
			mShowRvDialog = DialogUtils.showRvDialog(_mActivity, mContext, mPatientInfos,
									     "firstBind", position, mTbaseDevices);
		   }

		} else {
		   mPatientInfos = bingFindSchedulesBean.getPatientInfos();
		   mShowRvDialog = DialogUtils.showRvDialog(_mActivity, mContext, mPatientInfos,
									  "firstBind", position, mTbaseDevices);
		}
	   }
	});
   }

}
