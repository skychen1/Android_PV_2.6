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
        boolean isUnknown=item.getOperator().equals("Unknown");
        boolean isOperate= "出柜关联 移除判断 绑定患者".contains(item.getOperate());
        int resId=item.getOperate().equals("已过期")?R.color.card_stock_text:R.color.text_color_3;
        helper.setText(R.id.nine_seven1,item.getOperator())
                .setText(R.id.nine_seven2,item.getOperator())
                .setGone(R.id.nine_seven1,!isUnknown)
                .setGone(R.id.nine_seven2,isUnknown)
                .setText(R.id.nine_nine1,item.getOperate())
                .setText(R.id.nine_nine2,item.getOperate())
                .setTextColor(R.id.nine_nine1, UIUtils.getColor(resId))
                .setGone(R.id.nine_nine1,!isOperate)
                .setGone(R.id.nine_nine2,isOperate)
                .addOnClickListener(R.id.nine_seven2)//关联操作人
                .addOnClickListener(R.id.nine_nine2);//操作
    }
}
