package high.rivamed.myapplication.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import high.rivamed.myapplication.activity.SplashActivity;

import static high.rivamed.myapplication.base.App.mAppContext;

/**
 * 项目名称:    Android_PV_2.6.8.0
 * 创建者:      DanMing
 * 创建时间:    2019/9/16 0016 14:58
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
   public static final String TAG = "CrashHandler";

   // 系统默认的UncaughtException处理类
   private        Thread.UncaughtExceptionHandler mDefaultHandler;
   // CrashHandler实例
   private static CrashHandler                    INSTANCE = new CrashHandler();
   // 程序的Context对象
   private        Context                         mContext;
   // 用来存储设备信息和异常信息
   private        Map<String, String>             infos    = new HashMap<String, String>();

   // 用于格式化日期,作为日志文件名的一部分
   private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
   private String     nameString;

   /**
    * 保证只有一个CrashHandler实例
    */
   private CrashHandler() {
   }

   /**
    * 获取CrashHandler实例 ,单例模式
    */
   public static CrashHandler getInstance() {
	return INSTANCE;
   }

   /**
    * 初始化
    *
    * @param context
    */
   public void init(Context context) {
	mContext = context;
	// 获取系统默认的UncaughtException处理器
	mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	// 设置该CrashHandler为程序的默认处理器
	Thread.setDefaultUncaughtExceptionHandler(this);
	nameString ="gzhc";
   }

   /**
    * 当UncaughtException发生时会转入该函数来处理
    */
   @Override
   public void uncaughtException(Thread thread, Throwable ex) {
	if (!handleException(ex) && mDefaultHandler != null) {
	   // 如果用户没有处理则让系统默认的异常处理器来处理
	   mDefaultHandler.uncaughtException(thread, ex);
	} else {
	   try {
		Thread.sleep(3000);
	   } catch (InterruptedException e) {
		Log.e(TAG, "error : ", e);
	   }

	   // 退出程序
//	   android.os.Process.killProcess(android.os.Process.myPid());
//	   System.exit(1);


	   Intent it=new Intent(mAppContext, SplashActivity.class);
	   @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(mAppContext, 0, it, Intent.FLAG_ACTIVITY_NEW_TASK);
	   //退出程序
	   AlarmManager mgr = (AlarmManager)mAppContext.getSystemService(Context.ALARM_SERVICE);
	   mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
	   android.os.Process.killProcess(android.os.Process.myPid());
	   System.exit(1);
	}
   }

   /**
    * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
    *
    * @param ex
    * @return true:如果处理了该异常信息;否则返回false.
    */
   private boolean handleException(Throwable ex) {
	if (ex == null) {
	   return false;
	}
	//        WonderMapApplication.getInstance().getSpUtil().setCrashLog(true);// 每次进入应用检查，是否有log，有则上传
	// 使用Toast来显示异常信息
	new Thread() {
	   @Override
	   public void run() {
		Looper.prepare();
		Toast.makeText(mContext, "很抱歉,程序出现异常,正在收集日志，即将退出", Toast.LENGTH_LONG)
			.show();
		Looper.loop();
	   }
	}.start();
	// 收集设备参数信息
	collectDeviceInfo(mContext);
	// 保存日志文件
	String fileName = saveCrashInfo2File(ex);
	return true;
   }

   /**
    * 收集设备参数信息
    *
    * @param ctx
    */
   public void collectDeviceInfo(Context ctx) {
	try {
	   PackageManager pm = ctx.getPackageManager();
	   PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
							  PackageManager.GET_ACTIVITIES);
	   if (pi != null) {
		String versionName = pi.versionName == null ? "null"
			: pi.versionName;
		String versionCode = pi.versionCode + "";
		infos.put("versionName", versionName);
		infos.put("versionCode", versionCode);
	   }
	} catch (PackageManager.NameNotFoundException e) {
	   Log.e(TAG, "an error occured when collect package info", e);
	}
	Field[] fields = Build.class.getDeclaredFields();
	for (Field field : fields) {
	   try {
		field.setAccessible(true);
		infos.put(field.getName(), field.get(null).toString());
		Log.d(TAG, field.getName() + " : " + field.get(null));
	   } catch (Exception e) {
		Log.e(TAG, "an error occured when collect crash info", e);
	   }
	}
   }

   /**
    * 保存错误信息到文件中
    *
    * @param ex
    * @return 返回文件名称, 便于将文件传送到服务器
    */
   private String saveCrashInfo2File(Throwable ex) {

	StringBuffer sb = new StringBuffer();
	for (Map.Entry<String, String> entry : infos.entrySet()) {
	   String key = entry.getKey();
	   String value = entry.getValue();
	   sb.append(key + "=" + value + "\n");
	}

	Writer writer = new StringWriter();
	PrintWriter printWriter = new PrintWriter(writer);
	ex.printStackTrace(printWriter);
	Throwable cause = ex.getCause();
	while (cause != null) {
	   cause.printStackTrace(printWriter);
	   cause = cause.getCause();
	}
	printWriter.close();
	String result = writer.toString();
	sb.append(result);
	Log.e("CrashHandler",sb.toString());
	try {
	   long timestamp = System.currentTimeMillis();
	   String time = formatter.format(new Date());
	   String fileName = nameString + "-" + time + "-" + timestamp
				   + ".log";
	   if (Environment.getExternalStorageState().equals(
		   Environment.MEDIA_MOUNTED)) {
		String path =Environment.getExternalStorageDirectory() + "/"+"RuiHua_CrashLogDir";
		File dir = new File(path);
		if (!dir.exists()) {
		   dir.mkdirs();
		}
		File file = new File(dir,fileName);
		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
		fos.write(sb.toString().getBytes());
		fos.close();
	   }
	   return fileName;
	} catch (Exception e) {
	   Log.e(TAG, "an error occured while writing file...", e);
	}
	return null;
   }

}
