package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.OutMealTopSuitAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.OrderCstResultBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.OutMealSuitBeanResult;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.MealPopupWindow;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static android.widget.LinearLayout.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 11:04
 * 描述:        取出套餐
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutMealActivity extends BaseSimpleActivity {
    private final String TAG = "OutMealActivity";
    @BindView(R.id.meal_tv_search)
    TextView mMealTvSearch;
    @BindView(R.id.meal_open_btn)
    TextView mMealOpenBtn;

    @BindView(R.id.recyclerview_null)
    RelativeLayout mRecyclerviewNull;
    @BindView(R.id.timely_ll)
    LinearLayout mLinearLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.public_ll)
    LinearLayout mPublicLl;
    List<OrderCstResultBean.CstPlanVosBean> movies = new ArrayList<>();
    private MealPopupWindow mPopupWindowSearch;
    private OutMealTopSuitAdapter mPublicAdapter;
    private View mHeadView;
    private int mLayout;
    private int mSize;
    private String mMealbing;
    private List<String> titeleList = null;

    /**
     * 套餐列表
     */
    private List<OutMealSuitBeanResult> mOutMealSuitList;
    /**
     * 一个套餐所包含的耗材列表
     */
    private OrderCstResultBean mOrderCstResult;

    /**
     * 开门柜子列表
     */
    private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevicesFromEvent = new ArrayList<>();

    /**
     * 关柜子是否跳转界面，防止界面stop时重发跳转新界面；
     */
    private boolean mIsCanSkipToSurePage = true;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onActString(Event.EventAct event) {
        mMealbing = event.mString;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPopupBean(Event.EventOutMealSuit event) {
        if (event.isMute) {
            mMealTvSearch.setText(event.mOutMealSuitBeanResult.getPlanName());
            mPopupWindowSearch.dismiss();
            mRecyclerviewNull.setVisibility(View.GONE);
            if (mMealbing != null && mMealbing.equals("BING_MEAL")) {//判断是否是绑定患者的套餐
                String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
                titeleList = Arrays.asList(array);
                mSize = array.length;
            } else {
                String[] array = mContext.getResources().getStringArray(R.array.six_meal_arrays);
                titeleList = Arrays.asList(array);
                mSize = array.length;
            }
            findOrderCstListDate("" + event.mOutMealSuitBeanResult.getId(), SPUtils.getString(mContext, THING_CODE));
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_meal_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("套餐领用");
        initlistener();
        findOrderCstPlanDate(true);
    }

    private void initData(List<OrderCstResultBean.CstPlanVosBean> movies) {

        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mLayout = R.layout.item_form_seven_layout;
        mHeadView = getLayoutInflater().inflate(R.layout.item_form_seven_title_layout,
                (ViewGroup) mLinearLayout.getParent(), false);
        if (mMealbing != null && mMealbing.equals("BING_MEAL")) {

            ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
            ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
            ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
            ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
            ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
            ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));

        } else {
            ((TextView) mHeadView.findViewById(R.id.seven_four)).setVisibility(View.GONE);
            ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
            ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
            ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
            ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(3));
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(4));
            ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(5));

        }
        if (movies != null && movies.size() > 0) {
            mRecyclerviewNull.setVisibility(View.GONE);
        } else {
            mRecyclerviewNull.setVisibility(View.VISIBLE);
        }
        mPublicAdapter = new OutMealTopSuitAdapter(mLayout, movies);
        mLinearLayout.addView(mHeadView);
        mRecyclerview.setAdapter(mPublicAdapter);
        mPublicAdapter.notifyDataSetChanged();
        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPublicAdapter.getItem(position).getStatus() != null) {
                    if (mMealbing != null && mMealbing.equals("BING_MEAL")) {
                        String six = mPublicAdapter.getItem(position).getStatus();
                        ToastUtils.showShort("six！" + six);
                        if (!six.equals("已领取")) {
                            DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", "BING_MEAL");
                        } else {
                            ToastUtils.showShort("此项已领取！");
                        }
                    } else {
                        String six = mPublicAdapter.getItem(position).getStatus();
                        if (!six.equals("已领取")) {
                            DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", null);
                        } else {
                            ToastUtils.showShort("此项已领取！");
                        }
                    }
                }
                if (mPublicAdapter.getItem(position).getDeviceCodes() != null && mPublicAdapter.getItem(position).getDeviceCodes().size() > 0) {
                    mTbaseDevicesFromEvent.clear();
                    for (String deviceCode : mPublicAdapter.getItem(position).getDeviceCodes()) {
                        BoxSizeBean.TbaseDevicesBean oneDoor = new BoxSizeBean.TbaseDevicesBean();
                        oneDoor.setDeviceCode(deviceCode);
                        mTbaseDevicesFromEvent.add(oneDoor);
                    }
                    AllDeviceCallBack.getInstance().openDoor(0, mTbaseDevicesFromEvent);

                } else {
                    ToastUtils.showShort("该耗材无耗材柜信息!");
                }
            }
        });

    }

    private void initlistener() {
        mRecyclerviewNull.setVisibility(View.GONE);
        String[] array = mContext.getResources().getStringArray(R.array.six_meal_arrays);
        titeleList = Arrays.asList(array);
        mSize = array.length;
    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.meal_tv_search, R.id.meal_open_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_tab_icon_right:
            case R.id.base_tab_tv_name:
                mPopupWindow = new SettingPopupWindow(mContext);
                mPopupWindow.showPopupWindow(view);
                mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
                                break;
                            case 1:
                                mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
                                break;

                        }
                    }
                });
                break;
            case R.id.base_tab_tv_outlogin:
                TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
                builder.setTwoMsg("您确认要退出登录吗?");
                builder.setMsg("温馨提示");
                builder.setLeft("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.setRight("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        App.getInstance().removeALLActivity_();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.base_tab_btn_msg:
                break;
            case R.id.base_tab_back:
                finish();
                break;
            case R.id.meal_tv_search:
                if (mOutMealSuitList != null) {
                    if (mOutMealSuitList.size() > 0) {
                        mPopupWindowSearch = new MealPopupWindow(this, mOutMealSuitList);
                        mPopupWindowSearch.showPopupWindow(mMealTvSearch);
                    } else {
                        ToastUtils.showShort("该部门暂未套组");
                    }
                } else {
                    findOrderCstPlanDate(false);
                }
                break;
            case R.id.meal_open_btn:
                if (mPublicAdapter.getData() != null && mPublicAdapter.getData().size() > 0) {
                    ToastUtils.showShort("全部开柜");
                    mTbaseDevicesFromEvent.clear();
                    for (int i = 0; i < mPublicAdapter.getData().size(); i++) {
                        for (String deviceCode : mPublicAdapter.getItem(i).getDeviceCodes()) {
                            BoxSizeBean.TbaseDevicesBean oneDoor = new BoxSizeBean.TbaseDevicesBean();
                            oneDoor.setDeviceCode(deviceCode);
                            mTbaseDevicesFromEvent.add(oneDoor);
                        }
                    }
                    if (mTbaseDevicesFromEvent.size() > 0) {
                        AllDeviceCallBack.getInstance().openDoor(0, mTbaseDevicesFromEvent);
                    } else {
                        ToastUtils.showShort("无耗材柜数据");
                    }
                } else {
                    ToastUtils.showShort("无耗材数据");
                }
                break;
        }
    }

    /**
     * 获取套餐列表
     */
    private void findOrderCstPlanDate(boolean isDefulat) {
        NetRequest.getInstance().findOrderCstPlanDate(SPUtils.getString(mContext, SAVE_DEPT_CODE), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                mOutMealSuitList = mGson.fromJson(result, new TypeToken<List<OutMealSuitBeanResult>>() {
                }.getType());
                if (!isDefulat) {
                    if (mOutMealSuitList.size() > 0) {
                        mPopupWindowSearch = new MealPopupWindow(OutMealActivity.this, mOutMealSuitList);
                        mPopupWindowSearch.showPopupWindow(mMealTvSearch);
                    } else {
                        ToastUtils.showShort("该部门暂未套组");
                    }
                } else {
                    if (mOutMealSuitList.size() > 0) {
                        mMealTvSearch.setText(mOutMealSuitList.get(0).getPlanName());
                        //默认数据
                        findOrderCstListDate("" + mOutMealSuitList.get(0).getId(), SPUtils.getString(mContext, THING_CODE));
                    } else {
                        mRecyclerviewNull.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }

    /**
     * 获取套餐包含耗材
     */
    private void findOrderCstListDate(String cstPlanId, String thingCode) {
        NetRequest.getInstance().findOrderCstListById(cstPlanId, thingCode, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                mOrderCstResult = mGson.fromJson(result, OrderCstResultBean.class);
                if (mPublicAdapter == null) {
                    movies.addAll(mOrderCstResult.getCstPlanVos());
                    initData(movies);
                } else {
                    movies.clear();
                    movies.addAll(mOrderCstResult.getCstPlanVos());
                    mPublicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isDoorOpened(Event.HomeNoClickEvent event) {
        if (event.isClick) {
            DialogUtils.showNoDialog(mContext, "柜门已开", 2, "form", null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isDoorClosed(Event.HomeNoClickEvent event) {
        if (mIsCanSkipToSurePage) {
            if (!event.isClick) {
                // 统一数据格式
                OrderSheetBean.RowsBean orderSheetBean = new OrderSheetBean.RowsBean();
                orderSheetBean.setId("" + mOrderCstResult.getCstPlan().getId());
                List<BillStockResultBean.TransReceiveOrderDetailVosBean> transReceiveOrderDetailVosList = new ArrayList<>();
                for (OrderCstResultBean.CstPlanVosBean item : mOrderCstResult.getCstPlanVos()) {
                    BillStockResultBean.TransReceiveOrderDetailVosBean info = new BillStockResultBean.TransReceiveOrderDetailVosBean();
                    info.setOrderDetailId(item.getId());
                    info.setIsHaveNum(item.getTotalCount());
                    info.setCounts(item.getPlanNum());
                    info.setCstId(item.getCstId());
                    info.setCstName(item.getCstName());
                    info.setCstSpec(item.getCstSpec());
                    info.setReceivedStatus(item.getStatus());
                    info.setReceiveNum(item.getTotalCount());
                    info.setNeedNum(item.getTotalCount());
                    info.setPatientName("");
//                    StringBuffer sb = new StringBuffer();
//                    for (int i = 0; i < item.getDeviceNames().size(); i++) {
//                        sb.append(item.getDeviceNames().get(i));
//                    }
//                    info.setThingName(sb.toString());
//                    transReceiveOrderDetailVosList.add(info);
                }
                EventBusUtils.postSticky(new Event.EventBillOrder(orderSheetBean, transReceiveOrderDetailVosList, mTbaseDevicesFromEvent));
                Intent intent = new Intent(mContext, NewOutMealBingConfirmActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsCanSkipToSurePage = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsCanSkipToSurePage = false;
    }
}
