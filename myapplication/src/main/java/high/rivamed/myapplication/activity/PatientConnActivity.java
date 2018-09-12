package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.bean.PatientConnBean;
import high.rivamed.myapplication.bean.PatientConnResultBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.RvDialog2;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_PATIENT_CONN;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/*
 * 选择临时患者页面
 * */
public class PatientConnActivity extends BaseTimelyActivity {

    private static final String TAG = "PatientConnActivity";
    @BindView(R.id.search_et)
    EditText mSearchEt;
    private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos = new ArrayList<BingFindSchedulesBean.PatientInfosBean>();
    private RvDialog2.Builder mBuilder;
    private int page = 1;
    private String mString = "";

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_PATIENT_CONN;
        return my_id;
    }


    @Override
    protected void onResume() {
        LogUtils.i(TAG, "onResume   ");
        loadTempBingDate("");

        super.onResume();
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        loadTempBingDate("");
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String trim = charSequence.toString().trim();
                loadTempBingDate(trim);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            case R.id.tv_patient_conn://选择患者关联
                if (patientInfos.size() > 0) {
                    mPatientInfos.clear();
                    page = 1;
                    loadAllDate("");
                }
                break;
        }
    }

    /**
     * 获取所有未绑定临时患者
     */
    private void loadTempBingDate(String optienNameOrId) {
        NetRequest.getInstance().findTempPatients(optienNameOrId, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
                        BingFindSchedulesBean.class);
                if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfos() != null) {
                    patientInfos.clear();
                    patientInfos.addAll(bingFindSchedulesBean.getPatientInfos());
                    if (patientInfos.size() > 0) {
                        patientInfos.get(0).setSelected(true);
                    }
                    mTypeView.mTempPatientAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRvEvent(Event.EventString event) {
        if (event != null && event.mString != null) {
            mString = event.mString;
        } else {
            mString = "";
        }
        mPatientInfos.clear();
        page = 1;
        loadAllDate(mString);
    }

    /**
     * 查询所有在院患者信息
     */
    private void loadAllDate(String optienNameOrId) {
        final int rows = 20;
        NetRequest.getInstance().findInPatientPage(optienNameOrId, page, rows, this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                FindInPatientBean bean = mGson.fromJson(result,
                        FindInPatientBean.class);
                if (bean != null && bean.getRows() != null && bean.getRows().size() >= 0) {
                    boolean isClear;
                    if (mPatientInfos.size() == 0) {
                        isClear = true;
                    }else {
                        isClear = false;
                    }

                    for (int i = 0; i < bean.getRows().size(); i++) {
                        BingFindSchedulesBean.PatientInfosBean data = new BingFindSchedulesBean.PatientInfosBean();
                        data.setPatientId(bean.getRows().get(i).getPatientId());
                        data.setPatientName(bean.getRows().get(i).getPatientName());
                        data.setDeptName(bean.getRows().get(i).getDeptName());
                        data.setOperatingRoomNoName(bean.getRows().get(i).getOperatingRoomNoName());
                        data.setOperationBeginDateTime(bean.getRows().get(i).getOperationBeginDateTime());
                        data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
                        data.setLoperPatsId(bean.getRows().get(i).getLoperPatsId());
                        data.setLpatsInId(bean.getRows().get(i).getLpatsInId());
                        mPatientInfos.add(data);
                    }

                    if (isClear && mPatientInfos.size() > 0) {
                        mPatientInfos.get(0).setSelected(true);
                    }
                    //                    if (mBuilder != null && mBuilder.mRefreshLayout != null) {
                    //                        if (bean.getRows().size() > 0) {
                    //                            if (bean.getRows().size() < rows) {
                    //                                mBuilder.mRefreshLayout.finishLoadMoreWithNoMoreData();
                    //                            } else {
                    ////                                mBuilder.mRefreshLayout.setNoMoreData(false);
                    //                            }
                    //
                    //                        } else {
                    //                            mBuilder.mRefreshLayout.finishLoadMoreWithNoMoreData();
                    //                        }
                    //                        mBuilder.mRefreshLayout.finishRefresh();
                    //                    }

                    if (mBuilder == null) {
                        mBuilder = DialogUtils.showRvDialog2(PatientConnActivity.this, mContext, mPatientInfos, new OnClickBackListener() {
                            @Override
                            public void OnClickBack(int position, DialogInterface dialog) {
                                Log.e("PatientConnActivity", "mTypeView.mTempPatientAdapter position:" + mTypeView.mTempPatientAdapter.mSelectedPos);
                                Log.e("PatientConnActivity", "showRvDialog2 position:" + position);
                                //进行患者关联
                                if (mPatientInfos.size() > 0) {
                                    connPatient(position, dialog);
                                }else {
                                    ToastUtils.showShort("没有患者数据");
                                }
                            }
                        });
                        mBuilder.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                            @Override
                            public void onRefresh(RefreshLayout refreshLayout) {
                                mBuilder.mRefreshLayout.setNoMoreData(false);
                                page = 1;
                                mPatientInfos.clear();
                                loadAllDate(mString);
                                mBuilder.mRefreshLayout.finishRefresh();
                            }
                        });
                        mBuilder.mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore(RefreshLayout refreshLayout) {
                                page++;
                                loadAllDate(mString);
                                mBuilder.mRefreshLayout.finishLoadMore();
                            }
                        });
                    } else {
                        if (mBuilder.mDialog.isShowing()) {
                        } else {
                            mBuilder.mDialog.show();
                        }
                        RvDialog2.sTableTypeView2.mBingOutAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort("没有患者数据");
                }
            }
        });
    }

    //进行患者关联
    private void connPatient(int position, DialogInterface dialog) {
        PatientConnBean bean = new PatientConnBean();
        bean.setLPatsInId(mPatientInfos.get(position).getLpatsInId());
        bean.setOperationScheduleId(patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getOperationScheduleId());
        bean.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
        bean.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
        NetRequest.getInstance().tempPatientConnPatient(mGson.toJson(bean), this, null, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "result   " + result);
                try {
                    PatientConnResultBean bean = mGson.fromJson(result,
                            PatientConnResultBean.class);
                    if (bean.isOperateSuccess()) {
                        DialogUtils.showNoDialog(mContext, "关联患者成功", 2, "form", null);
                        loadTempBingDate("");
                    } else {
                        ToastUtils.showShort("关联患者失败");
                    }
                } catch (Exception e) {
                    ToastUtils.showShort("关联患者失败");
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    //提供接口
    public interface OnClickBackListener {
        void OnClickBack(int x, DialogInterface dialog);
    }

}