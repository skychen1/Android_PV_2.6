package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.MealPopAdapter;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.OutMealBean;
import high.rivamed.myapplication.utils.EventBusUtils;

import static android.widget.LinearLayout.VERTICAL;

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
public class MealPopupWindow extends PopupWindow {

    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];

    private final EditText mEditText;
    private final RecyclerView mRecyclerView;
    private final View mView;

    private String TAG = "SettingPopupWindow";
    private OnClickListener mItemClickListener;
    private MealPopAdapter mMealPopAdapter;
    private final List<OutMealBean.SuitesBean> mMovies;
    private final List<OutMealBean.SuitesBean> mMovies1;

    public MealPopupWindow(Context context, List<OutMealBean.SuitesBean> movies) {
        mView = LayoutInflater.from(context).inflate(R.layout.meal_popupwindow, null);
        mEditText = (EditText) mView.findViewById(R.id.search_et);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.search_rv);
        this.mMovies = movies;
        mMovies1 = new ArrayList<>();
        mMovies1.addAll(mMovies);
        mMealPopAdapter = new MealPopAdapter(R.layout.item_meal_single, mMovies1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));
        mRecyclerView.setAdapter(mMealPopAdapter);

        mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBusUtils.postSticky(new Event.EventOutMealSuit(true,mMovies1.get(position)));
            }
        });

        heightMeth();

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String trim = mEditText.getText().toString().trim();
                mMovies1.clear();

                for (int i = 0; i < mMovies.size() - 1; i++) {
                    String string = mMovies.get(i).getSuiteName();
                    if (string.contains(trim)) {
                        mMovies1.add(mMovies.get(i));
                    }
                }
                if (trim.equals("")) {
                    mMovies1.clear();
                    mMovies1.addAll(mMovies);
                }

                heightMeth();
                mMealPopAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void heightMeth() {
        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
        if (mMovies1.size() > 8) {
            lp.height = 400;
        } else {
            lp.height = 60 * mMovies1.size();
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

            showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0],
                    mLocation[1] + parent.getHeight());
            //	   showAtLocation(parent, Gravity.NO_GRAVITY, 100,
            //				240);
        } else {
            mMovies1.clear();
            dismiss();
        }
    }
}
