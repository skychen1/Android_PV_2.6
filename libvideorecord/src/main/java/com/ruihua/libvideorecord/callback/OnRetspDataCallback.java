package com.ruihua.libvideorecord.callback;

/**
 * describe ： 网络rtsp数据流解析返回的数据
 *
 * @author : Yich
 * date: 2019/4/11
 */
public interface OnRetspDataCallback {

    /**
     * 解析正确数据的回调
     *
     * @param argb   argb的图片数据
     * @param width  宽度
     * @param height 高度
     */
    void onData(int[] argb, int width, int height);
}
