package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.timeutil.DateListener;
import high.rivamed.myapplication.timeutil.TimeConfig;
import high.rivamed.myapplication.timeutil.TimeSelectorDialog;
import high.rivamed.myapplication.views.EpcTestDialog;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.OneDialog;
import high.rivamed.myapplication.views.OneFingerDialog;
import high.rivamed.myapplication.views.OnePassWordDialog;
import high.rivamed.myapplication.views.RegisteDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.StoreRoomDialog;
import high.rivamed.myapplication.views.TwoDialog;
import high.rivamed.myapplication.views.WifiDialog;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 9:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class DialogUtils {

   private static String sTimes;

   public static void showRvDialog(Activity activity, final Context context) {
	RvDialog.Builder builder = new RvDialog.Builder(activity, context);
	builder.setMsg("耗材中包含过期耗材，请查看！");
	builder.setLeft("取消", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		dialog.dismiss();
	   }
	});
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {

		for (int x = 0; x <= RvDialog.genData5().size(); x++) {
		   if (RvDialog.sTableTypeView.mCheckStates.get(x)) {
			RvDialog.sMovie = RvDialog.genData5().get(x);
			Log.i("vv", "   " + RvDialog.sMovie.five + "   " +
					RvDialog.sTableTypeView.mCheckStates.get(x));
			EventBusUtils.postSticky(new Event.EventCheckbox(RvDialog.sMovie.five));
			String title = "患者绑定成功";
			DialogUtils.showNoDialog(context, title, 2, "nojump", null);
			dialog.dismiss();
		   }
		}
	   }
	});
	builder.create().show();
   }

   public static void showOneDialog(Context context, String title) {
	OneDialog.Builder builder = new OneDialog.Builder(context);
	builder.setMsg(title);
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {

		dialog.dismiss();
	   }
	});

	builder.create().show();

   }

   /**
    * @param context
    * @param title
    * @param mType   1异常类的弹框  黄色   2成功
    * @param nojump  nojump不跳转，out拿出  in 拿入
    * @param bing    是否是绑定病人
    */
   public static NoDialog.Builder showNoDialog(
	   final Context context, String title, int mType, final String nojump, final String bing) {
	final NoDialog.Builder builder = new NoDialog.Builder(context, mType, nojump, bing);
	builder.setMsg(title);
	builder.setLeft("", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		//		Log.i("TT", " nojump  " +nojump);
		//	      if(nojump.equals("out")){
		//		   //TODO:换成关门后触发跳转柜子的扫描界面。拿出
		//		   if (bing==null){  //没有绑定病人
		//			context.startActivity(new Intent(context, OutBoxFoutActivity.class));
		//		   }else {
		//			context.startActivity(new Intent(context, OutBoxBingActivity.class));
		//
		//		   }
		//		}else if (nojump.equals("in")){
		//		   Log.i("TT", " EventAct  " );
		//		   //TODO:换成关门后触发跳转柜子的扫描界面。拿入
		////		   EventBusUtils.postSticky(new Event.EventAct("all"));
		////		   Intent intent2 = new Intent(context, InOutBoxTwoActivity.class);
		////		   context.startActivity(intent2);
		//
		//		}else if (nojump.equals("form")){
		//		   if (bing ==null){
		//			context.startActivity(new Intent(context, OutFormConfirmActivity.class));
		//		   }else {//绑定患者的套餐
		//			context.startActivity(new Intent(context, OutMealBingConfirmActivity.class));
		//		   }
		//
		//		}

		dialog.dismiss();
	   }
	});

	builder.create().show();
	return builder;
   }

   public static void showStoreDialog(Context context, int mNumColumn, int mType,HospNameBean hospNameBean) {
	StoreRoomDialog.Builder builder = new StoreRoomDialog.Builder(context, mNumColumn, mType,hospNameBean);
	if (mType == 2) {
	   builder.setTitle("请选择退货原因");
	} else if (mType ==1){
	   builder.setTitle("请选择库房");
	}else {
	   builder.setTitle("请选择目标科室");
	}
	builder.setLeft("", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		dialog.dismiss();
	   }
	});

	builder.create().show();
   }

   public static void showTwoDialog(Context context, int mType, String title, String msg) {
	TwoDialog.Builder builder = new TwoDialog.Builder(context, mType);
	if (mType == 1) {
	   builder.setTwoMsg(msg);
	   builder.setMsg(title);
	   builder.setLeft("取消", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int i) {
		   dialog.dismiss();
		}
	   });
	   builder.setRight("确认", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int i) {
		   dialog.dismiss();
		}
	   });
	} else {
	   builder.setTwoMsg(msg);
	   builder.setMsg(title);
	   builder.setLeft("确认", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int i) {
		   dialog.dismiss();
		}
	   });
	   builder.setRight("确认并退出登录", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int i) {
		   dialog.dismiss();
		}
	   });
	}
	builder.create().show();
   }

   public static void showOnePassWordDialog(Context context) {
	OnePassWordDialog.Builder builder = new OnePassWordDialog.Builder(context);
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {

		dialog.dismiss();
	   }
	});

	builder.create().show();

   }

   public static void showOneFingerDialog(Context context) {
	OneFingerDialog.Builder builder = new OneFingerDialog.Builder(context);
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {

		dialog.dismiss();
	   }
	});

	builder.create().show();

   }

   public static String showTimeDialog(final Context context, final TextView textView) {
	Date date = new Date();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String format = sdf.format(date);
	Log.i("cc", "    " + format);
	TimeSelectorDialog dialog = new TimeSelectorDialog(context);
	//设置标题
	dialog.setTimeTitle("选择时间:  ");
	//显示类型
	dialog.setIsShowtype(TimeConfig.YEAR_MONTH_DAY_HOUR_MINUTE);
	//默认时间
	dialog.setCurrentDate(format);
	//隐藏清除按钮
	dialog.setEmptyIsShow(false);
	//设置起始时间
	dialog.setStartYear(2000);
	dialog.setDateListener(new DateListener() {
	   @Override
	   public void onReturnDate(
		   String time, int year, int month, int day, int hour, int minute, int isShowType,
		   long times) {
		textView.setText(time);
		textView.setTextColor(context.getResources().getColor(R.color.text_color_3));
		sTimes = times + "";
	   }

	   @Override
	   public void onReturnDate(String empty) {
	   }
	});
	dialog.show();
	return sTimes;
   }



   public static void showRegisteDialog(final Context context, Activity activity) {

	RegisteDialog.Builder builder = new RegisteDialog.Builder(context, activity);
	builder.setLeft("取消", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		dialog.dismiss();
	   }
	});
	builder.setOnSettingListener(new RegisteDialog.Builder.SettingListener() {
	   @Override
	   public void getDialogDate(
		   String branchCode, String deptCode, String storehouseCode, String operationRoomNo,
		   Dialog dialog) {
		EventBusUtils.postSticky(new Event.dialogEvent(branchCode,deptCode,storehouseCode,operationRoomNo,dialog));
	   }
	});


	builder.create().show();
   }

   /**
    * 设置功率
    *
    * @param context
    */
   public static void showWifiDialog(final Context context) {
	WifiDialog.Builder builder = new WifiDialog.Builder(context);
	builder.create().show();
   }

   /**
    * 设置功率
    *
    * @param context
    */
   public static void showEpcDialog(final Context context) {
	EpcTestDialog.Builder builder = new EpcTestDialog.Builder(context);
	builder.create().show();
   }

   public static LoadingDialog.Builder showLoading(final Context context) {
	LoadingDialog.Builder builder = new LoadingDialog.Builder(context);
	builder.create().show();
	return builder;
   }
}
