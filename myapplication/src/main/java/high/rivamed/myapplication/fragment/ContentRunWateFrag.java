package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.BOX_SIZE_DATE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材流水主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentRunWateFrag extends BaseSimpleFragment {

    @BindView(R.id.home_runwate_viewpager)
    ViewPager      mHomeRunWateViewpager;
    @BindView(R.id.home_runwate_rg)
    MagicIndicator mHomeRunwateRg;

    EditText mSearchEt;
    @BindView(R.id.search_iv_delete)
    ImageView mSearchIvDelete;
    @BindView(R.id.search_time_all)
    RadioButton mSearchTimeAll;
    @BindView(R.id.search_time_day)
    RadioButton mSearchTimeDay;
    @BindView(R.id.search_time_week)
    RadioButton mSearchTimeWeek;
    @BindView(R.id.search_time_moon)
    RadioButton mSearchTimeMoon;
    @BindView(R.id.search_time_rg)
    RadioGroup mSearchTimeRg;
    TextView mSearchTimeStart;
    TextView mSearchTimeEnd;
    @BindView(R.id.search_type_all)
    RadioButton mSearchTypeAll;
    @BindView(R.id.search_type_hous)
    RadioButton mSearchTypeHous;
    @BindView(R.id.search_type_use)
    RadioButton mSearchTypeUse;
    @BindView(R.id.search_type_info)
    RadioButton mSearchTypeInfo;
    @BindView(R.id.search_type_out)
    RadioButton mSearchTypeOut;
    @BindView(R.id.search_type_return)
    RadioButton mSearchTypeReturn;
    @BindView(R.id.search_type_return_goods)
    RadioButton mSearchTypeReturnGoods;

    RadioGroup mSearchTypeRg;
    @BindView(R.id.search_type_db)
    RadioButton mSearchTypeDb;
    @BindView(R.id.search_type_thzc)
    RadioButton mSearchTypeThzc;

    public RunWatePagerAdapter mPagerAdapter;
    public List<BoxSizeBean.DevicesBean> mTbaseDevices;

    public static ContentRunWateFrag newInstance() {
        Bundle args = new Bundle();
        ContentRunWateFrag fragment = new ContentRunWateFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.ctrunwate_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    //    /**
//     * 重新加载数据
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onStartFrag(Event.EventFrag event) {
//        if (event.type.equals("START2")) {
//            initData();
//        }
//    }

    private void initData() {
        mBaseTabBtnLeft.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("耗材流水");
        mSearchEt = mContext.findViewById(R.id.search_et);
        mSearchTypeRg = mContext.findViewById(R.id.search_type_rg);
        mSearchTimeEnd = mContext.findViewById(R.id.search_time_end);
        mSearchTimeStart = mContext.findViewById(R.id.search_time_start);
            mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " + SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));

        loadTopBoxSize();
    }

    private void loadTopBoxSize() {
        String string = SPUtils.getString(UIUtils.getContext(), BOX_SIZE_DATE);
        mTbaseDevices = mGson.fromJson(string, new TypeToken<List<BoxSizeBean.DevicesBean>>() {}.getType());
        if (mTbaseDevices != null) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            for (BoxSizeBean.DevicesBean devicesBean : mTbaseDevices) {
                fragments.add(new RunWatePagerFrag(devicesBean.getDeviceId()));
            }
            mPagerAdapter = new RunWatePagerAdapter(getChildFragmentManager(), fragments);
            mHomeRunWateViewpager.setAdapter(mPagerAdapter);
            mHomeRunWateViewpager.setCurrentItem(0);
            mHomeRunWateViewpager.setOffscreenPageLimit(6);
            //                  mHomeRunwateRg.setViewPager(mHomeRunWateViewpager);
            UIUtils.initPvTabLayout(mTbaseDevices,  mHomeRunWateViewpager, mHomeRunwateRg);
//            mHomeRunWateViewpager.addOnPageChangeListener(new PageChangeListener());
        }

    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg, R.id.base_tab_tv_outlogin,
            R.id.search_iv_delete, R.id.search_time_start, R.id.search_time_end})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.search_iv_delete:
                break;
            case R.id.search_time_start:
                //		mStartTime = DialogUtils.showTimeDialog(mContext, mSearchTimeStart);
                break;
            case R.id.search_time_end:
                //		mEndTime = DialogUtils.showTimeDialog(mContext, mSearchTimeEnd);
                break;
        }
    }

    private class RunWatePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;

        public RunWatePagerAdapter(FragmentManager fm, List<Fragment> Fragments) {
            super(fm);
            this.mFragments = Fragments;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTbaseDevices.get(position).getDeviceName();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

//    private class PageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            mSearchTypeRg.check(R.id.search_type_all);
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
}
