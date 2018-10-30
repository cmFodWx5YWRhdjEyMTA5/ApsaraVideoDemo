package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVidSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import java.lang.ref.WeakReference;

import avd.nk.com.apsaravideodemo.PlayActivity;
import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.listeners.player.OnCircleStartListener;
import avd.nk.com.apsaravideodemo.listeners.player.OnCompletionListener;
import avd.nk.com.apsaravideodemo.widget.view.PlayerBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.PlayerControlPanel;
import avd.nk.com.apsaravideodemo.widget.view.PlayerTopPanel;

/**
 * Created by Nikou Karter
 *
 *
 */
public class PlayerView extends RelativeLayout {
    private final String TAG = PlayerView.class.getSimpleName();

    private String pullPath;
    private SurfaceView playerView;
    private ImageView lockScreen;

    private PlayerTopPanel playerTopPanel;
    private PlayerBottomPanel playerBottomPanel;
    private PlayerControlPanel playerControlPanel;

    private AliyunVodPlayer aliyunVodPlayer;
    private PlayActivity.OrientationListener callback;
    private PlayActivity.OnPlayerBackPressListener backPressListener;

    private boolean isFullScreen = false;
    private boolean isScreenLock = false;
    private boolean isLiveVideo = false;
    private boolean isCtrlPanelShown = false;
    private boolean isTopBottomPanelShown = true;

    private int playerViewHeight;
    private int currentVideoWidth;
    private int currentVideoHeight;

    private BottomPanelActionCallback bottomPanelActionCallback = new BottomPanelActionCallback() {
        @Override
        public void onPlayClicked(ImageView view) {
            switch (getPlayerState()) {
                case Idle:
                    playByUrl(pullPath);
                    playerBottomPanel.onPlayStateChanged(getPlayerState());
                    break;
                case Started:
                    pausePlay();
                    break;
                case Paused:
                    resumePlay();
                    break;
                case Completed:
                    restartPlay();
                    break;
            }
        }

        @Override
        public void onFullScreenClicked(ImageView view) {
            if (!isFullScreen) {
                playerTopPanel.showOptionBtn();
                callback.onOrientationChanged(isFullScreen);
                isFullScreen = true;
            } else {
                playerTopPanel.hideOptionBtn();
                callback.onOrientationChanged(isFullScreen);
                isFullScreen = false;
                if(isCtrlPanelShown){
                    playerControlPanel.hide();
                }
            }
        }

        @Override
        public void onPlayProgressChanged(float progress) {
            if (!isLiveVideo) {
                aliyunVodPlayer.seekTo((int) (aliyunVodPlayer.getDuration() * (progress / 100f)));
            }
        }

        @Override
        public void onPlayProgressChanging(float progress) {
            stopProgressUpdateTimer();
        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    private TopPanelActionCallback topPanelActionCallback = new TopPanelActionCallback() {
        @Override
        public void onBackClicked() {
            if(isFullScreen){
                playerTopPanel.hideOptionBtn();
                callback.onOrientationChanged(isFullScreen);
                isFullScreen = false;
                playerBottomPanel.setFullScreen(isFullScreen);
                if(isCtrlPanelShown){
                    playerControlPanel.hide();
                }
            }else {
                backPressListener.onBackPress();
            }
        }

        @Override
        public void onOptionClicked(ImageView view) {
            if (isCtrlPanelShown) {
                playerControlPanel.hide();
                isCtrlPanelShown = false;
            } else {
                playerControlPanel.show();
                isCtrlPanelShown = true;
            }
        }
    };

    private ControlPanelActionCallback controlPanelActionCallback = new ControlPanelActionCallback() {
        @Override
        public void onVolumeChanged(float volume) {
            aliyunVodPlayer.setVolume((int) volume);
        }

        @Override
        public void onBrightnessChanged(float brightness) {
            aliyunVodPlayer.setScreenBrightness((int) brightness);
        }

        @Override
        public void onPlayModeChanged(PlayMode mode) {
            switch (mode) {
                case FIT:
                    aliyunVodPlayer.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    break;
                case CROP:
                    aliyunVodPlayer.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlaySpeedChanged(PlaySpeed speed) {
            switch (speed) {
                case SPEED_100:
                    aliyunVodPlayer.setPlaySpeed(1.0f);
                    break;
                case SPEED_125:
                    aliyunVodPlayer.setPlaySpeed(1.25f);
                    break;
                case SPEED_150:
                    aliyunVodPlayer.setPlaySpeed(1.5f);
                    break;
                case SPEED_200:
                    aliyunVodPlayer.setPlaySpeed(2.0f);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onDownloadClicked() {
            Toast.makeText(getContext(), "Download onClick", Toast.LENGTH_SHORT).show();
        }
    };

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPlayer();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_player, this, true);

        initSurfaceView();

        lockScreen = findViewById(R.id.lockScreen);
        lockScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScreenLock) {
                    showPanel();
                    isTopBottomPanelShown = true;
                } else {
                    hidePanel();
                    isTopBottomPanelShown = true;
                    playerControlPanel.hide();
                }
                isScreenLock = !isScreenLock;
            }
        });

        playerBottomPanel = findViewById(R.id.bottomPanel);
        playerBottomPanel.setBottomPanelActionCallback(bottomPanelActionCallback);

        playerTopPanel = findViewById(R.id.topPanel);
        playerTopPanel.setOnTopPanelClickedListener(topPanelActionCallback);

        playerControlPanel = findViewById(R.id.controlPanel);
        playerControlPanel.setControlPanelActionCallback(controlPanelActionCallback);

        adjustPlayerViewHeight(getScreenHeight(ScreenRatio.MODE_9_16));

        autoHidePanel();
    }

