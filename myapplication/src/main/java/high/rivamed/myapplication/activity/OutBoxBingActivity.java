package high.rivamed.myapplication.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

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
import cn.rivamed.DeviceManager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.entity.Inventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.fragment.TimelyAllFrag;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_HAOCAI;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.FINISH_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack2;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.UIUtils.getVosType;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getAllCstDate;

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

   private static final String TAG = "OutBoxBingActivity";
   private String       mRvEventString;
   private int          mIntentType;
   private InventoryDto mTCstInventoryTwoDto;
   private String                     mPatient             = "";
   private String                     mPatientId           = "";
   private String                     mTempPatientId       = "";
   private String                     mOperationScheduleId = "";
   private String                     mMedicalId           = "";
   private String                     mSurgeryId           = "";
   private String                     mHisPatientId        = "";
   private boolean                    mPause               = true;
   private Map<String, List<EpcInfo>> mEPCDate             = new TreeMap<>();
   private int                        mAllPage             = 1;
   private int                        mRows                = 20;
   int k = 0;

   private LoadingDialog.Builder mLoading;
   private RvDialog.Builder      mAfterBind;
   private       String  mIdNo                = "";
   private       String  mScheduleDateTime    = "";
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
   public InventoryDto mInventoryDto;
   public InventoryDto mPatientDto;
   public List<InventoryVo>                          mInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息
   public List<BingFindSchedulesBean.PatientInfoVos> patientInfos  = new ArrayList<>();
   private int           mDtoOperation;
   public  TableTypeView mTypeView;
   List<String> titeleList = null;
   public  int    mSize;
   private String mBingType;

   /**
    * (检测没有关门)语音
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClick(Event.HomeNoClickEvent event) {
	LogUtils.i(TAG, "ssssssss");
	mIsClick = event.isClick;
	if (event.isClick) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	   mTimelyLeft.setEnabled(false);
	   mTimelyRight.setEnabled(false);
	   if (mStarts != null) {
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	} else {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	}
	EventBusUtils.post(new Event.EventButton(true, true));
	EventBusUtils.removeStickyEvent(getClass());
   }

   /**
    * 接收数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onOutBoxBingEvent(Event.EventOutBoxBingDto event) {
	if (mTypeView != null && mTypeView.mRecogHaocaiAdapter != null) {
	   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
	}
	LogUtils.i(TAG, "mTitleConn  " + mTitleConn);
	if (mInventoryDto != null && mInventoryVos != null) {
	   mInventoryDto = event.mInventoryDto;
	   List<InventoryVo> inventoryVos = event.mInventoryDto.getInventoryVos();
	   mInventoryVos.clear();
	   mInventoryVos.addAll(inventoryVos);//选择开柜
	   setAfterBing();
	} else {
	   mInventoryDto = event.mInventoryDto;
	   mInventoryVos = event.mInventoryDto.getInventoryVos();//选择开柜
	}
	mDtoOperation = mInventoryDto.getOperation();
	mPatient = mInventoryDto.getPatientName();
	mPatientId = mInventoryDto.getPatientId();
	mMedicalId = mInventoryDto.getMedicalId();
	mSurgeryId = mInventoryDto.getSurgeryId();
	mHisPatientId = mInventoryDto.getHisPatientId();
	LogUtils.i(TAG, "mHisPatientId     " + mHisPatientId);
	mPatientDto = event.mPatientDto;
	if (mPatientDto != null) {
	   mDtoOperation = mPatientDto.getOperation();
	   mPatient = mPatientDto.getPatientName();
	   mPatientId = mPatientDto.getPatientId();
	   mOperationScheduleId = mPatientDto.getOperationScheduleId();
	   mTempPatientId = mPatientDto.getTempPatientId();
	   mOperatingRoomNoName = mPatientDto.getOperatingRoomNoName();
	   mScheduleDateTime = mPatientDto.getSurgeryTime();
	   mIsCreate = mPatientDto.isCreate();
	   mIdNo = mPatientDto.getIdNo();
	   mOperatingRoomNo = mPatientDto.getOperatingRoomNo();
	   mSex = mPatientDto.getSex();
	   mDeptId = mPatientDto.getDeptId();
	   mMedicalId = mPatientDto.getMedicalId();
	   mHisPatientId = mPatientDto.getHisPatientId();
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
	   for (InventoryVo b : mInventoryVos) {
		ArrayList<String> strings = new ArrayList<>();
		strings.add(b.getCstCode());
		if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		    ((b.getPatientId() == null || b.getPatientId().equals("")) ||
		     (b.getPatientName() == null || b.getPatientName().equals(""))) || mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   setPointOutText(b, mInventoryVos, mIsClick);
		   LogUtils.i(TAG, "OutBoxBingActivity   少时诵诗书 cancel"+b.getPatientName().equals("")+(b.getPatientName() == null ));
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
		    mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   setPointOutText(b, mInventoryVos, mIsClick);
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
		   mTimelyNumberText.setVisibility(View.GONE);
		   if (mStarts != null) {
			LogUtils.i(TAG, "OutBoxBingActivity   ssss");
			mStarts.cancel();
			mStarts.start();
		   }
		}
	   }
	}
   }

   private void setPointOutText(InventoryVo b, List<InventoryVo> InventoryVo, boolean type) {

	if ((b.getPatientName() == null || b.getPatientName().equals("")) &&
	    !StringUtils.isExceedTime(InventoryVo)) {
	   mTimelyNumberText.setText(R.string.bind_error_string);
	} else if (type) {
	   mTimelyNumberText.setText(R.string.open_error_string);
	} else {
	   mTimelyNumberText.setText(R.string.op_error_ly);
	}
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
	   LogUtils.i(TAG, "EventOverPut");
	   mIntentType = 2;//2确认并退出
	   loadBingFistDate(mIntentType);
	}
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
    * 扫描后EPC准备传值
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	if (mTypeView != null && mTypeView.mRecogHaocaiAdapter != null) {
	   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
	}
	LogUtils.i(TAG, "TAG   " + mEthDeviceIdBack2.size() + "    mOnBtnGone    " + mOnBtnGone);
	if (!mOnBtnGone) {
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
				getDeviceDate(event.deviceId, mEPCDate);
			   }
			}
		   } else {
			if (!mPause) {
			   LogUtils.i(TAG, "event.epcs直接走   " + event.epcs);
			   getDeviceDate(event.deviceId, event.epcs);
			}
		   }
		}
	   }
	}

   }

   @Override
   protected void onPause() {
	if (!mOnBtnGone) {
	   mStarts.cancel();
	}
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
	super.onResume();

   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.register(this);
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (mStarts == null && !mOnBtnGone) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyLeft, mTimelyRight);
	   mStarts.cancel();
	}
	setAfterBing();

   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_outboxbing_layout;
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventBing(Event.EventCheckbox event) {
	mPatient = event.mString;
	mPatientId = event.id;
	mTempPatientId = event.mTempPatientId;
	mOperationScheduleId = event.operationScheduleId;
	mIdNo = event.idNo;
	mScheduleDateTime = event.scheduleDateTime;
	mOperatingRoomNo = event.operatingRoomNo;
	mOperatingRoomNoName = event.operatingRoomNoName;
	mSex = event.sex;
	mDeptId = event.deptId;
	mIsCreate = event.create;
	mBingType = event.type;
	mMedicalId = event.mMedicalId;
	mSurgeryId = event.mSurgeryId;
	mHisPatientId = event.mHisPatientId;
	Log.i(TAG, "mMovie  " + mPatient);
	LogUtils.i(TAG, "mOperatingRoomNoName  " + mOperatingRoomNoName);
	if (mBingType != null && mBingType.equals("afterBindTemp")) {
	   Log.i(TAG, "mMovie DDD " + mPatient);
	   if (!TextUtils.isEmpty(mPatient)) {
		for (int i = 0; i < mInventoryVos.size(); i++) {
		   mInventoryVos.get(i).setPatientName(mPatient);
		   mInventoryVos.get(i).setPatientId(mPatientId);
		   mInventoryVos.get(i).setTempPatientId(mTempPatientId);
		   mInventoryVos.get(i).setIdNo(mIdNo);
		   mInventoryVos.get(i).setOperationScheduleId(mOperationScheduleId);
		   mInventoryVos.get(i).setSurgeryTime(mScheduleDateTime);
		   mInventoryVos.get(i).setOperatingRoomNo(mOperatingRoomNo);
		   mInventoryVos.get(i).setOperatingRoomName(mOperatingRoomNoName);
		   mInventoryVos.get(i).setSex(mSex);
		   mInventoryVos.get(i).setDeptId(mDeptId);
		   mInventoryVos.get(i).setCreate(mIsCreate);
		   mInventoryVos.get(i).setMedicalId(mMedicalId);
		   mInventoryVos.get(i).setSurgeryId(mSurgeryId);
		   mInventoryVos.get(i).setHisPatientId(mHisPatientId);
		}
		if (mTypeView != null && mTypeView.mRecogHaocaiAdapter != null) {
		   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
		}

		for (InventoryVo b : mInventoryVos) {
		   ArrayList<String> strings = new ArrayList<>();
		   strings.add(b.getCstCode());
		   if ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			 (b.getPatientName() == null || b.getPatientName().equals(""))) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			mTimelyNumberText.setVisibility(View.VISIBLE);
			setPointOutText(b, mInventoryVos, mIsClick);
			if (mStarts != null) {
			   mStarts.cancel();
			}
			mTimelyRight.setText("确认并退出登录");
			return;
		   }

		   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			 (b.getExpireStatus() == 0 && b.getDeleteCount() == 0)) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			setPointOutText(b, mInventoryVos, mIsClick);
			mTimelyNumberText.setVisibility(View.VISIBLE);

			if (mStarts != null) {
			   mStarts.cancel();
			}
			mTimelyRight.setText("确认并退出登录");
			return;
		   } else {
			LogUtils.i(TAG, "我走了falsesss");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
			mTimelyNumberText.setVisibility(View.GONE);
			if (mStarts != null) {
			   mStarts.cancel();
			   mStarts.start();
			}
		   }
		}
	   }
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	mRvEventString = event.mString;
	mAllPage = 1;
	patientInfos.clear();
	//	loadBingDate(mRvEventString);
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_left, R.id.timely_right,
	   R.id.timely_start_btn_right, R.id.timely_open_door_right, R.id.ly_bing_btn_right})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(view);
		mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
		   @Override
		   public void onItemClick(int position) {
			switch (position) {
			   case 0:
				mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
				break;
			   case 1:
				mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
				break;

			}
		   }
		});
		break;
	   case R.id.base_tab_tv_outlogin:
		TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
		builder.setTwoMsg("您确认要退出登录吗?");
		builder.setMsg("温馨提示");
		builder.setLeft("取消", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int i) {
			dialog.dismiss();
		   }
		});
		builder.setRight("确认", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int i) {
			mContext.startActivity(new Intent(mContext, LoginActivity.class));
			App.getInstance().removeALLActivity_();
			dialog.dismiss();
			MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
		   }
		});
		builder.create().show();
		break;
	   case R.id.base_tab_btn_msg:
		mContext.startActivity(new Intent(this, MessageActivity.class));

		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.timely_start_btn_right://重新扫描
		if (mStarts != null) {
		   mStarts.cancel();
		}
		mTimelyRight.setText("确认并退出登录");
		moreStartScan();
		break;
	   case R.id.timely_open_door_right://重新开门
		if (!mIsClick) {
		   if (mStarts != null) {
			mStarts.cancel();
		   }
		   mTimelyRight.setText("确认并退出登录");
		   List<DeviceInventoryVo> deviceInventoryVoss = mInventoryDto.getDeviceInventoryVos();
		   mInventoryDto.getInventoryVos().clear();
		   if (deviceInventoryVoss != null) {
			deviceInventoryVoss.clear();
		   }
		   TimelyAllFrag.mPauseS = true;
		   if (UIUtils.getConfigType(mContext, CONFIG_009)) {
			mPatient = null;
			mPatientId = null;
		   }
		   //		   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
		   for (String deviceInventoryVo : mEthDeviceIdBack) {
			String deviceCode = deviceInventoryVo;
			LogUtils.i(TAG, "deviceCode    " + deviceCode);
			DeviceManager.getInstance().OpenDoor(deviceCode);
		   }
		} else {
		   ToastUtils.showShort("请关闭柜门，再进行操作！");
		}
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick(R.id.timely_left)) {
		   return;
		} else {
		   if (!mIsClick) {
			mIntentType = 1;//确认
			loadBingFistDate(mIntentType);
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick(R.id.timely_right)) {
		   return;
		} else {
		   if (!mIsClick) {
			mIntentType = 2;//2确认并退出
			loadBingFistDate(mIntentType);
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.ly_bing_btn_right://选择绑定患者  区分是否有临时患者
		if (UIUtils.isFastDoubleClick(R.id.ly_bing_btn_right)) {
		   return;
		} else {
		   if (!mIsClick) {
			if (StringUtils.isExceedTime(mInventoryVos)) {
			   DialogUtils.showNoDialog(mContext, "耗材中包含异常耗材，请取出异常耗材后再进行操作！", 1, "noJump", null);
			} else {
			   if (mTitleConn){
				goToFirstBindAC(-2);
			   }else {
				setErrorBindDate("-1", -2);
			   }
			}
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	}
   }

   private void moreStartScan() {
	mEPCDate.clear();
//	mTimelyLeft.setEnabled(true);
//	mTimelyRight.setEnabled(true);
	if (UIUtils.getConfigType(mContext, CONFIG_009)) {
	   mPatient = null;
	   mPatientId = null;
	}

	List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
	mInventoryDto.getInventoryVos().clear();
	if (deviceInventoryVos != null) {
	   deviceInventoryVos.clear();
	}
	//	mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
	TimelyAllFrag.mPauseS = true;
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   startScan(deviceCode);
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
					.putExtra("type", "afterBindTemp")
					.putExtra("mRbKey", mDtoOperation)
					.putExtra("GoneType", "VISIBLE"));
		   } else {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", "afterBindTemp")
					.putExtra("mRbKey", mDtoOperation)
					.putExtra("GoneType", "GONE"));
		   }
		} else {
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", "afterBindTemp")
					.putExtra("mRbKey", mDtoOperation)
					.putExtra("GoneType", "VISIBLE"));
		   } else {
			ToastUtils.showShort("没有患者数据");
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
    * @param result
    * @param position
    */
   private void setErrorBindDate(String result, int position) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1")) {
	   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
		startActivity(
			new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
				"position", position)
				.putExtra("type", "afterBindTemp")
				.putExtra("mRbKey", mDtoOperation)
				.putExtra("GoneType", "VISIBLE"));
	   } else {
		ToastUtils.showShort("请开启管理端临时患者创建");
	   }
	}
   }

   /**
    * 绑定患者用操作耗材接口
    * @param mIntentType
    */
   private void loadBingFistDate(int mIntentType) {
	mInventoryDto.setOperationScheduleId(mOperationScheduleId);
	mInventoryDto.setPatientName(mPatient);
	mInventoryDto.setPatientId(mPatientId);
	mInventoryDto.setOperatingRoomNo(mOperatingRoomNo);
	mInventoryDto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mInventoryDto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mInventoryDto.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	String toJson = mGson.toJson(mInventoryDto);
	LogUtils.i(TAG, "toJson  " + toJson);

	if (mOnBtnGone) {
	   NetRequest.getInstance().putAllOperateYes(toJson, this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result   " + result);
		   MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
		   EventBusUtils.postSticky(new Event.EventFastMoreScan(true));
		   UnNetCstUtils.putUnNetOperateYes(mGson, OutBoxBingActivity.this);//提交离线耗材和重新获取在库耗材数据
		   finish();
		}
	   });
	} else {
	   if (mDtoOperation == 4) {
		if (mTitleConn) {
		   putEpcLyThDate(mIntentType, toJson);
		} else {
		   setErrorDate(mIntentType, "-1", 4);
		}
	   } else {
		if (mTitleConn) {
		   putEpcDate(mIntentType, toJson);
		} else {
		   setErrorDate(mIntentType, "-1", 3);
		}
	   }

	}
   }

   /**
    * 绑定患者的领用数据提交
    *
    * @param mIntentType
    * @param toJson
    */
   private void putEpcDate(int mIntentType, String toJson) {
	NetRequest.getInstance().putOperateYes(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
		new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		if (mIntentType == 2) {
		   UIUtils.putOrderId(mContext);
		   startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		}
		UnNetCstUtils.putUnNetOperateYes(mGson, OutBoxBingActivity.this);//提交离线耗材和重新获取在库耗材数据
		finish();
	   }

	   @Override
	   public void onError(String result) {
		setErrorDate(mIntentType, result, 3);

	   }
	});
   }

   /**
    * 确认提交领用退回
    *
    * @param mIntentType
    * @param toJson
    */
   private void putEpcLyThDate(int mIntentType, String toJson) {
	NetRequest.getInstance().putOperateLyThYes(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
		new Thread(() -> deleteVo(result)).start();//数据库删除已经操作过的EPC
		if (mIntentType == 2) {
		   UIUtils.putOrderId(mContext);
		   startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		}
		UnNetCstUtils.putUnNetOperateYes(mGson, OutBoxBingActivity.this);//提交离线耗材和重新获取在库耗材数据
		finish();
	   }

	   @Override
	   public void onError(String result) {
		setErrorDate(mIntentType, result, 4);
	   }
	});
   }

   /**
    * 请求失败的错误数据存储
    * @param mIntentType
    */
   private void setErrorDate(int mIntentType, String result, int type) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
	    mDtoOperation == type) {
	   List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	   ContentValues values = new ContentValues();
	   for (InventoryVo s : mInventoryDto.getInventoryVos()) {
		if (!getVosType(voList, s.getEpc())) {
		   s.save();
		} else {
		   values.put("status", "3");
		   values.put("operationstatus", 98);
		   values.put("renewtime", getDates());
		   values.put("idNo", s.getIdNo());
		   values.put("isCreate", s.isCreate());
		   values.put("medicalId", s.getMedicalId());
		   values.put("operatingRoomName", s.getOperatingRoomName());
		   values.put("operatingRoomNo", s.getOperatingRoomNo());
		   values.put("patientId", s.getPatientId());
		   values.put("patientName", s.getPatientName());
		   values.put("sex", s.getSex());
		   values.put("surgerytime", s.getSurgeryTime());
		   values.put("accountid", SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		   values.put("username", SPUtils.getString(mContext, KEY_USER_NAME));
		   Log.e(TAG, "s.getEpc()   " + s.getEpc());
		   LitePal.updateAll(InventoryVo.class, values, "epc = ?", s.getEpc());
		}
	   }
	   ToastUtils.showShort("操作成功");
	   MusicPlayer.playSoundByOperation(mDtoOperation);//播放操作成功提示音

	   if (mIntentType == 2) {
		UIUtils.putOrderId(mContext);
		startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
		App.getInstance().removeALLActivity_();
	   } else {
		EventBusUtils.postSticky(new Event.EventFrag("START1"));
	   }
	   finish();
	}
   }

   @Override
   protected void onDestroy() {
	mOnBtnGone = false;
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	if (mPatientDto != null) {
	   mPatientDto = null;
	}
	EventBusUtils.postSticky(new Event.EventFrag("START1"));
	EventBusUtils.unregister(this);
	if (mStarts != null) {
	   mStarts.cancel();
	   mStarts = null;
	}
	mOnBtnGone = false;
	super.onDestroy();
   }

   private void startScan(String deviceIndentify) {
	EventBusUtils.postSticky(new Event.EventLoading(true));
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);

	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
		LogUtils.i(TAG, "开始扫描了状态    " + i);
	   }

	}
   }

   /**
    * 扫描后传值
    */

   private void getDeviceDate(String deviceId, Map<String, List<EpcInfo>> epcs) {

	InventoryDto inventoryDto = new InventoryDto();
	List<Inventory> epcList = new ArrayList<>();

	for (Map.Entry<String, List<EpcInfo>> v : epcs.entrySet()) {
	   Inventory inventory = new Inventory();
	   inventory.setEpc(v.getKey());
	   epcList.add(inventory);
	}
	DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
	List<DeviceInventoryVo> deviceList = new ArrayList<>();

	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   Log.i(TAG, "device_id   " + box_id);
	   deviceInventoryVo.setDeviceId(box_id);
	}
	deviceInventoryVo.setInventories(epcList);
	deviceList.add(deviceInventoryVo);
	inventoryDto.setThingId(SPUtils.getString(mContext, THING_CODE));
	inventoryDto.setOperation(mInventoryDto.getOperation());
	inventoryDto.setDeviceInventoryVos(deviceList);
	inventoryDto.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	inventoryDto.setPatientName(mInventoryDto.getPatientName());
	inventoryDto.setPatientId(mInventoryDto.getPatientId());
	inventoryDto.setMedicalId(mInventoryDto.getMedicalId());
	inventoryDto.setSurgeryId(mInventoryDto.getSurgeryId());
	inventoryDto.setHisPatientId(mInventoryDto.getHisPatientId());
	String toJson = mGson.toJson(inventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	mEPCDate.clear();
	if (mInventoryDto.getOperation() == 4) {
	   if (mTitleConn) {
		getEpcDtoLyThDate(toJson);
	   } else {
		setUnNetDate(toJson, "-1");
	   }
	} else {
	   if (mTitleConn) {
		getEpcDate(toJson);
	   } else {
		setUnNetDate(toJson, "-1");
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
		if (mTCstInventoryTwoDto != null) {
		   mTCstInventoryTwoDto = null;
		}
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		setDateEpc(mTCstInventoryTwoDto, true);
	   }

	   @Override
	   public void onError(String result) {
		setUnNetDate(toJson, result);
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
		if (mTCstInventoryTwoDto != null) {
		   mTCstInventoryTwoDto = null;
		}
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		setDateEpc(mTCstInventoryTwoDto, true);
	   }

	   @Override
	   public void onError(String result) {
		setUnNetDate(toJson, result);
	   }
	});
   }

   /**
    * 无网的扫描后的EPC信息赋值
    *
    * @param toJson
    */
   private void setUnNetDate(String toJson, String result) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1")) {
	   List<InventoryVo> mInVo = new ArrayList<>();
	   InventoryDto cc = LitePal.findFirst(InventoryDto.class);
	   InventoryDto inventoryDto = new InventoryDto();
	   inventoryDto.setOperation(mDtoOperation);
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
			   if (!getVosType(mInVo, s.getEpc())) {//避免重复加入
				mInVo.add(inventoryVo);
			   }
			} else {
			   if (first != null) {
				mInVo.remove(first);
			   }
			}
		   }
		}
	   }
	   for (InventoryVo vo : mInVo) {
		if (vo.getExpireStatus() == 0) {
		   vo.setStatus("已过期");
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

	List<InventoryVo> inventoryVos = mInventoryDto.getInventoryVos();
	List<InventoryVo> inventoryVos1 = mTCstInventoryTwoDto.getInventoryVos();
	if (UIUtils.getConfigType(mContext, CONFIG_009)) {
	   mTCstInventoryTwoDto.setBindType("afterBind");
	} else {
	   mTCstInventoryTwoDto.setBindType("firstBind");
	}

	inventoryVos1.addAll(inventoryVos);
	for (InventoryVo ff : inventoryVos1) {
	   LogUtils.i(TAG, "ff   " + mPatient);
	   if (UIUtils.getConfigType(mContext, CONFIG_010) &&
		 UIUtils.getConfigType(mContext, CONFIG_012)) {
		LogUtils.i(TAG, "virtual  CONFIG_012 " + mHisPatientId);
		ff.setPatientName(mPatient);
		ff.setCreate(mIsCreate);
		ff.setPatientId(mPatientId);
		ff.setTempPatientId(mTempPatientId);
		ff.setIdNo(mIdNo);
		ff.setOperationScheduleId(mOperationScheduleId);
		ff.setSurgeryTime(mScheduleDateTime);
		ff.setOperatingRoomNo(mOperatingRoomNo);
		ff.setOperatingRoomName(mOperatingRoomNoName);
		ff.setSex(mSex);
		ff.setDeptId(mDeptId);
		ff.setMedicalId(mMedicalId);
		ff.setSurgeryId(mSurgeryId);
		ff.setHisPatientId(mHisPatientId);
	   } else {
		if (mPatientId != null && mPatientId.equals("virtual")) {
		   LogUtils.i(TAG, "virtual   " + mTempPatientId);
		   ff.setPatientName(mPatient);
		   ff.setCreate(mIsCreate);
		   ff.setPatientId(mPatientId);
		   ff.setTempPatientId(mTempPatientId);
		   ff.setIdNo(mIdNo);
		   ff.setOperationScheduleId(mOperationScheduleId);
		   ff.setSurgeryTime(mScheduleDateTime);
		   ff.setOperatingRoomNo(mOperatingRoomNo);
		   ff.setOperatingRoomName(mOperatingRoomNoName);
		   ff.setSex(mSex);
		   ff.setDeptId(mDeptId);
		   ff.setMedicalId(mMedicalId);
		   ff.setSurgeryId(mSurgeryId);
		   ff.setHisPatientId(mHisPatientId);

		} else {
		   LogUtils.i(TAG, "ZHENGSHI   ");
		   ff.setPatientName(mPatient);
		   ff.setPatientId(mPatientId);
		   ff.setOperationScheduleId(mOperationScheduleId);
		   ff.setMedicalId(mMedicalId);
		   ff.setSurgeryId(mSurgeryId);
		   ff.setHisPatientId(mHisPatientId);
		}
	   }
	   if (ff.getOperationStatus() == 7 || ff.getOperationStatus() == 99) {
		ff.setPatientName("vr");
		ff.setPatientId("vr");
	   }
	}
	mTCstInventoryTwoDto.setInventoryVos(inventoryVos1);
	EventBusUtils.postSticky(new Event.EventOutBoxBingDto(mTCstInventoryTwoDto));
	String toJson = mGson.toJson(mTCstInventoryTwoDto);
	LogUtils.i(TAG, "dddddddd    " + toJson);
	if (!mIsClick && (mTCstInventoryTwoDto.getInventoryVos() == null ||
				mTCstInventoryTwoDto.getInventoryVos().size() < 1)) {
	   if (mTimelyLeft != null && mTimelyRight != null) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
	   }
	   EventBusUtils.postSticky(new Event.EventLoading(false));
	   Toast.makeText(this, "未扫描到操作的耗材,即将返回主界面，请重新操作", Toast.LENGTH_SHORT).show();
	   mTimelyOpenDoorRight.setEnabled(false);
	   mTimelyStartBtnRight.setEnabled(false);
	   new Handler().postDelayed(new Runnable() {
		public void run() {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   finish();
		}
	   }, FINISH_TIME);
	} else {
	   EventBusUtils.postSticky(new Event.EventLoading(false));
	   mTimelyOpenDoorRight.setEnabled(true);
	   mTimelyStartBtnRight.setEnabled(true);
	}
	//	}
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
		if (mStarts != null) {
		   mStarts.cancel();
		}
		break;
	}
	return super.dispatchTouchEvent(ev);
   }

   /**
    * 绑定患者
    */
   private void setAfterBing() {
	if (mDtoOperation == 4) {
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
	if (UIUtils.getConfigType(mContext, CONFIG_009)) {//后绑定
	   backBind();
	} else if (UIUtils.getConfigType(mContext, CONFIG_010)) {//先绑定
	   firstBind();
	} else {//hua
	   mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	   mLyBingBtnRight.setVisibility(View.VISIBLE);
	   mTimelyLeft.setEnabled(false);
	   mTimelyRight.setEnabled(false);
	   if (mStarts != null) {
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	}
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : mInventoryVos) {
	   if (vosBean.getCstId() != null) {
		strings.add(vosBean.getCstId());
	   }
	   if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		 ((vosBean.getPatientId() == null || vosBean.getPatientId().equals("")) ||
		  (vosBean.getPatientName() == null || vosBean.getPatientName().equals("")))) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
		mTimelyNumberText.setVisibility(View.VISIBLE);
		mTimelyNumberText.setText(R.string.bind_error_string);
		if (mStarts != null) {
		   mStarts.cancel();
		   mTimelyRight.setText("确认并退出登录");
		}
		break;
	   }
	}
	setTableTypeDate(strings);
	setTimeStart();
   }

   /**
    * 界面显示的数据
    *
    * @param strings
    */
   private void setTableTypeDate(ArrayList<String> strings) {
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							    "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							    mInventoryVos.size() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_title_bing_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(this, this, mInventoryVos, titeleList, mSize, mLinearLayout,
						   mRecyclerview, mRefreshLayout, ACTIVITY,
						   ACT_TYPE_CONFIRM_HAOCAI, -10);
	}
	mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
   }

   /**
    * 后绑定患者
    */
   private void backBind() {
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mTimelyLeft.setEnabled(false);
	mTimelyRight.setEnabled(false);
	mTimelyNumberText.setVisibility(View.VISIBLE);
	mTimelyNumberText.setText(R.string.bind_error_string);
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	mLyBingBtnRight.setVisibility(View.VISIBLE);
   }

   /**
    * 先绑定患者
    */
   private void firstBind() {
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mLyBingBtnRight.setVisibility(View.GONE);
	mTimelyLeft.setEnabled(true);
	mTimelyRight.setEnabled(true);
	mTimelyNumberText.setVisibility(View.GONE);
	for (InventoryVo vosBean : mInventoryVos) {
	   if ((vosBean.getIsErrorOperation() == 0 && vosBean.getExpireStatus() != 0) ||
		 (vosBean.getIsErrorOperation() == 1 && vosBean.getDeleteCount() != 0 &&
		  vosBean.getExpireStatus() == 0)) {
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		mTimelyNumberText.setVisibility(View.GONE);
	   } else {
		LogUtils.i(TAG, "我走了falsesss");
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
		mTimelyNumberText.setVisibility(View.VISIBLE);
		setPointOutText(vosBean, mInventoryVos, mIsClick);
		if (mStarts != null) {
		   mStarts.cancel();
		   mTimelyRight.setText("确认并退出登录");
		}
		break;
	   }
	}
   }

   private void setTimeStart() {
	for (InventoryVo b : mInventoryVos) {
	   //这代码自己看的都蛋疼，优化优化优化 TODO
	   if (isError(b)) {
		if (isSuccess(b)) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   mTimelyNumberText.setVisibility(View.GONE);

		   if (mStarts != null) {
			LogUtils.i(TAG, "true  ssssssfafafa mObject ss mObject ");
			mStarts.cancel();
			mStarts.start();
		   }
		} else {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   setPointOutText(b, mInventoryVos, mIsClick);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		}
	   } else {
		LogUtils.i(TAG, "我走了false");
		if (mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   mTimelyNumberText.setVisibility(View.VISIBLE);
		   setPointOutText(b, mInventoryVos, mIsClick);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		} else {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   mTimelyNumberText.setVisibility(View.GONE);
		   if (mStarts != null) {
			mStarts.cancel();
			mStarts.start();
		   }
		}

	   }
	}
   }

   private boolean isSuccess(InventoryVo b) {
	return (mDtoOperation == 8 && b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		  b.getExpireStatus() == 0) ||
		 (b.getIsErrorOperation() != 1 && b.getDeleteCount() == 0 && b.getExpireStatus() != 0 &&
		  ((b.getOperationStatus() == 7 || b.getOperationStatus() == 99) &&
		   (b.getPatientName() == null ||
		    ((b.getOperationStatus() == 7 || b.getOperationStatus() == 99) &&
		     b.getPatientName().equals("vr")))));
   }

   private boolean isError(InventoryVo b) {
	return (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && b.getExpireStatus() == 0 &&
		  mDtoOperation != 8) || ((mDtoOperation == 3 || mDtoOperation == 4) &&
						  UIUtils.getConfigType(mContext, CONFIG_007) &&
						  (b.getPatientName() == null ||
						   ((b.getOperationStatus() == 7 ||
						     b.getOperationStatus() == 99) &&
						    b.getPatientName().equals("vr"))));
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
}
