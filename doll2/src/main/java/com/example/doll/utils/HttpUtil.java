package com.example.doll.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {

    public static String gethttpresult(String urlStr){
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            System.out.println(connection.getResponseCode());
            StringBuffer sb = new StringBuffer();
            while ((line=in.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println(e.toString());
            return null;
        }

    }
}
