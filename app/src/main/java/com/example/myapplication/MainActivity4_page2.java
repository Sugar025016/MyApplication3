package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity4_page2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4_page2);
        TextView tv=findViewById(R.id.tv);
        TextView tv2=findViewById(R.id.tv2);
        Bundle bundle = getIntent().getExtras();
        String time = bundle.getString("time");
        String AA = bundle.getString("AA");
        tv2.setText(AA);
        tv.setText(time);
        TextView button=findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent =new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("response","rrrrrrr");
        Log.d("12345","12345");
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK,intent);
        Log.d("page2 12345","page2 12345");
        finish();
    }
}