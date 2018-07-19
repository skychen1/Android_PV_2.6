package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Movie;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/19 11:02
 * 描述:        epc列表adapter 标签读取
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class EpcTestAdapter extends BaseQuickAdapter<Movie,BaseViewHolder>{

   public EpcTestAdapter(
         int layoutResId, @Nullable List<Movie> data) {
      super(layoutResId, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, Movie item) {
      helper.setText(R.id.seven_one,item.one);
      helper.setText(R.id.seven_two,item.two);
   }
}
