package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.MealPopupWindow;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static android.widget.LinearLayout.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_NOBING;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 11:04
 * 描述:        取出套餐
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutMealActivity extends BaseSimpleActivity {

    @BindView(R.id.meal_tv_search)
    TextView mMealTvSearch;
    @BindView(R.id.meal_open_btn)
    TextView mMealOpenBtn;

    //   @BindView(R.id.meal_rv)
    //   RecyclerView       mMealRv;
    @BindView(R.id.recyclerview_null)
    RelativeLayout mRecyclerviewNull;
    @BindView(R.id.timely_ll)
    LinearLayout mLinearLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.public_ll)
    LinearLayout mPublicLl;
    List<Movie> movies = new ArrayList<>();
    private MealPopupWindow mPopupWindowSearch;
    private TimelyPublicAdapter mPublicAdapter;
    private View mHeadView;
    private int mLayout;
    private int mSize;
    private String mMealbing;
    private List<String> titeleList = null;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onActString(Event.EventAct event) {
        mMealbing = event.mString;

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPopupBean(Event.PopupEvent event) {

        if (event.isMute) {
            mMealTvSearch.setText(event.mString);
            mPopupWindowSearch.dismiss();
            mRecyclerviewNull.setVisibility(View.GONE);

            if (mMealbing != null && mMealbing.equals("BING_MEAL")) {//判断是否是绑定患者的套餐
                String[] array = mContext.getResources().getStringArray(R.array.seven_outform_arrays);
                titeleList = Arrays.asList(array);
                mSize = array.length;
                if (mPublicAdapter == null) {
                    movies.addAll(genData7());
                    initData(movies);
                } else {
                    movies.clear();
                    movies.addAll(genData7());
                    mPublicAdapter.notifyDataSetChanged();
                }

            } else {
                String[] array = mContext.getResources().getStringArray(R.array.six_meal_arrays);
                titeleList = Arrays.asList(array);
                mSize = array.length;

                if (mPublicAdapter == null) {
                    if (event.mString.equals("xxx套餐-1")) {
                        movies.addAll(genData6());
                        initData(movies);
                    } else if (event.mString.equals("xxx套餐-5")) {
                        movies.addAll(genData61());
                        initData(movies);
                    } else {
                        movies.addAll(genData62());
                        initData(movies);
                    }
                    Log.i("CC", " event.isMute   ");
                } else {
                    if (event.mString.equals("xxx套餐-1")) {
                        movies.clear();
                        movies.addAll(genData6());
                        mPublicAdapter.notifyDataSetChanged();
                        Log.i("CC", " event.xxx套餐-1   ");
                    } else if (event.mString.equals("xxx套餐-5")) {
                        movies.clear();
                        movies.addAll(genData61());
                        mPublicAdapter.notifyDataSetChanged();
                        Log.i("CC", " event.xxx套餐-5   ");
                    } else {
                        movies.clear();
                        movies.addAll(genData62());
                        mPublicAdapter.notifyDataSetChanged();
                        Log.i("CC", " event.isMxxxxxute   ");
                    }
                }

                //		Log.i("CC", " event.isMxxxxxute   " );

            }

        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_meal_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("套餐领用");
        initlistener();
    }

    private void initData(List<Movie> movies) {

        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mLayout = R.layout.item_form_seven_layout;
        mHeadView = getLayoutInflater().inflate(R.layout.item_form_seven_title_layout,
                (ViewGroup) mLinearLayout.getParent(), false);
        if (mMealbing != null && mMealbing.equals("BING_MEAL")) {

            ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
            ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
            ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
            ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
            ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
            ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(6));

        } else {
            ((TextView) mHeadView.findViewById(R.id.seven_four)).setVisibility(View.GONE);
            ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
            ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
            ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
            ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(3));
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(4));
            ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(5));

        }

        mPublicAdapter = new TimelyPublicAdapter(mLayout, movies, mSize, STYPE_MEAL_NOBING,
                mMealbing);
        mLinearLayout.addView(mHeadView);
        mRecyclerview.setAdapter(mPublicAdapter);
        mPublicAdapter.notifyDataSetChanged();
        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (mMealbing != null && mMealbing.equals("BING_MEAL")) {
                    String six = mPublicAdapter.getItem(position).six;
                    ToastUtils.showShort("six！" + six);
                    if (!six.equals("已领取")) {
                        DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", "BING_MEAL");
                    } else {
                        ToastUtils.showShort("此项已领取！");
                    }
                } else {
                    String six = mPublicAdapter.getItem(position).five;
                    if (!six.equals("已领取")) {
                        DialogUtils.showNoDialog(mContext, position + "号柜门已开", 2, "form", null);
                    } else {
                        ToastUtils.showShort("此项已领取！");
                    }
                }

            }
        });

    }

    private void initlistener() {

    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.meal_tv_search, R.id.meal_open_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_tab_icon_right:
            case R.id.base_tab_tv_name:
                mPopupWindow = new SettingPopupWindow(mContext);
                mPopupWindow.showPopupWindow(view);
                mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
                                break;
                            case 1:
                                mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
                                break;

                        }
                    }
                });
                break;
            case R.id.base_tab_tv_outlogin:
                TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
                builder.setTwoMsg("您确认要退出登录吗?");
                builder.setMsg("温馨提示");
                builder.setLeft("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.setRight("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        App.getInstance().removeALLActivity_();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.base_tab_btn_msg:
                break;
            case R.id.base_tab_back:
                finish();
                break;
            case R.id.meal_tv_search:
                mPopupWindowSearch = new MealPopupWindow(this);
                mPopupWindowSearch.showPopupWindow(mMealTvSearch);
                break;
            case R.id.meal_open_btn:
                ToastUtils.showShort("全部开柜");
                Intent intent = new Intent(mContext, NewOutMealBingConfirmActivity.class);
                startActivity(intent);
                break;
        }
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

    private List<Movie> genData6() {

        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String one = "微创路genData6入系统";
            String two = "genData6" + i;
            String three = "" + i;
            String four = i + "号柜";
            String five = "";
            String six = "打开柜门";

            if (i == 2) {
                five = "已领取";
            } else {
                five = "未领取";
            }
            Movie movie = new Movie(one, two, three, four, five, six, null, null);
            list.add(movie);
        }
        return list;
    }

    private List<Movie> genData61() {

        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String one = "微创路入系统genData61";
            String two = "genData61" + i;
            String three = "" + i;
            String four = i + "号柜";
            String five = "";
            String six = "打开柜门";

            if (i == 2) {
                five = "已领取";
            } else {
                five = "未领取";
            }
            Movie movie = new Movie(one, two, three, four, five, six, null, null);
            list.add(movie);
        }
        return list;
    }

    private List<Movie> genData62() {

        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String one = "genData62微创路入系统";
            String two = "genData62" + i;
            String three = "" + i;
            String four = i + "号柜";
            String five = "";
            String six = "打开柜门";

            if (i == 2) {
                five = "已领取";
            } else {
                five = "未领取";
            }
            Movie movie = new Movie(one, two, three, four, five, six, null, null);
            list.add(movie);
        }
        return list;
    }

    private List<Movie> genData7() {

        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String one = "微创路genData6入系统";
            String two = "genData6" + i;
            String three = "" + i;
            String four = i + "号柜";
            String five = "王麻子";
            String six = "";
            String seven = "打开柜门";

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
