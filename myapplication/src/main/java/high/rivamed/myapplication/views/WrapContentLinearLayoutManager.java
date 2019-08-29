package high.rivamed.myapplication.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 项目名称:    Android_PV_2.6.8.0
 * 创建者:      DanMing
 * 创建时间:    2019/8/28 0028 13:54
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class WrapContentLinearLayoutManager extends LinearLayoutManager {
   public WrapContentLinearLayoutManager(Context context) {
	super(context);
   }

   public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
	super(context, orientation, reverseLayout);
   }

   public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	super(context, attrs, defStyleAttr, defStyleRes);
   }

   @Override
   public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
	try {
	   super.onLayoutChildren(recycler, state);
	} catch (IndexOutOfBoundsException e) {
	   e.printStackTrace();
	}
   }

}
