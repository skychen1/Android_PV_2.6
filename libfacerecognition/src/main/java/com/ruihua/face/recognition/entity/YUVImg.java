/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.entity;

/**
 * Created by litonghui on 2018/1/22.
 */
public class YUVImg {
    public byte[] data;
    public int width;
    public int height;
    public int angle = 0;
    public int flip = 0;

    public YUVImg(byte[] _data, int _width, int _height, int _angle, int _flip) {
        data = _data.clone();
        width = _width;
        height = _height;
        angle = _angle;
        flip = _flip;
    }
}
