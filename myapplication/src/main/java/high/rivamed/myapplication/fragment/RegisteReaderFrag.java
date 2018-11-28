package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegistReaderAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.widget.GridLayout.VERTICAL;
import static cn.rivamed.DeviceManager.getInstance;
import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.SAVE_READER_TIME;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/11/27 18:16
 * 描述:        reader设置
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegisteReaderFrag extends SimpleFragment {

   @BindView(R.id.frag_start)
   TextView           mFragStart;
   @BindView(R.id.timely_ll)
   LinearLayout       mTimelyLl;
   @BindView(R.id.header)
   MaterialHeader     mHeader;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.public_ll)
   LinearLayout       mPublicLl;
   @BindView(R.id.txt_log)
   TextView           mTxtLog;
   @BindView(R.id.scroll_log)
   ScrollView         mScrollLog;
   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
   @BindView(R.id.item_time_text)
   EditText     mItemTimeText;
   @BindView(R.id.item_setting_time)
   TextView     mItemSettingTime;
   @BindView(R.id.gone_ll)
   LinearLayout mGoneLl;
   private String mDiviceId;
   List<String> mDate = new ArrayList<>();
   private RegistReaderAdapter mAdapter;

   /**
    * adapter显示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClick(Event.EventTestIdAndPower event) {
      if (event.readerId==null){
	   AppendLog(event.readerPower);
	}else {
	   getInstance().StartUhfScan(event.readerId, READER_TIME);
	   AppendLog("启动持续扫描,设备ID=" + event.readerId + "，扫描时间为" + READER_TIME + "s ;");
	}
   }

   @Override
   public int getLayoutId() {
	return R.layout.registe_reader_layout;
   }

   public static RegisteReaderFrag newInstance() {
	RegisteReaderFrag fragment = new RegisteReaderFrag();
	return fragment;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	initCallBack();
	mGoneLl.setVisibility(View.GONE);
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   public void AppendLog(String msg) {
	mTxtLog.post(new Runnable() {
	   @Override
	   public void run() {
		Date time = new Date();
		String s = ">>" + simpleDateFormat.format(time) + "  " + msg + "\n";
		mTxtLog.append(s);

		int offset = mTxtLog.getMeasuredHeight() - mScrollLog.getMeasuredHeight();
		if (offset < 0) {
		   offset = 0;
		}
		mScrollLog.scrollTo(0, offset);
		Log.i("fff", msg);
	   }
	});
   }

   private void initCallBack() {
	getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
	   @Override
	   public void OnDeviceConnected(DeviceType deviceType, String deviceIndentify) {

	   }

	   @Override
	   public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {
		AppendLog("设备已断开：" + deviceType + ":::ID=" + deviceIndentify);
	   }

	   @Override
	   public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {
	   }

	   @Override
	   public void OnIDCard(String deviceId, String idCard) {
	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {
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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UHF Reader (" + deviceId + ")扫描完成\n");
		stringBuilder.append("\t EPC数量" + epcs.size() + "个:\n");
		for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
		   stringBuilder.append(v.getKey() + ";");
		}
		AppendLog(stringBuilder.toString());
	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {
		AppendLog("RFID扫描结束：" + deviceId + ";");
	   }

	   /**
	    * 获取UHF Reader 的天线状态
	    * <p>
	    * 仅标识已使能的天线 （开启）
	    * <p>
	    * 无法获取 天线是否连接；
	    * <p>
	    * 对部分reader,连接即意味着使能
	    * 但部分Reader，连接并不意味着使能，使能也并不意味着连接
	    *
	    * @param deviceId
	    * @param success
	    * @param ants
	    */
	   @Override
	   public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

	   }

	   @Override
	   public void OnUhfSetPowerRet(String deviceId, boolean success) {
		if (success) {
		   AppendLog("RFID设置功率结果：标识: " + deviceId + "----成功！");
		} else {
		   AppendLog("RFID设置功率结果：标识: " + deviceId + "----失败！");
		}
	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
		if (success) {
		   AppendLog("RFID读取功率结果：标识: " + deviceId + "; 功率 = " + power);
		} else {
		   AppendLog("RFID读取功率结果：标识: " + deviceId + "; 功率获取失败！");
		}
	   }
	});

   }

   @OnClick({R.id.frag_start, R.id.item_setting_time})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_start:
	      if (mDate!=null){
		   mDate.clear();
		   mGoneLl.setVisibility(View.GONE);
		   mTxtLog.setText("");
		}

		List<DeviceManager.DeviceInfo> deviceInfos = getInstance().QueryConnectedDevice();
		String s = "";
		for (DeviceManager.DeviceInfo d : deviceInfos) {
		   if (d.getDeviceType() == DeviceType.UHFREADER) {
			mDiviceId = d.getIdentifition();
			mDate.add(mDiviceId);
			getInstance().getUhfReaderPower(mDiviceId);
			s += "\t  设备类型 \t" + d.getDeviceType() + ";\t\t设备ID \t" + d.getIdentifition() +
			     "\n";
		   }
		}

		AppendLog(StringUtils.isEmpty(s) ? "目前暂无reader连接" : ("已连接设备如下：\n" + s));

		int mLayout = R.layout.item_reader_layout;
		if (mAdapter != null) {
		   mAdapter.notifyDataSetChanged();
		} else {
		   mAdapter = new RegistReaderAdapter(mLayout, mDate);
		   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
		   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		   mRefreshLayout.setEnableAutoLoadMore(false);
		   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		   mRecyclerview.setAdapter(mAdapter);
		}
		mGoneLl.setVisibility(View.VISIBLE);

		break;
	   case R.id.item_setting_time:
		try {
		   int time = Integer.parseInt(mItemTimeText.getText().toString().trim());
		   SPUtils.putInt(UIUtils.getContext(), SAVE_READER_TIME,time);
		   READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
		   AppendLog("设置RFID reader扫描时长为：" + READER_TIME);
		} catch (Exception ex) {
		}
		break;
	}
   }

   @Override
   public void onDestroy() {
	super.onDestroy();
	EventBusUtils.unregister(this);
   }
}
