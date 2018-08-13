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
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.MacPopupWindow;
import high.rivamed.myapplication.views.TypePopupWindow;

import static high.rivamed.myapplication.cont.Constants.SAVE_ACTIVATION_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/16 13:41
 * 描述:        柜体信息内部部件列表adapter
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
   public TextView mMFootName;
   public EditText mMFootIp;
   public ImageView mMFootDelete;
   public ImageView mMFootAdd;
   private TypePopupWindow mTypePopupWindow;
   private List<TBaseDevices.tBaseDevices.partsmacBean> mPartsmac;
   private List<TBaseDevices.tBaseDevices.partsnameBean> mPartsmacName;
   private TextView mGone_dictid;
   private TextView mGone_devicetype;
   private TextView mGone_deviceCode;

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
	mMFootName = (TextView) helper.getView(R.id.foot_name);
	mMFootMac = (TextView) helper.getView(R.id.foot_mac);
	mMFootIp = (EditText) helper.getView(R.id.foot_ip);
	mMFootDelete = (ImageView) helper.getView(R.id.foot_delete);
	mMFootAdd = (ImageView) helper.getView(R.id.foot_add);
	mGone_dictid = (TextView)helper.getView(R.id.gone_dictid);
	mGone_devicetype =(TextView) helper.getView(R.id.gone_devicetype);
	mGone_deviceCode =(TextView) helper.getView(R.id.gone_device_code);
	final LinearLayout mLinearLayout = (LinearLayout) helper.getView(R.id.foot_ll);

//	if (mTBaseList.size()>0){
//	   mFootIp.setText(mTBaseList.get(helper.getAdapterPosition()-2).getPartip());
//	}else {
//	   mFootIp.setText("");
//	}
	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
//	   List<TBaseDevices.tBaseDevices.partsmacBean> partsmac = item.getPartsmac();

	   mMFootName.setText(item.getPartsname());
	   mMFootMac.setText(item.getPartmac());
	   mMFootIp.setText(item.getPartip());
	   mGone_dictid.setText(item.getDictId());
	   mGone_devicetype.setText(item.getDeviceType());
	   mGone_deviceCode.setText(item.getDeviceCodes());
	}


	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)){
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

	mMFootDelete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();

		data.remove(pos-1);

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

	mPartsmac = item.getPartsmac();
	mPartsmacName = item.getPartsmacName();
	mMFootAdd.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		mMFootDelete.setVisibility(View.VISIBLE);
		mMFootAdd.setVisibility(View.GONE);

		TBaseDevices.tBaseDevices tBaseDevice = new TBaseDevices.tBaseDevices();
		tBaseDevice.setPartsmac(mPartsmac);
		tBaseDevice.setPartsmacName(mPartsmacName);
		data.add(pos, tBaseDevice);
		notifyItemInserted(pos+1);

	   }
	});


	mMFootMac.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		Log.i("xxa",helper.getAdapterPosition()+"   条");
		Log.i("xxa","mPartsmac   "+(data.get(pos-1).getPartsmac()));
		Log.i("xxa","mPartsmac   "+(data.get(pos-1).getPartsmac().size()));
		mMacPopupWindow = new MacPopupWindow(mContext, data.get(pos-1).getPartsmac());
		mMacPopupWindow.showPopupWindow(helper.getView(R.id.foot_mac),helper.getView(R.id.foot_ip),pos);
	   }
	});

	mMFootName.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();

		Log.i("xxa",helper.getAdapterPosition()+"   条");
		View foot_name = helper.getView(R.id.foot_name);

		mTypePopupWindow = new TypePopupWindow(mContext, data.get(pos-1).getPartsmacName());
		mTypePopupWindow.showPopupWindow(foot_name, mGone_dictid, mGone_devicetype, pos);
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
