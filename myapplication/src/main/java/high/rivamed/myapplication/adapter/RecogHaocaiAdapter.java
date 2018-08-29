package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.DELETE_TATUS1;

/**
 * 识别耗材页面adapter
 */
public class RecogHaocaiAdapter extends BaseQuickAdapter<TCstInventoryVo, BaseViewHolder> {
    SparseBooleanArray checkStates1;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_one;

    public RecogHaocaiAdapter(
            int layoutResId, List<TCstInventoryVo> data) {
        super(layoutResId, data);
    }

    public RecogHaocaiAdapter(
            int layout, List<TCstInventoryVo> tCstInventoryVos, SparseBooleanArray checkStates1) {
        super(layout, tCstInventoryVos);
        this.checkStates1 = checkStates1;
    }

    @Override
    protected void convert(
            BaseViewHolder helper, TCstInventoryVo item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = (TextView) helper.getView(R.id.seven_six);
        SwipeLayout swipe = (SwipeLayout) helper.getView(R.id.swipe);
        swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        LinearLayout  delete = (LinearLayout) helper.getView(R.id.ll_delete);
        mSeven_six.setVisibility(View.VISIBLE);

        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getEpc());
        mSeven_three.setText(item.getCstSpec());
        mSeven_four.setText(item.getExpiration());
        mSeven_five.setText(item.getDeviceName());

        if (item.getPatientName()==null||item.getPatientName().length()<1){
            mSeven_six.setText("");
        }else {
            mSeven_six.setText(item.getPatientName()+" / "+item.getPatientId());
        }
        UIUtils.initTermOfValidity(mContext, helper, item.getStopFlag(), mSeven_four);

        setDeleteView(item.isDelete(),swipe);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TCstInventoryVo inventoryVo = mData.get(helper.getAdapterPosition());
                inventoryVo.setDelete(true);
                inventoryVo.setDeletetatus(DELETE_TATUS1);
                mData.remove(helper.getAdapterPosition());
                mData.add(inventoryVo);

                notifyDataSetChanged();
            }
        });
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
