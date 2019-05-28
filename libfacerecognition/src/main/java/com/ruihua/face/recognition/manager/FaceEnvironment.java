/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.manager;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

import com.baidu.idl.facesdk.FaceSDK;

/**
 * SDK全局配置信息
 */
public class FaceEnvironment {

    // SDK版本号
    public static final String VERSION = "1.1.0";

        // SDK配置参数
    public static final float VALUE_BRIGHTNESS = 40f;
    public static final float VALUE_BLURNESS = 0.5f;
    public static final float VALUE_OCCLUSION = 0.5f;
    public static final int VALUE_HEAD_PITCH = 45;
    public static final int VALUE_HEAD_YAW = 45;
    public static final int VALUE_HEAD_ROLL = 45;
    public static final int VALUE_CROP_FACE_SIZE = 400;
    public static final float VALUE_NOT_FACE_THRESHOLD = 0.6f;
    public static final boolean VALUE_IS_CHECK_QUALITY = false;
    public static final int VALUE_DECODE_THREAD_NUM = 2;
    public static final int VALUE_LIVENESS_DEFAULT_RANDOM_COUNT = 3;
    public static final int VALUE_MAX_CROP_IMAGE_NUM = 1;

    public static final float LIVENESS_RGB_THRESHOLD = 0.8f;
    public static final float LIVENESS_IR_THRESHOLD = 0.8f;
    public static final float LIVENESS_DEPTH_THRESHOLD = 0.8f;

    /**
     * 默认非人脸阈值
     */
    public static final float DEFAULT_NOT_FACE_THRESHOLD = 0.8f;

    /**
     * 默认最小人脸，小于此值的人脸将检测不出来
     */
    public static final int VALUE_MIN_FACE_SIZE = 80;

    // 根据设备的cpu核心数设定人脸sdk使用的线程数，如双核设置为2，四核设置为4
    private int numberOfThreads;
    // 设置人脸概率阈值。范围是0-1。1是最严格
    private float notFaceThreshold = DEFAULT_NOT_FACE_THRESHOLD;
    // 设置最小检测人脸（两个眼睛之间的距离）小于此值的人脸检测不出来。范围为80-200。该值会严重影响检测性能。
    // 设置为100的性能损耗大概是200的4倍。所以在满足要求的前提下尽量设置大一些。默认值为 @see (DEFAULT_MIN_FACE_SIZE)
    private int miniFaceSize = VALUE_MIN_FACE_SIZE;
    // 设置最低光照强度（YUV中的Y分量）取值范围0-255，建议值大于40.
    private float illuminationThreshold = VALUE_BRIGHTNESS;
    // 设置模糊度。取值范围为0-1;0表示特别清晰，1表示，特别模糊。默认值为 @see(DEFAULT_BLURRINESS_THRESHOLD)。
    private float blurrinessThreshold = VALUE_BLURNESS;
    //
    private float OcclulationThreshold = VALUE_OCCLUSION ;
    // 设置是否检测质量, 开启后性能会下降
    private boolean checkQuality = VALUE_IS_CHECK_QUALITY;
    // 左右摇头的角度阈值 比如-15~15
    private int yaw = VALUE_HEAD_YAW;
    // 顺时针扭头的角度
    private int roll = VALUE_HEAD_ROLL;
    // 上下点头的角度。
    private int pitch = VALUE_HEAD_PITCH;
    // 检测间隔，越小越快检测人脸，可设置50ms+ ，越小性能越差
    private int detectInterval = 50;
    // 人脸跟踪间隔
    private int trackInterval = 200;

    /**
     * 根据设备的cpu核心数设定人脸sdk使用的线程数，如双核设置为2，四核设置为4
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
        this.notFaceThreshold = threshold;
    }

    /**
     * 设置最小检测人脸（两个眼睛之间的距离）小于此值的人脸检测不出来。范围为80-200。该值会严重影响检测性能。
     * 设置为100的性能损耗大概是200的4倍。所以在满足要求的前提下尽量设置大一些。默认值为 @see (DEFAULT_MIN_FACE_SIZE)
     *
     * @param faceSize 最小可检测人脸大小。
     */
    public void setMinFaceSize(@IntRange(from = 80, to = 200) int faceSize) {
        this.miniFaceSize = faceSize;
    }

    /** 设置最低光照强度（YUV中的Y分量）取值范围0-255，建议值大于40.
     * @param threshold 最低光照强度。
     */
    public void setIlluminationThreshold(float threshold) {
        this.illuminationThreshold = threshold;
    }

    /**
     * 设置模糊度。取值范围为0-1;0表示特别清晰，1表示，特别模糊。默认值为 @see(DEFAULT_BLURRINESS_THRESHOLD)。
     *
     * @param threshold 模糊度
     */
    public void setBlurrinessThreshold(@FloatRange(from = 0, to = 1) float threshold) {
        this.blurrinessThreshold = threshold;
    }

    public void setOcclulationThreshold(float threshold) {
        this.OcclulationThreshold = threshold;
    }

    /**
     * 设置是否检测质量
     *
     * @param checkQuality 是否检测质量
     */
    public void setCheckQuality(boolean checkQuality) {
        this.checkQuality = checkQuality;
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
        this.yaw = yaw;
        this.roll = roll;
        this.pitch = pitch;
    }

    /**
     * 检测间隔设置，单位ms.该值控制检测间隔。值越大，检测时间越长，性能消耗越低。值越小，能更快的检测到人脸。
     * @param interval 间隔时间，单位ms;
     */
    public void setDetectInterval(int interval) {
        this.detectInterval = interval;
    }

    public void setTrackInterval(int interval) {
        this.trackInterval = interval;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public float getNotFaceThreshold() {
        return notFaceThreshold;
    }

    public int getMiniFaceSize() {
        return miniFaceSize;
    }

    public void setMiniFaceSize(int miniFaceSize) {
        this.miniFaceSize = miniFaceSize;
    }

    public float getIlluminationThreshold() {
        return illuminationThreshold;
    }

    public float getBlurrinessThreshold() {
        return blurrinessThreshold;
    }

    public float getOcclulationThreshold() {
        return OcclulationThreshold;
    }

    public boolean isCheckQuality() {
        return checkQuality;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getDetectInterval() {
        return detectInterval;
    }

    public int getTrackInterval() {
        return trackInterval;
    }
}
