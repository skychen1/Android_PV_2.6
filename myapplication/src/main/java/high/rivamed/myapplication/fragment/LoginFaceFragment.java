package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.face.TexturePreviewView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.utils.FileUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;

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
    private String TAG = "高Face";

    @Override
    public int getLayoutId() {
        return R.layout.login_face_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        //识别成功的回调 回调用户的编号userId，唯一标识，回调速度可能会很快，需要自己加标识控制处理；
        if (FaceManager.getManager().getInitStatus()== FaceCode.SDK_INITED)
            FaceManager.getManager().initIdentityFace(_mActivity, previewView, textureView, this::loginFace);
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
            if (0==FaceManager.getManager().getFaceLibraryNum()){
                ToastUtils.showShortSafe("人脸识别底库无数据");
            }else {
                //可以开启识别
                FaceManager.getManager().startIdentity();
            }
        }else {
            // 停止检测
            FaceManager.getManager().stopIdentity();
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

    //人脸照缓存路径
    String cacheFaceDir = FileUtils.getDiskCacheDir(UIUtils.getContext())+"/face";
    //记录当前缓存的图片
    private int index=0;
    //图片列表
    private List<String> imageList=new ArrayList<>();
    private void registerFaceLibrary(String userId,String userName,File file){
        FaceManager.getManager().registerFace(userId, userName, file.getAbsolutePath(),
                (code, msg) -> {
                    LogUtils.i("LoginFace","人脸注册结果：：code=" + code + ":::msg=" + msg);
                    if (code==FaceCode.CODE_REGISTER_SUCCESS){
                        //注册成功即可删除缓存照片
                        file.delete();
                        //注册成功，继续注册下一张
                        if (imageList.size()-1>index) {
                            index++;
                            cacheFace();
                        }
                    }
                });
    }

    private void cacheFace(){
        String image = imageList.get(index);
        OkGo.<File>get(MAIN_URL + "").tag(this)//
                .params("systemType",SYSTEMTYPE)
                .execute(new FileCallback(cacheFaceDir, "faceName.jpg") {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(Response<File> response) {
                        //缓存至本地，注册人脸
                        registerFaceLibrary("","",response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
//                        mDialog.setProgress((int) (progress.fraction / -1024));
                        super.downloadProgress(progress);

                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.connection_fails);
                    }
                });
    }
}
