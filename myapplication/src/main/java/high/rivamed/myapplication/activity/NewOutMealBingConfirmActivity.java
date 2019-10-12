package high.rivamed.myapplication.activity;

import android.content.Intent;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.Eth002Manager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.BillOrderAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindBillOrderBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.UseCstOrderBean;
import high.rivamed.myapplication.dto.InventoryDto;
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
import high.rivamed.myapplication.views.LoadingDialogX;
import high.rivamed.myapplication.views.OpenDoorDialog;

import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.TEMP_AFTERBIND;
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
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 15:34
 * 描述:        套餐耗材扫描操作界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutMealBingConfirmActivity extends BaseSimpleActivity {

   private String   TAG = "NewOutMealBingConfirmActivity";
   public  int      mSize;
   @BindView(R.id.timely_start_btn)
   public  TextView mTimelyStartBtn;
   @BindView(R.id.timely_open_door)
   public  TextView mTimelyOpenDoor;
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
   @BindView(R.id.ly_bind_patient)
   public TextView mBindPatient;
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
   public TextView mAllOutText;
   public String   mData;
   List<String> titeleList = null;
   public  List<BingFindSchedulesBean.PatientInfoVos> patientInfos = new ArrayList<>();
   private int                                        mLayout;
   private int                                        mTitleLayout;
   private View                                       mHeadView;
   private BillOrderAdapter                           mPublicAdapter;
   /**
    * epc扫描请求数据使用
    */
   private OrderSheetBean.RowsBean                    mPrePageDate;
   /**
    * 查看套组清单使用
    */
   private List<BillStockResultBean.OrderDetailVo>    mTransReceiveOrderDetailVos;
   /**
    * 根据EPC请求耗材列表参数
    */
   private FindBillOrderBean                          mFindBillOrderBean;
   /**
    * 查询耗材返回数据
    */
   private BillOrderResultBean                        mBillOrderResultBean;
   /**
    * 确认领用-请求参数
    */
   private UseCstOrderBean                            mUseCstOrderRequest;

   private List<BoxSizeBean.DevicesBean> mTbaseDevices;
   private Handler                       mHandler;
   private Runnable                      mRunnable;
   private Runnable                      mRunnableW;

   private LoadingDialogX.Builder    mBuilder;
   private String                    mClossEthId;
   private RxUtils.BaseEpcObservable mObs;
   public  List<InventoryVo>         mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   private boolean                   mResume;
   private OpenDoorDialog.Builder    mBuildero;
   private int                       mLocalAllSize;
   private String                    mEpc;
   private int                       mAllSize;

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.register(this);
	mAllSize = getLocalAllCstVos().size();
	mLocalAllSize = mAllSize;
	EventBusUtils.postSticky(new Event.EventLoadingX(true));
	mHandler = new Handler();
	Event.EventBillStock data = (Event.EventBillStock) getIntent().getExtras()
		.getSerializable("DATA");
	mClossEthId = getIntent().getStringExtra("mEthId");
	mPrePageDate = data.orderSheetBean;
	mTransReceiveOrderDetailVos = data.transReceiveOrderDetailVosList;
	mTbaseDevices = data.tbaseDevices;
	mFindBillOrderBean = new FindBillOrderBean();
	mFindBillOrderBean.setSuiteId(mPrePageDate.getSuiteId());
	mFindBillOrderBean.setInventoryVos(new ArrayList<>());
	mFindBillOrderBean.setDeviceIds(new ArrayList<>());
	setRunnable();
	initRxJavaSearch();
	initData();

	if (mPublicAdapter != null && mBillOrderResultBean.getInventoryVos() != null) {
	   initView();
	}

   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   if (event.bing) {//绑定的按钮转换
		for (InventoryVo b : mBoxInventoryVos) {
		   if (mDoorStatusType) {
			if (UIUtils.getConfigType(mContext, CONFIG_009) &&
			    !UIUtils.getConfigType(mContext, CONFIG_019) &&
			    ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			     (b.getPatientName() == null || b.getPatientName().equals("")))) {
			   mDownBtnOne.setEnabled(false);
			   mTimelyOpenDoor.setEnabled(true);
			   mLyBingBtn.setEnabled(true);
			   mBindPatient.setEnabled(true);
			   if (b.getPatientName() == null || b.getPatientName().equals("")) {
				mAllOutText.setVisibility(View.VISIBLE);
				mAllOutText.setText(R.string.bind_error_string);
				if (StringUtils.isExceedTime(mBoxInventoryVos)) {
				   mAllOutText.setText(R.string.op_error_newoutmeal);
				}
			   } else {
				mAllOutText.setVisibility(View.VISIBLE);
				mAllOutText.setText(R.string.op_error_newoutmeal);
			   }
			   return;
			}
			if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			    (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
			     b.getExpireStatus() == 0) ||
			    (UIUtils.getConfigType(mContext, CONFIG_007) && b.getPatientName() == null)) {
			   mDownBtnOne.setEnabled(false);
			   mTimelyOpenDoor.setEnabled(true);
			   mLyBingBtn.setEnabled(true);
			   mBindPatient.setEnabled(true);

			   if (b.getPatientName() == null || b.getPatientName().equals("")) {
				mAllOutText.setVisibility(View.VISIBLE);
				mAllOutText.setText(R.string.bind_error_string);
				if (StringUtils.isExceedTime(mBoxInventoryVos)) {
				   mAllOutText.setText(R.string.op_error_newoutmeal);
				}
			   } else {
				mAllOutText.setVisibility(View.VISIBLE);
				mAllOutText.setText(R.string.op_error_newoutmeal);
			   }

			   return;
			} else {
			   mDownBtnOne.setEnabled(true);
			   mTimelyOpenDoor.setEnabled(true);
			   mLyBingBtn.setEnabled(true);
			   mBindPatient.setEnabled(true);
			   mAllOutText.setVisibility(View.GONE);
			}
		   } else {
			mDownBtnOne.setEnabled(false);
			mTimelyOpenDoor.setEnabled(false);
			mLyBingBtn.setEnabled(false);
			mBindPatient.setEnabled(false);
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
		mBuilder.setMsg(mLocalAllSize + "");
	   } else {
		if (!mBuilder.mDialog.isShowing()) {
		   mBuilder.create().show();
		   mBuilder.setMsg(mLocalAllSize + "");
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
    * 门锁的提示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	   if (mBuildero == null) {
		mBuildero = DialogUtils.showOpenDoorDialog(mContext, event.mString);
	   }
	   mDownBtnOne.setEnabled(false);
	   mTimelyOpenDoor.setEnabled(false);
	   mLyBingBtn.setEnabled(false);
	   mBindPatient.setEnabled(false);
	   mAllOutText.setVisibility(View.VISIBLE);
	   mAllOutText.setText(R.string.open_error_string);
	}
	if (!event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   if (mBuildero != null) {
		mBuildero.mDialog.dismiss();
	   }
	   startScan(mBoxInventoryVos, mObs, event.mEthId);
	}
	if (mDoorStatusType) {
	   setTitleRightNum();
	} else {
	   mDownBtnOne.setEnabled(false);
	   mTimelyOpenDoor.setEnabled(false);
	   mLyBingBtn.setEnabled(false);
	   mBindPatient.setEnabled(false);
	   mAllOutText.setVisibility(View.VISIBLE);
	   mAllOutText.setText(R.string.open_error_string);
	}
   }

   /**
    * EPC扫描返回数据（单个返回）
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventOneEpcDeviceCallBack event) {
	mLocalAllSize--;
	if (mLocalAllSize > 0) {
	   mBuilder.setMsg(mLocalAllSize + "");
	}
	mEpc = event.epc;
	mHandler.postDelayed(new Runnable() {
	   @Override
	   public void run() {
		if (mEpc.equals(event.epc) && !event.epc.equals("-1")) {
		   setTitleRightNum();
		   setNotifyData();
		   EventBusUtils.post(new Event.EventButton(true, true));
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   Log.i("LOGSCAN", "xxxxxxxxxxxx-   ");
		}
	   }
	}, 600);
	if (getVosType(mBoxInventoryVos, event.epc)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	   Iterator<InventoryVo> iterator = mBoxInventoryVos.iterator();
	   while (iterator.hasNext()) {
		InventoryVo next = iterator.next();
		if (next.getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   iterator.remove();
		   setTitleRightNum();
		   break;
		}
		if (getVosRemark(mTransReceiveOrderDetailVos, next.getCstId())) {
		   next.setRemark("1");
		} else {
		   next.setRemark("0");
		}
	   }

	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理
	   if (event.epc == null || event.epc.equals("0") || event.epc.equals("-1")) {
		setTitleRightNum();
		setNotifyData();
		EventBusUtils.post(new Event.EventButton(true, true));
	   } else {
		mObs.getScanEpc(event.deviceId, event.epc);
	   }
	}

	//
	//      if (getVosType(mBoxInventoryVos, event.epc)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	//	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
	//		if (mBoxInventoryVos.get(i).getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
	//		   mBoxInventoryVos.remove(i);
	//		}
	//	   }
	//	   for (InventoryVo vo : mBoxInventoryVos) {
	//	      if (getVosRemark(mTransReceiveOrderDetailVos,vo.getCstId())){
	//		   vo.setRemark("1");
	//		}else {
	//		   vo.setRemark("0");
	//		}
	//	   }
	//	   setTitleRightNum();
	//	   setNotifyData();
	//	   EventBusUtils.post(new Event.EventButton(true,true));
	//	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理
	//	   if (event.epc==null||event.epc.equals("0")){
	//		setTitleRightNum();
	//		setNotifyData();
	//		EventBusUtils.post(new Event.EventButton(true,true));
	//	   }else {
	//		mObs.getScanEpc(event.deviceId, event.epc);
	//	   }
	//	}
   }

   /**
    * 500ms进行网络请求一次RXJAVA的处理
    */
   private void initRxJavaSearch() {
	mObs = new RxUtils.BaseEpcObservable() {};
	RxUtils.getInstance().setEpcResultListener(mObs, new RxUtils.EpcDebounceResultListener() {
	   @Override
	   public void goEpcSearch(List<DeviceInventoryVo> vos) {
		for (DeviceInventoryVo vo : vos) {
		   if (mFindBillOrderBean.getDeviceIds().size() == 0 ||
			 !getVosBoxIdVo(mFindBillOrderBean.getInventoryVos(), vo.getDeviceId())) {
			mFindBillOrderBean.getDeviceIds().add(vo.getDeviceId());
		   }
		   for (Inventory inventory : vo.getInventories()) {
			if (!getVosType(mFindBillOrderBean.getInventoryVos(), inventory.getEpc())) {
			   InventoryVo item = new InventoryVo();
			   item.setEpc(inventory.getEpc());
			   mFindBillOrderBean.getInventoryVos().add(item);
			}
		   }
		}
		if (mTitleConn) {
		   findBillOrder(mFindBillOrderBean);
		}
		//		else {
		//		   new Thread(() -> setScanDateInBoxVo(vos)).start();
		//		}

	   }
	});
   }

   /**
    * 数据加载
    */
   private void initData() {
	startScan(mBoxInventoryVos, mObs, mClossEthId);
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mBaseTabBtnMsg.setEnabled(false);

	if (mUseCstOrderRequest == null) {
	   mUseCstOrderRequest = new UseCstOrderBean();
	   mUseCstOrderRequest.setInventoryVos(new ArrayList<>());
	}
	mBaseTabTvTitle.setText("套组领用识别耗材");
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" + 0 +
							"</big></font>"));
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mDownBtnOneLL.setVisibility(View.VISIBLE);
	String[] array;

	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
	     UIUtils.getConfigType(mContext, CONFIG_019))) {
	   mBindPatient.setVisibility(View.VISIBLE);
	   mDownBtnOne.setEnabled(false);
	   array = mContext.getResources().getStringArray(R.array.eight_bindmeal_arrays);
	} else {
	   mBindPatient.setVisibility(View.GONE);
	   mDownBtnOne.setEnabled(true);
	   array = mContext.getResources().getStringArray(R.array.seven_nobindmeal_arrays);
	}
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
	mLyBingBtn.setText("查看套组清单");

	mTimelyOpenDoor.setEnabled(false);
	mLyBingBtn.setEnabled(false);
	mBindPatient.setEnabled(false);
   }

   @Override
   protected void onResume() {
	super.onResume();
	mResume = true;
   }

   @Override
   protected void onPause() {
	super.onPause();
	mResume = false;
   }

   @OnClick({R.id.ly_bing_btn_right, R.id.ly_bing_btn, R.id.timely_start_btn, R.id.ly_bind_patient,
	   R.id.timely_open_door, R.id.activity_btn_one})
   public void onViewClicked(View view) {
	switch (view.getId()) {

	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   if (mDoorStatusType) {
			mLocalAllSize = mAllSize;
			mBoxInventoryVos.clear();
			setRemoveRunnable();
			for (String deviceInventoryVo : mEthDeviceIdBack) {
			   String deviceCode = deviceInventoryVo;
			   startScan(mBoxInventoryVos, mObs, deviceCode);
			}
		   } else {
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
			mObs.removeVos();
			setRemoveRunnable();
			reOpenDoor();
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.ly_bing_btn:
		if (!UIUtils.isFastDoubleClick(R.id.ly_bing_btn)) {
		   LogUtils.i(TAG, "mTransReceiveOrderDetailVos   " +
					 mGson.toJson(mTransReceiveOrderDetailVos));
		   setRemoveRunnable();
		   DialogUtils.showLookUpDetailedListDialog(mContext, false,
									  mTransReceiveOrderDetailVos, mPrePageDate);

		}
		break;
	   case R.id.activity_btn_one:
		if (!UIUtils.isFastDoubleClick(R.id.activity_btn_one)) {
		   if (mDoorStatusType) {
			mDownBtnOne.setEnabled(false);
			setRemoveRunnable();
			useOrderCst();
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.ly_bind_patient:
		if (mDoorStatusType) {
		   if (StringUtils.isExceedTime(mBoxInventoryVos)) {
			DialogUtils.showNoDialog(mContext, "耗材中包含异常耗材，请取出异常耗材后再进行操作！", 1, "noJump", null);
		   } else {
			setRemoveRunnable();
			if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			   EventBusUtils.postSticky(new Event.EventButGone(true));//禁止触摸
			   Intent intent = new Intent(mContext, TemPatientBindActivity.class);
			   intent.putExtra("type", TEMP_AFTERBIND);
			   intent.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices);
			   intent.putExtra("position", -1000);
			   intent.putExtra("GoneType", "VISIBLE");
			   startActivity(intent);
			} else {
			   EventBusUtils.postSticky(new Event.EventButGone(true));//禁止触摸
			   Intent intent = new Intent(mContext, TemPatientBindActivity.class);
			   intent.putExtra("type", TEMP_AFTERBIND);
			   intent.putExtra("position", -1000);
			   intent.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices);
			   intent.putExtra("GoneType", "GONE");
			   startActivity(intent);
			   //		   loadBingDateNoTemp("", 0, mTbaseDevices);
			}
		   }
		}else {
		   ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		}
		break;
	}
   }

   private void initView() {
	String[] array;
	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
	     UIUtils.getConfigType(mContext, CONFIG_019))) {
	   array = mContext.getResources().getStringArray(R.array.eight_bindmeal_arrays);
	   mLayout = R.layout.item_formcon_eight_layout;
	   mTitleLayout = R.layout.item_formcon_eight_title_layout;
	} else {
	   array = mContext.getResources().getStringArray(R.array.seven_nobindmeal_arrays);
	   mLayout = R.layout.item_formcon_seven_layout;
	   mTitleLayout = R.layout.item_formcon_seven_title_layout;
	}
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mHeadView = mContext.getLayoutInflater()
		.inflate(mTitleLayout, (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(5));

	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||
	     UIUtils.getConfigType(mContext, CONFIG_019))) {
	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(7));
	} else {
	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(6));

	}
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.removeView(mHeadView);
	if (mPublicAdapter != null) {
	   setNotifyData();
	} else {
	   mPublicAdapter = new BillOrderAdapter(mLayout, mSize, mBoxInventoryVos);

	   mRecyclerview.addItemDecoration(
		   new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRefreshLayout.setEnableAutoLoadMore(false);
	   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mRecyclerview.setAdapter(mPublicAdapter);
	   mLinearLayout.removeAllViews();
	   View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	   mPublicAdapter.setEmptyView(inflate);
	   mLinearLayout.addView(mHeadView);
	}
   }

   /**
    * 根据EPC查询的套组耗材信息
    */
   private void findBillOrder(FindBillOrderBean mDto) {

	NetRequest.getInstance()
		.findOrderCstListByEpc(mGson.toJson(mDto), this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			EventBusUtils.postSticky(new Event.EventLoadingX(false));
			LogUtils.i(TAG, "result   " + result);
			mFindBillOrderBean.getInventoryVos().clear();
			mFindBillOrderBean.getDeviceIds().clear();
			mBillOrderResultBean = mGson.fromJson(result, BillOrderResultBean.class);

			setDateEpc(mBillOrderResultBean);
		   }

		   @Override
		   public void onError(String result) {
			super.onError(result);
			EventBusUtils.postSticky(new Event.EventLoadingX(false));
			mTimelyOpenDoor.setEnabled(true);
			mLyBingBtn.setEnabled(true);
			mBindPatient.setEnabled(true);
		   }
		});
   }

   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc(BillOrderResultBean mBillOrderResultBean) {

	if (mBillOrderResultBean.getInventoryVos() != null &&
	    mBillOrderResultBean.getInventoryVos().size() > 0) {
	   setBoxVosDate(mBoxInventoryVos, mBillOrderResultBean.getInventoryVos());
	   EventBusUtils.postSticky(new Event.EventLoadingX(false));
	}
	setTitleRightNum();
   }

   private void setTitleRightNum() {
	if (mPublicAdapter == null) {
	   initView();
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
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
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
		   mTimelyOpenDoor.setEnabled(false);
		   mLyBingBtn.setEnabled(false);
		   mBindPatient.setEnabled(false);

		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   Toast.makeText(NewOutMealBingConfirmActivity.this, "未扫描到操作的耗材,即将返回主界面，请重新操作",
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
    * 确认套组领用
    */
   private void useOrderCst() {
	mUseCstOrderRequest.getInventoryVos().clear();
	mUseCstOrderRequest.setInventoryVos(mBoxInventoryVos);
	mUseCstOrderRequest.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	if (mUseCstOrderRequest.getInventoryVos().size() == 0) {
	   ToastUtils.showShort("无耗材，无法领用");
	   mDownBtnOne.setEnabled(true);
	   return;
	}
	stopScan();

	LogUtils.i(TAG, "JSON  " + mGson.toJson(mUseCstOrderRequest));
	NetRequest.getInstance()
		.useOrderCst(mGson.toJson(mUseCstOrderRequest), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result  " + result);
			InventoryDto info = mGson.fromJson(result, InventoryDto.class);
			List<InventoryVo> inventoryVos = info.getInventoryVos();
			EventBusUtils.post(new Event.EventMealType(inventoryVos));//用来判断套组是否已经领取
			if (info.isOperateSuccess()) {
			   MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
			   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
			   ToastUtils.showShort(info.getMsg());
			   UnNetCstUtils.putUnNetOperateYes(
				   NewOutMealBingConfirmActivity.this);//提交离线耗材和重新获取在库耗材数据
			   finish();
			} else {
			   mDownBtnOne.setEnabled(true);
			   ToastUtils.showShort(info.getMsg());
			}
		   }

		   @Override
		   public void onError(String result) {
			mDownBtnOne.setEnabled(true);
		   }
		});
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvCheckBindEvent(Event.EventCheckbox event) {
	mUseCstOrderRequest.setPatientId(event.vo.getPatientId());
	for (InventoryVo item : mBoxInventoryVos) {
	   item.setPatientId(event.vo.getPatientId());
	   item.setSurgeryId(event.vo.getSurgeryId());
	   item.setMedicalId(event.vo.getMedicalId());
	   item.setPatientName(event.vo.getPatientName());

	   if ("virtual".equals(event.vo.getPatientId())) {

		item.setOperationScheduleId(event.vo.getOperationScheduleId());
		item.setOperatingRoomName(event.vo.getOperatingRoomName());
		item.setOperatingRoomNo(event.vo.getOperatingRoomNo());
		item.setIdNo(event.vo.getIdNo());
		item.setSurgeryTime(event.vo.getSurgeryTime());
		item.setSex(event.vo.getSex());
		item.setCreate(event.vo.isCreate());
		item.setTempPatientId(event.vo.getTempPatientId());
		item.setMedicalId(event.vo.getMedicalId());
	   }

	   EventBusUtils.post(new Event.EventButton(true, true));
	}
	initView();
	setNotifyData();
   }

   /**
    * 重新打开柜门
    */
   private void reOpenDoor() {
	mLocalAllSize = mAllSize;
	stopScan();
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   Eth002Manager.getEth002Manager().openDoor(deviceCode);
	}
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	mBoxInventoryVos.clear();
	mFindBillOrderBean.getInventoryVos().clear();
	mFindBillOrderBean.getDeviceIds().clear();
	mEthDeviceIdBack.clear();
	mObs.removeVos();
	RxUtils.getInstance().unRegister();
	mHandler.removeCallbacksAndMessages(null);
	if (mBuilder != null) {
	   mBuilder.mLoading.stop();
	   if (mBuilder.mHandler != null) {
		mBuilder.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuilder.mDialog.dismiss();
	   mBuilder = null;
	}
	if (mBuildero != null) {
	   if (mBuildero.mHandler != null) {
		mBuildero.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuildero.mDialog.dismiss();
	   mBuildero = null;
	}
	cancel();
   }
}