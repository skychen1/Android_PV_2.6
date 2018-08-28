package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_PATIENT_CONN;

/*
 * 选择临时患者页面
 * */
public class PatientConnActivity extends BaseTimelyActivity {

    private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos = new ArrayList<>();

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_PATIENT_CONN;
        return my_id;
    }


    @OnClick({R.id.tv_patient_conn, R.id.ly_bing_btn_right, R.id.dialog_left, R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
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
                                        PatientConnActivity.this.startActivity(new Intent(PatientConnActivity.this, LoginActivity.class));
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
            case R.id.tv_patient_conn://选择患者关联
                int position = -1;
                boolean isHaveChecked = false;
                for (int i = 0; i < mTypeView.mCheckStates1.size(); i++) {
                    if (mTypeView.mCheckStates1.get(i)) {
                        position = i;
                    }
                }
                if (position != -1) {

                    loadBingDate("");

                } else {
                    ToastUtils.showShort("请先选择患者");
                }
                break;
        }
    }

    /**
     * 获取所有未绑定临时患者
     */
    private void loadTempBingDate(String optienNameOrId) {
        //todo
//        NetRequest.getInstance().findSchedulesDate(optienNameOrId, this, null, new BaseResult() {
//            @Override
//            public void onSucceed(String result) {
//                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
//                        BingFindSchedulesBean.class);
//                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
//                    patientInfos.clear();
//                    patientInfos.addAll(bingFindSchedulesBean.getPatientInfos());
//                }
//
//                LogUtils.i("TemPatientBindActivity", "result   " + result);
//            }
//        });
    }

    /**
     * 查询所有在院患者信息
     */
    private void loadBingDate(String optienNameOrId) {

        NetRequest.getInstance().findInPatientPage(optienNameOrId, 1,10,this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
                    mPatientInfos.clear();
                    mPatientInfos.addAll(bingFindSchedulesBean.getPatientInfos());
                    DialogUtils.showRvDialog2(PatientConnActivity.this, mContext, mPatientInfos);
                }

                LogUtils.i("TemPatientBindActivity", "result   " + result);
            }
        });
    }

}