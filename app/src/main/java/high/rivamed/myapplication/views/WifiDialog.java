package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.utils.ToastUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/18 20:27
 * 描述:       设置功率
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class WifiDialog extends Dialog {

   private static final String TAG = "LiveRewarDialog";

   public WifiDialog(Context context) {
	super(context);
   }

   public WifiDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	ImageView     mDialogCloss;

	AddAndSubView mAddandsubView;

	TextView      mDialogLeft;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	TextView      mDialogRight;
	private Context         mContext;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mRewarBtn;
	//		private LiveDetailsBean mDetailsBean;
	private String          mCoins;
	private int mAmount = 10;

	public Builder(Context context) {
	   this.mContext = context;
	}
	public Builder setLeft(String left, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftBtn = listener;
	   return this;
	}
	public Builder setRight(String left, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightBtn = listener;
	   return this;
	}

	public Builder setRewarBtn(OnClickListener listener) {

	   this.mRewarBtn = listener;
	   return this;
	}

	public WifiDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final WifiDialog dialog = new WifiDialog(mContext, R.style.Dialog);
	   //	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_wifi_layout, null);
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									    ViewGroup.LayoutParams.WRAP_CONTENT));



	   mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);
	   AddAndSubView mAddandsubView = (AddAndSubView) layout.findViewById(R.id.addandsub_view);

	   mAddandsubView.setGoods_storage(999);

	   mAddandsubView.setOnAmountChangeListener(new AddAndSubView.OnAmountChangeListener() {
		@Override
		public void onAmountChange(View view, int amount) {
		   mAmount = amount;
		}
	   });

	   mDialogCloss.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   dialog.dismiss();
		}
	   });
	   mDialogLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   dialog.dismiss();
		}
	   });
	   mDialogRight.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   ToastUtils.showShort("我设置了功率是："+mAmount);
		}
	   });
	   return dialog;
	}
   }

}
