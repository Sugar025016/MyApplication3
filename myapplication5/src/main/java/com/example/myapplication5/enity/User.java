package com.example.myapplication5.enity;

import android.database.sqlite.SQLiteDatabase;

public class User {

    public int id ;
    public String name;
    public int age;
    public long height;
    public float weight;
    public boolean married;

    public User() {
    }

    public User(String name, int age, long height, float weight, boolean married) {

        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.married = married;

    }
    public User(int id, String name, int age, long height, float weight, boolean married) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.married = married;

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", married=" + married +
                '}';
    }
}
