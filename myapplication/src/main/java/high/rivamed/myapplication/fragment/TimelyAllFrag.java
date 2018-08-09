package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
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
public class TimelyAllFrag extends SimpleFragment {

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
   private LoadingDialog.Builder       mBuilder;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEvent(Event.EventString event) {
	mDeviceCode =event.mString;
   }

   public static TimelyAllFrag newInstance(String deviceCode) {
	Bundle args = new Bundle();
	TimelyAllFrag fragment = new TimelyAllFrag();
	args.putString(DEVICECODE, deviceCode);
	fragment.setArguments(args);
	return fragment;
   }
   @Override
   public int getLayoutId() {
	return R.layout.public_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
      EventBusUtils.register(this);
//	Bundle arguments = getArguments();
//	mDeviceCode = arguments.getString(DEVICECODE);
	LogUtils.i(TAG,"initDataAndEvent  mDeviceCode   "+mDeviceCode);
	initDate(0, 0, 0, 0);
	initCallBack();

   }

   private void initDate(int epcSize, int reduce, int add, int count) {
	mTimelyReality.setText(
		Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + epcSize + "</big>&emsp</font>"));
	mTimelyBook.setText(
		Html.fromHtml("账面库存数：<font color='#262626'><big>" + count + "</big>&emsp</font>"));

	mTimelyLoss.setText(Html.fromHtml("盘亏：" + "<font color='#F5222D'>" + reduce + "</font>"));
	mTimelyProfit.setText(Html.fromHtml("盘盈：" + "<font color='#F5222D'>" + add + "</font>"));
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
	mRefreshLayout.setEnableAutoLoadMore(true);
	mLinearLayout.addView(mHeadView);
	mRecyclerview.setAdapter(mTimelyAllAdapter);
	View inflate = getLayoutInflater().inflate(R.layout.recy_null, null);
	mTimelyAllAdapter.setEmptyView(inflate);
	mTimelyAllAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

