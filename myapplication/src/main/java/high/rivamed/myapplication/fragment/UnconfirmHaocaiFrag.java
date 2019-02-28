package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.adapter.UnconfirmHaocaiAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 20:04
 * 描述:        未确认耗材fragment
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class UnconfirmHaocaiFrag extends SimpleFragment {

    private static final String TAG = "PublicStockFrag";
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
    @BindView(R.id.search_et)
    EditText mSearchEt;
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

    private int mParam;
    private TimelyPublicAdapter mPublicAdapter;
    private int mSize; //假数据 举例6个横向格子
    private View mHeadView;
    private int mLayout;
    List<String> titeleList = null;
    private List<InventoryVo>      mTCstStockRightList;
    private UnconfirmHaocaiAdapter mRightAdapter;

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

    public static UnconfirmHaocaiFrag newInstance() {
        UnconfirmHaocaiFrag fragment = new UnconfirmHaocaiFrag();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.unconfirm_haocai_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        Bundle arguments = getArguments();

        initData();

        initlistener();
    }

    /**
     * 数据加载
     */
    private void initData() {
        mRightTop.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.GONE);
        String[] array = mContext.getResources().getStringArray(R.array.eight_unconfirm_arrays);
        titeleList = Arrays.asList(array);
        mSize = array.length;

        mPublicRl.setVisibility(View.VISIBLE);
        mStockRightLL.setVisibility(View.GONE);
        mStockSearch.setVisibility(View.GONE);
        mSearchEt.setVisibility(View.GONE);
        loadStockRightDate("", "");//todo 待完成 调用接口


    }


    private void initlistener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mRefreshLayout.setNoMoreData(false);
                Log.i("BaseQuickAdapter", "点击即将单独的的的的的");

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

    /**
     * 未确认耗材
     *
     * @param deviceCode
     */
    private void loadStockRightDate(String deviceCode, String mTrim) {
        LogUtils.i(TAG, "deviceCode   " + deviceCode);
        LogUtils.i(TAG, "mTrim   " + mTrim);
        NetRequest.getInstance().getRightUnconfDate(deviceCode, mTrim, mContext, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                Log.i("ffa", "result   " + result);
                if (mTCstStockRightList != null) {
                    mTCstStockRightList.clear();
                    InventoryDto socketRightBean = mGson.fromJson(result, InventoryDto.class);
                    List<InventoryVo> inventoryVos = socketRightBean.getInventoryVos();
                    mTCstStockRightList.addAll(inventoryVos);
                    mRightAdapter.notifyDataSetChanged();

                } else {
                    InventoryDto socketRightBean = mGson.fromJson(result, InventoryDto.class);
                    mTCstStockRightList = socketRightBean.getInventoryVos();
                    mLayout = R.layout.item_runwate_eight_layout;
                    mHeadView = LayoutInflater.from(_mActivity)
                            .inflate(R.layout.item_runwate_eight_title_layout,
                                    (ViewGroup) mLinearLayout.getParent(), false);
                    ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
                    ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
                    ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
                    ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
                    ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
                    ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
                    ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
                    ((TextView) mHeadView.findViewById(R.id.seven_eight)).setText(titeleList.get(7));

                    mRightAdapter = new UnconfirmHaocaiAdapter(mLayout, mTCstStockRightList);

                    mHeadView.setBackgroundResource(R.color.bg_green);
                    LogUtils.i("CC", "mRecyclerview   " + (mRecyclerview == null));
                    mRecyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
                    mRecyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
                    mRefreshLayout.setEnableAutoLoadMore(false);
                    mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
                    mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
                    View inflate = LayoutInflater.from(_mActivity)
                            .inflate(R.layout.recy_null, null);
                    mRightAdapter.setEmptyView(inflate);
                    mRecyclerview.setAdapter(mRightAdapter);
                    mLinearLayout.addView(mHeadView);
                }
            }

        });
    }

}