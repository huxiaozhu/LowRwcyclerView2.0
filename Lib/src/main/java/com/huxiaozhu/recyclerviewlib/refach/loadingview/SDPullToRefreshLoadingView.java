package com.huxiaozhu.recyclerviewlib.refach.loadingview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huxiaozhu.recyclerviewlib.refach.ISDPullToRefreshView;
import com.huxiaozhu.recyclerviewlib.refach.SDPullToRefreshView;

import java.lang.reflect.Constructor;

public abstract class SDPullToRefreshLoadingView extends FrameLayout implements ISDPullToRefreshView.IPullToRefreshLoadingView {
    public SDPullToRefreshLoadingView(@NonNull Context context) {
        super(context);
    }

    public SDPullToRefreshLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SDPullToRefreshLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final ISDPullToRefreshView.LoadingViewType getLoadingViewType() {
        if (this.getPullToRefreshView().getHeaderView() == this) {
            return ISDPullToRefreshView.LoadingViewType.HEADER;
        } else {
            return this.getPullToRefreshView().getFooterView() == this ? ISDPullToRefreshView.LoadingViewType.FOOTER : null;
        }
    }

    public final SDPullToRefreshView getPullToRefreshView() {
        return (SDPullToRefreshView)this.getParent();
    }

    public void onViewPositionChanged(SDPullToRefreshView view) {
    }

    public boolean canRefresh(int scrollDistance) {
        return scrollDistance >= this.getMeasuredHeight();
    }

    public int getRefreshHeight() {
        return this.getMeasuredHeight();
    }

    public static SDPullToRefreshLoadingView getInstanceByClassName(String className, Context context) {
        if (!TextUtils.isEmpty(className) && context != null) {
            try {
                Class clazz = Class.forName(className);
                Constructor constructor = clazz.getConstructor(Context.class);
                return (SDPullToRefreshLoadingView)constructor.newInstance(context);
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}