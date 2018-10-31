package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushBGMListener;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushError;
import com.alivc.live.pusher.AlivcLivePushErrorListener;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePushNetworkListener;
import com.alivc.live.pusher.AlivcLivePushStats;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;
import com.alivc.live.pusher.AlivcResolutionEnum;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.widget.view.BaseLivePusherView;
import avd.nk.com.apsaravideodemo.widget.view.LivePushBottomPanel;
import avd.nk.com.apsaravideodemo.widget.view.LiveVideoTopPanel;
import avd.nk.com.apsaravideodemo.widget.view.MessageView;

import com.alivc.live.pusher.a;

/**
 * Created by Nikou Karter.
 * <p>
 * LivePusherView is use to display the screen of anchor side in live video, inherit from
 * {@link BaseLivePusherView}, containing a few control panels {@link #liveVideoTopPanel},
 * {@link #livePushBottomPanel}, this view also use to pushing stream to server by simply call
 * {@link #push(String)} function.
 */
public class LivePusherView extends ConstraintLayout {
    private static final String TAG = LivePusherView.class.getSimpleName();
    private String pushPath;
    private SurfaceView surfaceView;
    private MessageView messageView;
    private LivePushBottomPanel livePushBottomPanel;
    private LiveVideoTopPanel liveVideoTopPanel;
    private PusherViewActionCallback callback;
    private AlivcLivePusher aliVCLivePusher;
    private AlivcLivePushConfig aliVCLivePushConfig;
    private WeakReference<Context> weakReference;

    public LivePusherView(Context context) {
        this(context, null);
    }

