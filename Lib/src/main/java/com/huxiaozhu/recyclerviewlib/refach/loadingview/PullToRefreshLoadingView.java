package com.huxiaozhu.recyclerviewlib.refach.loadingview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huxiaozhu.recyclerviewlib.refach.IPullToRefreshView;
import com.huxiaozhu.recyclerviewlib.refach.PullToRefreshView;
import com.huxiaozhu.recyclerviewlib.refach.enmu.LoadingViewType;

import java.lang.reflect.Constructor;

public abstract class PullToRefreshLoadingView extends FrameLayout implements IPullToRefreshView.IPullToRefreshLoadingView {
    public PullToRefreshLoadingView(@NonNull Context context) {
        super(context);
    }

    public PullToRefreshLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final LoadingViewType getLoadingViewType() {
        if (this.getPullToRefreshView().getHeaderView() == this) {
            return LoadingViewType.HEADER;
        } else {
            return this.getPullToRefreshView().getFooterView() == this ? LoadingViewType.FOOTER : null;
        }
    }

    public final PullToRefreshView getPullToRefreshView() {
        return (PullToRefreshView)this.getParent();
    }

    public void onViewPositionChanged(PullToRefreshView view) {
    }

    public boolean canRefresh(int scrollDistance) {
        return scrollDistance >= this.getMeasuredHeight();
    }

    public int getRefreshHeight() {
        return this.getMeasuredHeight();
    }

    public static PullToRefreshLoadingView getInstanceByClassName(String className, Context context) {
        if (!TextUtils.isEmpty(className) && context != null) {
            try {
                Class clazz = Class.forName(className);
                Constructor constructor = clazz.getConstructor(Context.class);
                return (PullToRefreshLoadingView)constructor.newInstance(context);
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}