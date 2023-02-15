package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity6_time extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    TextView tv_time;
    private TimePicker tp_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity6_time);
        tp_time = findViewById(R.id.tp_time);
        tp_time.setIs24HourView(true);
        tv_time = findViewById(R.id.tv_time);
        Button btn_time = findViewById(R.id.btn_time);
        btn_time.setOnClickListener(this);
        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }
    @Override
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.btn_ok:
                String desc ="AAAAAAAAAAAAAA:"+tp_time.getHour()+":"+tp_time.getMinute();
                tv_time.setText(desc);
                break;
           case R.id.btn_time:
               Calendar calendar = Calendar.getInstance();

               //新增時間視窗
               TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                       calendar.get(Calendar.HOUR_OF_DAY),
                       calendar.get(Calendar.MINUTE),
                       true);
               //開啟時間視窗
               timePickerDialog.show();
                break;
        }
    }
    public  void onTimeSet(TimePicker view,int hourOfDay,int minute){
        String desc ="BBBBBBBBB:"+hourOfDay+":"+minute;
        tv_time.setText(desc);
    }
}