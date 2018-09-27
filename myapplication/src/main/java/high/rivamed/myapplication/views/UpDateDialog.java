package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import high.rivamed.myapplication.R;

/**
 * 项目名称:    WE_TG
 * 创建者:      LiangDanMing
 * 创建时间:    2017/3/28 9:22
 * 描述:        版本更新的dialog
 * 包名:        high.rivamed.myapplication.views
 * <p/>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UpDateDialog extends Dialog {



   public UpDateDialog(Context context) {
	super(context);
   }

   public UpDateDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Context         mContext;
	private String          mTitle;
	private String          mMsgText;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mRigtht;
	private TextView        mLeft;
	private int        mLeftTextColor=-1;
	private int        mRightTextColor;

	public Builder(Context context) {
	   this.mContext = context;

	}

	public Builder setTitle(String title) {
	   this.mTitle = title;
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

	public UpDateDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final UpDateDialog dialog = new UpDateDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_update_layout, null);
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									    ViewGroup.LayoutParams.WRAP_CONTENT));

	   ((TextView) layout.findViewById(R.id.dialog_title)).setText(mTitle);
	   ((TextView) layout.findViewById(R.id.dialog_msg)).setText(mMsgText);
	   mLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mRigtht = (TextView) layout.findViewById(R.id.dialog_right);
	   if (mLeftTextColor!=-1){
		mLeft.setTextColor(mLeftTextColor);
		mRigtht.setTextColor(mRightTextColor);
	   }
	   mLeft.setText(mLeftText);
	   mRigtht.setText(mRightText);
	   mLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	   });
	   mRigtht.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	   });
	   return dialog;
	}
   }

}
