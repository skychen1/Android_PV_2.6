package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.ExceptionDealBean;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理
 */
public class ExceptionDealAdapter extends BaseQuickAdapter<ExceptionDealBean, BaseViewHolder> {
    public ExceptionDealAdapter(@Nullable List<ExceptionDealBean> data) {
        super(R.layout.item_excp_nine_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExceptionDealBean item) {
        // TODO: 2019/5/17 设置数据
        boolean isUnknown = item.getOperator().equals("Unknown");
        boolean isOperate = "出柜关联 移除判断 绑定患者".contains(item.getOperate());
        int resIdOperate = item.getOperate().equals("已过期") ? R.color.card_stock_text : (isOperate ? R.color.white : R.color.text_color_3);
        int resIdOperator = isUnknown ? R.color.white : R.color.text_color_3;

        helper.setText(R.id.nine_seven1, item.getOperator())
                .setText(R.id.nine_nine1, item.getOperate())
                .setTextColor(R.id.nine_seven1, UIUtils.getColor(resIdOperator))
                .setTextColor(R.id.nine_nine1, UIUtils.getColor(resIdOperate));

        if (isOperate) {
            helper.setBackgroundRes(R.id.nine_nine1, R.drawable.bg_btn_gray_sel)
                    .addOnClickListener(R.id.nine_nine1);//操作
        } else {
            helper.setBackgroundColor(R.id.nine_nine1, UIUtils.getColor(android.R.color.transparent));
        }

        if (isUnknown) {
            helper.setBackgroundRes(R.id.nine_seven1, R.drawable.bg_btn_gray_sel)
                    .addOnClickListener(R.id.nine_seven1);//关联操作人
        } else {
            helper.setBackgroundColor(R.id.nine_seven1, UIUtils.getColor(android.R.color.transparent));
        }

    }
}
