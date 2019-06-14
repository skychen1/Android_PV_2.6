package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

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
   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   public StockDetailsAdapter(
	   int layoutResId, @Nullable List<InventoryVo>  data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {
//	if (helper.getAdapterPosition() % 2 == 0) {
//	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
//	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
//	}
	findId(helper);
	int four = item.getExpireStatus();
	mSeven_one.setText(item.getEpc());
	mSeven_two.setText(item.getUpdateDateTime());
	mSeven_three.setText(item.getStatus());
	mSeven_four.setText(item.getExpiryDate());
	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), four, mSeven_four);
	}if (item.getExpireStatus()==0){
	   mSeven_four.setText("已过期");
	   mSeven_four.setBackgroundResource(R.drawable.bg_text_red);
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	}
	if (item.getExpireStatus()==0){
	   mSeven_four.setText("已过期");
	   mSeven_four.setBackgroundResource(R.drawable.bg_text_red);
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	}
   }
   private void findId(BaseViewHolder helper) {
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));


   }
}
