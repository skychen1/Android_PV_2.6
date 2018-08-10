package high.rivamed.myapplication.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegisteSmallAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.DeviceNameBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.RegisteReturnBean;
import high.rivamed.myapplication.bean.TBaseDevices;
import high.rivamed.myapplication.bean.TBaseThingDto;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.NetWorkReceiver;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.WifiUtils;

import static high.rivamed.myapplication.cont.Constants.SAVE_ACTIVATION_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_REGISTE_DATE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP_TEXT;
import static high.rivamed.myapplication.cont.Constants.SN_NUMBER;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

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

public class RegisteFrag extends SimpleFragment implements NetWorkReceiver.IntAction {

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

   public static RecyclerView mRecyclerview;
   @BindView(R.id.fragment_btn_one)
   TextView mFragmentBtnOne;
   private RegisteSmallAdapter             mSmallAdapter;
   private List<TBaseDevices>              mTBaseDevicesAll;
   private List<TBaseDevices.tBaseDevices> mTBaseDevicesSmall;
   int i = generateData().size();
   private String                                      mFootNameStr;
   private String                                      mFootIpStr;
   private String                                      mFootMacStr;
   private String                                      mHeadName;
   private List<DeviceManager.DeviceInfo>              mDeviceInfos;
   private List<TBaseDevices>                          mBaseDevices;
   private int                                         dateType;
   private DeviceNameBean                              mNameBean;
   private List<DeviceNameBean.TBaseDeviceDictVosBean> mNameList;
   private RegisteReturnBean                           mSnRecoverBean;
   private NetWorkReceiver                             mNetWorkReceiver;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onActivationEvent(Event.dialogEvent event) {

	if (event.dialog != null) {
	   event.dialog.dismiss();
	   event.dialog = null;
	   String s = mGson.toJson(
		   addFromDate(event.deptName, event.branchCode, event.deptCode, event.storehouseCode,
				   event.operationRoomNo));
	   LogUtils.i(TAG, "激活的   " + s);
	   SPUtils.putString(UIUtils.getContext(), SAVE_BRANCH_CODE, event.branchCode);
	   SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_CODE, event.deptCode);
	   SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_NAME, event.deptName);

	   //	   SPUtils.putString(UIUtils.getContext(),SAVE_SEVER_IP,);
	   setSaveRegister(s, true);

	}

   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onRecoverEvent(RegisteReturnBean event) {
	mSnRecoverBean = event;
	String s = mGson.toJson(event);
	SPUtils.putString(UIUtils.getContext(), SAVE_REGISTE_DATE, s);
	LogUtils.i(TAG, "我是恢复的   " + s);
	SPUtils.putBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE, true);
	SPUtils.putBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE, true);//激活
	SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_NAME,
				mSnRecoverBean.getTbaseThing().getDeptName());
	SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_CODE,
				mSnRecoverBean.getTbaseThing().getDeptCode());
	SPUtils.putString(UIUtils.getContext(), SAVE_BRANCH_CODE,
				mSnRecoverBean.getTbaseThing().getBranchCode());
	setRegiestDate(s);
	setSaveRegister(s, true);

	//	setSaveRegister(s, true);
   }

   public static RegisteFrag newInstance() {
	Bundle args = new Bundle();
	RegisteFrag fragment = new RegisteFrag();
	//	args.putInt(TYPE_SIZE, param);
	//	args.putString(TYPE_PAGE, type);
	//	fragment.setArguments(args);
	return fragment;
   }

   @Override
   public int getLayoutId() {
	return R.layout.fragment_registe_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	applyNet();
	mRecyclerview = mContext.findViewById(R.id.recyclerview);
	Log.i(TAG, "SAVE_DEPT_NAME    " + SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_NAME));
