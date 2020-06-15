package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihua.libfacerecognitionv3.main.camera.AutoTexturePreviewView;
import com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager;
import com.ruihua.libfacerecognitionv3.main.model.SingleBaseConfig;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HomeAuthorityMenuBean;
import high.rivamed.myapplication.dbmodel.AccountVosBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.LoginUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;

import static com.ruihua.libfacerecognitionv3.main.presenter.FaceManager.CODE_SUCCESS;
import static high.rivamed.myapplication.cont.Constants.LOGIN_TYPE_FACE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/21
 * 描述：人脸识别登录
 */
public class LoginFaceFragment extends SimpleFragment {
//        @BindView(R.id.preview_view)
    AutoTexturePreviewView previewView;
    @BindView(R.id.rl_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.texture_view)
    TextureView textureView;
    @BindView(R.id.tv_hint)
    TextView textHint;
    private String TAG = "Face";
    private String faceId;
    private boolean startIdentity;

    @Override
    public int getLayoutId() {
        return R.layout.login_face_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        //识别成功的回调 回调用户的编号userId，唯一标识，回调速度可能会很快，需要自己加标识控制处理；
//        if (FaceManager.getManager().getInitStatus() == FaceCode.SDK_INITED)
//            FaceManager.getManager().initIdentityFace(_mActivity, previewView, textureView, (isSuccess, faceId) -> {
//                if (isSuccess) {
//                    loginFace(faceId);
//                }
//            });


        // 注册默认开启质量检测
        SingleBaseConfig.getBaseConfig().setQualityControl(true);
        FaceSDKManager.getInstance().initConfig();
    }

    /**
     * 使用人脸特征id进行登录
     */
    private void loginFace(String Id) {
        EventBusUtils.postSticky(new Event.EventLoading(true));
        Log.e(TAG, "loginFace: " + Id);
        faceId = Id;
        //获取配置项并登陆
        LoginUtils.getConfigDate(mContext, (canLogin, canDevice, hasNet) ->
                textHint.post(() -> {
                    if (canLogin) {
                        loginEnjoin(canDevice, hasNet);
                    }
                }));
    }


    /**
     * 是否禁止使用
     *
     * @param canDevice
     * @param hasNet
     */
    private void loginEnjoin(boolean canDevice, boolean hasNet) {
        if (canDevice) {
            textHint.setText("");
            LoginActivity.mLoginGone.setVisibility(View.GONE);
            if (hasNet) {
                //有网
                loadLogin();
            } else {
                //离线，从本地数据库查询
                unNetFaceLogin();
            }
        } else {
            EventBusUtils.postSticky(new Event.EventLoading(false));
            textHint.setText("正在维护，请到管理端启用");
            LoginActivity.mLoginGone.setVisibility(View.VISIBLE);
            ToastUtils.showShortToast("正在维护，请到管理端启用");
        }

    }

    /**
     * 离线登录
     */
    private void unNetFaceLogin() {
        List<AccountVosBean> beans = LitePal.where("faceId = ? ", faceId).find(AccountVosBean.class);
        LogUtils.i("LoginA", "LitePal   " + mGson.toJson(beans));
        if (beans != null && beans.size() > 0) {
            //设置离线数据:当前用户离线登陆所需数据
            List<HomeAuthorityMenuBean> fromJson = LoginUtils.setUnNetSPdate(beans.get(0).getAccountName(), mGson);
            //设置菜单权限并跳转至首页
            LoginUtils.setMenuDateAndStart(fromJson, mGson, _mActivity, hasMenu -> {
                if (hasMenu) {
                    //销毁人脸识别sdk
                    destroyFaceSDK();
                    _mActivity.finish();
                } else {
                    //开启重新预览
                    onTabShowPreview(true);
                }
            });
        } else {
            ToastUtils.showShortToast("登录失败，暂无登录信息！");
            //开启重新预览
            onTabShowPreview(true);
        }
        EventBusUtils.postSticky(new Event.EventLoading(false));
    }

