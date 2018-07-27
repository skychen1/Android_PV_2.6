package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.StockDetailsBean;
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
	BaseQuickAdapter<StockDetailsBean.TCstInventoryVosBean, BaseViewHolder> {
   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   public StockDetailsAdapter(
	   int layoutResId, @Nullable List<StockDetailsBean.TCstInventoryVosBean> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, StockDetailsBean.TCstInventoryVosBean item) {
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}
	findId(helper);
	int four = item.getStopFlag();
	mSeven_one.setText(item.getEpc());
	mSeven_two.setText(item.getLastUpdateDate());
	mSeven_three.setText(item.getStatus());
	mSeven_four.setText(item.getExpiration());
	UIUtils.initTermOfValidity(mContext,helper, four, mSeven_four);

   }
   private void findId(BaseViewHolder helper) {
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));


   }
}