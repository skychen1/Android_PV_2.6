package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.FastInOutBoxActivity;
import high.rivamed.myapplication.activity.HomeActivity;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.activity.FastInOutBoxActivity.mFastViewpager;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Android_PV_2.6.3
 * 创建者:      DanMing
 * 创建时间:    2018/12/7 10:07
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class FastInFragment extends SimpleFragment {

   private static final String TAG = "FastInFragment";
   @BindView(R.id.timely_start_btn)
   TextView mTimelyStartBtn;
   @BindView(R.id.all_out_text)
   TextView mAllOutText;

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
   TextView           mBtnFourLy;
   @BindView(R.id.btn_four_yc)
   TextView           mBtnFourYc;
   @BindView(R.id.btn_four_tb)
   TextView           mBtnFourTb;
   @BindView(R.id.btn_four_th)
   TextView           mBtnFourTh;
   @BindView(R.id.activity_down_btn_four_ll)
   LinearLayout       mActivityDownBtnFourLl;
   @BindView(R.id.timely_number)
   TextView           mTimelyNumber;
   List<String> titeleList = null;
   public int           mSize;
   public TableTypeView mTypeView;
   @BindView(R.id.public_ll)
   LinearLayout mPublicLl;
   @BindView(R.id.timely_left)
   TextView     mTimelyLeft;
   @BindView(R.id.timely_right)
   TextView     mTimelyRight;
   @BindView(R.id.activity_down_btnll)
   LinearLayout mActivityDownBtnTwoll;
   Unbinder unbinder;
   private       int            mDtoOperation;
   private       int            mIntentType;
   private       int            mOutSize;
   private       int            mInSize;
   public        InventoryDto   mInOutDto;
   public static CountDownTimer mStarts;
   public static boolean mOnResume =false;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (event.type) {
	   if (!event.bing) {//绑定的按钮转换
		for (InventoryVo b : mInOutDto.getInInventoryVos()) {
		   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 ) ||
			 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
			  b.getExpireStatus() != 0)) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			LogUtils.i(TAG, "SelInOutBoxTwoActivity   cancel");
			if (mStarts != null) {
			   mStarts.cancel();
			   mTimelyRight.setText("确认并退出登录");
			}
			return;
		   } else {
			LogUtils.i(TAG, "SelInOutBoxTwoActivity   start");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
			if (mStarts != null) {
			   mStarts.cancel();
			   mStarts.start();
			}
		   }
		}
	   }
	}
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
	   mInOutDto.setInInventoryVos(event.inventoryDto.getInInventoryVos());
	   mInSize = event.inSize;
	   mOutSize = event.outSize;
	} else {
	   mInSize = event.inSize;
	   mOutSize = event.outSize;
	   mInOutDto = event.inventoryDto;

	}
	mDtoOperation = mInOutDto.getOperation();
	if (mAllOutText != null && mInOutDto.getOutInventoryVos().size() != 0) {
	   mAllOutText.setText("注意：您还有出柜耗材尚未确认，请完成操作后确认！");
	} else {
	   mAllOutText.setText("");
	}
	if (event.type!=null&&event.type.equals("moreScan")){
	   setInBoxDate(mInOutDto.getInInventoryVos());
	}
   }
   /**
    * 选中入柜的界面开始倒计时
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void EventFastTimeStart(Event.EventFastTimeStart event) {
      if (mInOutDto!=null&&mTimelyRight!=null){
	   if (event.b) {
	      LogUtils.i(TAG,"true");
		setTimeStart();
	   }else {
		LogUtils.i(TAG,"false");
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	}
   }
   /**
    * 倒计时结束发起
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onOverEvent(Event.EventOverPut event) {
	if (event.b) {
	   LogUtils.i(TAG, "EventOverPut");
	   mIntentType = 2;//2确认并退出
	   setDate(mIntentType);
	}
   }

   public static FastInFragment newInstance() {
	Bundle args = new Bundle();
	FastInFragment fragment = new FastInFragment();
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
	if (((FastInOutBoxActivity) getActivity()).mCurrentFragment == FastInFragment.this) {
	   mOnResume = true;
	}
	if (mStarts == null) {
	   mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyRight);
	}
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	setInBoxDate(mInOutDto.getInInventoryVos());
	if (mFastViewpager.getCurrentItem()==0){
	   mStarts.cancel();
	}
   }

   @Override
   public void onPause() {
	super.onPause();
	mOnResume = false;
	mStarts.cancel();
   }

   /**
    * 设置提交值
    */
   private void setDate(int mIntentType) {

	InventoryDto dto = new InventoryDto();
	dto.setSthId(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	dto.setInventoryVos(mInOutDto.getInInventoryVos());
	dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));

	String s = mGson.toJson(dto);
	LogUtils.i(TAG, "返回  " + s);
	NetRequest.getInstance().putAllOperateYes(s, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result  " + result);
		ToastUtils.showShort("操作成功");
		MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
		if (mIntentType == 2) {
		   if (mInOutDto.getOutInventoryVos().size()==0){
			UIUtils.putOrderId(mContext);
			mContext.startActivity(new Intent(mContext, LoginActivity.class));
			App.getInstance().removeALLActivity_();
		   }else {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
		   }
		} else {
		   if (mInOutDto.getOutInventoryVos().size()==0){
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
			mContext.startActivity(new Intent(mContext, HomeActivity.class));
		   }else {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
		   }

		}
	   }
	});
   }

   /**
    * 快速开柜入柜后赋值界面
    */
   private void setInBoxDate(List<InventoryVo> voList) {
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	if (mInSize==0){
	   mStarts.cancel();
	   mTimelyRight.setEnabled(false);
	   mTimelyLeft.setEnabled(false);
	}
	String[] array = mContext.getResources().getStringArray(R.array.six_singbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	setInBoxTitles(mInOutDto);
	if (mTypeView != null) {
	   mTypeView.mInBoxAllAdapter.getData().clear();
	   mTypeView.mInBoxAllAdapter.addData(voList);
	   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	} else {
	   mTypeView = new TableTypeView(mContext, _mActivity, voList, titeleList, mSize,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   STYPE_IN, -10);
	   mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	}

   }

   private void setTimeStart() {
	for (InventoryVo b : mInOutDto.getInInventoryVos()) {
	   String status = b.getStatus();
	   LogUtils.i(TAG, "b.getPatientName()    " + (b.getPatientName() == null) + mDtoOperation);
	   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
		 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 && b.getExpireStatus() == 0 &&
		  mDtoOperation != 8) ||
		 (mDtoOperation == 3 && UIUtils.getConfigType(mContext, CONFIG_007) &&
		  b.getPatientName() == null)) {
		if (mDtoOperation == 8 && b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
		    b.getExpireStatus() == 0) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		   if (mStarts != null) {
			LogUtils.i(TAG, "true  ssssssfafafa mObject ss mObject ");
			mStarts.cancel();
			mStarts.start();
		   }
		} else {
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		}
	   } else {
		LogUtils.i(TAG, "SelInOutBoxTwoActivity.mStart   " + (mStarts == null));
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		if (mStarts != null) {
		   LogUtils.i(TAG, "true  mObject mObject ss mObject ");
		   mStarts.cancel();
		   mStarts.start();
		}
	   }
	}
   }

   /**
    * 给入柜的顶部设置数据和调整底部按钮选择状态
    */
   private void setInBoxTitles(InventoryDto mInOutDto) {
	ArrayList<String> strings = new ArrayList<>();
	for (InventoryVo vosBean : mInOutDto.getInInventoryVos()) {
	   strings.add(vosBean.getCstCode());
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumber.setText(Html.fromHtml(
		"入库：<font color='#262626'><big>" + mInOutDto.getCountTwoin() +
		"</big>&emsp</font>移入：<font color='#262626'><big>" + mInOutDto.getCountMoveIn() +
		"</big>&emsp</font>退回：<font color='#262626'><big>" + mInOutDto.getCountBack() +
		"</big>&emsp</font>耗材种类：<font color='#262626'><big>" + list.size() +
		"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
		mInOutDto.getInInventoryVos().size() + "</big></font>"));
	setTimeStart();
   }



   @OnClick({R.id.timely_start_btn, R.id.timely_left, R.id.timely_right})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   EventBusUtils.postSticky(new Event.EventFastMoreScan(true));
		   mStarts.cancel();
		   mTimelyRight.setText("确认并退出登录");
		}
		break;
	   case R.id.timely_left:
		//确认
		mIntentType = 1;
		if (mInOutDto.getInInventoryVos() != null) {
		   setDate(mIntentType);
		} else {
		   ToastUtils.showShort("数据异常");
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mIntentType = 2;
		   if (mInOutDto.getInInventoryVos() != null) {
			setDate(mIntentType);
		   } else {
			ToastUtils.showShort("数据异常");
		   }
		}
		break;
	}
   }

   /* 定义一个倒计时的内部类 */
   public class TimeCount extends CountDownTimer {

	TextView textView;

	public TimeCount(long millisInFuture, long countDownInterval, TextView textView) {

	   super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
	   this.textView = textView;
	}

	@Override
	public void onFinish() {// 计时完毕时触发
	   EventBusUtils.post(new Event.EventOverPut(true));
	}

	@Override
	public void onTick(long millisUntilFinished) {// 计时过程显示
	   LogUtils.i(TAG, "millisUntilFinished     " + millisUntilFinished);
	   if (millisUntilFinished / 1000 <= 35) {
		textView.setText("确认并退出登录 " + "( " + millisUntilFinished / 1000 + " s )");
	   } else {
		textView.setText("确认并退出登录");
	   }
	}
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();
	mInOutDto.getInInventoryVos().clear();
	mInOutDto.getOutInventoryVos().clear();
	mInOutDto=null;
	EventBusUtils.unregister(this);
   }
}
