package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.view.View.GONE;
import static high.rivamed.myapplication.base.BaseTimelyActivity.mOnBtnGone;

/**
 * 识别耗材页面adapter
 */
public class RecogHaocaiAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_one;
    LinearLayout mLl;
    public RecogHaocaiAdapter(
            int layoutResId, List<InventoryVo> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(
            BaseViewHolder helper, InventoryVo item) {
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

        if (mOnBtnGone) {
            mSeven_seven.setVisibility(GONE);
        }else {
            mSeven_seven.setVisibility(View.VISIBLE);
        }

        SwipeLayout swipe = (SwipeLayout) helper.getView(R.id.swipe);
        swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        LinearLayout  delete = (LinearLayout) helper.getView(R.id.ll_delete);
        TextView mdeleteTv = (TextView) helper.getView(R.id.tv_delete);
        ImageView mdeleteIv = (ImageView) helper.getView(R.id.iv_delete);

        if (item.getDeleteCount()>0){
            LogUtils.i("InBox","解除移除");
            mdeleteTv.setText("取消移除");
            delete.setBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.bg_greens));
            mdeleteIv.setVisibility(GONE);
        }else {
            LogUtils.i("InBox","移除");
            mdeleteTv.setText("移除");
            delete.setBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.bg_delete));
            mdeleteIv.setVisibility(View.VISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryVo inventoryVo = mData.get(helper.getAdapterPosition());
                if (inventoryVo.getDeleteCount()>0){
                    inventoryVo.setDelete(false);
                    inventoryVo.setDeleteCount(0);
                    mData.remove(helper.getAdapterPosition());
                    mData.add(inventoryVo);
                }else {
                    inventoryVo.setDelete(true);
                    inventoryVo.setDeleteCount(inventoryVo.getDeleteCount()+1);
                    mData.remove(helper.getAdapterPosition());
                    mData.add(inventoryVo);
                }

                EventBusUtils.post(new Event.EventButton(true,true));
                notifyDataSetChanged();
            }
        });
        String status = item.getStatus();
        mSeven_six.setVisibility(View.VISIBLE);

        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getEpc());
        mSeven_three.setText(item.getCstSpec());
        mSeven_four.setText(item.getExpirationText());
        mSeven_five.setText(item.getDeviceName());
//        if (status.equals("2")){
//            mSeven_seven.setText("领用");
//        }else {
            mSeven_seven.setText(status);
//        }

        if (item.getIsErrorOperation()==1&&item.getDeleteCount()==0){
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
            if (item.getPatientId().equals("virtual")){
                mSeven_six.setText(item.getPatientName());
            }else {
                mSeven_six.setText(item.getPatientName()+" / "+item.getPatientId());
            }
        }
        UIUtils.initTermOfValidity(mContext, helper, item.getExpireStatus(), mSeven_four);

        setDeleteView(item.isDelete(),swipe);
        if (item.getDeleteCount()>0){
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_four.setBackgroundResource(R.color.bg_color);
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mLl.setBackgroundResource(R.color.bg_color);
        }

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
            swipe.setSwipeEnabled(true);
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
