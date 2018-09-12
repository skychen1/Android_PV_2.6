package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.fragment.TimelyAllFrag;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 19:59
 * 描述:       后绑定患者  拿出  绑定病人（快速开柜需要选择框，选择操作不需要选者框）
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutBoxBingActivity extends BaseTimelyActivity {

   private static final String TAG = "OutBoxBingActivity";
   private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos;
   private String                                       mRvEventString;
   private int                                          mIntentType;
   private TCstInventoryDto                             mTCstInventoryTwoDto;
   private LoadingDialog.Builder                        mShowLoading;
   private String                                       mPatient;
   private String                                       mPatientId;
   private String                                       mOperationScheduleId;
   private boolean mPause = true;
   private Map<String, List<TagInfo>> mEPCDate  = new TreeMap<>();;
   int k = 0;
   /**
    * 扫描后EPC准备传值
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	LogUtils.i(TAG, "TAG   " + mEthDeviceIdBack.size());
	AllDeviceCallBack.getInstance().initCallBack();
	List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId).find(BoxIdBean.class);

	for (BoxIdBean boxIdBean : boxIdBeanss) {
	   String box_id = boxIdBean.getBox_id();
	   if (box_id != null) {
		List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE).find(BoxIdBean.class);
		if (boxIdBeansss.size()>1){

		   for (BoxIdBean BoxIdBean:boxIdBeansss){
			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			if (BoxIdBean.getDevice_id().equals(event.deviceId)){
			   mEPCDate.putAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			}
		   }
		   if (k==boxIdBeansss.size()){
			k=0;
			if (!mPause) {
			   LogUtils.i(TAG, "mEPCDate  zou l  " );
			   getDeviceDate(event.deviceId, mEPCDate);
			}
		   }

		}else {
		   if (!mPause) {
			LogUtils.i(TAG, "event.epcs直接走   " +event.epcs);
			getDeviceDate(event.deviceId, event.epcs);
		   }
		}
	   }
	}
   }

   @Override
   protected void onPause() {
	mPause = true;
	super.onPause();
   }

   @Override
   public void onResume() {
//	moreStartScan();
	mPause = false;
	super.onResume();
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	Intent intent = getIntent();
	int type = intent.getIntExtra("type", 0);
	if (type == 100) {
	   mPatient = intent.getStringExtra("patientName");
	   mPatientId = intent.getStringExtra("patientId");
	   mOperationScheduleId = intent.getStringExtra("operationScheduleId");

	}
	Log.e(TAG, "intent:" + mPatient);
	Log.e(TAG, "intent:" + mPatientId);
	Log.e(TAG, "intent:" + mOperationScheduleId);
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventBing(Event.EventCheckbox event) {
	int operation = mTCstInventoryDto.getOperation();

	mPatient = event.mString;
	mPatientId = event.id;
	mOperationScheduleId = event.operationScheduleId;
	Log.i(TAG, "mMovie  " + mPatient);
	if (event.type != null && event.type.equals("firstBind")) {

	} else {
	   Log.i(TAG, "mMovie DDD " + mPatient);
	   if (!TextUtils.isEmpty(mPatient)) {
		for (int i = 0; i < mTCstInventoryVos.size(); i++) {
		   mTCstInventoryVos.get(i).setPatientName(mPatient);
		   mTCstInventoryVos.get(i).setPatientId(event.id);
		}
		if (mTypeView!=null&&mTypeView.mRecogHaocaiAdapter!=null){
		   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
		}

		for (TCstInventoryVo b : mTCstInventoryVos) {
		   ArrayList<String> strings = new ArrayList<>();
		   strings.add(b.getCstCode());
		   if ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			 (b.getPatientName() == null || b.getPatientName().equals(""))) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			return;
		   }
		   String status = b.getStatus();
		   if (status.equals("禁止操作")||status.equals("禁止入库") || status.equals("禁止移入") || status.equals("禁止退回") ||
			 (operation == 3 && !status.contains("领用")&&!status.equals("移除")) ||
			 (operation == 2 && !status.contains("入库")&&!status.equals("移除")) ||
			 (operation == 9 && !status.contains("移出")&&!status.equals("移除")) ||
			 (operation == 11 && !status.contains("调拨")&&!status.equals("移除")) ||
			 (operation == 10 && !status.contains("移入")&&!status.equals("移除")) ||
			 (operation == 7 && !status.contains("退回")&&!status.equals("移除")) ||
			 (operation == 8 && !status.contains("退货")&&!status.equals("移除"))) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			return;
		   } else {
			LogUtils.i(TAG, "我走了falsesss");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);

		   }
		}
	   }
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	mRvEventString = event.mString;
	loadBingDate(mRvEventString);
   }

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_BING;
	return my_id;
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_left, R.id.timely_right, R.id.timely_start_btn_right,
	   R.id.timely_open_door_right, R.id.ly_bing_btn_right})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(view);
		mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
		   @Override
		   public void onItemClick(int position) {
			switch (position) {
			   case 0:
				mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
				break;
			   case 1:
				mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
				break;
			   case 2:
				TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
				builder.setTwoMsg("您确认要退出登录吗?");
				builder.setMsg("温馨提示");
				builder.setLeft("取消", new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialog, int i) {
					dialog.dismiss();
				   }
				});
				builder.setRight("确认", new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialog, int i) {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
					App.getInstance().removeALLActivity_();
					dialog.dismiss();
				   }
				});
				builder.create().show();
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.timely_start_btn_right://重新扫描
		//		   mShowLoading = DialogUtils.showLoading(mContext);
		moreStartScan();

		break;
	   case R.id.timely_open_door_right://重新开门
		List<DeviceInventoryVo> deviceInventoryVoss = mTCstInventoryDto.getDeviceInventoryVos();
		mTCstInventoryDto.gettCstInventoryVos().clear();
		deviceInventoryVoss.clear();
		TimelyAllFrag.mPause = true;
		mPatient = null;
		mPatientId = null;
		for (String deviceInventoryVo : mEthDeviceIdBack) {
		   String deviceCode = deviceInventoryVo;
		   LogUtils.i(TAG, "deviceCode    " + deviceCode);
		   DeviceManager.getInstance().OpenDoor(deviceCode);
		}
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mIntentType = 1;//确认
		   loadBingFistDate(mIntentType);
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mIntentType = 2;//2确认并退出
		   loadBingFistDate(mIntentType);
		}
		break;
	   case R.id.ly_bing_btn_right://选择绑定患者  区分是否有临时患者
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {//创建临时患者
			goToFirstBindAC(-1);
		   } else {
			//不绑定临时患者，弹框
			loadBingDate("");
		   }
		}
		break;
	}
   }

   private void moreStartScan() {
	mEPCDate.clear();
	mTimelyLeft.setEnabled(true);
	mTimelyRight.setEnabled(true);
	mPatient = null;
	mPatientId = null;
	List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
	mTCstInventoryDto.gettCstInventoryVos().clear();
	deviceInventoryVos.clear();
	TimelyAllFrag.mPause = true;
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   startScan(deviceCode);
	}
   }

   /**
    * 后绑定患者
    *
    * @param mIntentType
    */
   private void loadBingAfterDate(int mIntentType) {
	mTCstInventoryDto.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
	mTCstInventoryDto.setOperationScheduleId(mOperationScheduleId);
	mTCstInventoryDto.setPatientName(mPatient);
	mTCstInventoryDto.setPatientId(mPatientId);
	String toJson = mGson.toJson(mTCstInventoryDto);
	LogUtils.i(TAG, "toJson  " + toJson);
	NetRequest.getInstance().bingPatientsDate(toJson, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ToastUtils.showShort("操作成功");
		EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
		if (mIntentType == 2) {
		   startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		}
		finish();
	   }
	});
   }

   private void goToFirstBindAC(int position) {
	//获取需要绑定的患者
	NetRequest.getInstance().findSchedulesDate("", this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
		   mContext.startActivity(
			   new Intent(mContext, TemPatientBindActivity.class).putExtra("position",
													   position)
				   .putExtra("type", "afterBindTemp"));
		} else {
		   ToastUtils.showShort("没有患者数据");
		}
	   }
	});
   }

   /**
    * 绑定患者用操作耗材接口
    *
    * @param mIntentType
    */
   private void loadBingFistDate(int mIntentType) {
	//	mTCstInventoryDto.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mTCstInventoryDto.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
	mTCstInventoryDto.setOperationScheduleId(mOperationScheduleId);
	mTCstInventoryDto.setPatientName(mPatient);
	mTCstInventoryDto.setPatientId(mPatientId);
	String toJson = mGson.toJson(mTCstInventoryDto);
	LogUtils.i(TAG, "toJson  " + toJson);
	NetRequest.getInstance().putOperateYes(toJson, this, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ToastUtils.showShort("操作成功");
		EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
		if (mIntentType == 2) {
		   startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		}
		finish();
	   }
	});
   }

   /**
    * 获取需要绑定的患者
    */
   private void loadBingDate(String optienNameOrId) {

	NetRequest.getInstance()
		.findSchedulesDateNoTemp(optienNameOrId, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
													 BingFindSchedulesBean.class);
			mPatientInfos = bingFindSchedulesBean.getPatientInfos();
			DialogUtils.showRvDialog(OutBoxBingActivity.this, mContext, mPatientInfos,
							 "afterBind", -1, null);
			LogUtils.i(TAG, "result   " + result);
		   }
		});
   }

   @Override
   protected void onDestroy() {
	EventBusUtils.postSticky(new Event.EventFrag("START1"));
	super.onDestroy();
   }

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

	TCstInventoryDto tCstInventoryDto = new TCstInventoryDto();
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
	   deviceInventoryVo.setDeviceCode(box_id);
	}
	deviceInventoryVo.settCstInventories(epcList);
	deviceList.add(deviceInventoryVo);

	tCstInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
	tCstInventoryDto.setOperation(mTCstInventoryDto.getOperation());
	tCstInventoryDto.setDeviceInventoryVos(deviceList);
	tCstInventoryDto.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	   tCstInventoryDto.setPatientName(mTCstInventoryDto.getPatientName());
	   tCstInventoryDto.setPatientId(mTCstInventoryDto.getPatientId());
	String toJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	mEPCDate.clear();
	NetRequest.getInstance().putEPCDate(toJson, this, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result    " + result);
		if (mTCstInventoryTwoDto != null) {
		   mTCstInventoryTwoDto = null;
		}
		mTCstInventoryTwoDto = mGson.fromJson(result, TCstInventoryDto.class);

		setDateEpc();

	   }

	});
   }

   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc() {
	String string = null;
	if (mTCstInventoryTwoDto.getErrorEpcs() != null &&
	    mTCstInventoryTwoDto.getErrorEpcs().size() > 0) {
	   string = StringUtils.listToString(mTCstInventoryTwoDto.getErrorEpcs());
	   ToastUtils.showLong(string);
	} else if (mTCstInventoryTwoDto.getErrorEpcs() == null &&
		     (mTCstInventoryTwoDto.gettCstInventoryVos() == null ||
			mTCstInventoryTwoDto.gettCstInventoryVos().size() < 1) &&
		     mEthDeviceIdBack.size() == 1) {

	   if (mTimelyLeft != null && mTimelyRight != null) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
	   }
	   EventBusUtils.postSticky(new Event.EventAct(mActivityType));
	   EventBusUtils.postSticky(mTCstInventoryTwoDto);
	   ToastUtils.showLong("未扫描到操作的耗材,即将返回主界面，请重新操作");
	   new Handler().postDelayed(new Runnable() {
		public void run() {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   finish();
		}
	   }, 3000);

	} else {

	   List<TCstInventoryVo> tCstInventoryVos = mTCstInventoryDto.gettCstInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
	   List<TCstInventoryVo> tCstInventoryVos1 = mTCstInventoryTwoDto.gettCstInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos1 = mTCstInventoryTwoDto.getDeviceInventoryVos();
	   //
	   Set<DeviceInventoryVo> set = new HashSet<DeviceInventoryVo>();
	   set.addAll(deviceInventoryVos);
	   set.addAll(deviceInventoryVos1);
	   List<DeviceInventoryVo> c = new ArrayList<DeviceInventoryVo>(set);
	   if (UIUtils.getConfigType(mContext, CONFIG_009)) {
		mTCstInventoryTwoDto.setBindType("afterBind");
	   } else {
		mTCstInventoryTwoDto.setBindType("firstBind");
	   }
	   tCstInventoryVos1.addAll(tCstInventoryVos);
	   tCstInventoryVos1.removeAll(tCstInventoryVos);
	   tCstInventoryVos1.addAll(tCstInventoryVos);
	   for (TCstInventoryVo ff : tCstInventoryVos1) {
	      LogUtils.i(TAG,"ff   "+ mPatient );
		if (UIUtils.getConfigType(mContext,CONFIG_010)&&UIUtils.getConfigType(mContext,CONFIG_012) ){
		   ff.setPatientName( mTCstInventoryDto.getPatientName());
		   ff.setPatientId( mTCstInventoryDto.getPatientId());
		   ff.setOperationScheduleId(mTCstInventoryDto.getOperationScheduleId());
		}else {
		   ff.setPatientName(mPatient);
		   ff.setPatientId(mPatientId);
		   ff.setOperationScheduleId(mOperationScheduleId);
		}
	   }

	   if (mTCstInventoryTwoDto.getPatientName() == null) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
	   }
	   mTCstInventoryTwoDto.settCstInventoryVos(tCstInventoryVos1);
	   mTCstInventoryTwoDto.setDeviceInventoryVos(c);
	   //
	   EventBusUtils.postSticky(new Event.EventAct(mActivityType));
	   EventBusUtils.postSticky(mTCstInventoryTwoDto);
	   //
	   //	}
	   //	if (mShowLoading != null) {
	   //	   mShowLoading.mDialog.dismiss();
	   //
	}
   }

   /**
    * 禁用系统返回键
    * @param keyCode
    * @param event
    * @return
    */
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   return true;
	}
	return super.onKeyDown(keyCode, event);

   }
}
