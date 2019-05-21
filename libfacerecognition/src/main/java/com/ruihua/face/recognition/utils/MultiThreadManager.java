package com.ruihua.face.recognition.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.baidu.idl.facesdk.FaceAttribute;
import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;
import com.baidu.idl.facesdk.FaceVerifyData;
import com.ruihua.face.recognition.entity.ARGBImg;

import java.io.InputStream;

/**
 * FaceSDK 多线程支持
 */

public class MultiThreadManager {

    private static final String TAG = "multithread";

    // MAX_THREAD_NUM 预先表示线程个数，用户需要根据自己需求提前初始化好线程个数，不支持动态扩充线程个数
    private static final int MAX_THREAD_NUM = 2;

    // 单线程测试SDK 功能次数，不用在意
    private static final int MAX_TEST_TIMES = 500;
    private String mPicName = "pic/testRegPic";

    private static class HolderClass {
        private static final MultiThreadManager instance = new MultiThreadManager();
    }

    public static MultiThreadManager getInstance() {
        return HolderClass.instance;
    }

    private MultiThreadManager() {

    }

    /**
     * 默认初始化 MAX_THREAD_NUM 个，不同线程通过index 为0~MAX_THREAD_NUM-1，彼此调用相应的方法
     *
     * @param context
     */
    public void start(final Context context) {

        // 人脸tacker 模型初始化，支持动态初始化，但是多个tracker初始化必须在同一个线程中，切记不要放在子线程中初始化，face
        // 在子线程中通过FaceTracker 实例调用faceVerification等方法进行人脸--detect检测，align72个特征点获取，quality质量检测，copyImage抠图等
        // final FaceTracker faceTracker = getFaceTracker(context);
        // final FaceTracker faceTracker1 = getFaceTracker(context);
        // final FaceTracker faceTracker2 = getFaceTracker(context);


        // 人脸可见光离线识别模型初始化，通过index调用FaceSDK.extractFeature方法提取人脸特征值
        FaceSDK.recognizeModelInit(context, MAX_THREAD_NUM, FaceSDK.RecognizeType.RECOGNIZE_LIVE);
        // 人脸属性模型初始化，通过index 调用FaceSDK.faceAttribute方法获取人脸属性提取
        FaceSDK.faceAttributeModelInit(context, MAX_THREAD_NUM);
        // 人脸可见光静默活体模型初始化，通过index 调用FaceSDK.run_livenessSilentPredict方法获取静默活体分值，
        FaceSDK.livenessSilentInit(context, FaceSDK.LivenessTypeId.LIVEID_VIS, MAX_THREAD_NUM);
        // 超分辨率模型初始化，通过FaceSDK.superResolution 方法将模糊图片处理为高清图片
        FaceSDK.superResolutionMoelInit(context, MAX_THREAD_NUM);
        // 去网纹模型初始化，通过FaceSDK.removeTexture 方法将身份证上的条纹擦除掉
        FaceSDK.removeTextureModelInit(context, MAX_THREAD_NUM);

        // 人脸tacker 单个功能模型初始化，通过index 调用FaceSDK.detect，FaceSDK.align，FaceSDK.imgQuality单个功能方法

        FaceSDK.initModel(context, MAX_THREAD_NUM);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ARGBImg argbImg = getData(context, mPicName + 0 + ".png");
                for (int i = 0; i < MAX_TEST_TIMES; i++) {
                    // stratFaceWork(0, faceTracker, argbImg);
                    startFaceWorkSingle(0, argbImg);
                }
                //startFaceWorkSingle(0, argbImg);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ARGBImg argbImg = getData(context, mPicName + 0 + ".png");
                for (int i = 0; i < MAX_TEST_TIMES; i++) {
                    // stratFaceWork(1, faceTracker1, argbImg);
                    startFaceWorkSingle(1, argbImg);
                }
                //startFaceWorkSingle(1, argbImg);
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ARGBImg argbImg = getData(context, mPicName + 0 + ".png");
//                for (int i = 0; i < MAX_TEST_TIMES; i++) {
//                    // stratFaceWork(2, faceTracker2, argbImg);
//                    startFaceWorkSingle(2, argbImg);
//                }
//            }
//        }).start();
    }


