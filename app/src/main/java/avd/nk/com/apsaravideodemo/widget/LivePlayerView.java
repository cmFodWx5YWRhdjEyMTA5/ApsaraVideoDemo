package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class LivePlayerView extends RelativeLayout {
    private Role role;

    public LivePlayerView(Context context) {
        this(context, null);
    }

    public LivePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    public void setRole(Role role){
        this.role = role;
    }

    public enum Role{
        AUDIENCE, ANCHOR
    }
}
