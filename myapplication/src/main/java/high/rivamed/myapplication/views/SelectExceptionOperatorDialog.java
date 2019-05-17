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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.ExceptionOperatorAdapter;
import high.rivamed.myapplication.bean.ExceptionOperatorBean;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

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
        public interface OnSelectOperatorListener {
            void onSelectOperator(ExceptionOperatorBean operator);

            void onCancel();
        }

        private Context mContext;
        private OnSelectOperatorListener listener;

        private LinearLayout mPublicLl;
        private LinearLayout mListTag;
        private View mHeadView;
        private TextView mLeft;
        private TextView mRight;
        private EditText mEtSearch;


        private SmartRefreshLayout mSmartRefreshLayout;

        private RecyclerView mRecyclerView;

        private List<ExceptionOperatorBean> mDate;
        private ExceptionOperatorAdapter adapter;


        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setDate(List<ExceptionOperatorBean> list) {
            this.mDate = list;
            return this;
        }


        public Builder setOnSelectListener(OnSelectOperatorListener listener) {
            this.listener = listener;
            return this;
        }


        public SelectExceptionOperatorDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final SelectExceptionOperatorDialog dialog = new SelectExceptionOperatorDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_excpbing_operator_layout, null);
            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.x1536),
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mEtSearch = layout.findViewById(R.id.search_et);
            mPublicLl = layout.findViewById(R.id.public_ll);
            mLeft = layout.findViewById(R.id.dialog_left);
            mRight = layout.findViewById(R.id.dialog_right);
            mRecyclerView = layout.findViewById(R.id.recyclerview);
            mListTag = layout.findViewById(R.id.timely_ll);
            mSmartRefreshLayout = layout.findViewById(R.id.refreshLayout);
            initViews();
            mEtSearch.setHint("请输入操作人姓名/ID号");
            mEtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)){
                        adapter.setNewData(mDate);
                    }
                }
            });
            mEtSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String mSearchKey = mEtSearch.getText().toString().trim();
                    UIUtils.hideSoftInput(mContext, mEtSearch);
                    //加载数据
                    if (TextUtils.isEmpty(mSearchKey))
                        return true;
                    loadData(mSearchKey);
                    return true;
                }
                return false;
            });
            mLeft.setOnClickListener(view -> {
                dialog.dismiss();
                if (listener != null)
                    listener.onCancel();
            });
            mRight.setOnClickListener(view -> {
                ExceptionOperatorBean operatorBean = adapter.getSelect();
                if (operatorBean == null) {
                    ToastUtils.showShort("请选择操作人");
                    return;
                }
                dialog.dismiss();
                if (listener != null)
                    listener.onSelectOperator(operatorBean);
            });
            return dialog;
        }

        private void loadData(String mSearchKey) {
            ArrayList<ExceptionOperatorBean> list = new ArrayList<>();
            for (int i = 0; i < mDate.size(); i++) {
                ExceptionOperatorBean bean = mDate.get(i);
                if (bean.getOperator().contains(mSearchKey) ||
                        bean.getId().contains(mSearchKey)) {
                    list.add(bean);
                }
            }

            adapter.setNewData(list);
        }

        /**
         * 初始控件
         */
        private void initViews() {
//            ViewGroup.LayoutParams lps = mRecyclerView.getLayoutParams();
//            lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y550);
//            if (mDate.size() >= 8) {
//            } else if (mDate.size() < 3) {
//                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y180) * mDate.size() / 2;
//            } else {
//                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y80) * mDate.size();
//            }
//            mRecyclerView.setLayoutParams(lps);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            lp.setMargins(40, 0, 40, 0);
//            mPublicLl.setLayoutParams(lp);
            mHeadView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_excpbing_operator_seven_title_layout,
                    (ViewGroup) mListTag.getParent(), false);
            adapter = new ExceptionOperatorAdapter(mDate);
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
