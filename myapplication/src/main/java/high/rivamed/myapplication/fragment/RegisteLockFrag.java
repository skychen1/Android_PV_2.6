package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
import high.rivamed.myapplication.adapter.RegistLockAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.utils.StringUtils;

import static android.widget.GridLayout.VERTICAL;
import static cn.rivamed.DeviceManager.getInstance;

/**
 * 项目名称:    Android_PV_2.6.6_416D
 * 创建者:      DanMing
 * 创建时间:    2019/4/22 16:40
 * 描述:        工程模式锁
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegisteLockFrag extends SimpleFragment {
   @BindView(R.id.frag_start)
   TextView           mFragStart;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
//   @BindView(R.id.txt_log)
   static TextView           mTxtLog;
//   @BindView(R.id.scroll_log)
   static ScrollView         mScrollLog;
   static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
   List<String> mDate = new ArrayList<>();
   private String mDiviceId;
 public static   String fingerData;
   public static String fingerTemplate;
   private RegistLockAdapter mAdapter;

   public static RegisteLockFrag newInstance() {
	RegisteLockFrag fragment = new RegisteLockFrag();
	return fragment;
   }

   @Override
   public int getLayoutId() {
	return R.layout.registe_lock_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initCallBack();
   }

   @Override
   public void onBindViewBefore(View view) {
	mTxtLog =view.findViewById(R.id.txt_log);
	mScrollLog =view.findViewById(R.id.scroll_log);
   }

   public static void AppendLog(String msg) {
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
		if (deviceType == DeviceType.Eth002) {
		   mDiviceId = deviceIndentify;
		}
	   }

	   @Override
	   public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {
		AppendLog("设备已断开：设备ID:   " + deviceType + "   ;   ID=" + deviceIndentify);
	   }

	   @Override
	   public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {
		AppendLog("检查门锁开关：设备ID:   " + deviceType + "   ;   ID = " + deviceId + "   ;   RET = " + code);
	   }

	   @Override
	   public void OnIDCard(String deviceId, String idCard) {
		AppendLog("接收到刷卡信息：设备ID:   " + deviceId + "   ;   ID = " + idCard);
	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {
		AppendLog("接收到指纹采集信息：设备ID:   " + deviceId + "   ;   FingerData = " + fingerFea);
		fingerData = fingerFea;
	   }

	   @Override
	   public void OnFingerRegExcuted(String deviceId, boolean success) {
	      String type ;
	      if(success){
	         type ="成功";
		}else {
	         type="失败";
		}

		AppendLog("指纹注册命令已执行：设备ID:   " + deviceId + "   ;   操作状态 = " + type);
	   }

	   @Override
	   public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {
		String type ;
		if(success){
		   type ="成功";
		}else {
		   type="失败";
		}
		AppendLog("接收到指纹注册结果：设备ID:   " + deviceId + "   ;   操作状态 = " + type + "   ;   FingerData = " + fingerData);
		if (success) {
		   fingerTemplate = fingerData;
		}
	   }

	   @Override
	   public void OnDoorOpened(String deviceIndentify, boolean success) {
		String type ;
		if(success){
		   type ="成功";
		}else {
		   type="失败";
		}
		AppendLog("开门结果：设备ID:   " + deviceIndentify + "   ;   开门状态 = " + type);
	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {
		String type ;
		if(success){
		   type ="成功";
		}else {
		   type="失败";
		}
		AppendLog("门锁已关闭：设备ID:   " + deviceIndentify + "   ;   关门状态 = " + type);
	   }

	   @Override
	   public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
		String type ;
		if(opened){
		   type ="门锁打开状态";
		}else {
		   type="门锁关闭状态";
		}
		AppendLog("门锁状态检查：设备ID:   " + deviceIndentify + "   ;   门锁状态 = " + type);
	   }

	   @Override
	   public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {

	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {

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

	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

	   }
	});

   }
   @OnClick({R.id.frag_start})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_start:
		if (mDate!=null){
		   mDate.clear();
		   mTxtLog.setText("");
		}

		List<DeviceManager.DeviceInfo> deviceInfos = getInstance().QueryConnectedDevice();
		String s = "";
		for (DeviceManager.DeviceInfo d : deviceInfos) {
		   if (d.getDeviceType() == DeviceType.Eth002) {
			mDiviceId = d.getIdentifition();
			mDate.add(mDiviceId);
			s += "\t  设备类型 \t" + d.getDeviceType() + ";\t\t设备ID \t" + d.getIdentifition() +
			     "\n";
		   }
		}

		AppendLog(StringUtils.isEmpty(s) ? "目前暂无连接" : ("已连接设备如下：\n" + s));

		int mLayout = R.layout.item_lock_layout;
		if (mAdapter != null) {
		   mAdapter.notifyDataSetChanged();
		} else {
		   mAdapter = new RegistLockAdapter(mLayout, mDate);
		   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
		   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		   mRefreshLayout.setEnableAutoLoadMore(false);
		   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		   mRecyclerview.setAdapter(mAdapter);
		}
		break;

	}
   }
}
