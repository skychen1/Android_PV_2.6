package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
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

public class InBoxAllAdapter extends BaseQuickAdapter<TCstInventoryVo, BaseViewHolder> {

   int      mOperation;
   TextView mSeven_one;
   TextView mSeven_two;
   TextView mSeven_three;
   TextView mSeven_four;
   TextView mSeven_five;
   TextView mSeven_six;
   TextView mdeleteTv;
   ImageView mdeleteIv;
   LinearLayout mLl;
   public InBoxAllAdapter(
	   int layoutResId, List<TCstInventoryVo> data) {
	super(layoutResId, data);
   }

   public InBoxAllAdapter(
	   int layoutResId, List<TCstInventoryVo> data, int operation) {
	super(layoutResId, data);
	this.mOperation = operation;
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, TCstInventoryVo item) {

	LogUtils.i("faf", "   mTCstInventoryVos  " + item.getCstName());
	LogUtils.i("faf", "   mTCstInventoryVos  " + item.getDeviceName());
	 mLl=((LinearLayout) helper.getView(R.id.seven_ll));
	if (helper.getAdapterPosition() % 2 == 0) {
	   mLl.setBackgroundResource(R.color.bg_color);
	} else {
	   mLl.setBackgroundResource(R.color.bg_f);
	}
	 mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	 mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	 mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	 mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	 mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	 mSeven_six = ((TextView) helper.getView(R.id.seven_six));
	SwipeLayout swipe = (SwipeLayout) helper.getView(R.id.swipe);
	swipe.setShowMode(SwipeLayout.ShowMode.LayDown);

	LinearLayout delete = (LinearLayout) helper.getView(R.id.ll_delete);
	TextView mdeleteTv = (TextView) helper.getView(R.id.tv_delete);
	ImageView mdeleteIv = (ImageView) helper.getView(R.id.iv_delete);

	if (item.getDeleteCount()>0){
	   mdeleteTv.setText("取消移除");
	   delete.setBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.bg_greens));
	   mdeleteIv.setVisibility(View.GONE);
	}else {
	   mdeleteTv.setText("移除");
	   delete.setBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.bg_delete));
	   mdeleteIv.setVisibility(View.VISIBLE);
	}

	String status = item.getStatus();
	mSeven_one.setText(item.getCstName());
	mSeven_two.setText(item.getEpc());
	mSeven_three.setText(item.getCstSpec());
	mSeven_four.setText(item.getExpiration());
	mSeven_five.setText(item.getDeviceName());
	mSeven_six.setText(status);
	delete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
		TCstInventoryVo inventoryVo = mData.get(helper.getAdapterPosition());
		if (inventoryVo.getDeleteCount()>0){
		   inventoryVo.setDelete(false);
		   inventoryVo.setDeleteCount(0);
		   mData.remove(helper.getAdapterPosition());
		   mData.add(inventoryVo);
		}else {
		   inventoryVo.setDelete(true);
		   inventoryVo.setDeleteCount(inventoryVo.getDeleteCount()+1);
		   mData.remove(helper.getAdapterPosition());
		   mData.add(inventoryVo);
		}

		EventBusUtils.post(new Event.EventButton(true,false));
		notifyDataSetChanged();
	   }
	});

	if (item.getIsErrorOperation()==1&&item.getDeleteCount()==0){
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.color_red));
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));

	} else {
	   if (helper.getAdapterPosition() % 2 == 0) {
		mLl.setBackgroundResource(R.color.bg_color);
	   } else {
		mLl.setBackgroundResource(R.color.bg_f);
	   }
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}

	UIUtils.initTermOfValidity(mContext, helper, item.getStopFlag(), mSeven_four);

	setDeleteView(mData.get(helper.getAdapterPosition()).isDelete(), swipe);
	if (item.getDeleteCount()>0){
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_four.setBackgroundResource(R.color.bg_color);
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mLl.setBackgroundResource(R.color.bg_color);
	}else if (item.getStopFlag()==0){
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	}


   }

   public void setDeleteView(boolean isDeleteView, SwipeLayout swipe) {
	if (isDeleteView) {
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_four.setBackgroundResource(R.color.bg_color);
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mLl.setBackgroundResource(R.color.bg_color);
	   swipe.setSwipeEnabled(true);
	} else {
	   mSeven_one.setTextColor(Color.parseColor("#333333"));
	   mSeven_two.setTextColor(Color.parseColor("#333333"));
	   mSeven_three.setTextColor(Color.parseColor("#333333"));
	   mSeven_five.setTextColor(Color.parseColor("#333333"));
//	   mSeven_six.setTextColor(Color.parseColor("#333333"));
	   swipe.setSwipeEnabled(true);
	}
   }
}
