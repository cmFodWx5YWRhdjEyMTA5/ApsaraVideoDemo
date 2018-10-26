package avd.nk.com.apsaravideodemo.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;
import avd.nk.com.apsaravideodemo.widget.dialog.OnClickListener;
import avd.nk.com.apsaravideodemo.widget.dialog.UniversalDialog;

public class LivePlayerView extends RelativeLayout {
    private Role role;
    private UniversalDialog sendMessageDialog;
    private RecyclerView messageView;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

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

        for (int i = 0; i < 10; i++) {
            messageList.add(new Message("user" + i, "content text " + i));
        }

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
                .build();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageDialog.show();
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
