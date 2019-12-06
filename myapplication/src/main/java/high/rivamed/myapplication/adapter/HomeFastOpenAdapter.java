package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

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
	extends BaseQuickAdapter<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean, BaseViewHolder> {

   public HomeBoxAdapter mDataAdapter;
   public RadioGroup  RadioGroup;

   public HomeFastOpenAdapter(@Nullable List<BoxSizeBean.DeviceTypeVoBean.DeviceVosBean> data,
					RadioGroup RadioGroup) {
	super(R.layout.item_home_box_layout, data);
	this.RadioGroup = RadioGroup ;
   }

   @Override
   protected void convert(BaseViewHolder helper, BoxSizeBean.DeviceTypeVoBean.DeviceVosBean item) {
	RecyclerView mRecyclerView=(RecyclerView)helper.getView(R.id.recyclerviews);
	mDataAdapter = new HomeBoxAdapter(item.getDevices());
	mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerView.setAdapter(mDataAdapter);
	mDataAdapter.setOnItemClickListener(new OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		int id = RadioGroup.getCheckedRadioButtonId();
		if (id == -1) {
		   ToastUtils.showShortToast("请选择操作方式！");
		} else {
		   //点击柜子进行操作
		   BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX item = (BoxSizeBean.DeviceTypeVoBean.DeviceVosBean.DevicesBeanX) adapter
			   .getItem(position);
		   String deviceId = item.getDeviceId();
		   Log.i("deviceId", "deviceId    " + deviceId + "    " + item.getDeviceName());
		   if (!UIUtils.isFastDoubleClick3()) {
			EventBusUtils.post(new Event.SelectOption(deviceId, id));
		   } else {
			ToastUtils.showShortToast("请勿频繁操作！");
		   }
		}
	   }
	});

   }
}
