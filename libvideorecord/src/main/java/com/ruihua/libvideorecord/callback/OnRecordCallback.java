package com.ruihua.libvideorecord.callback;

/**
 * describe ： 视频录制回调接口；
 *
 * @author : Yich
 * date: 2019/4/15
 */
public interface OnRecordCallback {
    /**
     * 录制是否成功，
     *
     * @param isSuccess true为成功，false为失败
     */
    void onRecordResult(boolean isSuccess);
}
