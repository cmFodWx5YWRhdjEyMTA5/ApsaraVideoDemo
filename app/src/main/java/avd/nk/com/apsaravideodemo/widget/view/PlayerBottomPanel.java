package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.PlayerView;

public class PlayerBottomPanel extends LinearLayout {
    private final String TAG = PlayerBottomPanel.class.getSimpleName();
    private long duration;
    private ImageView playBtn;
    private ImageView fullScreenBtn;
    private TextView currentDuration;
    private TextView videoDuration;
    private ProgressView progressView;

    private PlayerView.BottomPanelActionCallback actionCallback;

    private boolean isFullScreen = false;

    public PlayerBottomPanel(Context context) {
        this(context, null);
    }

    public PlayerBottomPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerBottomPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        setBackgroundColor(Color.TRANSPARENT);
        setGravity(Gravity.START);
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_panel, this, true);

        playBtn = findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCallback.onPlayClicked((ImageView) v);
            }
        });

        currentDuration = findViewById(R.id.currentDuration);
        videoDuration = findViewById(R.id.videoDuration);

        fullScreenBtn = findViewById(R.id.fullScreenBtn);
        fullScreenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFullScreen) {
                    fullScreenBtn.setImageResource(R.drawable.ic_resscrn);
                    isFullScreen = true;
                } else {
                    fullScreenBtn.setImageResource(R.drawable.ic_fullscrn);
                    isFullScreen = false;
                }
                actionCallback.onFullScreenClicked((ImageView) v);
            }
        });

        progressView = findViewById(R.id.progressView);
        progressView.setOnProgressChangedListener(new ProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float progress) {
                actionCallback.onPlayProgressChanged(progress);
            }

            @Override
            public void onProgressChanging(float progress) {
                actionCallback.onPlayProgressChanging(progress);
            }
        });
    }

    public void setBottomPanelActionCallback(PlayerView.BottomPanelActionCallback callback) {
        this.actionCallback = callback;
    }

    public void onPlayStateChanged(IAliyunVodPlayer.PlayerState state) {
        switch (state) {
            case Idle:
            case Started:
                playBtn.setImageResource(R.drawable.ic_stop);
                break;
            case Paused:
                playBtn.setImageResource(R.drawable.ic_play);
                break;
            case Replay:
                playBtn.setImageResource(R.drawable.ic_stop);
                break;
            case Stopped:
                break;
            case Prepared:
                break;
            case Completed:
                playBtn.setImageResource(R.drawable.ic_replay);
                progressView.setProgress(100f);
                break;
            default:
                break;
        }
    }

    public void setVideoDuration(long duration) {
        this.duration = duration;
        videoDuration.setText(videoDurationToString(duration));
    }

    public void updateCurrentDuration(long currentDuration) {
        this.currentDuration.setText(videoDurationToString(currentDuration));
        float progress = (float) currentDuration / duration * 100;
        progressView.setProgress(progress);
        //Log.i(TAG, "totalDuration:" + duration + " | currentDuration:" + currentDuration + " | progress:" + progress);
    }

    public void setFullScreen(boolean isFullScreen){
        if (!isFullScreen) {
            fullScreenBtn.setImageResource(R.drawable.ic_fullscrn);
            this.isFullScreen = isFullScreen;
        } else {
            fullScreenBtn.setImageResource(R.drawable.ic_resscrn);
            this.isFullScreen = isFullScreen;
        }
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    private String videoDurationToString(Long videoDuration) {
        Long diffTime = videoDuration / 1000L;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 2; i > 0; i--) {
            Long modTime = diffTime % 60;
            if (modTime < 10) {
                stringBuilder.insert(0, "0" + modTime + (i % 2 == 1 ? ":" : ""));
            } else {
                stringBuilder.insert(0, modTime + (i % 2 == 1 ? ":" : ""));
            }
            diffTime /= 60;
        }
        if (diffTime > 0) {
            stringBuilder.insert(0, diffTime + ":");
        }
        return stringBuilder.toString();
    }

    public void removeActionCallback() {
        if(actionCallback != null){
            actionCallback = null;
        }
    }
}
