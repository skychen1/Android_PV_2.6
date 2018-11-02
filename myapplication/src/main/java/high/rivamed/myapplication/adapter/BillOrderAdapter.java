package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.Movie;

import static high.rivamed.myapplication.cont.Constants.STYPE_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_IN;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_NOBING;
import static high.rivamed.myapplication.cont.Constants.STYPE_OUT;
import static high.rivamed.myapplication.cont.Constants.STYPE_TIMELY_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.TYPE_TIMELY;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:08
 * 描述:         套组领用--根据EPC查回的耗材
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class BillOrderAdapter extends BaseQuickAdapter<BillOrderResultBean, BaseViewHolder> {
    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;
    public String TAG = "BillOrderAdapter";
    public int mSize;
    public String mType;
    private SparseBooleanArray mCheckStates;
    private SparseBooleanArray mCheckStates2 = new SparseBooleanArray();
    public CheckBox mMCheckBox;


    public BillOrderAdapter(
            int layout, int type, List<BillOrderResultBean> data) {
        super(layout, data);
        this.mType = "" + type;
    }

    public void clear() {
        if (mCheckStates != null) {
            mCheckStates.clear();
        }
        if (mCheckStates2 != null) {
            mCheckStates2.clear();
        }
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, BillOrderResultBean item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_color);
        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
        }
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        if ("7".equals(mType)) {
            mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
            mSeven_seven.setText(item.getPatientName());
        } else {
            mSeven_seven = null;
        }
        ImageView view = (ImageView) helper.getView(R.id.seven_six);
        if (item.getRemark() != null) {
            if (item.getRemark().equals("1")) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        } else {
            view.setVisibility(View.VISIBLE);
        }
        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getEpc());
        mSeven_three.setText(item.getCstSpec());
        mSeven_four.setText(item.getExpirationTime());
        mSeven_five.setText(item.getDeviceName());
        initTermOfValidity(helper, item.getExpirationTime(), mSeven_four);
    }

    /**
     * 设置某个效期的背景
     *
     * @param helper
     * @param text
     * @param textview
     */
    private void initTermOfValidity(BaseViewHolder helper, String text, TextView textview) {

        if (text.equals("已过期")) {
            textview.setBackgroundResource(R.drawable.bg_text_red);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else if (text.equals("≤100天")) {
            textview.setBackgroundResource(R.drawable.bg_text_yellow1);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else if (text.equals("≤70天")) {
            textview.setBackgroundResource(R.drawable.bg_text_yellow2);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else if (text.equals("≤28天")) {
            textview.setBackgroundResource(R.drawable.bg_text_orange);
            textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
        } else {
            if (helper.getAdapterPosition() % 2 == 0) {
                textview.setBackgroundResource(R.color.bg_color);
            } else {
                textview.setBackgroundResource(R.color.bg_f);
            }
            textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        }

    }
}
