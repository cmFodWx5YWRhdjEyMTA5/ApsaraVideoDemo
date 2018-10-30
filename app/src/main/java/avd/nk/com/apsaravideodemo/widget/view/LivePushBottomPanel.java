package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcLivePushStats;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.dialog.UniversalDialog;

/**
 * Created by Nikou Karter.
 * <p>
 * LiveVideoBottomPanel is a custom view for {@link avd.nk.com.apsaravideodemo.widget.LivePusherView}.
 * It's use to display the action buttons for user, including functions such as sending messages, start
 * or stop pushing, switch camera and so on(audience side only display sending message button).
 * {@link #messageBtn} show sending message dialog.
 * {@link #startBtn} use to start or stop pushing stream.
 * {@link #switchCamera} use to switch the camera of anchor's display.
 */
public class LivePushBottomPanel extends ConstraintLayout {
    private ImageView messageBtn;
    private ImageView startBtn;
    private ImageView switchCamera;
    private UniversalDialog sendMessageDialog;//send message dialog.
    private LiveVideoBottomPanelActionCallback callback;

    public LivePushBottomPanel(Context context) {
        this(context, null);
    }

    public LivePushBottomPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePushBottomPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_push_bottom_panel, this, true);
        initDialog();

        messageBtn = findViewById(R.id.messageBtn);
        messageBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageDialog.show();
            }
        });

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPushClick();
            }
        });

        switchCamera = findViewById(R.id.switchCamera);
        switchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSwitchCamera();
            }
        });
    }

    private void initDialog() {
        sendMessageDialog = new UniversalDialog.Builder(getContext())
                .setContentView(R.layout.dialog_send_message)
                .setGravity(Gravity.BOTTOM)
                .setWidthPercent(1f)
                .setOnClickListener(R.id.sendBtn, new avd.nk.com.apsaravideodemo.widget.dialog.OnClickListener() {
                    @Override
                    public void onClick(View v, boolean isPerformed) {
                        EditText editText = (EditText) sendMessageDialog.getViewById(R.id.inputMessage);
                        if (editText != null) {
                            String message = editText.getText().toString();
                            callback.onMessageClick(message);
                            editText.setText("");
                        } else {
                            Toast.makeText(getContext(), "can't get text from input...", Toast.LENGTH_SHORT).show();
                        }
                        sendMessageDialog.dismiss();
                    }
                })
                .setOnClickListener(R.id.faceBtn, new avd.nk.com.apsaravideodemo.widget.dialog.OnClickListener() {
                    @Override
                    public void onClick(View v, boolean isPerformed) {

                    }
                })
                .build();
        sendMessageDialog.isHideSoftKeyboardOnDismiss(true);
    }

    public void setLiveVideoBottomPanelActionCallback(LiveVideoBottomPanelActionCallback callback) {
        this.callback = callback;
    }

    public void onStateChanged(AlivcLivePushStats state) {
        switch (state) {
            case PUSHING:
                startBtn.setImageResource(R.drawable.ic_stop_red);
                break;
            case PUSHED:
                startBtn.setImageResource(R.drawable.ic_stop_red);
                break;
            case ERROR:
            case PAUSED:
            case PREVIEWED:
                startBtn.setImageResource(R.drawable.ic_play_green);
                break;
            default:
                break;
        }
    }

    public void clearCallback(){
        this.callback = null;
    }

    /**
     * A simple callback of LiveVideoBottomPanel.
     */
    public interface LiveVideoBottomPanelActionCallback {
        void onMessageClick(String msg);

        void onPushClick();

        void onSwitchCamera();
    }
}
