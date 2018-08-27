package high.rivamed.myapplication.adapter;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/2 18:12
 * 描述:        后绑定患者
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class AfterBingAdapter extends BaseQuickAdapter<TCstInventoryVo, BaseViewHolder> {

   SparseBooleanArray checkStates1;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   public TextView mSeven_seven;

   public AfterBingAdapter(
	   int layoutResId, List<TCstInventoryVo> data) {
	super(layoutResId, data);
   }


   @Override
   protected void convert(
	   BaseViewHolder helper, TCstInventoryVo item) {
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}

	CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));

	//	mCheckBox.setChecked(checkStates1.get(helper.getAdapterPosition()));

	mSeven_two.setText(item.getCstName());
	mSeven_three.setText(item.getEpc());
	mSeven_four.setText(item.getCstSpec());
	mSeven_five.setText(item.getExpiration());
	mSeven_six.setText(item.getDeviceName());

	if (item.getPatientName() == null || item.getPatientName().length() < 1) {
	   mSeven_seven.setText("");
	} else {
	   if (mCheckBox.isChecked()&&mSeven_seven.getText().toString().trim().equals("")) {
		mSeven_seven.setText(item.getPatientName() + " / " + item.getPatientId());
	   }
	}

	UIUtils.initTermOfValidity(mContext, helper, item.getStopFlag(), mSeven_five);
   }
}
