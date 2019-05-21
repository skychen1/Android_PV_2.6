package com.ruihua.face.recognition.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.ruihua.face.recognition.entity.Feature;
import com.ruihua.face.recognition.entity.Group;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DBManager {
    /**
     * The constant TAG
     */
    private static final String TAG = "DBManager";

    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static DBManager instance;
    private static SQLiteOpenHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private boolean allowTransaction = true;
    private Lock writeLock = new ReentrantLock();
    private volatile boolean writeLocked = false;

    /**
     * 获取DBManager 实例
     *
     * @return DBManager
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public void release() {
        if (mDBHelper != null) {
            mDBHelper.close();
            mDBHelper = null;
        }
        instance = null;
    }

    public void init(Context context) {
        if (context == null) {
            return;
        }

        if (mDBHelper == null) {
            mDBHelper = new DBHelper(context.getApplicationContext());
        }
    }

    /**
     * 打开数据库
     *
     * @return SQLiteDatabase
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            try {
                mDatabase = mDBHelper.getWritableDatabase();
            } catch (Exception e) {
                mDatabase = mDBHelper.getReadableDatabase();
            }
        }
        return mDatabase;
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    public boolean addGroup(Group group) {
        if (mDBHelper == null) {
            return false;
        }

        mDatabase = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("group_id", group.getGroupId());
        cv.put("desc", group.getDesc() == null ? "" : group.getDesc());
        cv.put("update_time", System.currentTimeMillis());
        cv.put("ctime", System.currentTimeMillis());

        long rowId = -1;
        try {
            rowId = mDatabase.insert(DBHelper.TABLE_USER_GROUP, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rowId < 0) {
            return false;
        }
        LogUtils.i("insert group success:" + rowId);
        return true;
    }

    public boolean addUser(User user) {
        if (mDBHelper == null) {
            return false;
        }
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            ContentValues cv = new ContentValues();
            cv.put("user_id", user.getUserId());
            cv.put("user_info", user.getUserInfo());
            cv.put("group_id", user.getGroupId());
            cv.put("update_time", System.currentTimeMillis());
            cv.put("ctime", System.currentTimeMillis());

            long rowId = mDatabase.insert(DBHelper.TABLE_USER, null, cv);
            if (rowId < 0) {
                return false;
            }

            for (Feature feature : user.getFeatureList()) {
                if (!addFeature(feature, mDatabase)) {
                    return false;
                }
            }
            setTransactionSuccessful(mDatabase);
            LogUtils.i("insert user success:" + rowId);
        } finally {
            endTransaction(mDatabase);
        }

        return true;
    }


    public boolean addFeature(Feature feature) {
        if (mDBHelper == null) {
            return false;
        }
        mDatabase = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("face_token", feature.getFaceToken());
        cv.put("feature", feature.getFeature());
        cv.put("user_id", feature.getUserId());
        cv.put("group_id", feature.getGroupId());
        cv.put("update_time", System.currentTimeMillis());
        cv.put("ctime", System.currentTimeMillis());


        if (mDatabase.insert(DBHelper.TABLE_FEATURE, null, cv) < 0) {
            return false;
        }

        return true;
    }

    public boolean addFeature(Feature feature, SQLiteDatabase mDatabase) {

        ContentValues cv = new ContentValues();
        cv.put("face_token", feature.getFaceToken());
        cv.put("feature", feature.getFeature());
        cv.put("user_id", feature.getUserId());
        cv.put("group_id", feature.getGroupId());
        cv.put("image_name", feature.getImageName());
        cv.put("update_time", System.currentTimeMillis());
        cv.put("ctime", System.currentTimeMillis());

        if (mDatabase.insert(DBHelper.TABLE_FEATURE, null, cv) < 0) {
            return false;
        }

        return true;
    }

    public List<Feature> queryFeatureByUeserId(String userId) {
        ArrayList<Feature> featureList = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (mDBHelper == null) {
                return featureList;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "user_id = ? ";
            String[] whereValue = {userId};
            cursor = db.query(DBHelper.TABLE_FEATURE, null, where, whereValue, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                String faceToken = cursor.getString(cursor.getColumnIndex("face_token"));
                byte[] featureContent = cursor.getBlob(cursor.getColumnIndex("feature"));
                String groupId = cursor.getString(cursor.getColumnIndex("group_id"));
                String imageName = cursor.getString(cursor.getColumnIndex("image_name"));
                long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                long ctime = cursor.getLong(cursor.getColumnIndex("ctime"));

                Feature feature = new Feature();
                feature.setFaceToken(faceToken);
                feature.setFeature(featureContent);
                feature.setCtime(ctime);
                feature.setUpdateTime(updateTime);
                feature.setGroupId(groupId);
                feature.setUserId(userId);
                feature.setImageName(imageName);
                featureList.add(feature);
            }
        } finally {
            closeCursor(cursor);
        }
        return featureList;
    }

    public List<Feature> queryFeatureByGroupId(String groupId) {
        ArrayList<Feature> featureList = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (mDBHelper == null) {
                return featureList;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "group_id = ? ";
            String[] whereValue = {groupId};
            cursor = db.query(DBHelper.TABLE_FEATURE, null, where, whereValue, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                String faceToken = cursor.getString(cursor.getColumnIndex("face_token"));
                byte[] featureContent = cursor.getBlob(cursor.getColumnIndex("feature"));
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                long ctime = cursor.getLong(cursor.getColumnIndex("ctime"));
                String imageName = cursor.getString(cursor.getColumnIndex("image_name"));

                Feature feature = new Feature();
                feature.setFaceToken(faceToken);
                feature.setFeature(featureContent);
                feature.setCtime(ctime);
                feature.setUpdateTime(updateTime);
                feature.setGroupId(groupId);
                feature.setUserId(userId);
                feature.setImageName(imageName);
                featureList.add(feature);
            }
        } finally {
            closeCursor(cursor);
        }
        return featureList;
    }

    public byte[] queryFeature(String faceToken) {
        byte[] feature = null;
        Cursor cursor = null;

        try {
            if (mDBHelper == null) {
                return feature;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "face_token = ? ";
            String[] whereValue = {faceToken};
            cursor = db.query(DBHelper.TABLE_FEATURE, null, where, whereValue, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                feature = cursor.getBlob(cursor.getColumnIndex("feature"));

            }
        } finally {
            closeCursor(cursor);
        }
        return feature;
    }

    public List<Feature> queryFeature(String groupId, String userId) {
        ArrayList<Feature> featureList = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (mDBHelper == null) {
                return featureList;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "group_id = ? and user_id = ?";
            String[] whereValue = {groupId, userId};
            cursor = db.query(DBHelper.TABLE_FEATURE, null, where, whereValue, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                String faceToken = cursor.getString(cursor.getColumnIndex("face_token"));
                byte[] featureContent = cursor.getBlob(cursor.getColumnIndex("feature"));
                long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                long ctime = cursor.getLong(cursor.getColumnIndex("ctime"));
                String imageName = cursor.getString(cursor.getColumnIndex("image_name"));

                Feature feature = new Feature();
                feature.setFaceToken(faceToken);
                feature.setFeature(featureContent);
                feature.setCtime(ctime);
                feature.setUpdateTime(updateTime);
                feature.setGroupId(groupId);
                feature.setUserId(userId);
                feature.setImageName(imageName);
                featureList.add(feature);
            }
        } finally {
            closeCursor(cursor);
        }
        return featureList;
    }

    public User queryUser(String groupId, String userId) {
        Cursor cursor = null;

        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "user_id = ? and group_id = ? ";
            String[] whereValue = {userId, groupId};
            cursor = db.query(DBHelper.TABLE_USER, null, where, whereValue, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                String userInfo = cursor.getString(cursor.getColumnIndex("user_info"));
                long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                long ctime = cursor.getLong(cursor.getColumnIndex("ctime"));

                User user = new User();
                user.setUserId(userId);
                user.setGroupId(groupId);
                user.setUserInfo(userInfo);
                user.setCtime(ctime);
                user.setUpdateTime(updateTime);
                return user;
            }
        } finally {
            closeCursor(cursor);
        }
        return null;
    }

    public List<User> queryUserByGroupId(String groupId) {
        Cursor cursor = null;
        List<User> users = new ArrayList<>();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "group_id = ? ";
            String[] whereValue = {groupId};
            cursor = db.query(DBHelper.TABLE_USER, null, where, whereValue, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                String userInfo = cursor.getString(cursor.getColumnIndex("user_info"));
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                long ctime = cursor.getLong(cursor.getColumnIndex("ctime"));

                User user = new User();
                user.setUserId(userId);
                user.setGroupId(groupId);
                user.setUserInfo(userInfo);
                user.setCtime(ctime);
                user.setUpdateTime(updateTime);
                users.add(user);
            }
        } finally {
            closeCursor(cursor);
        }
        return users;
    }

    public List<Group> queryGroups(int start, int offset) {
        Cursor cursor = null;
        List<Group> groupList = new ArrayList<>();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
//            String limit = "_id asc limit " +  start + " offset " + offset;
            String limit = start + " , " + offset;
            cursor = db.query(DBHelper.TABLE_USER_GROUP, null, null, null, null, null, null, limit);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                String groupId = cursor.getString(cursor.getColumnIndex("group_id"));
                String desc = cursor.getString(cursor.getColumnIndex("desc"));
                long updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                long ctime = cursor.getLong(cursor.getColumnIndex("ctime"));

                Group group = new Group();
                group.setGroupId(groupId);
                group.setDesc(desc);

                groupList.add(group);
            }
        } finally {
            closeCursor(cursor);
        }
        return groupList;
    }

    public boolean updateUser(User user, int mode) {

        boolean success = false;
        if (mDBHelper == null) {
            return success;
        }
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            if (user != null) {
                mDatabase.beginTransaction();
                String where = "user_id = ? and group_id = ?";
                String[] whereValue = {user.getUserId(), user.getGroupId()};
                ContentValues cv = new ContentValues();

                cv.put("user_id", user.getUserInfo());
                cv.put("user_info", user.getUserInfo());
                cv.put("group_id", user.getGroupId());
                cv.put("update_time", System.currentTimeMillis());
                if (mDatabase.update(DBHelper.TABLE_USER, cv, where, whereValue) < 0) {
                    return false;
                }

                // 1更新，直接删掉已有feature  2追加
                if (mode == 1) {
                    if (mDatabase.delete(DBHelper.TABLE_FEATURE, where, whereValue) < 0) {
                        return false;
                    }
                    for (Feature feature : user.getFeatureList()) {
                        if (!addFeature(feature, mDatabase)) {
                            return false;
                        }
                    }
                } else if (mode == 2) {
                    for (Feature feature : user.getFeatureList()) {
                        if (!addFeature(feature, mDatabase)) {
                            return false;
                        }
                    }
                }
            }
            setTransactionSuccessful(mDatabase);
            success = true;
        } finally {
            endTransaction(mDatabase);
        }
        return success;
    }

    public boolean deleteFeature(String userId, String groupId, String faceToken) {
        boolean success = false;
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            if (!TextUtils.isEmpty(userId)) {
                String where = "user_id = ? and groupId = ? and face_token=?";
                String[] whereValue = {userId, groupId, faceToken};

                if (mDatabase.delete(DBHelper.TABLE_FEATURE, where, whereValue) < 0) {
                    return false;
                }
                setTransactionSuccessful(mDatabase);
                success = true;
            }

        } finally {
            endTransaction(mDatabase);
        }
        return success;
    }

    public boolean deleteUser(String userId, String groupId) {
        boolean success = false;
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            if (!TextUtils.isEmpty(userId)) {
                String where = "user_id = ? and group_id = ?";
                String[] whereValue = {userId, groupId};

                if (mDatabase.delete(DBHelper.TABLE_FEATURE, where, whereValue) < 0) {
                    return false;
                }
                if (mDatabase.delete(DBHelper.TABLE_USER, where, whereValue) < 0) {
                    return false;
                }

                setTransactionSuccessful(mDatabase);
                success = true;
            }

        } finally {
            endTransaction(mDatabase);
        }
        return success;
    }

    public boolean deleteGroup(String groupId) {
        boolean success = false;
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            if (!TextUtils.isEmpty(groupId)) {
                String where = "group_id = ?";
                String[] whereValue = {groupId};
                if (mDatabase.delete(DBHelper.TABLE_FEATURE, where, whereValue) < 0) {
                    return false;
                }
                if (mDatabase.delete(DBHelper.TABLE_USER, where, whereValue) < 0) {
                    return false;
                }
                if (mDatabase.delete(DBHelper.TABLE_USER_GROUP, where, whereValue) < 0) {
                    return false;
                }

                setTransactionSuccessful(mDatabase);
                success = true;
            }

        } finally {
            endTransaction(mDatabase);
        }
        return success;
    }


    private void beginTransaction(SQLiteDatabase mDatabase) {
        if (allowTransaction) {
            mDatabase.beginTransaction();
        } else {
            writeLock.lock();
            writeLocked = true;
        }
    }

    private void setTransactionSuccessful(SQLiteDatabase mDatabase) {
        if (allowTransaction) {
            mDatabase.setTransactionSuccessful();
        }
    }

    private void endTransaction(SQLiteDatabase mDatabase) {
        if (allowTransaction) {
            mDatabase.endTransaction();
        }
        if (writeLocked) {
            writeLock.unlock();
            writeLocked = false;
        }
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable e) {
            }
        }
    }
}