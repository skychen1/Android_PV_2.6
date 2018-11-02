package high.rivamed.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.androidpn.client.ServiceManager;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.ConfigBean;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.bean.VersionBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.FingerLoginDto;
import high.rivamed.myapplication.dto.IdCardLoginDto;
import high.rivamed.myapplication.fragment.LoginPassFragment;
import high.rivamed.myapplication.fragment.LoginPassWordFragment;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.FileUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MyValueFormatter;
import high.rivamed.myapplication.utils.NotificationsUtils;
import high.rivamed.myapplication.utils.PackageUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.CustomViewPager;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.UpDateDialog;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.cont.Constants.CONFIG_013;
import static high.rivamed.myapplication.cont.Constants.CONFIG_017;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_ICON;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.SAVE_CONFIG_STRING;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.http.NetApi.URL_UPDATE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:05
 * 描述:        登录界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginActivity extends SimpleActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_logo)
    ImageView mLoginLogo;
    @BindView(R.id.login_password)
    RadioButton mLoginPassword;
    @BindView(R.id.login_pass)
    RadioButton mLoginPass;
    @BindView(R.id.login_radiogroup)
    RadioGroup mLoginRadiogroup;
    @BindView(R.id.login_viewpager)
    CustomViewPager mLoginViewpager;
    @BindView(R.id.chart1)
    BarChart mChart;
    @BindView(R.id.down_text)
    TextView mDownText;
    @BindView(R.id.left_guo_text)
    TextView mTextGuo;
    @BindView(R.id.left_jin_text)
    TextView mTextJin;
    //   @BindView(R.id.login_gone)
    public static View mLoginGone;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    final static int COUNTS = 5;// 点击次数  2s内点击8次进入注册界面
    final static long DURATION = 2000;// 规定有效时间
    long[] mHits = new long[COUNTS];
    private SQLiteDatabase mDb;
    private LoadingDialog.Builder mBuilder;
    private int mConfigType;
    private String mDesc;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null) {
            MAIN_URL = SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP);
        }
        mLoginGone = findViewById(R.id.login_gone);
        //清空accountID
        mDownText.setText(
                "© 2018 Rivamed  All Rights Reserved  V: " + UIUtils.getVersionName(mContext));
        //	mDownText.setText("Rivamed  智能耗材管理柜:   V " + UIUtils.getVersionName(mContext));
        //-----检测分辨率---------------------------------------
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;

        Log.d("fbl", size.x + " , " + size.y + " , ");

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;
        int widthPixels = dm.widthPixels;
        float density = dm.density;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;
        float smallestWidthDP;
        if (widthDP < heightDP) {
            smallestWidthDP = widthDP;
        } else {
            smallestWidthDP = heightDP;
        }

        LogUtils.i("fbl", smallestWidthDP + "    我是dp单位     " + density);
        //---------------------------------------------------

        //创建数据库表
        mDb = LitePal.getDatabase();
        Log.i("dss", SPUtils.getString(mContext, THING_CODE) + "");

        if (!SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
            LitePal.deleteAll(BoxIdBean.class);
        }
        mFragments.add(new LoginPassWordFragment());//用户名登录
        mFragments.add(new LoginPassFragment());//紧急登录

        intiPushService();

        initData();
        initlistener();
        initCall();

        if (MAIN_URL != null && SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
            getLeftDate();
        }

    }

    /*
    * 初始化推送服务
    * */
    private void intiPushService() {
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            //		   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            //			   // 进入设置系统应用权限界面
            //			   Intent intent = new Intent(Settings.ACTION_SETTINGS);
            //			   startActivity(intent);
            //			   return;
            //		   } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            //			   // 进入设置系统应用权限界面
            //			   Intent intent = new Intent(Settings.ACTION_SETTINGS);
            //			   startActivity(intent);
            //			   return;
            //		   }
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", LoginActivity.this.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);

                localIntent.setClassName("com.android.settings",
                        "com.android.settings.InstalledAppDetails");

                localIntent.putExtra("com.android.settings.ApplicationPkgName",
                        LoginActivity.this.getPackageName());
            }
            startActivity(localIntent);
        }
        // Start the service
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(org.androidpn.demoapp.R.drawable.notification);
        serviceManager.startService();
    }

    @Override
    public void onStart() {
        super.onStart();
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, "");
        SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, "");
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, "");
        SPUtils.putString(UIUtils.getContext(), KEY_USER_ICON, "");
        SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX, "");

        mConfigType = 0;//默认获取
        getConfigDate(mConfigType, null);
    }

    private boolean getConfigTrue(List<ConfigBean.TCstConfigVosBean> tCstConfigVos) {
        for (ConfigBean.TCstConfigVosBean s : tCstConfigVos) {
            if (s.getCode().equals(CONFIG_013)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取配置项
     */
    public void getConfigDate(int configType, String loginType) {
        if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
            NetRequest.getInstance().findThingConfigDate(UIUtils.getContext(), null, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    LogUtils.i(TAG, "findThingConfigDate   " + result);
                    SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                    ConfigBean configBean = mGson.fromJson(result, ConfigBean.class);
                    List<ConfigBean.TCstConfigVosBean> tCstConfigVos = configBean.getTCstConfigVos();
                    if (tCstConfigVos.size() != 0) {
                        getUpDateVer(tCstConfigVos, configType, loginType);
                    }
                    if (UIUtils.getConfigType(mContext, CONFIG_017)) {
                        mLoginPass.setVisibility(View.VISIBLE);
                        mLoginViewpager.setScanScroll(true);
                    } else {
                        mLoginPass.setVisibility(View.GONE);
                        mLoginViewpager.setScanScroll(false);
                    }
                }
            });
        }
    }

    /**
     * 是否禁止使用
     *
     * @param tCstConfigVos
     * @param configType
     * @param loginType
     */
    private void loginEnjoin(
            List<ConfigBean.TCstConfigVosBean> tCstConfigVos, int configType, String loginType) {
        if (getConfigTrue(tCstConfigVos)) {//禁止
            if (configType == 0) {//正常登录密码登录限制
                mLoginGone.setVisibility(View.VISIBLE);
            } else if (configType == 1) {//IC卡登录限制
                mLoginGone.setVisibility(View.VISIBLE);
                ToastUtils.showShort("正在维护，请到管理端启用");
            } else if (configType == 2) {
                //设备是否禁用
                mLoginGone.setVisibility(View.VISIBLE);
                ToastUtils.showShort("正在维护，请到管理端启用");
            }
        } else {
            if (configType == 0) {//正常登录密码登录限制
                mLoginGone.setVisibility(View.GONE);
            } else if (configType == 1) {//IC卡登录限制
                validateLoginIdCard(loginType);
            } else if (configType == 2) {
                validateLoginFinger(loginType);
            }
        }
    }

    private void initCall() {
        DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
            @Override
            public void OnDeviceConnected(DeviceType deviceType, String deviceIndentify) {

            }

            @Override
            public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {

            }

            @Override
            public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {

            }

            @Override
            public void OnIDCard(String deviceId, String idCard) {
                Log.e("fff", idCard);
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    mConfigType = 1;//IC卡
                    getConfigDate(mConfigType, idCard);
                }
            }

            @Override
            public void OnFingerFea(String deviceId, String fingerFea) {
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    mConfigType = 2;//指纹登录
                    getConfigDate(mConfigType, fingerFea.trim().replaceAll("\n", ""));
                }
            }

            @Override
            public void OnFingerRegExcuted(String deviceId, boolean success) {

            }

            @Override
            public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {

            }

            @Override
            public void OnDoorOpened(String deviceIndentify, boolean success) {

            }

            @Override
            public void OnDoorClosed(String deviceIndentify, boolean success) {

            }

            @Override
            public void OnDoorCheckedState(String deviceIndentify, boolean opened) {

            }

            @Override
            public void OnUhfScanRet(
                    boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {

            }

            @Override
            public void OnUhfScanComplete(boolean success, String deviceId) {

            }

            @Override
            public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

            }

            @Override
            public void OnUhfSetPowerRet(String deviceId, boolean success) {

            }

            @Override
            public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

            }
        });

    }

    private void validateLoginIdCard(String idCard) {
        IdCardLoginDto data = new IdCardLoginDto();
        IdCardLoginDto.UserFeatureInfoBean bean = new IdCardLoginDto.UserFeatureInfoBean();
        bean.setData(idCard);
        bean.setType("2");
        data.setUserFeatureInfo(bean);
        data.setThingCode(SPUtils.getString(mContext, THING_CODE));
        LogUtils.i(TAG, "mGson.toJson(data)   " + mGson.toJson(data));
        NetRequest.getInstance().validateLoginIdCard(mGson.toJson(data), this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "validateLoginIdCard  result   " + result);
                try {
                    LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
                    if (loginResultBean.isOperateSuccess()) {
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
                        SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME,
                                loginResultBean.getAppAccountInfoVo().getUserName());
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID,
                                loginResultBean.getAppAccountInfoVo().getAccountId());
                        //			SPUtils.putString(UIUtils.getContext(), KEY_USER_ICON,loginResultBean.getAppAccountInfoVo().getSex());
                        SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX,
                                loginResultBean.getAppAccountInfoVo().getSex());
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    } else {
                        Toast.makeText(mContext, "登录失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void validateLoginFinger(String fingerFea) {
        String thingCode = SPUtils.getString(mContext, THING_CODE);
        FingerLoginDto data = new FingerLoginDto();
        FingerLoginDto.UserFeatureInfoBean bean = new FingerLoginDto.UserFeatureInfoBean();
        bean.setData(fingerFea);
        data.setUserFeatureInfo(bean);
        data.setThingCode(thingCode);
        LogUtils.i("Login", "THING_CODE validateLoginFinger  " + mGson.toJson(data));
        NetRequest.getInstance().validateLoginFinger(mGson.toJson(data), this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "validateLoginFinger   result   " + result);
                try {
                    LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
                    if (loginResultBean.isOperateSuccess()) {
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
                        SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME,
                                loginResultBean.getAppAccountInfoVo().getUserName());
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID,
                                loginResultBean.getAppAccountInfoVo().getAccountId());
                        SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX,
                                loginResultBean.getAppAccountInfoVo().getSex());
                        //			SPUtils.getString(UIUtils.getContext(), KEY_USER_ICON,loginResultBean.getAppAccountInfoVo().getHeadIcon());
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    } else {
                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    @Override
    public void onBindViewBefore() {

    }

    public void getLeftDate() {

        NetRequest.getInstance().materialControl(mContext, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "getLeftDate   result   " + result);
                SocketLeftTopBean leftTopBean = mGson.fromJson(result, SocketLeftTopBean.class);
                if (leftTopBean.getCstExpirationVos() != null &&
                        leftTopBean.getCstExpirationVos().size() > 0) {
                    setLeftDate(leftTopBean);
                    //		   setOperationDate(leftTopBean);//柱状图
                }
            }
        });
    }

    /**
     * 左侧界面显示
     *
     * @param leftTopBean
     */
    private void setLeftDate(SocketLeftTopBean leftTopBean) {
        List<SocketLeftTopBean.CstExpirationVosBean> cstExpirationVos = leftTopBean.getCstExpirationVos();
        int guoNumber = 0;
        int jinNumber = 0;
        for (SocketLeftTopBean.CstExpirationVosBean s : cstExpirationVos) {
            guoNumber += s.getExpireCount();
            jinNumber += s.getNearExpireCount();
        }
        mTextGuo.setText("" + guoNumber);
        mTextJin.setText("" + jinNumber);
    }

    private void initlistener() {
        mLoginLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuousClick(COUNTS, DURATION);
            }
        });
        mLoginGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("正在维护，请到管理端启用");
                mConfigType = 0;
                getConfigDate(mConfigType, null);
            }
        });
    }

    private void continuousClick(int count, long time) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
            Toast.makeText(this, "已进入工程模式", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, TestLoginActivity.class));
        }
    }

    private void initData() {

        if (mLoginRadiogroup.getCheckedRadioButtonId() == R.id.login_password) {
            mLoginViewpager.setCurrentItem(0);
        } else {
            mLoginViewpager.setCurrentItem(1);
        }

        mLoginViewpager.setAdapter(new LoginTitleAdapter(getSupportFragmentManager()));
        mLoginViewpager.addOnPageChangeListener(new PageChangeListener());
        mLoginRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (UIUtils.getConfigType(mContext, CONFIG_017)) {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.login_password:
                            mLoginViewpager.setCurrentItem(0);
                            break;
                        case R.id.login_pass:
                            mLoginViewpager.setCurrentItem(1);
                            break;
                    }
                } else {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.login_password:
                            mLoginViewpager.setCurrentItem(0);
                            break;

                    }
                }

            }
        });

    }

    private void setOperationDate(SocketLeftTopBean leftTopBean) {
        List<SocketLeftTopBean.CstExpirationVosBean> cstExpirationVos = leftTopBean.getCstExpirationVos();
        List<String> xAxisValue = new ArrayList<>();
        if (cstExpirationVos.size() < 6) {
            for (int i = 1; i < 6; i++) {
                if (cstExpirationVos.size() >= i) {
                    xAxisValue.add(i + "号柜");
                } else {
                    xAxisValue.add("");
                }
            }
        } else {
            for (int i = 1; i < cstExpirationVos.size() + 1; i++) {
                xAxisValue.add(i + "号柜");
            }
        }

        //Y轴过期
        List<Float> yAxisValue1 = new ArrayList<>();
        for (int i = 0; i < cstExpirationVos.size(); i++) {
            LogUtils.i(TAG, "(float) cstExpirationVos.get(i).getExpireCount()   " +
                    (float) cstExpirationVos.get(i).getExpireCount());
            yAxisValue1.add((float) cstExpirationVos.get(i).getExpireCount());

        }
        //Y轴近效期
        List<Float> yAxisValue2 = new ArrayList<>();
        for (int i = 0; i < cstExpirationVos.size(); i++) {
            LogUtils.i(TAG, "(float) cstExpirationVos.get(i).getNearExpireCount()   " +
                    (float) cstExpirationVos.get(i).getNearExpireCount());
            yAxisValue2.add((float) cstExpirationVos.get(i).getNearExpireCount());

        }

        setTwoBarChart(mChart, xAxisValue, yAxisValue1, yAxisValue2, "", "");

        mChart.setPinchZoom(true);
        mChart.setTouchEnabled(false);
        mChart.invalidate();
    }

    /**
     * 版本检测
     */
    public void getUpDateVer(
            List<ConfigBean.TCstConfigVosBean> tCstConfigVos, int configType, String loginType) {
        NetRequest.getInstance().checkVer(this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "checkVer:" + result);

                VersionBean versionBean = mGson.fromJson(result, VersionBean.class);
                // 本地版本号
                String localVersion = PackageUtils.getVersionName(mContext);
                // 网络版本
                String netVersion = versionBean.getVersion();
                // 比对LogUtils.i(TAG, "localVersion   " + localVersion);
                LogUtils.i(TAG, "比较   " + StringUtils.compareVersion(netVersion, localVersion));
                int i = StringUtils.compareVersion(netVersion, localVersion);
                if (i == 1) {
                    mDesc = versionBean.getDesc();
                    showUpdateDialog(tCstConfigVos, configType, loginType);
                } else {
                    // 不需要更新
                    loginEnjoin(tCstConfigVos, configType, loginType);
                }
            }

            @Override
            public void onNetFailing(String result) {
                super.onNetFailing(result);
                loginEnjoin(tCstConfigVos, configType, loginType);

            }
        });
    }

    /**
     * 展现更新的dialog
     */
    private void showUpdateDialog(
            List<ConfigBean.TCstConfigVosBean> tCstConfigVos, int configType, String loginType) {
        UpDateDialog.Builder builder = new UpDateDialog.Builder(this);

        builder.setTitle(UIUtils.getString(R.string.ver_title));
        builder.setMsg(mDesc);
        builder.setLeft(UIUtils.getString(R.string.ver_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        loginEnjoin(tCstConfigVos, configType, loginType);
                        dialog.dismiss();
                    }
                });
        builder.setRight(UIUtils.getString(R.string.ver_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                downloadNewVersion(tCstConfigVos, configType, loginType);//未下载就开始下载
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void downloadNewVersion(
            List<ConfigBean.TCstConfigVosBean> tCstConfigVos, int configType, String loginType) {
        // 1.显示进度的dialog
        ProgressDialog mDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.setMax(100);
        mDialog.show();

        loadUpDataVersion(mDialog, tCstConfigVos, configType, loginType);

    }

    private void loadUpDataVersion(
            final ProgressDialog mDialog, List<ConfigBean.TCstConfigVosBean> tCstConfigVos,
            int configType, String loginType) {
        LogUtils.i(TAG, "apkUri " + FileUtils.getDiskCacheDir(mContext));
        OkGo.<File>get(URL_UPDATE).tag(this)//
                .execute(new FileCallback(FileUtils.getDiskCacheDir(mContext),
                        "RivamedPV.apk") {  //文件下载时，需要指定下载的文件目录和文件名
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
                        loginEnjoin(tCstConfigVos, configType, loginType);
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
            LogUtils.i(TAG, "apkUri " + Uri.fromFile(file));
        }
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (UIUtils.getConfigType(mContext, CONFIG_017)) {
                switch (position) {
                    case 0:
                        mLoginRadiogroup.check(R.id.login_password);
                        break;

                    case 1:
                        mLoginRadiogroup.check(R.id.login_pass);
                        break;
                }
            } else {
                switch (position) {
                    case 0:
                        mLoginRadiogroup.check(R.id.login_password);
                        break;
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class LoginTitleAdapter extends FragmentStatePagerAdapter {

        public LoginTitleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void setTwoBarChart(
            BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue1,
            List<Float> yAxisValue2, String bartilte1, String bartitle2) {
        barChart.getDescription().setEnabled(false);//设置描述
        //	barChart.setPinchZoom(true);//设置按比例放缩柱状图
        barChart.setExtraBottomOffset(20);
        barChart.setExtraTopOffset(30);
        barChart.setScaleEnabled(true);

        //x坐标轴设置
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(xAxisValue.size());
        xAxis.setCenterAxisLabels(true);//设置标签居中
        xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue));
        xAxis.setTextSize(
                UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.textsize_14));
        xAxis.setAxisMaximum(1);
        xAxis.setAxisLineColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
        xAxis.setTextColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));

        //y轴设置
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setGridColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
        leftAxis.setAxisLineColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setGranularity(1f); // interval 1
        leftAxis.setTextSize(
                UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.textsize_14));
        leftAxis.setTextColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
        // start at zero

        //	leftAxis.setAxisMaximum(yMax);
        //	//设置坐标轴最大最小值
        Float yMin1 = Collections.min(yAxisValue1);
        Float yMin2 = Collections.min(yAxisValue2);
        Float yMax1 = Collections.max(yAxisValue1);
        Float yMax2 = Collections.max(yAxisValue2);

        Float yMin = Double.valueOf((yMin1 < yMin2 ? yMin1 : yMin2) * 0.1).floatValue();
        Float yMax = Double.valueOf((yMax1 > yMax2 ? yMax1 : yMax2) * 1.1).floatValue();

        leftAxis.setAxisMaximum(yMax);
        leftAxis.setAxisMinimum(yMin);
        barChart.getAxisRight().setEnabled(false);

        //图例设置
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        legend.setForm(Legend.LegendForm.EMPTY);

        //设置柱状图数据
        setTwoBarChartData(barChart, xAxisValue, yAxisValue1, yAxisValue2, bartilte1, bartitle2);

        barChart.animateX(500);//数据显示动画，从左往右依次显示
        barChart.invalidate();
    }

    /**
     * 设置柱状图数据源
     */
    private static void setTwoBarChartData(
            BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue1,
            List<Float> yAxisValue2, String bartilte1, String bartitle2) {
        float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.40f;
        // (0.45 + 0.03) * 2 + 0.04 = 1，即一个间隔为一组，包含两个柱图 -> interval per "group"

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        for (int i = 0; i < yAxisValue1.size(); ++i) {
            entries1.add(new BarEntry(i, yAxisValue1.get(i)));
        }
        for (int x = 0; x < yAxisValue2.size(); ++x) {
            entries2.add(new BarEntry(x, yAxisValue2.get(x)));
        }

        BarDataSet dataset1, dataset2;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            dataset1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            dataset2 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            dataset1.setValues(entries1);
            dataset2.setValues(entries2);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            dataset1 = new BarDataSet(entries1, bartilte1);
            dataset2 = new BarDataSet(entries2, bartitle2);

            dataset2.setColor(Color.rgb(252, 206, 95));
            dataset1.setColor(Color.rgb(244, 110, 86));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.clear();
            dataSets.add(dataset1);
            dataSets.add(dataset2);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(
                    UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.textsize_12));
            data.setValueTextColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
            data.setBarWidth(0.9f);
            data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(
                        float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
                    int value1 = (int) value;
                    String s;
                    if (i % 2 == 0) {
                        s = "过期\n" + String.valueOf(value1);
                    } else {
                        s = "近期\n" + String.valueOf(value1);
                    }
                    return s;
                }
            });

            barChart.setData(data);
        }

        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        barChart.getXAxis()
                .setAxisMaximum(
                        barChart.getBarData().getGroupWidth(groupSpace, barSpace) * xAxisValue.size() +
                                0);
        barChart.groupBars(0, groupSpace, barSpace);

    }
}
