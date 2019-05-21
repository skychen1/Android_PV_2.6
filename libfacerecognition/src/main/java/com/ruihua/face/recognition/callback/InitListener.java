package com.ruihua.face.recognition.callback;

/**
 * describe ： 初始化回调监听
 *
 * @author : Yich
 * date: 2019/3/14
 */
public interface InitListener {
    /**
     * 初始化成功回调
     */
    void initSuccess();

    /**
     * 初始化失败
     *
     * @param errorCode 错误代码 参看百度sdk
     * @param msg       错误提示，
     */
    void initFail(int errorCode, String msg);
}
