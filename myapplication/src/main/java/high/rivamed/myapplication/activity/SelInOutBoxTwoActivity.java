package high.rivamed.myapplication.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import cn.rivamed.model.TagInfo;
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
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.READER_TIME;
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
   private Map<String, List<TagInfo>> mEPCDate = new TreeMap<>();
   int k          = 0;
   private LoadingDialog.Builder mLoading;
   @BindView(R.id.timely_open_door)
   TextView           mTimelyOpenDoor;
   @BindView(R.id.timely_start_btn)
   TextView           mTimelyStartBtn;
   @BindView(R.id.timely_left)
   TextView           mTimelyLeft;
   @BindView(R.id.timely_right)
   TextView           mTimelyRight;
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
   public List<InventoryVo> mInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息
   private int mOperation;
   private Event.EventButton mEventButton;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   mEventButton = event;
	   setButtonType(mEventButton);
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
		    (UIUtils.getConfigType(mContext, CONFIG_007) && b.getPatientName() == null)||mIsClick) {
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
	} else {
	   for (InventoryVo b : mInventoryVos) {
		LogUtils.i(TAG, "mOperation   cancel" + mOperation);
		if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && mOperation != 8) ||
		    (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		     b.getExpireStatus() != 0)||mIsClick) {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
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
		   if (mStarts != null) {
			mStarts.cancel();
			mStarts.start();
		   }
		}
	   }
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
    * (检测没有关门)语音
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClick(Event.HomeNoClickEvent event) {
	mIsClick = event.isClick;
	if (event.isClick) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	} else {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);

	}
	EventBusUtils.post(new Event.EventButton(true,false));
	EventBusUtils.removeStickyEvent(getClass());
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
    * 接收出入库的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onSelInOutBoxEvent(Event.EventSelInOutBoxDto event) {
	if (mInventoryDto != null && mInventoryVos != null) {
	   mInventoryDto = event.mInventoryDto;
	   List<InventoryVo> inventoryVos = event.mInventoryDto.getInventoryVos();
	   mInventoryVos.clear();
	   mOperation = event.mInventoryDto.getOperation();
	   mInventoryVos.addAll(inventoryVos);//选择开柜
	   setInBoxDate();
	   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	   LogUtils.i(TAG, "mOperation   " + mOperation);
	} else {
	   mInventoryDto = event.mInventoryDto;
	   mInventoryVos = event.mInventoryDto.getInventoryVos();//选择开柜
	}
	mDtoOperation = mInventoryDto.getOperation();
	LogUtils.i(TAG, "mDtoOperation   " + mDtoOperation);
   }

   /**
    * 扫描后EPC准备传值
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	LogUtils.i(TAG, "TAG   " + mEthDeviceIdBack.size());

	AllDeviceCallBack.getInstance().initCallBack();
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
			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
			   mEPCDate.putAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			}
		   }
		   if (k == boxIdBeansss.size()) {
			LogUtils.i(TAG, "mEPCDate  zou l  ");
			k = 0;
			getDeviceDate(event.deviceId, mEPCDate);
		   }
		} else {
		   LogUtils.i(TAG, "event.epcs直接走   " + event.epcs.size());
		   LogUtils.i(TAG, "event.deviceId   " + event.deviceId);
		   getDeviceDate(event.deviceId, event.epcs);
		}
	   }
	}
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_selinoutbox_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	if (mStarts == null) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyLeft, mTimelyRight);
	   mStarts.cancel();
	}
   }

   private void setInBoxDate() {
	if (mDtoOperation == 8) {
	   mBaseTabTvTitle.setText("耗材退货");
	} else if (mDtoOperation == 3) {
	   mBaseTabTvTitle.setText("耗材领用");
	} else if (mDtoOperation == 2) {
	   mBaseTabTvTitle.setText("耗材入库");
	} else if (mDtoOperation == 9) {
	   mBaseTabTvTitle.setText("耗材移出");
	} else if (mDtoOperation == 11) {
	   mBaseTabTvTitle.setText("耗材调拨");
	} else if (mDtoOperation == 10) {
	   mBaseTabTvTitle.setText("耗材移入");
	} else if (mDtoOperation == 7) {
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
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : mInventoryVos) {
	   strings.add(vosBean.getCstCode());
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							mInventoryVos.size() + "</big></font>"));

	mOperation = mInventoryDto.getOperation();
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(this, this, mInventoryVos, titeleList, mSize, mLinearLayout,
						   mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_IN,
						   mOperation);
	}
	setTimeStart();
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn, R.id.timely_open_door,
	   R.id.timely_left, R.id.timely_right})
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
				startActivity(
					new Intent(SelInOutBoxTwoActivity.this, MyInfoActivity.class));
				break;
			   case 1:
				startActivity(
					new Intent(SelInOutBoxTwoActivity.this, LoginInfoActivity.class));
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
		EventBusUtils.postSticky(new Event.EventFrag("START1"));
		finish();
		break;
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (!mIsClick) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
			moreStartScan();
		   } else {
			ToastUtils.showShort("请关闭柜门，再进行操作！");
		   }
		}
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick()) {
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
		if (UIUtils.isFastDoubleClick()) {
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
	mTimelyLeft.setEnabled(true);
	mTimelyRight.setEnabled(true);
	mEPCDate.clear();
	List<DeviceInventoryVo> deviceInventoryVos = mInventoryDto.getDeviceInventoryVos();
	mInventoryDto.getInventoryVos().clear();
	if (null != deviceInventoryVos) {
	   deviceInventoryVos.clear();
	}
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   startScan(deviceCode);
	}
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
		if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
		    mDtoOperation == 3) {
		   ContentValues values = new ContentValues();
		   values.put("status", "3");
		   values.put("operationstatus", "3");
		   values.put("renewtime", getDates());
		   values.put("accountid", SPUtils.getString(mContext, KEY_ACCOUNT_ID));
		   values.put("username", SPUtils.getString(mContext, KEY_USER_NAME));
		   for (InventoryVo s : dto.getInventoryVos()) {
			LitePal.updateAll(InventoryVo.class, values, "epc = ?", s.getEpc());
		   }
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

	   }
	});
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
	inventoryDto.setOperation(mDtoOperation);
	inventoryDto.setDeviceInventoryVos(deviceList);
	inventoryDto.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	LogUtils.i(TAG, "mGson    " + (mGson == null));
	LogUtils.i(TAG, "inventoryDto    " + (inventoryDto == null));
	String toJson = mGson.toJson(inventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	mEPCDate.clear();
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
		    mDtoOperation == 3) {
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
	inventoryDto.setOperation(mDtoOperation);
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
	if (mTCstInventoryTwoDto.getErrorEpcs() != null &&
	    mTCstInventoryTwoDto.getErrorEpcs().size() > 0) {
	   string = StringUtils.listToString(mTCstInventoryTwoDto.getErrorEpcs());
	   ToastUtils.showLong(string);
	   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
	} else {
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
		mTimelyOpenDoor.setEnabled(true);
		mTimelyStartBtn.setEnabled(true);
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

	super.onDestroy();
   }

   @Override
   protected void onResume() {
	setInBoxDate();
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
		  mDtoOperation != 8) ||
		 (mDtoOperation == 3 && UIUtils.getConfigType(mContext, CONFIG_007) &&
		  b.getPatientName() == null)) {
		if (mDtoOperation == 8 && b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
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
