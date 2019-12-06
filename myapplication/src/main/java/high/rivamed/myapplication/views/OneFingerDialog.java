package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.devices.AllDeviceCallBack;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/2 11:27
 * 描述:        指纹1个按钮的dialog
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OneFingerDialog extends Dialog {

    public OneFingerDialog(Context context) {
        super(context);
    }

    public OneFingerDialog(Context context, int theme) {
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
        public TextView mDialogBtn;
        private TextView mFingerTitle;
        public TextView mFingerTitle2;
        public TextView mFingerTime;
        public TextView mErrorText;
        private ImageView mFingerIcon;
        private ImageView mFingerSuccess;
        public OneFingerDialog mDialog;

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
        public void setSuccess() {
            if (mFingerSuccess != null) {
                mFingerSuccess.setVisibility(View.VISIBLE);
                mFingerSuccess.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.hccz_ic_tx));
                mDialogBtn.setText("确定");
                mErrorText.setVisibility(View.GONE);
            }
        }
        public void setError(String msg) {
            if (mFingerSuccess != null) {
                mFingerSuccess.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.hccz_ic_xx));
                mFingerSuccess.setVisibility(View.VISIBLE);
                mDialogBtn.setText("重试");
//                mErrorText.setText(msg);
                mErrorText.setVisibility(View.VISIBLE);
            }
        }

        public OneFingerDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new OneFingerDialog(mContext, R.style.Dialog);
            mDialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_one_finger_layout, null);
            mDialog.addContentView(layout,
                                   new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));


            mDialogBtn = (TextView) layout.findViewById(R.id.dialog_right);
            mFingerTitle = (TextView) layout.findViewById(R.id.finger_title);
            mFingerTitle2 = (TextView) layout.findViewById(R.id.title2_text);
            mFingerTime = (TextView) layout.findViewById(R.id.finger_time);
            mErrorText = (TextView) layout.findViewById(R.id.error_text);
            mCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
            mFingerIcon = (ImageView) layout.findViewById(R.id.finger_icon);
            mFingerSuccess = (ImageView) layout.findViewById(R.id.iv_finger_success);
            mFingerSuccess.setVisibility(View.INVISIBLE);
            mFingerTitle.setText(mMsgTwo+"绑定");
            mCloss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllDeviceCallBack.getInstance().initCallBack();
                    mDialog.dismiss();
                }
            });
            mDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRightBtn.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            return mDialog;
        }


    }

}
