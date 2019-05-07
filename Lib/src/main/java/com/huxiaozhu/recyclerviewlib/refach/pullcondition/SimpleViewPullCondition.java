package com.huxiaozhu.recyclerviewlib.refach.pullcondition;

import android.view.View;

import androidx.core.view.ViewCompat;

import com.huxiaozhu.recyclerviewlib.refach.IPullToRefreshView;

import java.lang.ref.WeakReference;

public class SimpleViewPullCondition implements IPullToRefreshView.IPullCondition {
    private WeakReference<View> mView;

    public SimpleViewPullCondition(View view) {
        this.mView = new WeakReference(view);
    }

    public View getView() {
        return this.mView != null ? (View)this.mView.get() : null;
    }

    public boolean canPullFromHeader() {
        if (this.getView() == null) {
            return true;
        } else {
            return !ViewCompat.canScrollVertically(this.getView(), -1);
        }
    }

    public boolean canPullFromFooter() {
        if (this.getView() == null) {
            return true;
        } else {
            return !ViewCompat.canScrollVertically(this.getView(), 1);
        }
    }
}
