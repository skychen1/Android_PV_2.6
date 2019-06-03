package high.rivamed.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.androidpn.utils.XmppEvent;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.PendingTaskBean;
import high.rivamed.myapplication.dbmodel.ChildrenBean;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;

import static high.rivamed.myapplication.base.App.mPushFormDateBean;
import static high.rivamed.myapplication.cont.Constants.REFRESH_TOKEN;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

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

   private static long lastClickTime = 0L;
   private static int  lastButtonId  = -1;

   public static boolean isFastDoubleClick() {
	long time = System.currentTimeMillis();
	if (time - lastClickTime < 3000) {
	   return true;
	}
	lastClickTime = time;
	return false;

   }

   public static boolean isFastDoubleClick(int buttonId) {
	long time = System.currentTimeMillis();
	if (lastButtonId == buttonId && time - lastClickTime < 3000) {
	   return true;
	}
	lastClickTime = time;
	lastButtonId = buttonId;
	return false;
   }

   /**
    * 设置某个效期的背景
    *
    * @param type     0 过期，1 小鱼30   2小鱼90    3  小鱼120   4
    * @param textview
    */
   public static void initTermOfValidity(
	   Context mContext, int IsErrorOperation, int type, TextView textview) {
	if (type == 0) {
	   //	   textview.setBackgroundResource(R.drawable.bg_text_red);
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_overdue_prompt));
	} else if (type == 3) {
	   //	   textview.setBackgroundResource(R.drawable.bg_text_yellow1);
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_100_prompt));
	} else if (type == 2) {
	   //	   textview.setBackgroundResource(R.drawable.bg_text_yellow2);
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_70_prompt));
	} else if (type == 1) {
	   //	   textview.setBackgroundResource(R.drawable.bg_text_orange);
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_28_prompt));
	} else if (type == 4) {
	   //	   if (helper.getAdapterPosition() % 2 == 0) {
	   //		textview.setBackgroundResource(R.color.bg_color);
	   //	   } else {
	   //		textview.setBackgroundResource(R.color.bg_f);
	   //	   }
	   if (IsErrorOperation == 1) {
		textview.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   } else {
		textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   }

	} else {
	   LogUtils.i("SSS", "type   " + type);
	   textview.setVisibility(View.GONE);
	}

   }

   /**
    * 底色不同
    *
    * @param mContext
    * @param helper
    * @param type
    * @param textview
    */
   public static void initTermOfValidity2(
	   Context mContext, BaseViewHolder helper, int type, TextView textview) {

	if (type == 0) {
	   textview.setBackgroundResource(R.drawable.bg_text_red);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type == 3) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow1);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type == 2) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow2);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type == 1) {
	   textview.setBackgroundResource(R.drawable.bg_text_orange);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else {
	   textview.setBackgroundResource(R.color.bg_f);
	   textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}

   }

   private static InputFilter filter = new InputFilter() {
	@Override
	public CharSequence filter(
		CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
	   //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
	   if (source.equals(" ")) {
		return "";
	   } else {
		return null;
	   }
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
    *
    * @param context
    * @param code
    * @return
    */
   public static boolean getConfigType(Context context, String code) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_CONFIG_STRING);
	//	LogUtils.i("ContentConsumeOperateFrag","SAVE_CONFIG_STRING   "+string);
	ConfigBean configBean = gson.fromJson(string, ConfigBean.class);
	if (configBean != null) {
	   List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
	   for (ConfigBean.ThingConfigVosBean configType : tCstConfigVos) {
		if (code.equals(configType.getCode())) {
		   return true;
		}
	   }
	}
	return false;
   }

   /**
    * 获取权限配置主按钮
    *
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuLeftType(Context context, String title) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_MENU_LEFT_TYPE);
	List<HomeAuthorityMenuBean> fromJson = gson.fromJson(string,
									     new TypeToken<List<HomeAuthorityMenuBean>>() {}
										     .getType());
	if (fromJson != null && fromJson.size() > 0) {
	   for (HomeAuthorityMenuBean mType : fromJson) {
		if (title.equals(mType.getTitle())) {
		   return true;
		}
	   }
	}
	return false;
   }

   /**
    * 获取权限配置主按钮
    *
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuDownType(Context context, String title) {
	Gson gson = new Gson();
	String string = SPUtils.getString(context, SAVE_MENU_DOWN_TYPE);
	List<ChildrenBean> fromJson = gson.fromJson(string,
								  new TypeToken<List<ChildrenBean>>() {}.getType());
	if (fromJson != null && fromJson.size() > 0) {
	   for (ChildrenBean mType : fromJson) {
		if (title.equals(mType.getTitle())) {
		   return true;
		}
	   }
	}
	return false;
   }

   public static String getRefreshToken() {
	String RefreshToken = SPUtils.getString(UIUtils.getContext(), REFRESH_TOKEN);
	return RefreshToken;
   }

   public static void putOrderId(Object tag) {
	mPushFormDateBean.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	Gson gson = new Gson();
	gson.toJson(mPushFormDateBean);
	Log.i("twoDialog", gson.toJson(mPushFormDateBean));
	NetRequest.getInstance()
		.submitOrderCstInfo(gson.toJson(mPushFormDateBean), tag, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			Log.i("twoDialog", "result：receiveOrderId上传成功");
		   }

		   @Override
		   public void onError(String result) {
			Log.i("twoDialog", "Erorr：" + result);
		   }
		});
   }

   /**
    * 全屏显示，隐藏虚拟按钮
    *
    * @param view
    */
   public static void fullScreenImmersive(View view) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	   int uiOptions =
		   View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
		   View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
		   View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN |
		   View.SYSTEM_UI_FLAG_IMMERSIVE;
	   view.setSystemUiVisibility(uiOptions);
	}
   }

   /**
    * 获取代办任务的数量
    */
   public static void setMessagersV() {
	new Thread(new Runnable() {
	   @Override
	   public void run() {
		Gson mGson = new Gson();
		NetRequest.getInstance().getPendingTaskList(this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			try {
			   PendingTaskBean emergencyBean = mGson.fromJson(result, PendingTaskBean.class);
			   int size = emergencyBean.getMessages().size();
			   if (emergencyBean.getMessages().size() == 0) {
				EventBusUtils.post(new XmppEvent.EventPushMessageNum(size));
			   } else {
				EventBusUtils.post(new XmppEvent.EventPushMessageNum(size));
			   }
			} catch (JsonSyntaxException e) {

			}
		   }
		});
	   }
	}).start();
   }

   /**
    * 禁止radiogroup点击
    *
    * @param testRadioGroup
    */
   public static void disableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(false);
	}
   }

   /**
    * 恢复点击
    *
    * @param testRadioGroup
    */
   public static void enableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(true);
	}
   }

   /**
    * 是否包含epc
    * @return
    */
   public static boolean getVosType(List<InventoryVo> vos, String epc) {
	if (vos != null) {
	   for (int i = 0; i < vos.size(); i++) {
		if (vos.get(i).getEpc().equals(epc)) {
		   return true;
		}
	   }
	}
	return false;
   }
}