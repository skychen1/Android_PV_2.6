package high.rivamed.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.androidpn.utils.XmppEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.bean.VersionBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dbmodel.ChildrenBean;
import high.rivamed.myapplication.dbmodel.ChildrenBeanX;
import high.rivamed.myapplication.dbmodel.OperationRoomsBean;
import high.rivamed.myapplication.dbmodel.RoomsBean;
import high.rivamed.myapplication.dbmodel.UserBean;
import high.rivamed.myapplication.dbmodel.UserFeatureInfosBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.FingerLoginDto;
import high.rivamed.myapplication.dto.IdCardLoginDto;
import high.rivamed.myapplication.fragment.LoginPassFragment;
import high.rivamed.myapplication.fragment.LoginPassWordFragment;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.service.TimerService;
import high.rivamed.myapplication.utils.FileUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.PackageUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.CustomViewPager;
import high.rivamed.myapplication.views.UpDateDialog;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.getAppContext;
import static high.rivamed.myapplication.base.App.mPushFormOrders;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACCESS_TOKEN;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.CONFIG_013;
import static high.rivamed.myapplication.cont.Constants.CONFIG_017;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_s_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_ICON;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
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
import static high.rivamed.myapplication.http.NetApi.URL_UPDATE;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
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
   @BindView(R.id.left_jin_text)
   TextView        mTextJin;
   public static View mLoginGone;

   private ArrayList<Fragment> mFragments = new ArrayList<>();

   final static int  COUNTS   = 5;// 点击次数  2s内点击8次进入注册界面
   final static long DURATION = 2000;// 规定有效时间
   long[] mHits = new long[COUNTS];
   public static int    mConfigType;
   private       String mDesc;
   private boolean mOnStart = false;

   /**
    * ic卡和指纹仪登陆回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventICFinger(Event.EventICAndFinger event) {
	if (event.type == 1) {
	   getConfigDate(event.type, event.date);
	} else if (event.type == 2) {
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
	hasNetWork(event.connect);
	if (mTitleConn && mOnStart) {
	   new Thread(new Runnable() {
		@Override
		public void run() {
		   getAllCstDate(mGson, this);
		   getUnNetUseDate();
		   getUnEntFindOperation();
		}
	   }).start();
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
	}
	LogUtils.i(TAG, "getDates()   " + getDates());

   }

   public void getBoxSize() {
	NetRequest.getInstance().loadBoxSize(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
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
	new Thread(new Runnable() {
	   @Override
	   public void run() {
		AllDeviceCallBack.getInstance().initCallBack();
		if (mTitleConn) {
		   getAllCstDate(mGson, this);
		}
	   }
	}).start();
	mOnStart = true;
	mPushFormOrders.clear();

	mLoginGone = findViewById(R.id.login_gone);
	mDownText.setText(
		"© 2018 Rivamed  All Rights Reserved  V: " + UIUtils.getVersionName(this) + "_c");
	if (MAIN_URL != null && SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
	   if (SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME) != -1) {
		COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME);
	   }
	   getLeftDate();
	   getBoxSize();
	}

	mFragments.add(new LoginPassWordFragment());//用户名登录
	mFragments.add(new LoginPassFragment());//紧急登录
	initData();
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
	}

	mConfigType = 0;//默认获取
	getConfigDate(mConfigType, null);
	mLoginPass.setVisibility(View.GONE);
	mLoginViewpager.setScanScroll(false);
   }

   @Override
   protected void onResume() {
	super.onResume();
	if (MAIN_URL != null && mIntent == null) {
	   mIntent = new Intent(this, TimerService.class);
	   startService(mIntent);
	}
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
		//		LogUtils.w(TAG, "getUnNetUseDate    " + result);
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
		   if (null!=homeAuthorityMenuBean.getChildren().get(0).getChildren()) {
			for (int x = 0; x < homeAuthorityMenuBean.getChildren().get(0).getChildren().size(); x++) {
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

   private boolean getConfigTrue(List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
	if (tCstConfigVos.size() == 0) {
	   return false;
	} else {
	   for (ConfigBean.ThingConfigVosBean s : tCstConfigVos) {
		if (s.getCode().equals(CONFIG_013)) {
		   return true;
		}
	   }
	   return false;
	}
   }

   /**
    * 获取配置项
    */
   public void getConfigDate(int configType, String loginType) {

	if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
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
	List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
	//	if (tCstConfigVos!=null&&tCstConfigVos.size() != 0) {
	getUpDateVer(tCstConfigVos, configType, loginType);
	if (UIUtils.getConfigType(mContext, CONFIG_017)) {
	   mLoginPass.setVisibility(View.VISIBLE);
	   mLoginViewpager.setScanScroll(true);
	} else {
	   mLoginPass.setVisibility(View.GONE);
	   mLoginViewpager.setScanScroll(false);
	}
	//	} else {
	//	   ToastUtils.showShortToast("请先在管理端对配置项进行设置，后进行登录！");
	//	}
   }

   /**
    * 是否禁止使用
    *
    * @param tCstConfigVos
    * @param configType
    * @param loginType
    */
   private void loginEnjoin(
	   List<ConfigBean.ThingConfigVosBean> tCstConfigVos, int configType, String loginType) {
	if (getConfigTrue(tCstConfigVos)) {//禁止
	   if (configType == 0) {//正常登录密码登录限制
		mLoginGone.setVisibility(View.VISIBLE);
	   } else if (configType == 1) {//IC卡登录限制
		mLoginGone.setVisibility(View.VISIBLE);
		ToastUtils.showShort("正在维护，请到管理端启用");
	   } else if (configType == 2) {
		//设备是否禁用
		mLoginGone.setVisibility(View.VISIBLE);
		ToastUtils.showShort("正在维护，请到管理端启用");
	   }
	} else {
	   if (configType == 0) {//正常登录密码登录限制
		mLoginGone.setVisibility(View.GONE);
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
	   List<HomeAuthorityMenuBean> fromJson = setUnNetSPdate(accountName, mGson);
	   LogUtils.i(TAG, " menus1     " + mGson.toJson(fromJson));
	   setMenuDateAndStart(fromJson, mGson, this);
	} else {
	   ToastUtils.showShortToast("登录失败，暂无登录信息！");
	}
   }

   @NonNull
   public static List<HomeAuthorityMenuBean> setUnNetSPdate(String accountName, Gson mGson) {
	AccountVosBean beanss = LitePal.where("accountname = ? ", accountName)
		.findFirst(AccountVosBean.class);
	SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME, beanss.getAccountId());
	SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME, beanss.getAccountName());
	SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, beanss.getUserName());
	SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, beanss.getAccountId());
	SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX, beanss.getSex());
	List<HomeAuthorityMenuBean> menusList = beanss.getMenusList(accountName);
	if (menusList.size() > 0 && menusList.get(0).getTitle().equals("耗材操作")) {
	   List<ChildrenBeanX> childrenXbean = menusList.get(0).getChildrenXbean(accountName);
	   menusList.get(0).setChildren(childrenXbean);
	   if (childrenXbean.size() > 0 && childrenXbean.get(0).getTitle().equals("选择操作")) {
		List<ChildrenBean> childrenbean = childrenXbean.get(0).getChildrenbean(accountName);
		childrenXbean.get(0).setChildren(childrenbean);
	   }
	}
	List<HomeAuthorityMenuBean> fromJson = new ArrayList<>();
	fromJson.addAll(menusList);
	SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE, mGson.toJson(fromJson));
	return fromJson;
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
		loginSpDate(result, mContext, mGson);
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
		loginSpDate(result, mContext, mGson);
	   }
	});

   }

   /**
    * 重启推送和赋值
    *
    * @param result
    */
   public static void loginSpDate(String result, Activity activity, Gson mGson) {
	try {
	   LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
	   if (loginResultBean.isOperateSuccess()) {
//		if (mServiceManager != null) {
//		   mServiceManager.stopService();
//		   mServiceManager = null;
//		   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME, "");
//		}
		SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME,
					loginResultBean.getAppAccountInfoVo().getAccountId());
		SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
		SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME,
					loginResultBean.getAppAccountInfoVo().getAccountName());
		SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME,
					loginResultBean.getAppAccountInfoVo().getUserName());
		SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID,
					loginResultBean.getAppAccountInfoVo().getAccountId());
		SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX,
					loginResultBean.getAppAccountInfoVo().getSex());
		SPUtils.putString(UIUtils.getContext(), ACCESS_TOKEN,
					loginResultBean.getAccessToken().getTokenId());
		SPUtils.putString(UIUtils.getContext(), REFRESH_TOKEN,
					loginResultBean.getAccessToken().getRefreshToken());
		//			SPUtils.getString(UIUtils.getContext(), KEY_USER_ICON,loginResultBean.getAppAccountInfoVo().getHeadIcon());