		LogUtils.i(TAG, "详情 position   " + position);
		String cstCode = mTCstInventoryVos.get(position).getCstCode();
		LogUtils.i(TAG, "cstCode  " + cstCode);
		TCstInventoryDto tCstInventoryDto = mGson.fromJson(mToJson, TCstInventoryDto.class);
		tCstInventoryDto.setCstCode(cstCode);
		String s = mGson.toJson(tCstInventoryDto);
		LogUtils.i(TAG, "详情 s   " + s);
		NetRequest.getInstance().getDetailDate(s, this, null, new BaseResult() {
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
		   mBuilder = DialogUtils.showLoading(_mActivity);
		   eth002DeviceIdList = DevicesUtils.getEthDeviceId();
		   for (int i = 0; i < eth002DeviceIdList.size(); i++) {
			DeviceManager.getInstance().CheckDoorState((String) eth002DeviceIdList.get(i));
		   }
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
				if (tCstInventoryDto.getInventorys().size()>0){
				   mContext.startActivity(new Intent(mContext, TimelyProfitActivity.class));
				   tCstInventoryDto.setAdd(mCstInventoryDto.getAdd());
				   EventBusUtils.postSticky(new Event.timelyDate("盘盈", tCstInventoryDto));
				}else {
				  ToastUtils.showShort("暂无详情数据");
				}
			   }
			});
		   }

		   //		   mContext.startActivity(new Intent(mContext, TimelyProfitActivity.class));
		   //		   EventBusUtils.postSticky(new Event.EventAct("盘盈"));
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
				if (tCstInventoryDto.getInventorys().size()>0){
				   mContext.startActivity(new Intent(mContext, TimelyLossActivity.class));
				   EventBusUtils.postSticky(new Event.timelyDate("盘亏", tCstInventoryDto));
				}else {
				   ToastUtils.showShort("暂无详情数据");
				}
			   }
			});
		   }

		}
		break;
	}
   }

   private void openDoor() {

	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	LogUtils.i(TAG, "mDeviceCode    "+mDeviceCode);
	if (mDeviceCode==null||mDeviceCode.equals("")) {
	   List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", READER_TYPE).find(BoxIdBean.class);
	   mReaderMap = new HashMap<>();
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		String box_id = boxIdBean.getBox_id();
		mReaderMap.put(device_id, box_id);
		if (mReaderDeviceId.size() == 0) {
		   ToastUtils.showShort("请重新扫描");
		}
		for (int i = 0; i < mReaderDeviceId.size(); i++) {
		   LogUtils.i(TAG, "mReaderDeviceId.get(i)   " + mReaderDeviceId.get(i));
		   if (mReaderDeviceId.get(i).equals(device_id)) {

			int ret = DeviceManager.getInstance().StartUhfScan(device_id);
			if (ret == 100) {
			   LogUtils.i(TAG, "扫描失败    ");
			} else {
			   LogUtils.i(TAG, "开始扫描了状态0    " + ret);
			   DeviceManager.getInstance().StartUhfScan(device_id);
			}
		   }
		}
	   }

	} else {


	   LogUtils.i(TAG, "deviceCode   " + mDeviceCode + " READER_TYPE  " + READER_TYPE);
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    READER_TYPE).find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		LogUtils.i(TAG, "device_id   " + device_id);
		LogUtils.i(TAG, "mReaderDeviceId.size   " + mReaderDeviceId.size());
		if (mReaderDeviceId.size() == 0) {

		   ToastUtils.showShort("请重新扫描");
		}
		for (int i = 0; i < mReaderDeviceId.size(); i++) {
		   LogUtils.i(TAG, "mReaderDeviceId.get(i)   " + mReaderDeviceId.get(i));
		   if (mReaderDeviceId.get(i).equals(device_id)) {

			int ret = DeviceManager.getInstance().StartUhfScan(device_id);
			if (ret == 100) {

			} else {
			   LogUtils.i(TAG, "开始扫描了状态1    " + ret);

			   DeviceManager.getInstance().StartUhfScan(device_id);
			}
		   }
		}
	   }
	}
   }

   private void initCallBack() {
	DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
	   @Override
	   public void OnDeviceConnected(
		   DeviceType deviceType, String deviceIndentify) {

	   }

	   @Override
	   public void OnDeviceDisConnected(
		   DeviceType deviceType, String deviceIndentify) {

	   }

	   @Override
	   public void OnCheckState(
		   DeviceType deviceType, String deviceId, Integer code) {

	   }

	   @Override
	   public void OnIDCard(String deviceId, String idCard) {

	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {

	   }

	   @Override
	   public void OnFingerRegExcuted(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {

	   }

	   @Override
	   public void OnDoorOpened(String deviceIndentify, boolean success) {
		if (success) {//未关闭柜门
		   Toast.makeText(mContext, "请关闭柜门", Toast.LENGTH_SHORT).show();
		} else {
		   openDoor();
		}
	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {

	   }

	   @Override
	   public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
	   }

	   @Override
	   public void OnUhfScanRet(
		   boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
		if (!success) {
		   //		   mBuilder.mDialog.dismiss();
		}

		getDeviceDate(deviceId, epcs);
	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {

	   }

	   @Override
	   public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

	   }

	   @Override
	   public void OnUhfSetPowerRet(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

	   }
	});
   }

   /**
    * 扫描后传值
    */

   private void getDeviceDate(String deviceId, Map<String, List<TagInfo>> epcs) {

	TCstInventoryDto tCstInventoryDto = new TCstInventoryDto();
	List<TCstInventory> epcList = new ArrayList<>(); //耗材信息
	for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
	   TCstInventory tCstInventory = new TCstInventory();
	   tCstInventory.setEpc(v.getKey());
	   epcList.add(tCstInventory);
	}
	DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
	List<DeviceInventoryVo> deviceList = new ArrayList<>();

	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   Log.i(TAG, "device_id   " + box_id);
	   deviceInventoryVo.setDeviceCode(box_id);
	}
	deviceInventoryVo.settCstInventories(epcList);
	deviceList.add(deviceInventoryVo);

	tCstInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
	tCstInventoryDto.setDeviceInventoryVos(deviceList);

	mToJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + mToJson);

	NetRequest.getInstance().startTimelyScan(mToJson, _mActivity, mBuilder, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mBuilder.mDialog.dismiss();
		if (mTCstInventoryVos == null) {//第一次盘点的数据
		   mCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
		   mTCstInventoryVos = mCstInventoryDto.gettCstInventoryVos();
//		   if(mReaderMap!=null){
//			mReaderMap
//		   }
		   int number = 0;
		   for (TCstInventoryVo TCstInventoryVo : mTCstInventoryVos) {
			number += TCstInventoryVo.getCountStock();
		   }
		   initDate(epcs.size(), mCstInventoryDto.getReduce(), mCstInventoryDto.getAdd(),
				number);
		   mTimelyAllAdapter.notifyDataSetChanged();
		} else {//重新刷新的数据
		   mCstInventoryDto = null;
		   mTCstInventoryVos.clear();
		   mCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
		   List<TCstInventoryVo> tCstInventoryVos = mCstInventoryDto.gettCstInventoryVos();
		   mTCstInventoryVos.addAll(tCstInventoryVos);
		   mTimelyAllAdapter.notifyDataSetChanged();
		   int number = 0;
		   for (TCstInventoryVo TCstInventoryVo : mTCstInventoryVos) {
			number += TCstInventoryVo.getCountStock();
		   }
		   mTimelyReality.setText(Html.fromHtml(
			   "实际扫描数：<font color='#F5222D'><big>" + epcs.size() + "</big>&emsp</font>"));
		   mTimelyBook.setText(Html.fromHtml(
			   "账面库存数：<font color='#262626'><big>" + number + "</big>&emsp</font>"));

		   mTimelyLoss.setText(Html.fromHtml(
			   "盘亏：" + "<font color='#F5222D'>" + mCstInventoryDto.getReduce() + "</font>"));
		   mTimelyProfit.setText(Html.fromHtml(
			   "盘盈：" + "<font color='#F5222D'>" + mCstInventoryDto.getAdd() + "</font>"));
		}

	   }
	});

   }

   @Override
   public void onBindViewBefore(View view) {

   }
}
