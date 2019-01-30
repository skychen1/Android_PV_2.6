package high.rivamed.myapplication.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import org.androidpn.client.ServiceManager;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.rivamed.DeviceManager;
import cn.rivamed.device.Service.Eth002Service.Eth002ServiceType;
import cn.rivamed.device.Service.UhfService.UhfDeviceType;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.PushFormDateBean;
import high.rivamed.myapplication.cont.Constants;
import high.rivamed.myapplication.utils.ACache;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LogcatHelper;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;
import okhttp3.OkHttpClient;

import static high.rivamed.myapplication.cont.Constants.SAVE_READER_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP_TEXT;

public class App extends Application {

   private List<Activity> oList;//用于存放所有启动的Activity的集合

   public static final String TAG         = "BaseApplication";
   public static       int    READER_TIME = 3000;     //扫描时间
   private static App     instance;
   private static Handler mHandler;
   public static PushFormDateBean                  mPushFormDateBean = new PushFormDateBean();
   public static List<PushFormDateBean.OrdersBean> mPushFormOrders   = new ArrayList<>();

   /**
    * 缓存
    */
   private static ACache mAppCache;

   public static String         MAIN_URL        = null;
   public static boolean        mTitleConn      = false;
   public static boolean        mTitleMsg       = false;
   public static ServiceManager mServiceManager = null;
   private static Context mAppContext;

   public static Handler getHandler() {
	return mHandler;
   }

   public static synchronized App getInstance() {
	return instance;
   }

   /**
    * 获取系统上下文：用于ToastUtil类
    */
   public static Context getAppContext() {
	return mAppContext;
   }

   @Override
   protected void attachBaseContext(Context base) {
	super.attachBaseContext(base);
	MultiDex.install(this);
   }

   @Override
   public void onCreate() {
	super.onCreate();
	oList = new ArrayList<Activity>();
	mAppContext = getApplicationContext();
	SPUtils.putString(this, "TestLoginName", "admin");
	SPUtils.putString(this, "TestLoginPass", "rivamed");
	LitePal.initialize(this);//数据库初始化
	instance = this;
	mHandler = new Handler();
	mPushFormDateBean.setOrders(mPushFormOrders);
	Logger.addLogAdapter(new AndroidLogAdapter());

	initBugly();

	initOkGo();

	InitDeviceService();
	MAIN_URL = SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP);
	if (SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME) == -1) {
	   READER_TIME = 3000;
	} else {
	   READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
	}
	LogcatHelper.getInstance(this).start();
	if (mServiceManager == null && SPUtils.getString(this, SAVE_SEVER_IP_TEXT) != null ) {
	   initMessgeService();
	}

   }

   /**
    * 初始化消息Service
    */
   private void initMessgeService() {
	LogUtils.i(TAG, "initMessgeService   ");
	String id = "xxxxxx";
	mServiceManager = new ServiceManager(App.this, SPUtils.getString(this, SAVE_SEVER_IP_TEXT),
							 id);
	mServiceManager.startService();
   }

   public static void InitDeviceService() {
	//        DeviceManager.getInstance().StartUhfReaderService(UhfDeviceType.UHF_READER_RODINBELL, 8010);
	////        DeviceManager.getInstance().StartUhfReaderService(UhfDeviceType.UHF_READER_COLU_NETTY, 8010);
	//        DeviceManager.getInstance().StartEth002Service(Eth002ServiceType.Eth002V2, 8012);

	DeviceManager.getInstance().AppendUhfService(UhfDeviceType.UHF_READER_COLU_NETTY, 8010);
	DeviceManager.getInstance().AppendUhfService(UhfDeviceType.UHF_READER_RODINBELL, 8014);
	DeviceManager.getInstance().AppendEht002Service(Eth002ServiceType.Eth002V2, 8012);
	DeviceManager.getInstance().AppendEht002Service(Eth002ServiceType.Eth002V26, 8016);
   }

   private void initBugly() {
	/* Bugly SDK初始化
	 * 参数1：上下文对象
	 * 参数2：APPID，平台注册时得到,注意替换成你的appId
	 * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
	 */

	Context context = getApplicationContext();
	// 获取当前包名
	String packageName = context.getPackageName();
	// 获取当前进程名
	String processName = getProcessName(android.os.Process.myPid());
	// 设置是否为上报进程
	CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
	strategy.setUploadProcess(processName == null || processName.equals(packageName));
	// 初始化Bugly
	CrashReport.initCrashReport(context, UIUtils.getString(R.string.bugly_val), true, strategy);
   }

   /**
    * 加载网络框架
    */
   private void initOkGo() {
	OkHttpClient.Builder builder = new OkHttpClient.Builder();
	//log相关
	HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
	loggingInterceptor.setPrintLevel(
		HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
	loggingInterceptor.setColorLevel(
		Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
	builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
	//第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
	//builder.addInterceptor(new ChuckInterceptor(this));

	//超时时间设置，默认60秒
	builder.readTimeout(Constants.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
	builder.writeTimeout(Constants.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
	builder.connectTimeout(Constants.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

	//自动管理cookie（或者叫session的保持），以下几种任选其一就行
	//builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
	builder.cookieJar(new CookieJarImpl(
		new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
	//builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

	// 其他统一的配置
	// 详细说明看GitHub文档：https://github.com/jeasonlzy/
	OkGo.getInstance().init(this)                           //必须调用初始化
		.setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
		.setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
		.setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
		.setRetryCount(
			0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0

   }

   /**
    * 获取进程号对应的进程名
    *
    * @param pid 进程号
    * @return 进程名
    */
   private static String getProcessName(int pid) {
	BufferedReader reader = null;
	try {
	   reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
	   String processName = reader.readLine();
	   if (!TextUtils.isEmpty(processName)) {
		processName = processName.trim();
	   }
	   return processName;
	} catch (Throwable throwable) {
	   throwable.printStackTrace();
	} finally {
	   try {
		if (reader != null) {
		   reader.close();
		}
	   } catch (IOException exception) {
		exception.printStackTrace();
	   }
	}
	return null;
   }

   /**
    * 添加Activity
    */
   public void addActivity_(Activity activity) {
	// 判断当前集合中不存在该Activity
	if (!oList.contains(activity) && !activity.getClass()
		.getName()
		.toString()
		.equals("high.rivamed.myapplication.activity.LoginActivity")) {
	   oList.add(activity);//把当前Activity添加到集合中
	   Log.e(TAG, "Activity-------------->" + activity.getClass().getName());
	}
   }

   /**
    * 销毁单个Activity
    */
   public void removeActivity_(Activity activity) {
	//判断当前集合中存在该Activity
	if (oList.contains(activity)) {
	   oList.remove(activity);//从集合中移除
	   activity.finish();//销毁当前Activity
	}
   }

   /**
    * 销毁所有的Activity
    */
   public void removeALLActivity_() {
	//通过循环，把集合中的所有Activity销毁
	for (Activity activity : oList) {
	   activity.finish();
	}
   }

   public boolean ifActivityRun(String className) {
	Intent intent = new Intent();
	intent.setClassName(getPackageName(), className);
	Log.d(TAG, "getPackageName" + getPackageName() + "className:" + className);
	if (getPackageManager().resolveActivity(intent, 0) != null) {
	   // 说明系统中不存在这个activity
	   return true;
	}
	return false;
   }
}
