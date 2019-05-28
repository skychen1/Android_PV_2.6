/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.widget.Toast;

import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceTracker;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.api.FaceApi;
import com.ruihua.face.recognition.callback.FaceIdentityCallback;
import com.ruihua.face.recognition.config.FaceConfig;
import com.ruihua.face.recognition.entity.IdentifyRet;
import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.face.CameraImageSource;
import com.ruihua.face.recognition.face.FaceDetectManager;
import com.ruihua.face.recognition.face.PreviewView;
import com.ruihua.face.recognition.face.camera.CameraView;
import com.ruihua.face.recognition.face.camera.ICameraControl;
import com.ruihua.face.recognition.manager.FaceLiveness;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.GlobalFaceTypeModel;
import com.ruihua.face.recognition.utils.LogUtils;
import com.ruihua.face.recognition.utils.PreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 人脸识别页面
 */
public class RgbVideoIdentityActivity extends Activity {

    public static final int CODE_RECOGNISE = 101;
    public static final String USER_ID = "user_id";
    private PreviewView previewView;
    private TextureView textureView;


    /**
     * 启动页面方法
     *
     * @param act 启动页面44
     */
    public static void launch(Activity act) {
        Intent intent = new Intent(act, RgbVideoIdentityActivity.class);
        act.startActivityForResult(intent, CODE_RECOGNISE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_video_identify);
        findView();
        FaceManager.getManager().initIdentityFace(this, previewView, textureView, new FaceIdentityCallback() {
            @Override
            public void onIdentityResult(String userId) {
                Intent intent = new Intent();
                intent.putExtra(USER_ID, userId);
                setResult(CODE_RECOGNISE, intent);
                RgbVideoIdentityActivity.this.finish();
            }
        });

    }

    private void findView() {
        previewView = (PreviewView) findViewById(R.id.preview_view);
        textureView = (TextureView) findViewById(R.id.texture_view);

    }


    @Override
    protected void onStart() {
        super.onStart();
        // 开始检测
        FaceManager.getManager().startIdentity();
//        faceDetectManager.start();
//        faceDetectManager.setUseDetect(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 结束检测。
        FaceManager.getManager().stopIdentity();
//        faceDetectManager.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        faceDetectManager.stop();
        FaceManager.getManager().destroyIdentity();
    }

}
