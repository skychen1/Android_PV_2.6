/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.api.FaceApi;
import com.ruihua.face.recognition.entity.Feature;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.utils.FileUitls;

import java.io.File;
import java.util.List;

public class UserActivity extends Activity {

    private TextView userIdTv;
    private TextView userInfoTv;
    private TextView groupIdTv;
    private TextView featureTv;
    private ImageView faceIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_user);

        findView();
        display();

    }

    private void findView() {
        userIdTv = (TextView) findViewById(R.id.user_id_tv);
        userInfoTv = (TextView) findViewById(R.id.user_info_tv);
        groupIdTv = (TextView) findViewById(R.id.group_id_tv);
        featureTv = (TextView) findViewById(R.id.feature_tv);
        faceIv = (ImageView) findViewById(R.id.face_iv);
    }

    private void display() {
        Intent intent = getIntent();
        if (intent != null) {
            String userId = intent.getStringExtra("user_id");
            String userInfo = intent.getStringExtra("user_info");
            String groupId = intent.getStringExtra("group_id");


            userIdTv.setText(userId);
            userInfoTv.setText(userInfo);
            groupIdTv.setText(groupId);

            User user = FaceApi.getInstance().getUserInfo(groupId, userId);
            List<Feature> featureList = user.getFeatureList();
            if (featureList != null && featureList.size() > 0) {
                // featureTv.setText(new String(featureList.get(0).getFeature()));
                File faceDir = FileUitls.getFaceDirectory();
                if (faceDir != null && faceDir.exists()) {
                    File file = new File(faceDir, featureList.get(0).getImageName());
                    if (file != null && file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        faceIv.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }
}
