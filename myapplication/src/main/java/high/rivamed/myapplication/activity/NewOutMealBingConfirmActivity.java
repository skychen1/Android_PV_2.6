package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.BillOrderAdapter;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindBillOrderBean;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.OutFormConfirmResultBean;
import high.rivamed.myapplication.bean.OutFromConfirmRequestBean;
import high.rivamed.myapplication.bean.UseCstOderResultBean;
import high.rivamed.myapplication.bean.UseCstOrderBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.RvDialog;
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
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.COUNTDOWN_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.STYPE_FORM_CONF;
import static high.rivamed.myapplication.cont.Constants.STYPE_MEAL_BING;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.views.RvDialog.sTableTypeView;

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
    @BindView(R.id.ly_bind_patient)
    public TextView mBindPatient;
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
    private int mTitleLayout;
    private View mHeadView;
    private BillOrderAdapter mPublicAdapter;
    public SparseBooleanArray mCheckStates = new SparseBooleanArray();

    /**
     * epc扫描请求数据使用
     */
    private OrderSheetBean.RowsBean mPrePageDate;
    /**
     * 查看套组清单使用
     */
    private List<BillStockResultBean.TransReceiveOrderDetailVosBean> mTransReceiveOrderDetailVos;
    /**
     * 根据EPC请求耗材列表参数
     */
    private FindBillOrderBean mFindBillOrderBean;

    /**
     * 查询耗材返回数据
     */
    private List<BillOrderResultBean> mBillOrderResultList;
    /**
     * 确认领用-请求参数
     */
    private UseCstOrderBean mUseCstOrderRequest;

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
        initData(false);
        if (mPublicAdapter != null && mBillOrderResultList != null) {
            initView(false);
        }
    }

    /**
     * 数据加载
     */
    private void initData(boolean isShowPatient) {
        if (mUseCstOrderRequest == null) {
            mUseCstOrderRequest = new UseCstOrderBean();
            mUseCstOrderRequest.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
            mUseCstOrderRequest.setTCstInventoryVos(new ArrayList<>());
        }
        mBaseTabTvTitle.setText("识别耗材");
        mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 2 +
                "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
                7 + "</big></font>"));
        mTimelyStartBtn.setVisibility(View.VISIBLE);
        mDownBtnOneLL.setVisibility(View.VISIBLE);
        String[] array;
        if (isShowPatient) {
            array = mContext.getResources().getStringArray(R.array.seven_meal_arrays);
        } else {
            array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
        }
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
        if (UIUtils.getConfigType(mContext, CONFIG_007)) {
            mBindPatient.setVisibility(View.VISIBLE);
        } else {
            mBindPatient.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn_right,
            R.id.ly_bing_btn_right, R.id.timely_left, R.id.timely_right, R.id.ly_bing_btn, R.id.ly_bind_patient, R.id.activity_btn_one})
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
                DialogUtils.showLookUpDetailedListDialog(mContext, false, mTransReceiveOrderDetailVos);
                break;
            case R.id.activity_btn_one:
                useOrderCst();
                break;
            case R.id.ly_bind_patient:
                if (UIUtils.getConfigType(mContext, CONFIG_012)) {
                    Intent intent = new Intent(mContext, TemPatientBindActivity.class);
                    intent.putExtra("type", "afterBindTemp");
                    startActivity(intent);
                } else {
                    loadBingDateNoTemp("", 0, mTbaseDevices);
                }

                break;
        }
    }

    private void initView(boolean isShowPatient) {
        String[] array;
        if (isShowPatient) {
            array = mContext.getResources().getStringArray(R.array.seven_meal_arrays);
            mLayout = R.layout.item_formcon_seven_layout;
            mTitleLayout = R.layout.item_formcon_seven_title_layout;
        } else {
            array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
            mLayout = R.layout.item_formcon_six_layout;
            mTitleLayout = R.layout.item_formcon_six_title_layout;
        }
        titeleList = Arrays.asList(array);
        mSize = array.length;
        mHeadView = mContext.getLayoutInflater()
                .inflate(mTitleLayout,
                        (ViewGroup) mLinearLayout.getParent(), false);
        ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
        ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
        ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
        ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
        ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
        if (!isShowPatient) {
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
        } else {
            ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(5));
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(6));
        }
        mPublicAdapter = new BillOrderAdapter(mLayout, mSize, mBillOrderResultList);
        mHeadView.setBackgroundResource(R.color.bg_green);

        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRecyclerview.setAdapter(mPublicAdapter);
        mLinearLayout.removeView(mHeadView);
        mLinearLayout.removeAllViews();
        mLinearLayout.addView(mHeadView);
        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //TODO 10.23 选择要打开的耗材柜弹出框
//                DialogUtils.showSelectOpenCabinetDialog(mContext, genData6());
                //TODO 10.24 查看请领单
