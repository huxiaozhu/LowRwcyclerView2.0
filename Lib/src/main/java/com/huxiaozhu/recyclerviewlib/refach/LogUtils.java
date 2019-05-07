package com.huxiaozhu.recyclerviewlib.refach;

import android.util.Log;

import com.huxiaozhu.recyclerviewlib.BuildConfig;

class LogUtils {
    private boolean mIsDebug;

    LogUtils() {
        mIsDebug = BuildConfig.DEBUG;
    }

    boolean isDebug() {
        return this.mIsDebug;
    }

    void i(String msg) {
        if (this.mIsDebug) {
            Log.i("RBLog", msg);
        }

    }

    void e(String msg) {
        if (this.mIsDebug) {
            Log.e("RBLog", msg);
        }

    }
}