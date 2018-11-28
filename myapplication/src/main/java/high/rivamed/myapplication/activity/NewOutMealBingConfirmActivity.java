package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.BillOrderAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindBillOrderBean;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.UseCstOderResultBean;
import high.rivamed.myapplication.bean.UseCstOrderBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.views.RvDialog.sTableTypeView;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 15:34
 * 描述:        套餐耗材扫描操作界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutMealBingConfirmActivity extends BaseSimpleActivity {

    private String TAG = "NewOutMealBingConfirmActivity";
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
    private BillOrderResultBean mBillOrderResultBean;
    /**
     * 确认领用-请求参数
     */
    private UseCstOrderBean mUseCstOrderRequest;


    /**
     * 传输的EPC
     *
     * @param event
     */
    private Map<String, List<TagInfo>> mEPCMapDate = new TreeMap<>();
    private LoadingDialog.Builder mLoading;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_timely_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        Event.EventBillStock data = (Event.EventBillStock) getIntent().getExtras().getSerializable("DATA");
        mPrePageDate = data.orderSheetBean;
        mTransReceiveOrderDetailVos = data.transReceiveOrderDetailVosList;

        mTbaseDevices = data.tbaseDevices;
        mFindBillOrderBean = new FindBillOrderBean();
        FindBillOrderBean.CstPlanBean cstPlanBean = new FindBillOrderBean.CstPlanBean();
        cstPlanBean.setId(mPrePageDate.getId());
        mFindBillOrderBean.setCstPlan(cstPlanBean);
        mFindBillOrderBean.setCstInventoryVos(new ArrayList<>());
        initData(false);
        if (mPublicAdapter != null && mBillOrderResultBean.getCstInventoryVos() != null) {
            initView(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventLoading(Event.EventLoading event) {
        if (event.loading) {
            if (mLoading == null) {
                LogUtils.i(TAG, "     mLoading  新建 ");
                mLoading = DialogUtils.showLoading(this);
            } else {
                if (!mLoading.mDialog.isShowing()) {
                    LogUtils.i(TAG, "     mLoading   重新开启");
                    mLoading.create().show();
                }
            }
        } else {
            if (mLoading != null) {
                LogUtils.i(TAG, "     mLoading   关闭");
                mLoading.mAnimationDrawable.stop();
                mLoading.mDialog.dismiss();
                mLoading = null;
            }
        }
    }


    /**
     * 数据加载
     */
    private void initData(boolean isShowPatient) {
        mBaseTabBack.setVisibility(View.GONE);
        mBaseTabIconRight.setEnabled(false);
        mBaseTabTvName.setEnabled(false);
        mBaseTabOutLogin.setEnabled(false);
        mBaseTabBtnMsg.setEnabled(false);
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

        if (mUseCstOrderRequest == null) {
            mUseCstOrderRequest = new UseCstOrderBean();
            mUseCstOrderRequest.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
            mUseCstOrderRequest.setTCstInventoryVos(new ArrayList<>());
        }
        mBaseTabTvTitle.setText("套组领用识别耗材");
        mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
                "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
                0 + "</big></font>"));
        mTimelyStartBtn.setVisibility(View.VISIBLE);
        mDownBtnOneLL.setVisibility(View.VISIBLE);
        String[] array;

        if (UIUtils.getConfigType(mContext, CONFIG_007)) {
            mBindPatient.setVisibility(View.VISIBLE);
            mDownBtnOne.setEnabled(false);
            array = mContext.getResources().getStringArray(R.array.seven_meal_arrays);
        } else {
            mBindPatient.setVisibility(View.GONE);
            mDownBtnOne.setEnabled(true);
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
        mLyBingBtn.setText("查看套组清单");

    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn_right,
            R.id.ly_bing_btn_right, R.id.timely_left, R.id.timely_right, R.id.ly_bing_btn, R.id.timely_start_btn, R.id.ly_bind_patient, R.id.timely_open_door, R.id.activity_btn_one})
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
                mContext.startActivity(new Intent(this, MessageActivity.class));

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
            case R.id.timely_start_btn:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    mEPCMapDate.clear();
                    mFindBillOrderBean.getCstInventoryVos().clear();
                    mFindBillOrderBean=null;
                    for (String deviceInventoryVo : mEthDeviceIdBack) {
                        String deviceCode = deviceInventoryVo;
                        LogUtils.i(TAG, "deviceCode    " + deviceCode);
                        startScan(deviceCode);
                    }
                }
                break;
            case R.id.timely_open_door:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    reOpenDoor();
                }
                break;
            case R.id.ly_bing_btn:
                OrderSheetBean.RowsBean prePageDate = new OrderSheetBean.RowsBean();
                prePageDate.cstType = "" + mBillOrderResultBean.getCountKind();
                prePageDate.cstNumber = "" + mBillOrderResultBean.getCountNum();
                LogUtils.i(TAG,"mTransReceiveOrderDetailVos   "+(mGson.toJson(mTransReceiveOrderDetailVos)));
                DialogUtils.showLookUpDetailedListDialog(mContext, false, mTransReceiveOrderDetailVos, prePageDate);
                break;
            case R.id.activity_btn_one:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    useOrderCst();
                }
                break;
            case R.id.ly_bind_patient:
                if (UIUtils.getConfigType(mContext, CONFIG_012)) {
                    EventBusUtils.postSticky(new Event.EventButGone(true));//禁止触摸
                    Intent intent = new Intent(mContext, TemPatientBindActivity.class);
                    intent.putExtra("type", "afterBindTemp");
                    intent.putExtra("position", -1000);
                    startActivity(intent);
                } else {
                    loadBingDateNoTemp("", 0, mTbaseDevices);
                }

                break;
        }
    }

    private void initView(boolean isShowPatient) {
        String[] array;

        if (UIUtils.getConfigType(mContext, CONFIG_007)) {
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
        if (!UIUtils.getConfigType(mContext, CONFIG_007)) {
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
        } else {
            ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(5));
            ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(6));
        }
        mHeadView.setBackgroundResource(R.color.bg_green);
        mLinearLayout.removeView(mHeadView);
        if (mPublicAdapter!=null){
            mPublicAdapter.notifyDataSetChanged();
        }else {
            mPublicAdapter = new BillOrderAdapter(mLayout, mSize, mBillOrderResultBean.getCstInventoryVos());

            mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
            mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            mRefreshLayout.setEnableAutoLoadMore(false);
            mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
            mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
            mRecyclerview.setAdapter(mPublicAdapter);
            mLinearLayout.removeAllViews();
            View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
            mPublicAdapter.setEmptyView(inflate);
            mLinearLayout.addView(mHeadView);
        }

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


