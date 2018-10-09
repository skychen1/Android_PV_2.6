package high.rivamed.myapplication.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/9 14:18
 * 描述:       可设置禁止滑动和可以滑动
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class CustomViewPager extends ViewPager {
   private boolean isCanScroll = true;

   public CustomViewPager(Context context) {
	super(context);
   }

   public CustomViewPager(Context context, AttributeSet attrs) {
	super(context, attrs);
   }

   /**
    * 设置其是否能滑动换页
    * @param isCanScroll false 不能换页， true 可以滑动换页
    */
   public void setScanScroll(boolean isCanScroll) {
	this.isCanScroll = isCanScroll;
   }

   @Override
   public boolean onInterceptTouchEvent(MotionEvent ev) {
	return isCanScroll && super.onInterceptTouchEvent(ev);
   }

   @Override
   public boolean onTouchEvent(MotionEvent ev) {
	return isCanScroll && super.onTouchEvent(ev);

   }
}