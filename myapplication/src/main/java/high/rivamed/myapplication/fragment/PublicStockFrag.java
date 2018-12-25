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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.StockMidTypeActivity;
import high.rivamed.myapplication.adapter.StockLeftDownAdapter;
import high.rivamed.myapplication.adapter.StockRightAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_LEFT;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_MIDDLE;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_RIGHT;

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

public class PublicStockFrag extends SimpleFragment {

   private static final String TYPE_SIZE  = "TYPE_SIZE";
   private static final String TYPE_PAGE  = "TYPE_PAGE";
   private static final String DEVICECODE = "DEVICECODE";
   private static final int    FIVE       = 5;
   private static final int    EIGHT      = 8;
   private static final String TAG        = "PublicStockFrag";
   @BindView(R.id.timely_reality2)
   TextView           mTimelyReality2;
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
   @BindView(R.id.right_top)
   LinearLayout       mRightTop;
   @BindView(R.id.stock_right_btn)
   LinearLayout   mStockRightLL;
   @BindView(R.id.public_rl)
   RelativeLayout mPublicRl;
   @BindView(R.id.stock_left_zhengchang)
   RadioButton    mStockLeftZhengchang;
   @BindView(R.id.stock_left_rg)
   RadioGroup     mStockLeftRg;
   @BindView(R.id.stock_timely_ll)
   RelativeLayout mStockTimelyLl;

