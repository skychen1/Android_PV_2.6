package high.rivamed.myapplication.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_FORM_CONFIRM;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_STOCK_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_LOSS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_PROFIT;
import static high.rivamed.myapplication.cont.Constants.STYPE_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
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
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_rl_title)
   RelativeLayout     mRelativeLayout;
   @BindView(R.id.timely_ll_gone)
   LinearLayout       mTimelyLlGone;
   @BindView(R.id.timely_number_left)
   TextView           mTimelyNumberLeft;
   @BindView(R.id.timely_start_btn_right)
   TextView           mTimelyStartBtnRight;
   @BindView(R.id.ly_bing_btn_right)
   TextView           mLyBingBtnRight;
   @BindView(R.id.timely_ll_gone_right)
   LinearLayout       mTimelyLlGoneRight;

   private int           mLayout;
   private View          mHeadView;
   public  String        mData;
   public  TableTypeView mTypeView;
   public  String        mActivityType;
   private String        mMovie;
   List<String> titeleList = null;

   private TCstInventoryVo       mStockDetailsTopBean;
   private List<TCstInventoryVo> mStockDetailsDownList;
   public  List<TCstInventoryVo> mTCstInventoryVos; //入柜扫描到的epc信息
   //   public  List<InBoxDtoBean.TCstInventoryVosBean>     mTCstInventoryVos; //入柜扫描到的epc信息
   public  TCstInventoryDto      mTCstInventoryDto;

   private TCstInventoryDto mDto;
   private String mBindFirstType;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEvent(Event.EventAct event) {
	mActivityType = event.mString;
	LogUtils.i(TAG, "  mActivityType    " + mActivityType);

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
	//	if (s.equals("详情")){
	//	   loadTimelyDetailsDate();
	//	}else if (s.equals("盘盈")){
	//	   loadTimelyProfitDate(event.tCstInventoryDto);
	//	}else if (s.equals("盘亏")){
	//	   loadTimelyLossesDate(event.tCstInventoryDto);
	//	}
   }

   /**
    * 获取盘亏数据
    */
   private void loadTimelyLossesDate() {
	mBaseTabTvTitle.setText("盘亏耗材详情");
	List<TCstInventoryVo> tCstInventoryVos = mDto.gettCstInventoryVos();

	mTimelyNumber.setText(
		Html.fromHtml("盘亏数：<font color='#262626'><big>" + mDto.getReduce() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;

	mTypeView = new TableTypeView(this, this, titeleList, tCstInventoryVos, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY);

   }

   /**
    * 获取盘盈数据
    */
   private void loadTimelyProfitDate() {

	mBaseTabTvTitle.setText("盘盈耗材详情");
//	List<TCstInventoryDto.InventorysBean> inventorys = mDto.getInventorys();
	List<TCstInventoryVo> tCstInventoryVos = mDto.gettCstInventoryVos();
	mTimelyNumber.setText(
		Html.fromHtml("盘盈数：<font color='#262626'><big>" + mDto.getAdd() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, titeleList, tCstInventoryVos, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY);
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

	mTimelyNumber.setText(Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + Actual +
							"</big>&emsp</font>账面库存数：<font color='#262626'><big>" +
							number + "</big></font>"));
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

	LogUtils.i(TAG,"event  "+(event==null));
	if (mTCstInventoryDto != null && mTCstInventoryVos != null){
	   mTCstInventoryDto = event;
	   List<TCstInventoryVo> tCstInventoryVos = event.gettCstInventoryVos();
	   mTCstInventoryVos.clear();
	   mTCstInventoryVos.addAll(tCstInventoryVos);

	   if (my_id==ACT_TYPE_HCCZ_OUT){
		LogUtils.i(TAG,"event  "+ "ACT_TYPE_HCCZ_OUT");
		setOutBoxTitles();
		mTypeView.mOutBoxAllAdapter.notifyDataSetChanged();
	   }else if (my_id == ACT_TYPE_HCCZ_IN){
		LogUtils.i(TAG,"event  "+ "ACT_TYPE_HCCZ_IN");
		setInBoxTitles();
		mTypeView.mInBoxAllAdapter.notifyDataSetChanged();
	   }

	}else {
	   mTCstInventoryDto = event;
	   mTCstInventoryVos = event.gettCstInventoryVos();
	}

   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	getCompanyType();

	initData();
	initlistener();
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
    * 先绑定患者
    */
   private void setAfterBing() {
	mBaseTabTvTitle.setText("耗材领用");
	mTimelyStartBtn.setVisibility(View.GONE);
	mLyBingBtn.setVisibility(View.GONE);
	mTimelyNumber.setVisibility(View.GONE);
	mTimelyNumberLeft.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	if (mTCstInventoryDto.getBindType()==null){
	   mLyBingBtnRight.setVisibility(View.VISIBLE);
	   mTimelyLeft.setEnabled(false);
	   mTimelyRight.setEnabled(false);
	}else {
	   mLyBingBtnRight.setVisibility(View.GONE);
	   mTimelyLeft.setEnabled(true);
	   mTimelyRight.setEnabled(true);
	}
	//	mTimelyStartBtnRight.setVisibility(View.VISIBLE);

	ArrayList<String> strings = new ArrayList<>();
	for (TCstInventoryVo vosBean : mTCstInventoryVos) {
	   strings.add(vosBean.getCstCode());
	}

	ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);

	mTimelyNumberLeft.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
							    "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							    mTCstInventoryVos.size() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_bing_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_BING);
   }

   /**
    * 快速开柜拿出数据
    */
   private void setOutBoxDate() {
	mBaseTabTvTitle.setText("识别耗材");
	setOutBoxTitles();
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mActivityDownBtnFourLl.setVisibility(View.VISIBLE);
	String[] array = mContext.getResources().getStringArray(R.array.six_outbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos, mLinearLayout,
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
	mBaseTabTvTitle.setText("识别耗材");
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	String[] array = mContext.getResources().getStringArray(R.array.six_singbox_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;

	if (mActivityType.equals("all")) {
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
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, mTCstInventoryVos,
						   mLinearLayout, mRecyclerview, mRefreshLayout, ACTIVITY,
						   STYPE_IN, operation);
	   for (TCstInventoryVo b : mTCstInventoryVos) {
		String status = b.getStatus();
		if ((operation == 3 && status.contains("领用")) ||
		    (operation == 2 && status.contains("入库")) ||
		    (operation == 9 && status.contains("移出")) ||
		    (operation == 11 && status.contains("调拨")) ||
		    (operation == 10 && (status.contains("移入")&&!status.equals("禁止移入"))) ||
		    (operation == 7 && status.contains("退回")) ||
		    (operation == 8 && status.contains("退货"))) {
		   LogUtils.i(TAG, "我走了truestatus   " + status + "    operation  " + operation);
		} else {
		   LogUtils.i(TAG, "我走了false");
		   mTimelyLeft.setEnabled(false);
		   mTimelyRight.setEnabled(false);
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

	for (TCstInventoryVo b : mTCstInventoryVos) {
	   String status = b.getStatus();
	   if (status.equals("禁止入库") || status.equals("禁止移入") || status.equals("禁止退回")) {
		DialogUtils.showNoDialog(mContext, "耗材中包含过期耗材，请查看！", 1, "noJump", null);
		mTimelyLeft.setEnabled(false);
		mTimelyRight.setEnabled(false);
		return;
	   }
	}
   }

   /**
    * 耗材详情
    */
   private void loadStockDetails() {
	mBaseTabTvTitle.setText("耗材详情");
	String deviceCode = mStockDetailsTopBean.getDeviceCode();
	String cstCode = mStockDetailsTopBean.getCstCode();
	String[] array = mContext.getResources().getStringArray(R.array.four_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	NetRequest.getInstance().getStockDetailDate(deviceCode, cstCode, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
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

   /**
    * 上拉下拉刷新
    */
   private void initlistener() {
	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		mRefreshLayout.setNoMoreData(false);
		refreshLayout.finishRefresh();
	   }
	});
	mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		refreshLayout.finishLoadMoreWithNoMoreData();
	   }
	});
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
