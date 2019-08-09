package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_LEFT;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/10
 * 描述：异常处理
 */
public class ExceptionDealFrag extends SimpleFragment {
    @BindView(R.id.ecpt_deal_tab)
    MagicIndicator ecptDealTab;
    @BindView(R.id.ecpt_deal_viewpager)
    ViewPager      ecptDealViewpager;
    private List<BoxSizeBean.DevicesBean> mTbaseDevices;
    public ExceptionLeftPagerAdapter mPagerAdapter;
    public static int CURRENT_TAB=0;

    @Override
    public int getLayoutId() {
        return R.layout.exception_deal_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        getBoxData();
    }

    public void getBoxData() {
        //加载柜子数据
        NetRequest.getInstance().loadBoxSize(mContext, new BaseResult() {
            @Override
            public void onSucceed(String result) {
//		mBuilder.mDialog.dismiss();
                BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
                mTbaseDevices = boxSizeBean.getDevices();
                if (mTbaseDevices != null) {
                    onSucceedData();
                }
            }
        });
    }

    private void onSucceedData() {
        if (mTbaseDevices.size() > 1) {
            BoxSizeBean.DevicesBean devicesBean1 = new BoxSizeBean.DevicesBean();
            devicesBean1.setDeviceName("全部");
            devicesBean1.setDeviceId("");
            mTbaseDevices.add(0, devicesBean1);
        }
        if (!isAdded()) return;
        mPagerAdapter = new ExceptionLeftPagerAdapter(getChildFragmentManager());
        ecptDealViewpager.setAdapter(mPagerAdapter);
        ecptDealViewpager.setCurrentItem(0);
        ecptDealViewpager.setOffscreenPageLimit(6);
//        ecptDealTab.setViewPager(ecptDealViewpager);
        UIUtils.initPvTabLayout(mTbaseDevices, ecptDealViewpager, ecptDealTab);
        ecptDealViewpager.addOnPageChangeListener(new PageChangeListener());
    }

    @Override
    public void onBindViewBefore(View view) {

    }

    private class ExceptionLeftPagerAdapter extends FragmentStatePagerAdapter {

        public ExceptionLeftPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PublicExceptionFrag.newInstance(position, STYPE_EXCEPTION_LEFT, mTbaseDevices.get(position).getDeviceId());
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
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            CURRENT_TAB=position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
