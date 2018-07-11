package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.InOutBoxTwoActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutBoxFoutActivity;
import high.rivamed.myapplication.activity.OutFormConfirmActivity;
import high.rivamed.myapplication.activity.OutMealBingConfirmActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;

/**
 * 项目名称:    WE_TG
 * 创建者:      LiangDanMing
 * 创建时间:    2017/3/28 9:22
 * 描述:        无按钮的dialog
 * 包名:        wetg.p5w.net.views
 * <p/>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class NoDialog extends Dialog {

   public NoDialog(Context context) {
	super(context);
   }

   public NoDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Context         mContext;
	private String          mMsgTwo;
	private String          mMsgText;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mRigtht;
	private TextView        mLeft;
	private ImageView        mCloss;
	private int        mLeftTextColor=-1;
	private int        mRightTextColor;
	private int        mType;
	private TextView mDialogMsg;
	private TextView mDialogRed;
	private NoDialog mDialog;
	private String mNojump;
	private String mBing;

	public Builder(Context context,int mType,String nojump, String bing) {
	   this.mContext = context;
	   this.mType = mType;
	   this.mNojump = nojump;
	   this.mBing = bing;

	}

	public Builder setTwoMsg(String title) {
	   this.mMsgTwo = title;
	   return this;
	}

	public Builder setMsg(String msg) {
	   this.mMsgText = msg;
	   return this;
	}

	public Builder setLeft(String left,int color, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftTextColor = color;
	   this.mLeftBtn = listener;
	   return this;
	}
	public Builder setLeft(String left, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftBtn = listener;
	   return this;
	}
	public Builder setRight(String left,int color, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightTextColor = color;
	   this.mRightBtn = listener;
	   return this;
	}
	public Builder setRight(String left, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightBtn = listener;
	   return this;
	}

	public NoDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new NoDialog(mContext, R.style.Dialog);
	   mDialog.setCancelable(true);
	   View layout = inflater.inflate(R.layout.dialog_no_layout, null);
	   mDialog.addContentView(layout,
					  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									    ViewGroup.LayoutParams.WRAP_CONTENT));

	   mDialogMsg = (TextView) layout.findViewById(R.id.dialog_msg);
	   mCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
	   if (mType==1){//异常类的弹框  黄色
		Drawable img = layout.getResources().getDrawable(R.mipmap.hccz_ic_tk_gth);
		img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		mDialogMsg.setCompoundDrawables(img, null, null, null); //设置左图
	   }else {//正常的弹框   绿色
		Drawable img = layout.getResources().getDrawable(R.mipmap.hccz_ic_tc_dg);
		img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		mDialogMsg.setCompoundDrawables(img, null, null, null); //设置左图

		new Handler().postDelayed(new Runnable() {
		   @Override
		   public void run() {
			mDialog.dismiss();
			ToastUtils.showShort("mNojump"+mNojump+mBing);
			Log.i("FF",mNojump+"   "+mBing);
			if(mNojump.equals("out")){
			   //TODO:换成关门后触发跳转柜子的扫描界面。拿出
			   if (mBing==null){  //没有绑定病人
				mContext.startActivity(new Intent(mContext, OutBoxFoutActivity.class));
			   }else {
				mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));

			   }
			}else if (mNojump.equals("in")){
			   //TODO:换成关门后触发跳转柜子的扫描界面。拿入
			   EventBusUtils.postSticky(new Event.EventAct("all"));
			   Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
			   mContext.startActivity(intent2);
			}else if (mNojump.equals("form")){
			   Log.i("FF","  DDDD   "+mNojump+"   "+mBing);
			   if (mBing .equals("BING_MEAL")){//绑定患者的套餐
				mContext.startActivity(new Intent(mContext, OutMealBingConfirmActivity.class));

			   }else {
				mContext.startActivity(new Intent(mContext, OutFormConfirmActivity.class));
			   }
			}
		   }
		}, 1000);


	   }

	   mDialogMsg.setText(mMsgText);


	   mCloss.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
		}
	   });
	   mDialog.setOnDismissListener(new OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {

		}
	   });
	   return mDialog;
	}


   }

}
