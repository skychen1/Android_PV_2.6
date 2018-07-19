package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegisteSmallAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.TBaseDevice;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/12 18:06
 * 描述:        设备注册和激活界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteFrag extends SimpleFragment {

   @BindView(R.id.frag_registe_name_edit)
   EditText     mFragRegisteNameEdit;
   @BindView(R.id.frag_registe_model_edit)
   EditText     mFragRegisteModelEdit;
   @BindView(R.id.frag_registe_number_edit)
   EditText     mFragRegisteNumberEdit;
   @BindView(R.id.frag_registe_localip_edit)
   EditText     mFragRegisteLocalipEdit;
   @BindView(R.id.frag_registe_severip_edit)
   EditText     mFragRegisteSeveripEdit;
   @BindView(R.id.frag_registe_right)
   TextView     mFragRegisteRight;
   @BindView(R.id.frag_registe_left)
   TextView     mFragRegisteLeft;
   @BindView(R.id.recyclerview)
   RecyclerView mRecyclerview;
   @BindView(R.id.fragment_btn_one)
   TextView     mFragmentBtnOne;

   private RegisteSmallAdapter           mSmallAdapter;
   private List<TBaseDevice>             mTBaseDevicesAll;
   private List<TBaseDevice.tBaseDevice> mTBaseDevicesSmall;
   int i = generateData().size();

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onActivationEvent(Event.activationEvent  event) {
	if (event.isActivation){
	   event.dialog.dismiss();
	   SPUtils.putBoolean(UIUtils.getContext(), "activationRegiste", true);//激活
	   ToastUtils.showShort("设备已激活！");
	   mFragmentBtnOne.setText("已激活");
	   mFragmentBtnOne.setEnabled(false);
	}

   }
   public static RegisteFrag newInstance() {
	Bundle args = new Bundle();
	RegisteFrag fragment = new RegisteFrag();
	//	args.putInt(TYPE_SIZE, param);
	//	args.putString(TYPE_PAGE, type);
	//	fragment.setArguments(args);
	return fragment;
   }

   @Override
   public int getLayoutId() {
	return R.layout.fragment_registe_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	initData();
   }

   private void initData() {
	initListener();

	mSmallAdapter = new RegisteSmallAdapter(R.layout.item_registe_head_layout, generateData());
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
//	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerview.setAdapter(mSmallAdapter);

	if (SPUtils.getBoolean(UIUtils.getContext(), "oneRegiste")) {
	   if (SPUtils.getBoolean(UIUtils.getContext(), "activationRegiste")){
		mFragmentBtnOne.setText("已激活");
		mFragmentBtnOne.setEnabled(false);
//		mSmallAdapter.mHeadAdapter.getHeaderLayout().getChildAt(0).findViewById(R.id.foot_mac).setVisibility(View.GONE);
//		mSmallAdapter.mHeadAdapter.getChildAt(0);
	   }else {
		mFragmentBtnOne.setText("激 活");
		mFragmentBtnOne.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			DialogUtils.showRegisteDialog(mContext,_mActivity);
		   }
		});
	   }
	} else {
	   mFragmentBtnOne.setText("预注册");
	   mFragmentBtnOne.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   SPUtils.putBoolean(UIUtils.getContext(), "oneRegiste", true);
		   initData();
		   ToastUtils.showShort("注册成功！");
		   mFragmentBtnOne.setText("激 活");
		}
	   });
	}

   }

   private void initListener() {

   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick({R.id.frag_registe_right, R.id.frag_registe_left, R.id.fragment_btn_one})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_registe_right:
		mSmallAdapter.addData(generateData());
		mSmallAdapter.notifyItemChanged(i);
		i++;
		break;
	   case R.id.frag_registe_left:
		break;
	   case R.id.fragment_btn_one:
		break;
	}
   }

   private List<TBaseDevice.tBaseDevice.partsmacBean> mSmallmac;

   private List<TBaseDevice> generateData() {
	mSmallmac = new ArrayList<>();
	mTBaseDevicesAll = new ArrayList<>();
	mTBaseDevicesSmall = new ArrayList<>();
	TBaseDevice.tBaseDevice.partsmacBean partsmacBean1 = new TBaseDevice.tBaseDevice.partsmacBean();
	TBaseDevice.tBaseDevice registeBean1 = new TBaseDevice.tBaseDevice();
	TBaseDevice registeAddBean1 = new TBaseDevice();
	for (int i = 0; i < 5; i++) {//第三层内部部件标识的数据
	   partsmacBean1.setPartsmacnumber("3232323+" + i);
	   mSmallmac.add(partsmacBean1);
	}
	for (int i = 0; i < 1; i++) {//第二层柜体内条目的数据
	   registeBean1.setPartsname("");
	   registeBean1.setPartsmac(mSmallmac);
	   mTBaseDevicesSmall.add(registeBean1);
	}
	for (int i = 0; i < 1; i++) {//第一层数据
	   if (i == 0) {
		registeAddBean1.setBoxname("1号柜");
	   } else {
		registeAddBean1.setBoxname("");
	   }
	   registeAddBean1.setList(mTBaseDevicesSmall);
	   mTBaseDevicesAll.add(registeAddBean1);
	}
	return mTBaseDevicesAll;
   }
}
