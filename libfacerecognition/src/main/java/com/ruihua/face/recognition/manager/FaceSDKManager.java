package com.ruihua.face.recognition.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.license.AndroidLicenser;
import com.ruihua.face.recognition.ui.Activation;
import com.ruihua.face.recognition.utils.FileUitls;
import com.ruihua.face.recognition.utils.PreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * describe ：百度人脸sdk管理类（百度demo自带）
 *
 * @author : Yich
 * data: 2019/2/21
 */
public class FaceSDKManager {
    /**
     * 初始化状态的种类
     */
    public static final int SDK_UNACTIVATION = 1;
    public static final int SDK_UNINIT = 2;
    public static final int SDK_INITING = 3;
    public static final int SDK_INITED = 4;
    public static final int SDK_FAIL = 5;

    public static final String LICENSE_NAME = "idl-license.face-android";
    private FaceDetector faceDetector;
    private FaceFeature faceFeature;
    private Context context;
    private SdkInitListener sdkInitListener;
    public static volatile int initStatus = SDK_UNACTIVATION;
    private Handler handler = new Handler(Looper.getMainLooper());

    private FaceSDKManager() {
        faceDetector = new FaceDetector();
        faceFeature = new FaceFeature();
    }

    private static class HolderClass {
        private static final FaceSDKManager instance = new FaceSDKManager();
    }

    public static FaceSDKManager getInstance() {
        return HolderClass.instance;
    }

    public int initStatus() {
        return initStatus;
    }

    public void setSdkInitListener(SdkInitListener sdkInitListener) {
        this.sdkInitListener = sdkInitListener;
    }

    public FaceDetector getFaceDetector() {
        return faceDetector;
    }

    public FaceFeature getFaceFeature() {
        return faceFeature;
    }


    /**
     * FaceSDK 初始化，用户可以根据自己的需求实例化FaceTracker 和 FaceRecognize
     *
     * @param context 上下文
     */
    public void init(final Context context) {
        this.context = context;
        //建测是否授权，没授权不再运行
        if (!check()) {
            initStatus = SDK_UNACTIVATION;
            return;
        }
        PreferencesUtil.initPrefs(context.getApplicationContext());
        final String key = PreferencesUtil.getString("activate_key", "");
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(context, "激活序列号为空, 请先激活", Toast.LENGTH_SHORT).show();
            return;
        }

        initStatus = SDK_INITING;
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(new Runnable() {
            @Override
            public void run() {
                if (sdkInitListener != null) {
                    sdkInitListener.initStart();
                }
                Log.e("FaceSDK", "初始化授权");
                FaceSDK.initLicense(context, key, LICENSE_NAME, false);
                if (!sdkInitStatus()) {
                    return;
                }
                Log.e("FaceSDK", "初始化sdk");
                faceDetector.init(context);
                faceFeature.init(context);
                initLiveness(context);
            }
        });
    }

    /**
     * 初始化 活体检测
     *
     * @param context
     */
    private void initLiveness(Context context) {
        FaceSDK.livenessSilentInit(context, FaceSDK.LivenessTypeId.LIVEID_VIS, 2);
        FaceSDK.livenessSilentInit(context, FaceSDK.LivenessTypeId.LIVEID_IR);
        FaceSDK.livenessSilentInit(context, FaceSDK.LivenessTypeId.LIVEID_DEPTH);
    }

    private boolean sdkInitStatus() {
        boolean success = false;
        int status = FaceSDK.getAuthorityStatus();
        if (status == AndroidLicenser.ErrorCode.SUCCESS.ordinal()) {
            initStatus = SDK_INITED;
            success = true;
            faceDetector.setInitStatus(initStatus);
            Log.e("FaceSDK", "授权成功");
            if (sdkInitListener != null) {
                sdkInitListener.initSuccess();
            }

        } else if (status == AndroidLicenser.ErrorCode.LICENSE_EXPIRED.ordinal()) {
            initStatus = SDK_FAIL;
            // FileUitls.deleteLicense(context, LICENSE_NAME);
            Log.e("FaceSDK", "授权过期");
            if (sdkInitListener != null) {
                sdkInitListener.initFail(status, "授权过期");
            }
            showActivation();
        } else {
            initStatus = SDK_FAIL;
            // FileUitls.deleteLicense(context, LICENSE_NAME);
            Log.e("FaceSDK", "授权失败" + status);
            if (sdkInitListener != null) {
                sdkInitListener.initFail(status, "授权失败");
            }
            showActivation();
        }
        return success;
    }

    /**
     * 检查设备是否授权
     *
     * @return 是否
     */
    public boolean check() {
        //如果没有授权
        if (!FileUitls.checklicense(context, LICENSE_NAME)) {
            //展示授权弹窗
            showActivation();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 展示弹窗界面
     */
    private void showActivation() {
        //如果已经激活
        if (FaceSDK.getAuthorityStatus() == AndroidLicenser.ErrorCode.SUCCESS.ordinal()) {
            Toast.makeText(context, "已经激活成功", Toast.LENGTH_LONG).show();
        }
        //如果没有激活，就展示弹窗界面
        final Activation activation = new Activation(context);
        //设置弹窗操作回调
        activation.setActivationCallback(new Activation.ActivationCallback() {
            @Override
            public void callback(boolean success) {
                //收到授权操作回调，就要隐藏授权操作框
                initStatus = SDK_UNINIT;
                activation.hide();
                init(context);
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                activation.show();
            }
        });

    }


    public interface SdkInitListener {

        public void initStart();

        public void initSuccess();

        public void initFail(int errorCode, String msg);
    }


}