package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG2;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        Recyclerview的2个按钮的dialog
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RvDialog2 extends Dialog {

    public static TableTypeView sTableTypeView2;

    public RvDialog2(Context context) {
        super(context);
    }

    public RvDialog2(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        public EditText mSearchEt;
        ImageView mSearchIvDelete;
        FrameLayout mStockSearch;
        LinearLayout mLinearLayout;
        RecyclerView mRecyclerview;
        public SmartRefreshLayout mRefreshLayout;
        TextView mDialogLeft;
        TextView mDialogRight;
        private int mSize; //假数据 举例6个横向格子
        private View mHeadView;
        private int mLayout;
        private TimelyPublicAdapter mPublicAdapter;
        private Context mContext;
        private String mMsgTwo;
        private String mMsgText;
        private String mLeftText;
        private String mRightText;
        private OnClickListener mLeftBtn;
        private OnClickListener mRightBtn;
        private TextView mRigtht;
        private TextView mLeft;
        private int mLeftTextColor = -1;
        private int mRightTextColor;
        private String mType;
        private TextView mDialogMsg;
        private TextView mDialogRed;
        private Activity mActivity;
        private List<BingFindSchedulesBean.PatientInfosBean> patientInfos;
        public RvDialog2 mDialog;

        public Builder(Activity mActivity, Context context, List<BingFindSchedulesBean.PatientInfosBean> patientInfos) {
            this.mContext = context;
            this.mActivity = mActivity;
            this.patientInfos = patientInfos;

        }

        public Builder setTwoMsg(String title) {
            this.mMsgTwo = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.mMsgText = msg;
            return this;
        }

        public Builder setLeft(String left, int color, OnClickListener listener) {
            this.mLeftText = left;
            this.mLeftTextColor = color;
            this.mLeftBtn = listener;
            return this;
        }

        public Builder setLeft(String left, OnClickListener listener) {
            this.mLeftText = left;
            this.mLeftBtn = listener;
            return this;
        }

        public Builder setRight(String left, int color, OnClickListener listener) {
            this.mRightText = left;
            this.mRightTextColor = color;
            this.mRightBtn = listener;
            return this;
        }

        public Builder setRight(String left, OnClickListener listener) {
            this.mRightText = left;
            this.mRightBtn = listener;
            return this;
        }

        public RvDialog2 create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new RvDialog2(mContext, R.style.Dialog);
            mDialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_rv_layout, null);
            mDialog.addContentView(layout,
                    new ViewGroup.LayoutParams(1536,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            mSearchEt = (EditText) layout.findViewById(R.id.search_et);
            mSearchIvDelete = (ImageView) layout.findViewById(R.id.search_iv_delete);
            mStockSearch = (FrameLayout) layout.findViewById(R.id.stock_search);
            mLinearLayout = (LinearLayout) layout.findViewById(R.id.timely_ll);
            mRecyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);
            mRefreshLayout = (SmartRefreshLayout) layout.findViewById(R.id.refreshLayout);
            mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
            mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);

            mSearchEt.setHint("请输入患者姓名、患者ID、手术间查询");
            List<String> titeleList = new ArrayList<String>();
            titeleList.add(0, "选择");
            titeleList.add(1, "患者姓名");
            titeleList.add(2, "患者ID");
            titeleList.add(3, "手术时间");
            titeleList.add(4, "医生");
            titeleList.add(5, "手术间");

            //	   String[] array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
            //	   titeleList = Arrays.asList(array);
            mSize = titeleList.size();

            sTableTypeView2 = new TableTypeView(mContext, mActivity, patientInfos, titeleList, mSize,
                    mLinearLayout, mRecyclerview,
                    mRefreshLayout, ACTIVITY, STYPE_DIALOG2);

            mSearchEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String trim = charSequence.toString().trim();
                    EventBusUtils.postSticky(new Event.EventString(trim));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (mLeftTextColor != -1) {
                mDialogLeft.setTextColor(mLeftTextColor);
                mDialogRight.setTextColor(mRightTextColor);
            }
            mDialogLeft.setText(mLeftText);
            mDialogRight.setText(mRightText);
            mDialogLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLeftBtn.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });
            mDialogRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mRightBtn.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            return mDialog;
        }
    }
}
