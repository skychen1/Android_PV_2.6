package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.StockMidTypeActivity;
import high.rivamed.myapplication.activity.TimelyDetailsActivity;
import high.rivamed.myapplication.activity.TimelyLossActivity;
import high.rivamed.myapplication.activity.TimelyProfitActivity;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_LEFT;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_MIDDLE;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_RIGHT;
import static high.rivamed.myapplication.cont.Constants.TYPE_RUNWATE;
import static high.rivamed.myapplication.cont.Constants.TYPE_TIMELY;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 20:04
 * 描述:        公用fragment
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PublicTimelyFrag extends SimpleFragment {

   private static final String TYPE_SIZE = "TYPE_SIZE";
   private static final String TYPE_PAGE = null;
   private static final String TYPE_LIST = "list";
   private static final int    FIVE      = 5;
   private static final int    SIX       = 6;
   private static final int    SEVEN     = 7;
   private static final int    EIGHT     = 8;
   @BindView(R.id.timely_start_btn)
   TextView           mTimelyStartBtn;
   @BindView(R.id.timely_book)
   TextView           mTimelyBook;
   @BindView(R.id.timely_reality)
   TextView           mTimelyReality;
   @BindView(R.id.timely_profit)
   TextView           mTimelyProfit;
   @BindView(R.id.timely_loss)
   TextView           mTimelyLoss;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.timely_title)
   RelativeLayout     mRelativeLayout;
   @BindView(R.id.stock_search)
   FrameLayout        mStockSearch;
   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView          mSearchIvDelete;
   @BindView(R.id.right_top)
   LinearLayout       mRightTop;
   @BindView(R.id.stock_right_btn)
   LinearLayout       mStockRightLL;
   @BindView(R.id.public_rl)
   RelativeLayout     mPublicRl;
   @BindView(R.id.public_ll)
   LinearLayout     mPublicLl;
   private int                 mParam;
   private TimelyPublicAdapter mPublicAdapter;
   private int                 mSize; //假数据 举例6个横向格子
   private View                mHeadView;
   private int                 mLayout;
   private int                 mType_size;
   private String              mType_page;

   /**
    * @param param 表格的列数  用来判断数据长度  表格的列表
    * @param type  表格类别
    * @return
    */
   public static PublicTimelyFrag newInstance(int param, String type) {
	Bundle args = new Bundle();
	PublicTimelyFrag fragment = new PublicTimelyFrag();
	args.putInt(TYPE_SIZE, param);
	args.putString(TYPE_PAGE, type);
	fragment.setArguments(args);
	return fragment;
   }


   @Override
   public int getLayoutId() {
	return R.layout.public_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	Bundle arguments = getArguments();
	mType_size = arguments.getInt(TYPE_SIZE);//假数据   用来判断数据长度  表格的列表
	mType_page = arguments.getString(TYPE_PAGE);
	initData();
	//	final List<Movie> movies = new Gson().fromJson(JSON_MOVIES,
	//								     new TypeToken<ArrayList<Movie>>() {}.getType());
	//	mPublicAdapter.replaceData(movies);
	initlistener();
   }

   /**
    * 数据加载
    */
   private void initData() {
	//表格的title区分和一部分数据
	List<String> titeleList = null;

	if (mType_size == FIVE &&
	    (mType_page.equals(STYPE_STOCK_MIDDLE) || mType_page.equals(STYPE_STOCK_LEFT))) {
	   Log.i("BaseQuickAdapter", "TYPE_STOCKxxx");
	   Log.i("BaseQuickAdapter", "xxxxx   " + FIVE);
	   mSearchEt.setHint("请输入耗材名称、型号规格查询");
	   mRelativeLayout.setVisibility(View.GONE);
	   mStockSearch.setVisibility(View.VISIBLE);
	   String[] array = mContext.getResources().getStringArray(R.array.five_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;
	   if (mType_page.equals(STYPE_STOCK_LEFT)) {
		mTimelyReality.setText("耗材种类：" + FIVE);
		mTimelyBook.setText("耗材数量：" + FIVE);
	   } else {
		mTimelyReality.setText("耗材种类：" + 6);
		mTimelyBook.setText("耗材数量：" + 0);
	   }

	} else if (mType_size == SIX) {
	   if ( mType_page.equals(TYPE_TIMELY)){
		String str = "3";
		mTimelyLoss.setText(Html.fromHtml("盘亏：" + "<font color='#F5222D'>" + str + "</font>"));
		String str1 = "99";
		mTimelyReality.setText(
			Html.fromHtml("实际扫描数：" + "<font color='#F5222D'>" + str1 + "</font>"));
		mTimelyBook.setText("账面库存数：" + SIX);
		String[] array = mContext.getResources().getStringArray(R.array.six_real_time_arrays);
		titeleList = Arrays.asList(array);
		mSize = array.length;
	   }
	} else if (mType_size == SEVEN ) {
//	   if ( mType_page.equals(TYPE_TIMELY)){
//		Log.i("BaseQuickAdapter", "mType_size   " + SEVEN);
//		String str = "3";
//		mTimelyLoss.setText(Html.fromHtml("盘亏：" + "<font color='#F5222D'>" + str + "</font>"));
//		String str1 = "99";
//		mTimelyReality.setText(
//			Html.fromHtml("实际扫描数：" + "<font color='#F5222D'>" + str1 + "</font>"));
//		mTimelyBook.setText("账面库存数：" + FIVE);
//		String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
//		titeleList = Arrays.asList(array);
//		mSize = array.length;
//	   }else
	      if (mType_page.equals(STYPE_FORM)){

		mPublicRl.setVisibility(View.GONE);
		String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
		titeleList = Arrays.asList(array);
		mSize = array.length;
	   }
	} else if (mType_size == EIGHT &&
		     (mType_page.equals(TYPE_RUNWATE) || mType_page.equals(STYPE_STOCK_RIGHT))) {
	   Log.i("BaseQuickAdapter", "mType_size   " + EIGHT);
	   if (mType_page.equals(STYPE_STOCK_RIGHT)) {
		mPublicRl.setVisibility(View.VISIBLE);
		mStockRightLL.setVisibility(View.GONE);
		mStockSearch.setVisibility(View.VISIBLE);
		mSearchEt.setHint("请输入耗材名称、型号规格、操作人查询");
	   } else {
		mStockRightLL.setVisibility(View.GONE);
		mPublicRl.setVisibility(View.GONE);
	   }
	   mRightTop.setVisibility(View.GONE);
	   mRelativeLayout.setVisibility(View.GONE);

	   String[] array = mContext.getResources().getStringArray(R.array.eight_runwate_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;

	}
	//各个表格不同区分
	if (mSize == FIVE &&
	    (mType_page.equals(STYPE_STOCK_MIDDLE) || mType_page.equals(STYPE_STOCK_LEFT))) {
	   Log.i("BaseQuickAdapter", "mType_page   " + FIVE);
	   mLayout = R.layout.item_stockmid_five_layout;
	   mHeadView = getLayoutInflater().inflate(R.layout.item_stockmid_five_title_layout,
								 (ViewGroup) mLinearLayout.getParent(), false);
	   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	   mPublicAdapter = new TimelyPublicAdapter(mLayout, genData5(), mSize);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
		@Override
		public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		   mContext.startActivity(new Intent(mContext, StockMidTypeActivity.class));
		}
	   });

	} else if (mSize == SIX) {
	   if (mType_page.equals(TYPE_TIMELY)) {
		mLayout = R.layout.item_realtime_six_layout;
		mHeadView = getLayoutInflater().inflate(R.layout.item_realtime_six_title_layout,
								    (ViewGroup) mLinearLayout.getParent(), false);
		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData6(), mSize,TYPE_TIMELY);

		mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
		   @Override
		   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			mContext.startActivity(new Intent(mContext, TimelyDetailsActivity.class));
		   }
		});
	   }else {
		mLayout = R.layout.item_frg_six_layout;
		mHeadView = getLayoutInflater().inflate(R.layout.item_frg_six_title_layout,
								    (ViewGroup) mLinearLayout.getParent(), false);
		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	   }

	} else if (mSize == SEVEN ) {
//	   if (mType_page.equals(TYPE_TIMELY)) {
//		Log.i("BaseQuickAdapter", "mType_page   " + SEVEN);
//		mLayout = R.layout.item_realtime_seven_layout;
//		mHeadView = getLayoutInflater().inflate(R.layout.item_realtime_seven_title_layout,
//								    (ViewGroup) mLinearLayout.getParent(), false);
//		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
//		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
//		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
//		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
//		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
//		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
//		((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
//		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData7(), mSize);
//
//		mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//		   @Override
//		   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//			Log.i("BaseQuickAdapter", "mPublicAdapter");
//		   }
//		});
//	   } else
	      if (mType_page.equals(STYPE_FORM)) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		lp.setMargins(0,0,0,0);
		mPublicLl.setLayoutParams(lp);
		mLayout = R.layout.item_form_seven_layout;
		mHeadView = getLayoutInflater().inflate(R.layout.item_form_seven_title_layout,
								    (ViewGroup) mLinearLayout.getParent(), false);

		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData72(), mSize, STYPE_FORM);
		mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
		   @Override
		   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			String six = mPublicAdapter.getItem(position).six;
			if (!six.equals("已领取")) {
			   DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", null);
			} else {
			   ToastUtils.showShort("此项已领取！");
			}

		   }
		});
	   }

	} else if (mSize == EIGHT &&
		     (mType_page.equals(TYPE_RUNWATE) || mType_page.equals(STYPE_STOCK_RIGHT))) {
	   Log.i("BaseQuickAdapter", "mType_page   " + EIGHT);
	   mLayout = R.layout.item_runwate_eight_layout;
	   mHeadView = getLayoutInflater().inflate(R.layout.item_runwate_eight_title_layout,
								 (ViewGroup) mLinearLayout.getParent(), false);
	   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	   ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
	   if (mType_page.equals(STYPE_STOCK_RIGHT)) {
		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData82(), mSize,
								     STYPE_STOCK_RIGHT);
	   } else {
		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData8(), mSize);
	   }

	}
	mHeadView.setBackgroundResource(R.color.bg_green);
	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRefreshLayout.setEnableAutoLoadMore(true);
	mRecyclerview.setAdapter(mPublicAdapter);
	mLinearLayout.addView(mHeadView);

   }

   private void initlistener() {

	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		mRefreshLayout.setNoMoreData(false);
		Log.i("BaseQuickAdapter", "点击即将单独的的的的的");

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

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick({R.id.timely_start_btn, R.id.timely_profit, R.id.timely_loss, R.id.search_iv_delete})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (true) {//未关闭柜门
			Toast.makeText(mContext, "请关闭柜门", Toast.LENGTH_SHORT).show();
		   } else {
			//TODO:开始盘点
		   }

		}
		break;
	   case R.id.timely_profit://盘盈
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mContext.startActivity(new Intent(mContext, TimelyProfitActivity.class));
		}
		break;
	   case R.id.timely_loss://盘亏
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mContext.startActivity(new Intent(mContext, TimelyLossActivity.class));
		}
		break;
	   case R.id.search_iv_delete://清空搜索框
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   mContext.startActivity(new Intent(mContext, TimelyLossActivity.class));
		}
		break;
	}
   }

   private List<Movie> genData5() {

	ArrayList<Movie> list = new ArrayList<>();
	String five;
	for (int i = 0; i < 20; i++) {
	   if (i == 0) {
		five = "已过期";
	   } else if (i == 1) {
		five = "≤100天";
	   } else if (i == 2) {
		five = "≤70天";
	   } else if (i == 3) {
		five = "≤28天";
	   } else {
		five = "2019-10-22";
	   }
	   String one = "电动腔镜直线型血管切割吻合器和钉仓" + i;
	   String two = "大圈（ASJ-1-S）" + i;
	   String three = "1" + i;
	   String four = "1";

	   Movie movie = new Movie(one, two, three, four, five, null, null, null);
	   list.add(movie);
	}
	return list;
   }

   private List<Movie> genData7() {

	ArrayList<Movie> list = new ArrayList<>();
	String four;
	for (int i = 0; i < 20; i++) {
	   if (i == 0) {
		four = "已过期";
	   } else if (i == 1) {
		four = "≤100天";
	   } else if (i == 2) {
		four = "≤70天";
	   } else if (i == 3) {
		four = "≤28天";
	   } else {
		four = "2019-10-22";
	   }
	   String one = "微创系统" + i;
	   String two = "*15151223333ddd3" + i;
	   String three = "RFID01" + i;
	   String five = "1" + i;
	   String six = "0" + i;
	   String seven = "1" + i;

	   Movie movie = new Movie(one, two, three, four, five, six, seven, null);
	   list.add(movie);
	}
	return list;
   }
   private List<Movie> genData6() {

	ArrayList<Movie> list = new ArrayList<>();
	String three;
	for (int i = 0; i < 20; i++) {
	   if (i == 0) {
		three = "已过期";
	   } else if (i == 1) {
		three = "≤100天";
	   } else if (i == 2) {
		three = "≤70天";
	   } else if (i == 3) {
		three = "≤28天";
	   } else {
		three = "2019-10-22";
	   }
	   String one = "微创系统" + i;
	   String two = "RFID01" + i;
	   String four = i+"号柜";
	   String five = "1" + i;
	   String six = "0" + i;

	   Movie movie = new Movie(one, two, three, four, five, six, null, null);
	   list.add(movie);
	}
	return list;
   }
   private List<Movie> genData72() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 25; i++) {
	   String one = "微创路入系统";
	   String two = "FLR01" + i;
	   ;
	   String three = "" + i;
	   String four = "王麻子" + i;
	   String five = i + "号柜";
	   ;
	   String seven = "打开柜门";
	   String six = "";
	   if (i == 2) {
		six = "已领取";
	   } else {
		six = "未领取";
	   }
	   Movie movie = new Movie(one, two, three, four, five, six, seven, null);
	   list.add(movie);
	}
	return list;
   }

   private List<Movie> genData82() {
	String one;
	String five = null;
	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 20; i++) {
	   if (i == 0) {
		one = "未使用";
		five = "已过期";
	   } else if (i == 1) {
		one = "待入库";
		five = "≤100天";
	   } else if (i == 2) {
		one = "待移入";
		five = "≤70天";
	   } else if (i == 3) {
		one = "未使用";
		five = "≤28天";
	   } else if (i == 4) {
		one = "未使用";
	   } else {
		one = "未使用";
		five = "2019-10-22";
	   }
	   String two = "微创录入系统";
	   String three = "*15151223333ddd3" + i;
	   String four = "RFID01" + i;
	   String six = "1" + i;
	   String seven = "2019-10-22\n16:2" + i;
	   String eight = "张小" + i;

	   Movie movie = new Movie(one, two, three, four, five, six, seven, eight);
	   list.add(movie);
	}
	return list;
   }

   private List<Movie> genData8() {
	String one;
	String five = null;
	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 20; i++) {
	   if (i == 0) {
		one = "退货";
		five = "已过期";
	   } else if (i == 1) {
		one = "移入";
		five = "≤100天";
	   } else if (i == 2) {
		one = "返回";
		five = "≤70天";
	   } else if (i == 3) {
		one = "领用";
		five = "≤28天";
	   } else if (i == 4) {
		one = "移出";
	   } else {
		one = "入库";
		five = "2019-10-22";
	   }
	   String two = "微创录入系统";
	   String three = "*15151223333ddd3" + i;
	   String four = "RFID01" + i;
	   String six = "1" + i;
	   String seven = "2019-10-22\n16:2" + i;
	   String eight = "张小" + i;

	   Movie movie = new Movie(one, two, three, four, five, six, seven, eight);
	   list.add(movie);
	}
	return list;
   }

}