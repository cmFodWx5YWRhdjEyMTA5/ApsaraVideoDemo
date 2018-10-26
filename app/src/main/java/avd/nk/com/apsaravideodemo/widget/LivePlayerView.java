package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.dialog.NDialog;

public class LivePlayerView extends RelativeLayout {
    private Role role;
    private NDialog sendMessageDialog;

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
        LayoutInflater.from(getContext()).inflate(R.layout.view_player_live, this, true);

        sendMessageDialog = new NDialog.Builder(getContext())
                .setContentView(R.layout.dialog_send_message)
                .setGravity(Gravity.BOTTOM)
                .setWidthPercent(100f)
                .build();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageDialog.show();
            }
        });
    }

    public void setRole(Role role){
        this.role = role;
    }

    public enum Role{
        AUDIENCE, ANCHOR
    }
}
