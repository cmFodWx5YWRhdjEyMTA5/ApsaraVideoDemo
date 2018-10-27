package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.widget.view.BaseLiveVideoView;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.MessageView;

public class LivePusherView extends BaseLiveVideoView {
    private static final String TAG = LivePusherView.class.getSimpleName();
    private String pushPath;
    private MessageView messageView;
    private LiveVideoBottomPanel liveVideoBottomPanel;

    public LivePusherView(Context context) {
        this(context, null);
    }

    public LivePusherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePusherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.view_live_push, this, false),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        messageView = findViewById(R.id.messageView);

        liveVideoBottomPanel = findViewById(R.id.liveVideoBottomPanel);
        liveVideoBottomPanel.setLiveVideoBottomPanelActionCallback(new LiveVideoBottomPanel.LiveVideoBottomPanelActionCallback() {
            @Override
            public void onMessageClick(String msg) {
                //call http get the messages.
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

    public void preview() {
        try {
            aliVCLivePusher.startPreviewAysnc(surfaceView);
            //aliVCLivePusher.startPreview(surfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPreview(){
        try{
            aliVCLivePusher.stopPreview();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push() {
        try {
            if (!aliVCLivePusher.isPushing()) {
                if (pushPath != null) {
                    aliVCLivePusher.startPush(pushPath);
                    Toast.makeText(getContext(), "try to push", Toast.LENGTH_SHORT).show();
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
            if (aliVCLivePusher.isPushing()) {
                aliVCLivePusher.stopPush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume(){
        aliVCLivePusher.resume();
    }

    public void onPause(){
        aliVCLivePusher.pause();
    }

    public void onDestroy(){
        aliVCLivePusher.destroy();
    }
}