    /**
     * 获取登录
     */
    private void loadLogin() {
        HashMap<String, String> param = new HashMap<>();
        param.put("faceId", faceId);
        param.put("systemType", SYSTEMTYPE);
        param.put("loginType",LOGIN_TYPE_FACE);
        param.put("thingId", SPUtils.getString(mContext, THING_CODE));
        param.put("deptId", SPUtils.getString(mContext, SAVE_DEPT_CODE));
        LogUtils.i("Login", "THING_CODE   " + mGson.toJson(param));
        NetRequest.getInstance().userLoginByUserId(mGson.toJson(param), _mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                EventBusUtils.postSticky(new Event.EventLoading(false));
                LogUtils.i("getAppAccountInfoVo", "result  " + result);
                LoginUtils.loginSpDate(result, mContext, mGson, hasMenu -> {
                    if (hasMenu) {
                        //销毁人脸识别sdk
                        destroyFaceSDK();
                        _mActivity.finish();
                    } else {
                        //开启重新预览
                        onTabShowPreview(true);
                    }
                });
            }

            @Override
            public void onError(String result) {
                EventBusUtils.postSticky(new Event.EventLoading(false));
                LogUtils.i("BaseSimpleFragment", "登录失败  " + result);
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                //开启重新预览
                onTabShowPreview(true);
            }
        });
    }


    @Override
    public void onBindViewBefore(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyFaceSDK();
    }

    public void destroyFaceSDK() {
        //在确定长时间不使用人脸识别，或者需要使用人脸检测注册人脸底库时，要销毁人脸识别初始
        LogUtils.d(TAG, "destroyIdentity---::::::::::: ");
        FaceManager.getManager().releaseRegister();
    }

    //控制相机预览，开启人脸识别
    public void onTabShowPreview(boolean isShow) {
        if (isShow && (LoginActivity.mLoginGone==null||LoginActivity.mLoginGone.getVisibility() == View.GONE)) {
            if (startIdentity)
                return;
            faceId = "";
            int initStatus = FaceSDKManager.initStatus;
            if (initStatus == FaceSDKManager.SDK_UNACTIVATION) {
                textHint.post(() -> textHint.setText("SDK还未激活，请先激活"));
            } else if (initStatus == FaceSDKManager.SDK_INIT_FAIL) {
                textHint.post(() -> textHint.setText("SDK初始化失败，请重新激活初始化"));
            } else if (initStatus == FaceSDKManager.SDK_INIT_SUCCESS) {
                textHint.post(() -> textHint.setText("SDK正在加载模型，请稍后再试"));
            } else if (initStatus == FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
                //在开启人脸识别以前需要获取人脸照片数量，
                //如果为0表示没有底库，是不能开启识别的；
                FaceManager.getManager().queryAllUserCount(count -> {
                    textHint.post(() -> {
                        //在开启人脸识别以前需要获取人脸照片数量，
                        //如果为0表示没有底库，是不能开启识别的；
                        if (count > 0) {
                            //可以开启识别
                            previewView = new AutoTexturePreviewView(getActivity());
                            previewView.setScaleX(-1);
                            previewView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                            relativeLayout.addView(previewView);
                            FaceManager.getManager().initShowFrame(textureView);
                            FaceManager.getManager().faceStartRGBSearch(getActivity(), previewView, (code, msg, user) -> {
                                if (code == CODE_SUCCESS) {
                                    String userId = user.getUserName();
                                    if (!TextUtils.isEmpty(userId)) {
                                        onTabShowPreview(false);
                                        loginFace(userId);
                                    }
                                    Log.e("Face", "onTip code:" + code + " msg:" + msg + " user:" + user.toString());
                                } else {
                                    Log.d("Face", "onTip code:" + code + " msg:" + msg);
                                }
                            });
                            startIdentity = true;
                            LogUtils.d(TAG, "startIdentity---::::::::::: " + startIdentity);
                            textHint.setText("");
                        } else {
                            textHint.setText("人脸识别底库无数据");
                        }
                    });
                });
            }
        } else {
            // 停止检测
            LogUtils.d(TAG, "stopIdentity---::::::::::: ");
            FaceManager.getManager().stopPreviewAndRelease();
            relativeLayout.removeAllViews();
            startIdentity = false;
            textHint.post(() -> textHint.setText(""));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        Log.e(TAG, "onSupportInvisible: ");
        onTabShowPreview(false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        Log.e(TAG, "onSupportVisible: ");
        onTabShowPreview(true);
    }
}
