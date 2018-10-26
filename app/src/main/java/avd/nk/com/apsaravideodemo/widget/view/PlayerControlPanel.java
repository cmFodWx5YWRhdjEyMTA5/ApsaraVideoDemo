package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.PlayerView;

public class PlayerControlPanel extends RelativeLayout {
    private ProgressView volumeProgressView;
    private ProgressView brightnessProgressView;
    private TextView modeFit;
    private TextView modeCrop;
    private TextView speed100;
    private TextView speed125;
    private TextView speed150;
    private TextView speed200;

    private PlayerView.ControlPanelActionCallback controlPanelActionCallback;

    public PlayerControlPanel(Context context) {
        this(context, null);
    }

    public PlayerControlPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerControlPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_control_panel, this, true);

        modeFit = findViewById(R.id.modeFit);
        modeFit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                modeFit.setBackgroundResource(R.drawable.shape_play_mode_ft);
                modeCrop.setBackgroundResource(R.drawable.shape_play_mode_bg);
                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onPlayModeChanged(PlayerView.PlayMode.FIT);
                }
            }
        });

        modeCrop = findViewById(R.id.modeCrop);
        modeCrop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                modeFit.setBackgroundResource(R.drawable.shape_play_mode_bg);
                modeCrop.setBackgroundResource(R.drawable.shape_play_mode_ft);
                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onPlayModeChanged(PlayerView.PlayMode.CROP);
                }
            }
        });

        volumeProgressView = findViewById(R.id.volumeProgressView);
        volumeProgressView.setOnProgressChangedListener(new ProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float progress) {
                if (controlPanelActionCallback != null) {

                }
            }

            @Override
            public void onProgressChanging(float progress) {
                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onVolumeChanged(progress);
                }
            }
        });

        brightnessProgressView = findViewById(R.id.brightnessProgressView);
        brightnessProgressView.setOnProgressChangedListener(new ProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float progress) {
                if (controlPanelActionCallback != null) {

                }
            }

            @Override
            public void onProgressChanging(float progress) {
                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onBrightnessChanged(progress);
                }
            }
        });

        speed100 = findViewById(R.id.speed100);
        speed100.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                speed100.setBackgroundResource(R.drawable.shape_play_mode_ft);
                speed125.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed150.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed200.setBackgroundResource(R.drawable.shape_play_mode_bg);

                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onPlaySpeedChanged(PlayerView.PlaySpeed.SPEED_100);
                }
            }
        });

        speed125 = findViewById(R.id.speed125);
        speed125.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                speed125.setBackgroundResource(R.drawable.shape_play_mode_ft);
                speed100.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed150.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed200.setBackgroundResource(R.drawable.shape_play_mode_bg);

                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onPlaySpeedChanged(PlayerView.PlaySpeed.SPEED_125);
                }
            }
        });

        speed150 = findViewById(R.id.speed150);
        speed150.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                speed150.setBackgroundResource(R.drawable.shape_play_mode_ft);
                speed100.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed125.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed200.setBackgroundResource(R.drawable.shape_play_mode_bg);

                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onPlaySpeedChanged(PlayerView.PlaySpeed.SPEED_150);
                }
            }
        });

        speed200 = findViewById(R.id.speed200);
        speed200.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                speed200.setBackgroundResource(R.drawable.shape_play_mode_ft);
                speed100.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed125.setBackgroundResource(R.drawable.shape_play_mode_bg);
                speed150.setBackgroundResource(R.drawable.shape_play_mode_bg);

                if (controlPanelActionCallback != null) {
                    controlPanelActionCallback.onPlaySpeedChanged(PlayerView.PlaySpeed.SPEED_200);
                }
            }
        });
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    public void setCurrentVolume(float volume) {
        volumeProgressView.setProgress(volume);
    }

    public void setCurrentBrightness(float brightness) {
        brightnessProgressView.setProgress(brightness);
    }

    public void setControlPanelActionCallback(PlayerView.ControlPanelActionCallback controlPanelActionCallback) {
        this.controlPanelActionCallback = controlPanelActionCallback;
    }

    public void removeActionCallback() {
        if (controlPanelActionCallback != null) {
            controlPanelActionCallback = null;
        }
    }
}