    private void stratFaceWork(int index, FaceTracker faceTracker, ARGBImg argbImg) {

        if (argbImg != null) {
            // 人脸检测
            faceTracker.faceVerification(argbImg.data, argbImg.height, argbImg.width,
                    FaceSDK.ImgType.ARGB, FaceTracker.ActionType.RECOGNIZE);

            // 特征提取
            FaceInfo[] faceInfos = faceTracker.get_TrackedFaceInfo();
            if (faceInfos != null && faceInfos.length > 0) {
                FaceInfo faceInfo = faceInfos[0];
                byte[] feature = new byte[2048];
                FaceSDK.extractFeature(index, argbImg.data, argbImg.height, argbImg.width,
                        FaceSDK.ImgType.ARGB.ordinal(), feature, faceInfo.landmarks, 1,
                        FaceSDK.RecognizeType.RECOGNIZE_LIVE.ordinal());
                float score = FaceSDK.getFaceSimilarity(feature, feature,
                        1, FaceSDK.RecognizeType.RECOGNIZE_LIVE.ordinal());

                FaceAttribute attribute = FaceSDK.faceAttribute(index, argbImg.data, argbImg.height, argbImg.width,
                        FaceSDK.ImgType.ARGB, faceInfo.landmarks);

                float slentScore = FaceSDK.run_livenessSilentPredict(FaceSDK.LivenessTypeId.LIVEID_VIS, index,
                        argbImg.data, argbImg.height, argbImg.width, 24, faceInfo.landmarks);

                FaceVerifyData removeData = FaceSDK.removeTexture(index, argbImg.data, argbImg.height,
                        argbImg.width, FaceSDK.ImgType.ARGB);

                FaceVerifyData superData = FaceSDK.superResolution(index, argbImg.data, argbImg.height,
                        argbImg.width, FaceSDK.ImgType.ARGB);

                Log.e(TAG, "thread " + index
                        + "   " + score
                        + "   " + faceInfo.mConf
                        + "   " + attribute.age
                        + "   " + slentScore
                        + "   " + removeData.rows
                        + "   " + superData.rows
                );
            }

        }
    }

    private void startFaceWorkSingle(int index, ARGBImg argbImg) {


        FaceInfo[] faces = FaceSDK.detect(index, argbImg.data, argbImg.height,
                argbImg.width, FaceSDK.ImgType.ARGB.ordinal(), FaceSDK.DetectMethodType.CNN.ordinal(), 50);
        if (faces != null && faces.length > 0) {
            float maxWidth = 0;
            int maxId = 0;
            float detectScore = 0;
            for (int i = 0; i < faces.length; i++) {
                if (faces[i].mWidth > maxWidth) {
                    maxId = i;
                    maxWidth = faces[i].mWidth;
                    detectScore = faces[i].mConf;
                }
            }

            float[] faceT = new float[]{faces[maxId].mCenter_x, faces[maxId].mCenter_y, faces[maxId].mWidth, faces[maxId].mAngle};
            int[] shape = new int[144];
            int[] nPoint = new int[]{0};
            float[] score = new float[]{0.0F};

            FaceSDK.align(index, argbImg.data, argbImg.height,
                    argbImg.width, FaceSDK.ImgType.ARGB.ordinal(),
                    FaceSDK.AlignMethodType.CDNN.ordinal(), faceT, shape, nPoint, score, detectScore);

            float[] bluriness = new float[1];
            int[] illum = new int[1];
            float[] occlusion = new float[7];
            int[] nOccluPart = new int[1];
            FaceSDK.imgQuality(index, argbImg.data, argbImg.height,
                    argbImg.width, FaceSDK.ImgType.ARGB.ordinal(), nPoint, 72,
                    bluriness, illum, occlusion, nOccluPart);

            byte[] feature = new byte[2048];
            FaceSDK.extractFeature(index, argbImg.data, argbImg.height, argbImg.width,
                    FaceSDK.ImgType.ARGB.ordinal(), feature, shape, 1,
                    FaceSDK.RecognizeType.RECOGNIZE_LIVE.ordinal());
            float scoreSimilar = FaceSDK.getFaceSimilarity(feature, feature,
                    1, FaceSDK.RecognizeType.RECOGNIZE_LIVE.ordinal());

            FaceAttribute attribute = FaceSDK.faceAttribute(index, argbImg.data, argbImg.height, argbImg.width,
                    FaceSDK.ImgType.ARGB, shape);

            float slentScore = FaceSDK.run_livenessSilentPredict(FaceSDK.LivenessTypeId.LIVEID_VIS, index,
                    argbImg.data, argbImg.height, argbImg.width, 24, shape);

            FaceVerifyData removeData = FaceSDK.removeTexture(index, argbImg.data, argbImg.height,
                    argbImg.width, FaceSDK.ImgType.ARGB);

            FaceVerifyData superData = FaceSDK.superResolution(index, argbImg.data, argbImg.height,
                    argbImg.width, FaceSDK.ImgType.ARGB);


            Log.e(TAG, "thread " + index
                    + "   " + detectScore
                    + "   " + score[0]
                    + "   " + bluriness[0]
                    + "   " + illum[0]
                    + "   " + occlusion[0]
                    + "   " + nOccluPart[0]
                    + "   " + scoreSimilar
                    + "   " + attribute.age
                    + "   " + slentScore
                    + "   " + removeData.rows
                    + "   " + superData.rows
            );
        }
    }

