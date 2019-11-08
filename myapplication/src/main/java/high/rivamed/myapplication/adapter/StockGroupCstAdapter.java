package high.rivamed.myapplication.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.StockMidTypeActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.vo.InventoryGroupVos;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/6 0006 16:20
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StockGroupCstAdapter extends BaseQuickAdapter<InventoryGroupVos, BaseViewHolder> {
   public StockGroupCstAdapter(@Nullable List<InventoryGroupVos> mInventoryGroupVos) {
	super(R.layout.item_stockgroup_title_layout, mInventoryGroupVos);
//	R.layout.item_stockgroup_body_layout,
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryGroupVos item) {
	helper.setText(R.id.seven_one,item.getCstName());
	helper.setText(R.id.seven_two,item.getCountStock()+"");
	RecyclerView mRecyclerView=(RecyclerView)helper.getView(R.id.recyclerviews);

	StockGroupCstDataAdapter dataAdapter = new StockGroupCstDataAdapter(item.getInventoryVos());
	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
	mRecyclerView.setLayoutManager(linearLayoutManager);
	mRecyclerView.setAdapter(dataAdapter);
	dataAdapter.setOnItemClickListener(new OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		mContext.startActivity(
			new Intent(mContext, StockMidTypeActivity.class).putExtra("expireStatus", -1));
		InventoryVo vosBean = item.getInventoryVos().get(position);
		EventBusUtils.postSticky(new Event.EventStockDetailVo(vosBean));
	   }
	});
   }
}
