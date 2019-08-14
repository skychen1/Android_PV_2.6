package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.rivamed.libdevicesbase.base.DeviceInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegisteSmallAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.DeviceNameBeanX;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.TBaseDevices;
import high.rivamed.myapplication.bean.ThingDto;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVoError;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LogcatHelper;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.WifiUtils;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.cont.Constants.LOGCAT_OPEN;
import static high.rivamed.myapplication.cont.Constants.SAVE_ACTIVATION_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_REGISTE_DATE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP_TEXT;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;
import static high.rivamed.myapplication.cont.Constants.SN_NUMBER;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.http.NetApi.URL_CLOSE;
import static high.rivamed.myapplication.http.NetApi.URL_OPEN;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/12 18:06
 * 描述:        设备注册和激活界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

//public class RegisteFrag extends SimpleFragment implements NetWorkReceiver.IntAction {
public class RegisteFrag extends SimpleFragment {

   String TAG = "RegisteFrag";
   @BindView(R.id.frag_registe_name_edit)
   EditText mFragRegisteNameEdit;
   @BindView(R.id.frag_registe_model_edit)
   EditText mFragRegisteModelEdit;
   @BindView(R.id.frag_registe_number_edit)
   EditText mFragRegisteNumberEdit;
   @BindView(R.id.frag_registe_localip_edit)
   EditText mFragRegisteLocalipEdit;
   @BindView(R.id.frag_registe_severip_edit)
   EditText mFragRegisteSeveripEdit;
   @BindView(R.id.frag_registe_port_edit)
   EditText mFragRegistePortEdit;
   @BindView(R.id.frag_registe_right)
   TextView mFragRegisteRight;
   @BindView(R.id.frag_registe_left)
   TextView mFragRegisteLeft;
   @BindView(R.id.frag_registe_loginout_edit)
   EditText mFragRegisteLoginoutEdit;
   public RecyclerView mRecyclerview;
   @BindView(R.id.switch_btn)
   Switch   mSwitch;
   @BindView(R.id.fragment_btn_one)
   TextView mFragmentBtnOne;
   public  RegisteSmallAdapter             mSmallAdapter;
   private List<TBaseDevices>              mTBaseDevicesAll;
   private List<TBaseDevices.tBaseDevices> mTBaseDevicesSmall;
   int i = generateData().size();
   private       String                              mFootNameStr;
   private       String                              mFootIpStr;
   private       String                              mFootMacStr;
   private       String                              mHeadName;
   public static List<DeviceInfo>                    mDeviceInfos;
   private       List<TBaseDevices>                  mBaseDevices;
   private       DeviceNameBeanX                     mNameBean;
   private       List<DeviceNameBeanX.DeviceDictVos> mNameList;
   private       ThingDto                            mSnRecoverBean;
   public static List<ThingDto.DeviceVosBean>        mDeviceVos = new ArrayList<>();//柜子list

   private       String                              mBoxCode;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onActivationEvent(Event.dialogEvent event) {

	if (event.dialog != null) {
	   event.dialog.dismiss();
	   event.dialog = null;
	   String s = mGson.toJson(
		   addFromDate(event.deptName, event.branchCode, event.deptId, event.storehouseCode,
				   event.operationRoomNo));
	   LogUtils.i(TAG, "激活的   " + s);

	   mFragRegisteRight.setEnabled(false);
	   if (mSmallAdapter.mRightDelete != null) {
		mSmallAdapter.mRightDelete.setVisibility(View.GONE);
	   }
	   LitePal.deleteAll(BoxIdBean.class);
	   setSaveActive(s);
	}

   }

