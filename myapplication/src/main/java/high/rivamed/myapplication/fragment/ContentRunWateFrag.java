package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.TYPE_RUNWATE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材流水主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentRunWateFrag extends BaseSimpleFragment {

   @BindView(R.id.home_runwate_viewpager)
   ViewPager        mHomeRunWateViewpager;
   @BindView(R.id.home_runwate_rg)
   SlidingTabLayout mHomeRunwateRg;
   @BindView(R.id.search_et)
   EditText         mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView        mSearchIvDelete;
   @BindView(R.id.search_time_all)
   RadioButton      mSearchTimeAll;
   @BindView(R.id.search_time_day)
   RadioButton      mSearchTimeDay;
   @BindView(R.id.search_time_week)
   RadioButton      mSearchTimeWeek;
   @BindView(R.id.search_time_moon)
   RadioButton      mSearchTimeMoon;
   @BindView(R.id.search_time_rg)
   RadioGroup       mSearchTimeRg;
   @BindView(R.id.search_time_start)
   TextView         mSearchTimeStart;
   @BindView(R.id.search_time_end)
   TextView         mSearchTimeEnd;
   @BindView(R.id.search_type_all)
   RadioButton      mSearchTypeAll;
   @BindView(R.id.search_type_hous)
   RadioButton      mSearchTypeHous;
   @BindView(R.id.search_type_use)
   RadioButton      mSearchTypeUse;
   @BindView(R.id.search_type_info)
   RadioButton      mSearchTypeInfo;
   @BindView(R.id.search_type_out)
   RadioButton      mSearchTypeOut;
   @BindView(R.id.search_type_return)
   RadioButton      mSearchTypeReturn;
   @BindView(R.id.search_type_return_goods)
   RadioButton      mSearchTypeReturnGoods;
   @BindView(R.id.search_type_rg)
   RadioGroup       mSearchTypeRg;
   Unbinder unbinder;

   private RunWatePagerAdapter mPagerAdapter;
   private List<String>        mTitles;
   private String[]            mKeys;
   private List<Integer>       mList;
   private       int TOTAL_SIZE = 26;
   private final int PAGE_SIZE  = 20;
   private       int mCount     = 0;

   public static ContentRunWateFrag newInstance() {
	Bundle args = new Bundle();
	ContentRunWateFrag fragment = new ContentRunWateFrag();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.ctrunwate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();
	mPagerAdapter = new RunWatePagerAdapter(getChildFragmentManager());
	mHomeRunWateViewpager.setAdapter(mPagerAdapter);
	mHomeRunWateViewpager.setCurrentItem(0);
	mKeys = mTitles.toArray(new String[mTitles.size()]);
	mHomeRunwateRg.setViewPager(mHomeRunWateViewpager, mKeys);
	mHomeRunWateViewpager.addOnPageChangeListener(new PageChangeListener());
   }

   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材流水");
	mList = new ArrayList<>();
	mTitles = new ArrayList<>();
	for (int i = 0; i < PAGE_SIZE; i++) {
	   if (i == 0) {
		mTitles.add("全部");
	   } else {
		mTitles.add(i + "号柜");
	   }
	   mCount++;
	   mList.add(mCount);
	}
   }


   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.search_iv_delete, R.id.search_time_start, R.id.search_time_end})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);
		popupClick();
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.search_iv_delete:
		break;
	   case R.id.search_time_start:
		DialogUtils.showTimeDialog(mContext,mSearchTimeStart);
		break;
	   case R.id.search_time_end:
		DialogUtils.showTimeDialog(mContext,mSearchTimeEnd);
		break;
	}
   }


   private class RunWatePagerAdapter extends FragmentStatePagerAdapter {

	public RunWatePagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   return PublicTimelyFrag.newInstance(8,TYPE_RUNWATE);
	}

	@Override
	public CharSequence getPageTitle(int position) {
	   return mKeys[position];
	}

	@Override
	public int getCount() {
	   return mList == null ? 0 : mList.size();
	}
   }

   private class PageChangeListener implements ViewPager.OnPageChangeListener {

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
	   mSearchTimeRg.check(R.id.search_time_all);
	   mSearchTypeRg.check(R.id.search_type_all);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
   }
}
