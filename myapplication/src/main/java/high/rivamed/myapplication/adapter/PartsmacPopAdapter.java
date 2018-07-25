package high.rivamed.myapplication.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TBaseDevices;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        硬件标识
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PartsmacPopAdapter extends BaseQuickAdapter<TBaseDevices.tBaseDevices.partsmacBean, BaseViewHolder> {

   TextView mMeal;

   public PartsmacPopAdapter(int layout, List<TBaseDevices.tBaseDevices.partsmacBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, TBaseDevices.tBaseDevices.partsmacBean item) {
      findId(helper);
      mMeal.setText(item.getPartsmacnumber());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));

   }
}
