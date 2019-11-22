package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rivamed.FingerCallback;
import com.rivamed.FingerManager;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libconsumables.callback.ConsumableCallBack;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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
//	initCallBack();
	initBom();
	initFinger();
	initIC();
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
   /**
    * IC卡
    */
   private void initIC() {
	IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {

	   }

	   @Override
	   public void onReceiveCardNum(String cardNo) {
		AppendLog("接收到刷卡信息：设备ID:     ID = " + cardNo);
	   }
	});
   }
   /**
    * 指纹仪
    */
   private void initFinger() {
	FingerManager.getManager().registerCallback(new FingerCallback() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {
	   }

	   @Override
	   public void onFingerFeatures(String deviceId, String features) {
		AppendLog("接收到指纹采集信息：设备ID:   " + deviceId + "   ;   FingerData = " + features);
		fingerData = features;
	   }

	   @Override
	   public void onRegisterResult(String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
		//		appendLog("设备：：" + deviceId + "注册结果码是：：" + code + "\n>>>>>>>" + msg
		//			    + "\n指纹照片数据：：" + (fingerPicPath == null ? 0 : fingerPicPath.size()) + "\n特征值是：：：" + features);
		//收到注册结果标识注册完成就开启读取
	   }

	   @Override
	   public void onFingerUp(String deviceId) {
		//		appendLog("设备：：" + deviceId + "请抬起手指：");
	   }

	   @Override
	   public void onRegisterTimeLeft(String deviceId, long time) {
		//		setLog("设备：：" + deviceId + "剩余注册时间：：" + time + "\n");
	   }
	});
   }
   private void initBom() {
	ConsumableManager.getManager().registerCallback(new ConsumableCallBack() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {
		if (!isConnect) {
		   AppendLog("设备已断开：设备ID:   " + deviceId + "；");
		}
	   }

	   @Override
	   public void onOpenDoor(String deviceId, int which, boolean isSuccess) {
		String type ;
		String whichs ;
		if(isSuccess){
		   type ="成功";
		}else {
		   type="失败";
		}
		if(which==0){
		   whichs ="0号端口";
		}else {
		   whichs="1号端口";
		}
		AppendLog("开门结果：设备ID:   " + deviceId + "  端口： "+whichs+" ;   开门状态 = " + type);
	   }

	   @Override
	   public void onCloseDoor(String deviceId, int which, boolean isSuccess) {
		String type ;
		String whichs ;
		if(isSuccess){
		   type ="成功";
		}else {
		   type="失败";
		}
		if(which==0){
		   whichs ="0号端口";
		}else {
		   whichs="1号端口";
		}
		AppendLog("门锁已关闭：设备ID:   " + deviceId + "  端口： "+which+"   which   "+whichs+"  ;   关门状态 = " + type);
	   }

	   @Override
	   public void onDoorState(String deviceId, int which, boolean state) {
		String type ;
		String whichs ;
		if(state){
		   type ="门锁打开状态";
		}else {
		   type="门锁关闭状态";
		}
		if(which==0){
		   whichs ="0号端口";
		}else {
		   whichs="1号端口";
		}
		AppendLog("门锁状态检查：设备ID:   " + deviceId + "  端口： "+which+"   which   "+whichs+"    ;   门锁状态 = " + type);
	   }

	   @Override
	   public void onOpenLight(String deviceId, int which, boolean isSuccess) {

	   }

	   @Override
	   public void onCloseLight(String deviceId, int which, boolean isSuccess) {

	   }

	   @Override
	   public void onLightState(String deviceId, int which, boolean state) {

	   }

	   @Override
	   public void onFirmwareVersion(String deviceId, String version) {

	   }

	   @Override
	   public void onNeedUpdateFile(String deviceId) {

	   }

	   @Override
	   public void onUpdateProgress(String deviceId, int percent) {

	   }

	   @Override
	   public void onUpdateResult(String deviceId, boolean isSuccess) {

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
		List<DeviceInfo> deviceInfos = ConsumableManager.getManager().getConnectedDevice();
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
