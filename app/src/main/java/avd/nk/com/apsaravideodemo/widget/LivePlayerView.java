package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushBGMListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushErrorListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushNetworkListener;
import avd.nk.com.apsaravideodemo.widget.view.BaseLiveVideoView;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.MessageView;

public class LivePlayerView extends BaseLiveVideoView {
    private MessageView messageView;
    private LiveVideoBottomPanel liveVideoBottomPanel;

    private AlivcLivePusher aliVCLivePusher;
    private AlivcLivePushConfig aliVCLivePushConfig;

    public LivePlayerView(Context context) {
        this(context, null);
    }

    public LivePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        //initPushConfig();
        //initPusher();
    }

    /*private void initPusher() {
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
    }*/

    public void preview() {
        try {
            aliVCLivePusher.startPreview(surfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_live_push, this, false);
        ConstraintLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);

        messageView = findViewById(R.id.messageView);

        liveVideoBottomPanel = findViewById(R.id.liveVideoBottomPanel);
        liveVideoBottomPanel.setLiveVideoBottomPanelActionCallback(new LiveVideoBottomPanel.LiveVideoBottomPanelActionCallback() {
            @Override
            public void onMessageClick(String msg) {
                messageView.newMessage(new Message("user live", msg));
            }

            @Override
            public void onPushClick() {
                preview();
            }

            @Override
            public void onSwitchCamera() {
                aliVCLivePusher.switchCamera();
            }
        });
    }

}
