/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceTracker;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.config.FaceConfig;
import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.face.CameraImageSource;
import com.ruihua.face.recognition.face.FaceCropper;
import com.ruihua.face.recognition.face.FaceDetectManager;
import com.ruihua.face.recognition.face.PreviewView;
import com.ruihua.face.recognition.face.camera.CameraView;
import com.ruihua.face.recognition.face.camera.ICameraControl;
import com.ruihua.face.recognition.manager.FaceLiveness;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.FileUitls;
import com.ruihua.face.recognition.utils.GlobalFaceTypeModel;
import com.ruihua.face.recognition.utils.ImageUtils;
import com.ruihua.face.recognition.utils.PreferencesUtil;

import java.io.File;

/**
 * describe ： 人脸检测类
 * 检测到合格的人脸照片并且裁剪后保存
 *
 * @author : Yich
 * date: 2019/3/13
 */
public class RgbDetectActivity extends Activity {
    /**
     * 启动返回码
     * FILE_PATH 数据返回取值的key值
     */
    public static final int CODE_PICK_PHOTO = 100;
    public static final String FILE_PATH = "file_path";
    /**
     * previewView 预览界面
     * textureView 人脸狂绘制界面
     */
    private PreviewView previewView;
    private TextureView textureView;
    private TextView rgbLivenssDurationTv;
    private TextView rgbLivenessScoreTv;
    private FaceDetectManager faceDetectManager;
    private TextView tipTv;
    private Handler handler = new Handler();
    private String fileName = "";
    private boolean isMirror;

    /**
     * 启动方法
     *
     * @param act 启动页
     */
    public static void launch(Activity act, String fileName, boolean isMirror) {
        Intent intent = new Intent(act, RgbDetectActivity.class);
        intent.putExtra("fileName", fileName);
        intent.putExtra("isMirror", isMirror);
        act.startActivityForResult(intent, CODE_PICK_PHOTO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_rgb_detect);
        findView();
        initData();
        init();
        addListener();
    }

    /**
     * 初始化控件
     */
    private void findView() {
        previewView = (PreviewView) findViewById(R.id.preview_view);
        textureView = (TextureView) findViewById(R.id.texture_view);
        tipTv = (TextView) findViewById(R.id.tip_tv);
        rgbLivenssDurationTv = (TextView) findViewById(R.id.rgb_liveness_duration_tv);
        rgbLivenessScoreTv = (TextView) findViewById(R.id.rgb_liveness_score_tv);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            toast("数据传递错误，请重试");
            finish();
            return;
        }
        fileName = intent.getStringExtra("fileName");
        isMirror = intent.getBooleanExtra("isMirror", false);
        if (TextUtils.isEmpty(fileName)) {
            toast("请传入正确的图片名");
            finish();
        }
    }

    private void init() {
        faceDetectManager = new FaceDetectManager(getApplicationContext());
        // 从系统相机获取图片帧。
        final CameraImageSource cameraImageSource = new CameraImageSource(this);
        // 图片越小检测速度越快，闸机场景640 * 480 可以满足需求。实际预览值可能和该值不同。和相机所支持的预览尺寸有关。
        // 可以通过 camera.getParameters().getSupportedPreviewSizes()查看支持列表。
        cameraImageSource.getCameraControl().setPreferredPreviewSize(1280, 720);
        // 设置最小人脸，该值越小，检测距离越远，该值越大，检测性能越好。范围为80-200
        FaceSDKManager.getInstance().getFaceDetector().setMinFaceSize(120);
        // 设置预览
        cameraImageSource.setPreviewView(previewView);
        // 设置图片源
        faceDetectManager.setImageSource(cameraImageSource);
        // 设置人脸过滤角度，角度越小，人脸越正，比对时分数越高
        faceDetectManager.getFaceFilter().setAngle(20);
        faceDetectManager.setUseDetect(true);
        textureView.setOpaque(false);
        // 不需要屏幕自动变黑。
        textureView.setKeepScreenOn(true);
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (isPortrait) {
            previewView.setScaleType(PreviewView.ScaleType.FIT_WIDTH);
            // 相机坚屏模式
            cameraImageSource.getCameraControl().setDisplayOrientation(CameraView.ORIENTATION_PORTRAIT);
        } else {
            previewView.setScaleType(PreviewView.ScaleType.FIT_HEIGHT);
            // 相机横屏模式
            cameraImageSource.getCameraControl().setDisplayOrientation(CameraView.ORIENTATION_HORIZONTAL);
        }

        setCameraType(cameraImageSource);
    }

    private void setCameraType(CameraImageSource cameraImageSource) {
        // TODO 选择使用前置摄像头
//         cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_FRONT);

        // TODO 选择使用usb摄像头
        cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_USB);
        if (isMirror) {
            //如果有镜像需要设置镜像
            // 如果不设置，人脸框会镜像，显示不准
            previewView.getTextureView().setScaleX(-1);
        }
        // TODO 选择使用后置摄像头
