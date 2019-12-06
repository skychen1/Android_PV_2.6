package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BoxSizeBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/27 10:59
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class HomeBoxAdapter
	extends BaseQuickAdapter<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX, BaseViewHolder> {

   TextView mFastopenTitle;
   TextView mFastopenTitle2;

   public HomeBoxAdapter(@Nullable List<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX> data) {
	super(R.layout.item_home_fastopen_layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX item) {
	findId(helper);
	if (item.getDeviceName().equals("全部")){
	   mFastopenTitle.setText(item.getDeviceName()+"开柜");
	}else {
	   mFastopenTitle.setText(item.getDeviceName());
	}
//	mFastopenTitle2.setText(item.getDeviceName());
   }

   private void findId(BaseViewHolder helper) {

	mFastopenTitle = ((TextView) helper.getView(R.id.fastopen_title));
//	mFastopenTitle2 = ((TextView) helper.getView(R.id.fastopen_title2));

   }
}
