package com.example.musicapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class MediaButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                // 处理媒体按钮按下事件
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        // 执行播放操作
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        // 执行暂停操作
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        // 执行下一首操作
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        // 执行上一首操作
                        break;
                }
            }
        }
    }
}
