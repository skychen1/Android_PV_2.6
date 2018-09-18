package high.rivamed.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TimelyDetailsActivity;
import high.rivamed.myapplication.activity.TimelyLossActivity;
import high.rivamed.myapplication.activity.TimelyProfitActivity;
import high.rivamed.myapplication.adapter.TimelyAllAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/1 10:38
 * 描述:        实时盘点单个界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

@SuppressLint("ValidFragment")
public class TimelyAllFrag extends SimpleFragment {

   private List<BoxSizeBean.TbaseDevicesBean> mBoxList = new ArrayList<>();
   private List<DeviceInventoryVo> mDeviceInventoryVos;
   private TCstInventoryDto  mInventoryDto = new TCstInventoryDto();
   private ArrayList<String> mBoxIdList    = new ArrayList<String>();
   private ArrayList<String> mBoxIdListss;

   private List<DeviceInventoryVo> mDeviceList = new ArrayList<>();
   private List<TCstInventory> mEpcList;
   private int                   mEpcsNumber = 0;
   private List<TCstInventoryVo> mVoList     = new ArrayList<>();

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START4")) {
	   AllDeviceCallBack.getInstance().initCallBack();
	}
   }

   private static final String TAG = "TimelyAllFrag";
   private String mDeviceCode;
   @BindView(R.id.timely_loss)
   TextView mTimelyLoss;
   @BindView(R.id.timely_start_btn)
   TextView mTimelyStartBtn;
   @BindView(R.id.timely_book)
   TextView mTimelyBook;
   @BindView(R.id.timely_reality)
   TextView mTimelyReality;

   @BindView(R.id.timely_reality2)
   TextView mTimelyReality2;
   @BindView(R.id.timely_profit)
   TextView mTimelyProfit;

   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.timely_title)
   RelativeLayout     mRelativeLayout;
   @BindView(R.id.stock_search)
   FrameLayout        mStockSearch;
   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView          mSearchIvDelete;
   @BindView(R.id.right_top)
   LinearLayout       mRightTop;

   @BindView(R.id.stock_right_btn)
   LinearLayout   mStockRightLL;
   @BindView(R.id.public_rl)
   RelativeLayout mPublicRl;
   @BindView(R.id.public_ll)
   LinearLayout   mPublicLl;
   @BindView(R.id.stock_left_all)
   RadioButton    mStockLeftAll;
   @BindView(R.id.stock_left_guoqi)
   RadioButton    mStockLeftGuoqi;

   @BindView(R.id.stock_left_jinqi)
   RadioButton    mStockLeftJinqi;
   @BindView(R.id.stock_left_zhengchang)
   RadioButton    mStockLeftZhengchang;
   @BindView(R.id.stock_left_rg)
   RadioGroup     mStockLeftRg;
   @BindView(R.id.stock_timely_ll)
   RelativeLayout mStockTimelyLl;
   List<String> titeleList = null;
   private int  mSize; //假数据 举例6个横向格子
   private View mHeadView;
   private int  mLayout;
   private int  mPosition;
   private static final String DEVICECODE = "DEVICECODE";
   private HashMap<String, String> mReaderMap;
   List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;

   public static List<String>          mReaderIdList;
   private       List<TCstInventoryVo> mTCstInventoryVos;
   private       TCstInventoryDto      mCstInventoryDto;
   private       TimelyAllAdapter      mTimelyAllAdapter;
   private       String                mToJson;
   private       LoadingDialog.Builder mBuilder;
   public static boolean                    mPause   = true;
   private       Map<String, List<TagInfo>> mEPCDate = new TreeMap<>();
   int k = 0;

   private LoadingDialog.Builder mLoading;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoading event) {
      if(!mPause){
	   if (event.loading) {
		if (mLoading == null) {
		   LogUtils.i(TAG, "     mLoading  新建 ");
		   mLoading = DialogUtils.showLoading(mContext);
		} else {
		   if (!mLoading.mDialog.isShowing()){
			LogUtils.i(TAG,"     mLoading   重新开启");
			mLoading.create().show();
		   }
		}
	   }
	}

   }

   /**
    * 扫描后EPC准备传值
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	LogUtils.i(TAG, "onCallBackEvent  mPause    " + mPause);
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
		.find(BoxIdBean.class);

	for (BoxIdBean boxIdBean : boxIdBeanss) {
	   String box_id = boxIdBean.getBox_id();
	   if (box_id != null) {
		List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
									   READER_TYPE).find(BoxIdBean.class);
		if (boxIdBeansss.size() > 1) {

		   for (BoxIdBean BoxIdBean : boxIdBeansss) {
			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
			   mEPCDate.putAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			}
		   }
		   if (k == boxIdBeansss.size()) {
			k = 0;
			if (!mPause) {
			   LogUtils.i(TAG, "mEPCDate  zou l  ");
			   AllDeviceCallBack.getInstance().initCallBack();
			   getDeviceDate(event.deviceId, mEPCDate);
			}
		   }

		} else {
		   if (!mPause) {
			LogUtils.i(TAG, "event.epcs直接走   " + event.epcs);
			AllDeviceCallBack.getInstance().initCallBack();
			getDeviceDate(event.deviceId, event.epcs);
		   }
		}
	   }
	}
   }

   @SuppressLint("ValidFragment")
   public TimelyAllFrag(String deviceCode, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	this.mDeviceCode = deviceCode;
	this.mTbaseDevices = mTbaseDevices;

   }

   @Override
   public int getLayoutId() {
	return R.layout.public_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	mPause = false;
	LogUtils.i(TAG, "initDataAndEvent  mDeviceCode   " + mDeviceCode);
	initDateAll();
	//	initDate(0, 0, 0, 0);
	AllDeviceCallBack.getInstance().initCallBack();
	//	openDoor();

   }

   @Override
   public void onPause() {
	mPause = true;
	EventBusUtils.unregister(this);
	super.onPause();

   }

   @Override
   public void onResume() {
	EventBusUtils.register(this);
	mPause = false;
	super.onResume();
   }

   private void initDateAll() {
	initDate(0, 0, 0, 0);
   }

   private void initDate(int epcSize, int reduce, int add, int count) {
	if (epcSize == count) {
	   mTimelyReality.setText(
		   Html.fromHtml("实际扫描数：<font color='#262626'><big>" + epcSize + "</big>&emsp</font>"));
	} else {
	   mTimelyReality.setText(
		   Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + epcSize + "</big>&emsp</font>"));
	}

	mTimelyBook.setText(
		Html.fromHtml("账面库存数：<font color='#262626'><big>" + count + "</big>&emsp</font>"));
	if (reduce == 0) {
	   mTimelyLoss.setText(Html.fromHtml("盘亏：" + "<font color='#262626'>" + reduce + "</font>"));
	} else {
	   mTimelyLoss.setText(Html.fromHtml("盘亏：" + "<font color='#F5222D'>" + reduce + "</font>"));
	}
	if (add == 0) {
	   mTimelyProfit.setText(Html.fromHtml("盘盈：" + "<font color='#262626'>" + add + "</font>"));
	} else {
	   mTimelyProfit.setText(Html.fromHtml("盘盈：" + "<font color='#F5222D'>" + add + "</font>"));
	}

	String[] array = mContext.getResources().getStringArray(R.array.six_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;

	mLayout = R.layout.item_realtime_six_layout;
	mHeadView = getLayoutInflater().inflate(R.layout.item_realtime_six_title_layout,
							    (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));

	mHeadView.setBackgroundResource(R.color.bg_green);
	mTimelyAllAdapter = new TimelyAllAdapter(mLayout, mTCstInventoryVos);
	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRefreshLayout.setEnableAutoLoadMore(false);

	mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	mLinearLayout.addView(mHeadView);
	mRecyclerview.setAdapter(mTimelyAllAdapter);
	View inflate = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
	mTimelyAllAdapter.setEmptyView(inflate);
	mTimelyAllAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

		LogUtils.i(TAG, "详情 position   " + position);
		String cstId = mTCstInventoryVos.get(position).getCstCode();
		LogUtils.i(TAG, "cstId  " + cstId);
		LogUtils.i(TAG, "mToJson  " + mToJson);
		TCstInventoryDto tCstInventoryDto = mGson.fromJson(mToJson, TCstInventoryDto.class);
		tCstInventoryDto.setCstCode(cstId);
		String deviceCode1 = mTCstInventoryVos.get(position).getDeviceCode();

		TCstInventoryDto dto = new TCstInventoryDto();
		dto.setCstCode(cstId);
		String xxx = "";
		if (mDeviceCode == null || mDeviceCode.equals("")) {//全部的柜子详情
		   dto.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
		   List<DeviceInventoryVo> deviceInventoryVos = tCstInventoryDto.getDeviceInventoryVos();
		   List<DeviceInventoryVo> devo = new ArrayList<>();
		   for (int i = 0; i < deviceInventoryVos.size(); i++) {
			String deviceCode = deviceInventoryVos.get(i).getDeviceCode();
			if (deviceCode.equals(deviceCode1)) {
			   devo.add(deviceInventoryVos.get(i));
			}
		   }
		   dto.setDeviceInventoryVos(devo);
		   xxx = mGson.toJson(dto);
		} else {//单柜
		   dto = tCstInventoryDto;
		   xxx = mGson.toJson(dto);
		}

		LogUtils.i(TAG, "详情 xxx   " + xxx);
		NetRequest.getInstance().getDetailDate(xxx, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "详情 result   " + result);
			TCstInventoryDto tCstInventoryDto = mGson.fromJson(result,
											   TCstInventoryDto.class);
			tCstInventoryDto.setEpcName(mTCstInventoryVos.get(position).getCstName());
			tCstInventoryDto.setCstSpec(mTCstInventoryVos.get(position).getCstSpec());
			mContext.startActivity(new Intent(mContext, TimelyDetailsActivity.class));
			EventBusUtils.postSticky(new Event.timelyDate("详情", tCstInventoryDto));
		   }
		});
	   }
	});

   }

   @OnClick({R.id.timely_start_btn, R.id.timely_profit, R.id.timely_loss})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   DeviceManager.getInstance().UnRegisterDeviceCallBack();
		   AllDeviceCallBack.getInstance().initCallBack();
		   mEPCDate.clear();
		   mBoxList.clear();
		   mBoxList.addAll(mTbaseDevices);

		   if (mTCstInventoryVos != null) {
			mTCstInventoryVos.clear();
			mDeviceInventoryVos.clear();
			mEpcList.clear();
			mDeviceList.clear();
			mBoxIdListss.clear();
			mToJson = null;
			mEpcsNumber = 0;
		   }
		   mTimelyAllAdapter.notifyDataSetChanged();
		   ContentConsumeOperateFrag2.mPause = true;
		   openDoor();

		}
		break;
	   case R.id.timely_profit://盘盈
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (mToJson == null) {
			ToastUtils.showShort("请先盘点后再查看");
		   } else {
			LogUtils.i(TAG, "盘盈 s   " + mToJson);

			NetRequest.getInstance().getProfitDate(mToJson, this, null, new BaseResult() {
			   @Override
			   public void onSucceed(String result) {
				LogUtils.i(TAG, "盘盈 result   " + result);
				TCstInventoryDto tCstInventoryDto = mGson.fromJson(result,
												   TCstInventoryDto.class);
				if (tCstInventoryDto.gettCstInventoryVos() != null &&
				    tCstInventoryDto.gettCstInventoryVos().size() > 0) {
				   mContext.startActivity(new Intent(mContext, TimelyProfitActivity.class));
				   tCstInventoryDto.setAdd(mCstInventoryDto.getAdd());
				   EventBusUtils.postSticky(new Event.timelyDate("盘盈", tCstInventoryDto));
				} else {
				   ToastUtils.showShort("暂无详情数据");
				}
			   }
			});

		   }
		}
		break;
	   case R.id.timely_loss://盘亏
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (mToJson == null) {
			ToastUtils.showShort("请先盘点后再查看");
		   } else {
			LogUtils.i(TAG, "盘亏 s   " + mToJson);
			NetRequest.getInstance().getLossesDate(mToJson, this, null, new BaseResult() {
			   @Override
			   public void onSucceed(String result) {
				LogUtils.i(TAG, "盘亏 result   " + result);
				TCstInventoryDto tCstInventoryDto = mGson.fromJson(result,
												   TCstInventoryDto.class);
				tCstInventoryDto.setReduce(mCstInventoryDto.getReduce());
				if (tCstInventoryDto.gettCstInventoryVos().size() > 0) {
				   mContext.startActivity(new Intent(mContext, TimelyLossActivity.class));
				   EventBusUtils.postSticky(new Event.timelyDate("盘亏", tCstInventoryDto));
				} else {
				   ToastUtils.showShort("暂无详情数据");
				}
			   }
			});

		   }
		}
		break;
	}
   }

   private void getBoxIdList() {
	mBoxIdListss = new ArrayList<String>();
	for (BoxSizeBean.TbaseDevicesBean s : mTbaseDevices) {
	   if (s.getDeviceCode() != null && !s.getDeviceCode().equals("")) {
		mBoxIdListss.add(s.getDeviceCode());
	   }
	}
   }

   private void openDoor() {
	EventBusUtils.postSticky(new Event.EventLoading(true));
	List<String> mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	LogUtils.i(TAG, "mDeviceCode    " + mDeviceCode + "  " + (mDeviceCode == null) + "    " +
			    mDeviceCode.equals("") + "     mReaderDeviceId    " + mReaderDeviceId.size());
	if (mDeviceCode == null || mDeviceCode.equals("")) {
	   getBoxIdList();
	   if (mReaderDeviceId.size() == 0) {
		ToastUtils.showShort("reader未启动，请稍后重新扫描");
	   }
	   for (String readerCode : mReaderDeviceId) {
		DeviceManager.getInstance().StartUhfScan(readerCode);
	   }

	} else {
	   mBoxIdListss = new ArrayList<String>();
	   mBoxIdListss.add(mDeviceCode);
	   LogUtils.i(TAG, "deviceCode   " + mDeviceCode + " READER_TYPE  " + READER_TYPE);
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    READER_TYPE).find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		LogUtils.i(TAG, "device_id   " + device_id);
		LogUtils.i(TAG, "mReaderDeviceId.size   " + mReaderDeviceId.size());
		if (mReaderDeviceId.size() == 0) {
		   ToastUtils.showShort("reader未启动，请稍后重新扫描");
		}
		for (int i = 0; i < mReaderDeviceId.size(); i++) {
		   LogUtils.i(TAG, "mReaderDeviceId.get(i)   " + mReaderDeviceId.get(i));
		   if (mReaderDeviceId.get(i).equals(device_id)) {
			DeviceManager.getInstance().StartUhfScan(device_id);
		   }
		}
	   }
	}
   }

   /**
    * 扫描后传值
    */

   private void getDeviceDate(
	   String deviceId, Map<String, List<TagInfo>> epcs) {

	LogUtils.i(TAG,
		     "mDeviceCode dddd  " + (mDeviceCode == null) + "   " + mDeviceCode.equals(""));
	if (mBoxIdListss != null) {
	   for (String s : mBoxIdListss) {
		LogUtils.i(TAG, "mBoxIdListss   " + s);
		List<BoxIdBean> boxIdBeansF = LitePal.where("device_id = ?", deviceId)
			.find(BoxIdBean.class);
		for (BoxIdBean boxIdBeanF : boxIdBeansF) {
		   String box_id = boxIdBeanF.getBox_id();
		   Log.i(TAG, "device_id   " + box_id);
		   if (box_id != null && box_id.equals(s)) {
			DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
			mEpcList = new ArrayList<>();
			mEpcsNumber += epcs.size();
			for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
			   TCstInventory tCstInventory = new TCstInventory();
			   tCstInventory.setEpc(v.getKey());
			   mEpcList.add(tCstInventory);
			}
			deviceInventoryVo.setDeviceCode(box_id);
			deviceInventoryVo.settCstInventories(mEpcList);
			mDeviceList.add(deviceInventoryVo);
			mBoxIdListss.remove(s);
		   }
		}
	   }
	   mInventoryDto.setDeviceInventoryVos(mDeviceList);
	   mInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));

	   if (mBoxIdListss.size() == 0) {
		mToJson = mGson.toJson(mInventoryDto);
		LogUtils.i(TAG, "toJson deviceId   " + deviceId + "    " + mToJson);
		setTimelyDate(mToJson, deviceId, mEpcsNumber);
	   }
	}
   }

   private void setTimelyDate(String mToJson, String deviceId, int epcs) {
	mEPCDate.clear();
	NetRequest.getInstance().startTimelyScan(mToJson, _mActivity, mBuilder, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result    " + result);
		if (mBuilder != null) {
		   mBuilder.mDialog.dismiss();
		}
		//
		if (mDeviceCode == null || mDeviceCode.equals("")) { //全部柜子扫描显示
		   moreScanTimely(result, epcs, deviceId);
		} else {
		   List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId)
			   .find(BoxIdBean.class);
		   for (BoxIdBean boxIdBean : boxIdBeans) {
			String box_id = boxIdBean.getBox_id();
			if (mDeviceCode.equals(box_id)) {
			   if (mTCstInventoryVos == null) {//第一次盘点的数据
				mCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
				mTCstInventoryVos = mCstInventoryDto.gettCstInventoryVos();
				mDeviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
				int number = 0;
				for (high.rivamed.myapplication.dto.vo.TCstInventoryVo TCstInventoryVo : mTCstInventoryVos) {
				   number += TCstInventoryVo.getCountStock();
				}
				int Reduce = 0;
				int Add = 0;
				for (DeviceInventoryVo l : mDeviceInventoryVos) {
				   Reduce += l.getReduce();
				   Add += l.getAdd();
				}
				initDate(epcs, mCstInventoryDto.getReduce(), mCstInventoryDto.getAdd(),
					   number);
			   } else {//重新刷新的数据
				mTCstInventoryVos.clear();
				mDeviceInventoryVos.clear();
				TCstInventoryDto mCstInventoryDto = mGson.fromJson(result,
												   TCstInventoryDto.class);
				List<TCstInventoryVo> tCstInventoryVos = mCstInventoryDto.gettCstInventoryVos();
				List<DeviceInventoryVo> deviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
				mTCstInventoryVos.addAll(tCstInventoryVos);
				mDeviceInventoryVos.addAll(deviceInventoryVos);
				int number = 0;
				for (TCstInventoryVo TCstInventoryVo : mTCstInventoryVos) {
				   number += TCstInventoryVo.getCountStock();
				}
				int Reduce = 0;
				int Add = 0;
				for (DeviceInventoryVo l : mDeviceInventoryVos) {
				   Reduce += l.getReduce();
				   Add += l.getAdd();
				}
				if (epcs == number) {
				   mTimelyReality.setText(Html.fromHtml(
					   "实际扫描数：<font color='#262626'><big>" + epcs +
					   "</big>&emsp</font>"));
				} else {
				   mTimelyReality.setText(Html.fromHtml(
					   "实际扫描数：<font color='#F5222D'><big>" + epcs +
					   "</big>&emsp</font>"));
				}

				mTimelyBook.setText(Html.fromHtml(
					"账面库存数：<font color='#262626'><big>" + number + "</big>&emsp</font>"));

				if (mCstInventoryDto.getReduce() == 0) {
				   mTimelyLoss.setText(Html.fromHtml(
					   "盘亏：" + "<font color='#262626'>" + mCstInventoryDto.getReduce() +
					   "</font>"));
				} else {
				   mTimelyLoss.setText(Html.fromHtml(
					   "盘亏：" + "<font color='#F5222D'>" + mCstInventoryDto.getReduce() +
					   "</font>"));
				}
				if (mCstInventoryDto.getAdd() == 0) {
				   mTimelyProfit.setText(Html.fromHtml(
					   "盘盈：" + "<font color='#262626'>" + mCstInventoryDto.getAdd() +
					   "</font>"));
				} else {
				   mTimelyProfit.setText(Html.fromHtml(
					   "盘盈：" + "<font color='#F5222D'>" + mCstInventoryDto.getAdd() +
					   "</font>"));
				}

				mTimelyAllAdapter.notifyDataSetChanged();

			   }
			}
		   }
		}

	   }
	});
   }

   /**
    * 多柜的全部扫描
    *
    * @param result
    * @param epcs
    * @param deviceId
    */
   private void moreScanTimely(String result, int epcs, String deviceId) {
	if (mTCstInventoryVos == null) {  //第一次扫描
	   mCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
	   mTCstInventoryVos = mCstInventoryDto.gettCstInventoryVos();
	   mDeviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
	   int number = 0;
	   for (TCstInventoryVo TCstInventoryVo : mTCstInventoryVos) {
		number += TCstInventoryVo.getCountStock();
	   }
	   initDate(epcs, mCstInventoryDto.getReduce(), mCstInventoryDto.getAdd(), number);
	} else {//扫描后数据更新
	   mTCstInventoryVos.clear();
	   mDeviceInventoryVos.clear();
	   TCstInventoryDto mCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
	   List<TCstInventoryVo> tCstInventoryVos = mCstInventoryDto.gettCstInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
	   mTCstInventoryVos.addAll(tCstInventoryVos);
	   mDeviceInventoryVos.addAll(deviceInventoryVos);
	   int number = 0;
	   for (TCstInventoryVo TCstInventoryVo : mTCstInventoryVos) {
		number += TCstInventoryVo.getCountStock();
	   }
	   if (epcs == number) {
		mTimelyReality.setText(
			Html.fromHtml("实际扫描数：<font color='#262626'><big>" + epcs + "</big>&emsp</font>"));
	   } else {
		mTimelyReality.setText(
			Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + epcs + "</big>&emsp</font>"));
	   }

	   mTimelyBook.setText(
		   Html.fromHtml("账面库存数：<font color='#262626'><big>" + number + "</big>&emsp</font>"));
	   if (mCstInventoryDto.getReduce() == 0) {
		mTimelyLoss.setText(Html.fromHtml(
			"盘亏：" + "<font color='#262626'>" + mCstInventoryDto.getReduce() + "</font>"));
	   } else {
		mTimelyLoss.setText(Html.fromHtml(
			"盘亏：" + "<font color='#F5222D'>" + mCstInventoryDto.getReduce() + "</font>"));
	   }

	   if (mCstInventoryDto.getAdd() == 0) {
		mTimelyProfit.setText(Html.fromHtml(
			"盘盈：" + "<font color='#262626'>" + mCstInventoryDto.getAdd() + "</font>"));
	   } else {
		mTimelyProfit.setText(Html.fromHtml(
			"盘盈：" + "<font color='#F5222D'>" + mCstInventoryDto.getAdd() + "</font>"));

	   }

	   mTimelyAllAdapter.notifyDataSetChanged();

	}
	mTimelyAllAdapter.notifyDataSetChanged();
   }

   @Override
   public void onBindViewBefore(View view) {

   }
}
