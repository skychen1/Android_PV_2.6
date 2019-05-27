package high.rivamed.myapplication.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import cn.rivamed.DeviceManager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
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
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.FINISH_TIME;
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

   public TableTypeView mTypeView;
   List<String> titeleList = null;
   public int          mSize;
   public InventoryDto mInventoryDto;
   public List<InventoryVo> mInventoryVos    = new ArrayList<>(); //入柜扫描到的epc信息
   public List<InventoryVo> mBoxInventoryVos = new ArrayList<>(); //在柜epc信息
   private int                       mOperation;
   private Event.EventButton         mEventButton;
   private int                       mOperationType;
   private String                    mClossEthId;
   private RxUtils.BaseEpcObservable mObs;
   private List<InventoryVo>         mLocalAllCstVos;
   private InventoryDto mDto = new InventoryDto();
   private boolean mDoorStatusType;

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

   /**
    * 门锁的状态检测回调
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventDoorStatus(Event.EventDoorStatus event) {
	mDoorStatusType = event.type;
   }

   private void setButtonType(Event.EventButton event) {

	if (event.bing) {//绑定的按钮转换
	   for (InventoryVo b : mBoxInventoryVos) {

		ArrayList<String> strings = new ArrayList<>();
		strings.add(b.getCstCode());
		if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		    ((b.getPatientId() == null || b.getPatientId().equals("")) ||
		     (b.getPatientName() == null || b.getPatientName().equals(""))) || mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   setAllTextVis();

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
		    mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   setAllTextVis();
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
		     b.getExpireStatus() != 0) || mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   setAllTextVis();

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

   private void setAllTextVis() {
	mAllText.setVisibility(View.VISIBLE);
	if (mOperationType == 8) {
	   mAllText.setText(R.string.op_error_tuihuo);
	} else if (mOperationType == 3) {
	   mAllText.setText(R.string.op_error_ly);
	} else if (mOperationType == 2) {
	   mAllText.setText(R.string.op_error_rk);
	} else if (mOperationType == 9) {
	   mAllText.setText(R.string.op_error_yc);
	} else if (mOperationType == 10) {
	   mAllText.setText(R.string.op_error_yr);
	} else if (mOperationType == 7) {
	   mAllText.setText(R.string.op_error_th);
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
	Log.i("SelSelfff", "EventOneEpcDeviceCallBack    " + event.deviceId + "    " + event.epc);
	if (getVosType(mLocalAllCstVos, event.epc)) {//过滤不在库存的epc进行请求
	   for (int i = 0; i < mBoxInventoryVos.size(); i++) {
		if (mBoxInventoryVos.get(i).getEpc().equals(event.epc)) {//本来在库存的且未拿出柜子的就remove
		   mBoxInventoryVos.remove(i);
		}
	   }

	   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	} else {
	   mObs.getScanEpc(event.deviceId, event.epc);
	}
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_selinoutbox_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.post(new Event.EventLoading(true));
	AllDeviceCallBack.getInstance().initCallBack();
	mLocalAllCstVos = getLocalAllCstVos();
	Log.i("SelSelfff", "mLocalAllCstVos    " + mGson.toJson(mLocalAllCstVos));
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
		//		if (mTitleConn) {
		//		   getDeviceDate(mDto);
		//		} else {
		new Thread(() -> setScanDateInBoxVo(vos)).start();
		//		}

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
//	runOnUiThread(new Runnable() {
//	   @Override
//	   public void run() {
//		Log.i("SelSelfff",
//			"mGson.toJson(mBoxInventoryVos)    " + mGson.toJson(mBoxInventoryVos));
//		mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
//
//	   }
//	});
	runOnUiThread(() -> mTypeView.mInBoxAllAdapter.notifyDataSetChanged());
   }

   private void setInventoryVoDate(List<DeviceInventoryVo> vos, int x) {
	List<Inventory> list = vos.get(x).getInventories();
	for (int i = 0; i < list.size(); i++) {
	   if (!getVosType(mBoxInventoryVos,list.get(i).getEpc())){
		InventoryVo inventoryVo = new InventoryVo();
		inventoryVo.setEpc(list.get(i).getEpc());
		inventoryVo.setDeviceId(vos.get(x).getDeviceId());
		inventoryVo.setOperationStatus("3");
		inventoryVo.setStatus("禁止放入");
		inventoryVo.setIsErrorOperation(1);
		inventoryVo.setRenewTime(getDates());
		inventoryVo.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		inventoryVo.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
		mBoxInventoryVos.add(inventoryVo);
	   }
	}
   }

   private void setInBoxDate() {
	if (mOperationType == 8) {
	   mBaseTabTvTitle.setText("耗材退货");
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

	if (mOperationType == 8) {//耗材退货
	   mBoxInventoryVos.clear();
	   mBoxInventoryVos.addAll(mLocalAllCstVos);
	   for (InventoryVo vo : mBoxInventoryVos) {
		vo.setStatus("退货");
	   }
	} else if (mOperationType == 3) {//耗材领用
	   mBoxInventoryVos.clear();
	   mBoxInventoryVos.addAll(mLocalAllCstVos);
	   for (InventoryVo vo : mBoxInventoryVos) {
		vo.setStatus("领用");
	   }
	} else if (mOperationType == 2) {//耗材入库
	   mBoxInventoryVos.clear();
	   mBoxInventoryVos.addAll(mLocalAllCstVos);
	   for (InventoryVo vo : mBoxInventoryVos) {
		vo.setStatus("入库");
	   }
	} else if (mOperationType == 9) {//耗材移出
	   mBoxInventoryVos.clear();
	   mBoxInventoryVos.addAll(mLocalAllCstVos);
	   for (InventoryVo vo : mBoxInventoryVos) {
		vo.setStatus("移出");
	   }
	} else if (mOperationType == 11) {//耗材调拨

	} else if (mOperationType == 10) {//耗材移入

	} else if (mOperationType == 7) {//耗材退回

	}
	//	if (mOperationType==3||mOperationType==8||mOperationType==11||mOperationType==9){//出柜
	//	   mBoxInventoryVos.clear();
	//	   mBoxInventoryVos.addAll(getLocalAllCstVos());
	//	}else if (mOperationType ==2||mOperationType==10||mOperationType==7){//入柜
	//
	//	}
	if (mBoxInventoryVos != null) {
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
		setAllTextVis();
	   } else {
		mAllText.setVisibility(View.GONE);
	   }
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

   @OnClick({R.id.timely_start_btn, R.id.timely_open_door, R.id.timely_left, R.id.timely_right})
   public void onViewClicked(View view) {
	super.onViewClicked(view);
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   if (!mDoorStatusType) {
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
		   if (!mIsClick) {
			mIntentType = 1;
			if (mInventoryVos != null) {
			   if (mInventoryDto.getOperation() == 9) {//移出
				setYcDate(mIntentType);
			   } else if (mInventoryDto.getOperation() == 11) {//调拨
				setDbDate(mIntentType);
			   } else if (mInventoryDto.getOperation() == 8) {//退货
				setThDate(mIntentType);
			   } else {//其他操作
				setDate(mIntentType);
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
		if (UIUtils.isFastDoubleClick(R.id.timely_left)) {
		   return;
		} else {
		   if (!mIsClick) {
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
		if (UIUtils.isFastDoubleClick(R.id.timely_right)) {
		   return;
		} else {
		   if (!mIsClick) {
			mStarts.cancel();
			List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
			mInventoryDto.getInventoryVos().clear();
			if (null != deviceInventoryVos) {
			   deviceInventoryVos.clear();
			}
			for (String deviceInventoryVo : mEthDeviceIdBack) {
			   String deviceCode = deviceInventoryVo;
			   LogUtils.i(TAG, "deviceCode    " + deviceCode);
			   DeviceManager.getInstance().OpenDoor(deviceCode);
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
	if (mInventoryDto.getOperation() == 9) {//移出
	   setYcDate(mIntentType);
	} else if (mInventoryDto.getOperation() == 11) {//调拨
	   setDbDate(mIntentType);
	} else if (mInventoryDto.getOperation() == 8) {//退货
	   setThDate(mIntentType);
	} else {//其他操作
	   setDate(mIntentType);
	}
   }

   /**
    * 重新扫描
    */
   private void moreStartScan() {
	//
	//	List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
	//	mInventoryDto.getInventoryVos().clear();
	//	if (null != deviceInventoryVos) {
	//	   deviceInventoryVos.clear();
	//	}

	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   startScan(deviceCode);
	}
   }

   private void startScan(String deviceIndentify) {
	//	EventBusUtils.postSticky(new Event.EventLoading(true));
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		ReaderManager.getManager().stopScan(device_id);
		mBoxInventoryVos.clear();
		mBoxInventoryVos.addAll(mLocalAllCstVos);
		if (mObs != null) {
		   mObs.removeVos();
		}
		ReaderManager.getManager().startScan(device_id, 10000);
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
   private void setDate(int mIntentType) {
	InventoryDto dto = new InventoryDto();
	dto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	dto.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	dto.setInventoryVos(mInventoryVos);
	dto.setOperation(mInventoryDto.getOperation());
	dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	String s = mGson.toJson(dto);
	LogUtils.i(TAG, "返回  " + s);
	NetRequest.getInstance().putOperateYes(s, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result  " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.playSoundByOperation(mInventoryDto.getOperation());//播放操作成功提示音
		if (mIntentType == 2) {
		   UIUtils.putOrderId(mContext);
		   startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		} else {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		}
		UnNetCstUtils.putUnNetOperateYes(mGson, SelInOutBoxTwoActivity.this);//提交离线耗材和重新获取在库耗材数据
		finish();
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.postSticky(new Event.EventLoading(false));
		if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
		    mOperationType == 3) {
		   ContentValues values = new ContentValues();
		   values.put("status", "3");
		   values.put("operationstatus", "3");
		   values.put("renewtime", getDates());
		   values.put("accountid", SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		   values.put("username", SPUtils.getString(mContext, KEY_USER_NAME));
		   for (InventoryVo s : dto.getInventoryVos()) {
			LitePal.updateAll(InventoryVo.class, values, "epc = ?", s.getEpc());
		   }
		   MusicPlayer.playSoundByOperation(mOperationType);//播放操作成功提示音

		   if (mIntentType == 2) {
			UIUtils.putOrderId(mContext);
			startActivity(new Intent(SelInOutBoxTwoActivity.this, LoginActivity.class));
			App.getInstance().removeALLActivity_();
		   } else {
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   }
		   finish();
		}

	   }
	});
   }

   /**
    * 扫描后传值
    */
   //   private void getDeviceDate(String deviceId, Map<String, List<EpcInfo>> epcs) {
   private void getDeviceDate(InventoryDto Dto) {
	String toJson = mGson.toJson(Dto);
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		Log.i(TAG, "result    " + result);
		mTCstInventoryTwoDto = mGson.fromJson(result, InventoryDto.class);
		setDateEpc(mTCstInventoryTwoDto, true);
	   }

	   @Override
	   public void onError(String result) {
		if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
		    mOperationType == 3) {
		   setUnNetDate(toJson);
		}
	   }
	});
   }

   /**
    * 无网的扫描后的EPC信息赋值
    *
    * @param toJson
    */
   private void setUnNetDate(String toJson) {
	List<InventoryVo> mInVo = new ArrayList<>();
	InventoryDto cc = LitePal.findFirst(InventoryDto.class);
	InventoryDto inventoryDto = new InventoryDto();
	inventoryDto.setOperation(mOperationType);
	inventoryDto.setThingId(cc.getThingId());
	LogUtils.i(TAG, "FDFDF0   " + mGson.toJson(inventoryDto));
	InventoryDto dto = mGson.fromJson(toJson, InventoryDto.class);
	if (dto.getDeviceInventoryVos().size() > 0) {
	   List<Inventory> list = dto.getDeviceInventoryVos().get(0).getInventories();
	   String deviceId = dto.getDeviceInventoryVos().get(0).getDeviceId();

	   List<InventoryVo> vos = LitePal.where("deviceid = ? and status = ?", deviceId, "2")
		   .find(InventoryVo.class);
	   mInVo.addAll(vos);
	   if (list.size() != 0) {
		for (Inventory s : list) {
		   InventoryVo first = LitePal.where("epc = ? and deviceid = ?", s.getEpc(), deviceId)
			   .findFirst(InventoryVo.class);
		   InventoryVo unnetEPC = LitePal.where("epc = ? ", s.getEpc())
			   .findFirst(InventoryVo.class);
		   if (unnetEPC == null) {
			inventoryDto.getUnNetMoreEpcs().add(s.getEpc());
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

   /**
    * 扫描EPC返回后进行赋值
    */
   private void setDateEpc(InventoryDto mTCstInventoryTwoDto, boolean type) {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	String string = null;
	if (!type && null != mTCstInventoryTwoDto.getUnNetMoreEpcs() &&
	    mTCstInventoryTwoDto.getUnNetMoreEpcs().size() > 0) {
	   string = StringUtils.listToStrings(mTCstInventoryTwoDto.getUnNetMoreEpcs());
	   ToastUtils.showLong(string);
	   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
	}
	//	if (mTCstInventoryTwoDto.getErrorEpcs() != null &&
	//	    mTCstInventoryTwoDto.getErrorEpcs().size() > 0) {
	//	   string = StringUtils.listToString(mTCstInventoryTwoDto.getErrorEpcs());
	//	   ToastUtils.showLong(string);
	//	   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
	//	} else {
	List<InventoryVo> inventoryVos = mInventoryDto.getInventoryVos();
	//	   List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
	List<InventoryVo> inventoryVos1 = mTCstInventoryTwoDto.getInventoryVos();
	//	   List<DeviceInventoryVo> deviceInventoryVos1 = mTCstInventoryTwoDto.getDeviceInventoryVos();
	//	   Set<DeviceInventoryVo> set = new HashSet<DeviceInventoryVo>();
	//	   set.addAll(deviceInventoryVos);
	//	   set.addAll(deviceInventoryVos1);
	//	   List<DeviceInventoryVo> c = new ArrayList<DeviceInventoryVo>(set);
	inventoryVos1.addAll(inventoryVos);
	inventoryVos1.removeAll(inventoryVos);
	inventoryVos1.addAll(inventoryVos);
	mTCstInventoryTwoDto.setInventoryVos(inventoryVos1);
	//	   mTCstInventoryTwoDto.setDeviceInventoryVos(c);
	EventBusUtils.postSticky(new Event.EventSelInOutBoxDto(mTCstInventoryTwoDto));

	//	   if ((mTCstInventoryTwoDto.getInventoryVos() == null ||
	//		  mTCstInventoryTwoDto.getInventoryVos().size() < 1) && mEthDeviceIdBack.size() != 0) {

	if (!mIsClick && (mTCstInventoryTwoDto.getInventoryVos() == null ||
				mTCstInventoryTwoDto.getInventoryVos().size() < 1)) {
	   if (mTimelyLeft != null && mTimelyRight != null) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	   EventBusUtils.postSticky(new Event.EventLoading(false));
	   Toast.makeText(this, "未扫描到操作的耗材,即将返回主界面，请重新操作", Toast.LENGTH_SHORT).show();
	   mTimelyOpenDoor.setEnabled(false);
	   mTimelyStartBtn.setEnabled(false);
	   new Handler().postDelayed(new Runnable() {
		public void run() {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   finish();
		}
	   }, FINISH_TIME);
	} else {
	   EventBusUtils.postSticky(new Event.EventLoading(false));
	   mTimelyOpenDoor.setEnabled(true);
	   mTimelyStartBtn.setEnabled(true);
	}
	//	}
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
	if (mTCstInventoryTwoDto == null) {
	   for (int i = 0; i < mInventoryDto.getInventoryVos().size(); i++) {
		inventoryVos.add(mInventoryDto.getInventoryVos().get(i));
	   }
	} else {
	   for (int i = 0; i < mTCstInventoryTwoDto.getInventoryVos().size(); i++) {
		inventoryVos.add(mTCstInventoryTwoDto.getInventoryVos().get(i));
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
   protected void onDestroy() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	EventBusUtils.unregister(this);
	mStarts.cancel();
	mStarts = null;
	super.onDestroy();
   }

   @Override
   protected void onResume() {

	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	super.onResume();
   }

   private void setTimeStart() {
	for (InventoryVo b : mInventoryVos) {
	   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && b.getExpireStatus() == 0 &&
		  mOperationType != 8) ||
		 (mOperationType == 3 && UIUtils.getConfigType(mContext, CONFIG_007) &&
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
   }

   @Override
   protected void onPause() {
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
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
	if (mTCstInventoryTwoDto == null && mInventoryDto.getInventoryVos() != null) {
	   for (int i = 0; i < mInventoryDto.getInventoryVos().size(); i++) {
		inventoryVos.add(mInventoryDto.getInventoryVos().get(i));
	   }
	} else {
	   for (int i = 0; i < mTCstInventoryTwoDto.getInventoryVos().size(); i++) {
		inventoryVos.add(mTCstInventoryTwoDto.getInventoryVos().get(i));
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
	if (mTCstInventoryTwoDto == null) {
	   for (int i = 0; i < mInventoryDto.getInventoryVos().size(); i++) {
		inventoryVos.add(mInventoryDto.getInventoryVos().get(i));
	   }
	} else {
	   for (int i = 0; i < mTCstInventoryTwoDto.getInventoryVos().size(); i++) {
		inventoryVos.add(mTCstInventoryTwoDto.getInventoryVos().get(i));
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

}
