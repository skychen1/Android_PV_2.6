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
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;

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
   private final int PAGE_SIZE  = 20;
   private       int mCount     = 0;
   private CttimeCheckPagerAdapter            mPagerAdapter;
   private List<String>                       mTitles;
   private String[]                           mKeys;
   public  List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;
   private LoadingDialog.Builder              mBuilder;

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

   }

   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("实时盘点");
//	mBuilder = DialogUtils.showLoading(mContext);
	loadTopBoxSize();
	mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME));
   }

   private void loadTopBoxSize() {
	NetRequest.getInstance().loadBoxSize(mContext,null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
		mTbaseDevices = boxSizeBean.getTbaseDevices();
		if (mTbaseDevices != null) {
		   if (mTbaseDevices.size() > 1) {
//			mBuilder.mDialog.dismiss();
			BoxSizeBean.TbaseDevicesBean devicesBean1 = new BoxSizeBean.TbaseDevicesBean();
			devicesBean1.setDeviceName("全部");
			devicesBean1.setDeviceCode("");
			mTbaseDevices.add(0, devicesBean1);
		   }

		   //		   mBuilder.mDialog.dismiss();
		   ArrayList<Fragment> fragments = new ArrayList<>();
		   for (BoxSizeBean.TbaseDevicesBean devicesBean : mTbaseDevices) {
			fragments.add(new TimelyAllFrag(devicesBean.getDeviceCode(),mTbaseDevices));
		   }
		   mPagerAdapter = new CttimeCheckPagerAdapter(getChildFragmentManager(), fragments);
		   mCttimecheckViewpager.setAdapter(mPagerAdapter);
		   mCttimecheckViewpager.setCurrentItem(0);
		   mCttimecheckViewpager.setOffscreenPageLimit(fragments.size());
		   mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager);
		   mCttimecheckViewpager.addOnPageChangeListener(new PageChangeListener());
		}
	   }

	   @Override
	   public void onError(String result) {
//		mBuilder.mDialog.dismiss();
	   }
	});
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
//	@Override
//	public Fragment getItem(int position) {
//	   String deviceCode = null;
//	   if (mTbaseDevices.size()>1){
//		if (position == 0) {
//		   deviceCode = null;
//		} else {
//		   deviceCode = mTbaseDevices.get(position - 1).getDeviceCode();
//		}
//	   }else {
//		deviceCode = mTbaseDevices.get(position).getDeviceCode();
//	   }
//	   mBuilder.mDialog.dismiss();
//	   return TimelyAllFrag.newInstance(deviceCode);
//
//	}

	@Override
	public CharSequence getPageTitle(int position) {
	   return mTbaseDevices.get(position).getDeviceName();
//	   String deviceName = null;
//	   if (mTbaseDevices.size()>1) {
//		if (position == 0) {
//		   deviceName = "全部";
//		} else {
//		   deviceName = mTbaseDevices.get(position - 1).getDeviceName();
//		}
//	   }else {
//		deviceName = mTbaseDevices.get(position).getDeviceName();
//
//	   }
//	   return deviceName;
	}

	@Override
	public int getCount() {
	   return mFragments.size();
	   //	   if (mTbaseDevices.size()>1) {
	   //		return mTbaseDevices == null ? 0 : mTbaseDevices.size()+1 ;
	   //	   }else {
	   //		return mTbaseDevices == null ? 0 : mTbaseDevices.size() ;
	   //	   }
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
//	   String deviceCode = null;
//	   if (position == 0) {
//		deviceCode = "";
//	   } else {
//		deviceCode = mTbaseDevices.get(position - 1).getDeviceCode();
//	   }
//	   EventBusUtils.postSticky(new Event.EventTimelyCode(deviceCode));
	}
   }



   //
   //
   //   private class CttimeCheckPagerAdapter extends FragmentStatePagerAdapter {
   //
   //	private List<Fragment> mFragments;
   //
   //	public CttimeCheckPagerAdapter(FragmentManager fm, List<Fragment> Fragments) {
   //	   super(fm);
   //	   this.mFragments = Fragments;
   //	}
   //
   //	@Override
   //	public Fragment getItem(int position) {
   //
   //	   return mFragments.get(position);
   //
   //	}
   //
   //	@Override
   //	public CharSequence getPageTitle(int position) {
   //	   return mTemPTbaseDevices.get(position).getDeviceName();
   //	}
   //
   //	@Override
   //	public int getCount() {
   //	   return mFragments.size();
   //	}
   //   }
}



