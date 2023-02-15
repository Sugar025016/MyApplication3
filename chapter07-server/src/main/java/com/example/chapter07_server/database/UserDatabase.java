package com.example.chapter07_server.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication6.enity.User;

@Database(entities = {User.class},version = 1,exportSchema = true)
public abstract class UserDatabase extends RoomDatabase {

    public abstract User user();


}
 