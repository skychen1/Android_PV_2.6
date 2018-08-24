package high.rivamed.myapplication.adapter;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;

/**
 * 创建临时患者
 */
public class BindTemporaryAdapter extends BaseQuickAdapter<BingFindSchedulesBean.PatientInfosBean, BaseViewHolder> {
    SparseBooleanArray checkStates1;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    public int mCheckPosition;

    public BindTemporaryAdapter(
            int layoutResId, List<BingFindSchedulesBean.PatientInfosBean> data) {
        super(layoutResId, data);
    }

    public BindTemporaryAdapter(
            int layout, List<BingFindSchedulesBean.PatientInfosBean> tCstInventoryVos, SparseBooleanArray checkStates1) {
        super(layout, tCstInventoryVos);
        this.checkStates1 = checkStates1;
    }

    @Override
    protected void convert(
            BaseViewHolder helper, BingFindSchedulesBean.PatientInfosBean item) {
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

        boolean checked = checkStates1.get(helper.getAdapterPosition());
        mCheckBox.setChecked(checked);
        if (checked) {
            mCheckPosition = helper.getAdapterPosition();
        }
        mSeven_two.setText(item.getPatientName());
        mSeven_three.setText(item.getPatientId());
        mSeven_four.setText(item.getRequestDateTime());
        mSeven_five.setText(item.getOperationSurgeonName());
        mSeven_six.setText(item.getOperatingRoomNoName());
        if ("virtual".equals(item.getPatientId())) {
            mSeven_seven.setText("是");
        } else {
            mSeven_seven.setText("否");
        }

    }
}
