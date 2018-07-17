package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/2 11:27
 * 描述:        2个按钮的dialog
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteDialog extends Dialog {

   private String mString;

   public RegisteDialog(Context context) {
	super(context);
   }

   public RegisteDialog(Context context, int theme) {
	super(context, theme);
   }

   @Override
   public void show() {
	super.show();
	WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

	layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
	layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
	getWindow().getDecorView().setPadding(530, 200, 0, 0);
	getWindow().setAttributes(layoutParams);
   }

   public static class Builder {

	ImageView mDialogCloss;
	EditText  mAddressOne;
	TextView  mAddressTwo;
	TextView  mAddressThree;
	TextView  mAddressFour;
	TextView  mAddressFive;
	TextView  mDialogLeft;
	TextView  mDialogRight;
	private Context         mContext;
	private Activity        mActivity;
	private String          mMsgTwo;
	private String          mMsgText;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mRigtht;
	private TextView        mLeft;
	private int mLeftTextColor = -1;
	private int      mRightTextColor;
	private int      mType;
	private TextView mDialogMsg;
	private TextView mDialogBtn;
	List<Movie> mMovies;
	List<Movie> mMovies1 = new ArrayList<>();
	private hospitalPopupWindow mMhospWindow;

	public Builder(Context context, Activity activity) {
	   this.mContext = context;
	   this.mActivity = activity;

	}

	public Builder setTwoMsg(String title) {
	   this.mMsgTwo = title;
	   return this;
	}

	public Builder setMsg(String msg) {
	   this.mMsgText = msg;
	   return this;
	}

	public Builder setLeft(String left, int color, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftTextColor = color;
	   this.mLeftBtn = listener;
	   return this;
	}

	public Builder setLeft(String left, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftBtn = listener;
	   return this;
	}

	public Builder setRight(String left, int color, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightTextColor = color;
	   this.mRightBtn = listener;
	   return this;
	}

	public Builder setRight(String left, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightBtn = listener;
	   return this;
	}

	public RegisteDialog create() {

	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final RegisteDialog dialog = new RegisteDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   final View layout = inflater.inflate(R.layout.dialog_registe_layout, null);

	   dialog.addContentView(layout, new ViewGroup.LayoutParams(860,
											ViewGroup.LayoutParams.WRAP_CONTENT));

	   mMovies = genDatax();
	   mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mAddressOne = (EditText) layout.findViewById(R.id.address_one);
	   mAddressTwo = (TextView) layout.findViewById(R.id.address_two);
	   mAddressThree = (TextView) layout.findViewById(R.id.address_three);
	   mAddressFour = (TextView) layout.findViewById(R.id.address_four);
	   mAddressFive = (TextView) layout.findViewById(R.id.address_five);
	   mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);

	   mAddressOne.addTextChangedListener(new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		   String trim = mAddressOne.getText().toString().trim();
		   if (trim.length() != 0) {
			Log.i("ttt", "w jin");
			mMovies1.clear();
			for (Movie detail : mMovies) {

			   if (detail.mString.contains(trim)) {
				mMovies1.add(detail);
			   }
			}
			mMhospWindow = new hospitalPopupWindow(mContext, mMovies1);
			mMhospWindow.showPopupWindow(mAddressOne);
			mMhospWindow.mMealPopAdapter.setOnItemClickListener(
				new BaseQuickAdapter.OnItemClickListener() {
				   @Override
				   public void onItemClick(
					   BaseQuickAdapter adapter, View view, int position) {
					mMhospWindow.dismiss();
					UIUtils.hideSoftInput(mContext,
								    layout.findViewById(R.id.address_one));
					TextView textView = (TextView) view.findViewById(R.id.item_meal);
					String trim = textView.getText().toString().trim();
					mAddressOne.setText(trim);
					mMhospWindow.dismiss();
					mAddressOne.clearFocus();
				   }
				});
		   } else {
			mMovies1.clear();
			mMhospWindow.dismiss();
		   }

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	   });

	   mAddressTwo.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   final hospitalPopupWindow window = new hospitalPopupWindow(mContext, mMovies);
		   window.showPopupWindow(mAddressTwo);
		   window. mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			   TextView textView = (TextView) view.findViewById(R.id.item_meal);
			   String trim = textView.getText().toString().trim();
			   mAddressTwo.setText(trim);
			   window.dismiss();
			}
		   });
		}
	   });
	   mAddressThree.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   final hospitalPopupWindow window = new hospitalPopupWindow(mContext, mMovies);
		   window.showPopupWindow(mAddressThree);
		   window. mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			   TextView textView = (TextView) view.findViewById(R.id.item_meal);
			   String trim = textView.getText().toString().trim();
			   mAddressThree.setText(trim);
			   window.dismiss();
			}
		   });
		}
	   });
	   mAddressFour.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   final hospitalPopupWindow window = new hospitalPopupWindow(mContext, mMovies);
		   window.showPopupWindow(mAddressFour);
		   window. mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			   TextView textView = (TextView) view.findViewById(R.id.item_meal);
			   String trim = textView.getText().toString().trim();
			   mAddressFour.setText(trim);
			   window.dismiss();
			}
		   });
		}
	   });
	   mAddressFive.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   final hospitalPopupWindow window = new hospitalPopupWindow(mContext, mMovies);
		   window.showPopupWindow(mAddressFive);
		   window. mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			   TextView textView = (TextView) view.findViewById(R.id.item_meal);
			   String trim = textView.getText().toString().trim();
			   mAddressFive.setText(trim);
			   window.dismiss();
			}
		   });
		}
	   });

	   mDialogCloss.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   dialog.dismiss();
		}
	   });
	   mDialogLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	   });
	   mDialogRight.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mRightBtn.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
		}
	   });
	   return dialog;
	}
   }

   private static List<Movie> genDatax() {

	List<Movie> list = new ArrayList<>();
	String one;
	for (int i = 0; i < 40; i++) {
	   if (i == 1) {
		one = "协和医院_医院" + i;
	   } else if (i == 2) {
		one = "协和ss_医院" + i;
	   } else if (i == 3) {
		one = "协和ssss_医院" + i;
	   } else if (i == 4) {
		one = "医院" + i;
	   } else if (i == 5) {
		one = "协和" + i;
	   } else if (i == 6) {
		one = "协和ss_医院" + i;
	   } else if (i == 7) {
		one = "ccss_医院" + i;
	   } else {
		one = "xx-" + i;
	   }

	   Movie movie = new Movie(one);
	   list.add(movie);
	}
	return list;
   }

}
