package high.rivamed.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;

import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;

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
	if (time - lastClickTime < 2500) {
	   return true;
	}
	lastClickTime = time;
	return false;

   }

   /**
    * 设置某个效期的背景
    *
    * @param helper
    * @param type 0 过期，1 小鱼30   2小鱼90    3  小鱼120   4
    * @param textview
    */
   public static void initTermOfValidity(Context mContext,BaseViewHolder helper, int type, TextView textview) {

	if (type==0) {
	   textview.setBackgroundResource(R.drawable.bg_text_red);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type==3) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow1);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type==2) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow2);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type==1) {
	   textview.setBackgroundResource(R.drawable.bg_text_orange);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else {
	   if (helper.getAdapterPosition() % 2 == 0) {
		textview.setBackgroundResource(R.color.bg_color);
	   } else {
		textview.setBackgroundResource(R.color.bg_f);
	   }
	   textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}

   }

	private static InputFilter filter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			//返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
			if (source.equals(" "))
				return "";
			else
				return null;
		}
	};

	public static void setInputLenWithNoBlank(TextView textView, int max) {

		textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max), filter});

	}
   /**
    * 获得版本信息
    *
    * @param context
    * @return
    */
   public static String getVersionName(Context context) {
	// 获得包管理器
	PackageManager pm = context.getPackageManager();
	try {
	   // 清单文件的对象
	   PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
	   return packageInfo.versionName;

	} catch (PackageManager.NameNotFoundException e) {
	   e.printStackTrace();
	   return "未知版本";
	}
   }

   /**
    * 获取配置项
    * @param context
    * @param code
    * @return
    */
   public static boolean getConfigType(Context context,String code) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_CONFIG_STRING);
//	LogUtils.i("ContentConsumeOperateFrag","SAVE_CONFIG_STRING   "+string);
	ConfigBean configBean = gson.fromJson(string, ConfigBean.class);
	if (configBean!=null){
	   List<ConfigBean.TCstConfigVosBean> tCstConfigVos = configBean.getTCstConfigVos();
	   for (ConfigBean.TCstConfigVosBean configType :tCstConfigVos) {
		if (code.equals(configType.getCode())){
		   return true;
		}
	   }
	}
	return false;
   }
   /**
    * 获取配置项reader的val
    * @param context
    * @param code
    * @return
    */
   public static String getConfigReaderType(Context context,String code) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_CONFIG_STRING);
//	LogUtils.i("ContentConsumeOperateFrag","SAVE_CONFIG_STRING   "+string);
	ConfigBean configBean = gson.fromJson(string, ConfigBean.class);
	if (configBean!=null){
	   List<ConfigBean.TCstConfigVosBean> tCstConfigVos = configBean.getTCstConfigVos();
	   for (ConfigBean.TCstConfigVosBean configType :tCstConfigVos) {
		if (code.equals(configType.getCode())){
		   return configType.getValue();
		}
	   }
	}
	return "1";
   }
   /**
    * 获取权限配置主按钮
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuLeftType(Context context,String title) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_MENU_LEFT_TYPE);
	List<HomeAuthorityMenuBean> fromJson = gson.fromJson(string,new TypeToken<List<HomeAuthorityMenuBean>>() {}
											.getType());
	if (fromJson!=null&&fromJson.size()>0){
	   for (HomeAuthorityMenuBean mType :fromJson) {
		if (title.equals(mType.getTitle())){
		   return true;
		}
	   }
	}
	return false;
   }
   /**
    * 获取权限配置主按钮
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuDownType(Context context,String title) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_MENU_DOWN_TYPE);
	List<HomeAuthorityMenuBean.ChildrenBeanX.ChildrenBean> fromJson = gson.fromJson(string,new TypeToken<List<HomeAuthorityMenuBean.ChildrenBeanX.ChildrenBean>>() {}
		.getType());
	if (fromJson!=null&&fromJson.size()>0){
	   for (HomeAuthorityMenuBean.ChildrenBeanX.ChildrenBean mType :fromJson) {
		if (title.equals(mType.getTitle())){
		   return true;
		}
	   }
	}
	return false;
   }
}
