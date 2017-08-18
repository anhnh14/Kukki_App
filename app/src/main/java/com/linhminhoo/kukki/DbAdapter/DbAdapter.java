package com.linhminhoo.kukki.DbAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by linhminhoo on 6/25/2015.
 */
public class DbAdapter extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Kukki";
    public static final int DATABASE_VERSION=5;

    private static final String CREATE_TABLE_USER="CREATE TABLE User (\n" +
            "id INTEGER  PRIMARY KEY NOT NULL,\n" +
            "name vaRCHAR(45)  NULL,\n" +
            "email vaRCHAR(45)  NULL,\n" +
            "api_key vaRCHAR(45)  NULL,\n" +
            "avatar_url vaRCHAR(255)  NULL\n" +
            ")";
    private static final String CREATE_TABLE_POST="CREATE TABLE Post (\n" +
            "id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "name vaRCHAR(60)  NULL,\n" +
            "description vaRCHAR(255)  NULL,\n" +
            "created_at vaRCHAR(45)  NULL,\n" +
            "updated_at vaRCHAR(45)  NULL,\n" +
            "online inTEGER  NULL,\n" +
            "user_id inTEGER  NULL\n" +
            ")";
    private static final String CREATE_TABLE_ALBUM="CREATE TABLE Album (\n" +
            "id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "name vaRCHAR(45)  NULL,\n" +
            "created_at vaRCHAR(45)  NULL,\n" +
            "updated_at vaRCHAR(45)  NULL\n" +
            ")";
    private static final String CREATE_TABLE_ALBUM_POST="CREATE TABLE Album_post (\n" +
            "id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "post_id inTEGER  NULL,\n" +
            "album_id inTEGER  NULL\n" +
            ")";
    private static final String CREATE_TABLE_CONTENT="CREATE TABLE Content (\n" +
            "id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "description vaRCHAR(255)  NULL,\n" +
            "post_id inTEGER  NULL\n" +
            ")";
    private static final String CREATE_TABLE_IMAGE="CREATE TABLE Image (\n" +
            "id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "url vaRCHAR(255)  NULL,\n" +
            "content_id inTEGER  NULL\n" +
            ")";
    private static final String CREATE_TABLE_REMEMBER="CREATE TABLE Remember (\n" +
            "email vaRCHAR(45)  NULL,\n" +
            "password vaRCHAR(45)  NULL,\n" +
            "isRember inTEGER  NULL\n" +
            ")";

    public DbAdapter(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_ALBUM);
        db.execSQL(CREATE_TABLE_ALBUM_POST);
        db.execSQL(CREATE_TABLE_CONTENT);
        db.execSQL(CREATE_TABLE_IMAGE);
        db.execSQL(CREATE_TABLE_REMEMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Post");
        db.execSQL("drop table if exists Album");
        db.execSQL("drop table if exists Album_post");
        db.execSQL("drop table if exists Content");
        db.execSQL("drop table if exists Image");
        db.execSQL("drop table if exists Remember");
        onCreate(db);
    }
}
