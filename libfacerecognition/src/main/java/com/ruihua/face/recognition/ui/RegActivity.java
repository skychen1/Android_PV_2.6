/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceTracker;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.api.FaceApi;
import com.ruihua.face.recognition.callback.FaceRegisterCallback;
import com.ruihua.face.recognition.config.FaceConfig;
import com.ruihua.face.recognition.entity.ARGBImg;
import com.ruihua.face.recognition.entity.Feature;
import com.ruihua.face.recognition.entity.ImageFrame;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.face.FaceCropper;
import com.ruihua.face.recognition.face.FaceDetectManager;
import com.ruihua.face.recognition.face.FileImageSource;
import com.ruihua.face.recognition.manager.FaceDetector;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.FeatureUtils;
import com.ruihua.face.recognition.utils.FileUitls;
import com.ruihua.face.recognition.utils.GlobalFaceTypeModel;
import com.ruihua.face.recognition.utils.ImageUtils;
import com.ruihua.face.recognition.utils.PreferencesUtil;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类提供人脸注册功能，注册的人脸可以通个自动检测和选自相册两种方式获取。
 */

public class RegActivity extends Activity implements View.OnClickListener {

    private EditText usernameEt;
    private ImageView avatarIv;
    private Button autoDetectBtn;
    private Button submitButton;
    private String faceImagePath;
    private String imageName;

    /**
     * 页面启动方法
     *
     * @param act 上个页面
     */
    public static void launch(Activity act) {
        Intent intent = new Intent(act, RegActivity.class);
        act.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_reg);
        usernameEt = (EditText) findViewById(R.id.username_et);
        avatarIv = (ImageView) findViewById(R.id.avatar_iv);
        autoDetectBtn = (Button) findViewById(R.id.auto_detect_btn);
        submitButton = (Button) findViewById(R.id.submit_btn);
        submitButton.setVisibility(View.GONE);
        autoDetectBtn.setOnClickListener(this);
        submitButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == autoDetectBtn) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest
                    .permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 100);
                return;
            }
            avatarIv.setImageResource(R.drawable.face_avatar);
            faceImagePath = null;
            int type = PreferencesUtil.getInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_NO_LIVENSS);
            if (type == GlobalFaceTypeModel.TYPE_NO_LIVENSS || type == GlobalFaceTypeModel.TYPE_RGB_LIVENSS) {
                imageName = UUID.randomUUID().toString();
                boolean facePicture = FaceManager.getManager().getFacePicture(RegActivity.this, imageName);
                if (!facePicture) {
                    toast("照片名称不能为空");
                }
            }
        } else if (v == submitButton) {
            final String username = usernameEt.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                toast("用户名称不能为空");
                return;
            }
            FaceManager.getManager().registerFace(imageName, username, faceImagePath, new FaceRegisterCallback() {
                @Override
                public void onRegisterResult(int code, String msg) {
                    toast("人脸注册结果：：code=" + code + ":::msg=" + msg);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RgbDetectActivity.CODE_PICK_PHOTO && data != null) {
            faceImagePath = data.getStringExtra(RgbDetectActivity.FILE_PATH);
            Bitmap bitmap = BitmapFactory.decodeFile(faceImagePath);
            avatarIv.setImageBitmap(bitmap);
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    private void toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper());
}
