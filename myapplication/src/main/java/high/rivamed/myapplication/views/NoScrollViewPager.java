package high.rivamed.myapplication.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/29 20:05
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NoScrollViewPager extends ViewPager {

   public NoScrollViewPager(Context context, AttributeSet attrs) {
	super(context, attrs);
   }

   public NoScrollViewPager(Context context) {
	super(context);
   }

   /**
    * 1.是否派发
    * */
   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
	return super.dispatchTouchEvent(ev);
   }

   /**
    * 2.是否拦截
    */
   @Override
   public boolean onInterceptTouchEvent(MotionEvent ev) {
	return false;//不拦截--没有任何风险
   }

   /**
    * 3.是否消费
    * true:事件被处理-->结束
    */
   @Override
   public boolean onTouchEvent(MotionEvent ev) {
	return true;
   }
}
