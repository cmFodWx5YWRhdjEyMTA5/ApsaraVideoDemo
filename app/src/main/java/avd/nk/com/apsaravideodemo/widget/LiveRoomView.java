package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVidSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.listeners.player.OnCompletionListener;
import avd.nk.com.apsaravideodemo.widget.view.LiveRoomBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoTopPanel;
import avd.nk.com.apsaravideodemo.widget.view.MessageView;

/**
 * Created by Nikou Karter.
 */
public class LiveRoomView extends ConstraintLayout {
    private final String TAG = LiveRoomView.class.getSimpleName();
    private MessageView messageView;
    protected SurfaceView surfaceView;
    protected AliyunVodPlayer aliyunVodPlayer;
    private LiveRoomBottomPanel liveRoomBottomPanel;
    private LiveVideoTopPanel liveVideoTopPanel;
    private LiveRoomView.LiveRoomActionCallback callback;

    public LiveRoomView(Context context) {
        this(context, null);
    }

    public LiveRoomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveRoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPlayer();
    }

    private void initPlayer() {
        aliyunVodPlayer = new AliyunVodPlayer(getContext());
        aliyunVodPlayer.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        aliyunVodPlayer.setAutoPlay(true);

        aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {

            }
        });

        aliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override
            public void onError(final int i, int i1, String s) {
                Log.i(TAG, "on error : i=" + i + " | i1=" + i1 + " | s=" + s);
                LiveRoomView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (i){
                            case 4008:
                                Toast.makeText(getContext(), "error : video loading over time.", Toast.LENGTH_LONG).show();
                                liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                                break;
                        }
                        Toast.makeText(getContext(), "error : no stream from url, please exit the activity and come back later.", Toast.LENGTH_LONG).show();
                        liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                    }
                });
            }
        });

        aliyunVodPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                LiveRoomView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "live video complete.", Toast.LENGTH_LONG).show();
                        liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                    }
                });
            }
        });

        aliyunVodPlayer.setOnInfoListener(new IAliyunVodPlayer.OnInfoListener() {
            @Override
            public void onInfo(final int i, int i1) {
                Log.i(TAG, "on info : i=" + i + " | i1=" + i1);
                LiveRoomView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (i) {
                            case 101://start buffering.
                                Toast.makeText(getContext(), "start buffering stream.", Toast.LENGTH_SHORT).show();
                                break;
                            case 102://complete buffering.
                                Toast.makeText(getContext(), "complete buffering stream.", Toast.LENGTH_SHORT).show();
                                break;
                            case 3://first frame is previewed.
                                Toast.makeText(getContext(), "first frame loaded.", Toast.LENGTH_SHORT).show();
                                break;
                            case 105://error stream from server.
                                Toast.makeText(getContext(), "pusher maybe stopped, please exit the room.", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                    }
                });
            }
        });

        aliyunVodPlayer.setOnLoadingListener(new IAliyunVodPlayer.OnLoadingListener() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadEnd() {

            }

            @Override
            public void onLoadProgress(int i) {

            }
        });

        /*aliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {

            }
        });*/

        aliyunVodPlayer.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
            @Override
            public void onFirstFrameStart() {

            }
        });

        aliyunVodPlayer.setOnStoppedListner(new IAliyunVodPlayer.OnStoppedListener() {
            @Override
            public void onStopped() {

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

        aliyunVodPlayer.setOnCircleStartListener(new IAliyunVodPlayer.OnCircleStartListener() {
            @Override
            public void onCircleStart() {

            }
        });
    }

    private void initSurfaceView() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                aliyunVodPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                aliyunVodPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    public void playByUrl(String url) {
        AliyunLocalSource.AliyunLocalSourceBuilder builder = new AliyunLocalSource.AliyunLocalSourceBuilder();
        builder.setSource(url);
        AliyunLocalSource aliyunLocalSource = builder.build();
        aliyunVodPlayer.prepareAsync(aliyunLocalSource);
    }

    public void playByVidSts(String vid, String akid, String aks, String token) {
        AliyunVidSts aliyunVidSts = new AliyunVidSts();
        aliyunVidSts.setVid(vid);
        aliyunVidSts.setAcId(akid);
        aliyunVidSts.setAkSceret(aks);
        aliyunVidSts.setSecurityToken(token);

        aliyunVodPlayer.prepareAsync(aliyunVidSts);
    }

    public void playByVidPlayAuth(String vid, String authInfo) {
        AliyunPlayAuth.AliyunPlayAuthBuilder builder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        builder.setVid(vid);
        builder.setPlayAuth(authInfo);
        builder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_LOW);
        AliyunPlayAuth playAuth = builder.build();
        aliyunVodPlayer.prepareAsync(playAuth);
    }

    public void playByMPS(String vid, String authInfo, String token, String akid, String aks) {
        AliyunVidSource aliyunVidSource = new AliyunVidSource();
        aliyunVidSource.setVid(vid);
        aliyunVidSource.setAuthInfo(authInfo);
        aliyunVidSource.setAcKey(akid);
        aliyunVidSource.setAcKey(aks);
        aliyunVidSource.setStsToken(token);
        aliyunVidSource.setDomainRegion("cn-shanghai");

        aliyunVodPlayer.prepareAsync(aliyunVidSource);
    }

    private void initView() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.view_live_room, this, false),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        initSurfaceView();

        messageView = findViewById(R.id.messageView);

        liveRoomBottomPanel = findViewById(R.id.liveRoomBottomPanel);
        liveRoomBottomPanel.setLiveRoomBottomPanelActionCallback(new LiveRoomBottomPanel.LiveRoomBottomPanelActionCallback() {
            @Override
            public void onMessageClick(String msg) {
                messageView.newMessage(new Message("user live", msg));
            }

            @Override
            public void onStartPlayClick() {
                switch (aliyunVodPlayer.getPlayerState()) {
                    case Started:
                        aliyunVodPlayer.pause();
                        liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                        break;
                    case Paused:
                        aliyunVodPlayer.resume();
                        liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                        break;
                    case Completed:
                        aliyunVodPlayer.replay();
                        liveRoomBottomPanel.onStateChanged(aliyunVodPlayer.getPlayerState());
                        break;
                }
            }
        });

        liveVideoTopPanel = findViewById(R.id.liveVideoTopPanel);
        liveVideoTopPanel.setLiveVideoTopPanelActionCallback(new LiveVideoTopPanel.LiveVideoTopPanelActionCallback() {
            @Override
            public void onExitClick() {
                callback.onExitClick();
            }
        });
    }

    public void setLiveRoomActionCallback(LiveRoomActionCallback callback) {
        this.callback = callback;
    }

    public void onPause() {
        aliyunVodPlayer.pause();
    }

    public void onDestroy() {
        aliyunVodPlayer.release();
    }

    public interface LiveRoomActionCallback {
        void onExitClick();
    }
}
