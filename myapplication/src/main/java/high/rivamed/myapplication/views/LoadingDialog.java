package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
public class LoadingDialog extends Dialog {

   public LoadingDialog(Context context) {
	super(context);
   }

   public LoadingDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Context       mContext;
	private ImageView     mLoading;
	public  LoadingDialog mDialog;
	public AnimationDrawable mAnimationDrawable;

	public Builder(Context context) {
	   this.mContext = context;
	}

	public LoadingDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new LoadingDialog(mContext, R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_loading_layout, null);
	   mLoading = (ImageView) layout.findViewById(R.id.animProgress);
	   mDialog.addContentView(layout,
					  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									     ViewGroup.LayoutParams.MATCH_PARENT));
	   mAnimationDrawable = (AnimationDrawable) mLoading.getBackground();
	   mAnimationDrawable.start();
	   new Handler().postDelayed(new Runnable() {
		@Override
		public void run() {
		   mAnimationDrawable.stop();
		   mDialog.dismiss();
		}
	   }, 4000);
	   return mDialog;
	}

   }

}
