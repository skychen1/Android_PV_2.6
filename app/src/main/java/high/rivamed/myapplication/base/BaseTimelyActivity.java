package high.rivamed.myapplication.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_FORM_CONFIRM;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_STOCK_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_LOSS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_PROFIT;
import static high.rivamed.myapplication.cont.Constants.STYPE_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
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

   public int my_id;
   public int mSize;
   @BindView(R.id.timely_start_btn)
   TextView     mTimelyStartBtn;
   @BindView(R.id.ly_bing_btn)
   TextView     mLyBingBtn;
   @BindView(R.id.timely_left)
   TextView     mTimelyLeft;
   @BindView(R.id.timely_right)
   TextView     mTimelyRight;
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
   LinearLayout       mDownBtnOneLL;
   @BindView(R.id.activity_btn_one)
   TextView       mDownBtnOne;
   @BindView(R.id.timely_name)
   TextView           mTimelyName;
   @BindView(R.id.timely_number)
   TextView           mTimelyNumber;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;

   private TimelyPublicAdapter mPublicAdapter;
   private int                 mLayout;
   private View                mHeadView;
   public  String              mData;
   private TableTypeView mTypeView;
   public String mActivityType;

   @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
   public void onEvent(Event.EventAct event) {
	mActivityType = event.mString;
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


	getData();
	if (getData() != null && getData().equals("我有过期的")) {
	   DialogUtils.showNoDialog(mContext, "耗材中包含过期耗材，请查看！", 1, "noJump", null);
	   mTimelyLeft.setClickable(true);
	   mTimelyRight.setClickable(false);
	   mTimelyRight.setBackgroundResource(R.drawable.bg_btn_gray_pre);
	}
	List<String> titeleList = null;
	if (my_id == ACT_TYPE_TIMELY_LOSS) {
	   mBaseTabTvTitle.setText("盘亏耗材详情");
	   String str = "3";
	   mTimelyNumber.setText(Html.fromHtml("盘亏数：<font color='#262626'><big>" +
							   str + "</big></font>"));
	   String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, genData(),
						   mLinearLayout, mRecyclerview,
						   mRefreshLayout, ACTIVITY);
	} else if (my_id == ACT_TYPE_TIMELY_PROFIT) {
	   mBaseTabTvTitle.setText("盘盈耗材详情");
	   String str = "3";
	   mTimelyNumber.setText(Html.fromHtml("盘亏数：<font color='#262626'><big>" +
							   str + "</big></font>"));
	   String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView=  new TableTypeView(this, this, titeleList, mSize, genData(), mLinearLayout, mRecyclerview,
				   mRefreshLayout, ACTIVITY);
	} else if (my_id == ACT_TYPE_STOCK_FOUR_DETAILS) {
	   mBaseTabTvTitle.setText("耗材详情");
	   String str = "35";
	   mTimelyNumber.setText(Html.fromHtml("耗材数量：<font color='#262626'><big>" +
							   str + "</big></font>"));
	   mTimelyName.setVisibility(View.VISIBLE);
	   mTimelyName.setText("耗材名称：微创路入系统" + "    型号规格：101");
	   String[] array = mContext.getResources().getStringArray(R.array.four_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView=  new TableTypeView(this, this, titeleList, mSize, genData4(), mLinearLayout, mRecyclerview,
				   mRefreshLayout, ACTIVITY);
	} else if (my_id == ACT_TYPE_HCCZ_IN) {//首页耗材操作单个或者全部柜子的详情界面 放入
	   mBaseTabTvTitle.setText("识别耗材");
	   Log.i("TT","   "+mActivityType);
	   if (mActivityType.equals("all")){
		mTimelyNumber.setText(Html.fromHtml("入库：<font color='#262626'><big>" + 4 +
								"</big>&emsp</font>移入：<font color='#262626'><big>" +
								1 +
								"</big>&emsp</font>退回：<font color='#262626'><big>" +
								4 +
								"</big>&emsp</font>耗材种类：<font color='#262626'><big>" +
								2 +
								"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
								7 + "</big></font>"));

	   }else {
		mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" +
								2 +
								"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
								7 + "</big></font>"));

	   }

	   mTimelyStartBtn.setVisibility(View.VISIBLE);
	   mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources().getStringArray(R.array.six_singbox_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView= new TableTypeView(this, this, titeleList, mSize, genData6(), mLinearLayout, mRecyclerview,
				   mRefreshLayout, ACTIVITY, STYPE_IN);
	} else if (my_id == ACT_TYPE_HCCZ_OUT) {//首页耗材操作单个或者全部柜子的详情界面   拿出
	   mBaseTabTvTitle.setText("识别耗材");
	   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
							   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							   7 + "</big></font>"));
	   mTimelyStartBtn.setVisibility(View.VISIBLE);
	   mActivityDownBtnFourLl.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources().getStringArray(R.array.six_outbox_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView=   new TableTypeView(this, this, titeleList, mSize, genData6(), mLinearLayout, mRecyclerview,
				   mRefreshLayout, ACTIVITY, STYPE_OUT);
	} else if (my_id == ACT_TYPE_HCCZ_BING) {//首页耗材操作单个或者全部柜子的详情界面   拿出
	   mBaseTabTvTitle.setText("耗材领用");
	   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
							   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							   7 + "</big></font>"));
	   mTimelyStartBtn.setVisibility(View.VISIBLE);
	   mLyBingBtn.setVisibility(View.VISIBLE);
	   mActivityDownBtnTwoll.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources().getStringArray(R.array.seven_bing_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView= new TableTypeView(this, this, titeleList, mSize, genData7(), mLinearLayout, mRecyclerview,
				   mRefreshLayout, ACTIVITY, STYPE_BING);
	}else if (my_id == ACT_TYPE_FORM_CONFIRM){
	   mBaseTabTvTitle.setText("识别耗材");
	   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
							   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							   7 + "</big></font>"));
	   mTimelyStartBtn.setVisibility(View.VISIBLE);
	   mDownBtnOneLL.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView= new TableTypeView(this, this, titeleList, mSize, genData6(), mLinearLayout, mRecyclerview,
						  mRefreshLayout, ACTIVITY, STYPE_FORM_CONF);
	}else if (my_id == ACT_TYPE_TIMELY_FOUR_DETAILS) {
	   mBaseTabTvTitle.setText("耗材详情");
	   String str = "35";
	   mTimelyNumber.setText(Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + 12 +
							   "</big>&emsp</font>账面库存数：<font color='#262626'><big>" +
							   7 + "</big></font>"));
	   mTimelyName.setVisibility(View.VISIBLE);
	   mTimelyName.setText("耗材名称：微创路入系统" + "    型号规格：101");
	   String[] array = mContext.getResources().getStringArray(R.array.timely_four_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   mTypeView = new TableTypeView(this, this, titeleList, mSize, genData41(), mLinearLayout,
						   mRecyclerview, mRefreshLayout, ACTIVITY,
						   STYPE_TIMELY_FOUR_DETAILS);
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

   /**
    * 假数据
    *
    * @return
    */
   private List<Movie> genData() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 15; i++) {
	   String one;
	   String two;
	   if (i == 2) {
		one = "微创系统微创系统微创系统微创系统" + i;
		two = "*15151223333dddddssssssssssssss3" + i;
	   } else {
		one = "微创系统" + i;
		two = "*15151223333ddd3" + i;
	   }

	   String three = "RFID01" + i;
	   String four = "2019-10-22";
	   String five = "1" + i;
	   String six = "0" + i;
	   String seven = "1" + i;
	   String eight = "XXX" + i;
	   Movie movie = new Movie(one, two, three, four, five, six, seven, eight);
	   list.add(movie);
	}
	return list;
   }

   private List<Movie> genData4() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 15; i++) {
	   String one = null;
	   String two = null;
	   String three = null;
	   String four = null;
	   if (i == 1) {
		one = "*15170116220035c2dddddsssssssssss3" + i;
		two = "2018-09-22 22:30";
		three = "入库" + i;
		four = "已过期";
	   } else if (i == 2) {
		one = "*15170116220035c2" + i;
		two = "2018-12-22 22:30";
		three = "退回" + i;
		four = "≤100天";
	   } else if (i == 3) {
		one = "*15170116220035c2" + i;
		three = "移入" + i;
		two = "2018-09-22 22:30";
		four = "≤70天";
	   } else if (i == 4) {
		one = "*15170116220035c2" + i;
		three = "退货" + i;
		two = "2018-09-22 22:30";
		four = "≤28天";
	   } else {
		three = "领用" + i;
		one = "*15170116220035sssssss3" + i;
		two = "2018-09-22 22:30";
		four = "2019-10-22";
	   }

	   Movie movie = new Movie(one, two, three, four, null, null, null, null);
	   list.add(movie);
	}
	return list;
   }
   private List<Movie> genData41() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 15; i++) {
	   String one = null;
	   String two = null;
	   String three = null;
	   String four = i+"";
	   if (i == 1) {
		one = "*15170116220035c2dddddsssssssssss3" + i;
		two = "已过期";
		three = 1+"";
	   } else if (i == 2) {
		one = "*15170116220035c2" + i;
		two = "≤100天";
		three = 2+"";
	   } else if (i == 3) {
		one = "*15170116220035c2" + i;
		two = "≤70天";
		three = 3+"";
	   } else if (i == 4) {
		one = "*15170116220035c2" + i;
		two = "≤28天";
		three = 2+"";
	   } else {
		one = "*15170116220035sssssss3" + i;
		two = "2019-10-22";
		three = i+"";
	   }

	   Movie movie = new Movie(one, two, three, four, null, null, null, null);
	   list.add(movie);
	}
	return list;
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

   private List<Movie> genData7() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 25; i++) {
	   String one = null;
	   String two = null;
	   String three = null;
	   String four = null;
	   String five = null;
	   String seven = "1";
	   String six = "张三/dddddddd";
	   if (i == 1) {
		two = "*15170116220035c2dddddsssssssssss3" + i;
		one = "微创路入系统";
		three = "FLR01" + i;
		five = i + "号柜";
		four = "已过期";
	   } else if (i == 2) {
		two = "*15170116220035c2" + i;
		one = "微创路入系统";
		three = "FLR01" + i;
		four = "≤100天";
		five = i + "号柜";
	   } else if (i == 3) {
		one = "微创路入系统";
		two = "*15170116220035c2" + i;
		three = "FLR01" + i;
		four = "≤70天";
		five = i + "号柜";
	   } else if (i == 4) {
		one = "微创路入系统";
		two = "*15170116220035c2" + i;
		three = "FLR01" + i;
		four = "≤28天";
		five = i + "号柜";
	   } else {
		one = "微创路入系统";
		three = "FLR01" + i;
		two = "*15170116220035sssssss3" + i;
		five = i + "号柜";
		four = "2019-10-22";
	   }

	   Movie movie = new Movie(one, two, three, four, five, six, seven, null);
	   list.add(movie);
	}
	return list;
   }

}
