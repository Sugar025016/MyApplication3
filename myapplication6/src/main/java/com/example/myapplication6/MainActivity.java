package com.example.myapplication6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private CheckBox cb_married;
    private TextView et_select;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        cb_married = findViewById(R.id.cb_married);
        et_select = findViewById(R.id.et_select);

        findViewById(R.id.btn_insert).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_select).setOnClickListener(this);
        findViewById(R.id.btn_selectByName).setOnClickListener(this);
        preferences= getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        String name =et_name.getText().toString();
        String age =et_age.getText().toString();
        String weight =et_weight.getText().toString();
        String height =et_height.getText().toString();
        switch (v.getId()) {
            case R.id.btn_insert:
                break;
        }
    }
}