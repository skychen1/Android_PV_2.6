package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Movie;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/21 14:59
 * 描述:        公用fragment
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PublicConRunWateFrag extends SimpleFragment {

   private static final String ARG_PARAM = "param";
   private static final int    FIVE      = 5;
   private static final int    SIX       = 6;
   private static final int    SEVEN     = 7;
   private static final int    EIGHT     = 8;

   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   private int                 mParam;
   private TimelyPublicAdapter mPublicAdapter;
   private int                 mSize; //假数据 举例6个横向格子
   private View                mHeadView;
   private int                 mLayout;

   public static PublicConRunWateFrag newInstance(int param) {
	Bundle args = new Bundle();
	PublicConRunWateFrag fragment = new PublicConRunWateFrag();
	args.putInt(ARG_PARAM, param);
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   public int getLayoutId() {
	return R.layout.public_runwate_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	initData();
	//	final List<Movie> movies = new Gson().fromJson(JSON_MOVIES,
	//								     new TypeToken<ArrayList<Movie>>() {}.getType());
	//	mPublicAdapter.replaceData(movies);
	initlistener();
   }

   /**
    * 数据加载
    */
   private void initData() {

	String[] array = mContext.getResources().getStringArray(R.array.eight_runwate_arrays);
	List<String> titeleList = Arrays.asList(array);
	mSize = array.length;

	mLayout = R.layout.item_runwate_eight_layout;
	mHeadView = getLayoutInflater().inflate(R.layout.item_runwate_eight_title_layout,
							    (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
	((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));

	mHeadView.setBackgroundResource(R.color.bg_green);
	mPublicAdapter = new TimelyPublicAdapter(mLayout, genData8(), mSize);
	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRefreshLayout.setEnableAutoLoadMore(true);
	mRecyclerview.setAdapter(mPublicAdapter);
	mLinearLayout.addView(mHeadView);
   }

   private void initlistener() {
	mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		mRefreshLayout.setNoMoreData(false);
		refreshLayout.finishRefresh();
	   }
	});
	mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	   @Override
	   public void onLoadMore(RefreshLayout refreshLayout) {
		refreshLayout.finishLoadMoreWithNoMoreData();
	   }
	});
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   private List<Movie> genData8() {
	String one;
	String five = null;
	ArrayList<Movie> list = new ArrayList<>();
	for (int i = 0; i < 20; i++) {
	   if (i==0){
		one = "退货";
		five = "已过期";
	   }else if (i==1){
		one = "移入";
		five = "≤100天";
	   }else if (i==2){
		one = "返回";
		five = "≤70天";
	   }else if (i==3){
		one = "领用";
		five = "≤28天";
	   }else if (i==4){
		one = "移出";
	   }else {
		one = "入库";
		five = "2019-10-22";
	   }
	   String two = "微创录入系统";
	   String three = "*15151223333ddd3" + i;
	   String four = "RFID01" + i;
	   String six = "1" + i;
	   String seven = "2019-10-22\n16:2"+i;
	   String eight = "张小" + i;

	   Movie movie = new Movie(one, two, three, four, five, six, seven, eight);
	   list.add(movie);
	}
	return list;
   }
}
