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

import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.dialog.UniversalDialog;

/**
 * Created by Nikou Karter.
 *
 * LiveVideoBottomPanel is a custom view for {@link avd.nk.com.apsaravideodemo.widget.LiveRoomView}.
 * It's use to display the action buttons for user, including functions such as sending messages, start
 * or stop.
 * {@link #messageBtn} show sending message dialog.
 * {@link #startBtn} use to start or stop pull stream.
 *
 */
public class LiveRoomBottomPanel extends ConstraintLayout {
    private ImageView messageBtn;
    private ImageView startBtn;
    private UniversalDialog sendMessageDialog;//send message dialog.
    private LiveRoomBottomPanelActionCallback callback;

    public LiveRoomBottomPanel(Context context) {
        this(context, null);
    }

    public LiveRoomBottomPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveRoomBottomPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_room_bottom_panel, this, true);
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
                callback.onStartPlayClick();
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

    public void setLiveRoomBottomPanelActionCallback(LiveRoomBottomPanelActionCallback callback){
        this.callback = callback;
    }

    public void onStateChanged(IAliyunVodPlayer.PlayerState state){
        switch (state) {
            case Started:
                startBtn.setImageResource(R.drawable.ic_stop_red);
                break;
            case Paused:
            case Idle:
            case Error:
                startBtn.setImageResource(R.drawable.ic_play_green);
                break;
            case Completed:

                break;
        }
    }

    /**
     * A simple callback of LiveVideoBottomPanel.
     */
    public interface LiveRoomBottomPanelActionCallback {
        void onMessageClick(String msg);
        void onStartPlayClick();
    }
}
