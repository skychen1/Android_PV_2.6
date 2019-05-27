package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/2 9:00
 * 描述:        盘亏盘盈
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimelyLossAdapter
	extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {
   private CheckBox          mCheckBox;
   private TextView          mSeven_two;
   private TextView          mSeven_three;
   private TextView          mSeven_four;
   private TextView          mSeven_five;
   private TextView          mSeven_six;
   private TextView          mSeven_seven;
   public int                mSelectedPos;
   public  List<InventoryVo> mLossData;
   public TimelyLossAdapter(
	   int layoutResId, @Nullable List<InventoryVo> data) {
	super(layoutResId, data);
	this.mLossData =data;

   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {
//	if (helper.getAdapterPosition() % 2 == 0) {
//	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
//	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
//	}
//	if (mSelectedPos == 0 && mLossData.size() > 0) {
//	   mLossData.get(mSelectedPos).setSelected(true);
//	}
	for (int i = 0; i < mLossData.size(); i++) {
	   if (mLossData.get(i).isSelected()) {
		mSelectedPos = i;
	   }
	}
	mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));

	mSeven_two.setText(item.getCstName());
	mSeven_three.setText(item.getEpc());
	mSeven_four.setText(item.getCstSpec());
	mSeven_five.setText(item.getExpiryDate());
	mSeven_six.setText(item.getDeviceName());
	if (item.getRemark()==null||item.getRemark().equals("")){
	   mSeven_seven.setText("");
	}else {
	   mSeven_seven.setText(item.getRemark());

	}

	mCheckBox.setOnCheckedChangeListener(null);
	int position = helper.getAdapterPosition();
	mCheckBox.setChecked(item.isSelected());
	helper.itemView.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
//		mLossData.get(mSelectedPos).setSelected(false);
		//设置新的Item勾选状态
		mSelectedPos = position;
		if (mLossData.get(mSelectedPos).isSelected()){
		   mLossData.get(mSelectedPos).setSelected(false);
		}else {
		   mLossData.get(mSelectedPos).setSelected(true);
		}

		notifyDataSetChanged();
	   }
	});
	mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//		mLossData.get(mSelectedPos).setSelected(false);
		//设置新的Item勾选状态
		mSelectedPos = position;
		if (mLossData.get(mSelectedPos).isSelected()){
		   mLossData.get(mSelectedPos).setSelected(false);
		}else {
		   mLossData.get(mSelectedPos).setSelected(true);
		}

		notifyDataSetChanged();
	   }
	});


	int StopFlag = item.getExpireStatus();
	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), StopFlag, mSeven_five);
	}
   }
}
