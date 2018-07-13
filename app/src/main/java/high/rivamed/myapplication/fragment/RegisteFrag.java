package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegisteHeadAdapter;
import high.rivamed.myapplication.adapter.RegisteSmallAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.RegisteAddBean;

import static android.widget.GridLayout.VERTICAL;

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

   private List<RegisteAddBean>                          mAddBeans;
   private List<RegisteAddBean.RegisteBean>              mAddBeansSmall;
   private List<RegisteAddBean.RegisteBean.partsmacBean> mSmallmac;
   private RegisteSmallAdapter                           mSmallAdapter;
   private RegisteHeadAdapter                            mHeadAdapter;

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
	initData();
   }

   private void initData() {
	mAddBeans = new ArrayList<>();
	mAddBeansSmall = new ArrayList<>();
	mSmallmac = new ArrayList<>();
	RegisteAddBean.RegisteBean.partsmacBean partsmacBean1 = new RegisteAddBean.RegisteBean.partsmacBean();
	RegisteAddBean.RegisteBean registeBean1 = new RegisteAddBean.RegisteBean();
	RegisteAddBean registeAddBean1 = new RegisteAddBean();
	for (int i=0;i<5;i++){//第三层内部部件标识的数据
	   partsmacBean1.setPartsmacnumber("3232323+"+i);
	   mSmallmac.add(partsmacBean1);
	}
	for (int i=0;i<3;i++){//第二层柜体内条目的数据
	   registeBean1.setPartsname("");
	   registeBean1.setPartsmac(mSmallmac);
	   mAddBeansSmall.add(registeBean1);

	}
	for (int i=0;i<2;i++){//第一层数据
	   if (i==0){
		registeAddBean1.setBoxname("1号柜");
	   }else {
		registeAddBean1.setBoxname("");
	   }

	   registeAddBean1.setList(mAddBeansSmall);
	   mAddBeans.add(registeAddBean1);
	}

	//	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));

	mSmallAdapter = new RegisteSmallAdapter( R.layout.item_registe_head_layout,mAddBeans);

//	mHeadAdapter = new RegisteHeadAdapter(R.layout.item_registe_all,mAddBeans);
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerview.setAdapter(mSmallAdapter);

   }

   @Override
   public void onBindViewBefore(View view) {

   }


   @OnClick({R.id.frag_registe_right, R.id.frag_registe_left, R.id.fragment_btn_one})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.frag_registe_right:
		break;
	   case R.id.frag_registe_left:
		break;
	   case R.id.fragment_btn_one:
		break;
	}
   }
}
