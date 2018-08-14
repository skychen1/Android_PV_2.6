package high.rivamed.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import java.util.List;

import butterknife.Unbinder;
import high.rivamed.myapplication.base.mvp.IPresent;
import high.rivamed.myapplication.base.mvp.IViewFrg;
import high.rivamed.myapplication.base.mvp.KnifeKit;
import high.rivamed.myapplication.base.mvp.VDelegate;
import high.rivamed.myapplication.base.mvp.VDelegateBase;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        抽取的fragment，没有标题栏
 * 包名:        high.rivamed.myapplication.base
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public abstract class SimpleFragment<P extends IPresent> extends SupportFragment
	implements IViewFrg<P> {
	
	private   VDelegate      vDelegate;
	private   P              p;
	protected Activity       mContext;
	private   View           rootView;
	protected LayoutInflater layoutInflater;
	
	public  Gson         mGson;
   public    List<String> mReaderDeviceId;
   public    List<String> eth002DeviceIdList;
	private Unbinder     unbinder;
   private Object mTitleName;

   @Nullable
   @Override
   public View onCreateView(
	   LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	layoutInflater = inflater;
	eth002DeviceIdList = DevicesUtils.getEthDeviceId();
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	if (rootView == null && getLayoutId() > 0) {
	   rootView = inflater.inflate(getLayoutId(), null);

	   onBindViewBefore(rootView);
	   bindUI(rootView);
	   getTitleName();
	} else {
	   ViewGroup viewGroup = (ViewGroup) rootView.getParent();
	   if (viewGroup != null) {
		viewGroup.removeView(rootView);
	   }
	}
	mGson = new Gson();
	return rootView;
   }


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bindEvent();

		initDataAndEvent(savedInstanceState);
	}
	
	@Override
	public void bindUI(View rootView) {
		unbinder = KnifeKit.bind(this, rootView);
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
	public void onResume() {
	LogUtils.i("ContentConsumeOperateFrag","onResume");
		super.onResume();
	}


	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Activity) {
			this.mContext = (Activity) context;
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mContext = null;
	}
	
	@Override
	public boolean useEventBus() {
		return false;
	}
	
	@Override
	public P newP() {
		return null;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBusUtils.unregister(this);
		OkGo.getInstance().cancelTag(this);
		UIUtils.removeAllCallbacks();
		if (getP() != null) {
			getP().detachV();
		}
		getvDelegate().destory();
		p = null;
		vDelegate = null;
	}
	
	@Override
	public int getOptionsMenuId() {
		return 0;
	}
	
	@Override
	public void bindEvent() {
	
	}

   public void getTitleName() {
   }
}

