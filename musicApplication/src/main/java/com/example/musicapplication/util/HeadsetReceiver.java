package com.example.musicapplication.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeadsetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            if (state == 0) {
                // 耳機已拔出
            } else if (state == 1) {
                // 耳機已插入
            }
        }
    }
}