    private void autoHidePanel(){
        //getHandler().sendEmptyMessageDelayed(1, 5000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isTopBottomPanelShown){
                    hidePanel();
                    if(isCtrlPanelShown){
                        playerControlPanel.hide();
                    }
                    isTopBottomPanelShown = false;
                }else {
                    showPanel();
                    isTopBottomPanelShown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void initSurfaceView() {
        playerView = findViewById(R.id.playerView);
        playerView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private int getScreenHeight(ScreenRatio ratio) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        switch (ratio) {
            case MODE_10_16:
                playerViewHeight = (int) (dm.widthPixels * (10f / 16f));
                break;
            case MODE_9_16:
                playerViewHeight = (int) (dm.widthPixels * (9f / 16f));
                break;
            default:
                playerViewHeight = (int) (dm.widthPixels * (9f / 16f));
                break;
        }
        return playerViewHeight;
    }

    private void adjustPlayerViewHeight(int height) {
        ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        playerView.setLayoutParams(layoutParams);
    }

    private void initPlayer() {
        aliyunVodPlayer = new AliyunVodPlayer(getContext());
        aliyunVodPlayer.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        aliyunVodPlayer.setAutoPlay(true);

        aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                currentVideoWidth = aliyunVodPlayer.getVideoWidth();
                currentVideoHeight = aliyunVodPlayer.getVideoHeight();

                playerBottomPanel.setVideoDuration(aliyunVodPlayer.getDuration());
                playerTopPanel.setSubjectTitle(aliyunVodPlayer.getMediaInfo().getTitle());
            }
        });

        aliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override
            public void onError(int i, int i1, String s) {
                //stopProgressUpdateTimer();
            }
        });

        aliyunVodPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                playerBottomPanel.onPlayStateChanged(getPlayerState());
                stopProgressUpdateTimer();
            }
        });

        aliyunVodPlayer.setOnInfoListener(new IAliyunVodPlayer.OnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {

            }
        });

        aliyunVodPlayer.setOnLoadingListener(new IAliyunVodPlayer.OnLoadingListener() {
            @Override
            public void onLoadStart() {
                Log.i(TAG, "onLoadingStart");
            }

            @Override
            public void onLoadEnd() {
                Log.i(TAG, "onLoadEnd");
            }

            @Override
            public void onLoadProgress(int i) {
                Log.i(TAG, "onLoadProgress = " + 1);
            }
        });

        aliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                startProgressUpdateTimer();
            }
        });

        aliyunVodPlayer.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
            @Override
            public void onFirstFrameStart() {
                startProgressUpdateTimer();
                playerBottomPanel.onPlayStateChanged(getPlayerState());
            }
        });

        aliyunVodPlayer.setOnStoppedListner(new IAliyunVodPlayer.OnStoppedListener() {
            @Override
            public void onStopped() {
                stopProgressUpdateTimer();
            }
        });

        aliyunVodPlayer.setOnChangeQualityListener(new IAliyunVodPlayer.OnChangeQualityListener() {
            @Override
            public void onChangeQualitySuccess(String s) {

            }

            @Override
            public void onChangeQualityFail(int i, String s) {

            }
        });

        aliyunVodPlayer.setOnBufferingUpdateListener(new IAliyunVodPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(int i) {
                Log.i(TAG, "onBufferingUpdate i = " + i);
            }
        });

        aliyunVodPlayer.setOnCircleStartListener(new OnCircleStartListener());

        playerControlPanel.setCurrentVolume(aliyunVodPlayer.getVolume());
        playerControlPanel.setCurrentBrightness(aliyunVodPlayer.getScreenBrightness());
    }

    public void playByUrl(String url) {
        AliyunLocalSource.AliyunLocalSourceBuilder builder = new AliyunLocalSource.AliyunLocalSourceBuilder();
        builder.setSource(url);
        AliyunLocalSource aliyunLocalSource = builder.build();
        aliyunVodPlayer.prepareAsync(aliyunLocalSource);
    }

    private void playByVidSts(String vid, String akid, String aks, String token) {
        AliyunVidSts aliyunVidSts = new AliyunVidSts();
        aliyunVidSts.setVid(vid);
        aliyunVidSts.setAcId(akid);
        aliyunVidSts.setAkSceret(aks);
        aliyunVidSts.setSecurityToken(token);

        aliyunVodPlayer.prepareAsync(aliyunVidSts);
    }

    private void playByVidPlayAuth(String vid, String authInfo) {
        AliyunPlayAuth.AliyunPlayAuthBuilder builder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        builder.setVid(vid);
        builder.setPlayAuth(authInfo);
        builder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_LOW);
        AliyunPlayAuth playAuth = builder.build();
        aliyunVodPlayer.prepareAsync(playAuth);
    }

    private void playByMPS(String vid, String authInfo, String token, String akid, String aks) {
        AliyunVidSource aliyunVidSource = new AliyunVidSource();
        aliyunVidSource.setVid(vid);
        aliyunVidSource.setAuthInfo(authInfo);
        aliyunVidSource.setAcKey(akid);
        aliyunVidSource.setAcKey(aks);
        aliyunVidSource.setStsToken(token);
        aliyunVidSource.setDomainRegion("cn-shanghai");

        aliyunVodPlayer.prepareAsync(aliyunVidSource);
    }

    public void resumePlay() {
        aliyunVodPlayer.start();
        playerBottomPanel.onPlayStateChanged(getPlayerState());
    }

    private void pausePlay() {
        aliyunVodPlayer.pause();
        playerBottomPanel.onPlayStateChanged(getPlayerState());
    }

    private void restartPlay() {
        aliyunVodPlayer.replay();
        playerBottomPanel.onPlayStateChanged(getPlayerState());
    }

    public VideoOrientation getVideoOrientation() {
        return currentVideoHeight > currentVideoWidth ? VideoOrientation.PORTRAIT_VIDEO : VideoOrientation.LANDSCAPE_VIDEO;
    }

    public IAliyunVodPlayer.PlayerState getPlayerState() {
        return aliyunVodPlayer.getPlayerState();
    }

    public void onResume() {
        aliyunVodPlayer.resume();
    }

    public void onPause() {
        aliyunVodPlayer.pause();
    }

    public void onDestroy() {
        stopProgressUpdateTimer();
        progressUpdateTimer = null;
        playerControlPanel.removeActionCallback();
        playerTopPanel.removeActionCallback();
        playerBottomPanel.removeActionCallback();
        aliyunVodPlayer.release();
    }

    public void showPanel() {
        playerTopPanel.show();
        playerBottomPanel.show();
    }

    public void hidePanel() {
        playerTopPanel.hide();
        playerBottomPanel.hide();
    }

    public void fullScreen(boolean isFullScreen) {
        ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
        if (isFullScreen) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(layoutParams);
            Toast.makeText(getContext(), "full screen", Toast.LENGTH_SHORT).show();
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = playerViewHeight;
            playerView.setLayoutParams(layoutParams);
        }
    }

    public void setPlayerControlCallback(PlayActivity.OrientationListener callback) {
        this.callback = callback;
    }

    public void setLiveVideo(boolean isLiveVideo) {
        this.isLiveVideo = isLiveVideo;
    }

    public void setPullPath(String pullPath) {
        this.pullPath = pullPath;
        playByUrl(pullPath);
    }

    public void updateVolume(int streamVolume) {
        //Log.i(TAG, "streamVolume = " + streamVolume);
        //Log.i(TAG, "aliYunVolume = " + aliyunVodPlayer.getVolume());
        playerControlPanel.setCurrentVolume(streamVolume);
    }

    public void updateBrightness() {
        playerControlPanel.setCurrentBrightness(aliyunVodPlayer.getScreenBrightness());
    }

    private ProgressUpdateTimer progressUpdateTimer = new ProgressUpdateTimer(this);

    public void setOnPlayerBackPressListener(PlayActivity.OnPlayerBackPressListener backPressListener) {
        this.backPressListener = backPressListener;
    }

    private static class ProgressUpdateTimer extends Handler {

        private WeakReference<PlayerView> viewWeakReference;

        ProgressUpdateTimer(PlayerView playerView) {
            viewWeakReference = new WeakReference<>(playerView);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayerView playerView = viewWeakReference.get();
            if (playerView != null) {
                playerView.handleProgressUpdateMessage(msg);
            }
            super.handleMessage(msg);
        }
    }

    private void handleProgressUpdateMessage(Message msg) {
        if (aliyunVodPlayer != null /*&& !inSeek*/) {
            //.setVideoBufferPosition(aliyunVodPlayer.getBufferingPosition());
            playerBottomPanel.updateCurrentDuration(aliyunVodPlayer.getCurrentPosition());
        }
        startProgressUpdateTimer();
    }

    private void startProgressUpdateTimer() {
        if (progressUpdateTimer != null) {
            progressUpdateTimer.removeMessages(0);
            progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
        }
    }

    private void stopProgressUpdateTimer() {
        if (progressUpdateTimer != null) {
            progressUpdateTimer.removeMessages(0);
        }
    }

    public interface BottomPanelActionCallback {
        void onPlayClicked(ImageView view);

        void onFullScreenClicked(ImageView view);

        void onPlayProgressChanged(float progress);

        void onPlayProgressChanging(float progress);

        void onVolumeChanged(int volume);
    }

    public interface ControlPanelActionCallback {
        void onVolumeChanged(float volume);

        void onBrightnessChanged(float brightness);

        void onPlayModeChanged(PlayMode mode);

        void onPlaySpeedChanged(PlaySpeed speed);

        void onDownloadClicked();
    }

    public interface TopPanelActionCallback {
        void onBackClicked();

        void onOptionClicked(ImageView view);
    }

    public enum ScreenRatio {
        MODE_9_16, MODE_10_16
    }

    public enum VideoOrientation {
        PORTRAIT_VIDEO, LANDSCAPE_VIDEO
    }

    public enum PlayMode {
        FIT, CROP
    }

    public enum PlaySpeed {
        SPEED_100, SPEED_125, SPEED_150, SPEED_200
    }
}
