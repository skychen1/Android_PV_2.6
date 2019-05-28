/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.entity;


import com.baidu.idl.facesdk.FaceInfo;

public class LivenessModel {

    private ImageFrame imageFrame = new ImageFrame();
    private int faceDetectCode;
    private FaceInfo faceInfo;
    private long rgbDetectDuration;
    private long rgbLivenessDuration;
    private float irLivenessScore;
    private long irLivenessDuration;
    private long detphtLivenessDuration;
    private float rgbLivenessScore;
    private float depthLivenessScore;
    private int liveType;

    public int[] getShape() {
        return shape;
    }

    public void setShape(int[] shape) {
        this.shape = shape;
    }

    private int[] shape;

    public FaceInfo[] getTrackFaceInfo() {
        return trackFaceInfo;
    }

    public void setTrackFaceInfo(FaceInfo[] trackFaceInfo) {
        this.trackFaceInfo = trackFaceInfo;
    }

    private FaceInfo[]trackFaceInfo;

    public ImageFrame getImageFrame() {
        return imageFrame;
    }

    public void setImageFrame(ImageFrame imageFrame) {
        this.imageFrame = imageFrame;
    }

    public int getFaceDetectCode() {
        return faceDetectCode;
    }

    public void setFaceDetectCode(int faceDetectCode) {
        this.faceDetectCode = faceDetectCode;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public long getRgbDetectDuration() {
        return rgbDetectDuration;
    }

    public void setRgbDetectDuration(long rgbDetectDuration) {
        this.rgbDetectDuration = rgbDetectDuration;
    }

    public float getRgbLivenessDuration() {
        return rgbLivenessDuration;
    }

    public void setRgbLivenessDuration(long rgbLivenessDuration) {
        this.rgbLivenessDuration = rgbLivenessDuration;
    }

    public float getIrLivenessScore() {
        return irLivenessScore;
    }

    public void setIrLivenessScore(float irLivenessScore) {
        this.irLivenessScore = irLivenessScore;
    }

    public long getIrLivenessDuration() {
        return irLivenessDuration;
    }

    public void setIrLivenessDuration(long irLivenessDuration) {
        this.irLivenessDuration = irLivenessDuration;
    }

    public long getDetphtLivenessDuration() {
        return detphtLivenessDuration;
    }

    public void setDetphtLivenessDuration(long detphtLivenessDuration) {
        this.detphtLivenessDuration = detphtLivenessDuration;
    }

    public float getRgbLivenessScore() {
        return rgbLivenessScore;
    }

    public void setRgbLivenessScore(float rgbLivenessScore) {
        this.rgbLivenessScore = rgbLivenessScore;
    }

    public float getDepthLivenessScore() {
        return depthLivenessScore;
    }

    public void setDepthLivenessScore(float depthLivenessScore) {
        this.depthLivenessScore = depthLivenessScore;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }
}


