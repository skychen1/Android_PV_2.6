package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.vo.TempPatientVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TEMPORARY_BING;

/*
 * 患者列表页面,可以创建临时患者
 * */
public class LingyongActivity extends BaseTimelyActivity {

    private static final String TAG = "OutBoxBingActivity";
    private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos;
    private String mRvEventString;

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onRvEvent(Event.EventString event) {
    //        mRvEventString = event.mString;
    //        loadBingDate(mRvEventString);
    //    }
    @BindView(R.id.dialog_right)
    TextView mDialogRight;

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_TEMPORARY_BING;
        return my_id;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        mDialogRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = -1;
                boolean isHaveChecked = false;
                for (int i = 0; i < mTypeView.mCheckStates1.size(); i++) {
                    if (mTypeView.mCheckStates1.get(i)) {
                        position = i;
                    }
                }
                if (position != -1) {
                    LingyongActivity.this.startActivity(new Intent(LingyongActivity.this, RecognizeActivity.class));
                } else {
                    ToastUtils.showShort("请先选择患者");
                }
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
                DialogUtils.showCreatTempPatientDialog(mContext, LingyongActivity.this);
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

            TempPatientVo tempPatientVo = new TempPatientVo();

            tempPatientVo.setCstName(event.userName);
            tempPatientVo.setEpc(event.idCard);
            tempPatientVo.setCstSpec(event.time);
            tempPatientVo.setDeviceName(event.roomNum);
            mTempPatientVoVos.add(0, tempPatientVo);
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
                mPatientInfos = bingFindSchedulesBean.getPatientInfos();
                DialogUtils.showRvDialog(LingyongActivity.this, mContext, mPatientInfos, "afterBind", -1, null);
                LogUtils.i(TAG, "result   " + result);
            }
        });
    }
}