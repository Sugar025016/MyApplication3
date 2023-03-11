package com.example.doll;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doll.entity.ResponseData;
import com.example.doll.util.ViewUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.net.HttpHeaders;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    OkHttpClient client = new OkHttpClient().newBuilder().build();


    private static final int REQUEST_ACCESS_FINE_LOCATION= 102;
    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private Button btn_login;
    private CheckBox ck_remember;
    private EditText et_account;
    private ActivityResultLauncher<Intent> register;
    private static MyApplication myApplication ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermission();
        myApplication= (MyApplication) getApplicationContext();
        tv_password= findViewById(R.id.tv_password);
        et_account= findViewById(R.id.et_account);
        et_account.setText("sugar025016@gmail.com");
        et_password= findViewById(R.id.et_password);
        et_password.setText("123");
        btn_forget= findViewById(R.id.btn_forget);
        ck_remember= findViewById(R.id.ck_remember);
        btn_forget= findViewById(R.id.btn_forget);
        btn_login= findViewById(R.id.btn_login);
        et_account.addTextChangedListener(new HideTextWatcher(et_account,20));
        et_password.addTextChangedListener(new HideTextWatcher(et_password,6));

        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                if(intent != null && result.getResultCode() == Activity.RESULT_OK){

//                    mpassword = intent.getStringExtra("new_password");
                }

            }
        });

    }
    private void checkPermission(){
        int i = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if(i!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS_FINE_LOCATION);
        }


    }


    @Override
    public void onClick(View v) {
        String s  =et_account.getText().toString();

//        if(s.length()<30){
//            Toast.makeText(this,"pleas input XXXXXXX",Toast.LENGTH_SHORT).show();
//            return;
//        }
        switch  (v.getId()){
            case R.id.btn_forget:
                    Intent intent = new Intent(this, LoginForgetActivity.class);
                    intent.putExtra("phone",s);
                    register.launch(intent);
                break;
            case R.id.btn_login:

                System.out.println("et_phone"+et_account.getText().toString());
                System.out.println("et_password"+et_password.getText().toString());

                FormBody formBody = new FormBody.Builder()
                        .add("username", et_account.getText().toString())
                        .add("password", et_password.getText().toString())
                        .add("remember-me", String.valueOf(ck_remember.isChecked()))
                        .build();

                // 建立Request，設置連線資訊
                Request request = new Request.Builder()
                        .url("http://192.168.43.87:8081/login")
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
                okhttp3.Call call = client.newCall(request);

                // 執行Call連線到網址
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        // 連線失敗

                        Log.d("OkHttp result error", e.toString());

                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {

//                        String header = response.header("Set-Cookie");
//                        Log.d("response", header);
                        String header="";
                        // 連線成功，自response取得連線結果
                        List<String> values = response.headers().values("Set-Cookie");
                        for (String value : values) {
                            header=header+value+";";
                            String[] pairs = value.split(";");
                            Optional<String> first = Arrays.stream(pairs).filter(v ->
                                    v.split("=")[0].equals("XSRF-TOKEN")
                            ).collect(Collectors.toList()).stream().findFirst();
                            if(first.isPresent()){
                                myApplication.setXsrf(first.get().split("=")[1]);
                            }
                        }
                        myApplication.setCookie(header);
                        Log.d("response-header", header);

                        String result = response.body().string();
                        Log.d("OkHttp result", result);
//                        new Intent(this,ShoppingActivity.class)‘
                        Gson gson = new Gson();
                        ResponseData responseData = gson.fromJson(result,  ResponseData.class);
                        Log.d("OkHttp -----getMessage", responseData.getMessage());
                        response.close();

                        if(responseData.isSuccess()){
                            Intent intent = new Intent(LoginActivity.this, ShoppingActivity.class);
                            startActivity(intent);
                        }else {
                          Looper.prepare();
                            Toast.makeText(LoginActivity.this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    }


                });


                break;

        }
    }
    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;

        public HideTextWatcher(EditText v, int maxLength) {
            this.mView=v;
            this.mMaxLength=maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length() == mMaxLength){
                ViewUtil.hideOneInputMethod(LoginActivity.this,mView);
            }
        }
    }
}