package high.rivamed.myapplication.activity;

import android.app.Activity;
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
import high.rivamed.myapplication.service.ScanService;
import high.rivamed.myapplication.utils.FaceTask;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LogcatHelper;
import high.rivamed.myapplication.utils.RxPermissionUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_READER_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.utils.UIUtils.fullScreenImmersive;

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
		if (SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME) == -1) {
//		   READER_TIME = 3000;
		} else {
		   READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
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
	new Thread(new Runnable() {
	   @Override
	   public void run() {
		startService(new Intent(SplashActivity.this, ScanService.class));
	   }
	}).start();

		//人脸识别SDK初始化权限申请：存储 相机 这里elo设备点击允许存储权限页面会关闭，原因未知
	   RxPermissionUtils.checkCameraReadWritePermission(this, hasPermission -> {
		   if (hasPermission && FaceManager.getManager().hasActivation(SplashActivity.this)) {
			   //检测设备是否激活授权码
			   //启动页初始化人脸识别sdk
			   FaceManager.getManager().init(SplashActivity.this, false, new InitListener() {
				   @Override
				   public void initSuccess() {
					   ToastUtils.showShortSafe("人脸识别SDK初始化成功");
					   //初始化分组
					   boolean b = FaceManager.getManager().initGroup();
					   if (!b) {
						   ToastUtils.showShortSafe("创建人脸照分组失败");
						   //初始化完成后跳转页面
						   launchLogin();
					   } else {
						   //设置是否需要活体
						   FaceManager.getManager().setNeedLive(false);
						   //从服务器更新人脸底库并注册至本地
						   FaceTask faceTask = new FaceTask(SplashActivity.this);
						   faceTask.setCallBack((hasRegister, msg) -> {
							   LogUtils.d("faceTask", "initListener: " + msg);
							   ToastUtils.showShortSafe(msg);
						   });
						   faceTask.getAllFaceAndRegister();
						   //初始化完成后跳转页面
						   launchLogin();
					   }
				   }

				   @Override
				   public void initFail(int errorCode, String msg) {
					   ToastUtils.showShortSafe("人脸识别SDK初始化失败：：errorCode = " + errorCode + ":::msg：" + msg);
					   //初始化完成后跳转页面
					   launchLogin();
				   }
			   });
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
