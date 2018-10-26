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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by adm on 2018/1/5.
 * Dialog Controller.
 */

class DialogCtrl {

    private static final String TAG = DialogCtrl.class.getSimpleName();

    private final UniversalDialog dialog;
    private final Window window;

    DialogCtrl(UniversalDialog dialog, Window window) {
        this.dialog = dialog;
        this.window = window;
    }

    static class DialogParams {
        Context context;
        View contentView;
        int themeId;
        int layoutResId;

        DialogInterface.OnCancelListener onCancelListener;
        DialogInterface.OnDismissListener onDismissListener;
        DialogInterface.OnKeyListener onKeyListener;
        SparseArray<CharSequence> texts;
        SparseArray<OnClickListener> onClickListeners;

        boolean isFullScreen = false;
        boolean cancelable = true;

        int gravity = Gravity.NO_GRAVITY;
        int animationsResId = 0;
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        float widthPercent = 0f;
        float heightPercent = 0f;

        DialogParams(Context context, int themeId) {
            this.context = context;
            this.themeId = themeId;

            texts = new SparseArray<>();
            onClickListeners = new SparseArray<>();
        }

        void apply(DialogCtrl dialogCtrl) {
            DialogHelper dialogHelper = null;

            if (layoutResId != 0) {
                dialogHelper = new DialogHelper(context, layoutResId);
            }

            if (contentView != null) {
                dialogHelper = new DialogHelper(context, contentView);
            }

            if (dialogHelper == null) {
                throw new IllegalArgumentException("nullContentViewException.");
            }

            View view = dialogHelper.getContentView();
            if (view != null) {
                if (dialogCtrl != null) {
                    Dialog d = dialogCtrl.getDialog();
                    if (d != null) {

                        dialogCtrl.getDialog().setContentView(view);
                    } else {
                        Log.i(TAG, "Dialog is null");
                    }
                } else {
                    Log.i(TAG, "dialogCtrl is null");
                }
                Log.i(TAG, "dialogHelper.getContentView = " + view.getId());
            } else {
                Log.i(TAG, "dialogHelper.getContentView is null");
            }

            for (int i = 0; i < texts.size(); i++) {
                dialogHelper.setText(texts.keyAt(i), texts.valueAt(i));
            }

            for (int i = 0; i < onClickListeners.size(); i++) {
                dialogHelper.setOnClickListener(onClickListeners.keyAt(i), onClickListeners.valueAt(i));
            }

            //set window attrs.
            Window window = dialogCtrl.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();

            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (gravity != Gravity.NO_GRAVITY) {
                layoutParams.gravity = gravity;
            }

            if (widthPercent != 0f) {
                layoutParams.width = (int) (metrics.widthPixels * widthPercent);
                //Log.i(TAG, "widthPercent = " + widthPercent);
                //Log.i(TAG, "metrics.widthPixels = " + metrics.widthPixels);
                //Log.i(TAG, "layoutParams.width = " + layoutParams.width);
            }

            if (heightPercent != 0f) {
                layoutParams.height = (int) (metrics.heightPixels * heightPercent);
            }

            if (isFullScreen) {
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            }

            window.setAttributes(layoutParams);

            if (animationsResId != 0) {
                window.setWindowAnimations(animationsResId);
            }

        }
    }

    UniversalDialog getDialog() {
        return dialog;
    }

    Window getWindow() {
        return window;
    }
}
