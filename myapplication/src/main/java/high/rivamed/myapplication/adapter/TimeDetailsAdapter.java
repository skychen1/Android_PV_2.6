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
 * 创建时间:    2018/8/1 19:04
 * 描述:        实时扫描的详情
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimeDetailsAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {
   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   public TimeDetailsAdapter(
	   int layoutResId, @Nullable List<InventoryVo> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	int Stock = item.getCountStock();
	int Actual = item.getCountActual();
	helper.setText(R.id.seven_one,item.getEpc());
	helper.setText(R.id.seven_two,item.getExpiryDate());
	helper.setText(R.id.seven_three,Actual+"");
	helper.setText(R.id.seven_four,Stock+"");
	helper.setText(R.id.seven_five,"展开");
	if (Stock!=Actual) {
	   helper.setTextColor(R.id.seven_three,mContext.getResources().getColor(R.color.color_red));
	} else {
	   helper.setTextColor(R.id.seven_three,mContext.getResources().getColor(R.color.text_color_3));
	}
	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(UIUtils.getContext(), item.getIsErrorOperation(), item.getExpireStatus(), helper.getView(R.id.seven_two));
	}
	helper.getView(R.id.seven_five).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_five),mDm.widthPixels);
	});
   }
}
