package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;

import static high.rivamed.myapplication.cont.Constants.TYPE_TIMELY;

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
   SlidingTabLayout mCttimeCheck_Rg;
   @BindView(R.id.cttimecheck_viewpager)
   ViewPager        mCttimecheckViewpager;
   private List<Integer> mList;
   private       int TOTAL_SIZE = 26;
   private final int PAGE_SIZE  =20;
   private       int mCount     = 0;
   private CttimeCheckPagerAdapter mPagerAdapter;
   private List<String> mTitles;
   private String[] mKeys;

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
	initData();
	mPagerAdapter = new CttimeCheckPagerAdapter(getChildFragmentManager());
	mCttimecheckViewpager.setAdapter(mPagerAdapter);
	mCttimecheckViewpager.setCurrentItem(0);
	mKeys = mTitles.toArray(new String[mTitles.size()]);
	mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager, mKeys);
   }

   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("实时盘点");

	mList = new ArrayList<>();
	mTitles = new ArrayList<>();
	for (int i = 0; i < PAGE_SIZE; i++) {
	   if (i==0){
		mTitles.add("全部");
	   }else {
		mTitles.add(i+"号柜");
	   }
	   mCount++;
	   mList.add(mCount);
	}

   }


   private class CttimeCheckPagerAdapter extends FragmentStatePagerAdapter {

	public CttimeCheckPagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return PublicTimelyFrag.newInstance(6,TYPE_TIMELY);
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
	public void onPageScrolled(
		int position, float offsetPerc, int offsetPixel) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageSelected(int position) {
	   int size = mList.size();
	   if (position == size - 1 && TOTAL_SIZE > size) {
		//实际项目中是网络请求下一页的列表数据
		getMoreDetailList();
	   }

	}
   }

   private void getMoreDetailList() {
	for (int i = mCount; i < TOTAL_SIZE; i++) {
	   mCount++;
	   mList.add(mCount);
	}
	mPagerAdapter.notifyDataSetChanged();
   }

}
