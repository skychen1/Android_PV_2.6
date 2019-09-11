package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.HomeActivity;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.bean.VersionBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.dbmodel.ChildrenBean;
import high.rivamed.myapplication.dbmodel.ChildrenBeanX;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.views.UpDateDialog;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.getAppContext;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.cont.Constants.ACCESS_TOKEN;
import static high.rivamed.myapplication.cont.Constants.CONFIG_013;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_s_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.PATIENT_TYPE;
import static high.rivamed.myapplication.cont.Constants.REFRESH_TOKEN;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_DOWN_TYPE_ALL;
import static high.rivamed.myapplication.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.http.NetApi.URL_UPDATE;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/6/12
 * 描述：登录相关操作
 * getConfigTrue=true 设备禁用 显示遮罩
 *  不可登录
 * getConfigTrue=false 设备可用 隐藏遮罩
 *  有网登录
 *  离线登录
 */
public class LoginUtils {
    /**
     * 获取配置项
     */
    public static void getConfigDate(Activity mContext, LoginCallback callback) {
        if (mTitleConn) {
            if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
                NetRequest.getInstance().findThingConfigDate(mContext, new BaseResult() {
                    @Override
                    public void onSucceed(String result) {
                        SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                        ConfigBean configBean = new Gson().fromJson(result, ConfigBean.class);
                        List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
//                    if (tCstConfigVos.size()!=0){
                        getUpDateVer(mContext, tCstConfigVos, callback);
//                    }else {
//                        ToastUtils.showShortToast("请先在管理端对配置项进行设置，后进行登录！");
//                    }
                    }

                    @Override
                    public void onError(String result) {
                        if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null && result.equals("-1")) {
                            String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                            LogUtils.i("LoginA", "string   " + string);
                            ConfigBean configBean = new Gson().fromJson(string, ConfigBean.class);
                            List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
                            callback.onLogin(true, !getConfigTrue(tCstConfigVos), false);
                        } else {
                            callback.onLogin(false, false, false);
                        }
                    }
                });
            } else {
                callback.onLogin(false, false, false);
            }
        }else {
            if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null ) {
                String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                LogUtils.i("LoginA", "string   " + string);
                ConfigBean configBean = new Gson().fromJson(string, ConfigBean.class);
                if (string!=null){
                    List<ConfigBean.ThingConfigVosBean> tCstConfigVos = configBean.getThingConfigVos();
                    callback.onLogin(true, !getConfigTrue(tCstConfigVos), false);
                }else {
                    ToastUtils.showShortToast("请到管理端开启配置项！");
                }

            } else {
                callback.onLogin(false, false, false);
            }
        }
    }

    /**
     * 检测版本更新
     */
    public static void getUpDateVer(Activity mContext, List<ConfigBean.ThingConfigVosBean> tCstConfigVos, LoginCallback callback) {
        if (mTitleConn) {
            NetRequest.getInstance().checkVer(mContext, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    VersionBean versionBean = new Gson().fromJson(result, VersionBean.class);
                    if (versionBean.isOperateSuccess()) {
                        // 本地版本号
                        String localVersion = PackageUtils.getVersionName(UIUtils.getContext());
                        // 网络版本
                        String netVersion = versionBean.getSystemVersion().getVersion();
                        if (netVersion != null && 1 == StringUtils.compareVersion(netVersion, localVersion)) {
                            String mDesc = versionBean.getSystemVersion().getDescription();
                            showUpdateDialog(mContext, tCstConfigVos, mDesc, callback);
                        } else {
                            // 不需要更新
                            loginEnjoin(tCstConfigVos, true, callback);
                        }
                    } else {
                        loginEnjoin(tCstConfigVos, true, callback);
                    }
                }

                @Override
                public void onError(String result) {
                    loginEnjoin(tCstConfigVos, true, callback);
                }
            });
        }else {
            loginEnjoin(tCstConfigVos, false, callback);
        }
    }


    /**
     * 离线状态：1.登录数据保存，2.获取本地缓存菜单数据，3.跳转首页
     */
    @NonNull
    public static List<HomeAuthorityMenuBean> setUnNetSPdate(String accountName, Gson mGson) {
        AccountVosBean beanss = LitePal.where("accountname = ? ", accountName)
                .findFirst(AccountVosBean.class);
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME, beanss.getAccountId());
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME, beanss.getAccountName());
        SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, beanss.getUserName());
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, beanss.getAccountId());
        SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX, beanss.getSex());
        List<HomeAuthorityMenuBean> menusList = beanss.getMenusList(accountName);
        if (menusList.size() > 0 && menusList.get(0).getTitle().equals("耗材操作")) {
            List<ChildrenBeanX> childrenXbean = menusList.get(0).getChildrenXbean(accountName);
            menusList.get(0).setChildren(childrenXbean);
            if (childrenXbean.size() > 0 && childrenXbean.get(0).getTitle().equals("选择操作")) {
                List<ChildrenBean> childrenbean = childrenXbean.get(0).getChildrenbean(accountName);
                childrenXbean.get(0).setChildren(childrenbean);
            }
        }
        List<HomeAuthorityMenuBean> fromJson = new ArrayList<>();
        fromJson.addAll(menusList);
        SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE, mGson.toJson(fromJson));
        return fromJson;
    }


    /**
     * 有网状态:1.登录数据保存，2.获取网络菜单数据，3.跳转首页
     * 重启推送和赋值
     *
     * @param result
     */
    public static void loginSpDate(String result, Activity activity, Gson mGson, MenuCallback callback) {
        try {
            LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
            if (loginResultBean.isOperateSuccess()) {
//		if (mServiceManager != null) {
//		   mServiceManager.stopService();
//		   mServiceManager = null;
//		   SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME, "");
//		}
                SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME,
                        loginResultBean.getAppAccountInfoVo().getAccountId());
                SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
                SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME,
                        loginResultBean.getAppAccountInfoVo().getAccountName());
                SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME,
                        loginResultBean.getAppAccountInfoVo().getUserName());
                SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID,
                        loginResultBean.getAppAccountInfoVo().getAccountId());
                SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX,
                        loginResultBean.getAppAccountInfoVo().getSex());
                SPUtils.putString(UIUtils.getContext(), ACCESS_TOKEN,
                        loginResultBean.getAccessToken().getTokenId());
                SPUtils.putString(UIUtils.getContext(), REFRESH_TOKEN,
                        loginResultBean.getAccessToken().getRefreshToken());
                SPUtils.putString(UIUtils.getContext(), PATIENT_TYPE,
                                  loginResultBean.getPatientType());
                //			SPUtils.getString(UIUtils.getContext(), KEY_USER_ICON,loginResultBean.getAppAccountInfoVo().getHeadIcon());

