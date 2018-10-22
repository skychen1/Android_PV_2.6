package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.OutFormAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.fragment.PublicTimelyFrag;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.STYPE_FORM;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/29 14:45
 * 描述:        请领单主界面（医嘱）
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutFormActivity extends BaseSimpleActivity {

   @BindView(R.id.out_form_rv)
   RecyclerView   mOutFormRv;
   @BindView(R.id.cttimecheck_viewpager)
   ViewPager      mCttimecheckViewpager;
//   @BindView(R.id.timely_ll)
//   LinearLayout   mLinearLayout;
   @BindView(R.id.recyclerview_null)
   RelativeLayout mRecyclerviewNull;
   public int mSize;

   private TableTypeView  mTypeView;
   private OutFormAdapter mOutFormAdapter;
   private List<String>   mTiteleList;
   private OutFormPagerAdapter mPagerAdapter;

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_outform_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("识别耗材");
	initData();
	initlistener();
   }

   private void initData() {

	LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
	layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
	mOutFormAdapter = new OutFormAdapter(R.layout.item_outform_top_layout, genData3());
	mOutFormRv.setLayoutManager(layoutManager);
	mOutFormRv.setAdapter(mOutFormAdapter);

	mRecyclerviewNull.setVisibility(View.VISIBLE);

	mPagerAdapter = new OutFormPagerAdapter(
		getSupportFragmentManager());
	mCttimecheckViewpager.setAdapter(mPagerAdapter);
	mCttimecheckViewpager.setVisibility(View.GONE);
	mOutFormAdapter.setOnItemClickListener(new OutFormAdapter.OnItemClickListener() {
	   @Override
	   public void OnItemClick(View view, BaseViewHolder helper, int position) {
		mRecyclerviewNull.setVisibility(View.GONE);
		mCttimecheckViewpager.setVisibility(View.VISIBLE);
		mCttimecheckViewpager.setCurrentItem(position);
	   }
	});

   }

   private List<Movie> genData7() {

	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 25; i++) {
	   String one = "微创路入系统";
	   String two = "FLR01" + i;
	   ;
	   String three = "" + i;
	   String four = "王麻子" + i;
	   String five = i + "号柜";
	   ;
	   String seven = "打开柜门";
	   String six = "";
	   if (i == 2) {
		six = "已领取";
	   } else {
		six = "未领取";
	   }
	   Movie movie = new Movie(one, two, three, four, five, six, seven, null);
	   list.add(movie);
	}
	return list;
   }

   /**
    * 上拉下拉刷新
    */
   private void initlistener() {

   }

   private List<Movie> genData3() {

	ArrayList<Movie> list = new ArrayList<>();
	String one;
	String two;
	String three;
	for (int i = 1; i < 20; i++) {
	   one = i + "123121";
	   if (i == 1) {
		two = "张珊";
		three = "xxxxx";
	   } else if (i == 2) {
		two = "张山";
		three = "xxxxx";
	   } else if (i == 3) {
		two = "张三";
		three = "xxxxx";
	   } else if (i == 4) {
		two = "李四";
		three = "xxxxx";
	   } else {
		two = "王麻子";
		three = "xxxxx";
	   }
	   Movie movie = new Movie(one, two, three, null, null, null, null, null);
	   list.add(movie);
	}
	return list;
   }

   private class OutFormPagerAdapter extends FragmentStatePagerAdapter {

	public OutFormPagerAdapter(FragmentManager fm) {
	   super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	   return PublicTimelyFrag.newInstance(7, STYPE_FORM);
	}


	@Override
	public int getCount() {
	   return genData7() == null ? 0 : genData7().size();
	}
   }
}
