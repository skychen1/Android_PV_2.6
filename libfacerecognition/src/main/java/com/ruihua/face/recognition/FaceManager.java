package com.ruihua.face.recognition;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.TextureView;

import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceTracker;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;
import com.ruihua.face.recognition.api.FaceApi;
import com.ruihua.face.recognition.callback.FaceIdentityCallback;
import com.ruihua.face.recognition.callback.FaceRegisterCallback;
import com.ruihua.face.recognition.callback.InitListener;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.config.FaceConfig;
import com.ruihua.face.recognition.db.DBManager;
import com.ruihua.face.recognition.entity.ARGBImg;
import com.ruihua.face.recognition.entity.Feature;
import com.ruihua.face.recognition.entity.Group;
import com.ruihua.face.recognition.entity.IdentifyRet;
import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.face.CameraImageSource;
import com.ruihua.face.recognition.face.FaceDetectManager;
import com.ruihua.face.recognition.face.PreviewView;
import com.ruihua.face.recognition.face.camera.CameraView;
import com.ruihua.face.recognition.face.camera.ICameraControl;
import com.ruihua.face.recognition.manager.FaceDetector;
import com.ruihua.face.recognition.manager.FaceLiveness;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.ui.RgbDetectActivity;
import com.ruihua.face.recognition.ui.RgbVideoIdentityActivity;
import com.ruihua.face.recognition.utils.FeatureUtils;
import com.ruihua.face.recognition.utils.GlobalFaceTypeModel;
import com.ruihua.face.recognition.utils.LogUtils;
import com.ruihua.face.recognition.utils.PreferencesUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * describe ： 人脸识别管理类（单例模式）
 *
 * @author : Yich
 * date: 2019/3/13
 */
public class FaceManager {
    private static FaceManager manager;

    private FaceManager() {
    }

    public static synchronized FaceManager getManager() {
        if (manager == null) {
            manager = new FaceManager();
        }
        return manager;
    }


    /**
     * 正则表达式，限定人员编号的规则
     */
    private static Pattern pattern = Pattern.compile("^[0-9a-zA-Z_-]{1,}$");


    private volatile int identityStatus = FEATURE_DATAS_UNREADY;
    private static final int FEATURE_DATAS_UNREADY = 1;
    private static final int IDENTITY_IDLE = 2;
    private static final int IDENTITYING = 3;
    private ExecutorService es;
    private int liveType;
    private Paint paint;
    private RectF rectF;
    private FaceDetectManager faceDetectManager;

