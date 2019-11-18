package high.rivamed.myapplication.views;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.FastInOutBoxActivity;
import high.rivamed.myapplication.adapter.AfterBingAdapter;
import high.rivamed.myapplication.adapter.BindTemporaryAdapter;
import high.rivamed.myapplication.adapter.BingDialogOutAdapter;
import high.rivamed.myapplication.adapter.InBoxAllAdapter;
import high.rivamed.myapplication.adapter.OutBoxAllAdapter;
import high.rivamed.myapplication.adapter.RecogHaocaiAdapter;
import high.rivamed.myapplication.adapter.StockDetailsAdapter;
import high.rivamed.myapplication.adapter.TimeDetailsAdapter;
import high.rivamed.myapplication.adapter.TimelyProfitAdapter;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.activity.OutBoxBingActivity.mOnBtnGone;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_HAOCAI;
import static high.rivamed.myapplication.cont.Constants.DOWN_MENU_YICHU;
import static high.rivamed.myapplication.cont.Constants.PATIENT_TYPE;
import static high.rivamed.myapplication.cont.Constants.STYPE_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG2;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.STYPE_LOSS_TYPE;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
import static high.rivamed.myapplication.cont.Constants.STYPE_PROFIT_TYPE;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_DETAILS;
import static high.rivamed.myapplication.cont.Constants.STYPE_TIMELY_FIVE_DETAILS;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/20 15:45
 * 描述:        各种列表的样式集合
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TableTypeView extends LinearLayout {

   public               Activity            mActivity;
   public               Context             mContext;
   public               List<String>        titeleList = new ArrayList<>();
   public               int                 mSize;
   public               int                 mOperation;
   public               Object              mMoviess;
   public               List<Movie>         mMovies    = (List<Movie>) mMoviess;
   public               LinearLayout        mLinearLayout;
   public               RecyclerView        mRecyclerview;
   public               RefreshLayout       mRefreshLayout;
   public               int                 mType;
   private              int                 mLayout;
   private              View                mHeadView;
   public               TimelyPublicAdapter mPublicAdapter;
   private static final int                 FOUR       = 4;
   private static final int                 FIVE       = 5;
   private static final int                 SIX        = 6;
   private static final int                 SEVEN      = 7;
   private static final int                 EIGHT      = 8;
   private              TextView            mSeven_one;
   private              TextView            mSeven_two;
   private              TextView            mSeven_three;
   private              TextView            mSeven_four;
   private              TextView            mSeven_five;
   private              TextView            mSeven_six;
   private              TextView            mSeven_seven;
   private              String              mDialog;

   public  InBoxAllAdapter      mInBoxAllAdapter;
   public  OutBoxAllAdapter     mOutBoxAllAdapter;
   private TimeDetailsAdapter   mTimeDetailsAdapter;
   public  Object               mObject;
   public  BingDialogOutAdapter mBingOutAdapter;
   public  AfterBingAdapter     mAfterBingAdapter;
   public  BindTemporaryAdapter mTempPatientAdapter;
   public  RecogHaocaiAdapter   mRecogHaocaiAdapter;

   public TableTypeView(
	   Context context, Activity activity, Object patientInfos, List<String> titeleList, int size,
	   LinearLayout linearLayout, RecyclerView recyclerview, SmartRefreshLayout refreshLayout,
	   int type, String dialog, int operation) {
	super(context);
	this.mActivity = activity;
	this.mContext = context;
	this.titeleList = titeleList;
	this.mSize = size;
	this.mObject = patientInfos;
	this.mOperation = operation;
	this.mLinearLayout = linearLayout;
	this.mRecyclerview = recyclerview;
	this.mRefreshLayout = refreshLayout;
	this.mType = type;
	this.mDialog = dialog;
	initData();
   }

   private void initData() {
	switch (mType) {
	   case ACTIVITY://activity
		if (mSize == FIVE) {
		   List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
		   if (mDialog != null && mDialog.equals(STYPE_STOCK_DETAILS)) {
			//-----------库存状态耗材详情-----------
			mLayout = R.layout.item_stockmid_five_details_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_stockmid_five_details_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			StockDetailsAdapter detailsAdapter = new StockDetailsAdapter(mLayout,
													 mInventoryVos);
			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			mRecyclerview.setAdapter(detailsAdapter);
			mLinearLayout.addView(mHeadView);
		   } else if (mDialog != null && mDialog.equals(STYPE_TIMELY_FIVE_DETAILS)) {
			mLayout = R.layout.item_stockmid_five_details_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_stockmid_five_details_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			mTimeDetailsAdapter = new TimeDetailsAdapter(mLayout, mInventoryVos);

			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			mRecyclerview.setAdapter(mTimeDetailsAdapter);
			mLinearLayout.addView(mHeadView);
		   } else {
			mLayout = R.layout.item_act_five_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_act_five_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
			mHeadView.setBackgroundResource(R.color.bg_green);

			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			mRecyclerview.setAdapter(mPublicAdapter);
			mLinearLayout.addView(mHeadView);
		   }
		} else if (mSize == SIX) {
		   if (mDialog != null && mDialog.equals(STYPE_DIALOG2)) {//所有在院患者Dialog
			List<BingFindSchedulesBean.PatientInfoVos> patientInfos = (List<BingFindSchedulesBean.PatientInfoVos>) mObject;
			mLayout = R.layout.item_dialog_six_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_dialog_six_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));

			for (int i = 0; i < patientInfos.size(); i++) {
			   patientInfos.get(i).setSelected(false);
			}
			mBingOutAdapter = new BingDialogOutAdapter(mLayout, patientInfos, mSize);
			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(true);
			mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能

			mRecyclerview.setAdapter(mBingOutAdapter);
			View inflate = LayoutInflater.from(mActivity).inflate(R.layout.recy_null, null);
			mBingOutAdapter.setEmptyView(inflate);
			mLinearLayout.addView(mHeadView);
		   } else if (mDialog != null && mDialog.equals(STYPE_OUT)) {
			List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
			mLayout = R.layout.item_out_six_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_out_six_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
			for (int i = 0; i < mInventoryVos.size(); i++) {
			   mInventoryVos.get(i).setSelected(true);
			}
			if (mOutBoxAllAdapter != null) {
			   mOutBoxAllAdapter.notifyDataSetChanged();
			} else {

			   mOutBoxAllAdapter = new OutBoxAllAdapter(mLayout, mInventoryVos);
			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRefreshLayout.setEnableAutoLoadMore(false);
			   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			   View inflate = LayoutInflater.from(mActivity)
				   .inflate(R.layout.recy_null, null);
			   mOutBoxAllAdapter.setEmptyView(inflate);
			   mRecyclerview.setAdapter(mOutBoxAllAdapter);
			   mLinearLayout.addView(mHeadView);
			}
			mOutBoxAllAdapter.setOnItemClickListener(
				new BaseQuickAdapter.OnItemClickListener() {
				   @Override
				   public void onItemClick(
					   BaseQuickAdapter adapter, View view, int position) {
					if (mOutBoxAllAdapter.getData().get(position).isSelected()) {
					   mOutBoxAllAdapter.getData().get(position).setSelected(false);
					} else {
					   mOutBoxAllAdapter.getData().get(position).setSelected(true);
					}
					FastInOutBoxActivity.mInOutDto.setOutInventoryVos(
						mOutBoxAllAdapter.getData());
					mOutBoxAllAdapter.notifyDataSetChanged();
				   }
				});

		   }
		} else if (mSize == SEVEN) {
		   if (mDialog != null && mDialog.equals(STYPE_BING)) {//绑定患者
			List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
			mLayout = R.layout.item_outbing_seven_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_outbing_seven_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			findId();
			if (mOperation != 3) {
			   mSeven_one.setVisibility(VISIBLE);
			} else {
			   mSeven_one.setVisibility(GONE);
			}
			mSeven_two.setText(titeleList.get(1));
			mSeven_three.setText(titeleList.get(2));
			mSeven_four.setText(titeleList.get(3));
			mSeven_five.setText(titeleList.get(4));
			mSeven_six.setText(titeleList.get(5));
			mSeven_seven.setText(titeleList.get(6));

			if (mAfterBingAdapter == null) {
			   mAfterBingAdapter = new AfterBingAdapter(mLayout, mInventoryVos, mOperation);
			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRefreshLayout.setEnableAutoLoadMore(false);
			   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能

			   mRecyclerview.setAdapter(mAfterBingAdapter);
			   mLinearLayout.addView(mHeadView);
			} else {
			   mAfterBingAdapter.notifyDataSetChanged();
			}
			mAfterBingAdapter.setOnItemClickListener(
				new BaseQuickAdapter.OnItemClickListener() {
				   @Override
				   public void onItemClick(
					   BaseQuickAdapter adapter, View view, int position) {
					CheckBox checkBox = (CheckBox) view.findViewById(R.id.seven_one);
					if (checkBox.isChecked()) {
					   checkBox.setChecked(false);
					} else {
					   mSeven_seven.setText("");
					   checkBox.setChecked(true);
					}
					mAfterBingAdapter.notifyItemChanged(position);
				   }
				});

		   } else if (mDialog != null && mDialog.equals(STYPE_DIALOG)) {//患者列表
			String mDeptType = SPUtils.getString(mContext, PATIENT_TYPE);
			List<BingFindSchedulesBean.PatientInfoVos> patientInfos = (List<BingFindSchedulesBean.PatientInfoVos>) mObject;
			mLayout = R.layout.item_temp_seven_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_outbing_seven_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			findId();
			mSeven_one.setText(titeleList.get(0));
			mSeven_two.setText(titeleList.get(1));
			mSeven_three.setText(titeleList.get(2));
			mSeven_four.setText(titeleList.get(3));
			mSeven_five.setText(titeleList.get(4));
			mSeven_six.setText(titeleList.get(5));
			mSeven_seven.setText(titeleList.get(6));
			if (mTempPatientAdapter != null) {
			   mTempPatientAdapter.notifyDataSetChanged();
			} else {
			   mTempPatientAdapter = new BindTemporaryAdapter(mLayout, patientInfos,
											  mDeptType, mSize);
			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRefreshLayout.setEnableAutoLoadMore(true);
			   mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
			   mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
			   View inflate = LayoutInflater.from(mActivity)
				   .inflate(R.layout.recy_null, null);
			   mTempPatientAdapter.setEmptyView(inflate);
			   mRecyclerview.setAdapter(mTempPatientAdapter);
			   mLinearLayout.addView(mHeadView);
			   mTempPatientAdapter.notifyDataSetChanged();
			}
		   } else if (mDialog != null && mDialog.equals(STYPE_IN)) {//入柜的界面
			List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
			mLayout = R.layout.item_singbox_seven_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_singbox_seven_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
			((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));

			if (mInBoxAllAdapter != null) {
			   mInBoxAllAdapter.notifyDataSetChanged();

			} else {
			   boolean menuDownType = UIUtils.getMenuDownType(UIUtils.getContext(),
											  DOWN_MENU_YICHU);
			   mInBoxAllAdapter = new InBoxAllAdapter(mLayout, mInventoryVos, mOperation,
										menuDownType);
			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			   //                         mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(mContext,
														   LinearLayoutManager.VERTICAL,
														   false));
			   mRefreshLayout.setEnableAutoLoadMore(false);
			   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			   View inflate = LayoutInflater.from(mActivity)
				   .inflate(R.layout.recy_null, null);
			   mInBoxAllAdapter.setEmptyView(inflate);
			   mRecyclerview.setAdapter(mInBoxAllAdapter);
			   mLinearLayout.addView(mHeadView);
			}

		   }
		} else if (mSize == EIGHT) {
		   if (mDialog != null && mDialog.equals(ACT_TYPE_CONFIRM_HAOCAI)) {//耗材领用
			List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
			mLayout = R.layout.item_text_eight_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_text_eight_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
			((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
			((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
			if (mOnBtnGone) {
			   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setVisibility(GONE);
			} else {
			   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setVisibility(VISIBLE);
			}
			if (mRecogHaocaiAdapter != null) {
			   mRecogHaocaiAdapter.notifyDataSetChanged();
			} else {
			   boolean menuDownType = UIUtils.getMenuDownType(UIUtils.getContext(),
											  DOWN_MENU_YICHU);
			   mRecogHaocaiAdapter = new RecogHaocaiAdapter(mLayout, mInventoryVos,
											menuDownType);
			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRefreshLayout.setEnableAutoLoadMore(false);
			   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			   View inflate = LayoutInflater.from(mActivity)
				   .inflate(R.layout.recy_null, null);
			   mRecogHaocaiAdapter.setEmptyView(inflate);
			   mRecyclerview.setAdapter(mRecogHaocaiAdapter);
			   mLinearLayout.removeView(mHeadView);
			   mLinearLayout.addView(mHeadView);
			}
		   } else if (mDialog != null && mDialog.equals(STYPE_LOSS_TYPE)) {
			List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
			//盘亏盘盈
			//			mLayout = R.layout.item_loss_seven_layout;//盘亏的提交的修改界面
			mLayout = R.layout.item_realtime_eight_layout;
			mHeadView = mActivity.getLayoutInflater()
				//				.inflate(R.layout.item_loss_seven_title_layout,//盘亏的提交的修改界面
				.inflate(R.layout.item_realtime_eight_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			findId();
			mSeven_one.setText(titeleList.get(0));
			mSeven_two.setText(titeleList.get(1));
			mSeven_three.setText(titeleList.get(2));
			mSeven_four.setText(titeleList.get(3));
			mSeven_five.setText(titeleList.get(4));
			mSeven_six.setText(titeleList.get(5));
			mSeven_seven.setText(titeleList.get(6));
			((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
			//			mTimelyLossAdapter = new TimelyLossAdapter(mLayout, mInventoryVos);//盘亏的提交
			TimelyProfitAdapter timelyProfitAdapter = new TimelyProfitAdapter(mLayout,
														mInventoryVos);
			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			mRecyclerview.setAdapter(timelyProfitAdapter);
			mLinearLayout.addView(mHeadView);
		   } else if (mDialog != null && mDialog.equals(STYPE_PROFIT_TYPE)) {
			List<InventoryVo> mInventoryVos = (List<InventoryVo>) mObject;
			//盘亏盘盈
			mLayout = R.layout.item_realtime_eight_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_realtime_eight_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			findId();
			mSeven_one.setText(titeleList.get(0));
			mSeven_two.setText(titeleList.get(1));
			mSeven_three.setText(titeleList.get(2));
			mSeven_four.setText(titeleList.get(3));
			mSeven_five.setText(titeleList.get(4));
			mSeven_six.setText(titeleList.get(5));
			mSeven_seven.setText(titeleList.get(6));
			((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
			TimelyProfitAdapter timelyProfitAdapter = new TimelyProfitAdapter(mLayout,
														mInventoryVos);
			mHeadView.setBackgroundResource(R.color.bg_green);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			mRecyclerview.setAdapter(timelyProfitAdapter);
			mLinearLayout.addView(mHeadView);
		   } else if (mDialog != null && mDialog.equals(STYPE_DIALOG)) {//患者列表
			String mDeptType = SPUtils.getString(mContext, PATIENT_TYPE);
			List<BingFindSchedulesBean.PatientInfoVos> patientInfos = (List<BingFindSchedulesBean.PatientInfoVos>) mObject;
			mLayout = R.layout.item_temp_eight_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_temp_eight_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			findId();
			mSeven_one.setText(titeleList.get(0));
			mSeven_two.setText(titeleList.get(1));
			mSeven_three.setText(titeleList.get(2));
			mSeven_four.setText(titeleList.get(3));
			mSeven_five.setText(titeleList.get(4));
			mSeven_six.setText(titeleList.get(5));
			mSeven_seven.setText(titeleList.get(6));
			((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));

			if (mTempPatientAdapter != null) {
			   mTempPatientAdapter.notifyDataSetChanged();
			} else {
			   mTempPatientAdapter = new BindTemporaryAdapter(mLayout, patientInfos,
											  mDeptType, mSize);
			   mHeadView.setBackgroundResource(R.color.bg_green);
			   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			   mRefreshLayout.setEnableAutoLoadMore(true);
			   mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
			   mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
			   View inflate = LayoutInflater.from(mActivity)
				   .inflate(R.layout.recy_null, null);
			   mTempPatientAdapter.setEmptyView(inflate);
			   mRecyclerview.setAdapter(mTempPatientAdapter);
			   mLinearLayout.addView(mHeadView);
			   mTempPatientAdapter.notifyDataSetChanged();
			}
		   }
		}
		break;
	}
   }

   private void findId() {
	mSeven_one = (TextView) mHeadView.findViewById(R.id.seven_one);
	mSeven_two = (TextView) mHeadView.findViewById(R.id.seven_two);
	mSeven_three = (TextView) mHeadView.findViewById(R.id.seven_three);
	mSeven_four = (TextView) mHeadView.findViewById(R.id.seven_four);
	mSeven_five = (TextView) mHeadView.findViewById(R.id.seven_five);
	mSeven_six = (TextView) mHeadView.findViewById(R.id.seven_six);
	mSeven_seven = (TextView) mHeadView.findViewById(R.id.seven_seven);
   }
}
