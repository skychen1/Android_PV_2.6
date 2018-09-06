package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.StockMidTypeActivity;
import high.rivamed.myapplication.activity.TimelyLossActivity;
import high.rivamed.myapplication.activity.TimelyProfitActivity;
import high.rivamed.myapplication.adapter.StockLeftDownAdapter;
import high.rivamed.myapplication.adapter.StockRightAdapter;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.bean.RunWateBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.StringUtils;
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

   private static final String TYPE_SIZE  = "TYPE_SIZE";
   private static final String TYPE_PAGE  = "TYPE_PAGE";
   private static final String DEVICECODE = "DEVICECODE";
   private static final String TYPE_LIST  = "list";
   private static final int    FIVE       = 5;
   private static final int    SIX        = 6;
   private static final int    SEVEN      = 7;
   private static final int    EIGHT      = 8;
   private static final String TAG        = "PublicTimelyFrag";
   @BindView(R.id.timely_start_btn)
   TextView mTimelyStartBtn;
   @BindView(R.id.timely_book)
   TextView mTimelyBook;
   @BindView(R.id.timely_reality)
   TextView mTimelyReality;

   @BindView(R.id.timely_reality2)
   TextView           mTimelyReality2;
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
   LinearLayout   mStockRightLL;
   @BindView(R.id.public_rl)
   RelativeLayout mPublicRl;
   @BindView(R.id.public_ll)
   LinearLayout   mPublicLl;
   @BindView(R.id.stock_left_all)
   RadioButton    mStockLeftAll;
   @BindView(R.id.stock_left_guoqi)
   RadioButton    mStockLeftGuoqi;

   @BindView(R.id.stock_left_jinqi)
   RadioButton    mStockLeftJinqi;
   @BindView(R.id.stock_left_zhengchang)
   RadioButton    mStockLeftZhengchang;
   @BindView(R.id.stock_left_rg)
   RadioGroup     mStockLeftRg;
   @BindView(R.id.stock_timely_ll)
   RelativeLayout mStockTimelyLl;

   private int                 mParam;
   private TimelyPublicAdapter mPublicAdapter;
   private int                 mSize; //假数据 举例6个横向格子
   private View                mHeadView;
   private int                 mLayout;
   private int                 mType_size;
   private String              mType_page;
   private String              mDeviceCode;
   private TCstInventoryDto  mLeftDownBean;
   List<String> titeleList = null;
   private int                        mStopFlag;
   private TCstInventoryDto           mTCstInventoryDto;
   private List<TCstInventoryVo>      mTCstInventoryVos;
   private StockLeftDownAdapter       mDownAdapter;
   private String                     mTrim;
   private List<TCstInventoryVo>      mTCstStockRightList;
   private StockRightAdapter          mRightAdapter;
   private List<RunWateBean.RowsBean> mWateBeanRows;
   private StockLeftDownAdapter mStockLeftAdapter;
   private List<TCstInventoryVo> mInventoryVos;

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START3")) {

	   initData();
	}
   }
   /**
    * @param param 表格的列数  用来判断数据长度  表格的列表
    * @param type  表格类别
    * @returnList<RunWateBean.RowsBean>
    */
   public static PublicTimelyFrag newInstance(int param, String type, String deviceCode) {
	Bundle args = new Bundle();
	PublicTimelyFrag fragment = new PublicTimelyFrag();
	Log.i("c", "mDeviceCode   " + deviceCode);
	Log.i("c", "mType_page   " + type);
	Log.i("c", "mType_size   " + param);
	args.putInt(TYPE_SIZE, param);
	args.putString(TYPE_PAGE, type);
	args.putString(DEVICECODE, deviceCode);
	fragment.setArguments(args);
	return fragment;
   }

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
      EventBusUtils.register(this);
	Bundle arguments = getArguments();
	mType_size = arguments.getInt(TYPE_SIZE);//假数据   用来判断数据长度  表格的列表
	mType_page = arguments.getString(TYPE_PAGE);
	mDeviceCode = arguments.getString(DEVICECODE);

	initData();

	initlistener();
   }

   /**
    * 数据加载
    */
   private void initData() {
	//表格的title区分和一部分数据
	//	mLoadingView.setVisibility(View.VISIBLE);
	if (mType_size == FIVE) {
	   if (mType_page.equals(STYPE_STOCK_MIDDLE)) {
		mSearchEt.setHint("请输入耗材名称、规格型号查询");
		mRelativeLayout.setVisibility(View.GONE);
		mStockSearch.setVisibility(View.VISIBLE);
		mStockTimelyLl.setVisibility(View.VISIBLE);
		mRightTop.setVisibility(View.GONE);
		Log.i("ccc", "ssfsfsf:  " + mDeviceCode);

		getMiddleDate(mDeviceCode, mSearchEt);

	   } else if (mType_page.equals(STYPE_STOCK_LEFT)) {
		mPublicRl.setVisibility(View.GONE);
		getLeftDownDate(mDeviceCode);
	   }

	} else if (mType_size == SIX) {
	   if (mType_page.equals(TYPE_TIMELY)) {

		getTimelyDate(mDeviceCode);

	   }
	} else if (mType_size == SEVEN) {

	   if (mType_page.equals(STYPE_FORM)) {

		mPublicRl.setVisibility(View.GONE);
		String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
		titeleList = Arrays.asList(array);
		mSize = array.length;
	   }
	} else if (mType_size == EIGHT && (mType_page.equals(STYPE_STOCK_RIGHT))) {

	   mRightTop.setVisibility(View.GONE);
	   mRelativeLayout.setVisibility(View.GONE);
	   String[] array = mContext.getResources().getStringArray(R.array.eight_runwate_arrays);
	   titeleList = Arrays.asList(array);
	   mSize = array.length;

	   if (mType_page.equals(STYPE_STOCK_RIGHT)) {
		mPublicRl.setVisibility(View.VISIBLE);
		mStockRightLL.setVisibility(View.GONE);
		mStockSearch.setVisibility(View.VISIBLE);
		mSearchEt.setHint("请输入耗材名称、规格型号、操作人查询");
		loadStockRightDate(mDeviceCode, "");
		mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		   @Override
		   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			   mTrim = mSearchEt.getText().toString().trim();

			   Toast.makeText(mContext, mTrim, Toast.LENGTH_SHORT).show();
			   UIUtils.hideSoftInput(_mActivity, mSearchEt);
			   loadStockRightDate(mDeviceCode, mTrim);
			   return true;
			}
			return false;
		   }
		});

	   } else {
		mStockRightLL.setVisibility(View.GONE);
		mPublicRl.setVisibility(View.GONE);

	   }

	}
	//各个表格不同区分
	if (mSize == SIX) {
	   if (mType_page.equals(TYPE_TIMELY)) {//实时盘点
		//
		//		mLayout = R.layout.item_realtime_six_layout;
		//		mHeadView = getLayoutInflater().inflate(R.layout.item_realtime_six_title_layout,
		//								    (ViewGroup) mLinearLayout.getParent(), false);
		//		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		//		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		//		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		//		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		//		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		//		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		//
		//		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData6(), mSize, TYPE_TIMELY);
		//
		//		mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
		//		   @Override
		//		   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		//			mContext.startActivity(new Intent(mContext, TimelyDetailsActivity.class));
		//		   }
		//		});
		//		mHeadView.setBackgroundResource(R.color.bg_green);
		//		mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
		//		mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		//		mRefreshLayout.setEnableAutoLoadMore(true);
		//		mRecyclerview.setAdapter(mPublicAdapter);
		//		mLinearLayout.addView(mHeadView);
	   } else {
		mLayout = R.layout.item_frg_six_layout;
		mHeadView = getLayoutInflater().inflate(R.layout.item_frg_six_title_layout,
								    (ViewGroup) mLinearLayout.getParent(), false);
		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		mHeadView.setBackgroundResource(R.color.bg_green);
		mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
		mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		mRefreshLayout.setEnableAutoLoadMore(false);mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		mRecyclerview.setAdapter(mPublicAdapter);
		mLinearLayout.addView(mHeadView);
	   }

	} else if (mSize == SEVEN) {
	   //	   	   if (mType_page.equals(TYPE_TIMELY)) {
	   //	   		Log.i("BaseQuickAdapter", "mType_page   " + SEVEN);
	   //	   		mLayout = R.layout.item_realtime_seven_layout;
	   //	   		mHeadView = getLayoutInflater().inflate(R.layout.item_realtime_seven_title_layout,
	   //	   								    (ViewGroup) mLinearLayout.getParent(), false);
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	   //	   		((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	   //	   		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData7(), mSize);
	   //
	   //	   		mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   //	   		   @Override
	   //	   		   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
	   //	   			Log.i("BaseQuickAdapter", "mPublicAdapter");
	   //	   		   }
	   //	   		});
	   //	   	   } else
	   if (mType_page.equals(STYPE_FORM)) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		lp.setMargins(0, 0, 0, 0);
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
		mHeadView.setBackgroundResource(R.color.bg_green);
		mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
		mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		mRefreshLayout.setEnableAutoLoadMore(false);mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		mRecyclerview.setAdapter(mPublicAdapter);
		mLinearLayout.addView(mHeadView);
	   }

	} else if (mSize == EIGHT && (mType_page.equals(TYPE_RUNWATE))) {
	   Log.i("BaseQuickAdapter", "mType_page   " + EIGHT);
	   //	   mLayout = R.layout.item_runwate_eight_layout;
	   //	   mHeadView = getLayoutInflater().inflate(R.layout.item_runwate_eight_title_layout,
	   //								 (ViewGroup) mLinearLayout.getParent(), false);
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	   //	   ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
	   //	   //	   if (mType_page.equals(STYPE_STOCK_RIGHT)) {
	   //	   //		mPublicAdapter = new TimelyPublicAdapter(mLayout, genData82(), mSize,
	   //	   //								     STYPE_STOCK_RIGHT);
	   //	   //	   } else {
	   //	   mPublicAdapter = new TimelyPublicAdapter(mLayout, genData8(), mSize);
	   //	   //	   }
	   //	   mHeadView.setBackgroundResource(R.color.bg_green);
	   //	   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   //	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   //	   mRefreshLayout.setEnableAutoLoadMore(true);
	   //	   mRecyclerview.setAdapter(mPublicAdapter);
	   //	   mLinearLayout.addView(mHeadView);
	}

   }

   /**
    * 获取实时盘点的数据
    *
    * @param deviceCode
    */
   private void getTimelyDate(String deviceCode) {

	String str = "3";
	mTimelyLoss.setText(Html.fromHtml("盘亏：" + "<font color='#F5222D'>" + str + "</font>"));
	String str1 = "99";
	mTimelyReality.setText(Html.fromHtml("实际扫描数：" + "<font color='#F5222D'>" + str1 + "</font>"));
	mTimelyBook.setText("账面库存数：" + SIX);
	String[] array = mContext.getResources().getStringArray(R.array.six_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
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

   /**
    * 库存状态和 库存监控底部和库存详情
    *
    * @param mDeviceCode
    */
   public void getLeftDownDate(String mDeviceCode) {
	mPublicRl.setVisibility(View.GONE);
	String[] array = _mActivity.getResources().getStringArray(R.array.five_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	NetRequest.getInstance().getStockDown(null, mDeviceCode, -1, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		LogUtils.i(TAG,"result  "+result);
		if (mInventoryVos != null) {
		   mInventoryVos.clear();
		   mLeftDownBean = mGson.fromJson(result, TCstInventoryDto.class);
		   List<TCstInventoryVo> inventoryVos = mLeftDownBean.gettCstInventoryVos();
		   mInventoryVos.addAll(inventoryVos);
		   mStockLeftAdapter.notifyDataSetChanged();
		}else {
		   mLeftDownBean = mGson.fromJson(result, TCstInventoryDto.class);
		   mInventoryVos = mLeftDownBean.gettCstInventoryVos();
		   if (mInventoryVos != null) {
			mLayout = R.layout.item_stockmid_five_layout;
			mHeadView = LayoutInflater.from(_mActivity)
				.inflate(R.layout.item_stockmid_five_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));

			mStockLeftAdapter = new StockLeftDownAdapter(mLayout, mInventoryVos,
										   mSize);
			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
			mRefreshLayout.setEnableAutoLoadMore(false);mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			View inflate = LayoutInflater.from(_mActivity)
				.inflate(R.layout.recy_null, null);
			mStockLeftAdapter.setEmptyView(inflate);
			mRecyclerview.setAdapter(mStockLeftAdapter);
			mLinearLayout.addView(mHeadView);

			mStockLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(
				   BaseQuickAdapter adapter, View view, int position) {
				mContext.startActivity(new Intent(mContext, StockMidTypeActivity.class));
				TCstInventoryVo vosBean = mInventoryVos.get(position);
				EventBusUtils.postSticky(vosBean);
			   }
			});

		   }
		}
	   }
	});
   }

   /**
    * 库存状态和 库存监控底部和库存详情
    *
    * @param mDeviceCode
    */
   public void getMiddleDate(String mDeviceCode, EditText mSearchEt) {
	String[] array = _mActivity.getResources().getStringArray(R.array.five_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTimelyReality2.setVisibility(View.VISIBLE);
	mStockLeftZhengchang.setVisibility(View.GONE);
	mStopFlag = -1;

	LoadMiddleRgDate(mDeviceCode, mStopFlag, null);
	mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	   @Override
	   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		   mTrim = mSearchEt.getText().toString().trim();
		   UIUtils.hideSoftInput(_mActivity, mSearchEt);
		   LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
		   return true;
		}
		return false;
	   }
	});

	mStockLeftRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		   case R.id.stock_left_all://全部
			mStopFlag = -1;
			mTCstInventoryDto = null;
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
			break;
		   case R.id.stock_left_guoqi:
			mStopFlag = 0;
			mTCstInventoryDto = null;
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
			break;
		   case R.id.stock_left_jinqi:
			mStopFlag = 1;
			mTCstInventoryDto = null;
			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
			break;
