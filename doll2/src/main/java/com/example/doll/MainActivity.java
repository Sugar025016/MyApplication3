package com.example.doll;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.StatusLine;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.but_get);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("222222222");

                FormBody formBody = new FormBody.Builder()
                        .add("username", "sugar025016@gmail.com")
                        .add("password", "123")
                        .build();

                // 建立Request，設置連線資訊
                Request request = new Request.Builder()
                        .url("http://192.168.43.87:8081/api/login")
                        .post(formBody) // 使用post連線
                        .build();

                // 建立Request，設置連線資訊
//        Request request = new Request.Builder()
//                .url("http://192.168.43.87:8081/api/aa")
//                .build();
//                Request request = new Request.Builder()
//                        .url("http://192.168.43.87:8081/api/aa")
//                        .build();
//        Request request = new Request.Builder().get()
//                .url("https://developer.android.com/studio/intro?hl=zh-tw")
//                .build();
//        Request request = new Request.Builder()
//                .url("https://jsonplaceholder.typicode.com/posts/1")
//                .build();
                // 建立Call
                Call call = client.newCall(request);

                // 執行Call連線到網址
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 連線成功，自response取得連線結果
                        String result = response.body().string();
                        Log.d("OkHttp result", result);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 連線失敗

                        Log.d("OkHttp result error", e.toString());
                    }
                });
            }
        });
        System.out.println("12312312131321313");

    }

}