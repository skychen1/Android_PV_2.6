package high.rivamed.myapplication.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.StockGroupCstAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryGroupVos;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.StringUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/6 0006 15:16
 * 描述:        库存详情--耗材组
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StockGroupCstActivity extends BaseSimpleActivity {

   @BindView(R.id.right_text)
   TextView           mRightText;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   List<InventoryGroupVos> mInventoryGroupVos = new ArrayList<>();
   @Override
   protected int getContentLayoutId() {
	return R.layout.act_group_layout;
   }

   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材组详情");

	loadDate();
   }

   private void loadDate() {
	NetRequest.getInstance()
		.getStockMiddleDetails("", "", -1, mContext, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			mInventoryGroupVos.clear();
			InventoryDto dto = mGson.fromJson(result, InventoryDto.class);
			mInventoryGroupVos = dto.getInventoryGroupVos();
			List<InventoryVo> inventoryVos = dto.getInventoryVos();

			ArrayList<String> strings = new ArrayList<>();
			int SIZE = 0;
			for (InventoryVo vosBean : inventoryVos) {
			   if (vosBean.getCstId() != null) {
				strings.add(vosBean.getCstId());
			   }
			   SIZE += vosBean.getCountStock();

			}
			ArrayList<String> list = new ArrayList<>();
			try {
			   list = StringUtils.removeDuplicteUsers(strings);
			} catch (Exception e) {
			   e.printStackTrace();
			}

			mRightText.setText(
				Html.fromHtml("耗材组：<font color='#262626'><big>" + mInventoryGroupVos.size() +
						  "</big>&emsp</font>耗材种类：<font color='#262626'><big>" + list.size() +
						  "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
						  SIZE + "</big></font>"));
			setAdapterDate(mInventoryGroupVos);
		   }
		});
   }

   private void setAdapterDate(List<InventoryGroupVos> mInventoryGroupVos) {
	StockGroupCstAdapter cstAdapter = new StockGroupCstAdapter(mInventoryGroupVos);
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext, VERTICAL);

	Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.recyclerdivider);
	itemDecoration.setDrawable(drawable);

	mRecyclerview.addItemDecoration(itemDecoration);
	mRefreshLayout.setEnableAutoLoadMore(false);
	mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	cstAdapter.setEmptyView(inflate);
	mRecyclerview.setAdapter(cstAdapter);
	cstAdapter.notifyDataSetChanged();
   }
}
