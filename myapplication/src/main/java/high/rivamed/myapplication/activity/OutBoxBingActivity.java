package high.rivamed.myapplication.activity;

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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
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
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_HAOCAI;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.FINISH_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack2;

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
   private String                                       mPatient="";
   private String                                       mPatientId="";
   private String                                       mTempPatientId="";
   private String                                       mOperationScheduleId="";
   private String                                       mMedicalId="";
   private String                                       mSurgeryId="";
   private boolean                    mPause   = true;
   private Map<String, List<TagInfo>> mEPCDate = new TreeMap<>();
   private int                        mAllPage = 1;
   private int                        mRows    = 20;
   int k = 0;

   private LoadingDialog.Builder mLoading;
   private RvDialog.Builder      mAfterBind;
   private String                mIdNo="";
   private String                mScheduleDateTime="";
   private String                mOperatingRoomNo="";
   private String                mOperatingRoomNoName="";
   private String                mSex="";
   private String                mDeptId="";
   private boolean               mIsCreate=false;
   public static boolean mOnBtnGone = false;
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
    * 接收数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onOutBoxBingEvent(Event.EventOutBoxBingDto event) {
	LogUtils.i(TAG, "event");
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
	mMedicalId =mInventoryDto.getMedicalId();
	mSurgeryId =mInventoryDto.getSurgeryId();
	mPatientDto =event.mPatientDto;
	if (mPatientDto!=null){
	   mDtoOperation = mPatientDto.getOperation();
	   mPatient = mPatientDto.getPatientName();
	   mPatientId = mPatientDto.getPatientId();
	   mOperationScheduleId = mPatientDto.getOperationScheduleId();
	   mTempPatientId = mPatientDto.getTempPatientId();
	   mOperatingRoomNoName = mPatientDto.getOperatingRoomNoName();
	   mScheduleDateTime = mPatientDto.getSurgeryTime();
	   mIsCreate =  mPatientDto.isCreate();
	   mIdNo= mPatientDto.getIdNo();
	   mOperatingRoomNo =   mPatientDto.getOperatingRoomNo();
	   mSex =  mPatientDto.getSex();
	   mDeptId = mPatientDto.getDeptId();
	   mMedicalId = mPatientDto.getMedicalId();
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   if (event.bing) {//绑定的按钮转换
		for (InventoryVo b : mInventoryVos) {
		   ArrayList<String> strings = new ArrayList<>();
		   strings.add(b.getCstCode());

		   if (UIUtils.getConfigType(mContext, CONFIG_009) &&
			 ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			  (b.getPatientName() == null || b.getPatientName().equals("")))) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
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
			 (UIUtils.getConfigType(mContext, CONFIG_007) && b.getPatientName() == null)) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
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
			if (mStarts != null) {
			   LogUtils.i(TAG, "OutBoxBingActivity   ssss");
			   mStarts.cancel();
			   mStarts.start();
			}
		   }
		}
	   }
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
	mStarts.cancel();
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
	if (mStarts == null) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000,mTimelyLeft, mTimelyRight);
	   mStarts.cancel();
	}
	setAfterBing();
	//        Intent intent = getIntent();
	//        int type = intent.getIntExtra("type", 0);
	//        if (type == 100) {
	//            mPatient = intent.getStringExtra("patientName");
	//            mPatientId = intent.getStringExtra("patientId");
	//            mOperationScheduleId = intent.getStringExtra("operationScheduleId");
	//        }
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
	mSurgeryId =event.mSurgeryId;
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
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
			return;
		   }

		   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			 b.getExpireStatus() == 0) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
			return;
		   } else {
			LogUtils.i(TAG, "我走了falsesss");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
			mStarts.cancel();
			mStarts.start();
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
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
		moreStartScan();
		break;
	   case R.id.timely_open_door_right://重新开门
		if (!mIsClick) {
		   mStarts.cancel();
		   mTimelyRight.setText("确认并退出登录");
		   List<DeviceInventoryVo> deviceInventoryVoss = mInventoryDto.getDeviceInventoryVos();
		   mInventoryDto.getInventoryVos().clear();
		   deviceInventoryVoss.clear();
		   TimelyAllFrag.mPauseS = true;
		   if (UIUtils.getConfigType(mContext, CONFIG_009)) {
			mPatient = null;
			mPatientId = null;
		   }
		   mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
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
		if (UIUtils.isFastDoubleClick()) {
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
		if (UIUtils.isFastDoubleClick()) {
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
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (!mIsClick) {
			if (UIUtils.getConfigType(mContext, CONFIG_012)) {//创建临时患者
			   goToFirstBindAC(-2);
			} else {
			   goToFirstBindAC(-2);
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
	mTimelyLeft.setEnabled(true);
	mTimelyRight.setEnabled(true);
	if (UIUtils.getConfigType(mContext, CONFIG_009)) {
	   mPatient = null;
	   mPatientId = null;
	}

	List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
	mInventoryDto.getInventoryVos().clear();
	deviceInventoryVos.clear();
	mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
	TimelyAllFrag.mPauseS = true;
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   startScan(deviceCode);
	}
   }

   private void goToFirstBindAC(int position) {
	//获取需要绑定的患者
	NetRequest.getInstance().findSchedulesDate("", mAllPage, mRows, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);

		FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
		if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", "afterBindTemp")
					.putExtra("GoneType", "VISIBLE"));
		   } else {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", "afterBindTemp")
					.putExtra("GoneType", "GONE"));
		   }
		} else {
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			startActivity(
				new Intent(OutBoxBingActivity.this, TemPatientBindActivity.class).putExtra(
					"position", position)
					.putExtra("type", "afterBindTemp")
					.putExtra("GoneType", "VISIBLE"));
		   } else {
			ToastUtils.showShort("没有患者数据");
		   }
		}
	   }
	});
   }

   /**
    * 绑定患者用操作耗材接口
    *
    * @param mIntentType
    */
   private void loadBingFistDate(int mIntentType) {
	mInventoryDto.setOperationScheduleId(mOperationScheduleId);
	mInventoryDto.setPatientName(mPatient);
	mInventoryDto.setPatientId(mPatientId);
	mInventoryDto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
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
		   finish();
		}
	   });
	} else {
	   NetRequest.getInstance().putOperateYes(toJson, this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result   " + result);
		   ToastUtils.showShort("操作成功");
		   EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
		   MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
		   if (mIntentType == 2) {
		      UIUtils.putOrderId(mContext);
			startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
			App.getInstance().removeALLActivity_();
		   }
		   finish();
		}
	   });
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
	if (mPatientDto!=null){
	   mPatientDto=null;
	}
	EventBusUtils.postSticky(new Event.EventFrag("START1"));
	EventBusUtils.unregister(this);
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

   private void getDeviceDate(String deviceId, Map<String, List<TagInfo>> epcs) {

	InventoryDto inventoryDto = new InventoryDto();
	List<Inventory> epcList = new ArrayList<>();

	for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
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
	String toJson = mGson.toJson(inventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	mEPCDate.clear();
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result    " + result);
		if (mTCstInventoryTwoDto != null) {
		   mTCstInventoryTwoDto = null;
		}
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		setDateEpc();
	   }
	});
   }

   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc() {
	String string = null;
	if (mTCstInventoryTwoDto.getErrorEpcs() != null &&
	    mTCstInventoryTwoDto.getErrorEpcs().size() > 0) {
	   string = StringUtils.listToString(mTCstInventoryTwoDto.getErrorEpcs());
	   ToastUtils.showLong(string);
	   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
	} else {

	   List<InventoryVo> inventoryVos = mInventoryDto.getInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
	   List<InventoryVo> inventoryVos1 = mTCstInventoryTwoDto.getInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos1 = mTCstInventoryTwoDto.getDeviceInventoryVos();

	   Set<DeviceInventoryVo> set = new HashSet<DeviceInventoryVo>();
	   set.addAll(deviceInventoryVos);
	   set.addAll(deviceInventoryVos1);
	   List<DeviceInventoryVo> c = new ArrayList<DeviceInventoryVo>(set);
	   if (UIUtils.getConfigType(mContext, CONFIG_009)) {
		mTCstInventoryTwoDto.setBindType("afterBind");
	   } else {
		mTCstInventoryTwoDto.setBindType("firstBind");
	   }
	   inventoryVos1.addAll(inventoryVos);
	   inventoryVos1.removeAll(inventoryVos);
	   inventoryVos1.addAll(inventoryVos);
	   for (InventoryVo ff : inventoryVos1) {
		LogUtils.i(TAG, "ff   " + mPatient);
		if (UIUtils.getConfigType(mContext, CONFIG_010) &&
		    UIUtils.getConfigType(mContext, CONFIG_012)) {
		   LogUtils.i(TAG,"virtual  CONFIG_012 ");
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
		} else {
		   if (mPatientId!=null&&mPatientId.equals("virtual")){
		      LogUtils.i(TAG,"virtual   "+mTempPatientId);
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

		   }else {
			LogUtils.i(TAG,"ZHENGSHI   ");
			ff.setPatientName(mPatient);
			ff.setPatientId(mPatientId);
			ff.setOperationScheduleId(mOperationScheduleId);
			ff.setMedicalId(mMedicalId);
			ff.setSurgeryId(mSurgeryId);
		   }
		}
	   }
	   if (mTCstInventoryTwoDto.getPatientName() == null) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
	   }
	   mTCstInventoryTwoDto.setInventoryVos(inventoryVos1);
	   mTCstInventoryTwoDto.setDeviceInventoryVos(c);

	   EventBusUtils.postSticky(new Event.EventOutBoxBingDto(mTCstInventoryTwoDto));
	   String toJson = mGson.toJson(mTCstInventoryTwoDto);
	   LogUtils.i(TAG, "dddddddd    " + toJson);
	   if (mTCstInventoryTwoDto.getErrorEpcs() == null &&
		 (mTCstInventoryTwoDto.getInventoryVos() == null ||
		  mTCstInventoryTwoDto.getInventoryVos().size() < 1) &&
		 mEthDeviceIdBack.size() == 1) {
		if (mTimelyLeft != null && mTimelyRight != null) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		}
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
		mTimelyOpenDoorRight.setEnabled(true);
		mTimelyStartBtnRight.setEnabled(true);
	   }
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
		LogUtils.i(TAG, "   ACTION_UP  ");
		if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
		    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")&&mTimelyRight.isEnabled()) {

		   mStarts.cancel();
		   mStarts.start();
		}
		break;
	   //否则其他动作计时取消
	   default:
		mStarts.cancel();
		LogUtils.i(TAG, "   其他操作  ");

		break;
	}

	return super.dispatchTouchEvent(ev);
   }

   /**
    * 绑定患者
    */
   private void setAfterBing() {
	mBaseTabTvTitle.setText("耗材领用");
	mTimelyNumberLeft.setVisibility(View.VISIBLE);
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabBtnMsg.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	if (UIUtils.getConfigType(mContext, CONFIG_009)) {//后绑定
	   mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	   mTimelyLeft.setEnabled(false);
	   mTimelyRight.setEnabled(false);
	   if (mStarts != null) {
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	   mLyBingBtnRight.setVisibility(View.VISIBLE);
	} else if (UIUtils.getConfigType(mContext, CONFIG_010)) {//先绑定
	   mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	   mLyBingBtnRight.setVisibility(View.GONE);
	   mTimelyLeft.setEnabled(true);
	   mTimelyRight.setEnabled(true);
	   for (InventoryVo vosBean : mInventoryVos) {
		if ((vosBean.getIsErrorOperation() == 0 && vosBean.getExpireStatus() != 0) ||
		    (vosBean.getIsErrorOperation() == 1 && vosBean.getDeleteCount() != 0 &&
		     vosBean.getExpireStatus() == 0)) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		} else {
		   LogUtils.i(TAG, "我走了falsesss");
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   break;
		}
	   }
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
	   strings.add(vosBean.getCstCode());
	   if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		 ((vosBean.getPatientId() == null || vosBean.getPatientId().equals("")) ||
		  (vosBean.getPatientName() == null || vosBean.getPatientName().equals("")))) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
		if (mStarts != null) {
		   mStarts.cancel();
		   mTimelyRight.setText("确认并退出登录");
		}
		break;
	   }
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							    "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							    mInventoryVos.size() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_title_bing_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(this, this, mInventoryVos, titeleList, mSize,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   ACT_TYPE_CONFIRM_HAOCAI, -10);
	}
	mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
	setTimeStart();
   }

   private void setTimeStart() {
	for (InventoryVo b : mInventoryVos) {

	   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && b.getExpireStatus() == 0 &&
		  mDtoOperation != 8) ||
		 (mDtoOperation == 3 && UIUtils.getConfigType(mContext, CONFIG_007) &&
		  b.getPatientName() == null)) {
		if (mDtoOperation == 8 && b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		    b.getExpireStatus() == 0) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   if (mStarts != null) {
			LogUtils.i(TAG, "true  ssssssfafafa mObject ss mObject ");
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
		LogUtils.i(TAG, "我走了false");
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		if (mStarts != null) {
		   mStarts.cancel();
		   mStarts.start();
		}
	   }
	}
   }
}
