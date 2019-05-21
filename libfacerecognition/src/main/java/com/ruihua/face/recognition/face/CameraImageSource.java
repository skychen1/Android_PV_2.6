/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.face.camera.Camera1Control;
import com.ruihua.face.recognition.face.camera.Camera2Control;
import com.ruihua.face.recognition.face.camera.ICameraControl;
import com.ruihua.face.recognition.manager.FaceDetector;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * 封装了系统做机做为输入源。
 */
public class CameraImageSource extends ImageSource {

    /**
     * 相机控制类
     */
    private ICameraControl cameraControl;
    private Context context;

    public ICameraControl getCameraControl() {
        return cameraControl;
    }

    private ArgbPool argbPool = new ArgbPool();

    private int cameraFaceType = ICameraControl.CAMERA_FACING_FRONT;

    public void setCameraFacing(int type) {
        this.cameraFaceType = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] imageToByteArray(Image image) {
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[2].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[1].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        // U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        uBuffer.get(nv21, ySize, vSize);
        vBuffer.get(nv21, ySize + vSize, uSize);

        return nv21;
    }

    public CameraImageSource(Context context) {
        this.context = context;
        cameraControl = new Camera1Control(getContext());
        cameraControl.setCameraFacing(cameraFaceType);
        cameraControl.setOnFrameListener(new ICameraControl.OnFrameListener<byte[]>() {
            @Override
            public void onPreviewFrame(byte[] data, int rotation, int width, int height) {
                int[] argb = argbPool.acquire(width, height);

                if (argb == null || argb.length != width * height) {
                    argb = new int[width * height];
                }

                rotation = rotation < 0 ? 360 + rotation : rotation;
                long starttime = System.currentTimeMillis();
                FaceDetector.yuvToARGB(data, width, height, argb, rotation, 0);

                // 旋转了90或270度。高宽需要替换
                if (rotation % 180 == 90) {
                    int temp = width;
                    width = height;
                    height = temp;
                }

                ImageFrame frame = new ImageFrame();
                frame.setArgb(argb);
                frame.setWidth(width);
                frame.setHeight(height);
                frame.setPool(argbPool);
                ArrayList<OnFrameAvailableListener> listeners = getListeners();
                for (OnFrameAvailableListener listener : listeners) {
                    listener.onFrameAvailable(frame);
                }
                argbPool.release(argb);
            }
        });
    }

    private void addCamera2Control() {
        cameraControl = new Camera2Control(getContext());
        cameraControl.setCameraFacing(cameraFaceType);
        cameraControl.setOnFrameListener(new ICameraControl.OnFrameListener<Bitmap>() {
            @Override
            public void onPreviewFrame(Bitmap data, int rotation, int width, int height) {
                int[] argb = argbPool.acquire(width, height);

                if (argb == null || argb.length != width * height) {
                    argb = new int[width * height];
                }

                rotation = rotation < 0 ? 360 + rotation : rotation;
                long starttime = System.currentTimeMillis();
                data.getPixels(argb, 0, width, 0, 0, width, height);

                // 旋转了90或270度。高宽需要替换
                if (rotation % 180 == 90) {
                    int temp = width;
                    width = height;
                    height = temp;
                }

                ImageFrame frame = new ImageFrame();
                frame.setArgb(argb);
                frame.setWidth(width);
                frame.setHeight(height);
                frame.setPool(argbPool);
                ArrayList<OnFrameAvailableListener> listeners = getListeners();
                for (OnFrameAvailableListener listener : listeners) {
                    listener.onFrameAvailable(frame);
                }
            }
        });
    }


    @Override
    public void start() {
        super.start();
        cameraControl.start();
    }

    @Override
    public void stop() {
        super.stop();
        cameraControl.stop();
    }

    private Context getContext() {
        return context;
    }

    @Override
    public void setPreviewView(PreviewView previewView) {
        cameraControl.setPreviewView(previewView);
    }
}
