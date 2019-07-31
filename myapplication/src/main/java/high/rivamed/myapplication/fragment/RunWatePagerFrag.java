package high.rivamed.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RunWatePageAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.RunWateBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.PowerDateUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.CONFIG_042;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/25 11:15
 * 描述:        耗材流水page  fragment
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
@SuppressLint("ValidFragment")
public class RunWatePagerFrag extends SimpleFragment {

   private static final int loadTime      = 200;
   private static final String TAG        = "RunWatePagerFrag";
   List<String> titeleList = null;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView          mSearchIvDelete;
   @BindView(R.id.search_time_all)
   RadioButton        mSearchTimeAll;
   @BindView(R.id.search_time_day)
   RadioButton        mSearchTimeDay;
   @BindView(R.id.search_time_week)
   RadioButton        mSearchTimeWeek;
   @BindView(R.id.search_time_moon)
   RadioButton        mSearchTimeMoon;
   @BindView(R.id.search_time_rg)
   RadioGroup         mSearchTimeRg;
   @BindView(R.id.search_time_start)
   TextView           mSearchTimeStart;
   @BindView(R.id.search_time_end)
   TextView           mSearchTimeEnd;
   @BindView(R.id.search_time_start_gone)
   TextView           mSearchTimeStartGone;
   @BindView(R.id.search_time_end_gone)
   TextView           mSearchTimeEndGone;
   @BindView(R.id.search_type_all)
   RadioButton        mSearchTypeAll;
   @BindView(R.id.search_type_hous)
   RadioButton        mSearchTypeHous;
   @BindView(R.id.search_type_use)
   RadioButton        mSearchTypeUse;
   @BindView(R.id.search_type_binduse)
   RadioButton        mSearchTypeBindUse;
   @BindView(R.id.search_type_info)
   RadioButton        mSearchTypeInfo;
   @BindView(R.id.search_type_out)
   RadioButton        mSearchTypeOut;
   @BindView(R.id.search_type_db)
   RadioButton        mSearchTypeDb;
   @BindView(R.id.search_type_return)
   RadioButton        mSearchTypeReturn;
   @BindView(R.id.search_type_return_goods)
   RadioButton        mSearchTypeReturnGoods;
   @BindView(R.id.search_type_thzc)
   RadioButton        mSearchTypeThzc;
   @BindView(R.id.search_type_tf)
   RadioButton        mSearchTypeTf;
   @BindView(R.id.search_type_jf)
   RadioButton        mSearchTypeJf;
   @BindView(R.id.search_type_rg)
   RadioGroup         mSearchTypeRg;
   @BindView(R.id.header)
   MaterialHeader     mHeader;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.public_ll)
   LinearLayout       mPublicLl;
   Unbinder unbinder;
   private View mHeadView;
   private int  mLayout;
   private int PAGE = 1;
   private int SIZE = 20;
   public   String mDeviceCode;
   private String mTerm = null;
   private String mEndTime = null;
   private String mStartTime= null;
   private String mStatus = null;
   private List<RunWateBean.RowsBean> mWateBeanRows;
   private RunWatePageAdapter         mWatePageAdapter;
   private RunWateBean                mRunWateBean;

   @SuppressLint("ValidFragment")
   public RunWatePagerFrag(String deviceCode) {
	this.mDeviceCode = deviceCode;

   }
   @SuppressLint("ValidFragment")
   public RunWatePagerFrag() {
   }

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
      LogUtils.i(TAG,"START2   "+event.type);
	if (event.type.equals("START2")&&mSearchTimeStart!=null) {
	   mSearchTimeStart.setText("");
	   mSearchTimeEnd.setText("");
	   initDate();
	}
   }
   @Override
   public int getLayoutId() {
	return R.layout.runwate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	mSearchTypeDb.setVisibility(View.GONE);
	initDate();

   }

   private void initDate() {
      if (UIUtils.getConfigType(mContext, CONFIG_019)){
	   mSearchTypeBindUse.setVisibility(View.VISIBLE);
	   mSearchTypeUse.setVisibility(View.VISIBLE);
	}else if (UIUtils.getConfigType(mContext, CONFIG_007)){
	   mSearchTypeBindUse.setVisibility(View.VISIBLE);
	   mSearchTypeUse.setVisibility(View.GONE);
	}else if (!UIUtils.getConfigType(mContext, CONFIG_007)&&!UIUtils.getConfigType(mContext, CONFIG_019)){
	   mSearchTypeBindUse.setVisibility(View.GONE);
	   mSearchTypeUse.setVisibility(View.VISIBLE);
	}
	if (UIUtils.getConfigType(mContext, CONFIG_042)){
	   mSearchTypeTf.setVisibility(View.VISIBLE);
	   mSearchTypeJf.setVisibility(View.VISIBLE);
	}else {
	   mSearchTypeTf.setVisibility(View.GONE);
	   mSearchTypeJf.setVisibility(View.GONE);
	}
	mSearchTypeThzc.setVisibility(View.GONE);
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String format = sdf.format(date);
	mSearchTimeStart.setHint(PowerDateUtils.getTime());
	mSearchTimeEnd.setHint(format);

	mSearchEt.setHint("请输入耗材名称、规格型号、操作人、EPC查询");

	mStartTime=null;
	mEndTime=null;
	loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
	loadDate(mDeviceCode);

	String[] array = mContext.getResources().getStringArray(R.array.nine_runwate_arrays);
	titeleList = Arrays.asList(array);

   }

   private void initlistener() {
	/**
	 * 下拉刷新
	 */
	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		PAGE=1;
		mRefreshLayout.setNoMoreData(false);
		loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
		finishRefresh();
	   }
	});
	/**
	 * 加载更多
	 */
	mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {

		loadMoreRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
		finishLoadMore();
	   }
	});
	mSearchTimeStart.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		mStartTime = s.toString().trim();
	   }

	   @Override
	   public void afterTextChanged(Editable s) {
		if (mEndTime!=null){
		   loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
		}
	   }
	});
	mSearchTimeEnd.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		mEndTime = s.toString().trim();
	   }

	   @Override
	   public void afterTextChanged(Editable s) {
		if (mStartTime!=null){
		   loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
		}

	   }
	});
	mSearchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		mTerm = s.toString().trim();
