package high.rivamed.myapplication.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.bean.OutMealSuitBeanResult;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class MealPopAdapter extends BaseQuickAdapter<OutMealSuitBeanResult, BaseViewHolder> {

    TextView mMeal;

    public MealPopAdapter(int layout, List<OutMealSuitBeanResult> data) {
        super(layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutMealSuitBeanResult item) {
        findId(helper);
        mMeal.setText(item.getPlanName());
    }

    private void findId(BaseViewHolder helper) {

        mMeal = ((TextView) helper.getView(R.id.item_meal));

    }
}
