package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;

import org.androidpn.utils.XmppEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dbmodel.ChildrenBean;
import high.rivamed.myapplication.dbmodel.ChildrenBeanX;
import high.rivamed.myapplication.dbmodel.OperationRoomsBean;
import high.rivamed.myapplication.dbmodel.RoomsBean;
import high.rivamed.myapplication.dbmodel.UserBean;
import high.rivamed.myapplication.dbmodel.UserFeatureInfosBean;
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
import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.getAppContext;
import static high.rivamed.myapplication.base.App.mPushFormOrders;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACCESS_TOKEN;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.CONFIG_015;
import static high.rivamed.myapplication.cont.Constants.CONFIG_017;
import static high.rivamed.myapplication.cont.Constants.CONFIG_026;
import static high.rivamed.myapplication.cont.Constants.CONFIG_034;
import static high.rivamed.myapplication.cont.Constants.CONFIG_043;
import static high.rivamed.myapplication.cont.Constants.CONFIG_044;
import static high.rivamed.myapplication.cont.Constants.CONFIG_045;
import static high.rivamed.myapplication.cont.Constants.CONFIG_046;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_ICON;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.PATIENT_TYPE;
import static high.rivamed.myapplication.cont.Constants.REFRESH_TOKEN;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE_ALL;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getAllCstDate;

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
   @BindView(R.id.login_unconfirmcst)
   TextView        mTVLoginUnConfirmCst;
   @BindView(R.id.login_unrl)
   RelativeLayout  mLoginUnRL;
   @BindView(R.id.left_jin_text)
   TextView        mTextJin;
   public static View mLoginGone;

   private ArrayList<Fragment> mFragments = new ArrayList<>();

   final static int  COUNTS   = 5;// 点击次数  2s内点击8次进入注册界面
   final static long DURATION = 2000;// 规定有效时间
   long[] mHits = new long[COUNTS];
   public static int               mConfigType;
   private       boolean           mOnStart = false;
   private       LoginFaceFragment faceFragment;
   private LoadingDialog.Builder   mLoading;
   public static List<ConfigBean.ThingConfigVosBean> sTCstConfigVos;
   public static          boolean           mConfigType015;
   public static          boolean           mConfigType043;
   public static     boolean           mConfigType044;
   public static          boolean           mConfigType045;
   private Thread mThread;
   private Thread mThread1;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		mLoading = DialogUtils.showLoading(this);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
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
	EventBusUtils.removeStickyEvent(getClass());
   }
	/**
    * ic卡和指纹仪登陆回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventICFinger(Event.EventICAndFinger event) {
	if (event.type == 1) {
	   EventBusUtils.postSticky(new Event.EventLoading(true));
	   getConfigDate(event.type, event.date);
	} else if (event.type == 2) {
	   EventBusUtils.postSticky(new Event.EventLoading(true));
	   getConfigDate(event.type, event.date.trim().replaceAll("\n", ""));
	}
   }

   /**
    * 设备title连接状态
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onTitleConnEvent(XmppEvent.XmmppConnect event) {
	mTitleConn = event.connect;
	hasNetWork(event.connect, event.net);
	if (mTitleConn && mOnStart) {
	   mThread = new Thread(new Runnable() {
		@Override
		public void run() {
		   List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
		   if (voList == null || voList.size() == 0) {
			getAllCstDate(this);
		   }
		   UnNetCstUtils.putUnNetOperateYes(this);//提交离线耗材和重新获取在库耗材数据
		   getUnNetUseDate();
		   getUnEntFindOperation();
		}
	   });
	   mThread.start();
	}
   }

   @Override
   public int getLayoutId() {
	return R.layout.activity_login;
   }

   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null) {
	   MAIN_URL = SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP);
	  if (UIUtils.getConfigType(mContext, CONFIG_034)){
	     faceFragment = new LoginFaceFragment();
	     mFragments.add(faceFragment);//人脸识别登录 TODO
	  }
	}
	mLoginGone = findViewById(R.id.login_gone);

	mFragments.add(new LoginPassWordFragment());//用户名登录
//	mFragments.add(new LoginPassFragment());//紧急登录
	mLoginViewpager.setAdapter(new LoginTitleAdapter(getSupportFragmentManager()));
	mLoginViewpager.addOnPageChangeListener(new PageChangeListener());

   }

   public void getBoxSize() {
	NetRequest.getInstance().loadBoxSize(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		mTitleConn=true;
		SPUtils.putString(getAppContext(), BOX_SIZE_DATE, "");
		Gson gson = new Gson();
		BoxSizeBean boxSizeBean = gson.fromJson(result, BoxSizeBean.class);
		List<BoxSizeBean.DevicesBean> devices = boxSizeBean.getDevices();
		if (devices.size() > 1) {
		   BoxSizeBean.DevicesBean tbaseDevicesBean = new BoxSizeBean.DevicesBean();
		   tbaseDevicesBean.setDeviceName("全部开柜");
		   devices.add(0, tbaseDevicesBean);
		}
		SPUtils.putString(getAppContext(), BOX_SIZE_DATE, gson.toJson(devices));
	   }

	   @Override
	   public void onError(String result) {

	   }
	});
   }

   @Override
   public void onStart() {
	super.onStart();

	if (!UIUtils.isServiceRunning(this, "high.rivamed.myapplication.service.ScanService")) {
	   if (mIntentService!=null){
		startService(mIntentService);
	   }else {
		mIntentService = new Intent(this, ScanService.class);
		startService(mIntentService);
	   }

	}

	mThread1 = new Thread(new Runnable() {
	   @Override
	   public void run() {
		getAllCstDate(this);//重新获取在库耗材数据
		List<UserFeatureInfosBean> all = LitePal.findAll(UserFeatureInfosBean.class);
		List<OperationRoomsBean> roomsBeans = LitePal.findAll(OperationRoomsBean.class);
		if (all.size() == 0) {
		   getUnNetUseDate();
		}
		if (roomsBeans.size() == 0) {
		   getUnEntFindOperation();
		}
	   }
	});
	mThread1.start();
	mOnStart = true;
	mPushFormOrders.clear();
	mDownText.setText(
		"© 2018 Rivamed  All Rights Reserved  V: " + UIUtils.getVersionName(this));
	if (MAIN_URL != null && SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
	   if (SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME) != -1) {
		COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME);
	   }
	   getLeftDate();
	   getBoxSize();
	}
	initTab();
	initlistener();
	if (mTitleConn) {
	   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME, "");
	   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, "");
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
	NetRequest.getInstance().getRightUnconfDate("", "", mContext, new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		InventoryDto socketRightBean = mGson.fromJson(result, InventoryDto.class);
		List<InventoryVo> inventoryVos = socketRightBean.getInventoryVos();
		if (inventoryVos.size()>0){
		   mTVLoginUnConfirmCst.setText("未确认耗材（"+inventoryVos.size()+"）");
		}
	   }
	});
   }

   /**
    * 本地手术室
    */
   private void getUnEntFindOperation() {

	NetRequest.getInstance().getUnEntFindOperation(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "getUnEntFindOperation    " + result);
		LitePal.deleteAll(RoomsBean.class);
		LitePal.deleteAll(OperationRoomsBean.class);
		RoomsBean roomsBean = mGson.fromJson(result, RoomsBean.class);
		RoomsBean mRoomsBean = new RoomsBean();
		if (roomsBean.getOperationRooms().size() > 0) {
		   mRoomsBean.setThingId(roomsBean.getThingId());
		   for (OperationRoomsBean mOperationRooms : roomsBean.getOperationRooms()) {
			OperationRoomsBean bean = new OperationRoomsBean();
			bean.setOptRoomId(mOperationRooms.getOptRoomId());
			bean.setRoomName(mOperationRooms.getRoomName());
			bean.save();
			mRoomsBean.getOperationRooms().add(bean);
		   }
		   mRoomsBean.save();
		}
	   }
	});
   }

   /**
    * 所有用户的本地数据
    */
   private void getUnNetUseDate() {
	NetRequest.getInstance().getUnNetUseDate(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		deleteLitepal();
		UserBean userBean = mGson.fromJson(result, UserBean.class);
		new Thread(new Runnable() {
		   @Override
		   public void run() {
			setLitePalUseBean(userBean);
		   }
		}).start();
	   }
	});
   }

   /**
    * 数据存储
    *
    * @param userBean
    */
   private void setLitePalUseBean(UserBean userBean) {
	UserBean mUserBean = new UserBean();
	mUserBean.setDeptId(userBean.getDeptId());
	for (AccountVosBean accountVosBean : userBean.getAccountVos()) {
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
	   vosBean.save();
	   mUserBean.getAccountVos().add(vosBean);
	}
	mUserBean.save();
   }

   /**
    * 删除本地数据库用户信息表
    */
   private void deleteLitepal() {
	LitePal.deleteAll(UserBean.class);
	LitePal.deleteAll(AccountVosBean.class);
	LitePal.deleteAll(HomeAuthorityMenuBean.class);
	LitePal.deleteAll(UserFeatureInfosBean.class);
	LitePal.deleteAll(ChildrenBeanX.class);
	LitePal.deleteAll(ChildrenBean.class);
   }

   /**
    * 获取配置项
    */
   public void getConfigDate(int configType, String loginType) {
	LogUtils.i(TAG, "getConfigDate   ");
	if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
	   LogUtils.i(TAG, "getConfigDate    ddd   "+mTitleConn);
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
	if (configBean==null){
	   return;
	}
	sTCstConfigVos = configBean.getThingConfigVos();
	LoginUtils.getUpDateVer(this, sTCstConfigVos,
					(canLogin, canDevice, hasNet) -> loginEnjoin(canDevice, configType,
												   loginType));
	mConfigType015 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_015);
	mConfigType043 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_043);
	mConfigType044 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_044);
	mConfigType045 = UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_045);

	//控制紧急登录tab的显示
	mLoginPass.setVisibility(
		UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_017) ? View.VISIBLE : View.GONE);
	//有人脸识别，显示人脸识别tab，默认选中人脸识别tab
	//没有人脸识别，隐藏人脸识别tab，默认选中用户名登录tab
	mLoginFace.setVisibility(isConfigFace() ? View.VISIBLE : View.GONE);
	mLoginViewpager.setCurrentItem(isConfigFace() ? 0 : 1);
	//有人脸识别或紧急登录时，可滑动
	mLoginViewpager.setScanScroll(isConfigFace() || UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_017));
	LogUtils.i(TAG, "getConfigDatddffdfe   "+UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_026));
	if (UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_026)){
	   mLoginUnRL.setVisibility(View.VISIBLE);
	   mTVLoginUnConfirmCst.setText("未确认耗材（0）");
	   getNoConfirm();
	}else {
	   mLoginUnRL.setVisibility(View.INVISIBLE);
	}
	mTVLoginToBePutInStorage.setVisibility(UIUtils.getConfigLoginType(sTCstConfigVos, CONFIG_046)?View.VISIBLE:View.GONE);
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
		mLoginGone.setVisibility(View.VISIBLE);
		if (mLoginFace.isChecked() && isConfigFace()) {
		   //设备可用切换至设备禁用时当前选中显示的是人脸识别页面，停止人脸识别的预览
		   faceFragment.onTabShowPreview(false);
		}
	   } else if (configType == 1) {//IC卡登录限制
		mLoginGone.setVisibility(View.VISIBLE);
		ToastUtils.showShortToast("正在维护，请到管理端启用");
	   } else if (configType == 2) {
		//设备是否禁用
		mLoginGone.setVisibility(View.VISIBLE);
		ToastUtils.showShortToast("正在维护，请到管理端启用");
	   }
	} else {
	   if (configType == 0) {//正常登录密码登录限制
		mLoginGone.setVisibility(View.GONE);
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
		validateLoginFinger(loginType);
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
	if (beans.size() > 0 && beans.get(0).getData().equals(loginType)) {
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
	data.setSystemType(SYSTEMTYPE);
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
	   }
	});

   }

   private void validateLoginFinger(String fingerFea) {
	String thingCode = SPUtils.getString(mContext, THING_CODE);
	FingerLoginDto data = new FingerLoginDto();
	FingerLoginDto.UserFeatureInfoBean bean = new FingerLoginDto.UserFeatureInfoBean();
	bean.setData(fingerFea);
	data.setUserFeatureInfo(bean);
	data.setThingId(thingCode);
	data.setSystemType(SYSTEMTYPE);
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
	/**
	 * 未确认耗材
	 */
	mTVLoginUnConfirmCst.setOnClickListener(view -> {
	   if (mTitleConn) {
		startActivity(new Intent(LoginActivity.this, LoginUnconfirmActivity.class));
	   } else {
		ToastUtils.showShortToast("网络异常，请检查网络!");
	   }
	});
	/**
	 * 未入库耗材
	 */
	mTVLoginToBePutInStorage.setOnClickListener(view->{
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
			   mLoginViewpager.setCurrentItem(0);
			   break;
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(1);
			   break;
			case R.id.login_pass:
			   mLoginViewpager.setCurrentItem(2);
			   break;
		   }
		} else {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(1);
			   break;
			case R.id.login_pass:
			   mLoginViewpager.setCurrentItem(2);
			   break;
		   }
		}
	   } else {
		if (isConfigFace()) {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_face:
			   mLoginViewpager.setCurrentItem(0);
			   break;
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(1);
			   break;
		   }
		} else {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(1);
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
	mLoginViewpager.setCurrentItem(1);
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
			case 0:
			   mLoginRadiogroup.check(R.id.login_face);
			   break;
			case 1:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
			case 2:
			   mLoginRadiogroup.check(R.id.login_pass);
			   break;
		   }
		} else {
		   switch (position) {
			case 1:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
			case 2:
			   mLoginRadiogroup.check(R.id.login_pass);
			   break;
		   }
		}
	   } else {
		if (isConfigFace()) {
		   switch (position) {
			case 0:
			   mLoginRadiogroup.check(R.id.login_face);
			   break;
			case 1:
			   mLoginRadiogroup.check(R.id.login_password);
			   break;
		   }
		} else {
		   switch (position) {
			case 1:
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
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (mTitleConn) {
	   UnNetCstUtils.putUnNetOperateYes(this);//提交离线耗材和重新获取在库耗材数据
	}
   }

   @Override
   public Object newP() {
	return null;
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	if (mLoginGone!=null){
	   mLoginGone.onFinishTemporaryDetach();
	   mLoginGone= null;
	}
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
   }
}
