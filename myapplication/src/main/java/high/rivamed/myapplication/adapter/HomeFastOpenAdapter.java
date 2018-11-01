package high.rivamed.myapplication.adapter;

import android.support.v7.widget.CardView;
import android.widget.LinearLayout;
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

public class HomeFastOpenAdapter
	extends BaseQuickAdapter<BoxSizeBean.TbaseDevicesBean, BaseViewHolder> {

   TextView mFastopenTitle;

   public HomeFastOpenAdapter(int layout, List<BoxSizeBean.TbaseDevicesBean> data) {
	super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, BoxSizeBean.TbaseDevicesBean item) {
	findId(helper);

	   mFastopenTitle.setText(item.getDeviceName());
   }

   private void findId(BaseViewHolder helper) {

	mFastopenTitle = ((TextView) helper.getView(R.id.fastopen_title));

   }
}
