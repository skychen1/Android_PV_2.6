package high.rivamed.myapplication.base;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_HAOCAI;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_RECEIVE;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_FORM_CONFIRM;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_PATIENT_CONN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_STOCK_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TEMPORARY_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_LOSS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_PROFIT;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.STYPE_LOSS_TYPE;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
import static high.rivamed.myapplication.cont.Constants.STYPE_PROFIT_TYPE;
import static high.rivamed.myapplication.cont.Constants.STYPE_TIMELY_FOUR_DETAILS;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/20 14:39
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class BaseTimelyActivity extends BaseSimpleActivity {

   private static final String TAG = "BaseTimelyActivity";
   public int my_id;
   public int mSize;
   @BindView(R.id.timely_start_btn)
   TextView mTimelyStartBtn;
   @BindView(R.id.timely_open_door)
   TextView mTimelyOpenDoor;
   @BindView(R.id.ly_bing_btn)
   TextView mLyBingBtn;
   @BindView(R.id.timely_left)
   public TextView mTimelyLeft;
   @BindView(R.id.timely_right)
   public TextView mTimelyRight;
   @BindView(R.id.activity_down_btnll)
   LinearLayout mActivityDownBtnTwoll;
   @BindView(R.id.btn_four_ly)
   TextView     mBtnFourLy;
   @BindView(R.id.btn_four_yc)
   TextView     mBtnFourYc;
   @BindView(R.id.btn_four_tb)
   TextView     mBtnFourTb;
   @BindView(R.id.btn_four_th)
   TextView     mBtnFourTh;
   @BindView(R.id.activity_down_btn_four_ll)
   LinearLayout mActivityDownBtnFourLl;
   @BindView(R.id.activity_down_btn_one_ll)
   LinearLayout mDownBtnOneLL;
   @BindView(R.id.activity_btn_one)
   TextView     mDownBtnOne;
   @BindView(R.id.timely_name)
   TextView     mTimelyName;
   @BindView(R.id.timely_number)
   public TextView mTimelyNumber;
   @BindView(R.id.timely_ll)
   LinearLayout mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView mRecyclerview;
   @BindView(R.id.refreshLayout)
   public SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_rl_title)
   RelativeLayout mRelativeLayout;
   @BindView(R.id.timely_ll_gone)
   LinearLayout   mTimelyLlGone;
   @BindView(R.id.timely_number_left)
   TextView       mTimelyNumberLeft;
   @BindView(R.id.timely_start_btn_right)
   TextView       mTimelyStartBtnRight;
   @BindView(R.id.timely_open_door_right)
   TextView       mTimelyOpenDoorRight;
   @BindView(R.id.ly_bing_btn_right)
   TextView       mLyBingBtnRight;
   @BindView(R.id.timely_ll_gone_right)
   LinearLayout   mTimelyLlGoneRight;
   @BindView(R.id.search_et)
   EditText       mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView      mSearchIvDelete;
   @BindView(R.id.stock_search)
   FrameLayout    mStockSearch;
   @BindView(R.id.ly_creat_temporary_btn)
   TextView       mLyCreatTemporaryBtn;
   @BindView(R.id.dialog_left)
   TextView       mDialogLeft;
   @BindView(R.id.dialog_right)
   TextView       mDialogRight;
   @BindView(R.id.activity_down_btn_seven_ll)
   LinearLayout   mActivityDownBtnSevenLl;
   @BindView(R.id.timely_rl)
   LinearLayout   mTimelyRl;
   @BindView(R.id.header)
   MaterialHeader mHeader;
   @BindView(R.id.public_ll)
   LinearLayout   mPublicLl;
   @BindView(R.id.tv_patient_conn)
   TextView       mTvPatientConn;
   @BindView(R.id.activity_down_patient_conn)
   LinearLayout   mActivityDownPatientConn;
   @BindView(R.id.all_out_text)
   public TextView       mAllOutText;
   private int           mLayout;
   private View          mHeadView;
   public  String        mData;
   public  TableTypeView mTypeView;
   public  String        mActivityType;
   private String        mMovie;
   List<String> titeleList = null;

   private TCstInventoryVo       mStockDetailsTopBean;
   private List<TCstInventoryVo> mStockDetailsDownList;
   public List<TCstInventoryVo> mTCstInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息

   public TCstInventoryDto mTCstInventoryDto;
   public List<BingFindSchedulesBean.PatientInfosBean> patientInfos = new ArrayList<>();

   public  TCstInventoryDto mDto;
   private String           mBindFirstType;
   private int              mOperation;
   private int              mDtoOperation;
   public  CountDownTimer   mStarts;
   public boolean mOnBtnGone =false;

   /**
    * 隐藏按钮
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onBtnGoneEvent(Event.EventButGone event) {
	mOnBtnGone = event.b;
	if (mOnBtnGone){
	   mTimelyStartBtnRight.setVisibility(View.GONE);
	   mTimelyOpenDoorRight.setVisibility(View.GONE);
	}
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

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEvent(Event.EventAct event) {
	mActivityType = event.mString;
	LogUtils.i(TAG, "  mActivityType    " + mActivityType);

   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	if (mTCstInventoryDto != null) {
	   mOperation = mTCstInventoryDto.getOperation();
	}
	LogUtils.i(TAG, "fffffafafafafafa");
	if (event.type) {
	   if (event.bing) {
		for (TCstInventoryVo b : mTCstInventoryVos) {
		   ArrayList<String> strings = new ArrayList<>();
		   strings.add(b.getCstCode());
		   if (UIUtils.getConfigType(mContext, CONFIG_009) &&
			 ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			  (b.getPatientName() == null || b.getPatientName().equals("")))) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			LogUtils.i(TAG, "OutBoxBingActivity   少时诵诗书 cancel");
			if (mStarts != null) {
			   mStarts.cancel();
			   mTimelyRight.setText("确认并退出登录");
			}
			return;
		   }
		   //		   String status = b.getStatus();
		   //		   if (status.equals("禁止操作") || status.equals("禁止入库") || status.equals("禁止移入") ||
		   //			 status.equals("禁止退回") ||
		   //			 (mOperation == 3 && !status.contains("领用") && !status.equals("移除")) ||
		   //			 (mOperation == 2 && !status.contains("入库") && !status.equals("移除")) ||
		   //			 (mOperation == 9 && !status.contains("移出") && !status.equals("移除")) ||
		   //			 (mOperation == 11 && !status.contains("调拨") && !status.equals("移除")) ||
		   //			 (mOperation == 10 && !status.contains("移入") && !status.equals("移除")) ||
		   //			 (mOperation == 7 && !status.contains("退回") && !status.equals("移除")) ||
		   //			 (mOperation == 8 && !status.contains("退货") && !status.equals("移除"))) {
		   if (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			LogUtils.i(TAG, "OutBoxBingActivity   cancel");
			if (mStarts != null) {
			   mStarts.cancel();
			   mTimelyRight.setText("确认并退出登录");
			}
			return;
		   } else {
			LogUtils.i(TAG, "OutBoxBingActivity   start");
			mTimelyLeft.setEnabled(true);
			mTimelyRight.setEnabled(true);
			if (mStarts != null) {
			   mStarts.cancel();
			   mStarts.start();
			}
		   }
		}
	   } else {
		for (TCstInventoryVo b : mTCstInventoryVos) {
		   //		   String status = b.getStatus();
		   //		   if (status.equals("禁止操作") || status.equals("禁止入库") || status.equals("禁止移入") ||
		   //			 status.equals("禁止退回") ||
		   //			 (mOperation == 3 && !status.contains("领用") && !status.equals("移除")) ||
		   //			 (mOperation == 2 && !status.contains("入库") && !status.equals("移除")) ||
		   //			 (mOperation == 9 && !status.contains("移出") && !status.equals("移除")) ||
		   //			 (mOperation == 11 && !status.contains("调拨") && !status.equals("移除")) ||
		   //			 (mOperation == 10 && !status.contains("移入") && !status.equals("移除")) ||
		   //			 (mOperation == 7 && !status.contains("退回") && !status.equals("移除")) ||
		   //			 (mOperation == 8 && !status.contains("退货") && !status.equals("移除"))) {
		   if (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) {
			mTimelyLeft.setEnabled(false);
			mTimelyRight.setEnabled(false);
			LogUtils.i(TAG, "InOutBoxTwoActivity   cancel");
			if (mStarts != null) {
			   mStarts.cancel();
			   mTimelyRight.setText("确认并退出登录");
			}
			return;
		   } else {
			LogUtils.i(TAG, "InOutBoxTwoActivity   start");
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
    * 盘点详情、盘亏、盘盈
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onTimelyEvent(Event.timelyDate event) {
	String s = event.type;
	mDto = event.tCstInventoryDto;

   }

   /**
    * 获取盘亏数据
    */
   private void loadTimelyLossesDate() {
	//	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);//盘亏界面修改
	//	mTimelyLeft.setText("盘亏原因");//盘亏界面修改
	//	mTimelyRight.setText("确认提交");//盘亏界面修改
	mBaseTabTvTitle.setText("盘亏耗材详情");
	List<TCstInventoryVo> tCstInventoryVos = mDto.gettCstInventoryVos();

	mTimelyNumber.setText(
		Html.fromHtml("盘亏数：<font color='#262626'><big>" + mDto.getReduce() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;

	mTypeView = new TableTypeView(this, this, titeleList, mSize, tCstInventoryVos, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_LOSS_TYPE);

   }

   /**
    * 获取盘盈数据
    */
   private void loadTimelyProfitDate() {

	mBaseTabTvTitle.setText("盘盈耗材详情");
	List<TCstInventoryVo> tCstInventoryVos = mDto.gettCstInventoryVos();
	mTimelyNumber.setText(
		Html.fromHtml("盘盈数：<font color='#262626'><big>" + mDto.getAdd() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, titeleList, mSize, tCstInventoryVos, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_PROFIT_TYPE);
   }

   /**
    * 获取耗材盘点详情
    */
   private void loadTimelyDetailsDate() {
	mBaseTabTvTitle.setText("耗材详情");
	List<TCstInventoryVo> tCstInventoryVos = mDto.gettCstInventoryVos();
	int number = 0;
	int Actual = 0;
	for (TCstInventoryVo TCstInventoryVo : tCstInventoryVos) {
	   number += TCstInventoryVo.getCountStock();
	   Actual += TCstInventoryVo.getCountActual();
	   Log.i(TAG, " TCstInventoryVo.getCountStock()   " + TCstInventoryVo.getCountStock());
	   Log.i(TAG, " TCstInventoryVo.getCountActual()   " + TCstInventoryVo.getCountActual());
	}
	if (Actual == number) {
	   mTimelyNumber.setText(Html.fromHtml("实际扫描数：<font color='#262626'><big>" + Actual +
							   "</big>&emsp</font>账面库存数：<font color='#262626'><big>" +
							   number + "</big></font>"));
	} else {
	   mTimelyNumber.setText(Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + Actual +
							   "</big>&emsp</font>账面库存数：<font color='#262626'><big>" +
							   number + "</big></font>"));
	}

	mTimelyName.setVisibility(View.VISIBLE);
	mTimelyName.setText("耗材名称：" + mDto.getEpcName() + "    型号规格：" + mDto.getCstSpec());
	String[] array = mContext.getResources().getStringArray(R.array.timely_four_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, titeleList, mSize, tCstInventoryVos, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY,
						STYPE_TIMELY_FOUR_DETAILS);
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onMidTypeEvent(TCstInventoryVo event) {
	mStockDetailsTopBean = event;

   }

   /**
    * 接收入库的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onInBoxEvent(TCstInventoryDto event) {

	LogUtils.i(TAG, "event  " + (event == null));
	if (mTCstInventoryDto != null && mTCstInventoryVos != null) {
	   mTCstInventoryDto = event;
	   List<TCstInventoryVo> tCstInventoryVos = event.gettCstInventoryVos();
	   List<TCstInventory> tCstInventories = event.gettCstInventorys();
	   mTCstInventoryVos.clear();
	   mTCstInventoryVos.addAll(tCstInventoryVos);//选择开柜
	   if (my_id == ACT_TYPE_HCCZ_OUT) {
		LogUtils.i(TAG, "event  " + "ACT_TYPE_HCCZ_OUT");
		setOutBoxTitles();
		mTypeView.mOutBoxAllAdapter.notifyDataSetChanged();
	   } else if (my_id == ACT_TYPE_HCCZ_IN) {
		if (mActivityType.equals("all")) {
		   setInBoxTitles();
		} else {
		   setInBoxDate();
		}
		mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	   } else if (my_id == ACT_TYPE_HCCZ_BING) {
		setAfterBing();
	   }

	} else {
	   mTCstInventoryDto = event;
	   mTCstInventoryVos = event.gettCstInventoryVos();//选择开柜
	}
	mDtoOperation = mTCstInventoryDto.getOperation();
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	mStarts = new TimeCount(COUNTDOWN_TIME, 1000, mTimelyRight);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvName.setText(SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
	if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
	    SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nan)
		   .error(R.mipmap.hccz_mrtx_nan)
		   .into(mBaseTabIconRight);
	} else {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nv)
		   .error(R.mipmap.hccz_mrtx_nv)
		   .into(mBaseTabIconRight);
	}
	getCompanyType();

	initData();
   }

   public int getCompanyType() {
	return my_id;
   }

   public String getData() {
	return mData;
   }

   /**
    * 数据加载
    */
   private void initData() {
	//
	//	getData();
	//	if (getData() != null && getData().equals("我有过期的")) {
	//	   DialogUtils.showNoDialog(mContext, "耗材中包含过期耗材，请查看！", 1, "noJump", null);
	//	   mTimelyLeft.setClickable(true);
	//	   mTimelyRight.setClickable(false);
	//	   mTimelyRight.setBackgroundResource(R.drawable.bg_btn_gray_pre);
	//	}

	if (my_id == ACT_TYPE_TIMELY_LOSS) {
	   loadTimelyLossesDate();

	} else if (my_id == ACT_TYPE_TIMELY_PROFIT) {

	   loadTimelyProfitDate();
	} else if (my_id == ACT_TYPE_STOCK_FOUR_DETAILS) {

	   loadStockDetails();
	} else if (my_id == ACT_TYPE_HCCZ_IN) {//首页耗材操作单个或者全部柜子的详情界面 放入
	   setInBoxDate();
	} else if (my_id == ACT_TYPE_HCCZ_OUT) {//首页耗材操作单个或者全部柜子的详情界面   拿出
	   setOutBoxDate();

	} else if (my_id == ACT_TYPE_HCCZ_BING) {//首页耗材操作单个或者全部柜子的详情界面   拿出
	   setAfterBing();

	} else if (my_id == ACT_TYPE_FORM_CONFIRM) {
	   mBaseTabTvTitle.setText("识别耗材");
	   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
							   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							   7 + "</big></font>"));
	   mTimelyStartBtn.setVisibility(View.VISIBLE);
	   mDownBtnOneLL.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, genData6(), mLinearLayout,
						   mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_FORM_CONF);
	} else if (my_id == ACT_TYPE_CONFIRM_RECEIVE) {//确认领用耗材
	   mBaseTabTvTitle.setText("识别耗材");
	   ArrayList<String> strings = new ArrayList<>();
	   for (TCstInventoryVo vosBean : mTCstInventoryVos) {
		strings.add(vosBean.getCstCode());
	   }
	   ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);

	   mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
								 "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
								 mTCstInventoryVos.size() + "</big></font>"));

	   mTimelyStartBtn.setVisibility(View.VISIBLE);
	   mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources()
		   .getStringArray(R.array.six_confirm_receive_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   ACT_TYPE_CONFIRM_HAOCAI);
	} else if (my_id == ACT_TYPE_TIMELY_FOUR_DETAILS) {
	   loadTimelyDetailsDate();
	} else if (my_id == ACT_TYPE_MEAL_BING) {//套餐绑定患者的耗材识别
	   mBaseTabTvTitle.setText("识别耗材");
	   mTimelyStartBtn.setVisibility(View.GONE);
	   mLyBingBtn.setVisibility(View.GONE);
	   mTimelyNumber.setVisibility(View.GONE);
	   mTimelyNumberLeft.setVisibility(View.VISIBLE);
	   mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	   mLyBingBtnRight.setVisibility(View.VISIBLE);
	   mTimelyStartBtnRight.setVisibility(View.VISIBLE);
	   mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
								 "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
								 7 + "</big></font>"));
	   String[] array = mContext.getResources().getStringArray(R.array.eight_meal_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;

	   mTypeView = new TableTypeView(this, this, titeleList, mSize, genData8(), mLinearLayout,
						   mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_MEAL_BING);
	} else if (my_id == ACT_TYPE_TEMPORARY_BING) {//患者列表
	   setTemporaryBing();
	} else if (my_id == ACT_TYPE_PATIENT_CONN) {//选择临时患者,患者关联
	   selectTempPatient();
	}
	//单独做的acitvity的table
	//TODO:需要修改titleList和真实数据
	/**
	 * @titleList: title的名字List<String>;
	 * @msize: title的个数;
	 * @genData(): 添加的数据List<Bean>;
	 * @mLinearLayout: Title的存放layout;
	 * @mRecyclerview: mRecyclerview
	 * @mRefreshLayout: 刷新的RefreshLayout控件;
	 * @type: 1表示activity , 2表示 fragment;
	 */
	//	new TableTypeView(this, this, titeleList, mSize, genData(), mLinearLayout, mRecyclerview,
	//				mRefreshLayout, ACTIVITY);
   }

   /**
    * 绑定患者
    */
   private void setAfterBing() {
	mBaseTabTvTitle.setText("耗材领用");
	mTimelyStartBtn.setVisibility(View.GONE);
	mLyBingBtn.setVisibility(View.GONE);
	mTimelyNumber.setVisibility(View.GONE);
	if (mOnBtnGone){
	   mBaseTabBack.setVisibility(View.VISIBLE);
	}else {
	   mBaseTabBack.setVisibility(View.GONE);
	}
	mTimelyNumberLeft.setVisibility(View.VISIBLE);
	mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	if (UIUtils.getConfigType(mContext, CONFIG_009)) {//后绑定
	   mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	   mTimelyLeft.setEnabled(false);
	   mTimelyRight.setEnabled(false);
	   if (mStarts != null) {
		mStarts.cancel();
		mTimelyRight.setText("确认并退出登录");
	   }
	   mLyBingBtnRight.setVisibility(View.VISIBLE);
	} else {//先绑定
	   mTimelyLlGoneRight.setVisibility(View.VISIBLE);
	   mLyBingBtnRight.setVisibility(View.GONE);
	   mTimelyLeft.setEnabled(true);
	   mTimelyRight.setEnabled(true);
	   for (TCstInventoryVo vosBean : mTCstInventoryVos) {
		//		if (!vosBean.getStatus().equals("禁止操作")) {
		if (vosBean.getIsErrorOperation() == 0 ||
		    (vosBean.getIsErrorOperation() == 1 && vosBean.getDeleteCount() != 0)) {
		   mTimelyLeft.setEnabled(true);
		   mTimelyRight.setEnabled(true);
		} else {
		   LogUtils.i(TAG, "我走了falsesss");
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   break;
		}
	   }
	}

	ArrayList<String> strings = new ArrayList<>();
	for (TCstInventoryVo vosBean : mTCstInventoryVos) {
	   strings.add(vosBean.getCstCode());
	   if (UIUtils.getConfigType(mContext, CONFIG_009) &&
		 ((vosBean.getPatientId() == null || vosBean.getPatientId().equals("")) ||
		  (vosBean.getPatientName() == null || vosBean.getPatientName().equals("")))) {
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
		if (mStarts != null) {
		   mStarts.cancel();
		   mTimelyRight.setText("确认并退出登录");
		}
		break;
	   }
	}

	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	LogUtils.i(TAG, "list.size()  " + list.size() + "      " + mTCstInventoryVos.size());
	mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							    "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							    mTCstInventoryVos.size() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_title_bing_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	int operation = mTCstInventoryDto.getOperation();
	if (mTypeView == null) {
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   ACT_TYPE_CONFIRM_HAOCAI, operation);
	}

   }

   /**
    * 患者列表
    */
   private void setTemporaryBing() {
	mBaseTabTvTitle.setText("患者列表");
	mTimelyStartBtn.setVisibility(View.GONE);
	mLyBingBtn.setVisibility(View.GONE);
	mTimelyNumber.setVisibility(View.GONE);
	mStockSearch.setVisibility(View.VISIBLE);
	mLyCreatTemporaryBtn.setVisibility(View.VISIBLE);
	mActivityDownBtnSevenLl.setVisibility(View.VISIBLE);
	mTimelyLeft.setEnabled(false);
	mTimelyRight.setEnabled(false);
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	ArrayList<String> strings = new ArrayList<>();
	if (null != mTCstInventoryVos) {
	   for (TCstInventoryVo vosBean : mTCstInventoryVos) {
		strings.add(vosBean.getCstCode());
	   }
	}

	String[] array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
	titeleList = Arrays.asList(array);
	mSize = titeleList.size();

	mTypeView = new TableTypeView(mContext, this, patientInfos, titeleList, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_DIALOG);

   }

   /**
    * 选择临时患者
    */
   private void selectTempPatient() {
	mBaseTabTvTitle.setText("选择临时患者");
	mTimelyStartBtn.setVisibility(View.GONE);
	mLyBingBtn.setVisibility(View.GONE);
	mTimelyNumber.setVisibility(View.GONE);
	mStockSearch.setVisibility(View.VISIBLE);
	mActivityDownPatientConn.setVisibility(View.VISIBLE);
	mTimelyLeft.setEnabled(false);
	mTimelyRight.setEnabled(false);
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}
	ArrayList<String> strings = new ArrayList<>();
	if (null != mTCstInventoryVos) {
	   for (TCstInventoryVo vosBean : mTCstInventoryVos) {
		strings.add(vosBean.getCstCode());
	   }
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	//
	//        mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
	//                "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
	//                mTCstInventoryVos.size() + "</big></font>"));
	List<String> titeleList = new ArrayList<String>();
	titeleList.add(0, "选择");
	titeleList.add(1, "患者姓名");
	titeleList.add(2, "患者ID");
	titeleList.add(3, "手术时间");
	titeleList.add(4, "医生");
	titeleList.add(5, "手术间");
	titeleList.add(6, "是否为临时患者");

	//	   String[] array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
	//	   titeleList = Arrays.asList(array);
	mSize = titeleList.size();

	mTypeView = new TableTypeView(mContext, this, patientInfos, titeleList, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_DIALOG);

	//        List<BingFindSchedulesBean.PatientInfosBean> patientInfos = new ArrayList<>();
	//        mTypeView = new TableTypeView(mContext, this, patientInfos, titeleList, mSize,
	//                mLinearLayout, mRecyclerview,
	//                mRefreshLayout, ACTIVITY, STYPE_DIALOG);

   }

   /**
    * 快速开柜拿出数据
    */
   private void setOutBoxDate() {
	mBaseTabTvTitle.setText("识别耗材");
	setOutBoxTitles();
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mActivityDownBtnFourLl.setVisibility(View.VISIBLE);
	mBtnFourTb.setVisibility(View.GONE);//隐藏调拨
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	String[] array = mContext.getResources().getStringArray(R.array.six_outbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos,mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_OUT);
   }

   /**
    * 取出耗材 重新扫描后增减的数据  title显示
    */
   private void setOutBoxTitles() {
	ArrayList<String> strings = new ArrayList<>();
	for (TCstInventoryVo vosBean : mTCstInventoryVos) {
	   strings.add(vosBean.getCstCode());
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							mTCstInventoryVos.size() + "</big></font>"));
   }

   /**
    * 快速开柜入柜后赋值界面
    */
   private void setInBoxDate() {
	if (mDtoOperation == 8) {
	   mBaseTabTvTitle.setText("耗材退货");
	} else if (mDtoOperation == 3) {
	   mBaseTabTvTitle.setText("耗材领用");
	} else if (mDtoOperation == 2) {
	   mBaseTabTvTitle.setText("耗材入库");
	} else if (mDtoOperation == 9) {
	   mBaseTabTvTitle.setText("耗材移出");
	} else if (mDtoOperation == 11) {
	   mBaseTabTvTitle.setText("耗材调拨");
	} else if (mDtoOperation == 10) {
	   mBaseTabTvTitle.setText("耗材移入");
	} else if (mDtoOperation == 7) {
	   mBaseTabTvTitle.setText("耗材退回");
	} else {
	   mBaseTabTvTitle.setText("耗材识别");
	}
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	mTimelyOpenDoor.setVisibility(View.VISIBLE);
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	if (mStarts != null) {
	   mStarts.cancel();
	   mTimelyRight.setText("确认并退出登录");
	}

	String[] array = mContext.getResources().getStringArray(R.array.six_singbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;

	if (mActivityType != null && mActivityType.equals("all")) {
	   setInBoxTitles();
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   STYPE_IN);
	} else {
	   ArrayList<String> strings = new ArrayList<>();
	   for (TCstInventoryVo vosBean : mTCstInventoryVos) {
		strings.add(vosBean.getCstCode());
	   }
	   ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							   mTCstInventoryVos.size() + "</big></font>"));

	   int operation = mTCstInventoryDto.getOperation();
	   LogUtils.i(TAG, "operation  " + operation);
	   if (mTypeView == null) {
		mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos,
							mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
							STYPE_IN, operation);
	   }

	   for (TCstInventoryVo b : mTCstInventoryVos) {
		String status = b.getStatus();
		//		if ((operation == 3 && status.contains("领用") && b.getStopFlag() != 0) ||
		//		    (operation == 2 && status.contains("入库") && b.getStopFlag() != 0) ||
		//		    (operation == 9 && status.contains("移出") && b.getStopFlag() != 0) ||
		//		    (operation == 11 && status.contains("调拨") && b.getStopFlag() != 0) ||
		//		    (operation == 10 &&
		//		     (status.contains("移入") && !status.equals("禁止移入") && b.getStopFlag() != 0)) ||
		//		    (operation == 7 && status.contains("退回") && b.getStopFlag() != 0) ||
		//		    (operation == 8 && status.contains("退货"))) {
		if (b.getIsErrorOperation() == 0 ||
		    (b.getIsErrorOperation() == 1 && b.getDeleteCount() != 0)) {
		   LogUtils.i(TAG, "我走了truestatus   " + status + "    operation  " + operation);
		   if (mStarts != null) {
			mStarts.cancel();
			mStarts.start();
		   }
		} else {
		   LogUtils.i(TAG, "我走了false");
		   LogUtils.i(TAG, "InOutBoxTwoActivity.mStart   " + (mStarts == null));
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
		   if (mStarts != null) {
			mStarts.cancel();
			mTimelyRight.setText("确认并退出登录");
		   }
		   return;
		}
	   }

	}
   }

   /**
    * 给入柜的顶部设置数据和调整底部按钮选择状态
    */
   private void setInBoxTitles() {
	ArrayList<String> strings = new ArrayList<>();
	for (TCstInventoryVo vosBean : mTCstInventoryVos) {
	   strings.add(vosBean.getCstCode());
	}
	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
	mTimelyNumber.setText(Html.fromHtml(
		"入库：<font color='#262626'><big>" + mTCstInventoryDto.getCountTwoin() +
		"</big>&emsp</font>移入：<font color='#262626'><big>" +
		mTCstInventoryDto.getCountMoveIn() +
		"</big>&emsp</font>退回：<font color='#262626'><big>" + mTCstInventoryDto.getCountBack() +
		"</big>&emsp</font>耗材种类：<font color='#262626'><big>" + list.size() +
		"</big>&emsp</font>耗材数量：<font color='#262626'><big>" + mTCstInventoryVos.size() +
		"</big></font>"));

//	for (TCstInventoryVo b : mTCstInventoryVos) {
//	   String status = b.getStatus();
//	   if (status.equals("禁止入库") || status.equals("禁止移入") || status.equals("禁止退回")) {
//		DialogUtils.showNoDialog(mContext, "耗材中包含过期耗材，请查看！", 1, "noJump", null);
//		mTimelyLeft.setEnabled(false);
//		mTimelyRight.setEnabled(false);
//		return;
//	   }
//	}
   }

   /**
    * 耗材详情
    */
   private void loadStockDetails() {
	mBaseTabTvTitle.setText("耗材详情");
	String deviceCode = mStockDetailsTopBean.getDeviceCode();
	String cstId = mStockDetailsTopBean.getCstCode();
	String[] array = mContext.getResources().getStringArray(R.array.four_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	NetRequest.getInstance().getStockDetailDate(deviceCode, cstId, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		LogUtils.i(TAG, "result  " + result);
		TCstInventoryDto tCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
		mStockDetailsDownList = tCstInventoryDto.gettCstInventoryVos();
		mTimelyNumber.setText(Html.fromHtml(
			"耗材数量：<font color='#262626'><big>" + mStockDetailsTopBean.getCount() +
			"</big></font>"));
		mTimelyName.setVisibility(View.VISIBLE);
		mTimelyName.setText("耗材名称：" + mStockDetailsTopBean.getCstName() + "    型号规格：" +
					  mStockDetailsTopBean.getCstSpec());
		mTypeView = new TableTypeView(mContext, mContext, titeleList, mSize,
							mStockDetailsDownList, mLinearLayout, mRecyclerview,
							mRefreshLayout, ACTIVITY);
	   }
	});

   }

   /* 定义一个倒计时的内部类 */
   private class TimeCount extends CountDownTimer {

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
	   if (millisUntilFinished / 1000 <= 135) {
		textView.setText("确认并退出登录 " + "( " + millisUntilFinished / 1000 + " s )");
	   } else {
		textView.setText("确认并退出登录");
	   }
	}
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	   return true;
	}
	return super.onKeyDown(keyCode, event);

   }

   private List<Movie> genData6() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 25; i++) {
	   String one = null;
	   String two = null;
	   String three = null;
	   String four = null;
	   String five = null;
	   String six = null;
	   String seven = null;
	   if (i == 1) {
		two = "*15170116220035c2dddddsssssssssss3" + i;
		one = "微创路入系统";
		three = "FLR01" + i;
		five = i + "号柜";
		four = "已过期";
		six = "禁止操作";
		seven = "0";
	   } else if (i == 2) {
		two = "*15170116220035c2" + i;
		one = "微创路入系统";
		three = "FLR01" + i;
		four = "≤100天";
		five = i + "号柜";
		six = "禁止操作";
		seven = "1";
	   } else if (i == 3) {
		one = "微创路入系统";
		two = "*15170116220035c2" + i;
		three = "FLR01" + i;
		four = "≤70天";
		five = i + "号柜";
		six = "入库";
		seven = "0";
	   } else if (i == 4) {
		one = "微创路入系统";
		two = "*15170116220035c2" + i;
		three = "FLR01" + i;
		four = "≤28天";
		five = i + "号柜";
		six = "退回";
		seven = "1";
	   } else {
		one = "微创路入系统";
		three = "FLR01" + i;
		two = "*15170116220035sssssss3" + i;
		five = i + "号柜";
		four = "2019-10-22";
		six = "移入";
		seven = "0";
	   }

	   Movie movie = new Movie(one, two, three, four, five, six, seven, null);
	   list.add(movie);
	}
	return list;
   }

   private List<Movie> genData8() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 25; i++) {
	   String one = null;
	   String two = null;
	   String three = null;
	   String four = null;
	   String five = null;
	   String six = "";
	   String seven = "";
	   if (i == 1) {
		two = "*15170116220035c2dddddsssssssssss3" + i;
		one = "微创路入系统";
		three = "FLR01" + i;
		five = i + "号柜";
		four = "已过期";
		seven = "1";
	   } else if (i == 2) {
		two = "*15170116220035c2" + i;
		one = "微创路入系统";
		three = "FLR01" + i;
		four = "≤100天";
		five = i + "号柜";
		seven = "0";
	   } else if (i == 3) {
		one = "微创路入系统";
		two = "*15170116220035c2" + i;
		three = "FLR01" + i;
		four = "≤70天";
		five = i + "号柜";
		seven = "1";
	   } else if (i == 4) {
		one = "微创路入系统";
		two = "*15170116220035c2" + i;
		three = "FLR01" + i;
		four = "≤28天";
		five = i + "号柜";
		seven = "1";
	   } else {
		one = "微创路入系统";
		three = "FLR01" + i;
		two = "*15170116220035sssssss3" + i;
		five = i + "号柜";
		four = "2019-10-22";
		seven = "0";
	   }

	   Movie movie = new Movie(one, two, three, four, five, six, seven, null);
	   list.add(movie);
	}
	return list;
   }
}
