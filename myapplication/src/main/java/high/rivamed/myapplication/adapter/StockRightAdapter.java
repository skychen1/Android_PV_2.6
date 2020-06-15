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
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static high.rivamed.myapplication.base.App.mDm;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/24 19:37
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StockRightAdapter
	extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   private TextView mSeven_seven;
   private TextView mSeven_eight;

   public StockRightAdapter(
	   int layoutResId, @Nullable List<InventoryVo>  data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {

	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	findId(helper);
	int five = item.getExpireStatus();

	mSeven_one.setText("未使用");
	mSeven_two.setText(item.getCstName());
	mSeven_three.setText(item.getEpc());
	mSeven_four.setText(item.getCstSpec());
	mSeven_five.setText(item.getExpiryDate());
	mSeven_six.setText(item.getDeviceName());
	mSeven_seven.setText(item.getUpdateTime());
	mSeven_eight.setText(item.getUpdator());
	helper.setText(R.id.seven_nine,"展开");
	helper.getView(R.id.seven_nine).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_nine),mDm.widthPixels);
	});
	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), five, mSeven_five);
	}
	mSeven_one.setTextColor(mContext.getResources().getColor(R.color.color_red));

   }

   private void findId(BaseViewHolder helper) {
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	mSeven_five = ((TextView) helper.getView(R.id.seven_five));

	mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
	mSeven_eight = ((TextView) helper.getView(R.id.seven_eight));

   }
}