//		App.initPush(loginResultBean.getAppAccountInfoVo().getAccountId());

		getAuthorityMenu(activity, mGson);

	   } else {
		Toast.makeText(UIUtils.getContext(), loginResultBean.getMsg(), Toast.LENGTH_SHORT)
			.show();
	   }
	} catch (JsonSyntaxException e) {
	   e.printStackTrace();
	}
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
	mLoginLogo.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		continuousClick();
	   }
	});
	mLoginGone.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		if (mTitleConn) {
		   ToastUtils.showShort("正在维护，请到管理端启用");
		   mConfigType = 0;
		   getConfigDate(mConfigType, null);
		} else {
		   ToastUtils.showShortToast("请检查网络");

		}

	   }
	});
	mStockStatus.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {

		if (mTitleConn) {
		   startActivity(new Intent(LoginActivity.this, LoginStockStatusActivity.class));
		} else {
		   ToastUtils.showShortToast("网络异常，请检查网络!");
		}

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

   private void initData() {

	if (mLoginRadiogroup.getCheckedRadioButtonId() == R.id.login_password) {
	   mLoginViewpager.setCurrentItem(0);
	} else {
	   mLoginViewpager.setCurrentItem(1);
	}

	mLoginViewpager.setAdapter(new LoginTitleAdapter(getSupportFragmentManager()));
	mLoginViewpager.addOnPageChangeListener(new PageChangeListener());
	mLoginRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup radioGroup, int i) {
		if (UIUtils.getConfigType(mContext, CONFIG_017)) {
		   switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.login_password:
			   mLoginViewpager.setCurrentItem(0);
			   break;
			case R.id.login_pass:
			   mLoginViewpager.setCurrentItem(1);
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
	});
   }

   /**
    * 版本检测
    */
   public void getUpDateVer(
	   List<ConfigBean.ThingConfigVosBean> tCstConfigVos, int configType, String loginType) {
	NetRequest.getInstance().checkVer(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "checkVer:" + result);

		VersionBean versionBean = mGson.fromJson(result, VersionBean.class);
		if (versionBean.isOperateSuccess()) {
		   // 本地版本号
		   String localVersion = PackageUtils.getVersionName(mContext);
		   // 网络版本
		   String netVersion = versionBean.getSystemVersion().getVersion();
		   if (netVersion != null) {
			int i = StringUtils.compareVersion(netVersion, localVersion);
			if (i == 1) {
			   mDesc = versionBean.getSystemVersion().getDescription();
			   showUpdateDialog(tCstConfigVos, configType, loginType);
			} else {
			   // 不需要更新
			   loginEnjoin(tCstConfigVos, configType, loginType);
			}
		   } else {
			loginEnjoin(tCstConfigVos, configType, loginType);
		   }
		} else {
		   loginEnjoin(tCstConfigVos, configType, loginType);
		}
	   }

	   @Override
	   public void onError(String result) {
		loginEnjoin(tCstConfigVos, configType, loginType);
	   }

	});
   }

   /**
    * 展现更新的dialog
    */
   private void showUpdateDialog(
	   List<ConfigBean.ThingConfigVosBean> tCstConfigVos, int configType, String loginType) {
	UpDateDialog.Builder builder = new UpDateDialog.Builder(this);
	builder.setTitle(UIUtils.getString(R.string.ver_title));
	builder.setMsg(mDesc);
	builder.setLeft(UIUtils.getString(R.string.ver_cancel),
			    new DialogInterface.OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int i) {
				    loginEnjoin(tCstConfigVos, configType, loginType);
				    dialog.dismiss();
				 }
			    });
	builder.setRight(UIUtils.getString(R.string.ver_ok), new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		downloadNewVersion(tCstConfigVos, configType, loginType);//未下载就开始下载
		dialog.dismiss();
	   }
	});

	builder.create().show();

   }

   private void downloadNewVersion(
	   List<ConfigBean.ThingConfigVosBean> tCstConfigVos, int configType, String loginType) {
	// 1.显示进度的dialog
	ProgressDialog mDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
	mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	mDialog.setCancelable(false);
	mDialog.setMax(100);
	mDialog.show();

	loadUpDataVersion(mDialog, tCstConfigVos, configType, loginType);

   }

   private void loadUpDataVersion(
	   final ProgressDialog mDialog, List<ConfigBean.ThingConfigVosBean> tCstConfigVos,
	   int configType, String loginType) {
	OkGo.<File>get(MAIN_URL + URL_UPDATE).tag(this)//
		.params("systemType", SYSTEMTYPE)
		.execute(new FileCallback(FileUtils.getDiskCacheDir(mContext),
						  "RivamedPV.apk") {  //文件下载时，需要指定下载的文件目录和文件名
		   @Override
		   public void onSuccess(Response<File> response) {
			mDialog.dismiss();
			upActivity(response.body());
		   }

		   @Override
		   public void downloadProgress(Progress progress) {
			mDialog.setProgress((int) (progress.fraction / -1024));
			super.downloadProgress(progress);

		   }

		   @Override
		   public void onError(Response<File> response) {
			super.onError(response);
			ToastUtils.showShort(R.string.connection_fails);
			mDialog.dismiss();
			loginEnjoin(tCstConfigVos, configType, loginType);
		   }
		});

   }

   private void upActivity(File file) {
	Intent intent = new Intent(Intent.ACTION_VIEW);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本

	   StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
	   StrictMode.setVmPolicy(builder.build());
	   intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

	} else {
	   intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
	   LogUtils.i(TAG, "apkUri " + Uri.fromFile(file));
	}
	startActivity(intent);
	android.os.Process.killProcess(android.os.Process.myPid());
   }

   //   public void getBoxSize() {
   //	NetRequest.getInstance().loadBoxSize(mContext, new BaseResult() {
   //	   @Override
   //	   public void onSucceed(String result) {
   //		SPUtils.putString(getAppContext(),BOX_SIZE_DATE,"");
   //		BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
   //		LogUtils.i(TAG, "result  " + result);
   //		List<BoxSizeBean.DevicesBean> devices = boxSizeBean.getDevices();
   //		if (devices.size() > 1) {
   //		   BoxSizeBean.DevicesBean tbaseDevicesBean = new BoxSizeBean.DevicesBean();
   //		   tbaseDevicesBean.setDeviceName("全部开柜");
   //		   devices.add(0, tbaseDevicesBean);
   //		}
   //		SPUtils.putString(getAppContext(),BOX_SIZE_DATE,mGson.toJson(devices));
   //	   }
   //
   //	   @Override
   //	   public void onError(String result) {
   //
   //	   }
   //	});
   //   }

   private class PageChangeListener implements ViewPager.OnPageChangeListener {

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
	   if (UIUtils.getConfigType(mContext, CONFIG_017)) {
		switch (position) {
		   case 0:
			mLoginRadiogroup.check(R.id.login_password);
			break;
		   case 1:
			mLoginRadiogroup.check(R.id.login_pass);
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

   /**
    * 获取权限菜单
    */
   public static void getAuthorityMenu(Activity activity, Gson mGson) {
	NetRequest.getInstance().getAuthorityMenu(getAppContext(), new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "getAuthorityMenu  " + result);
		SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE, result);
		List<HomeAuthorityMenuBean> fromJson = mGson.fromJson(result,
											new TypeToken<List<HomeAuthorityMenuBean>>() {}
												.getType());
		setMenuDateAndStart(fromJson, mGson, activity);
	   }
	});
   }

   /**
    * 保存权限配置和跳转
    *
    * @param fromJson
    * @param mGson
    * @param activity
    */
   public static void setMenuDateAndStart(
	   List<HomeAuthorityMenuBean> fromJson, Gson mGson, Activity activity) {
	if (fromJson.size() > 0 && null != fromJson.get(0) && null != fromJson.get(0).getChildren() &&
	    fromJson.get(0).getChildren().get(0).getTitle().equals("选择操作")){
	   SPUtils.putBoolean(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE_ALL, true);
	   List<ChildrenBean> children = fromJson.get(0).getChildren().get(0).getChildren();
	   SPUtils.putString(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE, mGson.toJson(children));
	} else {
	   SPUtils.putBoolean(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE_ALL, false);
	}
	if (fromJson != null && fromJson.size() > 0) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.LOGIN_SUC);
	   Intent intent = new Intent(UIUtils.getContext(), HomeActivity.class);
	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
	   UIUtils.getContext().startActivity(intent);
	   activity.finish();
	} else {
	   Toast.makeText(UIUtils.getContext(), "此账号未配置权限，请联系管理员", Toast.LENGTH_SHORT).show();
	}
   }

   @Override
   protected void onPause() {
	super.onPause();
	mOnStart = false;

   }

   @Override
   public Object newP() {
	return null;
   }
}
