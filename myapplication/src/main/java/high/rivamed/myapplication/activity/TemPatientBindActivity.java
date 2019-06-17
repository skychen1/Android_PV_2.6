package high.rivamed.myapplication.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.CreatTempPatientBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TempPatientDialog;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.TEMP_AFTERBIND;
import static high.rivamed.myapplication.cont.Constants.TEMP_FIRSTBIND;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack2;

/*
 * 患者列表页面,可以创建临时患者
 * TODO: 该界面代码后续优化
 * */
public class TemPatientBindActivity extends BaseSimpleActivity {

   private static final String TAG    = "TemPatientBindActivity";
   private              int    mRbKey;

   @BindView(R.id.dialog_right)
   TextView           mDialogRight;
   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.search_et_right)
   EditText           mSearchRight;
   @BindView(R.id.activity_down_btn_one_ll)
   LinearLayout       mDownBtnOneLL;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.stock_search)

   LinearLayout        mStockSearch;
   @BindView(R.id.stock_search_right)
   LinearLayout        mStockSearchRight;

   @BindView(R.id.search_dept)
   TextView           mSearchDept;
   @BindView(R.id.ly_creat_temporary_btn)
   TextView           mLyCreatTemporaryBtn;
   @BindView(R.id.activity_down_btn_seven_ll)
   LinearLayout       mActivityDownBtnSevenLl;
   public List<BoxSizeBean.DevicesBean> mTemPTbaseDevices = new ArrayList<>();
   private int mPosition;
//   private String  mName  = "";
//   private String  mId    = "";
   private boolean mPause = true;
   private String  mType  = "";
   private String  mGoneType  = "";
//   private String mOperationScheduleId;
//   private String mMedicalId;
//   private String mOperatingRoomNo;
//   private String mSurgeryId;
//   private String mHisPatientId;
//   private String mTempPatientId;
//   private Map<String, List<EpcInfo>> mEPCDate = new TreeMap<>();
//   private LoadingDialog.Builder mLoading;
   int k = 0;
   private int    mAllPage = 1;
   private int    mRows    = 20;
   private String mTrim    = "";
   private String mTrims    = "";
   private CreatTempPatientBean mPatientBean;
   public  TableTypeView        mTypeView;
   public List<BingFindSchedulesBean.PatientInfoVos> patientInfos = new ArrayList<>();
   List<String> titeleList = null;
   public int mSize;
   private String mDeptType;
