package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dto.UserLoginDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.Coder;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LoginUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
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

    @Override
    public int getLayoutId() {
        return R.layout.login_passname_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {

        if (BuildConfig.DEBUG) {
            mLoginName.setText("admin");
            mLoginPassword.setText("000000");
        }
    }

    @Override
    public void onBindViewBefore(View view) {

    }

   @OnClick(R.id.login_button)
   public void onViewClicked() {
	if (UIUtils.isFastDoubleClick(R.id.login_button)) {
	   return;
	} else {
	   if (isvalidate()) {
           //获取配置项并登陆
           LoginUtils.getConfigDate(mContext, (canLogin, canDevice, hasNet) -> {
               if (canLogin) {
                   loginEnjoin(canDevice,hasNet);
               }
           });
	   } else {
		Toast.makeText(mContext, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
	   }
	}
   }

    /**
     * 是否禁止使用
     *
     */
    private void loginEnjoin(boolean canDevice, boolean type) {
        if (canDevice) {//设备未禁用
            if (type) {//有网登录
                loadLogin();
            } else {//离线登录
                String accountName = mLoginName.getText().toString();
                List<AccountVosBean> beans = LitePal.where("accountname = ? ", accountName)
                        .find(AccountVosBean.class);
                LogUtils.i("LoginA", "LitePal   " + mGson.toJson(beans));

                String password = mLoginPassword.getText().toString();
                if (beans.size() > 0 && Coder.loginCheck(password, beans.get(0).getSalt(), beans.get(0).getPassword())) {
                    List<HomeAuthorityMenuBean> fromJson = LoginUtils.setUnNetSPdate(accountName, mGson);
                    LoginUtils.setMenuDateAndStart(fromJson, mGson, _mActivity, null);
                } else {
                    ToastUtils.showShortToast("登录失败，暂无登录信息！");
                }
            }
        } else {
            LoginActivity.mLoginGone.setVisibility(View.VISIBLE);
            ToastUtils.showShort("正在维护，请到管理端启用");
        }
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
        userLoginDto.setSystemType(SYSTEMTYPE);
        userLoginDto.setThingId(SPUtils.getString(mContext, THING_CODE));
        LogUtils.i("Login", "THING_CODE   " + mGson.toJson(userLoginDto));
        NetRequest.getInstance().userLogin(mGson.toJson(userLoginDto), _mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i("getAppAccountInfoVo", "result  " + result);

                LoginUtils.loginSpDate(result,mContext,mGson,null);
            }

            @Override
            public void onError(String result) {
                LogUtils.i("BaseSimpleFragment", "登录失败  " + result);
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
