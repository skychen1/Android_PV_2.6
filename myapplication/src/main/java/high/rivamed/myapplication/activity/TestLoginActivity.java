package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.utils.StringUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/19 13:30
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TestLoginActivity extends SimpleActivity {

   @BindView(R.id.login_name)
   EditText       mLoginName;
   @BindView(R.id.login_password)
   EditText       mLoginPassword;
   @BindView(R.id.dialog_left)
   TextView       mDialogLeft;
   @BindView(R.id.dialog_right)
   TextView       mDialogRight;
   @BindView(R.id.login_all)
   RelativeLayout mLoginAll;
   private String mUserPhone;
   private String mPassword;
   @Override
   public int getLayoutId() {
	return R.layout.test_login_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

   }

   @Override
   public void onBindViewBefore() {

   }

   @Override
   public Object newP() {
	return null;
   }



   @OnClick({R.id.dialog_left, R.id.dialog_right, R.id.login_all})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.dialog_left:
		finish();
		break;
	   case R.id.dialog_right:
//		if (UIUtils.isFastDoubleClick()) {
//		   return;
//		} else {
//		   if (isvalidate() && WifiUtils.isWifi(mContext) != 0) {
			loadLogin();
//		   } else {
//			Toast.makeText(mContext, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
//		   }
//		}

		break;
	   case R.id.login_all:
		mLoginName.clearFocus();
		mLoginPassword.clearFocus();
		break;
	}
   }
   private boolean isvalidate() {
	// 获取控件输入的值
	mUserPhone = mLoginName.getText().toString().trim();
	mPassword = mLoginPassword.getText().toString().trim();
	if (StringUtils.isEmpty(mUserPhone)) {
	   Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
	   return false;
	}

	if (StringUtils.isEmpty(mPassword)) {
	   Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
	   return false;
	}

	return true;
   }
   /**
    * 获取登录
    */
   private void loadLogin() {
	startActivity(new Intent(this,RegisteActivity.class));
	finish();
   }
}
