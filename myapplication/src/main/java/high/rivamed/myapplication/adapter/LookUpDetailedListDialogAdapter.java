package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.util.SparseBooleanArray;
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
 * 描述:        查看医嘱请领单弹出框适配器
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class LookUpDetailedListDialogAdapter extends BaseQuickAdapter<BillStockResultBean.TransReceiveOrderDetailVosBean, BaseViewHolder> {

    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private SparseBooleanArray mCheckStates;

    public LookUpDetailedListDialogAdapter(List<BillStockResultBean.TransReceiveOrderDetailVosBean> data) {
        super(R.layout.item_form_four_layout, data);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        this.mData = data;
    }

    public void clear() {
        if (mCheckStates != null) {
            mCheckStates.clear();
        }
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, BillStockResultBean.TransReceiveOrderDetailVosBean item) {
//        if (helper.getAdapterPosition() % 2 == 0) {
//            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
//        } else {
//            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
//        }
        ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        findId(helper);
        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getCstSpec());
        mSeven_three.setText("" + item.getCounts());
        StringBuffer deviceNames=new StringBuffer();
        for (int i = 0; i < item.getDeviceName().size(); i++) {
            deviceNames.append(item.getDeviceName().get(i));
        }
        mSeven_four.setText(deviceNames);
    }

    private void findId(BaseViewHolder helper) {
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
    }

    public void setDeleteView(boolean isDeleteView, SwipeLayout swipe) {
        if (isDeleteView) {
            mSeven_one.setTextColor(Color.parseColor("#999999"));
            mSeven_two.setTextColor(Color.parseColor("#999999"));
            mSeven_three.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setTextColor(Color.parseColor("#999999"));
            swipe.setSwipeEnabled(false);
        } else {
            mSeven_one.setTextColor(Color.parseColor("#333333"));
            mSeven_two.setTextColor(Color.parseColor("#333333"));
            mSeven_three.setTextColor(Color.parseColor("#333333"));
            mSeven_four.setTextColor(Color.parseColor("#333333"));
            swipe.setSwipeEnabled(true);
        }
    }
}
