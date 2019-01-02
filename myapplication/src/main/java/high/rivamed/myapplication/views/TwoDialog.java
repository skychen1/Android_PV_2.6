package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.base.App.mPushFormDateBean;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        2个按钮的dialog
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TwoDialog extends Dialog {


    public TwoDialog(Context context) {
        super(context);
    }

    public TwoDialog(Context context, int theme) {
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
        private int mLeftTextColor = -1;
        private int mRightTextColor;
        private int mType;
        private TextView mDialogMsg;
        private TextView mDialogRed;
        private ImageView mDialogCloss;
        private ImageView mDialogIcon;

        public Builder(Context context, int mType) {
            this.mContext = context;
            this.mType = mType;

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

        public TwoDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final TwoDialog dialog = new TwoDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_two_layout, null);
            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.x850),
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            mDialogIcon = (ImageView) layout.findViewById(R.id.dialog_icon);
            mDialogMsg = (TextView) layout.findViewById(R.id.dialog_msg);
            mDialogRed = (TextView) layout.findViewById(R.id.dialog_red);
            mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
            mLeft = (TextView) layout.findViewById(R.id.dialog_left);
            mRigtht = (TextView) layout.findViewById(R.id.dialog_right);
            mDialogMsg.setText(mMsgText);
            if (mType == 1) {
                mDialogRed.setText(mMsgTwo);
            } else if(mType ==2){
                mDialogCloss.setVisibility(View.GONE);
                mDialogRed.setTextColor(mContext.getResources().getColor(R.color.color_line));
                String str3 = "您还有耗材尚未领用，请前往<font color='#F94641'><big>" + mMsgTwo + "</big></font>领取耗材";
                mDialogRed.setText(Html.fromHtml(str3));
                mLeft.setBackgroundResource(R.drawable.bg_btn_line_pre);
                mLeft.setTextColor(mContext.getResources().getColor(R.color.bg_f));
            }else if (mType ==3){
                mDialogCloss.setVisibility(View.GONE);
                mDialogRed.setText(mMsgTwo);
            }
            if (mLeftTextColor != -1) {
                mLeft.setTextColor(mLeftTextColor);
                mRigtht.setTextColor(mRightTextColor);
            }
            mLeft.setText(mLeftText);
            mRigtht.setText(mRightText);
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
                    if (mMsgTwo.contains("退出登录")) {
                        mPushFormDateBean.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
			     Gson gson = new Gson();
			     gson.toJson(mPushFormDateBean);
			     Log.e("twoDialog",  gson.toJson(mPushFormDateBean));
			     NetRequest.getInstance().submitOrderCstInfo(gson.toJson(mPushFormDateBean),this,  new BaseResult() {
                            @Override
                            public void onSucceed(String result) {
                                Log.e("twoDialog", "result：receiveOrderId上传成功");
                            }

                            @Override
                            public void onError(String result) {
                                Log.e("twoDialog", "Erorr：" + result);
                            }
                        });
                    }

			 mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            return dialog;
        }
    }

}
