package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;

/**
 * 项目名称:    Rivamed_High_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/3/28 9:22
 * 描述:        加载的dialog
 * 包名:        high.rivamed.myapplication.views;
 * <p/>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoadingDialogX extends Dialog {

   public LoadingDialogX(Context context) {
	super(context);
   }

   public LoadingDialogX(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Activity        mContext;
	public  RadarView      mLoading;
	public  TextView       mLoadingText;
	public  LoadingDialogX mDialog;
	public Handler mHandler;
	private WeakReference<Activity> activitySRF = null;
	public Builder(Activity context) {
	   this.mContext = context;
	}

	public Builder setMsg(String msg) {
	   mLoadingText.setText(msg);
	   return this;
	}

	public Builder setMsgSize(int size) {
	   mLoadingText.setTextSize(size);
	   return this;
	}

	public LoadingDialogX create() {
	   activitySRF = new WeakReference<Activity>(mContext);
	   LayoutInflater inflater = (LayoutInflater) activitySRF.get().getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new LoadingDialogX(activitySRF.get(), R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_radar_loading_layout, null);
	   mLoading = (RadarView) layout.findViewById(R.id.radar);
	   mLoadingText = (TextView) layout.findViewById(R.id.radar_text);
	   mDialog.addContentView(layout,
					  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									     ViewGroup.LayoutParams.MATCH_PARENT));
	   mLoading.start();
	   if (null != activitySRF && null != activitySRF.get() && !activitySRF.get().isFinishing()) {
	      if (mHandler!=null){
		   postDialog();
		}else {
		   mHandler = new Handler(activitySRF.get().getMainLooper());
		   postDialog();
		}

	   }
	   return mDialog;
	}

	private void postDialog() {
	   mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
		   if (null != activitySRF && null != activitySRF.get() && !activitySRF.get().isFinishing()) {
			if (mDialog != null && mDialog.isShowing()) {
			   mLoading.stop();
			   mHandler.removeCallbacksAndMessages(null);
			   EventBusUtils.post(new Event.StartScanType(true, false));
//			   ToastUtils.showShortToast("readr未连接，请稍后重试！");
			   mDialog.dismiss();
			}
		   }
		}
	   }, 25000);
	}

   }

}
