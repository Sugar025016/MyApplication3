package com.example.doll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doll.entity.Carts;
import com.example.doll.entity.Page;
import com.example.doll.entity.Product;
import com.example.doll.entity.ResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShoppingActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    private List<Product> products;
    private GridLayout gridLayout;
    private TextView tv_count;
    private String url ="http://192.168.43.87:8081/api/products";
    private String urlCart ="http://192.168.43.87:8081/user/cart";
    private ImageView iv_cart;
    private static MyApplication myApplication ;
    private ResponseData<List<Product>> responseData;
//    private static MyApplication myApp = (MyApplication) getApplicationContext();

    @Override
    public void onResume() {
        super.onResume();
        // 调用 API 获取最新数据
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        myApplication= (MyApplication) getApplicationContext();
        gridLayout = findViewById(R.id.gl_channel);
        ImageView iv_cart = (ImageView) findViewById(R.id.iv_cart);
        iv_cart.setClickable(true);
        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ShoppingActivity.this,
//                        "The favorite list would appear on clicking this icon",
//                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ShoppingActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        tv_count = findViewById(R.id.tv_count);

        try {
            runCort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void runCort() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlCart)
                .header("Cookie",myApplication.getCookie())
                .build();
        System.out.println("--------Cookie--------"+request.headers("Cookie"));

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                ShoppingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OkHttp result", myResponse);
                        Gson gson = new Gson();
                        ResponseData responseData = new ResponseData<Carts>();
                         responseData = gson.fromJson(myResponse,  responseData.getClass());
                        System.out.println("///////////"+responseData.getData());
                        Carts cartsResponse = new Carts();
                        if(responseData.getData()!=null){

                            cartsResponse = gson.fromJson(responseData.getData().toString(), Carts.class);
                            System.out.println("/////cartsResponse//////"+cartsResponse.getTotal());
                            int sum = cartsResponse.getCarts().stream().mapToInt(v -> v.getQty()).sum();
                            tv_count.setText(String.valueOf(sum));
                        }
//                        if(cartsResponse.getTotal()!=null){

                    }
                });
            }
        });
    }


    void run() throws IOException {
          client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie",myApplication.getCookie())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                ShoppingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                Log.d("OkHttp result", myResponse);
                                        Gson gson = new Gson();
                        Type type = new TypeToken<ResponseData<Page<List<Product>>>>() {}.getType();
                        responseData = gson.fromJson(myResponse, type);
                        List<Product> productList=new ArrayList<>();
                        System.out.println("-------responseData.getData:"+responseData.getData());
                        System.out.println("-------((Page<List<Product>>)responseData.getData()).getContent():"+((Page<List<Product>>)responseData.getData()).getPageSet().getCurrentPage());
                        products= (List<Product>) ((Page<List<Product>>)responseData.getData()).getContent();
//                        System.out.println("-------productList:"+productList.get(0).getImageUrl());
                    if(responseData.isSuccess()){
                        showGoods();
                    }
//                        txtString.setText(myResponse);

                    }
                });
            }
        });
    }

    final void showGoods() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (Product product : products) {
            View view = LayoutInflater.from(ShoppingActivity.this).inflate(R.layout.item_goods, null);
            ImageView iv_thumb = view.findViewById(R.id.iv_thumb);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_price = view.findViewById(R.id.tv_price);
            TextView btn_add = view.findViewById(R.id.btn_add);
            if(product.getImageUrl()!=null){
                String url =  product.getImageUrl().replace("localhost","192.168.43.87");

                Glide.with(iv_thumb.getContext())
                        .load(url)
                        .into(iv_thumb);
            }
            tv_price.setText("$"+product.getPrice().toString());
            tv_name.setText(product.getTitle());
            btn_add.setOnClickListener(v->{
                addToCart(product.getId());
            });

            gridLayout.addView(view, layoutParams);
        }
//
    }
    private void addToCart(int goodsId){

        System.out.println("goodsId"+goodsId);

        String json = "{\"product_id\":"+goodsId+",\"qty\":1,\"one\":true}";
        System.out.println(json);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        String postUrl= "http://192.168.43.87:8081/user/cart";
        // 建立Request，設置連線資訊
        Request request = new Request.Builder()
                .url(postUrl)
                .header("Cookie",myApplication.getCookie())
                .header("x-xsrf-token",myApplication.getXsrf())
                .post(requestBody) // 使用post連線
                .build();
        System.out.println("--------Cookie--------"+request.headers("x-xsrf-token"));
        System.out.println("--------Cookie--------"+request.headers("Cookie"));

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
//                        new Intent(this,ShoppingActivity.class)‘
                Gson gson = new Gson();
                ResponseData responseData = gson.fromJson(result,  ResponseData.class);
                Log.d("OkHttp -----getMessage", String.valueOf(responseData.isSuccess()));
                if(responseData.isSuccess()){
                    try {
                        runCort();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

//                if(responseData.isSuccess()){
//
//                }else {
//                    Looper.prepare();
//                    Toast.makeText(ShoppingActivity.this, "錯誤", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }


            }


        });

    }

    public  static Bitmap getBitmapFromURL(String src){

        try {
            URL  url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



}