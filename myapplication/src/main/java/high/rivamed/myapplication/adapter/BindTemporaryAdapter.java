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
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.CONFIG_012;

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
    public int mSelectedPos=-1;
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

        CheckBox mCheckBox = ((CheckBox) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = ((TextView) helper.getView(R.id.seven_six));
        mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
        if (UIUtils.getConfigType(mContext, CONFIG_012)){
            mSeven_seven.setVisibility(View.VISIBLE);
        }else {
            mSeven_seven.setVisibility(View.GONE);
        }
        if (mSize==10){
            mSeven_five.setText(item.getPatientName()!=null? item.getPatientName():"/");
            mSeven_two.setText(item.getPatientWard()!=null? item.getPatientWard():"/");
            mSeven_three.setText(item.getFloor()!=null? item.getFloor():"/");
            mSeven_four.setText(item.getRoomName()!=null? item.getRoomName():"/");
            mSeven_six.setText(item.getSex()!=null? item.getSex():"/");
            mSeven_seven.setText(item.getAge()!=null? item.getAge():"/");
            helper.setText(R.id.seven_eight,item.getWardName()!=null? item.getWardName():"/");
            helper.setText(R.id.seven_nine,item.getBedNo()!=null? item.getBedNo():"/");
            helper.setText(R.id.seven_ten,item.getSurgeryName()!=null? item.getSurgeryName():"/");
        }else {
            if (mSize == 8){
                if (item.getOrderDeptName()!=null&&!item.getOrderDeptName().equals("")){
                    helper.setText(R.id.seven_eight,item.getOrderDeptName());
                }else {
                    helper.setText(R.id.seven_eight,"/");
                }
            }
            if (item.getSex()!=null){
                mSeven_two.setText(item.getPatientName()+" - "+item.getSex());
            }else {
                mSeven_two.setText(item.getPatientName());
            }
            mSeven_four.setText(item.getSurgeryTime());
            mSeven_five.setText(item.getDoctorName()!=null&&item.getDoctorName().equals("")? "/":item.getDoctorName());

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

        }


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
