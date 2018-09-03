package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.bean.ResetPassBean;
import high.rivamed.myapplication.dto.ResetPasswordDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/2 11:27
 * 描述:        1个按钮的dialog
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OnePassWordDialog extends Dialog {

    private static LoadingDialog.Builder mBuilder;
    private static Gson mGson;

    public OnePassWordDialog(Context context) {
        super(context);
    }

    public OnePassWordDialog(Context context, int theme) {
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
        private int mType;
        private TextView mDialogMsg;
        private TextView mDialogBtn;
        private EditText mPasswordOne;
        private EditText mPasswordTwo;

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

        public OnePassWordDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final OnePassWordDialog dialog = new OnePassWordDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_one_password_layout, null);
            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.x800),
                            ViewGroup.LayoutParams.MATCH_PARENT));


            mPasswordOne = (EditText) layout.findViewById(R.id.password_one);
            mPasswordTwo = (EditText) layout.findViewById(R.id.password_two);
            mDialogBtn = (TextView) layout.findViewById(R.id.dialog_right);
            mCloss = (ImageView) layout.findViewById(R.id.dialog_closs);

            UIUtils.setInputLenWithNoBlank(mPasswordOne, 16);
            UIUtils.setInputLenWithNoBlank(mPasswordTwo, 16);

            mCloss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            mDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String passOne = mPasswordOne.getText().toString().trim();
                    String passTwo = mPasswordTwo.getText().toString().trim();
                    if (TextUtils.isEmpty(passOne)) {
                        ToastUtils.showShort("请输入新密码");
                    } else if (TextUtils.isEmpty(passTwo)) {
                        ToastUtils.showShort("请再次输入新密码");
                    } else if (!passOne.equals(passTwo)) {
                        ToastUtils.showShort("两次密码输入不一致");
                    } else {
                        mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        setNewPass(passOne, dialog);
                    }
                }
            });
            return dialog;
        }

        private void setNewPass(String passOne, OnePassWordDialog dialog) {
            String accountData = SPUtils.getString(mContext, KEY_ACCOUNT_DATA, "");
            if (TextUtils.isEmpty(accountData))
                return;


            LoginResultBean data = new Gson().fromJson(accountData, LoginResultBean.class);

            LoginResultBean.AppAccountInfoVoBean appAccountInfoVo = data.getAppAccountInfoVo();

            String accountName = appAccountInfoVo.getAccountName();

            mBuilder = DialogUtils.showLoading(mContext);
            ResetPasswordDto dto = new ResetPasswordDto();
            ResetPasswordDto.AccountBean bean = new ResetPasswordDto.AccountBean();
            bean.setAccountName(accountName);
            bean.setPassword(passOne);
            dto.setAccount(bean);
            if (mGson == null) {
                mGson = new Gson();
            }
            NetRequest.getInstance().resetPassword(mGson.toJson(dto), mContext, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    mBuilder.mDialog.dismiss();
                    try {
                        ResetPassBean resultBean = mGson.fromJson(result, ResetPassBean.class);
                        if (resultBean.isOperateSuccess()) {
                            ToastUtils.showShort("设置成功");
                            dialog.dismiss();
                        } else {
                            ToastUtils.showShort("设置失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String result) {
                    super.onError(result);
                    mBuilder.mDialog.dismiss();
                    ToastUtils.showShort("设置失败");
                }
            });
        }
    }


}
