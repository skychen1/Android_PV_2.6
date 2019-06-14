package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.callback.InitListener;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.ui.FaceGatewayActivity;
import com.ruihua.face.recognition.ui.RgbVideoIdentityActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.utils.FaceTask;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:       chenyanling
 * 创建时间:    2019/6/12
 * 描述:        人脸识别初始化:授权码激活  人脸底库初始化同步
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteFaceFrag extends SimpleFragment {

    @BindView(R.id.fragment_btn_active)
    TextView fragmentBtnActive;
    @BindView(R.id.fragment_btn_init)
    TextView fragmentBtnInit;
    @BindView(R.id.switch_btn)
    Switch switchBtn;
    private boolean hasInit;

    public static RegisteFaceFrag newInstance() {
        Bundle args = new Bundle();
        RegisteFaceFrag fragment = new RegisteFaceFrag();
        //	args.putInt(TYPE_SIZE, param);
        //	args.putString(TYPE_PAGE, type);
        //	fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getLayoutId() {
        return R.layout.frg_face_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        hasInit = FaceManager.getManager().getInitStatus() == FaceCode.SDK_INITED;
        fragmentBtnActive.setEnabled(!hasInit);
        fragmentBtnActive.setText(hasInit ? "已初始化人脸识别SDK" : "初始化人脸识别SDK");
        switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //设置是否需要活体
            FaceManager.getManager().setNeedLive(isChecked);
        });
    }


    @Override
    public void onBindViewBefore(View view) {

    }


    @OnClick(R.id.fragment_btn_active)
    public void onActiveClicked() {
        if (UIUtils.isFastDoubleClick(R.id.fragment_btn_active)) {
            return;
        } else {
            FaceManager.getManager().init(_mActivity, false, new InitListener() {
                @Override
                public void initSuccess() {
                    ToastUtils.showShortSafe("人脸识别SDK初始化成功");
                    hasInit = true;
                    fragmentBtnActive.setEnabled(false);
                    fragmentBtnActive.setText("已初始化成功");

                    //初始化分组
                    boolean b = FaceManager.getManager().initGroup();
                    if (!b) {
                        ToastUtils.showShortSafe("创建人脸照分组失败");
                        //初始化完成后跳转页面
                    } else {
                        //设置是否需要活体
                        FaceManager.getManager().setNeedLive(switchBtn.isChecked());
                    }
                }

                @Override
                public void initFail(int errorCode, String msg) {
                    ToastUtils.showShortSafe("人脸识别SDK初始化失败：：errorCode = " + errorCode + ":::msg：" + msg);
                }
            });
        }
    }


    @OnClick(R.id.fragment_btn_init)
    public void onInitClicked() {
        if (UIUtils.isFastDoubleClick(R.id.fragment_btn_init)) {
            return;
        } else {
            if (hasInit) {
                if (FaceManager.getManager().initGroup()) {
                    //从服务器更新人脸底库并注册至本地
                    FaceTask faceTask = new FaceTask(_mActivity);
                    faceTask.setCallBack((hasRegister, msg) -> {
                        LogUtils.d("faceTask", "initListener: " + msg);
                        ToastUtils.showShortSafe(msg);
                    });
                    faceTask.getAllFaceAndRegister();
                } else {
                    ToastUtils.showShort("创建人脸照分组失败");
                }
            } else {
                ToastUtils.showShort("请先初始化人脸识别SDK");
            }
        }
    }
    @OnClick(R.id.fragment_btn_recongize)
    void onTest(View v) {
        int initStatus = FaceManager.getManager().getInitStatus();
        if (initStatus == FaceCode.SDK_NOT_ACTIVE) {
            ToastUtils.showShort("SDK还未激活，请先激活");
            return;
        } else if (initStatus == FaceCode.SDK_NOT_INIT) {
            ToastUtils.showShort("SDK还未初始化完成，请先初始化");
            return;
        } else if (initStatus == FaceCode.SDK_INITING) {
            ToastUtils.showShort("SDK正在初始化，请稍后再试");
            return;
        } else if (initStatus == FaceCode.SDK_INIT_FAIL) {
            ToastUtils.showShort("SDK初始化失败，请重新初始化SDK");
            return;
        }
        //在开启人脸识别以前需要获取人脸照片数量，
        //如果为0表示没有底库，是不能开启识别的；
        if (0 == FaceManager.getManager().getFaceLibraryNum()) {
            ToastUtils.showShort("人脸识别底库无数据，请先初始化人脸底库");
            return;
        }
        //跳转到识别页面
        RgbVideoIdentityActivity.launch(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //验证身份回调
        if (requestCode == resultCode && resultCode == RgbVideoIdentityActivity.CODE_RECOGNISE && data != null) {
            //拿到识别的人员的id
            String userId = data.getStringExtra(RgbVideoIdentityActivity.USER_ID);
            User user = FaceManager.getManager().getUserById(userId);
            if (user != null) {
                ToastUtils.showShort("识别到：：：" + user.getUserInfo());
            }
        }
    }
}
