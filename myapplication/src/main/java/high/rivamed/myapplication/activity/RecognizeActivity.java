package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
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
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_RECEIVE;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

/*
 识别耗材页面
* */
public class RecognizeActivity extends BaseTimelyActivity {

    private CountDownTimer mStart;
    private int                                          mIntentType;
    private static final String TAG = "RecognizeActivity";
    private String mRvEventString;
    private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos = new ArrayList<>();
    private boolean mPause = true;
    private int mRbKey = 3;
    private String mPatientName;
    private String mPatientId;

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_CONFIRM_RECEIVE;
        return my_id;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        Intent intent = getIntent();
        mPatientName = intent.getStringExtra("patientName");
        mPatientId = intent.getStringExtra("patientId");
//        AllDeviceCallBack.getInstance().initCallBack();
        DialogUtils.showNoDialog(mContext, "柜门已开!", 2, "form", null);
        mStart = new TimeCount(15000, 1000, mTimelyRight);
//        mStart.start();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onEventBing(Event.EventCheckbox event) {
//        String patient = event.mString;
//        Log.i("ff", "mMovie  " + patient);
//        if (event.type != null && event.type.equals("firstBind")) {
//
//        } else {
//            if (patient != null) {
//                for (int i = 0; i < mTCstInventoryVos.size(); i++) {
//                    mTCstInventoryVos.get(i).setPatientName(patient);
//                    mTCstInventoryVos.get(i).setPatientId(event.id);
//                }
//                mTimelyLeft.setEnabled(true);
//                mTimelyRight.setEnabled(true);
//                mTypeView.mRecogHaocaiAdapter.notifyDataSetChanged();
//            }
//        }
//
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRvEvent(Event.EventString event) {
//        mRvEventString = event.mString;
//        loadBingDate(mRvEventString);
//    }
    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {

        TextView textView;

        public TimeCount(long millisInFuture, long countDownInterval, TextView textView) {

            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.textView = textView;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (mTCstInventoryDto.getBindType() != null) {//先绑定患者
                mIntentType = 2;//2确认并退出
                loadBingFistDate(mIntentType);

            } else {//后绑定的未绑定
                int mType = 1;//1.8.3未绑定
                DialogUtils.showTwoDialog(mContext, mType, "您还有未绑定患者的耗材，确认领用吗？", "耗材未绑定患者");
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            textView.setText("确认并退出登录  " + "(" + millisUntilFinished / 1000 + "s)");
        }
    }

    @OnClick({R.id.timely_left, R.id.timely_right, R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
            R.id.base_tab_back, R.id.timely_start_btn, R.id.activity_btn_one})
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
                            case 2:
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
                        }
                    }
                });
                break;
            case R.id.base_tab_btn_msg:
                break;
            case R.id.base_tab_back:
                mStart.cancel();
                finish();
                break;
            case R.id.timely_start_btn://重新扫描
                Log.d(TAG, "重新扫描");
                mPause = false;
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //		   mShowLoading = DialogUtils.showLoading(mContext);
                    mTimelyLeft.setEnabled(true);
                    mTimelyRight.setEnabled(true);

                    List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
                    mTCstInventoryDto.gettCstInventoryVos().clear();
                    deviceInventoryVos.clear();

                    for (String deviceInventoryVo : mEthDeviceIdBack) {
                        String deviceCode = deviceInventoryVo;
                        LogUtils.i(TAG, "deviceCode    " + deviceCode);
                        startScan(deviceCode);
                    }
                }
                break;
            case R.id.timely_left:
                //                DialogUtils.showTwoDialog(mContext, 2, "耗材领用成功", "");
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    if (mTCstInventoryDto.getBindType() != null) {//先绑定患者
                        mIntentType = 1;//确认
                        loadBingFistDate(mIntentType);
                    } else {//后绑定的未绑定
                        int mType = 1;//1.8.3未绑定
                        DialogUtils.showTwoDialog(mContext, mType, "您还有未绑定患者的耗材，确认领用吗？", "耗材未绑定患者");
                    }
                }

                break;
            case R.id.timely_right:
                mStart.cancel();

                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    if (mTCstInventoryDto.getBindType() != null) {//先绑定患者
                        mIntentType = 2;//2确认并退出
                        loadBingFistDate(mIntentType);

                    } else {//后绑定的未绑定
                        int mType = 1;//1.8.3未绑定
                        DialogUtils.showTwoDialog(mContext, mType, "您还有未绑定患者的耗材，确认领用吗？", "耗材未绑定患者");
                    }
                }
                break;

        }
    }
    private void startScan(String deviceIndentify) {
        List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
                UHF_TYPE).find(BoxIdBean.class);
        for (BoxIdBean boxIdBean : boxIdBeans) {
            String box_id = boxIdBean.getBox_id();
            List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
                    .find(BoxIdBean.class);
            for (BoxIdBean deviceid : deviceBean) {
                String device_id = deviceid.getDevice_id();
                int i = DeviceManager.getInstance().StartUhfScan(device_id);

                LogUtils.i(TAG, "开始扫描了状态    " + i);
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
        LogUtils.i(TAG, "TAG   " + mEthDeviceIdBack.size());
        AllDeviceCallBack.getInstance().initCallBack();
        if (!mPause) {
            getDeviceDate(event.deviceId, event.epcs);
        }
        //	EventBus.getDefault().cancelEventDelivery(event);
    }
    @Override
    public void onPause() {
        mPause = true;
        super.onPause();

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
        if (mRbKey == 3) {
            tCstInventoryDto.setPatientName(mPatientName);
            tCstInventoryDto.setPatientId(mPatientId);
        }
        String toJson = mGson.toJson(tCstInventoryDto);
        LogUtils.i(TAG, "toJson    " + toJson);

        NetRequest.getInstance().putEPCDate(toJson, this, null, new BaseResult() {
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
                if (mRbKey == 3) {
                    for (TCstInventoryVo tCstInventoryVo : cstInventoryDto.gettCstInventoryVos()) {
                        tCstInventoryVo.setPatientName(cstInventoryDto.getPatientName());
                        tCstInventoryVo.setPatientId(cstInventoryDto.getPatientId());
                    }
                    cstInventoryDto.setBindType("firstBind");
                    EventBusUtils.postSticky(cstInventoryDto);
                    //                    if (App.getInstance().ifActivityRun(RecognizeActivity.class.getName())) {
                    //                        //已在后台运行
                    //                        return;
                    //                    } else {
                    //                    }
                    //                    mContext.startActivity(new Intent(mContext, OutBoxBingActivity.class));
//                    mContext.startActivity(new Intent(mContext, RecognizeActivity.class));
                    //

                }

            }
        });
    }



    private void loadBingFistDate(int mIntentType) {
        mTCstInventoryDto.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
        mTCstInventoryDto.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
        String toJson = mGson.toJson(mTCstInventoryDto);
        LogUtils.i(TAG, "toJson  " + toJson);
        NetRequest.getInstance().bingPatientsDate(toJson, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result   " + result);
                ToastUtils.showShort("操作成功");
                if (mIntentType==2){
                    startActivity(new Intent(RecognizeActivity.this, LoginActivity.class));
                    App.getInstance().removeALLActivity_();
                }
                finish();
            }
        });
    }
    /**
     * 获取需要绑定的患者
     */
    private void loadBingDate(String optienNameOrId) {

        NetRequest.getInstance().findSchedulesDate(optienNameOrId, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                mPatientInfos = bingFindSchedulesBean.getPatientInfos();
                DialogUtils.showRvDialog(RecognizeActivity.this, mContext, mPatientInfos, "afterBind",
                        -1, null);
                LogUtils.i(TAG, "result   " + result);
            }
        });
    }

}
