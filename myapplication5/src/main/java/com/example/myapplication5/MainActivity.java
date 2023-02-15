package com.example.myapplication5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication5.database.UserDBHelper;
import com.example.myapplication5.enity.User;
import com.example.myapplication5.util.ToastUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private CheckBox cb_married;
    private TextView et_select;
    private SharedPreferences preferences;
    private UserDBHelper mHelper;

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
//    public  void  reload(View v){
//        String name = preferences.getString("name", null);
//        if(name!= null){
//            et_name.setText(name);
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = UserDBHelper.getInstance(this);
        mHelper.openWriteLink();
        mHelper.openReadLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
                super.onStop();
        mHelper.closeLink();
    }


    @Override
    public  void onClick(View v){
        String name =et_name.getText().toString();
        String age =et_age.getText().toString();
        String weight =et_weight.getText().toString();
        String height =et_height.getText().toString();
        User user;
        switch (v.getId()){
            case R.id.btn_insert:
                user = new User(
                        name,
                        Integer.parseInt(age),
                        Long.parseLong(weight),
                        Float.parseFloat(height),
                        cb_married.isChecked());
                if(mHelper.insert(user)>0){
                    ToastUtil.show(this,"okok");
                }
                break;
            case R.id.btn_delete:
                if(mHelper.deleteByName(name)>0){
                    ToastUtil.show(this,"delete ok");

                }
                break;
            case R.id.btn_update:
                user = new User(
                        name,
                        Integer.parseInt(age),
                        Long.parseLong(weight),
                        Float.parseFloat(height),
                        cb_married.isChecked());
                if(mHelper.updateByName(user)>0){
                    ToastUtil.show(this,"update ok");
                }
                break;
            case R.id.btn_select:
                List<User> users = mHelper.queryAll();
                if(users.size()>0){
                    ToastUtil.show(this,"select ok");
                }
                String s ="";
                for (User u : users){
                    s=s+","+u.name;
                }
                et_select.setText(s);
                break;
            case R.id.btn_selectByName:
                List<User> usersByName = mHelper.querByName(name);
                if(usersByName.size()>0){
                    ToastUtil.show(this,"select ok");
                }
                String sByName ="";
                for (User u : usersByName){
                    sByName=sByName+","+u.name;
                    Log.d("user",u.toString());
                }
//                et_select.setText(sByName);
                break;
        }
    }

}