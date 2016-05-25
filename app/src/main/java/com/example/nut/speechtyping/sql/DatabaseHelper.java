package com.example.nut.speechtyping.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nut on 4/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Database
    public static final String DATABASE_NAME = "speechtyping_messages.db";
    public static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_NAME = "MESSAGES";

    // Table columns
    public static final String MEMBER_ID = "_id";
    public static final String MEMBER_TITLE = "title";
    public static final String MEMBER_MESSAGE = "message";
    public static final String MEMBER_IMAGE = "image";

    private SQLiteDatabase sqLiteDatabase;

    private static DatabaseHelper instance;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + MEMBER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MEMBER_TITLE + " TEXT NOT NULL, " + MEMBER_MESSAGE + " TEXT, " + MEMBER_IMAGE + " BLOB);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS ";
        db.execSQL(DROP_TABLE + TABLE_NAME);
        onCreate(db);
    }
}
