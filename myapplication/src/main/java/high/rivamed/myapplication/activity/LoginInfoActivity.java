package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/18 16:53
 * 描述:        登录信息
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class LoginInfoActivity extends BaseSimpleActivity {

   @BindView(R.id.setting_password)
   TextView  mSettingPassword;
   @BindView(R.id.setting_password_edit)
   TextView  mSettingPasswordEdit;
   @BindView(R.id.setting_revise_password)
   ImageView mSettingRevisePassword;
   @BindView(R.id.setting_fingerprint)
   TextView  mSettingFingerprint;
   @BindView(R.id.setting_fingerprint_edit)
   TextView  mSettingFingerprintEdit;
   @BindView(R.id.setting_fingerprint_bind)
   TextView  mSettingFingerprintBind;

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("登录信息");
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.setting_logininfo_layout;
   }


   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.setting_revise_password, R.id.setting_fingerprint_bind})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(view);
		mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
		   @Override
		   public void onItemClick(int position) {
			switch (position) {
			   case 0:
				mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
				break;
			   case 1:
				mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
				break;
			   case 2:
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.setting_revise_password:
		DialogUtils.showOnePassWordDialog(mContext);
		break;
	   case R.id.setting_fingerprint_bind:
		DialogUtils.showOneFingerDialog(mContext);
		break;
	}
   }
}
