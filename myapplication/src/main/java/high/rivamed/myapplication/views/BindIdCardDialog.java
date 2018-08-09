package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import high.rivamed.myapplication.R;

/**
 * 绑定腕带dialog
 */

public class BindIdCardDialog extends Dialog {

    private static LinearLayout mLlSuccess;
    private static RelativeLayout mRlLoading;
    private static TextView mTvIdNum;
    private static TextView mTvError;

    public BindIdCardDialog(Context context) {
        super(context);
    }

    public BindIdCardDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {


        private Context mContext;
        private String mMsgTwo;
        private String mMsgText;
        private String mLeftText;
        private String mRightText;
        private OnClickListener mLeftBtn;
        private OnClickListener mRightBtn;
        private TextView mRigtht;
        private TextView mLeft;
        private ImageView mCloss;
        private int mLeftTextColor = -1;
        private int mRightTextColor;
        private TextView mDialogBtn;

        public Builder(Context context) {
            this.mContext = context;

        }

        public Builder setTwoMsg(String title) {
            this.mMsgTwo = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.mMsgText = msg;
            return this;
        }

        public Builder setIdNum(String id) {
            mTvIdNum.setText(id);
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

        public void setSuccess(String id) {
            if (mLlSuccess != null) {
                mLlSuccess.setVisibility(View.VISIBLE);
                mRlLoading.setVisibility(View.INVISIBLE);
                mTvIdNum.setText(id);
            }
        }

        public BindIdCardDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final BindIdCardDialog dialog = new BindIdCardDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_bind_idcard_layout, null);
            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));


            mDialogBtn = (TextView) layout.findViewById(R.id.dialog_right);
            mCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
            mRlLoading = (RelativeLayout) layout.findViewById(R.id.rl_bind_loading);
            mLlSuccess = (LinearLayout) layout.findViewById(R.id.ll_bind_success);
            mTvIdNum = (TextView)layout.findViewById(R.id.tv_id_number);
            mTvError = (TextView)layout.findViewById(R.id.tv_error_layout);
            mLlSuccess.setVisibility(View.INVISIBLE);
            mRlLoading.setVisibility(View.VISIBLE);
            mTvError.setText("正在读取...");
            mCloss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            mDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            return dialog;
        }

    }

}
