package high.rivamed.myapplication.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.OrderSheetBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 14:09
 * 描述:        请领单
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutFormAdapter extends BaseQuickAdapter<OrderSheetBean.RowsBean, BaseViewHolder> {

    TextView mTvOpNumber;
    TextView mTvTime;
    TextView mTvRequestNameHint;
    TextView mTvRequestName;
    TextView mTvPatientNameHint;
    TextView mTvPatientName;
    LinearLayout mFormCardLL;
    public int selectedPosition =0; //默认一个参数

    public OutFormAdapter(int layout, List<OrderSheetBean.RowsBean> data) {
        super(layout, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, OrderSheetBean.RowsBean item) {
        findId(helper);
        if (helper.getAdapterPosition() == selectedPosition) {
            setView(true);
        } else {
            setView(false);
        }
        if (!TextUtils.isEmpty(item.getRoomName())) {
            mTvOpNumber.setText(item.getRoomName());
        } else {
            mTvOpNumber.setText("");
        }
        if (!TextUtils.isEmpty(item.getCreateTime())) {
            mTvTime.setText(item.getCreateTime());
        } else {
            mTvTime.setText("");
        }
        if (!TextUtils.isEmpty(item.getCreatorName())) {
            mTvRequestName.setText(item.getCreatorName());
        } else {
            mTvRequestName.setText("");
        }
        if (!TextUtils.isEmpty(item.getPatientName())) {
            mTvPatientName.setText(item.getPatientName());
        } else {
            mTvPatientName.setText("");
        }
        mFormCardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v, helper, helper.getAdapterPosition());
            }
        });
    }

    private void findId(BaseViewHolder helper) {
        mTvOpNumber = ((TextView) helper.getView(R.id.tv_oproom_number));
        mTvTime = ((TextView) helper.getView(R.id.tv_time));
        mTvRequestNameHint = ((TextView) helper.getView(R.id.tv_request_person_hint));
        mTvRequestName = ((TextView) helper.getView(R.id.tv_request_person));
        mTvPatientNameHint = ((TextView) helper.getView(R.id.tv_patient_name_hint));
        mTvPatientName = ((TextView) helper.getView(R.id.tv_patient_name));
        mFormCardLL = ((LinearLayout) helper.getView(R.id.form_card_ll));
    }

    private void setView(boolean isSelected) {
        if (isSelected) {
            mTvOpNumber.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvRequestNameHint.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvRequestName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvPatientNameHint.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvPatientName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mFormCardLL.setBackgroundResource(R.mipmap.bg_outform_top_item_pre);
        } else {
            mTvOpNumber.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_0));
            mTvTime.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_58));
            mTvRequestNameHint.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_58));
            mTvRequestName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_0));
            mTvPatientNameHint.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_58));
            mTvPatientName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_0));
            mFormCardLL.setBackgroundResource(R.mipmap.bg_outform_top_item_nor);
        }

    }


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {  //定义接口，实现Recyclerview点击事件
        void OnItemClick(View view, BaseViewHolder helper, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {   //实现点击
        this.onItemClickListener = onItemClickListener;
    }

}