//
//   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//   public void onEventLoading(Event.EventLoading event) {
//	if (event.loading) {
//	   if (mLoading == null) {
//		mLoading = DialogUtils.showLoading(this);
//	   } else {
//		if (!mLoading.mDialog.isShowing()) {
//		   mLoading.create().show();
//		}
//	   }
//	} else {
//	   if (mLoading != null) {
//		mLoading.mAnimationDrawable.stop();
//		mLoading.mDialog.dismiss();
//		mLoading = null;
//	   }
//	}
//   }
   /**
    * 门锁的提示
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
	}
	if (!event.isMute) {
	   MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
	   Log.i("FFASD","mType   "+mType );

	   if (null == mType || !mType.equals(TEMP_AFTERBIND)){//后绑定患者
	     setTempPatientDate(event.mEthId);
	  }

	}

   }
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
//	if (mLoading != null) {
//	   mLoading.mAnimationDrawable.stop();
//	   mLoading.mDialog.dismiss();
//	   mLoading = null;
//	}
//	AllDeviceCallBack.getInstance().initCallBack();
	mTemPTbaseDevices = (List<BoxSizeBean.DevicesBean>) getIntent().getSerializableExtra("mTemPTbaseDevices");
	mPosition = getIntent().getIntExtra("position", -1);
	mRbKey = getIntent().getIntExtra("mRbKey", -2);
	mType = getIntent().getStringExtra("type");
	mGoneType = getIntent().getStringExtra("GoneType");
	if (null != mType && mType.equals(TEMP_AFTERBIND)){
	   mBaseTabOutLogin.setEnabled(false);
	   mBaseTabIconRight.setEnabled(false);
	   mBaseTabBtnMsg.setEnabled(false);
	   mBaseTabTvName.setEnabled(false);
	   mBaseTabBack.setVisibility(View.VISIBLE);
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
	mBaseTabTvTitle.setText("患者列表");
	mStockSearch.setVisibility(View.VISIBLE);
	mStockSearchRight.setVisibility(View.VISIBLE);
	mActivityDownBtnSevenLl.setVisibility(View.VISIBLE);
	if (mTitleConn){
	   loadBingDate("","");
	}else {
	   setTemporaryBing(null);
	}
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_tempatientbind_layout;
   }

   /**
    * 患者列表
    */
   private void setTemporaryBing(String deptType) {
	String[] array;
	if (deptType==null||deptType.equals("2")||deptType.equals("")){
	   array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
	   mSearchDept.setText("查询手术间：");
	   mSearchRight.setHint("请输入手术间名称、编号、拼音码");
	}else {
	   array = mContext.getResources().getStringArray(R.array.six_dialog_arrays2);
	   mSearchDept.setText("查询申请科室：");
	   mSearchRight.setHint("请输入原科室名称、拼音码");
	}
	titeleList = Arrays.asList(array);
	mSize = titeleList.size();
	if (mTypeView==null){
	   mTypeView = new TableTypeView(mContext, this, (Object) patientInfos, titeleList, mSize,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   STYPE_DIALOG, -10);
	}
	initListener();

   }

   private void initListener() {

	mTypeView.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		mRefreshLayout.setNoMoreData(false);
		mAllPage = 1;
		patientInfos.clear();
		loadBingDate(mTrim,mTrims);
		mRefreshLayout.finishRefresh();
	   }
	});
	mTypeView.mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		mAllPage++;
		loadBingDate(mTrim,mTrims);
		mRefreshLayout.finishLoadMore();
	   }
	});
   }

   @Override
   protected void onResume() {
	mPause = false;
	super.onResume();
	mSearchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	   }

	   @Override
	   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		mTrim = charSequence.toString().trim();
		mAllPage = 1;
		patientInfos.clear();
		loadBingDate(mTrim,mTrims);
	   }

	   @Override
	   public void afterTextChanged(Editable editable) {

	   }
	});

	mSearchRight.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i("FFASD","onTextChanged");
		mTrims = s.toString().trim();
		mAllPage = 1;
		patientInfos.clear();
		loadBingDate(mTrim,mTrims);
	   }

	   @Override
	   public void afterTextChanged(Editable s) {

	   }
	});
   }

   @Override
   protected void onDestroy() {
	patientInfos.clear();
	mTrim="";
	super.onDestroy();
   }

   @Override
   public void onPause() {
	mPause = true;
	EventBusUtils.unregister(this);
	super.onPause();
   }

