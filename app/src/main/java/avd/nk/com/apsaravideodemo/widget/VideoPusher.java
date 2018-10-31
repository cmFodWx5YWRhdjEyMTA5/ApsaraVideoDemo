package avd.nk.com.apsaravideodemo.widget;

import com.alivc.live.pusher.AlivcLivePusher;

public class VideoPusher extends AlivcLivePusher {

    public void clear(VideoPusher pusher){
        mBluetoothHelper = null;
        try {
            ReflectHelper.setField(pusher, "mPushBGMListener", null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
