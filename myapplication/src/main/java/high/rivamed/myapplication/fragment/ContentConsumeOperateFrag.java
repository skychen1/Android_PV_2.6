package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.FastInOutBoxActivity;
import high.rivamed.myapplication.activity.HomeActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.activity.PatientConnActivity;
import high.rivamed.myapplication.activity.SelInOutBoxTwoActivity;
import high.rivamed.myapplication.activity.TemPatientBindActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.bean.RobotBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
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
import high.rivamed.myapplication.views.OpenDoorDialog;

import static high.rivamed.myapplication.activity.HomeActivity.mHomeRgGone;
import static high.rivamed.myapplication.base.App.HOME_COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE_HOME;
import static high.rivamed.myapplication.cont.Constants.CONFIG_011;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_014;
import static high.rivamed.myapplication.cont.Constants.CONFIG_015;
import static high.rivamed.myapplication.cont.Constants.CONFIG_058;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW01;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW02;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW04;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW05;
import static high.rivamed.myapplication.cont.Constants.CONSUMABLE_TYPE;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_DB;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_LY;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_LYTH;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_RK;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_THUI;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_THUO;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YC;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YR;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE_ALL;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;
import static high.rivamed.myapplication.cont.Constants.TEMP_AFTERBIND;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.service.ScanService.mDoorStatusType;
import static high.rivamed.myapplication.utils.ToastUtils.cancel;
import static high.rivamed.myapplication.utils.UIUtils.getMenuOnlyType;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getLocalAllCstVos;

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

public class ContentConsumeOperateFrag extends BaseSimpleFragment {

   String TAG = "ContentConsumeOperateFrag";
   @BindView(R.id.consume_openall_rv)
   RecyclerView mConsumeOpenallRv;
   @BindView(R.id.consume_openall_top)
   LinearLayout mConsumeOpenallTop;
   @BindView(R.id.function_title_meal)
   TextView     mFunctionTitleMeal;

   @BindView(R.id.fastopen_title_form)
   TextView       mFastopenTitleForm;
   @BindView(R.id.consume_openall_middle)
   RelativeLayout mConsumeOpenallMiddle;
   @BindView(R.id.consume_openall_down)
   LinearLayout   mConsumeOpenallDown;
   @BindView(R.id.content_rb_lyth)
   RadioButton    mContentRbLyTh;
   @BindView(R.id.content_rb_ly)
   RadioButton    mContentRbLy;
   @BindView(R.id.content_rb_rk)
   RadioButton    mContentRbRk;
   @BindView(R.id.content_rb_yc)
   RadioButton    mContentRbYc;
   @BindView(R.id.content_rb_tb)
   RadioButton    mContentRbTb;
   @BindView(R.id.content_rb_yr)
   RadioButton    mContentRbYr;
   @BindView(R.id.content_rb_tuihui)
   RadioButton    mContentRbTuihui;
   @BindView(R.id.content_rb_tuihuo)
   RadioButton    mContentRbTuihuo;
   @BindView(R.id.content_rg)
   RadioGroup     mContentRg;
   @BindView(R.id.consume_down_rv)
   RecyclerView   mConsumeDownRv;
   @BindView(R.id.consume_down)
   LinearLayout   mConsumeDown;
   @BindView(R.id.fastopen_title_guanlian)
   TextView       mFastopenTitleGuanlian;
   @BindView(R.id.rg_top_gone)
   View           mRgTopGone;
   @BindView(R.id.rg_middle_gone)
   View           mRgMiddleGone;
   @BindView(R.id.rg_down_gone)
   View           mRgDownGone;
   private HomeFastOpenAdapter mHomeFastOpenTopAdapter;
   private HomeFastOpenAdapter mHomeFastOpenDownAdapter;

