/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_START_SQL = "CREATE TABLE IF NOT EXISTS ";
    private static final String CREATE_TABLE_PRIMIRY_SQL = " integer primary key autoincrement,";
    private static final String CREATE_TABLE = " CREATE TABLE ";
    private static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY  NOT NULL  ";
    private static final String LEFT_BRACKET = " ( ";
    private static final String RIGHT_BRACKET = " ) ";
    private static final String VARCHAR = " VARCHAR ";
    private static final String INTEGER = " INTEGER ";
    private static final String DEFAULT = " DEFAULT ";
    private static final String DOT = " , ";
    private static final String DEFAULT_ENPTY_STRING = " DEFAULT \"\" ";
    private static final String DEFAULT_ZERO = " DEFAULT 0 ";
    private static final String DEFAULT_ONE = " DEFAULT 1 ";
    /** 数据库名称 */
    private static final String DB_NAME = "face.db";
    /** 数据库版本 */
    private static final int VERSION = 1;
    /** 人脸特征表 */
    public static final String TABLE_FEATURE = "feature";
    /** 用户组表 */
    public static final String TABLE_USER_GROUP = "user_group";
    public static final String TABLE_USER = "user";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }
    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_GROUP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

            onCreate(db);
        }
    }
    public synchronized void createTables(SQLiteDatabase db) {
        if (db == null || db.isReadOnly()) {
            db = getWritableDatabase();
        }
        // 创建人脸特征表的SQL语句
        StringBuffer featureSql = new StringBuffer();
        featureSql.append(CREATE_TABLE_START_SQL).append(TABLE_FEATURE).append(" ( ");
        featureSql.append(" _id").append(CREATE_TABLE_PRIMIRY_SQL);
        featureSql.append(" face_token").append(" varchar(128) default \"\" ,");
        featureSql.append(" group_id").append(" varchar(32) default \"\" ,");
        featureSql.append(" user_id").append(" varchar(32) default \"\" ,");
        featureSql.append(" feature").append(" blob   ,");
        featureSql.append(" image_name").append(" varchar(64) default \"\"  ,");
        featureSql.append(" ctime").append(" long ,");
        featureSql.append(" update_time").append(" long )");

        // 创建用户组表的SQL语句
        StringBuffer groupSql = new StringBuffer();
        groupSql.append(CREATE_TABLE_START_SQL).append(TABLE_USER_GROUP).append(" ( ");
        groupSql.append(" _id").append(CREATE_TABLE_PRIMIRY_SQL);
        groupSql.append(" group_id").append(" varchar(32) default \"\" ,");
        groupSql.append(" desc").append(" varchar(32) default \"\"  ,");
        groupSql.append(" ctime").append(" long ,");
        groupSql.append(" update_time").append(" long )");

        // 创建用户表的SQL语句
        StringBuffer userSql = new StringBuffer();
        userSql.append(CREATE_TABLE_START_SQL).append(TABLE_USER).append(" ( ");
        userSql.append(" _id").append(CREATE_TABLE_PRIMIRY_SQL);
        userSql.append(" user_id").append(" varchar(32) default \"\"   ,");
        userSql.append(" user_info").append(" varchar(32) default \"\"   ,");
        userSql.append(" group_id").append(" varchar(32) default \"\"   ,");

        userSql.append(" ctime").append(" long ,");
        userSql.append(" update_time").append(" long )");


        try {
            db.execSQL(groupSql.toString());
            db.execSQL(userSql.toString());
            db.execSQL(featureSql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