   private int          mSize; //假数据 举例6个横向格子
   private View         mHeadView;
   private int          mLayout;
   private int          mType_size;
   private String       mType_page;
   private String       mDeviceCode;
   private InventoryDto mLeftDownBean;
   List<String> titeleList = null;
   private int                  mStopFlag;
   private InventoryDto         mInventoryDto;
   private List<InventoryVo>    mInventoryVos;
   public  StockLeftDownAdapter mDownAdapter;
   private String               mTrim;
   private List<InventoryVo>    mTCstStockRightList;
   private StockRightAdapter    mRightAdapter;
   private StockLeftDownAdapter mStockLeftAdapter;

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START3")) {
	   Log.i("ccc", "START3:  " + mDeviceCode);
//	   initData();
	}
   }

   /**
    * @param param 表格的列数  用来判断数据长度  表格的列表
    * @param type  表格类别
    * @returnList<RunWateBean.RowsBean>
    */
   public static PublicStockFrag newInstance(int param, String type, String deviceCode) {
	Bundle args = new Bundle();
	PublicStockFrag fragment = new PublicStockFrag();
	args.putInt(TYPE_SIZE, param);
	args.putString(TYPE_PAGE, type);
	args.putString(DEVICECODE, deviceCode);
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
   }

   /**
    * 数据加载
    */
   private void initData() {
	//表格的title区分和一部分数据
	if (mType_size == FIVE) {
	   Log.i("ccc", "ssfsfsf:  " + mDeviceCode);
	   if (mType_page.equals(STYPE_STOCK_MIDDLE)) {
		mSearchEt.setHint("请输入耗材名称、规格型号查询");
		mRelativeLayout.setVisibility(View.GONE);
		mStockSearch.setVisibility(View.VISIBLE);
		mStockTimelyLl.setVisibility(View.VISIBLE);
		mRightTop.setVisibility(View.GONE);
		mStockLeftRg.check(R.id.stock_left_all);
		getMiddleDate(mDeviceCode, mSearchEt);
	   } else if (mType_page.equals(STYPE_STOCK_LEFT)) {
		mPublicRl.setVisibility(View.GONE);
		getLeftDownDate(mDeviceCode);
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
	LogUtils.i(TAG, "mDeviceCode  " + mDeviceCode);
	NetRequest.getInstance().getStockDown(null, mDeviceCode, -1, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result  " + result);
		if (mInventoryVos != null) {
		   mInventoryVos.clear();
		   mLeftDownBean = mGson.fromJson(result, InventoryDto.class);
		   List<InventoryVo> inventoryVos = mLeftDownBean.getInventoryVos();
		   mInventoryVos.addAll(inventoryVos);
		   mStockLeftAdapter.notifyDataSetChanged();
		} else {
		   mLeftDownBean = mGson.fromJson(result, InventoryDto.class);
		   mInventoryVos = mLeftDownBean.getInventoryVos();
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

			mStockLeftAdapter = new StockLeftDownAdapter(mLayout, mInventoryVos, mSize);
			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			View inflate = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
			mStockLeftAdapter.setEmptyView(inflate);
			mRecyclerview.setAdapter(mStockLeftAdapter);
			mLinearLayout.addView(mHeadView);

			mStockLeftAdapter.setOnItemClickListener(
				new BaseQuickAdapter.OnItemClickListener() {
				   @Override
				   public void onItemClick(
					   BaseQuickAdapter adapter, View view, int position) {
					mContext.startActivity(
						new Intent(mContext, StockMidTypeActivity.class));
					InventoryVo vosBean = mInventoryVos.get(position);
					EventBusUtils.postSticky(new Event.EventStockDetailVo(vosBean));
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
			mInventoryDto = null;
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
			break;
		   case R.id.stock_left_guoqi:
			mStopFlag = 0;
			mInventoryDto = null;
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
			break;
		   case R.id.stock_left_jinqi:
			mStopFlag = 1;
			mInventoryDto = null;
			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
			break;
		   //		   case R.id.stock_left_zhengchang:
		   //			mStopFlag = 4;
		   //			mInventoryDto = null;
		   //			LoadMiddleRgDate(mDeviceCode, mStopFlag, mTrim);
		   //			break;
		}

	   }
	});

   }

   private void LoadMiddleRgDate(String mDeviceCode, int mStopFlag, String editString) {
	LogUtils.i(TAG, "mDeviceCode  " + mDeviceCode);
	NetRequest.getInstance()
		.getStockDown(editString, mDeviceCode, mStopFlag, mContext, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "LoadMiddleRgDate   " + result);
			if (mInventoryVos != null) {
			   mInventoryVos.clear();
			   //			   mInventoryDto = mGson.fromJson(result, SocketLeftDownBean.class);
			   mInventoryDto = mGson.fromJson(result, InventoryDto.class);

			   List<InventoryVo> inventoryVos = mInventoryDto.getInventoryVos();
			   mInventoryVos.addAll(inventoryVos);
			   ArrayList<String> strings = new ArrayList<>();
			   int SIZE = 0;
			   for (InventoryVo vosBean : inventoryVos) {
				strings.add(vosBean.getCstId());
				SIZE += vosBean.getCountStock();
			   }
			   ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
			   mTimelyReality2.setText(Html.fromHtml(
				   "耗材种类：<font color='#262626'><big>" + list.size() +
				   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" + SIZE +
				   "</big></font>"));
			   mDownAdapter.notifyDataSetChanged();
			} else {
			   mInventoryDto = mGson.fromJson(result, InventoryDto.class);
			   mInventoryVos = mInventoryDto.getInventoryVos();
			   if (mInventoryVos != null) {
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

				mDownAdapter = new StockLeftDownAdapter(mLayout, mInventoryVos, mSize);
				mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
				mDownAdapter.setOnItemClickListener(
					new BaseQuickAdapter.OnItemClickListener() {
					   @Override
					   public void onItemClick(
						   BaseQuickAdapter adapter, View view, int position) {
						mContext.startActivity(
							new Intent(mContext, StockMidTypeActivity.class));
						InventoryVo inventoryVo = mInventoryVos.get(
							position);
						//						EventBusUtils.postSticky(inventoryVo);
						EventBusUtils.postSticky(
							new Event.EventStockDetailVo(inventoryVo));
					   }
					});
				mHeadView.setBackgroundResource(R.color.bg_green);
				mRecyclerview.addItemDecoration(
					new DividerItemDecoration(mContext, VERTICAL));
				mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
				mRefreshLayout.setEnableAutoLoadMore(false);
				mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
				mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
				View inflate = LayoutInflater.from(_mActivity)
					.inflate(R.layout.recy_null, null);
				mDownAdapter.setEmptyView(inflate);
				mRecyclerview.setAdapter(mDownAdapter);
				mLinearLayout.addView(mHeadView);
				ArrayList<String> strings = new ArrayList<>();
				int SIZE = 0;
				for (InventoryVo vosBean : mInventoryVos) {
				   strings.add(vosBean.getCstId());
				   SIZE += vosBean.getCountStock();

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
	LogUtils.i(TAG, "deviceCode   " + deviceCode);
	LogUtils.i(TAG, "mTrim   " + mTrim);
	NetRequest.getInstance().getRightUnconfDate(deviceCode, mTrim, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i("ffa", "result   " + result);
		if (mTCstStockRightList != null) {
		   mTCstStockRightList.clear();
		   InventoryDto socketRightBean = mGson.fromJson(result, InventoryDto.class);
		   List<InventoryVo> inventoryVos = socketRightBean.getInventoryVos();
		   mTCstStockRightList.addAll(inventoryVos);
		   mRightAdapter.notifyDataSetChanged();

		} else {
		   InventoryDto socketRightBean = mGson.fromJson(result, InventoryDto.class);
		   mTCstStockRightList = socketRightBean.getInventoryVos();
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
		   LogUtils.i("CC", "mRecyclerview   " + (mRecyclerview == null));
		   mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
		   mRecyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
		   mRefreshLayout.setEnableAutoLoadMore(false);
		   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		   View inflate = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
		   mRightAdapter.setEmptyView(inflate);
		   mRecyclerview.setAdapter(mRightAdapter);
		   mLinearLayout.addView(mHeadView);
		}
	   }

	});
   }
}