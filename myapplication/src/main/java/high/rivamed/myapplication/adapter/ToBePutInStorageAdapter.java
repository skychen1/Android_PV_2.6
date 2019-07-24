package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.ToBePutInStorageBean;
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static high.rivamed.myapplication.base.App.mDm;

/**
 * 项目名称:    Android_PV_2.6.8.0
 * 创建者:      DanMing
 * 创建时间:    2019/7/19 0019 9:36
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ToBePutInStorageAdapter extends
	BaseQuickAdapter<ToBePutInStorageBean.PageModelBean.RowsBean, BaseViewHolder> {
   public ToBePutInStorageAdapter(@Nullable List<ToBePutInStorageBean.PageModelBean.RowsBean> data) {
	super(R.layout.item_tobeputin_six_layout, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, ToBePutInStorageBean.PageModelBean.RowsBean item) {
      helper.setText(R.id.seven_one,item.getCstName()).setText(R.id.seven_two,item.getCstSpec()).setText(R.id.seven_three,item.getEpc())
	.setText(R.id.seven_four,"展开").setText(R.id.seven_five,item.getExpiryDate()).setText(R.id.seven_six,item.getStatus());

	helper.getView(R.id.seven_four).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_four),mDm.widthPixels);
	});
   }


}
