package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 16:55
 * 描述:        个人信息页面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class MyInfoActivity extends BaseSimpleActivity {

    @BindView(R.id.setting_name)
    TextView mSettingName;
    @BindView(R.id.setting_department)
    TextView mSettingDepartment;
    @BindView(R.id.setting_position)
    TextView mSettingPosition;
    @BindView(R.id.setting_remarks)
    TextView mSettingRemarks;
    @BindView(R.id.setting_account_name)
    TextView mSettingAccountName;
    private LoadingDialog.Builder mBuilder;

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("个人信息");
        initData();
    }

    private void initData() {
        String accountData = SPUtils.getString(getApplicationContext(), KEY_ACCOUNT_DATA, "");

        LoginResultBean data = mGson.fromJson(accountData, LoginResultBean.class);

        LoginResultBean.AppAccountInfoVoBean appAccountInfoVo = data.getAppAccountInfoVo();

        List<String> roleNames = appAccountInfoVo.getRoleNames();
        String roleName = "";
        for (int i = 0; i < roleNames.size(); i++) {
            roleName = roleName + roleNames.get(i);
            if (i < roleNames.size() - 1) {
                roleName = roleName + "/";
            }
        }
        mSettingAccountName.setText(appAccountInfoVo.getAccountName());
        mSettingName.setText("用户名：" + appAccountInfoVo.getUserName());
        mSettingPosition.setText("角色：" + roleName);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.setting_myinfo_layout;
    }
    //
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        // TODO: add setContentView(...) invocation
    //        ButterKnife.bind(this);
    //    }
}
