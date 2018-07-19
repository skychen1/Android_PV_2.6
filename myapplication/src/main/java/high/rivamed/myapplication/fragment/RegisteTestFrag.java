package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.RegisteTestAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.RegisteTestBean;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/12 18:06
 * 描述:        工程模式中功能验证
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteTestFrag extends SimpleFragment {

   @BindView(R.id.test_recyclerview)
   RecyclerView mTestRecyclerview;
   private int mLv0Count = 3;

   public static RegisteTestFrag newInstance() {
	Bundle args = new Bundle();
	RegisteTestFrag fragment = new RegisteTestFrag();
	//	args.putInt(TYPE_SIZE, param);
	//	args.putString(TYPE_PAGE, type);
	//	fragment.setArguments(args);
	return fragment;

   }

   @Override
   public int getLayoutId() {
	return R.layout.fragment_registe_test_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();
   }

   private void initData() {

	RegisteTestAdapter testAdapter = new RegisteTestAdapter(generateData1());
	mTestRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mTestRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mTestRecyclerview.setAdapter(testAdapter);
	testAdapter.expandAll();
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   private ArrayList<MultiItemEntity> generateData1() {
	int lv1Count = 5;
	ArrayList<MultiItemEntity> res = new ArrayList<>();

	for (int i = 0; i < mLv0Count; i++) {
	   RegisteTestBean testBean = new RegisteTestBean((i + 1) + "号柜");
	   for (int x = 0; x < 1; x++) {
		RegisteTestBean.RegisteTestTitleBean testTitleBean = new RegisteTestBean.RegisteTestTitleBean(
			"检测项目", "操作", "信息反馈", "备注");
		testBean.addSubItem(testTitleBean);
		for (int j = 0; j < lv1Count; j++) {
		   if (j == 0) {
			RegisteTestBean.RegisteTestContextBean testContextBean = new RegisteTestBean.RegisteTestContextBean(
				"指纹仪", "采集指纹",null,"按下指纹采集，超过5S无反馈数据为失败");
			testTitleBean.addSubItem(testContextBean);
		   }else if (j==1){
			RegisteTestBean.RegisteTestContextBean testContextBean = new RegisteTestBean.RegisteTestContextBean(
				"IC卡", "识别IC卡",null,"超过5S无反馈数据为失败");
			testTitleBean.addSubItem(testContextBean);
		   }
		   else if (j==2){
			RegisteTestBean.RegisteTestContextBean testContextBean = new RegisteTestBean.RegisteTestContextBean(
				"门锁", "开锁",null,"锁状态显示与要求不一致为失败");
			testTitleBean.addSubItem(testContextBean);
		   }
		   else if (j==3){
			RegisteTestBean.RegisteTestContextBean testContextBean = new RegisteTestBean.RegisteTestContextBean(
				"天线功率", "设置功率",null,"超过5S无反馈数据为失败");
			testTitleBean.addSubItem(testContextBean);
		   }else {
			RegisteTestBean.RegisteTestContextBean testContextBean = new RegisteTestBean.RegisteTestContextBean(
				"标签读取", "开始","查看读取结果","锁状态显示与要求不一致为失败");
			testTitleBean.addSubItem(testContextBean);
		   }
		}
	   }
	   res.add(testBean);
	}
	return res;
   }

   //
   //      private List<TBaseDevice> generateData() {
   //   	mSmallmac = new ArrayList<>();
   //   	mTBaseDevicesAll = new ArrayList<>();
   //   	mTBaseDevicesSmall = new ArrayList<>();
   //   	TBaseDevice.tBaseDevice.partsmacBean partsmacBean1 = new TBaseDevice.tBaseDevice.partsmacBean();
   //   	TBaseDevice.tBaseDevice registeBean1 = new TBaseDevice.tBaseDevice();
   //   	TBaseDevice registeAddBean1 = new TBaseDevice();
   //   	for (int i = 0; i < 5; i++) {//第三层内部部件标识的数据
   //   	   partsmacBean1.setPartsmacnumber("3232323+" + i);
   //   	   mSmallmac.add(partsmacBean1);
   //   	}
   //   	for (int i = 0; i < 1; i++) {//第二层柜体内条目的数据
   //   	   registeBean1.setPartsname("");
   //   	   registeBean1.setPartsmac(mSmallmac);
   //   	   mTBaseDevicesSmall.add(registeBean1);
   //   	}
   //   	for (int i = 0; i < 1; i++) {//第一层数据
   //   	   if (i == 0) {
   //   		registeAddBean1.setBoxname("1号柜");
   //   	   } else {
   //   		registeAddBean1.setBoxname("");
   //   	   }
   //   	   registeAddBean1.setList(mTBaseDevicesSmall);
   //   	   mTBaseDevicesAll.add(registeAddBean1);
   //   	}
   //   	return mTBaseDevicesAll;
   //      }

}
