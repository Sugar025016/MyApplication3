package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity5_packageManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5_package_manager);
        TextView viewById = findViewById(R.id.tv);
        PackageManager packageManager = getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Bundle bundle = activityInfo.metaData;
            String weather = bundle.getString("weather");
            viewById.setText(weather);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}