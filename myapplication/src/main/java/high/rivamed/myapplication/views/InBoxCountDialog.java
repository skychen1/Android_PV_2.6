package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.InBoxCountDialogAdapter;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.StringUtils;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2019/8/7 15:20
 * 描述:        入库统计
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InBoxCountDialog extends Dialog {

   public InBoxCountDialog(Context context) {
	super(context);
   }

   public InBoxCountDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	public               List<InventoryVo>  mInventoryVos;
	private              Context            mContext;
	private              View               mHeadView;
	private              TextView           mNumberTitle;
	private              TextView           mDialogSure;
	private              LinearLayout       mListTag;
	private              SmartRefreshLayout mSmartRefreshLayout;
	private static final int                loadTime = 200;//上下拉加载时间
	private              RecyclerView       mRecyclerView;
	private              int                PAGE     = 1;//当前页数
	private              int                SIZE     = 20;//分页：每页数据
	private              InventoryDto       mDto;
	private              InventoryDto       mPutDto;

	public  InBoxCountDialogAdapter mAdapter;
	private Gson                    mGson;
	public  InBoxCountDialog        mDialog;

	public Builder(Context context, InventoryDto putDto) {
	   this.mContext = context;
	   this.mPutDto = putDto;
	}

	public InBoxCountDialog create() {
	   mGson = new Gson();
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new InBoxCountDialog(mContext, R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_inboxcount_layout, null);
	   mDialog.addContentView(layout, new ViewGroup.LayoutParams(
		   mContext.getResources().getDimensionPixelSize(R.dimen.x1536),
		   ViewGroup.LayoutParams.WRAP_CONTENT));

	   mNumberTitle = layout.findViewById(R.id.timely_number);
	   mDialogSure = layout.findViewById(R.id.dialog_sure);
	   mRecyclerView = layout.findViewById(R.id.recyclerview);
	   mSmartRefreshLayout = layout.findViewById(R.id.refreshLayout);
	   mListTag = layout.findViewById(R.id.timely_ll);
	   initViews();
	   loadData();
	   mSmartRefreshLayout.setEnableAutoLoadMore(true);
	   mSmartRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能

	   mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
		@Override
		public void onRefresh(RefreshLayout refreshLayout) {
		   //刷新
		   loadData();
		}
	   });
	   mDialogSure.setOnClickListener(view -> {
		mDialog.dismiss();
//		if (mStarts != null) {
//		   mStarts.start();
//		}
	   });
	   return mDialog;
	}

	public void loadData() {
	   mInventoryVos.clear();
	   String toJson = mGson.toJson(mPutDto);
	   Log.i("ttadrf", "toJson   " + toJson);
	   NetRequest.getInstance().putInboxCountDate(toJson, mContext, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   mDto = gson.fromJson(result, InventoryDto.class);
		   List<InventoryVo> vos = mDto.getStorageCstCountVo();
		   mInventoryVos.addAll(vos);
		   ArrayList<String> strings = new ArrayList<>();
		   for (InventoryVo vosBean : mInventoryVos) {
			if (vosBean.getCstId() != null) {
			   strings.add(vosBean.getCstId());
			}
		   }
		   ArrayList<String> list = StringUtils.removeDuplicteUsers(strings);
		   mNumberTitle.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + list.size() +
								  "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
								  mDto.getCstSum() + "</big></font>"));
		   mAdapter.notifyDataSetChanged();
		}
	   });
	   mSmartRefreshLayout.finishRefresh(loadTime);
	}

	/**
	 * 初始控件
	 */
	private void initViews() {
	   mNumberTitle.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
							  "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
							  0 + "</big></font>"));
	   mHeadView = ((Activity) mContext).getLayoutInflater()
		   .inflate(R.layout.item_inboxcount_four_title_layout,
				(ViewGroup) mListTag.getParent(), false);
	   mInventoryVos = new ArrayList<>();
	   mAdapter = new InBoxCountDialogAdapter(mInventoryVos);
	   View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
	   mAdapter.setEmptyView(inflate);
	   mRecyclerView.setAdapter(mAdapter);

	   mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	   mSmartRefreshLayout.setEnableAutoLoadMore(false);
	   mSmartRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mListTag.addView(mHeadView);

	}
   }
}
