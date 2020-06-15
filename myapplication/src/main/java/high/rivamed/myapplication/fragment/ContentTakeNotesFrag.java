package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TakeNotesDetailsActivity;
import high.rivamed.myapplication.adapter.TakeNotesAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.TakeNotesBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.PowerDateUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;

import static android.support.v7.widget.RecyclerView.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/30 15:40
 * 描述:        使用记录
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ContentTakeNotesFrag extends BaseSimpleFragment {

   public String TAG = "ContentTakeNotesFrag";
   @BindView(R.id.search_et)
   EditText     mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView    mSearchIvDelete;
   @BindView(R.id.timely_ll)
   LinearLayout mLinearLayout;

   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   List<String> titeleList = null;
   @BindView(R.id.search_time_start)
   TextView mSearchTimeStart;
   @BindView(R.id.search_time_end)
   TextView mSearchTimeEnd;
   Unbinder unbinder;
   private              View                         mHeadView;
   private              int                          mLayout;
   private              TakeNotesAdapter             mNotesAdapter;
   private              int                          PAGE     = 1;
   private              int                          SIZE     = 20;
   private              String                       mTrim;
   private              List<TakeNotesBean.RowsBean> mRows;
   private              boolean                      hasNextPage;//分页：是否有下一页
   private static final int                          loadTime = 200;//上下拉加载时间

   private String mEndTime   = null;
   private String mStartTime = null;

   public static ContentTakeNotesFrag newInstance() {
	Bundle args = new Bundle();
	ContentTakeNotesFrag fragment = new ContentTakeNotesFrag();
	fragment.setArguments(args);
	return fragment;
   }

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START5")) {
	   PAGE = 1;
	   if (mRows != null) {
		mRows.clear();
	   }
	   mSearchEt.setText("");
	   setTimeTextDatas();
	   mStartTime=null;
	   mEndTime=null;
	   loadDate("",mStartTime,mEndTime);
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	initData();
	initListener();
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.content_takenotes_layout;
   }

   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("领用使用");
	mSearchEt.setHint("请输入患者姓名、患者ID、拼音码");
	mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " +
					SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));

	String[] array = mContext.getResources().getStringArray(R.array.syjl_title_seven);
	titeleList = Arrays.asList(array);

	mLayout = R.layout.item_takenotes_seven_layout;
	mHeadView = LayoutInflater.from(_mActivity)
		.inflate(R.layout.item_takenotes_seven_title_layout,
			   (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.addView(mHeadView);

	setTimeTextDatas();
	mStartTime = null;
	mEndTime = null;
	setDate();
   }

   /**
    * 设置初始化的时间选择器显示
    */
   private void setTimeTextDatas() {
	mSearchTimeStart.setText("");
	mSearchTimeEnd.setText("");
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String format = sdf.format(date);
	mSearchTimeStart.setHint(PowerDateUtils.getTime(2));
	mSearchTimeEnd.setHint(format);
   }

   private void initListener() {
	mSearchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	   }

	   @Override
	   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		mTrim = charSequence.toString().trim();
	   }

	   @Override
	   public void afterTextChanged(Editable editable) {
		PAGE = 1;
		loadDate(mTrim,mStartTime,mEndTime);
	   }
	});

	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		//刷新
		PAGE = 1;
		loadDate(mTrim,mStartTime,mEndTime);
	   }
	});

	mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		//加载下一页
		if (hasNextPage) {
		   PAGE++;
		   loadDate(mTrim,mStartTime,mEndTime);
		} else {
		   finishLoading();
		   ToastUtils.showShortToast("暂无更多数据");
		}
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
		if (mEndTime != null&&!mEndTime.equals("")) {
		   PAGE =1 ;
		   loadDate(mTrim,mStartTime,mEndTime);
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
		if (mStartTime != null&&!mStartTime.equals("")) {
		   PAGE =1 ;
		   loadDate(mTrim,mStartTime,mEndTime);
		}

	   }
	});
   }

   private void finishLoading() {
	mRefreshLayout.finishLoadMore(loadTime);
	mRefreshLayout.finishRefresh(loadTime);
   }


   /**
    * 加载数据
    *
    * @param string
    */
   private void loadDate(String string,String startTime, String endTime) {

	NetRequest.getInstance().getFindPatientDate(string,startTime, endTime,PAGE, SIZE, _mActivity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		TakeNotesBean takeNotesBean = mGson.fromJson(result, TakeNotesBean.class);
		List<TakeNotesBean.RowsBean> rows = takeNotesBean.getRows();if (PAGE == 1) {
		   mRows.clear();
		}
		mRows.addAll(rows);
		hasNextPage = (rows.size() > SIZE - 1);
		mNotesAdapter.notifyDataSetChanged();
	   }
	});
	mNotesAdapter.notifyDataSetChanged();
	finishLoading();
   }

   /**
    * 设置数据
    */
   private void setDate() {
	if (mNotesAdapter != null) {
	   mNotesAdapter.notifyDataSetChanged();
	} else {
	   mRows = new ArrayList<>();
	   mNotesAdapter = new TakeNotesAdapter(mLayout, mRows);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
	   mRefreshLayout.setEnableAutoLoadMore(true);
	   mRecyclerview.setAdapter(mNotesAdapter);
	   View inflate = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
	   mNotesAdapter.setEmptyView(inflate);
	}
	mNotesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		if (mRows == null || null == mRows.get(position) ||
		    null == mRows.get(position).getPatientIndexId()) {
		   ToastUtils.showShortToast("数据异常");
		} else {
		   String patientId = mRows.get(position).getPatientIndexId();
		   String patientName = mRows.get(position).getPatientName();
		   String hisPatientId = mRows.get(position).getHisPatientId();
		   int status = 3;
		   EventBusUtils.postSticky(new Event.EventPatientId(patientId,hisPatientId, status, patientName));
		   mContext.startActivity(new Intent(mContext, TakeNotesDetailsActivity.class));
		}
	   }
	});
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg, R.id.base_tab_tv_outlogin,R.id.search_time_start, R.id.search_time_end})
   public void onViewClicked(View view) {
	super.onViewClicked(view);
	switch (view.getId()) {
	   case R.id.search_time_start:
		DialogUtils.showTimeDialog(mContext, mSearchTimeStart);
		break;
	   case R.id.search_time_end:
		if (mStartTime == null) {
		   ToastUtils.showShortToast("请先选择开始时间");
		} else {
		   DialogUtils.showTimeDialog(mContext, mSearchTimeEnd);
		}
		break;
	}
   }
}
