package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.OrderCstResultBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;

import static high.rivamed.myapplication.activity.OutMealActivity.mMealbing;
import static high.rivamed.myapplication.activity.OutMealActivity.mTbaseDevicesFromEvent;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.adapter
 * @ClassName: OutMealAdapter
 * @Description: 套组领用-某个套组耗材列表
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/30 13:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/30 13:48
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OutMealTopSuitAdapter extends BaseQuickAdapter<OrderCstResultBean.SuiteVosBean, BaseViewHolder> {

    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_six;
    private TextView mSeven_seven;
    private TextView mSeven_eight;
    public String TAG = "OutMealTopSuitAdapter";
    public int mSize;
    public String mType;
    public String mMealBing;
    private SparseBooleanArray mCheckStates;
    private SparseBooleanArray mCheckStates2 = new SparseBooleanArray();

    public OutMealTopSuitAdapter(int layout, List<OrderCstResultBean.SuiteVosBean> data) {
        super(layout, data);
        this.mData = data;
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
    protected void convert(final BaseViewHolder helper, OrderCstResultBean.SuiteVosBean item) {
//        if (helper.getAdapterPosition() % 2 == 0) {
//            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
//        } else {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
//        }
        findId(helper, mSize);
        mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getCstSpec());
        mSeven_three.setText(""+item.getNeedNum());
        mSeven_four.setVisibility(View.GONE);
        StringBuffer deviceNames = new StringBuffer();
        for (int i = 0; i < item.getDeviceNames().size(); i++) {
            deviceNames.append(item.getDeviceNames().get(i)+",");
        }
        if (item.getDeviceNames().size()>0){
            String deviceName = deviceNames.toString().substring(0,deviceNames.toString().length()-1);
            mSeven_five.setText(deviceName);
        }else {
            mSeven_five.setText("缺货");
        }

        if (!TextUtils.isEmpty(item.getStatus())) {
            mSeven_six.setText(item.getStatus());
        } else {
            mSeven_six.setText("未领用");
        }
        mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
        mSeven_seven.setText("打开柜门");
        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("已领取")) {
                mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
                mSeven_seven.setText("打开柜门");
                mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
                mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
                mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
                mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
                mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            } else {
                mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
                mSeven_seven.setText("打开柜门");
                mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
                mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
                mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
                if (mSeven_five.getText().equals("缺货")){
                    mSeven_five.setTextColor(mContext.getResources().getColor(R.color.color_red));
                }else {
                    mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
                }
                mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            }
        } else {
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.color_green));
            mSeven_seven.setText("打开柜门");
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            if (mSeven_five.getText().equals("缺货")){
                mSeven_five.setTextColor(mContext.getResources().getColor(R.color.color_red));
            }else {
                mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            }
            mSeven_six.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        }
        mSeven_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int position= helper.getAdapterPosition();
                if (item.getStatus() != null) {
                    if (mMealbing != null && mMealbing.equals("BING_MEAL")) {
                        String six = item.getStatus();
                        ToastUtils.showShortToast("six！" + six);
                        if (!six.equals("已领取")) {
                            DialogUtils.showNoDialog(mContext, position + "柜门已开", 2, "form", "BING_MEAL");
                        } else {
                            ToastUtils.showShortToast("此项已领取！");
                        }
                    } else {
                        String six = item.getStatus();
                        if (!six.equals("已领取")) {
                            DialogUtils.showNoDialog(mContext, position + "柜门已开", 2, "form", null);
                        } else {
                            ToastUtils.showShortToast("此项已领取！");
                        }
                    }
                }
                if (item.getDeviceIds() != null && item.getDeviceIds().size() > 0) {
                    mTbaseDevicesFromEvent.clear();
                    for (String deviceCode : item.getDeviceIds()) {
                        BoxSizeBean.DevicesBean oneDoor = new BoxSizeBean.DevicesBean();
                        oneDoor.setDeviceId(deviceCode);
                        if (oneDoor != null && oneDoor.getDeviceId() != null) {
                            mTbaseDevicesFromEvent.add(oneDoor);
                        }
                    }
                    AllDeviceCallBack.getInstance().openDoor("", mTbaseDevicesFromEvent);

                } else {
                    ToastUtils.showShortToast("该耗材无耗材柜信息!");
                }
            }
        });

//        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (mPublicAdapter.getItem(position).getStatus() != null) {
//                    if (mMealbing != null && mMealbing.equals("BING_MEAL")) {
//                        String six = mPublicAdapter.getItem(position).getStatus();
//                        ToastUtils.showShort("six！" + six);
//                        if (!six.equals("已领取")) {
//                            DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", "BING_MEAL");
//                        } else {
//                            ToastUtils.showShort("此项已领取！");
//                        }
//                    } else {
//                        String six = mPublicAdapter.getItem(position).getStatus();
//                        if (!six.equals("已领取")) {
//                            DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", null);
//                        } else {
//                            ToastUtils.showShort("此项已领取！");
//                        }
//                    }
//                }
//                if (mPublicAdapter.getItem(position).getDeviceIds() != null && mPublicAdapter.getItem(position).getDeviceIds().size() > 0) {
//                    mTbaseDevicesFromEvent.clear();
//                    for (String deviceCode : mPublicAdapter.getItem(position).getDeviceIds()) {
//                        BoxSizeBean.DevicesBean oneDoor = new BoxSizeBean.DevicesBean();
//                        oneDoor.setDeviceId(deviceCode);
//                        if (oneDoor != null && oneDoor.getDeviceId() != null) {
//                            mTbaseDevicesFromEvent.add(oneDoor);
//                        }
//                    }
//                    AllDeviceCallBack.getInstance().openDoor(0, mTbaseDevicesFromEvent);
//
//                } else {
//                    ToastUtils.showShort("该耗材无耗材柜信息!");
//                }
//            }
//        });

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
//            if (helper.getAdapterPosition() % 2 == 0) {
//                textview.setBackgroundResource(R.color.bg_color);
//            } else {
                textview.setBackgroundResource(R.color.bg_f);
//            }
            textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
        }

    }

    private void findId(BaseViewHolder helper, int size) {
        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));
        mSeven_six = ((TextView) helper.getView(R.id.seven_six));


    }

    public void setDeleteView(boolean isDeleteView, SwipeLayout swipe) {
        if (isDeleteView) {
            mSeven_one.setTextColor(Color.parseColor("#999999"));
            mSeven_two.setTextColor(Color.parseColor("#999999"));
            mSeven_three.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setTextColor(Color.parseColor("#999999"));
            mSeven_five.setTextColor(Color.parseColor("#999999"));
            mSeven_six.setTextColor(Color.parseColor("#999999"));
            swipe.setSwipeEnabled(false);
        } else {
            mSeven_one.setTextColor(Color.parseColor("#333333"));
            mSeven_two.setTextColor(Color.parseColor("#333333"));
            mSeven_three.setTextColor(Color.parseColor("#333333"));
            mSeven_four.setTextColor(Color.parseColor("#333333"));
            mSeven_five.setTextColor(Color.parseColor("#333333"));
            mSeven_six.setTextColor(Color.parseColor("#333333"));
            swipe.setSwipeEnabled(true);
        }
    }
}

