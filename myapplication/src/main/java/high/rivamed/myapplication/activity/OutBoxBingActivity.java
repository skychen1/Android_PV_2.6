package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 19:59
 * 描述:       拿出  绑定病人
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutBoxBingActivity extends BaseTimelyActivity {

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_BING;
	return my_id;
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right,
	   R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_left,
	   R.id.timely_right, R.id.timely_start_btn_right, R.id.ly_bing_btn_right})
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
	   case R.id.timely_start_btn_right:
		break;
	   case R.id.timely_left:
		ToastUtils.showShort("timely_left");
		int mType = 1;//1.8.3未绑定
		DialogUtils.showTwoDialog(mContext, mType, "您还有未绑定患者的耗材，确认领用吗？", "耗材未绑定患者");
		break;
	   case R.id.timely_right:
		ToastUtils.showShort("timely_right");
		break;
	   case R.id.ly_bing_btn_right:
		ToastUtils.showShort("绑定");
		loadBingDate();
		DialogUtils.showRvDialog(this, mContext);
		break;
	}
   }

   /**
    * 获取
    */
   private void loadBingDate() {

   }
}
