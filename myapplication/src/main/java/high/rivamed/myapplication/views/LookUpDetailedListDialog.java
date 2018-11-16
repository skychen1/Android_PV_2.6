package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.Arrays;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.LookUpDetailedListDialogAdapter;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.utils.UIUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材请领-确认-查看请领单
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LookUpDetailedListDialog extends Dialog {


    public LookUpDetailedListDialog(Context context) {
        super(context);
    }

    public LookUpDetailedListDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context mContext;
        private OnClickListener mLeftBtn;
        private OnClickListener mRightBtn;

        private TextView mTvPatientNameHint;
        private TextView mTvPatientName;
        private TextView mTvCostType;
        private TextView mTvCostNumber;
        private TextView mRigtht;
        private TextView mLeft;
        private LinearLayout mPublicLl;
        private LinearLayout mListTag;
        private View mHeadView;

        private String patientName="";
        private String cstType="";
        private String cstNumber="";


        private SmartRefreshLayout mSmartRefreshLayout;
        private MaterialHeader mMaterialHeader;

        private RecyclerView mRecyclerView;

        private List<BillStockResultBean.TransReceiveOrderDetailVosBean> mDate;
        private List<String> titeleList = null;

        private boolean mIsShowLeftTopView = true;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setDate(List<BillStockResultBean.TransReceiveOrderDetailVosBean> list) {
            this.mDate = list;
            return this;
        }

        public Builder setLeftListener(OnClickListener listener) {
            this.mLeftBtn = listener;
            return this;
        }

        public Builder setLeftTopViewShow(boolean isShowLeftTopView) {
            this.mIsShowLeftTopView = isShowLeftTopView;
            return this;
        }

        public Builder setRightListener(OnClickListener listener) {
            this.mRightBtn = listener;
            return this;
        }
        public Builder setPatientName(String name) {
            this.patientName = name;
            return this;
        }
        public Builder setCstType(String type) {
            this.cstType = type;
            return this;
        }
        public Builder setCstNumber(String number) {
            this.cstNumber = number;
            return this;
        }


        public LookUpDetailedListDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final LookUpDetailedListDialog dialog = new LookUpDetailedListDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_look_detailedlist, null);
            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.x1536),
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mPublicLl = (LinearLayout) layout.findViewById(R.id.public_ll);
            mLeft = (TextView) layout.findViewById(R.id.dialog_left);
            mRigtht = (TextView) layout.findViewById(R.id.dialog_right);
            mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
            mTvPatientName = (TextView) layout.findViewById(R.id.tv_patient_name);
            mTvPatientNameHint = (TextView) layout.findViewById(R.id.tv_patient_name_hint);
            mTvCostType = (TextView) layout.findViewById(R.id.tv_cost_type);
            mTvCostNumber = (TextView) layout.findViewById(R.id.tv_cost_number);
            mListTag = (LinearLayout) layout.findViewById(R.id.timely_ll);
            mSmartRefreshLayout = (SmartRefreshLayout) layout.findViewById(R.id.refreshLayout);
            mMaterialHeader = (MaterialHeader) layout.findViewById(R.id.header);
            mLeft.setVisibility(View.GONE);
            mTvPatientName.setText(patientName);
            mTvCostType.setText(cstType);
            mTvCostNumber.setText(cstNumber);
            if (mIsShowLeftTopView) {
                mTvPatientName.setVisibility(View.VISIBLE);
                mTvPatientNameHint.setVisibility(View.VISIBLE);
            } else {
                mTvPatientName.setVisibility(View.GONE);
                mTvPatientNameHint.setVisibility(View.GONE);
            }
            initViews();
            mLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLeftBtn.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });
            mRigtht.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            return dialog;
        }

        /**
         * 初始控件
         */
        private void initViews() {

            ViewGroup.LayoutParams lps = mRecyclerView.getLayoutParams();
            if (mDate.size() >= 8) {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y550);
            } else if (mDate.size() < 3) {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y180) * mDate.size() / 2;
            } else {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y80) * mDate.size();
            }
            mRecyclerView.setLayoutParams(lps);
            String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
            titeleList = Arrays.asList(array);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(40, 0, 40, 0);
            mPublicLl.setLayoutParams(lp);
            mHeadView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_outform_dialog_four_title_layout,
                    (ViewGroup) mListTag.getParent(), false);
            ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
            ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
            ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
            ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
            LookUpDetailedListDialogAdapter mPublicAdapter = new LookUpDetailedListDialogAdapter(mDate);
            mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    String six = mPublicAdapter.getItem(position).getReceivedStatus();
//                    if (!six.equals("已领取")) {
//                        DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", null);
//                    } else {
//                        ToastUtils.showShort("此项已领取！");
//                    }

                }
            });
            mHeadView.setBackgroundResource(R.color.bg_green);
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
            mPublicAdapter.setEmptyView(inflate);
            mRecyclerView.setAdapter(mPublicAdapter);

            mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mSmartRefreshLayout.setEnableAutoLoadMore(false);
            mSmartRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
            mSmartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
            mListTag.addView(mHeadView);
        }
    }

}
