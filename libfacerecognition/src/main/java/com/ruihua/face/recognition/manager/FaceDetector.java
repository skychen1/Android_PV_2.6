/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.manager;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;
import com.ruihua.face.recognition.entity.ImageFrame;

public class FaceDetector {

    /**
     * 检测结果代码 成功
     */
    public static final int OK = FaceTracker.ErrCode.OK.ordinal();
    public static final int PITCH_OUT_OF_DOWN_MAX_RANGE = FaceTracker.ErrCode.PITCH_OUT_OF_DOWN_MAX_RANGE.ordinal();
    public static final int PITCH_OUT_OF_UP_MAX_RANGE = FaceTracker.ErrCode.PITCH_OUT_OF_UP_MAX_RANGE.ordinal();
    public static final int YAW_OUT_OF_LEFT_MAX_RANGE = FaceTracker.ErrCode.YAW_OUT_OF_LEFT_MAX_RANGE.ordinal();
    public static final int YAW_OUT_OF_RIGHT_MAX_RANGE = FaceTracker.ErrCode.YAW_OUT_OF_RIGHT_MAX_RANGE.ordinal();
    public static final int POOR_ILLUMINATION = FaceTracker.ErrCode.POOR_ILLUMINATION.ordinal();
    public static final int HIT_LAST = FaceTracker.ErrCode.DATA_HIT_LAST.ordinal();
    /**
     * 检测结果代码 没有检测到人脸， 人脸太小（必须打于最小检测人脸minFaceSize），或者人脸角度太大，人脸不是朝上
     */
    public static final int NO_FACE_DETECTED = FaceTracker.ErrCode.NO_FACE_DETECTED.ordinal();
    public static final int DATA_NOT_READY = FaceTracker.ErrCode.DATA_NOT_READY.ordinal();
    public static final int DATA_HIT_ONE = FaceTracker.ErrCode.DATA_HIT_ONE.ordinal();
    public static final int DATA_HIT_LAST = FaceTracker.ErrCode.DATA_HIT_LAST.ordinal();
    public static final int IMG_BLURED = FaceTracker.ErrCode.IMG_BLURED.ordinal();
    public static final int OCCLUSION_LEFT_EYE = FaceTracker.ErrCode.OCCLUSION_LEFT_EYE.ordinal();
    public static final int OCCLUSION_RIGHT_EYE = FaceTracker.ErrCode.OCCLUSION_RIGHT_EYE.ordinal();
    public static final int OCCLUSION_NOSE = FaceTracker.ErrCode.OCCLUSION_NOSE.ordinal();
    public static final int OCCLUSION_MOUTH = FaceTracker.ErrCode.OCCLUSION_MOUTH.ordinal();
    public static final int OCCLUSION_LEFT_CONTOUR = FaceTracker.ErrCode.OCCLUSION_LEFT_CONTOUR.ordinal();
    public static final int OCCLUSION_RIGHT_CONTOUR = FaceTracker.ErrCode.OCCLUSION_RIGHT_CONTOUR.ordinal();
    public static final int OCCLUSION_CHIN_CONTOUR = FaceTracker.ErrCode.OCCLUSION_CHIN_CONTOUR.ordinal();
    public static final int FACE_NOT_COMPLETE = FaceTracker.ErrCode.FACE_NOT_COMPLETE.ordinal();
    public static final int UNKNOW_TYPE = FaceTracker.ErrCode.UNKNOW_TYPE.ordinal();

    private Context context;
    private FaceTracker mFaceTracker;
    private int initStatus = FaceSDKManager.SDK_UNINIT;
    private FaceEnvironment faceEnvironment = new FaceEnvironment();

    /**
     * FaceSDK 初始化，用户可以根据自己的需求实例化FaceTracker 和 FaceRecognize ，具体功能参考文档
     *
     * @param context
     */
    public void init(final Context context) {
        this.context = context;
        init(context, faceEnvironment);
    }

