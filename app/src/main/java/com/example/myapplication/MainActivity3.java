package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.utils.TimeUtil;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {
    private TextView time;
    private TextView listener_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        listener_text = findViewById(R.id.listener_text);
        Button listener_btn_a = findViewById(R.id.listener_btn_a);
        listener_btn_a.setOnClickListener(this);
        Button listener_btn_b = findViewById(R.id.listener_btn_b);
        listener_btn_b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listener_btn_a:
                String desc = String.format("%s |a-OOXX| %s", TimeUtil.getNowTime(), ((Button) v).getText());
                listener_text.setText(desc);
                break;
            case R.id.listener_btn_b:
                String desc2 = String.format("%s |b-OOXX| %s", TimeUtil.getNowTime(), ((Button) v).getText());
                listener_text.setText(desc2);
                break;
        }
    }
}