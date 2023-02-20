package com.example.doll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doll.entity.CartResponse;
import com.example.doll.entity.Carts;
import com.example.doll.entity.Product;
import com.example.doll.entity.ResponseData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShoppingCartActivity extends AppCompatActivity {

    private String header;
    private String urlCart ="http://192.168.43.87:8081/user/cart";
    private Carts cartsResponse;
    private LinearLayout ll_cart;
    private TextView tv_cart_total;
    private TextView tv_count;
    private TextView  tv_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        header = getIntent().getStringExtra("header");
        ll_cart = findViewById(R.id.ll_cart);
        tv_cart_total = findViewById(R.id.tv_total);
        tv_total_price = findViewById(R.id.tv_total_price);


        try {
            runCort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void runCort() throws IOException {

        OkHttpClient client = new OkHttpClient();
        System.out.println("--------header--------"+header);
        tv_count = findViewById(R.id.tv_count);

        Request request = new Request.Builder()
                .url(urlCart)
                .header("Cookie",header)
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

                ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OkHttp result", myResponse);
                        Gson gson = new Gson();
                        ResponseData responseData = new ResponseData<Carts>();
                        responseData = gson.fromJson(myResponse,  responseData.getClass());
                        System.out.println("///////////"+responseData.getData());
                        cartsResponse = gson.fromJson(responseData.getData().toString(), Carts.class);

                        System.out.println("/////cartsResponse//////"+cartsResponse.getTotal());

                        showGoods();

                    }
                });

            }
        });
    }

    final void showGoods() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth , LinearLayout.LayoutParams.WRAP_CONTENT);
        int sum=0;
        int qtySum=0;
        for (CartResponse cartResponse : cartsResponse.getCartResponseList()) {

            View view = LayoutInflater.from(ShoppingCartActivity.this).inflate(R.layout.item_cart, null);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_total = view.findViewById(R.id.tv_total);
            TextView tv_qty = view.findViewById(R.id.tv_qty);
            tv_qty.setText(String.valueOf(cartResponse.getQty()));
            tv_total.setText("$"+String.valueOf(cartResponse.getTotal()));
            sum=sum+cartResponse.getQty();
            qtySum=qtySum+cartResponse.getTotal();

            try {
                getGoods( cartResponse.getProductId(), view);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Button btn_delete =  view.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        deleteGoods(cartResponse.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            ll_cart.addView(view, layoutParams);
        }

        tv_count.setText(String.valueOf(sum));
        tv_total_price.setText(String.valueOf(qtySum));
//
//        tv_cart_total.setText(String.valueOf(qtySum));
    }


    void getGoods(int id ,View view) throws IOException {


        OkHttpClient client = new OkHttpClient();
        System.out.println("--------header--------"+header);

        Request request = new Request.Builder()
                .url("http://192.168.43.87:8081/api/product/"+id)
                .header("Cookie",header)
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

                ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OkHttp result", myResponse);
                        Gson gson = new Gson();
                        ResponseData responseData = new ResponseData<Carts>();
                        responseData = gson.fromJson(myResponse,  responseData.getClass());
                        if(responseData.isSuccess()){
                            System.out.println("///product///product/////"+responseData.getData());

                            String s = responseData.getData().toString();
                            System.out.println("///product///s/////"+s);
                            Product product  = gson.fromJson(s, Product.class);
                            ImageView iv_thumb = view.findViewById(R.id.iv_thumb);
                            TextView tv_price = view.findViewById(R.id.tv_price);
                            TextView tv_name = view.findViewById(R.id.tv_name);

                            tv_name.setText(product.getTitle());

                            if (product.getImageUrl() != null) {
                                String url = "http://192.168.43.87:8082/img/" + product.getImageUrl();

                                Glide.with(iv_thumb.getContext())
                                        .load(url)
                                        .into(iv_thumb);
                            }
                            tv_price.setText("$" + product.getPrice().toString());
                        }


                    }
                });

            }
        });
    }






    void deleteGoods(int id ) throws IOException {


        OkHttpClient client = new OkHttpClient();
        System.out.println("--------header--------"+header);

        Request request = new Request.Builder()
                .url("http://192.168.43.87:8081/user/cart/"+id)
                .delete()
                .header("Cookie",header)
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

                ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OkHttp result", myResponse);
                        Gson gson = new Gson();
                        ResponseData responseData = new ResponseData<Carts>();
                        responseData = gson.fromJson(myResponse,  responseData.getClass());
                        System.out.println("///////////"+responseData.getData());
//                        String s = responseData.getData().toString();
                        try {
                            ll_cart.removeAllViews();
                            runCort();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }


}