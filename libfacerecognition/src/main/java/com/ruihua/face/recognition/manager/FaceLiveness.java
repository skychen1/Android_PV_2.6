/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.manager;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;
import com.ruihua.face.recognition.callback.ILivenessCallBack;
import com.ruihua.face.recognition.entity.LivenessModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FaceLiveness {

    private static final String TAG = "FaceLiveness";
    public static final int MASK_RGB = 0X0001;
    public static final int MASK_IR = 0X0010;
    public static final int MASK_DEPTH = 0X0100;

    public static FaceLiveness getInstance() {
        return FaceLiveness.HolderClass.instance;
    }

    private static class HolderClass {
        private static final FaceLiveness instance = new FaceLiveness();
    }

    private FaceLiveness() {
        es = Executors.newSingleThreadExecutor();
    }

    private Bitmap bitmap;
    private ILivenessCallBack livenessCallBack;

    private int[] nirRgbArray;

    private int[] mRgbArray;
    private volatile boolean isVisHavePixls = false;

    private byte[] mIrByte;
    private volatile boolean isIRHavePixls = false;

    private byte[] mDepthArray;
    private volatile boolean isDepthHavePixls;
    private ExecutorService es;
    private Future future;

    public void setLivenessCallBack(ILivenessCallBack callBack) {
        this.livenessCallBack = callBack;
    }

    /**
     * 设置可见光图
     *
     * @param bitmap
     */
    public void setRgbBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (mRgbArray == null) {
                mRgbArray = new int[bitmap.getWidth() * bitmap.getHeight()];
            }
            bitmap.getPixels(mRgbArray, 0, bitmap.getWidth(), 0, 0,
                    bitmap.getWidth(), bitmap.getHeight());
            this.bitmap = bitmap;
            isVisHavePixls = true;
        }
    }

    public void setNirRgbInt(int[] argbData) {
        if (nirRgbArray == null) {
            nirRgbArray = new int[argbData.length];
        }

        System.arraycopy(argbData, 0, nirRgbArray, 0, argbData.length);
        isVisHavePixls = true;
    }

    public void setRgbInt(int[] argbData) {
        if (mRgbArray == null) {
            mRgbArray = new int[argbData.length];
        }

        System.arraycopy(argbData, 0, mRgbArray, 0, argbData.length);
        isVisHavePixls = true;
    }

    private int[] byte2int(byte[] b) {
        // 数组长度对4余数
        int r;
        byte[] copy;
        if ((r = b.length % 4) != 0) {
            copy = new byte[b.length - r + 4];
            System.arraycopy(b, 0, copy, 0, b.length);
        } else {
            copy = b;
        }

        int[] x = new int[copy.length /4 + 1];
        int pos = 0;
        for (int i = 0; i < x.length - 1; i ++) {
            x[i] = (copy[pos] << 24 & 0xff000000) | (copy[pos+1] << 16 & 0xff0000) | (copy[pos+2] << 8 & 0xff00) | (copy[pos+3] & 0xff);
            pos += 4;
        }
        x[x.length - 1] = r;
        return x;
    }

    /**
     * 设置深度图
     *
     * @param irData
     */
    public void setIrData(byte[] irData) {

        if (irData == null) {
            return;
        }
        if (mIrByte == null) {
            mIrByte = new byte[irData.length];
        }

        System.arraycopy(irData, 0, mIrByte, 0, irData.length);
        isIRHavePixls = true;
    }


    /**
     * 设置深度图
     *
     * @param depthData
     */
    public void setDepthData(byte[] depthData) {

        if (mDepthArray == null) {
            mDepthArray = new byte[depthData.length];
        }

        System.arraycopy(depthData, 0, mDepthArray, 0, depthData.length);
        isDepthHavePixls = true;
    }


    public void clearInfo() {
        try {
            isDepthHavePixls = false;
            isIRHavePixls = false;
            isVisHavePixls = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float rgbLiveness(int[] data, int width, int height, int[] landmarks) {
        final float rgbScore = FaceSDK.run_livenessSilentPredict(FaceSDK.LivenessTypeId.LIVEID_VIS, data,
                height, width, 24, landmarks);
        return rgbScore;
    }

    public float irLiveness(byte[] data, int width, int height, int[] landmarks) {
        final float irScore = FaceSDK.run_livenessSilentPredictByte(FaceSDK.LivenessTypeId.LIVEID_IR, data,
                height, width, 0, landmarks);
        return irScore;
    }

    public float depthLiveness(byte[] data, int width, int height, int[] landmarks) {
        final float depthScore = FaceSDK.run_livenessSilentPredictByte(FaceSDK.LivenessTypeId.LIVEID_DEPTH, data,
                height, width, 2, landmarks);
        return depthScore;
    }

    public void livenessCheck(final int width, final int height, final int type) {

        if (future != null && !future.isDone()) {
            return;
        }
        future =  es.submit(new Runnable() {
            @Override
            public void run() {
                // Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                onLivenessCheck(width, height, type);

            }
        });
    }

    public void livenessCheck(final int width, final int height, final int type, final  int[] rgb,
                              final byte[] ir , final byte[] depth) {
        if (future != null && !future.isDone()) {
            return;
        }

        setDepthData(depth);
        setRgbInt(rgb);
        setIrData(ir);

        future =  es.submit(new Runnable() {
            @Override
            public void run() {
                onLivenessCheck(width, height, type);
            }
        });
    }


    // 活体检测
    private boolean onLivenessCheck(int width, int height, int type) {
        boolean isLiveness = false;
        // 判断当前是否有人脸
        long startTime = System.currentTimeMillis();
        int errorCode = FaceSDKManager.getInstance().getFaceDetector().detect(mRgbArray, width, height);

        LivenessModel livenessModel = new LivenessModel();
        livenessModel.setRgbDetectDuration(System.currentTimeMillis() - startTime);
        livenessModel.getImageFrame().setArgb(mRgbArray);
        livenessModel.getImageFrame().setWidth(width);
        livenessModel.getImageFrame().setHeight(height);
        livenessModel.setLiveType(type);
        livenessModel.setFaceDetectCode(errorCode);
        Log.i(TAG, "max_face_verification: " + errorCode +" duration:" + (System.currentTimeMillis() - startTime));

        if (errorCode == FaceTracker.ErrCode.OK.ordinal() || errorCode == FaceTracker.ErrCode.DATA_HIT_LAST.ordinal()) {
            FaceInfo[] trackedfaces = FaceSDKManager.getInstance().getFaceDetector().getTrackedFaces();
            livenessModel.setTrackFaceInfo(trackedfaces);
            if (trackedfaces != null && trackedfaces.length > 0) {
                FaceInfo faceInfo = trackedfaces[0];
                livenessModel.setFaceInfo(faceInfo);

                // 塞选人脸，可以调节距离、角度
                if (!filter(faceInfo, width, height)) {
                    livenessCallBack.onCallback(null);
                    return isLiveness;
                }

                livenessCallBack.onTip(0 , "活体判断中");

                float rgbScore = 0;
                if ((type & MASK_RGB) == MASK_RGB) {
                    startTime = System.currentTimeMillis();
                    rgbScore = rgbLiveness(mRgbArray, width, height, trackedfaces[0].landmarks);
                    livenessModel.setRgbLivenessScore(rgbScore);
                    livenessModel.setRgbLivenessDuration(System.currentTimeMillis() - startTime);
                }
                float irScore = 0;
                if ((type & MASK_IR) == MASK_IR) {
                    float maxWidth = 0;
                    int maxId = 0;
                    float detectScore = 0;
                    if (trackedfaces != null && trackedfaces.length > 0) {
                        for (int i = 0; i < trackedfaces.length; i++) {
                            if (trackedfaces[i].mWidth > maxWidth) {
                                maxId = i;
                                maxWidth = trackedfaces[i].mWidth;
                                detectScore = trackedfaces[i].mConf;
                            }
                        }
                    }
                    float[] faceT = new float[]{trackedfaces[maxId].mCenter_x, trackedfaces[maxId].mCenter_y, trackedfaces[maxId].mWidth, trackedfaces[maxId].mAngle};
                    int[] shape = new int[144];
                    int[] nPoint = new int[]{0};
                    float[] score = new float[]{0.0F};
                    FaceSDK.run_align(nirRgbArray,height,width, FaceSDK.ImgType.ARGB, FaceSDK.AlignMethodType.CDNN,faceT,shape,nPoint,score,detectScore);
                    livenessModel.setShape(shape);
                    startTime = System.currentTimeMillis();
//                    irScore = irLiveness(mIrByte, width, height, trackedfaces[0].landmarks);
                    irScore = irLiveness(mIrByte, width, height, shape);
                    livenessModel.setIrLivenessDuration(System.currentTimeMillis() - startTime);
                    livenessModel.setIrLivenessScore(irScore);
                }

                float depthScore = 0;
                if ((type & MASK_DEPTH) == MASK_DEPTH) {
                    startTime = System.currentTimeMillis();
                    depthScore = depthLiveness(mDepthArray, width, height, trackedfaces[0].landmarks);
                    livenessModel.setDetphtLivenessDuration(System.currentTimeMillis() - startTime);
                    livenessModel.setDepthLivenessScore(depthScore);

                }

                if (livenessCallBack != null ) {
                    livenessCallBack.onCallback(livenessModel);
                }

//                long time = System.currentTimeMillis();
//                saveRgbImage(String.valueOf(time), rgbScore, mRgbArray, width, height);
//                saveFile(String.valueOf(time), "nir", irScore, mIrByte);
//                saveFile(String.valueOf(time), "depth", depthScore, mDepthArray);

            }
        } else {
            checkFaceCode(errorCode);
            livenessCallBack.onCallback(null);
        }
        // clearInfo();
        FaceInfo[] trackedfaces = FaceSDKManager.getInstance().getFaceDetector().getTrackedFaces();
        livenessModel.setTrackFaceInfo(trackedfaces);
        if (livenessCallBack!=null){
            livenessCallBack.onCanvasRectCallback(livenessModel);
        }
        return isLiveness;
    }

    private boolean filter(FaceInfo faceInfo, int bitMapWidth, int bitMapHeight) {

        if (faceInfo.mConf < 0.6) {
            livenessCallBack.onTip(0, "人脸置信度太低");
            // clearInfo();
            return false;
        }

        float[] headPose = faceInfo.headPose;
        // Log.i("wtf", "headpose->" + headPose[0] + " " + headPose[1] + " " + headPose[2]);
        if (Math.abs(headPose[0]) > 15 || Math.abs(headPose[1]) > 15 || Math.abs(headPose[2]) > 15) {
            livenessCallBack.onTip(0, "人脸置角度太大，请正对屏幕");
            return false;
        }

        // 判断人脸大小，若人脸超过屏幕二分一，则提示文案“人脸离手机太近，请调整与手机的距离”；
        // 若人脸小于屏幕三分一，则提示“人脸离手机太远，请调整与手机的距离”
        float ratio = (float) faceInfo.mWidth / (float) bitMapHeight;
        // Log.i("liveness_ratio", "ratio=" + ratio);
        if (ratio > 0.6) {
            livenessCallBack.onTip(0, "人脸离屏幕太近，请调整与屏幕的距离");
            // clearInfo();
            return false;
        } else if (ratio < 0.2) {
            livenessCallBack.onTip(0, "人脸离屏幕太远，请调整与屏幕的距离");
            // clearInfo();
            return false;
        } else if (faceInfo.mCenter_x > bitMapWidth * 3 / 4 ) {
            livenessCallBack.onTip(0 , "人脸在屏幕中太靠右");
            clearInfo();
            return false;
        } else if (faceInfo.mCenter_x < bitMapWidth / 4 ) {
            livenessCallBack.onTip(0 , "人脸在屏幕中太靠左");
            // clearInfo();
            return false;
        } else if (faceInfo.mCenter_y > bitMapHeight * 3 / 4 ) {
            livenessCallBack.onTip(0 , "人脸在屏幕中太靠下");
            // clearInfo();
            return false;
        } else if (faceInfo.mCenter_x < bitMapHeight / 4 ) {
            livenessCallBack.onTip(0 , "人脸在屏幕中太靠上");
            // clearInfo();
            return false;
        }

        return true;
    }


    private long lasttime;
    private int unDetectedFaceCount = 0;
    private void checkFaceCode(int errCode) {
        if (errCode == FaceTracker.ErrCode.NO_FACE_DETECTED.ordinal() ) {
            if (System.currentTimeMillis() - lasttime > 1000 || unDetectedFaceCount > 5) {
                livenessCallBack.onTip(errCode, "未检测到人脸");
                unDetectedFaceCount = 0;
            }
            unDetectedFaceCount++;
            lasttime = System.currentTimeMillis();
        } else if (errCode == FaceTracker.ErrCode.IMG_BLURED.ordinal() ||
            errCode == FaceTracker.ErrCode.PITCH_OUT_OF_DOWN_MAX_RANGE.ordinal() ||
            errCode == FaceTracker.ErrCode.PITCH_OUT_OF_UP_MAX_RANGE.ordinal() ||
            errCode == FaceTracker.ErrCode.YAW_OUT_OF_LEFT_MAX_RANGE.ordinal() ||
            errCode == FaceTracker.ErrCode.YAW_OUT_OF_RIGHT_MAX_RANGE.ordinal())  {
            livenessCallBack.onTip(errCode, "请静止平视屏幕");
            unDetectedFaceCount = 0;
        } else if (errCode == FaceTracker.ErrCode.POOR_ILLUMINATION.ordinal()) {
            livenessCallBack.onTip(errCode, "光线太暗，请到更明亮的地方");
            unDetectedFaceCount = 0;
        } else {
            livenessCallBack.onTip(errCode, "未检测到人脸");
            unDetectedFaceCount = 0;
        }
    }


    public void release() {
        if (future != null) {
            future.cancel(true);
        }

    }

    private boolean saveRgbImage(String prefix, float score, int[] rgb, int width, int height) {
        boolean success = false;
        if (rgb == null) {
            return success;
        }
        if (!Environment.isExternalStorageEmulated()) {
            return success;
        }
        File sdCard = Environment.getExternalStorageDirectory();
        String uuid = UUID.randomUUID().toString();
        File dir = new File(sdCard.getAbsolutePath() + "/rgb_ir_depth/" + uuid);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String rgbFilename = String.format("%s_%s_%s.jpg", prefix, "rgb", score);
        File rgbFile = new File(dir, rgbFilename);
        if (rgbFile.exists()) {
            rgbFile.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(rgbFile);
            final Bitmap bitmap = Bitmap.createBitmap(rgb, width, height, Bitmap.Config.ARGB_8888);
            Log.i(TAG, "strFileName 1= " + rgbFile.getPath());
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                success = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
    public boolean saveFile(String prefix, String type, float score, byte [] data) {
        boolean success = false;
        if (data == null) {
            return success;
        }
        if (!Environment.isExternalStorageEmulated()) {
            return success;
        }
        File sdCard = Environment.getExternalStorageDirectory();
        String uuid = UUID.randomUUID().toString();
        File dir = new File(sdCard.getAbsolutePath() + "/rgb_ir_depth/" + uuid);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (data != null) {
            String nirFilename = String.format("%s_%s_%s", prefix, type, score);
            File nirFile = new File(dir, nirFilename);
            if (nirFile.exists()) {
                nirFile.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(nirFile);
                fos.write(data, 0, data.length);
                fos.flush();
                fos.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return success;
    }

}
