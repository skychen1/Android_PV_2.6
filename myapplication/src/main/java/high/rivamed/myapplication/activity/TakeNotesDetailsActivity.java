package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TakeNotesEpcAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.TakeNotesDetailsBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/31 13:45
 * 描述:        使用记录 耗材详情
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TakeNotesDetailsActivity extends BaseSimpleActivity {

   private static final String TAG = "TakeNotesDetailsActivity";
   @BindView(R.id.tag)
   TextView           mTag;
   @BindView(R.id.take_left_ly)
   RadioButton        mTakeLeftLy;
   @BindView(R.id.take_left_tu)
   RadioButton        mTakeLeftTu;
   @BindView(R.id.take_rg)
   RadioGroup         mTakeRg;
   @BindView(R.id.take_number)
   TextView           mTakeNumber;
   @BindView(R.id.stock_timely_ll)
   RelativeLayout     mStockTimelyLl;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   private List<String> titeleList;
   private View mHeadView;
   private int  mLayout;
   private TakeNotesEpcAdapter mNotesEpcAdapter;
   private String mPatientId;
   private int mStatus;
   private List<TakeNotesDetailsBean.JournalUseRecordDetailVos> mDetailVos =new ArrayList<>();

   /**
    * 接收id和status
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
   public void onPidEvent(Event.EventPatientId event) {
	mPatientId = event.patientId;
	mStatus = event.status;

   }

   /**
    * 获取数据
    * @param patientId
    * @param status
    */
   private void loadDate(String patientId, int status) {
	NetRequest.getInstance().getFindEpcDetails(patientId, status, this, new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		mDetailVos.clear();
		TakeNotesDetailsBean takeNotesDetailsBean = mGson.fromJson(result, TakeNotesDetailsBean.class);
		List<TakeNotesDetailsBean.JournalUseRecordDetailVos> detailVos = takeNotesDetailsBean.getJournalUseRecordDetailVos();
		mDetailVos.addAll(detailVos);
		setAdapterDates();
	   }

	   @Override
	   public void onError(String result) {

		setAdapterDates();
	   }
	});
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.act_take_notes_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mStatus=3;
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材明细");
	mBaseTabTvName.setText(SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
	if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
	    SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nan)
		   .error(R.mipmap.hccz_mrtx_nan)
		   .into(mBaseTabIconRight);
	} else {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nv)
		   .error(R.mipmap.hccz_mrtx_nv)
		   .into(mBaseTabIconRight);
	}
	initDate();
	initListener();
   }

   private void initListener() {
	mTakeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		   case R.id.take_left_ly://最终领用
			mStatus=3;
			loadDate(mPatientId,mStatus);
			break;
		   case R.id.take_left_tu://退回
			mStatus=7;
			loadDate(mPatientId,mStatus);
			break;
		}
	   }
	});
   }

   private void initDate() {

	String[] array = mContext.getResources().getStringArray(R.array.eight_takenotes_arrays);
	titeleList = Arrays.asList(array);

	mLayout = R.layout.item_takeepc_eight_layout;
	mHeadView = LayoutInflater.from(this).inflate(R.layout.item_takeepc_eight_title_layout,
									    (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.addView(mHeadView);
	loadDate(mPatientId,mStatus);

	mBaseTabBack.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		EventBusUtils.postSticky(new Event.EventFrag("START5"));
		finish();
	   }
	});
   }

   /**
    * 设置数据
    */
   private void setAdapterDates() {
      if (mDetailVos!=null){
	   mTakeNumber.setText(
		   Html.fromHtml("耗材数量：<font color='#262626'><big>" + mDetailVos.size() + "</big>&emsp</font>"));
	}else {
	   mTakeNumber.setText(
		   Html.fromHtml("耗材数量：<font color='#262626'><big>" + 0 + "</big>&emsp</font>"));
	}

      if (mNotesEpcAdapter==null){
	   mNotesEpcAdapter = new TakeNotesEpcAdapter(mLayout, mDetailVos);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mRefreshLayout.setEnableAutoLoadMore(false);
	   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mRecyclerview.setAdapter(mNotesEpcAdapter);
	   View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	   mNotesEpcAdapter.setEmptyView(inflate);
	}else {
         mNotesEpcAdapter.notifyDataSetChanged();
	}

   }


}
