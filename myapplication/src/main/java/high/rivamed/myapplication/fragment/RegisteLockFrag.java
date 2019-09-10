package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.Eth002Manager;
import cn.rivamed.callback.Eth002CallBack;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegistLockAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.StringUtils;

import static android.widget.GridLayout.VERTICAL;

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
   @BindView(R.id.txt_log)
    TextView           mTxtLog;
   @BindView(R.id.scroll_log)
    ScrollView         mScrollLog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
   List<String> mDate = new ArrayList<>();
   private String mDiviceId;
 public    String fingerData;
   public  String fingerTemplate;
   private RegistLockAdapter mAdapter;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onLockType(Event.lockType event) {
	if (event.type==1){
	   AppendLog("开门命令已发出 ret=" + event.ret + "      DeviceId   " + event.item);
	}else if (event.type ==2){
	   AppendLog("检查门锁指令已发出 ret=" + event.ret+"   ：设备ID:   "+event.item);
	}else if (event.type==3){
	   AppendLog("指纹注册命令已发送 RET=" + event.ret + ";请等待质问注册执行结果");
	}
   }


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
	initEth002();
//	initCallBack();
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

   private void initEth002() {
	Eth002Manager.getEth002Manager().registerCallBack(new Eth002CallBack() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {
		if (!isConnect) {
		   AppendLog("设备已断开：设备ID:   " + deviceId + "；");
		}
	   }

	   @Override
	   public void onFingerFea(String deviceId, String fingerFea) {
		AppendLog("接收到指纹采集信息：设备ID:   " + deviceId + "   ;   FingerData = " + fingerFea);
		fingerData = fingerFea;
	   }

	   @Override
	   public void onFingerRegExcuted(String deviceId, boolean success) {
		String type ;
		if(success){
		   type ="成功";
		}else {
		   type="失败";
		}

		AppendLog("指纹注册命令已执行：设备ID:   " + deviceId + "   ;   操作状态 = " + type);
	   }

	   @Override
	   public void onFingerRegisterRet(String deviceId, boolean success, String fingerData) {
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
	   public void onIDCard(String deviceId, String idCard) {
		AppendLog("接收到刷卡信息：设备ID:   " + deviceId + "   ;   ID = " + idCard);
	   }

	   @Override
	   public void onDoorOpened(String deviceIndentify, boolean success) {
		String type ;
		if(success){
		   type ="成功";
		}else {
		   type="失败";
		}
		AppendLog("开门结果：设备ID:   " + deviceIndentify + "   ;   开门状态 = " + type);
	   }

	   @Override
	   public void onDoorClosed(String deviceIndentify, boolean success) {
		String type ;
		if(success){
		   type ="成功";
		}else {
		   type="失败";
		}
		AppendLog("门锁已关闭：设备ID:   " + deviceIndentify + "   ;   关门状态 = " + type);
	   }

	   @Override
	   public void onDoorCheckedState(String deviceIndentify, boolean opened) {
		String type ;
		if(opened){
		   type ="门锁打开状态";
		}else {
		   type="门锁关闭状态";
		}
		AppendLog("门锁状态检查：设备ID:   " + deviceIndentify + "   ;   门锁状态 = " + type);
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
		List<DeviceInfo> deviceInfos = Eth002Manager.getEth002Manager().getConnectedDevice();
		String s = "";
		for (DeviceInfo d : deviceInfos) {
			mDiviceId = d.getIdentification();
			mDate.add(mDiviceId);
			s += "\t  设备类型 \t" + d.getProduct() + ";\t\t设备ID \t" + d.getIdentification() +
			     "\n";
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
