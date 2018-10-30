package avd.nk.com.apsaravideodemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import avd.nk.com.apsaravideodemo.widget.LivePusherView;

/**
 * Created by Nikou Karter.
 *
 *
 *
 */
public class PushActivity extends AppCompatActivity {
    //private static final String TAG = "PushActivity";
    private final int START_PREVIEW = 11;

    private LivePusherView livePusherView;

    private String pushPath;

    private boolean isPushing = false;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_PREVIEW:
                    livePusherView.preview();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        initView();
        pushPath = getPushPathFromIntent();
    }

    private String getPushPathFromIntent() {
        String pushPath = getIntent().getStringExtra("pushPath");
        //Log.i(TAG, "got intent extra string push path is : " + pushPath);

        if (pushPath == null) {
            //Log.e(TAG, "error! got an empty push path!");
        }

        return pushPath;
    }

    private void initView() {
        livePusherView = findViewById(R.id.livePlayerVew);
        livePusherView.setPusherViewActionCallback(new LivePusherView.PusherViewActionCallback() {
            @Override
            public void onExitClick() {
                /*livePusherView.stopPush();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        livePusherView.stopPreview();
                    }
                }, 1000);*/
                finish();
            }

            @Override
            public void onPushClick() {
                if(isPushing){
                    livePusherView.stopPush();
                    isPushing = false;
                }else {
                    livePusherView.push(pushPath);
                    isPushing = true;
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mAliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT);
            mAliVCLivePusher.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT);
        } else {
            mAliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(START_PREVIEW, 500);
    }

    @Override
    protected void onPause() {
        livePusherView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //livePusherView.clearCallback();
        livePusherView.onDestroy();
        //livePusherView = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
