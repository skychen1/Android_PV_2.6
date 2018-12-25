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

public class BillStockAdapter extends BaseQuickAdapter<BillStockResultBean.OrderDetailVo, BaseViewHolder> {

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


    public BillStockAdapter(int layout, List<BillStockResultBean.OrderDetailVo> data) {
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
    protected void convert(final BaseViewHolder helper, BillStockResultBean.OrderDetailVo item) {
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
        mSeven_four.setText(item.getCounts()+"");



        StringBuffer deviceNames = new StringBuffer();
        for (int i = 0; i < item.getDeviceNames().size(); i++) {
            deviceNames.append(item.getDeviceNames().get(i));
        }
        mSeven_five.setText(deviceNames.toString());
        mSeven_six.setText(item.getPatientName());
        mSeven_seven.setText(item.getReceivedStatus());
        mSeven_eight.setTextColor(mContext.getResources().getColor(R.color.color_green));
        mSeven_eight.setText("打开柜门");
        if (item.getReceivedStatus().equals("已领取")) {
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
        }else{
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            if(item.getCounts()==0){
                mSeven_four.setTextColor(mContext.getResources().getColor(R.color.color_red));
		   mSeven_eight.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            }else {
                mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
		   mSeven_eight.setTextColor(mContext.getResources().getColor(R.color.color_green));
            }
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.text_color_3));

            mSeven_eight.setText("打开柜门");
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
        mSeven_eight = ((TextView) helper.getView(R.id.seven_eight));
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
