package high.rivamed.myapplication.activity;

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

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.Eth002Manager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.dto.InventoryDto;
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
import high.rivamed.myapplication.views.InBoxCountDialog;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.LoadingDialogX;
import high.rivamed.myapplication.views.OpenDoorDialog;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.service.ScanService.mDoorStatusType;
import static high.rivamed.myapplication.utils.DevicesUtils.getDoorStatus;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType2;
import static high.rivamed.myapplication.utils.LyDateUtils.moreStartScan;
import static high.rivamed.myapplication.utils.LyDateUtils.setBoxVosDate;
import static high.rivamed.myapplication.utils.LyDateUtils.setInventoryVoDate;
import static high.rivamed.myapplication.utils.LyDateUtils.setUnNetDate;
import static high.rivamed.myapplication.utils.LyDateUtils.startScan;
import static high.rivamed.myapplication.utils.LyDateUtils.stopScan;
import static high.rivamed.myapplication.utils.ToastUtils.cancel;
import static high.rivamed.myapplication.utils.UIUtils.removeAllAct;
import static high.rivamed.myapplication.utils.UnNetCstUtils.deleteVo;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getAllCstDate;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getLocalAllCstVos;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getSqlChangeType;
import static high.rivamed.myapplication.utils.UnNetCstUtils.saveErrorVo;

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
   @BindView(R.id.timely_inbox_list)
   TextView           mTimelyInboxList;
   @BindView(R.id.door_closs)
   TextView           mDoorCloss;
   public  TableTypeView mTypeView;
   List<String> titeleList = null;
   public int          mSize;
   public List<InventoryVo> mInventoryVos    = new ArrayList<>(); //入柜扫描到的epc信息
   public List<InventoryVo>          mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   private Event.EventButton         mEventButton;
   private int                       mOperationType;
   private String                       mClossEthId;
   private RxUtils.BaseEpcObservable    mObs;
   private InventoryDto                 mDto            = new InventoryDto();
   private InventoryDto                 mDtoLossCounts            = new InventoryDto();
   private List<DeviceInventoryVo>                 mDeviceInventoryVos            = new ArrayList<>();
   private Handler                      mHandler;
   private Runnable                     mRunnable;
   private Runnable                     mRunnableW;
   public int                           a = 0;
   private boolean                      mResume;
   private InBoxCountDialog.Builder     mShowInBoxCountBuilder;
   private int                          mLocalAllSize;
   private       LoadingDialogX.Builder mBuilder;
   private String                       mEpc;
   private OpenDoorDialog.Builder       mBuildero;
   private int                          mAllSize;
   private boolean                      mFirstFinishLoading; //先关闭
   private boolean                      mLastFinishLoading =false;  //后关闭
   private List<BoxSizeBean.DevicesBean> mBoxsize;

   /**
    * 按钮的显示转换
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
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
		String patientId = b.getPatientId();
		String patientName = b.getPatientName();
		int deleteCount = b.getDeleteCount();
		int isErrorOperation = b.getIsErrorOperation();
		if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		    ((patientId == null || patientId.equals("")) ||
		     (patientName == null || patientName.equals(""))) ||
		    !mDoorStatusType) {
		   setFalseEnabled(false);
		   setAllTextVis(mDoorStatusType);
		   return;
		}
		if ((isErrorOperation == 1 && deleteCount == 0) ||
		    (isErrorOperation == 1 && deleteCount == 0 &&
		     b.getExpireStatus() == 0) ||
		    (UIUtils.getConfigType(mContext, CONFIG_007) && patientName == null) ||
		    !mDoorStatusType) {
		   setFalseEnabled(false);
		   setAllTextVis(mDoorStatusType);
		   return;
		} else {
		   setFalseEnabled(true);
		   mAllText.setVisibility(View.GONE);
		}
	   }
	} else {
	   for (InventoryVo b : mBoxInventoryVos) {
		int isErrorOperation = b.getIsErrorOperation();
		int deleteCount = b.getDeleteCount();
		if ((isErrorOperation == 1 && deleteCount == 0 && mOperationType != 8) ||
		    (isErrorOperation == 1 && deleteCount == 0 &&
		     b.getExpireStatus() != 0) || !ScanService.mDoorStatusType) {
		   setFalseEnabled(false);
		   setAllTextVis(ScanService.mDoorStatusType);
		   return;
		} else {
		   setFalseEnabled(true);
		   mAllText.setVisibility(View.GONE);
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
	   mIntentType = 2;//2确认并退出
	   putDateOutLogin(mIntentType);
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
	   if (mBuildero == null) {
		mBuildero = DialogUtils.showOpenDoorDialog(SelInOutBoxTwoActivity.this, event.mString);
	   }
	}
	if (!event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   if (mBuildero != null) {
		if (mBuildero.mHandler!=null){
		   mBuildero.mHandler.removeCallbacksAndMessages(null);
		}
		mBuildero.mDialog.dismiss();
		mBuildero = null;
	   }
	   startScan(mBoxInventoryVos,mObs,event.mEthId);
	}
	if (mDoorStatusType) {
	   setTitleRightNum();
	}
   }
   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventDoorStatus(Event.EventDoorV event) {
	if (mBoxsize.size()>1){
	   if (event.mBoolean) {//门关
		mDoorCloss.setVisibility(View.GONE);
	   }
	   if (!event.mBoolean) {//门没关
		mDoorCloss.setVisibility(View.VISIBLE);
	   }
	}
   }
   /**
    * dialog操作数据
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
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLoading(Event.EventLoadingX event) {
	if (event.loading) {
	   if (mBuilder == null) {
		mBuilder = DialogUtils.showRader(SelInOutBoxTwoActivity.this);
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
		if (mBuilder.mHandler!=null){
		   mBuilder.mHandler.removeCallbacksAndMessages(null);
		}
		mBuilder.mDialog.dismiss();
	   }
	}
   }
   /**
    * EPC扫描返回数据（单个返回）
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventOneEpcDeviceCallBack event) {

	if (mLocalAllSize>0){
	   mLocalAllSize--;
	   if (mBuilder!=null){
		mBuilder.setMsg(mLocalAllSize + "");
	   }
	}
	mEpc = event.epc;
	if (mOperationType==2||mOperationType==7||mOperationType==10){
	   mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
		   if (mEpc.equals(event.epc) && !event.epc.equals("-1") && mFirstFinishLoading){
			Log.i("LOGSCAN", "入柜---有返回---的动画停止---   ");
			mFirstFinishLoading =false;
			mLastFinishLoading = true;
			setTimeStart();
			EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   }
		}
	   },1000);
	}else {
	   mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
		   if (mEpc.equals(event.epc)&&!event.epc.equals("-1")){
			setTitleRightNum();
			setNotifyData();
			setTimeStart();
			EventBusUtils.postSticky(new Event.EventLoadingX(false));
			Log.i("LOGSCAN", "出柜---有耗材的动画停止-   ");
		   }
		}
	   },600);
	}
	if (getVosType2(mBoxInventoryVos, event.epc,mOperationType)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	   Iterator<InventoryVo> iterator = mBoxInventoryVos.iterator();
	   while (iterator.hasNext()){
		InventoryVo next = iterator.next();
		if (next.getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   iterator.remove();
//		   setTitleRightNum();
		   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
		   break;
		}else {
		   setVosOperationType(next);
		}
	   }
	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理

	   if (event.epc.equals("-1")) {//扫描完全结束的走向
		if (mOperationType==2||mOperationType==7||mOperationType==10){
		   Log.i("LOGSCAN", "入柜-有耗材的结束   ");
		   setTitleRightNum();
		   setNotifyData();
		   setTimeStart();
		   mFirstFinishLoading =false;
		   mLastFinishLoading = true;
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		}else {
		   setTitleRightNum();
		   setNotifyData();
		   setTimeStart();
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		}
	   }else {
	      if (event.epc == null || event.epc.equals("0")){//无耗材结束的走向
		   Log.i("LOGSCAN", "没得耗材的结束  ");
		   setTitleRightNum();
		   setNotifyData();
		   setTimeStart();
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		}else {
		   mObs.getScanEpc(event.deviceId, event.epc);
		}
	   }
	}
   }

   /**
    * 给vos数据设置特定值
    * @param next
    */
   private void setVosOperationType(InventoryVo next) {
	if (mOperationType == 9 || mOperationType == 8 ||
	    (mOperationType == 3 && next.getOperationStatus() != 98) || mOperationType == 4) {
	   if (next.getIsErrorOperation() != 1||(next.getIsErrorOperation()==1&&next.getExpireStatus()==0)) {
		next.setStatus(mOperationType + "");
	   }
	   if (mOperationType == 4) {
		next.setOperationStatus(3);
	   }
	} else {
	   if (next.isDateNetType() || !mTitleConn) {
		next.setIsErrorOperation(1);
	   }
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
//	mBuilder = DialogUtils.showRader(SelInOutBoxTwoActivity.this);
//	mBuilder.mLoading.stop();
//	mBuilder.mDialog.dismiss();
	EventBusUtils.post(new Event.EventLoadingX(true));
	getDoorStatus();
	String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	mBoxsize = mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType());
	mHandler = new Handler();
	if (mStarts == null) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyLeft, mTimelyRight);
	   mStarts.cancel();
	}
	mOperationType = getIntent().getIntExtra("OperationType", -3);
	mClossEthId = getIntent().getStringExtra("mEthId");
	Log.i("outtccc","mOperationType   "+mOperationType);
	setRunnable();
	setInBoxDate();
	initRxJavaSearch();
   }

   @Override
   public void onStart() {
	super.onStart();
	mAllSize = getLocalAllCstVos().size();
	mLocalAllSize=mAllSize;
	mFirstFinishLoading =false;
	mLastFinishLoading =false;
	Log.i("outtccc","onStart   "+mOperationType);

   }

   /**
    * 500ms进行网络请求一次RXJAVA的处理
    */
   private void initRxJavaSearch() {
	mObs = new RxUtils.BaseEpcObservable() {};
	RxUtils.getInstance().setEpcResultListener(mObs, new RxUtils.EpcDebounceResultListener() {
	   @Override
	   public void goEpcSearch(List<DeviceInventoryVo> vos) {
		mDto.setThingId(SPUtils.getString(mContext, THING_CODE));
		mDto.setOperation(mOperationType);
		mDto.setDeviceInventoryVos(vos);
		mDto.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
		if (mTitleConn) {
		   getDeviceDate(mDto);
		   if (mOperationType == 2&&mShowInBoxCountBuilder!=null&&mShowInBoxCountBuilder.mDialog.isShowing()){
			mShowInBoxCountBuilder.loadData();
		   }
		} else {
		   new Thread(() -> setScanDateInBoxVo(vos)).start();
		}

	   }
	});
   }

   /**
    * 断网放入的EPC将显示在界面上
    * @param vos
    */
   private void setScanDateInBoxVo(List<DeviceInventoryVo> vos) {
	if (mBoxInventoryVos.size() > 0) {
	   for (int x = 0; x < vos.size(); x++) {
		String deviceId = vos.get(x).getDeviceId();
		for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		   String id = mBoxInventoryVos.get(i).getDeviceId();
		   if (id.equals(deviceId)) {
			setInventoryVoDate(mBoxInventoryVos,vos, x);
			setTitleRightNum();
		   }
		}
	   }
	} else {
	   for (int x = 0; x < vos.size(); x++) {
		setInventoryVoDate(mBoxInventoryVos,vos, x);
		setTitleRightNum();
	   }
	}
	runOnUiThread(() -> setNotifyData());
   }



   private void setInBoxDate() {
	mTimelyInboxList.setVisibility(View.GONE);
	if (mOperationType == 8) {
	   mBaseTabTvTitle.setText("耗材退货");
	} else if (mOperationType == 4) {
	   mBaseTabTvTitle.setText("耗材领用/退回");
	} else if (mOperationType == 3) {
	   mBaseTabTvTitle.setText("耗材领用");
	} else if (mOperationType == 2) {
	   mBaseTabTvTitle.setText("耗材入库");
	   mTimelyInboxList.setVisibility(View.VISIBLE);
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
	String[] array = mContext.getResources().getStringArray(R.array.seven_singbox_arrays);
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
	   setNotifyData();
	}
	startScan(mBoxInventoryVos,mObs,mClossEthId);
   }
   /**
    * adapter数据的刷新
    */
   private void setNotifyData() {
	mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	setHandlerToastAndFinish();
   }

   private void setTitleRightNum() {
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : mBoxInventoryVos) {
	   if (vosBean.getCstId() != null) {
		strings.add(vosBean.getCstId());
	   }
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	UIUtils.runInUIThread(()->mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
											  "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
											  mBoxInventoryVos.size() + "</big></font>")));

	if (StringUtils.isExceedTime(mBoxInventoryVos)) {
	   setAllTextVis(ScanService.mDoorStatusType);
	} else {
	   mAllText.setVisibility(View.GONE);
	}
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
    * 没有数据就跳转到主界面
    */
   private void setRunnable() {
	mRunnable = new Runnable() {
	   @Override
	   public void run() {
		if (mBoxInventoryVos.size() == 0 && mDoorStatusType && mResume) {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
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
		   setFalseEnabled(false);
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   ToastUtils.showShortToast("未扫描到操作的耗材,即将返回主界面，请重新操作");
		   mHandler.postDelayed(mRunnable, 3000);
		} else {
		   setRemoveRunnable();
		}
	   }
	};
   }


   @OnClick({R.id.timely_start_btn, R.id.timely_open_door, R.id.timely_left, R.id.timely_right,R.id.timely_inbox_list})
   public void onViewClicked(View view) {
	super.onViewClicked(view);
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   mFirstFinishLoading =false;
		   mLastFinishLoading =false;
		   if (mDoorStatusType) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
			mBoxInventoryVos.clear();
			mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
			mLocalAllSize = mAllSize;
			Log.i("selII","mLocalAllSize   "+mLocalAllSize);
			setRemoveRunnable();
			moreStartScan(mBoxInventoryVos,mObs);
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick(R.id.timely_left)) {
		   return;
		} else {
		   //确认
		   mFirstFinishLoading =false;
		   mLastFinishLoading =false;
		   if (mDoorStatusType) {
			mIntentType = 1;
			stopScan();
			setRemoveRunnable();
			if (mInventoryVos != null) {
			   mTimelyLeft.setEnabled(false);
			   mTimelyRight.setEnabled(false);
			   if (mStarts != null) {
				mStarts.cancel();
				mTimelyRight.setText("确认并退出登录");
			   }
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
			   ToastUtils.showShortToast("数据异常");
			}
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick(R.id.timely_right)) {
		   return;
		} else {
		   mFirstFinishLoading =false;
		   mLastFinishLoading =false;
		   if (mDoorStatusType) {
			stopScan();
			setRemoveRunnable();
			mIntentType = 2;
			if (mInventoryVos != null) {
			   mTimelyLeft.setEnabled(false);
			   mTimelyRight.setEnabled(false);
			   if (mStarts != null) {
				mStarts.cancel();
				mTimelyRight.setText("确认并退出登录");
			   }
			   putDateOutLogin(mIntentType);
			} else {
			   ToastUtils.showShortToast("数据异常");
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
		   mFirstFinishLoading =false;
		   mLastFinishLoading =false;
		   if (mDoorStatusType) {
			setFalseEnabled(false);
			mBoxInventoryVos.clear();
			mObs.removeVos();
			mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
			stopScan();
			mLocalAllSize = mAllSize;
			setRemoveRunnable();
			Log.i("selII","mLocalAllSize   "+mLocalAllSize);
			for (String deviceInventoryVo : mEthDeviceIdBack) {
			   String deviceCode = deviceInventoryVo;
			   Eth002Manager.getEth002Manager().openDoor(deviceCode);
			}
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_inbox_list://查看入库统计（入库才有）
		if (!UIUtils.isFastDoubleClick(R.id.timely_inbox_list)) {
		   if (mDoorStatusType) {
			mFirstFinishLoading =false;
			mLastFinishLoading =false;
			setRemoveRunnable();
			Log.i("ttadrf","mDto   "+mGson.toJson(mDto));
			mShowInBoxCountBuilder = DialogUtils.showInBoxCountDialog(mContext, mDto,mTimelyRight);
		   }else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
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

	   @Override
	   public void onError(String result) {
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
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
	if (mTitleConn) {
	   NetRequest.getInstance().putOperateLyThYes(s, this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result  " + result);
		   InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		   if (fromJson.isOperateSuccess()) {
			ToastUtils.showShortToast("操作成功");
			MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音
			new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
			if (mIntentType == 2) {
			   UIUtils.putOrderId(mContext);
			   removeAllAct(SelInOutBoxTwoActivity.this);

			} else {
			   EventBusUtils.postSticky(new Event.EventFrag("START1"));
			}
			UnNetCstUtils.putUnNetOperateYes(SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
			if (!getSqlChangeType()) {
			   getAllCstDate(this);
			}
			finish();
		   }else {
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
		   }
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
	    mOperationType == 4) {
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
		   InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		   if (fromJson.isOperateSuccess()) {
			ToastUtils.showShortToast("操作成功");

			MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音
			new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
			if (mIntentType == 2) {
			   UIUtils.putOrderId(mContext);
			   removeAllAct(SelInOutBoxTwoActivity.this);
			} else {
			   EventBusUtils.postSticky(new Event.EventFrag("START1"));
			}
			UnNetCstUtils.putUnNetOperateYes(SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
			if (!getSqlChangeType()) {
			   getAllCstDate(this);
			}
			finish();
		   }else {
			ToastUtils.showShortToast("数据返回异常，请与实施联系！");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
		   }
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
	    mOperationType == 3) {
	   putErrorEpcDate(dto, mIntentType);
	}
   }

   /**
    * 断网数据成功后的处理
    * @param dto
    * @param mIntentType
    */
   private void putErrorEpcDate(InventoryDto dto, int mIntentType) {
	List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	for (InventoryVo s : dto.getInventoryVos()) {
	   if (!getVosType(voList, s.getEpc())) {
		s.save();//放入，存入库存
		saveErrorVo(s.getEpc(),s.getDeviceId(),true,false,true);//放入，存入error流水表
	   } else {
		deleteVo(voList,s.getEpc());//拿出时，删除库存表内的该条数据
		saveErrorVo(s.getEpc(),s.getDeviceId(),true,true,true);//拿出，存入error流水表
	   }
	}

	ToastUtils.showShortToast("操作成功");
	MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音
	if (mIntentType == 2) {
	   UIUtils.putOrderId(mContext);
	   removeAllAct(SelInOutBoxTwoActivity.this);
	} else {
	   EventBusUtils.postSticky(new Event.EventFrag("START1"));
	}
	mTimelyLeft.setEnabled(true);
	mTimelyRight.setEnabled(true);

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
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, "-1", 4);
		if (dto!=null){
		   setDateEpc(dto);
		}
	   }
	} else {
	   if (mTitleConn) {
		getEpcDtoDate(toJson);
	   } else {
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, "-1", 3);
		if (dto!=null){
		   setDateEpc(dto);
		}
	   }
	}
   }

   private void getEpcLyThDtoDate(String toJson) {
	NetRequest.getInstance().putEPCLyThDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		if (mTCstInventoryTwoDto.isOperateSuccess()){
		   setDateEpc(mTCstInventoryTwoDto);
		}
	   }

	   @Override
	   public void onError(String result) {
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, result, 4);
		if (dto!=null){
		   setDateEpc(dto);
		}
	   }
	});
   }

   private void getEpcDtoDate(String toJson) {
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		if (mTCstInventoryTwoDto.isOperateSuccess()){
		   setDateEpc(mTCstInventoryTwoDto);
		}

	   }

	   @Override
	   public void onError(String result) {
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, result, 3);
		if (dto!=null){
		   setDateEpc(dto);
		}
	   }
	});
   }



   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc(InventoryDto mTCstInventoryTwoDto) {
	Log.i("LOGSCAN", "扫描完还没关就关   ");
	mFirstFinishLoading = true;
	if (mTCstInventoryTwoDto.getInventoryVos() != null &&
	    mTCstInventoryTwoDto.getInventoryVos().size() > 0) {
	   setBoxVosDate(mBoxInventoryVos,mTCstInventoryTwoDto.getInventoryVos());
	}
	setTitleRightNum();
	setNotifyData();
	setTimeStart();
	if(mLastFinishLoading){//扫描完还没关就关
	   Log.i("LOGSCAN", "setDateEpc- EventLoadingX    ");
	   EventBusUtils.postSticky(new Event.EventLoadingX(false));
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
		InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		if (fromJson.isOperateSuccess()) {
		   ToastUtils.showShortToast("操作成功");
		   MusicPlayer.playSoundByOperation(mDtoLy.getOperation());//播放操作成功提示音
		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		   if (event.mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			removeAllAct(SelInOutBoxTwoActivity.this);
		   } else {
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   }
		   UnNetCstUtils.putUnNetOperateYes(SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		}else {
		   ToastUtils.showShortToast("数据返回异常，请与实施联系！");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		}
	   }

	   @Override
	   public void onError(String result) {
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		ToastUtils.showShortToast("操作失败，请重试！");
	   }
	});
   }

   @Override
   protected void onResume() {
	mResume = true;
	setTimeStart();
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
		   setFalseEnabled(true);
		} else {
		   setFalseEnabled(false);
		   return;
		}
	   } else {
		setFalseEnabled(true);
	   }
	}
	if (mDoorStatusType) {
	   mTimelyOpenDoor.setEnabled(true);
	   mTimelyStartBtn.setEnabled(true);
	   setFalseEnabled(true);
	} else {
	   setFalseEnabled(false);
	}
   }

   @Override
   protected void onPause() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   if (mLoading.mHandler!=null){
		mLoading.mHandler.removeCallbacksAndMessages(null);
	   }
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	mResume =false;
	if (mStarts!=null){
	   mStarts.cancel();
	}
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
		InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		if (fromJson.isOperateSuccess()) {
		   ToastUtils.showShortToast("操作成功");
		   MusicPlayer.playSoundByOperation(mDtoLy.getOperation());//播放操作成功提示音
		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		   if (event.mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			removeAllAct(SelInOutBoxTwoActivity.this);
		   } else {
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   }
		   UnNetCstUtils.putUnNetOperateYes(SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		}else {
		   ToastUtils.showShortToast("数据返回异常，请与实施联系！");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		}
	   }

	   @Override
	   public void onError(String result) {
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		ToastUtils.showShortToast("操作失败，请重试！");
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
		InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		if (fromJson.isOperateSuccess()) {
		   ToastUtils.showShortToast("操作成功");
		   MusicPlayer.playSoundByOperation(mDtoLy.getOperation());//播放操作成功提示音
		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		   if (event.mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			removeAllAct(SelInOutBoxTwoActivity.this);
		   } else {
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   }
		   UnNetCstUtils.putUnNetOperateYes(SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		}else {
		   ToastUtils.showShortToast("数据返回异常，请与实施联系！");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		}
	   }

	   @Override
	   public void onError(String result) {
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		ToastUtils.showShortToast("操作失败，请重试！");
	   }
	});
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
    * 设置界面按钮状态
    * @param b true可以点击，false不可点击
    */
   private void setFalseEnabled(boolean b) {
	if (mTimelyLeft!=null&&mTimelyRight!=null){
	   mTimelyLeft.setEnabled(b);
	   mTimelyRight.setEnabled(b);
	}
	if (mStarts != null&&!b) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	if (mBuilder!=null&&mBuilder.mDialog!=null&&!mBuilder.mDialog.isShowing()&&mStarts !=null &&b) {
	   mStarts.cancel();
	   mStarts.start();
	}
//	if (mStarts !=null &&b){
//	   mStarts.cancel();
//	   mStarts.start();
//	}
	if (mShowInBoxCountBuilder!=null&&mShowInBoxCountBuilder.mDialog.isShowing()){
	   if (mStarts != null) {
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	}
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
		if (mTimelyRight.isEnabled()){
		   if (mStarts != null) {
			mStarts.cancel();
		   }
		}
		break;
	}
	return super.dispatchTouchEvent(ev);
   }

   @Override
   protected void onStop() {
	super.onStop();
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   if (mLoading.mHandler!=null){
		mLoading.mHandler.removeCallbacksAndMessages(null);
	   }
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (mBuildero != null) {
	   if (mBuildero.mHandler!=null){
		mBuildero.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuildero.mDialog.dismiss();
	   mBuildero = null;

	}
	if (mBuilder != null) {
	   mBuilder.mLoading.stop();
	   if (mBuilder.mHandler!=null){
		mBuilder.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuilder.mDialog.dismiss();
	   mBuilder = null;
	}
	cancel();
   }

   @Override
   protected void onDestroy() {
      Log.i("outtccc",getClass().getName()+"  onDestroy");

	EventBusUtils.unregister(this);
	mStarts.cancel();
	mStarts = null;
	mBoxInventoryVos.clear();
	mEthDeviceIdBack.clear();
	mObs.removeVos();
	RxUtils.getInstance().unRegister();
	if (mBuilder != null) {
	   mBuilder.mLoading.stop();
	   mBuilder.mDialog.dismiss();
	}
	mHandler.removeCallbacksAndMessages(null);
	super.onDestroy();
   }
}
