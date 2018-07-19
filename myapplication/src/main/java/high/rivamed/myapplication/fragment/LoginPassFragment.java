package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.HomeActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.views.CustomNumKeyView;
import high.rivamed.myapplication.views.VerificationCodeView;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 18:49
 * 描述:        密码登录界面Fragment
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class LoginPassFragment extends SimpleFragment
	implements CustomNumKeyView.CallBack, VerificationCodeView.InputCompleteListener {

   public String TAG = "LoginPassFragment";
   @BindView(R.id.login_et)
   VerificationCodeView mLoginEt;
   @BindView(R.id.login_keynum)
   CustomNumKeyView     mLoginKeynum;
   @BindView(R.id.login_f_ll)
   LinearLayout         mLoginFLl;


   @Override
   public int getLayoutId() {
	return R.layout.login_password_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();
	initListener();
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   private void initData() {
	mLoginEt.setInputCompleteListener(this);
	mLoginKeynum.setOnCallBack(this);
   }

   private void initListener() {

   }

   @Override
   public void clickNum(String num) {
	if (num.equals("删除")) {
	   mLoginEt.onKeyDelete();
	} else if (num.equals("清空")) {
	   mLoginEt.clearInputContent();
	} else {
	   mLoginEt.setText(num);
	}
   }

   @Override
   public void deleteNum() {

   }

   /**
    * 获取输入的密码
    */
   @Override
   public void inputComplete() {
	String inputContent = mLoginEt.getInputContent();
	LogUtils.i(TAG, "inputContent   " + inputContent);
	if (inputContent.length() == 5) {
		loadLogin();
	}

   }

   /**
    * 获取删除后的密码
    */
   @Override
   public void deleteContent() {
	String inputContent = mLoginEt.getInputContent();
	LogUtils.i(TAG, "deleteContent   " + inputContent);
   }

   /**
    * 获取登录
    */
   private void loadLogin() {
	Intent intent = new Intent(mContext, HomeActivity.class);
	mContext.startActivity(intent);
	mContext.finish();
   }
}
