package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcFpsEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;

import java.util.ArrayList;
import java.util.List;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushBGMListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushErrorListener;
import avd.nk.com.apsaravideodemo.listeners.pusher.LivePushNetworkListener;
import avd.nk.com.apsaravideodemo.widget.dialog.UniversalDialog;

public class LivePlayerView extends RelativeLayout {
    private Role role;
    private UniversalDialog sendMessageDialog;
    private SurfaceView pusherView;
    private RecyclerView messageView;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    private AlivcLivePusher aliVCLivePusher;
    private AlivcLivePushConfig aliVCLivePushConfig;

    public LivePlayerView(Context context) {
        this(context, null);
    }

    public LivePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPushConfig();
        initPusher();
    }

    private void initPusher() {
        aliVCLivePusher = new AlivcLivePusher();
        aliVCLivePusher.init(getContext(), aliVCLivePushConfig);

        aliVCLivePusher.setLivePushErrorListener(new LivePushErrorListener());
        aliVCLivePusher.setLivePushNetworkListener(new LivePushNetworkListener());
        aliVCLivePusher.setLivePushBGMListener(new LivePushBGMListener());
        aliVCLivePusher.setLivePushInfoListener(new AlivcLivePushInfoListener() {
            @Override
            public void onPreviewStarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPreviewStoped(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushStarted(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushPauesed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushResumed(AlivcLivePusher alivcLivePusher) {

            }

            @Override
            public void onPushStoped(AlivcLivePusher alivcLivePusher) {

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

    private void initPushConfig() {
        aliVCLivePushConfig = new AlivcLivePushConfig();
        //mAliVCLivePushConfig.setResolution(AlivcResolutionEnum.RESOLUTION_540P);//set resolution.
        aliVCLivePushConfig.setFps(AlivcFpsEnum.FPS_20); //set frames per second.
        aliVCLivePushConfig.setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT);
        aliVCLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LC);//set audio profile.
        aliVCLivePushConfig.setEnableBitrateControl(true);// set auto bitrate, default value is true.
        aliVCLivePushConfig.setEnableAutoResolution(true);
        aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
        aliVCLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL);
    }

    private void setCustomQualityMode(int targetBitrate, int minBitrate, int initBitrate) {
        if (aliVCLivePushConfig != null) {
            aliVCLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_CUSTOM);
            aliVCLivePushConfig.setTargetVideoBitrate(targetBitrate);
            aliVCLivePushConfig.setMinVideoBitrate(minBitrate);
            aliVCLivePushConfig.setInitialVideoBitrate(initBitrate);
        }
    }

    public void preview(){
        try{
            aliVCLivePusher.startPreview(pusherView);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_player_live, this, true);

        initSurfaceView();
        /*for (int i = 0; i < 10; i++) {
            messageList.add(new Message("user" + i, "content text " + i));
        }*/

        //messageView.
        messageView = findViewById(R.id.messageView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) manager).setOrientation(LinearLayout.VERTICAL);
        adapter = new MessageAdapter();
        messageView.setLayoutManager(manager);
        messageView.setAdapter(adapter);

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
                            //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            newMessage(new Message("user live", message));
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
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Toast.makeText(getContext(), "onDismiss", Toast.LENGTH_SHORT).show();
                        hideKeyBoard();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(getContext(), "onCancel", Toast.LENGTH_SHORT).show();
                        hideKeyBoard();
                    }
                })
                .build();

        findViewById(R.id.sendBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageDialog.show();
            }
        });

        findViewById(R.id.activityTitle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                preview();
            }
        });
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        /*getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*/
    }

    private void initSurfaceView() {
        pusherView = findViewById(R.id.pusherView);
        pusherView.getHolder().addCallback(new SurfaceHolder.Callback() {
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

    public void setRole(Role role) {
        this.role = role;
    }

    public void newMessage(Message message) {
        messageList.add(message);
        adapter.notifyDataSetChanged();
        messageView.scrollToPosition(messageList.size() - 1);
    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_massegeview, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.senderName.setText(messageList.get(i).getSenderName());
            viewHolder.content.setText(messageList.get(i).getContent());
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView senderName;
            TextView content;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                senderName = itemView.findViewById(R.id.senderName);
                content = itemView.findViewById(R.id.content);
            }
        }
    }

    public enum Role {
        AUDIENCE, ANCHOR
    }
}
