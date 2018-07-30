package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

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
import high.rivamed.myapplication.bean.InBoxDtoBean;
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
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

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
   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_IN;
	return my_id;
   }
//   @Override
//   protected void onStart() {
//	super.onStart();
//	Intent intent = getIntent();
//	mActivityType=intent.getStringExtra("type");
//   }

//   @Override
//   public String getActivityType() {
//	Intent intent = getIntent();
//	super.mActivityType=intent.getStringExtra("type");
//	return mActivityType;
//   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_start_btn, R.id.timely_left, R.id.timely_right,
	   R.id.btn_four_ly, R.id.btn_four_yc, R.id.btn_four_tb, R.id.btn_four_th})
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
		initCallBack();
		for (String readerid : ContentConsumeOperateFrag.mReaderIdList) {
		   LogUtils.i(TAG, "readerid    " + readerid);
		   DeviceManager.getInstance().StartUhfScan(readerid);
		}

		break;
	}


	if (mActivityType.equals("all")){
	   switch (view.getId()) {
		case R.id.btn_four_ly:
		   DialogUtils.showNoDialog(mContext, "耗材领用成功！", 2, "nojump", null);
		   break;
		case R.id.btn_four_yc:
		   mType = 1;//1.6移出
		   DialogUtils.showStoreDialog(mContext,3, mType);
		   break;
		case R.id.btn_four_tb:
		   mType =  3;//1.8调拨
		   DialogUtils.showStoreDialog(mContext,2, mType);
		   break;
		case R.id.btn_four_th:
		   mType = 2;//1.7退货
		   DialogUtils.showStoreDialog(mContext,2, mType);
		   break;
	   }
	}else {
	   switch (view.getId()) {
		case R.id.timely_left:
		   ToastUtils.showShort("timely_left");
		   break;
		case R.id.timely_right:
		   ToastUtils.showShort("timely_right");
		   break;
	   }
	}

	}
   private String                             uhfDeviceId;
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

		getDeviceDate(deviceId, epcs);
	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {

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
	NetRequest.getInstance().putEPCDate(toJson, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		//TCstInventoryDto
		InBoxDtoBean inBoxDtoBean = mGson.fromJson(result, InBoxDtoBean.class);

		EventBusUtils.postSticky(inBoxDtoBean);


	   }
	});
   }

}
