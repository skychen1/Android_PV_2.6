/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.ruihua.face.recognition.db.DBManager;
import com.ruihua.face.recognition.entity.ARGBImg;
import com.ruihua.face.recognition.entity.Feature;
import com.ruihua.face.recognition.entity.Group;
import com.ruihua.face.recognition.entity.IdentifyRet;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.FeatureUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaceApi {

    public static final int FACE_FILE = 1;
    public static final int FACE_TOKEN = 2;
    private HashMap<String, HashMap<String, byte[]>> group2Facesets = new HashMap<>();
    private static FaceApi instance;

    private FaceApi() {

    }

    public static synchronized FaceApi getInstance() {
        if (instance == null) {
            instance = new FaceApi();
        }
        return instance;
    }

    public boolean groupAdd(Group group) {
        if (group == null || TextUtils.isEmpty(group.getGroupId())) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_-]{1,}$");
        Matcher matcher = pattern.matcher(group.getGroupId());
        if (!matcher.matches()) {
            return false;
        }
        return DBManager.getInstance().addGroup(group);
    }

    public boolean userAdd(User user) {
        if (user == null || TextUtils.isEmpty(user.getGroupId()) || user.getFeatureList().size() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_-]{1,}$");
        Matcher matcher = pattern.matcher(user.getUserId());
        if (!matcher.matches()) {
            return false;
        }
        boolean ret = DBManager.getInstance().addUser(user);
        return ret;
    }

    public boolean userDelete(String userId, String groupId) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(groupId)) {
            return false;
        }
        boolean ret = DBManager.getInstance().deleteUser(userId, groupId);
        return ret;
    }

    public boolean userUpdate(User user, int mode) {
        if (user == null) {
            return false;
        }

        boolean ret = DBManager.getInstance().updateUser(user, mode);
        return ret;
    }

    public boolean userFaceDelete(String userId, String groupId, String faceToken) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(groupId) || TextUtils.isEmpty(faceToken)) {
            return false;
        }
        boolean ret = DBManager.getInstance().deleteFeature(userId, groupId, faceToken);
        return ret;
    }

    public boolean groupDelete(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return false;
        }
        boolean ret = DBManager.getInstance().deleteGroup(groupId);
        return ret;
    }

    public User getUserInfo(String groupId, String userId) {
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return null;
        }
        User user = DBManager.getInstance().queryUser(groupId, userId);
        List<Feature> featureList = DBManager.getInstance().queryFeature(groupId, userId);
        user.setFeatureList(featureList);
        return user;
    }

    public List<User> getUserList(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }
        List<User> userList = DBManager.getInstance().queryUserByGroupId(groupId);
        return userList;
    }

    public List<Group> getGroupList(int start, int length) {
        if (start < 0 || length < 0) {
            return null;
        }
        if (length > 1000) {
            length = 1000;
        }
        List<Group> groupList = DBManager.getInstance().queryGroups(start, length);
        return groupList;
    }

    public byte[] getFeature(String faceToken) {
        if (TextUtils.isEmpty(faceToken)) {
            return null;
        }
        byte[] feature = DBManager.getInstance().queryFeature(faceToken);
        return feature;
    }

    public int getFeature(Bitmap bitmap, byte[] feature) {
        if (bitmap == null) {
            return -1;
        }
        ARGBImg argbImg = FeatureUtils.getImageInfo(bitmap);
        int ret = FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg, feature);

        return ret;
    }

    public int getFeature(Bitmap bitmap, byte[] feature, int minFaceSize) {
        if (bitmap == null) {
            return -1;
        }
        ARGBImg argbImg = FeatureUtils.getImageInfo(bitmap);
        int ret = FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg, feature, minFaceSize);

        return ret;
    }


    public int getFeatureForIDPhoto(Bitmap bitmap, byte[] feature, int minFaceSize) {
        if (bitmap == null) {
            return -1;
        }
        ARGBImg argbImg = FeatureUtils.getImageInfo(bitmap);
        int ret = FaceSDKManager.getInstance().getFaceFeature().faceFeatureForIDPhoto(argbImg, feature, minFaceSize);

        return ret;
    }


    public float match(String image1, String image2, int type, Context context) {
        if (TextUtils.isEmpty(image1) || TextUtils.isEmpty(image2)) {
            return -1;
        }
        float ret = -1;
        if (type == FACE_FILE) {
            Uri uri1 = Uri.parse(image1);
            Uri uri2 = Uri.parse(image1);
            ret = match(uri1, uri2, context);
        } else if (type == FACE_TOKEN) {

            byte[] firstFeature = DBManager.getInstance().queryFeature(image1);
            byte[] secondFeature = DBManager.getInstance().queryFeature(image2);

            ret = FaceSDKManager.getInstance().getFaceFeature()
                    .getFaceFeatureDistance(firstFeature, secondFeature);
        }

        return ret;
    }

    public float match(Uri image1, Uri image2, Context context) {
        if (image1 == null) {
            return -100;
        }

        if (image2 == null) {
            return -101;
        }
        float ret = -1;

        try {
            byte[] firstFeature = new byte[2048];
            byte[] secondFeature = new byte[2048];
            final Bitmap bitmap1 = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image1));
            ARGBImg argbImg1 = FeatureUtils.getImageInfo(bitmap1);
            int ret1 = FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg1, firstFeature);

            final Bitmap bitmap2 = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image2));
            ARGBImg argbImg2 = FeatureUtils.getImageInfo(bitmap2);
            int ret2 = FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg2, secondFeature);
            if (ret1 != 512) {
                return -102;
            }
            if (ret2 != 512) {
                return -103;
            }

            ret = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistance(firstFeature, secondFeature);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }


    public float match(final byte[] photoFeature, int[] argbData, int rows, int cols, int[] landmarks) {
        if (photoFeature == null || argbData == null || landmarks == null) {
            return -1;
        }
        byte[] imageFrameFeature = new byte[2048];

        FaceSDKManager.getInstance().getFaceFeature().extractFeature(argbData, rows, cols,
                imageFrameFeature, landmarks);
        final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistance(photoFeature,
                imageFrameFeature);
        return score;
    }

    public float matchIDPhoto(final byte[] photoFeature, int[] argbData, int rows, int cols, int[] landmarks) {
        if (photoFeature == null || argbData == null || landmarks == null) {
            return -1;
        }
        byte[] imageFrameFeature = new byte[2048];

        FaceSDKManager.getInstance().getFaceFeature().extractFeatureForIDPhoto(argbData, rows, cols,
                imageFrameFeature, landmarks);
        final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistanceForIDPhoto(photoFeature,
                imageFrameFeature);
        return score;
    }

    public float match(final byte[] feature1, final byte[] feature2) {
        if (feature1 == null || feature2 == null) {
            return -1;
        }

        final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistance(feature1, feature2);
        return score;
    }

    public float matchIDPhoto(final byte[] feature1, final byte[] feature2) {
        if (feature1 == null || feature2 == null) {
            return -1;
        }

        final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistanceForIDPhoto(feature1, feature2);
        return score;
    }


    public IdentifyRet identity(String image, int type, String groupId, Context context) {
        if (TextUtils.isEmpty(image) || TextUtils.isEmpty(groupId)) {
            return null;
        }
        byte[] imageFrameFeature = new byte[2048];
        if (type == FACE_FILE) {
            try {
                Uri uri = Uri.parse(image);
                final Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
                ARGBImg argbImg = FeatureUtils.getImageInfo(bitmap);
                FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg, imageFrameFeature);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (type == FACE_TOKEN) {
            imageFrameFeature = DBManager.getInstance().queryFeature(image);
        }

        if (imageFrameFeature == null || groupId == null) {
            return null;
        }

        HashMap<String, byte[]> userId2Feature = group2Facesets.get(groupId);

        String userIdOfMaxScore = "";
        float identifyScore = 0;
        Iterator iterator = userId2Feature.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, byte[]> entry = (Map.Entry<String, byte[]>) iterator.next();
            byte[] feature = entry.getValue();
            final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistance(
                    feature, imageFrameFeature);
            if (score > identifyScore) {
                identifyScore = score;
                userIdOfMaxScore = entry.getKey();
            }
        }

        return new IdentifyRet(userIdOfMaxScore, identifyScore);
    }

    public IdentifyRet identity(String image, int type, String groupId, String userId, Context context) {
        if (TextUtils.isEmpty(image) || TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return null;
        }
        byte[] imageFrameFeature = new byte[2048];
        if (type == FACE_FILE) {
            try {
                Uri uri = Uri.parse(image);
                final Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
                ARGBImg argbImg = FeatureUtils.getImageInfo(bitmap);
                FaceSDKManager.getInstance().getFaceFeature().faceFeature(argbImg, imageFrameFeature);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (type == FACE_TOKEN) {
            imageFrameFeature = DBManager.getInstance().queryFeature(image);
        }

        if (imageFrameFeature == null || groupId == null || userId == null) {
            return null;
        }

        HashMap<String, byte[]> userId2Feature = group2Facesets.get(groupId);

        byte[] feature = userId2Feature.get(userId);
        float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistance(
                feature, imageFrameFeature);

        return new IdentifyRet(userId, score);
    }


    public IdentifyRet identity(int[] argbData, int rows, int cols, int[] landmarks, String groupId) {
        if (argbData == null || landmarks == null || groupId == null || TextUtils.isEmpty(groupId)) {
            return null;
        }
        HashMap<String, byte[]> userId2Feature = group2Facesets.get(groupId);
        byte[] imageFrameFeature = new byte[2048];
        FaceSDKManager.getInstance().getFaceFeature().extractFeature(argbData, rows, cols,
                imageFrameFeature, landmarks);
        String userIdOfMaxScore = "";
        float identifyScore = 0;
        Iterator iterator = userId2Feature.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, byte[]> entry = (Map.Entry<String, byte[]>) iterator.next();
            byte[] feature = entry.getValue();
            final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistance(
                    feature, imageFrameFeature);
            if (score > identifyScore) {
                identifyScore = score;
                userIdOfMaxScore = entry.getKey();
            }
        }

        return new IdentifyRet(userIdOfMaxScore, identifyScore);
    }


    public IdentifyRet identityForIDPhoto(int[] argbData, int rows, int cols, int[] landmarks, String groupId) {
        if (argbData == null || landmarks == null || groupId == null || TextUtils.isEmpty(groupId)) {
            return null;
        }
        HashMap<String, byte[]> userId2Feature = group2Facesets.get(groupId);

        long startExtraTime = System.currentTimeMillis();
        byte[] imageFrameFeature = new byte[2048];

        FaceSDKManager.getInstance().getFaceFeature().extractFeatureForIDPhoto(argbData, rows, cols,
                imageFrameFeature, landmarks);
        // Log.i("identity", "extractFeature duration->" + (System.currentTimeMillis() - startExtraTime));

        String userIdOfMaxScore = "";
        float identifyScore = 0;
        Iterator iterator = userId2Feature.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, byte[]> entry = (Map.Entry<String, byte[]>) iterator.next();
            byte[] feature = entry.getValue();
            final float score = FaceSDKManager.getInstance().getFaceFeature().getFaceFeatureDistanceForIDPhoto(
                    feature, imageFrameFeature);
            if (score > identifyScore) {
                identifyScore = score;
                userIdOfMaxScore = entry.getKey();
            }
        }

        return new IdentifyRet(userIdOfMaxScore, identifyScore);
    }

    public void loadFacesFromDB(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        List<Feature> featureList = DBManager.getInstance().queryFeatureByGroupId(groupId);
        HashMap<String, byte[]> userId2Feature = new HashMap<String, byte[]>();
        for (Feature feature : featureList) {
            userId2Feature.put(feature.getUserId(), feature.getFeature());
            Log.i("wtf", " loadFeature2Memery feature " + feature.getFeature());
        }
        group2Facesets.put(groupId, userId2Feature);
    }

    public HashMap<String, HashMap<String, byte[]>> getGroup2Facesets() {
        return group2Facesets;
    }

    /**
     * 添加一个人脸特征到指定分组的内存中
     *
     * @param groupId 分组id
     * @param feature 人脸特征
     */
    public void addOneFaceToMemory(String groupId, Feature feature) {
        //通过分组id，拿到对应的特征集合
        HashMap<String, byte[]> userId2Feature = group2Facesets.get(groupId);
        //如果分组存在，就直接添加，加入包含有该人员id的特征，会自动覆盖；
        if (userId2Feature != null) {
            userId2Feature.put(feature.getUserId(), feature.getFeature());
        }
    }

    /**
     * 删除指定分组内存中指定人员的人脸特征
     *
     * @param groupId 分组id
     * @param userId  人员编号，唯一标识
     */

    public void deleOneFaceFromeMemory(String groupId, String userId) {
        //通过分组id，拿到对应的特征集合
        HashMap<String, byte[]> userId2Feature = group2Facesets.get(groupId);
        //如果分组存在，就直接删除
        if (userId2Feature != null) {
            userId2Feature.remove(userId);
        }

    }


}
