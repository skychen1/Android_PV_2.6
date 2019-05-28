package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.ExceptionRecordBean;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理-异常记录
 */
public class ExceptionRecordAdapter extends BaseQuickAdapter<ExceptionRecordBean, BaseViewHolder> {
    public ExceptionRecordAdapter(@Nullable List<ExceptionRecordBean> data) {
        super(R.layout.item_excp_record_nine_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExceptionRecordBean item) {
        // TODO: 2019/5/17 设置数据
        String exceptContent = item.getExceptContent();
        int resId = exceptContent.equals("连续移除") ? R.color.card_stock_text : R.color.color_28_prompt;
        //异常内容:未操作与异常做样式区分
        helper.setText(R.id.nine_record_seven1, exceptContent)
                .setTextColor(R.id.nine_record_seven1, UIUtils.getColor(resId));
    }
}
