package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 9:22
 * 描述:        自定义密码输入
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PwdTextView extends AppCompatTextView {


    private float radius;

    private boolean hasPwd;

    public PwdTextView(Context context) {
        this(context, null);
    }

    public PwdTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hasPwd) {
            // 画一个黑色的圆
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        }
    }


    public void clearPwd() {
        this.hasPwd = false;
        invalidate();
    }


    public void drawPwd(float radius) {
        this.hasPwd = true;
        if (radius == 0) {
            this.radius = getWidth() / 4;
        } else {
            this.radius = radius;
        }
        invalidate();
    }

}