//   /**
//    * 扫描后传值
//    */
//   private void getDeviceDate(String deviceId, Map<String, List<EpcInfo>> epcs) {
//	InventoryDto inventoryDto = new InventoryDto();
//	List<Inventory> epcList = new ArrayList<>();
//	for (Map.Entry<String, List<EpcInfo>> v : epcs.entrySet()) {
//	   Inventory inventory = new Inventory();
//	   inventory.setEpc(v.getKey());
//	   epcList.add(inventory);
//	}
//	DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
//	List<DeviceInventoryVo> deviceList = new ArrayList<>();
//	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId).find(BoxIdBean.class);
//	for (BoxIdBean boxIdBean : boxIdBeans) {
//	   String box_id = boxIdBean.getBox_id();
//	   Log.i(TAG, "device_id   " + box_id);
//	   if (box_id != null) {
//		deviceInventoryVo.setDeviceId(box_id);
//	   }
//	}
//	deviceInventoryVo.setInventories(epcList);
//	deviceList.add(deviceInventoryVo);
//	inventoryDto.setThingId(SPUtils.getString(mContext, THING_CODE));
//	inventoryDto.setDeviceInventoryVos(deviceList);
//	inventoryDto.setSthId(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
//	inventoryDto.setOperation(mRbKey);
//	if (mRbKey == 3||mRbKey == 4) {
//	   if (patientInfos != null) {
//		mPause = false;
//		mName = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getPatientName();
//		mId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getPatientId();
//		mOperationScheduleId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
//			.getOperationScheduleId();
//		mOperatingRoomNo = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
//			.getOperatingRoomNo();
//		mMedicalId= patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
//			.getMedicalId();
//		mSurgeryId= patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
//			.getSurgeryId();
//		mHisPatientId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getHisPatientId();
//	   }
//	   inventoryDto.setPatientName(mName);
//	   inventoryDto.setPatientId(mId);
//	   inventoryDto.setOperationScheduleId(mOperationScheduleId);
//	   inventoryDto.setMedicalId(mMedicalId);
//	   inventoryDto.setOperatingRoomNo(mOperatingRoomNo);
//	   inventoryDto.setSurgeryId(mSurgeryId);
//	   inventoryDto.setHisPatientId(mHisPatientId);
//	}
//	String toJson = mGson.toJson(inventoryDto);
//	LogUtils.i(TAG, "toJson    " + toJson);
//	mEPCDate.clear();
//	if (mRbKey == 4){
//	   if (mTitleConn) {
//		queryEpcLyThDate(toJson);
//	   }else {
//		queryErrorEpcLyTh("-1", toJson,4);
//	   }
//	}else {
//	   if (mTitleConn) {
//		queryEpcLyDate(toJson);
//	   }else {
//		queryErrorEpc("-1", toJson,3);
//	   }
//	}
//
//   }
//
//   /**
//    * 查询EPC信息领用
//    * @param toJson
//    */
//   private void queryEpcLyDate(String toJson) {
//	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
//	   @Override
//	   public void onSucceed(String result) {
//		Log.i(TAG, "result    " + result);
//		InventoryDto cstInventoryDto = mGson.fromJson(result, InventoryDto.class);
//		LogUtils.i(TAG, "我跳转    " + (cstInventoryDto.getInventoryVos() == null));
//		//绑定患者
//		   setTempPatientDate(cstInventoryDto);
//	   }
//	   @Override
//	   public void onError(String result) {
//		queryErrorEpc(result, toJson,3);
//	   }
//	});
//   }
//
//   /**
//    * 断网查询EPC
//    * @param result
//    * @param toJson
//    * @param type
//    */
//   private void queryErrorEpc(String result, String toJson,int type) {
//	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
//	    mRbKey == type) {
//	   queryErrorEpcDate(toJson);
//	}
//	EventBusUtils.postSticky(new Event.EventLoading(false));
//   }
//
//   /**
//    * 查询EPC信息（领用退回）
//    * @param toJson
//    */
//   private void queryEpcLyThDate(String toJson) {
//	NetRequest.getInstance().putEPCLyThDate(toJson, this, new BaseResult() {
//	   @Override
//	   public void onSucceed(String result) {
//		Log.i(TAG, "result    " + result);
//		InventoryDto cstInventoryDto = mGson.fromJson(result, InventoryDto.class);
//		LogUtils.i(TAG, "我跳转    " + (cstInventoryDto.getInventoryVos() == null));
//		//绑定患者
//		setTempPatientDate(cstInventoryDto);
//	   }
//	   @Override
//	   public void onError(String result) {
//		queryErrorEpcLyTh(result, toJson,4);
//	   }
//	});
//   }
//
//   /**
//    * 断网查询领用退回
//    * @param result
//    * @param toJson
//    * @param type
//    */
//   private void queryErrorEpcLyTh(String result, String toJson,int type) {
//	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
//	    mRbKey == type) {
//	   queryErrorEpcDate(toJson);
//	}
//	EventBusUtils.postSticky(new Event.EventLoading(false));
//   }
//
//   /**
//    * 断网查询后的数据
//    * @param toJson
//    */
//   private void queryErrorEpcDate(String toJson) {
//	List<InventoryVo> mInVo = new ArrayList<>();
//	InventoryDto cc = LitePal.findFirst(InventoryDto.class);
//	InventoryDto inventoryDto = new InventoryDto();
//	inventoryDto.setThingId(cc.getThingId());
//	inventoryDto.setOperation(mRbKey);
//	LogUtils.i(TAG, "FDFDF0   " + mGson.toJson(inventoryDto));
//	InventoryDto dto = mGson.fromJson(toJson, InventoryDto.class);
//	if (dto.getDeviceInventoryVos().size() > 0) {
//	   List<Inventory> list = dto.getDeviceInventoryVos().get(0).getInventories();
//	   String deviceId = dto.getDeviceInventoryVos().get(0).getDeviceId();
//
//	   List<InventoryVo> vos = LitePal.where("deviceid = ? and status = ?", deviceId,
//							     "2").find(InventoryVo.class);
//	   BoxIdBean boxIdBean = LitePal.where("device_id = ? ", deviceId)
//		   .findFirst(BoxIdBean.class);
//	   mInVo.addAll(vos);
//	   if (list.size() != 0) {
//		for (Inventory s : list) {
//		   InventoryVo first = LitePal.where("epc = ? and deviceid = ?", s.getEpc(),
//								 deviceId).findFirst(InventoryVo.class);
//		   if (!getVosType(vos, s.getEpc())) {//无网放入
//			InventoryVo inventoryVo = new InventoryVo();
//			inventoryVo.setEpc(s.getEpc());
//			inventoryVo.setDeviceId(deviceId);
//			inventoryVo.setDeviceName(boxIdBean.getName());
//			inventoryVo.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
//			inventoryVo.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
//			inventoryVo.setIsErrorOperation(0);
//			inventoryVo.setOperationStatus(99);
//			inventoryVo.setStatus("2");
//			inventoryVo.setRenewTime(getDates());
//			mInVo.add(inventoryVo);
//		   } else {
//			if (first != null) {
//			   mInVo.remove(first);
//			}
//		   }
//		}
//	   }
//	}
//	inventoryDto.setInventoryVos(mInVo);
//	setTempPatientDate(inventoryDto);
//   }

   /**
    * 先绑定患者，关门后获取选中的患者信息，并跳转界面
    */
   private void setTempPatientDate(String mEthId) {
	BingFindSchedulesBean.PatientInfoVos patientInfoVos = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos);
	InventoryVo vo = setBingPatientInfoDate(patientInfoVos);
	vo.setCreate(patientInfoVos.isCreate());
	vo.setIdNo(patientInfoVos.getIdNo());
	vo.setSurgeryTime(patientInfoVos.getSurgeryTime());
	vo.setOperatingRoomName(patientInfoVos.getRoomName());
	vo.setSex(patientInfoVos.getSex());
	vo.setDeptId(patientInfoVos.getDeptId());
	vo.setBindType(mType);
	EventBusUtils.post(new Event.EventCheckbox(vo));

	startActivity(new Intent(mContext, OutBoxBingActivity.class).putExtra("basePatientVo",vo).putExtra("OperationType", mRbKey)
				  .putExtra("bindType", TEMP_FIRSTBIND)
				  .putExtra("mEthId", mEthId));
	finish();

