package high.rivamed.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.InBoxAllTwoActivity;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.activity.MessageActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutBoxFoutActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.activity.PatientConnActivity;
import high.rivamed.myapplication.activity.SelInOutBoxTwoActivity;
import high.rivamed.myapplication.activity.TemPatientBindActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.AllOutBean;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.CONFIG_011;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_014;
import static high.rivamed.myapplication.cont.Constants.CONFIG_015;
import static high.rivamed.myapplication.cont.Constants.CONFIG_016;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_DB;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_LY;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_RK;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_THUI;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_THUO;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YC;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YR;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_OPERATION_ROOM_NONAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack2;
import static high.rivamed.myapplication.views.RvDialog.sTableTypeView;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材操作主界面（使用中）
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentConsumeOperateFrag2 extends BaseSimpleFragment {

   String TAG = "ContentConsumeOperateFrag";
   @BindView(R.id.consume_openall_rv)
   RecyclerView mConsumeOpenallRv;
   @BindView(R.id.consume_openall_top)
   LinearLayout mConsumeOpenallTop;
   @BindView(R.id.function_title_meal)
   TextView     mFunctionTitleMeal;

   @BindView(R.id.fastopen_title_form)
   TextView     mFastopenTitleForm;
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
   @BindView(R.id.fastopen_title_guanlian)
   TextView       mFastopenTitleGuanlian;
   @BindView(R.id.rg_top_gone)
   View           mRgTopGone;
   @BindView(R.id.rg_middle_gone)
   View           mRgMiddleGone;
   @BindView(R.id.rg_down_gone)
   View           mRgDownGone;
   private LoadingDialog.Builder mShowLoading;
   private HomeFastOpenAdapter   mHomeFastOpenTopAdapter;
   private HomeFastOpenAdapter   mHomeFastOpenDownAdapter;

   private NoDialog.Builder                   mBuilder;
   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;
   //   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices2;

   private       int          mRbKey;
   private       BoxSizeBean  mBoxSizeBean;
   private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos = new ArrayList<>();
   private String           mRvEventString;
   private String           mPatientName;
   private String           mPatientId;
   private String           mFirstBind;
   private RvDialog.Builder mShowRvDialog;
   private String mOppenDoor = null;
   public static boolean          mPause               = true;
   private int                                mPosition;
   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevicesFromEvent;
   private RvDialog.Builder                   mShowRvDialog2;
   private String                             mOperationScheduleId;
   private Map<String, List<TagInfo>> mEPCDate   = new TreeMap<>();
   private Map<String, String> mEPCDatess = new TreeMap<>();
//   private List<String> mDoorList = 	   new ArrayList<>();

   int k = 0;
   private int mAllPage   = 1;
   private int mNoTemPage = 1;
   private int mRows      = 20;
   private LoadingDialog.Builder mLoading;
   private boolean               mIsClick;
   private List<TCstInventoryVo> mVoOutList;
   private String mTypeAct;
   private boolean mOnStart=false;

   /**
    * 开锁后禁止点击左侧菜单栏按钮(检测没有关门)
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClick(Event.HomeNoClickEvent event) {
	LogUtils.i(TAG, "event   " + event.isClick);
	LogUtils.i(TAG, "door   " + event.door);
	mIsClick = event.isClick;
	if (mIsClick) {
	   mRgTopGone.setVisibility(View.VISIBLE);
	   mRgMiddleGone.setVisibility(View.VISIBLE);
	   mRgDownGone.setVisibility(View.VISIBLE);
	} else {
	   mRgTopGone.setVisibility(View.GONE);
	   mRgMiddleGone.setVisibility(View.GONE);
	   mRgDownGone.setVisibility(View.GONE);
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoading event) {
	if (!mPause) {
	   if (event.loading) {
		if (mLoading == null) {
		   LogUtils.i(TAG, "     mLoading  新建 ");
		   mLoading = DialogUtils.showLoading(mContext);
		} else {
		   if (!mLoading.mDialog.isShowing()) {
			LogUtils.i(TAG, "     mLoading   重新开启");
			mLoading.create().show();
		   }
		}
	   } else {
		if (mLoading != null) {
		   mLoading.mAnimationDrawable.stop();
		   mLoading.mDialog.dismiss();
		   mLoading = null;
		}
	   }
	}
   }

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

   /**
    * 扫描后EPC准备传值
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	LogUtils.i(TAG, "epc  " + event.deviceId + "   " + event.epcs.size());
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (mRbKey == -1) {
	   List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
		   .find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeanss) {
		String box_id = boxIdBean.getBox_id();
		List<BoxIdBean> boxIdDoor = LitePal.where("box_id = ? and name = ?", box_id,
									   UHF_TYPE).find(BoxIdBean.class);
		for (BoxIdBean BoxIdBean : boxIdDoor) {
		   String device_id = BoxIdBean.getDevice_id();
		   for (int x = 0;x<mEthDeviceIdBack2.size();x++){
			if (device_id.equals(mEthDeviceIdBack2.get(x))){
			   mEthDeviceIdBack2.remove(x);
			}
		   }
		}
		if (box_id != null) {
		   List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
										READER_TYPE).find(BoxIdBean.class);
		   if (boxIdBeansss.size() > 1) {
			for (BoxIdBean BoxIdBean : boxIdBeansss) {
			   LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			   if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
				mEPCDate.putAll(event.epcs);
				k++;
				LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			   }
			}
			LogUtils.i(TAG, "mEPCDate  k  " + k);
			if (k == boxIdBeansss.size()) {
			   k = 0;
			   if (!mPause) {
				if (mEPCDate.size()==0){
				   mEPCDatess.put("",box_id);//没有空格
				}
				for (Map.Entry<String, List<TagInfo>> v : mEPCDate.entrySet()) {
				   mEPCDatess.put(v.getKey(),box_id);
				}
				LogUtils.i(TAG, "mEPCDates.mEPCDates()多reader  " + mEPCDatess.size());
			   } else {
				mEPCDate.clear();
				mEPCDatess.clear();
			   }
			}else {
			   return;
			}
		   } else {
			if (!mPause) {
			   if (event.epcs.size()==0){
				mEPCDatess.put(" ",box_id);//1个空格
			   }
			   for (Map.Entry<String, List<TagInfo>> v : event.epcs.entrySet()) {
				mEPCDatess.put(v.getKey(),box_id);
			   }
			   LogUtils.i(TAG, "mEPCDates.mEPCDates()单reader  " + mEPCDatess.size());
			}
		   }
		}
	   }
	   LogUtils.i(TAG, "mEthDeviceIdBack2.size()   " + mEthDeviceIdBack2.size());
	   LogUtils.i(TAG, "mIsClick   " + mIsClick);
	   if (mIsClick||mEthDeviceIdBack2.size()!=0) {
		return;
	   }
	   LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size());
	   if (!mPause) {
		putAllOutEPCDates(mEPCDatess);
	   }
	} else {
	   List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
		   .find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeanss) {
		String box_id = boxIdBean.getBox_id();
		if (box_id != null) {
		   List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
										READER_TYPE).find(BoxIdBean.class);
		   if (boxIdBeansss.size() > 1) {
			for (BoxIdBean BoxIdBean : boxIdBeansss) {
			   LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			   if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
				mEPCDate.putAll(event.epcs);
				k++;
				LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			   }
			}
			LogUtils.i(TAG, "mEPCDate  k  " + k);
			if (k == boxIdBeansss.size()) {
			   k = 0;
			   LogUtils.i(TAG, "mEPCDate  zou l  " + (k == boxIdBeansss.size()));
			   if (!mPause) {
				getDeviceDate(event.deviceId, mEPCDate);
			   } else {
				mEPCDate.clear();
			   }
			}
		   } else {
			if (!mPause) {
			   LogUtils.i(TAG, "event.epcs直接走   " + event.epcs);
			   getDeviceDate(event.deviceId, event.epcs);
			}
		   }
		}
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
	if (event.mBoolean) {
	   new Thread(new Runnable() {
		@Override
		public void run() {
		   LogUtils.i(TAG, "EventBoolean   " + mOppenDoor);
		   if (mOppenDoor == null) {
			LogUtils.i(TAG, "EventBoolean  进来1");
			DeviceManager.getInstance().UnRegisterDeviceCallBack();
			AllDeviceCallBack.getInstance().initCallBack();
		   } else {
			LogUtils.i(TAG, "EventBoolean  进来2");
			mOppenDoor = null;
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
	   TimelyAllFrag.mPauseS = true;
	   mEPCDatess.clear();
	   mEPCDate.clear();
//	   initData();
	} else {
	   //            mPause = true;
	   LogUtils.i(TAG, "UnRegisterDeviceCallBack");
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	mRvEventString = event.mString;
	LogUtils.i(TAG, "mRvEventString   " + mRvEventString);
	mAllPage = 1;
	mPatientInfos.clear();
	loadBingDate(mRvEventString, -2, null);
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onToast(Event.EventToast event) {
	mEthDeviceIdBack2.clear();
	Toast.makeText(mContext, event.mString, Toast.LENGTH_SHORT).show();
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvCheckBindEvent(Event.EventCheckbox event) {
	mFirstBind = event.type;
	mOperationScheduleId = event.operationScheduleId;
	if (event.type.equals("firstBind")) {
	   mPatientName = event.mString;
	   mPatientId = event.id;
	   LogUtils.i(TAG, "mPatientName   " + mPatientName);
	   LogUtils.i(TAG, "mPatientId   " + mPatientId);
	   mPosition = event.position;
	   mTbaseDevicesFromEvent = event.mTbaseDevices;
	   AllDeviceCallBack.getInstance().openDoor(mPosition, mTbaseDevicesFromEvent);
	} else {
	   String type = event.type;
	   String mString = event.mString;
	   int position = event.position;
	   List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices = event.mTbaseDevices;
	   mRvEventString = event.mString;
	   loadBingDate(mRvEventString, -1, null);
	}

   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onActString(Event.EventAct event) {
	if (event.mString.equals("RecognizeActivity")) {
	   //识别耗材重新扫描回调
	   LogUtils.i(TAG, "RecognizeActivity  重新扫描 ");
	   AllDeviceCallBack.getInstance().openDoor(mPosition, mTbaseDevicesFromEvent);
	}else {
	   mTypeAct = event.mString;
	}

   }

   public static ContentConsumeOperateFrag2 newInstance() {
	Bundle args = new Bundle();
	ContentConsumeOperateFrag2 fragment = new ContentConsumeOperateFrag2();
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
	mPause = false;
	EventBusUtils.register(this);
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	//	mShowLoading = DialogUtils.showLoading(mContext);
	//	initCallBack();
	AllDeviceCallBack.getInstance().initCallBack();
	mContentRbTb.setVisibility(View.GONE);
	mContentRbTb.setVisibility(View.GONE);
	mContentRg.setVisibility(View.GONE);
	if (mEPCDate!=null){
	   mEPCDate.clear();
	}
	initData();

   }

   /**
    * 设置选择操作的权限
    */
   private void setDownType() {
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_LY)){//领用
	   mContentRbLy.setVisibility(View.VISIBLE);
	}else {
	   mContentRbLy.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_RK)){//入库
	   mContentRbRk.setVisibility(View.VISIBLE);
	}else {
	   mContentRbRk.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_YC)){//移出
	   mContentRbYc.setVisibility(View.VISIBLE);
	}else {
	   mContentRbYc.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_YR)){//移入
	   mContentRbYr.setVisibility(View.VISIBLE);
	}else {
	   mContentRbYr.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_DB)){//调拨
	   mContentRbTb.setVisibility(View.VISIBLE);
	}else {
	   mContentRbTb.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_THUI)){//退回
	   mContentRbTuihui.setVisibility(View.VISIBLE);
	}else {
	   mContentRbTuihui.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext,DOWN_MENU_THUO)){//退货
	   mContentRbTuihuo.setVisibility(View.VISIBLE);
	}else {
	   mContentRbTuihuo.setVisibility(View.GONE);
	}
   }

   @Override
   public void onStart() {
	EventBusUtils.register(this);
	mOnStart = true;
	LogUtils.i(TAG, "onStart   ");
	super.onStart();
   }

   @Override
   public void onResume() {
	super.onResume();
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}

   }

   private void goToPatientConn() {
	LogUtils.i(TAG, "result   ");
	NetRequest.getInstance().findTempPatients("", this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null &&
		    bingFindSchedulesBean.getPatientInfos().size() > 0) {
		   mContext.startActivity(new Intent(mContext, PatientConnActivity.class));
		} else {
		   ToastUtils.showShort("没有患者数据");
		}
	   }
	});
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
	   tCstInventoryDto.setOperationScheduleId(mOperationScheduleId);
	}
	String toJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	mEPCDate.clear();
	mEPCDatess.clear();
	putEPCDates(toJson);

   }

   /**
    * 快速开柜出柜查询
    * @param epcs
    */
   private void putAllOutEPCDates(Map<String, String> epcs) {
	String toJson = getEpcDtoString(epcs);
	mEPCDate.clear();
	mEPCDatess.clear();
	NetRequest.getInstance().putAllOutEPCDate(toJson, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result mObject   " + result);
		TCstInventoryDto cstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
		mVoOutList = cstInventoryDto.gettCstInventoryVos();
		for (int i = 0; i < mVoOutList.size(); i++) {
		   mVoOutList.get(i).setSelected(true);
		}
		LogUtils.i(TAG, "mVoOutList mObject   " + mVoOutList.size());
//		putAllInEPCDate(toJson);//用于判断出柜提示显示

		String string = null;
		if (cstInventoryDto.getErrorEpcs() != null &&
		    cstInventoryDto.getErrorEpcs().size() > 0) {
		   string = StringUtils.listToString(cstInventoryDto.getErrorEpcs());
		   ToastUtils.showLong(string);
		   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
		}
		if (mVoOutList != null && mVoOutList.size() != 0) {
		   LogUtils.i(TAG, "跳出柜" +toJson);
		   putAllInEPCDate(toJson);
		   EventBusUtils.postSticky(new Event.EventOutDto(cstInventoryDto,toJson));
		   mContext.startActivity(new Intent(mContext, OutBoxFoutActivity.class));
//		   EventBusUtils.postSticky(cstInventoryDto);
		} else {
		   putAllInEPCDate(toJson);
		}
	   }
	});
   }

   /**
    * 快速开柜epc放入DTO
    * @param epcs
    * @return
    */
   private String getEpcDtoString(Map<String, String> epcs) {
	AllOutBean allOutBean = new AllOutBean();
	List<AllOutBean.TCstInventoryVos> epcList = new ArrayList<>();
	for (Map.Entry<String, String> v : epcs.entrySet()) {
	   AllOutBean.TCstInventoryVos tCstInventory = new AllOutBean.TCstInventoryVos();
	   tCstInventory.setEpc(v.getKey());
	   tCstInventory.setDeviceCode(v.getValue());
	   epcList.add(tCstInventory);
	}
	allOutBean.setTCstInventoryVos(epcList);
	allOutBean.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	String toJson = mGson.toJson(allOutBean);
	LogUtils.i(TAG, "toJson mObject   " + toJson);
	return toJson;
   }

    /**
     * 快速开柜入柜查询
     */
    private void putAllInEPCDate(String json) {
        NetRequest.getInstance().putAllInEPCDate(json, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result sIn   " + result);
                TCstInventoryDto cstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
                String string = null;
                if (cstInventoryDto.getErrorEpcs() != null &&
                        cstInventoryDto.getErrorEpcs().size() > 0) {
                    string = StringUtils.listToString(cstInventoryDto.getErrorEpcs());
                    ToastUtils.showLong(string);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
                }
                if (mVoOutList != null && mVoOutList.size() == 0 && cstInventoryDto.gettCstInventoryVos() != null &&
                        cstInventoryDto.gettCstInventoryVos().size() != 0) {
                    LogUtils.i(TAG, "跳入入柜 ");
                    mContext.startActivity(new Intent(mContext, InBoxAllTwoActivity.class));
                    EventBusUtils.postSticky(new Event.EventAct("all"));
                    EventBusUtils.postSticky(cstInventoryDto);
                } else {
                    LogUtils.i(TAG, "显示提示");
                    if (cstInventoryDto.gettCstInventoryVos().size() > 0) {
                        EventBusUtils.postSticky(new Event.EventOutTitleV(true));
                        LogUtils.i(TAG, "xianshi ");
                    } else {
                        LogUtils.i(TAG, "mVoOutList 2s   " + mVoOutList.size());
                        if (mVoOutList != null && mVoOutList.size() == 0) {
				   mEthDeviceIdBack2.clear();
                            mEthDeviceIdBack.clear();
                            Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    /**
     * 选择开柜查询
     *
     * @param toJson
     */
    public void putEPCDates(String toJson) {
        if (mLoading != null) {
            mLoading.mAnimationDrawable.stop();
            mLoading.mDialog.dismiss();
            mLoading = null;
        }
        NetRequest.getInstance().putEPCDate(toJson, _mActivity,  new BaseResult() {
            @Override
            public void onSucceed(String result) {
                Log.i(TAG, "result    " + result);
                TCstInventoryDto cstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
                String string = null;
                if (cstInventoryDto.getErrorEpcs() != null &&
                        cstInventoryDto.getErrorEpcs().size() > 0) {
                    string = StringUtils.listToString(cstInventoryDto.getErrorEpcs());
                    ToastUtils.showLong(string);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
                    return;
                }
                LogUtils.i(TAG, "我跳转    " + (cstInventoryDto.gettCstInventoryVos() == null));
                //先绑定患者
                if (mFirstBind != null && mFirstBind.equals("firstBind") && mRbKey == 3) {
                    if (cstInventoryDto.gettCstInventoryVos() != null &&
                            cstInventoryDto.gettCstInventoryVos().size() != 0) {
                        for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {
                            tCstInventoryVo.setPatientName(cstInventoryDto.getPatientName());
                            tCstInventoryVo.setPatientId(cstInventoryDto.getPatientId());
                            tCstInventoryVo.setOperationScheduleId(
                                    cstInventoryDto.getOperationScheduleId());
                        }
                        cstInventoryDto.setBindType("firstBind");
                        //                        mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
                        mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
//					.putExtra("patientName",
//                                        cstInventoryDto.getPatientName())
//                                        .putExtra("patientId", cstInventoryDto.getPatientId())
//                                        .putExtra("operationScheduleId",
//                                                cstInventoryDto.getOperationScheduleId()));
                        EventBusUtils.postSticky(new Event.EventOutBoxBingDto(cstInventoryDto));
                    } else {
			     mEthDeviceIdBack2.clear();
                        mEthDeviceIdBack.clear();
                        Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
                        if (mBuilder != null) {
                            mBuilder.mDialog.dismiss();
                            mBuilder = null;
                        }
                    }
                } else if (UIUtils.getConfigType(mContext, CONFIG_009) && mRbKey == 3) {//后绑定患者
                    LogUtils.i(TAG, "后绑定患者DDDDDD");

		   if (cstInventoryDto.gettCstInventoryVos() != null &&
			 cstInventoryDto.gettCstInventoryVos().size() != 0) {
			for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {
			   tCstInventoryVo.setPatientName(cstInventoryDto.getPatientName());
			   tCstInventoryVo.setPatientId(cstInventoryDto.getPatientId());
			   tCstInventoryVo.setOperationScheduleId(
				   cstInventoryDto.getOperationScheduleId());
			}
			cstInventoryDto.setOperation(mRbKey);
			cstInventoryDto.setBindType("afterBind");
			mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
			EventBusUtils.postSticky(new Event.EventOutBoxBingDto(cstInventoryDto));
		   } else {
			mEthDeviceIdBack2.clear();
			mEthDeviceIdBack.clear();
			Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
			if (mBuilder != null) {
			   mBuilder.mDialog.dismiss();
			   mBuilder = null;
			}
		   }
		} else {//正常的领用或者其他正常操作
		   //		   mShowLoading.mDialog.dismiss();
		   if (cstInventoryDto.gettCstInventoryVos() == null ||
			 cstInventoryDto.gettCstInventoryVos().size() < 1) {
			if (mBuilder != null) {
			   mBuilder.mDialog.dismiss();
			}
			mEthDeviceIdBack.clear();
			Toast.makeText(mContext, "未扫描到操作的耗材", Toast.LENGTH_SHORT).show();
		   } else {
			LogUtils.i(TAG, "我跳转    " + cstInventoryDto.getType());
			EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
			if (cstInventoryDto.getType() == 0) {//放入
			   if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 ||
				 mRbKey == 10 || mRbKey == 7 || mRbKey == 8) {
				Intent intent2 = new Intent(mContext, SelInOutBoxTwoActivity.class);
				mContext.startActivity(intent2);
				EventBusUtils.postSticky(new Event.EventAct("inout"));
				cstInventoryDto.setOperation(mRbKey);
//				EventBusUtils.postSticky(cstInventoryDto);
				EventBusUtils.postSticky(new Event.EventSelInOutBoxDto(cstInventoryDto));
			   }
			} else {//拿出
			   if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 ||
				 mRbKey == 10 || mRbKey == 7 || mRbKey == 8) {
				Intent intent2 = new Intent(mContext, SelInOutBoxTwoActivity.class);
				mContext.startActivity(intent2);
				EventBusUtils.postSticky(new Event.EventAct("inout"));
				cstInventoryDto.setOperation(mRbKey);
//				EventBusUtils.postSticky(cstInventoryDto);
				EventBusUtils.postSticky(new Event.EventSelInOutBoxDto(cstInventoryDto));
			   }
			}
		   }
		}
	   }
	});
   }

   private void initData() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (UIUtils.getConfigType(mContext, CONFIG_011)&&mConsumeOpenallTop!=null) {
	   mConsumeOpenallTop.setVisibility(View.VISIBLE);
	} else {
	   mConsumeOpenallTop.setVisibility(View.GONE);
	}
//	//是否启用选择操作
//	if (UIUtils.getConfigType(mContext, CONFIG_019)){
//	   mConsumeDown.setVisibility(View.VISIBLE);
//	}else {
//	   mConsumeDown.setVisibility(View.GONE);
//	}
	//是否启用功能开柜
	if (UIUtils.getConfigType(mContext, CONFIG_016)) {
	   mConsumeOpenallMiddle.setVisibility(View.VISIBLE);

	   //是否启用套餐领用
	   if (UIUtils.getConfigType(mContext, CONFIG_014)) {
		mFunctionTitleMeal.setVisibility(View.VISIBLE);
	   } else {
		mFunctionTitleMeal.setVisibility(View.GONE);
	   }

	   //是否启用请领单领用
	   if (UIUtils.getConfigType(mContext, CONFIG_015)) {
		mFastopenTitleForm.setVisibility(View.VISIBLE);
	   } else {
		mFastopenTitleForm.setVisibility(View.GONE);
	   }

	   //是否启用关联患者
	   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
		mFastopenTitleGuanlian.setVisibility(View.VISIBLE);
	   } else {
		mFastopenTitleGuanlian.setVisibility(View.GONE);
	   }
	} else {
	   mConsumeOpenallMiddle.setVisibility(View.GONE);
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
		LogUtils.i(TAG, "result  " + result);
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
	mContentRg.setVisibility(View.VISIBLE);

	LogUtils.i(TAG, "onSucceedDate");
	//	mConsumeOpenallMiddle.setVisibility(View.GONE);//此处部分医院不需要可以隐藏  根据接口来
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材操作");
	if (SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME) != null) {
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " +
					   SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
	}
	if (SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME) != null) {
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " +
					   SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME));
	}

	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

	mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									  mTbaseDevices);
	mConsumeOpenallRv.setLayoutManager(layoutManager);
	mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

	mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		mRbKey = -1;
		mPause = false;
		AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	   }
	});
	LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
	layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

	if (mTbaseDevices.size() < 2) {
	   //如果只有一个柜子,就隐藏柜子列表
	   mConsumeDownRv.setVisibility(View.GONE);
	}
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
		   //点击柜子进行操作
		   if (UIUtils.isFastDoubleClick()) {
			return;
		   }
		   doSelectOption(position, id);
		}
	   }
	});
	String string = SPUtils.getString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE);
	List<HomeAuthorityMenuBean> fromJson = mGson.fromJson(string,
										new TypeToken<List<HomeAuthorityMenuBean>>() {}
											.getType());
	if (fromJson.get(0).getChildren().size()>0) {
		setDownType();//设置选择操作的权限
	   } else {
		mConsumeDown.setVisibility(View.GONE);
		mConsumeDownRv.setVisibility(View.GONE);

	}
   }

   /*
    * 选择操作
    * */
   private void doSelectOption(int position, int id) {
	EventBusUtils.postSticky(new Event.EventButGone(false));
	mPause = false;
	switch (id) {
	   case R.id.content_rb_ly://领用
		lingYong(position);
		break;
	   case R.id.content_rb_rk://入库
		inBoxClick(position);
		break;
	   case R.id.content_rb_yc://移出
		outBoxClick(position);
		break;
	   case R.id.content_rb_tb://调拨
		allotClick(position);
		break;
	   case R.id.content_rb_yr://移入
		moveInClick(position);
		break;
	   case R.id.content_rb_tuihui://退回
		returnClick(position);
		break;
	   case R.id.content_rb_tuihuo://退货
		sealsReturnClick(position);
		break;
	}
   }

   /*
		 点击退货
		 * */
   private void sealsReturnClick(int position) {
	mRbKey = 8;
	ToastUtils.showShort("退货！");//拿出
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
	   点击退回
	   * */
   private void returnClick(int position) {
	mRbKey = 7;
	ToastUtils.showShort("退回！");//拿入
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
	点击移入
	* */
   private void moveInClick(int position) {
	mRbKey = 10;
	ToastUtils.showShort("移入！");//拿入
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击调拨
   * */
   private void allotClick(int position) {
	mRbKey = 11;
	ToastUtils.showShort("调拨！");//拿出
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击移出
   * */
   private void outBoxClick(int position) {
	mRbKey = 9;
	ToastUtils.showShort("移出！");//拿出
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
	点击入库
	* */
   private void inBoxClick(int position) {
	mRbKey = 2;
	ToastUtils.showShort("入库！");//拿入
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
    * 点击领用
    * */
   private void lingYong(int position) {
	mRbKey = 3;
	ToastUtils.showShort("领用！");//拿出
	if (UIUtils.getConfigType(mContext, CONFIG_007) &&
	    UIUtils.getConfigType(mContext, CONFIG_010) &&
	    !UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //先绑定患者再开柜，不启动临时患者
	   LogUtils.i(TAG, "先绑定患者再开柜，不启动临时患者");
	   //                                loadBingDate("", position, mTbaseDevices);
	   mNoTemPage = 1;
	   loadBingDateNoTemp("", position, mTbaseDevices);
	} else if (UIUtils.getConfigType(mContext, CONFIG_007) &&
		     UIUtils.getConfigType(mContext, CONFIG_010) &&
		     UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //先绑定患者，启动临时患者
	   LogUtils.i(TAG, "先绑定患者，启动临时患者");

	   goToFirstBindAC(position);
	} else if (UIUtils.getConfigType(mContext, CONFIG_007) &&
		     UIUtils.getConfigType(mContext, CONFIG_009) &&
		     !UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //后绑定患者，不启用临时患者
	   LogUtils.i(TAG, "后绑定患者，不启用临时患者");
	   AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);

	} else if (UIUtils.getConfigType(mContext, CONFIG_007) &&
		     UIUtils.getConfigType(mContext, CONFIG_009) &&
		     UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //后绑定患者，启用临时患者
	   AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	   LogUtils.i(TAG, "后绑定患者，启用临时患者");
	} else {
	   //不绑定患者
	   LogUtils.i(TAG, "不绑定患者");
	   AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	}
	EventBusUtils.postSticky(new Event.EventAct("inout"));
	return;
   }

   private void goToFirstBindAC(int position) {
	//获取需要绑定的患者
	NetRequest.getInstance().findSchedulesDate("", mAllPage, mRows, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		if (UIUtils.getConfigType(mContext, CONFIG_012)) {
		   mContext.startActivity(
			   new Intent(mContext, TemPatientBindActivity.class).putExtra("position",
													   position)
				   .putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices));
		} else {
		   if (bingFindSchedulesBean != null &&
			 bingFindSchedulesBean.getPatientInfos() != null &&
			 bingFindSchedulesBean.getPatientInfos().size() > 0) {
			mContext.startActivity(
				new Intent(mContext, TemPatientBindActivity.class).putExtra("position",
														position)
					.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices));
		   } else {
			ToastUtils.showShort("没有患者数据");
		   }
		}

	   }
	});
   }

   @Override
   public void onPause() {
	mPause = true;
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	mEthDeviceIdBack2.clear();
	mEPCDate.clear();
	mEPCDatess.clear();
	EventBusUtils.unregister(this);
	super.onPause();
   }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.function_title_meal, R.id.fastopen_title_form,
            R.id.content_rb_ly, R.id.content_rb_rk, R.id.content_rb_yc, R.id.content_rb_tb,
            R.id.content_rb_yr, R.id.content_rb_tuihui, R.id.content_rb_tuihuo, R.id.fastopen_title_guanlian})
    public void onViewClicked(View view) {
        if (!mIsClick) {
            switch (view.getId()) {
                case R.id.base_tab_icon_right:
                case R.id.base_tab_tv_name:
                    mPopupWindow = new SettingPopupWindow(mContext);
                    mPopupWindow.showPopupWindow(mBaseTabIconRight);
                    popupClick();
                    break;
                case R.id.base_tab_tv_outlogin:
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
                            MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);

                        }
                    });
                    builder.create().show();
                    break;
                case R.id.base_tab_btn_msg:
                    mContext.startActivity(new Intent(mContext, MessageActivity.class));
                    break;
                case R.id.function_title_meal://套组
                    if (UIUtils.getConfigType(mContext, CONFIG_014)) {
                        mContext.startActivity(new Intent(mContext, OutMealActivity.class));
                        EventBusUtils.postSticky(new Event.EventAct("NOBING_MEAL"));
                    } else {
                        ToastUtils.showShort("此功能暂未开放");
                    }

		   break;
		case R.id.fastopen_title_form://医嘱
		   if (UIUtils.getConfigType(mContext, CONFIG_015)) {
			mContext.startActivity(new Intent(mContext, OutFormActivity.class));
		   } else {
			ToastUtils.showShort("此功能暂未开放");
			//		   DialogUtils.showRegisteDialog(mContext, _mActivity);
		   }
		   break;
		case R.id.fastopen_title_guanlian://患者关联
		   if (UIUtils.isFastDoubleClick()) {
			return;
		   }else {
			goToPatientConn();//患者关联
		   }
		   break;
	   }
	   //选择操作监听
	   if (null != mTbaseDevices && mTbaseDevices.size() == 1) {
		if (UIUtils.isFastDoubleClick()) {
		   return;
		}else {
		   doSelectOption(0, view.getId());
		}
	   }
	} else {
	   ToastUtils.showShort("请关闭柜门再进行操作");
	}

   }

   /**
    * 获取需要绑定的患者
    */
   private void loadBingDate(
	   String optienNameOrId, int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	LogUtils.i(TAG, "optienNameOrId   " + optienNameOrId);
	NetRequest.getInstance()
		.findSchedulesDate(optienNameOrId, mAllPage, mRows, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result   " + result);
			FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
			if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {

			   if (mPatientInfos != null) {
				for (int i = 0; i < bean.getRows().size(); i++) {
				   BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
				   data.setPatientId(bean.getRows().get(i).getPatientId());
				   data.setPatientName(bean.getRows().get(i).getPatientName());
				   data.setDeptName(bean.getRows().get(i).getDeptName());
				   data.setOperationSurgeonName(
					   bean.getRows().get(i).getOperationSurgeonName());
				   data.setOperatingRoomNoName(
					   bean.getRows().get(i).getOperatingRoomNoName());
				   data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
				   data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				   data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
				   data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
				   mPatientInfos.add(data);
				}
				if (mShowRvDialog != null && mShowRvDialog.mDialog.isShowing()) {
				   sTableTypeView.mBingOutAdapter.notifyDataSetChanged();
				} else {
				   if (!mPause) {
					mShowRvDialog = DialogUtils.showRvDialog(_mActivity, mContext,
											     mPatientInfos, "firstBind",
											     position, mTbaseDevices);
					mShowRvDialog.mRefreshLayout.setOnRefreshListener(
						new OnRefreshListener() {
						   @Override
						   public void onRefresh(RefreshLayout refreshLayout) {
							mShowRvDialog.mRefreshLayout.setNoMoreData(false);
							mAllPage = 1;
							mPatientInfos.clear();
							loadBingDate(optienNameOrId, position, mTbaseDevices);
							mShowRvDialog.mRefreshLayout.finishRefresh();
						   }
						});
					mShowRvDialog.mRefreshLayout.setOnLoadMoreListener(
						new OnLoadMoreListener() {
						   @Override
						   public void onLoadMore(RefreshLayout refreshLayout) {
							mAllPage++;
							loadBingDate(optienNameOrId, position, mTbaseDevices);
							mShowRvDialog.mRefreshLayout.finishLoadMore();
						   }
						});
				   }
				}

			   } else {
				if (!mPause) {
				   for (int i = 0; i < bean.getRows().size(); i++) {
					BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
					data.setPatientId(bean.getRows().get(i).getPatientId());
					data.setPatientName(bean.getRows().get(i).getPatientName());
					data.setDeptName(bean.getRows().get(i).getDeptName());
					data.setOperationSurgeonName(
						bean.getRows().get(i).getOperationSurgeonName());
					data.setOperatingRoomNoName(
						bean.getRows().get(i).getOperatingRoomNoName());
					data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
					data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
					data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
					data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
					mPatientInfos.add(data);
				   }
				   mShowRvDialog = DialogUtils.showRvDialog(_mActivity, mContext,
											  mPatientInfos, "firstBind",
											  position, mTbaseDevices);
				   mShowRvDialog.mRefreshLayout.setOnRefreshListener(
					   new OnRefreshListener() {
						@Override
						public void onRefresh(RefreshLayout refreshLayout) {
						   mShowRvDialog.mRefreshLayout.setNoMoreData(false);
						   mAllPage = 1;
						   mPatientInfos.clear();
						   loadBingDate(optienNameOrId, position, mTbaseDevices);
						   mShowRvDialog.mRefreshLayout.finishRefresh();
						}
					   });
				   mShowRvDialog.mRefreshLayout.setOnLoadMoreListener(
					   new OnLoadMoreListener() {
						@Override
						public void onLoadMore(RefreshLayout refreshLayout) {
						   mAllPage++;
						   loadBingDate(optienNameOrId, position, mTbaseDevices);
						   mShowRvDialog.mRefreshLayout.finishLoadMore();
						}
					   });
				}
			   }
			}
		   }
		});

   }

   /**
    * 获取需要绑定的患者（不包含临时患者）
    */
   private void loadBingDateNoTemp(
	   String optienNameOrId, int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	LogUtils.i(TAG, "optienNameOrId   " + optienNameOrId);
	NetRequest.getInstance()
		.findSchedulesDate(optienNameOrId, mNoTemPage, mRows, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result   " + result);

			FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
			if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {

			   if (mPatientInfos != null) {
				for (int i = 0; i < bean.getRows().size(); i++) {
				   BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
				   data.setPatientId(bean.getRows().get(i).getPatientId());
				   data.setPatientName(bean.getRows().get(i).getPatientName());
				   data.setDeptName(bean.getRows().get(i).getDeptName());
				   data.setOperationSurgeonName(
					   bean.getRows().get(i).getOperationSurgeonName());
				   data.setOperatingRoomNoName(
					   bean.getRows().get(i).getOperatingRoomNoName());
				   data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
				   data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				   data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
				   data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
				   mPatientInfos.add(data);
				}
				if (mShowRvDialog2 != null && mShowRvDialog2.mDialog.isShowing()) {
				   sTableTypeView.mBingOutAdapter.notifyDataSetChanged();
				} else {
				   mShowRvDialog2 = DialogUtils.showRvDialog(_mActivity, mContext,
											   mPatientInfos, "firstBind",
											   position, mTbaseDevices);
				   mShowRvDialog2.mRefreshLayout.setOnRefreshListener(
					   new OnRefreshListener() {
						@Override
						public void onRefresh(RefreshLayout refreshLayout) {
						   mShowRvDialog2.mRefreshLayout.setNoMoreData(false);
						   mNoTemPage = 1;
						   mPatientInfos.clear();
						   loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
						   mShowRvDialog2.mRefreshLayout.finishRefresh();
						}
					   });
				   mShowRvDialog2.mRefreshLayout.setOnLoadMoreListener(
					   new OnLoadMoreListener() {
						@Override
						public void onLoadMore(RefreshLayout refreshLayout) {
						   mNoTemPage++;
						   loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
						   mShowRvDialog2.mRefreshLayout.finishLoadMore();
						}
					   });
				}
			   } else {
				for (int i = 0; i < bean.getRows().size(); i++) {
				   BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
				   data.setPatientId(bean.getRows().get(i).getPatientId());
				   data.setPatientName(bean.getRows().get(i).getPatientName());
				   data.setDeptName(bean.getRows().get(i).getDeptName());
				   data.setOperationSurgeonName(
					   bean.getRows().get(i).getOperationSurgeonName());
				   data.setOperatingRoomNoName(
					   bean.getRows().get(i).getOperatingRoomNoName());
				   data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
				   data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				   data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
				   data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
				   mPatientInfos.add(data);
				}
				mShowRvDialog2 = DialogUtils.showRvDialog(_mActivity, mContext,
											mPatientInfos, "firstBind",
											position, mTbaseDevices);
				mShowRvDialog2.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
				   @Override
				   public void onRefresh(RefreshLayout refreshLayout) {
					mShowRvDialog2.mRefreshLayout.setNoMoreData(false);
					mNoTemPage = 1;
					mPatientInfos.clear();
					loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
					mShowRvDialog2.mRefreshLayout.finishRefresh();
				   }
				});
				mShowRvDialog2.mRefreshLayout.setOnLoadMoreListener(
					new OnLoadMoreListener() {
					   @Override
					   public void onLoadMore(RefreshLayout refreshLayout) {
						mNoTemPage++;
						loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
						mShowRvDialog2.mRefreshLayout.finishLoadMore();
					   }
					});
			   }
			} else {
			   if (mNoTemPage == 1) {
				ToastUtils.showShort("没有患者数据");
			   }
			}
		   }
		});
   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();

	mOnStart=false;
	mEthDeviceIdBack2.clear();
   }
}
