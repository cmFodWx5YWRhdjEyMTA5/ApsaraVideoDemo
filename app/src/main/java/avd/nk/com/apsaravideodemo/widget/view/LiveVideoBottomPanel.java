package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.dialog.UniversalDialog;

public class LiveVideoBottomPanel extends LinearLayout {
    private ImageView messageBtn;
    private ImageView startBtn;
    private ImageView switchCamera;
    private UniversalDialog sendMessageDialog;
    private LiveVideoBottomPanelActionCallback callback;

    private boolean isPushing = false;

    public LiveVideoBottomPanel(Context context) {
        this(context, null);
    }

    public LiveVideoBottomPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveVideoBottomPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_bottom_panel, this, true);
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

    public void setLiveVideoBottomPanelActionCallback(LiveVideoBottomPanelActionCallback callbak){
        this.callback = callbak;
    }

    public interface LiveVideoBottomPanelActionCallback {
        void onMessageClick(String msg);
        void onPushClick();
        void onSwitchCamera();
    }
}
