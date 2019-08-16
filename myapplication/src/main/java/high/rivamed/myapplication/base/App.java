package high.rivamed.myapplication.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ToastUtils;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.ReaderProducerType;

import org.androidpn.client.ServiceManager;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.rivamed.Eth002Manager;
import high.rivamed.myapplication.bean.PushFormDateBean;
import high.rivamed.myapplication.cont.Constants;
import high.rivamed.myapplication.http.MyHttpLoggingInterceptor;
import high.rivamed.myapplication.utils.ACache;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;
import okhttp3.OkHttpClient;

import static high.rivamed.myapplication.cont.Constants.READER_NAME;
import static high.rivamed.myapplication.cont.Constants.READER_NAME_COLU;
import static high.rivamed.myapplication.cont.Constants.READER_NAME_RODINBELL;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP_TEXT;

public class App extends Application {

   private List<Activity> oList;//用于存放所有启动的Activity的集合除开登录界面
   private List<Activity> oListAll;//用于存放所有启动的Activity的集合

   public static final String TAG            = "BaseApplication";
   public static       int    READER_TIME    = 3000;     //扫描时间
   public static       int    COUNTDOWN_TIME = 20000;         //无操作退出时间
   private static App instance;
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
   public static Context mAppContext;
   public static DisplayMetrics mDm;
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
	oListAll = new ArrayList<Activity>();
	mAppContext = getApplicationContext();
	mPushFormDateBean.setOrders(mPushFormOrders);
	LitePal.initialize(this);//数据库初始化
	registDevice();//注册硬件
	instance = this;
	initOkGo();
	LogUtils.setDebugMode(false);
	//设备基础module中有使用，需要注册初始化
	ToastUtils.register(this);

	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	if (wm != null) {
	   mDm = new DisplayMetrics();
	   wm.getDefaultDisplay().getMetrics(mDm);
	}
   }

   public static void initPush(String id) {
	if (id != null) {
	   mServiceManager = new ServiceManager(UIUtils.getContext());
	   mServiceManager.init("CC39A99BD90D179F",
					SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP_TEXT), "5222");
	   mServiceManager.startService(id);
	}
   }

   public void registDevice() {

	new Thread(new Runnable() {
	   @Override
	   public void run() {
		//设置reader的连接方式，不设置，默认为网络连接方式
		//		ReaderManager.getManager().setConnectType(ReaderConnectType.TYPE_NET);
		//设置是否是模拟形式，默认不是模拟
		//		ReaderManager.getManager().setSimulation(true);
		//连接罗丹贝尔的设备
		if (SPUtils.getString(mAppContext, READER_NAME) == null ||
		    SPUtils.getString(mAppContext, READER_NAME)
			    .equals(READER_NAME_RODINBELL)) {
		   ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);
		} else if (SPUtils.getString(mAppContext, READER_NAME) != null &&
			     SPUtils.getString(mAppContext, READER_NAME)
				     .equals(READER_NAME_COLU)) {
		   ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_COLU);
		}
		//		ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_COLU);
		//		ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);

		//li模块儿
		Eth002Manager.getEth002Manager().startService(8012);
	   }
	}).start();
   }

   /**
    * 加载网络框架
    */
   private void initOkGo() {
	OkHttpClient.Builder builder = new OkHttpClient.Builder();
	//log相关
	MyHttpLoggingInterceptor loggingInterceptor = new MyHttpLoggingInterceptor("OkGo");
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
	builder.connectTimeout(Constants.DEFAULT_CONNECTMILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

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
    * 添加Activity
    */
   public void addActivityAll(Activity activity) {
	// 判断当前集合中不存在该Activity
	if (!oListAll.contains(activity)) {
	   oListAll.add(activity);//把当前Activity添加到集合中
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
//	for (Activity activity : oListAll) {
//	   activity.finish();
//	}
   }
   public int getActivitySize(){
	return oListAll.size();
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
