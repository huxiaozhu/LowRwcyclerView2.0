package com.huxiaozhu.recyclerviewlib.refach;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.huxiaozhu.recyclerviewlib.R;
import com.huxiaozhu.recyclerviewlib.refach.enmu.Direction;
import com.huxiaozhu.recyclerviewlib.refach.enmu.Mode;
import com.huxiaozhu.recyclerviewlib.refach.enmu.State;
import com.huxiaozhu.recyclerviewlib.refach.loadingview.PullToRefreshLoadingView;
import com.huxiaozhu.recyclerviewlib.refach.loadingview.SimpleTextLoadingView;

public class PullToRefreshView extends ViewGroup implements IPullToRefreshView {
    private PullToRefreshLoadingView mHeaderView;
    private PullToRefreshLoadingView mFooterView;
    private View mRefreshView;
    private Mode mMode;
    private State mState;
    private Direction mDirection;
    private Direction mLastDirection;
    private boolean mCheckDragDegree;
    private boolean mIsOverLayMode;
    private float mComsumeScrollPercent;
    private int mDurationShowRefreshResult;
    private ViewDragHelper mViewDragHelper;
    private boolean mHasOnLayout;
    private Runnable mUpdatePositionRunnable;
    private OnRefreshCallback mOnRefreshCallback;
    private OnStateChangedCallback mOnStateChangedCallback;
    private OnViewPositionChangedCallback mOnViewPositionChangedCallback;
    private IPullCondition mPullCondition;
    private final LogUtils mLogUtils;
    private TouchHelper mTouchHelper;
    private Runnable mStopRefreshingRunnable;

    public PullToRefreshView(@NonNull Context context) {
        this(context, null);
    }