//		   case R.id.stock_left_zhengchang:
//			mStopFlag = 4;
//			mTCstInventoryDto = null;
//			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
//			break;
		}

	   }
	});

   }

   private void LoadMiddleRgDate(String mDeviceCode, int mStopFlag, String editString) {

	NetRequest.getInstance()
		.getStockDown(editString, mDeviceCode, mStopFlag, mContext, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG,"LoadMiddleRgDate   "+result);
			if (mTCstInventoryVos != null) {
			   mTCstInventoryVos.clear();
			   //			   mTCstInventoryDto = mGson.fromJson(result, SocketLeftDownBean.class);
			   mTCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);

			   List<TCstInventoryVo> tCstInventoryVos = mTCstInventoryDto.gettCstInventoryVos();
			   mTCstInventoryVos.addAll(tCstInventoryVos);
			   ArrayList<String> strings = new ArrayList<>();
			   int SIZE = 0;
			   for (TCstInventoryVo vosBean : tCstInventoryVos) {
				strings.add(vosBean.getCstCode());
				SIZE += vosBean.getCount();
			   }
			   ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
			   mTimelyReality2.setText(Html.fromHtml(
				   "耗材种类：<font color='#262626'><big>" + list.size() +
				   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" + SIZE +
				   "</big></font>"));
			   mDownAdapter.notifyDataSetChanged();
			} else {
			   mTCstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
			   mTCstInventoryVos = mTCstInventoryDto.gettCstInventoryVos();
			   if (mTCstInventoryVos != null) {
				mLayout = R.layout.item_stockmid_five_layout;
				mHeadView = LayoutInflater.from(_mActivity)
					.inflate(R.layout.item_stockmid_five_title_layout,
						   (ViewGroup) mLinearLayout.getParent(), false);
				((TextView) mHeadView.findViewById(R.id.seven_one)).setText(
					titeleList.get(0));
				((TextView) mHeadView.findViewById(R.id.seven_two)).setText(
					titeleList.get(1));
				((TextView) mHeadView.findViewById(R.id.seven_three)).setText(
					titeleList.get(2));
				((TextView) mHeadView.findViewById(R.id.seven_four)).setText(
					titeleList.get(3));
				((TextView) mHeadView.findViewById(R.id.seven_five)).setText(
					titeleList.get(4));

				mDownAdapter = new StockLeftDownAdapter(mLayout, mTCstInventoryVos, mSize);
				mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
				mDownAdapter.setOnItemClickListener(
					new BaseQuickAdapter.OnItemClickListener() {
					   @Override
					   public void onItemClick(
						   BaseQuickAdapter adapter, View view, int position) {
						mContext.startActivity(
							new Intent(mContext, StockMidTypeActivity.class));
						TCstInventoryVo tCstInventoryVo = mTCstInventoryVos.get(
							position);
						EventBusUtils.postSticky(tCstInventoryVo);
					   }
					});
				mHeadView.setBackgroundResource(R.color.bg_green);
				mRecyclerview.addItemDecoration(
					new DividerItemDecoration(mContext, VERTICAL));
				mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
				mRefreshLayout.setEnableAutoLoadMore(false);mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
				mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
				View inflate = LayoutInflater.from(_mActivity)
					.inflate(R.layout.recy_null, null);
				mDownAdapter.setEmptyView(inflate);
				mRecyclerview.setAdapter(mDownAdapter);
				mLinearLayout.addView(mHeadView);
				ArrayList<String> strings = new ArrayList<>();
				int SIZE = 0;
				for (TCstInventoryVo vosBean : mTCstInventoryVos) {
				   strings.add(vosBean.getCstCode());
				   SIZE += vosBean.getCount();

				}
				   ArrayList<String> list = new ArrayList<>();
				   try {
					   list = StringUtils.removeDuplicteUsers(strings);
				   } catch (Exception e) {
					   e.printStackTrace();
				   }

				   mTimelyReality2.setText(Html.fromHtml(
					"耗材种类：<font color='#262626'><big>" + list.size() +
					"</big>&emsp</font>耗材数量：<font color='#262626'><big>" + SIZE +
					"</big></font>"));

			   }

			}

		   }
		});
   }

   /**
    * 未确认耗材
    *
    * @param deviceCode
    */
   private void loadStockRightDate(String deviceCode, String mTrim) {
      LogUtils.i(TAG,"deviceCode   "+deviceCode);
      LogUtils.i(TAG,"mTrim   "+mTrim);
	NetRequest.getInstance().getRightUnconfDate(deviceCode, mTrim, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i("ffa", "result   " + result);
		if (mTCstStockRightList != null) {
		   mTCstStockRightList.clear();
		   TCstInventoryDto socketRightBean = mGson.fromJson(result, TCstInventoryDto.class);
		   List<TCstInventoryVo> tCstInventoryVos = socketRightBean.gettCstInventoryVos();
		   mTCstStockRightList.addAll(tCstInventoryVos);
		   mRightAdapter.notifyDataSetChanged();

		} else {
		   TCstInventoryDto socketRightBean = mGson.fromJson(result, TCstInventoryDto.class);
		   mTCstStockRightList = socketRightBean.gettCstInventoryVos();
		   mLayout = R.layout.item_runwate_eight_layout;
		   mHeadView = LayoutInflater.from(_mActivity)
			   .inflate(R.layout.item_runwate_eight_title_layout,
					(ViewGroup) mLinearLayout.getParent(), false);
		   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
		   ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));

		   mRightAdapter = new StockRightAdapter(mLayout, mTCstStockRightList);

		   mHeadView.setBackgroundResource(R.color.bg_green);
		   LogUtils.i("CC","mRecyclerview   "+(mRecyclerview==null));
		   mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
		   mRecyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
		   mRefreshLayout.setEnableAutoLoadMore(false);mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		   View inflate = LayoutInflater.from(_mActivity)
			   .inflate(R.layout.recy_null, null);
		   mRightAdapter.setEmptyView(inflate);
		   mRecyclerview.setAdapter(mRightAdapter);
		   mLinearLayout.addView(mHeadView);
		}
	   }

	});
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

}