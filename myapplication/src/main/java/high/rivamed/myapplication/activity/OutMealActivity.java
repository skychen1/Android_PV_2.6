package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.OrderCstResultBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.OutMealBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.MealPopupWindow;
import high.rivamed.myapplication.views.OpenDoorDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static android.widget.LinearLayout.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack3;
import static high.rivamed.myapplication.utils.UIUtils.removeAllAct;

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
    List<OrderCstResultBean.SuiteVosBean> movies = new ArrayList<>();
    private MealPopupWindow        mPopupWindowSearch;
    private OutMealTopSuitAdapter  mPublicAdapter;
    private View                   mHeadView;
    private int                    mLayout;
    private int                    mSize;
    public static String           mMealbing;
    private List<String>           titeleList = null;
    private OpenDoorDialog.Builder mBuilder;
    /**
     * 套餐列表
     */
    /**
     * 一个套餐所包含的耗材列表
     */
    private OrderCstResultBean     mOrderCstResult;

    /**
     * 开门柜子列表
     */
    public static List<BoxSizeBean.DevicesBean> mTbaseDevicesFromEvent = new ArrayList<>();

    /**
     * 关柜子是否跳转界面，防止界面stop时重发跳转新界面；
     */
    private boolean mIsCanSkipToSurePage = true;
   private List<OutMealBean.SuitesBean> mSuites;

   /**
     * 判断套餐是否已经领取
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCstDate(Event.EventMealType event) {
        for (InventoryVo s:event.inventoryVos){
	     String cstId = s.getCstId();
	     for(OrderCstResultBean.SuiteVosBean k:movies){
	        if (k.getCstId().equals(cstId)){
		     k.setStatus("已领取");
		  }
            }
        }
        mPublicAdapter.notifyDataSetChanged();

        EventBusUtils.removeStickyEvent(getClass());
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onActString(Event.EventAct event) {
        mMealbing = event.mString;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPopupBean(Event.EventOutMealSuit event) {
        if (event.isMute) {
            mMealTvSearch.setText(event.mOutMealSuitBeanResult.getSuiteName());
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
            findOrderCstListDate("" + event.mOutMealSuitBeanResult.getSuiteId(), SPUtils.getString(mContext, THING_CODE));
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_meal_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        EventBusUtils.register(this);
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("套组领用");
        mBaseTabTvName.setText(SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
        if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
                SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
            Glide.with(this)
                    .load(R.mipmap.hccz_mrtx_nan)
                    .error(R.mipmap.hccz_mrtx_nan)
                    .into(mBaseTabIconRight);
        } else {
            Glide.with(this)
                    .load(R.mipmap.hccz_mrtx_nv)
                    .error(R.mipmap.hccz_mrtx_nv)
                    .into(mBaseTabIconRight);
        }
        initlistener();
        findOrderCstPlanDate(true);
    }

    private void initData(List<OrderCstResultBean.SuiteVosBean> movies) {

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

    }

    @Override
    public void onStart() {
        super.onStart();
        if(mEthDeviceIdBack!=null){
            mEthDeviceIdBack3.clear();
            mEthDeviceIdBack.clear();
        }

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
                        removeAllAct(mContext);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.base_tab_btn_msg:
                break;
            case R.id.base_tab_back:
                mEthDeviceIdBack3.clear();
                mEthDeviceIdBack.clear();
                finish();

                break;
            case R.id.meal_tv_search:
                if (mSuites != null) {
                    if (mSuites.size() > 0) {
                        mPopupWindowSearch = new MealPopupWindow(this, mSuites);
                        mPopupWindowSearch.showPopupWindow(mMealTvSearch);
                    } else {
                        ToastUtils.showShortToast("该科室暂无套组");
                    }
                } else {
                    findOrderCstPlanDate(false);
                }
                break;
            case R.id.meal_open_btn:
                if (mPublicAdapter!=null && mPublicAdapter.getData() != null && mPublicAdapter.getData().size() > 0) {
                    ToastUtils.showShortToast("全部开柜");
                    mTbaseDevicesFromEvent.clear();
                    for (int i = 0; i < mPublicAdapter.getData().size(); i++) {
                        for (String deviceCode : mPublicAdapter.getItem(i).getDeviceIds()) {
                            BoxSizeBean.DevicesBean oneDoor = new BoxSizeBean.DevicesBean();
                            oneDoor.setDeviceId(deviceCode);
                            if (oneDoor != null && oneDoor.getDeviceId() != null) {
                                mTbaseDevicesFromEvent.add(oneDoor);
                            }
                        }
                    }
                    if (mTbaseDevicesFromEvent.size() > 0) {
                        AllDeviceCallBack.getInstance().openDoor("", mTbaseDevicesFromEvent);
                    } else {
                        ToastUtils.showShortToast("无耗材柜数据");
                    }
                } else {
                    ToastUtils.showShortToast("无套组数据，请选择套组");
                }
                break;
        }
    }

    /**
     * 获取套餐列表
     */
    private void findOrderCstPlanDate(boolean isDefulat) {
        NetRequest.getInstance().findOrderCstPlanDate( this,  new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG,"result   "+result);
		   OutMealBean outMealBean = mGson.fromJson(result, OutMealBean.class);
		   mSuites = outMealBean.getSuiteVos();
		   if (!isDefulat) {
                    if (mSuites.size()>0) {
                        mPopupWindowSearch = new MealPopupWindow(OutMealActivity.this, mSuites);
                        mPopupWindowSearch.showPopupWindow(mMealTvSearch);
                    } else {
                        ToastUtils.showShortToast("该科室暂无套组");
                    }
                } else {
                    if (mSuites.size() > 0) {
                        mMealTvSearch.setText(mSuites.get(0).getSuiteName());
                        //默认数据
                        findOrderCstListDate("" +mSuites.get(0).getSuiteId(), SPUtils.getString(mContext, THING_CODE));
                    } else {
                        mRecyclerviewNull.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String result) {
            }
        });
    }

    /**
     * 获取套餐包含耗材
     */
    private void findOrderCstListDate(String cstPlanId, String thingCode) {
        NetRequest.getInstance().findOrderCstListById(cstPlanId, thingCode, this,  new BaseResult() {
            @Override
            public void onSucceed(String result) {
               LogUtils.i(TAG,"result    "+result);
                mOrderCstResult = mGson.fromJson(result, OrderCstResultBean.class);
//                if (mOrderCstResult.isOperateSuccess()){
			 if (mPublicAdapter == null) {
			    movies.addAll(mOrderCstResult.getSuiteVos());
			    initData(movies);
			 } else {
			    movies.clear();
			    movies.addAll(mOrderCstResult.getSuiteVos());
			    mPublicAdapter.notifyDataSetChanged();
			 }
//		    }else {
//                   ToastUtils.showShortToast(mOrderCstResult.getMsg());
//		    }

            }

            @Override
            public void onError(String result) {
            }
        });
    }

    /**
     * 门锁的提示
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDialogEvent(Event.PopupEvent event) {
        if (mIsCanSkipToSurePage && event.isMute) {
            MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
            if (mBuilder == null) {
                mBuilder = DialogUtils.showOpenDoorDialog(mContext, event.mString);
            }
        }
        if (mIsCanSkipToSurePage && !event.isMute) {
            MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
            if (mBuilder != null) {
                if (mBuilder.mHandler!=null){
                    mBuilder.mHandler.removeCallbacksAndMessages(null);
                }
                mBuilder.mDialog.dismiss();
                mBuilder = null;
            }
            startActSetDate(event.mEthId);
        }
//        if (event.isMute) {
//            if (mBuilder == null) {
//                mBuilder = DialogUtils.showNoDialog(mContext, event.mString, 2, "form", null);
//            }
//        } else {
//            if (mBuilder != null) {
//                mBuilder.mDialog.dismiss();
//                mBuilder = null;
//            }
//        }
    }

    private void startActSetDate(String mEthId) {
        OrderSheetBean.RowsBean orderSheetBean = new OrderSheetBean.RowsBean();
        orderSheetBean.setSuiteId("" + mOrderCstResult.getSuiteId());
        orderSheetBean.cstType = "" + mOrderCstResult.getKindsOfCst();
        orderSheetBean.cstNumber = "" + mOrderCstResult.getCountNum();
        List<BillStockResultBean.OrderDetailVo> transReceiveOrderDetailVosList = new ArrayList<>();
        for (OrderCstResultBean.SuiteVosBean item : movies) {
		BillStockResultBean.OrderDetailVo info = new BillStockResultBean.OrderDetailVo();
		info.setNeedNum(item.getNeedNum());
		info.setCstId(item.getCstId());
		info.setCstName(item.getCstName());
		info.setCstSpec(item.getCstSpec());
		info.setPatientName("");
		info.setDeviceNames(item.getDeviceNames());
		transReceiveOrderDetailVosList.add(info);
	  }
        Intent intent = new Intent(mContext, NewOutMealBingConfirmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mEthId", mEthId);
        bundle.putSerializable("DATA", new Event.EventBillStock(orderSheetBean, transReceiveOrderDetailVosList, mTbaseDevicesFromEvent));
        intent.putExtras(bundle);
        startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBuilder != null) {
            if (mBuilder.mHandler!=null){
                mBuilder.mHandler.removeCallbacksAndMessages(null);
            }
            mBuilder.mDialog.dismiss();
            mBuilder = null;
        }
    }
}
