package high.rivamed.myapplication.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.model.Progress;
import com.ruihua.libvideorecord.RtspManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import high.rivamed.myapplication.bean.VideoUploadResultBean;
import high.rivamed.myapplication.http.FileUpResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.PowerDateUtils;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/22
 * 描述：开柜视频录制，关柜门停止录制并上传
 *
 * 目前暂时是每次录制都开rtsp流，录制完关闭rtsp流
 *
 * 考虑到rtsp流开关闭时有一定延时，所以后面需要根据实际柜子的数量
 * 在app启动后就开启所有的rtsp流连接，退出app时才关闭流，这样在开关柜时只需要开启停止录制就行了
 * 但不知道底层是否允许同时连接多个rtsp流
 */
public class VideoRecordTask {
    private final String TAG = "VideoRecordTask";
    private String filePath;//视频缓存绝对路径：默认为开启录制时间
    private String rtspUrl;//指定柜子对应的网络摄像头rtsp url
    private String deviceCode;//柜子ID
    private String startDate;//开始录制时间
    private String endDate;//结束录制时间
    private Object tag;
    private String cacheVideoDir;

    public VideoRecordTask(String rtspUrl, String deviceCode, Object tag) {
        this.rtspUrl = rtspUrl;
        this.deviceCode = deviceCode;
        this.tag = tag;
        //初始化视频缓存路径：/storage/emulated/0/Android/data/high.rivamed.myapplication/cache/VID_20190306_182403.mp4
        cacheVideoDir = FileUtils.getDiskCacheDir(UIUtils.getContext())+"/video";

        //注册视频录制回调，录制成功时上传录制视频
        RtspManager.getManager().registerOnRecordCallback(rtspUrl, isSuccess -> {
            //4.关闭rtsp流连接
            RtspManager.getManager().stopRtsp(rtspUrl);
            if (isSuccess){
                //5.视频上传
                File file = new File(filePath);
                NetRequest.getInstance()
                        .uploadVideoFile(this.deviceCode,
                                file,
                                this.startDate,
                                this.endDate,
                                this.tag,
                                new FileUpResult() {
                                    @Override
                                    public void uploadProgress(Progress progress) {
                                        int curProgress = (int) (progress.fraction / -1024);
                                        LogUtils.d(TAG, "uploadProgress 视频上传进度: " + curProgress);
                                    }

                                    @Override
                                    public void onSucceed(String result) {
                                        try {
                                            VideoUploadResultBean data = new Gson().fromJson(result, VideoUploadResultBean.class);
                                            if (data.isOperateSuccess()) {
                                                LogUtils.d(TAG, "视频上传成功");
                                                file.delete();
                                            } else {
                                                LogUtils.d(TAG, "视频上传失败");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogUtils.d(TAG, "视频上传失败");
                                        }
                                    }

                                    @Override
                                    public void onSucceedButNoData() {

                                    }

                                    @Override
                                    public void onNetFailing(String result) {
                                        if (TextUtils.isEmpty(result)) {
                                            ToastUtils.showShort("链接失败，请检查网络！");
                                        } else {
                                            ToastUtils.showShort(result);
                                        }
                                    }

                                    @Override
                                    public void onDataError(int errorCode, String msg) {

                                    }

                                    @Override
                                    public void onError(String result) {
                                        LogUtils.d(TAG, "视频上传失败:" + result);
                                    }
                                });
            }else {
                LogUtils.d(TAG, "视频录制失败:" );
            }
        });
    }

    private void connectAllRtsp(){
        //rtsp流开启有一定的延时，所以在App启动后就开启连接
    }

    public void startRecord() {
        //1.连接视频流
        RtspManager.getManager().startRtsp(rtspUrl, (isSuccess, code, msg) -> {
            if (isSuccess) {
                //2.连接成功，开启视频录制
                startDate = PowerDateUtils.getDates();
                String cacheVideoName = "VID_" + PowerDateUtils.dateSimpleFormat(new Date(), new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".mp4";
                filePath = cacheVideoDir + "/" + cacheVideoName;
                RtspManager.getManager().startEncode(rtspUrl, filePath);
            } else {

            }
        });
    }

    public void stopRecord() {
        //3.停止录制
        endDate = PowerDateUtils.getDates();
        RtspManager.getManager().stopEncode(rtspUrl);
    }
}