//		App.initPush(loginResultBean.getAppAccountInfoVo().getAccountId());
                //获取权限菜单数据
                getAuthorityMenu(activity, mGson, callback);

            } else {
                if (callback != null)
                    callback.onMenu(false);
                Toast.makeText(activity, loginResultBean.getMsg(), Toast.LENGTH_SHORT)
                        .show();
                EventBusUtils.postSticky(new Event.EventLoading(false));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            EventBusUtils.postSticky(new Event.EventLoading(false));
        }


    }

    /**
     * 登录成功-->权限菜单检测和首页跳转
     *
     * @param fromJson
     * @param mGson
     * @param activity
     */
    public static void setMenuDateAndStart(List<HomeAuthorityMenuBean> fromJson, Gson mGson, Activity activity, MenuCallback callback) {
        if (fromJson.size() > 0 && null != fromJson.get(0) && null != fromJson.get(0).getChildren() &&
                fromJson.get(0).getChildren().get(0).getTitle().equals("选择操作")) {
            SPUtils.putBoolean(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE_ALL, true);
            List<ChildrenBean> children = fromJson.get(0).getChildren().get(0).getChildren();
            SPUtils.putString(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE, mGson.toJson(children));
        } else {
            SPUtils.putBoolean(UIUtils.getContext(), SAVE_MENU_DOWN_TYPE_ALL, false);
        }
        if (fromJson.size() > 0) {
            MusicPlayer.getInstance().play(MusicPlayer.Type.LOGIN_SUC);
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            activity.startActivity(intent);
            if (callback != null){
                callback.onMenu(true);
            }else {
                activity.finish();
            }
        } else {
            if (callback != null)
                callback.onMenu(false);
            Toast.makeText(UIUtils.getContext(), "此账号未配置权限，请联系管理员", Toast.LENGTH_SHORT).show();
        }
        EventBusUtils.postSticky(new Event.EventLoading(false));
    }


    /**
     * 获取权限菜单
     */
    private static void getAuthorityMenu(Activity activity, Gson mGson, MenuCallback callback) {
        NetRequest.getInstance().getAuthorityMenu(getAppContext(), new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i("Login", "getAuthorityMenu  " + result);
                SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE, result);
                List<HomeAuthorityMenuBean> fromJson = mGson.fromJson(result,
                        new TypeToken<List<HomeAuthorityMenuBean>>() {}.getType());
                //检测权限菜单和首页跳转
                setMenuDateAndStart(fromJson, mGson, activity, callback);
            }

            @Override
            public void onError(String result) {
                EventBusUtils.postSticky(new Event.EventLoading(false));
                if (callback != null)
                    callback.onMenu(false);
            }
        });
    }

    /**
     * 登录检测回调：检测1.是否能够登录 2.设备是否禁用 3.登录模式：有网还是离线
     */
    private static void loginEnjoin(List<ConfigBean.ThingConfigVosBean> tCstConfigVos, boolean hasNet, LoginCallback callback) {
        callback.onLogin(true, !getConfigTrue(tCstConfigVos), hasNet);
    }

    /**
     * 检测设备是否禁用
     */
    public static boolean getConfigTrue(List<ConfigBean.ThingConfigVosBean> tCstConfigVos) {
        if (tCstConfigVos==null||tCstConfigVos.size() == 0)
            return false;
        Iterator<ConfigBean.ThingConfigVosBean> iterator = tCstConfigVos.iterator();
        while (iterator.hasNext()){
            ConfigBean.ThingConfigVosBean next = iterator.next();
            if (next.getCode().equals(CONFIG_013)) {
                return true;
            }
        }
//        for (ConfigBean.ThingConfigVosBean s : tCstConfigVos) {
//            if (s.getCode().equals(CONFIG_013)) {
//                return true;
//            }
//        }
        return false;
    }

    /**
     * 检测epc过滤的问题
     * @param tCstConfigVos
     * @return
     */
    public static ConfigBean.ThingConfigVosBean getEpcFilte(List<ConfigBean.ThingConfigVosBean> tCstConfigVos,String CONFIG_x) {
        if (tCstConfigVos==null||tCstConfigVos.size() == 0)
            return null;
        Iterator<ConfigBean.ThingConfigVosBean> iterator = tCstConfigVos.iterator();
        while (iterator.hasNext()){
            ConfigBean.ThingConfigVosBean next = iterator.next();
            if (next.getCode().equals(CONFIG_x)) {
                return next;
            }
        }
//        for (ConfigBean.ThingConfigVosBean s : tCstConfigVos) {
//            if (s.getCode().equals(CONFIG_x)) {
//                return s;
//            }
//        }
        return null;
    }

    /**
     * apk更新的dialog
     */
    private static void showUpdateDialog(Activity mContext, List<ConfigBean.ThingConfigVosBean> tCstConfigVos, String mDesc, LoginCallback callback) {
        UpDateDialog.Builder builder = new UpDateDialog.Builder(mContext);
        builder.setTitle(UIUtils.getString(R.string.ver_title));
        builder.setMsg(mDesc);
        builder.setLeft(UIUtils.getString(R.string.ver_cancel), (dialog, i) -> {
            //取消
            loginEnjoin(tCstConfigVos, true, callback);
            dialog.dismiss();
        });
        builder.setRight(UIUtils.getString(R.string.ver_ok), (dialog, i) -> {
            //更新
            downloadNewVersion(mContext, tCstConfigVos, callback);//未下载就开始下载
            dialog.dismiss();
        });

        builder.create().show();
    }

    /**
     * apk下载进度条
     */
    private static void downloadNewVersion(Activity mContext, List<ConfigBean.ThingConfigVosBean> tCstConfigVos, LoginCallback callback) {
        // 1.显示进度的dialog
        ProgressDialog mDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.setMax(100);
        mDialog.show();

        loadUpDataVersion(mContext, mDialog, tCstConfigVos, callback);
    }

    /**
     * apk下载
     */
    private static void loadUpDataVersion(Activity mContext, final ProgressDialog mDialog, List<ConfigBean.ThingConfigVosBean> tCstConfigVos, LoginCallback callback) {
        OkGo.<File>get(MAIN_URL + URL_UPDATE).tag(mContext)//
                .params("systemType", SYSTEMTYPE)
                .execute(new FileCallback(FileUtils.getDiskCacheDir(mContext), "RivamedPV.apk") {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(Response<File> response) {
                        mDialog.dismiss();
                        upActivity(mContext, response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        mDialog.setProgress((int) (-progress.fraction *100));

                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.connection_fails);
                        mDialog.dismiss();
                        loginEnjoin(tCstConfigVos, true, callback);
                    }
                });

    }

    /**
     * apk安装
     */
    private static void upActivity(Activity mContext, File file) {
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
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 登录检测回调
     */
    public interface LoginCallback {
        /**
         * @param canLogin  是否能登录：设备未注册激活或离线时没有本地数据，则无法登录
         * @param canDevice 设备是否禁用 通过配置项判断
         * @param hasNet    登录模式：有网还是离线
         */
        void onLogin(boolean canLogin, boolean canDevice, boolean hasNet);
    }

    /**
     * 权限菜单检测回调
     */
    public interface MenuCallback {
        /**
         * 这里是已经登录成功了之后的操作
         *
         * @param hasMenu 是否有权限菜单：有权限菜单，则跳转至首页；否则，提示并重新走登录流程
         */
        void onMenu(boolean hasMenu);
    }
}
