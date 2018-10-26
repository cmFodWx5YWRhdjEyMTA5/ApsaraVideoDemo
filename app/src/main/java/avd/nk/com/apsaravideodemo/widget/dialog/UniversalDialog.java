/*
 * Copyright (c) 2018.
 * It is only used in the commercial software development that the author participates in,
 * or in other non-commercial research studies.
 * If it is found that the code is used in the commercial software development without the consent of the author,
 * or the copyright issue will be investigated.
 * the author reserves all the right of interpretation of the code.
 * Author : Nikou Karter
 */

package avd.nk.com.apsaravideodemo.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Objects;

import avd.nk.com.apsaravideodemo.R;


/**
 * Created by Nikou Karter on 2018/1/4.
 * NTS Dialog.
 */

public class UniversalDialog extends Dialog {

    private static final String TAG = UniversalDialog.class.getSimpleName();

    private View contentView;
    private DialogCtrl dialogCtrl;

    UniversalDialog(@NonNull Context context) {
        this(context, 0);
    }

    private UniversalDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        dialogCtrl = new DialogCtrl(this, getWindow());
    }

    public View getViewById(int viewId) {
        if (contentView == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                contentView = Objects.requireNonNull(getWindow()).getDecorView();
                Log.i(TAG, "reid = " + contentView.getId());
            }
        }
        return contentView != null ? contentView.findViewById(viewId) : null;
    }

    public static class Builder {
        private final DialogCtrl.DialogParams dialogParams;

        public Builder(Context context) {
            this(context, R.style.defaultNDialogTheme);
        }

        public Builder(Context context, int themeId) {
            dialogParams = new DialogCtrl.DialogParams(context, themeId);
        }

        public Builder setContentView(View contentView) {
            dialogParams.contentView = contentView;
            dialogParams.layoutResId = 0;
            return this;
        }

        public Builder setContentView(@LayoutRes int layoutResId) {
            dialogParams.layoutResId = layoutResId;
            Log.i(TAG, "reid = " + layoutResId);
            return this;
        }

        public Builder setText(int viewId, CharSequence text) {
            dialogParams.texts.put(viewId, text);
            return this;
        }

        public Builder setFullScreen(boolean isFullScreen) {
            dialogParams.isFullScreen = isFullScreen;
            return this;
        }

        public Builder setWidth(int width) {
            dialogParams.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            dialogParams.height = height;
            return this;
        }

        public Builder setWidthPercent(float widthPercent) {
            dialogParams.widthPercent = widthPercent;
            return this;
        }

        public Builder setHeightPercent(float heightPercent) {
            dialogParams.heightPercent = heightPercent;
            return this;
        }

        public Builder setGravity(int gravity) {
            dialogParams.gravity = gravity;
            return this;
        }

        public Builder setAnnimations(int animationsResId) {
            dialogParams.animationsResId = animationsResId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            dialogParams.cancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            dialogParams.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            dialogParams.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            dialogParams.onKeyListener = onKeyListener;
            return this;
        }

        public Builder setOnClickListener(@IdRes int id, avd.nk.com.apsaravideodemo.widget.dialog.OnClickListener listener) {
            dialogParams.onClickListeners.put(id, listener);
            return this;
        }

        public UniversalDialog build() {
            final UniversalDialog dialog = new UniversalDialog(dialogParams.context, dialogParams.themeId);
            dialogParams.apply(dialog.dialogCtrl);
            //dialogParams.contentView = dialog.dialogCtrl.getDialog().contentView;
            //Log.i(TAG, "dialogParams.contentView = " + dialogParams.contentView);

            dialog.setCancelable(dialogParams.cancelable);

            if (dialogParams.onCancelListener != null) {
                dialog.setOnCancelListener(dialogParams.onCancelListener);
            }

            if (dialogParams.onDismissListener != null) {
                dialog.setOnDismissListener(dialogParams.onDismissListener);
            }

            if (dialogParams.onKeyListener != null) {
                dialog.setOnKeyListener(dialogParams.onKeyListener);
            }

            return dialog;
        }

        public UniversalDialog show() {
            final UniversalDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }
}
