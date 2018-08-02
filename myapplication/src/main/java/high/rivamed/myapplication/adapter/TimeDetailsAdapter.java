package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/1 19:04
 * 描述:        实时扫描的详情
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimeDetailsAdapter extends BaseQuickAdapter<TCstInventoryVo, BaseViewHolder> {
   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   public TimeDetailsAdapter(
	   int layoutResId, @Nullable List<TCstInventoryVo> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, TCstInventoryVo item) {
	findId(helper);
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}
	int Stock = item.getCountStock();
	int Actual = item.getCountActual();

	mSeven_one.setText(item.getEpc());
	mSeven_two.setText(item.getExpiration());
	mSeven_three.setText(Actual+"");
	mSeven_four.setText(Stock+"");
	if (Stock!=Actual) {
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.color_red));
	} else {
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}

	UIUtils.initTermOfValidity(UIUtils.getContext(), helper, item.getStopFlag(), mSeven_two);

   }
   private void findId(BaseViewHolder helper) {
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));


   }
}