    public PullToRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public PullToRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMode = Mode.BOTH;
        mState = State.RESET;
        mDirection = Direction.NONE;
        mLastDirection = Direction.NONE;
        mCheckDragDegree = true;
        mIsOverLayMode = false;
        mComsumeScrollPercent = 0.5F;
        mDurationShowRefreshResult = 600;
        mHasOnLayout = false;
        mLogUtils = new LogUtils();
        mTouchHelper = new TouchHelper();
        mStopRefreshingRunnable = new Runnable() {
            @Override
            public void run() {
                stopRefreshing();
            }
        };
        initInternal(attrs);
    }

    private void initInternal(AttributeSet attrs) {
        this.addLoadingViews();
        this.initViewDragHelper();
    }

    private void initViewDragHelper() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mLogUtils.i("ViewDragHelper onViewCaptured----------");
            }

            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                mLogUtils.i("ViewDragHelper onViewReleased");
                if (mState == State.RELEASE_TO_REFRESH) {
                    setState(State.REFRESHING);
                }

                smoothScrollViewByState();
            }

            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                if (state == 0) {
                    switch (mState) {
                        case REFRESHING:
                            notifyRefreshCallback();
                            break;
                        case PULL_TO_REFRESH:
                        case REFRESH_FINISH:
                            setState(State.RESET);
                    }
                }

            }

            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return child.getLeft();
            }

            public int clampViewPositionVertical(View child, int top, int dy) {
                int dyConsume = getComsumedDistance((float) dy);
                int topConsume = top - dyConsume;
                int result = child.getTop();
                if (child == mHeaderView) {
                    result = Math.max(getTopHeaderViewReset(), topConsume);
                } else if (child == mFooterView) {
                    result = Math.min(getTopFooterViewReset(), topConsume);
                }
                return result;
            }

            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (mViewDragHelper.getViewDragState() == 1) {
                    updateStateByMoveDistance();
                }
                moveViews(dy);
            }
        });
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public void setMode(Mode mode) {
        if (mode != null && mMode != mode) {
            mMode = mode;
        }

    }

    public void setOnRefreshCallback(OnRefreshCallback onRefreshCallback) {
        mOnRefreshCallback = onRefreshCallback;
    }

    public void setOnStateChangedCallback(OnStateChangedCallback onStateChangedCallback) {
        mOnStateChangedCallback = onStateChangedCallback;
    }

    public void setOnViewPositionChangedCallback(OnViewPositionChangedCallback onViewPositionChangedCallback) {
        mOnViewPositionChangedCallback = onViewPositionChangedCallback;
    }

    public void setPullCondition(IPullCondition pullCondition) {
        mPullCondition = pullCondition;
    }

    public void setOverLayMode(boolean overLayMode) {
        if (mViewDragHelper.getViewDragState() == 0 && mState == State.RESET) {
            mIsOverLayMode = overLayMode;
        }

    }

    public boolean isOverLayMode() {
        return this.mIsOverLayMode;
    }

    public void setComsumeScrollPercent(float comsumeScrollPercent) {
        if (comsumeScrollPercent < 0.0F) {
            comsumeScrollPercent = 0.0F;
        }

        if (comsumeScrollPercent > 1.0F) {
            comsumeScrollPercent = 1.0F;
        }

        this.mComsumeScrollPercent = comsumeScrollPercent;
    }

    public void setDurationShowRefreshResult(int durationShowRefreshResult) {
        if (durationShowRefreshResult < 0) {
            durationShowRefreshResult = 600;
        }

        this.mDurationShowRefreshResult = durationShowRefreshResult;
    }

    public void setCheckDragDegree(boolean checkDragDegree) {
        this.mCheckDragDegree = checkDragDegree;
    }

    public void startRefreshingFromHeader() {
        if (mMode != Mode.DISABLE) {
            if (mState == State.RESET) {
                setDirection(Direction.FROM_HEADER);
                setState(State.REFRESHING);
                smoothScrollViewByState();
            }

        }
    }

    public void startRefreshingFromFooter() {
        if (mMode != Mode.DISABLE) {
            if (mState == State.RESET) {
                setDirection(Direction.FROM_FOOTER);
                setState(State.REFRESHING);
                smoothScrollViewByState();
            }

        }
    }

    public void stopRefreshing() {
        if (mState != State.RESET && mState != State.REFRESH_FINISH) {
            setState(State.REFRESH_FINISH);
            smoothScrollViewByState();
        }

    }

    public void stopRefreshingWithResult(boolean success) {
        if (mState == State.REFRESHING) {
            if (success) {
                setState(State.REFRESH_SUCCESS);
            } else {
                setState(State.REFRESH_FAILURE);
            }
        }

    }

    public boolean isRefreshing() {
        return mState == State.REFRESHING;
    }

    public State getState() {
        return mState;
    }

    public PullToRefreshLoadingView getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(PullToRefreshLoadingView headerView) {
        if (headerView != null && headerView != mHeaderView) {
            removeView(mHeaderView);
            mHeaderView = headerView;
            addView(mHeaderView);
        }
    }

    public PullToRefreshLoadingView getFooterView() {
        return mFooterView;
    }

    public void setFooterView(PullToRefreshLoadingView footerView) {
        if (footerView != null && footerView != mFooterView) {
            removeView(mFooterView);
            mFooterView = footerView;
            addView(mFooterView);
        }
    }

    public View getRefreshView() {
        return mRefreshView;
    }

    public Direction getDirection() {
        return mLastDirection;
    }

    public int getScrollDistance() {
        return getDirection() == Direction.FROM_HEADER ? mHeaderView.getTop() -getTopHeaderViewReset() : mFooterView.getTop() - getTopFooterViewReset();
    }

    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            if (mLogUtils.isDebug()) {
                int top;
                if (getDirection() == Direction.FROM_HEADER) {
                    top = mHeaderView.getTop();
                } else {
                    top = mFooterView.getTop();
                }

                mLogUtils.i("computeScroll:" + top + " " + mState);
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.mLogUtils.i("computeScroll finish:" + mState);
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMode != Mode.DISABLE && !isRefreshing()) {
            if (mTouchHelper.isNeedIntercept()) {
                return true;
            } else {
                mTouchHelper.processTouchEvent(ev);
                switch (ev.getAction()) {
                    case 0:
                        mTouchHelper.setNeedIntercept(false);
                        TouchHelper.requestDisallowInterceptTouchEvent(this, false);
                        mViewDragHelper.processTouchEvent(ev);
                        break;
                    case 2:
                        if (canPull()) {
                            mTouchHelper.setNeedIntercept(true);
                            TouchHelper.requestDisallowInterceptTouchEvent(this, true);
                        }
                }

                return mTouchHelper.isNeedIntercept();
            }
        } else {
            return false;
        }
    }

    private boolean checkMoveParams() {
        return !mCheckDragDegree || mTouchHelper.getDegreeYFrom(0) < 40.0D;
    }

    private boolean isViewReset() {
        if (mViewDragHelper.getViewDragState() != 0) {
            return false;
        } else {
            return mState == State.RESET;
        }
    }

    private boolean canPull() {
        return checkMoveParams() && (canPullFromHeader() || canPullFromFooter()) && isViewReset();
    }

    private boolean canPullFromHeader() {
        return mTouchHelper.isMoveBottomFrom(0) && (mMode == Mode.BOTH ||mMode == Mode.PULL_FROM_HEADER)
                && TouchHelper.isScrollToTop(mRefreshView) && (mPullCondition == null || mPullCondition.canPullFromHeader());
    }

    private boolean canPullFromFooter() {
        return mTouchHelper.isMoveTopFrom(0) && (mMode == Mode.BOTH || mMode == Mode.PULL_FROM_FOOTER)
                && TouchHelper.isScrollToBottom(mRefreshView) && (mPullCondition == null || mPullCondition.canPullFromFooter());
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mMode != Mode.DISABLE && !isRefreshing()) {
            mTouchHelper.processTouchEvent(event);
            switch (event.getAction()) {
                case 1:
                case 3:
                    mViewDragHelper.processTouchEvent(event);
                    mTouchHelper.setNeedCosume(false);
                    mTouchHelper.setNeedIntercept(false);
                    TouchHelper.requestDisallowInterceptTouchEvent(this, false);
                    break;
                case 2:
                    if (mTouchHelper.isNeedCosume()) {
                        processMoveEvent(event);
                    } else if (!mTouchHelper.isNeedIntercept() && !canPull()) {
                        mTouchHelper.setNeedCosume(false);
                        mTouchHelper.setNeedIntercept(false);
                        TouchHelper.requestDisallowInterceptTouchEvent(this, false);
                    } else {
                        mTouchHelper.setNeedCosume(true);
                        mTouchHelper.setNeedIntercept(true);
                        TouchHelper.requestDisallowInterceptTouchEvent(this, true);
                    }
                    break;
                default:
                    mViewDragHelper.processTouchEvent(event);
            }

            return mTouchHelper.isNeedCosume() || event.getAction() == 0;
        } else {
            return false;
        }
    }

    private void processMoveEvent(MotionEvent event) {
        if (mTouchHelper.isMoveBottomFrom(0)) {
            setDirection(Direction.FROM_HEADER);
        } else if (mTouchHelper.isMoveTopFrom(0)) {
            setDirection(Direction.FROM_FOOTER);
        }

        if (getDirection() == Direction.FROM_HEADER) {
            if (mViewDragHelper.getCapturedView() != mHeaderView) {
                mViewDragHelper.captureChildView(mHeaderView, event.getPointerId(event.getActionIndex()));
            }
        } else if (getDirection() == Direction.FROM_FOOTER && mViewDragHelper.getCapturedView() != mFooterView) {
            mViewDragHelper.captureChildView(mFooterView, event.getPointerId(event.getActionIndex()));
        }

        mViewDragHelper.processTouchEvent(event);
    }

    private void moveViews(int dy) {
        if (getDirection() == Direction.FROM_HEADER) {
            if (mIsOverLayMode) {
                if (ViewCompat.getZ(mHeaderView) <= ViewCompat.getZ(mRefreshView)) {
                    ViewCompat.setZ(mHeaderView, ViewCompat.getZ(mRefreshView) + 1.0F);
                }
            } else {
                ViewCompat.offsetTopAndBottom(mRefreshView, dy);
            }

            this.mHeaderView.onViewPositionChanged(this);
        } else {
            if (mIsOverLayMode) {
                if (ViewCompat.getZ(mFooterView) <= ViewCompat.getZ(mRefreshView)) {
                    ViewCompat.setZ(mFooterView, ViewCompat.getZ(mRefreshView) + 1.0F);
                }
            } else {
                ViewCompat.offsetTopAndBottom(mRefreshView, dy);
            }

            mFooterView.onViewPositionChanged(this);
        }

        if (mOnViewPositionChangedCallback != null) {
            mOnViewPositionChangedCallback.onViewPositionChanged(this);
        }

    }

    private void updateStateByMoveDistance() {
        int distance = Math.abs(getScrollDistance());
        if (getDirection() == Direction.FROM_HEADER) {
            if (mHeaderView.canRefresh(distance)) {
                setState(State.RELEASE_TO_REFRESH);
            } else {
                setState(State.PULL_TO_REFRESH);
            }
        } else if (mFooterView.canRefresh(distance)) {
            setState(State.RELEASE_TO_REFRESH);
        } else {
            setState(State.PULL_TO_REFRESH);
        }

    }

    private void setState(State state) {
        if (mState != state) {
            State oldState =mState;
            mState = state;
            if (mLogUtils.isDebug()) {
                if (mState == State.RESET) {
                    mLogUtils.e("setState:" + mState);
                } else {
                    mLogUtils.i("setState:" + mState);
                }
            }

            removeCallbacks(mStopRefreshingRunnable);
            if (mState == State.REFRESH_SUCCESS || mState == State.REFRESH_FAILURE) {
                postDelayed(mStopRefreshingRunnable, (long) mDurationShowRefreshResult);
            }

            if (getDirection() == Direction.FROM_HEADER) {
                mHeaderView.onStateChanged(mState, oldState, this);
            } else {
                mFooterView.onStateChanged(mState, oldState, this);
            }

            if (mOnStateChangedCallback != null) {
                mOnStateChangedCallback.onStateChanged(mState, oldState, this);
            }

            if (mState == State.RESET) {
                requestLayoutIfNeed();
                setDirection(Direction.NONE);
            }

        }
    }

    private void notifyRefreshCallback() {
        mLogUtils.i("notifyRefreshCallback");
        if (mOnRefreshCallback != null) {
            if (getDirection() == Direction.FROM_HEADER) {
                mOnRefreshCallback.onRefreshingFromHeader(this);
            } else {
                mOnRefreshCallback.onRefreshingFromFooter(this);
            }
        }

    }

    private void requestLayoutIfNeed() {
        boolean needRequestLayout = false;
        if (getDirection() == Direction.FROM_HEADER) {
            if (mHeaderView.getTop() != getTopHeaderViewReset()) {
                needRequestLayout = true;
            }
        } else if (getDirection() == Direction.FROM_FOOTER && mFooterView.getTop() != getTopFooterViewReset()) {
            needRequestLayout = true;
        }

        if (needRequestLayout) {
            mLogUtils.i("requestLayout when reset");
            requestLayout();
        }

    }

    private void smoothScrollViewByState() {
        if (mHasOnLayout) {
            smoothScrollViewByStateReal();
        } else {
            mUpdatePositionRunnable = new Runnable() {
                public void run() {
                    smoothScrollViewByStateReal();
                    mUpdatePositionRunnable = null;
                }
            };
        }

    }

    private int getTopAlignTop() {
        return getPaddingTop();
    }

    private int getTopAlignBottom() {
        return getHeight() - getPaddingBottom();
    }

    private int getTopHeaderViewReset() {
        return getTopAlignTop() - mHeaderView.getMeasuredHeight();
    }

    private int getTopFooterViewReset() {
        return getTopAlignBottom();
    }

    private void smoothScrollViewByStateReal() {
        View view = null;
        boolean smoothScrollViewStarted = false;
        int endY;
        switch (mState) {
            case REFRESHING:
            case RELEASE_TO_REFRESH:
                if (getDirection() == Direction.FROM_HEADER) {
                    view = mHeaderView;
                    endY = getTopHeaderViewReset() + mHeaderView.getRefreshHeight();
                } else {
                    view = mFooterView;
                    endY = getTopFooterViewReset() - mFooterView.getRefreshHeight();
                }

                if (mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), endY)) {
                    mLogUtils.i("smoothScrollViewByState:" + view.getTop() + "," + endY + " " + mState);
                    smoothScrollViewStarted = true;
                    invalidate();
                }
                break;
            case PULL_TO_REFRESH:
            case REFRESH_FINISH:
            case RESET:
                if (getDirection() == Direction.FROM_HEADER) {
                    view = mHeaderView;
                    endY = getTopHeaderViewReset();
                } else {
                    view = mFooterView;
                    endY = getTopFooterViewReset();
                }
                if (mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), endY)) {
                    mLogUtils.i("smoothScrollViewByState:" + view.getTop() + "," + endY + " " + this.mState);
                    smoothScrollViewStarted = true;
                    invalidate();
                }
        }

        if (mState == State.REFRESHING && !smoothScrollViewStarted) {
            notifyRefreshCallback();
        }

    }

    private void setDirection(Direction direction) {
        if (mDirection != direction) {
            if (direction != Direction.NONE) {
                if (mDirection == Direction.NONE) {
                    mDirection = direction;
                    mLastDirection = direction;
                    mLogUtils.i("setDirection:" + mDirection);
                }
            } else {
                mDirection = Direction.NONE;
                mLogUtils.i("setDirection:" + mDirection);
            }

        }
    }

    private int getComsumedDistance(float distance) {
        distance -= distance * mComsumeScrollPercent;
        return (int) distance;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount =getChildCount();
        if (childCount < 3) {
            throw new IllegalArgumentException("you must add one child to SDPullToRefreshView in your xml file");
        } else if (childCount > 3) {
            throw new IllegalArgumentException("you can only add one child to SDPullToRefreshView in your xml file");
        } else {
           mRefreshView = getChildAt(2);
        }
    }

    private void addLoadingViews() {
        PullToRefreshLoadingView headerView = onCreateHeaderView();
        if (headerView == null) {
            String headerClassName = this.getResources().getString(R.string.lib_ptr_header_class);
            if (!TextUtils.isEmpty(headerClassName)) {
                headerView = PullToRefreshLoadingView.getInstanceByClassName(headerClassName, getContext());
            }
        }

        if (headerView == null) {
            headerView = new SimpleTextLoadingView(getContext());
        }

        this.setHeaderView(headerView);
        PullToRefreshLoadingView footerView = this.onCreateFooterView();
        if (footerView == null) {
            String footerClassName = this.getResources().getString(R.string.lib_ptr_footer_class);
            if (footerClassName != null) {
                footerView = PullToRefreshLoadingView.getInstanceByClassName(footerClassName, getContext());
            }
        }

        if (footerView == null) {
            footerView = new SimpleTextLoadingView(getContext());
        }

        this.setFooterView(footerView);
    }

    protected PullToRefreshLoadingView onCreateHeaderView() {
        return null;
    }

    protected PullToRefreshLoadingView onCreateFooterView() {
        return null;
    }

    private int getMinWidthInternal() {
        return Build.VERSION.SDK_INT >= 16 ? this.getMinimumWidth() : 0;
    }

    private int getMinHeightInternal() {
        return Build.VERSION.SDK_INT >= 16 ? this.getMinimumHeight() : 0;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        boolean needReMeasure = false;
        int widthMeasureSpecLoadingView = widthMeasureSpec;
        if (widthMode != View.MeasureSpec.EXACTLY) {
            widthMeasureSpecLoadingView = MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.UNSPECIFIED);
            needReMeasure = true;
        }

        int heightMeasureSpecLoadingView = MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED);
        this.measureLoadingView(mHeaderView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
        this.measureLoadingView(mFooterView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
        this.measureChild(mRefreshView, widthMeasureSpec, heightMeasureSpec);
        int maxHeight;
        if (widthMode != View.MeasureSpec.EXACTLY) {
            maxHeight = Math.max(mHeaderView.getMeasuredWidth(),mFooterView.getMeasuredWidth());
            maxHeight = Math.max(maxHeight,mRefreshView.getMeasuredWidth());
            maxHeight += getPaddingLeft() + getPaddingRight();
            maxHeight = Math.max(maxHeight, getMinWidthInternal());
            if (widthMode == 0) {
                width = maxHeight;
            } else if (widthMode == View.MeasureSpec.AT_MOST) {
                width = Math.min(maxHeight, width);
            }
        }

        if (heightMode != View.MeasureSpec.EXACTLY) {
            maxHeight = mRefreshView.getMeasuredHeight();
            if (maxHeight == 0) {
                maxHeight = Math.max(mHeaderView.getMeasuredHeight(), mFooterView.getMeasuredHeight());
            }

            maxHeight += getPaddingTop() + getPaddingBottom();
            maxHeight = Math.max(maxHeight, getMinHeightInternal());
            if (heightMode == 0) {
                height = maxHeight;
            } else if (heightMode == View.MeasureSpec.AT_MOST) {
                height = Math.min(maxHeight, height);
            }
        }

        if (needReMeasure) {
            widthMeasureSpecLoadingView = MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            this.measureLoadingView(mHeaderView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
            this.measureLoadingView(mFooterView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
        }

        this.setMeasuredDimension(width, height);
    }

    private void measureLoadingView(View loadingView, int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams params = loadingView.getLayoutParams();
        loadingView.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width),
                getChildMeasureSpec(heightMeasureSpec, 0, params.height));
    }

    private int getTopLayoutHeaderView() {
        int top = getTopHeaderViewReset();
        if (getDirection() == Direction.FROM_HEADER) {
            if (mViewDragHelper.getViewDragState() == 0) {
                switch (mState) {
                    case REFRESHING:
                    case REFRESH_SUCCESS:
                    case REFRESH_FAILURE:
                        top += mHeaderView.getRefreshHeight();
                }
            } else {
                top = mHeaderView.getTop();
            }
        }
        return top;
    }

    private int getTopLayoutFooterView() {
        int top = getTopFooterViewReset();
        if (getDirection() == Direction.FROM_FOOTER) {
            if (mViewDragHelper.getViewDragState() == 0) {
                switch (mState) {
                    case REFRESHING:
                    case REFRESH_SUCCESS:
                    case REFRESH_FAILURE:
                        top -= mFooterView.getRefreshHeight();
                }
            } else {
                top = mFooterView.getTop();
            }
        }

        return top;
    }

    private int getTopLayoutRefreshView() {
        int top = getTopAlignTop();
        if (!mIsOverLayMode) {
            if (mViewDragHelper.getViewDragState() == 0) {
                switch (mState) {
                    case REFRESHING:
                    case REFRESH_SUCCESS:
                    case REFRESH_FAILURE:
                        if (getDirection() == Direction.FROM_HEADER) {
                            top += mHeaderView.getRefreshHeight();
                        } else if (getDirection() == Direction.FROM_FOOTER) {
                            top -= mFooterView.getRefreshHeight();
                        }
                }
            } else {
                top = mRefreshView.getTop();
            }
        }

        return top;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left;
        if (mLogUtils.isDebug()) {
            left = mViewDragHelper.getViewDragState();
            if (left == 0) {
               mLogUtils.i("onLayout " + left + " totalHeight:----------" + getHeight());
            } else {
               mLogUtils.e("onLayout " + left + " totalHeight:----------" + getHeight());
            }
        }

        left = getPaddingLeft();
        int top = getTopLayoutHeaderView();
        int right = left + mHeaderView.getMeasuredWidth();
        int bottom = top + mHeaderView.getMeasuredHeight();
        this.mHeaderView.layout(left, top, right, bottom);
        this.mLogUtils.i("HeaderView:" + top + "," + bottom);
        top = getTopLayoutRefreshView();
        if (!mIsOverLayMode && getDirection() == Direction.FROM_HEADER && bottom > top) {
            top = bottom;
        }

        right = left + mRefreshView.getMeasuredWidth();
        bottom = top + mRefreshView.getMeasuredHeight();
        mRefreshView.layout(left, top, right, bottom);
        mLogUtils.i("RefreshView:" + top + "," + bottom);
        top = getTopLayoutFooterView();
        if (!mIsOverLayMode && bottom <= getTopAlignBottom() && bottom > top) {
            top = bottom;
        }

        right = left + mFooterView.getMeasuredWidth();
        bottom = top + mFooterView.getMeasuredHeight();
        mFooterView.layout(left, top, right, bottom);
        mLogUtils.i("FooterView:" + top + "," + bottom);
        mHasOnLayout = true;
        runUpdatePositionRunnableIfNeed();
    }

    private void runUpdatePositionRunnableIfNeed() {
        if (mHasOnLayout && mUpdatePositionRunnable != null) {
            post(mUpdatePositionRunnable);
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mStopRefreshingRunnable);
        mHasOnLayout = false;
        mUpdatePositionRunnable = null;
        mViewDragHelper.abort();
    }
}
