package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.PendingTaskAdapter;
import high.rivamed.myapplication.adapter.StockLeftDownAdapter;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.PendingTaskBean;
import high.rivamed.myapplication.bean.RunWateBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 20:04
 * 描述:        待办任务fragment
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PendingTaskFrag extends SimpleFragment {

    private static final String TYPE_SIZE = "TYPE_SIZE";
    private static final String TYPE_PAGE = "TYPE_PAGE";
    private static final String DEVICECODE = "DEVICECODE";
    private static final String TYPE_LIST = "list";
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final String TAG = "PublicTimelyFrag";
    @BindView(R.id.tv_task_num)
    TextView mTvTaskNum;
    @BindView(R.id.timely_ll)
    LinearLayout mTimelyLl;
    @BindView(R.id.header)
    MaterialHeader mHeader;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.public_ll)
    LinearLayout mPublicLl;
    Unbinder unbinder;


    private int mParam;
    private TimelyPublicAdapter mPublicAdapter;
    private int mSize; //假数据 举例6个横向格子
    private View mHeadView;
    private int mLayout;
    private TCstInventoryDto mLeftDownBean;
    List<String> titeleList = null;
    private int mStopFlag;
    private TCstInventoryDto mTCstInventoryDto;
    private List<TCstInventoryVo> mTCstInventoryVos;
    private StockLeftDownAdapter mDownAdapter;
    private String mTrim;
    private List<TCstInventoryVo> mTCstStockRightList;
    private PendingTaskAdapter mAdapter;
    private List<RunWateBean.RowsBean> mWateBeanRows;
    private StockLeftDownAdapter mStockLeftAdapter;
    private List<TCstInventoryVo> mInventoryVos;
    private List<PendingTaskBean.MessagesBean> mMessagesList = new ArrayList<>();

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

    public static PendingTaskFrag newInstance() {
        PendingTaskFrag fragment = new PendingTaskFrag();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pending_task_layout;
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
        mAdapter = new PendingTaskAdapter(R.layout.item_pending_task_layout, mMessagesList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        View inflate = LayoutInflater.from(_mActivity)
                .inflate(R.layout.recy_null, null);
        mAdapter.setEmptyView(inflate);
        mRecyclerview.setAdapter(mAdapter);

        NetRequest.getInstance().getPendingTaskList(this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                try {
                    PendingTaskBean emergencyBean = mGson.fromJson(result, PendingTaskBean.class);
                    mMessagesList.clear();
                    if (emergencyBean.getMessages() != null) {
                        mMessagesList.addAll(emergencyBean.getMessages());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
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

}