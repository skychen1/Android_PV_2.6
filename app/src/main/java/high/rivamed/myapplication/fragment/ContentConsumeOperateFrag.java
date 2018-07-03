package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.InBoxActivity;
import high.rivamed.myapplication.activity.OutBoxActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材操作主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentConsumeOperateFrag extends BaseSimpleFragment {

   @BindView(R.id.consume_openall_rv)
   RecyclerView mConsumeOpenallRv;
   @BindView(R.id.consume_openall_top)
   LinearLayout mConsumeOpenallTop;
   @BindView(R.id.function_title_meal)
   TextView     mFunctionTitleMeal;
   @BindView(R.id.function_cardview_meal)
   CardView     mFunctionCardviewMeal;
   @BindView(R.id.fastopen_title_form)
   TextView     mFastopenTitleForm;
   @BindView(R.id.function_cardview_form)
   CardView     mFunctionCardviewForm;
   @BindView(R.id.consume_openall_middle)
   LinearLayout mConsumeOpenallMiddle;
   @BindView(R.id.content_rb_ly)
   RadioButton  mContentRbLy;
   @BindView(R.id.content_rb_rk)
   RadioButton  mContentRbRk;
   @BindView(R.id.content_rb_yc)
   RadioButton  mContentRbYc;
   @BindView(R.id.content_rb_tb)
   RadioButton  mContentRbTb;
   @BindView(R.id.content_rb_yr)
   RadioButton  mContentRbYr;
   @BindView(R.id.content_rb_tuihui)
   RadioButton  mContentRbTuihui;
   @BindView(R.id.content_rb_tuihuo)
   RadioButton  mContentRbTuihuo;
   @BindView(R.id.content_rg)
   RadioGroup   mContentRg;
   @BindView(R.id.consume_down_rv)
   RecyclerView mConsumeDownRv;
   @BindView(R.id.consume_down)
   LinearLayout mConsumeDown;

   private HomeFastOpenAdapter mHomeFastOpenTopAdapter;
   private HomeFastOpenAdapter mHomeFastOpenDownAdapter;
   private List<String>        mTitles;

   public static ContentConsumeOperateFrag newInstance() {

	Bundle args = new Bundle();
	ContentConsumeOperateFrag fragment = new ContentConsumeOperateFrag();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.ctconsumeoperate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();
   }

   private void initData() {
	mTitles = new ArrayList<>();
	for (int i = 0; i < 14; i++) {
	   mTitles.add(i + "号柜");
	}
//	mConsumeOpenallMiddle.setVisibility(View.GONE);//此处部分医院不需要可以隐藏  根据接口来
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabBtnLeft.setText("导管室");
	mBaseTabTvTitle.setText("耗材操作");

	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
	mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									  genData1());
	mConsumeOpenallRv.setLayoutManager(layoutManager);
	mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

	mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		String title = "柜门已开";
		if (position == 0){
		   DialogUtils.showNoDialog(mContext, title, 2,"in",null);
		}else if (position == 1){
		   DialogUtils.showNoDialog(mContext, title, 2,"out",null);
		}else {
		   DialogUtils.showNoDialog(mContext, title, 2,"out","bing");
		}
		//		if (position == 0) {
		//		   int mType = 1;//1.8.3未绑定
		//		   showTwoDialog(mType);
		//		} else if (position == 1) {
		//		   int mType = 1;//1.6移出
		//		   showStoreDialog(3, mType);
		//		} else if (position == 2) {
		//		   int mType = 2;//1.7退货
		//		   showStoreDialog(2, mType);
		//		} else if (position == 3) {
		//		   int mType = 3;//1.8调拨
		//		   showStoreDialog(2, mType);
		//		} else if (position == 4) {
		//		   int mType = 2;//1.9.3请领单
		//		   showTwoDialog(mType);
		//		} else if (position == 5) {//1.2错误
		//		   String title = "耗材中包含过期耗材，请查看！";
		//		   showNoDialog(title, 1);
		//		} else if (position == 6) {//1.8.1选择患者
		//		   showRvDialog();
		//		} else {
		//		   String title = "柜门已开";
		//		   showNoDialog(title, 2);
		//		}
	   }
	});
	LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
	layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
	mHomeFastOpenDownAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
									   genData1());
	mConsumeDownRv.setLayoutManager(layoutManager2);
	mConsumeDownRv.setAdapter(mHomeFastOpenDownAdapter);
	mHomeFastOpenDownAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		int id = mContentRg.getCheckedRadioButtonId();
		if (id == -1) {
		   ToastUtils.showShort("请选择操作方式！");
		} else {
		   switch (id) {
			case R.id.content_rb_ly:
			   ToastUtils.showShort("领用！");//拿出
			   mContext.startActivity(new Intent(mContext, OutBoxActivity.class));
			   break;
			case R.id.content_rb_rk:
			   ToastUtils.showShort("入库！");//拿入
			   mContext.startActivity(new Intent(mContext, InBoxActivity.class));
			   break;
			case R.id.content_rb_yc:
			   ToastUtils.showShort("移出！");//拿出
			   mContext.startActivity(new Intent(mContext, OutBoxActivity.class));
			   break;
			case R.id.content_rb_tb:
			   ToastUtils.showShort("调拨！");//拿出
			   mContext.startActivity(new Intent(mContext, OutBoxActivity.class));
			   break;
			case R.id.content_rb_yr:
			   ToastUtils.showShort("移入！");//拿入
			   mContext.startActivity(new Intent(mContext, InBoxActivity.class));
			   break;
			case R.id.content_rb_tuihui:
			   ToastUtils.showShort("退回！");//拿入
			   mContext.startActivity(new Intent(mContext, InBoxActivity.class));
			   break;
			case R.id.content_rb_tuihuo:
			   ToastUtils.showShort("退货！");//拿出
			   mContext.startActivity(new Intent(mContext, OutBoxActivity.class));
			   break;
		   }
		}
	   }
	});
   }

   private List<Movie> genData1() {

	ArrayList<Movie> list = new ArrayList<>();
	String one;
	for (int i = 1; i < 20; i++) {
	   if (i == 1) {
		one = "全部开柜";
	   } else {
		one = i + "号柜";
	   }
	   Movie movie = new Movie(one);
	   list.add(movie);
	}
	return list;
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.function_title_meal, R.id.fastopen_title_form})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);
		LogUtils.i("sss", "base_tab_tv_name");
		popupClick();
		break;
	   case R.id.base_tab_btn_msg:
		LogUtils.i("sss", "base_tab_btn_msg");
		break;
	   case R.id.function_title_meal:
		mContext.startActivity(new Intent(mContext,OutMealActivity.class));
		break;
	   case R.id.fastopen_title_form:
		mContext.startActivity(new Intent(mContext,OutFormActivity.class));
		break;
	}
   }
}
