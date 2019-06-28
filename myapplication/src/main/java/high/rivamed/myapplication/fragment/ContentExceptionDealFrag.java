package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;

import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;
import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_LEFT;
import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_RIGHT;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/10
 * 描述：异常处理
 */
public class ContentExceptionDealFrag extends BaseSimpleFragment {
    @BindView(R.id.home_stock_viewpager)
    ViewPager mHomeStockViewpager;
    public static String CURRENT_TAB=STYPE_EXCEPTION_LEFT;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public static ContentExceptionDealFrag newInstance() {
        Bundle args = new Bundle();
        ContentExceptionDealFrag fragment = new ContentExceptionDealFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.home_ctstock_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mRgGroup3.setVisibility(View.VISIBLE);
        mBaseTabLl.setVisibility(View.VISIBLE);
        mBaseTabBtnLeft.setVisibility(View.VISIBLE);
        mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " + SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));

        mFragments.add(new ExceptionDealFrag());
        mFragments.add(new ExceptionRecordFrag());
        mHomeStockViewpager.setAdapter(new ExceptionPagerAdapter(getChildFragmentManager()));
        mHomeStockViewpager.addOnPageChangeListener(new PageChangeListener());
        mRgGroup3.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.excp_rdbtn_left:
                    CURRENT_TAB=STYPE_EXCEPTION_LEFT;
                    mHomeStockViewpager.setCurrentItem(0);
                    break;
                case R.id.excp_rdbtn_right:
                    CURRENT_TAB=STYPE_EXCEPTION_RIGHT;
                    mHomeStockViewpager.setCurrentItem(1);
                    break;
            }
        });
    }

    /**
     * 设置fragment
     */
    private class ExceptionPagerAdapter extends FragmentPagerAdapter {

        public ExceptionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    /**
     * fragment监听
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    CURRENT_TAB=STYPE_EXCEPTION_LEFT;
                    mRgGroup3.check(R.id.excp_rdbtn_left);
                    EventBusUtils.postSticky(new Event.EventFrag("START6"));
                    break;
                case 1:
                    CURRENT_TAB=STYPE_EXCEPTION_RIGHT;
                    mRgGroup3.check(R.id.excp_rdbtn_right);
                    EventBusUtils.postSticky(new Event.EventFrag("START6"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
