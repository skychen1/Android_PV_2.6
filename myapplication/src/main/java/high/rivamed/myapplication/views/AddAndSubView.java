package high.rivamed.myapplication.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import high.rivamed.myapplication.R;

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
public class AddAndSubView extends LinearLayout implements View.OnClickListener, TextWatcher {

   private static final String TAG    = "AddAndSubView";
   private              int    amount = 15; //购买数量
   private int goods_storage; //商品库存

   private OnAmountChangeListener mListener;

   private TextView etAmount;
   private Button   mBtnSub;
   private Button   mBtnAdd;

   public AddAndSubView(Context context) {
	this(context, null);
   }

   public AddAndSubView(Context context, AttributeSet attrs) {
	super(context, attrs);

	LayoutInflater.from(context).inflate(R.layout.view_addandsub, this);
	etAmount = (TextView) findViewById(R.id.et_number);
	mBtnSub = (Button) findViewById(R.id.btn_sub);
	mBtnAdd = (Button) findViewById(R.id.btn_add);
	etAmount.setText(amount + "");
	String string = amount + "";
//	etAmount.setSelection(string.length());

	mBtnSub.setOnClickListener(this);
	mBtnAdd.setOnClickListener(this);
	etAmount.addTextChangedListener(this);

	TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs,
													R.styleable.AddAndSubView);
	obtainStyledAttributes.recycle();
   }

   public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
	this.mListener = onAmountChangeListener;
   }

   public void setGoods_storage(int goods_storage) {
	this.goods_storage = goods_storage;
   }

   @Override
   public void onClick(View v) {
	int i = v.getId();
	if (i == R.id.btn_sub) {
	   if (amount > 1) {
		amount--;
		String s = amount + "";
		etAmount.setText(s);
	   }
	} else if (i == R.id.btn_add) {
	   if (amount < goods_storage) {
		amount++;
		String s = amount + "";
		etAmount.setText(s);
	   }
	}

	etAmount.clearFocus();

	if (mListener != null) {
	   mListener.onAmountChange(this, amount);
	}
   }

   @Override
   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

   }

   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count) {

   }

   @Override
   public void afterTextChanged(Editable s) {
	if (s.toString().isEmpty()) {
	   return;
	}
	if (etAmount.getText().toString().matches("^0")) {//判断当前的输入第一个数是不是为0
	   etAmount.setText("");
	}
	amount = Integer.valueOf(s.toString());
	if (amount > goods_storage) {
	   String s1 = goods_storage + "";
	   etAmount.setText(s1);
	   return;
	}

	if (mListener != null) {
	   mListener.onAmountChange(this, amount);
	}
   }

   public interface OnAmountChangeListener {

	void onAmountChange(View view, int amount);
   }

   public String getAmount() { return etAmount.getText().toString().trim();}
}
