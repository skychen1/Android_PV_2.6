package high.rivamed.myapplication.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.HospNameBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        激活设备选择的位置 库房
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class HospPopFourAdapter extends BaseQuickAdapter<HospNameBean.TcstBaseStorehousesBean, BaseViewHolder> {

   TextView mMeal;
   TextView mGoneMeal;

   public HospPopFourAdapter(int layout, List<HospNameBean.TcstBaseStorehousesBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, HospNameBean.TcstBaseStorehousesBean item) {
      findId(helper);
      mMeal.setText(item.getName());
      mGoneMeal.setText(item.getStorehouseCode());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));
      mGoneMeal = ((TextView) helper.getView(R.id.gone_meal));

   }
}