   private       OpenDoorDialog.Builder                           mBuilder;
   private       List<BoxSizeBean.DevicesBean>                    mTbaseDevices      = new ArrayList<>();
   private       List<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean> mDeviceTypeVoBeans = new ArrayList<>();
   private       int                                              mRbKey;
   public static boolean                                          mPause             = true;
   private       int                                              mAllPage           = 1;
   private       int                                              mRows              = 20;
   private       LoadingDialog.Builder                            mLoading;
   private       ArrayList<String>                                mEthDevices        = new ArrayList<>();
   private       List<String>                                     mDeviceSizeList    = new ArrayList<>();
   private       ArrayList<String>                                mListDevices;
   private       ArrayList<String>                                mOrderIds;
   private       TimeCountOver                                    mCountOver;

   /**
    * 主页倒计时结束发起
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventOverHome(Event.EventOverHome event) {
	if (event.b) {
	   if (mCountOver != null) {
		mCountOver.cancel();
		MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
		UIUtils.removeAllAct(mContext);
	   }
	}
   }

   /**
    * 整单入库的单号
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onOrderVosEvent(Event.OrderVosEvent event) {
	if (event.vos != null) {
	   mOrderIds = event.vos;
	   doSelectOption(event.mDeviceId, R.id.content_rb_rk);
	}
   }

   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventDoorStatus(Event.EventDoorStatus event) {

	if (event.type) {//门没关
	   noClossDoor();
	}
	if (!event.type) {//门关了
	   yesClossDoor(event);
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onSelectOption(Event.SelectOption event) {
	doSelectOption(event.deviceId, event.id);
   }

   /*
   门关完了
    */
   private void yesClossDoor(Event.EventDoorStatus event) {

	for (Object o : mDeviceSizeList) {
	   String s = (String) o;
	   if (s.equals(event.id) && !event.type) {
		mEthDevices.add(s);
		Log.i("onDoorStates", "s   " + s);
		mListDevices = StringUtils.removeDuplicteUsers(mEthDevices);
	   }
	}
	if (mDeviceSizeList.size() == mListDevices.size()) {
	   mDoorStatusType = true;
	   mRgTopGone.setVisibility(View.GONE);
	   mRgMiddleGone.setVisibility(View.GONE);
	   mRgDownGone.setVisibility(View.GONE);
	   mHomeRgGone.setVisibility(View.GONE);
	   //	   UIUtils.enableRadioGroup(mHomeRg);
	   Log.i("ssffff", "关门");
	   UIUtils.enableRadioGroup(mContentRg);
	   //	   mEthDeviceIdBack.clear();
	   mListDevices.clear();
	   mEthDevices.clear();
	}
   }

