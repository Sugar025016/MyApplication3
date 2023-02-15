package com.example.myapplication4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication4.enity.User;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME= "user.db";
    private static final String TABLE_NAME= "user_info";
    private static final int DB_VERSION= 1;
    private static  UserDBHelper mHelper=null;
    private SQLiteDatabase mRDB=null;
    private SQLiteDatabase mWDB=null;

    public UserDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static UserDBHelper getInstance(Context context) {
        if(mHelper == null){
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink(Context context) {
        if(mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    public SQLiteDatabase openWriteLink(Context context) {
        if(mWDB == null || !mWDB.isOpen()){
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }
    public void closeLink(Context context) {
        if(mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB=null;
        }
        if(mWDB != null && mWDB.isOpen()){
            mWDB.close();
            mWDB=null;
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + "TABLE_NAME" + "(" +
                "    _id INTEGER," +
                "    name varchar(255)," +
                "    age INTEGER(255)," +
                "    height LONG(255)," +
                "    weight FLOAT(255)," +
                "    married INTEGER(255)" +
                ");";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Long insert(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.name);
        contentValues.put("age",user.age);
        contentValues.put("height",user.height);
        contentValues.put("weight",user.weight);
        contentValues.put("married",user.married);
        return mWDB.insert(TABLE_NAME,null,contentValues);
    }
}