//                DialogUtils.showLookUpDetailedListDialog(mContext, false, genData6());
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void reciveBillOrderDate(Event.EventBillOrder event) {
        mPrePageDate = event.orderSheetBean;
        mTransReceiveOrderDetailVos = event.transReceiveOrderDetailVosList;
        if (mFindBillOrderBean == null) {
            mFindBillOrderBean = new FindBillOrderBean();
            FindBillOrderBean.CstPlanBean cstPlanBean = new FindBillOrderBean.CstPlanBean();
            cstPlanBean.setId(mPrePageDate.getId());
            mFindBillOrderBean.setCstPlan(cstPlanBean);
            mFindBillOrderBean.setCstInventoryVos(new ArrayList<>());
            FindBillOrderBean.CstInventoryVosBean item1 = new FindBillOrderBean.CstInventoryVosBean();
            item1.setEpc("00021720180412000336");
            FindBillOrderBean.CstInventoryVosBean item2 = new FindBillOrderBean.CstInventoryVosBean();
            item2.setEpc("00021720180409000045");
            FindBillOrderBean.CstInventoryVosBean item3 = new FindBillOrderBean.CstInventoryVosBean();
            item1.setEpc("00021020180921000002");
            FindBillOrderBean.CstInventoryVosBean item4 = new FindBillOrderBean.CstInventoryVosBean();
            item4.setEpc("000000002C10201809040035");
            mFindBillOrderBean.getCstInventoryVos().add(item1);
            mFindBillOrderBean.getCstInventoryVos().add(item2);
            mFindBillOrderBean.getCstInventoryVos().add(item3);
            mFindBillOrderBean.getCstInventoryVos().add(item4);
        }
        if (mUseCstOrderRequest == null) {
            mUseCstOrderRequest = new UseCstOrderBean();
            mUseCstOrderRequest.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
            mUseCstOrderRequest.setTCstInventoryVos(new ArrayList<>());
        }
        EventBusUtils.removeStickyEvent(Event.EventBillOrder.class);
        findBillOrder();
        loadDate();
    }

    /**
     * 根据EPC查询的套组耗材信息
     */
    private void findBillOrder() {
        NetRequest.getInstance().findOrderCstListByEpc(mGson.toJson(mFindBillOrderBean), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                mBillOrderResultList = mGson.fromJson(result, new TypeToken<List<BillOrderResultBean>>() {
                }.getType());
                if (mPublicAdapter == null) {
                    initView(false);
                } else {
                    mPublicAdapter.setNewData(mBillOrderResultList);
                    mPublicAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }

    /**
     * 确认套组领用
     */
    private void useOrderCst() {
        mUseCstOrderRequest.getTCstInventoryVos().clear();
        for (BillOrderResultBean item : mBillOrderResultList) {
            UseCstOrderBean.TCstInventoryVosBean info = new UseCstOrderBean.TCstInventoryVosBean();
            info.setCount(item.getCount());
            info.setCountActual(item.getCountActual());
            info.setCountStock(item.getCountStock());
            info.setCstId(item.getCstId());
            info.setCstName(item.getCstName());
            info.setCstSpec(item.getCstSpec());
            info.setDeviceCode(item.getDeviceCode());
            info.setDeviceName(item.getDeviceName());
            info.setEpc(item.getEpc());
            info.setExpiration(item.getExpiration());
            info.setExpirationTime(item.getExpirationTime());
            info.setPatientId(item.getPatientId());
            info.setPatientName(item.getPatientName());
            info.setStorehouseCode(item.getStorehouseCode());
            mUseCstOrderRequest.getTCstInventoryVos().add(info);
        }
        if (mUseCstOrderRequest.getTCstInventoryVos().size() == 0) {
            ToastUtils.showShort("无耗材，无法领用");
            return;
        }
        NetRequest.getInstance().useOrderCst(mGson.toJson(mUseCstOrderRequest), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                UseCstOderResultBean info = mGson.fromJson(result, UseCstOderResultBean.class);
                if (info.isOperateSuccess()) {
                    ToastUtils.showShort(info.getMsg());
                    finish();
                } else {
                    ToastUtils.showShort(info.getMsg());
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }

    private int mNoTemPage = 1;
    private int mRows = 20;
    private RvDialog.Builder mShowRvDialog2;
    private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos = new ArrayList<>();
    private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;

    /**
     * 获取需要绑定的患者（不包含临时患者）
     */
    private void loadBingDateNoTemp(
            String optienNameOrId, int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
        LogUtils.i(TAG, "optienNameOrId   " + optienNameOrId);
        NetRequest.getInstance()
                .findSchedulesDate(optienNameOrId, mNoTemPage, mRows, this, null, new BaseResult() {
                    @Override
                    public void onSucceed(String result) {
                        LogUtils.i(TAG, "result   " + result);

                        FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);
                        if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {

                            if (mPatientInfos != null) {
                                for (int i = 0; i < bean.getRows().size(); i++) {
                                    BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
                                    data.setPatientId(bean.getRows().get(i).getPatientId());
                                    data.setPatientName(bean.getRows().get(i).getPatientName());
                                    data.setDeptName(bean.getRows().get(i).getDeptName());
                                    data.setOperationSurgeonName(
                                            bean.getRows().get(i).getOperationSurgeonName());
                                    data.setOperatingRoomNoName(
                                            bean.getRows().get(i).getOperatingRoomNoName());
                                    data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
                                    data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
                                    data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
                                    data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
                                    mPatientInfos.add(data);
                                }
                                if (mShowRvDialog2 != null && mShowRvDialog2.mDialog.isShowing()) {
                                    sTableTypeView.mBingOutAdapter.notifyDataSetChanged();
                                } else {
                                    mShowRvDialog2 = DialogUtils.showRvDialog(mContext, mContext,
                                            mPatientInfos, "firstBind",
                                            position, mTbaseDevices);
                                    mShowRvDialog2.mRefreshLayout.setOnRefreshListener(
                                            new OnRefreshListener() {
                                                @Override
                                                public void onRefresh(RefreshLayout refreshLayout) {
                                                    mShowRvDialog2.mRefreshLayout.setNoMoreData(false);
                                                    mNoTemPage = 1;
                                                    mPatientInfos.clear();
                                                    loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
                                                    mShowRvDialog2.mRefreshLayout.finishRefresh();
                                                }
                                            });
                                    mShowRvDialog2.mRefreshLayout.setOnLoadMoreListener(
                                            new OnLoadMoreListener() {
                                                @Override
                                                public void onLoadMore(RefreshLayout refreshLayout) {
                                                    mNoTemPage++;
                                                    loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
                                                    mShowRvDialog2.mRefreshLayout.finishLoadMore();
                                                }
                                            });
                                }
                            } else {
                                for (int i = 0; i < bean.getRows().size(); i++) {
                                    BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
                                    data.setPatientId(bean.getRows().get(i).getPatientId());
                                    data.setPatientName(bean.getRows().get(i).getPatientName());
                                    data.setDeptName(bean.getRows().get(i).getDeptName());
                                    data.setOperationSurgeonName(
                                            bean.getRows().get(i).getOperationSurgeonName());
                                    data.setOperatingRoomNoName(
                                            bean.getRows().get(i).getOperatingRoomNoName());
                                    data.setScheduleDateTime(bean.getRows().get(i).getScheduleDateTime());
                                    data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
                                    data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
                                    data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
                                    mPatientInfos.add(data);
                                }
                                mShowRvDialog2 = DialogUtils.showRvDialog(mContext, mContext,
                                        mPatientInfos, "firstBind",
                                        position, mTbaseDevices);
                                mShowRvDialog2.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                                    @Override
                                    public void onRefresh(RefreshLayout refreshLayout) {
                                        mShowRvDialog2.mRefreshLayout.setNoMoreData(false);
                                        mNoTemPage = 1;
                                        mPatientInfos.clear();
                                        loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
                                        mShowRvDialog2.mRefreshLayout.finishRefresh();
                                    }
                                });
                                mShowRvDialog2.mRefreshLayout.setOnLoadMoreListener(
                                        new OnLoadMoreListener() {
                                            @Override
                                            public void onLoadMore(RefreshLayout refreshLayout) {
                                                mNoTemPage++;
                                                loadBingDateNoTemp(optienNameOrId, position, mTbaseDevices);
                                                mShowRvDialog2.mRefreshLayout.finishLoadMore();
                                            }
                                        });
                            }
                        } else {
                            if (mNoTemPage == 1) {
                                ToastUtils.showShort("没有患者数据");
                            }
                        }
                    }
                });
    }

    private LoadingDialog.Builder mShowLoading;
    private BoxSizeBean mBoxSizeBean;

    //数据加载
    private void loadDate() {
        LogUtils.i(TAG, "loadDate");
        NetRequest.getInstance().loadBoxSize(mContext, mShowLoading, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                mBoxSizeBean = mGson.fromJson(result, BoxSizeBean.class);
                LogUtils.i(TAG, "result  " + result);
                mTbaseDevices = mBoxSizeBean.getTbaseDevices();
                if (mTbaseDevices.size() > 1) {
                    BoxSizeBean.TbaseDevicesBean tbaseDevicesBean = new BoxSizeBean.TbaseDevicesBean();
                    tbaseDevicesBean.setDeviceName("全部开柜");
                    mTbaseDevices.add(0, tbaseDevicesBean);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRvCheckBindEvent(Event.EventCheckbox event) {
        mUseCstOrderRequest.setPatientId(event.id);
        for (BillOrderResultBean item : mBillOrderResultList) {
            item.setPatientId(event.id);
            item.setPatientName(event.mString);
        }
        initView(true);
        mPublicAdapter.notifyDataSetChanged();
    }
}