//	mFragRegisteNameEdit.setHint("2.6柜子");
//	mFragRegisteModelEdit.setHint("rivamed");
//	mFragRegisteNumberEdit.setHint("123456789");
//	mFragRegisteSeveripEdit.setHint("192.168.10.25");
//	mFragRegistePortEdit.setHint("8016");
		mFragRegisteNameEdit.setText("2.6柜子");
		mFragRegisteModelEdit.setText("rivamed26");
		mFragRegisteNumberEdit.setText("1212121212");
		mFragRegisteSeveripEdit.setText("192.168.10.25");
		mFragRegistePortEdit.setText("8015");

	mDeviceInfos = DeviceManager.getInstance().QueryConnectedDevice();
	mBaseDevices = generateData();
	initData();
   }

   private void applyNet() {
	IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	mNetWorkReceiver = new NetWorkReceiver();
	_mActivity.registerReceiver(mNetWorkReceiver, filter);
	mNetWorkReceiver.setInteractionListener(this);
   }

   @Override
   public void onDestroy() {
	super.onDestroy();
	_mActivity.unregisterReceiver(mNetWorkReceiver);
   }

   @Override
   public void setText(String d) {

   }

   /**
    * 获取本地和WIFI的IP  显示
    *
    * @param k
    */
   @Override
   public void setInt(int k) {
	if (k != -1) {
	   if (k == 2) {
		mFragRegisteLocalipEdit.setText(
			WifiUtils.getLocalIpAddress(mContext));   //获取WIFI IP地址显示
	   } else if (k == 0) {
		mFragRegisteLocalipEdit.setText("");
	   } else if (k == 1) {
		mFragRegisteLocalipEdit.setText(WifiUtils.getHostIP());//获取本地IP地址显示
	   }
	}
   }

   private void initData() {
	initListener();
	if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
	   NetRequest.getInstance().findThingConfigDate(mContext, null, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "findThingConfigDate   " + result);
		   SPUtils.putString(mContext, SAVE_CONFIG_STRING, result);
		}
	   });
	}
	//
	//
	//	if (WifiUtils.isWifi(mContext) == 2) {
	//	   mFragRegisteLocalipEdit.setText(WifiUtils.getHostIP());
	//	} else {
	//	   mFragRegisteLocalipEdit.setText(WifiUtils.getLocalIpAddress(mContext));
	//	}

	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
	   if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)) {
		String string = SPUtils.getString(UIUtils.getContext(), SAVE_REGISTE_DATE);
		LogUtils.i(TAG, "原有的   " + string);
		setRegiestDate(string);

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
			if (UIUtils.isFastDoubleClick()) {
			   return;
			} else {
			   DialogUtils.showRegisteDialog(mContext, _mActivity);
			}
		   }
		});
	   }
	} else {
	   mFragmentBtnOne.setText("预注册");

	   mSmallAdapter = new RegisteSmallAdapter(R.layout.item_registe_head_layout, mBaseDevices);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.setAdapter(mSmallAdapter);
	   mFragmentBtnOne.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   if (UIUtils.isFastDoubleClick()) {
			return;
		   } else {
			mFragmentBtnOne.setEnabled(false);
			String fromDate = mGson.toJson(addFromDate(null, null, null, null, null));
			LogUtils.i(TAG, "fromDate   " + fromDate);
			setSaveRegister(fromDate, false);//注册
		   }
		}
	   });
	}

   }

   //已有数据的时候   给激活之前添加界面数据
   private void setRegiestDate(String string) {

	RegisteReturnBean returnBean = mGson.fromJson(string, RegisteReturnBean.class);
	List<RegisteReturnBean.TBaseDeviceVosBean> tBaseDeviceVos = returnBean.getTBaseDeviceVos();
	RegisteReturnBean.TbaseThingBean tbaseThing = returnBean.getTbaseThing();
	mFragRegisteNameEdit.setText(tbaseThing.getThingName());
	mFragRegisteModelEdit.setText(tbaseThing.getThingType());
	mFragRegisteNumberEdit.setText(tbaseThing.getSn());
	mFragRegisteSeveripEdit.setText(SPUtils.getString(mContext, SAVE_SEVER_IP_TEXT));
	mFragRegistePortEdit.setText(SPUtils.getString(mContext, SAVE_SEVER_CODE));
	List<TBaseDevices.tBaseDevices.partsmacBean> mSmallmac = new ArrayList<>();
	List<TBaseDevices> mTBaseDevicesAll = new ArrayList<>();

	List<TBaseDevices.tBaseDevices.partsnameBean> deviceTypes = new ArrayList<>();//部件查询后的信息

	if (mDeviceInfos != null) {
	   for (int i = 0; i < mDeviceInfos.size(); i++) {//第三层内部部件标识的数据
		TBaseDevices.tBaseDevices.partsmacBean partsmacBean1 = new TBaseDevices.tBaseDevices.partsmacBean();
		partsmacBean1.setPartsmacnumber(mDeviceInfos.get(i).getIdentifition());
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
	   RegisteReturnBean.TBaseDeviceVosBean tBaseDeviceVosBean = tBaseDeviceVos.get(y);
	   List<TBaseDevices.tBaseDevices> mTBaseDevicesSmall = new ArrayList<>();
	   TBaseDevices registeAddBean1 = new TBaseDevices();
	   registeAddBean1.setBoxname(tBaseDeviceVosBean.getDeviceName());
	   registeAddBean1.setBoxCode(tBaseDeviceVosBean.getDeviceCode());
	   registeAddBean1.setList(mTBaseDevicesSmall);
	   for (int x = 0; x < tBaseDeviceVosBean.getTaBaseDevices().size(); x++) {//第二层柜体内条目的数据
		RegisteReturnBean.TBaseDeviceVosBean.TaBaseDevicesBean devicesBean = tBaseDeviceVosBean.getTaBaseDevices()
			.get(x);
		TBaseDevices.tBaseDevices registeBean1 = new TBaseDevices.tBaseDevices();
		registeBean1.setPartsmacName(deviceTypes);
		registeBean1.setPartsname(devicesBean.getDeviceName());
		registeBean1.setPartmac(devicesBean.getIdentification());
		registeBean1.setPartip(devicesBean.getIp());
		registeBean1.setPartsmac(mSmallmac);
		registeBean1.setDictId(devicesBean.getDictId());
		registeBean1.setDeviceType(devicesBean.getDeviceType());
		registeBean1.setDeviceCodes(devicesBean.getDeviceCode());

		mTBaseDevicesSmall.add(registeBean1);
	   }
	   mTBaseDevicesAll.add(registeAddBean1);
	}

	mSmallAdapter = new RegisteSmallAdapter(R.layout.item_registe_head_layout, mTBaseDevicesAll);
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerview.setAdapter(mSmallAdapter);
   }

   //提交预注册的数据
   private void setSaveRegister(String fromDate, boolean type) {

	NetRequest.getInstance().setSaveRegisteDate(fromDate, _mActivity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		RegisteReturnBean registeReturnBean = mGson.fromJson(result, RegisteReturnBean.class);
		if (registeReturnBean.isOperateSuccess()) {
		   if (type) {
			SPUtils.putBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE, true);//激活

			ToastUtils.showShort("设备已激活！");
			mFragmentBtnOne.setText("已激活");
			mFragmentBtnOne.setEnabled(false);
		   } else {
			ToastUtils.showShort("注册成功！");
			mFragmentBtnOne.setEnabled(true);
			SPUtils.putBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE, true);
			mFragmentBtnOne.setText("激 活");
		   }

		   SPUtils.putString(UIUtils.getContext(), SAVE_REGISTE_DATE, result);
		   SPUtils.putString(UIUtils.getContext(), SN_NUMBER,
					   registeReturnBean.getTbaseThing().getSn());
		   SPUtils.putString(UIUtils.getContext(), THING_CODE,
					   registeReturnBean.getTbaseThing().getThingCode());

		   putDbDate(registeReturnBean);
		   initData();
		}
		LogUtils.i(TAG, "result   " + result);
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		ToastUtils.showShort("操作失败 ("+result+")");
		mFragmentBtnOne.setEnabled(true);
	   }
	});
   }

   //数据库绑定IP
   private void putDbDate(RegisteReturnBean registeReturnBean) {

	for (RegisteReturnBean.TBaseDeviceVosBean b : registeReturnBean.getTBaseDeviceVos()) {
	   BoxIdBean boxIdBean = new BoxIdBean();
	   String boxName = b.getDeviceName();
	   String boxCode = b.getDeviceCode();
	   boxIdBean.setName(boxName);//柜子名字
	   boxIdBean.setBox_id(null);//柜子id
	   boxIdBean.setDevice_id(boxCode);//柜子子ID
	   boxIdBean.save();
	   List<RegisteReturnBean.TBaseDeviceVosBean.TaBaseDevicesBean> taBaseDevices = b.getTaBaseDevices();
	   for (RegisteReturnBean.TBaseDeviceVosBean.TaBaseDevicesBean x : taBaseDevices) {
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

   /**
    * 预注册存入数据
    */
   private TBaseThingDto addFromDate(
	   String deptName, String branchCode, String deptCode, String storehouseCode,
	   String operationRoomNo) {

	TBaseThingDto TBaseThingDto = new TBaseThingDto();//最外层
	TBaseThingDto.TBaseThing tBaseThing = new TBaseThingDto.TBaseThing();//设备信息
	List<TBaseThingDto.TBaseDeviceVo> tBaseThingVos = new ArrayList<>();//柜子list

	TBaseThingDto.HospitalInfoVo hospitalInfoVo = new TBaseThingDto.HospitalInfoVo();

	hospitalInfoVo.setDeptCode(deptCode);
	hospitalInfoVo.setBranchCode(branchCode);
	hospitalInfoVo.setStorehouseCode(storehouseCode);
	hospitalInfoVo.setOperationRoomNo(operationRoomNo);
	hospitalInfoVo.setDeptName(deptName);

	tBaseThing.setThingName(mFragRegisteNameEdit.getText().toString().trim());
	tBaseThing.setThingType(mFragRegisteModelEdit.getText().toString().trim());
	tBaseThing.setSn(mFragRegisteNumberEdit.getText().toString().trim());
	tBaseThing.setLocalIp(mFragRegisteLocalipEdit.getText().toString().trim());
	tBaseThing.setServerIp(mFragRegisteSeveripEdit.getText().toString().trim());
	tBaseThing.setPortNumber(mFragRegistePortEdit.getText().toString().trim());
	tBaseThing.setThingCode(SPUtils.getString(mContext, THING_CODE));
	TBaseThingDto.settBaseThing(tBaseThing);

	for (int i = 0; i < mRecyclerview.getChildCount(); i++) {
	   TBaseThingDto.TBaseDeviceVo tBaseThingVoBean = new TBaseThingDto.TBaseDeviceVo();
	   mHeadName = ((EditText) mRecyclerview.getChildAt(i)
		   .findViewById(R.id.head_left_name)).getText().toString().trim();
	   String boxCode = ((TextView) mRecyclerview.getChildAt(i)
		   .findViewById(R.id.gone_box_code)).getText().toString().trim();
	   tBaseThingVoBean.setDeviceName(mHeadName);
	   tBaseThingVoBean.setDeviceCode(boxCode);
	   RecyclerView mRecyclerView2 = mRecyclerview.getChildAt(i).findViewById(R.id.recyclerview2);
	   List<TBaseThingDto.TBaseDeviceVo.TBaseDevice> tBaseDevice = new ArrayList<>();//柜子内部的设备list
	   for (int x = 0; x < mRecyclerView2.getChildCount() - 1; x++) {
		TBaseThingDto.TBaseDeviceVo.TBaseDevice device = new TBaseThingDto.TBaseDeviceVo.TBaseDevice();
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
		device.setDeviceCode(gone_deviceCode);
		device.setDeviceName(mFootNameStr);
		device.setIdentification(mFootMacStr);
		device.setIp(mFootIpStr);
		tBaseDevice.add(device);
	   }
	   tBaseThingVoBean.setTaBaseDevices(tBaseDevice);
	   tBaseThingVos.add(tBaseThingVoBean);
	}
	TBaseThingDto.settBaseDeviceVos(tBaseThingVos);
	TBaseThingDto.setHospitalInfoVo(hospitalInfoVo);
	return TBaseThingDto;
   }

   private void initListener() {

   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick({R.id.frag_registe_right, R.id.frag_registe_left})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_registe_right:
		mSmallAdapter.addData(generateData());
		mSmallAdapter.notifyItemChanged(i);
		i++;
		break;
	   case R.id.frag_registe_left:
		mDeviceInfos = DeviceManager.getInstance().QueryConnectedDevice();
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < mDeviceInfos.size(); i++) {
		   strings.add(mDeviceInfos.get(i).getDeviceType().toString());
		}
		if (mFragRegisteSeveripEdit.getText().toString().trim().length() == 0 ||
		    mFragRegistePortEdit.getText().toString().trim().length() == 0) {
		   ToastUtils.showShort("请先填写服务器IP和端口");
		} else {
		   String url = "http://" + mFragRegisteSeveripEdit.getText().toString().trim() + ":" +
				    mFragRegistePortEdit.getText().toString().trim() + "/cst";
		   Log.i(TAG, "url   " + url);
		   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_IP, "");
		   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_IP, url);
		   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_IP_TEXT,
					   mFragRegisteSeveripEdit.getText().toString().trim());
		   SPUtils.putString(UIUtils.getContext(), SAVE_SEVER_CODE,
					   mFragRegistePortEdit.getText().toString().trim());
		   Log.i(TAG, "MAIN_URLMAIN_URL   " + url);
		   NetRequest.getInstance()
			   .getDeviceInfosDate(SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP),
						     strings, _mActivity, new BaseResult() {
					@Override
					public void onSucceed(String result) {
					   ToastUtils.showShort("服务器已连接成功！");
					   //				   Log.i(TAG, "App.MAIN_URL   " + MAIN_URL);
					   LogUtils.i(TAG, "SPUtils   " +
								 SPUtils.getString(mContext, SAVE_SEVER_IP));
					   LogUtils.i(TAG, "SPUtils   " +
								 SPUtils.getString(mContext, SAVE_SEVER_IP));
					   mNameBean = mGson.fromJson(result, DeviceNameBean.class);
					   mNameList = mNameBean.getTBaseDeviceDictVos();
					   mBaseDevices = generateData();
					   initData();
					}

					@Override
					public void onError(String result) {
					   ToastUtils.showShort("服务器异常，请检查网络！");
					}
				   });
		}

		break;

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
		partsmacBean1.setPartsmacnumber(mDeviceInfos.get(i).getIdentifition());
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
		registeAddBean1.setBoxname("1号柜");
	   } else {
		registeAddBean1.setBoxname("");
	   }
	   registeAddBean1.setList(mTBaseDevicesSmall);
	   mTBaseDevicesAll.add(registeAddBean1);
	}
	return mTBaseDevicesAll;
   }
}
