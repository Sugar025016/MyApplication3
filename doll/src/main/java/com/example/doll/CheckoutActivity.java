package com.example.doll;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.doll.entity.Carts;
import com.example.doll.entity.ResponseData;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    private EditText et_email;
    private EditText et_addressee_address;
    private EditText et_addressee_name;
    private EditText et_addressee_phone;
    private EditText et_message;
    private Button btn_send_order;
    private static MyApplication myApplication ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        myApplication= (MyApplication) getApplicationContext();
        et_email = findViewById(R.id.et_email);
        et_email.setText("AA@gmail.com");
        et_addressee_address = findViewById(R.id.et_addressee_address);
        et_addressee_address.setText("AAAAAAAAA");
        et_addressee_name = findViewById(R.id.et_addressee_name);
        et_addressee_name.setText("JJ");
        et_addressee_phone = findViewById(R.id.et_addressee_phone);
        et_addressee_phone.setText("0912123456");
        et_message = findViewById(R.id.et_message);
        et_message.setText("BBBBBBBBBBBBBBBBBBBB.........");
        btn_send_order = findViewById(R.id.btn_send_order);

        btn_send_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createOrder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createOrder()throws IOException {

        String json = "{\"email\":\""+et_email.getText().toString()+"\"," +
                "\"name\":\""+et_addressee_name.getText().toString()+"\"," +
                "\"tel\":\""+et_addressee_phone.getText().toString()+"\"," +
                "\"address\":\""+et_addressee_address.getText().toString()+"\"," +
                "\"message\":\""+et_message.getText().toString()+"\"" +"}";
        System.out.println(json);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        String postUrl= "http://192.168.43.87:8081/user/order";
        // 建立Request，設置連線資訊
        Request request = new Request.Builder()
                .url(postUrl)
                .header("Cookie",myApplication.getCookie())
                .header("x-xsrf-token",myApplication.getXsrf())
                .post(requestBody) // 使用post連線
                .build();

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
                // 連線成功，自response取得連線結果
                String result = response.body().string();
                Log.d("OkHttp result", result);
                Gson gson = new Gson();
                ResponseData responseData = gson.fromJson(result,  ResponseData.class);
                System.out.println("responseData"+responseData);
                Log.d("OkHttp -----getMessage", String.valueOf(responseData.isSuccess()));
                if(responseData.isSuccess()){
                    Intent intent = new Intent(  CheckoutActivity.this, PayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}