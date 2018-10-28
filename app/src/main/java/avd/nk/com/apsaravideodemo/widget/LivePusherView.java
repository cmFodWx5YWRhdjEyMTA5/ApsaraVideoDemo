package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import java.lang.ref.WeakReference;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.widget.view.BaseLiveVideoView;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoTopPanel;
import avd.nk.com.apsaravideodemo.widget.view.MessageView;

/**
 * Created by Nikou Karter.
 *
 * LivePusherView is use to display the screen of anchor side in live video, inherit from
 * {@link BaseLiveVideoView}, containing a few control panels {@link #liveVideoTopPanel},
 * {@link #liveVideoBottomPanel}, this view also use to pushing stream to server by simply call
 * {@link #push(String)} function.
 */
public class LivePusherView extends BaseLiveVideoView {
    private static final String TAG = LivePusherView.class.getSimpleName();
    private String pushPath;
    private MessageView messageView;
    private LiveVideoBottomPanel liveVideoBottomPanel;
    private LiveVideoTopPanel liveVideoTopPanel;
    private PusherViewActionCallback callback;

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
                callback.onPushClick();
            }

            @Override
            public void onSwitchCamera() {
                aliVCLivePusher.switchCamera();
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

    private static class PusherHandler extends Handler {
        private WeakReference<LivePusherView> weakReference;
        private LivePusherView livePusherView;

        PusherHandler(LivePusherView livePusherView){
            weakReference = new WeakReference<>(livePusherView);
            livePusherView = weakReference.get();
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            //livePusherView.handlerMessage(msg);
        }
    }

    public void setPusherViewActionCallback(PusherViewActionCallback callback){
        this.callback = callback;
    }

    public interface PusherViewActionCallback{
        void onExitClick();
        void onPushClick();
    }
}
