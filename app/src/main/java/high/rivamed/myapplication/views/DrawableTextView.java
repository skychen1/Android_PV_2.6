package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:20
 * 描述:        个人设置中的PopupWindow的自定义Textview
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class DrawableTextView extends AppCompatTextView {
   public DrawableTextView(Context context, AttributeSet attrs,
				   int defStyle) {
	super(context, attrs, defStyle);
   }

   public DrawableTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
   }

   public DrawableTextView(Context context) {
	super(context);
   }

   @Override
   protected void onDraw(Canvas canvas) {
	Drawable[] drawables = getCompoundDrawables();
	if (drawables != null) {
	   Drawable drawableLeft = drawables[0];
	   if (drawableLeft != null) {
		float textWidth = getPaint().measureText(getText().toString());
		int drawablePadding = getCompoundDrawablePadding();
		int drawableWidth = 0;
		drawableWidth = drawableLeft.getIntrinsicWidth();
		float bodyWidth = textWidth + drawableWidth + drawablePadding;
		canvas.translate((getWidth() - bodyWidth) / 2, 0);
	   }
	}
	super.onDraw(canvas);
   }
}
