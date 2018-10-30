package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVidSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.listeners.player.OnCompletionListener;

/**
 * Created by Nikou Karter.
 */
public class BaseLiveRoomView extends ConstraintLayout {
    private final String TAG = BaseLiveRoomView.class.getSimpleName();
    protected SurfaceView surfaceView;
    protected AliyunVodPlayer aliyunVodPlayer;

    public BaseLiveRoomView(Context context) {
        this(context, null);
    }

    public BaseLiveRoomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLiveRoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPlayer();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_base_live, this, true);
        initSurfaceView();
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
            public void onError(int i, int i1, String s) {
                Log.i(TAG, "on error : i=" + i + " | i1=" + i1 + " | s=" + s);
            }
        });

        aliyunVodPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {

            }
        });

        aliyunVodPlayer.setOnInfoListener(new IAliyunVodPlayer.OnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {
                Log.i(TAG, "on info : i=" + i + " | i1=" + i1);
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

        aliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {

            }
        });

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
}
