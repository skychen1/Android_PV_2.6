package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.InBoxOrderDialogBean;

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
public class InBoxBillDialogAdapter extends BaseQuickAdapter<InBoxOrderDialogBean.OrderVosBean, BaseViewHolder> {
   CheckBox mMCheckBox;
   public InBoxBillDialogAdapter(@Nullable List<InBoxOrderDialogBean.OrderVosBean> data, CheckBox mMCheckBox) {
	super(R.layout.item_inboxorder_five_layout, data);
	this.mMCheckBox =mMCheckBox;
   }

   @Override
   protected void convert(BaseViewHolder helper, InBoxOrderDialogBean.OrderVosBean item) {
      helper.setChecked(R.id.seven_one, item.isSelected());
      helper.setText(R.id.seven_two, item.getOrderNo());
      helper.setText(R.id.seven_three, item.getCreateTime());
      helper.setText(R.id.seven_four, "" + item.getInventoryNum());
      helper.setText(R.id.seven_five, "" + item.getInStockNum());
      CheckBox mSevenOne = (CheckBox)helper.getView(R.id.seven_one);
      mSevenOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            item.setSelected(isChecked);
            if(!isChecked){
               mMCheckBox.setChecked(false);
            }
         }
      });
   }
}
