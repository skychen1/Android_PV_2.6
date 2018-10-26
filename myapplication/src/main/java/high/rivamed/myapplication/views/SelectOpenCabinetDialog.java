package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.BingDialogOutAdapter;
import high.rivamed.myapplication.adapter.OpenCabinetConfirmDialogAdapter;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材请领选择打开的柜子
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SelectOpenCabinetDialog extends Dialog {


    public SelectOpenCabinetDialog(Context context) {
        super(context);
    }

    public SelectOpenCabinetDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context mContext;
        private String mMsgText;
        private String mLeftText;
        private String mRightText;
        private OnClickListener mLeftBtn;
        private OnClickListener mRightBtn;
        private TextView mRigtht;
        private TextView mLeft;
        private int mLeftTextColor = -1;
        private int mRightTextColor;
        private TextView mDialogMsg;
        private ImageView mDialogCloss;
        private RecyclerView mRecyclerView;

        private List<Movie> mDate;

        //是否启用设置文字等，0：使用默认，1：使用设置
        private int mType;

        //是否启用设置文字等，0：使用默认，1：使用设置
        public Builder(Context context, int type) {
            this.mContext = context;
            this.mType = type;
        }

        public Builder setMsg(String msg) {
            this.mMsgText = msg;
            return this;
        }

        public Builder setDate(List<Movie> list) {
            this.mDate = list;
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

        public Builder setLeftListener(OnClickListener listener) {
            this.mLeftBtn = listener;
            return this;
        }

        public Builder setRightListener(OnClickListener listener) {
            this.mRightBtn = listener;
            return this;
        }


        public SelectOpenCabinetDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final SelectOpenCabinetDialog dialog = new SelectOpenCabinetDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_select_open_cabinet, null);
            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.x1172),
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            mDialogMsg = (TextView) layout.findViewById(R.id.dialog_msg);
            mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
            mLeft = (TextView) layout.findViewById(R.id.dialog_left);
            mRigtht = (TextView) layout.findViewById(R.id.dialog_right);
            mRecyclerView = (RecyclerView) layout.findViewById(R.id.dialog_rv);
            if (mType == 1) {
                mDialogMsg.setText(mMsgText);
                mLeft.setText(mLeftText);
                mRigtht.setText(mRightText);
            }
            initRecyclerView();
            mDialogCloss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
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

        private void initRecyclerView() {
            ViewGroup.LayoutParams lps = mRecyclerView.getLayoutParams();
            Log.e("xb", "Date: " + mDate);
            if (mDate.size() >= 8) {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y550);
            } else if (mDate.size() < 3) {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y140) * mDate.size();
            } else {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y140) * mDate.size() / 2;
            }
            mRecyclerView.setLayoutParams(lps);
            //TODO 10.23修改OutAdapter
            OpenCabinetConfirmDialogAdapter mBingOutAdapter = new OpenCabinetConfirmDialogAdapter(mDate);
//            mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
            mBingOutAdapter.setEmptyView(inflate);
            mRecyclerView.setAdapter(mBingOutAdapter);
            mBingOutAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                }
            });
        }

    }

}
