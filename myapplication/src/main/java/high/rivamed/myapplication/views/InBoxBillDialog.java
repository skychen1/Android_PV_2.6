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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.InBoxBillDialogAdapter;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.InBoxOrderDialogBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2020/3/30
 * 描述:        入库单据
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InBoxBillDialog extends Dialog {

   public InBoxBillDialog(Context context) {
	super(context);
   }

   public InBoxBillDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	public               List<InBoxOrderDialogBean.OrderVosBean> mInventoryVos;
	private              Context                                 mContext;
	private              View                                    mHeadView;
	private              EditText                                mSearchEt;
	private              TextView                                mSearchBtn;
	private              TextView                                mDialogSure;
	private              TextView               mDialogLeft;
	private              LinearLayout           mListTag;
	private              SmartRefreshLayout     mSmartRefreshLayout;
	private static final int                    loadTime = 200;//上下拉加载时间
	private              RecyclerView           mRecyclerView;
	private              int                    PAGE     = 1;//当前页数
	private              int                    SIZE     = 20;//分页：每页数据
	private              InventoryDto           mDto;
	private              boolean                hasNextPage;//分页：是否有下一页
	public               InBoxBillDialogAdapter mAdapter;
	private              Gson                   mGson;
	public               InBoxBillDialog        mDialog;
	public               TextView               mTimelyRight;
	public               String                    mDeviceId;
	private              CheckBox               mMCheckBox;

	public Builder(Context context,String deviceId) {
	   this.mContext = context;
	   this.mDeviceId = deviceId;
	}

	public InBoxBillDialog create() {
	   mGson = new Gson();
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new InBoxBillDialog(mContext, R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_inboxbill_layout, null);
	   mDialog.addContentView(layout, new ViewGroup.LayoutParams(
		   mContext.getResources().getDimensionPixelSize(R.dimen.x1536),
		   ViewGroup.LayoutParams.WRAP_CONTENT));

	   mSearchEt = layout.findViewById(R.id.search_et);
	   mSearchBtn = layout.findViewById(R.id.search_btn);
	   mDialogSure = layout.findViewById(R.id.dialog_sure);
	   mDialogLeft = layout.findViewById(R.id.dialog_left);
	   mRecyclerView = layout.findViewById(R.id.recyclerview);
	   mSmartRefreshLayout = layout.findViewById(R.id.refreshLayout);
	   mListTag = layout.findViewById(R.id.timely_ll);
	   initViews();
	   PAGE = 1;
	   loadData(mSearchEt.getText().toString().trim());
	   mSmartRefreshLayout.setEnableAutoLoadMore(true);
	   mSmartRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能

	   mMCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		   if (b) {
			for (InBoxOrderDialogBean.OrderVosBean item : mInventoryVos) {
			   if (!item.isSelected()) {
				item.setSelected(b);
			   }
			}
		   } else {
			for (InBoxOrderDialogBean.OrderVosBean item : mInventoryVos) {
			   if (!item.isSelected()) {
				return;
			   }
			}
			for (InBoxOrderDialogBean.OrderVosBean item : mInventoryVos) {
			   if (item.isSelected()) {
				item.setSelected(b);
			   }
			}
		   }
		   mAdapter.notifyDataSetChanged();
		}
	   });
	   mSearchBtn.setOnClickListener(view -> {
		PAGE = 1;
		loadData(mSearchEt.getText().toString().trim());
	   });
	   mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
		@Override
		public void onRefresh(RefreshLayout refreshLayout) {
		   //刷新
		   PAGE = 1;
		   loadData(mSearchEt.getText().toString().trim());
		}
	   });

	   mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
		@Override
		public void onLoadMore(RefreshLayout refreshLayout) {
		   //加载下一页
		   if (hasNextPage) {
			PAGE++;
			loadData(mSearchEt.getText().toString().trim());
		   } else {
			finishLoading();
			ToastUtils.showShortToast("暂无更多数据");
		   }
		}
	   });
	   mDialogSure.setOnClickListener(view -> {
	      if (!UIUtils.isFastDoubleClick(R.id.dialog_sure)){
		   ArrayList<String> order = new ArrayList<>();
		   for (InBoxOrderDialogBean.OrderVosBean vo : mInventoryVos) {
			if (vo.isSelected()) {
			   order.add(vo.getOrderId());
			}
		   }
		   if (order.size()>0){
			EventBusUtils.post(new Event.OrderVosEvent(order, mDeviceId));
			mDialog.dismiss();
		   }else {
			ToastUtils.showShortToast("请选择入库单");
		   }
		}else {
	         ToastUtils.showShortToast("请不要频繁点击");
		}
	   });
	   mDialogLeft.setOnClickListener(view -> {
	      mDialog.dismiss();
	   });
	   return mDialog;
	}

	private void finishLoading() {
	   mSmartRefreshLayout.finishLoadMore(loadTime);
	   mSmartRefreshLayout.finishRefresh(loadTime);
	}

	public void loadData(String text) {
	   if (PAGE == 1) {
		mInventoryVos.clear();
		mAdapter.notifyDataSetChanged();
	   }
	   NetRequest.getInstance().putInboxOrderDate(PAGE, SIZE, text, mContext, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   InBoxOrderDialogBean orderDialogBean = gson.fromJson(result,
											  InBoxOrderDialogBean.class);
		   List<InBoxOrderDialogBean.OrderVosBean> orderVos = orderDialogBean.getOrderVos();
		   mInventoryVos.addAll(orderVos);
		   hasNextPage = (orderVos.size() > SIZE - 1);
		   mAdapter.notifyDataSetChanged();
		}
	   });
	   mAdapter.notifyDataSetChanged();
	   finishLoading();
	}

	/**
	 * 初始控件
	 */
	private void initViews() {

	   mHeadView = ((Activity) mContext).getLayoutInflater()
		   .inflate(R.layout.item_inboxorder_five_title_layout,
				(ViewGroup) mListTag.getParent(), false);
	   mMCheckBox = (CheckBox) mHeadView.findViewById(R.id.seven_one);
	   mInventoryVos = new ArrayList<>();
	   mAdapter = new InBoxBillDialogAdapter(mInventoryVos, mMCheckBox);
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
