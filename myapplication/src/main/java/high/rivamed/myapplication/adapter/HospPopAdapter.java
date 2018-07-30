package high.rivamed.myapplication.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.HospNameBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        激活设备选择的位置 医院
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class HospPopAdapter extends BaseQuickAdapter<HospNameBean.TbaseHospitalsBean, BaseViewHolder> {

   TextView mMeal;
   TextView mGoneMeal;

   public HospPopAdapter(int layout, List<HospNameBean.TbaseHospitalsBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, HospNameBean.TbaseHospitalsBean item) {
      findId(helper);
      mMeal.setText(item.getHospName());
      mGoneMeal.setText(item.getHospId());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));
      mGoneMeal = ((TextView) helper.getView(R.id.gone_meal));

   }
}
