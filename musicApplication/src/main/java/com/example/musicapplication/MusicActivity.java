package com.example.musicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.util.MediaButtonReceiver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleTextView;
    private TextView total_time_text;
    private TextView current_time_text;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton previous_button;
    private ImageButton next_button;
    private ImageButton repeat_status;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private SeekBar seekBar;
    private int playI;
    private int counter = 0;

    private List<Integer> integers;


    private MediaSessionCompat mediaSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        integers = new ArrayList<>();
        titleTextView = findViewById(R.id.title_text);
        total_time_text = findViewById(R.id.total_time_text);
        current_time_text = findViewById(R.id.current_time_text);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        previous_button = findViewById(R.id.previous_button);
        next_button = findViewById(R.id.next_button);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        previous_button.setOnClickListener(this);
        next_button.setOnClickListener(this);

        repeat_status = findViewById(R.id.repeat_status);
        repeat_status.setOnClickListener(this);



        ComponentName componentName = new ComponentName(this, MediaButtonReceiver.class.getName());


        mediaSession = new MediaSessionCompat(this, "tag",componentName,null);

        // 设置 MediaSessionCompat 的回调对象
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
                                     @Override
                                     public void onPlay() {
                                         super.onPlay();
                                         Toast.makeText(MusicActivity.this, "onPlay()", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onPause() {
                                         super.onPause();
                                         Toast.makeText(MusicActivity.this, "onPause()", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onSkipToNext() {
                                         super.onSkipToNext();
                                         Toast.makeText(MusicActivity.this, "onSkipToNext()", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onSkipToPrevious() {
                                         super.onSkipToPrevious();
                                         Toast.makeText(MusicActivity.this, "onSkipToPrevious()", Toast.LENGTH_SHORT).show();
                                     }
                                 });



        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        // 激活 MediaSessionCompat
        mediaSession.setActive(true);


        PlaybackStateCompat state = new PlaybackStateCompat.Builder() .setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        ).build();

        mediaSession.setPlaybackState(state);


        seekBar = findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 如果是由用戶觸發的，則將播放進度設置為 SeekBar 的進度
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 開始拖動 SeekBar 時不執行任何操作
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖動 SeekBar 時不執行任何操作
            }
        });

        //取/res/raw資料夾下的音樂resId
        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                integers.add(fields[i].getInt(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (integers.size() > 0) {
            playI = 0;
            setMediaPlayer();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaSession.setCallback(null);
        mediaSession.setActive(false);
        mediaSession.release();
        mediaPlayer.release();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.playButton:
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                mediaPlayer.start();

                break;
            case R.id.pauseButton:
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                mediaPlayer.pause();
                break;
            case R.id.previous_button:
                playI--;
                if (playI >= 0) {
                    play();
                } else {
                    playI = 0;
                    Toast.makeText(this, "這是第一首", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.next_button:
                playI++;
                if (playI < integers.size()) {

                    play();
                } else {
                    playI = integers.size() - 1;
                    Toast.makeText(this, "這是最後一首", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.repeat_status:
                repeatStatus();
                break;
        }
    }

    public void repeatStatus() {
        counter++;
        switch (counter % 3) {
            case 0:
                repeat_status.setImageResource(R.drawable.repeat_all);
                mediaPlayer.setLooping(false);
                break;
            case 1:
                repeat_status.setImageResource(R.drawable.repeat_one);
                mediaPlayer.setLooping(true);
                break;
            case 2:
                repeat_status.setImageResource(R.drawable.repeat_all_off);
                mediaPlayer.setLooping(false);
                break;
        }
    }

    public void play() {
        setMediaPlayer();
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        mediaPlayer.start();

    }

    public void setMediaPlayer() {

        if (playI < 0) {
            playI = 0;
        }
        if (playI >= integers.size()) {
            playI = integers.size() - 1;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        // 使用 MediaPlayer 播放音樂
        int resId = integers.get(playI);
        mediaPlayer = MediaPlayer.create(this, resId);
        if (counter % 3 == 1) {
            mediaPlayer.setLooping(true);
        }
//        mediaPlayer.setLooping(true);
        seekBar.setMax(mediaPlayer.getDuration());

        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    current_time_text.setText(intToTime(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 500);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 音樂播放完畢
                // 在這裡可以執行下一首音樂的播放等操作
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                playI++;

                handler.removeCallbacksAndMessages(null);

                if (playI >= integers.size()) {
                    System.out.println("playI:  "+playI);
                    playI = 0;
                    if (counter % 3 == 0) {
                        System.out.println("counter:  "+counter);

                        play();
                    } else {
                        setMediaPlayer();
                    }
                }else {
                    play();

                }
            }
        });
        // 取得音樂檔案名稱
        titleTextView.setText(getResources().getResourceEntryName(resId));
        total_time_text.setText(intToTime(mediaPlayer.getDuration()));
    }


    public static String intToTime(long time) {
        int temp = (int) time / 1000;
        int hh = temp / 3600;
        int mm = (temp % 3600) / 60;
        int ss = (temp % 3600) % 60;
        return (hh < 10 ? ("0" + hh) : hh) + ":" +
                (mm < 10 ? ("0" + mm) : mm) + ":" +
                (ss < 10 ? ("0" + ss) : ss);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("1111111111onStart");
        // 激活 MediaSessionCompat
        mediaSession.setActive(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("22222222222onStop");

        // 停用 MediaSessionCompat
        mediaSession.setActive(false);
    }


}