package high.rivamed.myapplication.base;

import android.app.Application;
import android.os.Handler;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.http.NetApi;
import high.rivamed.myapplication.utils.ACache;
import high.rivamed.myapplication.utils.UIUtils;
import okhttp3.OkHttpClient;

public class App extends Application {

   public static final String TAG = "BaseApplication";

   private static App     instance;
   private static Handler mHandler;
   /**
    * 缓存
    */
   private static ACache  mAppCache;

   public static String MAIN_URL = NetApi.RELEASED_URL;

   public static Handler getHandler() {
	return mHandler;
   }

   public static synchronized App getInstance() {
	return instance;
   }

   @Override
   public void onCreate() {
	super.onCreate();
	LitePal.initialize(this);//数据库初始化
	instance = this;
	mHandler = new Handler();
	mAppCache = ACache.get(UIUtils.getContext());
	Logger.addLogAdapter(new AndroidLogAdapter());
	initServer();

	//		initBugly();

	initOkGo();

   }

   private void initBugly() {
		/* Bugly SDK初始化
	  * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
	//		CrashReport.initCrashReport(getApplicationContext(), UIUtils.getString(R.string.bugly_val), BuildConfig.API_ENV);
	CrashReport.initCrashReport(getApplicationContext(), UIUtils.getString(R.string.bugly_val),
					    true);//测试
   }

   public ACache getAppCache() {
	return mAppCache;
   }

   /**
    * 选择服务器
    */
   private void initServer() {
	if (BuildConfig.API_ENV) {
	   MAIN_URL = NetApi.BETA_URL;
	} else {
	   MAIN_URL = NetApi.RELEASED_URL;
	}
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
	builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
	builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
	builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

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
			3);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0

   }

}