    /**
     * 初始化设备，检测权限，初始化数据库
     *
     * @param context 初始化的页面
     */
    public void init(@NonNull Activity context, InitListener listener) {
        //申请文件读取权限
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FaceConfig.EXTERNAL_STORAGE_REQ_CODE);
        }
        //申请相机权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest
                .permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        }
        //初始化数据库
        DBManager.getInstance().init(context);
        //初始化sp工具类
        PreferencesUtil.initPrefs(context);
        //sdk初始化监听
        FaceSDKManager.getInstance().setSdkInitListener(new FaceSDKManager.SdkInitListener() {
            @Override
            public void initStart() {
                //nothing
            }

            @Override
            public void initSuccess() {
                //初始化成功
                if (listener != null) {
                    listener.initSuccess();
                }
            }

            @Override
            public void initFail(int errorCode, String msg) {
                //初始化失败
                if (listener != null) {
                    listener.initFail(errorCode, msg);
                }
            }
        });
        //初始化SDK（包含检测授权信息）
        FaceSDKManager.getInstance().init(context);
    }

    /**
     * 初始化分组
     * 注该方法必须在初始化SDK成功以后调用
     *
     * @return 是否创建成果
     */
    public boolean initGroup() {
        //先判断本地是否已有分组，已有分组就返回true，没有分组就创建分组
        List<Group> groupList = FaceApi.getInstance().getGroupList(0, 1000);
        if (groupList != null && !groupList.isEmpty()) {
            return true;
        }
        Group group = new Group();
        group.setGroupId(FaceConfig.USE_GROUP);
        return FaceApi.getInstance().groupAdd(group);
    }

    /**
     * 设置是否需要活体
     *
     * @param needLive 是否需要活体
     */
    public void setNeedLive(boolean needLive) {
        //需要活体就是TYPE_RGB_LIVENSS，不需要活体就是TYPE_NO_LIVENSS
        int type = needLive ? GlobalFaceTypeModel.TYPE_RGB_LIVENSS : GlobalFaceTypeModel.TYPE_NO_LIVENSS;
        PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_LIVENSS, type);
    }

    /**
     * 获取sdk 初始化状态
     *
     * @return 返回状态值
     */
    public int getInitStatus() {
        int status = FaceCode.SDK_NOT_ACTIVE;
        switch (FaceSDKManager.getInstance().initStatus()) {
            case FaceSDKManager.SDK_UNACTIVATION:
                //未初始化状态，不需要修改
                break;
            case FaceSDKManager.SDK_UNINIT:
                status = FaceCode.SDK_NOT_INIT;
                break;
            case FaceSDKManager.SDK_INITING:
                status = FaceCode.SDK_INITING;
                break;
            case FaceSDKManager.SDK_INITED:
                status = FaceCode.SDK_INITED;
                break;
            case FaceSDKManager.SDK_FAIL:
                status = FaceCode.SDK_INIT_FAIL;
                break;
            default:
                break;
        }
        return status;
    }

    /**
     * 跳转到人脸检测界面
     *
     * @param activity 调用的界面
     * @param fileName 文件名（保存图片的名字，和返回的图片路经intent的key）
     */
    public boolean getFacePicture(@NonNull Activity activity, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        //跳转到人脸注册页面
        RgbDetectActivity.launch(activity, fileName);
        return true;
    }

    /**
     * 人脸注册的，必须等到回调之后才进行下一张照片注册，单线操作。防止出错
     * 注意：该方法的结果回调可能在子线程中，所以处理数据时请注意
     *
     * @param userId   用户的id
     * @param username 用户的姓名
     * @param filePath 照片名字未知
     * @param callback 注册成功与否的回调
     */
    public void registerFace(String userId, String username, final String filePath, FaceRegisterCallback callback) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(username) || TextUtils.isEmpty(filePath)) {
            if (callback != null) {
                callback.onRegisterResult(FaceCode.CODE_REGISTER_PARAM_ERROR, "所有参数都不能为空");
            }
            return;
        }
        Matcher matcher = pattern.matcher(userId);
        if (!matcher.matches()) {
            if (callback != null) {
                callback.onRegisterResult(FaceCode.CODE_REGISTER_PARAM_ERROR, "用户id命名不规则");
            }
            return;
        }
        final File file = new File(filePath);
        if (!file.exists()) {
            if (callback != null) {
                callback.onRegisterResult(FaceCode.CODE_REGISTER_PARAM_ERROR, "照片文件不存在");
            }
            return;
        }
        final User user = new User();
        user.setUserId(userId);
        user.setUserInfo(username);
        user.setGroupId(FaceConfig.USE_GROUP);
        //注册失耗时操作，所以异步进行
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                doRegister(filePath, callback, userId, file, user);
            }
        });
    }

    /**
     * 注册人脸底库
     *
     * @param filePath 文件路劲
     * @param callback 数据回调接口
     * @param userId   用户id，唯一标识
     * @param file     文件
     * @param user     用户
     */
    private void doRegister(String filePath, FaceRegisterCallback callback, String userId, File file, User user) {
        ARGBImg argbImg = FeatureUtils.getARGBImgFromPath(filePath);
        byte[] bytes = new byte[2048];
        //检测照片中是否有合格的人脸
        int ret = FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg, bytes, 50);
        if (ret == FaceDetector.NO_FACE_DETECTED) {
            if (callback != null) {
                callback.onRegisterResult(FaceCode.CODE_REGISTER_NO_FACE, "照片中没有合格的人脸");
            }
        } else if (ret != -1) {
            Feature feature = new Feature();
            feature.setGroupId(FaceConfig.USE_GROUP);
            feature.setUserId(userId);
            feature.setFeature(bytes);
            feature.setImageName(file.getName());
            user.getFeatureList().add(feature);
            if (FaceApi.getInstance().userAdd(user)) {
                //注册成功
                if (callback != null) {
                    callback.onRegisterResult(FaceCode.CODE_REGISTER_SUCCESS, "注册成功");
                    //如果状态是正在识别，或者识别完成状态，(也就是不为没准备的状态)则需要把特征加入内存中；
                    if (identityStatus != FEATURE_DATAS_UNREADY) {
                        add2Memory(feature);
                    }
                }
            } else {
                //注册失败
                if (callback != null) {
                    callback.onRegisterResult(FaceCode.CODE_REGISTER_FAILED, "注册失败，请重试");
                }
            }
        } else {
            //抽取特征失败
            if (callback != null) {
                callback.onRegisterResult(FaceCode.CODE_REGISTER_DETECT_FAILED, "人脸特征抽取失败");
            }
        }
    }

    private void add2Memory(Feature feature) {
        //如果是正在识别的状态就等待500ms
        while (identityStatus == IDENTITYING) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
        //在完成的状态就添加人员特征到内存中
        FaceApi.getInstance().addOneFaceToMemory(FaceConfig.USE_GROUP, feature);
    }

    /**
     * 删除人脸底库
     *
     * @param userId 用户编号，唯一标识
     */
    public boolean deleteFace(String userId) {
        boolean b = FaceApi.getInstance().userDelete(userId, FaceConfig.USE_GROUP);
        //删除成功就要在
        if (b) {
            //如果不是没准备的状态（也就是正在识别状态，或者识别完成状态）
            if (identityStatus != FEATURE_DATAS_UNREADY) {
                es.submit(new Runnable() {
                    @Override
                    public void run() {
                        //如果是正在识别状态就等待500ms
                        while (identityStatus == IDENTITYING) {
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                LogUtils.e(e.toString());
                            }
                        }
                        //不是正在识别状态，就是识别空闲状态，就可以删除对应的人员特征
                        FaceApi.getInstance().deleOneFaceFromeMemory(FaceConfig.USE_GROUP, userId);
                    }
                });
            }
        }
        return b;
    }

    /**
     * 获取人脸底库数量
     *
     * @return 人脸底库数量
     */
    public int getFaceLibraryNum() {
        //判断底库中是否有人脸，返回人脸个数
        List<User> users = DBManager.getInstance().queryUserByGroupId(FaceConfig.USE_GROUP);
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return users.size();
    }


    /**
     * 跳转到识别页面进行识别
     *
     * @param activity 启动页
     * @return 打开页面是否成功，打不开原因，没有分组或者没有注册人脸，
     */
    public boolean identityFace(@NonNull Activity activity) {
        //判断分组当中是否有人，没有人不跳到识别界面
        List<User> users = DBManager.getInstance().queryUserByGroupId(FaceConfig.USE_GROUP);
        if (users == null || users.isEmpty()) {
            return false;
        }
        RgbVideoIdentityActivity.launch(activity);
        return true;
    }

    /**
     * 通过userid 获取用户信息
     *
     * @param userId 用户唯一编号
     * @return 用户信息
     */
    public User getUserById(String userId) {
        return FaceApi.getInstance().getUserInfo(FaceConfig.USE_GROUP, userId);
    }

    /**
     * 人脸识别准备
     *
     * @param previewView 预览控件
     * @param textureView 画脸框控件
     * @param callback    回调
     */
    public void initIdentityFace(Context context, PreviewView previewView, TextureView
            textureView, FaceIdentityCallback callback) {
        //先拿到模式
        liveType = PreferencesUtil.getInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_NO_LIVENSS);
        //初始化画笔
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(30);
        //初始化画脸框
        rectF = new RectF();
        //初始化线程池
        es = Executors.newSingleThreadExecutor();
        initFaceDetect(context, previewView, textureView);
        addListener(previewView, textureView, callback);
        loadFeature2Memery();
    }

    /**
     * 开始识别
     *
     * @return 开启识别是否成功
     */
    public boolean startIdentity() {
        if (faceDetectManager == null) {
            return false;
        }
        faceDetectManager.start();
        faceDetectManager.setUseDetect(true);
        return true;
    }

    /**
     * 停止识别
     */
    public void stopIdentity() {
        if (faceDetectManager == null) {
            return;
        }
        faceDetectManager.stop();
    }

    /**
     * 销毁掉识别的准备 ,释放资源
     */
    public void destroyIdentity() {
        //状态回到初始
        identityStatus = FEATURE_DATAS_UNREADY;
        paint = null;
        es = null;
        rectF = null;
        if (faceDetectManager == null) {
            return;
        }
        faceDetectManager.stop();
        faceDetectManager = null;
    }

    /**
     * 初始化人脸检测
     *
     * @param context     上下文
     * @param previewView 预览界面
     * @param textureView 画框界面
     */
    private void initFaceDetect(Context context, PreviewView previewView, TextureView
            textureView) {
        if (faceDetectManager != null) {
            faceDetectManager = null;
        }
        faceDetectManager = new FaceDetectManager(context.getApplicationContext());
        // 从系统相机获取图片帧。
        final CameraImageSource cameraImageSource = new CameraImageSource(context);
        // 图片越小检测速度越快，闸机场景640 * 480 可以满足需求。实际预览值可能和该值不同。和相机所支持的预览尺寸有关。
        // 可以通过 camera.getParameters().getSupportedPreviewSizes()查看支持列表。
        cameraImageSource.getCameraControl().setPreferredPreviewSize(1280, 720);
        // 设置最小人脸，该值越小，检测距离越远，该值越大，检测性能越好。范围为80-200
        FaceSDKManager.getInstance().getFaceDetector().setMinFaceSize(100);
        // 设置预览
        cameraImageSource.setPreviewView(previewView);
        // 设置图片源
        faceDetectManager.setImageSource(cameraImageSource);
        // 设置人脸过滤角度，角度越小，人脸越正，比对时分数越高
        faceDetectManager.getFaceFilter().setAngle(20);
        textureView.setOpaque(false);
        // 不需要屏幕自动变黑。
        textureView.setKeepScreenOn(true);
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (isPortrait) {
            previewView.setScaleType(PreviewView.ScaleType.FIT_WIDTH);
            // 相机坚屏模式
            cameraImageSource.getCameraControl().setDisplayOrientation(CameraView.ORIENTATION_PORTRAIT);
        } else {
            previewView.setScaleType(PreviewView.ScaleType.FIT_HEIGHT);
            // 相机横屏模式
            cameraImageSource.getCameraControl().setDisplayOrientation(CameraView.ORIENTATION_HORIZONTAL);
        }
        // TODO 选择使用前置摄像头
        // cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_FRONT);
        // TODO 选择使用usb摄像头
        cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_USB);
        // 如果不设置，人脸框会镜像，显示不准
        previewView.getTextureView().setScaleX(-1);
        // TODO 选择使用后置摄像头
        //cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_BACK);
        //previewView.getTextureView().setScaleX(-1);
    }

    /**
     * 设置人脸检测回调
     *
     * @param previewView 预览界面
     * @param textureView 画框
     * @param callback    回调
     */
    private void addListener(PreviewView previewView, TextureView
            textureView, FaceIdentityCallback callback) {
        // 设置回调，回调人脸检测结果。
        faceDetectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(int retCode, FaceInfo[] infos, ImageFrame frame) {
                //判断返回值是否正确
                if (retCode == FaceTracker.ErrCode.OK.ordinal() && infos != null) {
                    asyncIdentity(frame, infos, callback);
                }
                //画脸框
                showFrame(previewView, textureView, frame, infos);
            }
        });
    }

    /**
     * 加载人脸底库到内存中
     */
    private void loadFeature2Memery() {
        if (identityStatus != FEATURE_DATAS_UNREADY) {
            return;
        }
        es.submit(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                FaceApi.getInstance().loadFacesFromDB(FaceConfig.USE_GROUP);
                identityStatus = IDENTITY_IDLE;
            }
        });
    }

    private void asyncIdentity(final ImageFrame imageFrame,
                               final FaceInfo[] faceInfos, FaceIdentityCallback callback) {
        if (identityStatus != IDENTITY_IDLE) {
            return;
        }
        if (es == null) {
            return;
        }
        identityStatus = IDENTITYING;
        es.submit(new Runnable() {
            @Override
            public void run() {
                //判断人脸信息
                if (faceInfos.length == 0) {
                    identityStatus = IDENTITY_IDLE;
                    return;
                }
                //判断偏转角度
                float raw = Math.abs(faceInfos[0].headPose[0]);
                float patch = Math.abs(faceInfos[0].headPose[1]);
                float roll = Math.abs(faceInfos[0].headPose[2]);
                // 人脸的三个角度大于20不进行识别
                if (raw > FaceConfig.FACE_RECOGNISE_ANGLE || patch > FaceConfig.FACE_RECOGNISE_ANGLE || roll > FaceConfig.FACE_RECOGNISE_ANGLE) {
                    identityStatus = IDENTITY_IDLE;
                    return;
                }
                //判断是否需要检测活体
                if (liveType == GlobalFaceTypeModel.TYPE_NO_LIVENSS) {
                    identity(imageFrame, faceInfos[0], callback);
                } else {
                    //如果活体通过就进行对比
                    if (rgbLive(imageFrame, faceInfos[0]) > FaceConfig.FACE_LIVE) {
                        identity(imageFrame, faceInfos[0], callback);
                    } else {
                        //如果活体不通过，修改标识(放下一帧照片进来)
                        identityStatus = IDENTITY_IDLE;
                    }
                }
            }
        });
    }

    /**
     * 活体检测
     *
     * @param imageFrame 数据
     * @param faceInfo   数据
     * @return 活体分数
     */
    private float rgbLive(ImageFrame imageFrame, FaceInfo faceInfo) {
        float score = FaceLiveness.getInstance().rgbLiveness(imageFrame.getArgb(), imageFrame.getWidth(), imageFrame.getHeight(), faceInfo.landmarks);
        LogUtils.e("活体分数是" + score);
        return score;
    }

    /**
     * 识别人脸
     *
     * @param imageFrame 数据
     * @param faceInfo   人脸数据
     * @param callback   回调接口
     */
    private void identity(ImageFrame imageFrame, FaceInfo faceInfo, FaceIdentityCallback
            callback) {
        int[] argb = imageFrame.getArgb();
        int rows = imageFrame.getHeight();
        int cols = imageFrame.getWidth();
        int[] landmarks = faceInfo.landmarks;
        //生活照模式识别
        IdentifyRet identifyRet = FaceApi.getInstance().identity(argb, rows, cols, landmarks, FaceConfig.USE_GROUP);
        LogUtils.e("识别分数" + identifyRet.getScore());
        if (identifyRet.getScore() > FaceConfig.FACE_RECOGNISE_SCORE) {
            if (callback != null) {
                callback.onIdentityResult(identifyRet.getUserId());
            }
        }
        //识别完成，就直接修改标识
        identityStatus = IDENTITY_IDLE;
    }

    /**
     * 绘制人脸框。
     */
    private void showFrame(PreviewView previewView, TextureView textureView, ImageFrame
            imageFrame, FaceInfo[] faceInfos) {
        Canvas canvas = textureView.lockCanvas();
        if (canvas == null) {
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
        if (rectF == null) {
            return;
        }
        rectF.set(getFaceRect(faceInfo, imageFrame));
        // 检测图片的坐标和显示的坐标不一样，需要转换。
        previewView.mapFromOriginalRect(rectF);
        float yaw = Math.abs(faceInfo.headPose[0]);
        float patch = Math.abs(faceInfo.headPose[1]);
        float roll = Math.abs(faceInfo.headPose[2]);
        if (yaw > FaceConfig.FACE_RECOGNISE_ANGLE || patch > FaceConfig.FACE_RECOGNISE_ANGLE || roll > FaceConfig.FACE_RECOGNISE_ANGLE) {
            // 不符合要求，绘制黄框
            paint.setColor(Color.YELLOW);
            String text = "请保持人脸正对屏幕";
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
        left = (int) (faceInfo.mCenter_x - (width / 2));
        top = (int) (faceInfo.mCenter_y - height / 2);
        rect.top = top < 0 ? 0 : top;
        rect.left = left < 0 ? 0 : left;
        rect.right = (left + width) > frame.getWidth() ? frame.getWidth() : (left + width);
        rect.bottom = (top + height) > frame.getHeight() ? frame.getHeight() : (top + height);
        return rect;
    }

}
