package high.rivamed.myapplication.adapter;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;

/**
 * 创建临时患者
 */
public class BindTemporaryAdapter extends BaseQuickAdapter<BingFindSchedulesBean.PatientInfosBean, BaseViewHolder> {
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    public int mSelectedPos;
    private List<BingFindSchedulesBean.PatientInfosBean> patientInfos = new ArrayList<>();

    public BindTemporaryAdapter(
            int layoutResId, List<BingFindSchedulesBean.PatientInfosBean> data) {
        super(layoutResId, data);
    }

    public BindTemporaryAdapter(
            int layout, List<BingFindSchedulesBean.PatientInfosBean> patientInfos, SparseBooleanArray checkStates1) {
        super(layout, patientInfos);
        this.patientInfos = patientInfos;
    }

    @Override
    protected void convert(
            BaseViewHolder helper, BingFindSchedulesBean.PatientInfosBean item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }
        if (mSelectedPos == 0 && patientInfos.size() > 0) {
            patientInfos.get(mSelectedPos).setSelected(true);
        }
        for (int i = 0; i < patientInfos.size(); i++) {
            if (patientInfos.get(i).isSelected()) {
                mSelectedPos = i;
            }
        }

        CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = ((TextView) helper.getView(R.id.seven_six));
        mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));

        if (item.getSex()!=null){
            mSeven_two.setText(item.getPatientName()+" - "+item.getSex());
        }else {
            mSeven_two.setText(item.getPatientName());
        }
        mSeven_four.setText(item.getScheduleDateTime());
        mSeven_five.setText(item.getOperationSurgeonName());
        mSeven_six.setText(item.getOperatingRoomNoName());
        if (item.getPatientId().equals("virtual")) {
            mSeven_three.setText("");
            mSeven_seven.setText("是");
        } else {
            mSeven_seven.setText("否");
            mSeven_three.setText(item.getPatientId());
        }
        mCheckBox.setOnCheckedChangeListener(null);
        int position = helper.getAdapterPosition();
        mCheckBox.setChecked(item.isSelected());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientInfos.get(mSelectedPos).setSelected(false);
                //设置新的Item勾选状态
                mSelectedPos = position;
                patientInfos.get(mSelectedPos).setSelected(true);
                notifyDataSetChanged();
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                patientInfos.get(mSelectedPos).setSelected(false);
                //设置新的Item勾选状态
                mSelectedPos = position;
                patientInfos.get(mSelectedPos).setSelected(true);
                notifyDataSetChanged();
            }
        });

    }
}
