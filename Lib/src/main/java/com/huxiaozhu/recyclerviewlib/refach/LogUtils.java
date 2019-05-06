package com.huxiaozhu.recyclerviewlib.refach;

import android.text.TextUtils;
import android.util.Log;

class LogUtils {
    private String mDefaultTag;
    private boolean mIsDebug;
    private String mDebugTag;

    public LogUtils(Class clazz) {
        this(clazz.getSimpleName());
    }

    public LogUtils(String defaultTag) {
        if (TextUtils.isEmpty(defaultTag)) {
            throw new IllegalArgumentException("defaultTag must not be null or empty");
        } else {
            this.mDefaultTag = defaultTag;
        }
    }

    public boolean isDebug() {
        return this.mIsDebug;
    }

    public void setDebug(boolean debug) {
        this.mIsDebug = debug;
    }

    public void setDebugTag(String debugTag) {
        this.mDebugTag = debugTag;
    }

    private String getDebugTag() {
        if (!TextUtils.isEmpty(this.mDebugTag)) {
            return this.mDebugTag;
        } else {
            return !TextUtils.isEmpty(this.mDefaultTag) ? this.mDefaultTag : "";
        }
    }

    public void i(String msg) {
        if (this.mIsDebug) {
            Log.i(this.getDebugTag(), msg);
        }

    }

    public void e(String msg) {
        if (this.mIsDebug) {
            Log.e(this.getDebugTag(), msg);
        }

    }
}