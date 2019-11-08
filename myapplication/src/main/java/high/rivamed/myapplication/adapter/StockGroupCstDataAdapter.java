package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

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
public class StockGroupCstDataAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   public StockGroupCstDataAdapter(@Nullable List<InventoryVo> data) {
	super(R.layout.item_stockgroup_body_layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, InventoryVo item) {

	helper.setText(R.id.seven_three, item.getCstSpec());
	helper.setText(R.id.seven_four, item.getCountStock() + "");
	helper.setText(R.id.seven_five, item.getDeviceName());
	TextView mSeven_six = (TextView) helper.getView(R.id.seven_six);
	View mBody_line = (View) helper.getView(R.id.body_line);
	mSeven_six.setText(item.getExpiryDate());
	mSeven_six.setBackgroundResource(R.color.bg_f);
	if (item.getExpireStatus() != null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(),
						item.getExpireStatus(), mSeven_six);
	}
	if (item.getExpireStatus() == 0) {
	   mSeven_six.setBackgroundResource(R.drawable.bg_text_red);
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	   mSeven_six.setText("已过期");
	}
	if (helper.getAdapterPosition()==this.getItemCount()-1){
	   mBody_line.setVisibility(View.GONE);
	}else {
	   mBody_line.setVisibility(View.VISIBLE);
	}
   }
}
