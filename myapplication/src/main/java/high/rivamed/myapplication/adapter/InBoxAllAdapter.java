package high.rivamed.myapplication.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.InBoxDtoBean;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/29 18:00
 * 描述:        入柜的
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class InBoxAllAdapter extends BaseQuickAdapter<InBoxDtoBean.TCstInventoryVosBean,BaseViewHolder>{

   public InBoxAllAdapter(
	   int layoutResId,  List<InBoxDtoBean.TCstInventoryVosBean> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InBoxDtoBean.TCstInventoryVosBean item) {
	if (helper.getAdapterPosition() % 2 == 0) {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
	} else {
	   ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	}
	LogUtils.i("faf","   mTCstInventoryVos  "+item.getCstName());
	LogUtils.i("faf","   mTCstInventoryVos  "+item.getDeviceName());
	TextView mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	TextView mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	TextView mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	TextView mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	TextView mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	TextView mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	String status = item.getStatus();
	mSeven_one.setText(item.getCstName());
	mSeven_two.setText(item.getEpc());
	mSeven_three.setText(item.getCstSpec());
	mSeven_four.setText(item.getExpiration());
	mSeven_five.setText(item.getDeviceName());
	mSeven_six.setText(status);
	UIUtils.initTermOfValidity(mContext,helper, item.getStopFlag(), mSeven_four);
	if (status.equals("禁止入库")||status.equals("禁止移入")||status.equals("禁止退回")  ) {
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.color_red));
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   if (helper.getAdapterPosition() % 2 == 0) {
		mSeven_four.setBackgroundResource(R.color.bg_color);
	   } else {
		mSeven_four.setBackgroundResource(R.color.bg_f);
	   }
	}
   }
}
