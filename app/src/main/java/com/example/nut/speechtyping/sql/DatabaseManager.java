package com.example.nut.speechtyping.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.nut.speechtyping.util.DbBitmapUtility;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class DatabaseManager {

//    private static DatabaseManager instance;
//
//    public static DatabaseManager getInstance() {
//        if (instance == null)
//            instance = new DatabaseManager();
//        return instance;
//    }

    private Context mContext;

    public DatabaseManager() {
        mContext = Contextor.getInstance().getContext();
    }

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;


    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();
        database = dbHelper.getReadableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }

    //Inserting Data into table
    public void insertData(String title, String message) {
        ContentValues cvInsert = new ContentValues();
        cvInsert.put(DatabaseHelper.MEMBER_TITLE, title);
        cvInsert.put(DatabaseHelper.MEMBER_MESSAGE, message);
        database.insert(DatabaseHelper.TABLE_NAME, null, cvInsert);
    }

    //Getting Cursor to read data from table
    public Cursor fetchData() {
        String[] allColumns = new String[]{DatabaseHelper.MEMBER_ID,
                DatabaseHelper.MEMBER_TITLE, DatabaseHelper.MEMBER_MESSAGE};
        Cursor c = database.query(DatabaseHelper.TABLE_NAME, allColumns, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor reFetchData() {
        String[] allColumns = new String[]{DatabaseHelper.MEMBER_ID,
                DatabaseHelper.MEMBER_TITLE, DatabaseHelper.MEMBER_MESSAGE};
        Cursor c = database.query(DatabaseHelper.TABLE_NAME, allColumns, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Bitmap fetchImage(int id) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{new Integer(id).toString()};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{"_id", "image"}, whereClause, whereArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                byte[] imgByte = cursor.getBlob(1);
                return DbBitmapUtility.getImage(imgByte);
            } else
                return null;
        } finally {
            cursor.close();
        }
    }

    public int updateName(long memberID, String title) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DatabaseHelper.MEMBER_TITLE, title);
        int i = database.update(DatabaseHelper.TABLE_NAME, cvUpdate,
                DatabaseHelper.MEMBER_ID + " = " + memberID, null);
        return i;
    }

    //Updating record data into table by id
    public int updateData(long memberID, String message) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DatabaseHelper.MEMBER_MESSAGE, message);
        int i = database.update(DatabaseHelper.TABLE_NAME, cvUpdate,
                DatabaseHelper.MEMBER_ID + " = " + memberID, null);
        return i;
    }

    public int updateImage(long memberID, Bitmap image) throws SQLiteException {
        byte[] data = DbBitmapUtility.getBytes(image);

        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DatabaseHelper.MEMBER_IMAGE, data);
        int i = database.update(DatabaseHelper.TABLE_NAME, cvUpdate,
                DatabaseHelper.MEMBER_ID + " = " + memberID, null);

        Log.d("image", "update image");
        return i;
    }


    // Deleting record data from table by id
    public void deleteData(long memberID) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.MEMBER_ID + "="
                + memberID, null);
    }

}
