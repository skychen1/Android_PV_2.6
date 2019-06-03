/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ToastUtils;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.callback.FaceIdentityCallback;
import com.ruihua.face.recognition.face.PreviewView;

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
            public void onIdentityResult(boolean isSuccess, String userId) {
                if (!isSuccess) {
                    LogUtils.e("识别到，陌生人");
                    return;
                }
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 结束检测。
        FaceManager.getManager().stopIdentity();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FaceManager.getManager().destroyIdentity();
    }

}
