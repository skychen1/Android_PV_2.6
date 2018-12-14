package high.rivamed.myapplication.fragment;

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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.StockLeftAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_LEFT;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 19:03
 * 描述:        库存监控
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class StockLeftListenerFrag extends SimpleFragment {

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
   private List<String>            mTitles;
   private String[]                mKeys;
   public  int                     mStockNumber=5;//列表的列数
   private StockLeftAdapter        mLeftAdapter;
   private List                    mDates;
   public  SocketLeftTopBean       mLeftTopBean;
   private LoadingDialog.Builder   mBuilder;

   @Override
   public int getLayoutId() {
	return R.layout.cttimecheck_layout;
   }

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START3")) {

	   getLeftDate();
	}
   }
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
//	mBuilder = DialogUtils.showLoading(mContext);
	getLeftDate();

   }



   public void getLeftDate() {
	NetRequest.getInstance().materialControl( mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		mLeftTopBean = mGson.fromJson(result, SocketLeftTopBean.class);
		if (mLeftTopBean != null) {
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
	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
	List<SocketLeftTopBean.CstExpirationVosBean> cstExpirationVos = mLeftTopBean.getCstExpirationVos();
	mLeftAdapter = new StockLeftAdapter(R.layout.item_stock_lefttop_layout, cstExpirationVos);
	mStockLeftRv.setLayoutManager(layoutManager);
	mStockLeftRv.setAdapter(mLeftAdapter);

	mLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		RadioGroup group = (RadioGroup) mContext.findViewById(R.id.rg_group);
		group.check(R.id.stock_rdbtn_middle);
		ViewPager viewPager = (ViewPager) mContext.findViewById(R.id.home_stock_viewpager);
		viewPager.setCurrentItem(1);
		if (mLeftAdapter.getData().size()>1){
		   EventBusUtils.postSticky(new Event.EventJump(position+1));
		}else {
		   EventBusUtils.postSticky(new Event.EventJump(position));
		}
		FragmentManager manager = getParentFragment().getFragmentManager();
		StockMiddleInforFrag inforFrag = new StockMiddleInforFrag();
		manager.beginTransaction().replace(R.id.home_stock_viewpager, inforFrag).commit();
	   }
	});
	mPagerAdapter = new StockMiddlePagerAdapter(getChildFragmentManager());
	mCttimecheckViewpager.setAdapter(mPagerAdapter);
	mCttimecheckViewpager.setCurrentItem(0);
	mCttimecheckViewpager.setOffscreenPageLimit(6);
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
	   if (position == 0) {
		deviceCode = null;
	   } else {
		deviceCode = mLeftTopBean.getCstExpirationVos().get(position - 1).getDeviceId();
	   }
	   mStockLeftAlltop.setVisibility(View.VISIBLE);

	   return PublicStockFrag.newInstance(mStockNumber, STYPE_STOCK_LEFT, deviceCode);

	}

	@Override
	public CharSequence getPageTitle(int position) {
	   String deviceName = null;

	   if (mLeftTopBean.getCstExpirationVos().size()>1) {
		if (position == 0) {
		   deviceName = "全部";
		} else {
		   deviceName = mLeftTopBean.getCstExpirationVos().get(position - 1).getDeviceName();
		}
	   }else {
		deviceName = mLeftTopBean.getCstExpirationVos().get(position).getDeviceName();
	   }
	   return deviceName;
	}

	@Override
	public int getCount() {
	   if (mLeftTopBean.getCstExpirationVos().size()>1) {
		return mLeftTopBean.getCstExpirationVos() == null ? 0 : mLeftTopBean.getCstExpirationVos().size()+1 ;
	   }else {
		return mLeftTopBean.getCstExpirationVos() == null ? 0 : mLeftTopBean.getCstExpirationVos().size() ;
	   }
	}
   }

}
