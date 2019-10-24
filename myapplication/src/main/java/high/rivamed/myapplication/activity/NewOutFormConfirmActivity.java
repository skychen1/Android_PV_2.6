package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.Eth002Manager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.OutFormConfirmAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindBillOrderBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.PushFormDateBean;
import high.rivamed.myapplication.bean.SureReciveOrder;
import high.rivamed.myapplication.dto.entity.Inventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.RxUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.OpenDoorDialog;
import high.rivamed.myapplication.views.LoadingDialogX;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.base.App.mPushFormOrders;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_RECEIVE_ORDERID;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.service.ScanService.mDoorStatusType;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosBoxIdVo;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosRemark;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;
import static high.rivamed.myapplication.utils.LyDateUtils.setBoxVosDate;
import static high.rivamed.myapplication.utils.LyDateUtils.startScan;
import static high.rivamed.myapplication.utils.LyDateUtils.stopScan;
import static high.rivamed.myapplication.utils.ToastUtils.cancel;
import static high.rivamed.myapplication.utils.UnNetCstUtils.deleteVo;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getLocalAllCstVos;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/29 17:41
 * 描述:       请领单确认领用界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutFormConfirmActivity extends BaseSimpleActivity {

   private String TAG = "NewOutFormConfirmActivity";
   public int      mSize;
   //重新扫描
   @BindView(R.id.timely_start_btn)
   public TextView mTimelyStartBtn;
   //打开柜门
   @BindView(R.id.timely_open_door)
   public TextView mTimelyOpenDoor;
   //查看医嘱清单
   @BindView(R.id.ly_bing_btn)
   TextView mLyBingBtn;
   @BindView(R.id.timely_left)
   public TextView mTimelyLeft;
   @BindView(R.id.timely_right)
   public TextView mTimelyRight;
   @BindView(R.id.activity_down_btnll)
   LinearLayout mActivityDownBtnTwoll;
   @BindView(R.id.btn_four_ly)
   public TextView mBtnFourLy;
   @BindView(R.id.btn_four_yc)
   public TextView mBtnFourYc;
   @BindView(R.id.btn_four_tb)
   public TextView mBtnFourTb;
   @BindView(R.id.btn_four_th)
   public TextView mBtnFourTh;
   @BindView(R.id.activity_down_btn_four_ll)
   LinearLayout mActivityDownBtnFourLl;
   @BindView(R.id.activity_down_btn_one_ll)
   LinearLayout mDownBtnOneLL;
   @BindView(R.id.activity_btn_one)
   TextView     mDownBtnOne;
   @BindView(R.id.timely_name)
   TextView     mTimelyName;
   @BindView(R.id.timely_number)
   public TextView mTimelyNumber;
   @BindView(R.id.timely_ll)
   LinearLayout mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView mRecyclerview;
   @BindView(R.id.refreshLayout)
   public SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_rl_title)
   RelativeLayout mRelativeLayout;
   @BindView(R.id.timely_ll_gone)
   LinearLayout   mTimelyLlGone;
   @BindView(R.id.timely_number_left)
   TextView       mTimelyNumberLeft;
   @BindView(R.id.timely_start_btn_right)
   public TextView mTimelyStartBtnRight;
   @BindView(R.id.timely_open_door_right)
   public TextView mTimelyOpenDoorRight;
   @BindView(R.id.ly_bing_btn_right)
   TextView     mLyBingBtnRight;
   @BindView(R.id.timely_ll_gone_right)
   LinearLayout mTimelyLlGoneRight;
   @BindView(R.id.search_et)
   EditText     mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView    mSearchIvDelete;
   @BindView(R.id.stock_search)
   FrameLayout  mStockSearch;
   @BindView(R.id.ly_creat_temporary_btn)
   TextView     mLyCreatTemporaryBtn;
   @BindView(R.id.dialog_left)
   TextView     mDialogLeft;
   @BindView(R.id.dialog_right)
   public TextView mDialogRight;
   @BindView(R.id.activity_down_btn_seven_ll)
   LinearLayout   mActivityDownBtnSevenLl;
   @BindView(R.id.timely_rl)
   LinearLayout   mTimelyRl;
   @BindView(R.id.header)
   MaterialHeader mHeader;
   @BindView(R.id.public_ll)
   LinearLayout   mPublicLl;
   @BindView(R.id.tv_patient_conn)
   TextView       mTvPatientConn;
   @BindView(R.id.activity_down_patient_conn)
   LinearLayout   mActivityDownPatientConn;
   @BindView(R.id.all_out_text)
   public TextView      mAllOutText;
   public TableTypeView mTypeView;
   List<String> titeleList = null;
   private int                          mLocalAllSize;
   private String                       mEpc;
   private       LoadingDialogX.Builder mBuilder;
   /**
    * 根据EPC请求网络参数
    */
   public  FindBillOrderBean            mFindBillOrderBean;
   /**
    * 确认领用使用参数
    */
   private OrderSheetBean.RowsBean      mPrePageDate;

   /**
    * 柜子信息
    */
   List<BillStockResultBean.OrderDetailVo> mTransReceiveOrderDetailVosBean;
   private BillOrderResultBean   mBillOrderResultBean; //提交确认的参数
   private int                   mLayout;
   private View                  mHeadView;
   private OutFormConfirmAdapter mPublicAdapter;
   private LoadingDialog.Builder mLoading;

   private String                    mClossEthId;
   private RxUtils.BaseEpcObservable mObs;
   public List<InventoryVo> mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   private Handler           mHandler;
   private Runnable          mRunnable;
   private Runnable          mRunnableW;
   private boolean mResume;
   private OpenDoorDialog.Builder mBuildero;
   private int mAllSize;

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_timely_layout;
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   if (event.bing) {//绑定的按钮转换
		for (InventoryVo b : mBoxInventoryVos) {
		   if (mDoorStatusType){
			if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			    (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
			     b.getExpireStatus() == 0) ||
			    (!b.getRemark().equals("1") && b.getDeleteCount() == 0)) {
			   mDownBtnOne.setEnabled(false);
			   if (!b.getRemark().equals("1")){
				mAllOutText.setVisibility(View.VISIBLE);
				mAllOutText.setText(R.string.outform_error_string);
			   }else {
				mAllOutText.setVisibility(View.VISIBLE);
				mAllOutText.setText(R.string.epc_error_string);
			   }
			   return;
			} else {
			   mDownBtnOne.setEnabled(true);
			   mAllOutText.setVisibility(View.GONE);

			}
		   }else {
			mDownBtnOne.setEnabled(false);
			mAllOutText.setVisibility(View.VISIBLE);
			mAllOutText.setText(R.string.open_error_string);
		   }
		}
	   }
	}
   }


   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoadingX event) {
	if (event.loading) {
	   if (mBuilder == null) {
		mBuilder = DialogUtils.showRader(this);
		mBuilder.setMsg(mLocalAllSize+"");
	   } else {
		if (!mBuilder.mDialog.isShowing()) {
		   mBuilder.create().show();
		   mBuilder.setMsg(mLocalAllSize+"");
		}
	   }
	} else {
	   if (mBuilder != null) {
		mBuilder.mLoading.stop();
		mBuilder.mDialog.dismiss();
	   }
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.register(this);
	EventBusUtils.postSticky(new Event.EventLoadingX(true));
	mHandler = new Handler();
	mAllSize = getLocalAllCstVos().size();
	mLocalAllSize = mAllSize;
	Event.EventBillStock data = (Event.EventBillStock) getIntent().getExtras()
		.getSerializable("DATA");
	mClossEthId = getIntent().getStringExtra("mEthId");
	mPrePageDate = data.orderSheetBean;
	mTransReceiveOrderDetailVosBean = data.transReceiveOrderDetailVosList;
	if (mFindBillOrderBean == null) {
	   mFindBillOrderBean = new FindBillOrderBean();
	   mFindBillOrderBean.setEpcs(new ArrayList<>());
	   mFindBillOrderBean.setDeviceIds(new ArrayList<>());
	}
	setRunnable();
	initView();
	initRxJavaSearch();
   }
   @Override
   protected void onResume() {
	super.onResume();
	mResume=true;
   }

   @Override
   protected void onPause() {
	super.onPause();
	mResume=false;
   }

   private void initView() {
	startScan(mBoxInventoryVos,mObs,mClossEthId);
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mBaseTabBtnMsg.setEnabled(false);
	mDownBtnOne.setEnabled(false);


	if (mBillOrderResultBean==null){
	   mBillOrderResultBean = new BillOrderResultBean();
	   mBillOrderResultBean.setInventoryVos(new ArrayList<>());
	}
	mBaseTabTvTitle.setText("请领单领用耗材");
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" + 0 +
							"</big></font>"));
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mDownBtnOneLL.setVisibility(View.VISIBLE);
	String[] array = mContext.getResources().getStringArray(R.array.seven_form_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	//重新扫描
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	//打开柜门
	mTimelyOpenDoor.setVisibility(View.VISIBLE);
	//查看医嘱清单
	mLyBingBtn.setVisibility(View.VISIBLE);
	mTimelyStartBtn.setText("重新扫描");
	mTimelyOpenDoor.setText("打开柜门");
	mLyBingBtn.setText("查看术间请领单");

   }

   @OnClick({ R.id.timely_start_btn, R.id.activity_btn_one,
	   R.id.ly_bing_btn, R.id.timely_open_door})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   if (mDoorStatusType) {
//			mBoxInventoryVos.clear();
			mLocalAllSize = mAllSize;
			setRemoveRunnable();
			for (String deviceInventoryVo : mEthDeviceIdBack) {
			   String deviceCode = deviceInventoryVo;
			   startScan(mBoxInventoryVos, mObs, deviceCode);
			}
		   }else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_open_door:
		if (UIUtils.isFastDoubleClick(R.id.timely_open_door)) {
		   return;
		} else {
		   if (mDoorStatusType) {
			mBoxInventoryVos.clear();
			mLocalAllSize = mAllSize;
			mObs.removeVos();
			setRemoveRunnable();
			reOpenDoor();
		   }else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.activity_btn_one:
		if (UIUtils.isFastDoubleClick(R.id.activity_btn_one)) {
		   return;
		} else {
		   if (mDoorStatusType) {
			mDownBtnOne.setEnabled(false);
			sureTransReceiveOrder();
			setRemoveRunnable();
		   }else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.ly_bing_btn:
		LogUtils.i(TAG, "mTransReceiveOrderDetailVosBean   " + mGson.toJson(mTransReceiveOrderDetailVosBean));
		setRemoveRunnable();
		DialogUtils.showLookUpDetailedListDialog(mContext, true,
								     mTransReceiveOrderDetailVosBean, mPrePageDate);
		break;
	}
   }
   /**
    * 500ms进行网络请求一次RXJAVA的处理
    */
   private void initRxJavaSearch() {
	mObs = new RxUtils.BaseEpcObservable() {};
	RxUtils.getInstance().setEpcResultListener(mObs, new RxUtils.EpcDebounceResultListener() {
	   @Override
	   public void goEpcSearch(List<DeviceInventoryVo> vos) {
		mFindBillOrderBean.getDeviceIds().clear();
		mFindBillOrderBean.getEpcs().clear();
		for (DeviceInventoryVo vo : vos) {
		   if (mFindBillOrderBean.getDeviceIds().size()==0||!getVosBoxIdVo(mFindBillOrderBean.getInventoryVos(),vo.getDeviceId())){
			mFindBillOrderBean.getDeviceIds().add(vo.getDeviceId());
		   }
		   for (Inventory inventory : vo.getInventories()) {
			if (!getVosType(mFindBillOrderBean.getInventoryVos(),inventory.getEpc())){
			   mFindBillOrderBean.getEpcs().add(inventory.getEpc());
			}
		   }
		}
		if (mTitleConn) {
		   getBillStockByEpc(mFindBillOrderBean);
		}
	   }
	});
   }
   private void setTitleRightNum() {
	if (mPublicAdapter == null) {
	   setLayoutDate();
	} else {
	   mPublicAdapter.setNewData(mBoxInventoryVos);
	   setNotifyData();
	}
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : mBoxInventoryVos) {
	   if (vosBean.getCstId() != null) {
		strings.add(vosBean.getCstId());
	   }
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumber.setText(Html.fromHtml(
		"耗材种类：<font color='#262626'><big>" + list.size() +
		"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
		mBoxInventoryVos.size() + "</big></font>"));



   }
   /**
    * adapter数据的刷新
    */
   private void setNotifyData() {
	mPublicAdapter.notifyDataSetChanged();
	setHandlerToastAndFinish();
   }
   /**
    * 没有数据就跳转到主界面
    */
   private void setRunnable() {
	mRunnable = new Runnable() {
	   @Override
	   public void run() {
		if (mBoxInventoryVos.size() == 0 && mDoorStatusType && mResume) {
		   finish();
		} else {
		   mHandler.removeCallbacks(mRunnable);
		}
	   }
	};

	mRunnableW = new Runnable() {
	   @Override
	   public void run() {
		if (mBoxInventoryVos.size() == 0 && mDoorStatusType && mResume) {
		   mDownBtnOne.setEnabled(false);
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   Toast.makeText(NewOutFormConfirmActivity.this, "未扫描到操作的耗材,即将返回主界面，请重新操作",
					Toast.LENGTH_SHORT).show();
		   mHandler.postDelayed(mRunnable, 3000);
		} else {
		   setRemoveRunnable();
		}
	   }
	};
   }
   /**
    * 没有扫到数据，就弹出toast和关闭本页
    */
   private void setHandlerToastAndFinish() {
	mHandler = new Handler();
	if (mBoxInventoryVos.size() == 0 && mDoorStatusType && mResume) {
	   mHandler.postDelayed(mRunnableW, 3000);
	} else {
	   setRemoveRunnable();
	}
   }
   /**
    * 取消runnable
    */
   private void setRemoveRunnable() {
	if (mHandler != null && mRunnableW != null) {
	   mHandler.removeCallbacks(mRunnableW);
	}
	if (mHandler != null && mRunnable != null) {
	   mHandler.removeCallbacks(mRunnable);
	}
   }

   /**
    * 设置界面显示adapter
    */
   private void setLayoutDate() {
	mLayout = R.layout.item_formcon_seven_layout;
	mHeadView = mContext.getLayoutInflater()
		.inflate(R.layout.item_formcon_seven_title_layout, (ViewGroup) mLinearLayout.getParent(),
			   false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(5));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(6));
	mPublicAdapter = new OutFormConfirmAdapter(mLayout, mBoxInventoryVos);
	mHeadView.setBackgroundResource(R.color.bg_green);

	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRefreshLayout.setEnableAutoLoadMore(false);
	mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	mRecyclerview.setAdapter(mPublicAdapter);
	mLinearLayout.removeView(mHeadView);
	View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	mPublicAdapter.setEmptyView(inflate);
	mLinearLayout.addView(mHeadView);
   }

   private void getBillStockByEpc(FindBillOrderBean findBillOrderBean) {

	findBillOrderBean.setOrderId(mPrePageDate.getOrderId());
	findBillOrderBean.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	LogUtils.i(TAG, "json   " + mGson.toJson(findBillOrderBean));
	NetRequest.getInstance()
		.findBillStockByEpc(mGson.toJson(findBillOrderBean), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			EventBusUtils.postSticky(new Event.EventLoadingX(false));
			LogUtils.i(TAG, "getBillStockByEpc   " + result);
			BillOrderResultBean resultBean = mGson.fromJson(result,
												   BillOrderResultBean.class);
			setDateEpc(resultBean);
		   }

		   @Override
		   public void onError(String result) {
			super.onError(result);
			EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   }
		});
   }
   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc(BillOrderResultBean resultBean) {

	if (resultBean.getInventoryVos() != null &&
	    resultBean.getInventoryVos().size() > 0) {
	   setBoxVosDate(mBoxInventoryVos,resultBean.getInventoryVos());
	   EventBusUtils.postSticky(new Event.EventLoadingX(false));
	}
	setTitleRightNum();
   }
   private void sureTransReceiveOrder() {
	mBillOrderResultBean.setInventoryVos(mBoxInventoryVos);
	mBillOrderResultBean.setOrderId(mPrePageDate.getOrderId());
	mBillOrderResultBean.setMedicalId(mPrePageDate.getMedicalId());
	mBillOrderResultBean.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mBillOrderResultBean.setSurgeryId(mPrePageDate.getSurgeryId());
	mBillOrderResultBean.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	stopScan();
	LogUtils.i(TAG, "mBillOrderResultBean   " + mGson.toJson(mBillOrderResultBean));
	NetRequest.getInstance()
		.sureReceiveOrder(mGson.toJson(mBillOrderResultBean), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {

			LogUtils.i(TAG, "getBillStockByEpc2s   " + result);
			SureReciveOrder sureReciveOrder = mGson.fromJson(result, SureReciveOrder.class);
			SPUtils.putString(mContext, SAVE_RECEIVE_ORDERID, mPrePageDate.getOrderId());

			if (sureReciveOrder.isOperateSuccess()) {
			   MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
			   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
			   if (sureReciveOrder.getMsg().equals("") ||
				 sureReciveOrder.getMsg().contains("您已领取请领单中所有耗材")) {
				DialogUtils.showTwoDialog(mContext, mContext, 3, "耗材领用成功",
								  sureReciveOrder.getMsg());
			   } else {
				DialogUtils.showTwoDialog(mContext, mContext, 2, "耗材领用成功",
								  sureReciveOrder.getMsg());
			   }
			   UnNetCstUtils.putUnNetOperateYes( NewOutFormConfirmActivity.this);//提交离线耗材和重新获取在库耗材数据

			} else {
			   mDownBtnOne.setEnabled(true);
			   ToastUtils.showShortToast(sureReciveOrder.getMsg());
			}
			new Thread(new Runnable() {
			   @Override
			   public void run() {
				setPushFormOrderDate();
			   }
			}).start();
		   }

		   @Override
		   public void onError(String result) {
			mDownBtnOne.setEnabled(true);
		   }
		});
   }

   /**
    * 需要上传的orders
    */
   private void setPushFormOrderDate() {
	if (mPushFormOrders != null && mPushFormOrders.size() == 0) {
	   PushFormDateBean.OrdersBean ordersBean = new PushFormDateBean.OrdersBean();
	   ordersBean.setOrderId(mPrePageDate.getOrderId());
	   mPushFormOrders.add(ordersBean);
	} else if (mPushFormOrders != null && mPushFormOrders.size() != 0) {
	   for (int x=0;x<mPushFormOrders.size();x++){
		if (!mPushFormOrders.get(x).getOrderId().equals(mPrePageDate.getOrderId())) {
		   PushFormDateBean.OrdersBean ordersBean = new PushFormDateBean.OrdersBean();
		   ordersBean.setOrderId(mPrePageDate.getOrderId());
		   mPushFormOrders.add(ordersBean);
		}
	   }
	}
   }

   /**
    * EPC扫描返回数据（单个返回）
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventOneEpcDeviceCallBack event) {
	mLocalAllSize--;
	if (mLocalAllSize>0){
	   mBuilder.setMsg(mLocalAllSize+"");
	}
	mEpc = event.epc;
	mHandler.postDelayed(new Runnable() {
	   @Override
	   public void run() {
		if (mEpc.equals(event.epc)&&!event.epc.equals("-1")){
		   setTitleRightNum();
		   setNotifyData();
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   Log.i("LOGSCAN", "xxxxxxxxxxxx-   ");
		}
	   }
	},600);
	if (getVosType(mBoxInventoryVos, event.epc)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	   Iterator<InventoryVo> iterator = mBoxInventoryVos.iterator();
	   while (iterator.hasNext()) {
		InventoryVo next = iterator.next();
		if (next.getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   iterator.remove();
		   setTitleRightNum();
		   break;
		}
		if (getVosRemark(mTransReceiveOrderDetailVosBean,next.getCstId())){
		   next.setRemark("1");
		}else {
		   next.setRemark("0");
		}
	   }

	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理
	   if (event.epc==null||event.epc.equals("0")||event.epc.equals("-1")){
		setTitleRightNum();
		setNotifyData();
		EventBusUtils.post(new Event.EventButton(true,true));
	   }else {
		mObs.getScanEpc(event.deviceId, event.epc);
	   }
	}
   }

   /**
    * 重新打开柜门
    */
   private void reOpenDoor() {
	stopScan();
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   Eth002Manager.getEth002Manager().openDoor(deviceCode);
	}
   }

   /**
    * 门锁的提示
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	   mDownBtnOne.setEnabled(false);
	   mAllOutText.setVisibility(View.VISIBLE);
	   mAllOutText.setText(R.string.open_error_string);
	   if (mBuildero == null) {
		mBuildero = DialogUtils.showOpenDoorDialog(mContext, event.mString);
	   }
	}
	if (!event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   if (mBuildero != null) {
		mBuildero.mDialog.dismiss();
	   }
	   startScan(mBoxInventoryVos,mObs,event.mEthId);
	}
	if (mDoorStatusType) {
	   setTitleRightNum();
	}else {
	   mDownBtnOne.setEnabled(false);
	   mAllOutText.setVisibility(View.VISIBLE);
	   mAllOutText.setText(R.string.open_error_string);
	}
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	mBoxInventoryVos.clear();
	mFindBillOrderBean.getDeviceIds().clear();
	mFindBillOrderBean.getEpcs().clear();
	mEthDeviceIdBack.clear();
	mObs.removeVos();
	RxUtils.getInstance().unRegister();
	mHandler.removeCallbacksAndMessages(null);
	if (mBuilder != null) {
	   mBuilder.mLoading.stop();
	   if (mBuilder.mHandler!=null){
		mBuilder.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuilder.mDialog.dismiss();
	   mBuilder=null;
	}
	if (mBuildero != null) {
	   if (mBuildero.mHandler!=null){
		mBuildero.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuildero.mDialog.dismiss();
	   mBuildero=null;
	}
	cancel();
   }
}
