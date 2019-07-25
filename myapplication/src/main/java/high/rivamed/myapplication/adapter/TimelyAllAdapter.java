package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.CONFIG_026;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/1 12:26
 * 描述:       实时盘点
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimelyAllAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   public TimelyAllAdapter(
	   int layoutResId, @Nullable List<InventoryVo> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {

	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	int six = item.getCountStock();
	int five = item.getCountActual();
	helper.setText(R.id.seven_one,item.getCstName());
	helper.setText(R.id.seven_two,item.getCstSpec());
	helper.setText(R.id.seven_three,item.getExpiryDate());
	helper.setText(R.id.seven_four,item.getDeviceName());
	helper.setText(R.id.seven_five,five+"");
	helper.setText(R.id.seven_six,six+"");

	if(UIUtils.getConfigType(mContext, CONFIG_026)){
	   helper.getView(R.id.seven_seven).setVisibility(View.VISIBLE);
	   helper.setText(R.id.seven_seven,item.getNoConfirmCount());
	}else {
	   helper.getView(R.id.seven_seven).setVisibility(View.GONE);
	}
	if (five!=six) {
	   helper.setTextColor(R.id.seven_five,mContext.getResources().getColor(R.color.color_red));
	} else {
	   helper.setTextColor(R.id.seven_five,mContext.getResources().getColor(R.color.text_color_3));
	}
	if (item.getExpireStatus()!=null) {
	   UIUtils.initTermOfValidity(UIUtils.getContext(), item.getIsErrorOperation(), item.getExpireStatus(), helper.getView(R.id.seven_three));
	}
   }
}
