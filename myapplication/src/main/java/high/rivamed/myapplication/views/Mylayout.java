package high.rivamed.myapplication.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/26 18:19
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class Mylayout extends LinearLayoutManager {

/*    private static final int CHILD_WIDTH = 0;
    private static final int CHILD_HEIGHT = 1;
    private static final int DEFAULT_CHILD_SIZE = 100;

    private final int[] childDimensions = new int[2];

    private int childSize = DEFAULT_CHILD_SIZE;
    private boolean hasChildSize;*/

   @SuppressWarnings("UnusedDeclaration")
   public Mylayout(Context context) {
	super(context);
   }

   @SuppressWarnings("UnusedDeclaration")
   public Mylayout(Context context, int orientation, boolean reverseLayout) {
	super(context, orientation, reverseLayout);
   }

   private int[] mMeasuredDimension = new int[2];

   @Override
   public void onMeasure(
	   RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
	final int widthMode = View.MeasureSpec.getMode(widthSpec);
	final int heightMode = View.MeasureSpec.getMode(heightSpec);
	final int widthSize = View.MeasureSpec.getSize(widthSpec);
	final int heightSize = View.MeasureSpec.getSize(heightSpec);
	int width = 0;
	int height = 0;

	for (int i = 0; i < 7; i++) {

	   try {
		measureScrapChild(recycler, i, widthSpec,
					View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
					mMeasuredDimension);
	   } catch (IndexOutOfBoundsException e) {

		e.printStackTrace();
	   }

	   if (getOrientation() == HORIZONTAL) {
		width = width + mMeasuredDimension[0];
		if (i == 0) {
		   height = mMeasuredDimension[1];
		}
	   } else {
		height = height + mMeasuredDimension[1];
		if (i == 0) {
		   width = mMeasuredDimension[0];
		}
	   }
	}

	switch (widthMode) {
	   case View.MeasureSpec.EXACTLY:
	   case View.MeasureSpec.AT_MOST:
	   case View.MeasureSpec.UNSPECIFIED:
	}

	switch (heightMode) {
	   case View.MeasureSpec.EXACTLY:
		height = heightSize;
	   case View.MeasureSpec.AT_MOST:
	   case View.MeasureSpec.UNSPECIFIED:
	}
	setMeasuredDimension(widthSpec, height);

   }

   private void measureScrapChild(
	   RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
	View view = recycler.getViewForPosition(position);

	if (view != null) {
	   RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
	   int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
										 getPaddingTop() + getPaddingBottom(), p.height);
	   view.measure(widthSpec, childHeightSpec);
	   measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
	   measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
	   recycler.recycleView(view);
	}
   }

   @Override
   public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
	super.onLayoutChildren(recycler, state);
   }
}
