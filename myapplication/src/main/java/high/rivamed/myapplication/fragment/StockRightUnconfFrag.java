package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_RIGHT;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 19:04
 * 描述:        未确认耗材
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class StockRightUnconfFrag extends SimpleFragment {
    public int mStockNumber = 9;//列表的列数
    @BindView(R.id.cttimecheck_rg)
    MagicIndicator mCttimeCheck_Rg;
    @BindView(R.id.cttimecheck_viewpager)
    public ViewPager mCttimecheckViewpager;
    @BindView(R.id.stock_left_rv)
    RecyclerView mStockLeftRv;
    @BindView(R.id.stock_left_alltop)
    LinearLayout mStockLeftAlltop;

    public StockMiddlePagerAdapter mPagerAdapter;

    private LoadingDialog.Builder mBuilder;
    private List<BoxSizeBean.DevicesBean> mTbaseDevices;

    @Override
    public int getLayoutId() {
        return R.layout.cttimecheck_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        //      mBuilder = DialogUtils.showLoading(mContext);
        getRightDate();
    }

    public void getRightDate() {
        NetRequest.getInstance().loadBoxSize(mContext, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                //            mBuilder.mDialog.dismiss();
                BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
                mTbaseDevices = boxSizeBean.getDevices();
                if (mTbaseDevices != null) {
                    onSucceedDate();
                }
            }

            @Override
            public void onError(String result) {
                //            mBuilder.mDialog.dismiss();
            }
        });
    }

    private void onSucceedDate() {
        if (mTbaseDevices.size() > 1) {
            BoxSizeBean.DevicesBean devicesBean1 = new BoxSizeBean.DevicesBean();
            devicesBean1.setDeviceName("全部");
            devicesBean1.setDeviceId("");
            mTbaseDevices.add(0, devicesBean1);
        }
        mPagerAdapter = new StockMiddlePagerAdapter(getChildFragmentManager());
        mCttimecheckViewpager.setAdapter(mPagerAdapter);
        mCttimecheckViewpager.setCurrentItem(0);
//        mCttimeCheck_Rg.setViewPager(mCttimecheckViewpager);
        UIUtils.initPvTabLayout(mTbaseDevices, mCttimecheckViewpager, mCttimeCheck_Rg);
    }

    @Override
    public void onBindViewBefore(View view) {

    }

    private class StockMiddlePagerAdapter extends FragmentStatePagerAdapter {

        public StockMiddlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mStockLeftAlltop.setVisibility(View.GONE);
            return PublicStockFrag.newInstance(mStockNumber, STYPE_STOCK_RIGHT, mTbaseDevices.get(position).getDeviceId());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTbaseDevices.get(position).getDeviceName();
        }

        @Override
        public int getCount() {
            return mTbaseDevices == null ? 0 : mTbaseDevices.size();
        }
    }
}
