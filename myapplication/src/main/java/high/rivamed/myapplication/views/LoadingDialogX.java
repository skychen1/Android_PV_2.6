package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import high.rivamed.myapplication.R;

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

	private Context        mContext;
	public RadarView      mLoading;
	public TextView       mLoadingText;
	public  LoadingDialogX mDialog;

	public Builder(Context context) {
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
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new LoadingDialogX(mContext, R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_radar_loading_layout, null);
	   mLoading = (RadarView) layout.findViewById(R.id.radar);
	   mLoadingText = (TextView) layout.findViewById(R.id.radar_text);
	   mDialog.addContentView(layout,
					  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									     ViewGroup.LayoutParams.MATCH_PARENT));
	   mLoading.start();
//
//	   EventBusUtils.post(new Event.EventHomeEnable(true));
//	   //	   new Handler().postDelayed(new Runnable() {
//	   //		@Override
//	   //		public void run() {
//	   //		   if (mDialog.isShowing()) {
//	   //			mAnimationDrawable.stop();
//	   //			mDialog.dismiss();
//	   //		   }
//	   //		}
//	   //	   }, 15000);
	   return mDialog;
	}

   }

}