    public void init(final Context context, FaceEnvironment environment) {
        this.context = context;
        this.faceEnvironment = environment;

        if (mFaceTracker == null) {
            mFaceTracker = new FaceTracker(context);
            mFaceTracker.set_isFineAlign(false);
            mFaceTracker.set_isVerifyLive(false);
            mFaceTracker.set_isCheckQuality(environment.isCheckQuality());
            mFaceTracker.set_DetectMethodType(1);
            mFaceTracker.set_isCheckQuality(environment.isCheckQuality());
            mFaceTracker.set_notFace_thr(environment.getNotFaceThreshold());
            mFaceTracker.set_min_face_size(environment.getMiniFaceSize());
            mFaceTracker.set_cropFaceSize(FaceEnvironment.VALUE_CROP_FACE_SIZE);
            mFaceTracker.set_illum_thr(environment.getIlluminationThreshold());
            mFaceTracker.set_blur_thr(environment.getBlurrinessThreshold());
            mFaceTracker.set_occlu_thr(environment.getOcclulationThreshold());
            mFaceTracker.set_max_reg_img_num(FaceEnvironment.VALUE_MAX_CROP_IMAGE_NUM);
            mFaceTracker.set_eulur_angle_thr(
                    environment.getPitch(),
                    environment.getYaw(),
                    environment.getRoll()
            );
            // mFaceTracker.set_track_by_detection_interval(50);
        }
    }

    public FaceEnvironment getFaceEnvironment() {
        return faceEnvironment;
    }

    public void setFaceEnvironment(FaceEnvironment environment) {
        this.faceEnvironment = environment;
    }

    public void setInitStatus(int status) {
        this.initStatus = status;
    }


    /**
     * 进行人脸检测。返回检测结果代码。如果返回值为DETECT_CODE_OK 可调用 getTrackedFaces 获取人脸相关信息。
     *
     * @param argb   人脸argb_8888图片。
     * @param width  图片宽度
     * @param height 图片高度
     * @return 检测结果代码。
     */
    public int detect(int[] argb, int width, int height) {
        if (initStatus != FaceSDKManager.SDK_INITED) {
            return UNKNOW_TYPE;
        }
        int minDetectFace = getFaceEnvironment().getMiniFaceSize();
        if (width < minDetectFace || height < minDetectFace) {
            return NO_FACE_DETECTED;
        }
        return this.mFaceTracker
//                .prepare_data_for_verify(argb, height, width, FaceSDK.ImgType.ARGB.ordinal(),
//                        FaceTracker.ActionType.RECOGNIZE.ordinal());
                .prepare_max_face_data_for_verify(argb, height, width, FaceSDK.ImgType.ARGB.ordinal(),
                        FaceTracker.ActionType.RECOGNIZE.ordinal());
    }

    /**
     * 进行人脸检测。返回检测结果代码。如果返回值为DETECT_CODE_OK 可调用 getTrackedFaces 获取人脸相关信息。
     *
     * @param imageFrame 人脸图片帧
     * @return 检测结果代码。
     */
    public int detect(ImageFrame imageFrame) {
        if (initStatus != FaceSDKManager.SDK_INITED) {
            return UNKNOW_TYPE;
        }
        return detect(imageFrame.getArgb(), imageFrame.getWidth(), imageFrame.getHeight());
    }

    public void detectMultiFace(ImageFrame imageFrame, int multiFaceNumber) {
        detectMultiFace(imageFrame.getArgb(), imageFrame.getWidth(), imageFrame.getHeight(), multiFaceNumber);
    }

    public void detectMultiFace(int[] argb, int width, int height, int multiFaceNumber) {
        this.mFaceTracker
//                .prepare_data_for_verify(argb, height, width, FaceSDK.ImgType.ARGB.ordinal(),
//                        FaceTracker.ActionType.RECOGNIZE.ordinal());
                .track(argb, height, width, FaceSDK.ImgType.ARGB.ordinal(),
                        multiFaceNumber);
    }

    /**
     * yuv图片转换为相应的argb;
     *
     * @param yuv      yuv_420p图片
     * @param width    图片宽度
     * @param height   图片高度
     * @param argb     接收argb用得 int数组
     * @param rotation yuv图片的旋转角度
     * @param mirror   是否为镜像
     */
    public static void yuvToARGB(byte[] yuv, int width, int height, int[] argb, int rotation, int mirror) {
        FaceSDK.getARGBFromYUVimg(yuv, argb, width, height, rotation, mirror);
    }

    /**
     * 获取当前跟踪的人脸信息。
     *
     * @return 返回人脸信息，没有时返回null
     */
    public FaceInfo[] getTrackedFaces() {
        return mFaceTracker.get_TrackedFaceInfo();
    }

    /**
     * 获取当前跟踪的人脸信息。只返回一个。
     *
     * @return 返回人脸信息，没有时返回null
     */
    public FaceInfo getTrackedFace() {
        FaceInfo[] faces = mFaceTracker.get_TrackedFaceInfo();
        if (faces != null && faces.length > 0) {
            return mFaceTracker.get_TrackedFaceInfo()[0];
        }
        return null;
    }

