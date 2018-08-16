package high.rivamed.myapplication.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.SelectBean;

/**
 *创建临时患者下拉adapter
 */

public class CreatTempAdapter extends BaseQuickAdapter<SelectBean, BaseViewHolder> {

   TextView mMeal;
   TextView mGoneMeal;

   public CreatTempAdapter(int layout, List<SelectBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, SelectBean item) {
      findId(helper);
      mMeal.setText(item.getContent());
      mGoneMeal.setText(item.getContent());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));
      mGoneMeal = ((TextView) helper.getView(R.id.gone_meal));

   }
}
