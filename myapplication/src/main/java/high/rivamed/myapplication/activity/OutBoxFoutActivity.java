package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 17:42
 * 描述:        从柜子拿出的界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutBoxFoutActivity extends BaseTimelyActivity {
   int mType;

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_OUT;
	return my_id;
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_start_btn, R.id.btn_four_ly, R.id.btn_four_yc,
	   R.id.btn_four_tb, R.id.btn_four_th})
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
	   case R.id.timely_start_btn:
		break;
	   case R.id.btn_four_ly:
		DialogUtils.showNoDialog(mContext,"耗材领用成功！",2,"nojump",null);
		break;
	   case R.id.btn_four_yc:
		mType = 1;//1.6移出
		DialogUtils.showStoreDialog(mContext,3, mType);
		break;
	   case R.id.btn_four_tb:
		mType =  3;//1.8调拨
		DialogUtils.showStoreDialog(mContext,2, mType);
		break;
	   case R.id.btn_four_th:
		mType = 2;//1.7退货
		DialogUtils.showStoreDialog(mContext,2, mType);
		break;
	}
   }

}
