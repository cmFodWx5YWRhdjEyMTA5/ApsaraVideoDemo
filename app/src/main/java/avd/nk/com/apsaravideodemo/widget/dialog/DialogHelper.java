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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Nikou Karter on 2018/1/5.
 * Dialog helper.
 */

class DialogHelper {

    private View contentView;

    DialogHelper(Context context, int layoutResId) {
        contentView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    DialogHelper(Context context, View contentView) {
        this.contentView = contentView;
    }

    View getContentView() {
        return contentView;
    }

    private View getViewById(int viewId) {
        View view = null;
        if (contentView != null) {
            view = contentView.findViewById(viewId);
        }
        return view;
    }

    void setText(int viewId, CharSequence charSequence) {
        View view = getViewById(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(charSequence);
        }
    }

    void setOnClickListener(int viewId, OnClickListener onClickListener) {
        View view = getViewById(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }
}
