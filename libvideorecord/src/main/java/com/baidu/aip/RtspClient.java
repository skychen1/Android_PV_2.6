package com.baidu.aip;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.ruihua.libvideorecord.Mp4Encoder;
import com.ruihua.libvideorecord.RtspManager;
import com.ruihua.libvideorecord.TranscordUtils;
import com.ruihua.libvideorecord.callback.OnRecordCallback;
import com.ruihua.libvideorecord.callback.OnRetspDataCallback;
import com.ruihua.libvideorecord.callback.OnRtspConnectCallback;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 单例模式
 * describe ：继续ffmpeg封装的RTSP流解析so文件调用类；
 *
 * @author : Yich
 * date: 2019/4/10
 */
public class RtspClient {
    //加载so文件
    static {
        System.loadLibrary("rtsp");
    }

    /**
     * 初始化本地库
     *
     * @return 指针(用于其他方法调用传入的参数)。
     */
    public native long nativeInit();

    /**
     * 开始流解析
     *
     * @param pointer 指针，初始化得到值
     * @param url     rtsp流的完整引用地址
     */
    public native void nativeStart(long pointer, String url);

    /**
     * 重新开启流解析
     *
     * @param pointer 指针
     */
    public native void nativeRestart(long pointer);

    /**
     * 停止流解析
     *
     * @param pointer 指针
     */
    public native void nativeStop(long pointer);

    /**
     * 释放资源
     *
     * @param pointer 指针
     */
    public native void nativeRelease(long pointer);


    /**
     * isFirst 是不是第一次收到数据，如果是，就回调了解成功，添加设备到管理类集合中，记录关键数据
     * mLastTime 收到上一帧数据的时间，如果超过5秒收不到数据就认为设备已经断开连接
     * isConnect rtsp流是否处于了解状态
     */

    private OnRtspConnectCallback mConnectCallback;
    private OnRetspDataCallback mDataCallback;
    private OnRecordCallback mRecordCallback;
    private boolean isFirst = true;
    private int colorFormat;
    private final int frameRate = 12;
    private Mp4Encoder mp4Encoder;
    private int width;
    private int height;
    private String url;
    private volatile boolean isRecording = false;
    private volatile long mLastTime;
    private ScheduledThreadPoolExecutor scheduled;
    private volatile boolean isConnect = false;
    /**
     * c++对象指针。
     */
    private long nativePointer;


    public RtspClient() {
        nativePointer = nativeInit();
        colorFormat = TranscordUtils.getColorFormat();
    }

    /**
     * 打开rtsp 流
     *
     * @param url rtsp地址；
     */
    public void start(String url) {
        this.url = url;
        nativeStart(nativePointer, url);
    }

    /**
     * 关闭流，同时就要释放地址；
     */
    public void stop() {
        nativeStop(nativePointer);
        nativeRelease(nativePointer);
    }

    /**
     * 开始录制视频
     *
     * @param filePath 保存识别的完整地址
     * @return 返回码
     */
    public int startRecord(String filePath) {
        //如果设备没有连接，就回调设备不存在
        if (!isConnect) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //如果有录像的类在就表示正在录像
        if (mp4Encoder != null) {
            return FunctionCode.DEVICE_BUSY;
        }
        mp4Encoder = new Mp4Encoder(width, height, frameRate, colorFormat, filePath);
        //注册录制回调
        mp4Encoder.registerOnRecordCallback(new MyRecordCallback());
        mp4Encoder.startEncode();
        isRecording = true;
        return FunctionCode.SUCCESS;
    }

    public void stopRecord() {
        //修改填数据的标识
        isRecording = false;
        if (mp4Encoder != null) {
            mp4Encoder.stopEncode();
        }
    }