    /**
     * 重置跟踪人脸。下次将重新开始跟踪。
     */
    public void clearTrackedFaces() {
        if (mFaceTracker != null) {
            mFaceTracker.clearTrackedFaces();
        }
    }


    /**
     * 根据设备的cpu核心数设定人脸sdk使用的线程数，如双核设置为2，四核设置为4
     *
     * @param numberOfThreads
     */
    public void setNumberOfThreads(int numberOfThreads) {
        FaceSDK.setNumberOfThreads(numberOfThreads);
    }

    /**
     * 设置人脸概率阈值。范围是0-1。1是最严格，基本不存在？
     *
     * @param threshold 人脸概率阈值
     */
    public void setNotFaceThreshold(float threshold) {
        this.faceEnvironment.setNotFaceThreshold(threshold);
        if (mFaceTracker != null) {
            mFaceTracker.set_notFace_thr(threshold);
        }
    }

    /**
     * 设置最小检测人脸（两个眼睛之间的距离）小于此值的人脸检测不出来。范围为80-200。该值会严重影响检测性能。
     * 设置为100的性能损耗大概是200的4倍。所以在满足要求的前提下尽量设置大一些。默认值为 @see (DEFAULT_MIN_FACE_SIZE)
     *
     * @param faceSize 最小可检测人脸大小。
     */
    public void setMinFaceSize(@IntRange(from = 80, to = 200) int faceSize) {
        this.faceEnvironment.setMinFaceSize(faceSize);
        if (mFaceTracker != null) {
            mFaceTracker.set_min_face_size(faceSize);
        }
    }

    /**
     * 设置最低光照强度（YUV中的Y分量）取值范围0-255，建议值大于40.
     *
     * @param threshold 最低光照强度。
     */
    public void setIlluminationThreshold(float threshold) {
        this.faceEnvironment.setIlluminationThreshold(threshold);
        if (mFaceTracker != null) {
            mFaceTracker.set_illum_thr(threshold);
        }
    }

    /**
     * 设置模糊度。取值范围为0-1;0表示特别清晰，1表示，特别模糊。默认值为 @see(DEFAULT_BLURRINESS_THRESHOLD)。
     *
     * @param threshold 模糊度
     */
    public void setBlurrinessThreshold(@FloatRange(from = 0, to = 1) float threshold) {
        this.faceEnvironment.setBlurrinessThreshold(threshold);
        if (mFaceTracker != null) {
            mFaceTracker.set_blur_thr(threshold);
        }
    }

    /**
     * 人脸遮挡阀值
     *
     * @param threshold
     */
    public void setOcclulationThreshold(float threshold) {
        this.faceEnvironment.setOcclulationThreshold(threshold);
        if (mFaceTracker != null) {
            mFaceTracker.set_occlu_thr(threshold);
        }
    }

    /**
     * 设置是否检测质量
     *
     * @param checkQuality 是否检测质量
     */
    public void setCheckQuality(boolean checkQuality) {
        this.faceEnvironment.setCheckQuality(checkQuality);
        if (mFaceTracker != null) {
            mFaceTracker.set_isCheckQuality(checkQuality);
        }
    }

    // yaw 左右
    // pitch 上下
    // roll 扭头

    /**
     * 设置头部欧拉角，大于这个值的人脸将不能识别。
     *
     * @param yaw   左右摇头的角度。
     * @param roll  顺时针扭头的角度
     * @param pitch 上下点头的角度。
     */
    public void setEulerAngleThreshold(int yaw, int roll, int pitch) {
        this.faceEnvironment.setYaw(yaw);
        this.faceEnvironment.setPitch(pitch);
        this.faceEnvironment.setRoll(roll);
    }

    /**
     * 检测间隔设置，单位ms.该值控制检测间隔。值越大，检测时间越长，性能消耗越低。值越小，能更快的检测到人脸。
     *
     * @param interval 间隔时间，单位ms;
     */
    public void setDetectInterval(int interval) {
        this.faceEnvironment.setDetectInterval(interval);
        if (mFaceTracker != null) {
            mFaceTracker.set_detect_in_video_interval(interval);
        }
    }

    public void setTrackInterval(int interval) {
        this.faceEnvironment.setTrackInterval(interval);
        if (mFaceTracker != null) {
            mFaceTracker.set_track_by_detection_interval(interval);
        }
    }

}
