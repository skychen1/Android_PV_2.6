/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.face;

import android.graphics.RectF;
import android.util.SparseIntArray;

import com.baidu.idl.facesdk.FaceInfo;
import com.ruihua.face.recognition.manager.FaceDetector;

public class LivenessDetector {

    /**
     * 提示代码，把脸移入框内
     */
    public static final int HINT_MOVE_INTO_FRAME = 30;
    /**
     * 提示代码，头抬高一些
     */
    public static final int HINT_HEAD_HIGHER = 31;
    /**
     * 提示代码，低头
     */
    public static final int HINT_HEAD_DOWN = 32;
    /**
     * 提示代码，靠近一些
     */
    public static final int HINT_COME_CLOSER = 33;
    /**
     * 提示代码，太近了，请你远一点
     */
    public static final int HINT_MOVE_FURTHER = 34;
    /**
     * 提示代码，向左转头
     */
    public static final int HINT_TURN_LEFT = 35;
    /**
     * 提示代码，向向转头
     */
    public static final int HINT_TURN_RIGHT = 36;
    /**
     * 提示代码，向左移动
     */
    public static final int HINT_MOVE_LEFT = 37;
    /**
     * 提示代码，向上转头
     */
    public static final int HINT_MOVE_UP = 38;
    /**
     * 提示代码，向右转头
     */
    public static final int HINT_MOVE_RIGHT = 39;
    /**
     * 提示代码，向下转头
     */
    public static final int HINT_MOVE_DOWN = 40;
    /**
     * 提示代码，左眼有遮挡
     */
    public static final int HINT_OCCLUSION_IN_LEFT_EYE = 11;
    /**
     * 提示代码，右眼有遮挡
     */
    public static final int HINT_OCCLUSION_IN_RIGHT_EYE = 12;
    /**
     * 提示代码，鼻子有遮挡
     */
    public static final int HINT_OCCLUSION_IN_NOSE = 13;
    /**
     * 提示代码，嘴部有遮挡
     */
    public static final int HINT_OCCLUSION_IN_MOUTH = 14;
    /**
     * 提示代码，左脸有遮挡
     */
    public static final int HINT_OCCLUSION_IN_LEFT_CHEEK = 15;
    /**
     * 提示代码，右眼有遮挡
     */
    public static final int HINT_OCCLUSION_IN_RIGHT_CHEEK = 16;
    /**
     * 提示代码，下颚有遮挡
     */
    public static final int HINT_OCCLUSION_IN_CHIN = 17;
    /**
     * 提示代码，脸部有遮挡
     */
    public static final int HINT_OCCLUSION_IN_FACE = 18;
    /**
     * 提示代码，光线太暗
     */
    public static final int HINT_LIGHT_LOW = 19;
    /**
     * 提示代码，请保持不动
     */
    public static final int HINT_KEEP_STILL = 20;
    /**
     * 提示代码，超时
     */
    public static final int HINT_TIMEOUT = 21;
    /**
     * 提示代码，成功
     */
    public static final int HINT_SUCCESS = 22;
    /**
     * 提示代码，很好
     */
    public static final int HINT_VERY_GOOD = 23;

    /**
     * 动作提示，请眨眼
     */
    public static final int ACTION_BLINK = 100;
    /**
     * 动作提示，请眨左眼
     */
    public static final int ACTION_BLINK_LEFT_EYE = 101;
    /**
     * 动作提示，请眨右眼
     */
    public static final int ACTION_BLINK_RIGHT_EYE = 102;
    /**
     * 动作提示，张嘴
     */
    public static final int ACTION_OPEN_MOUTH = 103;
    /**
     * 动作提示，向左转头
     */
    public static final int ACTION_LEAN_HEAD_LEFT = 104;
    /**
     * 动作提示，向右转头
     */
    public static final int ACTION_LEAN_HEAD_RIGHT = 105;
    /**
     * 动作提示，向上抬头
     */
    public static final int ACTION_HEAD_UP = 106;
    /**
     * 动作提示，向下抬头
     */
    public static final int ACTION_HEAD_DOWN = 107;
    /**
     * 动作提示，很好
     */
    public static final int HINT_OK = 0;

