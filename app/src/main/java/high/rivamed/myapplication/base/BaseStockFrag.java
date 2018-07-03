package high.rivamed.myapplication.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.StockLeftAdapter;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.fragment.PublicTimelyFrag;
import high.rivamed.myapplication.fragment.StockMiddleInforFrag;

import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_LEFT;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_MIDDLE;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_RIGHT;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 19:04
 * 描述:        库存详情
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class BaseStockFrag extends SimpleFragment {

   @BindView(R.id.cttimecheck_rg)
   SlidingTabLayout mCttimeCheck_Rg;
   @BindView(R.id.cttimecheck_viewpager)
  public ViewPager        mCttimecheckViewpager;
   @BindView(R.id.stock_left_rv)
   RecyclerView     mStockLeftRv;
   @BindView(R.id.stock_left_alltop)
   LinearLayout     mStockLeftAlltop;
   private List<Integer> mList;
   private       int TOTAL_SIZE = 26;
   private final int PAGE_SIZE  = 6;
   private       int mCount     = 0;
   public  StockMiddlePagerAdapter mPagerAdapter;
   private List<String>            mTitles;
   private String[]                mKeys;
   public  int                     mStockNumber;//列表的列数
   public  int                     mStockType;//列表的类型
   private StockLeftAdapter        mLeftAdapter;

   @Override
   public int getLayoutId() {
	return R.layout.cttimecheck_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	getStockType();
	getStockNumber();
	initData();
	mPagerAdapter = new StockMiddlePagerAdapter(getChildFragmentManager());
	mCttimecheckViewpager.setAdapter(mPagerAdapter);
	mCttimecheckViewpager.setCurrentItem(0);
	mKeys = mTitles.toArray(new String[mTitles.size()]);
	mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager, mKeys);
   }

   public int getStockNumber() {
	return mStockNumber;
   }

   public int getStockType() {
	return mStockType;
   }

   private void initData() {

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
	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
	mLeftAdapter = new StockLeftAdapter(R.layout.item_stock_lefttop_layout, genData3());
	mStockLeftRv.setLayoutManager(layoutManager);
	mStockLeftRv.setAdapter(mLeftAdapter);

	mLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		RadioGroup group = (RadioGroup) mContext.findViewById(R.id.rg_group);
		group.check(R.id.stock_rdbtn_middle);
		ViewPager viewPager = (ViewPager) mContext.findViewById(R.id.home_stock_viewpager);
		viewPager.setCurrentItem(1);

		FragmentManager manager = getParentFragment().getFragmentManager();
		StockMiddleInforFrag inforFrag = new StockMiddleInforFrag();
		manager.beginTransaction().replace(R.id.home_stock_viewpager, inforFrag)
			.commit();


		//		Intent intent = new Intent(mContext, HomeActivity.class);
		//		Bundle bundle=new Bundle();
		//		bundle.putString("type","middle");
		//		bundle.putInt("number",5);
		//		intent.putExtra("bundle",bundle);
		//		mContext.startActivity(intent);
	   }
	});
	//	mLeftAdapter.seton(new BaseQuickAdapter.OnItemClickListener() {
	//	   @Override
	//	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
	//
	////		mCttimecheckViewpager.setCurrentItem(1);
	//	   }
	//	});
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   private class StockMiddlePagerAdapter extends FragmentStatePagerAdapter {

	public StockMiddlePagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   getStockType();
	   getStockNumber();
	   if (mStockType == 0) {
		mStockLeftAlltop.setVisibility(View.VISIBLE);
		return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_LEFT);
	   } else if (mStockType == 1) {
		mStockLeftAlltop.setVisibility(View.GONE);
		return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_MIDDLE);
	   } else {
		mStockLeftAlltop.setVisibility(View.GONE);
		return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_RIGHT);
	   }
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

   private List<Movie> genData3() {

	ArrayList<Movie> list = new ArrayList<>();
	int one;
	int two;
	int three;
	for (int i = 1; i < 20; i++) {
	   one = i;
	   if (i == 1) {
		two = 10;
		three = i;
	   } else if (i == 2) {
		two = 9;
		three = 0;
	   } else if (i == 3) {
		two = 10;
		three = 20;
	   } else if (i == 4) {
		two = 0;
		three = 6;
	   } else {

		two = 0;
		three = 0;

	   }

	   Movie movie = new Movie(one, two, three);
	   list.add(movie);
	}
	return list;
   }
}
