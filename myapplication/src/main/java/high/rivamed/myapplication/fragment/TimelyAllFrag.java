package high.rivamed.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ruihua.reader.ReaderManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TimelyDetailsActivity;
import high.rivamed.myapplication.activity.TimelyLossActivity;
import high.rivamed.myapplication.activity.TimelyProfitActivity;
import high.rivamed.myapplication.adapter.TimelyAllAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.SavePadPdBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.entity.Inventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialogX;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.CONFIG_026;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.http.NetRequest.sThingCode;
import static high.rivamed.myapplication.utils.StringUtils.search;

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

   private List<BoxSizeBean.DevicesBean> mBoxList = new ArrayList<>();
   private List<DeviceInventoryVo> mDeviceInventoryVos;
   private InventoryDto mInventoryDto = new InventoryDto();
   private ArrayList<String> mBoxIdListss;
   private List<DeviceInventoryVo> mDeviceList = new ArrayList<>();
   private List<Inventory> mEpcList;
   private int mEpcsNumber = 0;
   public static boolean mTimelyOnResume;
   private SavePadPdBean mPutSavePadPdDto;

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	Log.e("FAFAS", "START4   "+event.type);
	if (event.type.equals("START4")) {
	   mPauseS = false;
	   if (mBuilder != null) {
		mBuilder.mLoading.stop();
		mBuilder.mDialog.dismiss();
	   }
	} else {
	   mPauseS = true;
	}
   }

   private static final String TAG = "TimelyAllFrag";
   private String mDeviceCode;
   @BindView(R.id.timely_loss)
   TextView mTimelyLoss;
   @BindView(R.id.timely_start_btn)
   TextView mTimelyStartBtn;
   @BindView(R.id.timely_put_btn)
   TextView mTimelyPutBtn;
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

   @BindView(R.id.search_etsss)
   EditText           mSearchEts;
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
   private View mHeadView;
   private int  mLayout;
   List<BoxSizeBean.DevicesBean> mTbaseDevices;

   private List<InventoryVo> mInventoryVos;
   private InventoryDto      mCstInventoryDto;
   private TimelyAllAdapter  mTimelyAllAdapter;
   private String            mToJson;
   public static boolean                    mPauseS  = true;
   private       List<String>  mEPCDate = new ArrayList<>();
   int k = 0;
   private int mLocalAllSize;
   private LoadingDialogX.Builder mBuilder;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoadingX event) {
	if (event.loading) {
	   if (mBuilder == null) {
		mBuilder = DialogUtils.showRader(mContext);
		mBuilder.setMsgSize(30);
		mBuilder.setMsg("扫描中...");
	   } else {
		if (!mBuilder.mDialog.isShowing()) {
		   mBuilder.create().show();
		   mBuilder.setMsgSize(30);
		   mBuilder.setMsg("扫描中...");
		}
	   }
	} else {
	   if (mBuilder != null) {
		mBuilder.mLoading.stop();
		mBuilder.mDialog.dismiss();
	   }
	}
   }


   /**
    * 扫描后EPC准备传值
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {

	Log.e("FAFAS", "epc   "+event.deviceId+"     "+event.epcs);
	if (!mPauseS) {
	   if (mBuilder != null) {
		mBuilder.mLoading.stop();
		mBuilder.mDialog.dismiss();
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
				mEPCDate.addAll(event.epcs);
				k++;
				LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			   }
			}
			if (k == boxIdBeansss.size()) {
			   k = 0;
			   if (!mPauseS) {
				LogUtils.i(TAG, "mEPCDate  zou l  ");
				getDeviceDate(event.deviceId, mEPCDate);
			   }
			}

		   } else {
			if (!mPauseS) {
			   LogUtils.i(TAG, "event.epcs直接走   " + event.epcs.size());
			   getDeviceDate(event.deviceId, event.epcs);
			}
		   }
		}
	   }
	}

   }

   @SuppressLint("ValidFragment")
   public TimelyAllFrag() {

   }

   @SuppressLint("ValidFragment")
   public TimelyAllFrag(String deviceCode, List<BoxSizeBean.DevicesBean> mTbaseDevices) {
	super();
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
	if (mBuilder != null) {
	   mBuilder.mLoading.stop();
	   mBuilder.mDialog.dismiss();
	}
	initDateAll();
   }

   @Override
   public void onPause() {
	mPauseS = true;
	mTimelyOnResume = false;
	super.onPause();

   }

   @Override
   public void onResume() {
	if (mBuilder != null) {
	   mBuilder.mLoading.stop();
	   mBuilder.mDialog.dismiss();
	}
	mTimelyOnResume = true;
	super.onResume();

	mSearchEts.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	   }

	   @Override
	   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	      if (mInventoryVos==null||mInventoryVos.size()==0){
	         ToastUtils.showShortToast("暂无数据！请盘点后进行搜索");
		}else {
		   String trim = charSequence.toString().trim();
		   getSearchData(trim);

		}
	   }

	   @Override
	   public void afterTextChanged(Editable editable) {

	   }
	});
   }

   /**
    * 获取搜索的数据集
    * @param trim
    */
   private void getSearchData(String trim)  {
	List<InventoryVo> search = search(trim, trim, mInventoryVos);
	mTimelyAllAdapter.getData().clear();
	mTimelyAllAdapter.getData().addAll(search);
	mTimelyAllAdapter.notifyDataSetChanged();
   }

   private void initDateAll() {
	String[] array = mContext.getResources().getStringArray(R.array.seven_home_real_time_arrays);
	titeleList = Arrays.asList(array);

	mLayout = R.layout.item_realtimehome_seven_layout;
	mHeadView = LayoutInflater.from(_mActivity)
		.inflate(R.layout.item_realtimehome_seven_title_layout, (ViewGroup) mLinearLayout.getParent(),
			   false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	if(UIUtils.getConfigType(mContext, CONFIG_026)){
	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setVisibility(View.VISIBLE);
	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	}else {
	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setVisibility(View.GONE);
	}
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.addView(mHeadView);
	LogUtils.i(TAG, "第一次");
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

	if (mTimelyAllAdapter == null) {
	   mTimelyAllAdapter = new TimelyAllAdapter(mLayout, mInventoryVos);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mRefreshLayout.setEnableAutoLoadMore(false);
	   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mRecyclerview.setAdapter(mTimelyAllAdapter);
	} else {
	   mTimelyAllAdapter.getData().clear();
	   mTimelyAllAdapter.getData().addAll(mInventoryVos);
	   mTimelyAllAdapter.notifyDataSetChanged();
	}
	if (mDeviceCode == null || mDeviceCode.equals("")||mTbaseDevices.size()==1) {//全部的柜子详情
	   mTimelyPutBtn.setVisibility(View.VISIBLE);
	}else {
	   mTimelyPutBtn.setVisibility(View.GONE);
	}
	View inflate = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
	mTimelyAllAdapter.setEmptyView(inflate);
	mTimelyAllAdapter.notifyDataSetChanged();

	mTimelyAllAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

		LogUtils.i(TAG, "详情 position   " + position);
		String cstId = mInventoryVos.get(position).getCstCode();
		LogUtils.i(TAG, "mToJson  " + mToJson);
		InventoryDto inventoryDto = mGson.fromJson(mToJson, InventoryDto.class);
		inventoryDto.setCstCode(cstId);
		String deviceCode1 = mInventoryVos.get(position).getDeviceId();

		InventoryDto dto = new InventoryDto();
		dto.setCstCode(cstId);
		String xxx = "";
		if (mDeviceCode == null || mDeviceCode.equals("")) {//全部的柜子详情
		   dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
		   List<DeviceInventoryVo> deviceInventoryVos = inventoryDto.getDeviceInventoryVos();
		   List<DeviceInventoryVo> devo = new ArrayList<>();
		   for (int i = 0; i < deviceInventoryVos.size(); i++) {
			String deviceCode = deviceInventoryVos.get(i).getDeviceId();
			if (deviceCode.equals(deviceCode1)) {
			   devo.add(deviceInventoryVos.get(i));
			}
		   }
		   dto.setDeviceInventoryVos(devo);
		   xxx = mGson.toJson(dto);
		} else {//单柜
		   dto = inventoryDto;
		   xxx = mGson.toJson(dto);
		}
		LogUtils.i(TAG, "详情 xxx   " + xxx);
		NetRequest.getInstance().getDetailDate(xxx, this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "详情 result   " + result);
			InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);
			inventoryDto.setEpcName(mInventoryVos.get(position).getCstName());
			inventoryDto.setCstSpec(mInventoryVos.get(position).getCstSpec());
			mContext.startActivity(new Intent(mContext, TimelyDetailsActivity.class));
			EventBusUtils.postSticky(new Event.timelyDate("详情", inventoryDto));
		   }
		});
	   }
	});

   }

   @OnClick({R.id.timely_start_btn, R.id.timely_profit, R.id.timely_loss,R.id.timely_put_btn})
   public void onViewClicked(View view) {
	mPauseS = false;
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   mEPCDate.clear();
		   mBoxList.clear();
		   mBoxList.addAll(mTbaseDevices);

		   if (mInventoryVos != null) {
			mInventoryVos.clear();
			mDeviceInventoryVos.clear();
			mEpcList.clear();
			mDeviceList.clear();
			mBoxIdListss.clear();
			mToJson = null;
			mEpcsNumber = 0;
		   }
		   mTimelyAllAdapter.notifyDataSetChanged();
		   ContentConsumeOperateFrag.mPause = true;
		   startScan();
		}
		break;
	   case R.id.timely_profit://盘盈
		if (UIUtils.isFastDoubleClick(R.id.timely_profit)) {
		   return;
		} else {
		   if (mToJson == null) {
			ToastUtils.showShort("请先盘点后再查看");
		   } else {
			LogUtils.i(TAG, "盘盈 mObject   " + mToJson);
			NetRequest.getInstance().getProfitDate(mToJson, this, new BaseResult() {
			   @Override
			   public void onSucceed(String result) {
				LogUtils.i(TAG, "盘盈 result   " + result);
				InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);

				if (inventoryDto.getInventoryVos() != null &&
				    inventoryDto.getInventoryVos().size() > 0) {
				   mContext.startActivity(new Intent(mContext, TimelyProfitActivity.class));
				   //				   inventoryDto.setAdd(mCstInventoryDto.getAdd());
				   EventBusUtils.postSticky(new Event.timelyDate("盘盈", inventoryDto));
				} else {
				   ToastUtils.showShort("暂无详情数据");
				}
			   }
			});
		   }
		}
		break;
	   case R.id.timely_loss://盘亏
		if (UIUtils.isFastDoubleClick(R.id.timely_loss)) {
		   return;
		} else {
		   if (mToJson == null) {
			ToastUtils.showShort("请先盘点后再查看");
		   } else {
			LogUtils.i(TAG, "盘亏 mObject   " + mToJson);
			NetRequest.getInstance().getLossesDate(mToJson, this, new BaseResult() {
			   @Override
			   public void onSucceed(String result) {
				LogUtils.i(TAG, "盘亏 result   " + result);
				InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);

				if (inventoryDto.getInventoryVos().size() > 0) {
				   mContext.startActivity(new Intent(mContext, TimelyLossActivity.class));
				   EventBusUtils.postSticky(new Event.timelyDate("盘亏", inventoryDto));
				} else {
				   ToastUtils.showShort("暂无详情数据");
				}
			   }
			});
		   }
		}
		break;
	   case R.id.timely_put_btn://提交盘点单
		if (!UIUtils.isFastDoubleClick(R.id.timely_put_btn)) {
		   if (mPutSavePadPdDto!=null){
			mTimelyPutBtn.setEnabled(false);
			NetRequest.getInstance().putSavePadPdDate(mGson.toJson(mPutSavePadPdDto),this,new BaseResult(){
			   @Override
			   public void onSucceed(String result) {
				mTimelyPutBtn.setEnabled(true);
				SavePadPdBean padPdBean = mGson.fromJson(result, SavePadPdBean.class);
				if (padPdBean.isOperateSuccess()){
				   ToastUtils.showShort("盘点单保存成功！");
				}else {
				   ToastUtils.showShort("盘点单保存失败，请重新操作！");
				}
			   }

			   @Override
			   public void onError(String result) {
				mTimelyPutBtn.setEnabled(true);
			   }
			});
		   }else {
		      ToastUtils.showShort("请盘点后进行保存！");
		   }

		}
	      break;
	}
   }

   private void getBoxIdList() {
	mBoxIdListss = new ArrayList<String>();
	for (BoxSizeBean.DevicesBean s : mTbaseDevices) {
	   if (s.getDeviceId() != null && !s.getDeviceId().equals("")) {
		mBoxIdListss.add(s.getDeviceId());
	   }
	}
   }

   private void startScan() {


	EventBusUtils.postSticky(new Event.EventLoadingX(true));
	List<String> mReaderDeviceId = DevicesUtils.getReaderDeviceId();

	if (mDeviceCode == null || mDeviceCode.equals("")) {
	   getBoxIdList();
	   if (mReaderDeviceId.size() == 0) {
		ToastUtils.showShort("reader未启动，请稍后重新扫描");
		EventBusUtils.postSticky(new Event.EventLoadingX(false));
	   }
	   for (String readerCode : mReaderDeviceId) {
		int x = ReaderManager.getManager().startScan(readerCode, READER_TIME);
		if (x == 2) {
		   ReaderManager.getManager().stopScan(readerCode);
		   ReaderManager.getManager().startScan(readerCode, READER_TIME);
		}
	   }
	} else {
	   mBoxIdListss = new ArrayList<String>();
	   mBoxIdListss.add(mDeviceCode);
	   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
								    READER_TYPE).find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String device_id = boxIdBean.getDevice_id();
		LogUtils.i(TAG, "mReaderDeviceId.size   " + mReaderDeviceId.size());
		if (mReaderDeviceId.size() == 0) {
		   ToastUtils.showShort("reader未启动，请稍后重新扫描");
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		}
		for (int i = 0; i < mReaderDeviceId.size(); i++) {
		   LogUtils.i(TAG, "mReaderDeviceId.get(i)   " + mReaderDeviceId.get(i));
		   if (mReaderDeviceId.get(i).equals(device_id)) {
			int x = ReaderManager.getManager().startScan(device_id, READER_TIME);
			LogUtils.i(TAG, "启动  " + x);
			if (x == 2) {
			   ReaderManager.getManager().stopScan(device_id);
			   ReaderManager.getManager().startScan(device_id, READER_TIME);
			}
		   }
		}
	   }
	}
   }

   /**
    * 扫描后传值
    */

   private void getDeviceDate(String deviceId, List<String>  epcs) {
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
			for (String epc : epcs) {
			   Inventory inventory = new Inventory();
			   inventory.setEpc(epc);
			   mEpcList.add(inventory);
			}

			deviceInventoryVo.setDeviceId(box_id);
			deviceInventoryVo.setInventories(mEpcList);
			mDeviceList.add(deviceInventoryVo);
			mBoxIdListss.remove(s);
		   }
		}
	   }
	   mInventoryDto.setDeviceInventoryVos(mDeviceList);
	   mInventoryDto.setThingId(SPUtils.getString(mContext, THING_CODE));

	   if (mBoxIdListss.size() == 0) {
		mToJson = mGson.toJson(mInventoryDto);
		LogUtils.i(TAG, "toJson deviceId   " + deviceId + "    " + mToJson);
		setTimelyDate(mToJson, deviceId, mEpcsNumber);
	   }
	}
   }

   private void setTimelyDate(String mToJson, String deviceId, int epcs) {
	mEPCDate.clear();
	NetRequest.getInstance().startTimelyScan(mToJson, _mActivity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result    " + result);
		mCstInventoryDto = mGson.fromJson(result, InventoryDto.class);
		if (mCstInventoryDto.isOperateSuccess()){
		   setScanTimelyDate(mCstInventoryDto, epcs, deviceId);

		   if (mDeviceCode == null || mDeviceCode.equals("")||mTbaseDevices.size()==1) {
			mPutSavePadPdDto = new SavePadPdBean();
			mPutSavePadPdDto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
			mPutSavePadPdDto.setThingId(sThingCode);
			mPutSavePadPdDto.setDeviceIds(mCstInventoryDto.getDeviceIds());
			mPutSavePadPdDto.setEpcs(mCstInventoryDto.getEpcs());
		   }
		}
		EventBusUtils.postSticky(new Event.EventLoadingX(false));
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.postSticky(new Event.EventLoadingX(false));
	   }
	});
   }

   private void setScanTimelyDate(InventoryDto mCstInventoryDto, int epcs, String deviceId) {
	if (mDeviceCode == null || mDeviceCode.equals("")) { //全部柜子扫描显示
	   moreScanTimely(mCstInventoryDto, epcs, deviceId);
	   LogUtils.i(TAG, "全部");
	} else {
	   LogUtils.i(TAG, "扫数");
	   List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId)
		   .find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		String box_id = boxIdBean.getBox_id();
		if (mDeviceCode.equals(box_id)) {
		   if (mInventoryVos == null) {//第一次盘点的数据
			mInventoryVos = mCstInventoryDto.getInventoryVos();
			mDeviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
			int number = 0;
			for (InventoryVo InventoryVo : mInventoryVos) {
			   number += InventoryVo.getCountStock();
			}
			int Reduce = 0;
			int Add = 0;
			for (DeviceInventoryVo l : mDeviceInventoryVos) {
			   Reduce += l.getReduce();
			   Add += l.getAdd();
			}
			LogUtils.i(TAG, "第二次");
			initDate(epcs, mCstInventoryDto.getReduce(), mCstInventoryDto.getAdd(),
				   number);
		   } else {//重新刷新的数据
			mInventoryVos.clear();
			mDeviceInventoryVos.clear();
			List<InventoryVo> inventoryVos = mCstInventoryDto.getInventoryVos();
			List<DeviceInventoryVo> deviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
			mInventoryVos.addAll(inventoryVos);
			mDeviceInventoryVos.addAll(deviceInventoryVos);
			mTimelyAllAdapter.getData().clear();
			mTimelyAllAdapter.getData().addAll(inventoryVos);
			int number = 0;
			for (InventoryVo InventoryVo : mInventoryVos) {
			   number += InventoryVo.getCountStock();
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

   /**
    * 多柜的全部扫描
    *
    * @param epcs
    * @param deviceId
    */
   private void moreScanTimely(InventoryDto mCstInventoryDto, int epcs, String deviceId) {
	if (mInventoryVos == null) {  //第一次扫描
	   mInventoryVos = mCstInventoryDto.getInventoryVos();
	   mDeviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
	   int number = 0;
	   for (InventoryVo inventoryVo : mInventoryVos) {
		number += inventoryVo.getCountStock();
	   }
	   LogUtils.i(TAG, "第二次");
	   initDate(epcs, mCstInventoryDto.getReduce(), mCstInventoryDto.getAdd(), number);
	} else {//扫描后数据更新
	   mInventoryVos.clear();
	   mDeviceInventoryVos.clear();
	   List<InventoryVo> inventoryVos = mCstInventoryDto.getInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos = mCstInventoryDto.getDeviceInventoryVos();
	   mInventoryVos.addAll(inventoryVos);
	   mDeviceInventoryVos.addAll(deviceInventoryVos);
	   mTimelyAllAdapter.getData().clear();
	   mTimelyAllAdapter.getData().addAll(inventoryVos);
	   int number = 0;
	   for (InventoryVo inventoryVo : mInventoryVos) {
		number += inventoryVo.getCountStock();
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

   @Override
   public void onDestroy() {
	super.onDestroy();
	EventBusUtils.unregister(this);
   }

}