   /**
    * 激活
    */
   private void setSaveActive(String fromDate) {

	NetRequest.getInstance().setSaveActiveDate(fromDate, _mActivity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ThingDto thingDto = mGson.fromJson(result, ThingDto.class);
		if (thingDto.isOperateSuccess()) {
		   SPUtils.putBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE, true);//激活
		   ToastUtils.showShort(thingDto.getMsg());
		   mFragmentBtnOne.setText("已激活");
		   mFragmentBtnOne.setEnabled(false);
		   SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_NAME,
					   thingDto.getThingSnVo().getSthName());
		   SPUtils.putString(UIUtils.getContext(), SAVE_BRANCH_CODE,
					   thingDto.getThingSnVo().getBranchCode());
		   SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_CODE,
					   thingDto.getThingSnVo().getDeptId());
		   SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_NAME,
					   thingDto.getThingSnVo().getDeptName());
		   SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE,
					   thingDto.getThingSnVo().getSthId());
		   SPUtils.putString(UIUtils.getContext(), SAVE_REGISTE_DATE, result);
		   SPUtils.putString(UIUtils.getContext(), SN_NUMBER, thingDto.getThing().getSn());
		   SPUtils.putString(UIUtils.getContext(), THING_CODE,
					   thingDto.getThing().getThingId());
		   putDbDate(thingDto);
		   initData();
		} else {
		   ToastUtils.showShort(thingDto.getMsg());
		}
		LogUtils.i(TAG, "result   " + result);
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		ToastUtils.showShort("操作失败 (" + result + ")");
		mFragmentBtnOne.setEnabled(true);
	   }
	});
   }

   @Override
   public void onStart() {
	super.onStart();
	if (WifiUtils.isWifi(mContext) == 1) {
	   mFragRegisteLocalipEdit.setText(WifiUtils.getLocalIpAddress(mContext));   //获取WIFI IP地址显示
	} else if (WifiUtils.isWifi(mContext) == 2) {
	   mFragRegisteLocalipEdit.setText(WifiUtils.getHostIP());//获取本地IP地址显示
	} else {
	   mFragRegisteLocalipEdit.setText("");
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRecoverEvent(ThingDto event) {
	mSnRecoverBean = event;
	String s = mGson.toJson(event);
	SPUtils.putString(UIUtils.getContext(), SAVE_REGISTE_DATE, s);
	LogUtils.i(TAG, "我是恢复的   " + s);
	//	SPUtils.putString(getAppContext(),BOX_SIZE_DATE,"");
	SPUtils.putBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE, true);
	SPUtils.putBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE, true);//激活
	SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_NAME,
				mSnRecoverBean.getThing().getDeptName());
	SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_CODE,
				mSnRecoverBean.getThing().getDeptId());
	SPUtils.putString(UIUtils.getContext(), SAVE_BRANCH_CODE,
				mSnRecoverBean.getThingSnVo().getBranchCode());
	SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE,
				mSnRecoverBean.getThingSnVo().getSthId());
	SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_NAME,
				mSnRecoverBean.getThingSnVo().getSthName());
	SPUtils.putString(UIUtils.getContext(), THING_CODE, mSnRecoverBean.getThing().getThingId());

	mFragRegisteRight.setEnabled(false);
	if (mSmallAdapter != null && mSmallAdapter.mRightDelete != null) {
	   mSmallAdapter.mRightDelete.setVisibility(View.GONE);
	}
	LitePal.deleteAll(BoxIdBean.class);
	LitePal.deleteAll(InventoryDto.class);
	LitePal.deleteAll(InventoryVo.class);
	LitePal.deleteAll(InventoryVoError.class);
	LitePal.deleteDatabase("rivamedhigh");
	LitePal.initialize(mContext);//数据库初始化
	setRegiestDate(s);
	putDbDate(mSnRecoverBean);
	initData();
   }

   public static RegisteFrag newInstance() {
	Bundle args = new Bundle();
	RegisteFrag fragment = new RegisteFrag();
	return fragment;
   }

   @Override
   public int getLayoutId() {
	return R.layout.fragment_registe_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);

	mRecyclerview = mContext.findViewById(R.id.recyclerviewc);
	Log.i(TAG, "SAVE_DEPT_NAME    " + SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_NAME));
	mFragRegisteNameEdit.setHint("2.6.7高值柜");
	mFragRegisteModelEdit.setHint("rivamed");
	mFragRegisteNumberEdit.setHint("1");
	mFragRegisteSeveripEdit.setHint("192.168.1.1");
	mFragRegistePortEdit.setHint("8016");

	if (BuildConfig.DEBUG) {
	   mFragRegisteNameEdit.setText("2.6.7柜子");
	   mFragRegisteModelEdit.setText("rivamed26xxx");
	   mFragRegisteNumberEdit.setText("1");
	   mFragRegisteSeveripEdit.setText("192.168.11.30");
	   mFragRegistePortEdit.setText("8018");
	}

	mDeviceInfos = DevicesUtils.QueryConnectedDevice();
	mBaseDevices = generateData();
	if (SPUtils.getBoolean(UIUtils.getContext(), LOGCAT_OPEN)) {
	   mSwitch.setChecked(true);
	} else {
	   mSwitch.setChecked(false);
	}
	initData();
	initListener();
   }

   private void initListener() {
	mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
		   OkGo.<String>get(MAIN_URL + URL_OPEN).tag(mContext).execute(new StringCallback() {
			@Override
			public void onSuccess(Response<String> response) {
			   ToastUtils.showShortToast(getString(R.string.log_open));
			   Log.i(TAG, "responseURL_OPEN   " + response.body());
			   LogcatHelper.getInstance(App.getAppContext()).stop();
			   LogcatHelper.getInstance(App.getAppContext()).start();
			   EventBusUtils.post(new Event.EventLogType(true));
			   SPUtils.putBoolean(UIUtils.getContext(), LOGCAT_OPEN, true);
			}

			@Override
			public void onError(Response<String> response) {
			   super.onError(response);
			   ToastUtils.showShortToast(getString(R.string.log_open));
			   LogcatHelper.getInstance(App.getAppContext()).stop();
			   LogcatHelper.getInstance(App.getAppContext()).start();
			   EventBusUtils.post(new Event.EventLogType(true));
			   SPUtils.putBoolean(UIUtils.getContext(), LOGCAT_OPEN, true);
			}
		   });
		} else {
		   OkGo.<String>get(MAIN_URL + URL_CLOSE).tag(mContext).execute(new StringCallback() {
			@Override
			public void onSuccess(Response<String> response) {
			   ToastUtils.showShortToast(getString(R.string.log_closs));
			   Log.i(TAG, "responseURL_CLOSE   " + response.body());
			   EventBusUtils.post(new Event.EventLogType(false));
			   LogcatHelper.getInstance(App.getAppContext()).stop();
			   LogcatHelper.getInstance(App.getAppContext()).start();
			   SPUtils.putBoolean(UIUtils.getContext(), LOGCAT_OPEN, false);
			}

			@Override
			public void onError(Response<String> response) {
			   super.onError(response);
			   ToastUtils.showShortToast(getString(R.string.log_closs));
			   EventBusUtils.post(new Event.EventLogType(false));
			   LogcatHelper.getInstance(App.getAppContext()).stop();
			   LogcatHelper.getInstance(App.getAppContext()).start();
			   SPUtils.putBoolean(UIUtils.getContext(), LOGCAT_OPEN, false);
			}
		   });
		}
	   }
	});
   }

   private void initData() {

	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
	   if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)) {
		String string = SPUtils.getString(UIUtils.getContext(), SAVE_REGISTE_DATE);
		LogUtils.i(TAG, "原有的   " + string);
		setRegiestDate(string);
		mFragRegisteRight.setEnabled(false);
		if (mSmallAdapter.mRightDelete != null) {
		   mSmallAdapter.mRightDelete.setVisibility(View.GONE);
		}
		mFragmentBtnOne.setText("已激活");
		mFragmentBtnOne.setEnabled(false);
	   } else {
		mFragmentBtnOne.setText("激 活");
		String string = SPUtils.getString(UIUtils.getContext(), SAVE_REGISTE_DATE);
		setRegiestDate(string);
		LogUtils.i(TAG, "string   " + string);

		mFragmentBtnOne.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			if (UIUtils.isFastDoubleClick(v.getId())) {
			   return;
			} else {
			   DialogUtils.showRegisteDialog(mContext, _mActivity);
			}
		   }
		});
	   }
	} else {
	   mDeviceVos.clear();
	   mFragmentBtnOne.setText("预注册");
	   mSmallAdapter = new RegisteSmallAdapter(R.layout.item_registe_head_layout, mBaseDevices);

	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.setAdapter(mSmallAdapter);
	   mFragmentBtnOne.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   if (UIUtils.isFastDoubleClick(v.getId())) {
			return;
		   } else {
			mFragmentBtnOne.setEnabled(false);
			String fromDate = mGson.toJson(addFromDate(null, null, null, null, null));
			LogUtils.i(TAG, "fromDate   " + fromDate);
			setSaveRegister(fromDate);//注册
		   }
		}
	   });
	}
   }

   //已有数据的时候   给激活之前添加界面数据
   private void  setRegiestDate(String string) {
	mDeviceVos.clear();
	ThingDto returnBean = mGson.fromJson(string, ThingDto.class);
	List<ThingDto.DeviceVosBean> tBaseDeviceVos = returnBean.getDeviceVos();
	ThingDto.ThingBean mThing = returnBean.getThing();
	mFragRegisteNameEdit.setText(mThing.getThingName());
	mFragRegisteModelEdit.setText(mThing.getThingModel());
	mFragRegisteNumberEdit.setText(mThing.getSn());

	mFragRegisteSeveripEdit.setText(SPUtils.getString(mContext, SAVE_SEVER_IP_TEXT));
	mFragRegistePortEdit.setText(SPUtils.getString(mContext, SAVE_SEVER_CODE));
	List<TBaseDevices.tBaseDevices.partsmacBean> mSmallmac = new ArrayList<>();
	List<TBaseDevices> mTBaseDevicesAll = new ArrayList<>();

	List<TBaseDevices.tBaseDevices.partsnameBean> deviceTypes = new ArrayList<>();//部件查询后的信息

	if (mDeviceInfos != null) {
	   for (int i = 0; i < mDeviceInfos.size(); i++) {//第三层内部部件标识的数据
		TBaseDevices.tBaseDevices.partsmacBean partsmacBean1 = new TBaseDevices.tBaseDevices.partsmacBean();
		partsmacBean1.setPartsmacnumber(mDeviceInfos.get(i).getIdentification());
		partsmacBean1.setPartsIp(mDeviceInfos.get(i).getRemoteIP());
		mSmallmac.add(partsmacBean1);
	   }
	}
	if (mNameList != null) {
	   for (int f = 0; f < mNameList.size(); f++) {//第三层内部部件标识的数据
		TBaseDevices.tBaseDevices.partsnameBean partsnameBean = new TBaseDevices.tBaseDevices.partsnameBean();
		partsnameBean.setName(mNameList.get(f).getName());
		partsnameBean.setDictId(mNameList.get(f).getDictId());
		partsnameBean.setDeviceType(mNameList.get(f).getDeviceType());

		deviceTypes.add(partsnameBean);
	   }
	}
	for (int y = 0; y < tBaseDeviceVos.size(); y++) {//第一层数据
	   ThingDto.DeviceVosBean deviceVo = tBaseDeviceVos.get(y);
	   List<TBaseDevices.tBaseDevices> mTBaseDevicesSmall = new ArrayList<>();
	   TBaseDevices registeAddBean1 = new TBaseDevices();
	   LogUtils.i(TAG, " deviceVo.getDeviceName()   " + deviceVo.getDeviceName() + "    " +
				 deviceVo.getDeviceId());
	   registeAddBean1.setDeviceName(deviceVo.getDeviceName());
	   registeAddBean1.setDeviceId(deviceVo.getDeviceId());
	   registeAddBean1.setList(mTBaseDevicesSmall);

	   if (deviceVo.getDevices() != null) {
		for (int x = 0; x < deviceVo.getDevices().size(); x++) {//第二层柜体内条目的数据
		   ThingDto.DeviceVosBean.DevicesBean devicesBean = deviceVo.getDevices().get(x);
		   LogUtils.i(TAG, "devicesBean   " + devicesBean.getDeviceName() + "    " +
					 devicesBean.getDeviceId());
		   TBaseDevices.tBaseDevices registeBean1 = new TBaseDevices.tBaseDevices();
		   registeBean1.setPartsmacName(deviceTypes);
		   registeBean1.setPartsname(devicesBean.getDeviceName());
		   registeBean1.setPartmac(devicesBean.getIdentification());
		   registeBean1.setPartip(devicesBean.getIp());
		   registeBean1.setPartsmac(mSmallmac);
		   registeBean1.setDictId(devicesBean.getDictId());
		   registeBean1.setDeviceType(devicesBean.getDeviceType());
		   registeBean1.setDeviceId(devicesBean.getDeviceId());

		   mTBaseDevicesSmall.add(registeBean1);

		}
	   }
	   mTBaseDevicesAll.add(registeAddBean1);
	}
	mDeviceVos.addAll(tBaseDeviceVos);

	mSmallAdapter = new RegisteSmallAdapter(R.layout.item_registe_head_layout, mTBaseDevicesAll);
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerview.setAdapter(mSmallAdapter);
	LogUtils.i(TAG, "XCSCSS");
   }

   //提交预注册的数据
   private void setSaveRegister(String fromDate) {

	NetRequest.getInstance().setSaveRegisteDate(fromDate, _mActivity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		ThingDto thingDto = mGson.fromJson(result, ThingDto.class);
		if (thingDto.isOperateSuccess()) {
		   ToastUtils.showShort(thingDto.getMsg());
		   mFragmentBtnOne.setEnabled(true);
		   mRecyclerview.scrollToPosition(i);
		   SPUtils.putBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE, true);
		   mFragmentBtnOne.setText("激 活");
		   SPUtils.putString(UIUtils.getContext(), SAVE_REGISTE_DATE, result);
		   SPUtils.putString(UIUtils.getContext(), SN_NUMBER, thingDto.getThing().getSn());
		   SPUtils.putString(UIUtils.getContext(), THING_CODE,
					   thingDto.getThing().getThingId());
		   putDbDate(thingDto);
		   initData();
		} else {
		   mFragmentBtnOne.setEnabled(true);
		   ToastUtils.showShortToast(thingDto.getMsg());
		}
		LogUtils.i(TAG, "result   " + result);
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		ToastUtils.showShort("操作失败 (" + result + ")");
		mFragmentBtnOne.setEnabled(true);
	   }
	});
   }

   //数据库绑定IP
   private void putDbDate(ThingDto thingDto) {

	for (ThingDto.DeviceVosBean b : thingDto.getDeviceVos()) {
	   BoxIdBean boxIdBean = new BoxIdBean();
	   String boxName = b.getDeviceName();
	   String boxCode = b.getDeviceId();
	   boxIdBean.setName(boxName);//柜子名字
	   boxIdBean.setBox_id(null);//柜子id
	   boxIdBean.setDevice_id(boxCode);//柜子子ID
	   boxIdBean.save();
	   List<ThingDto.DeviceVosBean.DevicesBean> taBaseDevices = b.getDevices();
	   for (ThingDto.DeviceVosBean.DevicesBean x : taBaseDevices) {
		BoxIdBean boxIdBean1 = new BoxIdBean();
		String parent = x.getParent();
		String identification = x.getIdentification();
		String deviceName = x.getDeviceType();
		boxIdBean1.setName(deviceName);
		boxIdBean1.setBox_id(parent);
		boxIdBean1.setDevice_id(identification);
		boxIdBean1.save();
	   }
	}
   }

   public ThingDto.DeviceVosBean getDeviceVos(int i) {
	ThingDto.DeviceVosBean tBaseThingVoBean = new ThingDto.DeviceVosBean();
	mHeadName = ((EditText) mRecyclerview.getChildAt(i)
		.findViewById(R.id.head_left_name)).getText().toString().trim();
	mBoxCode = ((TextView) mRecyclerview.getChildAt(i).findViewById(R.id.gone_box_code)).getText()
		.toString()
		.trim();
	tBaseThingVoBean.setDeviceName(mHeadName);
	tBaseThingVoBean.setDeviceId(mBoxCode);
	LogUtils.i(TAG, "boxCode " + mBoxCode);
	RecyclerView mRecyclerView2 = mRecyclerview.getChildAt(i).findViewById(R.id.recyclerview2);
	List<ThingDto.DeviceVosBean.DevicesBean> tBaseDevice = new ArrayList<>();//柜子内部的设备list
	for (int x = 0; x < mRecyclerView2.getChildCount() - 1; x++) {
	   ThingDto.DeviceVosBean.DevicesBean device = new ThingDto.DeviceVosBean.DevicesBean();
	   mFootNameStr = ((TextView) mRecyclerView2.getChildAt(x + 1)
		   .findViewById(R.id.foot_name)).getText().toString().trim();
	   mFootMacStr = ((TextView) mRecyclerView2.getChildAt(x + 1)
		   .findViewById(R.id.foot_mac)).getText().toString().trim();
	   mFootIpStr = ((EditText) mRecyclerView2.getChildAt(x + 1)
		   .findViewById(R.id.foot_ip)).getText().toString().trim();
	   String gone_dictid = ((TextView) mRecyclerView2.getChildAt(x + 1)
		   .findViewById(R.id.gone_dictid)).getText().toString().trim();
	   String gone_devicetype = ((TextView) mRecyclerView2.getChildAt(x + 1)
		   .findViewById(R.id.gone_devicetype)).getText().toString().trim();
	   String gone_deviceCode = ((TextView) mRecyclerView2.getChildAt(x + 1)
		   .findViewById(R.id.gone_device_code)).getText().toString().trim();
	   LogUtils.i(TAG,
			  "gone_dictid   " + gone_dictid + "   gone_devicetype   " + gone_devicetype +
			  "    gone_deviceCode   " + gone_deviceCode);
	   device.setDictId(gone_dictid);
	   device.setDeviceType(gone_devicetype);
	   device.setDeviceId(gone_deviceCode);
	   device.setDeviceName(mFootNameStr);
	   device.setIdentification(mFootMacStr);
	   device.setIp(mFootIpStr);
	   tBaseDevice.add(device);
	}
	tBaseThingVoBean.setDevices(tBaseDevice);

	return tBaseThingVoBean;
   }

   /**
    * 预注册存入数据
    */
   private ThingDto addFromDate(
	   String deptName, String branchCode, String deptId, String storehouseCode,
	   String operationRoomNo) {
	ThingDto TBaseThingDto = new ThingDto();//最外层
	ThingDto.ThingBean tBaseThing = new ThingDto.ThingBean();//设备信息
	ThingDto.HospitalInfoVo hospitalInfoVo = new ThingDto.HospitalInfoVo();

	hospitalInfoVo.setDeptId(deptId);
	hospitalInfoVo.setBranchCode(branchCode);
	hospitalInfoVo.setSthId(storehouseCode);
	hospitalInfoVo.setOptRoomId(operationRoomNo);
	hospitalInfoVo.setDeptName(deptName);
	TBaseThingDto.setHospitalInfoVo(hospitalInfoVo);

	tBaseThing.setThingName(mFragRegisteNameEdit.getText().toString().trim());
	tBaseThing.setThingModel(mFragRegisteModelEdit.getText().toString().trim());
	tBaseThing.setLocalIp(mFragRegisteLocalipEdit.getText().toString().trim());
	//	tBaseThing.setThingType("1");
	tBaseThing.setSn(mFragRegisteNumberEdit.getText().toString().trim());
	tBaseThing.setThingId(SPUtils.getString(mContext, THING_CODE));
	TBaseThingDto.setThing(tBaseThing);
	LogUtils.i(TAG, " mDeviceVos.size()     " + mDeviceVos.size());
	if (mRecyclerview.getAdapter().getItemCount() != mDeviceVos.size()) {
	   RecyclerView.LayoutManager layoutManager = mRecyclerview.getLayoutManager();
	   LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
	   int lastItemPosition = linearManager.findLastVisibleItemPosition();
	   int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
	   LogUtils.i(TAG, " i  lastItemPosition     " + lastItemPosition);
	   LogUtils.i(TAG, " i  firstVisibleItemPosition     " + firstVisibleItemPosition);
	   mDeviceVos.add(getDeviceVos(lastItemPosition - firstVisibleItemPosition));
	}
	TBaseThingDto.setDeviceVos(mDeviceVos);
	return TBaseThingDto;
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick({R.id.frag_registe_right, R.id.frag_registe_left, R.id.frag_registe_loginout_btn})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_registe_right:
		if (UIUtils.isFastDoubleClick(view.getId())) {
		   return;
		} else {
		   mRecyclerview.scrollToPosition(i);
		   RecyclerView.LayoutManager layoutManager = mRecyclerview.getLayoutManager();
		   LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
		   int lastItemPosition = linearManager.findLastVisibleItemPosition();
		   int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
		   mDeviceVos.add(getDeviceVos(lastItemPosition - firstVisibleItemPosition));
		   mSmallAdapter.addData(generateData());
		   mSmallAdapter.notifyItemChanged(i);
		   i++;
		}
		break;
	   case R.id.frag_registe_left:
		if (UIUtils.isFastDoubleClick(view.getId())) {
		   return;
		} else {
		   if (SPUtils.getString(UIUtils.getContext(), SAVE_REGISTE_DATE) == null) {
			mDeviceVos.clear();
		   }
		   getDeviceName();
		}

		break;
	   case R.id.frag_registe_loginout_btn:
		try {
		   int time = (Integer.parseInt(mFragRegisteLoginoutEdit.getText().toString().trim())*1000);
		   if (time>=20000){
			SPUtils.putInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME, time);
			COUNTDOWN_TIME = time;
			ToastUtils.showShortToast("设置成功！操作界面无操作后 " + COUNTDOWN_TIME / 1000 + " s后自动退出登录！");
		   }else {
			ToastUtils.showShortToast("设置失败，时间必须大于等于20秒，请重新设置！");
		   }
		} catch (Exception ex) {
		   ToastUtils.showShortToast("设置失败，请填写时间！");
		}
		break;
	}
   }

   /**
    * 填写服务器后进行绑定服务器，获取设备名称
    */
   private void getDeviceName() {
	mDeviceInfos = DevicesUtils.QueryConnectedDevice();
	List<String> strings = new ArrayList<>();
	for (int i = 0; i < mDeviceInfos.size(); i++) {
	   strings.add(mDeviceInfos.get(i).getDeviceType().toString());
	}
	if (mFragRegisteSeveripEdit.getText().toString().trim().length() == 0 ||
	    mFragRegistePortEdit.getText().toString().trim().length() == 0) {
	   ToastUtils.showShort("请先填写服务器IP和端口");
	} else {
	   String url = "http://" + mFragRegisteSeveripEdit.getText().toString().trim() + ":" +
			    mFragRegistePortEdit.getText().toString().trim();
	   Log.i(TAG, "url   " + url);
	   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_IP, "");
	   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_IP, url);

	   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_IP_TEXT,
				   mFragRegisteSeveripEdit.getText().toString().trim());
	   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_CODE,
				   mFragRegistePortEdit.getText().toString().trim());
	   MAIN_URL = SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP);
	   Log.i(TAG, "MAIN_URLMAIN_URL   " + url);

	   NetRequest.getInstance()
		   .getDeviceInfosDate(SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP), strings,
					     _mActivity, new BaseResult() {
				@Override
				public void onSucceed(String result) {
				   LogUtils.i(TAG, "result   " + result);
				   ToastUtils.showShort("服务器已连接成功！");
				   LogUtils.i(TAG,
						  "SPUtils   " + SPUtils.getString(mContext, SAVE_SEVER_IP));

				   mNameBean = mGson.fromJson(result, DeviceNameBeanX.class);
				   mNameList = mNameBean.getDeviceDictVos();
				   mBaseDevices = generateData();
				   initData();
				}

			   });
	}
   }

   private List<TBaseDevices.tBaseDevices.partsmacBean> mSmallmac;

   private List<TBaseDevices> generateData() {

	mSmallmac = new ArrayList<>();

	List<TBaseDevices.tBaseDevices.partsnameBean> deviceTypes = new ArrayList<>();
	mTBaseDevicesAll = new ArrayList<>();
	mTBaseDevicesSmall = new ArrayList<>();
	//	TBaseDevices.tBaseDevices.partsmacBean partsmacBean1 = new TBaseDevices.tBaseDevices.partsmacBean();
	TBaseDevices.tBaseDevices registeBean1 = new TBaseDevices.tBaseDevices();
	TBaseDevices registeAddBean1 = new TBaseDevices();
	if (mDeviceInfos != null) {
	   for (int i = 0; i < mDeviceInfos.size(); i++) {//第三层内部部件标识的数据
		TBaseDevices.tBaseDevices.partsmacBean partsmacBean1 = new TBaseDevices.tBaseDevices.partsmacBean();
		partsmacBean1.setPartsmacnumber(mDeviceInfos.get(i).getIdentification());
		partsmacBean1.setPartsIp(mDeviceInfos.get(i).getRemoteIP());

		mSmallmac.add(partsmacBean1);
	   }
	}
	if (mNameList != null) {
	   for (int f = 0; f < mNameList.size(); f++) {//第三层内部部件标识的数据
		TBaseDevices.tBaseDevices.partsnameBean partsnameBean = new TBaseDevices.tBaseDevices.partsnameBean();
		partsnameBean.setName(mNameList.get(f).getName());
		partsnameBean.setDictId(mNameList.get(f).getDictId());
		partsnameBean.setDeviceType(mNameList.get(f).getDeviceType());

		deviceTypes.add(partsnameBean);
	   }
	}
	for (int x = 0; x < 1; x++) {//第二层柜体内条目的数据
	   registeBean1.setPartsname("");
	   registeBean1.setPartsmac(mSmallmac);
	   registeBean1.setPartsmacName(deviceTypes);
	   mTBaseDevicesSmall.add(registeBean1);
	}
	for (int y = 0; y < 1; y++) {//第一层数据

	   if (mSmallAdapter == null && y == 0) {
		registeAddBean1.setDeviceName("1号柜");
	   } else {
		registeAddBean1.setDeviceName("");
	   }
	   registeAddBean1.setList(mTBaseDevicesSmall);
	   mTBaseDevicesAll.add(registeAddBean1);
	}
	return mTBaseDevicesAll;
   }

   @Override
   public void onDestroy() {
	super.onDestroy();
	//	mContext.unregisterReceiver(netWorkReceiver);
   }
}