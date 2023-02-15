package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class MainActivity4_page1 extends AppCompatActivity implements View.OnClickListener {
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
//    TextView tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4_page1);
        Button dial= findViewById(R.id.button_dial);
        Button sendto= findViewById(R.id.button_sendto);
        dial.setOnClickListener(this);
        sendto.setOnClickListener(this);
        Button button1= findViewById(R.id.button1);
        button1.setOnClickListener(this);

        TextView tv_back= findViewById(R.id.tv_back);

        Button button2= findViewById(R.id.button2);
        button2.setOnClickListener(this);
///////////////////  寫法1
//        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        Intent intent =result.getData();
//                        Bundle bundle = intent.getExtras();
//                        String response = bundle.getString("response");
//                        tv_back.setText(response);
//                    }
//                }
//        );
/////////////////   寫法2
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent intent =result.getData();
            Bundle bundle = intent.getExtras();
            String response = bundle.getString("response");
            tv_back.setText(response);
        }

        );

    }

    @Override
    public void onClick(View view) {
        String phoneNo = "123";
        Intent intent = new Intent();

        switch  (view.getId()){
//            case R.id.button_dial:
//                intent.setAction(Intent.ACTION_DIAL);
//                Uri parse = Uri.parse("tel:" + phoneNo);
//                intent.setData(parse);
//                startActivity(intent);
//                break;
//            case R.id.button_sendto:
//                intent.setAction(Intent.ACTION_SENDTO);
//                Uri parse2 = Uri.parse("smsto:" + phoneNo);
//                intent.setData(parse2);
//                startActivity(intent);
//                break;
//            case R.id.button1:
//                intent.setAction("android.intent.action.NING");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivity(intent);
//                break;
            case R.id.button2:
                intent.setClass(this,MainActivity4_page2.class);
                Bundle bundle = new Bundle();
                bundle.putString("time", LocalDateTime.now().toString());
                intent.putExtra("AA","AA");
                intent.putExtras(bundle);
//         過時寫法  >>>>   startActivityForResult();
                intentActivityResultLauncher.launch(intent);
                break;
        }
    }
}