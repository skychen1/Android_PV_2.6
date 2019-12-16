package com.ruihua.face.recognition.config;

/**
 * describe ： 人脸识别参数配置项
 *
 * @author : Yich
 * date: 2019/3/14
 */
public class FaceConfig {
    private FaceConfig() {
    }

    /**
     * 高于7.0的系统的文件访问权限
     */
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;
    /**
     * 分组的id（默认只有一个分组）
     */
    public static final String USE_GROUP = "ruihua";
    /**
     * 人脸注册时，检测最大转动角度
     */
    public static final int FACE_REGISTER_ANGLE = 10;
    /**
     * 人脸检测分数
     */
    public static final double FACE_CONF = 0.99;

    /**
     * 人脸检测分数
     */
    public static final float FACE_LIVE = 0.8f;
    /**
     * 人脸识别时，检测人脸转动最大角度
     */
    public static final double FACE_RECOGNISE_ANGLE = 20;
    /**
     * 人脸是被分数（大于这个分数认为是同一个人）
     */
//    public static final double FACE_RECOGNISE_SCORE = 80;
    public static final double FACE_RECOGNISE_SCORE = 70;


}
