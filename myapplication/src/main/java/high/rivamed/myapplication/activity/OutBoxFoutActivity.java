package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.fragment.ContentConsumeOperateFrag;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 17:42
 * 描述:        从柜子拿出的界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutBoxFoutActivity extends BaseTimelyActivity {

   private static final String TAG = "OutBoxFoutActivity";
   int mType;
   private LoadingDialog.Builder mShowLoading;
   private TCstInventoryDto      mTCstInventoryDtoFour;
   private String                uhfDeviceId;

   /**
    * dialog操作数据
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEvent(Event.outBoxEvent event) {
	event.dialog.dismiss();
	if (event.type.equals("1")) {//移出
	   putYcDates(event);
	} else if (event.type.equals("2")) {//退货
	   putThDates(event);
	} else {//调拨
	   putDbDates(event);
	}
	LogUtils.i(TAG,"TAG    "+event.context);

   }
   /**
    * 接收入库的数据
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onInBoxEvent(TCstInventoryDto event) {

	   mTCstInventoryDto = event;
	   mTCstInventoryVos = event.gettCstInventoryVos();
   }
   /**
    * 提交所有调拨的数据
    * @param event
    */
   private void putDbDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	if (mTCstInventoryDtoFour == null) {
	   mTCstInventoryDto.setOperation(11);
	   mTCstInventoryDto.setStorehouseCode(event.context);
	   mTCstInventoryDtoJsons = mGson.toJson(mTCstInventoryDto);
	} else {
	   mTCstInventoryDtoFour.setOperation(11);
	   mTCstInventoryDtoFour.setStorehouseCode(event.context);
	   mTCstInventoryDtoJsons = mGson.toJson(mTCstInventoryDtoFour);
	}
	LogUtils.i(TAG, "调拨   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result调拨   " + result);
			ToastUtils.showShort("操作成功");
			finish();
		   }
		   @Override
		   public void onError(String result) {
			ToastUtils.showShort("操作失败，请重试！");
		   }
		});
   }

   /**
    * 提交退货的所有数据
    * @param event
    */
   private void putThDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	if (mTCstInventoryDtoFour == null) {
	   mTCstInventoryDto.setOperation(11);
	   mTCstInventoryDto.setRemake(event.context);
	   mTCstInventoryDtoJsons = mGson.toJson(mTCstInventoryDto);
	} else {
	   mTCstInventoryDtoFour.setOperation(11);
	   mTCstInventoryDtoFour.setRemake(event.context);
	   mTCstInventoryDtoJsons = mGson.toJson(mTCstInventoryDtoFour);
	}
	LogUtils.i(TAG, "退货   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result退货   " + result);
			ToastUtils.showShort("操作成功");
			finish();
		   }
		   @Override
		   public void onError(String result) {
			ToastUtils.showShort("操作失败，请重试！");
		   }
		});
   }

   /**
    * 提交移出的所有数据
    * @param event
    */
   private void putYcDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	if (mTCstInventoryDtoFour == null) {
	   mTCstInventoryDto.setOperation(9);
	   mTCstInventoryDto.setStorehouseRemark(event.context);
	   mTCstInventoryDtoJsons = mGson.toJson(mTCstInventoryDto);
	} else {
	   mTCstInventoryDtoFour.setOperation(9);
	   mTCstInventoryDtoFour.setStorehouseRemark(event.context);
	   mTCstInventoryDtoJsons = mGson.toJson(mTCstInventoryDtoFour);
	}
	LogUtils.i(TAG, "移出   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result移出   " + result);
			ToastUtils.showShort("操作成功");
			finish();
		   }

		   @Override
		   public void onError(String result) {
			ToastUtils.showShort("操作失败，请重试！");
		   }
		});
   }

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_OUT;
	return my_id;
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_start_btn, R.id.btn_four_ly, R.id.btn_four_yc,
	   R.id.btn_four_tb, R.id.btn_four_th})
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
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.timely_start_btn:
		mShowLoading = DialogUtils.showLoading(mContext);
		initCallBack();
		for (String readerid : ContentConsumeOperateFrag.mReaderIdList) {
		   LogUtils.i(TAG, "readerid    " + readerid);
		   int ret = DeviceManager.getInstance().StartUhfScan(readerid);
		   LogUtils.i(TAG, "readerid    " + ret);
		   if (ret == 100) {
			ToastUtils.showShort("扫描失败，请重试！");
			mShowLoading.mDialog.dismiss();
			Log.i(TAG, "我是错误进入扫描   " + ret);
		   } else {
			Log.i(TAG, "我是正常进入扫描   " + ret);
			DeviceManager.getInstance().StartUhfScan(readerid);
		   }
		}
		break;
	   case R.id.btn_four_ly://领用 3
		setLyDate();
		break;
	   case R.id.btn_four_yc://移出
		setYcDate();
		break;
	   case R.id.btn_four_tb://调拨
		setDbDate();
		break;
	   case R.id.btn_four_th://退货
		setThDate();
		break;
	}
   }

   /**
    * 退货
    */
   private void setThDate() {
	mType = 2;//1.7退货
	DialogUtils.showStoreDialog(mContext, 2, mType, null);
   }

   /**
    * 调拨
    */
   private void setDbDate() {
	mType = 3;//1.8调拨

	String branchCode = SPUtils.getString(UIUtils.getContext(), SAVE_BRANCH_CODE);
	String deptCode = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance()
		.getOperateDbDialog(deptCode, branchCode, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "8调拨   " + result);
			HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
			DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean);
			//		List<HospNameBean.TcstBaseStorehousesBean> baseStorehouses = hospNameBean.getTcstBaseStorehouses();

		   }
		});

   }

   /**
    * 移出
    */
   private void setYcDate() {
	mType = 1;//1.6移出

	String deptCode = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getOperateYcDeptYes(deptCode, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "库房   " + result);
		HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
		DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean);

	   }
	});

   }

   /**
    * 耗材领用区分 0 绑定患者；1直接领用
    */
   private void setLyDate() {
	String mTCstInventoryDtoJson;
	if (mTCstInventoryDtoFour == null) {
	   mTCstInventoryDto.setOperation(3);
	   mTCstInventoryDtoJson = mGson.toJson(mTCstInventoryDto);
	} else {
	   mTCstInventoryDtoFour.setOperation(3);
	   mTCstInventoryDtoJson = mGson.toJson(mTCstInventoryDtoFour);
	}
	if (mTCstInventoryDto.getConfigPatientCollar().equals("0") ||
	    (mTCstInventoryDtoFour != null &&
	     mTCstInventoryDtoFour.getConfigPatientCollar().equals("0"))) {//直接领取
	   LogUtils.i(TAG, " 领用 " + mTCstInventoryDtoJson);
	   NetRequest.getInstance()
		   .putOperateYes(mTCstInventoryDtoJson, this, mShowLoading, new BaseResult() {
			@Override
			public void onSucceed(String result) {
			   LogUtils.i(TAG, "result 领用 " + result);
			   DialogUtils.showNoDialog(mContext, "耗材领用成功！", 2, "nojump", null);
			   finish();
			}

			@Override
			public void onError(String result) {
			   DialogUtils.showNoDialog(mContext, "耗材领用失败，请重试！", 1, "nojump", null);
			}
		   });
	} else {//绑定患者
	   startActivity(new Intent(OutBoxFoutActivity.this, OutBoxBingActivity.class));
	   if (mTCstInventoryDtoFour == null) {
		mTCstInventoryDto.setOperation(3);
		EventBusUtils.postSticky(mTCstInventoryDto);
	   } else {
		mTCstInventoryDtoFour.setOperation(3);
		EventBusUtils.postSticky(mTCstInventoryDtoFour);
	   }

	}
   }

   private void initCallBack() {
	DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
	   @Override
	   public void OnDeviceConnected(
		   DeviceType deviceType, String deviceIndentify) {
		if (deviceType == DeviceType.UHFREADER) {
		   uhfDeviceId = deviceIndentify;
		}
	   }

	   @Override
	   public void OnDeviceDisConnected(
		   DeviceType deviceType, String deviceIndentify) {

	   }

	   @Override
	   public void OnCheckState(
		   DeviceType deviceType, String deviceId, Integer code) {

	   }

	   @Override
	   public void OnIDCard(String deviceId, String idCard) {

	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {

	   }

	   @Override
	   public void OnFingerRegExcuted(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {

	   }

	   @Override
	   public void OnDoorOpened(String deviceIndentify, boolean success) {

	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {

	   }

	   @Override
	   public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
	   }

	   @Override
	   public void OnUhfScanRet(
		   boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
		LogUtils.i(TAG, "再次扫描  " + success);

		getDeviceDate(deviceId, epcs);
	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {
		if (!success) {
		   ToastUtils.showShort("扫描失败，请重试！");
		}

		LogUtils.i(TAG, "再次扫描OnUhfScanComplete  " + success);
	   }

	   @Override
	   public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

	   }

	   @Override
	   public void OnUhfSetPowerRet(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

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
	tCstInventoryDto.setDeviceInventoryVos(deviceList);

	String toJson = mGson.toJson(tCstInventoryDto);
	LogUtils.i(TAG, "toJson    " + toJson);
	NetRequest.getInstance().putEPCDate(toJson, this, mShowLoading, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		mTCstInventoryDtoFour = mGson.fromJson(result, TCstInventoryDto.class);
		if (mTCstInventoryDtoFour.gettCstInventoryVos() == null) {
		   ToastUtils.showShort("未扫描到操作的耗材");
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		} else {
		   EventBusUtils.postSticky(mTCstInventoryDtoFour);
		}

		mShowLoading.mDialog.dismiss();
	   }

	});
   }
}