   /*
   没关门
    */
   private void noClossDoor() {
	mDoorStatusType = false;
	mEthDevices.clear();
	if (mListDevices != null) {
	   mListDevices.clear();
	}
	if (mCountOver != null) {
	   mCountOver.cancel();
	}
	Log.i("ssffff", "没关门");
	UIUtils.disableRadioGroup(mContentRg);
	mRgTopGone.setVisibility(View.VISIBLE);
	mRgMiddleGone.setVisibility(View.VISIBLE);
	mRgDownGone.setVisibility(View.VISIBLE);
	mHomeRgGone.setVisibility(View.VISIBLE);
	//	UIUtils.disableRadioGroup(mHomeRg);
	return;
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLoading(Event.EventLoading event) {
	if (!mPause) {
	   if (event.loading) {
		if (mLoading == null) {
		   mLoading = DialogUtils.showLoading(mContext);
		} else {
		   if (!mLoading.mDialog.isShowing()) {
			mLoading.create().show();
		   }
		}
	   } else {
		if (mLoading != null) {
		   mLoading.mAnimationDrawable.stop();
		   if (mLoading.mHandler != null) {
			mLoading.mHandler.removeCallbacksAndMessages(null);
		   }
		   mLoading.mDialog.dismiss();
		   mLoading = null;
		}
	   }
	}
   }

   /**
    * 门锁的提示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (!mPause && event.isMute) {
	   Log.i("outtccc", "开门的接收    " + mRbKey);
	   if (event.openDoorType) {
		MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	   } else {
		MusicPlayer.getInstance().play(MusicPlayer.Type.QRS_MOREOPEN);
	   }

	   if (mBuilder == null) {
		mBuilder = DialogUtils.showOpenDoorDialog(mContext, event.mString);
	   }
	   if (mCountOver != null) {
		mCountOver.cancel();
	   }
	}
	if (!mPause && !event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   Log.i("outtccc", "关门的接收    " + mRbKey);
	   if (mBuilder != null) {
		if (mBuilder.mHandler != null) {
		   mBuilder.mHandler.removeCallbacksAndMessages(null);
		}
		mBuilder.mDialog.dismiss();
		mBuilder = null;
	   }
	   if (mRbKey != -3) {
		intentActType(event.mEthId);
	   }
	   Log.i("outtccc", "event.mEthId    " + mRbKey);
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
	   mEthDeviceIdBack.clear();
	} else {
	}
   }

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	EventBusUtils.register(this);
	List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", CONSUMABLE_TYPE).find(BoxIdBean.class);
	for (BoxIdBean idBean : boxIdBeans) {
	   if (idBean != null && idBean.getCabinetType() != null) {
		if (idBean.getCabinetType().equals("0") || idBean.getCabinetType().equals("1")) {
		   mDeviceSizeList.add(idBean.getDevice_id() + "0");
		} else if (idBean.getCabinetType().equals("2")) {
		   mDeviceSizeList.add(idBean.getDevice_id() + "1");
		}
	   }
	}
	getLocalAllCstVos();
   }

   public static ContentConsumeOperateFrag newInstance() {
	Bundle args = new Bundle();

	ContentConsumeOperateFrag fragment = new ContentConsumeOperateFrag();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.ctconsumeoperate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	Log.i("outtccc", "initDataAndEvent   " + mRbKey);
	mPause = false;
	mContentRbTb.setVisibility(View.GONE);
	mContentRg.setVisibility(View.VISIBLE);
	initData();
	initListener();

   }

   private void initListener() {
	if (null != mTbaseDevices && mTbaseDevices.size() == 1) {
	   String deviceId = mTbaseDevices.get(0).getDeviceId();
	   mContentRbLyTh.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_lyth);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbLy.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_ly);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbRk.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   if (UIUtils.getConfigType(mContext, CONFIG_058)) {
			DialogUtils.showInBoxBillDialog(mContext, deviceId);
		   } else {
			doSelectOption(deviceId, R.id.content_rb_rk);
		   }
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbYc.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_yc);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbTb.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_tb);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbYr.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_yr);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbTuihui.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_tuihui);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });
	   mContentRbTuihuo.setOnClickListener(view -> {
		if (!UIUtils.isFastDoubleClick3()) {
		   doSelectOption(deviceId, R.id.content_rb_tuihuo);
		} else {
		   ToastUtils.showShortToast("请勿频繁操作！");
		}
	   });

	}

   }

   /**
    * 设置选择操作的权限
    */
   private void setDownType() {
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_LYTH)) {//领用、退回合并按钮
	   mContentRbLyTh.setVisibility(View.VISIBLE);
	} else {
	   mContentRbLyTh.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_LY)) {//领用
	   mContentRbLy.setVisibility(View.VISIBLE);
	} else {
	   mContentRbLy.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_RK)) {//入库
	   mContentRbRk.setVisibility(View.VISIBLE);
	} else {
	   mContentRbRk.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_YC)) {//移出
	   mContentRbYc.setVisibility(View.VISIBLE);
	} else {
	   mContentRbYc.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_YR)) {//移入
	   mContentRbYr.setVisibility(View.VISIBLE);
	} else {
	   mContentRbYr.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_DB)) {//调拨
	   mContentRbTb.setVisibility(View.VISIBLE);
	} else {
	   mContentRbTb.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_THUI)) {//退回
	   mContentRbTuihui.setVisibility(View.VISIBLE);
	} else {
	   mContentRbTuihui.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuDownType(mContext, DOWN_MENU_THUO)) {//退货
	   mContentRbTuihuo.setVisibility(View.VISIBLE);
	} else {
	   mContentRbTuihuo.setVisibility(View.GONE);
	}
   }

   private HomeActivity.MyTouchListener myTouchListener = new HomeActivity.MyTouchListener() {
	@Override
	public void onTouch(MotionEvent ev) {
	   switch (ev.getAction()) {
		case MotionEvent.ACTION_UP://门关、本界面
		   if (mDoorStatusType) {
			if (mCountOver != null) {
			   mCountOver.start();
			}
		   } else {
			if (mCountOver != null) {
			   mCountOver.cancel();
			}
		   }
		   break;
		default://切换到其他界面、门锁打开停止
		   if (mCountOver != null) {
			mCountOver.cancel();
		   }
		   break;
	   }
	}
   };

   @Override
   public void onResume() {
	super.onResume();
	if (mCountOver == null) {
	   mCountOver = new TimeCountOver(HOME_COUNTDOWN_TIME, 1000);
	   if (mDoorStatusType) {
		mCountOver.start();
	   } else {
		mCountOver.cancel();
	   }
	} else {
	   if (mDoorStatusType) {
		mCountOver.cancel();
		mCountOver.start();
	   } else {
		mCountOver.cancel();
	   }
	}
	((HomeActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   if (mLoading.mHandler != null) {
		mLoading.mHandler.removeCallbacksAndMessages(null);
	   }
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
   }

   private void goToPatientConn() {
	LogUtils.i(TAG, "result   ");
	NetRequest.getInstance().findTempPatients("", this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		if (bingFindSchedulesBean != null &&
		    bingFindSchedulesBean.getPatientInfoVos() != null &&
		    bingFindSchedulesBean.getPatientInfoVos().size() > 0) {
		   mContext.startActivity(new Intent(mContext, PatientConnActivity.class));
		} else {
		   ToastUtils.showShortToast("没有患者数据");
		}
	   }
	});
   }

   /**
    * 关门后的跳转
    */
   private void intentActType(String mEthId) {
	Log.i("outtccc", "intentActType    " + mRbKey);
	//快速开柜
	if (mRbKey == -1) {
	   mContext.startActivity(
		   new Intent(mContext, FastInOutBoxActivity.class).putExtra("mEthId", mEthId));
	}
	//后绑定患者
	else if ((UIUtils.getConfigType(mContext, CONFIG_BPOW01) ||
		    UIUtils.getConfigType(mContext, CONFIG_BPOW04)) && (mRbKey == 3 || mRbKey == 4)) {
	   mContext.startActivity(
		   new Intent(mContext, OutBoxBingActivity.class).putExtra("OperationType", mRbKey)
			   .putExtra("bindType", TEMP_AFTERBIND)
			   .putExtra("mEthId", mEthId));
	}
	//正常的领用或者其他正常操作
	else if (mRbKey == 3 || mRbKey == 4 || mRbKey == 9 || mRbKey == 11 || mRbKey == 10 ||
		   mRbKey == 7 || mRbKey == 8) {
	   mContext.startActivity(
		   new Intent(mContext, SelInOutBoxTwoActivity.class).putExtra("OperationType", mRbKey)
			   .putExtra("mEthId", mEthId));
	} else if (mRbKey == 2) {//入库要区分是否有入库单
	   if (UIUtils.getConfigType(mContext, CONFIG_058)) {
		mContext.startActivity(
			new Intent(mContext, SelInOutBoxTwoActivity.class).putExtra("OperationType",
													mRbKey)
				.putExtra("mEthId", mEthId)
				.putStringArrayListExtra("orderids", mOrderIds));
	   } else {
		mContext.startActivity(
			new Intent(mContext, SelInOutBoxTwoActivity.class).putExtra("OperationType",
													mRbKey)
				.putExtra("mEthId", mEthId));
	   }

	}
   }

   private void initData() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   if (mLoading.mHandler != null) {
		mLoading.mHandler.removeCallbacksAndMessages(null);
	   }
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (UIUtils.getConfigType(mContext, CONFIG_011) && mConsumeOpenallTop != null) {
	   mConsumeOpenallTop.setVisibility(View.VISIBLE);
	} else {
	   mConsumeOpenallTop.setVisibility(View.GONE);
	}

	//是否启用功能开柜

	//是否启用套餐领用
	if (UIUtils.getConfigType(mContext, CONFIG_014)) {
	   mConsumeOpenallMiddle.setVisibility(View.VISIBLE);
	   mFunctionTitleMeal.setVisibility(View.VISIBLE);
	} else {
	   mConsumeOpenallMiddle.setVisibility(View.GONE);
	   mFunctionTitleMeal.setVisibility(View.GONE);
	}

	//是否启用请领单领用
	if (UIUtils.getConfigType(mContext, CONFIG_015)) {
	   mConsumeOpenallMiddle.setVisibility(View.VISIBLE);
	   mFastopenTitleForm.setVisibility(View.VISIBLE);
	} else {
	   mFunctionTitleMeal.setVisibility(View.GONE);
	   mFastopenTitleForm.setVisibility(View.GONE);
	}

	//是否启用关联患者
	if (UIUtils.getConfigType(mContext, CONFIG_012)) {
	   mConsumeOpenallMiddle.setVisibility(View.VISIBLE);
	   mFastopenTitleGuanlian.setVisibility(View.VISIBLE);
	} else {
	   mConsumeOpenallMiddle.setVisibility(View.GONE);
	   mFastopenTitleGuanlian.setVisibility(View.GONE);
	}

	loadDate();
   }

   //数据加载
   private void loadDate() {
	//	String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	//	LogUtils.i(TAG, "loadDate   " + string);
	//	if (string != null) {
	//	   mTbaseDevices.addAll(
	//		   mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType()));
	//	} else {
	String strings = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	if (!TextUtils.isEmpty(strings)) {
	   mTbaseDevices.addAll(mGson.fromJson(strings,
							   new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType()));
	}
	//	}
	String homeBoxSize = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE_HOME);
	Log.i("outtccc", "position    " + homeBoxSize);
	if (!TextUtils.isEmpty(homeBoxSize)) {
	   mDeviceTypeVoBeans.addAll(mGson.fromJson(homeBoxSize,
								  new TypeToken<List<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean>>() {}
									  .getType()));
	}
	if (mDeviceTypeVoBeans.size() > 0) {
	   mDeviceTypeVoBeans.get(0).getDevices().get(0).setDeviceId("");
	}
	//柜子唯一，无功能开柜，不能先绑定患者，只有领用/退回功能
	if (mTbaseDevices.size() == 1 && !UIUtils.getConfigType(mContext, CONFIG_012) &&
	    !UIUtils.getConfigType(mContext, CONFIG_014) &&
	    !UIUtils.getConfigType(mContext, CONFIG_015) &&
	    (UIUtils.getConfigType(mContext, CONFIG_BPOW01) ||
	     UIUtils.getConfigType(mContext, CONFIG_BPOW04)) &&
	    getMenuOnlyType(mContext, DOWN_MENU_LYTH)) {
	   String deviceId = mTbaseDevices.get(0).getDeviceId();
	   doSelectOption(deviceId, R.id.content_rb_lyth);
	}

	onSucceedDate();
   }

   //赋值
   private void onSucceedDate() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材操作");
	mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " +
					SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

	mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(mDeviceTypeVoBeans, mContentRg);
	mConsumeOpenallRv.setLayoutManager(layoutManager);
	mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

	mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		if (mTitleConn) {
		   mRbKey = -1;
		   mPause = false;
		   String deviceId = mTbaseDevices.get(position).getDeviceId();
		   AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
	   }
	});
	LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
	layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

	if (mTbaseDevices.size() < 2) {
	   //如果只有一个柜子,就隐藏柜子列表
	   mConsumeDownRv.setVisibility(View.GONE);
	}

	mHomeFastOpenDownAdapter = new HomeFastOpenAdapter(mDeviceTypeVoBeans, mContentRg);
	mConsumeDownRv.setLayoutManager(layoutManager2);
	mConsumeDownRv.setAdapter(mHomeFastOpenDownAdapter);

	//	mHomeFastOpenDownAdapter.mDataAdapter.setOnItemClickListener(
	//		new BaseQuickAdapter.OnItemClickListener() {
	//		   @Override
	//		   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
	//
	//			if (id == -1) {
	//			   ToastUtils.showShortToast("请选择操作方式！");
	//			} else {
	//			   //点击柜子进行操作
	//			   BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX item = (BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX) adapter
	//				   .getItem(position);
	//			   String deviceId = item.getDeviceId();
	//			   Log.i("deviceId", "deviceId    " + deviceId+"    "+item.getDeviceName() );
	//			   if (!UIUtils.isFastDoubleClick3()) {
	//
	//				doSelectOption(position, id);
	//			   } else {
	//				ToastUtils.showShortToast("请勿频繁操作！");
	//			   }
	//			}
	//		   }
	//		});
	boolean aBoolean = SPUtils.getBoolean(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE_ALL);
	if (aBoolean) {
	   setDownType();//设置选择操作的权限
	} else {
	   mConsumeOpenallDown.setVisibility(View.GONE);
	   mConsumeDown.setVisibility(View.GONE);
	   mConsumeDownRv.setVisibility(View.GONE);
	}
   }

   /*
    * 选择操作
    * */
   public void doSelectOption(String deviceId, int id) {
	EventBusUtils.postSticky(new Event.EventButGone(false));
	mPause = false;
	switch (id) {
	   case R.id.content_rb_lyth://领用或者退回
		ToastUtils.showShortToast("领用/退回！");//拿出
		lingYongAndBack(deviceId, 4);
		break;
	   case R.id.content_rb_ly://领用
		ToastUtils.showShortToast("领用！");//拿出
		lingYongAndBack(deviceId, 3);
		break;
	   case R.id.content_rb_rk://入库
		if (mTitleConn) {
		   inBoxClick(deviceId);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持领用！");
		}
		break;
	   case R.id.content_rb_yc://移出
		if (mTitleConn) {
		   outBoxClick(deviceId);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持领用！");
		}
		break;
	   case R.id.content_rb_tb://调拨
		if (mTitleConn) {
		   allotClick(deviceId);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	   case R.id.content_rb_yr://移入
		if (mTitleConn) {
		   moveInClick(deviceId);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	   case R.id.content_rb_tuihui://退回
		if (mTitleConn) {
		   returnClick(deviceId);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	   case R.id.content_rb_tuihuo://退货
		if (mTitleConn) {
		   sealsReturnClick(deviceId);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	}
   }

   /*
    点击退货
    * */
   private void sealsReturnClick(String deviceId) {
	mRbKey = 8;
	ToastUtils.showShortToast("退货！");//拿出
	AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击退回
   */
   private void returnClick(String deviceId) {
	mRbKey = 7;
	ToastUtils.showShortToast("退回！");//拿入
	AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
    点击移入
   */
   private void moveInClick(String deviceId) {
	mRbKey = 10;
	ToastUtils.showShortToast("移入！");//拿入
	AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击调拨
   */
   private void allotClick(String deviceId) {
	mRbKey = 11;
	ToastUtils.showShortToast("调拨！");//拿出
	AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击移出
   */
   private void outBoxClick(String deviceId) {
	mRbKey = 9;
	ToastUtils.showShortToast("移出！");//拿出
	AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
	点击入库
	* */
   private void inBoxClick(String deviceId) {
	mRbKey = 2;
	ToastUtils.showShortToast("入库！");//拿入
	AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
    * 点击领用
    * */
   private void lingYongAndBack(String deviceId, int mkey) {
	mRbKey = mkey;

	if ((UIUtils.getConfigType(mContext, CONFIG_BPOW05) ||
	     UIUtils.getConfigType(mContext, CONFIG_BPOW02)) &&
	    !UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //先绑定患者再开柜，不启动临时患者
	   LogUtils.i(TAG, "先绑定患者再开柜，不启动临时患者");
	   mAllPage = 1;
	   if (mTitleConn) {
		goToFirstBindAC(deviceId, "GONE");
	   } else {
		errorBind(deviceId, "GONE");
	   }
	} else if ((UIUtils.getConfigType(mContext, CONFIG_BPOW05) ||
			UIUtils.getConfigType(mContext, CONFIG_BPOW02)) &&
		     UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //先绑定患者，启动临时患者
	   LogUtils.i(TAG, "先绑定患者，启动临时患者");
	   mAllPage = 1;
	   if (mTitleConn) {
		goToFirstBindAC(deviceId, "VISIBLE");
	   } else {
		errorBind(deviceId, "VISIBLE");
	   }
	} else if ((UIUtils.getConfigType(mContext, CONFIG_BPOW04) ||
			UIUtils.getConfigType(mContext, CONFIG_BPOW01)) &&
		     !UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //后绑定患者，不启用临时患者
	   LogUtils.i(TAG, "后绑定患者，不启用临时患者");
	   AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	} else if ((UIUtils.getConfigType(mContext, CONFIG_BPOW04) ||
			UIUtils.getConfigType(mContext, CONFIG_BPOW01)) &&
		     UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //后绑定患者，启用临时患者
	   AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	   LogUtils.i(TAG, "后绑定患者，启用临时患者");
	} else {
	   //不绑定患者
	   LogUtils.i(TAG, "不绑定患者");
	   AllDeviceCallBack.getInstance().openDoor(deviceId, mTbaseDevices);
	}
	EventBusUtils.postSticky(new Event.EventAct("inout"));
	return;
   }

   private void goToFirstBindAC(String deviceId, String gonetype) {
	//获取需要绑定的患者
	NetRequest.getInstance().findSchedulesDate("", "", mAllPage, mRows, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
		Log.i(TAG, "deviceId   " + deviceId);
		Log.i(TAG, "mTbaseDevices   " + mTbaseDevices.size());
		if (UIUtils.getConfigType(mContext, CONFIG_012)) {
		   mContext.startActivity(
			   new Intent(mContext, TemPatientBindActivity.class).putExtra("deviceId",
													   deviceId)
				   .putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices)
				   .putExtra("mRbKey", mRbKey)
				   .putExtra("GoneType", gonetype));
		} else {
		   if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
			mContext.startActivity(
				new Intent(mContext, TemPatientBindActivity.class).putExtra("deviceId",
														deviceId)
					.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices)
					.putExtra("mRbKey", mRbKey)
					.putExtra("GoneType", gonetype));
		   } else {
			ToastUtils.showShortToast("没有患者数据，如需创建临时患者领用，请到管理端进行配置");
		   }
		}
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		errorBind(deviceId, gonetype);
	   }
	});
   }

   /**
    * 无网直接跳到患者界面
    *
    * @param deviceId
    * @param gonetype
    */
   private void errorBind(String deviceId, String gonetype) {
	if (UIUtils.getConfigType(mContext, CONFIG_012)) {
	   mContext.startActivity(
		   new Intent(mContext, TemPatientBindActivity.class).putExtra("deviceId", deviceId)
			   .putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices)
			   .putExtra("mRbKey", mRbKey)
			   .putExtra("GoneType", gonetype));
	}
   }

   @Override
   public void onPause() {
	mPause = true;
	super.onPause();
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.function_title_meal, R.id.fastopen_title_form,
	   R.id.fastopen_title_guanlian, R.id.base_tab_robot, R.id.rg_down_gone, R.id.rg_top_gone,
	   R.id.rg_middle_gone})
   public void onViewClicked(View view) {
	if (mDoorStatusType) {
	   super.onViewClicked(view);
	   switch (view.getId()) {
		//		case R.id.base_tab_icon_right:
		//		case R.id.base_tab_tv_name:
		//		   mPopupWindow = new SettingPopupWindow(mContext);
		//		   mPopupWindow.showPopupWindow(mBaseTabIconRight);
		//		   popupClick();
		//		   break;
		//		case R.id.base_tab_tv_outlogin:
		//		   TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
		//		   builder.setTwoMsg("您确认要退出登录吗?");
		//		   builder.setMsg("温馨提示");
		//		   builder.setLeft("取消", new DialogInterface.OnClickListener() {
		//			@Override
		//			public void onClick(DialogInterface dialog, int i) {
		//			   dialog.dismiss();
		//			}
		//		   });
		//		   builder.setRight("确认", new DialogInterface.OnClickListener() {
		//			@Override
		//			public void onClick(DialogInterface dialog, int i) {
		//			   removeAllAct(mContext);
		//			   dialog.dismiss();
		//			   MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
		//			}
		//		   });
		//		   builder.create().show();
		//		   break;
		//		case R.id.base_tab_btn_msg:
		//		   if (mTitleConn) {
		//			mContext.startActivity(new Intent(mContext, MessageActivity.class));
		//		   } else {
		//			ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		//		   }
		//		   break;
		case R.id.function_title_meal://套组
		   if (UIUtils.getConfigType(mContext, CONFIG_014)) {
			if (mTitleConn) {
			   mContext.startActivity(new Intent(mContext, OutMealActivity.class));
			   EventBusUtils.postSticky(new Event.EventAct("NOBING_MEAL"));
			} else {
			   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
			}
		   } else {
			ToastUtils.showShortToast("此功能暂未开放");
		   }

		   break;
		case R.id.fastopen_title_form://医嘱
		   if (UIUtils.getConfigType(mContext, CONFIG_015)) {
			if (mTitleConn) {
			   mContext.startActivity(new Intent(mContext, OutFormActivity.class));
			} else {
			   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
			}
		   } else {
			ToastUtils.showShortToast("此功能暂未开放");
			//		   DialogUtils.showRegisteDialog(mContext, _mActivity);
		   }
		   break;
		case R.id.fastopen_title_guanlian://患者关联
		   if (UIUtils.isFastDoubleClick(R.id.fastopen_title_guanlian)) {
			return;
		   } else {
			if (mTitleConn) {
			   goToPatientConn();//患者关联
			} else {
			   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
			}
		   }
		   break;
		case R.id.base_tab_robot://机器人
		   if (UIUtils.isFastDoubleClick(R.id.base_tab_robot)) {
			NetRequest.getInstance().CallRobot(_mActivity, new BaseResult() {
			   @Override
			   public void onSucceed(String result) {
				RobotBean robotBean = mGson.fromJson(result, RobotBean.class);
				if (robotBean.isOperateSuccess()) {
				   if (robotBean.getReciveMessage().getResultCode() == 0) {
					ToastUtils.showShortToast("机器人召唤成功！");
				   } else {
					ToastUtils.showShortToast("机器人召唤失败！");
				   }
				}
				Log.e(TAG, "机器人    " + result);
			   }
			});
		   }
		   break;
	   }

	} else {
	   ToastUtils.showShortToast(getString(R.string.content_clossdoor_text));
	}

   }

   @Override
   public void onStop() {
	super.onStop();
	if (mCountOver != null) {
	   mCountOver.cancel();
	}
	if (mBuilder != null) {
	   if (mBuilder.mHandler != null) {
		mBuilder.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuilder.mDialog.dismiss();
	   mBuilder = null;
	}
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   if (mLoading.mHandler != null) {
		mLoading.mHandler.removeCallbacksAndMessages(null);
	   }
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	cancel();
   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();
	EventBusUtils.unregister(this);
	mEthDevices = null;
	mListDevices = null;
	if (mOrderIds != null) {
	   mOrderIds.clear();
	}
	if (mCountOver != null) {
	   mCountOver.cancel();
	}
   }
}
