package high.rivamed.myapplication.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginInfoActivity;
import high.rivamed.myapplication.activity.MyInfoActivity;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        有标题栏的抽取的fragment
 * 包名:        high.rivamed.myapplication.base
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public abstract class BaseSimpleFragment extends SimpleFragment {
   public String TAG = "BaseSimpleFragment";
   @BindView(R.id.base_tab_back)
   public TextView        mBaseTabBack;
   @BindView(R.id.base_tab_btn_left)
   public TextView        mBaseTabBtnLeft;
   @BindView(R.id.base_tab_tv_title)
   public TextView        mBaseTabTvTitle;
   @BindView(R.id.stock_rdbtn_left)
   public RadioButton     mStockRdbtnLeft;
   @BindView(R.id.stock_rdbtn_middle)
   public RadioButton     mStockRdbtnMiddle;
   @BindView(R.id.stock_rdbtn_right)
   public RadioButton     mStockRdbtnRight;
   @BindView(R.id.rg_group)
   public RadioGroup      mRgGroup;
   @BindView(R.id.base_tab_tv_name)
   public TextView        mBaseTabTvName;
   @BindView(R.id.base_tab_icon_right)
   public CircleImageView mBaseTabIconRight;
   @BindView(R.id.base_tab_btn_msg)
   public ImageView       mBaseTabBtnMsg;
   @BindView(R.id.base_tab_ll)
   public RelativeLayout  mBaseTabLl;
   @BindView(R.id.base_tab_rlayout)
   public RelativeLayout  mBaseTabRlayout;

   private ViewStub           mStub;
   public SettingPopupWindow mPopupWindow;


   @Override
   public int getLayoutId() {
	return R.layout.fragment_base_title;
   }

   @Override
   public void onBindViewBefore(View root) {
	mStub = (ViewStub) root.findViewById(R.id.viewstub_layout);
	mStub.setLayoutResource(getContentLayoutId());
	mStub.inflate();
   }

   protected abstract int getContentLayoutId();

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);
		LogUtils.i("sss", "base_tab_tv_name");
		popupClick();
		break;
	   case R.id.base_tab_btn_msg:
		LogUtils.i("sss", "base_tab_btn_msg");
		break;

	}
   }

   public void popupClick() {
	mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
	   @Override
	   public void onItemClick(int position) {
		switch (position){
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
   }

}
