package high.rivamed.myapplication.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportActivity;

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
public abstract class SimpleActivity<P extends IPresent> extends SupportActivity implements
	IView<P> {
	
	
	private VDelegate    vDelegate;
	private P            p;
	public  Activity     mContext;
	public  Gson         mGson;
   public    List<String> mReaderDeviceId;
   public    List<String> eth002DeviceIdList;
	private Unbinder     unbinder;
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mGson = new GsonBuilder().disableHtmlEscaping().create();
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
