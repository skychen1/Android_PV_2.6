package high.rivamed.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import org.androidpn.utils.XmppEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.mvp.IPresent;
import high.rivamed.myapplication.base.mvp.IView;
import high.rivamed.myapplication.base.mvp.KnifeKit;
import high.rivamed.myapplication.base.mvp.VDelegate;
import high.rivamed.myapplication.base.mvp.VDelegateBase;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.WifiUtils;
import me.yokeyword.fragmentation.SupportActivity;

import static high.rivamed.myapplication.base.App.mTitleConn;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:23
 * 描述:       仿照XActivity写的基类没有标题栏
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public abstract class SimpleActivity<P extends IPresent> extends SupportActivity
	implements IView<P> {

   private VDelegate    vDelegate;
   private P            p;
   public  Activity     mContext;
   public  Gson         mGson;
   public  List<String> mReaderDeviceId;
   public  List<String> eth002DeviceIdList;
   private Unbinder     unbinder;
   View mTipView;
   WindowManager mWindowManager;
   WindowManager.LayoutParams mLayoutParams;
   /**
    * 设备title连接状态
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
   public void onTitleConnEvent(XmppEvent.XmmppConnect event) {
      Log.e("xxb","SimpleActivity     "+event.connect);
	mTitleConn = event.connect;
	hasNetWork(event.connect);
   }


   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	EventBusUtils.register(this);

	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
	mContext = this;
	mGson = new Gson();
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	if (getLayoutId() > 0) {
	   setContentView(getLayoutId());
	   initTipView();
	   onBindViewBefore();
	   bindUI(null);
	   bindEvent();
	}
	initDataAndEvent(savedInstanceState);
	App.getInstance().addActivity_(this);
   }

   /**
    * 判断显示网络异常
    * @param has:true
    */
   public void hasNetWork(boolean has) {
	   if (has) {
	      Log.e("xxb",(mTipView != null)+"         "+(mTipView.getParent() != null));
		if (mTipView != null && mTipView.getParent() != null) {
		   mWindowManager.removeView(mTipView);
		}
	   } else {
		if (mTipView.getParent() == null) {
		   mWindowManager.addView(mTipView, mLayoutParams);
		}
	   }

   }
   private void initTipView() {
	LayoutInflater inflater = getLayoutInflater();
	mTipView = inflater.inflate(R.layout.layout_network_tip, null); //提示View布局
	mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	mLayoutParams = new WindowManager.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.TYPE_APPLICATION,
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
		PixelFormat.TRANSLUCENT);
	//使用非CENTER时，可以通过设置XY的值来改变View的位置
	mLayoutParams.gravity = Gravity.TOP;
	mLayoutParams.x = 0;
	mLayoutParams.y = getResources().getDimensionPixelSize(R.dimen.y86);
   }

   @Override
   public void bindUI(View rootView) {
	unbinder = KnifeKit.bind(this);
   }

   protected VDelegate getvDelegate() {
	if (vDelegate == null) {
	   vDelegate = VDelegateBase.create(mContext);
	}
	return vDelegate;
   }

   protected P getP() {
	if (p == null) {
	   p = newP();
	   if (p != null) {
		p.attachV(this);
	   }
	}
	return p;
   }

   @Override
   public void onStart() {
	super.onStart();
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
   }

   @Override
   protected void onResume() {
	super.onResume();
	if (WifiUtils.isWifi(mContext) == 0) {
	   hasNetWork(false);
	}
	getvDelegate().resume();

   }

   @Override
   protected void onPause() {
	super.onPause();
	getvDelegate().pause();

   }

   @Override
   public boolean useEventBus() {
	return false;
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	if (mTipView != null && mTipView.getParent() != null) {
	   mWindowManager.removeView(mTipView);
	}
	EventBusUtils.unregister(this);
	OkGo.getInstance().cancelTag(this);
	UIUtils.removeAllCallbacks();

	if (getP() != null) {
	   getP().detachV();
	}
	getvDelegate().destory();
	p = null;
	vDelegate = null;
	unbinder.unbind();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	if (getOptionsMenuId() > 0) {
	   getMenuInflater().inflate(getOptionsMenuId(), menu);
	}
	return super.onCreateOptionsMenu(menu);
   }

   @Override
   public int getOptionsMenuId() {
	return 0;
   }

   @Override
   public void bindEvent() {

   }
}
