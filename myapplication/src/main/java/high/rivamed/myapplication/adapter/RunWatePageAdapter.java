package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.RunWateBean;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/25 11:23
 * 描述:        耗材流水pager adapter
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RunWatePageAdapter extends BaseQuickAdapter<RunWateBean.RowsBean, BaseViewHolder> {

    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;

    public RunWatePageAdapter(
            int layoutResId, @Nullable List<RunWateBean.RowsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(
            BaseViewHolder helper, RunWateBean.RowsBean item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }
        findId(helper);
        int five = item.getStopFlag();
        String one = item.getStatus();
        mSeven_one.setText(one);
        mSeven_two.setText(item.getCstName());
        mSeven_three.setText(item.getEpc());
        mSeven_four.setText(item.getCstSpec());
        mSeven_five.setText(item.getPatientNameAndId());
        mSeven_six.setText(item.getDeviceName());
        mSeven_seven.setText(item.getOptionDate());
        mSeven_eight.setText(item.getOptionName());

//        UIUtils.initTermOfValidity(mContext, helper, five, mSeven_five);

        mSeven_one.setTextColor(mContext.getResources().getColor(R.color.color_green));
        //	if (one.contains("入库") || one.equals("移入") || one.equals("退回") ) {
        //	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.color_green));
        //	} else {
        //	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.color_red_type));
        //	}
    }

    private void findId(BaseViewHolder helper) {
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = ((TextView) helper.getView(R.id.seven_six));
        mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
        mSeven_eight = ((TextView) helper.getView(R.id.seven_eight));

    }
}
