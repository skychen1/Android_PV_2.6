package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.utils.SPUtils;

import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_OPERATION_ROOM_NONAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        库存状态主页面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentStockStatusFrag extends BaseSimpleFragment {

   @BindView(R.id.home_stock_viewpager)
  public ViewPager       mHomeStockViewpager;
   private ArrayList<Fragment> mFragments = new ArrayList<>();

   public static ContentStockStatusFrag newInstance() {
	Bundle args = new Bundle();
	ContentStockStatusFrag fragment = new ContentStockStatusFrag();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.home_ctstock_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();
	mFragments.add(new StockLeftListenerFrag());
	mFragments.add(new StockMiddleInforFrag());
//	mFragments.add(new StockRightUnconfFrag());
	mStockRdbtnRight.setVisibility(View.GONE);
	mHomeStockViewpager.setAdapter(new StockPagerAdapter(getChildFragmentManager()));
	mHomeStockViewpager.addOnPageChangeListener(new PageChangeListener());
	initListener();
   }

   private void initData() {

	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mRgGroup.setVisibility(View.VISIBLE);
	mBaseTabLl.setVisibility(View.VISIBLE);
	if (SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME)!=null){
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME)+" - "+SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
	}
	if (SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME)!=null){
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME)+" - "+SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME));
	}
   }

   private void initListener() {
	mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		   case R.id.stock_rdbtn_left:
			mHomeStockViewpager.setCurrentItem(0);
			break;
		   case R.id.stock_rdbtn_middle:
			mHomeStockViewpager.setCurrentItem(1);
			break;
//		   case R.id.stock_rdbtn_right:
//			mHomeStockViewpager.setCurrentItem(2);
//			break;
		}
	   }
	});
   }

   /**
    * 设置fragment
    */
   private class StockPagerAdapter extends FragmentPagerAdapter {

	public StockPagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   return mFragments.get(position);
	}

	@Override
	public int getCount() {
	   return mFragments.size();
	}
   }

   /**
    * fragment监听
    */
   private class PageChangeListener implements ViewPager.OnPageChangeListener {

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
	   switch (position) {
		case 0:
		   mRgGroup.check(R.id.stock_rdbtn_left);
		   break;
		case 1:
		   mRgGroup.check(R.id.stock_rdbtn_middle);
		   break;
//		case 2:
//		   mRgGroup.check(R.id.stock_rdbtn_right);
//		   break;

	   }
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
   }
}
