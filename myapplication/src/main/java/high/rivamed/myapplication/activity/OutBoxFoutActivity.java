package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.AllOutBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
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
import high.rivamed.myapplication.views.NoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

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
   private TCstInventoryDto      mDtoLyFour;
   private TCstInventoryDto mDtoLy = new TCstInventoryDto();
   public  TCstInventoryDto      mNewDtos;
   private int                   mIntentType;
   private LoadingDialog.Builder mLoading;
   public  ArrayList<String>          mDoorList  = new ArrayList<>();
   private Map<String, List<TagInfo>> mEPCDate   = new TreeMap<>();
   private Map<String, String>        mEPCDatess = new TreeMap<>();
   int k = 0;
   public  List<TCstInventoryVo> mVoOutList;
   private boolean               mDate;
   private TCstInventoryDto      mCstInEpcDto;
   private String                mToJson;
   private int mSelType = 0;
   private List<TCstInventoryVo> mMTCstInventoryVoss;
   public static boolean mOnOutDestroy =false;
   private NoDialog.Builder mShowNoDialog;
   private NoDialog.Builder mShowNoDialog2;

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_OUT;
	return my_id;
   }

   /**
    * 再次显示后的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventFourDate(Event.EventDate event) {
	mDate = event.b;
	LogUtils.i(TAG, "mDate  " + mDate + "    mSelType   " + mSelType);

        if (mDate) {
            if (mSelType == 1 && event.moreScan) {
                //		LogUtils.i(TAG, "mTCstInventoryDto  " + mOutDto.gettCstInventoryVos().size());
                mStarts.cancel();
                moreStartScan();
            } else if (mSelType != 1 && mSelType != 0) {
                //		LogUtils.i(TAG, "mTCstInventoryDto  " + mOutDto.gettCstInventoryVos().size());
                //		if (mTCstInventoryDtoFour == null) {
                mOutDto.setStorehouseCode(
                        SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
                EventBusUtils.postSticky(new Event.EventOutDto(mOutDto, mInJson));
                //		}
                //		else {
                //		   mTCstInventoryDtoFour.setStorehouseCode(
                //			   SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
                //		   EventBusUtils.postSticky(new Event.EventOutDto(mTCstInventoryDtoFour));
                //		}
                //		LogUtils.i(TAG,"mO    "+mCstInEpcDto.gettCstInventoryVos().size());
                if (mOutDto.gettCstInventoryVos().size() == 0 && mCstInEpcDto != null &&
                        mCstInEpcDto.gettCstInventoryVos().size() != 0) {
                    Toast.makeText(mContext, "出柜完成，请继续入柜操作", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OutBoxFoutActivity.this, InBoxAllTwoActivity.class));
                    EventBusUtils.postSticky(new Event.EventAct("all"));
                    EventBusUtils.postSticky(mCstInEpcDto);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.UNCONFIRM_SUC);
                } else {
                    if (mOutDto.gettCstInventoryVos().size() == 0) {
                        mEthDeviceIdBack.clear();
                        finish();
                    } else {
                        mTypeView.mOutBoxAllAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

   /**
    * 顶部的红色提示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventTitle(Event.EventOutTitleV event) {
	if (event.b&&mAllOutText!=null) {
	   mAllOutText.setVisibility(View.VISIBLE);
	}
   }

   /**
    * 扫描后EPC准备传值
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onCallBackEvent(Event.EventDeviceCallBack event) {
	LogUtils.i(TAG, "epc  " + event.deviceId + "   " + event.epcs.size());
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}

	List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
		.find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeanss) {
	   String box_id = boxIdBean.getBox_id().trim();
	   List<BoxIdBean> boxIdDoor = LitePal.where("box_id = ? and name = ?", box_id, UHF_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean BoxIdBean : boxIdDoor) {
		String device_id = BoxIdBean.getDevice_id();
		LogUtils.i(TAG, "device_id  " + device_id );
		for (int x = 0; x < mDoorList.size(); x++) {

		   if (device_id.equals(mDoorList.get(x).trim())) {
			LogUtils.i(TAG, "mDoorList  " + mDoorList.get(x) );
			mDoorList.remove(x);
		   }
		}
	   }
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
		   LogUtils.i(TAG, "mEPCDate  k  " + k);
		   if (k == boxIdBeansss.size()) {
			k = 0;
			if (mEPCDate.size() == 0) {
			   mEPCDatess.put("", box_id);//没有空格
			}
			for (Map.Entry<String, List<TagInfo>> v : mEPCDate.entrySet()) {
			   mEPCDatess.put(v.getKey(), box_id);
			}
			LogUtils.i(TAG, "mEPCDates.mEPCDates()多reader  " + mEPCDatess.size());

		   } else {
			return;
		   }

		} else {
		   if (event.epcs.size() == 0) {
			mEPCDatess.put(" ", box_id);//1个空格
		   }
		   for (Map.Entry<String, List<TagInfo>> v : event.epcs.entrySet()) {
			mEPCDatess.put(v.getKey(), box_id);
		   }
		   LogUtils.i(TAG, "mEPCDates.mEPCDates()单reader  " + mEPCDatess.size());
		}
	   }
	}

	LogUtils.i(TAG, "mDoorList.size()   " + mDoorList.size());
	if (mDoorList.size() != 0) {
	   return;
	}
	LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size());
	putAllOutEPCDates(mEPCDatess);

   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		LogUtils.i(TAG, "     mLoading  新建 ");
		mLoading = DialogUtils.showLoading(this);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
		   LogUtils.i(TAG, "     mLoading   重新开启");
		   mLoading.create().show();
		}
	   }
	} else {
	   if (mLoading != null) {
		LogUtils.i(TAG, "     mLoading   关闭");
		mLoading.mAnimationDrawable.stop();
		mLoading.mDialog.dismiss();
		mLoading = null;
	   }
	}
   }

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
   //

   @Override
   protected void onDestroy() {
      if (mShowNoDialog!=null){
	   mShowNoDialog.mDialog.dismiss();
	}
	if (mShowNoDialog2!=null){
	   mShowNoDialog2.mDialog.dismiss();
	}

	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
	mSelType = 0;
	LogUtils.i(TAG, "onDestroy  ");
	EventBusUtils.unregister(this);

	mEPCDate.clear();
	mEPCDatess.clear();

	   mOnOutDestroy =true;
	super.onDestroy();

   }

   @Override
   protected void onPause() {
	mOnOutDestroy = true;
	super.onPause();
   }

   @Override
   protected void onResume() {
	mOnOutDestroy =false;
	LogUtils.i(TAG, "onResume  ");
	super.onResume();
   }

   @Override
   public void onStart() {
	LogUtils.i(TAG, "onStart  ");
//	putAllInEPCDate(mInJson);
	mOnOutDestroy =false;
	mDoorList.clear();
	mDoorList.addAll(mEthDeviceIdBack);
	super.onStart();
   }

   /**
    * 提交所有调拨的数据
    *
    * @param event
    */
   private void putDbDates(Event.outBoxEvent event) {

	String mTCstInventoryDtoJsons;
	mDtoLy.setOperation(11);
	mDtoLy.setStorehouseCode(event.context);
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
//	if (mTCstInventoryDtoFour == null) {
	   for (int i = 0; i < mOutDto.gettCstInventoryVos().size(); i++) {
		if (mOutDto.gettCstInventoryVos().get(i).isSelected()) {
		   tCstInventoryVos.add(mOutDto.gettCstInventoryVos().get(i));
		}
	   }
//	} else {
//	   for (int i = 0; i < mTCstInventoryDtoFour.gettCstInventoryVos().size(); i++) {
//		if (mTCstInventoryDtoFour.gettCstInventoryVos().get(i).isSelected()) {
//		   tCstInventoryVos.add(mTCstInventoryDtoFour.gettCstInventoryVos().get(i));
//		}
//	   }
//	}
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);

	LogUtils.i(TAG, "调拨   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putAllOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
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
    *
    * @param event
    */
   private void putThDates(Event.outBoxEvent event) {
	String mTCstInventoryDtoJsons;
	mDtoLy.setOperation(8);
	mDtoLy.setRemark(event.context);
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
//	if (mTCstInventoryDtoFour == null) {
	   for (int i = 0; i < mOutDto.gettCstInventoryVos().size(); i++) {
		if (mOutDto.gettCstInventoryVos().get(i).isSelected()) {
		   tCstInventoryVos.add(mOutDto.gettCstInventoryVos().get(i));
		}
	   }
//	} else {
//	   for (int i = 0; i < mTCstInventoryDtoFour.gettCstInventoryVos().size(); i++) {
//		if (mTCstInventoryDtoFour.gettCstInventoryVos().get(i).isSelected()) {
//		   tCstInventoryVos.add(mTCstInventoryDtoFour.gettCstInventoryVos().get(i));
//		}
//	   }
//	}
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);

	LogUtils.i(TAG, "退货   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putAllOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result退货   " + result);
			MusicPlayer.getInstance().play(MusicPlayer.Type.RETURN_GOOD_SUC);
			ToastUtils.showShort("操作成功");
			overFinish();
		   }

		   @Override
		   public void onError(String result) {
			ToastUtils.showShort("操作失败，请重试！");
		   }
		});
   }

   /**
    * 提交移出的所有数据
    *
    * @param event
    */
   private void putYcDates(Event.outBoxEvent event) {

	String mTCstInventoryDtoJsons;
	mDtoLy.setOperation(9);
	mDtoLy.setStorehouseRemark(event.context);
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
//	if (mTCstInventoryDtoFour == null) {
	   for (int i = 0; i < mOutDto.gettCstInventoryVos().size(); i++) {
		if (mOutDto.gettCstInventoryVos().get(i).isSelected()) {
		   tCstInventoryVos.add(mOutDto.gettCstInventoryVos().get(i));
		}
	   }
//	} else {
//	   for (int i = 0; i < mTCstInventoryDtoFour.gettCstInventoryVos().size(); i++) {
//		if (mTCstInventoryDtoFour.gettCstInventoryVos().get(i).isSelected()) {
//		   tCstInventoryVos.add(mTCstInventoryDtoFour.gettCstInventoryVos().get(i));
//		}
//	   }
//	}
	if (getExceedTime(tCstInventoryVos)) {
	   return;
	}
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "移出   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance()
		.putAllOperateYes(mTCstInventoryDtoJsons, this, mShowLoading, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result移出   " + result);
			MusicPlayer.getInstance().play(MusicPlayer.Type.MOVE_OUT_SUC);

			overFinish();
		   }

		   @Override
		   public void onError(String result) {
			ToastUtils.showShort("操作失败，请重试！");
		   }
		});
   }

   /**
    * 完成操作后看是否还有耗材，无耗材跳转入柜界面或者直接退出
    */
   private void overFinish() {
//	if (mTCstInventoryDtoFour == null) {

//	   LogUtils.i(TAG,"xxxxxx  "+mOutDto.gettCstInventoryVos().size());
	   mMTCstInventoryVoss = new ArrayList<>();

	   for (int i = 0;i<mOutDto.gettCstInventoryVos().size();i++) {
		if (mOutDto.gettCstInventoryVos().get(i).isSelected()) {
		   mMTCstInventoryVoss.add(mOutDto.gettCstInventoryVos().get(i));
		}else {
		}
	   }
	   if (mMTCstInventoryVoss.size()==mOutDto.gettCstInventoryVos().size()){
		mOutDto.gettCstInventoryVos().clear();
	   }else {
	      for (int x=0;x<mOutDto.gettCstInventoryVos().size();x++){
	        for (TCstInventoryVo s:mMTCstInventoryVoss){
	           if (s.getEpc().equals(mOutDto.gettCstInventoryVos().get(x).getEpc())){
			  mOutDto.gettCstInventoryVos().remove(x);
		     }
		  }
		}
	   }

	   putAllInEPCDate(mInJson);
	mTypeView.mOutBoxAllAdapter.notifyDataSetChanged();
	EventBusUtils.postSticky(new Event.EventDate(true));
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn, R.id.btn_four_ly,
	   R.id.btn_four_yc, R.id.btn_four_tb, R.id.btn_four_th})
   public void onViewClicked(View view) {
	switch (view.getId()) {

	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mStarts.cancel();
		   moreStartScan();
		}
		break;
	   case R.id.btn_four_ly://领用 3
		//确认
		mIntentType = 1;
		mSelType = 1;

		setLyDate(mIntentType);
		break;
	   case R.id.btn_four_yc://移出
		//确认
		mIntentType = 1;
		mSelType = 2;

		setYcDate(mIntentType);
		break;
	   //	   case R.id.btn_four_tb://调拨
	   //		//确认
	   //		mIntentType = 1;
	   //		setDbDate(mIntentType);
	   //		break;
	   case R.id.btn_four_th://退货
		//确认
		mIntentType = 1;
		mSelType = 3;
		setThDate(mIntentType);
		break;
	}
   }

   private boolean getExceedTime(List<TCstInventoryVo> voList) {
	for (TCstInventoryVo s:voList){
	   if (s.getIsErrorOperation()==1){
		   DialogUtils.showNoDialog(mContext, "耗材中包含过期耗材，请查看！", 1, "noJump", null);
		   mTimelyRight.setBackgroundResource(R.drawable.bg_btn_gray_pre);
		return true;
	   }
	}
	return false;
   }

   /**
    * 重新扫描
    */
   private void moreStartScan() {
	mDoorList.clear();
	mDoorList.addAll(mEthDeviceIdBack);
	mEPCDate.clear();
	mEPCDatess.clear();
	mSelType = 0;
	if (mTCstInventoryDtoFour != null) {
	   mTCstInventoryDtoFour = null;
	}
	if (mVoOutList != null) {
	   mVoOutList.clear();
	}
	LogUtils.i(TAG, "mEthDeviceIdBack    " + mEthDeviceIdBack.size());
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
		int i = DeviceManager.getInstance().StartUhfScan(device_id, 3000);
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
	String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getOperateDbDialog(deptId, branchCode, this, null, new BaseResult() {
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
	String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getOperateYcDeptYes(deptId, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "库房   " + result);
		HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
		DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean, mIntentType);

	   }
	});

   }

   /**
    * 耗材领用区分 0 绑定患者；1直接领用
    */
   private void setLyDate(int mIntentType) {
	String mTCstInventoryDtoJson = null;
//	if (mTCstInventoryDtoFour == null) {
	   mOutDto.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	   mTCstInventoryDtoJson = setNewDate(mOutDto);
//	} else {
//	   mTCstInventoryDtoFour.setStorehouseCode(
//		   SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
//	   mTCstInventoryDtoJson = setNewDate(mTCstInventoryDtoFour);
//	}
	LogUtils.i(TAG, " 领用 " + mTCstInventoryDtoJson);
	TCstInventoryDto tCstInventoryDtos = mGson.fromJson(mTCstInventoryDtoJson,
									    TCstInventoryDto.class);
	List<TCstInventoryVo> voList = tCstInventoryDtos.gettCstInventoryVos();
	if (getExceedTime(voList)) {
	   return;
	}
	if (!UIUtils.getConfigType(mContext, CONFIG_007)) {//直接领取
	   if (mDtoLy != null && mDtoLy.gettCstInventoryVos().size() == 0) {
		ToastUtils.showShort("未选择耗材");
	   } else {

		NetRequest.getInstance()
			.putAllOperateYes(mTCstInventoryDtoJson, this, null, new BaseResult() {
			   @Override
			   public void onSucceed(String result) {
				LogUtils.i(TAG, "result 领用 " + result);
				MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
				mShowNoDialog = DialogUtils.showNoDialog(mContext, "耗材领用成功！", 2,
										     "nojump", null);
				overFinish();
			   }

			   @Override
			   public void onError(String result) {
				mShowNoDialog2 = DialogUtils.showNoDialog(mContext, "耗材领用失败，请重试！",
												   1, "nojump", null);
			   }
			});
	   }

	} else {//绑定患者
	   if (mDtoLy != null && mDtoLy.gettCstInventoryVos().size() == 0) {
		ToastUtils.showShort("未选择耗材");
	   } else {
		TCstInventoryDto tCstInventoryDto = mGson.fromJson(mTCstInventoryDtoJson,
										   TCstInventoryDto.class);
		if (tCstInventoryDto.gettCstInventoryVos().size()==mOutDto.gettCstInventoryVos().size()){
		   mOutDto.gettCstInventoryVos().clear();
		}else {
		   for (int x=0;x<mOutDto.gettCstInventoryVos().size();x++){
			for (TCstInventoryVo s:tCstInventoryDto.gettCstInventoryVos()){
			   if (s.getEpc().equals(mOutDto.gettCstInventoryVos().get(x).getEpc())){
				mOutDto.gettCstInventoryVos().remove(x);
			   }
			}
		   }
		}
		for (TCstInventoryVo c:mOutDto.gettCstInventoryVos()){
		  c.setSelected(true);
		}


		tCstInventoryDto.setBindType("afterBind");
		EventBusUtils.postSticky(new Event.EventButGone(true));

		startActivity(new Intent(OutBoxFoutActivity.this, OutBoxBingActivity.class));
		EventBusUtils.postSticky(tCstInventoryDto);

	   }
	}
   }

   /**
    * 给选择后的数据赋值
    *
    * @param tCstInventoryDto
    */
   private String setNewDate(TCstInventoryDto tCstInventoryDto) {
	mDtoLy.setThingCode(tCstInventoryDto.getThingCode());
	mDtoLy.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setOperation(3);
	mDtoLy.setType(tCstInventoryDto.getType());
	mDtoLy.setConfigPatientCollar(tCstInventoryDto.getConfigPatientCollar());
	List<TCstInventoryVo> tCstInventoryVos = new ArrayList<>();
	for (int i = 0; i < tCstInventoryDto.gettCstInventoryVos().size(); i++) {
	   if (tCstInventoryDto.gettCstInventoryVos().get(i).isSelected()) {
		tCstInventoryVos.add(tCstInventoryDto.gettCstInventoryVos().get(i));
	   }
	}
	mDtoLy.settCstInventoryVos(tCstInventoryVos);
	mDtoLy.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));

	String mTCstInventoryDtoJson = mGson.toJson(mDtoLy);
	return mTCstInventoryDtoJson;
   }

    /**
     * 扫描后传值
     */
    private void putAllOutEPCDates(Map<String, String> epcs) {
        mToJson = null;
        mToJson = getEpcDtoString(epcs);
        NetRequest.getInstance().putAllOutEPCDate(mToJson, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result s   " + result);
                mTCstInventoryDtoFour = mGson.fromJson(result, TCstInventoryDto.class);
                mVoOutList = mTCstInventoryDtoFour.gettCstInventoryVos();
                for (int i = 0; i < mVoOutList.size(); i++) {
                    mVoOutList.get(i).setSelected(true);
                }
                putAllInEPCDate(mToJson);
                String string = null;
                if (mTCstInventoryDtoFour.getErrorEpcs() != null &&
                        mTCstInventoryDtoFour.getErrorEpcs().size() > 0) {
                    string = StringUtils.listToString(mTCstInventoryDtoFour.getErrorEpcs());
                    ToastUtils.showLong(string);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
                }
                if (mVoOutList.size() == 0) {
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
                    mTypeView.mOutBoxAllAdapter.setEmptyView(inflate);
                    mTypeView.mOutBoxAllAdapter.notifyDataSetChanged();
                    putAllInEPCDate(mToJson);

		   //	ToastUtils.showShort("未扫描到操作的耗材");
		} else {
		   LogUtils.i(TAG,"重新来了");
		   EventBusUtils.postSticky(new Event.EventAct(mActivityType));
		   EventBusUtils.postSticky(new Event.EventOutDto(mTCstInventoryDtoFour,mInJson));
		}
	   }
	});
   }

   /**
    * 快速开柜入柜查询
    */
   private void putAllInEPCDate(String json) {

        NetRequest.getInstance().putAllInEPCDate(json, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result s   " + result);
                mCstInEpcDto = mGson.fromJson(result, TCstInventoryDto.class);
                String string = null;
                if (mAllOutText != null && mCstInEpcDto.gettCstInventoryVos().size() == 0) {
                    mAllOutText.setVisibility(View.GONE);
                }
                if (mCstInEpcDto.getErrorEpcs() != null && mCstInEpcDto.getErrorEpcs().size() > 0) {
                    string = StringUtils.listToString(mCstInEpcDto.getErrorEpcs());
                    ToastUtils.showLong(string);
                    LogUtils.i(TAG, " string   " + string);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
                }

                if (((mVoOutList != null && mVoOutList.size() == 0) || mVoOutList == null) &&
                        ((mTCstInventoryDtoFour != null &&
                                mTCstInventoryDtoFour.gettCstInventoryVos().size() == 0) ||
                                (mOutDto != null && mOutDto.gettCstInventoryVos().size() == 0)) &&
                        mCstInEpcDto.gettCstInventoryVos() != null &&
                        mCstInEpcDto.gettCstInventoryVos().size() != 0) {
                    mBtnFourLy.setEnabled(false);
                    mBtnFourYc.setEnabled(false);
                    mBtnFourTh.setEnabled(false);
                    mTCstInventoryDto = mCstInEpcDto;
                    mTCstInventoryDto.settCstInventoryVos(mCstInEpcDto.gettCstInventoryVos());
                    if (mVoOutList != null) {
                        Toast.makeText(mContext, "出柜完成，请继续入柜操作", Toast.LENGTH_SHORT).show();
                        MusicPlayer.getInstance().play(MusicPlayer.Type.UNCONFIRM_SUC);
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            EventBusUtils.postSticky(mCstInEpcDto);
                            //			   LogUtils.i(TAG,"mTCstInventoryDto   "+mCstInEpcDto.gettCstInventoryVos().size());
                            EventBusUtils.postSticky(new Event.EventAct("all"));
                            EventBusUtils.postSticky(new Event.EventDoorList(mDoorList));
                            startActivity(new Intent(OutBoxFoutActivity.this, InBoxAllTwoActivity.class));
                            finish();
                        }
                    }, 2000);

		} else {
			 LogUtils.i(TAG,"mffafafafafaf ");

			 //		   LogUtils.i(TAG,"mCstInEpcDto.gettCstInventoryVos().size()    "+mCstInEpcDto.gettCstInventoryVos().size());
		   if (mCstInEpcDto.gettCstInventoryVos().size() > 0) {
			LogUtils.i(TAG, "请重新操作");
			EventBusUtils.postSticky(new Event.EventOutTitleV(true));
		   } else {
			LogUtils.i(TAG,"mffaffffffffffffffffffffffffafafafaf ");
			if ((mVoOutList != null && mVoOutList.size() == 0)||(mOutDto!=null&&mOutDto.gettCstInventoryVos().size()==0)) {
			   mBtnFourLy.setEnabled(false);
			   mBtnFourYc.setEnabled(false);
			   mBtnFourTh.setEnabled(false);
			   Toast.makeText(mContext, "出柜操作完成!", Toast.LENGTH_SHORT).show();
			   new Handler().postDelayed(new Runnable() {
				public void run() {
				   EventBusUtils.postSticky(new Event.EventFrag("START1"));
				   mEthDeviceIdBack.clear();
				   finish();
				}
			   }, 3000);
			}
		   }
		}
	   }
	});
   }

   /**
    * 快速开柜epc放入DTO
    *
    * @param epcs
    * @return
    */
   private String getEpcDtoString(Map<String, String> epcs) {
	AllOutBean allOutBean = new AllOutBean();
	List<AllOutBean.TCstInventoryVos> epcList = new ArrayList<>();
	for (Map.Entry<String, String> v : epcs.entrySet()) {
	   AllOutBean.TCstInventoryVos tCstInventory = new AllOutBean.TCstInventoryVos();
	   tCstInventory.setEpc(v.getKey());
	   tCstInventory.setDeviceCode(v.getValue());
	   epcList.add(tCstInventory);
	}
	allOutBean.setTCstInventoryVos(epcList);
	allOutBean.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
	String toJson = mGson.toJson(allOutBean);
	LogUtils.i(TAG, "toJson s   " + toJson);
	return toJson;
   }

}
