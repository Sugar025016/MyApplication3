package com.example.myapplication3.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ViewUtil {

    public static void hideOneInputMethod(Activity act, View v){
        InputMethodManager systemService = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        systemService.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

}
