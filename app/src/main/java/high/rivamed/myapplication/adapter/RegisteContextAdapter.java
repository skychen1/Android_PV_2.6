package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.TBaseDevice;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.MacPopupWindow;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/16 13:41
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteContextAdapter extends BaseQuickAdapter<TBaseDevice.tBaseDevice, BaseViewHolder>{
public RecyclerView recyclerView;
   public TBaseDevice.tBaseDevice mTBaseDevice;
   private DeleteClickListener mDeleteListener;
   private List<TBaseDevice.tBaseDevice> data;
   private final List<TBaseDevice.tBaseDevice> mTBaseList = new ArrayList<>();;
   private String mString;
   public TextView mMFootMac;
   private MacPopupWindow mMacPopupWindow;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onPopupBean(Event.PopupEvent  event) {
	if (!event.isMute){
	   mString = event.mString;
	   mMFootMac.setText(mString);
	   mMacPopupWindow.dismiss();
	}

   }

   public RegisteContextAdapter(
	   int layoutResId, @Nullable List<TBaseDevice.tBaseDevice> data, RecyclerView recyclerView) {
	super(layoutResId, data);
	EventBusUtils.register(this);
	this.recyclerView =recyclerView;
//	this.mDeleteListener = listener;
	this.data = data;


   }

   @Override
   protected void convert(
	   final BaseViewHolder helper, TBaseDevice.tBaseDevice item) {
	final EditText mFootName = (EditText) helper.getView(R.id.foot_name);
	mMFootMac = (TextView) helper.getView(R.id.foot_mac);
	final EditText mFootIp = (EditText) helper.getView(R.id.foot_ip);
	final ImageView mFootDelete = (ImageView) helper.getView(R.id.foot_delete);
	final ImageView mFootAdd = (ImageView) helper.getView(R.id.foot_add);
	final LinearLayout mLinearLayout = (LinearLayout) helper.getView(R.id.foot_ll);

//	if (mTBaseList.size()>0){
//	   mFootIp.setText(mTBaseList.get(helper.getAdapterPosition()-2).getPartip());
//	}else {
//	   mFootIp.setText("");
//	}
	if (SPUtils.getBoolean(UIUtils.getContext(), "activationRegiste")){
	   mLinearLayout.setVisibility(View.GONE);
	}else {
	   mLinearLayout.setVisibility(View.VISIBLE);
	}
	if (mData.size()==1){
	   mFootDelete.setVisibility(View.GONE);
	   mFootAdd.setVisibility(View.VISIBLE);
	}else if (mData.size()==helper.getAdapterPosition()){
	   mFootDelete.setVisibility(View.VISIBLE);
	   mFootAdd.setVisibility(View.VISIBLE);
	}else {
	   mFootDelete.setVisibility(View.VISIBLE);
	   mFootAdd.setVisibility(View.GONE);
	}
//	int pos = helper.getAdapterPosition();
//	Log.i("xxa", (pos-1) + "   xxx");
////	mFootDelete.setOnClickListener(mDeleteListener);
////	mFootDelete.setTag(pos-1);
	mFootDelete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		ToastUtils.showShort("" + mData.size()+"    "+pos);

		data.remove(pos-1);


//		mData.add(0,tBaseDevice);
//		notifyItemMoved(pos-1, 0);
//		notifyItemRangeChanged(pos,getItemCount());
//		if (mTBaseList.get(pos-1)!=null&&mData.size()==mTBaseList.size()){
//		   mTBaseList.remove(pos-1);
//		}
//		notifyItemChanged(pos);
		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (mData.size()==1){
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_delete).setVisibility(View.GONE);
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_add).setVisibility(View.VISIBLE);
		}else {
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_delete).setVisibility(View.VISIBLE);
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_add).setVisibility(View.VISIBLE);
		}

	   }
	});
	mFootAdd.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		mFootDelete.setVisibility(View.VISIBLE);
		mFootAdd.setVisibility(View.GONE);
		ToastUtils.showShort("" + mData.size()+"    "+pos);
//		mTBaseDevice = new TBaseDevice.tBaseDevice();
//		mTBaseDevice.setPartsname(mFootName.getText().toString().trim());
//		mTBaseDevice.setPartip(mFootIp.getText().toString().trim());
//		mTBaseDevice.setPartsmactext(mFootMac.getText().toString().trim());
//		mTBaseList.add(pos-1,mTBaseDevice);
		TBaseDevice.tBaseDevice tBaseDevice = new TBaseDevice.tBaseDevice();
		data.add(pos, tBaseDevice);
//		notifyItemChanged(pos);
		notifyItemInserted(pos+1);
//		addData(pos, tBaseDevice);
	   }
	});
	mMFootMac.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		Log.i("xxa",helper.getAdapterPosition()+"   条");
		mMacPopupWindow = new MacPopupWindow(mContext);
		mMacPopupWindow.showPopupWindow(helper.getView(R.id.foot_mac));
	   }
	});

   }
   public static abstract class DeleteClickListener implements View.OnClickListener {

	/**
	 * 基类的onClick方法
	 */
	@Override
	public void onClick(View v) {
	   myOnClick((Integer) v.getTag(), v);
	}

	public abstract void myOnClick(int position, View v);
   }
}
