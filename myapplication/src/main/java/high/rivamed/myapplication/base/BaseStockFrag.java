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

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.StockLeftAdapter;
import high.rivamed.myapplication.bean.SocketLeftDownBean;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.fragment.PublicTimelyFrag;
import high.rivamed.myapplication.fragment.StockMiddleInforFrag;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_LEFT;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_MIDDLE;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_RIGHT;
import static high.rivamed.myapplication.cont.Constants.TYPE_STOCK_LEFT;
import static high.rivamed.myapplication.cont.Constants.TYPE_STOCK_MIDDLE;

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

public abstract class BaseStockFrag extends SimpleFragment {
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
   private List<String>                                  mTitles;
   private String[]                                      mKeys;
   public  int                                           mStockNumber;//列表的列数
   public  int                                           mStockType;//列表的类型
   private StockLeftAdapter                              mLeftAdapter;
   private List                                          mDates;
   public  SocketLeftTopBean                             mLeftTopBean;
   private SocketLeftDownBean mLeftDownBean;
   private LoadingDialog.Builder mBuilder;

   @Override
   public int getLayoutId() {
	return R.layout.cttimecheck_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	mBuilder = DialogUtils.showLoading(mContext);
	getStockType();
	getStockNumber();
	if (mStockType==TYPE_STOCK_LEFT){
	   getLeftDate();
	}if (mStockType==TYPE_STOCK_MIDDLE){

	}else {
	   getLeftDate();
	}
	//	initData();

   }



   private void getLeftDownDate() {
	NetRequest.getInstance().getStockDown("23233",null,null, -1,mContext,new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		mLeftDownBean = mGson.fromJson(result, SocketLeftDownBean.class);
		if (mLeftDownBean!=null){
		   onSucceedDate(mLeftDownBean);
		}
	   }
	   @Override
	   public void onError(String result) {
		mBuilder.mDialog.dismiss();
	   }
	});
   }

   public int getStockNumber() {
	return mStockNumber;
   }

   public int getStockType() {
	return mStockType;
   }

   public void getLeftDate() {
	NetRequest.getInstance().materialControl("23233", mContext,new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		mLeftTopBean = mGson.fromJson(result, SocketLeftTopBean.class);
		if (mLeftTopBean!=null){
		   getLeftDownDate();
		}
	   }
	});
   }

   private void onSucceedDate(SocketLeftDownBean mLeftDownBean) {
	mBuilder.mDialog.dismiss();
	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
	List<SocketLeftTopBean.CstExpirationVosBean> cstExpirationVos = mLeftTopBean.getCstExpirationVos();
	mLeftAdapter = new StockLeftAdapter(R.layout.item_stock_lefttop_layout,cstExpirationVos);
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
	   }
	});
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
	   getStockType();
	   getStockNumber();
	   String deviceCode =null ;
	   if (position==0){
		deviceCode = null;
	   }else {
		deviceCode =mLeftTopBean.getCstExpirationVos().get(position-1).getDeviceCode();
	   }
	   if (mStockType == 0) {
		mStockLeftAlltop.setVisibility(View.VISIBLE);
		return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_LEFT,deviceCode);
	   } else if (mStockType == 1) {
		mStockLeftAlltop.setVisibility(View.GONE);
		return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_MIDDLE,deviceCode);
	   } else {
		mStockLeftAlltop.setVisibility(View.GONE);
		return PublicTimelyFrag.newInstance(mStockNumber, STYPE_STOCK_RIGHT,deviceCode);
	   }
	}

	@Override
	public CharSequence getPageTitle(int position) {
	   String deviceName=null;
	   if (position==0){
		deviceName = "全部";
	   }else {
		deviceName = mLeftTopBean.getCstExpirationVos().get(position-1).getDeviceName();
	   }

	   return deviceName;
	}

	@Override
	public int getCount() {
	   return  mLeftTopBean.getCstExpirationVos() == null ? 0 :  mLeftTopBean.getCstExpirationVos().size()+1;
	}
   }


}