    private static SparseIntArray tips = new SparseIntArray();
    private static SparseIntArray sounds = new SparseIntArray();

    private static LivenessDetector sInstance;

    private static final int YAW_ANGLE = 10;
    private static final int PITCH_ANGLE = 15;
    private int yawAngle = YAW_ANGLE;
    private int pitchAngle = PITCH_ANGLE;
    private RectF detectRect;

    public void setYawAngle(int angle) {
        this.yawAngle = angle;
    }

    public void setPitchAngle(int angle) {
        this.pitchAngle = angle;
    }

    public static LivenessDetector getInstance() {
        synchronized (LivenessDetector.class) {
            if (sInstance == null) {
                sInstance = new LivenessDetector();
            }
        }
        return sInstance;
    }

    /**
     * 设置提示语，status为，提示代码。
     *
     * @param hintCode 提示代码
     * @param res      资源id
     */
    public void setTip(int hintCode, int res) {
        tips.append(hintCode, res);
    }

    /**
     * 获取提示代码对应的提示语。
     *
     * @param hintCode 提示代码
     * @return 对应的提示语
     */
    public int getTip(int hintCode) {
        return tips.get(hintCode);
    }

    public void setSound(int hintCode, int res) {
        sounds.append(hintCode, res);
    }

    public int getSound(int hintCode) {
        return sounds.get(hintCode);
    }

    public int getHintCode(int code, FaceInfo faceInfo, int width, int height) {
        if (faceInfo == null) {
            return HINT_MOVE_INTO_FRAME;
        }


        int status = HINT_HEAD_DOWN;

        if (code == FaceDetector.OK) {
            int faceWidth = (int) faceInfo.mWidth;
            if (faceWidth > width * 0.95) {
                status = LivenessDetector.HINT_MOVE_FURTHER;
            } else if (faceWidth < (width * 0.4f)) {
                status = LivenessDetector.HINT_COME_CLOSER;
            } else if (faceInfo.mCenter_x < width / 3) {
                status = LivenessDetector.HINT_MOVE_LEFT;
            } else if (faceInfo.mCenter_x > width / 3 * 2) {
                status = LivenessDetector.HINT_MOVE_RIGHT;
            } else if (faceInfo.mCenter_y < height / 3) {
                status = LivenessDetector.HINT_MOVE_DOWN;
            } else if (faceInfo.mCenter_y > height / 3 * 2) {
                status = LivenessDetector.HINT_MOVE_UP;
            } else {
                // yaw 左右
                // pitch 上下
                // roll 扭头
                float pitch = faceInfo.headPose[0];
                float yaw = faceInfo.headPose[1];
//                float roll = faceInfo.headPose[2];
                if (Math.abs(yaw) < yawAngle && Math.abs(pitch) < pitchAngle) {
                    status = LivenessDetector.HINT_OK;
                } else {
                    if (yaw > yawAngle) {
                        status = LivenessDetector.HINT_TURN_LEFT;
                    }
                    if (yaw < -yawAngle) {
                        status = LivenessDetector.HINT_TURN_RIGHT;
                    }
                    if (pitch > pitchAngle) {
                        status = LivenessDetector.HINT_HEAD_HIGHER;
                    }
                    if (pitch < -pitchAngle) {
                        status = LivenessDetector.HINT_HEAD_DOWN;
                    }
                }
            }
        }
        if (code == FaceDetector.NO_FACE_DETECTED) {
            status = LivenessDetector.HINT_MOVE_INTO_FRAME;
        }
        if (code == FaceDetector.POOR_ILLUMINATION) {
            status = LivenessDetector.HINT_LIGHT_LOW;
        }
        return status; // TODO
    }

    public RectF getDetectRect() {
        return detectRect;
    }

    public void setDetectRect(RectF detectRect) {
        this.detectRect = detectRect;
    }
}
