package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.Message;

/**
 * Created by Nikou Karter.
 *
 * {@link MessageView} is use to display the messages while in live video room sent by audiences.
 */
public class MessageView extends ConstraintLayout {
    private RecyclerView messageView;
    private MessageView.MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    public MessageView(Context context) {
        this(context, null);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_message, this, true);
        initMessageView();
    }

    private void initMessageView() {
        messageView = findViewById(R.id.messageLooperView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        adapter = new MessageAdapter();
        messageView.setLayoutManager(manager);
        messageView.setAdapter(adapter);
    }

    /**
     * @param message a new message received.
     */
    public void newMessage(Message message) {
        messageList.add(message);
        adapter.notifyDataSetChanged();
        messageView.scrollToPosition(messageList.size() - 1);
    }

    /**
     * A simple recycleView adapter for MessageView use to display messages.
     */
    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        @NonNull
        @Override
        public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MessageAdapter.ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_massegeview, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MessageView.MessageAdapter.ViewHolder viewHolder, int i) {
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

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                senderName = itemView.findViewById(R.id.senderName);
                content = itemView.findViewById(R.id.content);
            }
        }
    }
}
