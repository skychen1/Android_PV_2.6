package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

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
public class ExceptionOperatorAdapter extends BaseQuickAdapter<ExceptionOperatorBean.PageModelBean.RowsBean, BaseViewHolder> {
    public ExceptionOperatorAdapter(@Nullable List<ExceptionOperatorBean.PageModelBean.RowsBean> data) {
        super(R.layout.item_excpbing_operator_seven_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExceptionOperatorBean.PageModelBean.RowsBean item) {
        helper.setChecked(R.id.seven_one, item.isSelected())
                .setText(R.id.seven_two, item.getName())
                .setText(R.id.seven_three, item.getDbCenterUserId())
                .setText(R.id.seven_four, item.getRoleName());
        helper.getView(R.id.seven_one).setClickable(false);
        helper.getView(R.id.seven_ll).setOnClickListener(v -> {
            for (int i = 0; i < getData().size(); i++) {
                getData().get(i).setSelected(false);
            }
            getData().get(helper.getAdapterPosition()).setSelected(!item.isSelected());
            notifyDataSetChanged();
        });
    }

    public ExceptionOperatorBean.PageModelBean.RowsBean getSelect() {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).isSelected()) {
                return getData().get(i);
            }
        }
        return null;
    }
}
