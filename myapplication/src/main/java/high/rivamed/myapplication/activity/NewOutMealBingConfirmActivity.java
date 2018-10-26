package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_ALL_IN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_HAOCAI;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_RECEIVE;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_FORM_CONFIRM;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_IN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_OUT;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_PATIENT_CONN;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_STOCK_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TEMPORARY_BING;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_FOUR_DETAILS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_LOSS;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_PROFIT;
import static high.rivamed.myapplication.cont.Constants.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_BING;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 15:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutMealBingConfirmActivity extends BaseSimpleActivity {

    private static final String TAG = "BaseTimelyActivity";
    public int my_id;
    public int mSize;
    @BindView(R.id.timely_start_btn)
    public TextView mTimelyStartBtn;
    @BindView(R.id.timely_open_door)
    public TextView mTimelyOpenDoor;
    @BindView(R.id.ly_bing_btn)
    TextView mLyBingBtn;
    @BindView(R.id.timely_left)
    public TextView mTimelyLeft;
    @BindView(R.id.timely_right)
    public TextView mTimelyRight;
    @BindView(R.id.activity_down_btnll)
    LinearLayout mActivityDownBtnTwoll;
    @BindView(R.id.btn_four_ly)
    public TextView mBtnFourLy;
    @BindView(R.id.btn_four_yc)
    public TextView mBtnFourYc;
    @BindView(R.id.btn_four_tb)
    public TextView mBtnFourTb;
    @BindView(R.id.btn_four_th)
    public TextView mBtnFourTh;
    @BindView(R.id.activity_down_btn_four_ll)
    LinearLayout mActivityDownBtnFourLl;
    @BindView(R.id.activity_down_btn_one_ll)
    LinearLayout mDownBtnOneLL;
    @BindView(R.id.activity_btn_one)
    TextView mDownBtnOne;
    @BindView(R.id.timely_name)
    TextView mTimelyName;
    @BindView(R.id.timely_number)
    public TextView mTimelyNumber;
    @BindView(R.id.timely_ll)
    LinearLayout mLinearLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.timely_rl_title)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.timely_ll_gone)
    LinearLayout mTimelyLlGone;
    @BindView(R.id.timely_number_left)
    TextView mTimelyNumberLeft;
    @BindView(R.id.timely_start_btn_right)
    public TextView mTimelyStartBtnRight;
    @BindView(R.id.timely_open_door_right)
    public TextView mTimelyOpenDoorRight;
    @BindView(R.id.ly_bing_btn_right)
    TextView mLyBingBtnRight;
    @BindView(R.id.timely_ll_gone_right)
    LinearLayout mTimelyLlGoneRight;
    @BindView(R.id.search_et)
    EditText mSearchEt;
    @BindView(R.id.search_iv_delete)
    ImageView mSearchIvDelete;
    @BindView(R.id.stock_search)
    FrameLayout mStockSearch;
    @BindView(R.id.ly_creat_temporary_btn)
    TextView mLyCreatTemporaryBtn;
    @BindView(R.id.dialog_left)
    TextView mDialogLeft;
    @BindView(R.id.dialog_right)
    public TextView mDialogRight;
    @BindView(R.id.activity_down_btn_seven_ll)
    LinearLayout mActivityDownBtnSevenLl;
    @BindView(R.id.timely_rl)
    LinearLayout mTimelyRl;
    @BindView(R.id.header)
    MaterialHeader mHeader;
    @BindView(R.id.public_ll)
    LinearLayout mPublicLl;
    @BindView(R.id.tv_patient_conn)
    TextView mTvPatientConn;
    @BindView(R.id.activity_down_patient_conn)
    LinearLayout mActivityDownPatientConn;
    @BindView(R.id.all_out_text)
    public TextView mAllOutText;

    public String mData;
    public TableTypeView mTypeView;
    List<String> titeleList = null;


    public List<TCstInventoryVo> mTCstInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息


    public List<BingFindSchedulesBean.PatientInfosBean> patientInfos = new ArrayList<>();

    private int mLayout;
    private View mHeadView;
    private TimelyPublicAdapter mPublicAdapter;
    public SparseBooleanArray mCheckStates = new SparseBooleanArray();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_timely_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvName.setText(SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
        if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
                SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
            Glide.with(this)
                    .load(R.mipmap.hccz_mrtx_nan)
                    .error(R.mipmap.hccz_mrtx_nan)
                    .into(mBaseTabIconRight);
        } else {
            Glide.with(this)
                    .load(R.mipmap.hccz_mrtx_nv)
                    .error(R.mipmap.hccz_mrtx_nv)
                    .into(mBaseTabIconRight);
        }
        initData();
        initView();
    }

    /**
     * 数据加载
     */
    private void initData() {
        mBaseTabTvTitle.setText("识别耗材");
        mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
                "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
                7 + "</big></font>"));
        mTimelyStartBtn.setVisibility(View.VISIBLE);
        mDownBtnOneLL.setVisibility(View.VISIBLE);
        String[] array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
        titeleList = Arrays.asList(array);
        mSize = array.length;

        //重新扫描
        mTimelyStartBtn.setVisibility(View.VISIBLE);
        //打开柜门
        mTimelyOpenDoor.setVisibility(View.VISIBLE);
        //查看医嘱清单
        mLyBingBtn.setVisibility(View.VISIBLE);

        mTimelyStartBtn.setText("重新扫描");
        mTimelyOpenDoor.setText("打开柜门");
        mLyBingBtn.setText("查看医嘱清单");
    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn_right,
            R.id.ly_bing_btn_right, R.id.timely_left, R.id.timely_right, R.id.ly_bing_btn})
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
            case R.id.timely_start_btn_right:
                break;
            case R.id.timely_left:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    ToastUtils.showShort("timely_left");
                }
                break;
            case R.id.timely_right:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    ToastUtils.showShort("timely_right");
                }
                break;
            case R.id.ly_bing_btn_right:
                ToastUtils.showShort("绑定");
                //		DialogUtils.showRvDialog(this, mContext);
                break;
            case R.id.ly_bing_btn:
                Intent intent = new Intent(mContext, TemPatientBindActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initView() {

        mLayout = R.layout.item_formcon_six_layout;
        mHeadView = mContext.getLayoutInflater()
                .inflate(R.layout.item_formcon_six_title_layout,
                        (ViewGroup) mLinearLayout.getParent(), false);
        ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
        ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
        ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
        ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
        ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
        ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
        mPublicAdapter = new TimelyPublicAdapter(mLayout, genData6(), mSize, STYPE_FORM_CONF,
                mCheckStates);
        mHeadView.setBackgroundResource(R.color.bg_green);

        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRecyclerview.setAdapter(mPublicAdapter);
        mLinearLayout.removeView(mHeadView);
        mLinearLayout.addView(mHeadView);
        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //TODO 10.23 选择要打开的耗材柜弹出框
                DialogUtils.showSelectOpenCabinetDialog(mContext, genData6());
                //TODO 10.24 查看请领单
                DialogUtils.showLookUpDetailedListDialog(mContext, false, genData6());
            }
        });
    }

    private List<Movie> genData6() {

        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String one = null;
            String two = null;
            String three = null;
            String four = null;
            String five = null;
            String six = null;
            String seven = null;
            if (i == 1) {
                two = "*15170116220035c2dddddsssssssssss3" + i;
                one = "微创路入系统";
                three = "FLR01" + i;
                five = i + "号柜";
                four = "已过期";
                six = "禁止操作";
                seven = "0";
            } else if (i == 2) {
                two = "*15170116220035c2" + i;
                one = "微创路入系统";
                three = "FLR01" + i;
                four = "≤100天";
                five = i + "号柜";
                six = "禁止操作";
                seven = "1";
            } else if (i == 3) {
                one = "微创路入系统";
                two = "*15170116220035c2" + i;
                three = "FLR01" + i;
                four = "≤70天";
                five = i + "号柜";
                six = "入库";
                seven = "0";
            } else if (i == 4) {
                one = "微创路入系统";
                two = "*15170116220035c2" + i;
                three = "FLR01" + i;
                four = "≤28天";
                five = i + "号柜";
                six = "退回";
                seven = "1";
            } else {
                one = "微创路入系统";
                three = "FLR01" + i;
                two = "*15170116220035sssssss3" + i;
                five = i + "号柜";
                four = "2019-10-22";
                six = "移入";
                seven = "0";
            }

            Movie movie = new Movie(one, two, three, four, five, six, seven, null);
            list.add(movie);
        }
        return list;
    }
}