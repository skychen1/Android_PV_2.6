package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TakeNotesDetailsBean;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/10/31 11:23
 * 描述:       使用记录明细
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TakeNotesEpcAdapter extends BaseQuickAdapter<TakeNotesDetailsBean.JournalUseRecordDetailVos, BaseViewHolder> {

    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;

    public TakeNotesEpcAdapter(
            int layoutResId, @Nullable List<TakeNotesDetailsBean.JournalUseRecordDetailVos> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(
            BaseViewHolder helper, TakeNotesDetailsBean.JournalUseRecordDetailVos item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }
        findId(helper);
        mSeven_one.setText(item.getEpc());
        mSeven_two.setText(item.getCstName());
        mSeven_three.setText(item.getCstSpec());
        mSeven_four.setText(item.getOperationTime());
        mSeven_five.setText(item.getUserName());
        mSeven_six.setText(item.getStatus());

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