    public LivePusherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePusherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPushConfig();
        initPlayer();
    }

    private void initSurfaceView() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    private void initPushConfig() {
        aliVCLivePushConfig = new AlivcLivePushConfig();
        aliVCLivePushConfig.setResolution(AlivcResolutionEnum.RESOLUTION_540P);//set resolution.
        aliVCLivePushConfig.setFps(AlivcFpsEnum.FPS_20); //set frames per second.
        aliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT);
        aliVCLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LC);//set audio profile.
        aliVCLivePushConfig.setEnableBitrateControl(true);// set auto bitrate, default value is true.
        //aliVCLivePushConfig.setEnableAutoResolution(true);
        aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
        aliVCLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL);
    }

    private void initPlayer() {

        weakReference = new WeakReference<>(this.getContext());
        aliVCLivePusher = new AlivcLivePusher();
        try{
            aliVCLivePusher.init(weakReference.get(), aliVCLivePushConfig);
        }catch (Exception e){
            e.printStackTrace();
        }

        aliVCLivePusher.setLivePushErrorListener(new AlivcLivePushErrorListener() {
            @Override
            public void onSystemError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
                Log.e(TAG, alivcLivePushError.getMsg());
                //alivcLivePusher.pause();
            }

            @Override
            public void onSDKError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
                //alivcLivePusher.pause();
                Log.e(TAG, alivcLivePushError.getMsg());
            }
        });

        aliVCLivePusher.setLivePushNetworkListener(new AlivcLivePushNetworkListener() {
            @Override
            public void onNetworkPoor(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onNetworkRecovery(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onReconnectStart(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onReconnectFail(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onReconnectSucceed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onSendDataTimeout(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onConnectFail(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public String onPushURLAuthenticationOverdue(AlivcLivePusher alivcLivePusher) {
                return null;
            }

            @Override
            public void onSendMessage(AlivcLivePusher alivcLivePusher) {

            }
        });

        aliVCLivePusher.setLivePushBGMListener(new AlivcLivePushBGMListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onStoped() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onResumed() {

            }

            @Override
            public void onProgress(long l, long l1) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onDownloadTimeout() {

            }

            @Override
            public void onOpenFailed() {

            }
        });

        aliVCLivePusher.setLivePushInfoListener(new AlivcLivePushInfoListener() {
            @Override
            public void onPreviewStarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPreviewStoped(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushStarted(AlivcLivePusher alivcLivePusher) {
                //Log.i(TAG, "onPushStarted");
                LivePusherView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        livePushBottomPanel.onStateChanged(aliVCLivePusher.getCurrentStatus());
                        Toast.makeText(getContext(), "start pushing", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Status = " + aliVCLivePusher.getCurrentStatus());
                    }
                });
                //livePushBottomPanel.onStateChanged(aliVCLivePusher.getCurrentStatus());
            }

            @Override
            public void onPushPauesed(AlivcLivePusher alivcLivePusher) {
                //Log.i(TAG, "onPushPaused");
                LivePusherView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "stop pushing", Toast.LENGTH_SHORT).show();
                        livePushBottomPanel.onStateChanged(aliVCLivePusher.getCurrentStatus());
                    }
                });
                //livePushBottomPanel.onStateChanged(aliVCLivePusher.getCurrentStatus());
            }

            @Override
            public void onPushResumed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushStoped(AlivcLivePusher alivcLivePusher) {
                //Log.i(TAG, "onPushStopped");
                LivePusherView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "stop pushing", Toast.LENGTH_SHORT).show();
                        livePushBottomPanel.onStateChanged(aliVCLivePusher.getCurrentStatus());
                        Log.i(TAG, "Status = " + aliVCLivePusher.getCurrentStatus());
                    }
                });
                //livePushBottomPanel.onStateChanged(aliVCLivePusher.getCurrentStatus());
            }

            @Override
            public void onPushRestarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onFirstFramePreviewed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onDropFrame(AlivcLivePusher alivcLivePusher, int i, int i1) {

            }

            @Override
            public void onAdjustBitRate(AlivcLivePusher alivcLivePusher, int i, int i1) {

            }

            @Override
            public void onAdjustFps(AlivcLivePusher alivcLivePusher, int i, int i1) {

            }
        });
    }

    private void initView() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.view_live_push, this, false),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        initSurfaceView();
        messageView = findViewById(R.id.messageView);

        livePushBottomPanel = findViewById(R.id.liveVideoBottomPanel);
        livePushBottomPanel.setLiveVideoBottomPanelActionCallback(new LivePushBottomPanel.LiveVideoBottomPanelActionCallback() {
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

    /*private static class PusherHandler extends Handler {
        private WeakReference<LivePusherView> weakReference;
        private LivePusherView livePusherView;

        PusherHandler(LivePusherView livePusherView) {
            weakReference = new WeakReference<>(livePusherView);
            livePusherView = weakReference.get();
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            //livePusherView.handlerMessage(msg);
        }
    }*/

    public void setPusherViewActionCallback(PusherViewActionCallback callback) {
        this.callback = callback;
    }

    public void clearCallback() {
        this.callback = null;
    }

    public void onResume() {
        aliVCLivePusher.resume();
    }

    public void onPause() {
        //stopPreview();
        //aliVCLivePusher.stopPreview();
        aliVCLivePusher.pause();
        //aliVCLivePusher.stopPush();
    }

    /*private void reflect(){
        Class c = aliVCLivePusher.getClass();
        a ac = new a(null);
        Field[] fields = c.getDeclaredFields("mBluetoothHelper");
        for(Field field:fields){
            if(field.getName().equals("")){
                try {
                    field.set(ac, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * this method works if aliVCLivePusher on its destroy life circle can not set "mBluetoothHelper"
     * and "mPushBGMListener" to null, if these param are not null, they may cause memory leaks.
     * @param aliVCLivePusher a local param of aliVCLivePusher.
     */
    private void clear(AlivcLivePusher aliVCLivePusher){
        try {
            ReflectHelper.setField(aliVCLivePusher, "mPushBGMListener", null);
            ReflectHelper.setField(aliVCLivePusher, "mBluetoothHelper", null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        liveVideoTopPanel.clearCallback();
        liveVideoTopPanel = null;
        livePushBottomPanel.clearCallback();
        livePushBottomPanel = null;
        messageView = null;
        callback = null;

        try {
            aliVCLivePusher.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

        clear(aliVCLivePusher);
    }

    public void preview() {
        try {
            aliVCLivePusher.startPreviewAysnc(surfaceView);
            //aliVCLivePusher.startPreview(surfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        try {
            aliVCLivePusher.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push(String pushPath) {
        try {
            if (!aliVCLivePusher.isPushing()) {
                if (pushPath != null) {
                    aliVCLivePusher.startPushAysnc(pushPath);
                    //aliVCLivePusher.startPush(pushPath);
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
            if (aliVCLivePusher.getCurrentStatus() == AlivcLivePushStats.PUSHED) {
                aliVCLivePusher.stopPush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PusherViewActionCallback {
        void onExitClick();

        void onPushClick();
    }
}
