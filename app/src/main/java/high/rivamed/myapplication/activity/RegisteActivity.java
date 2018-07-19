package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.fragment.RegisteFrag;
import high.rivamed.myapplication.fragment.RegisteRecoverFrag;
import high.rivamed.myapplication.fragment.RegisteSelfCheckFrag;
import high.rivamed.myapplication.fragment.RegisteTestFrag;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 10:59
 * 描述:        工程模式
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteActivity extends SimpleActivity {

   @BindView(R.id.base_tab_tv_title)
   TextView         mBaseTabTvTitle;
   @BindView(R.id.base_tab_tv_name)
   TextView         mBaseTabTvName;
   @BindView(R.id.base_tab_icon_right)
   CircleImageView  mBaseTabIconRight;
   @BindView(R.id.registe_tl)
   SlidingTabLayout mRegisteTl;
   @BindView(R.id.registe_viewpager)
   ViewPager        mRegisteViewpager;
   private String[] mKeys = {"设备注册/激活", "设备自检", "功能验证","数据恢复"};
   private RegistePagerAdapter mPagerAdapter;

   @Override
   public int getLayoutId() {
	return R.layout.activity_registe_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("工程模式");
	mBaseTabTvName.setText("退出  ");
	mBaseTabTvName.setTextSize(18);
	mBaseTabTvName.setTextColor(getResources().getColor(R.color.color_drak_green));
	ViewGroup.LayoutParams Params = (ViewGroup.LayoutParams) mBaseTabIconRight.getLayoutParams();
	Params.height = 50;
	Params.width = 50;
	mBaseTabIconRight.setLayoutParams(Params);
	mBaseTabIconRight.setImageResource(R.mipmap.gcms_ic_tc);

	mPagerAdapter = new RegistePagerAdapter(getSupportFragmentManager());
	mRegisteViewpager.setAdapter(mPagerAdapter);
	mRegisteViewpager.setCurrentItem(0);
	mRegisteTl.setViewPager(mRegisteViewpager, mKeys);
   }

   @Override
   public void onBindViewBefore() {

   }

   @Override
   public Object newP() {
	return null;
   }


   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_tv_name:
	   case R.id.base_tab_icon_right:
	      finish();
		break;
	}
   }

   private class RegistePagerAdapter extends FragmentStatePagerAdapter {

	public RegistePagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   if (position == 0) {
		return RegisteFrag.newInstance();
	   } else if (position == 1) {
		return RegisteSelfCheckFrag.newInstance();
	   } else if (position == 2){
		return RegisteTestFrag.newInstance();
	   }else {
		return RegisteRecoverFrag.newInstance();
	   }
	}

	@Override
	public CharSequence getPageTitle(int position) {
	   return mKeys[position];
	}

	@Override
	public int getCount() {
	   return mKeys == null ? 0 : mKeys.length;
	}
   }
}
