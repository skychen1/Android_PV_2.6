package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.reader.ReaderManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.fragment.FastInFragment;
import high.rivamed.myapplication.fragment.FastOutFragment;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.FINISH_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.CONSUMABLE_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.fragment.FastInFragment.mStartsType;

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

public class FastInOutBoxActivity extends BaseSimpleActivity
	implements ViewPager.OnPageChangeListener {

   private static final String TAG = "FastInOutBoxActivity";
   @BindView(R.id.fast_tl)
   SlidingTabLayout mFastTl;

   public static ViewPager        mFastViewpager;
   public        String[]     mKeys;
   public static InventoryDto mInOutDto;
   public        String       mInJson;
   public List<InventoryVo> mInventoryVosOut = new ArrayList<>(); //入柜扫描到的epc信息
   public List<InventoryVo> mInventoryVosIn  = new ArrayList<>(); //入柜扫描到的epc信息
   public  InventoryDto     mInDto;
   private FastPagerAdapter mFastPagerAdapter;

   public  ArrayList<String>          mDoorList  = new ArrayList<>();
   private List<String> mEPCDate   = new ArrayList<>();
   private Map<String, String>        mEPCDatess = new TreeMap<>();
   int k = 0;
   private int                   mOperation;
   private int                   mOutSize;
   private int                   mInSize;
   private InventoryDto          mFastInOutDto;
   private LoadingDialog.Builder mLoading;
   public  FastInFragment        mCurrentFragment;
   /**
    * 重新开柜
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventOpenDoor(Event.EventFastOpenDoor event) {
	EventBusUtils.post(new Event.EventFastTimeStart(false));
	if (event.b) {
	   if (!mIsClick) {
		mInOutDto=null;
		if (mStarts!=null){
		   mStarts.cancel();
		}
		for (String deviceInventoryVo : mEthDeviceIdBack) {
		   String deviceCode = deviceInventoryVo;
		   LogUtils.i(TAG, "deviceCode    " + deviceCode);
		   ConsumableManager.getManager().openDoor(deviceCode,0);
		}
	   } else {
		ToastUtils.showShortToast("请关闭柜门，再进行操作！");
	   }

	}
   }
   /**
    * 重新扫描
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventFastMoreScan event) {
	if (event.b) {
	   mInOutDto=null;
	   moreStartScan();
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		LogUtils.i(TAG, "     mLoading  新建 ");
		mLoading = DialogUtils.showLoading(this);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
		   LogUtils.i(TAG, "     mLoading   重新开启");
		   mLoading.create().show();
		}
	   }
	} else {
	   if (mLoading != null) {
		LogUtils.i(TAG, "     mLoading   关闭");
		mLoading.mAnimationDrawable.stop();
		mLoading.mDialog.dismiss();
		mLoading = null;
	   }
	}
   }

   /**
    * 接收快速开柜的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onOutDtoEvent(Event.EventOutDto event) {
	LogUtils.i(TAG, "event   " + event.outSize);
	if (mInOutDto != null) {
	   mInOutDto.setInInventoryVos(event.inventoryDto.getInInventoryVos());
	   mInOutDto.setOutInventoryVos(event.inventoryDto.getOutInventoryVos());
	   mOperation = event.inventoryDto.getOperation();
	   mInSize = event.inSize;
	   mOutSize = event.outSize;
	} else {
	   mInSize = event.inSize;
	   mOutSize = event.outSize;
	   mInOutDto = event.inventoryDto;
	   mOperation = event.inventoryDto.getOperation();
	   mInventoryVosOut = mInOutDto.getOutInventoryVos();
	   mInventoryVosIn = mInOutDto.getInInventoryVos();
	}
	for (int i = 0; i < mInOutDto.getOutInventoryVos().size(); i++) {
	   mInOutDto.getOutInventoryVos().get(i).setSelected(true);
	}
	List<InventoryVo> voList = mInOutDto.getOutInventoryVos();
	for (int i = 0; i < voList.size(); i++) {
	   voList.get(i).setSelected(true);
	}
	mKeys = new String[]{"出柜（" + mOutSize + "）", "入柜（" + mInSize + "）"};
	if (mFastViewpager != null) {
	   if (mOutSize == 0) {
		mFastViewpager.setCurrentItem(1);
	   } else {
		mFastViewpager.setCurrentItem(0);
	   }
	}

	if (mFastTl != null) {
	   mFastTl.notifyDataSetChanged();
	}
   }

   /**
    * 扫描后EPC准备传值
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	LogUtils.i(TAG, "epc  " + event.deviceId + "   " + event.epcs.size());
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
		.find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeanss) {
	   String box_id = boxIdBean.getBox_id().trim();
	   List<BoxIdBean> boxIdDoor = LitePal.where("box_id = ? and name = ?", box_id,
								   CONSUMABLE_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean BoxIdBean : boxIdDoor) {
		String device_id = BoxIdBean.getDevice_id();
		LogUtils.i(TAG, "device_id  " + device_id);
		for (int x = 0; x < mDoorList.size(); x++) {
		   if (device_id.equals(mDoorList.get(x).trim())) {
			LogUtils.i(TAG, "mDoorList  " + mDoorList.get(x));
			mDoorList.remove(x);
		   }
		}
	   }
	   if (box_id != null) {
		List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
									   READER_TYPE).find(BoxIdBean.class);
		if (boxIdBeansss.size() > 1) {
		   for (BoxIdBean BoxIdBean : boxIdBeansss) {
			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
			   mEPCDate.addAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			}
		   }
		   LogUtils.i(TAG, "mEPCDate  k  " + k);
		   if (k == boxIdBeansss.size()) {
			k = 0;
			if (mEPCDate.size() == 0) {
			   mEPCDatess.put("", box_id);//没有空格
			}
			for (String s : mEPCDate) {
			   mEPCDatess.put(s, box_id);
			}

			LogUtils.i(TAG, "mEPCDates.mEPCDates()多reader  " + mEPCDatess.size());
		   } else {
			return;
		   }
		} else {
		   if (event.epcs.size() == 0) {
			mEPCDatess.put(" ", box_id);//1个空格
		   }
		   for (String s : event.epcs) {
			mEPCDatess.put(s, box_id);
		   }

		   LogUtils.i(TAG, "mEPCDates.mEPCDates()单reader  " + mEPCDatess.size());
		}
	   }
	}

	LogUtils.i(TAG, "mDoorList.size()   " + mDoorList.size());
	if (mDoorList.size() != 0) {
	   return;
	}
	LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size());
	putOutAndInEPCDates(mEPCDatess);
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_fastinout_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mFastViewpager =this.findViewById(R.id.fast_viewpager);
	mBaseTabTvTitle.setText("快速开柜耗材列表");
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabBtnMsg.setEnabled(false);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mKeys = new String[]{"出柜（" + mOutSize + "）", "入柜（" + mInSize + "）"};
	mFastPagerAdapter = new FastPagerAdapter(getSupportFragmentManager());
	mFastViewpager.setAdapter(mFastPagerAdapter);
	mFastViewpager.addOnPageChangeListener(this);
	if (mOutSize == 0) {
	   mFastViewpager.setCurrentItem(1);

	} else {
	   mFastViewpager.setCurrentItem(0);
	}
	mFastTl.setViewPager(mFastViewpager);

   }

   /**
    * 快速开柜出入柜查询
    *
    * @param epcs
    */
   private void putOutAndInEPCDates(Map<String, String> epcs) {
	String toJson = getEpcDtoString(epcs);
	mEPCDate.clear();
	mEPCDatess.clear();
	LogUtils.i(TAG, "toJson  " + toJson);
	NetRequest.getInstance().putOutAndInEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		LogUtils.i(TAG, "result    " + result);
		mFastInOutDto = mGson.fromJson(result, InventoryDto.class);
		List<InventoryVo> inInventoryVos = mFastInOutDto.getInInventoryVos();//入柜的数据
		List<InventoryVo> outInventoryVos = mFastInOutDto.getOutInventoryVos();//出柜的数据

		//		if (mFastInOutDto.getErrorEpcs() != null && mFastInOutDto.getErrorEpcs().size() > 0) {
		//		   string = StringUtils.listToString(mFastInOutDto.getErrorEpcs());
		//		   ToastUtils.showLong(string);
		//		   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
		//		}


		if (mFastInOutDto != null &&
		    (inInventoryVos.size() != 0 || outInventoryVos.size() != 0)) {
		   if (outInventoryVos.size() == 0&&inInventoryVos.size()>0) {
			mFastViewpager.setCurrentItem(1);

		   } else {
			mFastViewpager.setCurrentItem(0);
		   }
		   for (int i = 0; i < outInventoryVos.size(); i++) {
			outInventoryVos.get(i).setSelected(true);
		   }
		   EventBusUtils.postSticky(new Event.EventLoading(false));
		   EventBusUtils.postSticky(new Event.EventOutDto(mFastInOutDto, inInventoryVos.size(),
										  outInventoryVos.size(), "moreScan"));
		} else {

		   mEthDeviceIdBack.clear();
		   mDoorList.clear();
		   //		   EventBusUtils.postSticky(new Event.EventOutDto(mFastInOutDto, inInventoryVos.size(),
		   //										  outInventoryVos.size(), "moreScan"));
		   EventBusUtils.post(new Event.FastOutDataClear(true));
		   FastOutFragment.mBtnFourLy.setEnabled(false);
		   FastOutFragment.mBtnFourTh.setEnabled(false);
		   FastOutFragment.mBtnFourYc.setEnabled(false);
		   EventBusUtils.postSticky(new Event.EventLoading(false));
		   ToastUtils.showShortToast("耗材操作完成，即将退回主页！");
		   new Handler().postDelayed(new Runnable() {
			public void run() {
			   EventBusUtils.postSticky(new Event.EventFrag("START1"));
			   mEthDeviceIdBack.clear();
			   finish();
			}
		   }, FINISH_TIME);
		}
	   }
	   @Override
	   public void onError(String result) {
		super.onError(result);
		EventBusUtils.postSticky(new Event.EventLoading(false));
	   }
	});
   }

   /**
    * 快速开柜epc放入DTO
    *
    * @param epcs
    * @return
    */
   private String getEpcDtoString(Map<String, String> epcs) {
	InventoryDto allOutBean = new InventoryDto();
	List<InventoryVo> epcList = new ArrayList<>();
	for (Map.Entry<String, String> v : epcs.entrySet()) {
	   InventoryVo tCstInventory = new InventoryVo();
	   tCstInventory.setEpc(v.getKey());
	   tCstInventory.setDeviceId(v.getValue());
	   epcList.add(tCstInventory);
	}
	allOutBean.setInventoryVos(epcList);
	allOutBean.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	allOutBean.setThingId(SPUtils.getString(mContext,THING_CODE));
	String toJson = mGson.toJson(allOutBean);
	LogUtils.i(TAG, "toJson mObject   " + toJson);
	return toJson;
   }

   @Override
   protected void onResume() {
	super.onResume();

   }

   @Override
   public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
   }

   @Override
   public void onPageSelected(int position) {
	LogUtils.i(TAG,"onPageSelected");
	if (position == 1 && mStartsType) {
	   EventBusUtils.post(new Event.EventFastTimeStart(true));
	}else {
	   EventBusUtils.post(new Event.EventFastTimeStart(false));
	}
   }

   @Override
   public void onPageScrollStateChanged(int state) {

   }

   private class FastPagerAdapter extends FragmentStatePagerAdapter {

	public FastPagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   if (position == 0) {
		return FastOutFragment.newInstance();
	   } else {
		return FastInFragment.newInstance();
	   }
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
	   if (position == 1) {
		mCurrentFragment = (FastInFragment) object;
	   }
	   super.setPrimaryItem(container, position, object);
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

   /**
    * 重新扫描
    */
   private void moreStartScan() {
	LogUtils.i(TAG, "mEthDeviceIdBack    " + mEthDeviceIdBack.size());
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   startScan(deviceCode);
	}
   }

   private void startScan(String deviceIndentify) {
	EventBusUtils.postSticky(new Event.EventLoading(true));
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 CONSUMABLE_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = ReaderManager.getManager().startScan(device_id, READER_TIME);
		LogUtils.i(TAG, "开始扫描了状态    " + i);
	   }
	}
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
	switch (ev.getAction()) {
	   //获取触摸动作，如果ACTION_UP，计时开始。
	   case MotionEvent.ACTION_UP:
		if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
		    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")&& mFastViewpager.getCurrentItem()==1&&FastInFragment.mTimelyLeft.isEnabled()) {
		   EventBusUtils.post(new Event.EventFastTimeStart(true));
		}
		break;
	   //否则其他动作计时取消
	   default:
		EventBusUtils.post(new Event.EventFastTimeStart(false));
		break;
	}
	return super.dispatchTouchEvent(ev);
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	mEPCDate.clear();
	mEPCDatess.clear();
   }
}
