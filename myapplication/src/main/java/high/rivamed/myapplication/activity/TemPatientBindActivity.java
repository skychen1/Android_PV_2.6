package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TEMPORARY_BING;

/*
 * 患者列表页面,可以创建临时患者
 * */
public class TemPatientBindActivity extends BaseTimelyActivity {

    private static final String TAG = "OutBoxBingActivity";
    private String mRvEventString;

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onRvEvent(Event.EventString event) {
    //        mRvEventString = event.mString;
    //        loadBingDate(mRvEventString);
    //    }
    @BindView(R.id.dialog_right)
    TextView mDialogRight;
    @BindView(R.id.search_et)
    EditText mSearchEt;
    public static List<BoxSizeBean.TbaseDevicesBean> mTemPTbaseDevices = new ArrayList<>();

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_TEMPORARY_BING;
        return my_id;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        mTemPTbaseDevices = (List<BoxSizeBean.TbaseDevicesBean>) getIntent().getSerializableExtra("mTemPTbaseDevices");
        mDialogRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = -1;
                for (int i = 0; i < mTypeView.mCheckStates1.size(); i++) {
                    if (mTypeView.mCheckStates1.get(i)) {
                        position = i;
                    }
                }
                if (position != -1) {
//                    TemPatientBindActivity.this.startActivity(new Intent(TemPatientBindActivity.this, RecognizeActivity.class));
                    String name = ((TextView)mTypeView.mRecyclerview.getChildAt(mTypeView.mTempPatientAdapter.mCheckPosition).findViewById(R.id.seven_two)).getText().toString();
                    String id = ((TextView)mTypeView.mRecyclerview.getChildAt(mTypeView.mTempPatientAdapter.mCheckPosition).findViewById(R.id.seven_three)).getText().toString();
                    EventBusUtils.postSticky(
                            new Event.EventCheckbox(name, id, "firstBind", position, mTemPTbaseDevices));
                } else {
                    ToastUtils.showShort("请先选择患者");
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

    @OnClick({R.id.ly_bing_btn_right, R.id.dialog_left, R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
            R.id.base_tab_back, R.id.search_et, R.id.ly_creat_temporary_btn,
    })
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
                DialogUtils.showCreatTempPatientDialog(mContext, TemPatientBindActivity.this);
                break;
            case R.id.dialog_left://取消
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onActivationEvent(Event.tempPatientEvent event) {

        if (event.dialog != null) {
            event.dialog.dismiss();
            event.dialog = null;
            Log.e("aaa", "JSON.toJSON(event):" + JSON.toJSON(event));

            BingFindSchedulesBean.PatientInfosBean tempPatientVo = new BingFindSchedulesBean.PatientInfosBean();

            tempPatientVo.setPatientName(event.userName);
            tempPatientVo.setPatientId("virtual");
            tempPatientVo.setRequestDateTime(event.time);
            tempPatientVo.setOperatingRoomNoName(event.roomNum);
            patientInfos.add(0, tempPatientVo);
            mTypeView.mTempPatientAdapter.notifyDataSetChanged();
        }
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
                    mTypeView.mTempPatientAdapter.notifyDataSetChanged();
                }

                LogUtils.i("TemPatientBindActivity", "result   " + result);
            }
        });
    }

}