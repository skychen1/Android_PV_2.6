package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TakeNotesBean;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/10/31 11:23
 * 描述:       使用记录患者
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TakeNotesAdapter extends BaseQuickAdapter<TakeNotesBean.RowsBean, BaseViewHolder> {

    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;

    public TakeNotesAdapter(
            int layoutResId, @Nullable List<TakeNotesBean.RowsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(
            BaseViewHolder helper, TakeNotesBean.RowsBean item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }
        findId(helper);
        mSeven_one.setText(item.getPatientName());
        mSeven_two.setText(item.getPatientId());
        mSeven_three.setText(item.getSex());
        mSeven_four.setText(item.getCreateDate());

        mSeven_five.setText(item.getOperationSurgeonName());

        mSeven_six.setText(item.getOperatingRoomNoName());
        mSeven_seven.setText("耗材明细");
        mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.bg_green));

    }

    private void findId(BaseViewHolder helper) {
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = ((TextView) helper.getView(R.id.seven_six));
        mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));

    }
}
