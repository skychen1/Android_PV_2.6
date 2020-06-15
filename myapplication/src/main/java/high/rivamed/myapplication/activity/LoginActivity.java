package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.rivamed.FingerManager;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dbmodel.UserFeatureInfosBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.FingerLoginDto;
import high.rivamed.myapplication.dto.IdCardLoginDto;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.fragment.LoginFaceFragment;
import high.rivamed.myapplication.fragment.LoginPassWordFragment;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.service.ScanService;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LoginUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.CustomViewPager;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.activity.SplashActivity.mIntentService;
import static high.rivamed.myapplication.base.App.CLOSSLIGHT_TIME;
import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.HOME_COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.NOEPC_LOGINOUT_TIME;
import static high.rivamed.myapplication.base.App.mPushFormOrders;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.base.BaseSimpleActivity.mLightTimeCount;
import static high.rivamed.myapplication.cont.Constants.ACCESS_TOKEN;
import static high.rivamed.myapplication.cont.Constants.CONFIG_015;
import static high.rivamed.myapplication.cont.Constants.CONFIG_017;
import static high.rivamed.myapplication.cont.Constants.CONFIG_026;
import static high.rivamed.myapplication.cont.Constants.CONFIG_034;
import static high.rivamed.myapplication.cont.Constants.CONFIG_043;
import static high.rivamed.myapplication.cont.Constants.CONFIG_044;
import static high.rivamed.myapplication.cont.Constants.CONFIG_045;
import static high.rivamed.myapplication.cont.Constants.CONFIG_046;
import static high.rivamed.myapplication.cont.Constants.CONFIG_060;
import static high.rivamed.myapplication.cont.Constants.FINGER_TYPE;
import static high.rivamed.myapplication.cont.Constants.FINGER_VERSION;
import static high.rivamed.myapplication.cont.Constants.IC_TYPE;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_FACE_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_ICON;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.LOGIN_TYPE_FINGER;
import static high.rivamed.myapplication.cont.Constants.LOGIN_TYPE_IC;
import static high.rivamed.myapplication.cont.Constants.PATIENT_TYPE;
import static high.rivamed.myapplication.cont.Constants.REFRESH_TOKEN;
import static high.rivamed.myapplication.cont.Constants.SAVE_CLOSSLIGHT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_HOME_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE_ALL;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_NOEPC_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.utils.ToastUtils.cancel;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:05
 * 描述:        登录界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginActivity extends SimpleActivity {

   private static final String TAG = "LoginActivity";
   @BindView(R.id.login_logo)
   ImageView       mLoginLogo;
   @BindView(R.id.login_face)
   RadioButton     mLoginFace;
   @BindView(R.id.login_pass)
   RadioButton     mLoginPass;
   @BindView(R.id.login_radiogroup)
   RadioGroup      mLoginRadiogroup;
   @BindView(R.id.login_viewpager)
   CustomViewPager mLoginViewpager;
   @BindView(R.id.chart1)
   BarChart        mChart;
   @BindView(R.id.down_text)
   TextView        mDownText;
   @BindView(R.id.login_stock_status)
   TextView        mStockStatus;
   @BindView(R.id.left_guo_text)
   TextView        mTextGuo;
   @BindView(R.id.login_uninbox)
   TextView        mTVLoginToBePutInStorage;
   @BindView(R.id.right_top_text)
   TextView         mRightTopText;
   @BindView(R.id.right_down_text)
   TextView         mRightDownText;
   @BindView(R.id.login_unconfirmcst)
   TextView        mTVLoginUnConfirmCst;
   @BindView(R.id.right_r)
   RelativeLayout  mRightRl;
   @BindView(R.id.right_top)
   LinearLayout    mRightTopL;
   @BindView(R.id.right_down_l)
   LinearLayout    mRightDownL;
   @BindView(R.id.login_unrl)
   RelativeLayout  mLoginUnRL;
   @BindView(R.id.left_jin_text)
   TextView        mTextJin;
   public static View mLoginGone;

   private ArrayList<Fragment> mFragments = new ArrayList<>();

   final static int  COUNTS   = 5;// 点击次数  2s内点击8次进入注册界面
   final static long DURATION = 2000;// 规定有效时间
   long[] mHits = new long[COUNTS];
   public static int                                 mConfigType;
   private       boolean                             mOnStart = false;
   private       LoginFaceFragment                   faceFragment;
   private       LoadingDialog.Builder               mLoading;
   public static List<ConfigBean.ConfigVosBean> sTCstConfigVos;
   public static boolean                             mConfigType015;
   public static boolean                             mConfigType043;
   public static boolean                             mConfigType044;
   public static boolean                             mConfigType045;

   int s =0;
   //
   //   private boolean mDestroyType =true;//处理thread执行
   //   private Thread mThread3;
   //   private       ScheduledExecutorService            scheduled;
   //   private       TimerTask                           mTask;
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLoading(Event.EventLoading event) {
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
	   }
	}
	EventBusUtils.removeStickyEvent(Event.EventLoading.class);
   }

   /**
    * ic卡和指纹仪登陆回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventICFinger(Event.EventICAndFinger event) {
	AllDeviceCallBack.getInstance().openLightStart();
	if (mLightTimeCount!=null){
	   mLightTimeCount.cancel();
	   mLightTimeCount.start();
	}
	if (event.type == 1) {
	   EventBusUtils.postSticky(new Event.EventLoading(true));
	   getConfigDate(event.type, event.date);
	} else if (event.type == 2) {
	   EventBusUtils.postSticky(new Event.EventLoading(true));
	   getConfigDate(event.type, event.date.trim().replaceAll("\n", ""));
	}
   }

   /**
    * 关灯
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLightCloss(Event.EventLightCloss event) {
	if (event.b){
	   AllDeviceCallBack.getInstance().closeLightStart();
	}
   }

   /**
    * 设备title连接状态
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onTitleConnEvent(Event.XmmppConnect event) {
	mTitleConn = event.connect;
	if (!event.connect){
	  s =1;//1记录上次的false
	}
	hasNetWork(event.connect, event.net);
	Log.i("aaaaaa", "mTitleConn   " + mTitleConn);
	if (mTitleConn && s==1 && mOnStart) {
	   EventBusUtils.post(new Event.EventServer(true, 2));//有网后处理数据
	   s=0;//有网后设置为0
	}
   }

   @Override
   public int getLayoutId() {
	return R.layout.activity_login;
   }

   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	File file = new File(
		Environment.getExternalStorageDirectory() + "/login_logo" + "/login_logo.png");
	Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
	Log.i("eerf", "file.getPath()   " + file.getPath());
	Log.i("eerf", "bitmap   " + (bitmap == null));
	if (bitmap == null) {
	   mLoginLogo.setImageResource(R.mipmap.bg_login_icon);
	} else {
	   mLoginLogo.setImageBitmap(bitmap);
	}

	//	Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.bg_login_icon).into(mLoginLogo);
	if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null) {
	   MAIN_URL = SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP);
	}

	mLoginGone = findViewById(R.id.login_gone);

	mFragments.add(new LoginPassWordFragment());//用户名登录
	if (UIUtils.getConfigType(mContext, CONFIG_034)) {
	   faceFragment = new LoginFaceFragment();
	   mFragments.add(faceFragment);//人脸识别登录 TODO
	}
	//	mFragments.add(new LoginPassFragment());//紧急登录
	mLoginViewpager.setAdapter(new LoginTitleAdapter(getSupportFragmentManager()));
	mLoginViewpager.addOnPageChangeListener(new PageChangeListener());

   }

   private void registFingerAndIc() {
	BoxIdBean idBean = LitePal.where("name = ?", FINGER_TYPE).findFirst(BoxIdBean.class);
	BoxIdBean idIc = LitePal.where("name = ?", IC_TYPE).findFirst(BoxIdBean.class);

	if (idBean != null) {
	   int i1 = ConsumableManager.getManager().closeLight(idBean.getDevice_id(), 11);
	   Log.i("appSatus", "电磁锁关闭      " + i1);
	   Log.i("appSatus", "FingerManager  idBean.getDevice_id()      " + idBean.getDevice_id());
	   int i = FingerManager.getManager().startReadFinger(idBean.getDevice_id());
	   Log.i("appSatus","FingerManager     "+i);
	}
	if (idIc != null) {
	   int i = IdCardManager.getIdCardManager().startReadCard(idIc.getDevice_id());
	   Log.i("appSatus","IdCardManager     "+i);
	}
   }

   @Override
   public void onStart() {
	super.onStart();
	registFingerAndIc();

	Log.i("onDoorState", "onStart   onStartonStartonStartonStart   ");
	if (MAIN_URL != null && SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
	   if (SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME) != -1) {
		COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME);
		CLOSSLIGHT_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_CLOSSLIGHT_TIME);
		Log.i("outtccc", "COUNTDOWN_TIME  LOG     " + COUNTDOWN_TIME);
		AllDeviceCallBack.getInstance().StateLightStart();
	   }
	   if (SPUtils.getInt(UIUtils.getContext(), SAVE_HOME_LOGINOUT_TIME) != -1) {
		HOME_COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_HOME_LOGINOUT_TIME);
	   }
	   if (SPUtils.getInt(UIUtils.getContext(), SAVE_NOEPC_LOGINOUT_TIME)!=-1){
		NOEPC_LOGINOUT_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_NOEPC_LOGINOUT_TIME);
	   }
	   getLeftDate();
	   //	   getBoxSize();
	}
	if (mLightTimeCount==null){
	   mLightTimeCount = new LoginUtils.LightTimeCount(CLOSSLIGHT_TIME, 1000);
	}
	EventBusUtils.register(this);
	if (!UIUtils.isServiceRunning(this, "high.rivamed.myapplication.service.ScanService")) {
	   if (mIntentService != null) {
		startService(mIntentService);
		registFingerAndIc();
	   } else {
		mIntentService = new Intent(this, ScanService.class);
		startService(mIntentService);
		registFingerAndIc();
	   }
	}

	EventBusUtils.post(new Event.EventServer(true, 1));
	LoginUtils.getUpDateVer(this);
	mOnStart = true;
	mPushFormOrders.clear();
	mDownText.setText("Copyright © Rivamed Corporation, All Rights Reserved  V " +
				UIUtils.getVersionName(this)+"_C");

	initTab();
	initlistener();
	if (mTitleConn) {
	   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_FACE_ID, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_USER_ICON, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX, "");
	   SPUtils.putBoolean(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE_ALL, false);
	   SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE, "");
	   SPUtils.putString(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE, "");
	   SPUtils.putString(UIUtils.getContext(), ACCESS_TOKEN, "");
	   SPUtils.putString(UIUtils.getContext(), REFRESH_TOKEN, "");
	   SPUtils.putString(UIUtils.getContext(), PATIENT_TYPE, "");
	}
	mConfigType = 0;//默认获取
	getConfigDate(mConfigType, null);
   }

   /**
    * 查询未确认耗材
    */
   private void getNoConfirm() {
	NetRequest.getInstance().getRightUnconfDate("", "", mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		InventoryDto socketRightBean = mGson.fromJson(result, InventoryDto.class);
		List<InventoryVo> inventoryVos = socketRightBean.getInventoryVos();
		if (inventoryVos.size() > 0) {
		   mRightTopText.setText(inventoryVos.size() +"");
		}
	   }
	});
   }
   /**
    * 获取低于下限
    */
   private void getFloorList() {
	NetRequest.getInstance().getFloorList( mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		String cstKinds = fromJson.getCstKinds()+"";
		mRightDownText.setText(cstKinds);
	   }
	});
   }
   /**
    * 获取配置项
    */
   public void getConfigDate(int configType, String loginType) {
	LogUtils.i(TAG, "getConfigDate   ");
	if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
	   LogUtils.i(TAG, "getConfigDate    ddd   " + mTitleConn);
	   if (mTitleConn) {
		NetRequest.getInstance().findThingConfigDate(UIUtils.getContext(), new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result   " + result);
			SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
			setConfigBean(result, configType, loginType);
		   }

		   @Override
		   public void onError(String result) {
			if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1")) {
			   String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
			   setConfigBean(string, configType, loginType);
			}
		   }
		});
	   } else {
		if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null) {
		   String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
		   setConfigBean(string, configType, loginType);
		}
	   }

	}
   }

   /**
    * 得到配置项后的操作
    *
    * @param result
    * @param configType
    * @param loginType
    */
   private void setConfigBean(String result, int configType, String loginType) {
	ConfigBean configBean = mGson.fromJson(result, ConfigBean.class);
	if (configBean == null) {
	   return;
	}
	sTCstConfigVos = configBean.getConfigVos();
	loginEnjoin(!LoginUtils.getConfigTrue(sTCstConfigVos), configType, loginType);
	//	LoginUtils.getUpDateVer(this, sTCstConfigVos,
	//					(canLogin, canDevice, hasNet) -> loginEnjoin(canDevice, configType,
	//												   loginType));
	mConfigType015 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_015);
	mConfigType043 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_043);
	mConfigType044 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_044);
	mConfigType045 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_045);

	//控制紧急登录tab的显示
	if (mLoginPass != null) {
	   mLoginPass.setVisibility(
		   UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_017) ? View.VISIBLE : View.GONE);
	}
	//有人脸识别，显示人脸识别tab，默认选中用户名登录tab
	//没有人脸识别，隐藏人脸识别tab，默认选中用户名登录tab
	if (mLoginFace != null) {
	   mLoginFace.setVisibility(isConfigFace() ? View.VISIBLE : View.GONE);
	}
	if (mLoginViewpager != null) {
	   mLoginViewpager.setCurrentItem(0);
	   //有人脸识别或紧急登录时，可滑动
	   mLoginViewpager.setScanScroll(
		   isConfigFace() || UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_017));
	}
	if (!UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_060)&&!UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_026)) {
	   mRightRl.setVisibility(View.GONE);
	} else {
	   mRightRl.setVisibility(View.VISIBLE);
	}

	if (UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_026)) {
	   if (mRightTopL!=null){
		mRightTopL.setVisibility(View.VISIBLE);
		mRightTopText.setText("0");
		getNoConfirm();
	   }
	} else {
	   if (mRightTopL!=null) {
		mRightTopL.setVisibility(View.GONE);
	   }
	}
	if (UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_060)) {
	   if (mRightDownL!=null){
		mRightDownL.setVisibility(View.VISIBLE);
		mRightDownText.setText("0");
		getFloorList();
		Log.i("3434s","ddddd   "+mRightDownText.getText());
	   }
	} else {
	   if (mRightDownL!=null) {
		mRightDownL.setVisibility(View.GONE);
	   }
	}

	if (mTVLoginToBePutInStorage != null) {
	   mTVLoginToBePutInStorage.setVisibility(
		   UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_046) ? View.VISIBLE : View.GONE);
	}
   }

   public boolean isConfigFace() {
	return UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_034);
	//测试时默认开启，真实情况需要根据后台配置
	//		return true;
	//		return true;
   }

   /**
    * 是否禁止使用
    *
    * @param configType
    * @param loginType
    */
   private void loginEnjoin(
	   boolean canDevice, int configType, String loginType) {
	if (!canDevice) {//禁止
	   if (configType == 0) {//正常登录密码登录限制
		if (mLoginGone != null) {
		   mLoginGone.setVisibility(View.VISIBLE);
		}
		if (mLoginFace!=null&&mLoginFace.isChecked() && isConfigFace()) {
		   //设备可用切换至设备禁用时当前选中显示的是人脸识别页面，停止人脸识别的预览
		   faceFragment.onTabShowPreview(false);
		}
	   } else if (configType == 1) {//IC卡登录限制
		if (mLoginGone != null) {
		   mLoginGone.setVisibility(View.VISIBLE);
		}
		ToastUtils.showShortToast("正在维护，请到管理端启用");
	   } else if (configType == 2) {
		//设备是否禁用
		if (mLoginGone != null) {
		   mLoginGone.setVisibility(View.VISIBLE);
		}
		ToastUtils.showShortToast("正在维护，请到管理端启用");
	   }
	} else {
	   if (configType == 0) {//正常登录密码登录限制
		if (mLoginGone != null) {
		   mLoginGone.setVisibility(View.GONE);
		}
		if (mLoginFace.isChecked() && isConfigFace()) {
		   //设备禁用切换至设备可用时当前选中显示的是人脸识别页面，开启人脸识别的预览
		   faceFragment.onTabShowPreview(true);
		}
	   } else if (configType == 1) {//IC卡登录限制
		if (mTitleConn) {
		   validateLoginIdCard(loginType);
		} else {
		   uNNetvalidateLoginIdCard(loginType);
		}
	   } else if (configType == 2) {
		if (mTitleConn) {
		   validateLoginFinger(loginType);
		} else {
		   EventBusUtils.postSticky(new Event.EventLoading(false));
		   ToastUtils.showShortToast("登录失败，离线模式请使用腕带或者账号登录！");
		}
	   }
	}
   }

   /**
    * 断网后IC卡登陆
    *
    * @param loginType
    */
   private void uNNetvalidateLoginIdCard(String loginType) {

	List<UserFeatureInfosBean> beans = LitePal.where("data = ? ", loginType)
		.find(UserFeatureInfosBean.class);
	LogUtils.i(TAG, " beans     " + mGson.toJson(beans));
	if (beans != null && beans.size() > 0 && beans.get(0).getData().equals(loginType)) {
	   String accountName = beans.get(0).getAccountName();
	   List<HomeAuthorityMenuBean> fromJson = LoginUtils.setUnNetSPdate(accountName, mGson);
	   LogUtils.i(TAG, " menus1     " + mGson.toJson(fromJson));
	   LoginUtils.setMenuDateAndStart(fromJson, mGson, this, null);
	} else {
	   ToastUtils.showShortToast("登录失败，暂无登录信息！");
	}
	EventBusUtils.postSticky(new Event.EventLoading(false));
   }

   private void validateLoginIdCard(String idCard) {
	IdCardLoginDto data = new IdCardLoginDto();
	IdCardLoginDto.UserFeatureInfoBean bean = new IdCardLoginDto.UserFeatureInfoBean();
	bean.setData(idCard);
	bean.setType("2");
	data.setUserFeatureInfo(bean);
	data.setThingId(SPUtils.getString(mContext, THING_CODE));
	data.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	data.setSystemType(SYSTEMTYPE);
	data.setLoginType(LOGIN_TYPE_IC);
	LogUtils.i(TAG, "mGson.toJson(data)   " + mGson.toJson(data));
	NetRequest.getInstance().validateLoginIdCard(mGson.toJson(data), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "validateLoginIdCard  result   " + result);
		LoginUtils.loginSpDate(result, mContext, mGson, null);
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.postSticky(new Event.EventLoading(false));
		uNNetvalidateLoginIdCard(idCard);
	   }
	});

   }

   private void validateLoginFinger(String fingerFea) {
	String thingCode = SPUtils.getString(mContext, THING_CODE);
	FingerLoginDto data = new FingerLoginDto();
	FingerLoginDto.UserFeatureInfoBean bean = new FingerLoginDto.UserFeatureInfoBean();
	bean.setData(fingerFea);
	data.setUserFeatureInfo(bean);
	data.setDeviceType(FINGER_VERSION);//3.0柜子需要传2   2.1传1
	data.setThingId(thingCode);
	data.setSystemType(SYSTEMTYPE);
	data.setLoginType(LOGIN_TYPE_FINGER);
	data.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	LogUtils.i("Login", "THING_CODE validateLoginFinger  " + mGson.toJson(data));
	NetRequest.getInstance().validateLoginFinger(mGson.toJson(data), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "validateLoginFinger   result   " + result);
		LoginUtils.loginSpDate(result, mContext, mGson, null);
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.postSticky(new Event.EventLoading(false));
	   }
	});

   }

   @Override
   public void onBindViewBefore() {

   }

   public void getLeftDate() {

	NetRequest.getInstance().materialControl(mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "getLeftDate   result   " + result);
		SocketLeftTopBean leftTopBean = mGson.fromJson(result, SocketLeftTopBean.class);
		if (leftTopBean.getCstExpirationVos() != null &&
		    leftTopBean.getCstExpirationVos().size() > 0) {
		   setLeftDate(leftTopBean);
		}
	   }

	   @Override
	   public void onError(String result) {

	   }
	});
   }

   /**
    * 左侧界面显示
    *
    * @param leftTopBean
    */
   private void setLeftDate(SocketLeftTopBean leftTopBean) {
	List<SocketLeftTopBean.CstExpirationVosBean> cstExpirationVos = leftTopBean.getCstExpirationVos();
	int guoNumber = 0;
	int jinNumber = 0;
	for (SocketLeftTopBean.CstExpirationVosBean s : cstExpirationVos) {
	   guoNumber += s.getExpireCount();
	   jinNumber += s.getNearExpireCount();
	}
	mTextGuo.setText("" + guoNumber);
	mTextJin.setText("" + jinNumber);
   }

   private void initlistener() {
	mLoginLogo.setOnClickListener(view -> {
	   continuousClick();
	});
	mLoginGone.setOnClickListener(view -> {
	   if (mTitleConn) {
		ToastUtils.showShortToast("正在维护，请到管理端启用");
		mConfigType = 0;
		getConfigDate(mConfigType, null);
	   } else {
		ToastUtils.showShortToast("请检查网络");

	   }
	});
	/**
	 * 库存详情
	 */
	mStockStatus.setOnClickListener(view -> {
	   if (mTitleConn) {
		startActivity(new Intent(LoginActivity.this, LoginStockStatusActivity.class));
	   } else {
		ToastUtils.showShortToast("网络异常，请检查网络!");
	   }
	});
//	/**
//	 * 未确认耗材
//	 */
//	mTVLoginUnConfirmCst.setOnClickListener(view -> {
//	   if (mTitleConn) {
//		startActivity(new Intent(LoginActivity.this, LoginUnconfirmActivity.class));
//	   } else {
//		ToastUtils.showShortToast("网络异常，请检查网络!");
//	   }
//	});
	/**
	 * 未入库耗材
	 */
	mTVLoginToBePutInStorage.setOnClickListener(view -> {
	   if (mTitleConn) {
		startActivity(new Intent(LoginActivity.this, LoginToBePutInStorageActivity.class));
	   } else {
		ToastUtils.showShortToast("网络异常，请检查网络!");
	   }
	});
   }

   private void continuousClick() {
	//每次点击时，数组向前移动一位
	System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
	//为数组最后一位赋值
	mHits[mHits.length - 1] = SystemClock.uptimeMillis();
	if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
	   mHits = new long[COUNTS];//重新初始化数组
	   Toast.makeText(this, "已进入工程模式", Toast.LENGTH_LONG).show();
	   startActivity(new Intent(this, TestLoginActivity.class));
	}
   }

   private void initTab() {

	mLoginRadiogroup.setOnCheckedChangeListener((radioGroup, i) -> {
	   if (UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_017)) {
		if (isConfigFace()) {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_face:
			   mLoginViewpager.setCurrentItem(1);
			   break;
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(0);
			   break;
			case R.id.login_pass:
			   mLoginViewpager.setCurrentItem(2);
			   break;
		   }
		} else {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(0);
			   break;
			case R.id.login_pass:
			   mLoginViewpager.setCurrentItem(1);
			   break;
		   }
		}
	   } else {
		if (isConfigFace()) {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_face:
			   mLoginViewpager.setCurrentItem(1);
			   break;
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(0);
			   break;
		   }
		} else {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(0);
			   break;
		   }
		}
	   }
	   mLoginRadiogroup.check(radioGroup.getCheckedRadioButtonId());
	});

	//初始状态只显示用户密码登录
	mLoginPass.setVisibility(View.GONE);
	mLoginFace.setVisibility(View.GONE);
	mLoginViewpager.setScanScroll(false);
	mLoginViewpager.setCurrentItem(0);
   }

   private class PageChangeListener implements ViewPager.OnPageChangeListener {

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
	   if (UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_017)) {
		if (isConfigFace()) {
		   switch (position) {
			case 1:
			   mLoginRadiogroup.check(R.id.login_face);
			   break;
			case 0:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
			case 2:
			   mLoginRadiogroup.check(R.id.login_pass);
			   break;
		   }
		} else {
		   switch (position) {
			case 0:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
			case 1:
			   mLoginRadiogroup.check(R.id.login_pass);
			   break;
		   }
		}
	   } else {
		if (isConfigFace()) {
		   switch (position) {
			case 1:
			   mLoginRadiogroup.check(R.id.login_face);
			   break;
			case 0:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
		   }
		} else {
		   switch (position) {
			case 0:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
		   }
		}
	   }
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
   }

   class LoginTitleAdapter extends FragmentStatePagerAdapter {

	public LoginTitleAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   return mFragments.get(position);
	}

	@Override
	public int getCount() {
	   return mFragments.size();
	}
   }

   @Override
   protected void onPause() {
	super.onPause();
	mOnStart = false;
	if (mTitleConn) {
	   UnNetCstUtils.putUnNetOperateYes(this);//提交离线耗材和重新获取在库耗材数据
	}
   }

   @Override
   protected void onStop() {
	super.onStop();
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   if (mLoading.mHandler != null) {
		mLoading.mHandler.removeCallbacksAndMessages(null);
	   }
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	cancel();
	EventBusUtils.unregister(this);
   }

   @Override
   public Object newP() {
	return null;
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	if (mLoginGone != null) {
	   mLoginGone.onFinishTemporaryDetach();
	   mLoginGone = null;
	}

	Log.i("ccetdaF", getClass().getName() + "  onDestroy");
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
	switch (ev.getAction()) {
	   //获取触摸动作，如果ACTION_UP，计时开始。
	   case MotionEvent.ACTION_DOWN:
		AllDeviceCallBack.getInstance().openLightStart();
		if (mLightTimeCount!=null){
		   mLightTimeCount.cancel();
		}
		break;
	   case MotionEvent.ACTION_UP:
	      if (mLightTimeCount==null){
		   mLightTimeCount = new LoginUtils.LightTimeCount(
			   CLOSSLIGHT_TIME, 1000);
		   Log.i("onDoorState", "LightTimeCount     " );
		   mLightTimeCount.start();
		}else {
		   mLightTimeCount.cancel();
		   mLightTimeCount.start();
		   Log.i("onDoorState", "ACTION_UP     " );
		}
		break;
	}
	return super.dispatchTouchEvent(ev);
   }
}
