package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.SelfCheckAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.SelfCheckBean;
import high.rivamed.myapplication.bean.SelfCheckTitleBean;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/12 18:06
 * 描述:        工程模式中设备自检
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteSelfCheckFrag extends SimpleFragment {

   @BindView(R.id.selfcheck_title_text)
   TextView     mSelfcheckTitleText;
   @BindView(R.id.selfcheck_btn_ll)
   LinearLayout mSelfcheckBtn;
   @BindView(R.id.selfcheck_progress)
   ProgressBar  mSelfcheckProgress;
   @BindView(R.id.selfcheck_start)
   TextView     mSelfcheckStart;
   @BindView(R.id.selfcheck_text)
   TextView     mSelfcheckText;
   @BindView(R.id.selfcheck_rv_title)
   LinearLayout mSelfcheckRvTitle;
   @BindView(R.id.selfcheck_recyclerview)
   RecyclerView mSelfcheckRecyclerview;
   private int                 mLayout;
   private View                mHeadView;
   public static RegisteSelfCheckFrag newInstance() {
	Bundle args = new Bundle();
	RegisteSelfCheckFrag fragment = new RegisteSelfCheckFrag();
	//	args.putInt(TYPE_SIZE, param);
	//	args.putString(TYPE_PAGE, type);
	//	fragment.setArguments(args);
	return fragment;

   }

   @Override
   public int getLayoutId() {
	return R.layout.fragment_selfcheck_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();
   }

   private void initData() {
      mSelfcheckStart.setText("开始检测");
      mSelfcheckProgress.setVisibility(View.GONE);
      String[] array = mContext.getResources().getStringArray(R.array.self_check_title);
      List<String>  titeleList = Arrays.asList(array);
      mLayout = R.layout.item_self_six_layout;
      mHeadView = getLayoutInflater().inflate(R.layout.item_self_six_title_layout,
                                              (ViewGroup) mSelfcheckRvTitle.getParent(), false);
      ((TextView) mHeadView.findViewById(R.id.seven_one)).setText("");
      ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(0));
      ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(1));
      ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(2));
      ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(3));
      ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(4));

	mHeadView.setBackgroundResource(R.color.bg_green);
	SelfCheckAdapter checkAdapter = new SelfCheckAdapter(generateData1());
	mSelfcheckRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mSelfcheckRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mSelfcheckRecyclerview.setAdapter(checkAdapter);
	mSelfcheckRvTitle.addView(mHeadView);
	checkAdapter.expandAll();
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   @OnClick(R.id.selfcheck_btn_ll)
   public void onViewClicked() {
      mSelfcheckStart.setText("正在检测");
      mSelfcheckProgress.setVisibility(View.VISIBLE);
      mSelfcheckBtn.setEnabled(false);
   }

   //   private List<TBaseDevice.tBaseDevice.partsmacBean> mSmallmac;
   //

   private ArrayList<MultiItemEntity> generateData1() {
      int lv0Count = 5;
      int lv1Count = 3;

	ArrayList<MultiItemEntity> res = new ArrayList<>();

      for (int i = 0; i < lv0Count; i++) {
	   SelfCheckBean selfCheckBean = new SelfCheckBean("高值耗材"+i+"号柜","reader"+i,"mac"+i,"192.168.1.2"+i,i);
         for (int j = 0; j < lv1Count; j++) {
		SelfCheckTitleBean selfCheckTitleBean = new SelfCheckTitleBean("reader" + j, "type" + j, "mac" + j, "192.168.1.2" + j, j);
		selfCheckBean.addSubItem(selfCheckTitleBean);
         }
         res.add(selfCheckBean);
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
