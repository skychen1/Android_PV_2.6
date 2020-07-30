package high.rivamed.myapplication.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/3 13:40
 * 描述:        选择绑定的患者
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BingDialogOutAdapter
	extends BaseQuickAdapter<BingFindSchedulesBean.PatientInfoVos, BaseViewHolder> {

   private int                                        mSelectedPos = -1;
   private int                                        mSize;
   private List<BingFindSchedulesBean.PatientInfoVos> patientInfos;

   public BingDialogOutAdapter(
	   int layout, List<BingFindSchedulesBean.PatientInfoVos> patientInfos, int size) {
	super(layout, patientInfos);
	this.patientInfos = patientInfos;
	this.mSize = size;
   }

//   public int getCheckedPosition() {
//	return mSelectedPos;
//   }

   @Override
   protected void convert(
	   BaseViewHolder helper, BingFindSchedulesBean.PatientInfoVos item) {
//	if (mSelectedPos == 0) {
//	   patientInfos.get(mSelectedPos).setSelected(true);
//	}
//	for (int i = 0; i < patientInfos.size(); i++) {
//	   if (patientInfos.get(i).isSelected()) {
//		mSelectedPos = i;
//	   }
//	}
	((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));

	helper.setText(R.id.seven_two, item.getPatientName())
		.setText(R.id.seven_three, item.getHisPatientId())
		.setText(R.id.seven_four, item.getSurgeryTime())
		.setText(R.id.seven_five, item.getDoctorName())
		.setText(R.id.seven_six, item.getRoomName());
//	if (mSize == 8) {
//	   helper.setText(R.id.seven_seven, item.getWardName());
//	   helper.setText(R.id.seven_eight, item.getBedNo());
//	}

	mCheckBox.setOnCheckedChangeListener(null);
	int position = helper.getAdapterPosition();
	mCheckBox.setChecked(item.isSelected());
	helper.itemView.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
		if (mSelectedPos!=-1){
		   patientInfos.get(mSelectedPos).setSelected(false);
		}
		//设置新的Item勾选状态
		mSelectedPos = position;
		patientInfos.get(mSelectedPos).setSelected(true);
		notifyDataSetChanged();
	   }
	});

	mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		if (mSelectedPos!=-1){
		   patientInfos.get(mSelectedPos).setSelected(false);
		}
		//设置新的Item勾选状态
		mSelectedPos = position;
		patientInfos.get(mSelectedPos).setSelected(true);
		notifyDataSetChanged();
	   }
	});
   }
}
