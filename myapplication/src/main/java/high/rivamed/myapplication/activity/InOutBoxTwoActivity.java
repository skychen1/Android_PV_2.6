package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/27 19:36
 * 描述:        放入柜子的界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class InOutBoxTwoActivity extends BaseTimelyActivity {
   int mType;
   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_IN;
	return my_id;
   }
//   @Override
//   protected void onStart() {
//	super.onStart();
//	Intent intent = getIntent();
//	mActivityType=intent.getStringExtra("type");
//   }
   @Override
   public String getData() {
	super.mData = "我有过期的";//提示弹出过期的框
	return mData;
   }
//   @Override
//   public String getActivityType() {
//	Intent intent = getIntent();
//	super.mActivityType=intent.getStringExtra("type");
//	return mActivityType;
//   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_start_btn, R.id.timely_left, R.id.timely_right,
	   R.id.btn_four_ly, R.id.btn_four_yc, R.id.btn_four_tb, R.id.btn_four_th})
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
	}


	if (mActivityType.equals("all")){
	   switch (view.getId()) {
		case R.id.btn_four_ly:
		   DialogUtils.showNoDialog(mContext, "耗材领用成功！", 2, "nojump", null);
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
	}else {
	   switch (view.getId()) {
		case R.id.timely_left:
		   ToastUtils.showShort("timely_left");
		   break;
		case R.id.timely_right:
		   ToastUtils.showShort("timely_right");
		   break;
	   }
	}

	}

}
