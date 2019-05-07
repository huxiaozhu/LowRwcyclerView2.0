package com.huxiaozhu.recyclerviewlib.refach;

import android.view.View;

import com.huxiaozhu.recyclerviewlib.refach.enmu.Direction;
import com.huxiaozhu.recyclerviewlib.refach.enmu.LoadingViewType;
import com.huxiaozhu.recyclerviewlib.refach.enmu.Mode;
import com.huxiaozhu.recyclerviewlib.refach.enmu.State;
import com.huxiaozhu.recyclerviewlib.refach.loadingview.PullToRefreshLoadingView;

public interface IPullToRefreshView {
    float DEFAULT_COMSUME_SCROLL_PERCENT = 0.5F;
    int DEFAULT_DURATION_SHOW_REFRESH_RESULT = 600;

    void setMode(Mode var1);

    void setOnRefreshCallback(IPullToRefreshView.OnRefreshCallback var1);

    void setOnStateChangedCallback(IPullToRefreshView.OnStateChangedCallback var1);

    void setOnViewPositionChangedCallback(IPullToRefreshView.OnViewPositionChangedCallback var1);

    void setPullCondition(IPullToRefreshView.IPullCondition var1);

    void setOverLayMode(boolean var1);

    boolean isOverLayMode();

    void setComsumeScrollPercent(float var1);

    void setDurationShowRefreshResult(int var1);

    void setCheckDragDegree(boolean var1);

    void startRefreshingFromHeader();

    void startRefreshingFromFooter();

    void stopRefreshing();

    void stopRefreshingWithResult(boolean var1);

    boolean isRefreshing();

    State getState();

    PullToRefreshLoadingView getHeaderView();

    void setHeaderView(PullToRefreshLoadingView var1);

    PullToRefreshLoadingView getFooterView();

    void setFooterView(PullToRefreshLoadingView var1);

    View getRefreshView();

    Direction getDirection();

    int getScrollDistance();

    interface IPullToRefreshLoadingView extends IPullToRefreshView.OnStateChangedCallback, IPullToRefreshView.OnViewPositionChangedCallback {
        int getRefreshHeight();

        boolean canRefresh(int var1);

        LoadingViewType getLoadingViewType();

        PullToRefreshView getPullToRefreshView();
    }

    interface IPullCondition {
        boolean canPullFromHeader();

        boolean canPullFromFooter();
    }

    interface OnViewPositionChangedCallback {
        void onViewPositionChanged(PullToRefreshView var1);
    }

    interface OnRefreshCallback {
        void onRefreshingFromHeader(PullToRefreshView var1);

        void onRefreshingFromFooter(PullToRefreshView var1);
    }

    interface OnStateChangedCallback {
        void onStateChanged(State var1, State var2, PullToRefreshView var3);
    }
}
