package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TBaseDevices;
import high.rivamed.myapplication.fragment.RegisteFrag;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.RotateUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.widget.GridLayout.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.SAVE_ACTIVATION_REGISTE;
import static high.rivamed.myapplication.cont.Constants.SAVE_ONE_REGISTE;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 9:53
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteSmallAdapter extends BaseQuickAdapter<TBaseDevices, BaseViewHolder> {

   private boolean                         mType     = false;
   private SparseBooleanArray              mExpanded = new SparseBooleanArray();
   public  RegisteContextAdapter           mHeadAdapter;
   private List<TBaseDevices.tBaseDevices> mList;
   public  EditText                        mLeftName;
   public  TextView                        mLeftCode;
   public  ImageView                       mRightDelete;
   public  RadioGroup                      mGroup;
   public  RadioButton                     registe_top;
   public  RadioButton                     registe_down;
   public  RadioButton                     registe_single;

   //   public RecyclerView mRecyclerView2;
   //   public static List<TBaseThingDto.TBaseDeviceVo.TBaseDevice> tBaseDevice ;//柜子内部数据
   //   public static TBaseThingDto.TBaseDeviceVo mMTBaseThingVoBean;

   public RegisteSmallAdapter(
	   int layoutResId, @Nullable List<TBaseDevices> data) {
	super(layoutResId, data);
   }

   public void disableRadioGroup(RadioGroup testRadioGroup) {
	//	ToastUtils.showShortToast("已经激活，点击无效！");
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(false);
	}
   }

   public void enableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(true);
	}
   }

   @Override
   protected void convert(final BaseViewHolder holder, TBaseDevices item) {
	//	mMTBaseThingVoBean = new TBaseThingDto.TBaseDeviceVo();
	mLeftName = (EditText) holder.getView(R.id.head_left_name);
	mLeftCode = (TextView) holder.getView(R.id.gone_box_code);
	mGroup = (RadioGroup) holder.getView(R.id.registe_head_rg);
	registe_top = (RadioButton) holder.getView(R.id.registe_top);
	registe_down = (RadioButton) holder.getView(R.id.registe_down);
	registe_single = (RadioButton) holder.getView(R.id.registe_single);
	String trim = mLeftName.getText().toString().trim();
	registe_single.setChecked(true);
	mLeftName.setText(trim + "(单)");
	mGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
	   switch (checkedId) {
		case R.id.registe_top:
		   //		   mBoxType = "1";
		   mLeftName.setText(trim + "(上)");
		   break;
		case R.id.registe_down:
		   //		   mBoxType = "-1";
		   mLeftName.setText(trim + "(下)");
		   break;
		case R.id.registe_single:
		   //		   mBoxType = "0";
		   mLeftName.setText(trim + "(单)");
		   break;
	   }
	});
	if (item.getCabinetType()!=null&&item.getCabinetType().equals("1")) {
	   registe_top.setChecked(true);
	   registe_down.setChecked(false);
	   registe_single.setChecked(false);
	} else if (item.getCabinetType()!=null&&item.getCabinetType().equals("2")) {
	   registe_top.setChecked(false);
	   registe_down.setChecked(true);
	   registe_single.setChecked(false);
	} else if (item.getCabinetType()!=null&&item.getCabinetType().equals("0")) {
	   registe_top.setChecked(false);
	   registe_down.setChecked(false);
	   registe_single.setChecked(true);
	}
	final RecyclerView mRecyclerView2 = (RecyclerView) holder.getView(R.id.recyclerview2);
	mRightDelete = (ImageView) holder.getView(R.id.right_delete);
	final ImageView rightFold = (ImageView) holder.getView(R.id.right_fold);

	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
	   mLeftName.setText(item.getDeviceName());
	   mLeftCode.setText(item.getDeviceId());
	   if (holder.getAdapterPosition() == 0) {
		mRightDelete.setVisibility(View.GONE);
	   } else {
		mRightDelete.setVisibility(View.VISIBLE);
	   }

	} else {

	   if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)) {
		mRightDelete.setVisibility(View.GONE);
	   } else {
		if (holder.getAdapterPosition() == 0) {
		   mRightDelete.setVisibility(View.GONE);
		} else if (item.deviceName.equals("")) {
		   mRightDelete.setVisibility(View.VISIBLE);
		}
	   }
	}

	rightFold.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		Log.i("ffa", "holder.getAdapterPosition()   " + holder.getAdapterPosition());
		if (mExpanded.get(holder.getAdapterPosition())) {
		   ViewGroup.LayoutParams lps = mRecyclerView2.getLayoutParams();
		   lps.height = ViewGroup.LayoutParams.MATCH_PARENT;
		   mRecyclerView2.setLayoutParams(lps);
		   RotateUtils.rotateArrow(rightFold, true);
		   mExpanded.put(holder.getAdapterPosition(), false);
		} else {
		   ViewGroup.LayoutParams lps = mRecyclerView2.getLayoutParams();
		   lps.height = 0;
		   mRecyclerView2.setLayoutParams(lps);
		   RotateUtils.rotateArrow(rightFold, false);
		   mExpanded.put(holder.getAdapterPosition(), true);
		}
	   }
	});
	mRightDelete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		LogUtils.i(TAG, "holder.getAdapterPosition()   " + holder.getAdapterPosition());
		LogUtils.i(TAG, "RegisteFrag.mDeviceVos.size()   " + RegisteFrag.mDeviceVos.size());
		mData.remove(holder.getAdapterPosition());
		if (RegisteFrag.mDeviceVos.size() > holder.getAdapterPosition()) {
		   RegisteFrag.mDeviceVos.remove(holder.getAdapterPosition());
		}
		notifyItemRemoved(holder.getAdapterPosition());
	   }
	});

	mList = item.getList();
	//	tBaseDevice = new ArrayList<>();//柜子内部数据

	mHeadAdapter = new RegisteContextAdapter(R.layout.item_foot_small_layout, mList,
							     mRecyclerView2, holder.getAdapterPosition());

	mRecyclerView2.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerView2.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerView2.setAdapter(mHeadAdapter);
	View view = mLayoutInflater.inflate(R.layout.item_head_small_layout,
							(ViewGroup) mRecyclerView2.getParent(), false);
	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)) {
	   view.findViewById(R.id.type_de).setVisibility(View.GONE);
	   disableRadioGroup(mGroup);
	} else {
	   view.findViewById(R.id.type_de).setVisibility(View.VISIBLE);
	   enableRadioGroup(mGroup);
	}
	mHeadAdapter.addHeaderView(view);
	mHeadAdapter.setOnItemClickListener(new OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		Log.i("xxa", position + "   我是多少条");
	   }
	});

   }

}