    private FaceTracker getFaceTracker(Context context) {
        // todo:开发者必须添加acedetect.binary,align.binary_64,score.binary 模型;
        // 实例化FaceTracker，初始化算法模型
        FaceTracker faceTracker = new FaceTracker(context);
        // 旧方法开启校验活体，需要传递连续图片校验，新接口有静默活体检测；建议false
        faceTracker.set_isVerifyLive(false);
        // 不进行人脸精确对齐检测
        faceTracker.set_isFineAlign(false);
        // 设置图像能被检测出人脸的最小脸值尺寸（宽高相等。单位：像素）
        faceTracker.set_min_face_size(100);
        // 设置人脸追踪最大缓存图片个数，默认为3，
        faceTracker.set_max_reg_img_num(3);
        // 设置图像能被检测出人脸阈值，范围是0~1，如果获得的人脸置信度小于设定值，则判断为非人脸
        faceTracker.set_notFace_thr(0.6f);
        // 设置图像中人脸角度的阈值，如果人脸姿态超出阈值，ErrorCode 会返回相应的类型
        // pitch_thr表示图像中人脸抬头低头角度阈值
        // yaw_thr表示图像中人脸左右角度阈值
        // roll_thr表示图像中人脸偏头阈值
        faceTracker.set_eulur_angle_thr(30, 30, 30);
        // 设置人脸检测类型，是否是普通图片（CNN）检测还是红外图片（NIR）检测
        // todo:如果设置为NIR，必须添加small_detect.model模型
        faceTracker.set_DetectMethodType(FaceSDK.DetectMethodType.CNN.ordinal());
        // 设置首次检测的目标前，执行人脸检测的时间间隔，单位毫秒，时间越短越快发现新目标，但增加资源消耗
        faceTracker.set_detect_in_video_interval(0);
        // 设置跟踪到目标前执行人脸检测的时间间隔，单位毫秒，时间越短越快发现新目标，但增加资源消耗
        faceTracker.set_track_by_detection_interval(0);
        // 开启人脸图像质量检测总开关
        // todo:如果设置为ture,必须添加blur.binary，occlu.binary模型
        faceTracker.set_isCheckQuality(true);
        // 开启质量检测（模糊值检测）
        faceTracker.setQualityCheckAbility(FaceSDK.FaceQualityType.BLUR.ordinal(), true);
        // 开启质量检测（光照值检测）
        faceTracker.setQualityCheckAbility(FaceSDK.FaceQualityType.ILLUMINATION.ordinal(), true);
        // 开启质量检测（遮挡值检测）
        faceTracker.setQualityCheckAbility(FaceSDK.FaceQualityType.OCCLUSION.ordinal(), true);
        // 开启疲劳检测，通过faceTracker.get_sleepnessInfo() 获取疲劳数据信息
        // todo:如果设置为CAR_SLEEPNESS，必须添加action_nir_rgb_eye.model和action_nir_rgb_mouth.model 模型
        // faceTracker.setAppType(FaceTracker.AppType.CAR_SLEEPNESS);
        return faceTracker;
    }

    private ARGBImg getData(Context context, String imageName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(imageName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            if (bitmap != null) {
                int[] argbData = new int[bitmap.getWidth() * bitmap.getHeight()];
                bitmap.getPixels(argbData, 0, bitmap.getWidth(),
                        0, 0, bitmap.getWidth(), bitmap.getHeight());
                return new ARGBImg(argbData, bitmap.getWidth(),
                        bitmap.getHeight(), 0, 0);
            }

        } catch (Exception e) {

        }
        return null;
    }
}
