package com.ruihua.face.recognition.callback;

/**
 * describe ：人脸注册回调监听的回调
 *
 * @author : Yich
 * date: 2019/3/15
 */
public interface FaceRegisterCallback {

    /**
     * 注册结果回调
     *
     * @param code 返回值代码
     * @param msg  错误提示内容
     */
    void onRegisterResult(int code, String msg);
}
