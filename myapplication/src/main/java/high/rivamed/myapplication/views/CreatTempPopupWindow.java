package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.CreatTempAdapter;
import high.rivamed.myapplication.bean.SelectBean;
import high.rivamed.myapplication.utils.UIUtils;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 创建临时患者下拉选择弹窗
 */
public class CreatTempPopupWindow extends PopupWindow {

    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];

    private final RecyclerView mRecyclerView;
    private final View mView;

    private String TAG = "SettingPopupWindow";
    public CreatTempAdapter mAdar;


    public CreatTempPopupWindow(Context context, List<SelectBean>  list) {
        mView = LayoutInflater.from(context).inflate(R.layout.mac_popupwindow, null);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.search_rv);
        mAdar = new CreatTempAdapter(R.layout.item_mac_single, list);
        mRecyclerView.setAdapter(mAdar);
        heightMeth(list.size());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));

    }

    private void heightMeth(int size) {
        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
        if (size > 6) {
            lp.height = UIUtils.getContext().getResources().getDimensionPixelOffset(R.dimen.y61) * 6;
        } else {
            lp.height = UIUtils.getContext().getResources().getDimensionPixelOffset(R.dimen.y61) * size;
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

            PopupWindowCompat.showAsDropDown(this,parent,0,-8, Gravity.START);
            //	   	   showAtLocation(parent, Gravity.NO_GRAVITY, 190,
            //					485);
        } else {
            dismiss();
        }
    }

}
