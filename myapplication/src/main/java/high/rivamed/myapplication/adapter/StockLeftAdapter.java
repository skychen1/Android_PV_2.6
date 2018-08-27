package high.rivamed.myapplication.adapter;

import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.SocketLeftTopBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 14:09
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class StockLeftAdapter extends BaseQuickAdapter<SocketLeftTopBean.CstExpirationVosBean, BaseViewHolder>{

   private TextView mStock_card_number;
   private TextView mStock_card_outofdate;
   private TextView mStock_card_unexpired;
   private CardView mStock_cardview;
   public StockLeftAdapter(int layout, List<SocketLeftTopBean.CstExpirationVosBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, SocketLeftTopBean.CstExpirationVosBean item) {
      findId(helper);

	String number1 = item.getDeviceName();
	int number2 = item.getExpireCount();
	int number3 = item.getNearExpireCount();

	mStock_card_number.setText(number1);
      mStock_card_outofdate.setText("过期耗材：" + number2 +"个");
      mStock_card_unexpired.setText("近效期耗材：" + number3+"个");

      if (number2>0){
	   mStock_card_outofdate.setTextColor(mContext.getResources().getColor(R.color.card_stock_text));
	}else {
	   mStock_card_outofdate.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	}
	if (number3>0){
	   mStock_card_unexpired.setTextColor(mContext.getResources().getColor(R.color.card_stock_text));
	}else {
	   mStock_card_unexpired.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	}
//	mStock_cardview.setOnClickListener(new View.OnClickListener() {
//	   @Override
//	   public void onClick(View v) {
//
//	   }
//	});
   }

   private void findId(BaseViewHolder helper) {

      mStock_card_number = ((TextView) helper.getView(R.id.stock_card_number));
      mStock_card_outofdate = ((TextView) helper.getView(R.id.stock_card_outofdate));
      mStock_card_unexpired = ((TextView) helper.getView(R.id.stock_card_unexpired));
      mStock_cardview = ((CardView) helper.getView(R.id.stock_cardview));


   }
}
