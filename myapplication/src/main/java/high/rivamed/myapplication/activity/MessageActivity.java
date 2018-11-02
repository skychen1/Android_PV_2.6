package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.fragment.PendingTaskFrag;
import high.rivamed.myapplication.utils.SPUtils;

import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_OPERATION_ROOM_NONAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_NAME;

public class MessageActivity extends BaseSimpleActivity {
    @BindView(R.id.home_stock_viewpager)
    public ViewPager mMsgViewpager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        initData();
        mFragments.add(PendingTaskFrag.newInstance());
//        mFragments.add(UnconfirmHaocaiFrag.newInstance());
        mMsgViewpager.setAdapter(new StockPagerAdapter(getSupportFragmentManager()));
        mMsgViewpager.addOnPageChangeListener(new PageChangeListener());
        initListener();
    }

    private void initData() {
        mBaseTabBack.setVisibility(View.VISIBLE);
//        mRgGroup2.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("待办任务");
        mBaseTabLl.setVisibility(View.GONE);
        if (SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME) != null) {
            mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " + SPUtils.getString(mContext, SAVE_STOREHOUSE_NAME));
        }
        if (SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME) != null) {
            mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME) + " - " + SPUtils.getString(mContext, SAVE_OPERATION_ROOM_NONAME));
        }
    }

    private void initListener() {
        mRgGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.msg_rdbtn_left:
                        mMsgViewpager.setCurrentItem(0);
                        break;
                    case R.id.msg_rdbtn_right:
                        mMsgViewpager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    /**
     * 设置fragment
     */
    private class StockPagerAdapter extends FragmentPagerAdapter {

        public StockPagerAdapter(FragmentManager fm) {
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
                    mRgGroup2.check(R.id.msg_rdbtn_left);
                    break;
                case 1:
                    mRgGroup2.check(R.id.msg_rdbtn_right);
                    break;

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
