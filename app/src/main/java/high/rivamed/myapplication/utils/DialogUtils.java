package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import high.rivamed.myapplication.activity.InOutBoxTwoActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutBoxFoutActivity;
import high.rivamed.myapplication.activity.OutFormConfirmActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.OneDialog;
import high.rivamed.myapplication.views.OneFingerDialog;
import high.rivamed.myapplication.views.OnePassWordDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.StoreRoomDialog;
import high.rivamed.myapplication.views.TwoDialog;

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
		String title = "患者绑定成功";
		showNoDialog(context,title, 2,"nojump",null);
		dialog.dismiss();
	   }
	});
	builder.create().show();
   }
   public  static void showOneDialog(Context context,String title) {
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
    *
    * @param context
    * @param title
    * @param mType 1异常类的弹框  黄色   2成功
    * @param nojump nojump不跳转，out拿出  in 拿入
    * @param bing 是否是绑定病人
    */
   public static void showNoDialog(final Context context, String title, int mType, final String nojump,
					     final String bing) {
	final NoDialog.Builder builder = new NoDialog.Builder(context, mType,nojump,bing);
	builder.setMsg(title);
	builder.setLeft("", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		Log.i("TT", " nojump  " +nojump);
	      if(nojump.equals("out")){
		   //TODO:换成关门后触发跳转柜子的扫描界面。拿出
		   if (bing==null){  //没有绑定病人
			context.startActivity(new Intent(context, OutBoxFoutActivity.class));
		   }else {
			context.startActivity(new Intent(context, OutBoxBingActivity.class));

		   }
		}else if (nojump.equals("in")){
		   Log.i("TT", " EventAct  " );
		   //TODO:换成关门后触发跳转柜子的扫描界面。拿入
		   EventBusUtils.postSticky(new Event.EventAct("all"));
		   Intent intent2 = new Intent(context, InOutBoxTwoActivity.class);
		   context.startActivity(intent2);

		}else if (nojump.equals("form")){
		   context.startActivity(new Intent(context, OutFormConfirmActivity.class));
		}

		dialog.dismiss();
	   }
	});

	builder.create().show();

   }

   public static void showStoreDialog(Context context,int mNumColumn, int mType) {
	StoreRoomDialog.Builder builder = new StoreRoomDialog.Builder(context, mNumColumn, mType);
	if (mType == 2) {
	   builder.setTitle("请选择退货原因");
	} else {
	   builder.setTitle("请选择目标科室");
	}
	builder.setLeft("", new DialogInterface.OnClickListener() {
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
	builder.create().show();
   }

   public static void showTwoDialog(Context context,int mType,String title,String msg) {
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
   public  static void showOnePassWordDialog(Context context) {
	OnePassWordDialog.Builder builder = new OnePassWordDialog.Builder(context);
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {

		dialog.dismiss();
	   }
	});

	builder.create().show();

   }
   public  static void showOneFingerDialog(Context context) {
	OneFingerDialog.Builder builder = new OneFingerDialog.Builder(context);
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {

		dialog.dismiss();
	   }
	});

	builder.create().show();

   }
}
