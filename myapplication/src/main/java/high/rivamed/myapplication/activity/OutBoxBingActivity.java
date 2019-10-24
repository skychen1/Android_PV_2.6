package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ruihua.reader.net.bean.EpcInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.Eth002Manager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVoError;
import high.rivamed.myapplication.fragment.TimelyAllFrag;
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
import high.rivamed.myapplication.views.TableTypeView;
import pl.droidsonroids.gif.GifDrawable;

import static high.rivamed.myapplication.base.App.ANIMATION_TIME;
import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_HAOCAI;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.TEMP_AFTERBIND;
import static high.rivamed.myapplication.cont.Constants.TEMP_FIRSTBIND;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.service.ScanService.mDoorStatusType;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.DevicesUtils.getDoorStatus;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType3;
import static high.rivamed.myapplication.utils.LyDateUtils.moreStartScan;
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
 * 创建时间:    2018/6/28 19:59
 * 描述:       后绑定患者  拿出  绑定病人（快速开柜需要选择框，选择操作不需要选者框）
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutBoxBingActivity extends BaseSimpleActivity {

   private static final String                     TAG                  = "OutBoxBingActivity";
   private              String                     mRvEventString;
   private              int                        mIntentType;
   private              InventoryDto               mTCstInventoryTwoDto;
   private              String                     mPatient             = "";
   private              String                     mPatientId           = "";
   private              String                     mTempPatientId       = "";
   private              String                     mOperationScheduleId = "";
   private              String                     mMedicalId           = "";
   private              String                     mSurgeryId           = "";
   private              String                     mHisPatientId        = "";
   private              boolean                    mPause               = true;
   private              Map<String, List<EpcInfo>> mEPCDate             = new TreeMap<>();
   private              int                        mAllPage             = 1;
   private              int                        mRows                = 20;
   int k = 0;

   private LoadingDialogX.Builder mBuilder;

   private       String  mIdNo                = "";
   private       String  mSurgeryTime         = "";
   private       String  mOperatingRoomNo     = "";
   private       String  mOperatingRoomNoName = "";
   private       String  mSex                 = "";
   private       String  mDeptId              = "";
   private       boolean mIsCreate            = false;
   public static boolean mOnBtnGone           = false;
   @BindView(R.id.timely_start_btn_right)
   TextView mTimelyStartBtnRight;
   @BindView(R.id.timely_open_door_right)
   TextView mTimelyOpenDoorRight;
   @BindView(R.id.timely_left)
   TextView mTimelyLeft;
   @BindView(R.id.timely_right)
   TextView mTimelyRight;

   @BindView(R.id.timely_number_left)
   TextView           mTimelyNumberLeft;
   @BindView(R.id.timely_number_text)
   TextView           mTimelyNumberText;
   @BindView(R.id.timely_ll_gone_right)
   LinearLayout       mTimelyLlGoneRight;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.ly_bing_btn_right)
   TextView           mLyBingBtnRight;
   @BindView(R.id.activity_down_btnll)
   LinearLayout       mActivityDownBtnTwoll;
   @BindView(R.id.door_closs)
   TextView           mDoorCloss;
   public InventoryDto                               mInventoryDto;
   public InventoryDto                               mPatientDto;
   //   public List<InventoryVo>                          mInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息
   public List<BingFindSchedulesBean.PatientInfoVos> patientInfos = new ArrayList<>();
   public TableTypeView                              mTypeView;
   List<String> titeleList = null;
   public  int    mSize;
   private String mBingType;

   private int    mOperationType;
   private String mClossEthId;
   private String mBindType;

   private Handler                       mHandler;
   private Runnable                      mRunnable;
   private Runnable                      mRunnableW;
   public  List<InventoryVo>             mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   private RxUtils.BaseEpcObservable     mObs;
   private InventoryDto                  mDto             = new InventoryDto();
   private InventoryVo                   mPatientVo;
   private int                           mLocalAllSize;
   private String                        mEpc;
   private OpenDoorDialog.Builder        mBuildero;
   private int                           mAllSize;
   private boolean                       mConfigType019;
   private boolean                       mConfigType009;
   private boolean                       mConfigType007;
   private List<BoxSizeBean.DevicesBean> mBoxsize;
   private boolean mScanType;

   /**
    * 门锁的提示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	Log.i("ttadrf", "event.isMute   "+event.isMute);
	if (event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	   mTimelyOpenDoorRight.setEnabled(false);
	   mTimelyStartBtnRight.setEnabled(false);
	   if (mBuildero == null) {
		mBuildero = DialogUtils.showOpenDoorDialog(mContext, event.mString);
	   }
	   setFalseEnabled(false, true);
	}
	if (!event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   startScan(mBoxInventoryVos, mObs, event.mEthId);
	   if (mBuildero != null) {
		if (mBuildero.mHandler != null) {
		   mBuildero.mHandler.removeCallbacksAndMessages(null);
		}
		mBuildero.mDialog.dismiss();
		mBuildero = null;
	   }
	}
	if (mDoorStatusType) {
	   setTitleRightNum();
	}
	setTimeStart();
   }
   /**
    * 正在扫描的回调
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onScanStartType(Event.StartScanType event) {
      if (!event.type){
	   GifDrawable gifDrawable = null;
	   try {
		gifDrawable = new GifDrawable(getResources(), R.drawable.icon_rfid_scan);
		mBaseGifImageView.setImageDrawable(gifDrawable);
	   } catch (IOException e) {
	   }
	}
	mScanType = event.type;
	Log.i(TAG,"mScanType   "+mScanType);
   }
   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventDoorStatus(Event.EventDoorV event) {
	if (mBoxsize.size() > 1) {
	   if (event.mBoolean) {//门关
		mDoorCloss.setVisibility(View.GONE);
	   }
	   if (!event.mBoolean) {//门没关
		mDoorCloss.setVisibility(View.VISIBLE);
	   }
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventBing(Event.EventCheckbox event) {
	mPatientVo = event.vo;
	getCheckBoxDate(mPatientVo);
	LogUtils.i(TAG, "EventCheckbox" + mGson.toJson(mPatientVo));
	if (mBingType != null && mBingType.equals(TEMP_AFTERBIND)) {
	   if (!TextUtils.isEmpty(mPatient)) {
		for (InventoryVo vo : mBoxInventoryVos) {
		   vo.setPatientName(mPatient);
		   vo.setCreate(mIsCreate);
		   vo.setPatientId(mPatientId);
		   vo.setTempPatientId(mTempPatientId);
		   vo.setIdNo(mIdNo);
		   vo.setOperationScheduleId(mOperationScheduleId);
		   vo.setSurgeryTime(mSurgeryTime);
		   vo.setOperatingRoomNo(mOperatingRoomNo);
		   vo.setOperatingRoomName(mOperatingRoomNoName);
		   vo.setSex(mSex);
		   vo.setDeptId(mDeptId);
		   vo.setMedicalId(mMedicalId);
		   vo.setSurgeryId(mSurgeryId);
		   vo.setHisPatientId(mHisPatientId);
		}

		if (mTypeView != null && mTypeView.mRecogHaocaiAdapter != null) {
		   setNotifyData();
		}

		for (InventoryVo b : mBoxInventoryVos) {
		   ArrayList<String> strings = new ArrayList<>();
		   strings.add(b.getCstCode());
		   if ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			 (b.getPatientName() == null || b.getPatientName().equals(""))) {
			mTimelyNumberText.setVisibility(View.VISIBLE);
			setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);
			setFalseEnabled(false, false);
			return;
		   }

		   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			 (b.getExpireStatus() == 0 && b.getDeleteCount() == 0)) {
			setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);
			mTimelyNumberText.setVisibility(View.VISIBLE);
			setFalseEnabled(false, false);
			return;
		   } else {
			LogUtils.i(TAG, "我走了falsesss");
			mTimelyNumberText.setVisibility(View.GONE);
			setFalseEnabled(true, false);
		   }
		}
	   }
	}
   }

   private void getCheckBoxDate(InventoryVo inventoryVo) {
	mPatient = inventoryVo.getPatientName();
	mPatientId = inventoryVo.getPatientId();
	mTempPatientId = inventoryVo.getTempPatientId();
	mOperationScheduleId = inventoryVo.getOperationScheduleId();
	mIdNo = inventoryVo.getIdNo();
	mSurgeryTime = inventoryVo.getSurgeryTime();
	mOperatingRoomNo = inventoryVo.getOperatingRoomNo();
	mOperatingRoomNoName = inventoryVo.getOperatingRoomName();
	mSex = inventoryVo.getSex();
	mDeptId = inventoryVo.getDeptId();
	mIsCreate = inventoryVo.isCreate();
	mBingType = inventoryVo.getBindType();
	mMedicalId = inventoryVo.getMedicalId();
	mSurgeryId = inventoryVo.getSurgeryId();
	mHisPatientId = inventoryVo.getHisPatientId();
   }

   /**
    * EPC扫描返回数据（单个返回）
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventOneEpcDeviceCallBack event) {

	if (mLocalAllSize > 0) {
	   mLocalAllSize--;
	   if (mBuilder!=null){
		mBuilder.setMsg(mLocalAllSize + "");
	   }
	}
	mEpc = event.epc;
	mHandler.postDelayed(new Runnable() {
	   @Override
	   public void run() {
		if (mEpc.equals(event.epc) && !event.epc.equals("-1")) {
		   setTitleRightNum();
		   setNotifyData();
		   setTimeStart();
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   Log.i("LOGSCAN", "xxxxxxxxxxxx-   ");
		}
	   }
	}, ANIMATION_TIME);
	if (getVosType3(mBoxInventoryVos, event.epc, mOperationType)) {//过滤不在库存的epc进行请求，拿出柜子并且有库存，本地处理
	   Iterator<InventoryVo> iterator = mBoxInventoryVos.iterator();
	   while (iterator.hasNext()) {
		InventoryVo next = iterator.next();
		if (next.getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   iterator.remove();
		   //		   setTitleRightNum();
		   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
		   break;
		} else {
		   setVosOperationType(next);
		}
	   }
	} else {//放入柜子并且无库存的逻辑走向，可能出现网络断的处理和有网络的处理
	   if (event.epc == null || event.epc.equals("0") || event.epc.equals("-1")) {
		Log.i("LOGSCAN", "最后   ");
		setTitleRightNum();
		setNotifyData();
		setTimeStart();
		EventBusUtils.postSticky(new Event.EventLoadingX(false));
		Drawable drawable = getResources().getDrawable(R.drawable.icon_rfid_normal);
		mBaseGifImageView.setImageDrawable(drawable);
	   } else {
		mObs.getScanEpc(event.deviceId, event.epc);
	   }
	}
   }

   /**
    * 给vos数据设置特定值
    *
    * @param next
    */
   private void setVosOperationType(InventoryVo next) {
	if ((mOperationType == 3 && next.getOperationStatus() != 98) || mOperationType == 4) {
	   if (next.getIsErrorOperation() != 1 ||
		 (next.getIsErrorOperation() == 1 && next.getExpireStatus() == 0)) {
		next.setStatus(mOperationType + "");
	   }
	   if (mOperationType == 4 && next.isDateNetType()) {
		next.setOperationStatus(3);
	   }
	} else {
	   if (next.isDateNetType() || !mTitleConn) {
		next.setIsErrorOperation(1);
	   }
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   setButtonType(event);
	}
   }

   private void setButtonType(Event.EventButton event) {
	if (event.bing) {//绑定的按钮转换
	   for (InventoryVo b : mBoxInventoryVos) {
		ArrayList<String> strings = new ArrayList<>();
		strings.add(b.getCstCode());
		if (!mConfigType019) {
		   if (mConfigType009 && ((b.getPatientId() == null || b.getPatientId().equals("")) ||
						  (b.getPatientName() == null ||
						   b.getPatientName().equals(""))) || !mDoorStatusType) {
			mTimelyNumberText.setVisibility(View.VISIBLE);
			setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);
			LogUtils.i(TAG, "OutBoxBingActivityfffff   cancel");
			setFalseEnabled(false, false);
			return;
		   }
		}
		if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		    (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		     b.getExpireStatus() == 0) ||
		    (mConfigType007 && !mConfigType019 && b.getPatientName() == null) ||
		    !mDoorStatusType) {
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);
		   LogUtils.i(TAG, "OutBoxBingActivity   cancel");
		   setFalseEnabled(false, false);
		   return;
		} else {
		   LogUtils.i(TAG, "OutBoxBingActivity   start");
		   mTimelyNumberText.setVisibility(View.GONE);
		   setFalseEnabled(true, false);
		}
	   }
	} else {
	   //	   if (UIUtils.getConfigType(mContext, CONFIG_019)) {
	   //		for (InventoryVo b : mBoxInventoryVos) {
	   //
	   //		   int isErrorOperation = b.getIsErrorOperation();
	   //		   int deleteCount = b.getDeleteCount();
	   //		   if ((isErrorOperation == 1 && deleteCount == 0 && b.getExpireStatus() == 0) ||
	   //			 !mDoorStatusType) {
	   //			mTimelyNumberText.setVisibility(View.VISIBLE);
	   //			setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);
	   //			Log.i(TAG, "OutBoxBingActivity   canE3333333cel");
	   //			setFalseEnabled(false, false);
	   //			return;
	   //		   } else {
	   //			Log.i(TAG, "OutBoxBingActivity   FDFDFDF");
	   //			mTimelyNumberText.setVisibility(View.GONE);
	   //			setFalseEnabled(true, false);
	   //		   }
	   //		}
	   //	   }
	}
   }

   private void setPointOutText(InventoryVo b, List<InventoryVo> InventoryVo, boolean type) {

	if ((b.getPatientName() == null || b.getPatientName().equals("")) &&
	    !StringUtils.isExceedTime(InventoryVo)) {
	   mTimelyNumberText.setText(R.string.bind_error_string);
	} else if (type) {
	   mTimelyNumberText.setText(R.string.open_error_string);
	} else if (!type && mOperationType == 4) {
	   mTimelyNumberText.setText(R.string.op_error_lyth);
	} else {
	   mTimelyNumberText.setText(R.string.op_error_ly);
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	mRvEventString = event.mString;
	mAllPage = 1;
	patientInfos.clear();
   }

   /**
    * 隐藏按钮
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onBtnGoneEvent(Event.EventButGone event) {
	mOnBtnGone = event.b;
   }

   /**
    * 倒计时结束发起
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onOverEvent(Event.EventOverPut event) {
	if (event.b && !mOnBtnGone) {
	   mIntentType = 2;//2确认并退出
	   loadBingFistDate(mIntentType);
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
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
	EventBusUtils.removeStickyEvent(Event.EventLoadingX.class);
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.register(this);
	mBaseGifImageView.setVisibility(View.VISIBLE);
	Drawable drawable = getResources().getDrawable(R.drawable.icon_rfid_normal);
	mBaseGifImageView.setImageDrawable(drawable);
	getDoorStatus();
	String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
	mBoxsize = mGson.fromJson(string,
					  new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType());
	EventBusUtils.postSticky(new Event.EventLoadingX(true));
	mConfigType019 = UIUtils.getConfigType(mContext, CONFIG_019);
	mConfigType009 = UIUtils.getConfigType(mContext, CONFIG_009);
	mConfigType007 = UIUtils.getConfigType(mContext, CONFIG_007);
	mHandler = new Handler();
	mAllSize = getLocalAllCstVos().size();
	mLocalAllSize = mAllSize;
	if (mStarts == null && !mOnBtnGone) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyLeft, mTimelyRight);
	   mStarts.cancel();
	}
	mOperationType = getIntent().getIntExtra("OperationType", -3);
	mClossEthId = getIntent().getStringExtra("mEthId");
	mBindType = getIntent().getStringExtra("bindType");
	mPatientVo = (InventoryVo) getIntent().getSerializableExtra("basePatientVo");
	Log.i("outtccc", "initDataAndEvent  OutBoxBi  " + mOperationType);
	if (mPatientVo != null) {
	   getCheckBoxDate(mPatientVo);
	}

	setRunnable();
	setAfterBing();
	initRxJavaSearch();

   }

   /**
    * 没有数据就跳转到主界面
    */
   private void setRunnable() {
	mRunnable = new Runnable() {
	   @Override
	   public void run() {
		if (mBoxInventoryVos.size() == 0 && mDoorStatusType && !mPause) {
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
		if (mBoxInventoryVos.size() == 0 && mDoorStatusType && !mPause) {
		   setFalseEnabled(false, false);
		   EventBusUtils.postSticky(new Event.EventLoadingX(false));
		   ToastUtils.showShortToast("未扫描到操作的耗材,即将返回主界面，请重新操作");
		   mHandler.postDelayed(mRunnable, 3000);
		} else {
		   setRemoveRunnable();
		}
	   }
	};
   }

   private void setRemoveRunnable() {
	if (mHandler != null && mRunnableW != null) {
	   mHandler.removeCallbacks(mRunnableW);
	}
	if (mHandler != null && mRunnable != null) {
	   mHandler.removeCallbacks(mRunnable);
	}
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_outboxbing_layout;
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
		if (mTitleConn) {
		   getDeviceDate(vos);
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
			setInventoryVoDate(mBoxInventoryVos, vos, x);
			setTitleRightNum();
		   }
		}
	   }
	} else {
	   for (int x = 0; x < vos.size(); x++) {
		setInventoryVoDate(mBoxInventoryVos, vos, x);
		setTitleRightNum();
	   }
	}
	runOnUiThread(() -> setNotifyData());
   }

   @Override
   protected void onPause() {
	if (!mOnBtnGone) {
	   Log.i("BaseSimpleActivity", "onPause  " );
	   mStarts.cancel();
	}

	setFalseEnabled(false, false);
	mPause = true;
	super.onPause();
   }

   @Override
   public void onResume() {
	mPause = false;
	if (mOnBtnGone) {
	   mTimelyStartBtnRight.setVisibility(View.GONE);
	   mTimelyOpenDoorRight.setVisibility(View.GONE);
	   mTimelyRight.setVisibility(View.GONE);
	}
	setTimeStart();
	super.onResume();

   }

   @OnClick({R.id.timely_left, R.id.timely_right, R.id.timely_start_btn_right,
	   R.id.timely_open_door_right, R.id.ly_bing_btn_right})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.timely_start_btn_right://重新扫描
		if (mDoorStatusType) {
		   if (mTimelyLeft != null && mTimelyRight != null && mStarts != null) {
			setFalseEnabled(false, false);
		   }
		   if (mConfigType009) {
			mPatient = null;
			mPatientId = null;
		   }
		   TimelyAllFrag.mPauseS = true;
		   mLocalAllSize = mAllSize;
		   setRemoveRunnable();
		   moreStartScan(mBoxInventoryVos, mObs);
		} else {
		   ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		}
		break;
	   case R.id.timely_open_door_right://重新开门
		if (mDoorStatusType) {
		   if (mStarts != null) {
			mStarts.cancel();
		   }
		   Log.i("ttadrf","timely_open_door_right   ");
		   mLocalAllSize = mAllSize;
		   mTimelyRight.setText("确认并退出登录");
		   TimelyAllFrag.mPauseS = true;
		   mBoxInventoryVos.clear();
		   mObs.removeVos();
		   stopScan();
		   if (mConfigType009) {
			mPatient = null;
			mPatientId = null;
		   }
		   setRemoveRunnable();
		   for (String deviceInventoryVo : mEthDeviceIdBack) {
			String deviceCode = deviceInventoryVo;
			Eth002Manager.getEth002Manager().openDoor(deviceCode);
		   }
		} else {
		   ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		}
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick(R.id.timely_left)) {
		   return;
		} else {
		   if (mDoorStatusType) {
			mIntentType = 1;//确认
			loadBingFistDate(mIntentType);
			setRemoveRunnable();
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick(R.id.timely_right)) {
		   return;
		} else {
		   if (mDoorStatusType) {
			mIntentType = 2;//2确认并退出
			loadBingFistDate(mIntentType);
			setRemoveRunnable();
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.ly_bing_btn_right://选择绑定患者  区分是否有临时患者
		if (UIUtils.isFastDoubleClick(R.id.ly_bing_btn_right)) {
		   return;
		} else {
		   if (mDoorStatusType) {
			setRemoveRunnable();
			if (StringUtils.isExceedTime(mBoxInventoryVos)) {
			   DialogUtils.showNoDialog(mContext, "耗材中包含异常耗材，请取出异常耗材后再进行操作！", 1, "noJump",
							    null);
			} else {
			   if (mTitleConn) {
				goToFirstBindAC(-2);
			   } else {
				setErrorBindDate("-1", -2);
			   }
			}
		   } else {
			ToastUtils.showShortToast("请关闭柜门，再进行操作！");
		   }
		}
		break;
	}
   }

   private void goToFirstBindAC(int position) {
	//获取需要绑定的患者
	NetRequest.getInstance().findSchedulesDate("", "", mAllPage, mRows, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);

		FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
		if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
		   String deptType = bean.getRows().get(0).getDeptType();
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", TEMP_AFTERBIND)
					.putExtra("mRbKey", mOperationType)
					.putExtra("GoneType", "VISIBLE"));
		   } else {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", TEMP_AFTERBIND)
					.putExtra("mRbKey", mOperationType)
					.putExtra("GoneType", "GONE"));
		   }
		} else {
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", TEMP_AFTERBIND)
					.putExtra("mRbKey", mOperationType)
					.putExtra("GoneType", "VISIBLE"));
		   } else {
			ToastUtils.showShortToast("没有患者数据");
		   }
		}
	   }

	   @Override
	   public void onError(String result) {
		setErrorBindDate(result, position);
	   }
	});
   }

   /**
    * 断网没有患者直接跳转
    *
    * @param result
    * @param position
    */
   private void setErrorBindDate(String result, int position) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1")) {
	   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
		startActivity(
			new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
				"position", position)
				.putExtra("type", TEMP_AFTERBIND)
				.putExtra("mRbKey", mOperationType)
				.putExtra("GoneType", "VISIBLE"));
	   } else {
		ToastUtils.showShortToast("请开启管理端临时患者创建");
	   }
	}
   }

   /**
    * 绑定患者用操作耗材接口
    *
    * @param mIntentType
    */
   private void loadBingFistDate(int mIntentType) {
	mTimelyLeft.setEnabled(false);
	mTimelyRight.setEnabled(false);
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	InventoryDto dto = new InventoryDto();
	dto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	dto.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	dto.setInventoryVos(mBoxInventoryVos);
	dto.setOperation(mOperationType);
	dto.setOperationScheduleId(mOperationScheduleId);
	dto.setPatientName(mPatient);
	dto.setPatientId(mPatientId);
	dto.setOperatingRoomNo(mOperatingRoomNo);
	dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	//	String toJson = mGson.toJson(dto);
	stopScan();
	if (mOnBtnGone) {
	   NetRequest.getInstance().putAllOperateYes(mGson.toJson(dto), this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result   " + result);
		   InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		   if (fromJson.isOperateSuccess()) {
			MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
			EventBusUtils.postSticky(new Event.EventFastMoreScan(true));
			UnNetCstUtils.putUnNetOperateYes(OutBoxBingActivity.this);//提交离线耗材和重新获取在库耗材数据
			finish();
		   } else {
			ToastUtils.showShortToast("数据返回异常，请与实施联系！");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
		   }
		}
	   });
	} else {
	   if (mOperationType == 4) {
		if (mTitleConn) {
		   putEpcLyThDate(mIntentType, dto);
		} else {
		   setErrorDate(dto, mIntentType, "-1", 4);
		}
	   } else {
		if (mTitleConn) {
		   putEpcDate(mIntentType, dto);
		} else {
		   setErrorDate(dto, mIntentType, "-1", 3);
		}
	   }

	}
   }

   /**
    * 绑定患者的领用数据提交
    *
    * @param mIntentType
    */
   private void putEpcDate(int mIntentType, InventoryDto dto) {
	NetRequest.getInstance().putOperateYes(mGson.toJson(dto), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);

		InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		if (fromJson.isOperateSuccess()) {
		   ToastUtils.showShortToast("操作成功");
		   MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		   if (mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			removeAllAct(OutBoxBingActivity.this);
		   }
		   if (!getSqlChangeType()) {
			getAllCstDate(this);
		   }
		   UnNetCstUtils.putUnNetOperateYes(OutBoxBingActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		} else {
		   ToastUtils.showShortToast("数据返回异常，请与实施联系！");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		}
	   }

	   @Override
	   public void onError(String result) {
		setErrorDate(dto, mIntentType, result, 3);
	   }
	});
   }

   /**
    * 确认提交领用退回
    *
    * @param mIntentType
    */
   private void putEpcLyThDate(int mIntentType, InventoryDto dto) {
	NetRequest.getInstance().putOperateLyThYes(mGson.toJson(dto), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		InventoryDto fromJson = mGson.fromJson(result, InventoryDto.class);
		if (fromJson.isOperateSuccess()) {
		   ToastUtils.showShortToast("操作成功");
		   MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);

		   new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC

		   if (mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			removeAllAct(OutBoxBingActivity.this);
		   }
		   if (!getSqlChangeType()) {
			getAllCstDate(this);
		   }
		   UnNetCstUtils.putUnNetOperateYes(OutBoxBingActivity.this);//提交离线耗材和重新获取在库耗材数据

		   finish();
		} else {
		   ToastUtils.showShortToast("数据返回异常，请与实施联系！");
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		}
	   }

	   @Override
	   public void onError(String result) {
		setErrorDate(dto, mIntentType, result, 4);
	   }
	});
   }

   /**
    * 请求失败的错误数据存储
    *
    * @param mIntentType
    */
   private void setErrorDate(InventoryDto dto, int mIntentType, String result, int type) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
	    mOperationType == type) {
	   List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	   for (InventoryVo s : dto.getInventoryVos()) {
		if (!getVosType(voList, s.getEpc())) {
		   s.save();//放入，存入库存
		   saveErrorVo(s.getEpc(), s.getDeviceId(), true, false, true);//放入，存入error流水表
		} else {
		   deleteVo(voList, s.getEpc());//拿出时，删除库存表内的该条数据
		   InventoryVoError inventory = new InventoryVoError();//拿出，存入error流水表
		   inventory.setStatus("3");
		   inventory.setOperationStatus(98);
		   inventory.setEpc(s.getEpc());
		   inventory.setDeviceId(s.getDeviceId());
		   inventory.setRenewTime(getDates());
		   inventory.setIdNo(s.getIdNo());
		   inventory.setCreate(s.isCreate());
		   inventory.setMedicalId(s.getMedicalId());
		   inventory.setOperatingRoomName(s.getOperatingRoomName());
		   inventory.setOperatingRoomNo(s.getOperatingRoomNo());
		   inventory.setPatientId(s.getPatientId());
		   inventory.setPatientName(s.getPatientName());
		   inventory.setSex(s.getSex());
		   inventory.setSurgeryTime(s.getSurgeryTime());
		   inventory.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		   inventory.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
		   inventory.save();
		}
	   }
	   ToastUtils.showShortToast("操作成功");
	   MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音

	   if (mIntentType == 2) {
		UIUtils.putOrderId(mContext);
		removeAllAct(OutBoxBingActivity.this);
	   } else {
		EventBusUtils.postSticky(new Event.EventFrag("START1"));
	   }
	   mTimelyLeft.setEnabled(true);
	   mTimelyRight.setEnabled(true);
	   finish();
	}
   }

   /**
    * 扫描后传值
    */

   private void getDeviceDate(List<DeviceInventoryVo> vos) {
	mDto.setThingId(SPUtils.getString(mContext, THING_CODE));
	mDto.setOperation(mOperationType);
	mDto.setDeviceInventoryVos(vos);
	mDto.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));

	String toJson = mGson.toJson(mDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	if (mOperationType == 4) {
	   if (mTitleConn) {
		getEpcDtoLyThDate(toJson);
	   } else {
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, "-1");
		if (dto != null) {
		   setDateEpc(dto);
		}
	   }
	} else {
	   if (mTitleConn) {
		getEpcDate(toJson);
	   } else {
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, "-1");
		if (dto != null) {
		   setDateEpc(dto);
		}
	   }
	}
   }

   /**
    * 选择操作的数据查询(领用退回)
    *
    * @param toJson
    */
   private void getEpcDtoLyThDate(String toJson) {
	NetRequest.getInstance().putEPCLyThDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result    " + result);
		InventoryDto dto = mGson.fromJson(result, InventoryDto.class);
		if (dto.isOperateSuccess()) {
		   setDateEpc(dto);
		}
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.postSticky(new Event.EventLoadingX(false));
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, result);
		if (dto != null) {
		   setDateEpc(dto);
		}
	   }
	});
   }

   /**
    * 开柜的数据查询（领用）
    *
    * @param toJson
    */
   private void getEpcDate(String toJson) {
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result    " + result);
		setDateEpc(mGson.fromJson(result, InventoryDto.class));
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.postSticky(new Event.EventLoadingX(false));
		InventoryDto dto = setUnNetDate(mContext, mGson, mOperationType, toJson, result);
		if (dto != null) {
		   setDateEpc(dto);
		}
	   }
	});
   }

   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc(InventoryDto mTCstInventoryTwoDto) {

	if (mConfigType009) {
	   mTCstInventoryTwoDto.setBindType(TEMP_AFTERBIND);
	} else {
	   mTCstInventoryTwoDto.setBindType(TEMP_FIRSTBIND);
	}
	LogUtils.i(TAG, "  11111   " + mGson.toJson(mBoxInventoryVos));
	if (mTCstInventoryTwoDto.getInventoryVos() != null &&
	    mTCstInventoryTwoDto.getInventoryVos().size() > 0) {
	   setBoxVosDate(mTCstInventoryTwoDto.getInventoryVos());
	   EventBusUtils.postSticky(new Event.EventLoadingX(false));
	}
	LogUtils.i(TAG, " 222222   " + mGson.toJson(mBoxInventoryVos));
	setTitleRightNum();
	setNotifyData();
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
		   inventoryVo.setDateNetType(false);
		   setTemPatientDate(inventoryVo);
		   mBoxInventoryVos.add(inventoryVo);
		}
	   }
	} else {
	   for (int x = 0; x < vos.size(); x++) {
		InventoryVo inventoryVo = vos.get(x);
		inventoryVo.setDateNetType(false);
		setTemPatientDate(inventoryVo);
		mBoxInventoryVos.add(inventoryVo);
	   }
	}
   }

   private void setTemPatientDate(InventoryVo inventoryVo) {
	if (mPatientVo != null) {
	   if ((UIUtils.getConfigType(mContext, CONFIG_010) &&
		  UIUtils.getConfigType(mContext, CONFIG_012)) ||
		 (mPatientId != null && mPatientId.equals("virtual"))) {
		setVoPatient(inventoryVo);
		inventoryVo.setCreate(mIsCreate);
		inventoryVo.setTempPatientId(mTempPatientId);
		inventoryVo.setIdNo(mIdNo);
		inventoryVo.setSurgeryTime(mSurgeryTime);
		inventoryVo.setOperatingRoomNo(mOperatingRoomNo);
		inventoryVo.setOperatingRoomName(mOperatingRoomNoName);
		inventoryVo.setSex(mSex);
		inventoryVo.setDeptId(mDeptId);

	   } else {
		setVoPatient(inventoryVo);
	   }
	}
	if (inventoryVo.getOperationStatus() == 7 || inventoryVo.getOperationStatus() == 99) {
	   inventoryVo.setPatientName("vr");
	   inventoryVo.setPatientId("vr");
	}
   }

   /**
    * 设置患者信息抽取后的部分
    *
    * @param inventoryVo
    */
   private void setVoPatient(InventoryVo inventoryVo) {
	inventoryVo.setPatientName(mPatient);
	inventoryVo.setPatientId(mPatientId);
	inventoryVo.setOperationScheduleId(mOperationScheduleId);
	inventoryVo.setMedicalId(mMedicalId);
	inventoryVo.setSurgeryId(mSurgeryId);
	inventoryVo.setHisPatientId(mHisPatientId);
   }

   /**
    * 绑定患者
    */
   private void setAfterBing() {
	if (mOperationType == 4) {
	   mBaseTabTvTitle.setText("耗材领用/退回");
	} else {
	   mBaseTabTvTitle.setText("耗材领用");
	}
	mTimelyNumberLeft.setVisibility(View.VISIBLE);
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabBtnMsg.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);

	if (mConfigType009) {//后绑定
	   backBind();
	} else if (UIUtils.getConfigType(mContext, CONFIG_010)) {//先绑定
	   firstBind();
	}

	String[] array = mContext.getResources().getStringArray(R.array.eight_title_bing_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	setTableTypeDate(mClossEthId);
	setTimeStart();
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
   }

   /**
    * 界面显示的数据
    *
    * @param mClossEthId
    */
   private void setTableTypeDate(String mClossEthId) {
	if (mBoxInventoryVos != null) {
	   setTitleRightNum();
	} else {
	   mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
								 "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
								 0 + "</big></font>"));
	}
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(this, this, mBoxInventoryVos, titeleList, mSize,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   ACT_TYPE_CONFIRM_HAOCAI, -10);
	} else {
	   mTypeView.mRecogHaocaiAdapter.getData().clear();
	   mTypeView.mRecogHaocaiAdapter.getData().addAll(mBoxInventoryVos);
	   setNotifyData();
	}
	startScan(mBoxInventoryVos, mObs, mClossEthId);
   }

   /**
    * adapter数据的刷新
    */
   private void setNotifyData() {
	mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
	setHandlerToastAndFinish();
   }

   private void setTitleRightNum() {
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : mBoxInventoryVos) {
	   if (mPatientVo != null) {
		vosBean.setPatientName(mPatient);
		vosBean.setCreate(mIsCreate);
		vosBean.setPatientId(mPatientId);
		vosBean.setTempPatientId(mTempPatientId);
		vosBean.setIdNo(mIdNo);
		vosBean.setOperationScheduleId(mOperationScheduleId);
		vosBean.setSurgeryTime(mSurgeryTime);
		vosBean.setOperatingRoomNo(mOperatingRoomNo);
		vosBean.setOperatingRoomName(mOperatingRoomNoName);
		vosBean.setSex(mSex);
		vosBean.setDeptId(mDeptId);
		vosBean.setMedicalId(mMedicalId);
		vosBean.setSurgeryId(mSurgeryId);
		vosBean.setHisPatientId(mHisPatientId);
	   }

	   if (vosBean.getCstId() != null) {
		strings.add(vosBean.getCstId());
	   }
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	UIUtils.runInUIThread(()->mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
												"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
												mBoxInventoryVos.size() + "</big></font>")));


   }

   /**
    * 没有扫到数据，就弹出toast和关闭本页
    */
   private void setHandlerToastAndFinish() {

	if (mBoxInventoryVos.size() == 0 && mDoorStatusType && !mPause) {
	   mHandler.postDelayed(mRunnableW, 3000);
	} else {
	   setRemoveRunnable();
	}
   }

   /**
    * 后绑定患者
    */
   private void backBind() {
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mTimelyNumberText.setVisibility(View.VISIBLE);
	mLyBingBtnRight.setVisibility(View.VISIBLE);
   }

   /**
    * 先绑定患者
    */
   private void firstBind() {
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mLyBingBtnRight.setVisibility(View.GONE);
	mTimelyNumberText.setVisibility(View.GONE);
	for (InventoryVo vosBean : mBoxInventoryVos) {
	   if ((vosBean.getIsErrorOperation() == 0 && vosBean.getExpireStatus() != 0) ||
		 (vosBean.getIsErrorOperation() == 1 && vosBean.getDeleteCount() != 0 &&
		  vosBean.getExpireStatus() == 0)) {

		mTimelyNumberText.setVisibility(View.GONE);
		setFalseEnabled(true, false);
	   } else {

		mTimelyNumberText.setVisibility(View.VISIBLE);
		setPointOutText(vosBean, mBoxInventoryVos, !mDoorStatusType);
		setFalseEnabled(false, false);
		break;
	   }
	}
   }

   private void setTimeStart() {
	for (InventoryVo b : mBoxInventoryVos) {
	   //这代码自己看的都蛋疼，优化优化优化 TODO
	   int isErrorOperation = b.getIsErrorOperation();
	   int deleteCount = b.getDeleteCount();
	   Integer expireStatus = b.getExpireStatus();
	   String patientName = b.getPatientName();
	   int operationStatus = b.getOperationStatus();
	   if ((isErrorOperation == 1 && deleteCount == 0) ||
		 (isErrorOperation == 1 && deleteCount == 0 && expireStatus == 0 &&
		  mOperationType != 8) ||
		 ((mOperationType == 3 || mOperationType == 4) && mConfigType007 &&
		  (patientName == null ||
		   ((operationStatus == 7 || operationStatus == 99) && patientName.equals("vr"))))) {
		Log.i("ffadef", "1111111111111111");
		if ((mOperationType == 8 && isErrorOperation == 1 && deleteCount == 0 &&
		     expireStatus == 0) ||
		    (isErrorOperation != 1 && deleteCount == 0 && expireStatus != 0 &&
		     ((operationStatus == 7 || operationStatus == 99) && (patientName == null ||
											    ((operationStatus == 7 ||
												operationStatus == 99) &&
											     patientName.equals(
												     "vr")))))) {
		   Log.i("ffadef", "2222222222222222");
		   mTimelyNumberText.setVisibility(View.GONE);
		   setFalseEnabled(true, false);
		} else {
		   Log.i("ffadef", "333333333333");
		   setFalseEnabled(false, true);
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   mTimelyOpenDoorRight.setEnabled(true);
		   mTimelyStartBtnRight.setEnabled(true);
		   setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);
		   return;
		}
	   } else {
		Log.i("ffadef", "444444444444444");
		if (!mDoorStatusType) {
		   Log.i("ffadef", "555555555555555555555");
		   setFalseEnabled(false, false);
		   mTimelyOpenDoorRight.setEnabled(false);
		   mTimelyStartBtnRight.setEnabled(false);
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   setPointOutText(b, mBoxInventoryVos, !mDoorStatusType);

		} else {
		   if (!mPause){
			Log.i("ffadef", "6666666666666666666666666");
			mTimelyOpenDoorRight.setEnabled(true);
			mTimelyStartBtnRight.setEnabled(true);
			setFalseEnabled(true, true);
			mTimelyNumberText.setVisibility(View.GONE);
		   }
		}

	   }
	}
	if (mBoxInventoryVos.size() == 0) {
	   Log.i("ffadef", "777777777777777777777");
	   setFalseEnabled(false, true);
	}
   }

   /**
    * 设置界面按钮状态
    *
    * @param b    true可以点击，false不可点击
    * @param type 是否控制绑定按钮转换 true有，false没有
    */
   private void setFalseEnabled(boolean b, boolean type) {
	if (mTimelyLeft != null && mTimelyRight != null) {
	   mTimelyLeft.setEnabled(b);
	   mTimelyRight.setEnabled(b);
	}
	//	if (mLyBingBtnRight != null && type) {
	//	   mLyBingBtnRight.setEnabled(b);
	//	}
	if (mStarts != null && !b) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	if (mStarts != null && b) {
	   mStarts.cancel();
	   mStarts.start();
	}
   }

   /**
    * 禁用系统返回键
    *
    * @param keyCode
    * @param event
    * @return
    */
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   return true;
	}
	return super.onKeyDown(keyCode, event);
   }

   /**
    * 分发触摸事件给所有注册了MyTouchListener的接口
    */
   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
	switch (ev.getAction()) {
	   //获取触摸动作，如果ACTION_UP，计时开始。
	   case MotionEvent.ACTION_UP:
		if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
		    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("") &&
		    mTimelyRight.isEnabled() && mTimelyRight.getVisibility() == View.VISIBLE) {
		   mStarts.cancel();
		   mStarts.start();
		}
		break;
	   //否则其他动作计时取消
	   default:
		if (mTimelyRight.isEnabled() && mTimelyRight.getVisibility() == View.VISIBLE) {
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
	Log.i("ffadef", "onStop     " );
	if (mStarts != null) {
	   mStarts.cancel();
	}
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
   }

   @Override
   protected void onDestroy() {
	mOperationType = -3;
	Log.i("outtccc", "onDestroy  OutBoxBi  " + mOperationType);
	mOnBtnGone = false;

	if (mPatientDto != null) {
	   mPatientDto = null;
	}
	cancel();
	RxUtils.getInstance().unRegister();
	mEthDeviceIdBack.clear();
	EventBusUtils.postSticky(new Event.EventFrag("START1"));
	EventBusUtils.unregister(this);
	if (mStarts != null) {
	   mStarts.cancel();
	   mStarts = null;
	}
	mObs.removeVos();
	mOnBtnGone = false;
	mHandler.removeCallbacksAndMessages(null);
	super.onDestroy();
   }
}
