package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TakeNotesDetailsActivity;
import high.rivamed.myapplication.adapter.TakeNotesAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
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
   @BindView(R.id.header)
   MaterialHeader     mHeader;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   List<String> titeleList = null;
   private View mHeadView;
   private int  mLayout;
   private TakeNotesAdapter mNotesAdapter;

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
	if (event.type.equals("START5")) {
	   initData();
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();

   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.content_takenotes_layout;
   }

   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("使用记录");
	mSearchEt.setHint("请输入耗材名称、型号规格查询");
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
	mHeadView.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		mContext.startActivity(new Intent(mContext, TakeNotesDetailsActivity.class));
	   }
	});
	loadDate();

   }

   private void loadDate() {

	NetRequest.getInstance().getFindPatientDate(_mActivity,new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG,"result   "+result);


	   }
	});
	mNotesAdapter = new TakeNotesAdapter(mLayout, null);

	mHeadView.setBackgroundResource(R.color.bg_green);
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
	mRefreshLayout.setEnableAutoLoadMore(true);
	mRecyclerview.setAdapter(mNotesAdapter);
	View inflate = LayoutInflater.from(_mActivity)
		.inflate(R.layout.recy_null, null);
	mNotesAdapter.setEmptyView(inflate);
	mLinearLayout.addView(mHeadView);
	mNotesAdapter.notifyDataSetChanged();
   }

}
