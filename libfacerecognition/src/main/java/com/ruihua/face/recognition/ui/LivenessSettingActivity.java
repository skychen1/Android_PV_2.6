/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.utils.GlobalFaceTypeModel;
import com.ruihua.face.recognition.utils.PreferencesUtil;

public class LivenessSettingActivity extends Activity implements View.OnClickListener {

    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioGroup livenessRg;
    private Button confirmBtn;
    private int livenessType = GlobalFaceTypeModel.TYPE_NO_LIVENSS;

    private RadioGroup rgCamera;
    private RadioButton rbOrbbec;
    private RadioButton rbIminect;
    private RadioButton rbOrbbecPro;

    private LinearLayout linearCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_liveness_setting_layout);
        findView();

        int livenessType = PreferencesUtil.getInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_NO_LIVENSS);
        int cameraType = PreferencesUtil.getInt(GlobalFaceTypeModel.TYPE_CAMERA, GlobalFaceTypeModel.ORBBEC);
        defaultLiveness(livenessType);
        defaultCamera(cameraType);
    }

    private void findView() {
        linearCamera = findViewById(R.id.linear_camera);
        radioButton1 = (RadioButton) findViewById(R.id.no_liveness_rb);
        radioButton2 = (RadioButton) findViewById(R.id.rgb_liveness_rb);
        radioButton3 = (RadioButton) findViewById(R.id.rgb_depth_liveness_rb);
        radioButton4 = (RadioButton) findViewById(R.id.rgb_ir_liveness_rb);
        radioButton5 = (RadioButton) findViewById(R.id.rgb_ir_depth_liveness_rb);
        livenessRg = (RadioGroup) findViewById(R.id.liveness_rg);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(this);
        rgCamera = findViewById(R.id.rg_camera);
        rbOrbbec = findViewById(R.id.rb_orbbec);
        rbIminect = findViewById(R.id.rb_iminect);
        rbOrbbecPro = findViewById(R.id.rb_orbbec_pro);

        livenessRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                if (checkedId == R.id.no_liveness_rb) {
                    linearCamera.setVisibility(View.GONE);
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_NO_LIVENSS);
                } else if (checkedId == R.id.rgb_liveness_rb) {
                    linearCamera.setVisibility(View.GONE);
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_RGB_LIVENSS);
                } else if (checkedId == R.id.rgb_ir_liveness_rb) {
                    linearCamera.setVisibility(View.GONE);
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_RGB_IR_LIVENSS);
                } else if (checkedId == R.id.rgb_depth_liveness_rb) {
                    linearCamera.setVisibility(View.VISIBLE);
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_RGB_DEPTH_LIVENSS);
                } else if (checkedId == R.id.rgb_ir_depth_liveness_rb) {
                    linearCamera.setVisibility(View.GONE);
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_RGB_IR_DEPTH_LIVENSS);
                }
            }
        });
        rgCamera.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i == R.id.rb_orbbec) {
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_CAMERA, GlobalFaceTypeModel.ORBBEC);
                } else if (i == R.id.rb_iminect) {
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_CAMERA, GlobalFaceTypeModel.IMIMECT);
                } else if (i == R.id.rb_orbbec_pro) {
                    PreferencesUtil.putInt(GlobalFaceTypeModel.TYPE_CAMERA, GlobalFaceTypeModel.ORBBECPRO);
                }
            }
        });
    }

    private void defaultLiveness(int livenessType) {
        if (livenessType == GlobalFaceTypeModel.TYPE_NO_LIVENSS) {
            radioButton1.setChecked(true);
        } else if (livenessType == GlobalFaceTypeModel.TYPE_RGB_LIVENSS) {
            radioButton2.setChecked(true);
        } else if (livenessType == GlobalFaceTypeModel.TYPE_RGB_DEPTH_LIVENSS) {
            radioButton3.setChecked(true);
        } else if (livenessType == GlobalFaceTypeModel.TYPE_RGB_IR_LIVENSS) {
            radioButton4.setChecked(true);
        } else if (livenessType == GlobalFaceTypeModel.TYPE_RGB_IR_DEPTH_LIVENSS) {
            radioButton5.setChecked(true);
        }
    }

    private void defaultCamera(int cameraType) {
        if (cameraType == GlobalFaceTypeModel.ORBBEC) {
            rbOrbbec.setChecked(true);
        } else if (cameraType == GlobalFaceTypeModel.IMIMECT) {
            rbIminect.setChecked(true);
        } else if (cameraType == GlobalFaceTypeModel.ORBBECPRO) {
            rbOrbbecPro.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == confirmBtn) {
            finish();
        }
    }
}
