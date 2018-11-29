package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.AllOutBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.TCstInventoryDto;
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

import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_ALL_IN;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/9/27 10:36
 * 描述:        放入柜子的界面  快速开柜
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class InBoxAllTwoActivity extends BaseTimelyActivity {

    private static final String TAG = "InBoxAllTwoActivity";
    private int mIntentType;
    private Map<String, List<TagInfo>> mEPCDate = new TreeMap<>();
    int k = 0;
    private LoadingDialog.Builder mLoading;
    public ArrayList<String> mDoorList = new ArrayList<>();
    private Map<String, String> mEPCDatess = new TreeMap<>();
    private TCstInventoryDto mTCstInventoryDtoTwo;

    /**
     * 倒计时结束发起
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoorEvent(Event.EventDoorList event) {
        LogUtils.i(TAG, " event  " + event.doorList.size());
        mDoorList.addAll(event.doorList);
    }

    /**
     * 倒计时结束发起
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOverEvent(Event.EventOverPut event) {
        if (event.b) {
            LogUtils.i(TAG, "EventOverPut");
            mIntentType = 2;//2确认并退出
            setDate(mIntentType);
        }
    }

    /**
     * 扫描后EPC准备传值
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallBackEvent(Event.EventDeviceCallBack event) {
        LogUtils.i(TAG, "epc  " + event.deviceId + "   " + event.epcs.size());
        if (mLoading != null) {
            mLoading.mAnimationDrawable.stop();
            mLoading.mDialog.dismiss();
            mLoading = null;
        }
        mStarts.cancel();
        mStarts.start();
        List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
                .find(BoxIdBean.class);
        for (BoxIdBean boxIdBean : boxIdBeanss) {
            String box_id = boxIdBean.getBox_id();
            //	   List<BoxIdBean> boxIdDoor = LitePal.where("box_id = ? and name = ?", box_id, UHF_TYPE)
            //		   .find(BoxIdBean.class);
            //	   for (BoxIdBean BoxIdBean : boxIdDoor) {
            //		String device_id = BoxIdBean.getDevice_id();
            //		for (int x = 0; x < mDoorList.size(); x++) {
            //		   if (device_id.equals(mDoorList.get(x))) {
            //			mDoorList.remove(x);
            //		   }
            //		}
            //	   }
            if (box_id != null) {
                List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
                        READER_TYPE).find(BoxIdBean.class);
                if (boxIdBeansss.size() > 1) {
                    for (BoxIdBean BoxIdBean : boxIdBeansss) {
                        LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
                        if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
                            mEPCDate.putAll(event.epcs);
                            k++;
                            LogUtils.i(TAG, "mEPCDate   " + mEPCDate.size());
                        }
                    }
                    LogUtils.i(TAG, "mEPCDate  k  " + k);
                    if (k == boxIdBeansss.size()) {
                        k = 0;
                        if (mEPCDate.size() == 0) {
                            mEPCDatess.put("", box_id);//没有空格
                        }
                        for (Map.Entry<String, List<TagInfo>> v : mEPCDate.entrySet()) {
                            mEPCDatess.put(v.getKey(), box_id);
                        }
                        LogUtils.i(TAG, "mEPCDates.mEPCDates()多reader  " + mEPCDatess.size());

                    } else {
                        return;
                    }

                } else {
                    if (event.epcs.size() == 0) {
                        mEPCDatess.put(" ", box_id);//1个空格
                    }
                    for (Map.Entry<String, List<TagInfo>> v : event.epcs.entrySet()) {
                        mEPCDatess.put(v.getKey(), box_id);
                    }
                    LogUtils.i(TAG, "mEPCDates.mEPCDates()单reader  " + mEPCDatess.size());
                }
            }
        }

        LogUtils.i(TAG, "mDoorList.size()   " + mDoorList.size());
        if (mDoorList.size() != 0) {
            return;
        }
        LogUtils.i(TAG, "mEPCDates.mEPCDates() " + mEPCDatess.size());
        String toJson = getEpcDtoString(mEPCDatess);
        putAllInEPCDate(toJson);

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

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_ALL_IN;
        return my_id;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
            R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn, R.id.timely_open_door,
            R.id.timely_left, R.id.timely_right, R.id.btn_four_ly, R.id.btn_four_yc, R.id.btn_four_tb,
            R.id.btn_four_th})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_tab_btn_msg:
                mContext.startActivity(new Intent(this, MessageActivity.class));

                break;
            case R.id.base_tab_back:
                EventBusUtils.postSticky(new Event.EventFrag("START1"));
                finish();
                break;
            case R.id.timely_start_btn:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //		   mShowLoading = DialogUtils.showLoading(mContext);
                    mStarts.cancel();
                    mTimelyRight.setText("确认并退出登录");
                    moreStartScan();
                }
                break;

            case R.id.timely_left:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //确认
                    mIntentType = 1;
                    if (mTCstInventoryVos != null) {
                        setDate(mIntentType);
                    } else {
                        ToastUtils.showShort("数据异常");
                    }

                }

                break;
            case R.id.timely_right:
                if (UIUtils.isFastDoubleClick()) {
                    return;
                } else {
                    mIntentType = 2;
                    if (mTCstInventoryVos != null) {
                        setDate(mIntentType);
                    } else {
                        ToastUtils.showShort("数据异常");
                    }
                }
                break;
            //	   case R.id.timely_open_door:
            //		mStarts.cancel();
            //
            //		List<DeviceInventoryVo> deviceInventoryVos = mTCstInventoryDto.getDeviceInventoryVos();
            //		mTCstInventoryDto.gettCstInventoryVos().clear();
            //		deviceInventoryVos.clear();
            //		for (String deviceInventoryVo : mEthDeviceIdBack) {
            //		   String deviceCode = deviceInventoryVo;
            //		   LogUtils.i(TAG, "deviceCode    " + deviceCode);
            //		   DeviceManager.getInstance().OpenDoor(deviceCode);
            //		}
            //		break;
        }

    }

    private void moreStartScan() {

        mEPCDate.clear();
        mEPCDatess.clear();
        mTCstInventoryDto.gettCstInventoryVos().clear();

        for (String deviceInventoryVo : mEthDeviceIdBack) {
            String deviceCode = deviceInventoryVo;
            LogUtils.i(TAG, "deviceCode    " + deviceCode);
            startScan(deviceCode);
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

            //	   if (READER_TAG.equals(READER_2)) {
            //		new Thread() {
            //		   public void run() {
            //			for (BoxIdBean deviceid : deviceBean) {
            //			   String device_id = deviceid.getDevice_id();
            //			   int i = DeviceManager.getInstance().StartUhfScan(device_id, 3000);
            //			   LogUtils.i(TAG, "开始扫描了状态  罗丹贝尔  " + i + "    " + device_id);
            //			   try {
            //				Thread.sleep(3000);
            //			   } catch (InterruptedException e) {
            //				e.printStackTrace();
            //			   }
            //			}
            //		   }
            //		}.start();
            //	   } else {
            for (BoxIdBean deviceid : deviceBean) {
                String device_id = deviceid.getDevice_id();
                int i = DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
                LogUtils.i(TAG, "开始扫描了状态    " + i);
            }
            //	   }
        }
    }

    /**
     * 设置提交值
     */
    private void setDate(int mIntentType) {

        TCstInventoryDto dto = new TCstInventoryDto();
        dto.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
        dto.settCstInventoryVos(mTCstInventoryVos);
        //	dto.setOperation(mTCstInventoryDto.getOperation());
        dto.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
        dto.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));

        String s = mGson.toJson(dto);
        LogUtils.i(TAG, "返回  " + s);
        NetRequest.getInstance().putAllOperateYes(s, this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result  " + result);
                ToastUtils.showShort("操作成功");
                MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
                if (mIntentType == 2) {
                    startActivity(new Intent(InBoxAllTwoActivity.this, LoginActivity.class));
                    App.getInstance().removeALLActivity_();
                } else {
                    EventBusUtils.postSticky(new Event.EventFrag("START1"));
                    startActivity(new Intent(InBoxAllTwoActivity.this, HomeActivity.class));
                }
                finish();

            }
        });
    }

    /**
     * 快速开柜入柜查询
     */
    private void putAllInEPCDate(String json) {
        NetRequest.getInstance().putAllInEPCDate(json, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result mObject   " + result);
                mTCstInventoryDtoTwo = mGson.fromJson(result, TCstInventoryDto.class);

                String string = null;
                if (mTCstInventoryDtoTwo.getErrorEpcs() != null &&
                        mTCstInventoryDtoTwo.getErrorEpcs().size() > 0) {
                    string = StringUtils.listToString(mTCstInventoryDtoTwo.getErrorEpcs());
                    ToastUtils.showLong(string);
                    MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
                }
                if (mTCstInventoryDtoTwo.gettCstInventoryVos() != null &&
                        mTCstInventoryDtoTwo.gettCstInventoryVos().size() != 0) {
                    //		   EventBusUtils.postSticky(new Event.EventAct(mActivityType));
                    EventBusUtils.postSticky(mTCstInventoryDtoTwo);
                } else {
                    if (mTimelyLeft != null && mTimelyRight != null) {
                        mTimelyLeft.setEnabled(false);
                        mTimelyRight.setEnabled(false);
                        mStarts.cancel();
                        mTimelyRight.setText("确认并退出登录");
                    }
                    Toast.makeText(mContext, "未扫描到操作耗材,请重新操作", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            EventBusUtils.postSticky(new Event.EventFrag("START1"));
                            finish();
                        }
                    }, 3000);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mLoading != null) {
            mLoading.mAnimationDrawable.stop();
            mLoading.mDialog.dismiss();
            mLoading = null;
        }

        EventBusUtils.unregister(this);
        mEthDeviceIdBack.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (mLoading != null) {
            mLoading.mAnimationDrawable.stop();
            mLoading.mDialog.dismiss();
            mLoading = null;
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mLoading != null) {
            mLoading.mAnimationDrawable.stop();
            mLoading.mDialog.dismiss();
            mLoading = null;
        }
        mStarts.cancel();
        super.onPause();
    }

    /**
     * 快速开柜epc放入DTO
     *
     * @param epcs
     * @return
     */
    private String getEpcDtoString(Map<String, String> epcs) {
        AllOutBean allOutBean = new AllOutBean();
        List<AllOutBean.TCstInventoryVos> epcList = new ArrayList<>();
        for (Map.Entry<String, String> v : epcs.entrySet()) {
            AllOutBean.TCstInventoryVos tCstInventory = new AllOutBean.TCstInventoryVos();
            tCstInventory.setEpc(v.getKey());
            tCstInventory.setDeviceCode(v.getValue());
            epcList.add(tCstInventory);
        }
        allOutBean.setTCstInventoryVos(epcList);
        allOutBean.setStorehouseCode(SPUtils.getString(mContext, SAVE_STOREHOUSE_CODE));
        String toJson = mGson.toJson(allOutBean);
        LogUtils.i(TAG, "toJson mObject   " + toJson);
        return toJson;
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                LogUtils.i(TAG, "   ACTION_UP  ");
                if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
                        !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")) {
                    mStarts.cancel();
                    mStarts.start();
                }
                break;
            //否则其他动作计时取消
            default:
                mStarts.cancel();
                LogUtils.i(TAG, "   其他操作  ");

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
