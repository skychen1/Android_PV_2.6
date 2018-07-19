package high.rivamed.myapplication.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import high.rivamed.myapplication.R;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 9:22
 * 描述:        自定义密码输入框
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class VerificationCodeView extends RelativeLayout {

   private LinearLayout containerEt;

   private EditText et;
   // 输入框数量
   private int      mEtNumber;
   // 输入框的宽度
   private int      mEtWidth;
   //输入框分割线
   private Drawable mEtDividerDrawable;
   //输入框文字颜色
   private int      mEtTextColor;
   //输入框文字大小
   private float    mEtTextSize;
   //输入框获取焦点时背景
   private Drawable mEtBackgroundDrawableFocus;
   //输入框没有焦点时背景
   private Drawable mEtBackgroundDrawableNormal;
   //是否是密码模式
   private boolean  mEtPwd;
   //密码模式时圆的半径
   private float    mEtPwdRadius;

   //存储TextView的数据 数量由自定义控件的属性传入
   private PwdTextView[] mPwdTextViews;

   private MyTextWatcher myTextWatcher = new MyTextWatcher();


   public VerificationCodeView(Context context) {
	this(context, null);
   }
   public VerificationCodeView(Context context, AttributeSet attrs) {
	this(context, attrs, 0);
   }

   public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
	super(context, attrs, defStyleAttr);
	init(context, attrs, defStyleAttr);
   }

   //初始化 布局和属性
   private void init(Context context, AttributeSet attrs, int defStyleAttr) {
	LayoutInflater.from(context).inflate(R.layout.layout_identifying_code, this);
	containerEt = (LinearLayout) this.findViewById(R.id.container_et);
	et = (EditText) this.findViewById(R.id.et);

	TypedArray typedArray = context.obtainStyledAttributes(attrs,
										 R.styleable.VerificationCodeView,
										 defStyleAttr, 0);
	mEtNumber = typedArray.getInteger(R.styleable.VerificationCodeView_icv_et_number, 1);
	mEtWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_icv_et_width,
								  42);
	mEtDividerDrawable = typedArray.getDrawable(
		R.styleable.VerificationCodeView_icv_et_divider_drawable);
	mEtTextSize = typedArray.getDimensionPixelSize(
		R.styleable.VerificationCodeView_icv_et_text_size, (int) sp2px(16, context));
	mEtTextColor = typedArray.getColor(R.styleable.VerificationCodeView_icv_et_text_color,
						     Color.BLACK);
	mEtBackgroundDrawableFocus = typedArray.getDrawable(
		R.styleable.VerificationCodeView_icv_et_bg_focus);
	mEtBackgroundDrawableNormal = typedArray.getDrawable(
		R.styleable.VerificationCodeView_icv_et_bg_normal);
	mEtPwd = typedArray.getBoolean(R.styleable.VerificationCodeView_icv_et_pwd, false);
	mEtPwdRadius = typedArray.getDimensionPixelSize(
		R.styleable.VerificationCodeView_icv_et_pwd_radius, 0);
	//释放资源
	typedArray.recycle();

	// 当xml中未配置时 这里进行初始配置默认图片
	if (mEtDividerDrawable == null) {
	   mEtDividerDrawable = context.getResources()
		   .getDrawable(R.drawable.shape_divider_identifying);
	}

	if (mEtBackgroundDrawableFocus == null) {
	   mEtBackgroundDrawableFocus = context.getResources()
		   .getDrawable(R.drawable.shape_icv_et_bg_focus);
	}

	if (mEtBackgroundDrawableNormal == null) {
	   mEtBackgroundDrawableNormal = context.getResources()
		   .getDrawable(R.drawable.shape_icv_et_bg_normal);
	}

	initUI();
   }

   // 初始UI
   private void initUI() {
	initTextViews(getContext(), mEtNumber, mEtWidth, mEtDividerDrawable, mEtTextSize,
			  mEtTextColor);
	initEtContainer(mPwdTextViews);
	setListener();
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// 设置当 高为 warpContent 模式时的默认值 为 50dp
	int mHeightMeasureSpec = heightMeasureSpec;

	int heightMode = MeasureSpec.getMode(mHeightMeasureSpec);
	if (heightMode == MeasureSpec.AT_MOST) {
	   mHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) dp2px(75, getContext()),
									    MeasureSpec.EXACTLY);
	}

	super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
   }

   //初始化TextView
   private void initTextViews(
	   Context context, int etNumber, int etWidth, Drawable etDividerDrawable, float etTextSize,
	   int etTextColor) {
	// 设置 editText 的输入长度

	et.setCursorVisible(false);//将光标隐藏
	et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etNumber)}); //最大输入长度
	// 设置分割线的宽度
	if (etDividerDrawable != null) {
	   etDividerDrawable.setBounds(0, 0, etDividerDrawable.getMinimumWidth(),
						 etDividerDrawable.getMinimumHeight());
	   containerEt.setDividerDrawable(etDividerDrawable);
	}
	mPwdTextViews = new PwdTextView[etNumber];

	for (int i = 0; i < mPwdTextViews.length; i++) {
	   PwdTextView textView = new PwdTextView(context);
	   textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, etTextSize);
	   textView.setTextColor(etTextColor);
	   textView.setWidth(etWidth);
	   textView.setHeight(etWidth);
	   if (i == 0) {
		textView.setBackgroundDrawable(mEtBackgroundDrawableFocus);
	   } else {
		textView.setBackgroundDrawable(mEtBackgroundDrawableNormal);
	   }
	   textView.setGravity(Gravity.CENTER);

	   textView.setFocusable(false);

	   mPwdTextViews[i] = textView;
	}
   }

   //初始化存储TextView 的容器
   private void initEtContainer(TextView[] mTextViews) {
	for (TextView mTextView : mTextViews) {
	   containerEt.addView(mTextView);
	}
   }

   private void setListener() {
	// 监听输入内容
	et.addTextChangedListener(myTextWatcher);

	// 监听删除按键
	et.setOnKeyListener(new OnKeyListener() {
	   @Override
	   public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
		   onKeyDelete();
		   return true;
		}
		return false;
	   }
	});
   }

   // 给TextView 设置文字
   public void setText(String inputContent) {

	for (int i = 0; i < mPwdTextViews.length; i++) {
	   PwdTextView tv = mPwdTextViews[i];
	   if (tv.getText().toString().trim().equals("")) {
		if (mEtPwd) {
		   tv.drawPwd(mEtPwdRadius);
		}
		tv.setText(inputContent);
		// 添加输入完成的监听
		if (inputCompleteListener != null) {
		   inputCompleteListener.inputComplete();
		}
		tv.setBackgroundDrawable(mEtBackgroundDrawableNormal);
		if (i < mEtNumber - 1) {
		   mPwdTextViews[i + 1].setBackgroundDrawable(mEtBackgroundDrawableFocus);
		}
		break;
	   }
	}
   }

   // 监听删除
   public void onKeyDelete() {
	for (int i = mPwdTextViews.length - 1; i >= 0; i--) {
	   PwdTextView tv = mPwdTextViews[i];
	   if (!tv.getText().toString().trim().equals("")) {
		if (mEtPwd) {
		   tv.clearPwd();
		}
		tv.setText("");
		// 添加删除完成监听
		if (inputCompleteListener != null) {
		   inputCompleteListener.deleteContent();
		}
		tv.setBackgroundDrawable(mEtBackgroundDrawableFocus);
		if (i < mEtNumber - 1) {
		   mPwdTextViews[i + 1].setBackgroundDrawable(mEtBackgroundDrawableNormal);
		}
		break;
	   }
	}
   }

   /**
    * 获取输入文本
    *
    * @return string
    */
   public String getInputContent() {
	StringBuffer buffer = new StringBuffer();
	for (TextView tv : mPwdTextViews) {
	   buffer.append(tv.getText().toString().trim());
	}
	return buffer.toString();
   }

   /**
    * 删除输入内容
    */
   public void clearInputContent() {
	for (int i = 0; i < mPwdTextViews.length; i++) {
	   if (i == 0) {
		mPwdTextViews[i].setBackgroundDrawable(mEtBackgroundDrawableFocus);
	   } else {
		mPwdTextViews[i].setBackgroundDrawable(mEtBackgroundDrawableNormal);
	   }
	   if (mEtPwd) {
		mPwdTextViews[i].clearPwd();
	   }
	   mPwdTextViews[i].setText("");
	}
   }

   /**
    * 设置输入框个数
    *
    * @param etNumber
    */
   public void setEtNumber(int etNumber) {
	this.mEtNumber = etNumber;
	et.removeTextChangedListener(myTextWatcher);
	containerEt.removeAllViews();
	initUI();
   }

   /**
    * 获取输入的位数
    *
    * @return int
    */
   public int getEtNumber() {
	return mEtNumber;
   }

   /**
    * 设置是否是密码模式 默认不是
    *
    * @param isPwdMode
    */
   public void setPwdMode(boolean isPwdMode) {
	this.mEtPwd = isPwdMode;
   }

   /**
    * 获取输入的EditText 用于外界设置键盘弹出
    *
    * @return
    */
   public EditText getEditText() {
	return et;
   }

   // 输入完成 和 删除成功 的监听
   private InputCompleteListener inputCompleteListener;

   public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
	this.inputCompleteListener = inputCompleteListener;
   }

   public interface InputCompleteListener {

	void inputComplete();

	void deleteContent();
   }

   public float dp2px(float dpValue, Context context) {
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
						   context.getResources().getDisplayMetrics());
   }

   public float sp2px(float spValue, Context context) {
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
						   context.getResources().getDisplayMetrics());
   }

   private class MyTextWatcher implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable editable) {
	   String inputStr = editable.toString();
	   if (!TextUtils.isEmpty(inputStr)) {
		setText(inputStr);
		et.setText("");
	   }
	}
   }

}
