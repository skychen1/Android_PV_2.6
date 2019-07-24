package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import high.rivamed.myapplication.adapter.ToBePutInStorageAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.ToBePutInStorageBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;

import static android.support.v7.widget.RecyclerView.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/22 15:54
 * 描述:        待入库耗材
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class LoginToBePutInStorageActivity extends BaseSimpleActivity {

   public InventoryDto mDto;
   @BindView(R.id.timely_name)
   TextView           mTimelyName;
   @BindView(R.id.timely_number)
   TextView           mTimelyNumber;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   private List<String> titeleList;
   private View         mHeadView;
   private int          mLayout;
   public  int          mSize;
   private int          PAGE = 1;//当前页数
   private int          SIZE = 20;//分页：每页数据
   List<ToBePutInStorageBean.PageModelBean.RowsBean> mAllRows = new ArrayList<>();
   private              boolean                 hasNextPage;//分页：是否有下一页
   private static final int                     loadTime = 200;//上下拉加载时间
   private              ToBePutInStorageAdapter mStorageAdapter;
   private              int                     mTotal;


   /**
    * 盘点详情、盘亏、盘盈
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onTimelyEvent(Event.timelyDate event) {
	String s = event.type;
	mDto = event.mInventoryDto;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_midtype_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mTimelyName.setVisibility(View.VISIBLE);
	mBaseTabTvName.setVisibility(View.GONE);
	mBaseTabBtnMsg.setVisibility(View.GONE);
	mBaseTabIconRight.setVisibility(View.GONE);
	mBaseTabOutLogin.setVisibility(View.GONE);
	mBaseTabTvTitle.setText("二级库待入库耗材");

	initData();
	initListener();
   }

   private void initListener() {
	mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		//加载下一页
		Log.i("XXXF", "onLoadMore");
		if (hasNextPage) {
		   PAGE++;
		   loadData();
		} else {
		   finishLoading();
		   ToastUtils.showShort("暂无更多数据");
		}
	   }
	});
	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		//刷新
		PAGE = 1;
		mAllRows.clear();
		loadData();
	   }
	});

   }

   private void finishLoading() {
	mRefreshLayout.finishLoadMore(loadTime);
	mRefreshLayout.finishRefresh(loadTime);
   }

   /**
    * 数据获取
    */
   private void loadData() {
	NetRequest.getInstance().getLoginCstMsg(PAGE, SIZE, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i("FDADDD", "result   " + result);
		ToBePutInStorageBean toBePutInStorageBean = mGson.fromJson(result,
											     ToBePutInStorageBean.class);
		List<ToBePutInStorageBean.PageModelBean.RowsBean> rows = toBePutInStorageBean.getPageModel()
			.getRows();
		mTotal = toBePutInStorageBean.getPageModel().getTotal();
		mAllRows.addAll(rows);
		hasNextPage = (rows.size() > SIZE - 1);
		mStorageAdapter.notifyDataSetChanged();
		if (mAllRows != null) {
		   mTimelyNumber.setText(Html.fromHtml(
			   "本次待入库数量：<font color='#262626'><big>" + mTotal + "</big>&emsp</font>"));
		}
	   }
	});
	mStorageAdapter.notifyDataSetChanged();
	finishLoading();
   }

   private void initData() {
	mTimelyName.setText("当前库房：" + SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
	String[] array = mContext.getResources().getStringArray(R.array.six_tobeputin_arrays);
	titeleList = Arrays.asList(array);

	mHeadView = LayoutInflater.from(this)
		.inflate(R.layout.item_tobeputin_six_title_layout,
			   (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.addView(mHeadView);
	setAdapterDates();
	loadData();

   }

   /**
    * 设置数据
    */
   private void setAdapterDates() {
	mTimelyNumber.setText(
		Html.fromHtml("本次待入库数量：<font color='#262626'><big>" + 0 + "</big>&emsp</font>"));
	if (mStorageAdapter == null) {
	   mStorageAdapter = new ToBePutInStorageAdapter(mAllRows);
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mRefreshLayout.setEnableAutoLoadMore(true);
	   mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
	   mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
	   mRecyclerview.setAdapter(mStorageAdapter);
	   View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	   mStorageAdapter.setEmptyView(inflate);
	} else {
	   mStorageAdapter.notifyDataSetChanged();
	}
   }
}
