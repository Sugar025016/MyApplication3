package com.example.musicapplication;

import androidx.annotation.CheckResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.util.MusicPlayer;
import com.example.musicapplication.util.MyBroadcastReceiver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private MusicPlayer musicActivity;
    private Handler handler;
    private SeekBar seekBar;
    private int playI;
    private int counter = 0;
    private BluetoothHeadset mBluetoothHeadset;
    private BluetoothA2dp mBluetoothA2dp;
    private MediaButtonReceiver mediaButtonReceiver;
    private List<Integer> integers;
    private MyBroadcastReceiver mMyReceiver;

    private BluetoothGatt mBluetoothGatt;
    private static final int REQUEST_ENABLE_BT = 1;
    private AudioManager mAudioManager;
    private ComponentName mComponentName;
    @SuppressLint("MissingPermission")
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


//        AudioManager audiomanage = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);





        MediaSessionCompat mediaSession = new MediaSessionCompat(this, "MusicService");

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                System.out.println("1111111");
                KeyEvent keyEvent = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    System.out.println("2222222222222");
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                            // 播放音樂
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            // 暫停音樂
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            // 播放下一曲
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            // 播放上一曲
                            break;
                        default:
                            break;
                    }
                    return true;
                }
                return super.onMediaButtonEvent(mediaButtonIntent);
            }
        });
        mediaSession.setActive(true);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
//        filter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);
//        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
//        filter.addAction(BluetoothA2dp.EXTRA_PREVIOUS_STATE);
//        filter.addAction(BluetoothA2dp.EXTRA_STATE);
//        filter.addAction(Intent.ACTION_POWER_CONNECTED);
//        registerReceiver(mReceiver, filter);


// Connect to profile proxies
//        BluetoothAdapter.getDefaultAdapter().getProfileProxy(this, mProfileListener, BluetoothProfile.HEADSET);
//        BluetoothAdapter.getDefaultAdapter().getProfileProxy(this, mProfileListener, BluetoothProfile.A2DP);
//
//// Unregister receiver and disconnect profile proxies when done
////        unregisterReceiver(mGattUpdateReceiver);
//        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.HEADSET, mBluetoothHeadset);
//        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.A2DP, mBluetoothA2dp);
//
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            // 裝置不支援藍牙
//        } else {
//            if (!bluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//
//            }
//        }


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
        mediaPlayer.release();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.playButton:
                play();
                break;
