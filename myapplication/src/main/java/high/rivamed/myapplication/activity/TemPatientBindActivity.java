package high.rivamed.myapplication.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.CreatTempPatientBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
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
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TempPatientDialog;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack2;

/*
 * 患者列表页面,可以创建临时患者
 * */
public class TemPatientBindActivity extends BaseSimpleActivity {

   private static final String TAG    = "TemPatientBindActivity";
   private              int    mRbKey = 3;

   @BindView(R.id.dialog_right)
   TextView           mDialogRight;
   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.activity_down_btn_one_ll)
   LinearLayout       mDownBtnOneLL;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.stock_search)
   FrameLayout        mStockSearch;
   @BindView(R.id.ly_creat_temporary_btn)
   TextView           mLyCreatTemporaryBtn;
   @BindView(R.id.activity_down_btn_seven_ll)
   LinearLayout       mActivityDownBtnSevenLl;
   public List<BoxSizeBean.TbaseDevicesBean> mTemPTbaseDevices = new ArrayList<>();
   private int mPosition;
   private String  mName  = "";
   private String  mId    = "";
   private boolean mPause = true;
   private String  mType  = "";
   private String  mGoneType  = "";
   private String mOperationScheduleId;
   private String mTempPatientId;
   private Map<String, List<TagInfo>> mEPCDate = new TreeMap<>();
   private LoadingDialog.Builder mLoading;
   int k = 0;
   private int    mAllPage = 1;
   private int    mRows    = 20;
   private String mTrim    = "";
   private CreatTempPatientBean mPatientBean;
   public  TableTypeView        mTypeView;
   public List<BingFindSchedulesBean.PatientInfosBean> patientInfos = new ArrayList<>();
   List<String> titeleList = null;
   public int mSize;

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

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	AllDeviceCallBack.getInstance().initCallBack();
	mTemPTbaseDevices = (List<BoxSizeBean.TbaseDevicesBean>) getIntent().getSerializableExtra("mTemPTbaseDevices");
	mPosition = getIntent().getIntExtra("position", -1);
	mType = getIntent().getStringExtra("type");
	mGoneType = getIntent().getStringExtra("GoneType");
	if (null != mType && mType.equals("afterBindTemp")){
	   mBaseTabOutLogin.setEnabled(false);
	   mBaseTabIconRight.setEnabled(false);
	   mBaseTabBtnMsg.setEnabled(false);
	   mBaseTabTvName.setEnabled(false);
	   mBaseTabBack.setVisibility(View.GONE);
	}else {
	   mBaseTabOutLogin.setEnabled(true);
	   mBaseTabIconRight.setEnabled(true);
	   mBaseTabBtnMsg.setEnabled(true);
	   mBaseTabTvName.setEnabled(true);
	   mBaseTabBack.setVisibility(View.VISIBLE);
	}
	//套组--界面
	if (mPosition == -1000) {
	   mDownBtnOneLL.setVisibility(View.GONE);
	}
	//无临时患者
	if (mGoneType==null ||mGoneType.equals("GONE")) {
	   mLyCreatTemporaryBtn.setVisibility(View.GONE);
	}else {
	   mLyCreatTemporaryBtn.setVisibility(View.VISIBLE);
	}

	loadBingDate("");
	setTemporaryBing();
	initListener();
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_tempatientbind_layout;
   }

   /**
    * 患者列表
    */
   private void setTemporaryBing() {
	mBaseTabTvTitle.setText("患者列表");
	mStockSearch.setVisibility(View.VISIBLE);
	mActivityDownBtnSevenLl.setVisibility(View.VISIBLE);
	String[] array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
	titeleList = Arrays.asList(array);
	mSize = titeleList.size();
	mTypeView = new TableTypeView(mContext, this, (Object) patientInfos, titeleList, mSize,
						mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						STYPE_DIALOG, -10);
   }

   private void initListener() {
	mDialogRight.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
		if (patientInfos != null && patientInfos.size() > 0) {
		   mPause = false;
		   mName = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getPatientName();
		   mId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getPatientId();
		   mOperationScheduleId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getOperationScheduleId();
		   mTempPatientId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getTempPatientId();
		   if (null != mType && mType.equals("afterBindTemp")) {
			//后绑定患者
			if (mId.equals("virtual")) {
			   if (mPatientBean == null) {
				LogUtils.i(TAG, "mPatientBean ");
				EventBusUtils.postSticky(new Event.EventCheckbox(mName, mId, mTempPatientId,
												 mOperationScheduleId,
												 mType, mPosition,
												 mTemPTbaseDevices));
			   } else {
				LogUtils.i(TAG, "mPatientBean2 ");
				String deptId = mPatientBean.getTTransOperationSchedule().getDeptId();
				String name = mPatientBean.getTTransOperationSchedule().getName();
				String idNo = mPatientBean.getTTransOperationSchedule().getIdNo();
				String scheduleDateTime = mPatientBean.getTTransOperationSchedule()
					.getScheduleDateTime();
				String operatingRoomNo = mPatientBean.getTTransOperationSchedule()
					.getOperatingRoomNo();
				String operatingRoomNoName = mPatientBean.getTTransOperationSchedule()
					.getOperatingRoomNoName();
				String sex = mPatientBean.getTTransOperationSchedule().getSex();
				boolean create = mPatientBean.getTTransOperationSchedule().isCreate();
				EventBusUtils.postSticky(
					new Event.EventCheckbox(name, mId, idNo, scheduleDateTime,
									operatingRoomNo, operatingRoomNoName, sex,
									deptId, create, mType, mPosition,
									mTemPTbaseDevices));
			   }
			} else {
			   EventBusUtils.postSticky(
				   new Event.EventCheckbox(mName, mId, mTempPatientId, mOperationScheduleId,
								   mType, mPosition, mTemPTbaseDevices));
			}
			finish();
		   } else {
			//先绑定患者
			EventBusUtils.post(new Event.EventButton(true, true));
			AllDeviceCallBack.getInstance().openDoor(mPosition, mTemPTbaseDevices);
		   }
		} else {
		   ToastUtils.showShort("暂无数据，请创建后再试！");
		}
	   }
	});
	mSearchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	   }

	   @Override
	   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		mTrim = charSequence.toString().trim();
		mAllPage = 1;
		patientInfos.clear();
		loadBingDate(mTrim);
	   }

	   @Override
	   public void afterTextChanged(Editable editable) {

	   }
	});
	mTypeView.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		mRefreshLayout.setNoMoreData(false);
		mAllPage = 1;
		patientInfos.clear();
		loadBingDate(mTrim);
		mRefreshLayout.finishRefresh();
	   }
	});
	mTypeView.mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		mAllPage++;
		loadBingDate(mTrim);
		mRefreshLayout.finishLoadMore();
	   }
	});
   }

   @Override
   protected void onResume() {
	mPause = false;
	super.onResume();
   }

   @Override
   protected void onDestroy() {
	patientInfos.clear();
	super.onDestroy();
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
			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
			   mEPCDate.putAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
			}
		   }
		   if (k == boxIdBeansss.size()) {
			if (!mPause) {
			   k = 0;
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

   @Override
   public void onPause() {
	mPause = true;
	EventBusUtils.unregister(this);
	super.onPause();
   }

   /**
    * 扫描后传值
    */
   private void getDeviceDate(String deviceId, Map<String, List<TagInfo>> epcs) {
	TCstInventoryDto tCstInventoryDto = new TCstInventoryDto();
	List<TCstInventory> epcList = new ArrayList<>();
	for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
	   TCstInventory tCstInventory = new TCstInventory();
	   tCstInventory.setEpc(v.getKey());
	   epcList.add(tCstInventory);
	}
	DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
	List<DeviceInventoryVo> deviceList = new ArrayList<>();
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   Log.i(TAG, "device_id   " + box_id);
	   if (box_id != null) {
		deviceInventoryVo.setDeviceCode(box_id);
	   }
	}
	deviceInventoryVo.settCstInventories(epcList);
	deviceList.add(deviceInventoryVo);
	tCstInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
	tCstInventoryDto.setDeviceInventoryVos(deviceList);
	tCstInventoryDto.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 || mRbKey == 10 ||
	    mRbKey == 7 || mRbKey == 8) {
	   tCstInventoryDto.setOperation(mRbKey);
	} else {
	   tCstInventoryDto.setOperation(mRbKey);
	}
	if (mRbKey == 3) {
	   if (patientInfos != null) {
		mPause = false;
		mName = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getPatientName();
		mId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getPatientId();
		mOperationScheduleId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			.getOperationScheduleId();
	   }
	   tCstInventoryDto.setPatientName(mName);
	   tCstInventoryDto.setPatientId(mId);
	   tCstInventoryDto.setOperationScheduleId(mOperationScheduleId);
	}
	String toJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	mEPCDate.clear();
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		TCstInventoryDto cstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
		String string = null;
		if (cstInventoryDto.getErrorEpcs() != null &&
		    cstInventoryDto.getErrorEpcs().size() > 0) {
		   string = StringUtils.listToString(cstInventoryDto.getErrorEpcs());
		   ToastUtils.showLong(string);
		   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
		   return;
		}
		LogUtils.i(TAG, "我跳转    " + (cstInventoryDto.gettCstInventoryVos() == null));
		//先绑定患者
		if (mRbKey == 3) {
		   String patientName = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getPatientName();
		   boolean create = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .isCreate();
		   String patientId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getPatientId();
		   String tempPatientId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getTempPatientId();
		   String idNo = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getIdNo();
		   String operationScheduleId = patientInfos.get(
			   mTypeView.mTempPatientAdapter.mSelectedPos).getOperationScheduleId();
		   String scheduleDateTime = patientInfos.get(
			   mTypeView.mTempPatientAdapter.mSelectedPos).getScheduleDateTime();
		   String operatingRoomNo = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getOperatingRoomNo();
		   String operatingRoomNoName = patientInfos.get(
			   mTypeView.mTempPatientAdapter.mSelectedPos).getOperatingRoomNoName();
		   String sex = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getSex();
		   String deptId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
			   .getDeptId();
		   for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {

			tCstInventoryVo.setPatientName(patientName);
			tCstInventoryVo.setCreate(create);
			tCstInventoryVo.setPatientId(patientId);
			tCstInventoryVo.setTempPatientId(tempPatientId);
			tCstInventoryVo.setIdNo(idNo);
			tCstInventoryVo.setOperationScheduleId(operationScheduleId);
			tCstInventoryVo.setScheduleDateTime(scheduleDateTime);
			tCstInventoryVo.setOperatingRoomNo(operatingRoomNo);
			tCstInventoryVo.setOperatingRoomNoName(operatingRoomNoName);
			tCstInventoryVo.setSex(sex);
			tCstInventoryVo.setDeptId(deptId);

		   }
		   TCstInventoryDto dto = new TCstInventoryDto();
		   dto.setPatientName(patientName);
		   dto.setCreate(create);
		   dto.setPatientId(patientId);
		   dto.setTempPatientId(tempPatientId);
		   dto.setIdNo(idNo);
		   dto.setOperationScheduleId(operationScheduleId);
		   dto.setScheduleDateTime(scheduleDateTime);
		   dto.setOperatingRoomNo(operatingRoomNo);
		   dto.setOperatingRoomNoName(operatingRoomNoName);
		   dto.setSex(sex);
		   dto.setDeptId(deptId);
		   dto.setBindType("firstBind");
		   dto.setOperation(mRbKey);
		   EventBusUtils.postSticky(new Event.EventOutBoxBingDto(cstInventoryDto,dto));
		   if (cstInventoryDto.gettCstInventoryVos() != null &&
			 cstInventoryDto.gettCstInventoryVos().size() != 0) {
			EventBusUtils.post(new Event.EventButton(true, true));
			mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
			finish();
		   } else {
			Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
		   }
		}

	   }
	});
   }

   @OnClick({ R.id.dialog_left, R.id.base_tab_tv_name,
	   R.id.base_tab_tv_outlogin, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back,  R.id.ly_creat_temporary_btn,})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:

	   case R.id.base_tab_tv_name:
		if (UIUtils.getConfigType(mContext, CONFIG_010)) {//先绑定患者
		   if (mEthDeviceIdBack2.size() == 0) {
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
		   } else {
			ToastUtils.showShort("请关闭柜门！");
		   }
		}
		break;
	   case R.id.base_tab_tv_outlogin:
		if (UIUtils.getConfigType(mContext, CONFIG_010)) {
		   if (mEthDeviceIdBack2.size() == 0) {
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
		   } else {
			ToastUtils.showShort("请关闭柜门！");
		   }
		}
		break;
	   case R.id.base_tab_back:
		if (UIUtils.getConfigType(mContext, CONFIG_010)) {
		   if (mEthDeviceIdBack2.size() == 0) {
			finish();
		   } else {
			ToastUtils.showShort("请关闭柜门！");
		   }
		} else {
		   finish();
		}
		break;
	   case R.id.base_tab_btn_msg://消息
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		  startActivity(new Intent(this, MessageActivity.class));
		}
		break;
	   case R.id.ly_creat_temporary_btn://创建临时患者
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   DialogUtils.showCreatTempPatientDialog(mContext, TemPatientBindActivity.this,
									new TempPatientDialog.Builder.SettingListener() {
									   @Override
									   public void getDialogDate(
										   String userName, String roomNum,
										   String roomId, String userSex,
										   String idCard, String time,
										   Dialog dialog) {
										Log.e(TAG,
											"showCreatTempPatientDialogaaaa");
										creatTemPatient(
											new Event.tempPatientEvent(
												userName, roomNum, roomId,
												userSex, idCard, time,
												dialog));
									   }
									});
		}
		break;
	   case R.id.dialog_left://取消
		if (UIUtils.getConfigType(mContext, CONFIG_010)) {
		   if (mEthDeviceIdBack2.size() == 0) {
			finish();
		   } else {
			ToastUtils.showShort("请关闭柜门！");
		   }
		} else {
		   finish();
		}
		break;
	}
   }

   /*
    * 创建临时患者
    * */
   private void creatTemPatient(Event.tempPatientEvent event) {
	mPatientBean = new CreatTempPatientBean();
	CreatTempPatientBean.TTransOperationScheduleBean bean = new CreatTempPatientBean.TTransOperationScheduleBean();
	bean.setName(event.userName);
	bean.setIdNo(event.idCard);//身份证
	bean.setScheduleDateTime(event.time);
	bean.setOperatingRoomNo(event.operatingRoomNo);
	bean.setOperatingRoomNoName(event.roomNum);
	bean.setSex(event.userSex);
	bean.setCreate(true);
	bean.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE, ""));
	mPatientBean.setTTransOperationSchedule(bean);
	if (patientInfos != null) {
	   BingFindSchedulesBean.PatientInfosBean data2 = new BingFindSchedulesBean.PatientInfosBean();
	   data2.setPatientId("virtual");
	   data2.setPatientName(event.userName);
	   data2.setCreate(true);
	   data2.setIdNo(event.idCard);//身份证
	   data2.setScheduleDateTime(event.time);
	   data2.setOperatingRoomNo(event.operatingRoomNo);
	   data2.setOperatingRoomNoName(event.roomNum);
	   data2.setSex(event.userSex);
	   data2.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE, ""));
	   patientInfos.add(0, data2);
	   ToastUtils.showShort("创建成功");
	   event.dialog.dismiss();
	   for (BingFindSchedulesBean.PatientInfosBean s : patientInfos) {
		s.setSelected(false);
	   }
	   patientInfos.get(0).setSelected(true);
	   mTypeView.mTempPatientAdapter.notifyDataSetChanged();
	   mTypeView.mRecyclerview.scrollToPosition(0);
	}

   }

   /**
    * 获取需要绑定的患者
    */
   private void loadBingDate(String optienNameOrId) {

	NetRequest.getInstance()
		.findSchedulesDate(optienNameOrId, mAllPage, mRows, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result   " + result);
			FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
			if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
			   boolean isClear;
			   if (patientInfos.size() == 0) {
				isClear = true;
			   } else {
				isClear = false;
			   }
			   for (int i = 0; i < bean.getRows().size(); i++) {
				BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
				data.setPatientId(bean.getRows().get(i).getPatientId());
				data.setTempPatientId(bean.getRows().get(i).getTempPatientId());
				data.setPatientName(bean.getRows().get(i).getPatientName());
				data.setDeptName(bean.getRows().get(i).getDeptName());
				data.setOperationSurgeonName(
					bean.getRows().get(i).getOperationSurgeonName());
				data.setOperatingRoomNoName(bean.getRows().get(i).getOperatingRoomNoName());
				data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
				data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
				data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
				patientInfos.add(data);
			   }
			   if (isClear && patientInfos.size() > 0) {
				patientInfos.get(0).setSelected(true);
			   }
			   mTypeView.mTempPatientAdapter.notifyDataSetChanged();
			}
		   }
		});
   }
}