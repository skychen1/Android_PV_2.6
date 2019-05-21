/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.face;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.baidu.idl.facesdk.FaceInfo;
import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.manager.FaceSDKManager;

import java.util.ArrayList;

/**
 * 封装了人脸检测的整体逻辑。
 */
public class FaceDetectManager {
    /**
     * 该回调用于回调，人脸检测结果。当没有人脸时，infos 为null,status为 FaceDetector.DETECT_CODE_NO_FACE_DETECTED
     */
    public interface OnFaceDetectListener {
        void onDetectFace(int status, FaceInfo[] infos, ImageFrame imageFrame);
    }

    public FaceDetectManager(Context context) {
    }

    /**
     * 图片源，获取检测图片。
     */
    private ImageSource imageSource;
    /**
     * 人脸检测事件监听器
     */
    private OnFaceDetectListener listener;
    private FaceFilter faceFilter = new FaceFilter();
    private HandlerThread processThread;
    private Handler processHandler;
    private Handler uiHandler;
    private ImageFrame lastFrame;
    private boolean useDetect = false;

    private ArrayList<FaceProcessor> preProcessors = new ArrayList<>();

    public void setUseDetect(boolean useDetect) {
        this.useDetect = useDetect;
        // 传给facesdk的图片高宽不同，将不能正确检出人脸，需要clear前面的trackedFaces
        FaceSDKManager.getInstance().getFaceDetector().clearTrackedFaces();
    }

    /**
     * 设置人脸检测监听器，检测后的结果会回调。
     *
     * @param listener 监听器
     */
    public void setOnFaceDetectListener(OnFaceDetectListener listener) {
        this.listener = listener;
    }

    /**
     * 设置图片帧来源
     *
     * @param imageSource 图片来源
     */
    public void setImageSource(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    /**
     * @return 返回图片来源
     */
    public ImageSource getImageSource() {
        return this.imageSource;
    }


    /**
     * 增加处理回调，在人脸检测前会被回调。
     *
     * @param processor 图片帧处理回调
     */
    public void addPreProcessor(FaceProcessor processor) {
        preProcessors.add(processor);
    }

    /**
     * 设置人检跟踪回调。
     *
     * @param onTrackListener 人脸回调
     */
    public void setOnTrackListener(FaceFilter.OnTrackListener onTrackListener) {
        faceFilter.setOnTrackListener(onTrackListener);
    }

    /**
     * @return 返回过虑器
     */
    public FaceFilter getFaceFilter() {
        return faceFilter;
    }

    public void start() {
        LogUtil.init();
        this.imageSource.addOnFrameAvailableListener(onFrameAvailableListener);
        processThread = new HandlerThread("process");
        processThread.setPriority(9);
        processThread.start();
        processHandler = new Handler(processThread.getLooper());
        uiHandler = new Handler();
        this.imageSource.start();
    }

    private Runnable processRunnable = new Runnable() {
        @Override
        public void run() {
            if (lastFrame == null) {
                return;
            }
            int[] argb;
            int width;
            int height;
            ArgbPool pool;
            synchronized (lastFrame) {
                argb = lastFrame.getArgb();
                width = lastFrame.getWidth();
                height = lastFrame.getHeight();
                pool = lastFrame.getPool();
                lastFrame = null;
            }
            process(argb, width, height, pool);
        }
    };

    public void stop() {
        if (imageSource != null) {
            this.imageSource.stop();
            this.imageSource.removeOnFrameAvailableListener(onFrameAvailableListener);
        }

        if (processThread != null) {
            processThread.quit();
            processThread = null;
        }
    }

    boolean skip = false;

    private void process(int[] argb, int width, int height, ArgbPool pool) {
//        if (skip) {
//            skip = !skip;
//            return;
//        } else {
//            skip = !skip;
//        }

        int value = 0;

        ImageFrame frame = imageSource.borrowImageFrame();
        frame.setArgb(argb);
        frame.setWidth(width);
        frame.setHeight(height);
        frame.setPool(pool);
        //        frame.retain();

        for (FaceProcessor processor : preProcessors) {
            if (processor.process(this, frame)) {
                break;
            }
        }
        if (useDetect) {
            long starttime = System.currentTimeMillis();
            value = FaceSDKManager.getInstance().getFaceDetector().detect(frame);
            //FaceSDKManager.getInstance().getFaceDetector().detectMultiFace(frame,5);
            FaceInfo[] faces = FaceSDKManager.getInstance().getFaceDetector().getTrackedFaces();
//            if (faces != null) {
//                Log.e("faceMulti", faces.length + "");
//            }
            if (value == 0) {
                faceFilter.filter(faces, frame);
            }
            if (listener != null) {
                listener.onDetectFace(value, faces, frame);
            }
        }

        frame.release();
    }

    private OnFrameAvailableListener onFrameAvailableListener = new OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(ImageFrame imageFrame) {
            lastFrame = imageFrame;
//            processHandler.removeCallbacks(processRunnable);
//            processHandler.post(processRunnable);
//            uiHandler.removeCallbacks(processRunnable);
//            uiHandler.post(processRunnable);
            processRunnable.run();
        }
    };
}
