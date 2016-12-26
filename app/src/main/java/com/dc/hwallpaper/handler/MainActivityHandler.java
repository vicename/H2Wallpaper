package com.dc.hwallpaper.handler;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Li DaChang on 16/12/25.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class MainActivityHandler {
    private Context mContext;
    private int mScreenWidth;

    public MainActivityHandler(Context mContext) {
        this.mContext = mContext;
    }

    public MainActivityHandler(Context mContext, int mScreenWidth) {
        this.mContext = mContext;
        this.mScreenWidth = mScreenWidth;
    }

    public void setBoxSize(View box) {
        int height = mScreenWidth / 4 * 3;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) box.getLayoutParams();
        lp.height = height;
        box.setLayoutParams(lp);
    }

    public boolean setShadowVisibility(View view, boolean isShow) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            return false;
        } else {
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }
}
