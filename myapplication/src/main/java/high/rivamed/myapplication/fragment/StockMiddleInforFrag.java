package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
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
   MagicIndicator mCttimeCheck_Rg;
   @BindView(R.id.cttimecheck_viewpager)
   public ViewPager mCttimecheckViewpager;
   @BindView(R.id.stock_left_rv)
   RecyclerView mStockLeftRv;
   @BindView(R.id.stock_left_alltop)
   LinearLayout mStockLeftAlltop;
   public  StockMiddlePagerAdapter mPagerAdapter;
   public  int                   mStockNumber=5;//列表的列数
   private List<BoxSizeBean.DevicesBean> mTbaseDevices;
   /**
    * 重新加载数据
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START3")) {
	   onSucceedDate();
	}
   }
   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onJump(Event.EventJump event) {
      if (mCttimecheckViewpager!=null){
         mCttimecheckViewpager.setCurrentItem(event.jump);
	}
   }
   @Override
   public int getLayoutId() {
	return R.layout.cttimecheck_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	getMiddleDate();

   }

   public void getMiddleDate() {
	String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	mTbaseDevices = mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType());
	if (mTbaseDevices != null) {
	   onSucceedDate();
	}
   }

   @Override
   public void onResume() {
	EventBusUtils.register(this);
	super.onResume();
   }

   private void onSucceedDate() {
	if (!isAdded()) return;
	mPagerAdapter = new StockMiddlePagerAdapter(getChildFragmentManager());
	mCttimecheckViewpager.setAdapter(mPagerAdapter);
	mCttimecheckViewpager.setCurrentItem(0);
	mCttimecheckViewpager.setOffscreenPageLimit(6);
//	mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager);
	UIUtils.initPvTabLayout(mTbaseDevices, mCttimecheckViewpager, mCttimeCheck_Rg);
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

	   mStockLeftAlltop.setVisibility(View.GONE);
	   return PublicStockFrag.newInstance(mStockNumber, STYPE_STOCK_MIDDLE, mTbaseDevices.get(position).getDeviceId(),mTbaseDevices.size());
	}

	@Override
	public CharSequence getPageTitle(int position) {
	   return mTbaseDevices.get(position).getDeviceName();
	}

	@Override
	public int getCount() {
	   return mTbaseDevices == null ? 0 : mTbaseDevices.size() ;
	}
   }


   @Override
   public void onDestroyView() {
	super.onDestroyView();
	try {
	   Field f = Fragment.class.getDeclaredField("mChildFragmentManager");
	   f.setAccessible(true);
	   f.set(this, null);
	} catch (Exception e) {
	   e.printStackTrace();
	}
   }
}
