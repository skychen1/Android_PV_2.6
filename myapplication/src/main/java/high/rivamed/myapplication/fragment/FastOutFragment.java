package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
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
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Android_PV_2.6.3
 * 创建者:      DanMing
 * 创建时间:    2018/12/7 10:04
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class FastOutFragment extends SimpleFragment {

   private static final String TAG = "FastOutFragment";
   @BindView(R.id.timely_start_btn)
   TextView           mTimelyStartBtn;
   @BindView(R.id.all_out_text)
   TextView           mAllOutText;
   @BindView(R.id.ly_bing_btn)
   TextView           mLyBingBtn;
   @BindView(R.id.ly_bind_patient)
   TextView           mLyBindPatient;
   @BindView(R.id.timely_ll_gone)
   LinearLayout       mTimelyLlGone;
   @BindView(R.id.timely_number_left)
   TextView           mTimelyNumberLeft;
   @BindView(R.id.timely_name)
   TextView           mTimelyName;
   @BindView(R.id.timely_rl_title)
   RelativeLayout     mTimelyRlTitle;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.header)
   MaterialHeader     mHeader;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.btn_four_ly)
   TextView mBtnFourLy;
   @BindView(R.id.btn_four_yc)
   TextView mBtnFourYc;
   @BindView(R.id.btn_four_th)
   TextView mBtnFourTh;
   @BindView(R.id.btn_four_tb)
  TextView mBtnFourTb;
   @BindView(R.id.activity_down_btn_four_ll)
   LinearLayout mActivityDownBtnFourLl;
   @BindView(R.id.timely_number)
   TextView     mTimelyNumber;
   List<String> titeleList = null;
   public int           mSize;
   public TableTypeView mTypeView;
   public List<InventoryVo> mInventoryVosOut = new ArrayList<>(); //入柜扫描到的epc信息
   @BindView(R.id.public_ll)
   LinearLayout mPublicLl;
   @BindView(R.id.timely_left)
   TextView     mTimelyLeft;
   @BindView(R.id.timely_right)
   TextView     mTimelyRight;
   @BindView(R.id.activity_down_btnll)
   LinearLayout mActivityDownBtnll;
   private int          mOutSize;
   private int          mInSize;
   public  InventoryDto mInOutDto;
   private int          mIntentType;
   private int mSelType = 0;
   int mType;
   private List<InventoryVo> mMTCstInventoryVosses;
   private InventoryDto mDtoLy = new InventoryDto();
   private NoDialog.Builder mShowNoDialog;
   private NoDialog.Builder mShowNoDialog2;

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
    * 接收快速开柜的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onOutDtoEvent(Event.EventOutDto event) {
	LogUtils.i(TAG, "event   " + event.outSize);
	LogUtils.i(TAG, "type   " + event.type);
	if (mInOutDto != null) {
	   mInOutDto.setOutInventoryVos(event.inventoryDto.getOutInventoryVos());
	   mInSize = event.inSize;
	   mOutSize = event.outSize;
	} else {
	   mInSize = event.inSize;
	   mOutSize = event.outSize;
	   mInOutDto = event.inventoryDto;
	   mInventoryVosOut = mInOutDto.getOutInventoryVos();
	}
	for (int i = 0; i < mInOutDto.getOutInventoryVos().size(); i++) {
	   mInOutDto.getOutInventoryVos().get(i).setSelected(true);
	}
	List<InventoryVo> voList = mInOutDto.getOutInventoryVos();
	for (int i = 0; i < voList.size(); i++) {
	   voList.get(i).setSelected(true);
	}
	if (mAllOutText != null && mInOutDto.getInInventoryVos().size() != 0) {
	   mAllOutText.setText("注意：您还有入柜耗材尚未确认，请完成操作后确认！");
	} else {
	   mAllOutText.setText("");
	}
	if (event.type != null && event.type.equals("moreScan")) {
	   LogUtils.i(TAG, "setOutBoxDate    " + 1);

	   setOutBoxDate(mInOutDto.getOutInventoryVos());
	}
   }

   public static FastOutFragment newInstance() {
	Bundle args = new Bundle();
	FastOutFragment fragment = new FastOutFragment();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   public int getLayoutId() {
	return R.layout.frg_fastout_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
//	mBtnFourLy = mContext.findViewById(R.id.btn_four_ly);
//	mBtnFourYc = mContext.findViewById(R.id.btn_four_yc);
//	mBtnFourTh = mContext.findViewById(R.id.btn_four_th);

	mActivityDownBtnFourLl.setVisibility(View.VISIBLE);
	mBtnFourTb.setVisibility(View.GONE);//隐藏调拨
	LogUtils.i(TAG, "setOutBoxDate    " + 2);
	setOutBoxDate(mInOutDto.getOutInventoryVos());

   }


   private void setOutBoxDate(List<InventoryVo> voList) {

	LogUtils.i(TAG, "voList.size()    " + voList.size());
	if (voList.size() == 0) {
	   mBtnFourLy.setEnabled(false);
	   mBtnFourTh.setEnabled(false);
	   mBtnFourYc.setEnabled(false);
	} else {
	   mBtnFourLy.setEnabled(true);
	   mBtnFourTh.setEnabled(true);
	   mBtnFourYc.setEnabled(true);
	}
	setOutBoxTitles(voList);
	String[] array = mContext.getResources().getStringArray(R.array.six_outbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(mContext, _mActivity, mInOutDto.getOutInventoryVos(),
						   titeleList, mSize, mLinearLayout, mRecyclerview,
						   mRefreshLayout, ACTIVITY, STYPE_OUT, -10);
	} else {
	   mTypeView.mOutBoxAllAdapter.getData().clear();
	   mTypeView.mOutBoxAllAdapter.addData(mInOutDto.getOutInventoryVos());
	   mTypeView.mOutBoxAllAdapter.notifyDataSetChanged();
	}
   }

   /**
    * 取出耗材 重新扫描后增减的数据  title显示
    */
   private void setOutBoxTitles(List<InventoryVo> voList) {
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : voList) {
	   strings.add(vosBean.getCstCode());
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							voList.size() + "</big></font>"));
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   /**
    * 提交移出的所有数据
    *
    * @param event
    */
   private void putYcDates(Event.outBoxEvent event) {

	String mTCstInventoryDtoJsons;
	mDtoLy.setOperation(9);
	mDtoLy.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mDtoLy.setToSthId(event.context);
	List<InventoryVo> inventoryVos = new ArrayList<>();
	for (int i = 0; i < mInOutDto.getOutInventoryVos().size(); i++) {
	   if (mInOutDto.getOutInventoryVos().get(i).isSelected()) {
		inventoryVos.add(mInOutDto.getOutInventoryVos().get(i));
	   }
	}
	if (getExceedTime(inventoryVos)) {
	   return;
	}
	mDtoLy.setInventoryVos(inventoryVos);
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);
	LogUtils.i(TAG, "移出   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance().putAllOperateYes(mTCstInventoryDtoJsons, this, new BaseResult() {
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
	EventBusUtils.postSticky(new Event.EventFastMoreScan(true));
	UnNetCstUtils.putUnNetOperateYes(mGson, _mActivity);//提交离线耗材和重新获取在库耗材数据
   }

   private boolean getExceedTime(List<InventoryVo> voList) {
	for (InventoryVo s : voList) {
	   if (s.getIsErrorOperation() == 1) {
		DialogUtils.showNoDialog(mContext, "耗材中包含过期耗材，请查看！", 1, "noJump", null);
		return true;
	   }
	}
	return false;
   }

   @OnClick({R.id.timely_start_btn, R.id.btn_four_ly, R.id.btn_four_yc, R.id.btn_four_tb,
	   R.id.btn_four_th})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   EventBusUtils.postSticky(new Event.EventFastMoreScan(true));
		}
		break;
	   case R.id.btn_four_ly:
		//确认
		mIntentType = 1;
		mSelType = 1;

		setLyDate();
		break;
	   case R.id.btn_four_yc:
		//确认
		mIntentType = 1;
		mSelType = 2;
		setYcDate(mIntentType);
		break;
	   //	   case R.id.btn_four_tb:
	   //		break;
	   case R.id.btn_four_th:
		//确认
		mIntentType = 1;
		mSelType = 3;
		setThDate(mIntentType);
		break;
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
	NetRequest.getInstance().getOperateDbDialog(deptId, branchCode, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "8调拨   " + result);
		HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
		DialogUtils.showStoreDialog(mContext, 2, mType, hospNameBean, mIntentType);
	   }
	});
   }

   /**
    * 移出
    */
   private void setYcDate(int mIntentType) {
	mType = 1;//1.6移出
	String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	NetRequest.getInstance().getHospBydept(deptId, this, new BaseResult() {
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
   private void setLyDate() {
	String mTCstInventoryDtoJson = null;
	mInOutDto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mTCstInventoryDtoJson = setNewDate(mInOutDto);
	LogUtils.i(TAG, " 领用 " + mTCstInventoryDtoJson);
	InventoryDto inventoryDtos = mGson.fromJson(mTCstInventoryDtoJson, InventoryDto.class);
	List<InventoryVo> voList = inventoryDtos.getInventoryVos();
	if (getExceedTime(voList)) {
	   return;
	}
	if (UIUtils.getConfigType(mContext, CONFIG_007) || UIUtils.getConfigType(mContext, CONFIG_019)) {//绑定患者
	   setBindLy(mTCstInventoryDtoJson);
	} else {//直接领取
	   setLy(mTCstInventoryDtoJson);
	}
   }

   private void setBindLy(String mTCstInventoryDtoJson) {
	if (mDtoLy != null && mDtoLy.getInventoryVos().size() == 0) {
	   ToastUtils.showShort("未选择耗材");
	} else {
	   InventoryDto inventoryDto = mGson.fromJson(mTCstInventoryDtoJson, InventoryDto.class);
	   if (inventoryDto.getInventoryVos().size() == mInOutDto.getOutInventoryVos().size()) {
		mInOutDto.getOutInventoryVos().clear();
	   } else {
		for (int x = 0; x < mInOutDto.getOutInventoryVos().size(); x++) {
		   for (InventoryVo s : inventoryDto.getInventoryVos()) {
			if (s.getEpc().equals(mInOutDto.getOutInventoryVos().get(x).getEpc())) {
			   mInOutDto.getOutInventoryVos().remove(x);
			}
		   }
		}
	   }
	   for (InventoryVo c : mInOutDto.getOutInventoryVos()) {
		c.setSelected(true);
	   }
	   inventoryDto.setBindType("afterBind");
	   EventBusUtils.postSticky(new Event.EventButGone(true));
	   startActivity(new Intent(mContext, OutBoxBingActivity.class));
	   EventBusUtils.postSticky(new Event.EventOutBoxBingDto(inventoryDto));
	}
   }

   private void setLy(String mTCstInventoryDtoJson) {
	if (mDtoLy != null && mDtoLy.getInventoryVos().size() == 0) {
	   ToastUtils.showShort("未选择耗材");
	} else {
	   NetRequest.getInstance()
		   .putAllOperateYes(mTCstInventoryDtoJson, this, new BaseResult() {
			@Override
			public void onSucceed(String result) {
			   LogUtils.i(TAG, "result 领用 " + result);
			   MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
			   mShowNoDialog = DialogUtils.showNoDialog(mContext, "耗材领用成功！", 2, "nojump",
										  null);
			   overFinish();
			}

			@Override
			public void onError(String result) {
			   mShowNoDialog2 = DialogUtils.showNoDialog(mContext, "耗材领用失败，请重试！", 1,
										   "nojump", null);
			}
		   });
	}
   }

   /**
    * 给选择后的数据赋值
    *
    * @param inventoryDto
    */
   private String setNewDate(InventoryDto inventoryDto) {
	mDtoLy.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	mDtoLy.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mDtoLy.setOperation(3);
	mDtoLy.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mDtoLy.setType(inventoryDto.getType());
	mDtoLy.setConfigPatientCollar(inventoryDto.getConfigPatientCollar());
	List<InventoryVo> inventoryVos = new ArrayList<>();
	for (int i = 0; i < inventoryDto.getOutInventoryVos().size(); i++) {
	   if (inventoryDto.getOutInventoryVos().get(i).isSelected()) {
		inventoryVos.add(inventoryDto.getOutInventoryVos().get(i));
	   }
	}
	mDtoLy.setInventoryVos(inventoryVos);
	String mTCstInventoryDtoJson = mGson.toJson(mDtoLy);
	return mTCstInventoryDtoJson;
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
	List<InventoryVo> inventoryVos = new ArrayList<>();
	for (int i = 0; i < mInOutDto.getOutInventoryVos().size(); i++) {
	   if (mInOutDto.getOutInventoryVos().get(i).isSelected()) {
		inventoryVos.add(mInOutDto.getOutInventoryVos().get(i));
	   }
	}
	mDtoLy.setInventoryVos(inventoryVos);
	mDtoLy.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);

	LogUtils.i(TAG, "退货   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance().putAllOperateYes(mTCstInventoryDtoJsons, this, new BaseResult() {
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
    * 提交所有调拨的数据
    *
    * @param event
    */
   private void putDbDates(Event.outBoxEvent event) {

	String mTCstInventoryDtoJsons;
	mDtoLy.setOperation(11);
	mDtoLy.setSthId(event.context);
	List<InventoryVo> inventoryVos = new ArrayList<>();
	for (int i = 0; i < mInOutDto.getOutInventoryVos().size(); i++) {
	   if (mInOutDto.getOutInventoryVos().get(i).isSelected()) {
		inventoryVos.add(mInOutDto.getOutInventoryVos().get(i));
	   }
	}

	mDtoLy.setInventoryVos(inventoryVos);
	mTCstInventoryDtoJsons = mGson.toJson(mDtoLy);

	LogUtils.i(TAG, "调拨   " + mTCstInventoryDtoJsons);
	NetRequest.getInstance().putAllOperateYes(mTCstInventoryDtoJsons, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result调拨   " + result);
		ToastUtils.showShort("操作成功");
		UnNetCstUtils.putUnNetOperateYes(mGson, _mActivity);//提交离线耗材和重新获取在库耗材数据
	   }

	   @Override
	   public void onError(String result) {
		ToastUtils.showShort("操作失败，请重试！");
	   }
	});
   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();
	mInOutDto.getInInventoryVos().clear();
	mInOutDto.getOutInventoryVos().clear();
	mInOutDto = null;
	if (mDtoLy.getInventoryVos() != null) {
	   mDtoLy.getInventoryVos().clear();
	   mDtoLy = null;
	}
	EventBusUtils.unregister(this);
   }
}
