package com.example.musicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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


        seekBar = findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // ????????????????????????????????????????????????????????? SeekBar ?????????
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // ???????????? SeekBar ????????????????????????
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // ???????????? SeekBar ????????????????????????
            }
        });

        //???/res/raw?????????????????????resId
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
                    Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.next_button:
                playI++;
                if (playI < integers.size()) {

                    play();
                } else {
                    playI = integers.size() - 1;
                    Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
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
        // ?????? MediaPlayer ????????????
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
                // ??????????????????
                // ??????????????????????????????????????????????????????
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
        // ????????????????????????
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

}