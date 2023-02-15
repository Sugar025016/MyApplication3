package com.example.myapplication4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.myapplication4.database.UserDBHelper;
import com.example.myapplication4.enity.User;
import com.example.myapplication4.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private CheckBox cb_married;
    private SharedPreferences preferences;
    private UserDBHelper userDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        cb_married = findViewById(R.id.cb_married);

        findViewById(R.id.btn_ok).setOnClickListener(this);
        preferences= getSharedPreferences("config", Context.MODE_PRIVATE);
    }
    public  void  reload(View v){
        String name = preferences.getString("name", null);
        if(name!= null){
            et_name.setText(name);
        }
    }

    @Override
    public  void onClick(View v){
        String name= et_name.getText().toString();
        String age= et_age.getText().toString();
        String height= et_height.getText().toString();
        String weight= et_weight.getText().toString();

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("name",name);
        edit.putInt("age", Integer.parseInt(age));
        edit.putFloat("height",Float.parseFloat(height));
        edit.putFloat("weight",Float.parseFloat(weight));
        edit.putBoolean("married",Boolean.parseBoolean(cb_married.toString()));
        edit.commit();
    }
}