package com.example.musicapplication.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

public class MediaButtonReceiver extends BroadcastReceiver {
    private MediaSessionCompat mediaSession;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("~~MediaButtonReceiver~~~~~~~~~~~~~~~");
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            System.out.println("~~MediaButtonReceiver~~~~~~~~~~~~~~~");
            if (mediaSession == null) {
                mediaSession = new MediaSessionCompat(context, "MediaButtonReceiver");
            }

            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                mediaSession.getController().dispatchMediaButtonEvent(keyEvent);
            }
        }
    }
}
