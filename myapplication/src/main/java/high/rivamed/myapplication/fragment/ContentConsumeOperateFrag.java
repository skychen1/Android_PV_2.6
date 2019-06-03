package high.rivamed.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.activity.MessageActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.activity.PatientConnActivity;
import high.rivamed.myapplication.activity.SelInOutBoxTwoActivity;
import high.rivamed.myapplication.activity.TemPatientBindActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.App;
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
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.activity.HomeActivity.mHomeRg;
import static high.rivamed.myapplication.activity.HomeActivity.mHomeRgGone;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.CONFIG_011;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_014;
import static high.rivamed.myapplication.cont.Constants.CONFIG_015;
import static high.rivamed.myapplication.cont.Constants.CONFIG_016;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_DB;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_LY;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_RK;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_THUI;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_THUO;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YC;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YR;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE_ALL;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
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
   LinearLayout mConsumeOpenallDown;
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

   private NoDialog.Builder mBuilder;
   private List<BoxSizeBean.DevicesBean> mTbaseDevices = new ArrayList<>();
   private int mRbKey;
   public static boolean mPause   = true;
   private       int     mAllPage = 1;
   private       int     mRows    = 20;
   private LoadingDialog.Builder mLoading;
   private boolean           mDoorStatus     = true;
   private ArrayList<String> mEthDevices     = new ArrayList<>();
   private List<String>      mDeviceSizeList = new ArrayList<>();
   private ArrayList<String> mListDevices;
   private String mYesClossId;

   /**
    * 门锁的状态检测回调
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

   /*
   门关完了
    */
   private void yesClossDoor(Event.EventDoorStatus event) {
	for (Object o : mDeviceSizeList) {
	   String s = (String) o;
	   if (s.equals(event.id) && !event.type) {
		mEthDevices.add(s);
		mListDevices = StringUtils.removeDuplicteUsers(mEthDevices);
	   }
	}
	if (mDeviceSizeList.size() == mListDevices.size()) {
	   mDoorStatus = true;
	   mRgTopGone.setVisibility(View.GONE);
	   mRgMiddleGone.setVisibility(View.GONE);
	   mRgDownGone.setVisibility(View.GONE);
	   mHomeRgGone.setVisibility(View.GONE);
	   UIUtils.enableRadioGroup(mHomeRg);
	   Log.i("ssffff", "关门");
	   UIUtils.enableRadioGroup(mContentRg);
	   mEthDeviceIdBack.clear();
	   mListDevices.clear();
	   mEthDevices.clear();
	}
   }

   /*
   没关门
    */
   private void noClossDoor() {
	mDoorStatus = false;
	mEthDevices.clear();
	if (mListDevices != null) {
	   mListDevices.clear();
	}
	Log.i("ssffff", "没关门");
	UIUtils.disableRadioGroup(mContentRg);
	mRgTopGone.setVisibility(View.VISIBLE);
	mRgMiddleGone.setVisibility(View.VISIBLE);
	mRgDownGone.setVisibility(View.VISIBLE);
	mHomeRgGone.setVisibility(View.VISIBLE);
	UIUtils.disableRadioGroup(mHomeRg);
	return;
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

   /**
    * 门锁的提示
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (!mPause && event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	}
	if (!mPause && !event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   intentActType(event.mEthId);
	}
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
	   LogUtils.i(TAG, "UnRegisterDeviceCallBack");
	}
   }

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean idBean : boxIdBeans) {
	   mDeviceSizeList.add(idBean.getDevice_id());
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
	AllDeviceCallBack.getInstance().initEth002();
	mPause = false;
	mContentRbTb.setVisibility(View.GONE);
	mContentRg.setVisibility(View.VISIBLE);
	initData();
   }

   /**
    * 设置选择操作的权限
    */
   private void setDownType() {
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
		   ToastUtils.showShort("没有患者数据");
		}
	   }
	});
   }

   /**
    * 关门后的跳转
    */
   private void intentActType(String mEthId) {
	//快速开柜
	if (mRbKey == -1) {
	   mContext.startActivity(new Intent(mContext, FastInOutBoxActivity.class).putExtra("mEthId", mEthId));
	}
	//后绑定患者
	else if (UIUtils.getConfigType(mContext, CONFIG_009) && mRbKey == 3) {
	   mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class)
			   .putExtra("OperationType", mRbKey)
			   .putExtra("bindType", "afterBind")
			   .putExtra("mEthId", mEthId));
	}
	//正常的领用或者其他正常操作
	else if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 || mRbKey == 10 ||
		   mRbKey == 7 || mRbKey == 8) {
	   mContext.startActivity(new Intent(mContext, SelInOutBoxTwoActivity.class)
			   .putExtra("OperationType", mRbKey)
	               .putExtra("mEthId", mEthId));
	}
   }

   private void initData() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (UIUtils.getConfigType(mContext, CONFIG_011) && mConsumeOpenallTop != null) {
	   mConsumeOpenallTop.setVisibility(View.VISIBLE);
	} else {
	   mConsumeOpenallTop.setVisibility(View.GONE);
	}

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
	String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	LogUtils.i(TAG, "loadDate   " + string);

	if (string != null) {
	   mTbaseDevices.addAll(
		   mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType()));
	} else {
	   String strings = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	   mTbaseDevices.addAll(mGson.fromJson(strings,
							   new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType()));
	}

	onSucceedDate();

   }

   //赋值
   private void onSucceedDate() {

	LogUtils.i(TAG, "onSucceedDate");
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材操作");
	mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " +
					SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));

	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

	mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									  mTbaseDevices);
	mConsumeOpenallRv.setLayoutManager(layoutManager);
	mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

	mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		if (mTitleConn) {
		   mRbKey = -1;
		   mPause = false;
		   AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
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
		   doSelectOption(position, id);
		}
	   }
	});
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
   private void doSelectOption(int position, int id) {
	EventBusUtils.postSticky(new Event.EventButGone(false));
	mPause = false;
	switch (id) {
	   case R.id.content_rb_ly://领用
		lingYong(position);
		break;
	   case R.id.content_rb_rk://入库
		if (mTitleConn) {
		   inBoxClick(position);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持领用！");
		}
		break;
	   case R.id.content_rb_yc://移出
		if (mTitleConn) {
		   outBoxClick(position);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持领用！");
		}
		break;
	   case R.id.content_rb_tb://调拨
		if (mTitleConn) {
		   allotClick(position);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	   case R.id.content_rb_yr://移入
		if (mTitleConn) {
		   moveInClick(position);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	   case R.id.content_rb_tuihui://退回
		if (mTitleConn) {
		   returnClick(position);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
		break;
	   case R.id.content_rb_tuihuo://退货
		if (mTitleConn) {
		   sealsReturnClick(position);
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
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
   */
   private void returnClick(int position) {
	mRbKey = 7;
	ToastUtils.showShort("退回！");//拿入
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
    点击移入
   */
   private void moveInClick(int position) {
	mRbKey = 10;
	ToastUtils.showShort("移入！");//拿入
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击调拨
   */
   private void allotClick(int position) {
	mRbKey = 11;
	ToastUtils.showShort("调拨！");//拿出
	AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	EventBusUtils.postSticky(new Event.EventAct("inout"));
   }

   /*
   点击移出
   */
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
	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
	     UIUtils.getConfigType(mContext, CONFIG_019)) &&
	    UIUtils.getConfigType(mContext, CONFIG_010) &&
	    !UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //先绑定患者再开柜，不启动临时患者
	   LogUtils.i(TAG, "先绑定患者再开柜，不启动临时患者");
	   //                                loadBingDate("", position, mTbaseDevices);
	   mAllPage = 1;
	   goToFirstBindAC(position, "GONE");
	   //	   loadBingDateNoTemp("", position, mTbaseDevices);
	} else if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
			UIUtils.getConfigType(mContext, CONFIG_019)) &&
		     UIUtils.getConfigType(mContext, CONFIG_010) &&
		     UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //先绑定患者，启动临时患者
	   LogUtils.i(TAG, "先绑定患者，启动临时患者");
	   mAllPage = 1;
	   goToFirstBindAC(position, "VISIBLE");
	} else if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
			UIUtils.getConfigType(mContext, CONFIG_019)) &&
		     UIUtils.getConfigType(mContext, CONFIG_009) &&
		     !UIUtils.getConfigType(mContext, CONFIG_012)) {
	   //后绑定患者，不启用临时患者
	   LogUtils.i(TAG, "后绑定患者，不启用临时患者");
	   AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
	} else if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
			UIUtils.getConfigType(mContext, CONFIG_019)) &&
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

   private void goToFirstBindAC(int position, String gonetype) {
	//获取需要绑定的患者
	NetRequest.getInstance().findSchedulesDate("", "", mAllPage, mRows, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);

		if (UIUtils.getConfigType(mContext, CONFIG_012)) {
		   mContext.startActivity(
			   new Intent(mContext, TemPatientBindActivity.class).putExtra("position",
													   position)
				   .putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices)
				   .putExtra("GoneType", gonetype));
		} else {
		   if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
			mContext.startActivity(
				new Intent(mContext, TemPatientBindActivity.class).putExtra("position",
														position)
					.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices)
					.putExtra("GoneType", gonetype));
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
	EventBusUtils.unregister(this);
	super.onPause();
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.function_title_meal, R.id.fastopen_title_form,
	   R.id.content_rb_ly, R.id.content_rb_rk, R.id.content_rb_yc, R.id.content_rb_tb,
	   R.id.content_rb_yr, R.id.content_rb_tuihui, R.id.content_rb_tuihuo,
	   R.id.fastopen_title_guanlian, R.id.base_tab_robot, R.id.rg_down_gone, R.id.rg_top_gone,
	   R.id.rg_middle_gone})
   public void onViewClicked(View view) {
	if (mDoorStatus) {
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
		   if (mTitleConn) {
			mContext.startActivity(new Intent(mContext, MessageActivity.class));
		   } else {
			ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		   }
		   break;
		case R.id.function_title_meal://套组
		   if (UIUtils.getConfigType(mContext, CONFIG_014)) {
			if (mTitleConn) {
			   mContext.startActivity(new Intent(mContext, OutMealActivity.class));
			   EventBusUtils.postSticky(new Event.EventAct("NOBING_MEAL"));
			} else {
			   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
			}
		   } else {
			ToastUtils.showShort("此功能暂未开放");
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
			ToastUtils.showShort("此功能暂未开放");
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
	   //选择操作监听
	   if (null != mTbaseDevices && mTbaseDevices.size() == 1) {
		if (UIUtils.isFastDoubleClick(view.getId())) {
		   return;
		} else {
		   doSelectOption(0, view.getId());
		}
	   }
	} else {
	   ToastUtils.showShortToast(getString(R.string.content_clossdoor_text));
	}

   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();
	mEthDevices = null;
	mListDevices = null;
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
   }
}
