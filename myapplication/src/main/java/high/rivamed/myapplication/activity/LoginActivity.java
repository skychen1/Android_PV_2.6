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
import high.rivamed.myapplication.bean.SocketLeftTopBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.TestDevicesActivity;
import high.rivamed.myapplication.dto.FingerLoginDto;
import high.rivamed.myapplication.dto.IdCardLoginDto;
import high.rivamed.myapplication.fragment.LoginPassWordFragment;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MyValueFormatter;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_NAME;
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

   private static final String TAG = "LoginActivity";
   @BindView(R.id.login_logo)
   ImageView   mLoginLogo;
   @BindView(R.id.login_password)
   RadioButton mLoginPassword;
   @BindView(R.id.login_pass)
   RadioButton mLoginPass;
   @BindView(R.id.login_radiogroup)
   RadioGroup  mLoginRadiogroup;
   @BindView(R.id.login_viewpager)
   ViewPager   mLoginViewpager;
   @BindView(R.id.chart1)
   BarChart    mChart;
   @BindView(R.id.down_text)
   TextView    mDownText;
   private ArrayList<Fragment> mFragments = new ArrayList<>();

   final static int  COUNTS   = 5;// 点击次数  2s内点击8次进入注册界面
   final static long DURATION = 2000;// 规定有效时间
   long[] mHits = new long[COUNTS];
   private SQLiteDatabase        mDb;
   private LoadingDialog.Builder mBuilder;

   @Override
   public int getLayoutId() {
	return R.layout.activity_login;
   }

   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	//清空accountID
	mDownText.setText("智能耗材管理柜：" + UIUtils.getVersionName(mContext));
	//-----检测分辨率---------------------------------------
	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	DisplayMetrics dm = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(dm);
	Point size = new Point();
	size.x = dm.widthPixels;
	size.y = dm.heightPixels;

	Log.d("fbl", size.x + " , " + size.y + " , ");

	//---------------------------------------------------

	//创建数据库表
	mDb = LitePal.getDatabase();
	Log.i("dss", SPUtils.getString(mContext, THING_CODE) + "");

	if (!SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
	   LitePal.deleteAll(BoxIdBean.class);
	}

	mFragments.add(new LoginPassWordFragment());//用户名登录
//	mFragments.add(new LoginPassFragment());//紧急登录
	mLoginPass.setVisibility(View.GONE);

	initData();
	initlistener();
	initCall();
   }

   @Override
   public void onStart() {
	super.onStart();
	LogUtils.i(TAG, "MAIN_URL     " + MAIN_URL);
	SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, "");
	SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME, "");
	SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, "");
	if (MAIN_URL != null&&SPUtils.getString(UIUtils.getContext(), THING_CODE)!=null) {
	   getLeftDate();
	}
   }

   private void initConfig() {
	NetRequest.getInstance().findThingConfigDate(this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
	   }
	});
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
		   validateLoginIdCard(idCard);
		}
	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   validateLoginFinger(fingerFea.trim().replaceAll("\n", ""));
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
	NetRequest.getInstance().validateLoginIdCard(mGson.toJson(data), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		try {
		   LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
		   if (loginResultBean.isOperateSuccess()) {
			SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
			SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME,
						loginResultBean.getAppAccountInfoVo().getAccountName());
			SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID,
						loginResultBean.getAppAccountInfoVo().getAccountId());
			Intent intent = new Intent(mContext, HomeActivity.class);
			mContext.startActivity(intent);
			mContext.finish();
		   }else {
			ToastUtils.showShort("登录验证失败");
		   }
		} catch (JsonSyntaxException e) {
		   e.printStackTrace();
		}
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		ToastUtils.showShort("登录失败");
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
	NetRequest.getInstance().validateLoginFinger(mGson.toJson(data), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		try {
		   LoginResultBean loginResultBean = mGson.fromJson(result, LoginResultBean.class);
		   if (loginResultBean.isOperateSuccess()) {
			SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_DATA, result);
			SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME,
						loginResultBean.getAppAccountInfoVo().getAccountName());
			SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID,
						loginResultBean.getAppAccountInfoVo().getAccountId());
			Intent intent = new Intent(mContext, HomeActivity.class);
			mContext.startActivity(intent);
			mContext.finish();
		   }else {
			ToastUtils.showShort("登录验证失败");
		   }
		} catch (JsonSyntaxException e) {
		   e.printStackTrace();
		}
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		ToastUtils.showShort("登录失败");
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
		if (leftTopBean.getCstExpirationVos()!=null&&leftTopBean.getCstExpirationVos().size()>0){
		   setOperationDate(leftTopBean);
		}
	   }
	});
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
//		   case R.id.login_pass:
//			mLoginViewpager.setCurrentItem(1);
//			break;
		}
	   }
	});

   }

   private void setOperationDate(SocketLeftTopBean leftTopBean) {
	List<SocketLeftTopBean.CstExpirationVosBean> cstExpirationVos = leftTopBean.getCstExpirationVos();
	List<String> xAxisValue = new ArrayList<>();
	if (cstExpirationVos.size()<6){
	   for (int i = 1; i < 6; i++) {
		if (cstExpirationVos.size()>=i) {
		   xAxisValue.add(i + "号柜");
		} else {
		   xAxisValue.add("");
		}
	   }
	}else {
	   for (int i = 1; i < cstExpirationVos.size()+1; i++) {
	      xAxisValue.add(i + "号柜");
	   }
	}


	//Y轴过期
	List<Float> yAxisValue1 = new ArrayList<>();
	for (int i = 0; i < cstExpirationVos.size(); i++) {
	   yAxisValue1.add((float) cstExpirationVos.get(i).getExpireCount());
	}

	//Y轴近效期
	List<Float> yAxisValue2 = new ArrayList<>();
	for (int i = 0; i < cstExpirationVos.size(); i++) {
	   yAxisValue2.add((float) cstExpirationVos.get(i).getNearExpireCount());
	   if (cstExpirationVos.get(i).getNearExpireCount()<6){
		yAxisValue2.add(6+0f);
	   }
	}

	setTwoBarChart(mChart, xAxisValue, yAxisValue1, yAxisValue2, "", "");

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
//		case 1:
//		   mLoginRadiogroup.check(R.id.login_pass);
//		   break;
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
	xAxis.setTextSize(18);
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
	leftAxis.setTextSize(18);
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
	   data.setValueTextSize(16f);
	   data.setValueTextColor(UIUtils.getContext().getResources().getColor(R.color.bg_f));
	   data.setBarWidth(0.9f);
	   data.setValueFormatter(new IValueFormatter() {
		@Override
		public String getFormattedValue(
			float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
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
	barChart.getXAxis()
		.setAxisMaximum(
			barChart.getBarData().getGroupWidth(groupSpace, barSpace) * xAxisValue.size() +
			0);
	barChart.groupBars(0, groupSpace, barSpace);

   }
}
