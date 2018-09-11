package high.rivamed.myapplication.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import java.util.List;

import butterknife.Unbinder;
import high.rivamed.myapplication.base.mvp.IPresent;
import high.rivamed.myapplication.base.mvp.IView;
import high.rivamed.myapplication.base.mvp.KnifeKit;
import high.rivamed.myapplication.base.mvp.VDelegate;
import high.rivamed.myapplication.base.mvp.VDelegateBase;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.MyTimer;
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportActivity;

import static high.rivamed.myapplication.cont.Constants.COUNTDOWN_TIME;

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

   private static final String TAG = "SimpleActivity";
   private VDelegate    vDelegate;
   private P            p;
   public  Activity     mContext;
   public  Gson         mGson;
   public  List<String> mReaderDeviceId;
   public  List<String> eth002DeviceIdList;
   private Unbinder     unbinder;
   private MyTimer      countTimerView;
   //倒计时总时间

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	mContext = this;
//	init();
	mGson = new Gson();
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	if (getLayoutId() > 0) {
	   setContentView(getLayoutId());
	   onBindViewBefore();
	   bindUI(null);
	   bindEvent();
	}
	initDataAndEvent(savedInstanceState);
	App.getInstance().addActivity_(this);
   }



   private void init() {
	//初始化CountTimer，设置倒计时为2分钟。
	countTimerView = MyTimer.getInstance(COUNTDOWN_TIME, 1000, this);
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
	getvDelegate().resume();
//	if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
//	    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")) {
//	   countTimerView.cancel();
//	   countTimerView.start();
//	}
	super.onResume();

   }

   @Override
   protected void onPause() {
	getvDelegate().pause();
//	countTimerView.cancel();
	super.onPause();

   }

   @Override
   public boolean useEventBus() {
	return false;
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();

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

//   /**
//    * 分发触摸事件给所有注册了MyTouchListener的接口
//    */
//   @Override
//   public boolean dispatchTouchEvent(MotionEvent ev) {
//	switch (ev.getAction()) {
//	   //获取触摸动作，如果ACTION_UP，计时开始。
//	   case MotionEvent.ACTION_UP:
//		LogUtils.i(TAG, "   ACTION_UP  ");
//		if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
//		    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")) {
//		   countTimerView.cancel();
//		   countTimerView.start();
//		}
//		break;
//	   //否则其他动作计时取消
//	   default:
//		countTimerView.cancel();
//		LogUtils.i(TAG, "   其他操作  ");
//
//		break;
//	}
//
//	return super.dispatchTouchEvent(ev);
//   }
}