    /**
     * 解析网络数据错误的回调
     *
     * @param code 回调错误的代码
     * @param msg  提示
     */
    @SuppressWarnings("unused")
    private void onError(int code, String msg) {
        LogUtils.e("rtsp打开错误code：" + code + "提示：：" + msg);
        isConnect = false;
        if (mConnectCallback != null) {
            mConnectCallback.onConnect(false, FunctionCode.CONNECT_FAILED, msg);
            //断开连接就删除已连接的设备
            RtspManager.getManager().delDisConnectClient(url);
            //连接的回调只需要回调一次之后就置为空
        }
    }

    /**
     * 解析正确数据的回调
     *
     * @param argb   argb的图片数据
     * @param width  宽度
     * @param height 高度
     */
    @SuppressWarnings("unused")
    private void onData(int[] argb, int width, int height) {
        //记录收到数据的时间
        mLastTime = System.currentTimeMillis();
        synchronized (this) {
            //回调连接成功，只有第一帧数据才会回调，如果在第一帧收到数据的额时候，没有设置回调，以后就再也收不到回调数据
            if (isFirst) {
                isFirst = false;
                isConnect = true;
                //记录图片的宽高，用于后期数据的处理
                this.width = width;
                this.height = height;
                //预处理一帧，因为第一次转码代码速度很慢
                TranscordUtils.getNV12(width, height, argb, colorFormat);
                //添加到已连接的集合中保存起来
                RtspManager.getManager().addConnectedClient(url, this);
                if (mConnectCallback != null) {
                    mConnectCallback.onConnect(true, FunctionCode.SUCCESS, "success");
                }
                //开启网络连接状态循环检测
                setConnectRate();
            }
            //有回调就回调数据
            if (mDataCallback != null) {
                mDataCallback.onData(argb, width, height);
            }
            //如果在录制就将数据传入录制类中
            if (isRecording) {
                //将数据转码成nv12
                byte[] nv12 = TranscordUtils.getNV12(width, height, argb, colorFormat);
                mp4Encoder.putDate(nv12);
            }
        }
    }

    /**
     * 监听rtsp流是否正常连接
     */
    private void setConnectRate() {
        if (scheduled == null) {
            //开启线程读取标签
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("rtsp-connect-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        }
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //如果5秒钟没有收到数据，认为设备断开连接了
                if (System.currentTimeMillis() - mLastTime >= 5 * 1000) {
                    //如果开始是连接的标识就修改为未连接
                    if (isConnect) {
                        isConnect = false;
                        if (mConnectCallback != null) {
                            mConnectCallback.onConnect(isConnect, FunctionCode.CONNECT_FAILED, "设备已断开");
                        }
                    }
                } else {
                    //如果收到的数据没有超过5秒，并且开始时断开的数据，就设置标识为已连接
                    if (!isConnect) {
                        isConnect = true;
                        if (mConnectCallback != null) {
                            mConnectCallback.onConnect(isConnect, FunctionCode.SUCCESS, "设备已连接");
                        }
                    }
                }

            }
        }, 50, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 录制结束回调
     */
    class MyRecordCallback implements OnRecordCallback {

        @Override
        public void onRecordResult(boolean isSuccess) {
            //录制结束了，就将录制工具置为空。标识可以录制
            mp4Encoder = null;
            //回调给用户
            if (mRecordCallback != null) {
                mRecordCallback.onRecordResult(isSuccess);
            }
        }
    }


    /**
     * 注册连接情况回调
     *
     * @param callback 回调接口
     */
    public void registerOnRtspConnectCallback(OnRtspConnectCallback callback) {
        this.mConnectCallback = callback;
    }

    public void unRegisterOnRtspConnectCallback() {
        this.mConnectCallback = null;
    }

    public void registerOnRetspDataCallback(OnRetspDataCallback callback) {
        this.mDataCallback = callback;
    }

    public void unRegisterOnRetspDataCallback() {
        this.mDataCallback = null;
    }

    public void registerOnRecordCallback(OnRecordCallback callback) {
        this.mRecordCallback = callback;
    }

}
