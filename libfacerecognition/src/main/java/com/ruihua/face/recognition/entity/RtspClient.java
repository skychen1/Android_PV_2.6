/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.entity;

import android.util.Log;


import com.ruihua.face.recognition.face.OnFrameAvailableListener;

import java.util.ArrayList;

public class RtspClient {

    private RtspErrorListener listener;

    static {
        System.loadLibrary("rtsp");
    }

    private static RtspClient instance = null;

    public static RtspClient getInstance() {
        if (instance == null) {
            synchronized(RtspClient.class) {
                if (instance == null) {
                    instance = new RtspClient();
                }
            }
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    public RtspClient() {
        nativePointer = nativeInit();
    }

    /**
     * c++对象指针。
     */
    private long nativePointer;

    /**
     * 初始化本地库
     * @return 指针。
     */
    private native long nativeInit();

    private native void nativeStart(long pointer, String url);

    private native void nativeRestart(long pointer);

    private native void nativeStop(long pointer);

    private native void nativeRelease(long pointer);

    private ArrayList<OnFrameAvailableListener> listeners = new ArrayList<>();

    public void start(String rtspUrl) {
        nativeStart(nativePointer, rtspUrl);
    }

    public void restart() {
        nativeRestart(nativePointer);
    }

    public void stop() {
        nativeStop(nativePointer);
    }

    public void release() {
        nativeRelease(nativePointer);
    }

    public synchronized void addBitmapListener(OnFrameAvailableListener ml) {
        listeners.add(ml);
    }

    public synchronized void clearBitmapListener() {
        listeners.clear();
    }

    public void setRtspErrorListener(RtspErrorListener l) {
        listener = l;
    }

    // used by jni code
    @SuppressWarnings("unused")
    private void onError(int code, String msg) {
        Log.e("wtf", "errorFromNative-> code:" + code + " msg:" + msg);
        if (listener != null) {
            listener.rtspError(code, msg);
        }
    }

    @SuppressWarnings("unused")
    private void onData(int[] argb, int width, int height) {
        synchronized (this) {
            for (int i = 0; i < listeners.size(); i++) {
                OnFrameAvailableListener ml = listeners.get(i);
                // TODO
                ImageFrame e = new ImageFrame(argb, width, height);
                ml.onFrameAvailable(e);
            }
        }
    }

    public interface RtspErrorListener {
        void rtspError(int code, String message);
    }
}
