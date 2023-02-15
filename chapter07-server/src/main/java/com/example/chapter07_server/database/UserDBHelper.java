package com.example.chapter07_server.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {

    private static  final  String DB_NAME ="user.db";
    private static  final  String TABLE_NAME ="user_info";
    private static  final  int  DB_VERSION =1;
    private static    UserDBHelper  mHelper =null;

    public UserDBHelper( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static UserDBHelper getInstance( Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
        if(mHelper == null){
            mHelper =new UserDBHelper(context);
        }
        return mHelper;
    }

//    public UserDBHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
//        super(context, name, version, openParams);
//    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+"("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "name VARCHAR NOT NULL ," +
                "age INTEGER NOT NULL," +
                "height LONG NOT NULL," +
                "weight FLOAT NOT NULL ," +
                "married INTEGER NOT NULL );";
        db.execSQL(sql);
            }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
