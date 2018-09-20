package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.HomeActivity;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.dto.UserLoginDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.WifiUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.CONFIG_013;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      LiangDanMing
 * 创建时间:    2018/7/1 22:07
 * 描述:        用户名登录
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginPassWordFragment extends SimpleFragment {

    @BindView(R.id.login_name)
    EditText mLoginName;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    @BindView(R.id.login_button)
    TextView mLoginButton;
    private String mUserPhone;
    private String mPassword;
    private LoadingDialog.Builder mBuilder;

    @Override
    public int getLayoutId() {
        return R.layout.login_passname_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mLoginName.setText("adminUM");
        mLoginPassword.setText("000000");
    }

    @Override
    public void onBindViewBefore(View view) {

    }

    @OnClick(R.id.login_button)
    public void onViewClicked() {
        if (UIUtils.isFastDoubleClick()) {
            return;
        } else {
            if (isvalidate() && WifiUtils.isWifi(mContext) != 0) {
                getConfigDate();
            } else {
                Toast.makeText(mContext, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 获取配置项
     */
    public void getConfigDate() {
        if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
            NetRequest.getInstance().findThingConfigDate(UIUtils.getContext(), null, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                    ConfigBean configBean = mGson.fromJson(result, ConfigBean.class);
                    List<ConfigBean.TCstConfigVosBean> tCstConfigVos = configBean.getTCstConfigVos();
                    if (getConfigTrue(tCstConfigVos)) {
                        LoginActivity.mLoginGone.setVisibility(View.VISIBLE);
                        ToastUtils.showShort("正在维护，请到管理端启用");
                    }else {
                        LoginActivity.mLoginGone.setVisibility(View.GONE);
                        loadLogin();
                    }
                }
            });
        }
    }

    private boolean getConfigTrue(List<ConfigBean.TCstConfigVosBean> tCstConfigVos) {
        for (ConfigBean.TCstConfigVosBean s:tCstConfigVos){

		if (s.getCode().equals(CONFIG_013)){
                return true;
		}
	  }
        return false;
    }

    private boolean isvalidate() {
        // 获取控件输入的值
        mUserPhone = mLoginName.getText().toString().trim();
        mPassword = mLoginPassword.getText().toString().trim();
        if (StringUtils.isEmpty(mUserPhone)) {
            Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(mPassword)) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * 获取登录
     */
    private void loadLogin() {
//        mBuilder = DialogUtils.showLoading(mContext);
        UserLoginDto userLoginDto = new UserLoginDto();
        UserLoginDto.AccountBean accountBean = new UserLoginDto.AccountBean();
        accountBean.setAccountName(mUserPhone);
        accountBean.setPassword(mPassword);
        userLoginDto.setAccount(accountBean);
        userLoginDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
        LogUtils.i("Login","THING_CODE   "+mGson.toJson(userLoginDto));
        NetRequest.getInstance().userLogin(mGson.toJson(userLoginDto), _mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                try {
                    LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);

                    if (loginResultBean.isOperateSuccess()) {
                        LogUtils.i("BaseSimpleFragment","result  "+result);
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
                        SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, loginResultBean.getAppAccountInfoVo().getUserName());
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, loginResultBean.getAppAccountInfoVo().getAccountId());
//                        SPUtils.getString(UIUtils.getContext(), KEY_USER_ICON,loginResultBean.getAppAccountInfoVo().getHeadIcon());
                        SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX,loginResultBean.getAppAccountInfoVo().getSex());

                        Intent intent = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    }else {
                        Toast.makeText(mContext, loginResultBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
//                mBuilder.mDialog.dismiss();
            }

            @Override
            public void onError(String result) {
                LogUtils.i("BaseSimpleFragment","登录失败  "+result);
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
//                mBuilder.mDialog.dismiss();
            }
        });
    }
}
