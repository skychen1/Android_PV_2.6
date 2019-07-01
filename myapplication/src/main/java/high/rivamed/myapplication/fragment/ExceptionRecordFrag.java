package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;

import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_RIGHT;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/10
 * 描述：异常处理-处理记录
 */
public class ExceptionRecordFrag extends SimpleFragment {
    @BindView(R.id.ecpt_deal_tab)
    SlidingTabLayout ecptDealTab;
//    @BindView(R.id.tv_open_door)
//    TextView tvOpenDoor;
    @BindView(R.id.ecpt_deal_viewpager)
    ViewPager ecptDealViewpager;
    private List<BoxSizeBean.DevicesBean> mTbaseDevices;
    public ExceptionRightPagerAdapter mPagerAdapter;
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
        NetRequest.getInstance().loadBoxSize(mContext, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                BoxSizeBean boxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
                mTbaseDevices = boxSizeBean.getDevices();
                if (mTbaseDevices != null) {
                    onSucceedData();
                }
            }
        });
    }

    private void onSucceedData() {
        if (!isAdded()) return;
        mPagerAdapter = new ExceptionRightPagerAdapter(getChildFragmentManager());
        ecptDealViewpager.setAdapter(mPagerAdapter);
        ecptDealViewpager.setCurrentItem(0);
        ecptDealViewpager.setOffscreenPageLimit(6);
        ecptDealTab.setViewPager(ecptDealViewpager);
        ecptDealTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                CURRENT_TAB=position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void onBindViewBefore(View view) {

    }

    private class ExceptionRightPagerAdapter extends FragmentStatePagerAdapter {

        public ExceptionRightPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String deviceCode = null;
            if (mTbaseDevices.size() > 1) {
                if (position == 0) {
                    deviceCode = null;
                } else {
                    deviceCode = mTbaseDevices.get(position - 1).getDeviceId();
                }
            } else {
                deviceCode = mTbaseDevices.get(position).getDeviceId();
            }
            return PublicExceptionFrag.newInstance(position, STYPE_EXCEPTION_RIGHT, deviceCode);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String deviceName = null;
            if (mTbaseDevices.size() > 1) {
                if (position == 0) {
                    deviceName = "全部";
                } else {
                    deviceName = mTbaseDevices.get(position - 1).getDeviceName();
                }
            } else {
                deviceName = mTbaseDevices.get(position).getDeviceName();
            }
            return deviceName;
        }

        @Override
        public int getCount() {
            if (mTbaseDevices.size() > 1) {
                return mTbaseDevices == null ? 0 : mTbaseDevices.size() + 1;
            } else {
                return mTbaseDevices == null ? 0 : mTbaseDevices.size();
            }
        }
    }

}
