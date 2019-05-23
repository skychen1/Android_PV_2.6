package com.ruihua.libvideorecord;

import com.baidu.aip.RtspClient;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.libvideorecord.callback.OnRecordCallback;
import com.ruihua.libvideorecord.callback.OnRetspDataCallback;
import com.ruihua.libvideorecord.callback.OnRtspConnectCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * describe ： 网络摄像头，rtsp流管理类(单例模式)
 * 流数据回调，视频录制等
 *
 * @author : Yich
 * date: 2019/4/11
 */
public class RtspManager {

    private static RtspManager manager;

    private RtspManager() {
    }

    public static synchronized RtspManager getManager() {
        if (manager == null) {
            manager = new RtspManager();
        }
        return manager;
    }

    /**
     * 保存所有连接的集合，用url作为唯一标识
     */
    private Map<String, RtspClient> mClients = new HashMap<>();

    /**
     * 添加已连接的设备到集合中
     *
     * @param url    设备地址
     * @param client 设备管理类
     */
    public void addConnectedClient(String url, RtspClient client) {
        if (mClients.containsKey(url)) {
            mClients.remove(url);
        }
        mClients.put(url, client);
    }

    /**
     * 删除断开连接的设备
     *
     * @param url 设备地址
     */
    public void delDisConnectClient(String url) {
        if (mClients.containsKey(url)) {
            mClients.remove(url);
        }
    }

    /**
     * 打开rtsp流 每次只能打开一个数据流，如果已经打开一个数据流，就需要关闭上一个流才行
     *
     * @param rtspUrl rtsp流地址；
     */
    public void startRtsp(String rtspUrl, OnRtspConnectCallback callback) {
        RtspClient mClient = mClients.get(rtspUrl);
        if (mClient != null) {
            if (callback != null) {
                callback.onConnect(false, FunctionCode.DEVICE_ALREADY_CONNECTED, "设备已连接");
            }
            return;
        }
        RtspClient rtspClient = new RtspClient();
        rtspClient.registerOnRtspConnectCallback(callback);
        rtspClient.start(rtspUrl);
    }

    /**
     * 停止获取rtsp流
     */
    public void stopRtsp(String rtspUrl) {
        RtspClient rtspClient = mClients.get(rtspUrl);
        if (rtspClient != null) {
            rtspClient.stop();
            mClients.remove(rtspUrl);
        }
    }

    /**
     * 开始录制视频
     *
     * @param rtspUrl 设备地址
     * @return 返回码
     */
    public int startEncode(String rtspUrl, String filePath) {
        RtspClient rtspClient = mClients.get(rtspUrl);
        if (rtspClient == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return rtspClient.startRecord(filePath);
    }

    /**
     * 停止录制
     *
     * @param rtspUrl 设备地址
     */
    public void stopEncode(String rtspUrl) {
        RtspClient rtspClient = mClients.get(rtspUrl);
        if (rtspClient != null) {
            rtspClient.stopRecord();
        }
    }


    /**
     * 注册某个摄像的照片数据回调，根据url地址注册，
     * 注意：：：：必须在这个设备打开成功之后才能注册回调，才能注册成功
     *
     * @param rtspUrl  设备地址
     * @param callback 数据回调
     */
    public void registerOnRetspDataCallback(String rtspUrl, OnRetspDataCallback callback) {
        RtspClient rtspClient = mClients.get(rtspUrl);
        if (rtspClient != null) {
            rtspClient.registerOnRetspDataCallback(callback);
        }
    }

    /**
     * 反注册数据回调，如果网络摄像头要持续打开，又不再需要照片数据回调的时候，必须反注册，防止内存泄露
     *
     * @param rtspUrl 设备的地址
     */
    public void unRegisterOnRetspDataCallback(String rtspUrl) {
        RtspClient rtspClient = mClients.get(rtspUrl);
        if (rtspClient != null) {
            rtspClient.unRegisterOnRetspDataCallback();
        }
    }


    public void registerOnRecordCallback(String rtspUrl, OnRecordCallback callback) {
        RtspClient rtspClient = mClients.get(rtspUrl);
        if (rtspClient != null) {
            rtspClient.registerOnRecordCallback(callback);
        }
    }
}
