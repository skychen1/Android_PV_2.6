package high.rivamed.myapplication.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.CreatTempPatientBean;
import high.rivamed.myapplication.bean.CreatTempPatientResultBean;
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
import high.rivamed.myapplication.views.TempPatientDialog;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TEMPORARY_BING;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

/*
 * 患者列表页面,可以创建临时患者
 * */
public class TemPatientBindActivity extends BaseTimelyActivity {

    private static final String TAG = "TemPatientBindActivity";
    private String mRvEventString;
    private int mRbKey = 3;

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onRvEvent(Event.EventString event) {
    //        mRvEventString = event.mString;
    //        loadBingDate(mRvEventString);
    //    }
    @BindView(R.id.dialog_right)
    TextView mDialogRight;
    @BindView(R.id.search_et)
    EditText mSearchEt;
    public List<BoxSizeBean.TbaseDevicesBean> mTemPTbaseDevices = new ArrayList<>();
    private int mPosition;
    private String mName = "";
    private String mId = "";
    private boolean mPause = true;
    private String mType = "";
    private String mOperationScheduleId;

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_TEMPORARY_BING;
        return my_id;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        AllDeviceCallBack.getInstance().initCallBack();
        mTemPTbaseDevices = (List<BoxSizeBean.TbaseDevicesBean>) getIntent().getSerializableExtra(
                "mTemPTbaseDevices");
        mPosition = getIntent().getIntExtra("position", -1);
        mType = getIntent().getStringExtra("type");
        mDialogRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPause = false;
                mName = ((TextView) mTypeView.mRecyclerview.getChildAt(
                        mTypeView.mTempPatientAdapter.mSelectedPos)
                        .findViewById(R.id.seven_two)).getText().toString();
                mId = ((TextView) mTypeView.mRecyclerview.getChildAt(
                        mTypeView.mTempPatientAdapter.mSelectedPos)
                        .findViewById(R.id.seven_three)).getText().toString();
                mOperationScheduleId = patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos)
                        .getOperationScheduleId();
                if (null != mType && mType.equals("afterBindTemp")) {
                    //后绑定患者
                    EventBusUtils.postSticky(
                            new Event.EventCheckbox(mName, mId, mOperationScheduleId, "afterBindTemp",
                                    mPosition, mTemPTbaseDevices));
                    finish();
                } else {
                    //先绑定患者
                    AllDeviceCallBack.getInstance().openDoor(mPosition, mTemPTbaseDevices);
                }
            }
        });

        loadBingDate("");

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String trim = charSequence.toString().trim();
                loadBingDate(trim);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    //    @Override
    //    public void onResume() {
    //        mPause =false;
    //        super.onResume();
    //    }
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
            tCstInventoryDto.setPatientName(mName);
            tCstInventoryDto.setPatientId(mId);
            tCstInventoryDto.setOperationScheduleId(mOperationScheduleId);
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
                        tCstInventoryVo.setOperationScheduleId(cstInventoryDto.getOperationScheduleId());
                    }
                    cstInventoryDto.setPatientId(mId);
                    cstInventoryDto.setPatientName(mName);
                    cstInventoryDto.setOperationScheduleId(mOperationScheduleId);
                    cstInventoryDto.setBindType("firstBind");
                    EventBusUtils.postSticky(cstInventoryDto);
                    //                    if (App.getInstance().ifActivityRun(RecognizeActivity.class.getName())) {
                    //                        //已在后台运行
                    //                        return;
                    //                    } else {
                    //                    }

                    mContext.startActivity(
                            new Intent(mContext, OutBoxBingActivity.class).putExtra("patientName", mName)
                                    .putExtra("patientId", mId)
                                    .putExtra("operationScheduleId", mOperationScheduleId));
                    //                    mContext.startActivity(new Intent(mContext, RecognizeActivity.class).putExtra("patientName", mName).putExtra("patientId", mId).putExtra("operationScheduleId", mOperationScheduleId));
                    finish();
                    //

                }

            }
        });
    }

    @OnClick({R.id.ly_bing_btn_right, R.id.dialog_left, R.id.base_tab_tv_name,
            R.id.base_tab_icon_right, R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.search_et,
            R.id.ly_creat_temporary_btn,})
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
            case R.id.base_tab_back:
                finish();
                break;
            case R.id.search_et://搜索
                break;
            case R.id.ly_creat_temporary_btn://创建临时患者
                if (!UIUtils.isFastDoubleClick()) {
                    DialogUtils.showCreatTempPatientDialog(mContext, TemPatientBindActivity.this, new TempPatientDialog.Builder.SettingListener() {
                        @Override
                        public void getDialogDate(String userName, String roomNum, String userSex, String idCard, String time, Dialog dialog) {
                            Log.e(TAG, "showCreatTempPatientDialogaaaa");
                            creatTemPatient(new Event.tempPatientEvent(userName, roomNum, userSex, idCard, time, dialog));
                        }
                    });
                }
                break;
            case R.id.dialog_left://取消
                finish();
                break;
        }
    }

    /*
     * 创建临时患者
     * */
    private void creatTemPatient(Event.tempPatientEvent event) {
        Log.e(TAG, "creatTemPatient");
        CreatTempPatientBean data = new CreatTempPatientBean();
        CreatTempPatientBean.TTransOperationScheduleBean bean = new CreatTempPatientBean.TTransOperationScheduleBean();
        bean.setName(event.userName);
        bean.setIdNo(event.idCard);
        bean.setRequestDateTime(event.time);
        bean.setOperatingRoomNo(event.roomNum);
        bean.setSex(event.userSex);
        bean.setOperatingDeptCode(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE, ""));
        data.setTTransOperationSchedule(bean);
        NetRequest.getInstance()
                .saveTempPatient(mGson.toJson(data), TemPatientBindActivity.this, null,
                        new BaseResult() {
                            @Override
                            public void onSucceed(String result) {
                                try {
                                    CreatTempPatientResultBean bean1 = mGson.fromJson(result,
                                            CreatTempPatientResultBean.class);

                                    if (bean1.isOperateSuccess()) {
                                        if (event.dialog != null) {
                                            event.dialog.dismiss();
                                            event.dialog = null;
                                            //                            BingFindSchedulesBean.PatientInfosBean tempPatientVo = new BingFindSchedulesBean.PatientInfosBean();
                                            //                            tempPatientVo.setPatientName(event.userName);
                                            //                            tempPatientVo.setPatientId("virtual");
                                            //                            tempPatientVo.setRequestDateTime(event.time);
                                            //                            tempPatientVo.setOperatingRoomNoName(event.roomNum);
                                            //                            patientInfos.add(0, tempPatientVo);
                                            //                            mTypeView.mTempPatientAdapter.notifyDataSetChanged();

                                            loadBingDate("");
                                            ToastUtils.showShort("创建成功");
                                        }
                                    } else {
                                        ToastUtils.showShort("创建失败");
                                    }
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String result) {
                                ToastUtils.showShort("创建失败");
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
                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
                    patientInfos.clear();
                    patientInfos.addAll(bingFindSchedulesBean.getPatientInfos());
                    if (patientInfos.size() > 0) {
                        for (int i = 0; i < patientInfos.size(); i++) {
                            patientInfos.get(i).setSelected(false);
                        }
                        patientInfos.get(0).setSelected(true);
                    }
                    mTypeView.mTempPatientAdapter.notifyDataSetChanged();
                }

                LogUtils.i("TemPatientBindActivity", "result   " + result);
            }
        });
    }

}