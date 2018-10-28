package avd.nk.com.apsaravideodemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import avd.nk.com.apsaravideodemo.widget.LiveRoomView;

/**
 * Created by Nikou Karter.
 *
 *
 */
public class LiveRoomActivity extends AppCompatActivity {
    private static final String TGA = LiveRoomActivity.class.getSimpleName();
    private final int START_PLAY = 0x01;
    private String pullPath;

    private LiveRoomView liveRoomView;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_PLAY:
                    pullStream();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveroom);
        initView();
        getPullPathFromIntent();
    }

    private void getPullPathFromIntent() {
        pullPath = getIntent().getStringExtra("pullPath");
    }

    private void initView() {
        liveRoomView = findViewById(R.id.liveRoomView);
        liveRoomView.setLiveRoomActionCallback(new LiveRoomView.LiveRoomActionCallback() {
            @Override
            public void onExitClick() {
                finish();
            }
        });
    }

    private void pullStream() {
        if (pullPath != null) {
            liveRoomView.playByUrl(pullPath);
        } else {
            Toast.makeText(this, "pull path is null!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(START_PLAY, 500);
    }

    @Override
    protected void onPause() {
        liveRoomView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        liveRoomView.onDestroy();
        super.onDestroy();
    }
}
