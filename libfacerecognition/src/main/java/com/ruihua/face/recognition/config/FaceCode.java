package com.ruihua.face.recognition.config;

/**
 * describe ： 人脸识别返回参数代码集合
 *
 * @author : Yich
 * date: 2019/3/18
 */
public class FaceCode {
    private FaceCode() {
    }

    /**
     * 注册成功
     */
    public static final int CODE_REGISTER_SUCCESS = 0;

    /**
     * 人脸注册参数错误
     */
    public static final int CODE_REGISTER_PARAM_ERROR = 100;
    /**
     * 照片中没有合格的人脸
     */
    public static final int CODE_REGISTER_NO_FACE = 101;
    /**
     * 人脸特征抽取失败
     */
    public static final int CODE_REGISTER_DETECT_FAILED = 102;
    /**
     * 注册失败，原因未知
     */
    public static final int CODE_REGISTER_FAILED = 103;

    /**
     * SDK初始化的状态返回值
     * SDK_NOT_ACTIVE 未激活
     * SDK_NOT_INIT 未初始化
     * SDK_INITING 正在初始化
     * SDK_INITED 初始化成功
     * SDK_INIT_FAIL初始化失败
     */
    public static final int SDK_NOT_ACTIVE = 1;
    public static final int SDK_NOT_INIT = 2;
    public static final int SDK_INITING = 3;
    public static final int SDK_INITED = 4;
    public static final int SDK_INIT_FAIL = 5;


}
