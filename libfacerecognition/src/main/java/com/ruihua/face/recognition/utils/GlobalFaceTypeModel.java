package com.ruihua.face.recognition.utils;

/**
 * describe ： 类型
 *
 * @author : Yich
 * date: 2019/3/13
 */
public class GlobalFaceTypeModel {
    private GlobalFaceTypeModel() {
    }

    /**
     * 摄像头选择
     */
    public static final String TYPE_CAMERA = "TYPE_CAMERA";
    public static final int ORBBEC = 1;
    public static final int IMIMECT = 2;
    public static final int ORBBECPRO = 3;

    /**
     * 活体选择
     */
    public static final String TYPE_LIVENSS = "TYPE_LIVENSS";
    public static final int TYPE_NO_LIVENSS = 1;
    public static final int TYPE_RGB_LIVENSS = 2;
    public static final int TYPE_RGB_IR_LIVENSS = 3;
    public static final int TYPE_RGB_DEPTH_LIVENSS = 4;
    public static final int TYPE_RGB_IR_DEPTH_LIVENSS = 5;


}
