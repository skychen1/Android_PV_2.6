package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.InBoxOrderOverDialogAdapter;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.OrderVos;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.ToastUtils;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2020/3/30
 * 描述:        入库单据后的最后单子确认
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InBoxOrderOverDialog extends Dialog {

   public InBoxOrderOverDialog(Context context) {
	super(context);
   }

   public InBoxOrderOverDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	public               List<OrderVos>  mOrderVos;
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

	public  InBoxOrderOverDialogAdapter mAdapter;
	private Gson                        mGson;
	public  InBoxOrderOverDialog        mDialog;
	public  TextView                    mTimelyRight;

	public Builder(Context context, List<OrderVos> vos) {
	   this.mContext = context;
	   this.mOrderVos = vos;
	}

	public InBoxOrderOverDialog create() {
	   mGson = new Gson();
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new InBoxOrderOverDialog(mContext, R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_inboxorder_over_layout, null);
	   mDialog.addContentView(layout, new ViewGroup.LayoutParams(
		   mContext.getResources().getDimensionPixelSize(R.dimen.x1536),
		   ViewGroup.LayoutParams.WRAP_CONTENT));

	   mDialogSure = layout.findViewById(R.id.dialog_sure);
	   mRecyclerView = layout.findViewById(R.id.recyclerview);
	   mSmartRefreshLayout = layout.findViewById(R.id.refreshLayout);
	   mListTag = layout.findViewById(R.id.timely_ll);
	   initViews();
	   mSmartRefreshLayout.setEnableAutoLoadMore(false);
	   mSmartRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能

	   mDialogSure.setOnClickListener(view -> {
	     List<String> orderids = new ArrayList<>();
		for (OrderVos orderVo : mOrderVos) {
		   if (orderVo.isSelected()){
			orderids.add(orderVo.getOrderId());
		   }
		}
		putNetData(orderids);
	   });
	   return mDialog;
	}

	/**
	 * 提交数据
	 */
	private void putNetData(List<String> orderids) {
	   if (orderids.size()>0){
		NetRequest.getInstance().putOrderStatus(orderids, this, new BaseResult(){
		   @Override
		   public void onSucceed(String result) {
			JSONObject jsonObject = JSON.parseObject(result);
			String operateSuccess = jsonObject.getString("operateSuccess");
			if (operateSuccess.equals("true")){
			   ToastUtils.showShortToast("入库单操作完成");
			}else {
			   ToastUtils.showShortToast("提交失败，单据默认不结束");
			}
			mDialog.dismiss();
			((Activity)mContext).finish();
		   }
		});
	   }else {
		ToastUtils.showShortToast("入库单操作完成");
		mDialog.dismiss();
		((Activity)mContext).finish();
	   }

	}

	/**
	 * 初始控件
	 */
	private void initViews() {

	   mHeadView = ((Activity) mContext).getLayoutInflater()
		   .inflate(R.layout.item_inboxorderover_five_title_layout,
				(ViewGroup) mListTag.getParent(), false);
	   mAdapter = new InBoxOrderOverDialogAdapter(mOrderVos);
	   View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
	   mAdapter.setEmptyView(inflate);
	   mRecyclerView.setAdapter(mAdapter);

	   mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	   mSmartRefreshLayout.setEnableAutoLoadMore(false);
	   mSmartRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mListTag.addView(mHeadView);
	   mAdapter.notifyDataSetChanged();

	}
   }
}
