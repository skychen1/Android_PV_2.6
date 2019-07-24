package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import high.rivamed.myapplication.R;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:20
 * 描述:        套餐选择的PopupWindow
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OnlyCodePopupWindow extends PopupWindow {

    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];

    private final TextView mTextView;
    private final View mView;
    Context mContext;
    public OnlyCodePopupWindow(Context context, String text) {
        mView = LayoutInflater.from(context).inflate(R.layout.onlycode_popupwindow, null);
        mTextView = (TextView) mView.findViewById(R.id.onlycode);
        mTextView.setText(text);
        this.mContext =context;
    }


    public void showPopupWindow(View parent,int width) {
        parent.getLocationOnScreen(mLocation);

        this.setContentView(mView);
//        this.setWidth(parent.getWidth());
        this.setWidth(width-(mLocation[0]+mContext.getResources().getDimensionPixelSize(R.dimen.x40)));

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
            showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0], mLocation[1]);

        } else {
            dismiss();
        }
    }
}
