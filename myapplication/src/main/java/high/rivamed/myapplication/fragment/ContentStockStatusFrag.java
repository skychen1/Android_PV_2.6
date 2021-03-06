package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.os.Parcelable;
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
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.CONFIG_026;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
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
   private static final String TYPE  = "TYPE";
   private boolean mABoolean;

   /**
    * @param type :true是登录状态显示，false非登录状态显示
    * @return
    */
   public static ContentStockStatusFrag newInstance(boolean type) {
	Bundle args = new Bundle();
	ContentStockStatusFrag fragment = new ContentStockStatusFrag();
	args.putBoolean(TYPE, type);
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.home_ctstock_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	initData();
	mFragments.add(new StockLeftListenerFrag());
	mFragments.add(new StockMiddleInforFrag());
	if(UIUtils.getConfigType(mContext, CONFIG_026)){
	   mFragments.add(new StockRightUnconfFrag());
	}else {
	   mStockRdbtnRight.setVisibility(View.GONE);
	}
	mHomeStockViewpager.setAdapter(new StockPagerAdapter(getChildFragmentManager()));
	mHomeStockViewpager.addOnPageChangeListener(new PageChangeListener());
	mHomeStockViewpager.setOffscreenPageLimit(3);
	initListener();
   }


   private void initData() {

	Bundle arguments = getArguments();
	mABoolean = arguments.getBoolean(TYPE);
	if (mABoolean){
	   mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	   mRgGroup.setVisibility(View.VISIBLE);
	   mBaseTabLl.setVisibility(View.VISIBLE);
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME)+" - "+SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
	}else {
	   mBaseTabBack.setVisibility(View.VISIBLE);
	   mBaseTabTvName.setVisibility(View.GONE);
	   mBaseTabBtnMsg.setVisibility(View.GONE);
	   mBaseTabIconRight.setVisibility(View.GONE);
	   mBaseTabOutLogin.setVisibility(View.GONE);
	   mRgGroup.setVisibility(View.VISIBLE);
	   mBaseTabLl.setVisibility(View.VISIBLE);
	}
   }

   private void initListener() {
	mBaseTabBack.setOnClickListener(view -> {
	   mContext.finish();
	});
	mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(UIUtils.getConfigType(mContext, CONFIG_026)){
		   switch (checkedId) {
			case R.id.stock_rdbtn_left:
			   mHomeStockViewpager.setCurrentItem(0);
			   break;
			case R.id.stock_rdbtn_middle:
			   mHomeStockViewpager.setCurrentItem(1);
			   break;

			case R.id.stock_rdbtn_right:
			   mHomeStockViewpager.setCurrentItem(2);
			   break;
		   }
		}else {
		   switch (checkedId) {
			case R.id.stock_rdbtn_left:
			   mHomeStockViewpager.setCurrentItem(0);
			   break;
			case R.id.stock_rdbtn_middle:
			   mHomeStockViewpager.setCurrentItem(1);
			   break;
		   }
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

	@Override
	public Parcelable saveState() {
	   return null;
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
	   if(UIUtils.getConfigType(mContext, CONFIG_026)){
		switch (position) {
		   case 0:
			mRgGroup.check(R.id.stock_rdbtn_left);
			break;
		   case 1:
			mRgGroup.check(R.id.stock_rdbtn_middle);
			break;
		   case 2:
			mRgGroup.check(R.id.stock_rdbtn_right);
			break;
		}
	   }else {
		switch (position) {
		   case 0:
			mRgGroup.check(R.id.stock_rdbtn_left);
			break;
		   case 1:
			mRgGroup.check(R.id.stock_rdbtn_middle);
			break;
		}
	   }
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
   }
}
