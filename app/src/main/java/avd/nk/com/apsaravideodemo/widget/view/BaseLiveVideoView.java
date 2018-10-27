package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushBGMListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushErrorListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushNetworkListener;

public class BaseLiveVideoView extends ConstraintLayout {
    protected SurfaceView surfaceView;
    protected AlivcLivePusher aliVCLivePusher;
    protected AlivcLivePushConfig aliVCLivePushConfig;

    public BaseLiveVideoView(Context context) {
        this(context, null);
    }

    public BaseLiveVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLiveVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPushConfig();
        initPusher();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_base_live, this, true);

        initSurfaceView();
    }

    private void initSurfaceView() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    private void initPusher() {
        aliVCLivePusher = new AlivcLivePusher();
        aliVCLivePusher.init(getContext(), aliVCLivePushConfig);

        aliVCLivePusher.setLivePushErrorListener(new LivePushErrorListener());
        aliVCLivePusher.setLivePushNetworkListener(new LivePushNetworkListener());
        aliVCLivePusher.setLivePushBGMListener(new LivePushBGMListener());
        aliVCLivePusher.setLivePushInfoListener(new AlivcLivePushInfoListener() {
            @Override
            public void onPreviewStarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPreviewStoped(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushStarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushPauesed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushResumed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushStoped(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushRestarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onFirstFramePreviewed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onDropFrame(AlivcLivePusher alivcLivePusher, int i, int i1) {

            }

            @Override
            public void onAdjustBitRate(AlivcLivePusher alivcLivePusher, int i, int i1) {

            }

            @Override
            public void onAdjustFps(AlivcLivePusher alivcLivePusher, int i, int i1) {

            }
        });
    }

    private void initPushConfig() {
        aliVCLivePushConfig = new AlivcLivePushConfig();
        //mAliVCLivePushConfig.setResolution(AlivcResolutionEnum.RESOLUTION_540P);//set resolution.
        aliVCLivePushConfig.setFps(AlivcFpsEnum.FPS_20); //set frames per second.
        aliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT);
        aliVCLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LC);//set audio profile.
        aliVCLivePushConfig.setEnableBitrateControl(true);// set auto bitrate, default value is true.
        aliVCLivePushConfig.setEnableAutoResolution(true);
        aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
        aliVCLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL);
    }

    private void setCustomQualityMode(int targetBitrate, int minBitrate, int initBitrate) {
        if (aliVCLivePushConfig != null) {
            aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_CUSTOM);
            aliVCLivePushConfig.setTargetVideoBitrate(targetBitrate);
            aliVCLivePushConfig.setMinVideoBitrate(minBitrate);
            aliVCLivePushConfig.setInitialVideoBitrate(initBitrate);
        }
    }
}
