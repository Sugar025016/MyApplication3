package com.example.myapplication.utils;

import android.content.Context;

public class Util {

    public static int dip2px(Context context,float qbValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (qbValue * scale + 0.5f);
    }
}
