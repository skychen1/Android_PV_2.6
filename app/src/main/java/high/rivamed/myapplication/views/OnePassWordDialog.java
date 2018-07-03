package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import high.rivamed.myapplication.R;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/2 11:27
 * 描述:        1个按钮的dialog
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OnePassWordDialog extends Dialog {

   public OnePassWordDialog(Context context) {
	super(context);
   }

   public OnePassWordDialog(Context context, int theme) {
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
	private ImageView       mCloss;
	private int mLeftTextColor = -1;
	private int      mRightTextColor;
	private int      mType;
	private TextView mDialogMsg;
	private TextView mDialogBtn;
	private EditText     mPasswordOne;
	private EditText     mPasswordTwo;
	public Builder(Context context) {
	   this.mContext = context;

	}

	public Builder setTwoMsg(String title) {
	   this.mMsgTwo = title;
	   return this;
	}

	public Builder setMsg(String msg) {
	   this.mMsgText = msg;
	   return this;
	}

	public Builder setLeft(String left, int color, OnClickListener listener) {
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

	public Builder setRight(String left, int color, OnClickListener listener) {
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

	public OnePassWordDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final OnePassWordDialog dialog = new OnePassWordDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_one_password_layout, null);
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									    ViewGroup.LayoutParams.WRAP_CONTENT));




	   mPasswordOne = (EditText) layout.findViewById(R.id.password_one);
	   mPasswordTwo = (EditText) layout.findViewById(R.id.password_two);
	   mDialogBtn = (TextView) layout.findViewById(R.id.dialog_right);
	   mCloss = (ImageView) layout.findViewById(R.id.dialog_closs);


	   mCloss.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   dialog.dismiss();
		}
	   });
	   mDialogBtn.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	   });
	   return dialog;
	}
   }

}
