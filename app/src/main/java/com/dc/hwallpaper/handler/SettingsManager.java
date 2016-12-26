package com.dc.hwallpaper.handler;

import android.content.Context;

import com.dc.hwallpaper.utils.SPUtils;

/**
 * Created by Li DaChang on 16/12/25.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class SettingsManager {
    private SPUtils mSPUtils;
    private static SettingsManager mInstance;

    public static synchronized SettingsManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SettingsManager(context);
        }
        return mInstance;
    }

    private SettingsManager(Context context) {
        mSPUtils = new SPUtils(context);
    }
}
