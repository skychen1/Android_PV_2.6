package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.bean.RegisteAddBean;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 17:18
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteHeadAdapter extends BaseQuickAdapter<RegisteAddBean, BaseViewHolder>{

   public RegisteHeadAdapter(
	   int layoutResId, @Nullable List<RegisteAddBean> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, RegisteAddBean item) {
//	helper.getView(R.id.framelayout);
   }
}
