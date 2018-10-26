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

import android.view.View;

/**
 * Created by adm on 2017/12/28.
 * OnClickListener.
 */

public abstract class OnClickListener implements View.OnClickListener {
    private int timeInterval = 1000;
    private long lastClickTime = 0L;

    public abstract void onClick(View v, boolean isPerformed);

    @Override
    public void onClick(View v) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= timeInterval) {
            lastClickTime = currentClickTime;
            onClick(v, true);
        }else {
            onClick(v, false);
        }
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }
}
