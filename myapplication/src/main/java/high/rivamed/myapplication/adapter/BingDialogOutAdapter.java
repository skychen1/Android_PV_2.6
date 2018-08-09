package high.rivamed.myapplication.adapter;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	extends BaseQuickAdapter<BingFindSchedulesBean.PatientInfosBean, BaseViewHolder> {
   SparseBooleanArray mCheckStates;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   public BingDialogOutAdapter(
	   int layout, List<BingFindSchedulesBean.PatientInfosBean> patientInfos,
	   SparseBooleanArray checkStates) {
      super(layout, patientInfos);
	this.mCheckStates =	checkStates;
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, BingFindSchedulesBean.PatientInfosBean item) {
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}
	LinearLayout layout = (LinearLayout) helper.getView(R.id.seven_ll);
	CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	mSeven_two.setText(item.getPatientName());
	mSeven_three.setText(item.getPatientId());
	mSeven_four.setText(item.getRequestDateTime());
	mSeven_five.setText(item.getOperationSurgeonName());
	mSeven_six.setText(item.getOperatingRoomNoName());
	mCheckBox.setOnCheckedChangeListener(null);
	mCheckBox.setChecked(mCheckStates.get(helper.getAdapterPosition()));
	mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		mCheckStates.put(helper.getAdapterPosition(), b);
	   }
	});
   }
}