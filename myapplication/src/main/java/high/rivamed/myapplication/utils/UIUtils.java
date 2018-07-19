package high.rivamed.myapplication.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import high.rivamed.myapplication.base.App;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class UIUtils {

   private static Handler mHandler = new Handler();

   /**
    * 在ui线程里面开启子线程
    *
    * @param task
    */
   public static void runInUIThread(Runnable task) {
	mHandler.post(task);
   }

   /**
    * 在ui线程里面开启延迟子线程
    *
    * @param task
    */
   public static void runInUIThread(Runnable task, long delayMillis) {
	mHandler.postDelayed(task, delayMillis);
   }

   /**
    * 移除所有主线程的Handler中的所有队列
    */
   public static void removeAllCallbacks() {
	mHandler.removeCallbacksAndMessages(null);
   }

   /**
    * 得到上下文
    *
    * @return
    */
   public static Context getContext() {
	return App.getInstance().getApplicationContext();
   }

   /**
    * 得到Resources对象
    *
    * @return
    */
   public static Resources getResources() {
	return getContext().getResources();
   }

   /**
    * 得到color.xml的颜色信息
    *
    * @param resId
    * @return
    */
   public static int getColor(int resId) {
	return getResources().getColor(resId);
   }

   /**
    * 得到String.xml中的字符
    *
    * @param resId 资源id
    * @return
    */
   public static String getString(int resId) {
	return getResources().getString(resId);
   }
   /**
    * 得到应用程序的包名
    *
    * @return
    */
   public static String getPackageName() {
	return getContext().getPackageName();
   }

   public static void hideSoftInput(Context context, View view) {
	InputMethodManager imm = (InputMethodManager) context.getSystemService(
		Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
   }

   private static long lastClickTime;

   public static boolean isFastDoubleClick() {
	long time = System.currentTimeMillis();
	if (time - lastClickTime < 2000) {
	   return true;
	}
	lastClickTime = time;
	return false;

   }
}
