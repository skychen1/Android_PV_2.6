package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.face.TexturePreviewView;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/21
 * 描述：人脸识别登录
 */
public class LoginFaceFragment extends SimpleFragment {
    @BindView(R.id.preview_view)
    TexturePreviewView previewView;
    @BindView(R.id.texture_view)
    TextureView textureView;
    @BindView(R.id.tv_hint)
    TextView textHint;
    private String TAG = "高Face";

    @Override
    public int getLayoutId() {
        return R.layout.login_face_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        //识别成功的回调 回调用户的编号userId，唯一标识，回调速度可能会很快，需要自己加标识控制处理；
        if (FaceManager.getManager().getInitStatus()== FaceCode.SDK_INITED)
            FaceManager.getManager().initIdentityFace(_mActivity, previewView, textureView, (isSuccess, userId) -> {
                if (isSuccess){
                    loginFace(userId);
                }
            });
    }

    /**
     * 使用人脸特征id进行登录
     */
    private void loginFace(String userId) {
        //停止识别
        onTabShowPreview(false);
        // TODO: 2019/5/21 处理userId登录
        Log.e(TAG, "loginFace: "+userId );
    }


    @Override
    public void onBindViewBefore(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在确定长时间不使用人脸识别，或者需要使用人脸检测注册人脸底库时，要销毁人脸识别初始
        FaceManager.getManager().destroyIdentity();
    }

    //控制相机预览，开启人脸识别
    public void onTabShowPreview(boolean isShow){
        if (isShow){
            int initStatus = FaceManager.getManager().getInitStatus();
            if (initStatus == FaceCode.SDK_NOT_ACTIVE) {
                textHint.setText("人脸识别SDK还未激活，请先激活");
                return;
            } else if (initStatus == FaceCode.SDK_NOT_INIT) {
                textHint.setText("人脸识别SDK还未初始化，请先初始化");
                return;
            } else if (initStatus == FaceCode.SDK_INITING) {
                textHint.setText("人脸识别SDK正在初始化，请稍后再试");
                return;
            } else if (initStatus == FaceCode.SDK_INIT_FAIL) {
                textHint.setText("人脸识别SDK初始化失败，请重新初始化");
                return;
            }
            //在开启人脸识别以前需要获取人脸照片数量，
            //如果为0表示没有底库，是不能开启识别的；
            if (0==FaceManager.getManager().getFaceLibraryNum()){
                textHint.setText("人脸识别底库无数据");
            }else {
                //可以开启识别
                FaceManager.getManager().startIdentity();
                textHint.setText("");
            }
        }else {
            // 停止检测
            FaceManager.getManager().stopIdentity();
            textHint.setText("");
        }
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        onTabShowPreview(false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        onTabShowPreview(true);
    }

}