//		UIUtils.hideSoftInput(_mActivity, mSearchEt);
		loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
	   }

	   @Override
	   public void afterTextChanged(Editable s) {

	   }
	});
   }

   private void loadDate(String mDeviceCode) {
	mSearchTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		   case R.id.search_type_all://全部
			mStatus = null;
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_hous://入库
			mStatus = "2";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_use://领用
			mStatus = "3";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_binduse://患者领用
			mStatus = "30";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_info://移入
			mStatus = "10";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_out://移出
			mStatus = "9";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_return://退回
			mStatus = "7";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_return_goods://退货
			mStatus = "8";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_db://调拨
			mStatus = "11";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_thzc://退货暂存
			mStatus = "12";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_jf://计费请求
			mStatus = "31";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_tf://退费请求
			mStatus = "32";
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		}
	   }
	});

//	mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//	   @Override
//	   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//		   mTerm = mSearchEt.getText().toString().trim();
//		   UIUtils.hideSoftInput(_mActivity, mSearchEt);
//		   loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
//		   return true;
//		}
//		return false;
//	   }
//	});
   }

   private void loadRunWateDate(
	   String deviceCode, String term, String startTime, String endTime, String status) {

	NetRequest.getInstance()
		.loadRunWate("1", String.valueOf(SIZE),deviceCode, term, startTime, endTime, status, mContext, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			if (mWateBeanRows != null) {
			   mWateBeanRows.clear();
			   mRunWateBean = mGson.fromJson(result, RunWateBean.class);
			   List<RunWateBean.RowsBean> rows = mRunWateBean.getRows();
			   mWateBeanRows.addAll(rows);
			   mWatePageAdapter.notifyDataSetChanged();

			} else {
			   mRunWateBean = mGson.fromJson(result, RunWateBean.class);
			   mWateBeanRows = mRunWateBean.getRows();
			   mLayout = R.layout.item_runwate_nine_layout;
			   mHeadView = LayoutInflater.from(_mActivity).inflate(
				   R.layout.item_runwate_nine_title_layout,
				   (ViewGroup) mLinearLayout.getParent(), false);
			   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(3));
			   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(2));
			   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
			   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
			   ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
			   ((TextView) mHeadView.findViewById(R.id.seven_nine)).setText(titeleList.get(8));

			   mWatePageAdapter = new RunWatePageAdapter(mLayout, mWateBeanRows);

			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
			   mRefreshLayout.setEnableAutoLoadMore(true);
			   mRecyclerview.setAdapter(mWatePageAdapter);
			   View inflate = LayoutInflater.from(_mActivity)
				   .inflate(R.layout.recy_null, null);
			   mWatePageAdapter.setEmptyView(inflate);
			   mLinearLayout.addView(mHeadView);
			   mWatePageAdapter.notifyDataSetChanged();
			}
		   }
		});
   }

   /**
    * 加载更多
    *
    * @param deviceCode
    * @param term
    * @param startTime
    * @param endTime
    * @param status
    */
   private void loadMoreRunWateDate(
	   String deviceCode, String term, String startTime, String endTime, String status) {
	PAGE++;
	NetRequest.getInstance()
		.loadRunWate(String.valueOf(PAGE), String.valueOf(SIZE),deviceCode, term, startTime, endTime, status, mContext, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {

			Log.i(TAG, "deviceCodedfdfdfd  :" + PAGE+ "     "  +SIZE);
			RunWateBean runWateBean = mGson.fromJson(result, RunWateBean.class);
			List<RunWateBean.RowsBean> rows = runWateBean.getRows();
			if (rows.size()<SIZE){
			   mWateBeanRows.addAll(rows);
			   finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			   mWatePageAdapter.notifyDataSetChanged();
			}else {
			   mWateBeanRows.addAll(rows);
			   mWatePageAdapter.notifyDataSetChanged();
			}
		   }
		});
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick({R.id.search_time_start, R.id.search_time_end})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.search_time_start:

	      DialogUtils.showTimeDialog(mContext, mSearchTimeStart);
		break;
	   case R.id.search_time_end:
		if (mStartTime == null) {
		   ToastUtils.showShort("请先选择开始时间");
		} else {
		  DialogUtils.showTimeDialog(mContext, mSearchTimeEnd);
		}
		break;
	}
   }
   /**
    * 上拉加载成功
    */
   public void finishLoadMore() {
	mRefreshLayout.finishLoadMore(loadTime);
   }

   /**
    * 下拉加载成功
    */
   public void finishRefresh() {
	mRefreshLayout.finishRefresh(loadTime);
   }
   public void finishLoadMoreWithNoMoreData(){
	//不用这个的原因是会去加载一遍才说没有数据
	mRefreshLayout.finishLoadMoreWithNoMoreData();
//	mRefreshLayout.setNoMoreData(true);
   }

   @Override
   public void onResume() {
	super.onResume();
	initlistener();
   }
}
