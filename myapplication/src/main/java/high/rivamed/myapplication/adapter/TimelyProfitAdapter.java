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
 * 创建时间:    2018/8/2 9:00
 * 描述:        盘亏盘盈
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimelyProfitAdapter
	extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {
   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   private TextView mSeven_seven;
   public TimelyProfitAdapter(
	   int layoutResId, @Nullable List<InventoryVo> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {
//	if (helper.getAdapterPosition() % 2 == 0) {
//	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
//	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
//	}/
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));

	mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
	int six =item.getCountActual();
	int seven =item.getCountStock();
	mSeven_one.setText(item.getCstName());
	mSeven_two.setText(item.getEpc());
	mSeven_three.setText(item.getCstSpec());
	mSeven_four.setText(item.getExpirationText());
	mSeven_five.setText(item.getDeviceName());
	mSeven_six.setText(six+"");
	mSeven_seven.setText(seven+"");
	if (six!=seven) {
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.color_red));
	} else {
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}
	int StopFlag = item.getExpireStatus();

	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), StopFlag, mSeven_four);
	}
   }
}
