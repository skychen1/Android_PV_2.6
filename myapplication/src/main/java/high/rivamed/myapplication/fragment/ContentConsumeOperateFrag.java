package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.InOutBoxTwoActivity;
import high.rivamed.myapplication.activity.OutBoxBingActivity;
import high.rivamed.myapplication.activity.OutBoxFoutActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.OutMealActivity;
import high.rivamed.myapplication.activity.PatientConnActivity;
import high.rivamed.myapplication.activity.TemPatientBindActivity;
import high.rivamed.myapplication.adapter.HomeFastOpenAdapter;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.TCstInventoryDto;
import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
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
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;

import static high.rivamed.myapplication.cont.Constants.CONFIG_0011;
import static high.rivamed.myapplication.cont.Constants.CONFIG_0012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_010;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_NAME;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.views.RvDialog.sTableTypeView;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        耗材操作主界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ContentConsumeOperateFrag extends BaseSimpleFragment {

    String TAG = "ContentConsumeOperateFrag";
    @BindView(R.id.consume_openall_rv)
    RecyclerView mConsumeOpenallRv;
    @BindView(R.id.consume_openall_top)
    LinearLayout mConsumeOpenallTop;
    @BindView(R.id.function_title_meal)
    TextView mFunctionTitleMeal;
    @BindView(R.id.function_cardview_meal)
    CardView mFunctionCardviewMeal;
    @BindView(R.id.fastopen_title_form)
    TextView mFastopenTitleForm;
    @BindView(R.id.function_cardview_form)
    CardView mFunctionCardviewForm;
    @BindView(R.id.consume_openall_middle)
    LinearLayout mConsumeOpenallMiddle;
    @BindView(R.id.content_rb_ly)
    RadioButton mContentRbLy;
    @BindView(R.id.content_rb_rk)
    RadioButton mContentRbRk;
    @BindView(R.id.content_rb_yc)
    RadioButton mContentRbYc;
    @BindView(R.id.content_rb_tb)
    RadioButton mContentRbTb;
    @BindView(R.id.content_rb_yr)
    RadioButton mContentRbYr;
    @BindView(R.id.content_rb_tuihui)
    RadioButton mContentRbTuihui;
    @BindView(R.id.content_rb_tuihuo)
    RadioButton mContentRbTuihuo;
    @BindView(R.id.content_rg)
    RadioGroup mContentRg;
    @BindView(R.id.consume_down_rv)
    RecyclerView mConsumeDownRv;
    @BindView(R.id.consume_down)
    LinearLayout mConsumeDown;
    @BindView(R.id.fastopen_title_guanlian)
    TextView mFastopenTitleGuanlian;
    @BindView(R.id.function_cardview_guanlian)
    CardView mFunctionCardviewGuanlian;
    Unbinder unbinder;
    private LoadingDialog.Builder mShowLoading;
    private HomeFastOpenAdapter mHomeFastOpenTopAdapter;
    private HomeFastOpenAdapter mHomeFastOpenDownAdapter;

    private String eth002DeviceId;
    private String uhfDeviceId;
    private NoDialog.Builder mBuilder;
    private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;
    //   private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices2;
    private HashMap<String, String> mReaderMap;

    public static List<String> mReaderIdList;
    private int mRbKey;
    private BoxSizeBean mBoxSizeBean;
    private BoxSizeBean mBoxSizeBean2;
    private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos;
    private String mRvEventString;
    private String mPatientName;
    private String mPatientId;
    private String mFirstBind;
    private String mDeviceCode;
    private RvDialog.Builder mShowRvDialog;
    private Handler mHandler;
    private String mOppenDoor = null;
    private List<String> mEthDeviceId;
    private TCstInventoryDto mTCstInventoryDtoAll = new TCstInventoryDto();
    ;
    public static boolean mPause = true;
    private int mPosition;
    private List<BoxSizeBean.TbaseDevicesBean> mTbaseDevicesFromEvent;
    private RvDialog.Builder mShowRvDialog2;
    private String mOperationScheduleId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDialogEvent(Event.PopupEvent event) {
        if (event.isMute) {
            if (mBuilder == null) {
                mBuilder = DialogUtils.showNoDialog(mContext, event.mString, 2, "in", null);
            }
        } else {
            if (mBuilder != null) {
                mBuilder.mDialog.dismiss();
                mBuilder = null;

            }
        }
    }

    /**
     * 扫描后EPC准备传值
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallBackEvent(Event.EventDeviceCallBack event) {
        LogUtils.i(TAG, "EventDeviceCallBack   " + mPause);
        if (!mPause) {
            getDeviceDate(event.deviceId, event.epcs);
        }
    }

    //   @Override
    //   public void onResume() {
    //	mPause = false;
    //	super.onResume();
    //   }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOppenDoorEvent(Event.EventOppenDoor event) {
        mOppenDoor = event.mString;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBooleanEvent(Event.EventBoolean event) {
        //
        if (event.mBoolean) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.i(TAG, "EventBoolean   " + mOppenDoor);
                    if (mOppenDoor == null) {
                        LogUtils.i(TAG, "EventBoolean  进来1");
                        DeviceManager.getInstance().UnRegisterDeviceCallBack();
                        AllDeviceCallBack.getInstance().initCallBack();
                        //			DeviceManager.getInstance().OpenDoor(event.mId);
                    } else {
                        //			DeviceManager.getInstance().UnRegisterDeviceCallBack();
                        //			AllDeviceCallBack.getInstance().initCallBack();
                        LogUtils.i(TAG, "EventBoolean  进来2");
                        mOppenDoor = null;
                    }
                }
            }).start();

        }
    }

    /**
     * 重新加载数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStartFrag(Event.EventFrag event) {
        if (event.type.equals("START1")) {
            //            mPause = false;
            //	   AllDeviceCallBack.getInstance().initCallBack();
            initData();
        } else {
            //            mPause = true;
            LogUtils.i(TAG, "UnRegisterDeviceCallBack");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRvEvent(Event.EventString event) {
        mRvEventString = event.mString;
        LogUtils.i(TAG, "mRvEventString   " + mRvEventString);
        loadBingDate(mRvEventString, -2, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToast(Event.EventToast event) {
        Toast.makeText(mContext, event.mString, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRvCheckBindEvent(Event.EventCheckbox event) {
        mFirstBind = event.type;
        mOperationScheduleId = event.operationScheduleId;
        if (event.type.equals("firstBind")) {
            mPatientName = event.mString;
            mPatientId = event.id;
            LogUtils.i(TAG, "mPatientName   " + mPatientName);
            LogUtils.i(TAG, "mPatientId   " + mPatientId);
            mPosition = event.position;
            mTbaseDevicesFromEvent = event.mTbaseDevices;
            AllDeviceCallBack.getInstance().openDoor(mPosition, mTbaseDevicesFromEvent);
        } else {
            String type = event.type;
            String mString = event.mString;
            int position = event.position;
            List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices = event.mTbaseDevices;
            mRvEventString = event.mString;
            loadBingDate(mRvEventString, -1, null);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onActString(Event.EventAct event) {
        if (event.mString.equals("RecognizeActivity")) {
            //识别耗材重新扫描回调
            LogUtils.i(TAG, "RecognizeActivity  重新扫描 ");
            AllDeviceCallBack.getInstance().openDoor(mPosition, mTbaseDevicesFromEvent);
        }

    }

    public static ContentConsumeOperateFrag newInstance() {

        Bundle args = new Bundle();
        ContentConsumeOperateFrag fragment = new ContentConsumeOperateFrag();
        fragment.setArguments(args);
        LogUtils.i("ContentConsumeOperateFrag", "newInstance");
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.ctconsumeoperate_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mPause = false;
        EventBusUtils.register(this);
        //	mShowLoading = DialogUtils.showLoading(mContext);
        LogUtils.i(TAG, "initDataAndEvent");
        //	initCallBack();
        AllDeviceCallBack.getInstance().initCallBack();
        mContentRbTb.setVisibility(View.GONE);
        initData();
        mFastopenTitleGuanlian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UIUtils.isFastDoubleClick())
                    return;
                goToPatientConn();

            }
        });
    }

    private void goToPatientConn() {
        NetRequest.getInstance().findTempPatients("", this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
                    mContext.startActivity(new Intent(mContext, PatientConnActivity.class));
                } else {
                    ToastUtils.showShort("没有患者数据");
                }
            }
        });
    }

    /**
     * 扫描后传值
     */
    private void getDeviceDate(String deviceId, Map<String, List<TagInfo>> epcs) {
        TCstInventoryDto tCstInventoryDto = new TCstInventoryDto();
        List<TCstInventory> epcList = new ArrayList<>();

        for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
            TCstInventory tCstInventory = new TCstInventory();
            tCstInventory.setEpc(v.getKey());
            epcList.add(tCstInventory);
        }

        DeviceInventoryVo deviceInventoryVo = new DeviceInventoryVo();
        List<DeviceInventoryVo> deviceList = new ArrayList<>();

        List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", deviceId).find(BoxIdBean.class);
        for (BoxIdBean boxIdBean : boxIdBeans) {
            String box_id = boxIdBean.getBox_id();
            Log.i(TAG, "device_id   " + box_id);
            if (box_id != null) {
                deviceInventoryVo.setDeviceCode(box_id);
            }
        }
        deviceInventoryVo.settCstInventories(epcList);
        deviceList.add(deviceInventoryVo);

        tCstInventoryDto.setThingCode(SPUtils.getString(mContext, THING_CODE));
        tCstInventoryDto.setDeviceInventoryVos(deviceList);
        tCstInventoryDto.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
        LogUtils.i(TAG, "mRbKey    " + mRbKey);
        if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 || mRbKey == 10 ||
                mRbKey == 7 || mRbKey == 8) {
            tCstInventoryDto.setOperation(mRbKey);
        } else {
            tCstInventoryDto.setOperation(mRbKey);
        }
        if (mFirstBind != null && mFirstBind.equals("firstBind") && mRbKey == 3) {
            tCstInventoryDto.setPatientName(mPatientName);
            tCstInventoryDto.setPatientId(mPatientId);
            tCstInventoryDto.setOperationScheduleId(mOperationScheduleId);
        }
        String toJson = mGson.toJson(tCstInventoryDto);
        LogUtils.i(TAG, "toJson    " + toJson);

        NetRequest.getInstance().putEPCDate(toJson, _mActivity, mShowLoading, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                Log.i(TAG, "result    " + result);
                TCstInventoryDto cstInventoryDto = mGson.fromJson(result, TCstInventoryDto.class);
                String string = null;
                if (cstInventoryDto.getErrorEpcs() != null &&
                        cstInventoryDto.getErrorEpcs().size() > 0) {
                    string = StringUtils.listToString(cstInventoryDto.getErrorEpcs());
                    ToastUtils.showLong(string);
                    return;
                }
                LogUtils.i(TAG, "我跳转    " + (cstInventoryDto.gettCstInventoryVos() == null));
                //先绑定患者
                if (mFirstBind != null && mFirstBind.equals("firstBind") && mRbKey == 3) {
                    if (cstInventoryDto.gettCstInventoryVos() != null &&
                            cstInventoryDto.gettCstInventoryVos().size() != 0) {
                        for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {
                            tCstInventoryVo.setPatientName(cstInventoryDto.getPatientName());
                            tCstInventoryVo.setPatientId(cstInventoryDto.getPatientId());
                            tCstInventoryVo.setOperationScheduleId(cstInventoryDto.getOperationScheduleId());
                        }
                        cstInventoryDto.setBindType("firstBind");
                        //                        mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
                        mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class).putExtra("patientName", cstInventoryDto.getPatientName()).putExtra("patientId", cstInventoryDto.getPatientId()).putExtra("operationScheduleId", cstInventoryDto.getOperationScheduleId()));
                        EventBusUtils.postSticky(cstInventoryDto);
                    } else {
                        Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
                    }
                } else if (UIUtils.getConfigType(mContext, CONFIG_009) && mRbKey == 3) {//后绑定患者
                    if (cstInventoryDto.gettCstInventoryVos() != null &&
                            cstInventoryDto.gettCstInventoryVos().size() != 0) {
                        for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {
                            tCstInventoryVo.setPatientName(cstInventoryDto.getPatientName());
                            tCstInventoryVo.setPatientId(cstInventoryDto.getPatientId());
                            tCstInventoryVo.setOperationScheduleId(cstInventoryDto.getOperationScheduleId());
                        }
                        cstInventoryDto.setOperation(mRbKey);
                        cstInventoryDto.setBindType("afterBind");
                        mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
                        EventBusUtils.postSticky(cstInventoryDto);
                    } else {
                        Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
                    }
                } else {//正常的领用或者其他正常操作
                    //		   mShowLoading.mDialog.dismiss();
                    if (cstInventoryDto.gettCstInventoryVos() == null ||
                            cstInventoryDto.gettCstInventoryVos().size() < 1) {
                        if (mBuilder != null) {
                            mBuilder.mDialog.dismiss();
                        }
                        Toast.makeText(mContext, "未扫描到操作的耗材", Toast.LENGTH_SHORT).show();
                    } else {
                        LogUtils.i(TAG, "我跳转    " + cstInventoryDto.getType());
                        EventBusUtils.post(new Event.PopupEvent(false, "关闭"));
                        if (cstInventoryDto.getType() == 0) {//放入
                            if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 ||
                                    mRbKey == 10 || mRbKey == 7 || mRbKey == 8) {
                                Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
                                mContext.startActivity(intent2);
                                EventBusUtils.postSticky(new Event.EventAct("inout"));
                                cstInventoryDto.setOperation(mRbKey);
                                EventBusUtils.postSticky(cstInventoryDto);
                            } else {
                                Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
                                mContext.startActivity(intent2);
                                EventBusUtils.postSticky(new Event.EventAct("all"));
                                EventBusUtils.postSticky(cstInventoryDto);
                            }
                        } else {//拿出
                            if (mRbKey == 3 || mRbKey == 2 || mRbKey == 9 || mRbKey == 11 ||
                                    mRbKey == 10 || mRbKey == 7 || mRbKey == 8) {
                                Intent intent2 = new Intent(mContext, InOutBoxTwoActivity.class);
                                mContext.startActivity(intent2);
                                EventBusUtils.postSticky(new Event.EventAct("inout"));
                                cstInventoryDto.setOperation(mRbKey);
                                EventBusUtils.postSticky(cstInventoryDto);
                            } else {
                                mContext.startActivity(new Intent(mContext, OutBoxFoutActivity.class));
                                EventBusUtils.postSticky(cstInventoryDto);
                            }
                        }
                    }
                }
            }
        });
    }

    private void initData() {
        if (UIUtils.getConfigType(mContext, CONFIG_0011)) {
            mConsumeOpenallTop.setVisibility(View.VISIBLE);
            mConsumeOpenallMiddle.setVisibility(View.VISIBLE);
        } else {
            mConsumeOpenallTop.setVisibility(View.GONE);
            mConsumeOpenallMiddle.setVisibility(View.GONE);
        }
        mConsumeOpenallMiddle.setVisibility(View.VISIBLE);//todo 修改
        loadDate();

    }

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
                onSucceedDate();
            }
        });
    }

    //赋值
    private void onSucceedDate() {
        LogUtils.i(TAG, "onSucceedDate");
        //	mConsumeOpenallMiddle.setVisibility(View.GONE);//此处部分医院不需要可以隐藏  根据接口来
        mBaseTabBtnLeft.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("耗材操作");
        mBaseTabBtnLeft.setText(SPUtils.getString(mContext, SAVE_DEPT_NAME));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mHomeFastOpenTopAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
                mTbaseDevices);
        mConsumeOpenallRv.setLayoutManager(layoutManager);
        mConsumeOpenallRv.setAdapter(mHomeFastOpenTopAdapter);

        mHomeFastOpenTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mRbKey = -1;
                AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);

                // else if (position == 1){
                //		   DialogUtils.showNoDialog(mContext, title, 2,"out",null);
                //		}else if (position ==2){
                //		   DialogUtils.showNoDialog(mContext, title, 2,"out","bing");
                //		}else if (position == 3){
                //		   ToastUtils.showShort("按套餐领用-绑定患者！");
                //		   mContext.startActivity(new Intent(mContext,OutMealActivity.class));
                //		   EventBusUtils.postSticky(new Event.EventAct("BING_MEAL"));
                //
                //		}else if (position ==4){
                //
                //		}

                //		if (position == 0) {
                //		   int mType = 1;//1.8.3未绑定
                //		   showTwoDialog(mType);
                //		} else if (position == 1) {
                //		   int mType = 1;//1.6移出
                //		   showStoreDialog(3, mType);
                //		} else if (position == 2) {
                //		   int mType = 2;//1.7退货
                //		   showStoreDialog(2, mType);
                //		} else if (position == 3) {
                //		   int mType = 3;//1.8调拨
                //		   showStoreDialog(2, mType);
                //		} else if (position == 4) {
                //		   int mType = 2;//1.9.3请领单
                //		   showTwoDialog(mType);
                //		} else if (position == 5) {//1.2错误
                //		   String title = "耗材中包含过期耗材，请查看！";
                //		   showNoDialog(title, 1);
                //		} else if (position == 6) {//1.8.1选择患者
                //		   showRvDialog();
                //		} else {
                //		   String title = "柜门已开";
                //		   showNoDialog(title, 2);
                //		}
            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        mHomeFastOpenDownAdapter = new HomeFastOpenAdapter(R.layout.item_home_fastopen_layout,
                mTbaseDevices);
        mConsumeDownRv.setLayoutManager(layoutManager2);
        mConsumeDownRv.setAdapter(mHomeFastOpenDownAdapter);
        mHomeFastOpenDownAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int id = mContentRg.getCheckedRadioButtonId();
                if (id == -1) {
                    ToastUtils.showShort("请选择操作方式！");
                } else {
                    mPause = false;
                    switch (id) {
                        case R.id.content_rb_ly:
                            mRbKey = 3;
                            ToastUtils.showShort("领用！");//拿出
                            if (UIUtils.getConfigType(mContext, CONFIG_007) &&
                                    UIUtils.getConfigType(mContext, CONFIG_010) && !UIUtils.getConfigType(mContext, CONFIG_0012)) {
                                //先绑定患者再开柜，不启动临时患者
                                LogUtils.i(TAG, "先绑定患者再开柜，不启动临时患者");
                                //                                loadBingDate("", position, mTbaseDevices);
                                loadBingDateNoTemp("", position, mTbaseDevices);
                            } else if (UIUtils.getConfigType(mContext, CONFIG_007) &&
                                    UIUtils.getConfigType(mContext, CONFIG_010) &&
                                    UIUtils.getConfigType(mContext, CONFIG_0012)) {
                                //先绑定患者，启动临时患者
                                LogUtils.i(TAG, "先绑定患者，启动临时患者");
                                //				mContext.startActivity(
                                //					new Intent(mContext, TemPatientBindActivity.class).putExtra(
                                //						"mTemPTbaseDevices", (Serializable) mTbaseDevices));
                                goToFirstBindAC(position);
                            } else if (UIUtils.getConfigType(mContext, CONFIG_007) &&
                                    UIUtils.getConfigType(mContext, CONFIG_009) && !UIUtils.getConfigType(mContext, CONFIG_0012)) {
                                //后绑定患者，不启用临时患者
                                LogUtils.i(TAG, "后绑定患者，不启用临时患者");
                                AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);

                            } else if (UIUtils.getConfigType(mContext, CONFIG_007) &&
                                    UIUtils.getConfigType(mContext, CONFIG_009) &&
                                    UIUtils.getConfigType(mContext, CONFIG_0012)) {
                                //后绑定患者，启用临时患者
                                AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                                LogUtils.i(TAG, "后绑定患者，启用临时患者");
                            } else {
                                //不绑定患者
                                LogUtils.i(TAG, "不绑定患者");
                                AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            }
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            //			   loadBingDate("", position, mTbaseDevices);


                            break;
                        case R.id.content_rb_rk:
                            mRbKey = 2;
                            ToastUtils.showShort("入库！");//拿入
                            AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            break;
                        case R.id.content_rb_yc:
                            mRbKey = 9;
                            ToastUtils.showShort("移出！");//拿出
                            AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            break;
                        case R.id.content_rb_tb:
                            mRbKey = 11;
                            ToastUtils.showShort("调拨！");//拿出
                            AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            break;
                        case R.id.content_rb_yr:
                            mRbKey = 10;
                            ToastUtils.showShort("移入！");//拿入
                            AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            break;
                        case R.id.content_rb_tuihui:
                            mRbKey = 7;
                            ToastUtils.showShort("退回！");//拿入
                            AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            break;
                        case R.id.content_rb_tuihuo:
                            mRbKey = 8;
                            ToastUtils.showShort("退货！");//拿出
                            AllDeviceCallBack.getInstance().openDoor(position, mTbaseDevices);
                            EventBusUtils.postSticky(new Event.EventAct("inout"));
                            break;
                    }
                }
            }
        });
    }

    private void goToFirstBindAC(int position) {
        //获取需要绑定的患者
        NetRequest.getInstance().findSchedulesDate("", this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
                    mContext.startActivity(new Intent(mContext, TemPatientBindActivity.class).putExtra("position", position).putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices));
                } else {
                    ToastUtils.showShort("没有患者数据");
                }
            }
        });
    }


    @Override
    public void onPause() {
        mPause = true;
        super.onPause();

    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
            R.id.function_title_meal, R.id.fastopen_title_form})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_tab_icon_right:
            case R.id.base_tab_tv_name:
                mPopupWindow = new SettingPopupWindow(mContext);
                mPopupWindow.showPopupWindow(mBaseTabIconRight);
                popupClick();
                break;
            case R.id.base_tab_btn_msg:
                break;
            case R.id.function_title_meal:
                if (UIUtils.getConfigType(mContext, CONFIG_007) &&
                        UIUtils.getConfigType(mContext, CONFIG_010)) {
                    mPause = false;
                    mContext.startActivity(new Intent(mContext, OutMealActivity.class));
                    EventBusUtils.postSticky(new Event.EventAct("NOBING_MEAL"));
                } else {
                    ToastUtils.showShort("此功能暂未开放");
                }

                break;
            case R.id.fastopen_title_form:
                if (UIUtils.getConfigType(mContext, CONFIG_007) &&
                        UIUtils.getConfigType(mContext, CONFIG_010)) {
                    mPause = false;
                    mContext.startActivity(new Intent(mContext, OutFormActivity.class));
                } else {
                    ToastUtils.showShort("此功能暂未开放");
                    //		   DialogUtils.showRegisteDialog(mContext, _mActivity);
                }

                break;
            case R.id.fastopen_title_guanlian://患者关联
                mPause = false;
                //                mContext.startActivity(new Intent(mContext, PatientConnActivity.class));
                break;
        }
    }

    /**
     * 获取需要绑定的患者
     */
    private void loadBingDate(
            String optienNameOrId, int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
        LogUtils.i(TAG, "optienNameOrId   " + optienNameOrId);
        NetRequest.getInstance().findSchedulesDate(optienNameOrId, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result   " + result);
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                if (mPatientInfos != null) {
                    mPatientInfos.clear();
                    List<BingFindSchedulesBean.PatientInfosBean> patientInfos = bingFindSchedulesBean.getPatientInfos();
                    mPatientInfos.addAll(patientInfos);
                    if (mShowRvDialog.mDialog.isShowing()) {
                        sTableTypeView.mBingOutAdapter.notifyDataSetChanged();
                    } else {
                        if (!mPause) {
                            mShowRvDialog = DialogUtils.showRvDialog(_mActivity, mContext, mPatientInfos,
                                    "firstBind", position, mTbaseDevices);
                        }
                    }

                } else {
                    if (!mPause) {
                        mPatientInfos = bingFindSchedulesBean.getPatientInfos();
                        mShowRvDialog = DialogUtils.showRvDialog(_mActivity, mContext, mPatientInfos,
                                "firstBind", position, mTbaseDevices);
                    }
                }
            }
        });
    }

    /**
     * 获取需要绑定的患者（不包含临时患者）
     */
    private void loadBingDateNoTemp(
            String optienNameOrId, int position, List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
        LogUtils.i(TAG, "optienNameOrId   " + optienNameOrId);
        NetRequest.getInstance().findSchedulesDateNoTemp(optienNameOrId, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result   " + result);
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null && bingFindSchedulesBean.getPatientInfos().size() > 0) {
                    if (mPatientInfos != null) {
                        mPatientInfos.clear();
                        List<BingFindSchedulesBean.PatientInfosBean> patientInfos = bingFindSchedulesBean.getPatientInfos();
                        mPatientInfos.addAll(patientInfos);
                        if (mShowRvDialog2 != null && mShowRvDialog2.mDialog.isShowing()) {
                            sTableTypeView.mBingOutAdapter.notifyDataSetChanged();
                        } else {
                            mShowRvDialog2 = DialogUtils.showRvDialog(_mActivity, mContext, mPatientInfos,
                                    "firstBind", position, mTbaseDevices);
                        }

                    } else {
                        mPatientInfos = bingFindSchedulesBean.getPatientInfos();
                        mShowRvDialog2 = DialogUtils.showRvDialog(_mActivity, mContext, mPatientInfos,
                                "firstBind", position, mTbaseDevices);
                    }
                } else {
                    ToastUtils.showShort("没有患者数据");
                }
            }
        });
    }


}
