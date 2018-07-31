package high.rivamed.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RunWatePageAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.RunWateBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

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

   private static final String TYPE_SIZE  = "TYPE_SIZE";
   private static final String DEVICECODE = "DEVICECODE";

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
   @BindView(R.id.search_type_all)
   RadioButton        mSearchTypeAll;
   @BindView(R.id.search_type_hous)
   RadioButton        mSearchTypeHous;
   @BindView(R.id.search_type_use)
   RadioButton        mSearchTypeUse;
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
   private View   mHeadView;
   private int    mLayout;
   private int    mType_size;
   private String mDeviceCode;
   private String mTerm = null;
   private String mEndTime;
   private String mStartTime;
   private String mStatus = null;
   private List<RunWateBean.RowsBean> mWateBeanRows;
   private RunWatePageAdapter         mWatePageAdapter;
   private LoadingDialog.Builder      mBuilder;
   private RunWateBean                mRunWateBean;
   private String mStartTimes;

   @SuppressLint("ValidFragment")
   public RunWatePagerFrag(String deviceCode) {
	this.mDeviceCode = deviceCode;

   }

   @Override
   public int getLayoutId() {
	return R.layout.runwate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	mSearchEt.setHint("请输入耗材名称、型号规格、操作人");
	loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
	loadDate(mDeviceCode, mStartTime, mEndTime);

	mBuilder = DialogUtils.showLoading(mContext);
	String[] array = mContext.getResources().getStringArray(R.array.eight_runwate_arrays);
	titeleList = Arrays.asList(array);

	mSearchTimeStart.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		mStartTimes = s.toString().trim();
	   }

	   @Override
	   public void afterTextChanged(Editable s) {

	   }
	});
	mSearchTimeEnd.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (count != 0 && mEndTime != null) {
		   Log.i("ccc", mEndTime);
		   loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
		}

	   }

	   @Override
	   public void afterTextChanged(Editable s) {

	   }
	});
   }

   private void loadDate(String mDeviceCode, String mStartTime, String mEndTime) {
	mSearchTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		   case R.id.search_type_all://全部
			mStatus = null;
			mBuilder.create().show();
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_hous://入库
			mStatus = "2";
			mBuilder.create().show();
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_use://领用
			mStatus = "3";
			mBuilder.create().show();
			Log.i("ccc", "ssfsfsf:fef  " + mDeviceCode);
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_info://移入
			mStatus = "10";
			mBuilder.create().show();
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_out://移出
			mStatus = "9";
			mBuilder.create().show();
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_return://退回
			mStatus = "7";
			mBuilder.create().show();
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_return_goods://退货
			mStatus = "8";
			mBuilder.create().show();

			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_db://调拨
			mStatus = "11";
			mBuilder.create().show();
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		   case R.id.search_type_thzc://退货暂存
			mStatus = "12";
			mBuilder.create().show();
			loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
			break;
		}
	   }
	});

	mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	   @Override
	   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		   String mTerm = mSearchEt.getText().toString().trim();
		   UIUtils.hideSoftInput(_mActivity, mSearchEt);
		   Log.i("ccc", "mTerm  " + mTerm);
		   loadRunWateDate(mDeviceCode, mTerm, mStartTime, mEndTime, mStatus);
		   return true;
		}
		return false;
	   }
	});
   }

   private void loadRunWateDate(
	   String deviceCode, String term, String startTime, String endTime, String status) {
	NetRequest.getInstance()
		.loadRunWate(deviceCode, term, startTime, endTime, status, mContext,
				 new BaseResult() {
				    @Override
				    public void onSucceed(String result) {
					 Log.i("ccc", "deviceCodedfdfdfd  :" + deviceCode);
					 if (mWateBeanRows != null) {
					    mWateBeanRows.clear();
					    mRunWateBean = mGson.fromJson(result, RunWateBean.class);
					    List<RunWateBean.RowsBean> rows = mRunWateBean.getRows();
					    mWateBeanRows.addAll(rows);
					    mWatePageAdapter.notifyDataSetChanged();

					 } else {
					    mRunWateBean = mGson.fromJson(result, RunWateBean.class);
					    mWateBeanRows = mRunWateBean.getRows();
					    mLayout = R.layout.item_runwate_eight_layout;
					    mHeadView = getLayoutInflater().inflate(
						    R.layout.item_runwate_eight_title_layout,
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
					    ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(
						    titeleList.get(5));
					    ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(
						    titeleList.get(6));
					    ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(
						    titeleList.get(7));

					    mWatePageAdapter = new RunWatePageAdapter(mLayout, mWateBeanRows);

					    mHeadView.setBackgroundResource(R.color.bg_green);
					    mRecyclerview.addItemDecoration(
						    new DividerItemDecoration(mContext, VERTICAL));
					    mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
					    mRefreshLayout.setEnableAutoLoadMore(true);
					    mRecyclerview.setAdapter(mWatePageAdapter);
					    mLinearLayout.addView(mHeadView);
					    mWatePageAdapter.notifyDataSetChanged();
					 }
					 mBuilder.mDialog.dismiss();
					 Log.i("ccc", "deviceCode:" + deviceCode + "   " + result);
				    }

				    @Override
				    public void onError(String result) {
					 Log.i("ccc", "result:" + deviceCode + "   " + result);
					 mBuilder.mDialog.dismiss();
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
		mStartTime = DialogUtils.showTimeDialog(mContext, mSearchTimeStart);
		break;
	   case R.id.search_time_end:
		if (mStartTimes == null) {
		   ToastUtils.showShort("请先选择开始时间");
		} else {
		   mEndTime = DialogUtils.showTimeDialog(mContext, mSearchTimeEnd);
		}

		break;
	}
   }
}
