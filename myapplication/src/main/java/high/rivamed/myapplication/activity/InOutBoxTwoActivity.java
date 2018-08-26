package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.bean.InBoxDtoBean;
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
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/27 19:36
 * 描述:        放入柜子的界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class InOutBoxTwoActivity extends BaseTimelyActivity {

   private static final String TAG = "InOutBoxTwoActivity";
   int mType;
   private InBoxDtoBean          mInBoxDtoBean;
   private boolean               mSuccess;
   private TCstInventoryDto      mTCstInventoryTwoDto;
   private LoadingDialog.Builder mShowLoading;
   private String                uhfDeviceId;
   private TCstInventoryDto mDtoLy = new TCstInventoryDto();
   private int          mIntentType;
   private List<String> mEthDeviceId;

   /**
    * dialog操作数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEvent(Event.outBoxEvent event) {
	event.dialog.dismiss();
	if (event.type.equals("x")) {//移出
	   putYcDates(event);
	} else if (event.type.equals("2")) {//退货
	   putThDates(event);
	} else {//调拨
	   putDbDates(event);
	}
	LogUtils.i(TAG, "TAG    " + event.context);

   }

   /**
    * 看关门后是否需要设置按钮为可以点击
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventClickBtn(Event.EventGoneBtn event) {
	runOnUiThread(new Runnable() {
	   @Override
	   public void run() {

		if (mTimelyLeft != null && mTimelyRight != null) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		}
	   }
	});
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

	getDeviceDate(event.deviceId, event.epcs);
	//	EventBus.getDefault().cancelEventDelivery(event);
   }

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_IN;
	return my_id;
   }

   @Override
   public void onStart() {
	//	AllDeviceCallBack.getInstance().initCallBack();
	super.onStart();
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_start_btn, R.id.timely_open_door, R.id.timely_left,
	   R.id.timely_right, R.id.btn_four_ly, R.id.btn_four_yc, R.id.btn_four_tb, R.id.btn_four_th})
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
			   case 2:
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
				   }
				});
				builder.create().show();
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		EventBusUtils.postSticky(new Event.EventFrag("START1"));

		finish();
		break;
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   //		   mShowLoading = DialogUtils.showLoading(mContext);
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);

		   List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
		   mTCstInventoryDto.gettCstInventoryVos().clear();
		   deviceInventoryVos.clear();

		   for (String deviceInventoryVo : mEthDeviceIdBack) {
			String deviceCode = deviceInventoryVo;
			LogUtils.i(TAG, "deviceCode    " + deviceCode);
			startScan(deviceCode);
		   }
		}
		break;

	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   //确认
		   mIntentType = 1;
		   if (mTCstInventoryVos != null) {
			if (mTCstInventoryDto.getOperation() == 9) {//移出
			   setYcDate(mIntentType);
			} else if (mTCstInventoryDto.getOperation() == 11) {//调拨
			   setDbDate(mIntentType);
			} else if (mTCstInventoryDto.getOperation() == 8) {//退货
			   setThDate(mIntentType);
			} else {//其他操作
			   setDate(mIntentType);
			}

		   } else {
			ToastUtils.showShort("数据异常");
		   }

		}

		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mIntentType = 2;
		   if (mTCstInventoryVos != null) {
			if (mTCstInventoryDto.getOperation() == 9) {//移出
			   setYcDate(mIntentType);
			} else if (mTCstInventoryDto.getOperation() == 11) {//调拨
			   setDbDate(mIntentType);
			} else if (mTCstInventoryDto.getOperation() == 8) {//退货
			   setThDate(mIntentType);
			} else {//其他操作
			   setDate(mIntentType);
			}

		   } else {
			ToastUtils.showShort("数据异常");
		   }
		}
		break;
	   case R.id.timely_open_door:
		List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
		mTCstInventoryDto.gettCstInventoryVos().clear();
		deviceInventoryVos.clear();
		for (String deviceInventoryVo : mEthDeviceIdBack) {
		   String deviceCode = deviceInventoryVo;
		   LogUtils.i(TAG, "deviceCode    " + deviceCode);
		   DeviceManager.getInstance().OpenDoor(deviceCode);
		}
		//		List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
		//		for (DeviceInventoryVo deviceInventoryVo : deviceInventoryVos) {
		//		   String deviceCode = deviceInventoryVo.getDeviceCode();
		//		   LogUtils.i(TAG, "deviceCode    " + deviceCode);
		//		   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", deviceCode,
		//									    UHF_TYPE).find(BoxIdBean.class);
		//		   for (BoxIdBean boxIdBean : boxIdBeans) {
		//			String device_id = boxIdBean.getDevice_id();
		//			LogUtils.i(TAG, "device_id    " + device_id);
		//
		//			int i = DeviceManager.getInstance().OpenDoor(device_id);
		//			LogUtils.i(TAG, "i    " + i);
		//		   }
		//		}

		break;
	}

   }

   private void startScan(String deviceIndentify) {
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = DeviceManager.getInstance().StartUhfScan(device_id);

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
   }

   /**
    * 调拨
    */
   private void setDbDate(int mIntentType) {
	mType = 3;//1.8调拨

	String branchCode = SPUtils.getString(UIUtils.getContext(), SAVE_BRANCH_CODE);
	String deptCode = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	LogUtils.i(TAG, "8调拨branchCode   " + branchCode);
	LogUtils.i(TAG, "8调拨deptCode  " + deptCode);
	NetRequest.getInstance()
		.getOperateDbDialog(deptCode, branchCode, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "8调拨   " + result);
			HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
			DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean, mIntentType);
			//		List<HospNameBean.TcstBaseStorehousesBean> baseStorehouses = hospNameBean.getTcstBaseStorehouses();

		   }
		});

   }

   /**
    * 移出
    */
   private void setYcDate(int mIntentType) {
	mType = 1;//1.6移出
	String deptCode = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getOperateYcDeptYes(deptCode, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "库房   " + result);

		HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
		DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean, mIntentType);

	   }
	});

   }

   /**
    * 设置提交值
    */
   private void setDate(int mIntentType) {

	TCstInventoryDto dto = new TCstInventoryDto();
	dto.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	dto.settCstInventoryVos(mTCstInventoryVos);
	dto.setOperation(mTCstInventoryDto.getOperation());
	dto.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
	dto.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	//	if (mTCstInventoryDto)
	//	dto.setOperation(-1);//多个操作
	String s = mGson.toJson(dto);
	LogUtils.i(TAG, "返回  " + s);
	NetRequest.getInstance().putOperateYes(s, this, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result  " + result);
		ToastUtils.showShort("操作成功");
		if (mIntentType == 2) {
		   mContext.startActivity(new Intent(mContext, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		}
		if (mShowLoading != null) {
		   mShowLoading.mDialog.dismiss();
		}
		EventBusUtils.postSticky(new Event.EventFrag("START1"));
		finish();
	   }
	});
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
	   deviceInventoryVo.setDeviceCode(box_id);
	}
	deviceInventoryVo.settCstInventories(epcList);
	deviceList.add(deviceInventoryVo);

	tCstInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
	tCstInventoryDto.setOperation(mTCstInventoryDto.getOperation());
	tCstInventoryDto.setDeviceInventoryVos(deviceList);
	tCstInventoryDto.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	String toJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	NetRequest.getInstance().putEPCDate(toJson, this, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mTCstInventoryTwoDto = mGson.fromJson(result, TCstInventoryDto.class);
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
	} else if (mTCstInventoryTwoDto.getErrorEpcs() == null &&
		     (mTCstInventoryTwoDto.gettCstInventoryVos() == null ||
			mTCstInventoryTwoDto.gettCstInventoryVos().size() < 1) &&
		     mEthDeviceIdBack.size() == 1) {

	   if (mShowLoading != null) {
		mShowLoading.mDialog.dismiss();
	   }

	   if (mTimelyLeft != null && mTimelyRight != null) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
	   }
	   EventBusUtils.postSticky(new Event.EventAct(mActivityType));
	   EventBusUtils.postSticky(mTCstInventoryTwoDto);
	   ToastUtils.showLong("未扫描到操作的耗材,即将返回主界面，请重新操作");
	   new Handler().postDelayed(new Runnable() {
		public void run() {
		   EventBusUtils.postSticky(new Event.EventFrag("START1"));
		   finish();
		}
	   }, 3000);

	} else {
	   List<TCstInventoryVo> tCstInventoryVos = mTCstInventoryDto.gettCstInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
	   List<TCstInventoryVo> tCstInventoryVos1 = mTCstInventoryTwoDto.gettCstInventoryVos();
	   List<DeviceInventoryVo> deviceInventoryVos1 = mTCstInventoryTwoDto.getDeviceInventoryVos();

	   Set<DeviceInventoryVo> set = new HashSet<DeviceInventoryVo>();
	   set.addAll(deviceInventoryVos);
	   set.addAll(deviceInventoryVos1);
	   List<DeviceInventoryVo> c = new ArrayList<DeviceInventoryVo>(set);

	   tCstInventoryVos1.addAll(tCstInventoryVos);
	   tCstInventoryVos1.removeAll(tCstInventoryVos);
	   tCstInventoryVos1.addAll(tCstInventoryVos);
	   mTCstInventoryTwoDto.settCstInventoryVos(tCstInventoryVos1);
	   mTCstInventoryTwoDto.setDeviceInventoryVos(c);

	   EventBusUtils.postSticky(new Event.EventAct(mActivityType));
	   EventBusUtils.postSticky(mTCstInventoryTwoDto);

	}
	if (mShowLoading != null) {
	   mShowLoading.mDialog.dismiss();

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
	mDtoLy.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setStorehouseRemark(event.context);
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
	if (mTCstInventoryTwoDto == null) {
	   for (int i = 0; i < mTCstInventoryDto.gettCstInventoryVos().size(); i++) {
		tCstInventoryVos.add(mTCstInventoryDto.gettCstInventoryVos().get(i));
	   }
	} else {
	   for (int i = 0; i < mTCstInventoryTwoDto.gettCstInventoryVos().size(); i++) {
		tCstInventoryVos.add(mTCstInventoryTwoDto.gettCstInventoryVos().get(i));
	   }
	}
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mDtoLy.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "移出   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result移出   " + result);
			ToastUtils.showShort("操作成功");
			if (event.mIntentType == 2) {
			   startActivity(new Intent(InOutBoxTwoActivity.this, LoginActivity.class));
			}
			if (mShowLoading != null) {
			   mShowLoading.mDialog.dismiss();
			}
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
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
	super.onDestroy();
	mEthDeviceIdBack.clear();
   }

   /**
    * 提交退货的所有数据
    *
    * @param event
    */
   private void putThDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	mDtoLy.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setOperation(8);
	mDtoLy.setRemark(event.context);
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
	if (mTCstInventoryTwoDto == null) {
	   for (int i = 0; i < mTCstInventoryDto.gettCstInventoryVos().size(); i++) {
		tCstInventoryVos.add(mTCstInventoryDto.gettCstInventoryVos().get(i));
	   }
	} else {
	   for (int i = 0; i < mTCstInventoryTwoDto.gettCstInventoryVos().size(); i++) {
		tCstInventoryVos.add(mTCstInventoryTwoDto.gettCstInventoryVos().get(i));
	   }
	}
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mDtoLy.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "退货   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result退货   " + result);
			ToastUtils.showShort("操作成功");
			if (mShowLoading != null) {
			   mShowLoading.mDialog.dismiss();
			}
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
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
	mDtoLy.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setOperation(11);
	mDtoLy.setStorehouseCode(event.context);
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
	if (mTCstInventoryTwoDto == null) {
	   for (int i = 0; i < mTCstInventoryDto.gettCstInventoryVos().size(); i++) {
		tCstInventoryVos.add(mTCstInventoryDto.gettCstInventoryVos().get(i));
	   }
	} else {
	   for (int i = 0; i < mTCstInventoryTwoDto.gettCstInventoryVos().size(); i++) {
		tCstInventoryVos.add(mTCstInventoryTwoDto.gettCstInventoryVos().get(i));
	   }
	}
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mDtoLy.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);

	LogUtils.i(TAG, "调拨   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result调拨   " + result);
			ToastUtils.showShort("操作成功");
			if (mShowLoading != null) {
			   mShowLoading.mDialog.dismiss();
			}
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
			finish();
		   }

		   @Override
		   public void onError(String result) {
			ToastUtils.showShort("操作失败，请重试！");
		   }
		});
   }

}
