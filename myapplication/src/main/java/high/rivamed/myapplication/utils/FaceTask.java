package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.utils.FileUitls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.AllFacePhotoBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;

import static high.rivamed.myapplication.cont.Constants.FACE_PHOTO;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/6/10
 * 描述：1.获取所有待注册人脸照，
 * 2.1 检测如果本地已注册且是最新更新的照片，则不用再次注册；
 * 2.2 否则，缓存图片至本地，
 * 3.1.已注册但不是最新图片，删除已注册的底照，再次注册
 * 3.2.未注册，注册
 * 人脸注册不能批量注册，只能每次注册一张，注册完成后再注册下一张
 */
public class FaceTask {

    private String TAG = "FaceTask";

    public interface CallBack {
        void finishRegister(boolean hasRegister,String msg);
    }

    //记录当前缓存的图片
    private int index;
    //人脸照缓存路径
    private File faceLibrary;
    //待注册的人脸
    private List<AllFacePhotoBean.UsersBean> updatePhotoList;
    //注册完成后缓存最新的人脸照数据
    private String updateFaceJson = "";
    private Gson mGson;
    private Activity _mActivity;
    private CallBack callBack;

    public FaceTask(Activity _mActivity) {
        this._mActivity = _mActivity;
        index = 0;
        mGson = new Gson();
        updatePhotoList = new ArrayList<>();
        faceLibrary = FileUitls.getBatchFaceDirectory("cache_face_library");
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 获取所有人脸信息，缓存至本地，注册至本地人脸照底库
     */
    public void getAllFaceAndRegister() {
        NetRequest.getInstance().getAllFace(_mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                updateFaceJson = result;
                AllFacePhotoBean facePhotoBean = mGson.fromJson(result, AllFacePhotoBean.class);
                if (null == facePhotoBean||!facePhotoBean.isOperateSuccess()) {
                    if (callBack != null)
//                        callBack.finishRegister(false,"暂时没有人脸照数据");
                        callBack.finishRegister(false,null);
                    return;
                }
                List<AllFacePhotoBean.UsersBean> facePhotoList = facePhotoBean.getUsers();
                //获取本地已缓存人脸数据
                String facePhotoJson = SPUtils.getString(UIUtils.getContext(), FACE_PHOTO, "");
                if (TextUtils.isEmpty(facePhotoJson)) {
                    updatePhotoList.addAll(facePhotoList);
                } else {
                    AllFacePhotoBean localFacePhotoBean = mGson.fromJson(facePhotoJson, AllFacePhotoBean.class);
                    List<AllFacePhotoBean.UsersBean> localFacePhotoList = localFacePhotoBean.getUsers();
                    LogUtils.d("Face", "localFacePhotoList   "+localFacePhotoList.size());
                    if (facePhotoList!=null){
                        new Thread(()->{
                            for (int i = 0; i < facePhotoList.size(); i++) {
                                AllFacePhotoBean.UsersBean photoBean = facePhotoList.get(i);
                                int index = localFacePhotoList.indexOf(photoBean);
                                if (index==-1) {
                                    //本地没有缓存
                                    updatePhotoList.add(photoBean);
                                } else {
                                    //有本地缓存，但不是最新更新的图片，也需要重新更新
                                    AllFacePhotoBean.UsersBean bean = localFacePhotoList.get(index);
                                    if (bean.getFaceUpdateTime().compareTo(photoBean.getFaceUpdateTime()) < 0) {
                                        //最新更新时间大于本地更新时间，更新底库
                                        photoBean.setUpdate(true);
                                        updatePhotoList.add(photoBean);
                                    }
                                }
                            }
                        }).start();
                    }

                }
                //没有新照片
                if (updatePhotoList.size() == 0){
                    if (callBack != null)
                        //                                callBack.finishRegister(false,"暂时没有待注册人脸照数据");
                        callBack.finishRegister(false,null);
                    return;
                }
                cacheFace();
            }

            @Override
            public void onError(String result) {
                ToastUtils.showShort(R.string.connection_fails);
                if (callBack != null)
                    callBack.finishRegister(false,"获取人脸照链接失败，请检查网络！");
            }
        });
    }

    /**
     * 缓存人脸照
     */
    private void cacheFace() {
        String faceUrl = updatePhotoList.get(index).getFaceFileName();
        if (TextUtils.isEmpty(faceUrl)){
            next();
            return;
        }
        OkGo.<File>get(faceUrl).tag(_mActivity)//
//                .params("systemType", SYSTEMTYPE)
                .execute(new FileCallback(faceLibrary.getAbsolutePath(), updatePhotoList.get(index).getUserId() + ".jpg") {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(Response<File> response) {
                        //缓存至本地，注册人脸
                        registerFaceLibrary(updatePhotoList.get(index), response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
//                        mDialog.setProgress((int) (progress.fraction / -1024));
                        super.downloadProgress(progress);

                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        //继续注册下一张
                        next();
                    }
                });
    }

    /**
     * 注册至人脸底库
     */
    private void registerFaceLibrary(AllFacePhotoBean.UsersBean facePhotoBean, File file) {
        String userId = facePhotoBean.getUserId();
        if (facePhotoBean.isUpdate()) {
            //更新：需要先删除本地已注册人脸，再重新注册
            if (!FaceManager.getManager().deleteFace(userId)) {
                LogUtils.d(TAG, "删除人脸底照失败：：" + userId);
                //继续注册下一张
                next();
                return;
            }
        }
        FaceManager.getManager().registerFace(userId, facePhotoBean.getName(), file.getAbsolutePath(),
                (code, msg) -> {
                    LogUtils.d(TAG, "LoginFace:人脸注册结果：：code=" + code + ":::msg=" + msg + ":::userId=" + userId+ ":::userName=" +  facePhotoBean.getName());
                    if (code == FaceCode.CODE_REGISTER_SUCCESS) {
                        //注册成功即可删除缓存照片
                        file.delete();
                    }
                    //注册完成，继续注册下一张
                    next();
                });
    }

    private void next() {
        if (updatePhotoList.size() - 1 > index) {
            index++;
            cacheFace();
        } else {
            LogUtils.d("Face", "人脸照注册完成11111");
            if (callBack != null)
                callBack.finishRegister(true, "人脸照注册完成");
            LogUtils.d("Face", "人脸照注册完成");
            SPUtils.putString(UIUtils.getContext(), FACE_PHOTO, updateFaceJson);
            _mActivity = null;
        }
    }
}
