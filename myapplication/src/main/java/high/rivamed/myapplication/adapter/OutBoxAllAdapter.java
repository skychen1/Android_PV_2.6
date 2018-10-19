package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * 创建时间:    2018/7/30 19:24
 * 描述:        快速出柜的
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OutBoxAllAdapter extends BaseQuickAdapter<TCstInventoryVo, BaseViewHolder> {

   public SparseBooleanArray mCheckStates;
   public  CheckBox           mMCheckBox;
   private TextView           mSeven_two;
   private TextView           mSeven_three;
   private TextView           mSeven_four;
   private TextView           mSeven_five;
   private TextView           mSeven_six;
   public int mSelectedPos;
   private List<TCstInventoryVo> mDataVo ;
   public OutBoxAllAdapter(
	   int layoutResId, @Nullable List<TCstInventoryVo> data) {
	super(layoutResId, data);
	this.mDataVo = data;
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, TCstInventoryVo item) {
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}


	mMCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	mSeven_six = ((TextView) helper.getView(R.id.seven_six));



	mSeven_two.setText(item.getCstName());
	mSeven_three.setText(item.getEpc());
	mSeven_four.setText(item.getCstSpec());
	mSeven_five.setText(item.getExpiration());
	mSeven_six.setText(item.getDeviceName());
	UIUtils.initTermOfValidity(mContext,helper, item.getStopFlag(), mSeven_five);

	mMCheckBox.setOnCheckedChangeListener(null);

	mMCheckBox.setChecked(item.isSelected());

//	helper.itemView.setOnClickListener(new View.OnClickListener() {
//	   @Override
//	   public void onClick(View view) {
//		int position = helper.getAdapterPosition();
//		LogUtils.i(TAG," position    "+position  +"     mDataVo.get(position).isSelected()   "+mDataVo.get(position).isSelected());
//		if (item.isSelected()){
//		   mData.get(position).setSelected(false);
//		}else {
//		   mData.get(position).setSelected(true);
//		}
////		((CheckBox)helper.itemView.getRootView().findViewById(R.id.seven_one)).setChecked(item.isSelected());
//		notifyDataSetChanged();
//		//设置新的Item勾选状态
////		mMCheckBox.setChecked(item.isSelected());
////		notifyDataSetChanged();
//	   }
//	});
	mMCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		int position = helper.getAdapterPosition();
		if (item.isSelected()){
		   item.setSelected(false);
		}else {
		   item.setSelected(true);
		}
//		BaseTimelyActivity.mOutDto.settCstInventoryVos(mTCstInventoryVos);
//		LogUtils.i("Out", " position    " + position + "     mDataVo.get(position).isSelected()   " + mTCstInventoryVos.get(position).isSelected());
		notifyDataSetChanged();
//		mMCheckBox.setChecked(mDataVo.get(position).isSelected());
		//设置新的Item勾选状态
//		notifyDataSetChanged();
	   }
	});
   }
}
