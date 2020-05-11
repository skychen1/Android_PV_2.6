package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.OrderVos;

/**
 * 项目名称:    Android_PV_2.6.9.0
 * 创建者:      DanMing
 * 创建时间:    2019/8/7 0007 16:53
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InBoxOrderOverDialogAdapter extends BaseQuickAdapter<OrderVos, BaseViewHolder> {

   public InBoxOrderOverDialogAdapter(@Nullable  List<OrderVos>  data) {
	super(R.layout.item_inboxorderover_five_layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, OrderVos item) {
      helper.setOnCheckedChangeListener(R.id.seven_five, new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            item.setSelected(b);
         }
      });
      helper.setText(R.id.seven_one, item.getOrderNo());
      helper.setText(R.id.seven_two, "" + item.getInventoryNum());
      helper.setText(R.id.seven_three, "" + item.getCurrentNum());
      helper.setText(R.id.seven_four, "" + item.getInStockNum());
   }
}
