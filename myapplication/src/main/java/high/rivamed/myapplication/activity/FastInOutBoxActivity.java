package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.fragment.RegisteFrag;
import high.rivamed.myapplication.fragment.RegisteReaderFrag;
import high.rivamed.myapplication.utils.LogUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/11/29 17:42
 * 描述:        快速开柜
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class FastInOutBoxActivity extends BaseSimpleActivity {

   private static final String TAG = "FastInOutBoxActivity";
   @BindView(R.id.fast_tl)
   SlidingTabLayout mFastTl;
   @BindView(R.id.fast_viewpager)
   ViewPager        mFastViewpager;
   public String[] mKeys = {"出柜", "入柜"};
   public TCstInventoryDto mOutDto;
   public String           mInJson;
   public List<TCstInventoryVo> mTCstInventoryVosOut = new ArrayList<>(); //入柜扫描到的epc信息
   public List<TCstInventoryVo> mTCstInventoryVosIn  = new ArrayList<>(); //入柜扫描到的epc信息
   public  TCstInventoryDto mInDto;
   private FastPagerAdapter mFastPagerAdapter;

   /**
    * 接收快速入柜的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onOutDtoEvent(Event.EventInDto event) {
      LogUtils.i(TAG,"outDto   "+mGson.toJson(event.outDto));
      LogUtils.i(TAG,"inDto   "+mGson.toJson(event.inDto));
	if (mInDto != null && mTCstInventoryVosIn != null&&mOutDto!=null&&mTCstInventoryVosOut!=null) {
	   mInDto = event.inDto;
	   List<TCstInventoryVo> tCstInventoryVos = event.inDto.gettCstInventoryVos();
	   mTCstInventoryVosIn.clear();
	   mTCstInventoryVosIn.addAll(tCstInventoryVos);
	   mOutDto.settCstInventoryVos(event.outDto.gettCstInventoryVos());

	   //		mTimelyOpenDoor.setVisibility(View.GONE);
	   //		setInBoxDate();
	   //		mTypeView.mInBoxAllAdapter.notifyDataSetChanged();

	} else {
	   mInDto = event.inDto;
	   mOutDto = event.outDto;
	   mTCstInventoryVosIn = event.inDto.gettCstInventoryVos();//选择开柜
	   mTCstInventoryVosOut = mOutDto.gettCstInventoryVos();
	}
	if (mOutDto!=null&&mTCstInventoryVosOut.size()>0){
	   for (int i = 0; i < mTCstInventoryVosOut.size(); i++) {
		mTCstInventoryVosOut.get(i).setSelected(true);
	   }
	   List<TCstInventoryVo> voList = mTCstInventoryVosOut;
	   for (int i = 0; i < voList.size(); i++) {
		voList.get(i).setSelected(true);
	   }
	}

	mKeys = new String[]{"出柜（" + mTCstInventoryVosOut.size() + "）",
			"入柜（" + mTCstInventoryVosIn.size() + "）"};


	mFastTl.setViewPager(mFastViewpager, mKeys);
   }

//   /**
//    * 接收快速开柜的数据
//    *
//    * @param event
//    */
//   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//   public void onOutDtoEvent(Event.EventOutDto event) {
//	LogUtils.i(TAG,"EventOutDto   "+mGson.toJson(event.cstInventoryDto));
//	if (mOutDto != null) {
//	   mOutDto.settCstInventoryVos(event.cstInventoryDto.gettCstInventoryVos());
//	   mInJson = event.json;
//	} else {
//	   mOutDto = event.cstInventoryDto;
//	   mTCstInventoryVosOut = mOutDto.gettCstInventoryVos();
//	   mInJson = event.json;
//	}
//	for (int i = 0; i < mOutDto.gettCstInventoryVos().size(); i++) {
//	   mOutDto.gettCstInventoryVos().get(i).setSelected(true);
//	}
//	List<TCstInventoryVo> voList = mOutDto.gettCstInventoryVos();
//	for (int i = 0; i < voList.size(); i++) {
//	   voList.get(i).setSelected(true);
//	}
//	if (mTCstInventoryVosOut != null) {
//	   if (mTCstInventoryVosIn == null) {
//		mKeys = new String[]{"出柜（" + mTCstInventoryVosOut.size() + "）", "入柜（0）"};
//	   } else {
//		mKeys = new String[]{"出柜（" + mTCstInventoryVosOut.size() + "）",
//			"入柜（" + mTCstInventoryVosIn.size() + "）"};
//	   }
//	}
//	mFastTl.setViewPager(mFastViewpager, mKeys);
//   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_fastinout_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mBaseTabTvTitle.setText("快速开柜耗材列表");
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabBtnMsg.setEnabled(false);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mFastPagerAdapter = new FastPagerAdapter(getSupportFragmentManager());
	mFastViewpager.setAdapter(mFastPagerAdapter);
	mFastViewpager.setCurrentItem(0);
   }

   @Override
   protected void onResume() {
	super.onResume();

   }

   private class FastPagerAdapter extends FragmentStatePagerAdapter {

	public FastPagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   if (position == 0) {
		return RegisteFrag.newInstance();
	   } else {
		return RegisteReaderFrag.newInstance();
	   }
	}

	@Override
	public CharSequence getPageTitle(int position) {
	   return mKeys[position];
	}

	@Override
	public int getCount() {
	   return mKeys == null ? 0 : mKeys.length;
	}
   }

}
