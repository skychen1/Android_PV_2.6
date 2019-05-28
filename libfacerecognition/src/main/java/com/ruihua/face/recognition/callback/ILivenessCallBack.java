/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.callback;

import com.ruihua.face.recognition.entity.LivenessModel;

public interface ILivenessCallBack {
    void onCallback(LivenessModel livenessModel);

    void onTip(int code, String msg);

    void onCanvasRectCallback(LivenessModel livenessModel);
}
