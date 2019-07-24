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
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static high.rivamed.myapplication.base.App.mDm;

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

public class InBoxAllAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

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
	   int layoutResId, List<InventoryVo> data) {
	super(layoutResId, data);
   }

   public InBoxAllAdapter(
	   int layoutResId, List<InventoryVo> data, int operation) {
	super(layoutResId, data);
	this.mOperation = operation;
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryVo item) {

	 mLl=((LinearLayout) helper.getView(R.id.seven_ll));
	   mLl.setBackgroundResource(R.color.bg_f);
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
	mSeven_four.setText(item.getExpiryDate());
	mSeven_five.setText(item.getDeviceName());
	helper.setText(R.id.seven_seven,"展开");
	helper.getView(R.id.seven_seven).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_seven),mDm.widthPixels);
	});
	if (status!=null&&((status.equals("3")&&item.getOperationStatus()!=98)||status.equals("4"))){
	   if (item.getExpireStatus()==0){
		mSeven_six.setText("已过期");
	   }else {
		mSeven_six.setText("领用");
	   }
	}else if (null==status){
	   mSeven_six.setText("异常耗材");
	}else if (status!=null&&status.equals("2")){
	   mSeven_six.setText("已入库");
	}else if (status!=null&&status.equals("9")){
	   mSeven_six.setText("移出");
	}else if (status!=null&&status.equals("8")){
	   mSeven_six.setText("退货");
	}else {
	   mSeven_six.setText(status);
	}
	delete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
		InventoryVo inventoryVo = mData.get(helper.getAdapterPosition());
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

	if ((item.getIsErrorOperation()==1&&item.getDeleteCount()==0)||(item.getIsErrorOperation()==1 &&item.getDeleteCount()==0 && item.getExpireStatus() != 0 && mOperation != 8)){
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	   mSeven_six.setBackgroundResource(R.drawable.bg_text_red);
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));

	} else {

	   mSeven_six.setBackgroundResource(R.color.bg_f);
	   mLl.setBackgroundResource(R.color.bg_f);
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}
	setDeleteView(mData.get(helper.getAdapterPosition()).isDelete(), swipe);
	if (item.getDeleteCount()>0){
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_four.setBackgroundResource(R.color.bg_color);
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_six.setBackgroundResource(R.color.bg_color);
	   mLl.setBackgroundResource(R.color.bg_color);
	}else if (item.getIsErrorOperation() == 1&&item.getDeleteCount()==0){
	   mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));

	}
	if (item.getExpireStatus()!=null){
	   UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), item.getExpireStatus(), mSeven_four);
	}
	if (item.getOperationStatus()==99){
	   mSeven_one.setText("/");
	   mSeven_three.setText("/");
	   mSeven_four.setText("/");
	   mSeven_six.setText("断网放入");
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
	   mSeven_four.setBackgroundResource(R.color.bg_f);
	   mLl.setBackgroundResource(R.color.bg_f);
//	   mSeven_six.setTextColor(Color.parseColor("#333333"));
	   swipe.setSwipeEnabled(true);
	}
   }
}
