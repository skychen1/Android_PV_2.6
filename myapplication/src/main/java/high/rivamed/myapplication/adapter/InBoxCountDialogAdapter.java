package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;

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
public class InBoxCountDialogAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   public InBoxCountDialogAdapter(@Nullable List<InventoryVo> data) {
	super(R.layout.item_inboxcount_four_layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, InventoryVo item) {

      helper.setText(R.id.seven_one,""+(mData.size()-helper.getAdapterPosition()));
      helper.setText(R.id.seven_two,item.getCstName());
      helper.setText(R.id.seven_three, item.getCstSpec());
      helper.setText(R.id.seven_four, ""+item.getStockNum());
   }
}
