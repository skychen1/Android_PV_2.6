package high.rivamed.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
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
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.litepal.LitePal;

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
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.TestDevicesActivity;
import high.rivamed.myapplication.dto.FingerLoginDto;
import high.rivamed.myapplication.fragment.LoginPassFragment;
import high.rivamed.myapplication.fragment.LoginPassWordFragment;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.MyValueFormatter;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

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

    @BindView(R.id.login_logo)
    ImageView mLoginLogo;
    @BindView(R.id.login_password)
    RadioButton mLoginPassword;
    @BindView(R.id.login_pass)
    RadioButton mLoginPass;
    @BindView(R.id.login_radiogroup)
    RadioGroup mLoginRadiogroup;
    @BindView(R.id.login_viewpager)
    ViewPager mLoginViewpager;
    @BindView(R.id.chart1)
    BarChart mChart;
    @BindView(R.id.down_text)
    TextView mDownText;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    final static int COUNTS = 5;// 点击次数  2s内点击8次进入注册界面
    final static long DURATION = 2000;// 规定有效时间
    long[] mHits = new long[COUNTS];
    private SQLiteDatabase mDb;
    private LoadingDialog.Builder mBuilder;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        //清空accountID
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, "");
        //-----检测分辨率---------------------------------------
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;

        Log.d("fbl", size.x + " , " + size.y + " , ");

        //---------------------------------------------------

        OkGo.<String>post("https://192.168.10.231:8443/cas/v1/tickets").tag(this).
                params("username", "adminUM").
                params("password", "000000")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("fff", "response.body()    " + response.body());
                        Log.i("fff", "response.code()    " + response.code());
                        Log.i("fff", "response.message()    " + response.message());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.i("fff", "response.body()    " + response.body());
                        Log.i("fff", "response.code()    " + response.code());
                        Log.i("fff", "response.message()    " + response.message());
                    }
                });


        //创建数据库表
        mDb = LitePal.getDatabase();
        Log.i("dss", SPUtils.getString(mContext, THING_CODE) + "");

        if (!SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
            LitePal.deleteAll(BoxIdBean.class);
        }

        //	List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?" , "402882a064da53150164da71e5100011", "Reader罗丹贝尔")
        //		.find(BoxIdBean.class);
        //
        //	for (int i=0;i<boxIdBeans.size();i++){
        //	   Log.i("dss", boxIdBeans.get(i).getDevice_id() + "");
        //	}
        //	loadBoxDate();
        //	BoxIdBean boxIdBean = new BoxIdBean();
        //	boxIdBean.setName("我是谁");
        //	boxIdBean.setBox_id("22");
        //	boxIdBean.setDevice_id("22xxx");
        //	boxIdBean.save();
        //	BoxIdBean boxIdBean2 = new BoxIdBean();
        //	boxIdBean2.setName("dddddddd");
        //	boxIdBean2.setBox_id("2afafafaf2");
        //	boxIdBean2.setDevice_id("wwwwwwwwwwwwwwwxxx");
        //	boxIdBean2.save();
        //	List<BoxIdBean> boxIdBeans = LitePal.where("device_id=?","wwwwwwwwwwwwwwwxxx")
        //		.find(BoxIdBean.class);
        //	//	BoxIdBean idBean = LitePal.find(BoxIdBean.class, 1);
        //	for (int i=0;i<boxIdBeans.size();i++){
        //	   Log.i("dss", boxIdBeans.get(i).getBox_id() + "");
        //	}
        mFragments.add(new LoginPassWordFragment());
        mFragments.add(new LoginPassFragment());
        initData();
        initlistener();
        initCall();
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

            }

            @Override
            public void OnFingerFea(String deviceId, String fingerFea) {
                Log.e("fff", fingerFea);
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    validateLoginFinger(fingerFea);
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
            public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {

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

    private void validateLoginFinger(String fingerFea) {
        Log.e("fff", "validateLoginFinger........");
        String thingCode = SPUtils.getString(mContext, THING_CODE);
        FingerLoginDto data = new FingerLoginDto();
        FingerLoginDto.UserFeatureInfoBean bean = new FingerLoginDto.UserFeatureInfoBean();
        bean.setData(fingerFea);
        data.setUserFeatureInfo(bean);
        data.setThingCode(thingCode);
        Log.e("fff", "#############################" + mGson.toJson(data));
        NetRequest.getInstance().validateLoginFinger(mGson.toJson(data), this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                Log.e("fff", "#############################result:" + result);
                try {
                    LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
                    if (loginResultBean.isOperateSuccess()) {
                        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                super.onError(result);
                ToastUtils.showShort("登录失败" + result);
            }
        });

    }
    //获取box个数和数据
    //   private void loadBoxDate() {
    //	NetRequest.getInstance().loadBoxSize( mContext,new BaseResult() {
    //	   @Override
    //	   public void onSucceed(String result) {
    //		BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
    //		List<BoxSizeBean.TbaseDevicesBean> tbaseDevices = boxSizeBean.getTbaseDevices();
    //
    //	   }
    //	});
    //   }

    @Override
    public void onBindViewBefore() {

    }

    private void initlistener() {
        mLoginLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuousClick(COUNTS, DURATION);
            }
        });
        mDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TestDevicesActivity.class));
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
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.login_password:
                        mLoginViewpager.setCurrentItem(0);
                        break;
                    case R.id.login_pass:
                        mLoginViewpager.setCurrentItem(1);
                        break;
                }
            }
        });
        List<String> xAxisValue = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            xAxisValue.add(i + "号柜");
        }
        List<String> xAxisValue0 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            xAxisValue0.add(i + "");
        }

        List<Float> yAxisValue1 = new ArrayList<>();
        yAxisValue1.add(1 + 0f);
        yAxisValue1.add(3 + 0f);
        yAxisValue1.add(2 + 0f);
        yAxisValue1.add(1 + 0f);
        yAxisValue1.add(7 + 0f);
        yAxisValue1.add(4 + 0f);

        List<Float> yAxisValue2 = new ArrayList<>();
        yAxisValue2.add(1 + 0f);
        yAxisValue2.add(2 + 0f);
        yAxisValue2.add(3 + 0f);
        yAxisValue2.add(3 + 0f);
        yAxisValue2.add(4 + 0f);
        yAxisValue2.add(2 + 0f);

        setTwoBarChart(mChart, xAxisValue, yAxisValue1, yAxisValue2, "", "", xAxisValue0);

        mChart.setPinchZoom(true);
        mChart.setTouchEnabled(false);
        mChart.invalidate();

    }


    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mLoginRadiogroup.check(R.id.login_password);
                    break;
                case 1:
                    mLoginRadiogroup.check(R.id.login_pass);
                    break;
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

    public static void setTwoBarChart(BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue1, List<Float> yAxisValue2, String bartilte1, String bartitle2, List<String> xAxisValue0) {
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
        xAxis.setTextSize(13);
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
        //	if (yMax1>=5||yMax2>=5){
        //	   if (yMax1>yMax2){
        //		leftAxis.setAxisMaximum(yMax1*1.1f);
        //	   }else {
        //		leftAxis.setAxisMaximum(yMax2*1.1f);
        //	   }
        //	}else {
        //	   leftAxis.setAxisMaximum(5);
        //	}
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
    private static void setTwoBarChartData(BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue1, List<Float> yAxisValue2, String bartilte1, String bartitle2) {
        float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.40f;
        // (0.45 + 0.03) * 2 + 0.04 = 1，即一个间隔为一组，包含两个柱图 -> interval per "group"

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int i = 0, n = yAxisValue1.size(); i < n; ++i) {
            entries1.add(new BarEntry(i, yAxisValue1.get(i)));
            entries2.add(new BarEntry(i, yAxisValue2.get(i)));
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
            dataSets.add(dataset1);
            dataSets.add(dataset2);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
            data.setBarWidth(0.9f);
            data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
                    int value1 = (int) value;
                    String s = String.valueOf(value1);
                    return s;
                }
            });

            barChart.setData(data);
        }

        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        barChart.getXAxis().setAxisMaximum(barChart.getBarData().getGroupWidth(groupSpace, barSpace) * xAxisValue.size() + 0);
        barChart.groupBars(0, groupSpace, barSpace);

    }
}
