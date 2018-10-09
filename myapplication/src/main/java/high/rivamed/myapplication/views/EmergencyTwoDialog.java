package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.EmergencyBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/10/14 16:22
 * 描述:        2个按钮的dialog 紧急登录
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class EmergencyTwoDialog extends Dialog {

   public EmergencyTwoDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder implements CustomNumKeyView2.CallBack,
	   VerificationCodeView.InputCompleteListener {

	private Context         mContext;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mRigtht;
	private TextView        mLeft;
	private String        mLeftText;
	private String        mRightText;

	private VerificationCodeView mEmergency1;
	private VerificationCodeView mEmergency2;
	private CustomNumKeyView2 mLoginKeynum;
	private String mInputContent;

	public Builder(Context context) {
	   this.mContext = context;
	}


	public Builder setLeft(String left, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftBtn = listener;
	   return this;
	}

	public Builder setRight(String left, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightBtn = listener;
	   return this;
	}

	public EmergencyTwoDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final EmergencyTwoDialog dialog = new EmergencyTwoDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_emergency_layout, null);
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.x850),
									    ViewGroup.LayoutParams.WRAP_CONTENT));

	   mEmergency1 = (VerificationCodeView) layout.findViewById(R.id.emergency_et1);
	   mLoginKeynum = (CustomNumKeyView2) layout.findViewById(R.id.login_keynum);
	   mEmergency1.setInputCompleteListener(this);
	   mLoginKeynum.setOnCallBack(this);
	   mLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mRigtht = (TextView) layout.findViewById(R.id.dialog_right);
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
//		   mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		   if (mInputContent.length()==6){
			NetRequest.getInstance().emergencySetting(mInputContent,this,new BaseResult(){
			   @Override
			   public void onSucceed(String result) {
				Gson gson = new Gson();
				EmergencyBean emergencyBean = gson.fromJson(result, EmergencyBean.class);
				ToastUtils.showShort(emergencyBean.getMsg());
				LogUtils.i("ddd","result   "+result);
			   }
			});
		   }else {
			ToastUtils.showShort("密码必须为 6 位数字");
		   }
		}
	   });
	   return dialog;
	}

	@Override
	public void clickNum(String num) {
	   if (num.equals("删除")) {
		mEmergency1.onKeyDelete();
	   } else if (num.equals("清空")) {
		mEmergency1.clearInputContent();
	   } else {
		mEmergency1.setText(num);
	   }
	}

	@Override
	public void deleteNum() {

	}

	@Override
	public void inputComplete() {
	   mInputContent = mEmergency1.getInputContent();
//	   if (mInputContent.length() == 6) {
////		loadLogin();
//	   }
	}

	@Override
	public void deleteContent() {
	   mInputContent = mEmergency1.getInputContent();
	}
   }

}
