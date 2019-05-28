package com.ruihua.libvideorecord.callback;

/**
 * describe ：网络摄像头数据流连接回调
 *
 * @author : Yich
 * date: 2019/4/11
 */
public interface OnRtspConnectCallback {

    /**
     * 设备连接的回调
     *
     * @param isSuccess 连接是否成功
     * @param code      返回代码
     * @param msg       提示
     */
    void onConnect(boolean isSuccess, int code, String msg);
}
