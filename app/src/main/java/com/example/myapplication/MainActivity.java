package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.utils.TimeUtil;
import com.example.myapplication.utils.Util;

public class MainActivity extends AppCompatActivity {
    private TextView time;
    private TextView listener_text;
    private TextView button_icon;
    Drawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv=findViewById(R.id.tv);
        tv.setText("123");
        tv.setTextColor(0xffff0000);
        ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();
        layoutParams.width = Util.dip2px(this,300);
        time=findViewById(R.id.time);

        Button button=findViewById(R.id.button);
        button.setEnabled(true);

        listener_text=findViewById(R.id.listener_text);
        Button listener_btn=findViewById(R.id.listener_btn);
        listener_btn.setOnClickListener(new MyOnClickListener(listener_text));




        button_icon = findViewById(R.id.button_icon);
        icon = getResources().getDrawable(R.drawable.images1);
        icon.setBounds(0, 0, 80, 80);
        button_icon.setCompoundDrawables(icon, null, null, null);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intrnt = new Intent();
                intrnt.setClass(MainActivity.this,MainActivity2.class);
                startActivity(intrnt);
            }
        });
    }


    static class MyOnClickListener implements View.OnClickListener{

        private final TextView listener_text;

        public MyOnClickListener(TextView listener_text) {
            this.listener_text=listener_text;
        }

        @Override
        public void onClick(View v){
            String desc =String.format("%s |OOXX| %s",TimeUtil.getNowTime(),((Button)v).getText());
            listener_text.setText(desc);
        }
    }











    public void doClick(View view){

        time.setText(String.format("%s |OOXX| %s",TimeUtil.getNowTime(),((Button)view).getText()));
    }
}