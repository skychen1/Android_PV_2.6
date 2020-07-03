package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rivamed.libdevicesbase.base.DeviceInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegisteSmallAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.DeviceNameBeanX;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.LogosBean;
import high.rivamed.myapplication.bean.TBaseDevices;
import high.rivamed.myapplication.bean.ThingDto;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dbmodel.ChildrenBean;
import high.rivamed.myapplication.dbmodel.ChildrenBeanX;
import high.rivamed.myapplication.dbmodel.OperationRoomsBean;
import high.rivamed.myapplication.dbmodel.RoomsBean;
import high.rivamed.myapplication.dbmodel.UserBean;
import high.rivamed.myapplication.dbmodel.UserFeatureInfosBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVoError;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.FileUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LogcatHelper;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.WifiUtils;

import static high.rivamed.myapplication.base.App.COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.HOME_COUNTDOWN_TIME;
import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.NOEPC_LOGINOUT_TIME;
import static high.rivamed.myapplication.base.App.REMOVE_LOGFILE_TIME;
import static high.rivamed.myapplication.base.App.VOICE_NOCLOSSDOOR_TIME;
import static high.rivamed.myapplication.base.App.SYSTEMTYPE;
import static high.rivamed.myapplication.base.App.mAppContext;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE_HOME;
import static high.rivamed.myapplication.cont.Constants.HOME_LOGO;
import static high.rivamed.myapplication.cont.Constants.LOGCAT_OPEN;
import static high.rivamed.myapplication.cont.Constants.LOGIN_LOGO;
import static high.rivamed.myapplication.cont.Constants.SAVE_ACTIVATION_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_HOME_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_NOEPC_LOGINOUT_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_REGISTE_DATE;
import static high.rivamed.myapplication.cont.Constants.SAVE_REMOVE_LOGFILE_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP_TEXT;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_VOICE_NOCLOSSDOOR_TIME;
import static high.rivamed.myapplication.cont.Constants.SN_NUMBER;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_MODEL;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.UIUtils.disableRadioGroup;
import static high.rivamed.myapplication.utils.UIUtils.enableRadioGroup;

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
   @BindView(R.id.rg_device_type)
   RadioGroup  mRgDeviceType;
   @BindView(R.id.frag_registe_name_edit)
   EditText    mFragRegisteNameEdit;
   @BindView(R.id.rb_standard_pv)
   RadioButton mRbStandardPv;
   @BindView(R.id.rb_embed_pv)
   RadioButton mRbEmbedPv;
   @BindView(R.id.frag_registe_number_edit)
   EditText    mFragRegisteNumberEdit;
   @BindView(R.id.frag_registe_localip_edit)
   EditText    mFragRegisteLocalipEdit;
   @BindView(R.id.frag_registe_severip_edit)
   EditText    mFragRegisteSeveripEdit;
   @BindView(R.id.frag_registe_port_edit)
   EditText    mFragRegistePortEdit;
   @BindView(R.id.frag_registe_right)
   TextView    mFragRegisteRight;
   @BindView(R.id.frag_registe_left)
   TextView    mFragRegisteLeft;
   @BindView(R.id.frag_registe_loginout_edit)
   EditText mFragRegisteLoginoutEdit;
   @BindView(R.id.frag_registe_loginout_edit2)
   EditText mFragRegisteLoginoutEdit2;
   @BindView(R.id.frag_registe_loginout_edit3)
   EditText mFragRegisteLoginoutEdit3;
   @BindView(R.id.frag_registe_loginout_edit4)
   EditText mFragRegisteLoginoutEdit4;
   @BindView(R.id.frag_registe_loginout_edit5)
   EditText mFragRegisteLoginoutEdit5;
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
   private       Thread                              mThread2;
   private       String                              mBoxCode;
   private       String                              mBoxType   = "0";
   private String                              mThingModel = "0";//设备类型，默认标准耗材柜
   private       RadioGroup                          mRadioGroup;
   private       String                              mHeadEndName;

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

	NetRequest.getInstance().setSaveActiveDate(fromDate, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ThingDto thingDto = mGson.fromJson(result, ThingDto.class);
		if (thingDto.isOperateSuccess()) {
		   SPUtils.putBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE, true);//激活
		   ToastUtils.showShortToast(thingDto.getMsg());
		   mFragmentBtnOne.setText("已激活");
		   mFragmentBtnOne.setEnabled(false);
		   disableRadioGroup(mRgDeviceType);
		   getBoxSize();
		   getLogos();
		   if (thingDto.getThing().getThingModel().equals("1")){
			SYSTEMTYPE ="EHCT";//嵌入式
		   }else {
			SYSTEMTYPE ="HCT";//耗材柜
		   }
		   SPUtils.putString(UIUtils.getContext(), THING_MODEL,
					   thingDto.getThingSnVo().getThingModel());
		   SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_NAME,
					   thingDto.getThingSnVo().getSthName());
		   SPUtils.putString(UIUtils.getContext(), SAVE_BRANCH_CODE,
					   thingDto.getThingSnVo().getBranchId());
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
		   ToastUtils.showLongToast("激活成功，请稍等5秒，初始化中！");
		   getUnNetUseDate();
		   getUnEntFindOperation();
		   initData();
		} else {
		   ToastUtils.showShortToast(thingDto.getMsg());
		}
		LogUtils.i(TAG, "result   " + result);
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		ToastUtils.showShortToast("操作失败 (" + result + ")");
		mFragmentBtnOne.setEnabled(true);
	   }
	});
   }

   public void getBoxSize() {
	NetRequest.getInstance().loadBoxSize(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		mTitleConn = true;
		Gson gson = new Gson();
		BoxSizeBean boxSizeBean = gson.fromJson(result, BoxSizeBean.class);
		List<BoxSizeBean.DevicesBean> devices = boxSizeBean.getDevices();

		if (devices.size() > 1) {
		   BoxSizeBean.DevicesBean tbaseDevicesBean = new BoxSizeBean.DevicesBean();
		   //		   tbaseDevicesBean.setDeviceName("全部开柜");
		   tbaseDevicesBean.setDeviceName("全部");
		   tbaseDevicesBean.setDeviceId("");
		   devices.add(0, tbaseDevicesBean);
		}
		List<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean> deviceVos = boxSizeBean.getDeviceTypeVo().getDeviceVos();

		SPUtils.putString(mAppContext, BOX_SIZE_DATE, gson.toJson(devices));
		SPUtils.putString(mAppContext, BOX_SIZE_DATE_HOME, gson.toJson(deviceVos));
	   }

	   @Override
	   public void onError(String result) {

	   }
	});
   }

   @Override
   public void onStart() {
	super.onStart();
	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)) {
	   disableRadioGroup(mRgDeviceType);
	} else {
	   enableRadioGroup(mRgDeviceType);
	}
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
	if (mSnRecoverBean.getThing().getThingModel().equals("1")){
	   SYSTEMTYPE ="EHCT";//嵌入式
	}else {
	   SYSTEMTYPE ="HCT";//耗材柜
	}
	//	SPUtils.putString(getAppContext(),BOX_SIZE_DATE,"");
	SPUtils.putBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE, true);
	SPUtils.putBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE, true);//激活
	SPUtils.putString(UIUtils.getContext(), THING_MODEL,
				mSnRecoverBean.getThingSnVo().getThingModel());
	SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_NAME,
				mSnRecoverBean.getThing().getDeptName());
	SPUtils.putString(UIUtils.getContext(), SAVE_DEPT_CODE,
				mSnRecoverBean.getThing().getDeptId());
	SPUtils.putString(UIUtils.getContext(), SAVE_BRANCH_CODE,
				mSnRecoverBean.getThingSnVo().getBranchId());
	SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE,
				mSnRecoverBean.getThingSnVo().getSthId());
	SPUtils.putString(UIUtils.getContext(), SAVE_STOREHOUSE_NAME,
				mSnRecoverBean.getThingSnVo().getSthName());
	SPUtils.putString(UIUtils.getContext(), THING_CODE, mSnRecoverBean.getThing().getThingId());
	SPUtils.putString(UIUtils.getContext(), SN_NUMBER, mSnRecoverBean.getThing().getSn());
	mFragRegisteRight.setEnabled(false);
	if (mSmallAdapter != null && mSmallAdapter.mRightDelete != null) {
	   mSmallAdapter.mRightDelete.setVisibility(View.GONE);
	}

	LitePal.deleteAll(BoxIdBean.class);
	LitePal.deleteAll(InventoryDto.class);
	LitePal.deleteAll(InventoryVo.class);
	LitePal.deleteAll(InventoryVoError.class);
	deleteLitepal();
	LitePal.deleteDatabase("rivamedhigh");
	LitePal.initialize(mAppContext);//数据库初始化
	disableRadioGroup(mRgDeviceType);
	setRegiestDate(s);
	putDbDate(mSnRecoverBean);
	getLogos();
	initData();
	getBoxSize();
	ToastUtils.showLongToast("数据恢复完成，请稍等5秒，初始化中！");
	getUnNetUseDate();
	getUnEntFindOperation();
   }

   /**
    * 获取logo
    */
   private void getLogos() {
	NetRequest.getInstance().loadLogo(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogosBean logosBean = mGson.fromJson(result, LogosBean.class);
		String loginPageLogo = logosBean.getHospitalFile().getHospLogo();
		String mainInterfaceLogo = logosBean.getHospitalFile().getMainInterfaceLogo();
		boolean saveloginPageLogo = FileUtils.savePicture(loginPageLogo, "login_logo");
		boolean savemainInterfaceLogo = FileUtils.savePicture(mainInterfaceLogo, "home_logo");
		File login_logo = new File(
			Environment.getExternalStorageDirectory() + "/login_logo" + "/login_logo.png");
		File home_logo = new File(
			Environment.getExternalStorageDirectory() + "/home_logo" + "/home_logo.png");
		String login_logoPath = login_logo.getPath();
		String home_logoPath = home_logo.getPath();
		SPUtils.putString(mContext, LOGIN_LOGO, login_logoPath);
		SPUtils.putString(mContext, HOME_LOGO, home_logoPath);
		Log.i("eees", "图片loginPageLogo保存+     " + saveloginPageLogo);
		Log.i("eees", "图片mainInterfaceLogo保存+     " + savemainInterfaceLogo);
	   }
	});
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
	mFragRegisteNameEdit.setHint("2.6.15高值柜");
	mFragRegisteNumberEdit.setHint("1");
	mFragRegisteSeveripEdit.setHint("192.168.1.1");
	mFragRegistePortEdit.setHint("8016");

	if (BuildConfig.DEBUG) {
	   mFragRegisteNameEdit.setText("3.0柜子");
	   mFragRegisteNumberEdit.setText("1");
	   mFragRegisteSeveripEdit.setText("192.168.111.80");
	   mFragRegistePortEdit.setText("8019");
	}
	mFragRegisteLoginoutEdit.setText(COUNTDOWN_TIME / 1000+"");
	mFragRegisteLoginoutEdit2.setText( HOME_COUNTDOWN_TIME / 1000+"");
	mFragRegisteLoginoutEdit5.setText(REMOVE_LOGFILE_TIME+"");
	mDeviceInfos = DevicesUtils.QueryConnectedDevice();
	mBaseDevices = generateData();
	if (SPUtils.getBoolean(UIUtils.getContext(), LOGCAT_OPEN)) {
	   mSwitch.setChecked(true);
	} else {
	   mSwitch.setChecked(false);
	}
	initData();
	initListener();
	initDeviceSelected();
   }
   /**
    * 设备类型选择
    */
   private void initDeviceSelected() {
	mRgDeviceType.setOnCheckedChangeListener((group, checkedId) -> {
	   switch (checkedId) {
		case R.id.rb_standard_pv:
		   //TODO 标准
		   mThingModel = "0";
		   SYSTEMTYPE ="HCT";//高值柜
		   break;
		case R.id.rb_embed_pv:
		   //TODO 嵌入式
		   mThingModel = "1";
		   SYSTEMTYPE ="EHCT";//嵌入式
		   break;
		default:
		   break;
	   }
	});
   }
   private void initListener() {
	mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
		   ToastUtils.showShortToast(getString(R.string.log_open));
		   LogcatHelper.getInstance(App.getAppContext()).stop();
		   LogcatHelper.getInstance(App.getAppContext()).start();
		   EventBusUtils.post(new Event.EventLogType(true));
		   SPUtils.putBoolean(UIUtils.getContext(), LOGCAT_OPEN, true);
		} else {
		   ToastUtils.showShortToast(getString(R.string.log_closs));
		   EventBusUtils.post(new Event.EventLogType(false));
		   LogcatHelper.getInstance(App.getAppContext()).stop();
		   LogcatHelper.getInstance(App.getAppContext()).start();
		   SPUtils.putBoolean(UIUtils.getContext(), LOGCAT_OPEN, false);
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
			   DialogUtils.showRegisteDialog(mContext, mContext);
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
   private void setRegiestDate(String string) {
	mDeviceVos.clear();
	ThingDto returnBean = mGson.fromJson(string, ThingDto.class);
	List<ThingDto.DeviceVosBean> tBaseDeviceVos = returnBean.getDeviceVos();
	ThingDto.ThingBean mThing = returnBean.getThing();
	mFragRegisteNameEdit.setText(mThing.getThingName());
	if (mThing.getThingModel().equals("0")){
	   mRbStandardPv.setChecked(true);
	}else if (mThing.getThingModel().equals("1")){
	   mRbEmbedPv.setChecked(true);
	}
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
	   registeAddBean1.setCabinetNum(deviceVo.getCabinetNum());
	   registeAddBean1.setCabinetType(deviceVo.getCabinetType());
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
   }

   //提交预注册的数据
   private void setSaveRegister(String fromDate) {

	NetRequest.getInstance().setSaveRegisteDate(fromDate, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		ThingDto thingDto = mGson.fromJson(result, ThingDto.class);
		if (thingDto.isOperateSuccess()) {
		   ToastUtils.showShortToast(thingDto.getMsg());
		   mFragmentBtnOne.setEnabled(true);
		   mRecyclerview.scrollToPosition(i);
		   SPUtils.putBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE, true);
		   SPUtils.putString(UIUtils.getContext(), THING_MODEL,
					   thingDto.getThing().getThingModel());
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
		ToastUtils.showShortToast("操作失败 (" + result + ")");
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
	   boxIdBean.setCabinetType(b.getCabinetType());
	   boxIdBean.setCabinetNum(b.getCabinetNum());
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
		boxIdBean1.setCabinetNum(b.getCabinetNum());
		boxIdBean1.setCabinetType(b.getCabinetType());
		boxIdBean1.save();
	   }
	}
   }

   public ThingDto.DeviceVosBean getDeviceVos(int i) {
	ThingDto.DeviceVosBean tBaseThingVoBean = new ThingDto.DeviceVosBean();
	EditText nameText = (EditText) mRecyclerview.getChildAt(i).findViewById(R.id.head_left_name);
	mHeadName = nameText.getText().toString().trim();
	mBoxCode = ((TextView) mRecyclerview.getChildAt(i).findViewById(R.id.gone_box_code)).getText()
		.toString()
		.trim();
	mRadioGroup = (RadioGroup) mRecyclerview.getChildAt(i).findViewById(R.id.registe_head_rg);
	if (mRadioGroup.getCheckedRadioButtonId() == R.id.registe_top) {
	   mBoxType = "1";
	} else if (mRadioGroup.getCheckedRadioButtonId() == R.id.registe_down) {
	   mBoxType = "2";
	} else if (mRadioGroup.getCheckedRadioButtonId() == R.id.registe_single) {
	   mBoxType = "0";
	}
	tBaseThingVoBean.setDeviceName(mHeadName);
	tBaseThingVoBean.setDeviceId(mBoxCode);
	tBaseThingVoBean.setCabinetType(mBoxType);
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
	   if (gone_devicetype.equals("1")) {
		tBaseThingVoBean.setCabinetNum(mFootMacStr);
	   }
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
	hospitalInfoVo.setBranchId(branchCode);
	hospitalInfoVo.setSthId(storehouseCode);
	hospitalInfoVo.setOptRoomId(operationRoomNo);
	hospitalInfoVo.setDeptName(deptName);
	TBaseThingDto.setHospitalInfoVo(hospitalInfoVo);
	if (mRgDeviceType.getCheckedRadioButtonId() == R.id.rb_standard_pv) {
	   mThingModel = "0";
	   SYSTEMTYPE ="HCT";//高值柜
	} else if (mRgDeviceType.getCheckedRadioButtonId() == R.id.rb_embed_pv) {
	   mThingModel = "1";
	   SYSTEMTYPE ="EHCT";//嵌入式
	}
	tBaseThing.setThingName(mFragRegisteNameEdit.getText().toString().trim());
	tBaseThing.setThingModel(mThingModel);
	tBaseThing.setLocalIp(mFragRegisteLocalipEdit.getText().toString().trim());
	//	tBaseThing.setThingType("1");
	tBaseThing.setSn(mFragRegisteNumberEdit.getText().toString().trim());
	tBaseThing.setThingId(SPUtils.getString(mContext, THING_CODE));
	tBaseThing.setSystemType(SYSTEMTYPE);
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
	LogUtils.i("fododo", "mDeviceVos       " + mGson.toJson(mDeviceVos));
	TBaseThingDto.setDeviceVos(mDeviceVos);
	return TBaseThingDto;
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick({R.id.frag_registe_right, R.id.frag_registe_left, R.id.frag_registe_loginout_btn,
	   R.id.frag_registe_txt,R.id.frag_registe_loginout_btn2,R.id.frag_registe_loginout_btn3,R.id.frag_registe_loginout_btn4, R.id.frag_registe_loginout_btn5})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_registe_right:
		if (!UIUtils.isFastDoubleClick(view.getId())) {
		   //		   mContext.startActivity(new Intent(mContext, RegisteBoxActivity.class));
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
		if (!UIUtils.isFastDoubleClick(view.getId())) {
		   if (SPUtils.getString(UIUtils.getContext(), SAVE_REGISTE_DATE) == null) {
			mDeviceVos.clear();
		   }
		   getDeviceName();
		}
		break;
	   case R.id.frag_registe_txt:
		DialogUtils.showRegisteTextDialog(mContext);
		break;
	   case R.id.frag_registe_loginout_btn:
		try {
		   int time = (Integer.parseInt(mFragRegisteLoginoutEdit.getText().toString().trim()) *
				   1000);
		   if (time >= 10000) {
			SPUtils.putInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME, time);
			COUNTDOWN_TIME = time;
			ToastUtils.showShortToast(
				"设置成功！操作界面无操作后 " + COUNTDOWN_TIME / 1000 + " s后自动退出登录！");
		   } else {
			ToastUtils.showShortToast("设置失败，时间必须大于等于10秒，请重新设置！");
		   }
		} catch (Exception ex) {
		   ToastUtils.showShortToast("设置失败，请填写时间！");
		}
		break;
	   case R.id.frag_registe_loginout_btn2:
		try {
		   int time = (Integer.parseInt(mFragRegisteLoginoutEdit2.getText().toString().trim())*1000);
		   if (time>=5000){
			SPUtils.putInt(UIUtils.getContext(), SAVE_HOME_LOGINOUT_TIME, time);
			HOME_COUNTDOWN_TIME = time;
			ToastUtils.showShortToast("设置成功！操作界面无操作后 " + HOME_COUNTDOWN_TIME / 1000 + " s后自动退出登录！");
		   }else {
			ToastUtils.showShortToast("设置失败，时间必须大于等于5秒，请重新设置！");
		   }
		} catch (Exception ex) {
		   ToastUtils.showShortToast("设置失败，请填写时间！");
		}
		break;
	   case R.id.frag_registe_loginout_btn3:
		try {
		   int time = (Integer.parseInt(mFragRegisteLoginoutEdit3.getText().toString().trim())*1000);
		   if (time>=3000){
			SPUtils.putInt(UIUtils.getContext(), SAVE_NOEPC_LOGINOUT_TIME, time);
			NOEPC_LOGINOUT_TIME = time;
			ToastUtils.showShortToast("设置成功！未扫描到操作耗材后 " + NOEPC_LOGINOUT_TIME / 1000 + " s后自动退出登录！");
		   }else {
			ToastUtils.showShortToast("设置失败，时间必须大于等于3秒，请重新设置！");
		   }
		} catch (Exception ex) {
		   ToastUtils.showShortToast("设置失败，请填写时间！");
		}
		break;
	   case R.id.frag_registe_loginout_btn4:
		try {
		   int time = (Integer.parseInt(mFragRegisteLoginoutEdit4.getText().toString().trim())*1000);
		   if (time>=60000){
			SPUtils.putInt(UIUtils.getContext(), SAVE_VOICE_NOCLOSSDOOR_TIME, time);
			VOICE_NOCLOSSDOOR_TIME= time;
			ToastUtils.showShortToast("设置成功！未关柜门后 " + VOICE_NOCLOSSDOOR_TIME / 1000 + " s后开始语音提示！");
		   }else {
			ToastUtils.showShortToast("设置失败，时间必须大于等于60秒，请重新设置！");
		   }
		} catch (Exception ex) {
		   ToastUtils.showShortToast("设置失败，请填写时间！");
		}
		break;
	   case R.id.frag_registe_loginout_btn5:
		try {
		   int time = (Integer.parseInt(mFragRegisteLoginoutEdit5.getText().toString().trim()));
		   if (time >= 2) {
			SPUtils.putInt(UIUtils.getContext(), SAVE_REMOVE_LOGFILE_TIME, time);
			REMOVE_LOGFILE_TIME = time;
			ToastUtils.showShortToast(
				"设置成功！删除 " + REMOVE_LOGFILE_TIME + " 天之前的日志！");
		   } else {
			ToastUtils.showShortToast("设置失败，时间必须大于等于2天，请重新设置！");
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

	if (mFragRegisteSeveripEdit.getText().toString().trim().length() == 0 ||
	    mFragRegistePortEdit.getText().toString().trim().length() == 0) {
	   ToastUtils.showShortToast("请先填写服务器IP和端口");
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
		   .getDeviceInfosDate(SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP),
					     mContext, new BaseResult() {
				@Override
				public void onSucceed(String result) {
				   LogUtils.i(TAG, "result   " + result);
				   ToastUtils.showShortToast("服务器已连接成功！");
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
		registeAddBean1.setCabinetType(mBoxType);
	   } else {
		registeAddBean1.setDeviceName("");
		registeAddBean1.setCabinetType(mBoxType);
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

   /**
    * 本地手术室
    */
   private void getUnEntFindOperation() {

	NetRequest.getInstance().getUnEntFindOperation(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "getUnEntFindOperation    " + result);
		LitePal.deleteAll(RoomsBean.class);
		LitePal.deleteAll(OperationRoomsBean.class);
		RoomsBean roomsBean = mGson.fromJson(result, RoomsBean.class);
		RoomsBean mRoomsBean = new RoomsBean();
		if (roomsBean.getOperationRooms().size() > 0) {
		   mRoomsBean.setThingId(roomsBean.getThingId());
		   for (OperationRoomsBean mOperationRooms : roomsBean.getOperationRooms()) {
			OperationRoomsBean bean = new OperationRoomsBean();
			bean.setOptRoomId(mOperationRooms.getOptRoomId());
			bean.setRoomName(mOperationRooms.getRoomName());
			bean.save();
			mRoomsBean.getOperationRooms().add(bean);
		   }
		   mRoomsBean.save();
		}
	   }
	});
   }

   /**
    * 所有用户的本地数据
    */
   private void getUnNetUseDate() {
	NetRequest.getInstance().getUnNetUseDate(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		UserBean userBean = mGson.fromJson(result, UserBean.class);
		Log.i("ddefad", "userBean  " + getDates());
		new Thread(new Runnable() {
		   @Override
		   public void run() {
			//			setLitePalUseBean(userBean);
			saveUserData(userBean);
		   }
		}).start();

	   }
	});
   }

   private void saveUserData(UserBean userBean) {
	UserBean mUserBean = new UserBean();
	mUserBean.setDeptId(userBean.getDeptId());
	List<AccountVosBean> accountVosBeans = new ArrayList<>();

	for (AccountVosBean accountVosBean : userBean.getAccountVos()) {
	   List<UserFeatureInfosBean> userFeatureInfosList = new ArrayList<>();
	   for (UserFeatureInfosBean userFeatureInfosBean : accountVosBean.getUserFeatureInfos()) {
		userFeatureInfosBean.setAccountName(accountVosBean.getAccountName());
		userFeatureInfosList.add(userFeatureInfosBean);
	   }
	   LitePal.saveAll(userFeatureInfosList);

	   List<HomeAuthorityMenuBean> homeAuthorityMenuBeanS = new ArrayList<>();
	   List<ChildrenBeanX> mChildrenBeanXS = new ArrayList<>();

	   for (HomeAuthorityMenuBean homeAuthorityMenuBean : accountVosBean.getMenus()) {
		homeAuthorityMenuBean.setAccountName(accountVosBean.getAccountName());
		if (homeAuthorityMenuBean.getTitle().equals("耗材操作") &&
		    null != homeAuthorityMenuBean.getChildren() &&
		    homeAuthorityMenuBean.getChildren().size() > 0) {

		   ChildrenBeanX childrenBeanX = homeAuthorityMenuBean.getChildren().get(0);
		   childrenBeanX.setAccountName(accountVosBean.getAccountName());
		   if (null != homeAuthorityMenuBean.getChildren().get(0).getChildren()) {
			List<ChildrenBean> children = homeAuthorityMenuBean.getChildren()
				.get(0)
				.getChildren();
			for (ChildrenBean child : children) {
			   child.setAccountName(accountVosBean.getAccountName());
			}
			LitePal.saveAll(children);
		   }
		   mChildrenBeanXS.add(childrenBeanX);
		}
		LitePal.saveAll(mChildrenBeanXS);
		homeAuthorityMenuBeanS.add(homeAuthorityMenuBean);
	   }
	   accountVosBean.setMenus(homeAuthorityMenuBeanS);
	   LitePal.saveAll(homeAuthorityMenuBeanS);
	   accountVosBeans.add(accountVosBean);
	}
	LitePal.saveAll(accountVosBeans);
	mUserBean.setAccountVos(accountVosBeans);
	boolean save = mUserBean.save();
	Log.i("ddefad", "userBean完成  " + save + "     " + getDates());
   }

   /**
    * 删除本地数据库用户信息表
    */
   private void deleteLitepal() {
	LitePal.deleteAll(UserBean.class);
	LitePal.deleteAll(AccountVosBean.class);
	LitePal.deleteAll(HomeAuthorityMenuBean.class);
	LitePal.deleteAll(UserFeatureInfosBean.class);
	LitePal.deleteAll(ChildrenBeanX.class);
	LitePal.deleteAll(ChildrenBean.class);
   }
}