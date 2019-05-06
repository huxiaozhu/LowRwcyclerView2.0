package com.huxiaozhu.recyclerviewlib.refach;

import android.view.View;

import com.huxiaozhu.recyclerviewlib.refach.loadingview.SDPullToRefreshLoadingView;

public interface ISDPullToRefreshView {
    float DEFAULT_COMSUME_SCROLL_PERCENT = 0.5F;
    int DEFAULT_DURATION_SHOW_REFRESH_RESULT = 600;

    void setMode(ISDPullToRefreshView.Mode var1);

    void setOnRefreshCallback(ISDPullToRefreshView.OnRefreshCallback var1);

    void setOnStateChangedCallback(ISDPullToRefreshView.OnStateChangedCallback var1);

    void setOnViewPositionChangedCallback(ISDPullToRefreshView.OnViewPositionChangedCallback var1);

    void setPullCondition(ISDPullToRefreshView.IPullCondition var1);

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

    ISDPullToRefreshView.State getState();

    SDPullToRefreshLoadingView getHeaderView();

    void setHeaderView(SDPullToRefreshLoadingView var1);

    SDPullToRefreshLoadingView getFooterView();

    void setFooterView(SDPullToRefreshLoadingView var1);

    View getRefreshView();

    ISDPullToRefreshView.Direction getDirection();

    int getScrollDistance();

    interface IPullToRefreshLoadingView extends ISDPullToRefreshView.OnStateChangedCallback, ISDPullToRefreshView.OnViewPositionChangedCallback {
        int getRefreshHeight();

        boolean canRefresh(int var1);

        ISDPullToRefreshView.LoadingViewType getLoadingViewType();

        SDPullToRefreshView getPullToRefreshView();
    }

    interface IPullCondition {
        boolean canPullFromHeader();

        boolean canPullFromFooter();
    }

    interface OnViewPositionChangedCallback {
        void onViewPositionChanged(SDPullToRefreshView var1);
    }

    interface OnRefreshCallback {
        void onRefreshingFromHeader(SDPullToRefreshView var1);

        void onRefreshingFromFooter(SDPullToRefreshView var1);
    }

    interface OnStateChangedCallback {
        void onStateChanged(ISDPullToRefreshView.State var1, ISDPullToRefreshView.State var2, SDPullToRefreshView var3);
    }

    enum LoadingViewType {
        HEADER,
        FOOTER;

        LoadingViewType() {
        }
    }

    enum Mode {
        BOTH,
        PULL_FROM_HEADER,
        PULL_FROM_FOOTER,
        DISABLE;

        Mode() {
        }
    }

    enum Direction {
        NONE,
        FROM_HEADER,
        FROM_FOOTER;

        Direction() {
        }
    }

    enum State {
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        REFRESH_SUCCESS,
        REFRESH_FAILURE,
        REFRESH_FINISH;

        State() {
        }
    }
}