//	InventoryDto dto = new InventoryDto();
//	dto.setPatientName(patientName);
//	dto.setCreate(create);
//	dto.setPatientId(patientId);
//	dto.setTempPatientId(tempPatientId);
//	dto.setIdNo(idNo);
//	dto.setOperationScheduleId(operationScheduleId);
//	dto.setSurgeryTime(scheduleDateTime);
//	dto.setOperatingRoomNo(operatingRoomNo);
//	dto.setOperatingRoomNoName(operatingRoomNoName);
//	dto.setSex(sex);
//	dto.setDeptId(deptId);
//	dto.setBindType("firstBind");
//	dto.setOperation(mRbKey);
//	dto.setMedicalId(medicalId);
//	dto.setSurgeryId(surgeryId);
//	dto.setHisPatientId(hisPatientId);
//	EventBusUtils.postSticky(new Event.EventOutBoxBingDto(cstInventoryDto, dto));
//	if (cstInventoryDto.getInventoryVos() != null &&
//	    cstInventoryDto.getInventoryVos().size() != 0) {
//	   EventBusUtils.post(new Event.EventButton(true, true));
//	   EventBusUtils.postSticky(new Event.EventLoading(false));
//	   mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
//	   finish();
//	} else {
//	   EventBusUtils.postSticky(new Event.EventLoading(false));
//	   Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
//	}


   }

   @OnClick({ R.id.dialog_left,R.id.dialog_right,R.id.base_tab_tv_name,
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
		if (UIUtils.isFastDoubleClick(R.id.base_tab_btn_msg)) {
		   return;
		} else {
		  startActivity(new Intent(this, MessageActivity.class));
		}
		break;
	   case R.id.ly_creat_temporary_btn://创建临时患者
		if (UIUtils.isFastDoubleClick(R.id.ly_creat_temporary_btn)) {
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
	   case R.id.dialog_right://确认
		if (!UIUtils.isFastDoubleClick(R.id.dialog_right)){
		   setRightEnter();
		}
		break;
	}
   }

   /**
    * 确定按钮的逻辑
    */
   private void setRightEnter() {
	if (patientInfos != null && patientInfos.size() > 0) {
	   mPause = false;
	   BingFindSchedulesBean.PatientInfoVos patientInfoVos = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos);
	   InventoryVo vo = setBingPatientInfoDate(patientInfoVos);
	   if (null != mType && mType.equals(TEMP_AFTERBIND)) {
		//后绑定患者
		if (patientInfoVos.getPatientId().equals("virtual")) {
		   if (mPatientBean == null) {
			vo.setBindType(mType);
			EventBusUtils.post(new Event.EventCheckbox(vo));
		   } else {
			CreatTempPatientBean.TTransOperationScheduleBean schedule = mPatientBean.getTTransOperationSchedule();
			InventoryVo vvo = new InventoryVo();
			vvo.setPatientName(schedule.getName());
			vvo.setCreate(schedule.isCreate());
			vvo.setIdNo(schedule.getIdNo());
			vvo.setSurgeryTime(schedule.getSurgeryTime());
			vvo.setOperatingRoomNo(schedule.getOperatingRoomNo());
			vvo.setOperatingRoomName(schedule.getOperatingRoomNoName());
			vvo.setSex(schedule.getSex());
			vvo.setDeptId(schedule.getDeptId());
			vvo.setMedicalId(schedule.getMedicalId());

			vvo.setBindType(mType);
			vvo.setPatientId(patientInfoVos.getPatientId());
			EventBusUtils.post(new Event.EventCheckbox(vvo));

		   }
		} else {
		   EventBusUtils.post(new Event.EventCheckbox(vo));
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

   @NonNull
   private InventoryVo setBingPatientInfoDate(BingFindSchedulesBean.PatientInfoVos patientInfoVos) {
	InventoryVo vo = new InventoryVo();
	vo.setPatientName(patientInfoVos.getPatientName());
	vo.setPatientId(patientInfoVos.getPatientId());
	vo.setTempPatientId(patientInfoVos.getTempPatientId());
	vo.setOperationScheduleId(patientInfoVos.getOperationScheduleId());
	vo.setOperatingRoomNo(patientInfoVos.getOperatingRoomNo());
	vo.setMedicalId(patientInfoVos.getMedicalId());

	if (patientInfoVos.getSurgeryId()!=null){
	   vo.setSurgeryId(patientInfoVos.getSurgeryId());
	}
	if (patientInfoVos.getHisPatientId()!=null){
	   vo.setHisPatientId(patientInfoVos.getHisPatientId());
	}

	return vo;
   }

   /*
    * 创建临时患者
    * */
   private void creatTemPatient(Event.tempPatientEvent event) {
	mPatientBean = new CreatTempPatientBean();
	CreatTempPatientBean.TTransOperationScheduleBean bean = new CreatTempPatientBean.TTransOperationScheduleBean();
	bean.setName(event.userName);
	bean.setIdNo(event.idCard);//身份证
	bean.setSurgeryTime(event.time);
	bean.setOperatingRoomNo(event.operatingRoomNo);
	bean.setOperatingRoomNoName(event.roomNum);
	bean.setSex(event.userSex);
	bean.setCreate(true);
	bean.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE, ""));
	bean.setMedicalId("virtual");
	bean.setDeptType(mDeptType);
	mPatientBean.setTTransOperationSchedule(bean);
	if (patientInfos != null) {
	   BingFindSchedulesBean.PatientInfoVos data2 = new BingFindSchedulesBean.PatientInfoVos();
	   data2.setPatientId("virtual");
	   data2.setPatientName(event.userName);
	   data2.setCreate(true);
	   data2.setIdNo(event.idCard);//身份证
	   data2.setSurgeryTime(event.time);
	   data2.setOperatingRoomNo(event.operatingRoomNo);
	   data2.setRoomName(event.roomNum);
	   data2.setSex(event.userSex);
	   data2.setMedicalId("virtual");
	   data2.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE, ""));
	   data2.setDeptType(mDeptType);
	   patientInfos.add(0, data2);
	   ToastUtils.showShort("创建成功");
	   event.dialog.dismiss();
	   for (BingFindSchedulesBean.PatientInfoVos s : patientInfos) {
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
   private void loadBingDate(String optienNameOrId,String deptName) {

	NetRequest.getInstance()
		.findSchedulesDate(optienNameOrId,deptName, mAllPage, mRows, this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result   " + result);
			FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
			if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
			   mDeptType = bean.getRows().get(0).getDeptType();
			   setTemporaryBing(mDeptType);
			   boolean isClear;
			   if (patientInfos.size() == 0) {
				isClear = true;
			   } else {
				isClear = false;
			   }
			   for (int i = 0; i < bean.getRows().size(); i++) {
				BingFindSchedulesBean.PatientInfoVos data = new BingFindSchedulesBean.PatientInfoVos();
				data.setPatientId(bean.getRows().get(i).getPatientId());
				data.setTempPatientId(bean.getRows().get(i).getTempPatientId());
				data.setPatientName(bean.getRows().get(i).getPatientName());
				data.setDeptName(bean.getRows().get(i).getDeptName());
				data.setDoctorName(
					bean.getRows().get(i).getDoctorName());
				data.setRoomName(bean.getRows().get(i).getRoomName());
				data.setSurgeryTime(bean.getRows().get(i).getSurgeryTime());
				data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				data.setSurgeryId(bean.getRows().get(i).getSurgeryId());
				data.setMedicalId(bean.getRows().get(i).getMedicalId());
				data.setHisPatientId(bean.getRows().get(i).getHisPatientId());
				data.setOperatingRoomNo(bean.getRows().get(i).getOperatingRoomNo());
				data.setDeptType(bean.getRows().get(i).getDeptType());
				if (bean.getRows().get(i).getSurgeryId()!=null){
				   data.setSurgeryId(bean.getRows().get(i).getSurgeryId());
				}
				patientInfos.add(data);
			   }
			   if (isClear && patientInfos.size() > 0) {
				patientInfos.get(0).setSelected(true);
			   }
			   mTypeView.mTempPatientAdapter.notifyDataSetChanged();
			}else {
			   setTemporaryBing("2");
			   if (mAllPage==1&&mTypeView!=null&&mTypeView.mTempPatientAdapter!=null){
				mTypeView.mTempPatientAdapter.getData().clear();
				mTypeView.mTempPatientAdapter.notifyDataSetChanged();
			   }
			}
		   }

		   @Override
		   public void onError(String result) {
			super.onError(result);
			setTemporaryBing(null);
		   }
		});
   }
}