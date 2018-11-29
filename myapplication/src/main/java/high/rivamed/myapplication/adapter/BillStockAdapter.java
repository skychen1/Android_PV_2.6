package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BillStockResultBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:08
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class BillStockAdapter extends BaseQuickAdapter<BillStockResultBean.TransReceiveOrderDetailVosBean, BaseViewHolder> {

    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;
    public String TAG = "TimelyPublicAdapter";
    public int mSize;
    public String mType;
    private SparseBooleanArray mCheckStates;
    private SparseBooleanArray mCheckStates2 = new SparseBooleanArray();
    public CheckBox mMCheckBox;


    public BillStockAdapter(int layout, List<BillStockResultBean.TransReceiveOrderDetailVosBean> data) {
        super(layout, data);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        this.mData = data;
    }


    public void clear() {
        if (mCheckStates != null) {
            mCheckStates.clear();
        }
        if (mCheckStates2 != null) {
            mCheckStates2.clear();
        }
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, BillStockResultBean.TransReceiveOrderDetailVosBean item) {
        if (item.getReceivedStatus().equals("已领取")) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }

        findId(helper);
        LinearLayout layout = (LinearLayout) helper.getView(R.id.seven_ll);
        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getCstSpec());
        mSeven_three.setText("" + item.getNeedNum());
        StringBuffer deviceNames = new StringBuffer();
        for (int i = 0; i < item.getDeviceName().size(); i++) {
            deviceNames.append(item.getDeviceName().get(i));
        }
        mSeven_four.setText(deviceNames.toString());
        mSeven_five.setText(item.getPatientName());
        mSeven_six.setText(item.getReceivedStatus());
        mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
        mSeven_seven.setText("打开柜门");
        if (item.getReceivedStatus().equals("已领取")) {
            mSeven_seven.setText("");
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
        }else{
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
            mSeven_seven.setText("打开柜门");
        }
    }

    /**
     * 设置某个效期的背景
     *
     * @param helper
     * @param text
     * @param textview
     */
    private void initTermOfValidity(BaseViewHolder helper, String text, TextView textview) {

        if (text.equals("已过期")) {
            textview.setBackgroundResource(R.drawable.bg_text_red);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else if (text.equals("≤100天")) {
            textview.setBackgroundResource(R.drawable.bg_text_yellow1);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else if (text.equals("≤70天")) {
            textview.setBackgroundResource(R.drawable.bg_text_yellow2);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else if (text.equals("≤28天")) {
            textview.setBackgroundResource(R.drawable.bg_text_orange);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else {
            if (helper.getAdapterPosition() % 2 == 0) {
                textview.setBackgroundResource(R.color.bg_color);
            } else {
                textview.setBackgroundResource(R.color.bg_f);
            }
            textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        }

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

    public void setDeleteView(boolean isDeleteView, SwipeLayout swipe) {
        if (isDeleteView) {
            mSeven_one.setTextColor(Color.parseColor("#999999"));
            mSeven_two.setTextColor(Color.parseColor("#999999"));
            mSeven_three.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setTextColor(Color.parseColor("#999999"));
            mSeven_five.setTextColor(Color.parseColor("#999999"));
            mSeven_six.setTextColor(Color.parseColor("#999999"));
            swipe.setSwipeEnabled(false);
        } else {
            mSeven_one.setTextColor(Color.parseColor("#333333"));
            mSeven_two.setTextColor(Color.parseColor("#333333"));
            mSeven_three.setTextColor(Color.parseColor("#333333"));
            mSeven_four.setTextColor(Color.parseColor("#333333"));
            mSeven_five.setTextColor(Color.parseColor("#333333"));
            mSeven_six.setTextColor(Color.parseColor("#333333"));
            swipe.setSwipeEnabled(true);
        }
    }
}
