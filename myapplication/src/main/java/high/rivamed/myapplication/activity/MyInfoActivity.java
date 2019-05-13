package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;

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

    private static final String TAG = "MyInfoActivity";
    @BindView(R.id.setting_name)
    TextView  mSettingName;
    @BindView(R.id.setting_department)
    TextView  mSettingDepartment;
    @BindView(R.id.setting_position)
    TextView  mSettingPosition;
    @BindView(R.id.setting_remarks)
    TextView  mSettingRemarks;
    @BindView(R.id.setting_account_name)
    TextView  mSettingAccountName;
    @BindView(R.id.top_icon)
    ImageView mTopIcon;

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        mBaseTabBack.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabTvTitle.setText("个人信息");
        if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX)!=null&&SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")){
            Glide.with(this)
                  .load(R.mipmap.hccz_mrtx_nan)
                  .error(R.mipmap.hccz_mrtx_nan)
                  .into(mTopIcon);
        }else {
            Glide.with(this)
                  .load(R.mipmap.hccz_mrtx_nv)
                  .error(R.mipmap.hccz_mrtx_nv)
                  .into(mTopIcon);
        }
        initData();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private void initData() {
        try {
            String accountData = SPUtils.getString(getApplicationContext(), KEY_ACCOUNT_DATA, "");
            LogUtils.i(TAG,"accountData   "+accountData);
            LoginResultBean data = mGson.fromJson(accountData, LoginResultBean.class);
            LoginResultBean.AppAccountInfoVoBean appAccountInfoVo = data.getAppAccountInfoVo();
	     List<LoginResultBean.AppAccountInfoVoBean.RolesBean> roles = appAccountInfoVo.getRoles();
	     String roleName = "";
            for (int i = 0; i < roles.size(); i++) {
                roleName = roleName + roles.get(i).getRoleName();
                if (i < roles.size() - 1) {
                    roleName = roleName + "/";
                }
            }
            mSettingAccountName.setText(appAccountInfoVo.getAccountName());
            mSettingName.setText("用户名：" + appAccountInfoVo.getUserName());
            mSettingPosition.setText("角色：" + roleName);
            mBaseTabTvName.setText(appAccountInfoVo.getUserName());
            if (appAccountInfoVo.getSex().equals("女")){
                Glide.with(this)
                      .load(R.mipmap.hccz_mrtx_nv)
                      .error(R.mipmap.hccz_mrtx_nv)
                      .into(mBaseTabIconRight);
            }else {
                Glide.with(this)
                      .load(R.mipmap.hccz_mrtx_nan)
                      .error(R.mipmap.hccz_mrtx_nv)
                      .into(mBaseTabIconRight);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.setting_myinfo_layout;
    }
}
