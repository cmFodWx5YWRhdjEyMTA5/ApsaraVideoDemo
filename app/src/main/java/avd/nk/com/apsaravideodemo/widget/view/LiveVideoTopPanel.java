package avd.nk.com.apsaravideodemo.widget.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.entity.GlideApp;

/**
 * Created by Nikou Karter.
 *
 * LiveVideoTopPanel is a custom view for {@link avd.nk.com.apsaravideodemo.widget.LivePusherView}.
 * It's use to display anchor and audience information including icons and names and how many people
 * are in this live video room, it also including an button for exit the live video room.
 */
public class LiveVideoTopPanel extends ConstraintLayout {
    private RecyclerView audienceView;//a recycleView use to display audience icon.
    private AudienceViewAdapter audienceViewAdapter;//simple recycleView adapter.
    private LiveVideoTopPanelActionCallback callback;//a callback of top panel actions.
    private List<Uri> audienceIconList = new ArrayList<>();//list of audience icon.

    public LiveVideoTopPanel(Context context) {
        this(context, null);
    }

    public LiveVideoTopPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveVideoTopPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_top_panel, this, true);
        initAudienceView();

        findViewById(R.id.leaveBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onExitClick();
            }
        });
    }

    private void initAudienceView() {
        Resources r =getContext().getResources();
        for (int i = 0; i < 5; i++) {
            Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(R.drawable.ic_user2) + "/"
                    + r.getResourceTypeName(R.drawable.ic_user2) + "/"
                    + r.getResourceEntryName(R.drawable.ic_user2));
            audienceIconList.add(uri);
        }

        audienceView = findViewById(R.id.audienceView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        audienceViewAdapter = new AudienceViewAdapter();
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.HORIZONTAL);
        audienceView.setLayoutManager(layoutManager);
        audienceView.setAdapter(audienceViewAdapter);
    }

    /**
     * see also {@link #updateAudienceList(Uri)}.
     * @param uriList a collection of the icon.
     */
    public void updateAudienceList(List<Uri> uriList) {
        if (audienceIconList != null) {
            audienceIconList.addAll(uriList);
            audienceViewAdapter.notifyDataSetChanged();
            audienceView.scrollToPosition(audienceIconList.size() - 1);
        }
    }

    /**
     * @param uri the address of the icon.
     */
    public void updateAudienceList(Uri uri) {
        if (audienceIconList != null) {
            audienceIconList.add(uri);
            audienceViewAdapter.notifyDataSetChanged();
            audienceView.scrollToPosition(audienceIconList.size() - 1);
        }
    }

    /**
     * @param callback a top panel action callback.
     */
    public void setLiveVideoTopPanelActionCallback(LiveVideoTopPanelActionCallback callback){
        this.callback = callback;
    }

    public void clearCallback(){
        this.callback = null;
    }

    /**
     * a simple top panel action callback, maybe more actions in future.
     */
    public interface LiveVideoTopPanelActionCallback {
        void onExitClick();
    }

    /**
     * A simple recyclerView adapter use for setting audience icons.
     */
    class AudienceViewAdapter extends RecyclerView.Adapter<AudienceViewAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_audienceview, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.audienceIcon.setImageURI(audienceIconList.get(i));
            //GlideApp.with(getContext()).load(audienceIconList.get(i)).into(viewHolder.audienceIcon);
        }

        @Override
        public int getItemCount() {
            return audienceIconList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView audienceIcon;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                audienceIcon = itemView.findViewById(R.id.audienceIcon);
            }
        }

    }
}
