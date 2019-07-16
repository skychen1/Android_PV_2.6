package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import high.rivamed.myapplication.adapter.ExceptionOperatorAdapter;
import high.rivamed.myapplication.bean.ExceptionOperatorBean;
import high.rivamed.myapplication.bean.ExceptionRecordBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.ToastUtils;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/15
 * 描述：异常处理-关联操作人
 */
public class SelectExceptionOperatorDialog extends Dialog {

   public SelectExceptionOperatorDialog(Context context) {
	super(context);
   }

   public SelectExceptionOperatorDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private String                                             mSearchText;
	private List<ExceptionOperatorBean.PageModelBean.RowsBean> mRowsOperators;

	public interface OnSelectOperatorListener {

	   void onSelectOperator(ExceptionOperatorBean.PageModelBean.RowsBean operator);

	   void onCancel();
	}

	private Context                  mContext;
	private OnSelectOperatorListener listener;

	private LinearLayout mPublicLl;
	private LinearLayout mListTag;
	private View         mHeadView;
	private TextView     mLeft;
	private TextView     mRight;
	private EditText     mEtSearch;

	private SmartRefreshLayout mSmartRefreshLayout;
	private static final int loadTime    = 200;//上下拉加载时间
	private RecyclerView mRecyclerView;
	private boolean                            hasNextPage;//分页：是否有下一页
	private              int PAGE        = 1;//当前页数
	private              int SIZE        = 20;//分页：每页数据
	private List<ExceptionRecordBean.RowsBean> mRowsDate;
	private ExceptionOperatorAdapter           adapter;

	public Builder(Context context) {
	   this.mContext = context;
	}

	public Builder setDate(List<ExceptionRecordBean.RowsBean> list) {
	   this.mRowsDate = list;
	   return this;
	}

	public Builder setOnSelectListener(OnSelectOperatorListener listener) {
	   this.listener = listener;
	   return this;
	}

	public SelectExceptionOperatorDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final SelectExceptionOperatorDialog dialog = new SelectExceptionOperatorDialog(mContext,
														    R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_excpbing_operator_layout, null);
	   dialog.addContentView(layout, new ViewGroup.LayoutParams(
		   mContext.getResources().getDimensionPixelSize(R.dimen.x1536),
		   ViewGroup.LayoutParams.WRAP_CONTENT));
	   mEtSearch = layout.findViewById(R.id.search_et);
	   mPublicLl = layout.findViewById(R.id.public_ll);
	   mLeft = layout.findViewById(R.id.dialog_left);
	   mRight = layout.findViewById(R.id.dialog_right);
	   mRecyclerView = layout.findViewById(R.id.recyclerview);
	   mListTag = layout.findViewById(R.id.timely_ll);
	   mSmartRefreshLayout = layout.findViewById(R.id.refreshLayout);
	   mEtSearch.setHint("请输入操作人姓名/ID号");

	   initViews();
	   PAGE = 1;
	   loadData();
	   mEtSearch.addTextChangedListener(new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		   mSearchText = s.toString().trim();
		}

		@Override
		public void afterTextChanged(Editable s) {
		   if (!TextUtils.isEmpty(mSearchText)) {
			//加载数据
			PAGE = 1;
			mRowsOperators.clear();
			loadData();
		   }
		}
	   });
	   mSmartRefreshLayout.setEnableAutoLoadMore(true);
	   mSmartRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
	   mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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
	   mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

		@Override
		public void onRefresh(RefreshLayout refreshLayout) {
		   //刷新
		   PAGE = 1;
		   loadData();
		}
	   });
	   mLeft.setOnClickListener(view -> {
		dialog.dismiss();
		if (listener != null) {
		   listener.onCancel();
		}
	   });
	   mRight.setOnClickListener(view -> {
		ExceptionOperatorBean.PageModelBean.RowsBean operatorBean = adapter.getSelect();
		if (operatorBean == null) {
		   ToastUtils.showShort("请选择操作人");
		   return;
		}
		dialog.dismiss();
		if (listener != null) {
		   listener.onSelectOperator(operatorBean);
		}
	   });
	   return dialog;
	}

	private void finishLoading() {
	   mSmartRefreshLayout.finishLoadMore(loadTime);
	   mSmartRefreshLayout.finishRefresh(loadTime);
	}

	private void loadData() {
	   NetRequest.getInstance().getExceptionOperator(PAGE,SIZE,mSearchText, mContext, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   ExceptionOperatorBean operatorBean = gson.fromJson(result,
											ExceptionOperatorBean.class);
		   List<ExceptionOperatorBean.PageModelBean.RowsBean> rows = operatorBean.getPageModel()
			   .getRows();
		   mRowsOperators.addAll(rows);
		   hasNextPage = (rows.size() > SIZE - 1);
		   adapter.notifyDataSetChanged();
		}
	   });
	}

	/**
	 * 初始控件
	 */
	private void initViews() {
	   mHeadView = ((Activity) mContext).getLayoutInflater()
		   .inflate(R.layout.item_excpbing_operator_seven_title_layout,
				(ViewGroup) mListTag.getParent(), false);
	   mRowsOperators = new ArrayList<>();
	   adapter = new ExceptionOperatorAdapter(mRowsOperators);
	   View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
	   adapter.setEmptyView(inflate);
	   mRecyclerView.setAdapter(adapter);

	   mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	   mSmartRefreshLayout.setEnableAutoLoadMore(false);
	   mSmartRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mSmartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mListTag.addView(mHeadView);

	}
   }
}