//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void reciveBillOrderDate(Event.EventBillOrder event) {
//        mPrePageDate = event.orderSheetBean;
//        if (mFindBillOrderBean == null) {
//            mFindBillOrderBean = new FindBillOrderBean();
//            FindBillOrderBean.CstPlanBean cstPlanBean = new FindBillOrderBean.CstPlanBean();
//            cstPlanBean.setId(mPrePageDate.getId());
//            mFindBillOrderBean.setCstPlan(cstPlanBean);
//            mFindBillOrderBean.setCstInventoryVos(new ArrayList<>());
//        }
//        if (mUseCstOrderRequest == null) {
//            mUseCstOrderRequest = new UseCstOrderBean();
//            mUseCstOrderRequest.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
//            mUseCstOrderRequest.setTCstInventoryVos(new ArrayList<>());
//        }
//        if (event.tbaseDevices != null) {
//            mTbaseDevices = event.tbaseDevices;
//        }
//        if (event.transReceiveOrderDetailVosList != null) {
//            mTransReceiveOrderDetailVos = event.transReceiveOrderDetailVosList;
//        }
//        //findBillOrder();
//        //loadDate();
//    }

    /**
     * 根据EPC查询的套组耗材信息
     */
    private void findBillOrder() {
        mFindBillOrderBean.setDeviceCodes(new ArrayList<>());
        for (BoxSizeBean.TbaseDevicesBean item : mTbaseDevices) {
            if (item.getDeviceCode() != null) {
                mFindBillOrderBean.getDeviceCodes().add(item.getDeviceCode());
            }
        }
        LogUtils.i(TAG," mGson.toJson(mFindBillOrderBean)  "+mGson.toJson(mFindBillOrderBean));
        NetRequest.getInstance().findOrderCstListByEpc(mGson.toJson(mFindBillOrderBean), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG,"result   "+result);
                mBillOrderResultBean = mGson.fromJson(result, BillOrderResultBean.class);
                if (mBillOrderResultBean.getErrorEpcs() != null &&
                        mBillOrderResultBean.getErrorEpcs().size() > 0) {
                    String string = StringUtils.listToString(mBillOrderResultBean.getErrorEpcs());
                    ToastUtils.showLong(string);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
                }
                if (mBillOrderResultBean.getCstInventoryVos() == null || mBillOrderResultBean.getCstInventoryVos().size() == 0) {
                    mDownBtnOne.setEnabled(false);
                    Toast.makeText(mContext, "未扫描到操作的耗材,即将返回主界面，请重新操作", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 3000);
                } else {
                    if (mBillOrderResultBean.getMsg() != null) {
                        ToastUtils.showLong(mBillOrderResultBean.getMsg());
                    }
                    if (mPublicAdapter == null) {
                        initView(false);
                    } else {
                        mPublicAdapter.setNewData(mBillOrderResultBean.getCstInventoryVos());
                        mPublicAdapter.notifyDataSetChanged();
                    }
                    mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + mBillOrderResultBean.getCountKind() +
                            "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
                            mBillOrderResultBean.getCountNum() + "</big></font>"));
                    if (UIUtils.getConfigType(mContext, CONFIG_007)){
                        mDownBtnOne.setEnabled(false);
                    }else {
                        mDownBtnOne.setEnabled(true);
                    }
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
        for (BillOrderResultBean.CstInventoryVosBean item : mBillOrderResultBean.getCstInventoryVos()) {
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
            if ("virtual".equals(item.getPatientId())) {
                LogUtils.i(TAG,"EventCheckbox    "+item.getPatientId());
                info.setOperationScheduleId(item.getOperationScheduleId());
                info.setOperatingRoomNoName(item.getOperatingRoomNoName());
                info.setOperatingRoomNo(item.getOperatingRoomNo());
                info.setIdNo(item.getIdNo());
                info.setScheduleDateTime(item.getScheduleDateTime());
                info.setSex(item.getSex());
                info.setIsCreate("" + item.getIsCreate());
                info.setTempPatientId(item.getTempPatientId());
            }
            mUseCstOrderRequest.getTCstInventoryVos().add(info);
        }
        if (mUseCstOrderRequest.getTCstInventoryVos().size() == 0) {
            ToastUtils.showShort("无耗材，无法领用");
            return;
        }
        LogUtils.i(TAG,"JSON  "+mGson.toJson(mUseCstOrderRequest));
        NetRequest.getInstance().useOrderCst(mGson.toJson(mUseCstOrderRequest), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG,"result  "+result);
                UseCstOderResultBean info = mGson.fromJson(result, UseCstOderResultBean.class);
		   List<UseCstOderResultBean.TCstInventoryVosBean> inventoryVos = info.getTCstInventoryVos();
		   EventBusUtils.post(inventoryVos);//用来判断套组是否已经领取

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRvCheckBindEvent(Event.EventCheckbox event) {
        mUseCstOrderRequest.setPatientId(event.id);
        for (BillOrderResultBean.CstInventoryVosBean item : mBillOrderResultBean.getCstInventoryVos()) {
            item.setPatientId(event.id);
            item.setPatientName(event.mString);
            LogUtils.i(TAG,"EventCheckbox    "+event.create);
            if ("virtual".equals(event.id)) {
		   LogUtils.i(TAG,"EventCheckbox    "+event.id);
                item.setOperationScheduleId(event.operationScheduleId);
                item.setOperatingRoomNoName(event.operatingRoomNoName);
                item.setOperatingRoomNo(event.operatingRoomNo);
                item.setIdNo(event.idNo);
                item.setScheduleDateTime(event.scheduleDateTime);
                item.setSex(event.sex);
                item.setIsCreate("" + event.create);
                item.setTempPatientId(event.mTempPatientId);

            }
            if (event.mString!=null){
                mDownBtnOne.setEnabled(true);
            }else {
                mDownBtnOne.setEnabled(false);
            }
        }
//        if ("virtual".equals(event.id)) {
//            UseCstOrderBean.CstTempPatient cstTempPatient = new UseCstOrderBean.CstTempPatient();
//            cstTempPatient.setOperationScheduleId(event.operationScheduleId);
//            cstTempPatient.setTempPatientName(event.mString);
//            cstTempPatient.setOperatingRoomNoName(event.operatingRoomNoName);
//            cstTempPatient.setOperatingRoomNo(event.operatingRoomNo);
//            cstTempPatient.setIdCard(event.idNo);
//            cstTempPatient.setScheduleDateTime(event.scheduleDateTime);
//            cstTempPatient.setSex(event.sex);
//            mUseCstOrderRequest.setCstTempPatient(cstTempPatient);
//        }
        initView(true);
        mPublicAdapter.notifyDataSetChanged();
    }

    int k = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanEPCResult(Event.EventDeviceCallBack event) {
        AllDeviceCallBack.getInstance().initCallBack();
        List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
                .find(BoxIdBean.class);
        for (BoxIdBean boxIdBean : boxIdBeanss) {
            String box_id = boxIdBean.getBox_id();
            if (box_id != null) {
                List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
                        READER_TYPE).find(BoxIdBean.class);
                Log.e("xb", "boxIdBeansss.size" + boxIdBeansss.size());
                if (boxIdBeansss.size() > 1) {
                    for (BoxIdBean BoxIdBean : boxIdBeansss) {
                        LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
                        if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
                            mEPCMapDate.putAll(event.epcs);
                            k++;
                            LogUtils.i(TAG, "mEPCDate   " + mEPCMapDate.size());
                        }
                    }
                    if (k == boxIdBeansss.size()) {
                        LogUtils.i(TAG, "mEPCDate  zou l  ");
                        k = 0;
                        for (Map.Entry<String, List<TagInfo>> v : mEPCMapDate.entrySet()) {
                            FindBillOrderBean.CstInventoryVosBean item = new FindBillOrderBean.CstInventoryVosBean();
                            item.setEpc(v.getKey());
                            if (!mFindBillOrderBean.getCstInventoryVos().contains(item)) {
                                mFindBillOrderBean.getCstInventoryVos().add(item);
                            }
                        }


                            findBillOrder();

                    }
                } else {
                    LogUtils.i(TAG, "event.epcs直接走   " + event.epcs.size());
                    for (Map.Entry<String, List<TagInfo>> v : event.epcs.entrySet()) {
                        FindBillOrderBean.CstInventoryVosBean item = new FindBillOrderBean.CstInventoryVosBean();
                        item.setEpc(v.getKey());
                        if (!mFindBillOrderBean.getCstInventoryVos().contains(item)) {
                            mFindBillOrderBean.getCstInventoryVos().add(item);
                        }
                    }
                        findBillOrder();

                }

            }
        }
    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void scanEPCResult(Event.EventDeviceCallBack event) {
//        mEPCMapDate.clear();
//        mFindBillOrderBean.getCstInventoryVos().clear();
//        mEPCMapDate.putAll(event.epcs);
//        for (Map.Entry<String, List<TagInfo>> v : mEPCMapDate.entrySet()) {
//            FindBillOrderBean.CstInventoryVosBean item = new FindBillOrderBean.CstInventoryVosBean();
//            item.setEpc(v.getKey());
//            mFindBillOrderBean.getCstInventoryVos().add(item);
//        }
//        if (mLoadingDialog != null) {
//            mLoadingDialog.mDialog.dismiss();
//        }
//        if (mFindBillOrderBean.getCstInventoryVos().size() > 0) {
//            findBillOrder();
//        } else {
//            ToastUtils.showShort("耗材扫描失败，请重新扫描");
//        }
//    }

    /**
     * 重新打开柜门
     */
    private void reOpenDoor() {
	 if (mFindBillOrderBean!=null){
           mFindBillOrderBean.getCstInventoryVos().clear();
	 }
        for (String deviceInventoryVo : mEthDeviceIdBack) {
            String deviceCode = deviceInventoryVo;
            LogUtils.i(TAG, "deviceCode    " + deviceCode);
            DeviceManager.getInstance().OpenDoor(deviceCode);
        }
//        if (mTbaseDevices != null && mTbaseDevices.size() > 0) {
//            AllDeviceCallBack.getInstance().openDoor(0, mTbaseDevices);
//        } else {
//            ToastUtils.showShort("无柜子信息!");
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isDoorOpened(Event.HomeNoClickEvent event) {
        if (event.isClick) {
            DialogUtils.showNoDialog(mContext, "柜门已开", 2, "form", null);
        }
    }

    private void startScan(String deviceIndentify) {
        EventBusUtils.postSticky(new Event.EventLoading(true));
        List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
                UHF_TYPE).find(BoxIdBean.class);
        for (BoxIdBean boxIdBean : boxIdBeans) {
            String box_id = boxIdBean.getBox_id();
            List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
                    .find(BoxIdBean.class);

            for (BoxIdBean deviceid : deviceBean) {
                String device_id = deviceid.getDevice_id();
                int i = DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
                LogUtils.i(TAG, "开始扫描了状态    " + i);
            }
        }
    }


}