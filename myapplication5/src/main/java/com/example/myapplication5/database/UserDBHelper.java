package com.example.myapplication5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication5.enity.User;

import java.util.ArrayList;
import java.util.List;

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

    public SQLiteDatabase openReadLink( ) {
        if(mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    public SQLiteDatabase openWriteLink( ) {
        if(mWDB == null || !mWDB.isOpen()){
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }
    public void closeLink( ) {
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "    _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "    name varchar(255)," +
                "    age INTEGER," +
                "    height   LONG NOT NULL," +
                "    weight FLOAT," +
                "    married INTEGER" +
                ");";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Long insert(User user){
        Log.d("user",user.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.name);
        contentValues.put("age",user.age);
        contentValues.put("height",user.height);
        contentValues.put("weight",user.weight);
        contentValues.put("married",user.married);
        return mWDB.insert(TABLE_NAME,null,contentValues);
    }

    public int deleteByName(String name){
        String s = new String();
        return mWDB.delete(TABLE_NAME,"name=?",new String[]{name});
    }

    public int updateByName(User user ){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.name);
        contentValues.put("age",user.age);
        contentValues.put("height",user.height);
        contentValues.put("weight",user.weight);
        contentValues.put("married",user.married);
        String s = new String();
        return mWDB.update(TABLE_NAME, contentValues,"name=?",new String[]{user.name});
    }

    public List<User> queryAll(){
        List<User> userArrayList = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getLong(3),
                    cursor.getFloat(4),
                    cursor.getInt(4)==0?false:true);
            userArrayList.add(user);
        }
        return userArrayList;
    }
    public List<User> querByName(String name){
        List<User> userArrayList = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME, null, "name=?",new String[]{name}, null, null, null);

        while (cursor.moveToNext()){
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getLong(3),
                    cursor.getFloat(4),
                    cursor.getInt(4)==0?false:true);
            userArrayList.add(user);
        }
        return userArrayList;
    }
}
