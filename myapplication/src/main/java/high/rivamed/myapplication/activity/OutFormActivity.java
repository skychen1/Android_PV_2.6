package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.OutFormAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.fragment.ReciveBillFrag;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.TableTypeView;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/29 14:45
 * 描述:        请领单主界面（医嘱）
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutFormActivity extends BaseSimpleActivity {
    private static final String TAG = "OutFormActivity";
    @BindView(R.id.out_form_rv)
    RecyclerView mOutFormRv;
    @BindView(R.id.cttimecheck_viewpager)
    ViewPager mCttimecheckViewpager;
    @BindView(R.id.tv_open_all)
    TextView mtTvOpenAll;
    @BindView(R.id.recyclerview_null)
    RelativeLayout mRecyclerviewNull;
    public int mSize;
    @BindView(R.id.tv_material_type)
    TextView tvMaterialType;
    @BindView(R.id.tv_material_number)
    TextView tvMaterialNumber;
    @BindView(R.id.stock_left_alltop)
    LinearLayout stockLeftAlltop;

    private TableTypeView mTypeView;
    private OutFormAdapter mOutFormAdapter;
    private List<String> mTiteleList;
    private OutFormPagerAdapter mPagerAdapter;

    //顶部全部医嘱单列表
    private List<OrderSheetBean.RowsBean> mAllOrderSheetList;
    /**
     * 当前所显示页面
     */
    private ReciveBillFrag mCurrentFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_outform_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mAllOrderSheetList = new ArrayList<>();
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("识别耗材");
        initlistener();
        getTopOrderSheetDate(1, 10);
    }

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mOutFormAdapter = new OutFormAdapter(R.layout.item_outform_top_layout, mAllOrderSheetList);
        mOutFormRv.setLayoutManager(layoutManager);
        mOutFormRv.setAdapter(mOutFormAdapter);
        mRecyclerviewNull.setVisibility(View.VISIBLE);

        mPagerAdapter = new OutFormPagerAdapter(
                getSupportFragmentManager());
        mCttimecheckViewpager.setAdapter(mPagerAdapter);
        if (mAllOrderSheetList.size() > 0) {
            mRecyclerviewNull.setVisibility(View.GONE);
            mCttimecheckViewpager.setVisibility(View.VISIBLE);
            mCttimecheckViewpager.setCurrentItem(0);
        } else {
            mCttimecheckViewpager.setVisibility(View.GONE);
        }
        mOutFormAdapter.setOnItemClickListener(new OutFormAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, BaseViewHolder helper, int position) {
                mRecyclerviewNull.setVisibility(View.GONE);
                mCttimecheckViewpager.setVisibility(View.VISIBLE);
                mCttimecheckViewpager.setCurrentItem(position);
                mOutFormAdapter.selectedPosition = position;
                mOutFormAdapter.notifyDataSetChanged();
                Log.e("xb", "position:" + mOutFormAdapter.selectedPosition);
            }
        });
        //横向滑动到最后自动加载更多
        mOutFormRv.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    private void getTopOrderSheetDate(int pageNo, int PageSize) {
        NetRequest.getInstance().findPatientOrderSheetDate(pageNo, PageSize, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "findPatientOrderSheetDate   " + result);
                OrderSheetBean orderSheetBean = mGson.fromJson(result, OrderSheetBean.class);
                mAllOrderSheetList.addAll(orderSheetBean.getRows());
                initData();
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }

    /**
     * 上拉下拉刷新
     */
    private void initlistener() {

    }


    @OnClick(R.id.tv_open_all)
    public void onViewClicked() {
        mCurrentFragment.openAllDoor();
    }

    public void setCstTypeAndNumber(String type, String number) {
        tvMaterialType.setText(type);
        tvMaterialNumber.setText(number);
    }


    private class OutFormPagerAdapter extends FragmentStatePagerAdapter {

        public OutFormPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ReciveBillFrag.newInstance(mAllOrderSheetList.get(position));
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = (ReciveBillFrag) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return mAllOrderSheetList == null ? 0 : mAllOrderSheetList.size();
        }
    }


    //横向滑动加载更多
    public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        // 用来标记是否正在向左滑动
        private boolean isSlidingToLeft = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取最后一个完全显示的itemPosition
                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int itemCount = manager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向左滑动
                if (lastItemPosition == (itemCount - 1) && isSlidingToLeft) {
                    // 加载更多
                    onLoadMore();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // dx值大于0表示正在向左滑动，小于或等于0表示向右滑动或停止
            isSlidingToLeft = dx > 0;
        }

        /**
         * 加载更多回调
         */
        public abstract void onLoadMore();
    }

}
