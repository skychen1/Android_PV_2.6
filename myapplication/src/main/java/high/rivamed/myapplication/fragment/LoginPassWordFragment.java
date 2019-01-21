package high.rivamed.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.VersionBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dto.UserLoginDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.Coder;
import high.rivamed.myapplication.utils.FileUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.PackageUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.UpDateDialog;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.CONFIG_013;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.http.NetApi.URL_UPDATE;

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
    private String mDesc;

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
        if (UIUtils.isFastDoubleClick()) {
            return;
        } else {
            if (isvalidate() ) {
               if (mTitleConn){
			getConfigDate();
		   }else {
			if (SPUtils.getString(mContext, SAVE_SEVER_IP)!=null){
			   String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
			   ConfigBean configBean = mGson.fromJson(string, ConfigBean.class);
			   List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
			   loginEnjoin(tCstConfigVos,false);
			}
		   }
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
            NetRequest.getInstance().findThingConfigDate(UIUtils.getContext(), new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                    ConfigBean configBean = mGson.fromJson(result, ConfigBean.class);
                    List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
                    if (tCstConfigVos.size()!=0){
                        getUpDateVer(tCstConfigVos);
                    }else {
                        ToastUtils.showShortToast("请先在管理端对配置项进行设置，后进行登录！");
                    }

                }

            });
        }
    }

    /**
     * 是否禁止使用
     *
     * @param tCstConfigVos
     */
    private void loginEnjoin(List<ConfigBean.ThingConfigVosBean> tCstConfigVos,boolean type) {
        if (getConfigTrue(tCstConfigVos)) {
            LoginActivity.mLoginGone.setVisibility(View.VISIBLE);
            ToastUtils.showShort("正在维护，请到管理端启用");
        } else {
            LoginActivity.mLoginGone.setVisibility(View.GONE);
            if (type){
                loadLogin();
            }else {
                List<AccountVosBean> beans = LitePal.where("accountname = ? ", mLoginName.getText().toString())
                      .find(AccountVosBean.class,true);
		   String password = mLoginPassword.getText().toString();
		   if (beans.size()>0&&Coder.loginCheck(password,beans.get(0).getSalt(),beans.get(0).getPassword())){
			SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE,mGson.toJson(beans.get(0).getMenus()));
			LoginActivity.setMenuDateAndStart( beans.get(0).getMenus(),mGson,_mActivity);
		   }else {
			ToastUtils.showShortToast("登录失败，暂无登录信息！");
		   }
            }
        }
    }

    private boolean getConfigTrue(List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
        for (ConfigBean.ThingConfigVosBean s : tCstConfigVos) {

            if (s.getCode()!=null&&s.getCode().equals(CONFIG_013)) {
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
        accountBean.setPassword(mPassword);
        userLoginDto.setAccount(accountBean);
        userLoginDto.setSystemType(SYSTEMTYPE);
        userLoginDto.setThingId(SPUtils.getString(mContext, THING_CODE));
        LogUtils.i("Login", "THING_CODE   " + mGson.toJson(userLoginDto));
        NetRequest.getInstance().userLogin(mGson.toJson(userLoginDto), _mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i("getAppAccountInfoVo", "result  " + result);

                LoginActivity.loginSpDate(result,mContext,mGson);
            }

            @Override
            public void onError(String result) {
                LogUtils.i("BaseSimpleFragment", "登录失败  " + result);
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUpDateVer(List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
        NetRequest.getInstance().checkVer(this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i("Login", "checkVer:" + result);
                VersionBean versionBean = mGson.fromJson(result, VersionBean.class);
                // 本地版本号
                String localVersion = PackageUtils.getVersionName(mContext);
                // 网络版本
                String netVersion = versionBean.getSystemVersion().getVersion();
                if (netVersion != null) {
                    int i = StringUtils.compareVersion(netVersion, localVersion);
                    if (i == 1) {
                        mDesc = versionBean.getSystemVersion().getDescription();
                        showUpdateDialog(tCstConfigVos);
                    } else {
                        // 不需要更新
                        loginEnjoin(tCstConfigVos,true);
                    }
                } else {
                    loginEnjoin(tCstConfigVos,true);
                }
            }

        });
    }

    /**
     * 展现更新的dialog
     */
    private void showUpdateDialog(List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
        UpDateDialog.Builder builder = new UpDateDialog.Builder(mContext);

        builder.setTitle(UIUtils.getString(R.string.ver_title));
        builder.setMsg(mDesc);
        builder.setLeft(UIUtils.getString(R.string.ver_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                loginEnjoin(tCstConfigVos,true);
                dialog.dismiss();
            }
        });
        builder.setRight(UIUtils.getString(R.string.ver_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                downloadNewVersion(tCstConfigVos);//未下载就开始下载
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void downloadNewVersion(List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
        // 1.显示进度的dialog
        ProgressDialog mDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.setMax(100);
        mDialog.show();

        loadUpDataVersion(mDialog, tCstConfigVos);

    }

    private void loadUpDataVersion(final ProgressDialog mDialog, List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
        OkGo.<File>get(MAIN_URL + URL_UPDATE).tag(this)//
              .params("systemType",SYSTEMTYPE)
                .execute(new FileCallback(FileUtils.getDiskCacheDir(mContext), "RivamedPV.apk") {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(Response<File> response) {
                        mDialog.dismiss();
                        upActivity(response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        mDialog.setProgress((int) (progress.fraction / -1024));
                        super.downloadProgress(progress);

                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.connection_fails);
                        mDialog.dismiss();
                        loginEnjoin(tCstConfigVos,true);
                    }
                });

    }

    private void upActivity(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            LogUtils.i("Login", "apkUri " + Uri.fromFile(file));
        }
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
