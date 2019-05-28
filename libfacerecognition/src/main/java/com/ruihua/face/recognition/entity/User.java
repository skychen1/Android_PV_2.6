/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息类
 */
public class User {
    /**
     * userId  用户id，唯一标识
     * userInfo 用户姓名
     * groupId  分组的id
     * ctime
     * featureList 一张底库的特征信息，一个人可以存多张，但是目前只存一张
     */
    private String userId = "";
    private String userInfo = "";
    private String groupId = "";
    private long ctime;
    private long updateTime;
    private List<Feature> featureList = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }
}
