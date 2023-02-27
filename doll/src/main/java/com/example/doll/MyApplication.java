package com.example.doll;

import android.app.Application;


public class MyApplication extends Application {

    private String cookie;
    private String xsrf;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getXsrf() {
        return xsrf;
    }

    public void setXsrf(String xsrf) {
        this.xsrf = xsrf;
    }
}
