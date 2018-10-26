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
import android.support.annotation.NonNull;
import android.view.View;

import avd.nk.com.apsaravideodemo.R;


/**
 * Created by Nikou Karter on 2018/1/4.
 * NTS Dialog.
 */

public class NDialog extends Dialog {

    private View contentView;
    private DialogCtrl dialogCtrl;

    NDialog(@NonNull Context context) {
        this(context, 0);
    }

    NDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        dialogCtrl = new DialogCtrl(this, getWindow());
    }

    public View getViewById(int viewId) {
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

        public Builder setContentView(View contentView){
            dialogParams.contentView = contentView;
            dialogParams.layoutResId = 0;
            return this;
        }

        public Builder setContentView(int layoutResId){
            dialogParams.contentView = null;
            dialogParams.layoutResId = layoutResId;
            return this;
        }

        public Builder setText(int viewId, CharSequence text){
            dialogParams.texts.put(viewId, text);
            return this;
        }

        public Builder setFullScreen(boolean isFullScreen){
            dialogParams.isFullScreen = isFullScreen;
            return this;
        }

        public Builder setWidth(int width){
            dialogParams.width = width;
            return this;
        }

        public Builder setHeight(int height){
            dialogParams.height = height;
            return this;
        }

        public Builder setWidthPercent(float widthPercent){
            dialogParams.widthPercent = widthPercent;
            return this;
        }

        public Builder setHeightPercent(float heightPercent){
            dialogParams.heightPercent = heightPercent;
            return this;
        }

        public Builder setGravity(int gravity){
            dialogParams.gravity = gravity;
            return this;
        }

        public Builder setAnnimations(int animationsResId){
            dialogParams.animationsResId = animationsResId;
            return this;
        }

        public Builder setCancelable(boolean cancelable){
            dialogParams.cancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener){
            dialogParams.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
            dialogParams.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener){
            dialogParams.onKeyListener = onKeyListener;
            return this;
        }


        public NDialog build() {
            final NDialog dialog = new NDialog(dialogParams.context, dialogParams.themeId);
            dialogParams.apply(dialog.dialogCtrl);

            dialog.setCancelable(dialogParams.cancelable);

            if(dialogParams.onCancelListener != null){
                dialog.setOnCancelListener(dialogParams.onCancelListener);
            }

            if(dialogParams.onDismissListener != null){
                dialog.setOnDismissListener(dialogParams.onDismissListener);
            }

            if (dialogParams.onKeyListener != null) {
                dialog.setOnKeyListener(dialogParams.onKeyListener);
            }

            return dialog;
        }

        public NDialog show() {
            final NDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }
}
