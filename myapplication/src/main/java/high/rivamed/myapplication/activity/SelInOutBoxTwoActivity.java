package high.rivamed.myapplication.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.net.bean.EpcInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.Eth002Manager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.entity.Inventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.service.ScanService;
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
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.UIUtils.getVosType;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getAllCstDate;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getLocalAllCstVos;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/27 19:36
 * 描述:        放入柜子的界面(在使用)
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class SelInOutBoxTwoActivity extends BaseSimpleActivity {

   private static final String TAG = "SelInOutBoxTwoActivity";
   int mType;
   private InventoryDto mTCstInventoryTwoDto;
   private InventoryDto mDtoLy = new InventoryDto();
   private int mIntentType;
   private Map<String, List<EpcInfo>> mEPCDate = new TreeMap<>();
   int k = 0;
   private LoadingDialog.Builder mLoading;
   @BindView(R.id.timely_open_door)
   TextView           mTimelyOpenDoor;
   @BindView(R.id.timely_start_btn)
   TextView           mTimelyStartBtn;
   @BindView(R.id.timely_left)
   TextView           mTimelyLeft;
   @BindView(R.id.timely_right)
   TextView           mTimelyRight;
   @BindView(R.id.all_out_text)
   TextView           mAllText;
   @BindView(R.id.activity_down_btnll)
   LinearLayout       mActivityDownBtnTwoll;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_number)
   TextView           mTimelyNumber;

   public  TableTypeView mTypeView;
   private int           mDtoOperation;
   List<String> titeleList = null;
   public int          mSize;
   public InventoryDto mInventoryDto;
   public List<InventoryVo> mInventoryVos    = new ArrayList<>(); //入柜扫描到的epc信息
   public List<InventoryVo> mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   public List<InventoryVo> mVos             = new ArrayList<>(); //在柜epc信息
   private int                       mOperation;
   private Event.EventButton         mEventButton;
   private int                       mOperationType;
   private String                    mClossEthId;
   private RxUtils.BaseEpcObservable mObs;
   private InventoryDto      mDto            = new InventoryDto();
   //   private boolean           mDoorStatusType =false;
   private ArrayList<String> mEthDevices     = new ArrayList<>();
   private List<String>      mDeviceSizeList = new ArrayList<>();
   private ArrayList<String> mListDevices;
   private Handler           mHandler;
   private Runnable          mRunnable;
   private Runnable          mRunnableW;
   public int a = 0;
   private boolean mResume;

   /**
    * 按钮的显示转换
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   mEventButton = event;
	   setButtonType(mEventButton);
	}
   }

   private void setButtonType(Event.EventButton event) {

	if (event.bing) {//绑定的按钮转换
	   for (InventoryVo b : mBoxInventoryVos) {
		ArrayList<String> strings = new ArrayList<>();
		strings.add(b.getCstCode());
		if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		    ((b.getPatientId() == null || b.getPatientId().equals("")) ||
		     (b.getPatientName() == null || b.getPatientName().equals(""))) ||
		    !ScanService.mDoorStatusType) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   setAllTextVis(ScanService.mDoorStatusType);
		   LogUtils.i(TAG, "OutBoxBingActivity   少时诵诗书 cancel");
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		}
		if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		    (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		     b.getExpireStatus() == 0) ||
		    (UIUtils.getConfigType(mContext, CONFIG_007) && b.getPatientName() == null) ||
		    !ScanService.mDoorStatusType) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   setAllTextVis(ScanService.mDoorStatusType);
		   LogUtils.i(TAG, "OutBoxBingActivity   cancel");
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		} else {
		   LogUtils.i(TAG, "OutBoxBingActivity   start");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   mAllText.setVisibility(View.GONE);

		   if (mStarts != null) {
			LogUtils.i(TAG, "OutBoxBingActivity   ssss");
			mStarts.cancel();
			mStarts.start();
		   }
		}
	   }
	} else {
	   for (InventoryVo b : mBoxInventoryVos) {
		LogUtils.i(TAG, "mOperation   cancel" + mOperation);
		if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && mOperation != 8) ||
		    (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		     b.getExpireStatus() != 0) || !ScanService.mDoorStatusType) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   setAllTextVis(ScanService.mDoorStatusType);

		   LogUtils.i(TAG, "SelInOutBoxTwoActivity   cancel");
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		} else {
		   LogUtils.i(TAG, "SelInOutBoxTwoActivity   start");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   mAllText.setVisibility(View.GONE);
		   if (mStarts != null) {
			mStarts.cancel();
			mStarts.start();
		   }
		}
	   }
	}
   }

   private void setAllTextVis(boolean type) {
	mAllText.setVisibility(View.VISIBLE);
	if (type && mOperationType == 8) {
	   mAllText.setText(R.string.op_error_tuihuo);
	} else if (type && mOperationType == 3) {
	   mAllText.setText(R.string.op_error_ly);
	} else if (type && mOperationType == 4) {
	   mAllText.setText(R.string.op_error_lyth);
	} else if (type && mOperationType == 2) {
	   mAllText.setText(R.string.op_error_rk);
	} else if (type && mOperationType == 9) {
	   mAllText.setText(R.string.op_error_yc);
	} else if (type && mOperationType == 10) {
	   mAllText.setText(R.string.op_error_yr);
	} else if (type && mOperationType == 7) {
	   mAllText.setText(R.string.op_error_th);
	} else if (!type) {
	   mAllText.setText(R.string.open_error_string);
	}
   }

   /**
    * 倒计时结束发起
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onOverEvent(Event.EventOverPut event) {
	if (event.b) {
	   LogUtils.i(TAG, "EventOverPut");
	   mIntentType = 2;//2确认并退出
	   putDateOutLogin(mIntentType);
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
	}
	if (!event.isMute) {
	   Log.i("SelSelfff", event.mEthId);
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);

	   startScan(event.mEthId);
	}
	if (ScanService.mDoorStatusType) {
	   setTitleRightNum();
	}
   }

   /**
    * dialog操作数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEvent(Event.outBoxEvent event) {
	event.dialog.dismiss();
	if (event.type.equals("x")) {//移出
	   putYcDates(event);
	} else if (event.type.equals("2")) {//退货
	   putThDates(event);
	} else {//调拨
	   putDbDates(event);
	}
	mStarts.start();
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		mLoading = DialogUtils.showLoading(this);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
		   mLoading.create().show();
		}
	   }
	} else {
	   if (mLoading != null) {
		mLoading.mAnimationDrawable.stop();
		mLoading.mDialog.dismiss();
		mLoading = null;
	   }
	}
   }

   /**
    * EPC扫描返回数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	Log.i("SelSelfff", "EventDeviceCallBack    " + event.deviceId);

   }

   /**
    * EPC扫描返回数据（单个返回）
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventOneEpcDeviceCallBack event) {
	Log.i("SelSelfff", "EventOneEpcDeviceCallBack    " + event.epc);
	if (getVosType(mBoxInventoryVos, event.epc)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		if (mBoxInventoryVos.get(i).getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   mBoxInventoryVos.remove(i);
		}
	   }
	   for (InventoryVo vo : mBoxInventoryVos) {
		if (mOperationType == 9 || mOperationType == 8 ||
		    (mOperationType == 3 && vo.getOperationStatus() != 98) || mOperationType == 4) {
		   vo.setStatus(mOperationType + "");
		   if (mOperationType == 4) {
			vo.setOperationStatus(3);
		   }
		} else {
		   if (vo.isDateNetType() && !mTitleConn) {
			vo.setIsErrorOperation(1);
		   }
		}
	   }
	   setTitleRightNum();
	   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	   setTimeStart();
	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理
	   mObs.getScanEpc(event.deviceId, event.epc);
	}
   }

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	EventBusUtils.register(this);
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_selinoutbox_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);

	EventBusUtils.post(new Event.EventLoading(true));
	if (mStarts == null) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyLeft, mTimelyRight);
	   mStarts.cancel();
	}
	mOperationType = getIntent().getIntExtra("OperationType", -3);
	mClossEthId = getIntent().getStringExtra("mEthId");

	setInBoxDate();
	initRxJavaSearch();
   }

   /**
    * 500ms进行网络请求一次RXJAVA的处理
    */
   private void initRxJavaSearch() {
	mObs = new RxUtils.BaseEpcObservable() {};
	RxUtils.getInstance().setEpcResultListener(mObs, new RxUtils.EpcDebounceResultListener() {
	   @Override
	   public void goEpcSearch(List<DeviceInventoryVo> vos) {
		Log.i("SelSelfff", "mGson.toJson(vos)   " + mGson.toJson(vos));
		mDto.setThingId(SPUtils.getString(mContext, THING_CODE));
		mDto.setOperation(mOperationType);
		mDto.setDeviceInventoryVos(vos);
		mDto.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
		if (mTitleConn) {
		   getDeviceDate(mDto);
		} else {
		   new Thread(() -> setScanDateInBoxVo(vos)).start();
		}

	   }
	});
   }

   /**
    * 断网放入的EPC将显示在界面上
    *
    * @param vos
    */
   private void setScanDateInBoxVo(List<DeviceInventoryVo> vos) {
	if (mBoxInventoryVos.size() > 0) {
	   for (int x = 0; x < vos.size(); x++) {
		String deviceId = vos.get(x).getDeviceId();
		for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		   String id = mBoxInventoryVos.get(i).getDeviceId();
		   if (id.equals(deviceId)) {
			setInventoryVoDate(vos, x);
		   }
		}
	   }
	} else {
	   for (int x = 0; x < vos.size(); x++) {
		setInventoryVoDate(vos, x);
	   }
	}
	runOnUiThread(() -> mTypeView.mInBoxAllAdapter.notifyDataSetChanged());
   }

   private void setInventoryVoDate(List<DeviceInventoryVo> vos, int x) {
	List<Inventory> list = vos.get(x).getInventories();
	for (int i = 0; i < list.size(); i++) {
	   if (!getVosType(mBoxInventoryVos, list.get(i).getEpc())) {
		InventoryVo inventoryVo = new InventoryVo();
		inventoryVo.setEpc(list.get(i).getEpc());
		inventoryVo.setDeviceId(vos.get(x).getDeviceId());
		inventoryVo.setOperationStatus(99);
		inventoryVo.setStatus("禁止放入");
		inventoryVo.setIsErrorOperation(1);
		inventoryVo.setRenewTime(getDates());
		inventoryVo.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		inventoryVo.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
		mBoxInventoryVos.add(inventoryVo);
	   }
	}
	setTitleRightNum();
   }

   private void setInBoxDate() {
	if (mOperationType == 8) {
	   mBaseTabTvTitle.setText("耗材退货");
	} else if (mOperationType == 4) {
	   mBaseTabTvTitle.setText("耗材领用/退回");
	} else if (mOperationType == 3) {
	   mBaseTabTvTitle.setText("耗材领用");
	} else if (mOperationType == 2) {
	   mBaseTabTvTitle.setText("耗材入库");
	} else if (mOperationType == 9) {
	   mBaseTabTvTitle.setText("耗材移出");
	} else if (mOperationType == 11) {
	   mBaseTabTvTitle.setText("耗材调拨");
	} else if (mOperationType == 10) {
	   mBaseTabTvTitle.setText("耗材移入");
	} else if (mOperationType == 7) {
	   mBaseTabTvTitle.setText("耗材退回");
	} else {
	   mBaseTabTvTitle.setText("耗材识别");
	}
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabBtnMsg.setEnabled(false);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mTimelyOpenDoor.setVisibility(View.VISIBLE);
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	String[] array = mContext.getResources().getStringArray(R.array.six_singbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;

	setTableTypeViewDate(mClossEthId);

   }

   /**
    * 设置界面显示的title
    */
   private void setTableTypeViewDate(String mClossEthId) {
	if (mBoxInventoryVos != null) {
	   setTitleRightNum();
	} else {
	   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
							   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							   0 + "</big></font>"));
	}
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(this, this, mBoxInventoryVos, titeleList, mSize,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   STYPE_IN, mOperationType);
	} else {
	   mTypeView.mInBoxAllAdapter.getData().clear();
	   mTypeView.mInBoxAllAdapter.getData().addAll(mBoxInventoryVos);
	   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	}
	startScan(mClossEthId);
   }

   private void setTitleRightNum() {

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
	if (StringUtils.isExceedTime(mBoxInventoryVos)) {
	   setAllTextVis(ScanService.mDoorStatusType);
	} else {
	   mAllText.setVisibility(View.GONE);
	}
	setHandlerToastAndFinish();
   }

   /**
    * 没有扫到数据，就弹出toast和关闭本页
    */
   private void setHandlerToastAndFinish() {
	mHandler = new Handler();
	if (mRunnableW == null) {
	   mRunnableW = new Runnable() {
		@Override
		public void run() {
		   if (mBoxInventoryVos.size() == 0 && ScanService.mDoorStatusType && mResume) {
			Log.i("SelSelfff", "  加来了  ");
			if (mTimelyLeft != null && mTimelyRight != null) {
			   mTimelyLeft.setEnabled(false);
			   mTimelyRight.setEnabled(false);
			   mStarts.cancel();
			   mTimelyRight.setText("确认并退出登录");
			}
			EventBusUtils.postSticky(new Event.EventLoading(false));
			Toast.makeText(SelInOutBoxTwoActivity.this, "未扫描到操作的耗材,即将返回主界面，请重新操作",
					   Toast.LENGTH_SHORT).show();
			if (mTimelyOpenDoor != null) {
			   mTimelyOpenDoor.setEnabled(false);
			   mTimelyStartBtn.setEnabled(false);
			}
			mRunnable = new Runnable() {
			   @Override
			   public void run() {
				EventBusUtils.postSticky(new Event.EventFrag("START1"));
				finish();
			   }
			};
			mHandler.postDelayed(mRunnable, 3000);
		   } else {
			if (mHandler != null && mRunnableW != null) {
			   mHandler.removeCallbacks(mRunnableW);
			   mRunnableW = null;
			}
		   }
		}
	   };
	   mHandler.postDelayed(mRunnableW, 3000);
	} else {
	   Log.i("SelSelfff", "  mRunnableW 下坡处  ");
	   mHandler.removeCallbacks(mRunnableW);
	   mRunnableW = null;
	   if (mHandler != null && mRunnable != null) {
		mHandler.removeCallbacks(mRunnable);
		mRunnable = null;
	   }
	}
   }

   @OnClick({R.id.timely_start_btn, R.id.timely_open_door, R.id.timely_left, R.id.timely_right})
   public void onViewClicked(View view) {
	super.onViewClicked(view);
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   if (ScanService.mDoorStatusType) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
			moreStartScan();
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick(R.id.timely_left)) {
		   return;
		} else {
		   //确认
		   if (ScanService.mDoorStatusType) {
			mIntentType = 1;
			if (mHandler != null && mRunnableW != null) {
			   mHandler.removeCallbacks(mRunnableW);
			   mRunnableW = null;
			}
			if (mInventoryVos != null) {
			   if (mOperationType == 9) {//移出
				setYcDate(mIntentType);
			   } else if (mOperationType == 11) {//调拨
				setDbDate(mIntentType);
			   } else if (mOperationType == 8) {//退货
				setThDate(mIntentType);
			   } else if (mOperationType == 4) {//领用退回
				setLyThDate(mIntentType);
			   } else {
				setDate(mIntentType);//其他操作
			   }
			} else {
			   ToastUtils.showShort("数据异常");
			}
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick(R.id.timely_right)) {
		   return;
		} else {
		   if (ScanService.mDoorStatusType) {
			if (mHandler != null && mRunnableW != null) {
			   mHandler.removeCallbacks(mRunnableW);
			   mRunnableW = null;
			}
			mIntentType = 2;
			if (mInventoryVos != null) {
			   putDateOutLogin(mIntentType);
			} else {
			   ToastUtils.showShort("数据异常");
			}
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_open_door:
		if (UIUtils.isFastDoubleClick(R.id.timely_open_door)) {
		   return;
		} else {
		   if (ScanService.mDoorStatusType) {
			mStarts.cancel();
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			mTimelyRight.setText("确认并退出登录");
			mBoxInventoryVos.clear();
			for (String deviceInventoryVo : mEthDeviceIdBack) {
			   String deviceCode = deviceInventoryVo;
			   Eth002Manager.getEth002Manager().openDoor(deviceCode);
			}
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	}
   }

   /**
    * 提交并退出登录
    */
   private void putDateOutLogin(int mIntentType) {
	if (mOperationType == 9) {//移出
	   setYcDate(mIntentType);
	} else if (mOperationType == 11) {//调拨
	   setDbDate(mIntentType);
	} else if (mOperationType == 8) {//退货
	   setThDate(mIntentType);
	} else if (mOperationType == 4) {//领用退回
	   setLyThDate(mIntentType);
	} else {
	   setDate(mIntentType);//其他操作
	}
   }

   /**
    * 重新扫描
    */
   private void moreStartScan() {

	mBoxInventoryVos.clear();
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   startScan(deviceCode);
	}
   }

   private void startScan(String deviceIndentify) {
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = ReaderManager.getManager().startScan(device_id, 10000);
		if (i == 2) {
		   ReaderManager.getManager().stopScan(device_id);
		   ReaderManager.getManager().startScan(device_id, 10000);
		}
		setBoxVosDate(box_id);
		if (mObs != null) {
		   mObs.removeVos();
		}

	   }
	}
   }

   /**
    * 给显示的vos赋值（区分各个柜子）
    *
    * @param box_id
    */
   private void setBoxVosDate(String box_id) {
	List<InventoryVo> cstVos = getLocalAllCstVos();
	if (cstVos.size() > 0) {
	   for (int i = cstVos.size() - 1; i >= 0; i--) {
		if (!box_id.equals(cstVos.get(i).getDeviceId())) {
		   cstVos.remove(i);
		}
	   }
	   mBoxInventoryVos.addAll(cstVos);

	   for (int i = 0; i < mBoxInventoryVos.size() - 1; i++) {
		for (int x = mBoxInventoryVos.size() - 1; x > i; x--) {
		   if (mBoxInventoryVos.get(x).getEpc().equals(mBoxInventoryVos.get(i).getEpc())) {
			mBoxInventoryVos.remove(x);
		   }
		}
	   }
	}
   }

   /**
    * 退货
    */
   private void setThDate(int mIntentType) {
	mType = 2;//1.7退货
	DialogUtils.showStoreDialog(mContext, 2, mType, null, mIntentType);
	mStarts.cancel();
	mTimelyRight.setText("确认并退出登录");
   }

   /**
    * 调拨
    */
   private void setDbDate(int mIntentType) {
	mType = 3;//1.8调拨
	String branchCode = SPUtils.getString(UIUtils.getContext(), SAVE_BRANCH_CODE);
	String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getOperateDbDialog(deptId, branchCode, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "8调拨   " + result);
		HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
		DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean, mIntentType);
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	});
   }

   /**
    * 移出
    */
   private void setYcDate(int mIntentType) {
	mType = 1;//1.6移出
	String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getHospBydept(deptId, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "库房   " + result);
		HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
		DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean, mIntentType);
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	});
   }

   /**
    * 设置提交值
    */
   private void setLyThDate(int mIntentType) {
	InventoryDto dto = new InventoryDto();
	dto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	dto.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	dto.setInventoryVos(mBoxInventoryVos);
	dto.setOperation(mOperationType);
	dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	String s = mGson.toJson(dto);
	LogUtils.i(TAG, "返回  " + s);
	if (mHandler != null && mRunnableW != null) {
	   mHandler.removeCallbacks(mRunnableW);
	   mRunnableW = null;
	}
	if (mTitleConn) {
	   NetRequest.getInstance().putOperateLyThYes(s, this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result  " + result);
		   ToastUtils.showShort("操作成功");
		   MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音
		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		   if (mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
			App.getInstance().removeALLActivity_();
		   } else {
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   }
		   UnNetCstUtils.putUnNetOperateYes(mGson,
								SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		}

		@Override
		public void onError(String result) {
		   putErrorOperateLyThYes(result, dto, mIntentType);
		}
	   });
	} else {
	   putErrorOperateLyThYes("-1", dto, mIntentType);
	}

   }

   /**
    * 确认断网存本地
    *
    * @param result
    * @param dto
    * @param mIntentType
    */
   private void putErrorOperateLyThYes(String result, InventoryDto dto, int mIntentType) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
	    mDtoOperation == 4) {
	   putErrorEpcDate(dto, mIntentType);
	}
   }

   /**
    * 设置提交值
    */
   private void setDate(int mIntentType) {
	InventoryDto dto = new InventoryDto();
	dto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	dto.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	dto.setInventoryVos(mBoxInventoryVos);
	dto.setOperation(mOperationType);
	dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	String s = mGson.toJson(dto);
	LogUtils.i(TAG, "返回  " + s);
	if (mTitleConn) {
	   NetRequest.getInstance().putOperateYes(s, this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result  " + result);
		   ToastUtils.showShort("操作成功");

		   MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音
		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		   if (mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
			App.getInstance().removeALLActivity_();
		   } else {
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   }
		   UnNetCstUtils.putUnNetOperateYes(mGson,
								SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		}

		@Override
		public void onError(String result) {
		   putErrorOperateYes(result, dto, mIntentType);

		}
	   });
	} else {
	   putErrorOperateYes("-1", dto, mIntentType);
	}

   }

   /**
    * 断网常规的本地
    *
    * @param result
    * @param dto
    * @param mIntentType
    */
   private void putErrorOperateYes(String result, InventoryDto dto, int mIntentType) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
	    mDtoOperation == 3) {
	   putErrorEpcDate(dto, mIntentType);
	}
   }

   /**
    * 断网数据成功后的处理
    *
    * @param dto
    * @param mIntentType
    */
   private void putErrorEpcDate(InventoryDto dto, int mIntentType) {
	List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	ContentValues values = new ContentValues();
	for (InventoryVo s : dto.getInventoryVos()) {
	   if (!getVosType(voList, s.getEpc())) {
		s.save();
	   } else {
		values.put("status", "3");
		values.put("operationstatus", 98);
		values.put("renewtime", getDates());
		values.put("accountid", SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		values.put("username", SPUtils.getString(mContext, KEY_USER_NAME));
		LitePal.updateAll(InventoryVo.class, values, "epc = ?", s.getEpc());
	   }
	}

	ToastUtils.showShort("操作成功");
	MusicPlayer.playSoundByOperation(mDtoOperation);//播放操作成功提示音
	if (mIntentType == 2) {
	   UIUtils.putOrderId(mContext);
	   startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
	   App.getInstance().removeALLActivity_();
	} else {
	   EventBusUtils.postSticky(new Event.EventFrag("START1"));
	}
	finish();
   }

   /**
    * 扫描后传值
    */
   private void getDeviceDate(InventoryDto Dto) {
	String toJson = mGson.toJson(Dto);
	if (mOperationType == 4) {
	   if (mTitleConn) {
		getEpcLyThDtoDate(toJson);
	   } else {
		setUnNetDate(toJson, "-1", 4);
	   }
	} else {
	   if (mTitleConn) {
		getEpcDtoDate(toJson);
	   } else {
		setUnNetDate(toJson, "-1", 3);
	   }
	}
   }

   private void getEpcLyThDtoDate(String toJson) {
	NetRequest.getInstance().putEPCLyThDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		setDateEpc(mTCstInventoryTwoDto, true);
	   }

	   @Override
	   public void onError(String result) {
		setUnNetDate(toJson, result, 4);
	   }
	});
   }

   private void getEpcDtoDate(String toJson) {
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		setDateEpc(mTCstInventoryTwoDto, true);
	   }

	   @Override
	   public void onError(String result) {
		setUnNetDate(toJson, result, 3);
	   }
	});
   }

   /**
    * 无网的扫描后的EPC信息赋值
    *
    * @param toJson
    */
   private void setUnNetDate(String toJson, String result, int type) {
	EventBusUtils.postSticky(new Event.EventLoading(false));
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
	    (mOperationType == type)) {
	   List<InventoryVo> mInVo = new ArrayList<>();
	   InventoryDto cc = LitePal.findFirst(InventoryDto.class);
	   InventoryDto inventoryDto = new InventoryDto();
	   inventoryDto.setOperation(mOperationType);
	   inventoryDto.setThingId(cc.getThingId());
	   InventoryDto dto = mGson.fromJson(toJson, InventoryDto.class);
	   if (dto.getDeviceInventoryVos().size() > 0) {
		List<Inventory> list = dto.getDeviceInventoryVos().get(0).getInventories();
		String deviceId = dto.getDeviceInventoryVos().get(0).getDeviceId();
		List<InventoryVo> vos = LitePal.where("deviceid = ? and status = ?", deviceId, "2")
			.find(InventoryVo.class);
		BoxIdBean boxIdBean = LitePal.where("device_id = ? ", deviceId)
			.findFirst(BoxIdBean.class);
		mInVo.addAll(vos);
		if (list.size() != 0) {
		   for (Inventory s : list) {
			InventoryVo first = LitePal.where("epc = ? and deviceid = ?", s.getEpc(),
								    deviceId).findFirst(InventoryVo.class);

			if (!getVosType(vos, s.getEpc())) {//无网放入
			   InventoryVo inventoryVo = new InventoryVo();
			   inventoryVo.setEpc(s.getEpc());
			   inventoryVo.setDeviceId(deviceId);
			   inventoryVo.setDeviceName(boxIdBean.getName());
			   inventoryVo.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
			   inventoryVo.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
			   inventoryVo.setIsErrorOperation(0);
			   inventoryVo.setOperationStatus(99);
			   inventoryVo.setStatus("2");
			   inventoryVo.setRenewTime(getDates());
			   mInVo.add(inventoryVo);
			} else {
			   if (first != null) {
				mInVo.remove(first);
			   }
			}
		   }
		}
	   }
	   inventoryDto.setInventoryVos(mInVo);
	   setDateEpc(inventoryDto, false);
	}
   }

   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc(InventoryDto mTCstInventoryTwoDto, boolean type) {

	if (mTCstInventoryTwoDto.getInventoryVos() != null &&
	    mTCstInventoryTwoDto.getInventoryVos().size() > 0) {
	   setBoxVosDate(mTCstInventoryTwoDto.getInventoryVos());
	   EventBusUtils.postSticky(new Event.EventLoading(false));
	}
	setTitleRightNum();
	mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	setTimeStart();
   }

   /**
    * 请求结束后的数据放入显示在界面上
    *
    * @param vos
    */
   private void setBoxVosDate(List<InventoryVo> vos) {
	if (mBoxInventoryVos.size() > 0) {
	   for (int x = 0; x < vos.size(); x++) {
		if (!getVosType(mBoxInventoryVos, vos.get(x).getEpc())) {
		   InventoryVo inventoryVo = vos.get(x);
		   inventoryVo.setDateNetType(true);
		   mBoxInventoryVos.add(inventoryVo);
		}
	   }
	} else {
	   for (int x = 0; x < vos.size(); x++) {
		InventoryVo inventoryVo = vos.get(x);
		inventoryVo.setDateNetType(true);
		mBoxInventoryVos.add(inventoryVo);
	   }
	}
   }

   /**
    * 提交移出的所有数据
    *
    * @param event
    */
   private void putYcDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	mDtoLy.setOperation(9);
	mDtoLy.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mDtoLy.setToSthId(event.context);
	List<InventoryVo> inventoryVos = new ArrayList<>();
	if (mBoxInventoryVos.size() != 0) {
	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		inventoryVos.add(mBoxInventoryVos.get(i));
	   }
	}

	mDtoLy.setInventoryVos(inventoryVos);
	mDtoLy.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "移出   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance().putOperateYes(mTCstInventoryDtoJsons, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result移出   " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.playSoundByOperation(mDtoLy.getOperation());//播放操作成功提示音
		new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		if (event.mIntentType == 2) {
		   UIUtils.putOrderId(mContext);
		   startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
		} else {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		}
		UnNetCstUtils.putUnNetOperateYes(mGson, SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		finish();
	   }

	   @Override
	   public void onError(String result) {
		ToastUtils.showShort("操作失败，请重试！");
	   }
	});
   }

   @Override
   protected void onResume() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	mResume = true;
	super.onResume();
   }

   private void setTimeStart() {
	for (InventoryVo b : mBoxInventoryVos) {
	   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && b.getExpireStatus() == 0 &&
		  mOperationType != 8) || ((mOperationType == 3 || mOperationType == 4) &&
						   UIUtils.getConfigType(mContext, CONFIG_007) &&
						   b.getPatientName() == null)) {
		if (mOperationType == 8 && b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		    b.getExpireStatus() == 0) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   if (mStarts != null) {
			mStarts.cancel();
			mStarts.start();
		   }
		} else {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		}
	   } else {
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		if (mStarts != null) {
		   mStarts.cancel();
		   mStarts.start();
		}
	   }
	}
	if (ScanService.mDoorStatusType) {
	   mTimelyOpenDoor.setEnabled(true);
	   mTimelyStartBtn.setEnabled(true);
	   mTimelyRight.setEnabled(true);
	   mTimelyLeft.setEnabled(true);
	} else {
	   mTimelyRight.setEnabled(false);
	   mTimelyLeft.setEnabled(false);
	   if (mStarts != null) {
		mStarts.cancel();
	   }
	}
   }

   @Override
   protected void onPause() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	mResume =false;
	mStarts.cancel();
	super.onPause();
   }

   /**
    * 提交退货的所有数据
    *
    * @param event
    */
   private void putThDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	mDtoLy.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mDtoLy.setOperation(8);
	mDtoLy.setRemark(event.context);
	List<InventoryVo> inventoryVos = new ArrayList<>();

	if (mBoxInventoryVos.size() != 0) {
	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		inventoryVos.add(mBoxInventoryVos.get(i));
	   }
	}
	mDtoLy.setInventoryVos(inventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mDtoLy.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "退货   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance().putOperateYes(mTCstInventoryDtoJsons, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result退货   " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.playSoundByOperation(mDtoLy.getOperation());//播放操作成功提示音
		new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		if (event.mIntentType == 2) {
		   UIUtils.putOrderId(mContext);
		   startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
		} else {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		}
		UnNetCstUtils.putUnNetOperateYes(mGson, SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		finish();
	   }

	   @Override
	   public void onError(String result) {
		ToastUtils.showShort("操作失败，请重试！");
	   }
	});
   }

   /**
    * 提交所有调拨的数据
    *
    * @param event
    */
   private void putDbDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	mDtoLy.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mDtoLy.setOperation(11);
	mDtoLy.setSthId(event.context);
	List<InventoryVo> inventoryVos = new ArrayList<>();

	if (mBoxInventoryVos.size() != 0) {
	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		inventoryVos.add(mBoxInventoryVos.get(i));
	   }
	}
	mDtoLy.setInventoryVos(inventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mDtoLy.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "调拨   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance().putOperateYes(mTCstInventoryDtoJsons, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result调拨   " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.playSoundByOperation(mDtoLy.getOperation());//播放操作成功提示音
		new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		if (event.mIntentType == 2) {
		   UIUtils.putOrderId(mContext);
		   startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
		} else {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		}
		UnNetCstUtils.putUnNetOperateYes(mGson, SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		finish();
	   }

	   @Override
	   public void onError(String result) {
		ToastUtils.showShort("操作失败，请重试！");
	   }
	});
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
	switch (ev.getAction()) {
	   //获取触摸动作，如果ACTION_UP，计时开始。
	   case MotionEvent.ACTION_UP:
		if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
		    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("") &&
		    mTimelyRight.isEnabled()) {
		   mStarts.cancel();
		   mStarts.start();

		}
		break;
	   //否则其他动作计时取消
	   default:
		mStarts.cancel();
		break;
	}
	return super.dispatchTouchEvent(ev);
   }

   /**
    * 删除数据库已有的已经操作过的耗材
    *
    * @param result
    */
   private void deleteVo(String result) {
	List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);
	List<InventoryVo> vos = inventoryDto.getInventoryVos();
	for (InventoryVo vo : vos) {
	   for (int i = 0; i < voList.size(); i++) {
		if (voList.get(i).getEpc().equals(vo.getEpc())) {
		   voList.get(i).delete();
		}
	   }
	}
	getAllCstDate(mGson, this);
   }

   @Override
   protected void onDestroy() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	EventBusUtils.unregister(this);
	mStarts.cancel();
	mStarts = null;
	mBoxInventoryVos.clear();
	super.onDestroy();
   }

}
