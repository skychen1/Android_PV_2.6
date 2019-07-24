package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static high.rivamed.myapplication.base.App.mDm;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/24 20:47
 * 描述:        耗材详情
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StockDetailsAdapter extends
	BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   public StockDetailsAdapter(
	   int layoutResId, @Nullable List<InventoryVo>  data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {

	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	int four = item.getExpireStatus();
	helper.setText(R.id.seven_one,item.getEpc());
	helper.setText(R.id.seven_two,item.getUpdateDateTime());
	helper.setText(R.id.seven_three,item.getStatus());
	helper.setText(R.id.seven_four,item.getExpiryDate());
	helper.setText(R.id.seven_five,"展开");
	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), four, helper.getView(R.id.seven_four));
	}
	if (item.getExpireStatus()==0){
	   helper.setText(R.id.seven_four,"已过期");
	   helper.getView(R.id.seven_four).setBackgroundResource(R.drawable.bg_text_red);
	   helper.setTextColor(R.id.seven_four,mContext.getResources().getColor(R.color.bg_f));
	}
	helper.getView(R.id.seven_five).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_five),mDm.widthPixels);
	});
   }
}
