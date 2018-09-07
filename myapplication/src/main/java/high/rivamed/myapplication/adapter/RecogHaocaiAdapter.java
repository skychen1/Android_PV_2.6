package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.DELETE_TATUS1;

/**
 * 识别耗材页面adapter
 */
public class RecogHaocaiAdapter extends BaseQuickAdapter<TCstInventoryVo, BaseViewHolder> {
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_one;
    int      mOperation;
    LinearLayout mLl;
    public RecogHaocaiAdapter(
            int layoutResId, List<TCstInventoryVo> data) {
        super(layoutResId, data);
    }

    public RecogHaocaiAdapter(
            int layout, List<TCstInventoryVo> tCstInventoryVos, int operation) {
        super(layout, tCstInventoryVos);
        this.mOperation = operation;

    }

    @Override
    protected void convert(
            BaseViewHolder helper, TCstInventoryVo item) {
        mLl=((LinearLayout) helper.getView(R.id.seven_ll));
        if (helper.getAdapterPosition() % 2 == 0) {
            mLl.setBackgroundResource(R.color.bg_color);
        } else {
            mLl.setBackgroundResource(R.color.bg_f);
        }
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = (TextView) helper.getView(R.id.seven_six);
        mSeven_seven = (TextView) helper.getView(R.id.seven_seven);
        SwipeLayout swipe = (SwipeLayout) helper.getView(R.id.swipe);
        swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        LinearLayout  delete = (LinearLayout) helper.getView(R.id.ll_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TCstInventoryVo inventoryVo = mData.get(helper.getAdapterPosition());
                inventoryVo.setDelete(true);
                inventoryVo.setDeletetatus(DELETE_TATUS1);
                inventoryVo.setStatus("移除");
                mData.remove(helper.getAdapterPosition());
                mData.add(inventoryVo);
                EventBusUtils.post(new Event.EventButton(true,true));
                notifyDataSetChanged();
            }
        });
        String status = item.getStatus();
        mSeven_six.setVisibility(View.VISIBLE);

        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getEpc());
        mSeven_three.setText(item.getCstSpec());
        mSeven_four.setText(item.getExpiration());
        mSeven_five.setText(item.getDeviceName());
        mSeven_seven.setText(status);

        if (status.equals("禁止入库") || status.equals("禁止移入") || status.equals("禁止退回") ||
            item.getStopFlag() == 0 || (mOperation == 3 && !status.contains("领用")) ||
            (mOperation == 2 && !status.contains("入库")) ||
            (mOperation == 9 && !status.contains("移出")) ||
            (mOperation == 11 && !status.contains("调拨")) ||
            (mOperation == 10 && !status.contains("移入")) ||
            (mOperation == 7 && !status.contains("退回")) ||
            (mOperation == 8 && !status.contains("退货"))) {
            LogUtils.i("InOutBoxTwoActivity", "mOperation   " + mOperation + "   status   " + status);

            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_red));
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));

        } else {
            if (helper.getAdapterPosition() % 2 == 0) {
                mLl.setBackgroundResource(R.color.bg_color);
            } else {
                mLl.setBackgroundResource(R.color.bg_f);
            }
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.text_color_3));

        }


        mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        if (item.getPatientName()==null||item.getPatientName().length()<1){
            mSeven_six.setText("");
        }else {
            mSeven_six.setText(item.getPatientName()+" / "+item.getPatientId());
        }
        UIUtils.initTermOfValidity(mContext, helper, item.getStopFlag(), mSeven_four);

        setDeleteView(item.isDelete(),swipe);

    }

    public void setDeleteView(boolean isDeleteView, SwipeLayout swipe) {
        if (isDeleteView) {
            mSeven_one.setTextColor(Color.parseColor("#999999"));
            mSeven_two.setTextColor(Color.parseColor("#999999"));
            mSeven_three.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setBackgroundResource(R.color.bg_color);
            mSeven_five.setTextColor(Color.parseColor("#999999"));
            mSeven_six.setTextColor(Color.parseColor("#999999"));
            mSeven_seven.setTextColor(Color.parseColor("#999999"));
            mLl.setBackgroundResource(R.color.bg_color);
            swipe.setSwipeEnabled(false);
        } else {
            mSeven_one.setTextColor(Color.parseColor("#333333"));
            mSeven_two.setTextColor(Color.parseColor("#333333"));
            mSeven_three.setTextColor(Color.parseColor("#333333"));
//            mSeven_four.setTextColor(Color.parseColor("#333333"));
            mSeven_five.setTextColor(Color.parseColor("#333333"));
            mSeven_six.setTextColor(Color.parseColor("#333333"));
            swipe.setSwipeEnabled(true);
        }
    }

}
