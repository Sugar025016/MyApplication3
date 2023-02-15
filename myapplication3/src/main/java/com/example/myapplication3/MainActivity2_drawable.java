package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity2_drawable extends AppCompatActivity implements View.OnClickListener {
    View viewById;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_drawable);
        viewById = findViewById(R.id.v_content);
        findViewById(R.id.btn_rect).setOnClickListener(this);
        findViewById(R.id.btn_oval).setOnClickListener(this);
    }
    @Override
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.btn_rect:
                viewById.setBackgroundResource(R.drawable.shape_oval_gold);
                break;
            case R.id.btn_oval:
                viewById.setBackgroundResource(R.drawable.shape_rect_gold);
                break;
        }
    }
}