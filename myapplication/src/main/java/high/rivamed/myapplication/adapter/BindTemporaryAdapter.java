package high.rivamed.myapplication.adapter;

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
public class BindTemporaryAdapter extends BaseQuickAdapter<BingFindSchedulesBean.PatientInfoVos, BaseViewHolder> {
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;
    private TextView mSeven_nine;
    public int mSelectedPos;
    public String mDeptType;
    public int mSize;
    private List<BingFindSchedulesBean.PatientInfoVos> patientInfos = new ArrayList<>();


    public BindTemporaryAdapter(
            int layout, List<BingFindSchedulesBean.PatientInfoVos> patientInfos,String mDeptType,int mSize) {
        super(layout, patientInfos);
        this.patientInfos = patientInfos;
        this.mDeptType = mDeptType;
        this.mSize = mSize;
    }

    @Override
    protected void convert(
            BaseViewHolder helper, BingFindSchedulesBean.PatientInfoVos item) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
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

        if (mSize==10){
            if (item.getWardName()==null||item.getWardName().equals("")){
                helper.setText(R.id.seven_eight, "/");
            }else {
                helper.setText(R.id.seven_eight, item.getWardName());
            }
            if (item.getBedNo()==null||item.getBedNo().equals("")){
                helper.setText(R.id.seven_nine,"/");
            }else {
                helper.setText(R.id.seven_nine, item.getBedNo());
            }
            if (item.getSurgeryName()==null||item.getSurgeryName().equals("")){
                helper.setText(R.id.seven_ten,"/");
            }else {
                helper.setText(R.id.seven_ten, item.getSurgeryName());
            }
        }
        if (item.getSex()!=null){
            mSeven_two.setText(item.getPatientName()+" - "+item.getSex());
        }else {
            mSeven_two.setText(item.getPatientName());
        }
        mSeven_four.setText(item.getSurgeryTime());
        mSeven_five.setText(item.getDoctorName());

        if (item.getPatientId()!=null){
            if (item.getPatientId().equals("virtual")) {
                mSeven_three.setText("/");
                mSeven_seven.setText("是");
                mSeven_five.setText("/");
                if (mDeptType==null||!mDeptType.equals("1")){
                    mSeven_four.setText(item.getSurgeryTime());
                    mSeven_six.setText(item.getRoomName());
                }else {
                    mSeven_four.setText("/");
                    mSeven_six.setText("/");
                }
            } else {
                mSeven_seven.setText("否");
                mSeven_three.setText(item.getHisPatientId());

		   if (mDeptType==null||!mDeptType.equals("1")){
			mSeven_six.setText(item.getRoomName());
		   }else {
			mSeven_six.setText(item.getDeptName());
		   }
            }
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
