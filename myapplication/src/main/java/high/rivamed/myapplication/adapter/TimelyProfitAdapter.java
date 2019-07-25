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
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);

	int six =item.getCountActual();
	int seven =item.getCountStock();
	helper.setText(R.id.seven_one,item.getCstName());
	helper.setText(R.id.seven_two,item.getEpc());
	helper.setText(R.id.seven_three,item.getCstSpec());
	helper.setText(R.id.seven_four,item.getExpiryDate());
	helper.setText(R.id.seven_five,item.getDeviceName());
	helper.setText(R.id.seven_six,six+"");
	helper.setText(R.id.seven_seven,seven+"");
	helper.setText(R.id.seven_eight,"展开");
	helper.getView(R.id.seven_eight).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_eight),mDm.widthPixels);
	});
	if (six!=seven) {
	   helper.setTextColor(R.id.seven_six,mContext.getResources().getColor(R.color.color_red));
	} else {
	   helper.setTextColor(R.id.seven_six,mContext.getResources().getColor(R.color.text_color_3));
	}
	int StopFlag = item.getExpireStatus();

	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), StopFlag,  helper.getView(R.id.seven_four));
	}
   }
}
