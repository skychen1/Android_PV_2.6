package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.StockLeftAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.SocketLeftDownBean;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_MIDDLE;

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

public class StockMiddleInforFrag extends SimpleFragment {
   @BindView(R.id.cttimecheck_rg)
   SlidingTabLayout mCttimeCheck_Rg;
   @BindView(R.id.cttimecheck_viewpager)
   public ViewPager mCttimecheckViewpager;
   @BindView(R.id.stock_left_rv)
   RecyclerView mStockLeftRv;
   @BindView(R.id.stock_left_alltop)
   LinearLayout mStockLeftAlltop;
   private List<Integer> mList;
   private       int TOTAL_SIZE = 26;
   private final int PAGE_SIZE  = 6;
   private       int mCount     = 0;
   public  StockMiddlePagerAdapter mPagerAdapter;
   private List<String>          mTitles;
   private String[]              mKeys;
   public  int                   mStockNumber=5;//列表的列数
   public  int                   mStockType;//列表的类型
   private StockLeftAdapter      mLeftAdapter;
   private List                  mDates;
   public  SocketLeftTopBean     mLeftTopBean;
   private SocketLeftDownBean    mMiddleDateBean;
   private LoadingDialog.Builder mBuilder;
   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;

   @Override
   public int getLayoutId() {
	return R.layout.cttimecheck_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	getMiddleDate();

   }


   public void getMiddleDate() {
	NetRequest.getInstance().loadBoxSize(mContext, mBuilder,new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
//		mBuilder.mDialog.dismiss();
		BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
		mTbaseDevices = boxSizeBean.getTbaseDevices();
		if (mTbaseDevices != null) {
		   onSucceedDate();
		}
	   }
	   @Override
	   public void onError(String result) {
//		mBuilder.mDialog.dismiss();
	   }
	});
   }

   private void onSucceedDate() {
//	mBuilder.mDialog.dismiss();

	mPagerAdapter = new StockMiddlePagerAdapter(getChildFragmentManager());
	mCttimecheckViewpager.setAdapter(mPagerAdapter);
	mCttimecheckViewpager.setCurrentItem(0);
	mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager);
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
	   String deviceCode = null;
	   if (mTbaseDevices.size()>1){
		if (position == 0) {
		   deviceCode = null;
		} else {
		   deviceCode = mTbaseDevices.get(position - 1).getDeviceCode();
		}
	   }else {
		deviceCode = mTbaseDevices.get(position).getDeviceCode();
	   }

	   mStockLeftAlltop.setVisibility(View.GONE);
//	   mBuilder.mDialog.dismiss();
	   return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_MIDDLE,deviceCode);

	}

	@Override
	public CharSequence getPageTitle(int position) {
	   String deviceName = null;
	   if (mTbaseDevices.size()>1) {
		if (position == 0) {
		   deviceName = "全部";
		} else {
		   deviceName = mTbaseDevices.get(position - 1).getDeviceName();
		}
	   }else {
		deviceName = mTbaseDevices.get(position).getDeviceName();

	   }
	   return deviceName;
	}

	@Override
	public int getCount() {
	   if (mTbaseDevices.size()>1) {
		return mTbaseDevices == null ? 0 : mTbaseDevices.size()+1 ;
	   }else {
		return mTbaseDevices == null ? 0 : mTbaseDevices.size() ;
	   }
	}
   }
}
