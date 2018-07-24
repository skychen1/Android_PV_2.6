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

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TBaseDevices;
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

public class RegisteContextAdapter extends BaseQuickAdapter<TBaseDevices.tBaseDevices, BaseViewHolder>{
public           RecyclerView                    recyclerView;
   public        TBaseDevices.tBaseDevices       mTBaseDevice;
   private       DeleteClickListener             mDeleteListener;
   private       List<TBaseDevices.tBaseDevices> data;
   private final List<TBaseDevices.tBaseDevices> mTBaseList = new ArrayList<>();;
   public String mString;
   public int mTopPos;
   public TextView mMFootMac;
   private MacPopupWindow mMacPopupWindow;
   public EditText mMFootName;
   public EditText mMFootIp;
   public ImageView mMFootDelete;
   public ImageView mMFootAdd;


   public RegisteContextAdapter(
	   int layoutResId, @Nullable List<TBaseDevices.tBaseDevices> data, RecyclerView recyclerView,int mTopPos) {
	super(layoutResId, data);
	this.recyclerView =recyclerView;
//	this.mDeleteListener = listener;
	this.data = data;
	this.mTopPos = mTopPos;


   }

   @Override
   protected void convert(
	   final BaseViewHolder helper, TBaseDevices.tBaseDevices item) {
	mMFootName = (EditText) helper.getView(R.id.foot_name);
	mMFootMac = (TextView) helper.getView(R.id.foot_mac);
	mMFootIp = (EditText) helper.getView(R.id.foot_ip);
	mMFootDelete = (ImageView) helper.getView(R.id.foot_delete);
	mMFootAdd = (ImageView) helper.getView(R.id.foot_add);
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
	   mMFootDelete.setVisibility(View.GONE);
	   mMFootAdd.setVisibility(View.VISIBLE);
	}else if (mData.size()==helper.getAdapterPosition()){
	   mMFootDelete.setVisibility(View.VISIBLE);
	   mMFootAdd.setVisibility(View.VISIBLE);
	}else {
	   mMFootDelete.setVisibility(View.VISIBLE);
	   mMFootAdd.setVisibility(View.GONE);
	}
//	int pos = helper.getAdapterPosition();
//	Log.i("xxa", (pos-1) + "   xxx");
////	mFootDelete.setOnClickListener(mDeleteListener);
////	mFootDelete.setTag(pos-1);
	mMFootDelete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		ToastUtils.showShort("" + mData.size()+"    "+pos);

		data.remove(pos-1);


//		mData.add(0,tBaseDevices);
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
	mMFootAdd.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		mMFootDelete.setVisibility(View.VISIBLE);
		mMFootAdd.setVisibility(View.GONE);
		ToastUtils.showShort("" + mData.size()+"    "+pos);
//		mTBaseDevice = new TBaseDevices.tBaseDevices();
//		mTBaseDevice.setPartsname(mFootName.getText().toString().trim());
//		mTBaseDevice.setPartip(mFootIp.getText().toString().trim());
//		mTBaseDevice.setPartsmactext(mFootMac.getText().toString().trim());
//		mTBaseList.add(pos-1,mTBaseDevice);
		TBaseDevices.tBaseDevices tBaseDevice = new TBaseDevices.tBaseDevices();
		data.add(pos, tBaseDevice);
//		notifyItemChanged(pos);
		notifyItemInserted(pos+1);
//		addData(pos, tBaseDevices);
	   }
	});
	mMFootMac.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		Log.i("xxa",helper.getAdapterPosition()+"   条");
		mMacPopupWindow = new MacPopupWindow(mContext);
		mMacPopupWindow.showPopupWindow(helper.getView(R.id.foot_mac),pos);
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
