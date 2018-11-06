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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TakeNotesDetailsActivity;
import high.rivamed.myapplication.adapter.TakeNotesAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.TakeNotesBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;

import static android.support.v7.widget.RecyclerView.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_OPERATION_ROOM_NONAME;
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
  public String TAG ="ContentTakeNotesFrag";
   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView          mSearchIvDelete;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;

   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   List<String> titeleList = null;
   private View mHeadView;
   private int  mLayout;
   private TakeNotesAdapter mNotesAdapter;
   private int PAGE = 1;
   private int SIZE = 20;
   private String mTrim;
   private List<TakeNotesBean.RowsBean> mRows=new ArrayList<>();

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
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
      LogUtils.i(TAG,event.type);
	if (event.type.equals("START5")) {
	   initData();
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
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
	mBaseTabTvTitle.setText("使用记录");
	mSearchEt.setHint("请输入患者姓名、患者ID、拼音码");
	if (SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME) != null){
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME)+" - "+SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
	}
	if (SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME)!=null){
	   mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME)+" - "+SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME));
	}

	String[] array = mContext.getResources().getStringArray(R.array.syjl_title_seven);
	titeleList = Arrays.asList(array);

	mLayout = R.layout.item_takenotes_seven_layout;
	mHeadView = LayoutInflater.from(_mActivity).inflate(
		R.layout.item_takenotes_seven_title_layout,
		(ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(
		titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(
		titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(
		titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(
		titeleList.get(6));
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.addView(mHeadView);

	loadDate("");

   }
   private void initListener() {
	mSearchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	   }

	   @Override
	   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		mTrim = charSequence.toString().trim();
		PAGE = 1;
		mRows.clear();
		loadDate(mTrim);
	   }

	   @Override
	   public void afterTextChanged(Editable editable) {

	   }
	});

	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		mRefreshLayout.setNoMoreData(false);
		PAGE = 1;
		mRows.clear();
		loadDate(mTrim);
		mRefreshLayout.finishRefresh();
	   }
	});

	mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		PAGE++;
		loadMoreDate(mTrim);
		mRefreshLayout.finishLoadMore();
	   }
	});
   }

   private void loadMoreDate(String trim) {
	NetRequest.getInstance().getFindPatientDate(trim,PAGE,SIZE,_mActivity,new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG,"result   "+result);
		TakeNotesBean takeNotesBean = mGson.fromJson(result, TakeNotesBean.class);
		List<TakeNotesBean.RowsBean> rows = takeNotesBean.getRows();
		mRows.addAll(rows);
		mNotesAdapter.notifyDataSetChanged();

	   }
	});
   }

   /**
    * 加载数据
    * @param string
    */
   private void loadDate(String string) {

	NetRequest.getInstance().getFindPatientDate(string,PAGE,SIZE,_mActivity,new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG,"result   "+result);
		mRows.clear();
		TakeNotesBean takeNotesBean = mGson.fromJson(result, TakeNotesBean.class);
		List<TakeNotesBean.RowsBean> rows = takeNotesBean.getRows();
		mRows.addAll(rows);
		setDate();
	   }
	});

   }

   /**
    * 设置数据
    */
   private void setDate() {
	if (mNotesAdapter!=null){
	   mNotesAdapter.notifyDataSetChanged();
	}else {
	   mNotesAdapter = new TakeNotesAdapter(mLayout, mRows);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
	   mRefreshLayout.setEnableAutoLoadMore(true);
	   mRecyclerview.setAdapter(mNotesAdapter);
	   View inflate = LayoutInflater.from(_mActivity)
		   .inflate(R.layout.recy_null, null);
	   mNotesAdapter.setEmptyView(inflate);
	}
	mNotesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		String patientId = mRows.get(position).getPatientId();
		int status = 3;
		EventBusUtils.postSticky(new Event.EventPatientId(patientId,status));
		mContext.startActivity(new Intent(mContext, TakeNotesDetailsActivity.class));
	   }
	});
   }


}
