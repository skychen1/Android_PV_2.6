package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_RIGHT;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/10
 * 描述：异常处理-处理记录
 */
public class ExceptionRecordFrag extends SimpleFragment {
    @BindView(R.id.ecpt_deal_tab)
    MagicIndicator ecptDealTab;
//    @BindView(R.id.tv_open_door)
//    TextView tvOpenDoor;
    @BindView(R.id.ecpt_deal_viewpager)
    ViewPager      ecptDealViewpager;
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
        String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
        mTbaseDevices = mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType());
        if (mTbaseDevices != null) {
            onSucceedData();
        }
    }

    private void onSucceedData() {
        if (!isAdded()) return;
        mPagerAdapter = new ExceptionRightPagerAdapter(getChildFragmentManager());
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

    private class ExceptionRightPagerAdapter extends FragmentStatePagerAdapter {

        public ExceptionRightPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PublicExceptionFrag.newInstance(position, STYPE_EXCEPTION_RIGHT, mTbaseDevices.get(position).getDeviceId());
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
