package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.MealPopAdapter;
import high.rivamed.myapplication.bean.Movie;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:20
 * 描述:        医院选择的PopupWindow
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class hospitalPopupWindow extends PopupWindow {

   // 坐标的位置（x、y）
   private final int[] mLocation = new int[2];

   private final RecyclerView mRecyclerView;
   private final View         mView;

   private String TAG = "SettingPopupWindow";
   private       OnClickListener mItemClickListener;
   public        MealPopAdapter  mMealPopAdapter;
   private final List<Movie>     mMovies;
//   private final List<Movie>     mMovies1;

   public hospitalPopupWindow(Context context,List<Movie> movie) {
	mView = LayoutInflater.from(context).inflate(R.layout.mac_popupwindow, null);
	mRecyclerView = (RecyclerView) mView.findViewById(R.id.search_rv);
	mMovies = movie;
//	mMovies1 = new ArrayList<>();
//	mMovies1.addAll(mMovies);
	mMealPopAdapter = new MealPopAdapter(R.layout.item_mac_single, mMovies);
	mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
	mRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));
	mRecyclerView.setAdapter(mMealPopAdapter);

//	mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//	   @Override
//	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//		TextView textView = (TextView) view.findViewById(R.id.item_meal);
//		String trim = textView.getText().toString().trim();
//		//		mEditText.setText(trim);
//		EventBusUtils.postSticky(new Event.PopupEvent(true, trim));
//	   }
//	});

	heightMeth();

   }

   private void heightMeth() {
	ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
	if (mMovies.size() > 8) {
	   lp.height = 370;
	} else {
	   lp.height = 60 * mMovies.size();
	}
	mRecyclerView.setLayoutParams(lp);
   }

   public void showPopupWindow(View parent) {
	parent.getLocationOnScreen(mLocation);

	this.setContentView(mView);
	this.setWidth(parent.getWidth());
	this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
	this.setFocusable(true);
	this.setOutsideTouchable(true);
	this.update();
	// 实例化一个ColorDrawable颜色为半透明
	ColorDrawable dw = new ColorDrawable(0000000000);
	// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
	this.setBackgroundDrawable(dw);
	this.setAnimationStyle(R.style.social_pop_anim);

	if (!this.isShowing()) {

	   showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0]-5,
				mLocation[1] +20);
//	   	   showAtLocation(parent, Gravity.NO_GRAVITY, 190,
//					485);
	} else {
	   dismiss();
	}
   }


}
