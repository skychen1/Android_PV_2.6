package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
import high.rivamed.myapplication.adapter.OutFormConfirmAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.OutFormConfirmResultBean;
import high.rivamed.myapplication.bean.OutFromConfirmRequestBean;
import high.rivamed.myapplication.bean.SureReciveOrder;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_RECEIVE_ORDERID;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/29 17:41
 * 描述:       请领单确认领用界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutFormConfirmActivity extends BaseSimpleActivity {
    private static final String TAG = "BaseTimelyActivity";
    public int mSize;
    //重新扫描
    @BindView(R.id.timely_start_btn)
    public TextView mTimelyStartBtn;
    //打开柜门
    @BindView(R.id.timely_open_door)
    public TextView mTimelyOpenDoor;
    //查看医嘱清单
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
    public TableTypeView mTypeView;
    List<String> titeleList = null;

    public List<TCstInventoryVo> mTCstInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息
    /**
     * 根据EPC请求网络参数
     */
    public OutFromConfirmRequestBean mOutFromConfirmRequestBean;
    /**
     * 确认领用使用参数
     */
    private OrderSheetBean.RowsBean mPrePageDate;

    /**
     * 所有耗材列表
     */
    private List<OutFormConfirmResultBean.TcstInventoryOrderVosBean> mTransReceiveOrderDetailVosAllList;
    /**
     * 柜子信息
     */
    private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;
    List<BillStockResultBean.TransReceiveOrderDetailVosBean> mTransReceiveOrderDetailVosBean;
    private OutFormConfirmResultBean mAllOutFormConfirmRequest;
    private LoadingDialog.Builder mLoadingDialog;
    private int mLayout;
    private View mHeadView;
    private OutFormConfirmAdapter mPublicAdapter;
    public SparseBooleanArray mCheckStates = new SparseBooleanArray();
    /**
     * 传输的EPC
     *
     * @param event
     */
    private Map<String, List<TagInfo>> mEPCMapDate = new TreeMap<>();

    private static boolean mIsFirst = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_timely_layout;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLoadingDialog = DialogUtils.showLoading(mContext);
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mIsFirst = true;
        EventBusUtils.register(this);
        initView();
    }

    private void initView() {
        mBaseTabBack.setVisibility(View.GONE);
        mBaseTabIconRight.setEnabled(false);
        mBaseTabTvName.setEnabled(false);
        mBaseTabOutLogin.setEnabled(false);
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

        if (mOutFromConfirmRequestBean == null) {
            mOutFromConfirmRequestBean = new OutFromConfirmRequestBean();
            mOutFromConfirmRequestBean.setEpcs(new ArrayList<>());
            mOutFromConfirmRequestBean.setDeviceCodes(new ArrayList<>());

        }
        if (mTbaseDevices != null) {
            for (BoxSizeBean.TbaseDevicesBean item : mTbaseDevices) {
                if (!mOutFromConfirmRequestBean.getDeviceCodes().contains(item.getDeviceCode())) {
                    mOutFromConfirmRequestBean.getDeviceCodes().add(item.getDeviceCode());
                }
            }
        }
        if (mTransReceiveOrderDetailVosAllList == null) {
            mTransReceiveOrderDetailVosAllList = new ArrayList<>();
        }
        mAllOutFormConfirmRequest = new OutFormConfirmResultBean();
        mBaseTabTvTitle.setText("套餐领用耗材");
        mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
                "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
                0 + "</big></font>"));
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
        mDownBtnOne.setBackgroundResource(R.drawable.bg_btn_gray_nor3);
    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn, R.id.activity_btn_one, R.id.ly_bing_btn, R.id.timely_open_door})
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
            case R.id.timely_start_btn:
                mEPCMapDate.clear();
                mOutFromConfirmRequestBean.getEpcs().clear();
                mTransReceiveOrderDetailVosAllList.clear();
                mLoadingDialog = DialogUtils.showLoading(mContext);
                for (String deviceInventoryVo : mEthDeviceIdBack) {
                    String deviceCode = deviceInventoryVo;
                    LogUtils.i(TAG, "deviceCode    " + deviceCode);
                    startScan(deviceCode);
                }
                break;
            case R.id.timely_open_door:
                reOpenDoor();
                break;
            case R.id.activity_btn_one:
                sureTransReceiveOrder();
                break;
            case R.id.ly_bing_btn:
                DialogUtils.showLookUpDetailedListDialog(mContext, true, mTransReceiveOrderDetailVosBean, mPrePageDate);
                break;
        }
    }

    private void initData() {
        mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + mAllOutFormConfirmRequest.getCstTypes() +
                "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
                mAllOutFormConfirmRequest.getCstCount() + "</big></font>"));
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
        mPublicAdapter = new OutFormConfirmAdapter(mLayout, mTransReceiveOrderDetailVosAllList);
        mHeadView.setBackgroundResource(R.color.bg_green);

        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRecyclerview.setAdapter(mPublicAdapter);
        mLinearLayout.removeView(mHeadView);
        View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
        mPublicAdapter.setEmptyView(inflate);
        mLinearLayout.addView(mHeadView);
        mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //TODO 10.23 选择要打开的耗材柜弹出框
                // DialogUtils.showSelectOpenCabinetDialog(mContext, genData6());
            }
        });
    }

    private void getBillStockByEpc(OutFromConfirmRequestBean outFromConfirmRequestBean) {
        Log.e("xb", "********getBillStockByEpc*****");
        mTransReceiveOrderDetailVosAllList.clear();
        for (BoxSizeBean.TbaseDevicesBean item : mTbaseDevices) {
            if (!mOutFromConfirmRequestBean.getDeviceCodes().contains(item.getDeviceCode()))
                mOutFromConfirmRequestBean.getDeviceCodes().add(item.getDeviceCode());
        }
        mOutFromConfirmRequestBean.setTransReceiveOrderDetailVos(mTransReceiveOrderDetailVosBean);
        NetRequest.getInstance().findBillStockByEpc(mGson.toJson(outFromConfirmRequestBean), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "getBillStockByEpc   " + result);
                OutFormConfirmResultBean outFormConfirmResultBean = mGson.fromJson(result, OutFormConfirmResultBean.class);
                if (outFormConfirmResultBean.getTcstInventoryOrderVos() != null) {
                    mTransReceiveOrderDetailVosAllList.addAll(outFormConfirmResultBean.getTcstInventoryOrderVos());
                }
                mAllOutFormConfirmRequest = outFormConfirmResultBean;
                mAllOutFormConfirmRequest.setTransReceiveOrder(mPrePageDate);
                boolean isCanUse = true;
                //是否可以点击领用按钮
                for (OutFormConfirmResultBean.TcstInventoryOrderVosBean item : mTransReceiveOrderDetailVosAllList) {
                    if (!item.isIsContain()) {
                        isCanUse = false;
                        break;
                    }
                }
                if (isCanUse) {
                    mDownBtnOne.setEnabled(true);
                } else {
                    mDownBtnOne.setEnabled(false);
                }
                initData();
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }

    private void sureTransReceiveOrder() {

        NetRequest.getInstance().sureReceiveOrder(mGson.toJson(mAllOutFormConfirmRequest), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "getBillStockByEpc   " + result);
                SureReciveOrder sureReciveOrder = mGson.fromJson(result, SureReciveOrder.class);
                SPUtils.putString(mContext, SAVE_RECEIVE_ORDERID, "" + mPrePageDate.getId());
                if (sureReciveOrder.isOperateSuccess()) {
                    if (!sureReciveOrder.getMsg().contains("全部")) {
                        DialogUtils.showTwoDialog(mContext, 2, "耗材领用成功", sureReciveOrder.getMsg());
                    } else {
                        DialogUtils.showTwoDialog(mContext, 1, "耗材领用成功", sureReciveOrder.getMsg());
                    }
                } else {
                    ToastUtils.showShort(sureReciveOrder.getMsg());
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Erorr：" + result);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void reciveBillStockDate(Event.EventBillStock event) {
        if (mIsFirst) {
            if (mTransReceiveOrderDetailVosAllList == null) {
                mTransReceiveOrderDetailVosAllList = new ArrayList<>();
            }
            if (mOutFromConfirmRequestBean == null) {
                mOutFromConfirmRequestBean = new OutFromConfirmRequestBean();
                mOutFromConfirmRequestBean.setEpcs(new ArrayList<>());
                mOutFromConfirmRequestBean.setDeviceCodes(new ArrayList<>());
                for (BoxSizeBean.TbaseDevicesBean item : event.tbaseDevices) {
                    mOutFromConfirmRequestBean.getDeviceCodes().add(item.getDeviceCode());
                }
            } else {
                for (BoxSizeBean.TbaseDevicesBean item : event.tbaseDevices) {
                    mOutFromConfirmRequestBean.getDeviceCodes().add(item.getDeviceCode());
                }
            }
            mPrePageDate = event.orderSheetBean;
            mTbaseDevices = event.tbaseDevices;
            mOutFromConfirmRequestBean.setTransReceiveOrderDetailVos(event.transReceiveOrderDetailVosList);
            mTransReceiveOrderDetailVosBean = event.transReceiveOrderDetailVosList;
            mIsFirst = false;
        }

    }

    private int k;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void scanEPCResult(Event.EventDeviceCallBack event) {
        Log.e("xb", "scanEPCResult");
        if (mOutFromConfirmRequestBean == null) {
            mOutFromConfirmRequestBean = new OutFromConfirmRequestBean();
            mOutFromConfirmRequestBean.setEpcs(new ArrayList<>());
            mOutFromConfirmRequestBean.setDeviceCodes(new ArrayList<>());
            for (BoxSizeBean.TbaseDevicesBean item : mTbaseDevices) {
                if (!mOutFromConfirmRequestBean.getDeviceCodes().contains(item.getDeviceCode())) {
                    mOutFromConfirmRequestBean.getDeviceCodes().add(item.getDeviceCode());
                }
            }
        }
        AllDeviceCallBack.getInstance().initCallBack();
        List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
                .find(BoxIdBean.class);
        for (BoxIdBean boxIdBean : boxIdBeanss) {
            String box_id = boxIdBean.getBox_id();
            if (box_id != null) {
                List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
                        READER_TYPE).find(BoxIdBean.class);
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
                            if (!mOutFromConfirmRequestBean.getEpcs().equals(v.getKey())) {
                                mOutFromConfirmRequestBean.getEpcs().add(v.getKey());
                            }
                        }
                        if (mLoadingDialog != null) {
                            mLoadingDialog.mDialog.dismiss();
                        }
                        if (mOutFromConfirmRequestBean.getEpcs().size() > 0) {
                            Log.e("xb", "getBillStockByEpc1");
                            getBillStockByEpc(mOutFromConfirmRequestBean);
                        } else {
                            ToastUtils.showShort("耗材扫描失败，请重新扫描");
                        }
                    }
                } else {
                    LogUtils.i(TAG, "event.epcs直接走   " + event.epcs);
                    for (Map.Entry<String, List<TagInfo>> v : event.epcs.entrySet()) {
                        if (!mOutFromConfirmRequestBean.getEpcs().equals(v.getKey())) {
                            mOutFromConfirmRequestBean.getEpcs().add(v.getKey());
                        }
                    }
                    if (mOutFromConfirmRequestBean.getEpcs().size() > 0) {
                        Log.e("xb", "getBillStockByEpc2");
                        getBillStockByEpc(mOutFromConfirmRequestBean);
                    } else {
                        ToastUtils.showShort("耗材扫描失败，请重新扫描");
                    }
                }

            }
        }

    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void scanEPCResult(Event.EventDeviceCallBack event) {
//        if (mOutFromConfirmRequestBean == null) {
//            mOutFromConfirmRequestBean = new OutFromConfirmRequestBean();
//            mOutFromConfirmRequestBean.setEpcs(new ArrayList<>());
//        }
//        mEPCMapDate.putAll(event.epcs);
//        mOutFromConfirmRequestBean.getEpcs().clear();
//        for (Map.Entry<String, List<TagInfo>> v : mEPCMapDate.entrySet()) {
//            mOutFromConfirmRequestBean.getEpcs().add(v.getKey());
//        }
//        if (mLoadingDialog != null) {
//            mLoadingDialog.mDialog.dismiss();
//        }
//        if (mOutFromConfirmRequestBean.getEpcs().size() > 0) {
//            getBillStockByEpc(mOutFromConfirmRequestBean);
//        } else {
//            ToastUtils.showShort("耗材扫描失败，请重新扫描");
//        }
//    }

    /**
     * 重新打开柜门
     */
    private void reOpenDoor() {
        if (mTbaseDevices != null && mTbaseDevices.size() > 0) {
            AllDeviceCallBack.getInstance().openDoor(0, mTbaseDevices);
        } else {
            ToastUtils.showShort("无柜子信息!");
        }
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
                int i = DeviceManager.getInstance().StartUhfScan(device_id, 3000);
                LogUtils.i(TAG, "开始扫描了状态    " + i);
            }
        }
    }

}
