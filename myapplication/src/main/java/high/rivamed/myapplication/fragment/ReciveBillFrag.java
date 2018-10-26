package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import high.rivamed.myapplication.adapter.StockLeftDownAdapter;
import high.rivamed.myapplication.adapter.StockRightAdapter;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.bean.RunWateBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM;

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

    private TimelyPublicAdapter mPublicAdapter;
    private int mSize; //假数据 举例6个横向格子
    private View mHeadView;
    private int mLayout;
    private TCstInventoryDto mLeftDownBean;
    List<String> titeleList = null;
    private TCstInventoryDto mTCstInventoryDto;
    private List<TCstInventoryVo> mTCstInventoryVos;
    private StockLeftDownAdapter mDownAdapter;
    private List<TCstInventoryVo> mTCstStockRightList;
    private StockRightAdapter mRightAdapter;
    private List<RunWateBean.RowsBean> mWateBeanRows;
    private StockLeftDownAdapter mStockLeftAdapter;
    private List<TCstInventoryVo> mInventoryVos;

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

    public static ReciveBillFrag newInstance() {
        Bundle args = new Bundle();
        ReciveBillFrag fragment = new ReciveBillFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.public_timely_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        initData();
        initlistener();
    }

    /**
     * 数据加载
     */
    private void initData() {

        mPublicRl.setVisibility(View.GONE);
        String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
        titeleList = Arrays.asList(array);
        mSize = array.length;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        mPublicLl.setLayoutParams(lp);
        mLayout = R.layout.item_form_seven_layout;
        mHeadView = getLayoutInflater().inflate(R.layout.item_form_seven_title_layout,
                (ViewGroup) mLinearLayout.getParent(), false);
        ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
        ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
        ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
        ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
        ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
        ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
        ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));
        mPublicAdapter = new TimelyPublicAdapter(mLayout, genData72(), mSize, STYPE_FORM);
        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String six = mPublicAdapter.getItem(position).six;
                if (!six.equals("已领取")) {
                    DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", null);
                } else {
                    ToastUtils.showShort("此项已领取！");
                }

            }
        });
        mHeadView.setBackgroundResource(R.color.bg_green);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRecyclerview.setAdapter(mPublicAdapter);
        mLinearLayout.addView(mHeadView);
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

    private List<Movie> genData72() {

        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String one = "微创路入系统";
            String two = "FLR01" + i;
            ;
            String three = "" + i;
            String four = "王麻子" + i;
            String five = i + "号柜";
            ;
            String seven = "打开柜门";
            String six = "";
            if (i == 2) {
                six = "已领取";
            } else {
                six = "未领取";
            }
            Movie movie = new Movie(one, two, three, four, five, six, seven, null);
            list.add(movie);
        }
        return list;
    }

}