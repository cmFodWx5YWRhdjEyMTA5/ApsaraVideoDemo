package avd.nk.com.apsaravideodemo.listeners.pusher;

import android.os.Handler;
import android.util.Log;

import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePusher;

import java.lang.ref.WeakReference;

import avd.nk.com.apsaravideodemo.PushActivity;

public class LivePushInfoListener implements AlivcLivePushInfoListener {
    private final String TAG = "LivePushInfoListener";
    private boolean isAutoPush;
    private Handler handler;

    public LivePushInfoListener(Handler handler, boolean isAutoPush) {
        WeakReference<Handler> handlerWeakReference = new WeakReference<>(handler);
        this.handler = handlerWeakReference.get();
        this.isAutoPush = isAutoPush;
    }

    @Override
    public void onPreviewStarted(AlivcLivePusher alivcLivePusher) {
        Log.i(TAG, "preview is started.");
    }

    @Override
    public void onPreviewStoped(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "preview is stopped.");
        handler.sendEmptyMessage(PushActivity.STATE_PREVIEW_STOP);
    }

    @Override
    public void onPushStarted(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "pushing started.");
    }

    @Override
    public void onPushPauesed(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "pushing paused.");
    }

    @Override
    public void onPushResumed(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "pushing resumed.");
    }

    @Override
    public void onPushStoped(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "pushing stopped.");
        handler.sendEmptyMessage(PushActivity.STATE_PUSH_STOP);
    }

    @Override
    public void onPushRestarted(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "pushing restarted.");
    }

    @Override
    public void onFirstFramePreviewed(AlivcLivePusher alivcLivePusher) {
        Log.w(TAG, "first frame is previewed.");
        if (isAutoPush && handler != null) {
            handler.sendEmptyMessage(PushActivity.START_PUSH);
        }
    }

    @Override
    public void onDropFrame(AlivcLivePusher alivcLivePusher, int i, int i1) {
        Log.w(TAG, "drop frame i=" + i + ", i1=" + i1);
    }

    @Override
    public void onAdjustBitRate(AlivcLivePusher alivcLivePusher, int i, int i1) {
        Log.w(TAG, "adjust bitrate i=" + i + ", i1=" + i1);
    }

    @Override
    public void onAdjustFps(AlivcLivePusher alivcLivePusher, int i, int i1) {
        Log.w(TAG, "adjust fps i=" + i + ", i1=" + i1);
    }

    public void removeHandler(){
        if(handler != null){
            handler = null;
        }
    }
}
