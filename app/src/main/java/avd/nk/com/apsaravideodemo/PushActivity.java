package avd.nk.com.apsaravideodemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;

import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushBGMListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushErrorListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushInfoListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushNetworkListener;

public class PushActivity extends AppCompatActivity {
    public static final int START_PREVIEW = 0x01;
    public static final int STOP_PREVIEW = 0x02;
    public static final int START_PUSH = 0x03;
    public static final int STOP_PUSH = 0x04;

    public static final int STATE_PUSH_STOP = 0x10;
    public static final int STATE_PREVIEW_STOP = 0x11;

    public static final int FINISH_ACTIVITY = 0xff;

    private static final String TAG = "PushActivity";
    private String pushPath;
    private boolean isFinish = false;
    private AlivcLivePusher mAliVCLivePusher;
    private AlivcLivePushConfig mAliVCLivePushConfig;
    private SurfaceView mPreviewArea;
    private OrientationEventListener orientationEventListener;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_PREVIEW:
                    preview();
                    break;
                case STOP_PREVIEW:
                    stopPreview();
                    break;
                case START_PUSH:
                    push();
                    break;
                case STOP_PUSH:
                    stopPush();
                    break;
                case STATE_PUSH_STOP:
                    if(isFinish){
                        stopPreview();
                    }
                    break;
                case STATE_PREVIEW_STOP:
                    if(isFinish){
                        finish();
                    }
                    break;
                case FINISH_ACTIVITY:
                    isFinish = true;
                    stopPush();
                    break;
                default:
                    break;
            }
        }
    };
    private LivePushInfoListener livePushInfoListener = new LivePushInfoListener(handler, true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        pushPath = getIntent().getStringExtra("pushPath");
        Log.i(TAG, "got intent extra string push path is : " + pushPath);
        if (pushPath == null) {
            Log.e(TAG, "error! got an empty push path!");
        }


        //initView();
        //initPushConfig();
        //initPusher();

        /*orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;
                }
                if (orientation > 350 || orientation < 10) {
                    //orientation = 0;

                    mAliVCLivePusher.setScreenOrientation(0);
                } else if (orientation > 80 && orientation < 100) {
                    //orientation = 90;
                    mAliVCLivePusher.setScreenOrientation(90);
                } else if (orientation > 170 && orientation < 190) {
                    //orientation = 180;
                    mAliVCLivePusher.setScreenOrientation(180);
                } else if (orientation > 260 && orientation < 280) {
                    //orientation = 270;
                    mAliVCLivePusher.setScreenOrientation(270);
                }
            }
        };*/
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
        if (mAliVCLivePusher != null) {
            try{
                mAliVCLivePusher.resume();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*if (orientationEventListener.canDetectOrientation()) {
            Log.v(TAG, "Can detect orientation");
            orientationEventListener.enable();
        } else {
            Log.v(TAG, "Cannot detect orientation");
            orientationEventListener.disable();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAliVCLivePusher != null) {
            try{
                mAliVCLivePusher.pause();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        /*if(orientationEventListener!=null){
            orientationEventListener.disable();
            orientationEventListener = null;
        }*/
        if(livePushInfoListener != null){
            livePushInfoListener.removeHandler();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (mAliVCLivePusher != null) {
            mAliVCLivePusher.destroy();
        }
        super.onDestroy();
    }

    private void initView() {
        /*mPreviewArea = findViewById(R.id.recView);
        mPreviewArea.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.pushBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push();
            }
        });

        findViewById(R.id.stopPushBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPush();
            }
        });

        findViewById(R.id.startPreviewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliVCLivePusher.startPreview(mPreviewArea);
            }
        });

        findViewById(R.id.stopPreviewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliVCLivePusher.stopPreview();
            }
        });

        findViewById(R.id.switchCameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliVCLivePusher.switchCamera();
            }
        });*/
    }

    private void initPushConfig() {
        mAliVCLivePushConfig = new AlivcLivePushConfig();
        //mAliVCLivePushConfig.setResolution(AlivcResolutionEnum.RESOLUTION_540P);//set resolution.
        mAliVCLivePushConfig.setFps(AlivcFpsEnum.FPS_20); //set frames per second.
        mAliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT);
        mAliVCLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LC);//set audio profile.
        mAliVCLivePushConfig.setEnableBitrateControl(true);// set auto bitrate, default value is true.
        mAliVCLivePushConfig.setEnableAutoResolution(true);
        mAliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
        mAliVCLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL);
    }

    private void setCustomQualityMode(int targetBitrate, int minBitrate, int initBitrate) {
        if (mAliVCLivePushConfig != null) {
            mAliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_CUSTOM);
            mAliVCLivePushConfig.setTargetVideoBitrate(targetBitrate);
            mAliVCLivePushConfig.setMinVideoBitrate(minBitrate);
            mAliVCLivePushConfig.setInitialVideoBitrate(initBitrate);
        }
    }

    private void setWaterMark(String markPath, float v1, float v2, float v3) {
        if (mAliVCLivePushConfig != null) {
            mAliVCLivePushConfig.addWaterMark(markPath, v1, v2, v3);
        }
    }

    private void setPauseImagePush(String imagePath) {
        if (mAliVCLivePushConfig != null) {
            mAliVCLivePushConfig.setPausePushImage(imagePath);
        }
    }

    private void initPusher() {
        mAliVCLivePusher = new AlivcLivePusher();
        mAliVCLivePusher.init(this, mAliVCLivePushConfig);

        mAliVCLivePusher.setLivePushErrorListener(new LivePushErrorListener());
        mAliVCLivePusher.setLivePushNetworkListener(new LivePushNetworkListener());
        mAliVCLivePusher.setLivePushBGMListener(new LivePushBGMListener());
        if (pushPath != null) {
            mAliVCLivePusher.setLivePushInfoListener(livePushInfoListener);
        }
    }

    private void preview(){
        try{
            mAliVCLivePusher.startPreview(mPreviewArea);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPreview(){
        try{
            mAliVCLivePusher.stopPreview();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void push() {
        try {
            if (!mAliVCLivePusher.isPushing()) {
                if (pushPath != null) {
                    mAliVCLivePusher.startPush(pushPath);
                    Toast.makeText(this, "try to push", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "push url is null, please check the path.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPush() {
        try {
            if (mAliVCLivePusher.isPushing()) {
                mAliVCLivePusher.stopPush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
