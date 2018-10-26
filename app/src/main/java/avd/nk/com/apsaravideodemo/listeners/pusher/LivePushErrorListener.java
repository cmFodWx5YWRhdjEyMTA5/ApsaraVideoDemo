package avd.nk.com.apsaravideodemo.listeners.pusher;

import android.annotation.SuppressLint;
import android.util.Log;

import com.alivc.live.pusher.AlivcLivePushError;
import com.alivc.live.pusher.AlivcLivePushErrorListener;
import com.alivc.live.pusher.AlivcLivePusher;

public class LivePushErrorListener implements AlivcLivePushErrorListener {

    private final String TAG = "avd.nk.com.apsaravideodemo.listeners.pusher.LivePushErrorListener";

    public LivePushErrorListener(){

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onSystemError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
        Log.e(TAG, alivcLivePushError.toString());
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onSDKError(AlivcLivePusher alivcLivePusher, AlivcLivePushError alivcLivePushError) {
        Log.e(TAG, alivcLivePushError.toString());
    }
}