//            case R.id.pauseButton:
//                playButton.setVisibility(View.VISIBLE);
//                pauseButton.setVisibility(View.GONE);
//                mediaPlayer.pause();
//                break;
            case R.id.previous_button:
                previous();

                break;
            case R.id.next_button:
                next();

                break;
            case R.id.repeat_status:
                repeatStatus();
                break;
        }


    }

    void play() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.play_selector);

        } else {
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.pause_selector);
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

    public void next() {
        playI++;
        if (playI < integers.size()) {

            nextOrPrevious();
        } else {
            playI = integers.size() - 1;
            Toast.makeText(this, "這是最後一首", Toast.LENGTH_SHORT).show();
        }

    }

    public void previous() {
        playI--;
        if (playI >= 0) {
            nextOrPrevious();
        } else {
            playI = 0;
            Toast.makeText(this, "這是第一首", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextOrPrevious() {
        setMediaPlayer();
//        playButton.setVisibility(View.GONE);
//        pauseButton.setVisibility(View.VISIBLE);
//        mediaPlayer.start();
        play();

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
                playButton.setImageResource(R.drawable.play_selector);
                playI++;
                handler.removeCallbacksAndMessages(null);

                if (playI >= integers.size()) {
                    System.out.println("playI:  " + playI);
                    playI = 0;
                    if (counter % 3 == 0) {
                        System.out.println("counter:  " + counter);

                        nextOrPrevious();
                    } else {
                        setMediaPlayer();
                    }
                } else {
                    nextOrPrevious();

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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("***/*///*/* ~~~~~~~~~");
            String action = intent.getAction();



            if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {
//
//                if (intent.getIntExtra("state", 0) == 1) {
//// do something
//// Log.d(TAG, "耳機檢測：插入");
//// Toast.makeText(context, "耳機檢測：插入", Toast.LENGTH_SHORT) .show();
//                    mAudioManager.registerMediaButtonEventReceiver(mComponentName);
//// phone head unplugged
//                } else {
//// do something
//// Log.d(TAG, "耳機檢測：没有插入");
//// Toast.makeText(context, "耳機檢測：没有插入", Toast.LENGTH_SHORT).show();
//                    mAudioManager.unregisterMediaButtonEventReceiver(mComponentName);
//                }


                KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            // 按下耳機上的下一首按鈕
                            // 處理下一首的相關操作
                            System.out.println("***/*///*/* EXTRA_KEY_EVENT");
                            next();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            // 按下耳機上的上一首按鈕
                            // 處理上一首的相關操作
                            previous();
                            break;
                    }
                }
            } else if (action.equals(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, -1);
                if (state == BluetoothHeadset.STATE_AUDIO_CONNECTED) {
                    // 藍牙耳機連接
                    // 處理相關操作
                } else if (state == BluetoothHeadset.STATE_AUDIO_DISCONNECTED) {
                    // 藍牙耳機斷開連接
                    // 處理相關操作
                }
            } else if (action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, -1);
                if (state == BluetoothHeadset.STATE_CONNECTED) {
                    // 藍牙耳機連接
                    // 處理相關操作
                } else if (state == BluetoothHeadset.STATE_DISCONNECTED) {
                    // 藍牙耳機斷開連接
                    // 處理相關操作
                }
            }
        }
    };

    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            System.out.println("***/*///*/* ");
            if (profile == BluetoothProfile.HEADSET) {
                System.out.println("***/*///*/*BluetoothProfile.HEADSET ");
                mBluetoothHeadset = (BluetoothHeadset) proxy;
            } else if (profile == BluetoothProfile.A2DP) {
                System.out.println("***/*///*/*BluetoothProfile.A2DP ");
                mBluetoothA2dp = (BluetoothA2dp) proxy;

//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    // 權限已經被授予，可以執行操作
//                } else {
//                    // 權限尚未被授予，需要請求權限
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH},
//                            MY_PERMISSIONS_REQUEST_BLUETOOTH);
//                }


            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null;
            } else if (profile == BluetoothProfile.A2DP) {
                mBluetoothA2dp = null;
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("~~~~/*///*/* 1"+context);
            System.out.println("~~~~/*///*/* 1"+intent.getAction());
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            KeyEvent keyEvent = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//            int keyEventKeyCode = keyEvent.getKeyCode();
//            int keyEventAction = keyEvent.getAction();
//            Set<String> strings = extras.keySet();
//            strings.stream().forEach(v->
//                    System.out.println("~~~~/*///*/* v: "+v));
//            System.out.println("~~~~/*///*/* 1"+intent.getExtras());
//            if(keyEvent.getKeyCode()!=0){
////                int keyCode = keyEvent.getKeyCode();
////                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
////// sb.append("KEYCODE_MEDIA_PLAY_PAUSE");
//////
//////                System.out.println("~~~~/*///*/* ??? KEYCODE_MEDIA_PLAY_PAUSE"+KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
////                }
//            }

            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {

//                System.out.println("~~~~/*///*/* 2 : :"+keyEvent.getAction());
                if (event != null) {
                    // 处理 KeyEvent
                    event.getAction();
                    System.out.println("~~~~/*///*/* 2 event.getAction():"+event.getAction());
                    System.out.println("~~~~/*///*/* 2 KeyEvent.ACTION_DOWN: "+KeyEvent.ACTION_DOWN);
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        // 處理按下事件
                        if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                            System.out.println("~~~~/*///*/* 播放或暫停");
                            // 播放或暫停
                        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT) {
                            System.out.println("~~~~/*///*/* 下一首");
                            // 下一首
                        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                            System.out.println("~~~~/*///*/* 上一首");
                            // 上一首
                        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_STOP) {
                            System.out.println("~~~~/*///*/* 停止播放");
                            // 停止播放
                        }
                    }
                }

            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        // 註冊mConnReceiver，並用IntentFilter設置接收的事件類型為網路開關
//        this.registerReceiver(mConnReceiver,
//                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        mAudioManager.registerMediaButtonEventReceiver(mComponentName); //註冊
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.AUDIO_BECOMING_NOISY");
        filter.addAction("android.media.MEDIA_NOFS");
        filter.addAction("android.intent.action.MEDIA_BUTTON");
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mReceiver,
                filter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // 解除註冊
        this.unregisterReceiver(mReceiver);
    }

    // 建立一個BroadcastReceiver，名為mConnReceiver
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("~~~~/*///*/* 1");
            // 當使用者開啟或關閉網路時會進入這邊

            // 判斷目前有無網路
            if(isNetworkAvailable()) {
                // 以連線至網路，做更新資料等事情
                Toast.makeText(MusicActivity.this, "有網路!", Toast.LENGTH_SHORT).show();
            }
            else {
                // 沒有網路
                Toast.makeText(MusicActivity.this, "沒網路!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 回傳目前是否已連線至網路
    public boolean isNetworkAvailable()
    {

        System.out.println("~~~~/*///*/* isNetworkAvailable");
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null &&
                networkInfo.isConnected();
    }

}