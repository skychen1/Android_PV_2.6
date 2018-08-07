package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_MEAL_BING;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 15:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutMealBingConfirmActivity extends BaseTimelyActivity {

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_MEAL_BING;
	return my_id;
   }
   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_start_btn_right, R.id.ly_bing_btn_right,R.id.timely_left, R.id.timely_right})
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
			   case 2: TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
				   builder.setTwoMsg("您确认要退出登录吗?");
				   builder.setMsg("温馨提示");
				   builder.setLeft("取消", new DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialog, int i) {
						   dialog.dismiss();
					   }
				   });
				   builder.setRight("确认", new DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialog, int i) {
						   mContext.startActivity(new Intent(mContext, LoginActivity.class));
						   App.getInstance().removeALLActivity_();
						   dialog.dismiss();
					   }
				   });
				   builder.create().show();
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
	   case R.id.timely_start_btn_right:
		break;
	   case R.id.timely_left:
		ToastUtils.showShort("timely_left");
		break;
	   case R.id.timely_right:
		ToastUtils.showShort("timely_right");
		break;
	   case R.id.ly_bing_btn_right:
		ToastUtils.showShort("绑定");
//		DialogUtils.showRvDialog(this, mContext);
		break;
	}
   }
}