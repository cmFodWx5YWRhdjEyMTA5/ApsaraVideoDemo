package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import avd.nk.com.apsaravideodemo.R;
import avd.nk.com.apsaravideodemo.widget.PlayerView;

/**
 * Created by Nikou Karter.
 *
 * coming soon.
 */
public class PlayerTopPanel extends LinearLayout {
    private PlayerView.TopPanelActionCallback actionCallback;
    private ImageView backBtn;
    private ImageView optionBtn;
    private TextView subjectTitle;

    public PlayerTopPanel(Context context) {
        this(context, null);
    }

    public PlayerTopPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerTopPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_top_panel, this, true);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCallback.onBackClicked();
            }
        });

        optionBtn = findViewById(R.id.optionBtn);
        optionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCallback.onOptionClicked((ImageView) v);
            }
        });

        subjectTitle = findViewById(R.id.subjectTitle);
    }

    public void setOnTopPanelClickedListener(PlayerView.TopPanelActionCallback listener) {
        this.actionCallback = listener;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle.setText(subjectTitle);
    }

    public void showOptionBtn() {
        optionBtn.setVisibility(VISIBLE);
    }

    public void hideOptionBtn() {
        optionBtn.setVisibility(GONE);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    public void removeActionCallback() {
        if (actionCallback != null) {
            actionCallback = null;
        }
    }
}
