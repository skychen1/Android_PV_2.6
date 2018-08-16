package high.rivamed.myapplication.adapter;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.TempPatientVo;

/**
 * 创建临时患者
 */
public class BindTemporaryAdapter extends BaseQuickAdapter<TempPatientVo, BaseViewHolder> {
    SparseBooleanArray checkStates1;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;

    public BindTemporaryAdapter(
            int layoutResId, List<TempPatientVo> data) {
        super(layoutResId, data);
    }

    public BindTemporaryAdapter(
            int layout, List<TempPatientVo> tCstInventoryVos, SparseBooleanArray checkStates1) {
        super(layout, tCstInventoryVos);
        this.checkStates1 = checkStates1;
    }

    @Override
    protected void convert(
            BaseViewHolder helper, TempPatientVo item) {
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

        mCheckBox.setChecked(checkStates1.get(helper.getAdapterPosition()));

        mSeven_two.setText(item.getCstName());
        mSeven_three.setText(item.getEpc());
        mSeven_four.setText(item.getCstSpec());
        mSeven_five.setText(item.getExpiration());
        mSeven_six.setText(item.getDeviceName());
        if (item.getPatientName() == null || item.getPatientName().length() < 1) {
            mSeven_seven.setText("");
        } else {
            mSeven_seven.setText(item.getPatientName() + "/" + item.getPatientId());
        }
    }
}
