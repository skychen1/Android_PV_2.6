package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.LogosBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dto.UserLoginDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.Coder;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.FileUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LoginUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.base.App.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.HOME_LOGO;
import static high.rivamed.myapplication.cont.Constants.LOGIN_LOGO;
import static high.rivamed.myapplication.cont.Constants.LOGIN_TYPE_PASSWORD;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
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
    @BindView(R.id.login_help)
    TextView mLoginHelp;
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
            mLoginName.setText("ldm");
            mLoginPassword.setText("000000");
        }
    }

    @Override
    public void onBindViewBefore(View view) {

    }

   @OnClick({R.id.login_button,R.id.login_help})
   public void onViewClicked(View view) {
       switch (view.getId()) {
           case R.id.login_button:
               if (!UIUtils.isFastDoubleClick(R.id.login_button)) {
                   if (isvalidate()) {
                       EventBusUtils.postSticky(new Event.EventLoading(true));
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
               break;
           case R.id.login_help:
               if (!UIUtils.isFastDoubleClick(R.id.login_help)){
                   getLogos();
               }
               break;
       }

   }
    private void getLogos() {
        NetRequest.getInstance().loadLogo(this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogosBean logosBean = mGson.fromJson(result, LogosBean.class);
                String loginPageLogo = logosBean.getHospitalFile().getHospLogo();
                String mainInterfaceLogo = logosBean.getHospitalFile().getMainInterfaceLogo();
                String helpInfo = logosBean.getHospitalFile().getHospital().getLoginHelpInfo();
                DialogUtils.showLoginHelpDialog(mContext, helpInfo);
                boolean saveloginPageLogo = FileUtils.savePicture(loginPageLogo, "login_logo");
                boolean savemainInterfaceLogo = FileUtils.savePicture(mainInterfaceLogo,
                                                                      "home_logo");
                File login_logo = new File(
                      Environment.getExternalStorageDirectory() + "/login_logo" + "/login_logo.png");
                File home_logo = new File(
                      Environment.getExternalStorageDirectory() + "/home_logo" + "/home_logo.png");
                String login_logoPath = login_logo.getPath();
                String home_logoPath = home_logo.getPath();
                SPUtils.putString(mContext, LOGIN_LOGO, login_logoPath);
                SPUtils.putString(mContext, HOME_LOGO, home_logoPath);
                Log.i("eees", "图片loginPageLogo保存+     " + saveloginPageLogo);
                Log.i("eees", "图片mainInterfaceLogo保存+     " + savemainInterfaceLogo);
            }
        });

    }
    /**
     * 是否禁止使用
     *
     */
    private void loginEnjoin(boolean canDevice, boolean type) {
        if (canDevice) {//设备未禁用
            LoginActivity.mLoginGone.setVisibility(View.GONE);
            if (type) {//有网登录
                loadLogin();
            } else {//离线登录
                unNetLoadLogin();
            }
        } else {
            LoginActivity.mLoginGone.setVisibility(View.VISIBLE);
            ToastUtils.showShortToast("正在维护，请到管理端启用");
        }
    }

    /**
     * 离线登录
     */
    private void unNetLoadLogin() {
        String accountName = mLoginName.getText().toString();
        List<AccountVosBean> beans = LitePal.where("accountname = ? ", accountName)
                .find(AccountVosBean.class);
        LogUtils.i("LoginA", "LitePal   " + mGson.toJson(beans));

        String password = mLoginPassword.getText().toString();
        if (beans!=null&&beans.size() > 0 && Coder.loginCheck(password, beans.get(0).getSalt(), beans.get(0).getPassword())) {
            List<HomeAuthorityMenuBean> fromJson = LoginUtils.setUnNetSPdate(accountName, mGson);
            LoginUtils.setMenuDateAndStart(fromJson, mGson, _mActivity, null);
        } else {
            ToastUtils.showShortToast("登录失败，暂无登录信息！");
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
        userLoginDto.setLoginType(LOGIN_TYPE_PASSWORD);
        userLoginDto.setDeptId(SPUtils.getString(mContext, SAVE_DEPT_CODE));
        userLoginDto.setThingId(SPUtils.getString(mContext, THING_CODE));
        LogUtils.i("Login", "THING_CODE   " + mGson.toJson(userLoginDto));
        NetRequest.getInstance().userLogin(mGson.toJson(userLoginDto), _mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i("BaseSimpleFragment", "result  " + result);
                LoginUtils.loginSpDate(result,mContext,mGson,null);
            }

            @Override
            public void onError(String result) {
                EventBusUtils.postSticky(new Event.EventLoading(false));
                LogUtils.i("BaseSimpleFragment", "登录失败  " + result);
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                unNetLoadLogin();
            }
        });
    }
}
