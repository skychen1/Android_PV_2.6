package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/6 0006 18:14
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimelyGroupCstDataAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   public TimelyGroupCstDataAdapter(@Nullable List<InventoryVo> data) {
	super(R.layout.item_stockgroup_body_layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, InventoryVo item) {

	helper.setText(R.id.seven_three, item.getCstSpec());
	helper.setText(R.id.seven_four, item.getDeviceName());
	helper.setText(R.id.seven_five, item.getCountActual()+"");
	helper.setText(R.id.seven_six, item.getCountStock()+"");
	TextView seven_five =(TextView)helper.getView(R.id.seven_five);
	TextView seven_six =(TextView)helper.getView(R.id.seven_six);
	View mBody_line = (View) helper.getView(R.id.body_line);
	if (item.getIsErrorOperation() != 1) {
	   if (item.getCountActual()!=item.getCountStock()) {
		seven_five.setTextColor(mContext.getResources().getColor(R.color.color_red));
	   } else {
		seven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   }
	} else {
	   seven_five.setTextColor(mContext.getResources().getColor(R.color.color_red));
	}
	if (helper.getAdapterPosition()==this.getItemCount()-1){
	   mBody_line.setVisibility(View.GONE);
	}else {
	   mBody_line.setVisibility(View.VISIBLE);
	}
   }
}
