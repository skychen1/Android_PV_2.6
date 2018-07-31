package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.LogUtils;

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

	private static final String TAG = "RegisteDialog";
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
	//	List<Movie> mMovies;
	List<Movie> mMovies1 = new ArrayList<>();
	private hospitalPopupWindow                   mMhospWindow;
	private TextView                              mGoneOneType;
	private List<HospNameBean.TbaseHospitalsBean> mHospitalsName;
	private TextView                              mGoneFiveType;
	private TextView                              mGoneFourType;
	private TextView                              mGoneThreeType;
	private TextView                              mGoneTwoType;


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

	   mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mGoneOneType = (TextView) layout.findViewById(R.id.gone_one_type);
	   mAddressOne = (EditText) layout.findViewById(R.id.address_one);
	   mAddressTwo = (TextView) layout.findViewById(R.id.address_two);
	   mAddressThree = (TextView) layout.findViewById(R.id.address_three);
	   mAddressFour = (TextView) layout.findViewById(R.id.address_four);
	   mAddressFive = (TextView) layout.findViewById(R.id.address_five);
	   mGoneTwoType = (TextView) layout.findViewById(R.id.gone_two_type);
	   mGoneThreeType = (TextView) layout.findViewById(R.id.gone_three_type);
	   mGoneFourType = (TextView) layout.findViewById(R.id.gone_four_type);
	   mGoneFiveType = (TextView) layout.findViewById(R.id.gone_five_type);

	   mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);

	   mAddressOne.addTextChangedListener(new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		   String trim = mAddressOne.getText().toString().trim();
		   if (mGoneOneType.getText().toString().length()==0){
			getHospDate(trim, mAddressOne, mGoneOneType, 1);
		   }
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	   });
	   //所属院区
	   mAddressTwo.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   if (mAddressOne.getText().toString().trim().length() > 0 ) {
			String trim = mGoneOneType.getText().toString().trim();
			LogUtils.i(TAG,"mGoneOneType   "+trim);
			getHospBranch(trim, mAddressTwo, mGoneTwoType, 2);
		   }

		}
	   });
	   mAddressThree.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   String trim = mGoneTwoType.getText().toString().trim();
		   LogUtils.i(TAG,"mGoneTwoType   "+trim);
		   getHospDept(trim, mAddressThree, mGoneThreeType, 3);
		}
	   });
	   mAddressFour.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   String trim = mGoneThreeType.getText().toString().trim();
		   LogUtils.i(TAG,"mGoneThreeType   "+trim);
		   getHospBydept(trim, mAddressFour, mGoneFourType, 4);
		}
	   });
	   mAddressFive.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   String trim = mGoneThreeType.getText().toString().trim();
		   LogUtils.i(TAG,"mGoneFourType   "+trim);
		   getHospRooms(trim, mAddressFive, mGoneFiveType, 5);
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
		public void onClick(View v) {
		   if (myListener!=null){
			String deptCode = mGoneThreeType.getText().toString().trim();
			String storehouseCode = mGoneFourType.getText().toString().trim();
			String operationRoomNo = mGoneFiveType.getText().toString().trim();
			String branchCode = mGoneTwoType.getText().toString().trim();
			myListener.getDialogDate(branchCode,deptCode,storehouseCode,operationRoomNo,dialog);
		   }
		}
	   });

	   return dialog;
	}


	private SettingListener myListener = null;
	public interface SettingListener {
	   public void getDialogDate(String branchCode,String deptCode,String storehouseCode,String operationRoomNo,Dialog dialog);
	}
	public void setOnSettingListener(SettingListener listener) {
	   myListener = listener;
	}
	/**
	 * 获取医院名字
	 */
	private void getHospDate(String name, TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospNameDate(name, mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   LogUtils.i(TAG, "result   " + result);
		   setAdapterDate(hospNameBean, textview, goneview, type);

		}
	   });
	}

	/**
	 * 给列表赋值
	 *
	 * @param hospNameBean
	 * @param textview
	 * @param goneview
	 */
	private void setAdapterDate(
		HospNameBean hospNameBean, TextView textview, TextView goneview, int type) {
	   if (hospNameBean != null) {
		mMhospWindow = new hospitalPopupWindow(mContext, hospNameBean, type);
		mMhospWindow.showPopupWindow(textview);
		adapterOnClick(mMhospWindow,textview, goneview, type);
	   } else {
		mMhospWindow.dismiss();
	   }
	}

	/**
	 * adapter点击
	 *
	 * @param mMhospWindow
	 * @param goneview
	 * @param type
	 */
	private void adapterOnClick(hospitalPopupWindow mMhospWindow,TextView textview, TextView goneview, int type) {
	   if (type == 1) {
		mMhospWindow.mHospPopAdar.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(
				   BaseQuickAdapter adapter, View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				String trim = textView.getText().toString().trim();
				String mGoneText = mGoneMeal.getText().toString().trim();
				mAddressOne.setText(trim);
				mAddressOne.clearFocus();
				goneview.setText(mGoneText);
				mMhospWindow.dismiss();
			   }
			});
	   } else if (type == 2) {
		mMhospWindow.mHospPopTwoAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(
				   BaseQuickAdapter adapter, View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				String trim = textView.getText().toString().trim();
				mAddressTwo.setText(trim);
				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mAddressOne.clearFocus();
				mMhospWindow.dismiss();
			   }
			});
	   } else if (type == 3) {
		mMhospWindow.mThreeAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(
				   BaseQuickAdapter adapter, View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				String trim = textView.getText().toString().trim();
				mAddressThree.setText(trim);

				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mMhospWindow.dismiss();

			   }
			});
	   } else if (type == 4) {
		mMhospWindow.mFourAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				String trim = textView.getText().toString().trim();
				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mAddressFour.setText(trim);
				mMhospWindow.dismiss();
			   }
			});

	   } else {
		mMhospWindow.mFiveAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				String trim = textView.getText().toString().trim();
				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mAddressFive.setText(trim);
				goneview.setText(mGoneText);
				mMhospWindow.dismiss();
			   }
			});

	   }

	}

	/**
	 * 根据医院id查询院区信息
	 */
	private void getHospBranch(
		String hospIds, TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospBranch(hospIds, mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   LogUtils.i(TAG, "result getHospBranch  " + result);
		   setAdapterDate(hospNameBean, textview, goneview, type);
		}
	   });
	}

	/**
	 * 根据院区编码查询科室信息
	 */
	private void getHospDept(String branchCode, TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospDept(branchCode, mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   LogUtils.i(TAG, "result getHospDept   " + result);
		   setAdapterDate(hospNameBean, textview, goneview, type);
		}
	   });
	}

	/**
	 * 根据科室查询库房情况
	 */
	private void getHospBydept(String deptCode, TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospBydept(deptCode, mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   LogUtils.i(TAG, "result getHospBydept   " + result);
		   setAdapterDate(hospNameBean, textview, goneview, type);
		}
	   });
	}

	/**
	 * 根据科室查询手术间
	 */
	private void getHospRooms(String deptCode, TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospRooms(deptCode, mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   LogUtils.i(TAG, "result getHospRooms   " + result);
		   setAdapterDate(hospNameBean, textview, goneview, type);
		}
	   });
	}
   }

}
