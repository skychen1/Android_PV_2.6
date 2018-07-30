package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.InOutBoxTwoActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.InBoxDtoBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材操作主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentConsumeOperateFrag extends BaseSimpleFragment {

   @BindView(R.id.consume_openall_rv)
   RecyclerView mConsumeOpenallRv;
   @BindView(R.id.consume_openall_top)
   LinearLayout mConsumeOpenallTop;
   @BindView(R.id.function_title_meal)
   TextView     mFunctionTitleMeal;
   @BindView(R.id.function_cardview_meal)
   CardView     mFunctionCardviewMeal;
   @BindView(R.id.fastopen_title_form)
   TextView     mFastopenTitleForm;
   @BindView(R.id.function_cardview_form)
   CardView     mFunctionCardviewForm;
   @BindView(R.id.consume_openall_middle)
   LinearLayout mConsumeOpenallMiddle;
   @BindView(R.id.content_rb_ly)
   RadioButton  mContentRbLy;
   @BindView(R.id.content_rb_rk)
   RadioButton  mContentRbRk;
   @BindView(R.id.content_rb_yc)
   RadioButton  mContentRbYc;
   @BindView(R.id.content_rb_tb)
   RadioButton  mContentRbTb;
   @BindView(R.id.content_rb_yr)
   RadioButton  mContentRbYr;
   @BindView(R.id.content_rb_tuihui)
   RadioButton  mContentRbTuihui;
   @BindView(R.id.content_rb_tuihuo)
   RadioButton  mContentRbTuihuo;
   @BindView(R.id.content_rg)
   RadioGroup   mContentRg;
   @BindView(R.id.consume_down_rv)
   RecyclerView mConsumeDownRv;
   @BindView(R.id.consume_down)
   LinearLayout mConsumeDown;

   private HomeFastOpenAdapter                mHomeFastOpenTopAdapter;
   private       HomeFastOpenAdapter                mHomeFastOpenDownAdapter;
   private       List<String>                       eth002DeviceIdList;
   private       String                             eth002DeviceId;
   private       String                             uhfDeviceId;
   private       NoDialog.Builder                   mBuilder;
   private       List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;
   private       LoadingDialog.Builder              mBuilder1;
   private       HashMap<String, String>            mReaderMap;
   private       List<String>                       mReaderDeviceId;
   public static List<String>                       mReaderIdList;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialogEvent(Event.PopupEvent event) {
	if (event.isMute) {
	   if (mBuilder == null) {
		mBuilder = DialogUtils.showNoDialog(mContext, event.mString, 2, "in", null);
	   }
	} else {
	   mBuilder.mDialog.dismiss();
	   mBuilder = null;
	}
   }

   public static ContentConsumeOperateFrag newInstance() {

	Bundle args = new Bundle();
	ContentConsumeOperateFrag fragment = new ContentConsumeOperateFrag();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.ctconsumeoperate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	mBuilder1 = DialogUtils.showLoading(mContext);
	initCallBack();
	initData();

   }

   private void initCallBack() {
	DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
	   @Override
	   public void OnDeviceConnected(
		   DeviceType deviceType, String deviceIndentify) {
		if (deviceType == DeviceType.UHFREADER) {
		   uhfDeviceId = deviceIndentify;
		} else if (deviceType == DeviceType.Eth002) {
		   eth002DeviceId = deviceIndentify;
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
		if (success) {
		   EventBusUtils.post(new Event.PopupEvent(success, "柜门已开"));
		}
	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {
		if (success) {
		   //		   List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?" , deviceIndentify)
		   //			   .find(BoxIdBean.class);
		   //		   mReaderMap = new HashMap<>();
		   //		   for (BoxIdBean boxIdBean :boxIdBeans){
		   //			String device_id = boxIdBean.getDevice_id();
		   //			String box_id = boxIdBean.getBox_id();
		   //			mReaderMap.put(device_id, box_id);
		   //		   }

		   int ret = DeviceManager.getInstance().StartUhfScan(uhfDeviceId);
		   LogUtils.i(TAG, "关门了，开始扫描了" + ret);
		   for (String readerid : mReaderIdList) {
			LogUtils.i(TAG, "关门了，readerid    " + readerid);
			DeviceManager.getInstance().StartUhfScan(readerid);
		   }
		   //		   EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
		   //		   EventBusUtils.postSticky(new Event.EventAct("all"));
		   //		   Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
		   //		   mContext.startActivity(intent2);
		}
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
	NetRequest.getInstance().putEPCDate(toJson, _mActivity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i(TAG, "result    " + result);
		//TCstInventoryDto
		InBoxDtoBean inBoxDtoBean = mGson.fromJson(result, InBoxDtoBean.class);
//		List<InBoxDtoBean.TCstInventoryVosBean> cstInventoryVos = inBoxDtoBean.getTCstInventoryVos();
		EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
		EventBusUtils.postSticky(new Event.EventAct("all"));
		Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
		EventBusUtils.postSticky(inBoxDtoBean);
		mContext.startActivity(intent2);

	   }
	});
   }

   private void initData() {
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	loadDate();

   }

   //数据加载
   private void loadDate() {
	NetRequest.getInstance().loadBoxSize(mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
		mTbaseDevices = boxSizeBean.getTbaseDevices();
		BoxSizeBean.TbaseDevicesBean tbaseDevicesBean = new BoxSizeBean.TbaseDevicesBean();
		tbaseDevicesBean.setDeviceName("全部开柜");
		mTbaseDevices.add(0, tbaseDevicesBean);
		onSucceedDate();
	   }

	   @Override
	   public void onError(String result) {
		mBuilder1.mDialog.dismiss();
	   }
	});
   }

   //赋值
   private void onSucceedDate() {
	mBuilder1.mDialog.dismiss();
	//	mConsumeOpenallMiddle.setVisibility(View.GONE);//此处部分医院不需要可以隐藏  根据接口来
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabBtnLeft.setText("导管室");
	mBaseTabTvTitle.setText("耗材操作");

	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
	mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									  mTbaseDevices);
	mConsumeOpenallRv.setLayoutManager(layoutManager);
	mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

	mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		LogUtils.i("TT", " position  " + position);

		if (position == 0) {
		   List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", READER_TYPE)
			   .find(BoxIdBean.class);
		   mReaderMap = new HashMap<>();
		   for (BoxIdBean boxIdBean : boxIdBeans) {
			String device_id = boxIdBean.getDevice_id();
			String box_id = boxIdBean.getBox_id();
			mReaderMap.put(device_id, box_id);
		   }
		   for (String deviceId : eth002DeviceIdList) {
			DeviceManager.getInstance().OpenDoor(deviceId);
		   }
		} else {
		   BoxSizeBean.TbaseDevicesBean devicesBean = mTbaseDevices.get(position);
		   String deviceCode = devicesBean.getDeviceCode();
		   LogUtils.i(TAG, "deviceCode   " + deviceCode + " READER_TYPE  " + READER_TYPE);
		   List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", deviceCode,
									    READER_TYPE).find(BoxIdBean.class);
		   if (mReaderIdList != null) {
			mReaderIdList.clear();
		   } else {
			mReaderIdList = new ArrayList<>();
		   }

		   for (BoxIdBean boxIdBean : boxIdBeans) {
			String device_id = boxIdBean.getDevice_id();
			mReaderIdList.add(device_id);
		   }
		   for (int i = 0; i < eth002DeviceIdList.size(); i++) {
			if ((position - 1) == i) {
			   DeviceManager.getInstance().OpenDoor(eth002DeviceIdList.get(i));
			}
		   }
		}

		//		DeviceManager.getInstance().OpenDoor("xxx");
		//		if (position == 0){
		//
		//
		//		}
		// else if (position == 1){
		//		   DialogUtils.showNoDialog(mContext, title, 2,"out",null);
		//		}else if (position ==2){
		//		   DialogUtils.showNoDialog(mContext, title, 2,"out","bing");
		//		}else if (position == 3){
		//		   ToastUtils.showShort("按套餐领用-绑定患者！");
		//		   mContext.startActivity(new Intent(mContext,OutMealActivity.class));
		//		   EventBusUtils.postSticky(new Event.EventAct("BING_MEAL"));
		//
		//		}else if (position ==4){
		//
		//		}

		//		if (position == 0) {
		//		   int mType = 1;//1.8.3未绑定
		//		   showTwoDialog(mType);
		//		} else if (position == 1) {
		//		   int mType = 1;//1.6移出
		//		   showStoreDialog(3, mType);
		//		} else if (position == 2) {
		//		   int mType = 2;//1.7退货
		//		   showStoreDialog(2, mType);
		//		} else if (position == 3) {
		//		   int mType = 3;//1.8调拨
		//		   showStoreDialog(2, mType);
		//		} else if (position == 4) {
		//		   int mType = 2;//1.9.3请领单
		//		   showTwoDialog(mType);
		//		} else if (position == 5) {//1.2错误
		//		   String title = "耗材中包含过期耗材，请查看！";
		//		   showNoDialog(title, 1);
		//		} else if (position == 6) {//1.8.1选择患者
		//		   showRvDialog();
		//		} else {
		//		   String title = "柜门已开";
		//		   showNoDialog(title, 2);
		//		}
	   }
	});
	LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
	layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
	mHomeFastOpenDownAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									   mTbaseDevices);
	mConsumeDownRv.setLayoutManager(layoutManager2);
	mConsumeDownRv.setAdapter(mHomeFastOpenDownAdapter);
	mHomeFastOpenDownAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		int id = mContentRg.getCheckedRadioButtonId();
		if (id == -1) {
		   ToastUtils.showShort("请选择操作方式！");
		} else {
		   switch (id) {
			case R.id.content_rb_ly:
			   ToastUtils.showShort("领用！");//拿出
			   Intent intent0 = new Intent(mContext, InOutBoxTwoActivity.class);
			   //			   intent0.putExtra("type","inout");
			   mContext.startActivity(intent0);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_rk:
			   ToastUtils.showShort("入库！");//拿入
			   Intent intent1 = new Intent(mContext, InOutBoxTwoActivity.class);
			   //			   intent1.putExtra("type","inout");
			   mContext.startActivity(intent1);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));

			   break;
			case R.id.content_rb_yc:
			   ToastUtils.showShort("移出！");//拿出
			   Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
			   //			   intent2.putExtra("type","inout");
			   mContext.startActivity(intent2);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_tb:
			   ToastUtils.showShort("调拨！");//拿出
			   Intent intent3 = new Intent(mContext, InOutBoxTwoActivity.class);
			   mContext.startActivity(intent3);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_yr:
			   ToastUtils.showShort("移入！");//拿入
			   Intent intent4 = new Intent(mContext, InOutBoxTwoActivity.class);
			   //			   intent4.putExtra("type","inout");
			   mContext.startActivity(intent4);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_tuihui:
			   ToastUtils.showShort("退回！");//拿入
			   Intent intent5 = new Intent(mContext, InOutBoxTwoActivity.class);
			   //			   intent5.putExtra("type","inout");
			   mContext.startActivity(intent5);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
			case R.id.content_rb_tuihuo:
			   ToastUtils.showShort("退货！");//拿出
			   Intent intent6 = new Intent(mContext, InOutBoxTwoActivity.class);
			   //			   intent6.putExtra("type","inout");
			   mContext.startActivity(intent6);
			   EventBusUtils.postSticky(new Event.EventAct("inout"));
			   break;
		   }
		}
	   }
	});
   }

   @Override
   public void onPause() {
	super.onPause();

   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.function_title_meal, R.id.fastopen_title_form})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);

		popupClick();
		break;
	   case R.id.base_tab_btn_msg:

		break;
	   case R.id.function_title_meal:
		mContext.startActivity(new Intent(mContext, OutMealActivity.class));
		EventBusUtils.postSticky(new Event.EventAct("NOBING_MEAL"));
		break;
	   case R.id.fastopen_title_form:
		mContext.startActivity(new Intent(mContext, OutFormActivity.class));
		break;
	}
   }

}
