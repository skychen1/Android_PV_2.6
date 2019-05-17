package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.ExceptionOperatorBean;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理-关联操作人
 */
public class ExceptionOperatorAdapter extends BaseQuickAdapter<ExceptionOperatorBean, BaseViewHolder> {
    public ExceptionOperatorAdapter(@Nullable List<ExceptionOperatorBean> data) {
        super(R.layout.item_excpbing_operator_seven_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExceptionOperatorBean item) {
        // TODO: 2019/5/17 设置数据
        helper.setChecked(R.id.seven_one, item.isCheck())
                .setText(R.id.seven_two, item.getOperator())
                .setText(R.id.seven_three, item.getId());
        helper.getView(R.id.seven_one).setClickable(false);
        helper.getView(R.id.seven_ll).setOnClickListener(v -> {
            for (int i = 0; i < getData().size(); i++) {
                getData().get(i).setCheck(false);
            }
            getData().get(helper.getAdapterPosition()).setCheck(!item.isCheck());
            notifyDataSetChanged();
        });
    }

    public ExceptionOperatorBean getSelect() {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).isCheck()) {
                return getData().get(i);
            }
        }
        return null;
    }
}
