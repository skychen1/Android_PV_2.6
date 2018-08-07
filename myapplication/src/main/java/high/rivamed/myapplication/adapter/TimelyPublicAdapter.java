package high.rivamed.myapplication.adapter;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Movie;

import static high.rivamed.myapplication.cont.Constants.STYPE_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_NOBING;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
import static high.rivamed.myapplication.cont.Constants.STYPE_TIMELY_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.TYPE_TIMELY;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:08
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TimelyPublicAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {

   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   private TextView mSeven_seven;
   private TextView mSeven_eight;
   public String TAG = "TimelyPublicAdapter";
   public  int                mSize;
   public  String             mType;
   public  String             mMealBing;
   private SparseBooleanArray mCheckStates;
   private SparseBooleanArray mCheckStates2 = new SparseBooleanArray();
   public CheckBox mMCheckBox;


   public TimelyPublicAdapter(int layout, List<Movie> data, int size) {
	super(layout, data);
	this.mSize = size;
	this.mData = data;
   }

   public TimelyPublicAdapter(int layout, List<Movie> data, int size, String type) {
	super(layout, data);

	this.mSize = size;
	this.mType = type;
	this.mData = data;

	Log.i("xxx", "mSize   " + mSize);
   }

   public TimelyPublicAdapter(int layout, List<Movie> data, int size, String type, String bing) {
	super(layout, data);

	this.mSize = size;
	this.mType = type;
	this.mData = data;
	this.mMealBing = bing;

	Log.i("xxx", "mSize   " + mSize);
   }

   public TimelyPublicAdapter(
	   int layout, List<Movie> data, int size, String type, SparseBooleanArray mCheckStates) {
	super(layout, data);
	this.mData = data;
	this.mSize = size;
	this.mType = type;
	this.mCheckStates = mCheckStates;
	Log.i("xxx", "mSize   " + mSize);
   }

   public void clear() {
	if (mCheckStates != null) {
	   mCheckStates.clear();
	}
	if (mCheckStates2 != null) {
	   mCheckStates2.clear();
	}
	mData.clear();
	notifyDataSetChanged();
   }

   @Override
   protected void convert(final BaseViewHolder helper, Movie item) {
	Log.i("xxx", "xxxxxxxxxxxxxxxxxx");
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}
	if (mSize == 4) {
	   if (mType != null && mType.equals(STYPE_TIMELY_FOUR_DETAILS)) {
		findId(helper, mSize);
		String two = item.two;
		String three = item.three;
		String four = item.four;
		mSeven_one.setText(item.one);
		mSeven_two.setText(two);
		mSeven_three.setText(three);
		if (!three.equals(four)) {
		   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.color_red));
		} else {
		   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
		}
		mSeven_four.setText(item.four);
		initTermOfValidity(helper, two, mSeven_two);
	   } else {
//		findId(helper, mSize);
//		String four = item.four;
//		mSeven_one.setText(item.one);
//		mSeven_two.setText(item.two);
//		mSeven_three.setText(item.three);
//		mSeven_four.setText(four);
//		initTermOfValidity(helper, four, mSeven_four);
	   }

	} else if (mSize == 5) {
	   findId(helper, mSize);
	   String five = item.five;
	   mSeven_one.setText(item.one);
	   mSeven_two.setText(item.two);
	   mSeven_three.setText(item.three);
	   mSeven_four.setText(item.four);
	   mSeven_five.setText(five);
	   initTermOfValidity(helper, five, mSeven_five);

	} else if (mSize == 6) {
	   Log.i("xxx", "xxxxxxxxxxxxxxxxxx");
	   if (mType != null && mType.equals(STYPE_DIALOG)) {
//		LinearLayout layout = (LinearLayout) helper.getView(R.id.seven_ll);
//		CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
//		mSeven_two = ((TextView) helper.getView(R.id.seven_two));
//		mSeven_three = ((TextView) helper.getView(R.id.seven_three));
//		mSeven_four = ((TextView) helper.getView(R.id.seven_four));
//		mSeven_five = ((TextView) helper.getView(R.id.seven_five));
//		mSeven_six = ((TextView) helper.getView(R.id.seven_six));
//		mSeven_two.setText(item.two);
//		mSeven_three.setText(item.three);
//		mSeven_four.setText(item.four);
//		mSeven_five.setText(item.five);
//		mSeven_six.setText(item.six);
//		mCheckBox.setOnCheckedChangeListener(null);
//		mCheckBox.setChecked(mCheckStates.get(helper.getAdapterPosition()));
//		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//		   @Override
//		   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//			mCheckStates.put(helper.getAdapterPosition(), b);
//		   }
//		});
	   } else if (mType != null && mType.equals(STYPE_IN)) {
		findId(helper, mSize);
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(item.three);
		mSeven_four.setText(item.four);
		mSeven_five.setText(item.five);
		mSeven_six.setText(item.six);
		initTermOfValidity(helper, item.four, mSeven_four);
		if (item.six.equals("禁止操作")) {
		   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.color_red));
		   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   if (helper.getAdapterPosition() % 2 == 0) {
			mSeven_four.setBackgroundResource(R.color.bg_color);
		   } else {
			mSeven_four.setBackgroundResource(R.color.bg_f);
		   }
		}
	   } else if (mType != null && mType.equals(STYPE_OUT)) {
		mMCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
		mSeven_two = ((TextView) helper.getView(R.id.seven_two));
		mSeven_three = ((TextView) helper.getView(R.id.seven_three));
		mSeven_four = ((TextView) helper.getView(R.id.seven_four));
		mSeven_five = ((TextView) helper.getView(R.id.seven_five));
		mSeven_six = ((TextView) helper.getView(R.id.seven_six));

		if (item.seven.equals("1")) {
		   mMCheckBox.setChecked(true);
		} else {
		   Log.i("rr", "helper.getAdapterPosition()  " + helper.getAdapterPosition());
		   Log.i("rr", "mCheckStates.get(helper.getAdapterPosition())  " +
				   mCheckStates.get(helper.getAdapterPosition()));
		   mMCheckBox.setChecked(mCheckStates.get(helper.getAdapterPosition()));
		}
		mSeven_two.setText(item.one);
		mSeven_three.setText(item.two);
		mSeven_four.setText(item.three);
		mSeven_five.setText(item.four);
		mSeven_six.setText(item.five);
		initTermOfValidity(helper, item.four, mSeven_five);

		mMCheckBox.setOnCheckedChangeListener(null);
		//		mCheckBox.setChecked(mCheckStates.get(helper.getAdapterPosition()));
		mMCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		   @Override
		   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
			mCheckStates.put(helper.getAdapterPosition(), b);
		   }
		});
	   } else if (mType != null && mType.equals(STYPE_FORM_CONF)) {
		mSeven_one = ((TextView) helper.getView(R.id.seven_one));
		mSeven_two = ((TextView) helper.getView(R.id.seven_two));
		mSeven_three = ((TextView) helper.getView(R.id.seven_three));
		mSeven_four = ((TextView) helper.getView(R.id.seven_four));
		mSeven_five = ((TextView) helper.getView(R.id.seven_five));
		ImageView view = (ImageView) helper.getView(R.id.seven_six);
		if (item.seven.equals("1")) {
		   view.setVisibility(View.VISIBLE);
		} else {
		   view.setVisibility(View.INVISIBLE);
		}
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(item.three);
		mSeven_four.setText(item.four);
		mSeven_five.setText(item.five);
		initTermOfValidity(helper, item.four, mSeven_four);

	   } else if (mType != null && mType.equals(STYPE_MEAL_NOBING)) {

		findId(helper, mSize);
		mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(item.three);
		mSeven_four.setVisibility(View.GONE);
		mSeven_five.setText(item.four);
		mSeven_six.setText(item.five);
		mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
		mSeven_seven.setText(item.six);
		if (item.five.equals("已领取")) {
		   mSeven_seven.setText("");
		   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		}

	   } else if (mType != null && mType.equals(TYPE_TIMELY)) {
		findId(helper, mSize);
		String four = item.four;
		String three = item.three;
		String five = item.five;
		String six = item.six;
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(three);
		mSeven_four.setText(four);
		mSeven_five.setText(five);
		mSeven_six.setText(six);
		if (!five.equals(six)) {
		   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.color_red));
		} else {
		   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
		}
		initTermOfValidity(helper, three, mSeven_three);
	   }
	} else if (mSize == 7) {
	   if (mType != null && mType.equals(STYPE_BING)) {
//		CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
//		mSeven_two = ((TextView) helper.getView(R.id.seven_two));
//		mSeven_three = ((TextView) helper.getView(R.id.seven_three));
//		mSeven_four = ((TextView) helper.getView(R.id.seven_four));
//		mSeven_five = ((TextView) helper.getView(R.id.seven_five));
//		mSeven_six = ((TextView) helper.getView(R.id.seven_six));
//		mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
//
//		if (item.seven.equals("1")) {
//		   mCheckBox.setChecked(true);
//		} else {
//		   mCheckBox.setChecked(mCheckStates.get(helper.getAdapterPosition()));
//		}
//		mSeven_two.setText(item.one);
//		mSeven_three.setText(item.two);
//		mSeven_four.setText(item.three);
//		mSeven_five.setText(item.four);
//		mSeven_six.setText(item.five);
//		mSeven_seven.setText(item.six);
//		initTermOfValidity(helper, item.four, mSeven_five);
	   } else if (mType != null && mType.equals(STYPE_FORM)) {
		findId(helper, mSize);
		LinearLayout layout = (LinearLayout) helper.getView(R.id.seven_ll);
		String four = item.four;
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(item.three);
		mSeven_four.setText(four);
		mSeven_five.setText(item.five);
		mSeven_six.setText(item.six);
		mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
		mSeven_seven.setText(item.seven);
		if (item.six.equals("已领取")) {
		   mSeven_seven.setText("");
		   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		}
	   } else if (mType != null && mType.equals(STYPE_MEAL_NOBING) && mMealBing != null &&
			  mMealBing.equals("BING_MEAL")) {
		findId(helper, mSize);
		//		   LinearLayout layout = (LinearLayout) helper.getView(R.id.seven_ll);
		mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(item.three);
		mSeven_four.setText(item.four);
		mSeven_five.setText(item.five);
		mSeven_six.setText(item.six);
		mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
		mSeven_seven.setText(item.seven);
		if (item.six.equals("已领取")) {
		   mSeven_seven.setText("");
		   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
		}
	   } else {
		findId(helper, mSize);
		String four = item.four;
		String six = item.six;
		String seven = item.seven;
		mSeven_one.setText(item.one);
		mSeven_two.setText(item.two);
		mSeven_three.setText(item.three);
		mSeven_four.setText(four);
		mSeven_five.setText(item.five);
		mSeven_six.setText(six);
		mSeven_seven.setText(seven);
		if (!six.equals(seven)) {
		   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.color_red));
		} else {
		   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
		}

		initTermOfValidity(helper, four, mSeven_four);
	   }
	} else if (mSize == 8) {
	   if (mType != null && mType.equals(STYPE_MEAL_BING)) {
		CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
		mSeven_two = ((TextView) helper.getView(R.id.seven_two));
		mSeven_three = ((TextView) helper.getView(R.id.seven_three));
		mSeven_four = ((TextView) helper.getView(R.id.seven_four));
		mSeven_five = ((TextView) helper.getView(R.id.seven_five));
		mSeven_six = ((TextView) helper.getView(R.id.seven_six));
		mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
		ImageView view = (ImageView) helper.getView(R.id.seven_eight);


		mSeven_two.setText(item.one);
		mSeven_three.setText(item.two);
		mSeven_four.setText(item.three);
		mSeven_five.setText(item.four);
		mSeven_six.setText(item.five);
		mSeven_seven.setText(item.six);
		if (item.seven.equals("1")) {
		   view.setVisibility(View.VISIBLE);
		} else {
		   view.setVisibility(View.INVISIBLE);
		}

		mCheckBox.setOnCheckedChangeListener(null);
		mCheckBox.setChecked(mCheckStates.get(helper.getAdapterPosition()));
		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		   @Override
		   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
			mCheckStates.put(helper.getAdapterPosition(), b);
		   }
		});
		initTermOfValidity(helper, item.four, mSeven_five);
	   } else {

//		findId(helper, mSize);
//		String five = item.five;
//		String one = item.one;
//		mSeven_one.setText(item.one);
//		mSeven_two.setText(item.two);
//		mSeven_three.setText(item.three);
//		mSeven_four.setText(item.four);
//		mSeven_five.setText(five);
//		mSeven_six.setText(item.six);
//		mSeven_seven.setText(item.seven);
//		mSeven_eight.setText(item.eight);
//
//		initTermOfValidity(helper, five, mSeven_five);
//
//		if (one.equals("领用") || one.equals("移出") || one.equals("退货") ||
//		    (mType != null && mType.equals(STYPE_STOCK_RIGHT))) {
//		   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.color_red));
//		} else {
//		   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.color_green));
//		}
	   }
	}
   }

   /**
    * 设置某个效期的背景
    *
    * @param helper
    * @param text
    * @param textview
    */
   private void initTermOfValidity(BaseViewHolder helper, String text, TextView textview) {

	if (text.equals("已过期")) {
	   textview.setBackgroundResource(R.drawable.bg_text_red);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (text.equals("≤100天")) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow1);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (text.equals("≤70天")) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow2);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (text.equals("≤28天")) {
	   textview.setBackgroundResource(R.drawable.bg_text_orange);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else {
	   if (helper.getAdapterPosition() % 2 == 0) {
		textview.setBackgroundResource(R.color.bg_color);
	   } else {
		textview.setBackgroundResource(R.color.bg_f);
	   }
	   textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}

   }

   private void findId(BaseViewHolder helper, int size) {
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	if (size == 6) {
	   mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	} else if (size == 7) {
	   mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	   mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
	} else if (size == 8) {
	   mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	   mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
	   mSeven_eight = ((TextView) helper.getView(R.id.seven_eight));
	}

   }
}
