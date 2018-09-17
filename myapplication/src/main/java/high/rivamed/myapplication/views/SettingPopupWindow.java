package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:20
 * 描述:        个人设置中的PopupWindow
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SettingPopupWindow extends PopupWindow implements OnClickListener {

   // 坐标的位置（x、y）
   private final int[] mLocation = new int[2];

   private final TextView mSettingMeInfo;
   private final TextView mSettingLogin;
//   private final TextView mSettingLoginOut;
   private final View     mView;
	private final Context mContext;

	private String TAG = "SettingPopupWindow";
   private OnClickListener mItemClickListener;

	public SettingPopupWindow(Context context) {
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.setting_popupwindow, null);
	mSettingMeInfo = (TextView) mView.findViewById(R.id.setting_meinfo);
	mSettingLogin = (TextView) mView.findViewById(R.id.setting_login);
//	mSettingLoginOut = (TextView) mView.findViewById(R.id.setting_loginout);

	mSettingMeInfo.setOnClickListener(this);
	mSettingLogin.setOnClickListener(this);
//	mSettingLoginOut.setOnClickListener(this);
   }

   public void showPopupWindow(View parent) {
	parent.getLocationOnScreen(mLocation);

	this.setContentView(mView);
	this.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
	this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
	this.setFocusable(true);
	this.setOutsideTouchable(true);
	this.update();
	// 实例化一个ColorDrawable颜色为半透明
	ColorDrawable dw = new ColorDrawable(0000000000);
	// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
	this.setBackgroundDrawable(dw);
	this.setAnimationStyle(R.style.social_pop_anim);

	if (!this.isShowing()) {

//	   showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0]-50,
//				mLocation[1] - ((this.getHeight() - parent.getHeight()-12)));
	   showAtLocation(parent, Gravity.NO_GRAVITY, UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x1510),
				UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y88));
	} else {
	   dismiss();
	}
   }
//
   @Override
   public void onClick(View view) {

	switch (view.getId()) {
	   case R.id.setting_meinfo:
		mItemClickListener.onItemClick(0);
		break;
	   case R.id.setting_login:
		mItemClickListener.onItemClick(1);
		break;
//	   case R.id.setting_loginout:
//		mItemClickListener.onItemClick(2);
//		break;
	   default:
		break;
	}
	dismiss();
   }

   public void setmItemClickListener(OnClickListener mItemClickListener) {
	this.mItemClickListener = mItemClickListener;
   }
   public static interface OnClickListener {
	public void onItemClick( int position);
   }
}
