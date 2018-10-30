package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushBGMListener;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushError;
import com.alivc.live.pusher.AlivcLivePushErrorListener;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePushNetworkListener;
import com.alivc.live.pusher.AlivcLivePushStats;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;
import com.alivc.live.pusher.AlivcResolutionEnum;

import avd.nk.com.apsaravideodemo.R;

/**
 * Created by Nikou Karter.
 * <p>
 * BaseLiveVideoView is a base view for live video pushing view, it's designed to be containing only
 * a surface view, including some of the common actions such as {@link #preview()}, {@link #push(String)}
 * and so on, any other views can inherit it and implement its only layout to create a new liveVideoView.
 */
public class BaseLivePusherView extends ConstraintLayout {
    private static final String TAG = BaseLivePusherView.class.getSimpleName();
    protected SurfaceView surfaceView;
    protected AlivcLivePusher aliVCLivePusher;
    protected AlivcLivePushConfig aliVCLivePushConfig;

    public BaseLivePusherView(Context context) {
        this(context, null);
    }

    public BaseLivePusherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLivePusherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPushConfig();
        initPusher();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_base_live, this, true);
        initSurfaceView();
    }

    /**
     * init the surface view for pusher.
     */
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

    /**
     * init pusher.
     */
    private void initPusher() {
        aliVCLivePusher = new AlivcLivePusher();
        aliVCLivePusher.init(getContext(), aliVCLivePushConfig);

        aliVCLivePusher.setLivePushErrorListener(new AlivcLivePushErrorListener() {
            @Override
            public void onSystemError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
                alivcLivePusher.pause();
            }

            @Override
            public void onSDKError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
                alivcLivePusher.pause();
            }
        });

        aliVCLivePusher.setLivePushNetworkListener(new AlivcLivePushNetworkListener() {
            @Override
            public void onNetworkPoor(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onNetworkRecovery(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onReconnectStart(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onReconnectFail(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onReconnectSucceed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onSendDataTimeout(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onConnectFail(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public String onPushURLAuthenticationOverdue(AlivcLivePusher alivcLivePusher) {
                return null;
            }

            @Override
            public void onSendMessage(AlivcLivePusher alivcLivePusher) {

            }
        });

        aliVCLivePusher.setLivePushBGMListener(new AlivcLivePushBGMListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onStoped() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onResumed() {

            }

            @Override
            public void onProgress(long l, long l1) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onDownloadTimeout() {

            }

            @Override
            public void onOpenFailed() {

            }
        });
    }

    /**
     * init config profile for pusher.
     */
    private void initPushConfig() {
        aliVCLivePushConfig = new AlivcLivePushConfig();
        aliVCLivePushConfig.setResolution(AlivcResolutionEnum.RESOLUTION_540P);//set resolution.
        aliVCLivePushConfig.setFps(AlivcFpsEnum.FPS_20); //set frames per second.
        aliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT);
        aliVCLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LC);//set audio profile.
        aliVCLivePushConfig.setEnableBitrateControl(true);// set auto bitrate, default value is true.
        //aliVCLivePushConfig.setEnableAutoResolution(true);
        aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
        aliVCLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL);
    }

    /**
     * use to set video quality by user himself.
     *
     * @param targetBitrate set target bitrate.
     * @param minBitrate    set min bitrate.
     * @param initBitrate   default bitrate.
     */
    private void setCustomQualityMode(int targetBitrate, int minBitrate, int initBitrate) {
        if (aliVCLivePushConfig != null) {
            aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_CUSTOM);
            aliVCLivePushConfig.setTargetVideoBitrate(targetBitrate);
            aliVCLivePushConfig.setMinVideoBitrate(minBitrate);
            aliVCLivePushConfig.setInitialVideoBitrate(initBitrate);
        }
    }

    public void onResume() {
        aliVCLivePusher.resume();
    }

    public void onPause() {
        stopPreview();
        aliVCLivePusher.pause();
    }

    public void onDestroy() {
        /*if(aliVCLivePusher.isPushing()){
            aliVCLivePusher.stopPush();
        }
        aliVCLivePusher.stopPreview();*/
        aliVCLivePusher.destroy();
        aliVCLivePusher = null;
    }

    public void preview() {
        try {
            //aliVCLivePusher.startPreviewAysnc(surfaceView);
            aliVCLivePusher.startPreview(surfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        try {
            aliVCLivePusher.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push(String pushPath) {
        try {
            if (!aliVCLivePusher.isPushing()) {
                if (pushPath != null) {
                    //aliVCLivePusher.startPushAysnc(pushPath);
                    aliVCLivePusher.startPush(pushPath);
                } else {
                    Log.e(TAG, "push url is null, please check the path.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPush() {
        try {
            if (aliVCLivePusher.getCurrentStatus() == AlivcLivePushStats.PUSHED) {
                aliVCLivePusher.stopPush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
