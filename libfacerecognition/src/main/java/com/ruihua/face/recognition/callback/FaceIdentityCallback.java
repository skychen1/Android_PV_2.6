package com.ruihua.face.recognition.callback;

/**
 * describe ： 人脸识别结果回调
 *
 * @author : Yich
 * date: 2019/3/19
 */
public interface FaceIdentityCallback {
    /**
     * 识别结果回调
     *
     * @param userId 识别到的人员的userId
     */
    void onIdentityResult(String userId);
}
