package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:23
 * 描述:        实时盘点主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentTimelyCheckFrag extends BaseSimpleFragment {

   @BindView(R.id.cttimecheck_rg)
   MagicIndicator mCttimeCheck_Rg;
   @BindView(R.id.cttimecheck_viewpager)
   ViewPager      mCttimecheckViewpager;
   private CttimeCheckPagerAdapter            mPagerAdapter;
   public  List<BoxSizeBean.DevicesBean> mTbaseDevices;

   public static ContentTimelyCheckFrag newInstance() {
	Bundle args = new Bundle();
	ContentTimelyCheckFrag fragment = new ContentTimelyCheckFrag();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.cttimecheck_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	initData();
   }

   @Override
   public void onResume() {
	super.onResume();

   }
   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("实时盘点");
	loadTopBoxSize();
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME)+" - "+SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
   }

   private void loadTopBoxSize() {
	String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	mTbaseDevices = mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType());
	if (mTbaseDevices != null) {
	   ArrayList<Fragment> fragments = new ArrayList<>();
	   for (BoxSizeBean.DevicesBean devicesBean : mTbaseDevices) {
		//			fragments.add(new TimelyAllFrag(devicesBean.getDeviceId(),mTbaseDevices));
		fragments.add(new TimelyAllFrag(devicesBean.getDeviceId(), mTbaseDevices));
	   }
	   mPagerAdapter = new CttimeCheckPagerAdapter(getChildFragmentManager(), fragments);
	   mCttimecheckViewpager.setAdapter(mPagerAdapter);
	   mCttimecheckViewpager.setCurrentItem(0);
	   mCttimecheckViewpager.setOffscreenPageLimit(fragments.size());
	   //		   mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager);\
	   UIUtils.initPvTabLayout(mTbaseDevices, mCttimecheckViewpager, mCttimeCheck_Rg);

	}

   }

   private class CttimeCheckPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> mFragments;
	public CttimeCheckPagerAdapter(FragmentManager fm, List<Fragment> Fragments) {
	   super(fm);
	   this.mFragments = Fragments;
	}
	@Override
	public Fragment getItem(int position) {
	   return mFragments.get(position);
	}


	@Override
	public CharSequence getPageTitle(int position) {
	   return mTbaseDevices.get(position).getDeviceName();

	}

	@Override
	public int getCount() {
	   return mFragments.size();
	}
   }
}



