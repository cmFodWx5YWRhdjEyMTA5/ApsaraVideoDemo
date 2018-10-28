package avd.nk.com.apsaravideodemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import avd.nk.com.apsaravideodemo.entity.GlideApp;
import avd.nk.com.apsaravideodemo.widget.PlayerView;

/**
 * Created by Nikou Karter.
 *
 *
 *
 */
public class PlayActivity extends AppCompatActivity {
    private final String TAG = PlayActivity.class.getSimpleName();
    private final String VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private String playPath;
    private PlayerView playerView;
    private LocalBroadcastReceiver receiver;
    private boolean isFullScreen = false;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private OrientationListener callback = new OrientationListener() {
        @Override
        public void onOrientationChanged(boolean isFullScreen) {
            PlayerView.VideoOrientation orientation = playerView.getVideoOrientation();
            switch (orientation) {
                case PORTRAIT_VIDEO:
                    if (isFullScreen) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        playerView.fullScreen(false);
                    } else {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                        playerView.fullScreen(true);
                    }
                    break;
                case LANDSCAPE_VIDEO:
                    if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        PlayActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        PlayActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private OnPlayerBackPressListener backPressListener = new OnPlayerBackPressListener() {
        @Override
        public void onBackPress() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playPath = getIntent().getStringExtra("pullPath");
        if (playPath == null) {
            Log.e(TAG, "error! got an empty play path!");
        }
        initView();
        setLocalBroadcastReceiver();
    }

    private void setLocalBroadcastReceiver() {
        receiver = new LocalBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VOLUME_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            playerView.fullScreen(true);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            playerView.fullScreen(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        playerView.onResume();
    }

    @Override
    protected void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        playerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isFullScreen){

        }
    }

    private EditText inputUrl;

    private void initView() {
        playerView = findViewById(R.id.playView);
        playerView.setPlayerControlCallback(callback);
        playerView.setOnPlayerBackPressListener(backPressListener);
        playerView.setPullPath(playPath);

        inputUrl = findViewById(R.id.inputUrl);
        findViewById(R.id.applyUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = inputUrl.getText().toString();
                if(!url.equals("")){
                    playerView.setPullPath(url);
                }
            }
        });
    }

    public interface OrientationListener {
        void onOrientationChanged(boolean isFullScreen);
    }

    public interface OnPlayerBackPressListener {
        void onBackPress();
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {
        private AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int minVol, maxVol;

        LocalBroadcastReceiver() {
            minVol = am.getStreamMinVolume(AudioManager.STREAM_MUSIC);
            maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case VOLUME_CHANGED:
                        playerView.updateVolume(getCurrentVolume());
                        break;
                }
            }
        }

        private int getCurrentVolume() {
            int currentVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            return (int) (currentVol / (float) (maxVol - minVol) * 100);
        }
    }
}
