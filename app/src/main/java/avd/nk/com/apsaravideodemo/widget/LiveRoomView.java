package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.widget.view.BaseLivePusherView;
import avd.nk.com.apsaravideodemo.widget.view.BaseLiveRoomView;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoTopPanel;
import avd.nk.com.apsaravideodemo.widget.view.MessageView;

/**
 * Created by Nikou Karter.
 *
 *
 */
public class LiveRoomView extends BaseLiveRoomView {
    private MessageView messageView;
    private LiveVideoBottomPanel liveVideoBottomPanel;
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
    }

    private void initView() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.view_live_room, this, false),
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

            }

            @Override
            public void onSwitchCamera() {

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
