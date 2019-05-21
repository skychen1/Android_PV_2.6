/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.face;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.entity.RtspClient;

/**
 * 该类封装了，RTSP网络摄像头。
 */
public class RtspImageSource extends ImageSource {
    private String url;
    private PreviewView previewView;
    private HandlerThread previewThread = new HandlerThread("rtsp preview");
    private Handler previewHandler;
    private Handler uiHandler;
    private Bitmap bitmap;
    private ImageFrame lastFrame;

    public RtspImageSource() {
        RtspClient.getInstance().addBitmapListener(frameListener);
    }

    /**
     * 网络视频流地址。具体格式见rtsp协议
     *
     * @param url 视频流地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void start() {
        super.start();
        RtspClient.getInstance().start(url);
        if (previewView != null) {
            previewThread.start();
            previewHandler = new Handler(previewThread.getLooper());
            uiHandler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    public void stop() {
        super.stop();
        RtspClient.getInstance().stop();
    }

    private OnFrameAvailableListener frameListener = new OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(ImageFrame frame) {
            if (previewView != null) {
                lastFrame = frame;
                previewView.setPreviewSize(frame.getWidth(), frame.getHeight());
                previewHandler.post(drawPreviewRun);
                // drawPreviewRun.run();
            }
            for (OnFrameAvailableListener listener : getListeners()) {
                listener.onFrameAvailable(frame);
            }
        }
    };

    @Override
    public void setPreviewView(PreviewView previewView) {
        super.setPreviewView(previewView);
        this.previewView = previewView;
    }

    private Matrix matrix = new Matrix();

//    private Runnable drawPreviewRun = new Runnable() {
//        @Override
//        public void run() {
//            Canvas canvas = previewView.getTextureView().lockCanvas();
//            if (canvas == null) {
//                return;
//            }
//            if (bitmap == null || bitmap.getWidth() != lastFrame.getWidth() || bitmap.getHeight() != lastFrame
//                    .getHeight()) {
//                bitmap = Bitmap.createBitmap(lastFrame.getWidth(), lastFrame.getHeight(), Bitmap.Config.ARGB_8888);
//            }
//            bitmap.setPixels(lastFrame.getArgb(), 0, lastFrame.getWidth(), 0, 0, lastFrame.getWidth(),
//                    lastFrame.getHeight());
//            matrix.reset();
//            float scale = 1.0f * previewView.getTextureView().getWidth() / lastFrame.getWidth();
//            matrix.setScale(scale, scale);
//            canvas.drawBitmap(bitmap, matrix, null);
//            //TODO matrix
//            previewView.getTextureView().unlockCanvasAndPost(canvas);
//        }
//    };

    private Runnable drawPreviewRun = new Runnable() {
        @Override
        public void run() {
            if (bitmap == null || bitmap.getWidth() != lastFrame.getWidth() || bitmap.getHeight() != lastFrame
                    .getHeight()) {
                bitmap = Bitmap.createBitmap(lastFrame.getWidth(), lastFrame.getHeight(), Bitmap.Config.ARGB_8888);
            }
            bitmap.setPixels(lastFrame.getArgb(), 0, lastFrame.getWidth(), 0, 0, lastFrame.getWidth(),
                    lastFrame.getHeight());
            runUiThread();

        }
    };

    private void runUiThread() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = previewView.getTextureView().lockCanvas();
                if (canvas == null) {
                    return;
                }
                long start = System.currentTimeMillis();

                matrix.reset();
                float scale = 1.0f * previewView.getTextureView().getWidth() / lastFrame.getWidth();
                matrix.setScale(scale, scale);
                canvas.drawBitmap(bitmap, matrix, null);

                previewView.getTextureView().unlockCanvasAndPost(canvas);
            }
        });
    }
}
