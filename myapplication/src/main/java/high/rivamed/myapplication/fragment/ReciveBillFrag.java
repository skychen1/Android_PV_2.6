package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.NewOutFormConfirmActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.adapter.BillStockAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.OpenDoorDialog;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 20:04
 * 描述:      请领单领用底部布局
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ReciveBillFrag extends SimpleFragment {

    @BindView(R.id.timely_start_btn)
    TextView mTimelyStartBtn;
    @BindView(R.id.timely_book)
    TextView mTimelyBook;
    @BindView(R.id.timely_reality)
    TextView mTimelyReality;
    @BindView(R.id.timely_reality2)
    TextView mTimelyReality2;
    @BindView(R.id.timely_profit)
    TextView mTimelyProfit;
    @BindView(R.id.timely_loss)
    TextView mTimelyLoss;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.timely_ll)
    LinearLayout mLinearLayout;
    @BindView(R.id.timely_title)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.stock_search)
    FrameLayout mStockSearch;
//    @BindView(R.id.search_etsss)
//    EditText mSearchEt;
    @BindView(R.id.search_iv_delete)
    ImageView mSearchIvDelete;
    @BindView(R.id.right_top)
    LinearLayout mRightTop;
    @BindView(R.id.stock_right_btn)
    LinearLayout mStockRightLL;
    @BindView(R.id.public_rl)
    RelativeLayout mPublicRl;
    @BindView(R.id.public_ll)
    LinearLayout mPublicLl;
    @BindView(R.id.stock_left_all)
    RadioButton mStockLeftAll;
    @BindView(R.id.stock_left_guoqi)
    RadioButton mStockLeftGuoqi;
    @BindView(R.id.stock_left_jinqi)
    RadioButton mStockLeftJinqi;
    @BindView(R.id.stock_left_zhengchang)
    RadioButton mStockLeftZhengchang;
    @BindView(R.id.stock_left_rg)
    RadioGroup mStockLeftRg;
    @BindView(R.id.stock_timely_ll)
    RelativeLayout mStockTimelyLl;

    private BillStockAdapter mPublicAdapter;
    private int mSize; //假数据 举例6个横向格子
    private View mHeadView;
    private int mLayout;
    List<String> titeleList = null;
    //上个界面-医嘱单顶部数据
    public OrderSheetBean.RowsBean mPrePageDate;
    private static final String TAG = "ReciveBillFrag";

    /**
     * 柜子信息
     */
    public static List<BoxSizeBean.DevicesBean> mTbaseDevices = new ArrayList<>();
    /**
     * 是否可以触发事件跳转界面
     */
    private boolean mIsCanSkipToSurePage = true;
    private BillStockResultBean mBillStockResultBean;
    private OpenDoorDialog.Builder mBuilder;

    /**
     * 重新加载数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStartFrag(Event.EventFrag event) {
        if (event.type.equals("START3")) {
            initData();
        }
    }

    public static ReciveBillFrag newInstance(OrderSheetBean.RowsBean item) {
        Bundle args = new Bundle();
        args.putSerializable("OrderSheet", item);
        ReciveBillFrag fragment = new ReciveBillFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPublicAdapter == null && mPrePageDate != null) {
            if (((OutFormActivity) getActivity()).mCurrentFragment == ReciveBillFrag.this) {
                getStockByOrderId(mPrePageDate.getOrderId());
            }
        }


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.public_timely_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        mPublicRl.setVisibility(View.GONE);
        mPrePageDate = (OrderSheetBean.RowsBean) getArguments().getSerializable("OrderSheet");
        if (((OutFormActivity) getActivity()).mCurrentFragment == ReciveBillFrag.this) {
            getStockByOrderId(mPrePageDate.getOrderId());
        }
        initlistener();
    }

    /**
     * 数据加载
     */
    private void initData() {

        String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
        titeleList = Arrays.asList(array);
        mSize = array.length;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        mPublicLl.setLayoutParams(lp);
        mLayout = R.layout.item_form_eight_layout;
        mHeadView = getLayoutInflater().inflate(R.layout.item_form_eight_title_layout,
                (ViewGroup) mLinearLayout.getParent(), false);
        ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
        ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
        ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
        ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
        ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
        ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
        ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
        ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));
        if (mPublicAdapter!=null){
            mPublicAdapter.getData().clear();
            mPublicAdapter.getData().addAll(mBillStockResultBean.getOrderDetailVos());
            mPublicAdapter.notifyDataSetChanged();
        }else {
            mPublicAdapter = new BillStockAdapter(mLayout, mBillStockResultBean.getOrderDetailVos());
            mHeadView.setBackgroundResource(R.color.bg_green);
            mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
            mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            mRefreshLayout.setEnableAutoLoadMore(false);
            mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
            mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.recy_null, null);
            mPublicAdapter.setEmptyView(inflate);
            mRecyclerview.setAdapter(mPublicAdapter);
            mLinearLayout.addView(mHeadView);
        }
    }

    private void initlistener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mRefreshLayout.setNoMoreData(false);
                refreshLayout.finishRefresh();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        });

    }

    @Override
    public void onBindViewBefore(View view) {

    }

    public void getStockByOrderId(String Id) {
        NetRequest.getInstance().findStockByOrderId(Id, this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "getStockByOrderId   " + result);
                mBillStockResultBean = mGson.fromJson(result, BillStockResultBean.class);

                ((OutFormActivity) getActivity()).setCstTypeAndNumber(
                      "" + mBillStockResultBean.getKindsOfCst(), "" + mBillStockResultBean.getCountNum());
                mPrePageDate.cstType = "" + mBillStockResultBean.getKindsOfCst();
                mPrePageDate.cstNumber = "" + mBillStockResultBean.getCountNum();

                initData();
                if (((OutFormActivity) getActivity()).mCurrentFragment == ReciveBillFrag.this) {
                    if (!mBillStockResultBean.isOperateSuccess()) {
                       Toast.makeText(mContext,mBillStockResultBean.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 打开全部柜子
     */
    public void openAllDoor() {
        if (mPublicAdapter != null) {
            if (mPublicAdapter.getData() != null && mPublicAdapter.getData().size() > 0) {
                ToastUtils.showShortToast("全部开柜");
                mTbaseDevices.clear();
                for (int i = 0; i < mPublicAdapter.getData().size(); i++) {
                    List<String> deviceCodes = mPublicAdapter.getItem(i).getDeviceIds();
                    for (String deviceCode : deviceCodes) {
                        BoxSizeBean.DevicesBean oneDoor = new BoxSizeBean.DevicesBean();
                        oneDoor.setDeviceId(deviceCode);
                        if (deviceCode != null) {
                            mTbaseDevices.add(oneDoor);
                        }
                    }
                }
                if (mTbaseDevices.size() > 0) {
                    AllDeviceCallBack.getInstance().openDoor(0, mTbaseDevices);
                } else {
                    ToastUtils.showShortToast("无耗材柜数据");
                }
            } else {
                ToastUtils.showShortToast("无耗材数据");
            }
        } else {
            Toast.makeText(mContext, "暂无数据！", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 门锁的提示
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isDoorOpened(Event.PopupEvent event) {
        if (((OutFormActivity) getActivity()).mCurrentFragment == ReciveBillFrag.this) {
            if (event.isMute) {
                MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_OPEN);
                if (mBuilder == null) {
                    mBuilder = DialogUtils.showOpenDoorDialog(mContext, event.mString);
                }
            }else {
                if (mIsCanSkipToSurePage) {
                    if (!event.isMute) {
			     MusicPlayer.getInstance().play(MusicPlayer.Type.DOOR_CLOSED);
                        if (mBuilder != null) {
                            mBuilder.mHandler.removeCallbacksAndMessages(null);
                            mBuilder.mDialog.dismiss();
                            mBuilder = null;
                        }
                        Intent intent = new Intent(mContext, NewOutFormConfirmActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mEthId", event.mEthId);
                        bundle.putSerializable("DATA", new Event.EventBillStock(mPrePageDate,
                                                                                mBillStockResultBean.getOrderDetailVos(),
                                                                                mTbaseDevices));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);

                    }
                }
            }
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG,"onResume");
        mIsCanSkipToSurePage = true;

        if (((OutFormActivity) getActivity()).mCurrentFragment == ReciveBillFrag.this) {
            getStockByOrderId(mPrePageDate.getOrderId());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsCanSkipToSurePage = false;
    }

    public OrderSheetBean.RowsBean getTypeAndNumber() {
        return mPrePageDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBuilder != null) {
            mBuilder.mHandler.removeCallbacksAndMessages(null);
            mBuilder.mDialog.dismiss();
            mBuilder = null;
        }
    }
}