//        cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_BACK);
//        previewView.getTextureView().setScaleX(-1);
    }

    private void addListener() {
        // 设置回调，回调人脸检测结果。
        faceDetectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(int retCode, FaceInfo[] infos, ImageFrame frame) {
                //检测图片
                checkFace(retCode, infos, frame);
                //画框
                showFrame(frame, infos);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开始检测
        faceDetectManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 结束检测。
        faceDetectManager.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        faceDetectManager.stop();
    }

    private void checkFace(int retCode, FaceInfo[] faceInfos, ImageFrame frame) {
        if (retCode == FaceTracker.ErrCode.OK.ordinal() && faceInfos != null) {
            FaceInfo faceInfo = faceInfos[0];
            String tip = filter(faceInfo, frame);
            displayTip(tip);
        } else {
            String tip = checkFaceCode(retCode);
            displayTip(tip);
        }
    }


    private String filter(FaceInfo faceInfo, ImageFrame imageFrame) {
        String tip = "";
        if (faceInfo.mConf < FaceConfig.FACE_CONF) {
            tip = "人脸置信度太低";
            return tip;
        }
        float[] headPose = faceInfo.headPose;
        if (Math.abs(headPose[0]) > FaceConfig.FACE_REGISTER_ANGLE || Math.abs(headPose[1]) > FaceConfig.FACE_REGISTER_ANGLE || Math.abs(headPose[2]) > FaceConfig.FACE_REGISTER_ANGLE) {
            tip = "请保持人脸正对屏幕";
            return tip;
        }
        int width = imageFrame.getWidth();
        int height = imageFrame.getHeight();
        // 判断人脸大小，若人脸超过屏幕二分一，则提示文案“人脸离手机太近，请调整与手机的距离”；
        // 若人脸小于屏幕三分一，则提示“人脸离手机太远，请调整与手机的距离”
        float ratio = (float) faceInfo.mWidth / (float) height;
        Log.i("liveness_ratio", "ratio=" + ratio);
        if (ratio > 0.6) {
            tip = "人脸离屏幕太近，请调整与屏幕的距离";
            return tip;
        } else if (ratio < 0.2) {
            tip = "人脸离屏幕太远，请调整与屏幕的距离";
            return tip;
        } else if (faceInfo.mCenter_x > width * 3 / 4) {
            tip = "人脸在屏幕中太靠右";
            return tip;
        } else if (faceInfo.mCenter_x < width / 4) {
            tip = "人脸在屏幕中太靠左";
            return tip;
        } else if (faceInfo.mCenter_y > height * 3 / 4) {
            tip = "人脸在屏幕中太靠下";
            return tip;
        } else if (faceInfo.mCenter_y < height / 4) {
            tip = "人脸在屏幕中太靠上";
            return tip;
        }
        int liveType = PreferencesUtil.getInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_NO_LIVENSS);
        if (liveType == GlobalFaceTypeModel.TYPE_NO_LIVENSS) {
            saveFace(faceInfo, imageFrame);
        } else if (liveType == GlobalFaceTypeModel.TYPE_RGB_LIVENSS) {
            if (rgbLiveness(imageFrame, faceInfo) > FaceConfig.FACE_LIVE) {
                saveFace(faceInfo, imageFrame);
            } else {
//                toast("rgb活体分数过低");
            }
        }
        return tip;
    }

    private String checkFaceCode(int errCode) {
        String tip = "";
        if (errCode == FaceTracker.ErrCode.NO_FACE_DETECTED.ordinal()) {
            //tip = "未检测到人脸";
        } else if (errCode == FaceTracker.ErrCode.IMG_BLURED.ordinal() ||
                errCode == FaceTracker.ErrCode.PITCH_OUT_OF_DOWN_MAX_RANGE.ordinal() ||
                errCode == FaceTracker.ErrCode.PITCH_OUT_OF_UP_MAX_RANGE.ordinal() ||
                errCode == FaceTracker.ErrCode.YAW_OUT_OF_LEFT_MAX_RANGE.ordinal() ||
                errCode == FaceTracker.ErrCode.YAW_OUT_OF_RIGHT_MAX_RANGE.ordinal()) {
            tip = "请静止平视屏幕";
        } else if (errCode == FaceTracker.ErrCode.POOR_ILLUMINATION.ordinal()) {
            tip = "光线太暗，请到更明亮的地方";
        } else if (errCode == FaceTracker.ErrCode.UNKNOW_TYPE.ordinal()) {
            tip = "未检测到人脸";
        }
        return tip;
    }

    private float rgbLiveness(ImageFrame imageFrame, FaceInfo faceInfo) {

        long starttime = System.currentTimeMillis();
        final float rgbScore = FaceLiveness.getInstance().rgbLiveness(imageFrame.getArgb(), imageFrame
                .getWidth(), imageFrame.getHeight(), faceInfo.landmarks);
        final long duration = System.currentTimeMillis() - starttime;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rgbLivenssDurationTv.setVisibility(View.VISIBLE);
                rgbLivenessScoreTv.setVisibility(View.VISIBLE);
                rgbLivenssDurationTv.setText("RGB活体耗时：" + duration);
                rgbLivenessScoreTv.setText("RGB活体得分：" + rgbScore);
            }
        });

        return rgbScore;
    }

    /**
     * 保存照片，并且关闭页面，回调给启动页面
     *
     * @param faceInfo   人脸数据
     * @param imageFrame 人脸数据
     */
    private void saveFace(FaceInfo faceInfo, ImageFrame imageFrame) {
        final Bitmap bitmap = FaceCropper.getFace(imageFrame.getArgb(), faceInfo, imageFrame.getWidth());
        // 注册来源保存到注册人脸目录
        File faceDir = FileUitls.getFaceDirectory();
        if (faceDir != null) {
            File file = new File(faceDir, fileName);
            // 压缩人脸图片至300 * 300，减少网络传输时间
            ImageUtils.resize(bitmap, file, 300, 300);
            Intent intent = new Intent();
            intent.putExtra(FILE_PATH, file.getAbsolutePath());
            setResult(CODE_PICK_PHOTO, intent);
            finish();
        } else {
            toast("注册人脸目录未找到");
        }
    }

    private void toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RgbDetectActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayTip(final String tip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tipTv.setText(tip);
            }
        });
    }

    private Paint paint = new Paint();

    {
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(30);
    }

    RectF rectF = new RectF();

    /**
     * 绘制人脸框。
     */
    private void showFrame(ImageFrame imageFrame, FaceInfo[] faceInfos) {
        Canvas canvas = textureView.lockCanvas();
        if (canvas == null) {
            textureView.unlockCanvasAndPost(canvas);
            return;
        }
        if (faceInfos == null || faceInfos.length == 0) {
            // 清空canvas
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            textureView.unlockCanvasAndPost(canvas);
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        FaceInfo faceInfo = faceInfos[0];
        rectF.set(getFaceRect(faceInfo, imageFrame));
        // 检测图片的坐标和显示的坐标不一样，需要转换。
        previewView.mapFromOriginalRect(rectF);
        float yaw = Math.abs(faceInfo.headPose[0]);
        float patch = Math.abs(faceInfo.headPose[1]);
        float roll = Math.abs(faceInfo.headPose[2]);
        if (yaw > FaceConfig.FACE_REGISTER_ANGLE || patch > FaceConfig.FACE_REGISTER_ANGLE || roll > FaceConfig.FACE_REGISTER_ANGLE) {
            // 不符合要求，绘制黄框
            paint.setColor(Color.YELLOW);
            String text = "请正视屏幕";
            float width = paint.measureText(text) + 50;
            float x = rectF.centerX() - width / 2;
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, x + 25, rectF.top - 20, paint);
            paint.setColor(Color.YELLOW);
        } else {
            // 符合检测要求，绘制绿框
            paint.setColor(Color.GREEN);
        }
        paint.setStyle(Paint.Style.STROKE);
        // 绘制框
        canvas.drawRect(rectF, paint);
        textureView.unlockCanvasAndPost(canvas);
    }


    /**
     * 获取人脸框区域。
     *
     * @return 人脸框区域
     */
    private Rect getFaceRect(FaceInfo faceInfo, ImageFrame frame) {
        Rect rect = new Rect();
        int[] points = new int[8];
        faceInfo.getRectPoints(points);
        int left = points[2];
        int top = points[3];
        int right = points[6];
        int bottom = points[7];
        int width = (right - left);
        int height = (bottom - top);
        left = (int) (faceInfo.mCenter_x - width / 2);
        top = (int) (faceInfo.mCenter_y - height / 2);
        rect.top = top < 0 ? 0 : top;
        rect.left = left < 0 ? 0 : left;
        rect.right = (left + width) > frame.getWidth() ? frame.getWidth() : (left + width);
        rect.bottom = (top + height) > frame.getHeight() ? frame.getHeight() : (top + height);
        return rect;
    }

}
