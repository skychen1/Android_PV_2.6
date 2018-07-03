package high.rivamed.myapplication.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.bean.Movie;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.FRAGMENT;
import static high.rivamed.myapplication.cont.Constants.STYPE_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
import static high.rivamed.myapplication.cont.Constants.STYPE_TIMELY_FOUR_DETAILS;

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

   public  Activity            mActivity;
   public  Context             mContext;
   public  List<String>        titeleList;
   public  int                 mSize;
   public  List<Movie>         mMovies;
   public  LinearLayout        mLinearLayout;
   public  RecyclerView        mRecyclerview;
   public  RefreshLayout       mRefreshLayout;
   public  int                 mType;
   private int                 mLayout;
   private View                mHeadView;
   private TimelyPublicAdapter mPublicAdapter;
   private static final int FOUR  = 4;
   private static final int FIVE  = 5;
   private static final int SIX   = 6;
   private static final int SEVEN = 7;
   private static final int EIGHT = 8;
   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   private TextView mSeven_seven;
   private String   mDialog;
   public SparseBooleanArray mCheckStates  = new SparseBooleanArray();
   public SparseBooleanArray mCheckStates1 = new SparseBooleanArray();

   public TableTypeView(
	   Context context, Activity activity, List<String> titeleList, int size, Object movies,
	   LinearLayout linearLayout, RecyclerView recyclerview, SmartRefreshLayout refreshLayout,
	   int type) {
	super(context);
	this.mActivity = activity;
	this.mContext = context;
	this.titeleList = titeleList;
	this.mSize = size;
	this.mMovies = (List<Movie>) movies;
	this.mLinearLayout = linearLayout;
	this.mRecyclerview = recyclerview;
	this.mRefreshLayout = refreshLayout;
	this.mType = type;
	initData();
   }

   public TableTypeView(
	   Context context, Activity activity, List<String> titeleList, int size, Object movies,
	   LinearLayout linearLayout, RecyclerView recyclerview, SmartRefreshLayout refreshLayout,
	   int type, String dialog) {
	super(context);
	this.mActivity = activity;
	this.mContext = context;
	this.titeleList = titeleList;
	this.mSize = size;
	this.mMovies = (List<Movie>) movies;
	this.mLinearLayout = linearLayout;
	this.mRecyclerview = recyclerview;
	this.mRefreshLayout = refreshLayout;
	this.mType = type;
	this.mDialog = dialog;
	initData();
   }

   public void clear() {
	mPublicAdapter.clear();
   }

   private void initData() {
	switch (mType) {
	   case ACTIVITY://activity
		if (mSize == FOUR) {
		   if (mDialog != null && mDialog.equals(STYPE_TIMELY_FOUR_DETAILS)) {
			mLayout = R.layout.item_timely_four_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_stockmid_four_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize,STYPE_TIMELY_FOUR_DETAILS);
		   } else {
			mLayout = R.layout.item_stockmid_four_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_stockmid_four_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
		   }
		} else if (mSize == FIVE) {
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
		} else if (mSize == SIX) {
		   if (mDialog != null && mDialog.equals(STYPE_DIALOG)) {
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
			ViewGroup.LayoutParams lp = mRecyclerview.getLayoutParams();
			if (mMovies.size() > 7) {
			   lp.height = 575;
			} else {
			   lp.height = 81 * mMovies.size();
			}
			mRecyclerview.setLayoutParams(lp);
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize, STYPE_DIALOG);

			mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.seven_one);
				if (checkBox.isChecked()) {
				   checkBox.setChecked(false);
				} else {
				   mCheckStates.put(position, true);
				   checkBox.setChecked(true);
				}
			   }
			});
		   } else if (mDialog != null && mDialog.equals(STYPE_IN)) {
			mLayout = R.layout.item_singbox_six_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_singbox_six_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize, STYPE_IN);

		   } else if (mDialog != null && mDialog.equals(STYPE_OUT)) {
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
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize, STYPE_OUT,
									     mCheckStates);
			mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.seven_one);
				if (checkBox.isChecked()) {
				   checkBox.setChecked(false);
				   mMovies.get(position).seven = "0";
				   mCheckStates.delete(position);
				} else {
				   mCheckStates.put(position, true);
				   mMovies.get(position).seven = "1";
				   checkBox.setChecked(true);
				}
				mPublicAdapter.notifyDataSetChanged();
			   }
			});

		   } else if (mDialog != null && mDialog.equals(STYPE_FORM_CONF)) {
			mLayout = R.layout.item_formcon_six_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_formcon_six_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
			((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
			((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
			((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
			((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
			((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize, STYPE_FORM_CONF,
									     mCheckStates);

		   }

		} else if (mSize == SEVEN) {
		   if (mDialog != null && mDialog.equals(STYPE_BING)) {
			mLayout = R.layout.item_outbing_seven_layout;
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
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize, STYPE_BING,
									     mCheckStates1);
			mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.seven_one);
				if (checkBox.isChecked()) {
				   checkBox.setChecked(false);
				   mMovies.get(position).seven = "0";
				   mCheckStates1.delete(position);
				} else {
				   mCheckStates1.put(position, true);
				   mMovies.get(position).seven = "1";
				   checkBox.setChecked(true);
				}
				mPublicAdapter.notifyDataSetChanged();
			   }
			});
		   } else {
			mLayout = R.layout.item_realtime_seven_layout;
			mHeadView = mActivity.getLayoutInflater()
				.inflate(R.layout.item_realtime_seven_title_layout,
					   (ViewGroup) mLinearLayout.getParent(), false);
			findId();
			mSeven_one.setText(titeleList.get(0));
			mSeven_two.setText(titeleList.get(1));
			mSeven_three.setText(titeleList.get(2));
			mSeven_four.setText(titeleList.get(3));
			mSeven_five.setText(titeleList.get(4));
			mSeven_six.setText(titeleList.get(5));
			mSeven_seven.setText(titeleList.get(6));
			mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
			mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				showRvDialog();
			   }
			});
		   }
		} else if (mSize == EIGHT) {
		   mLayout = R.layout.item_act_eight_layout;
		   mHeadView = mActivity.getLayoutInflater()
			   .inflate(R.layout.item_act_eight_title_layout,
					(ViewGroup) mLinearLayout.getParent(), false);
		   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
		   ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
		   mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
		}
		break;
	   case FRAGMENT://fragment
		if (mSize == FIVE) {
		   mLayout = R.layout.item_frg_five_layout;
		   mHeadView = mActivity.getLayoutInflater()
			   .inflate(R.layout.item_frg_five_title_layout,
					(ViewGroup) mLinearLayout.getParent(), false);
		   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		   mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
		} else if (mSize == SIX) {
		   mLayout = R.layout.item_frg_six_layout;
		   mHeadView = mActivity.getLayoutInflater()
			   .inflate(R.layout.item_frg_six_title_layout,
					(ViewGroup) mLinearLayout.getParent(), false);
		   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		   mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
		} else if (mSize == SEVEN) {
		   mLayout = R.layout.item_realtime_seven_layout;
		   mHeadView = mActivity.getLayoutInflater()
			   .inflate(R.layout.item_realtime_seven_title_layout,
					(ViewGroup) mLinearLayout.getParent(), false);
		   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
		   mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
		} else if (mSize == EIGHT) {
		   mLayout = R.layout.item_frg_eight_layout;
		   mHeadView = mActivity.getLayoutInflater()
			   .inflate(R.layout.item_frg_eight_title_layout,
					(ViewGroup) mLinearLayout.getParent(), false);
		   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
		   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
		   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
		   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
		   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
		   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
		   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
		   ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
		   mPublicAdapter = new TimelyPublicAdapter(mLayout, mMovies, mSize);
		}
		break;
	}

	mHeadView.setBackgroundResource(R.color.bg_green);

	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRefreshLayout.setEnableAutoLoadMore(true);
	mRecyclerview.setAdapter(mPublicAdapter);
	mLinearLayout.addView(mHeadView);
   }

   private void showRvDialog() {
	RvDialog.Builder builder = new RvDialog.Builder(mActivity, mContext);
	builder.setMsg("耗材中包含过期耗材，请查看！");
	builder.setLeft("取消", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		dialog.dismiss();
	   }
	});
	builder.setRight("确认", new DialogInterface.OnClickListener() {
	   @Override
	   public void onClick(DialogInterface dialog, int i) {
		dialog.dismiss();
	   }
	});
	builder.create().show();
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

   public TableTypeView(
	   Context context, @Nullable AttributeSet attrs) {
	super(context, attrs);
   }

}
