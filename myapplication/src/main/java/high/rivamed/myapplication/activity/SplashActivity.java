package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.callback.InitListener;

import org.litepal.LitePal;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.http.NetApi;
import high.rivamed.myapplication.service.ScanService;
import high.rivamed.myapplication.utils.FaceTask;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LogcatHelper;
import high.rivamed.myapplication.utils.RxPermissionUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.base.App.ANIMATION_TIME;
import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.base.App.mAppContext;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.SAVE_ANIMATION_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_READER_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP_TEXT;
import static high.rivamed.myapplication.utils.UIUtils.fullScreenImmersive;
import static high.rivamed.myapplication.utils.WifiUtils.isAvailableByPing;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/3/4 17:59
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SplashActivity extends FragmentActivity {

   public static Intent mIntentService;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	onWindowFocusChanged(true);
	setContentView(R.layout.activity_splash_layout);
	Log.e("版本号：", UIUtils.getVersionName(this));
	initData();
   }

   @Override
   public void onWindowFocusChanged(boolean hasFocus) {
	super.onWindowFocusChanged(hasFocus);
	fullScreenImmersive(this.getWindow().getDecorView());
   }

   private void initData() {
	mIntentService = new Intent(SplashActivity.this, ScanService.class);
	Logger.addLogAdapter(new AndroidLogAdapter());
	setDate();//设置默认值
	initLitePal();//数据库

	startAct();//页面跳转
   }

   private void setDate() {
	new Thread(new Runnable() {
	   @Override
	   public void run() {
		MAIN_URL = SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP);
		String urls = MAIN_URL + NetApi.URL_CONNECT;
		Log.i("outtccc","MAIN_URL     "+MAIN_URL+"    ");
		if (MAIN_URL!=null){
		   boolean byPing = isAvailableByPing(
			   SPUtils.getString(mAppContext, SAVE_SEVER_IP_TEXT));

		   if (byPing) {
			mTitleConn = true;
		   } else {
			mTitleConn = false;
		   }

//		   OkGo.<String>get(urls).tag(this).execute(new StringCallback() {
//			@Override
//			public void onSuccess(Response<String> response) {
//			   mTitleConn = true;
//			}
//
//			@Override
//			public void onError(Response<String> response) {
//			   mTitleConn = false;
//			}
//		   });
		}
		if (SPUtils.getInt(UIUtils.getContext(), SAVE_ANIMATION_TIME) == -1) {
		   ANIMATION_TIME = 1000;
		} else {
		   ANIMATION_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_ANIMATION_TIME);
		}
		if (SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME) == -1) {
		   READER_TIME = 3000;
		} else {
		   READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
		}
		if (SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME)==-1){
		   COUNTDOWN_TIME = 20000;
		}else {
		   COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME);
		}
		LogcatHelper.getInstance(getApplicationContext()).start();
		SPUtils.putString(getApplicationContext(), "TestLoginName", "admin");
		SPUtils.putString(getApplicationContext(), "TestLoginPass", "rivamed");
	   }
	}).start();

   }

   private void initLitePal() {
	new Thread(new Runnable() {
	   @Override
	   public void run() {
		//创建数据库表
		LitePal.getDatabase();
		if (!SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
		   LitePal.deleteAll(BoxIdBean.class);
		}
	   }
	}).start();
   }

   private void startAct() {

	new Thread(() -> startService(mIntentService)).start();

	//人脸识别SDK初始化权限申请：存储 相机 这里elo设备点击允许存储权限页面会关闭，原因未知
	RxPermissionUtils.checkCameraReadWritePermission(this, hasPermission -> {
	   if (hasPermission && FaceManager.getManager().hasActivation(SplashActivity.this)) {
		//		   if ( FaceManager.getManager().hasActivation(SplashActivity.this)) {
		//检测设备是否激活授权码
		//启动页初始化人脸识别sdk
		new Thread(() -> FaceManager.getManager()
			.init(SplashActivity.this, false, new InitListener() {
			   @Override
			   public void initSuccess() {
				UIUtils.runInUIThread(() -> ToastUtils.showShortToast("人脸识别SDK初始化成功"));
				//初始化分组
				boolean b = FaceManager.getManager().initGroup();
				LogUtils.d("Face","runInUIThread  "+b);
				if (!b) {
				   UIUtils.runInUIThread(() -> ToastUtils.showShortToast("创建人脸照分组失败"));
				   //初始化完成后跳转页面
				   launchLogin();
				} else {
				   LogUtils.d("Face", "initSuccess 1  " + b);
				   //从服务器更新人脸底库并注册至本地
				   FaceTask faceTask = new FaceTask(SplashActivity.this);
				   faceTask.setCallBack((hasRegister, msg) -> {
					if (msg != null) {
					   UIUtils.runInUIThread(() -> ToastUtils.showShortToast(msg));
					}
					//初始化完成后跳转页面
					launchLogin();
				   });
				   LogUtils.d("Face","initSuccess 2  "+b);
				   if (MAIN_URL!=null){
					faceTask.getAllFaceAndRegister();
				   }
				   //初始化完成后跳转页面
				   //launchLogin();
				}
			   }

			   @Override
			   public void initFail(int errorCode, String msg) {
				LogUtils.d("Face", "initFail 1  ");
				UIUtils.runInUIThread(() -> ToastUtils.showShortToast(
					"人脸识别SDK初始化失败：：errorCode = " + errorCode + ":::msg：" + msg));
				//初始化完成后跳转页面
				LogUtils.d("Face", "initFail 2  ");
				launchLogin();
			   }
			})).start();
	   } else {
		new Handler().postDelayed(this::launchLogin, 2000);
	   }
	});
   }

   private void launchLogin() {
	startActivity(new Intent(SplashActivity.this, LoginActivity.class));
	finish();
   }
}
