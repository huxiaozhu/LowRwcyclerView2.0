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
import com.huxiaozhu.recyclerviewlib.refach.loadingview.SDPullToRefreshLoadingView;
import com.huxiaozhu.recyclerviewlib.refach.loadingview.SimpleTextLoadingView;

public class SDPullToRefreshView extends ViewGroup implements ISDPullToRefreshView {
    private static final String TAG = "SDPullToRefreshView";
    private SDPullToRefreshLoadingView mHeaderView;
    private SDPullToRefreshLoadingView mFooterView;
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
    private SDTouchHelper mTouchHelper;
    private Runnable mStopRefreshingRunnable;

    public SDPullToRefreshView(@NonNull Context context) {
        super(context);
        this.mMode = Mode.BOTH;
        this.mState = State.RESET;
        this.mDirection = Direction.NONE;
        this.mLastDirection = Direction.NONE;
        this.mCheckDragDegree = true;
        this.mIsOverLayMode = false;
        this.mComsumeScrollPercent = 0.5F;
        this.mDurationShowRefreshResult = 600;
        this.mHasOnLayout = false;
        this.mLogUtils = new LogUtils(SDPullToRefreshView.class);
        this.mTouchHelper = new SDTouchHelper();
        this.mStopRefreshingRunnable = new StopRefreshingRunnable();
        this.initInternal((AttributeSet) null);
    }

    public SDPullToRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mMode = Mode.BOTH;
        this.mState = State.RESET;
        this.mDirection = Direction.NONE;
        this.mLastDirection = Direction.NONE;
        this.mCheckDragDegree = true;
        this.mIsOverLayMode = false;
        this.mComsumeScrollPercent = 0.5F;
        this.mDurationShowRefreshResult = 600;
        this.mHasOnLayout = false;
        this.mLogUtils = new LogUtils(SDPullToRefreshView.class);
        this.mTouchHelper = new SDTouchHelper();
        this.mStopRefreshingRunnable = new StopRefreshingRunnable();
        this.initInternal(attrs);
    }

    public SDPullToRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMode = Mode.BOTH;
        this.mState = State.RESET;
        this.mDirection = Direction.NONE;
        this.mLastDirection = Direction.NONE;
        this.mCheckDragDegree = true;
        this.mIsOverLayMode = false;
        this.mComsumeScrollPercent = 0.5F;
        this.mDurationShowRefreshResult = 600;
        this.mHasOnLayout = false;
        this.mLogUtils = new LogUtils(SDPullToRefreshView.class);
        this.mTouchHelper = new SDTouchHelper();


        this.mStopRefreshingRunnable = new StopRefreshingRunnable();
        this.initInternal(attrs);
    }

    class StopRefreshingRunnable implements Runnable {
        StopRefreshingRunnable() {
        }

        public void run() {
            SDPullToRefreshView.this.stopRefreshing();
        }
    }

    private void initInternal(AttributeSet attrs) {
        this.addLoadingViews();
        this.initViewDragHelper();
    }

    public void setDebug(boolean debug) {
        this.mLogUtils.setDebug(debug);
        this.mTouchHelper.setDebug(debug);
    }

    public void setDebugTag(String debugTag) {
        this.mLogUtils.setDebugTag(debugTag);
    }

    private void initViewDragHelper() {
        this.mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
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
        if (mode != null && this.mMode != mode) {
            this.mMode = mode;
        }

    }

    public void setOnRefreshCallback(OnRefreshCallback onRefreshCallback) {
        this.mOnRefreshCallback = onRefreshCallback;
    }

    public void setOnStateChangedCallback(OnStateChangedCallback onStateChangedCallback) {
        this.mOnStateChangedCallback = onStateChangedCallback;
    }

    public void setOnViewPositionChangedCallback(OnViewPositionChangedCallback onViewPositionChangedCallback) {
        this.mOnViewPositionChangedCallback = onViewPositionChangedCallback;
    }

    public void setPullCondition(IPullCondition pullCondition) {
        this.mPullCondition = pullCondition;
    }

    public void setOverLayMode(boolean overLayMode) {
        if (this.mViewDragHelper.getViewDragState() == 0 && this.mState == State.RESET) {
            this.mIsOverLayMode = overLayMode;
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
        if (this.mMode != Mode.DISABLE) {
            if (this.mState == State.RESET) {
                this.setDirection(Direction.FROM_HEADER);
                this.setState(State.REFRESHING);
                this.smoothScrollViewByState();
            }

        }
    }

    public void startRefreshingFromFooter() {
        if (this.mMode != Mode.DISABLE) {
            if (this.mState == State.RESET) {
                this.setDirection(Direction.FROM_FOOTER);
                this.setState(State.REFRESHING);
                this.smoothScrollViewByState();
            }

        }
    }

    public void stopRefreshing() {
        if (this.mState != State.RESET && this.mState != State.REFRESH_FINISH) {
            this.setState(State.REFRESH_FINISH);
            this.smoothScrollViewByState();
        }

    }

    public void stopRefreshingWithResult(boolean success) {
        if (this.mState == State.REFRESHING) {
            if (success) {
                this.setState(State.REFRESH_SUCCESS);
            } else {
                this.setState(State.REFRESH_FAILURE);
            }
        }

    }

    public boolean isRefreshing() {
        return this.mState == State.REFRESHING;
    }

    public State getState() {
        return this.mState;
    }

    public SDPullToRefreshLoadingView getHeaderView() {
        return this.mHeaderView;
    }

    public void setHeaderView(SDPullToRefreshLoadingView headerView) {
        if (headerView != null && headerView != this.mHeaderView) {
            this.removeView(this.mHeaderView);
            this.mHeaderView = headerView;
            this.addView(this.mHeaderView);
        }
    }

    public SDPullToRefreshLoadingView getFooterView() {
        return this.mFooterView;
    }

    public void setFooterView(SDPullToRefreshLoadingView footerView) {
        if (footerView != null && footerView != this.mFooterView) {
            this.removeView(this.mFooterView);
            this.mFooterView = footerView;
            this.addView(this.mFooterView);
        }
    }

    public View getRefreshView() {
        return this.mRefreshView;
    }

    public Direction getDirection() {
        return this.mLastDirection;
    }

    public int getScrollDistance() {
        return this.getDirection() == Direction.FROM_HEADER ? this.mHeaderView.getTop() - this.getTopHeaderViewReset() : this.mFooterView.getTop() - this.getTopFooterViewReset();
    }

    public void computeScroll() {
        if (this.mViewDragHelper.continueSettling(true)) {
            if (this.mLogUtils.isDebug()) {
                int top;
                if (this.getDirection() == Direction.FROM_HEADER) {
                    top = this.mHeaderView.getTop();
                } else {
                    top = this.mFooterView.getTop();
                }

                this.mLogUtils.i("computeScroll:" + top + " " + this.mState);
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.mLogUtils.i("computeScroll finish:" + this.mState);
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mMode != Mode.DISABLE && !this.isRefreshing()) {
            if (this.mTouchHelper.isNeedIntercept()) {
                return true;
            } else {
                this.mTouchHelper.processTouchEvent(ev);
                switch (ev.getAction()) {
                    case 0:
                        this.mTouchHelper.setNeedIntercept(false);
                        SDTouchHelper.requestDisallowInterceptTouchEvent(this, false);
                        this.mViewDragHelper.processTouchEvent(ev);
                        break;
                    case 2:
                        if (this.canPull()) {
                            this.mTouchHelper.setNeedIntercept(true);
                            SDTouchHelper.requestDisallowInterceptTouchEvent(this, true);
                        }
                }

                return this.mTouchHelper.isNeedIntercept();
            }
        } else {
            return false;
        }
    }

    private boolean checkMoveParams() {
        return this.mCheckDragDegree ? this.mTouchHelper.getDegreeYFrom(0) < 40.0D : true;
    }

    private boolean isViewReset() {
        if (this.mViewDragHelper.getViewDragState() != 0) {
            return false;
        } else {
            return this.mState == State.RESET;
        }
    }

    private boolean canPull() {
        return this.checkMoveParams() && (this.canPullFromHeader() || this.canPullFromFooter()) && this.isViewReset();
    }

    private boolean canPullFromHeader() {
        return this.mTouchHelper.isMoveBottomFrom(0) && (this.mMode == Mode.BOTH || this.mMode == Mode.PULL_FROM_HEADER) && SDTouchHelper.isScrollToTop(this.mRefreshView) && (this.mPullCondition == null || this.mPullCondition.canPullFromHeader());
    }

    private boolean canPullFromFooter() {
        return this.mTouchHelper.isMoveTopFrom(0) && (this.mMode == Mode.BOTH || this.mMode == Mode.PULL_FROM_FOOTER) && SDTouchHelper.isScrollToBottom(this.mRefreshView) && (this.mPullCondition == null || this.mPullCondition.canPullFromFooter());
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mMode != Mode.DISABLE && !this.isRefreshing()) {
            this.mTouchHelper.processTouchEvent(event);
            switch (event.getAction()) {
                case 1:
                case 3:
                    this.mViewDragHelper.processTouchEvent(event);
                    this.mTouchHelper.setNeedCosume(false);
                    this.mTouchHelper.setNeedIntercept(false);
                    SDTouchHelper.requestDisallowInterceptTouchEvent(this, false);
                    break;
                case 2:
                    if (this.mTouchHelper.isNeedCosume()) {
                        this.processMoveEvent(event);
                    } else if (!this.mTouchHelper.isNeedIntercept() && !this.canPull()) {
                        this.mTouchHelper.setNeedCosume(false);
                        this.mTouchHelper.setNeedIntercept(false);
                        SDTouchHelper.requestDisallowInterceptTouchEvent(this, false);
                    } else {
                        this.mTouchHelper.setNeedCosume(true);
                        this.mTouchHelper.setNeedIntercept(true);
                        SDTouchHelper.requestDisallowInterceptTouchEvent(this, true);
                    }
                    break;
                default:
                    this.mViewDragHelper.processTouchEvent(event);
            }

            return this.mTouchHelper.isNeedCosume() || event.getAction() == 0;
        } else {
            return false;
        }
    }

    private void processMoveEvent(MotionEvent event) {
        if (this.mTouchHelper.isMoveBottomFrom(0)) {
            this.setDirection(Direction.FROM_HEADER);
        } else if (this.mTouchHelper.isMoveTopFrom(0)) {
            this.setDirection(Direction.FROM_FOOTER);
        }

        if (this.getDirection() == Direction.FROM_HEADER) {
            if (this.mViewDragHelper.getCapturedView() != this.mHeaderView) {
                this.mViewDragHelper.captureChildView(this.mHeaderView, event.getPointerId(event.getActionIndex()));
            }
        } else if (this.getDirection() == Direction.FROM_FOOTER && this.mViewDragHelper.getCapturedView() != this.mFooterView) {
            this.mViewDragHelper.captureChildView(this.mFooterView, event.getPointerId(event.getActionIndex()));
        }

        this.mViewDragHelper.processTouchEvent(event);
    }

    private void moveViews(int dy) {
        if (this.getDirection() == Direction.FROM_HEADER) {
            if (this.mIsOverLayMode) {
                if (ViewCompat.getZ(this.mHeaderView) <= ViewCompat.getZ(this.mRefreshView)) {
                    ViewCompat.setZ(this.mHeaderView, ViewCompat.getZ(this.mRefreshView) + 1.0F);
                }
            } else {
                ViewCompat.offsetTopAndBottom(this.mRefreshView, dy);
            }

            this.mHeaderView.onViewPositionChanged(this);
        } else {
            if (this.mIsOverLayMode) {
                if (ViewCompat.getZ(this.mFooterView) <= ViewCompat.getZ(this.mRefreshView)) {
                    ViewCompat.setZ(this.mFooterView, ViewCompat.getZ(this.mRefreshView) + 1.0F);
                }
            } else {
                ViewCompat.offsetTopAndBottom(this.mRefreshView, dy);
            }

            this.mFooterView.onViewPositionChanged(this);
        }

        if (this.mOnViewPositionChangedCallback != null) {
            this.mOnViewPositionChangedCallback.onViewPositionChanged(this);
        }

    }

    private void updateStateByMoveDistance() {
        int distance = Math.abs(this.getScrollDistance());
        if (this.getDirection() == Direction.FROM_HEADER) {
            if (this.mHeaderView.canRefresh(distance)) {
                this.setState(State.RELEASE_TO_REFRESH);
            } else {
                this.setState(State.PULL_TO_REFRESH);
            }
        } else if (this.mFooterView.canRefresh(distance)) {
            this.setState(State.RELEASE_TO_REFRESH);
        } else {
            this.setState(State.PULL_TO_REFRESH);
        }

    }

    private void setState(State state) {
        if (this.mState != state) {
            State oldState = this.mState;
            this.mState = state;
            if (this.mLogUtils.isDebug()) {
                if (this.mState == State.RESET) {
                    this.mLogUtils.e("setState:" + this.mState);
                } else {
                    this.mLogUtils.i("setState:" + this.mState);
                }
            }

            this.removeCallbacks(this.mStopRefreshingRunnable);
            if (this.mState == State.REFRESH_SUCCESS || this.mState == State.REFRESH_FAILURE) {
                this.postDelayed(this.mStopRefreshingRunnable, (long) this.mDurationShowRefreshResult);
            }

            if (this.getDirection() == Direction.FROM_HEADER) {
                this.mHeaderView.onStateChanged(this.mState, oldState, this);
            } else {
                this.mFooterView.onStateChanged(this.mState, oldState, this);
            }

            if (this.mOnStateChangedCallback != null) {
                this.mOnStateChangedCallback.onStateChanged(this.mState, oldState, this);
            }

            if (this.mState == State.RESET) {
                this.requestLayoutIfNeed();
                this.setDirection(Direction.NONE);
            }

        }
    }

    private void notifyRefreshCallback() {
        this.mLogUtils.i("notifyRefreshCallback");
        if (this.mOnRefreshCallback != null) {
            if (this.getDirection() == Direction.FROM_HEADER) {
                this.mOnRefreshCallback.onRefreshingFromHeader(this);
            } else {
                this.mOnRefreshCallback.onRefreshingFromFooter(this);
            }
        }

    }

    private void requestLayoutIfNeed() {
        boolean needRequestLayout = false;
        if (this.getDirection() == Direction.FROM_HEADER) {
            if (this.mHeaderView.getTop() != this.getTopHeaderViewReset()) {
                needRequestLayout = true;
            }
        } else if (this.getDirection() == Direction.FROM_FOOTER && this.mFooterView.getTop() != this.getTopFooterViewReset()) {
            needRequestLayout = true;
        }

        if (needRequestLayout) {
            this.mLogUtils.i("requestLayout when reset");
            this.requestLayout();
        }

    }

    private void smoothScrollViewByState() {
        if (this.mHasOnLayout) {
            this.smoothScrollViewByStateReal();
        } else {
            this.mUpdatePositionRunnable = new Runnable() {
                public void run() {
                    smoothScrollViewByStateReal();
                    mUpdatePositionRunnable = null;
                }
            };
        }

    }

    private int getTopAlignTop() {
        return this.getPaddingTop();
    }

    private int getTopAlignBottom() {
        return this.getHeight() - this.getPaddingBottom();
    }

    private int getTopHeaderViewReset() {
        return this.getTopAlignTop() - this.mHeaderView.getMeasuredHeight();
    }

    private int getTopFooterViewReset() {
        return this.getTopAlignBottom();
    }

    private void smoothScrollViewByStateReal() {
        View view = null;
        boolean smoothScrollViewStarted = false;
        int endY;
        switch (this.mState) {
            case REFRESHING:
            case RELEASE_TO_REFRESH:
                if (this.getDirection() == Direction.FROM_HEADER) {
                    view = this.mHeaderView;
                    endY = this.getTopHeaderViewReset() + this.mHeaderView.getRefreshHeight();
                } else {
                    view = this.mFooterView;
                    endY = this.getTopFooterViewReset() - this.mFooterView.getRefreshHeight();
                }

                if (this.mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), endY)) {
                    this.mLogUtils.i("smoothScrollViewByState:" + view.getTop() + "," + endY + " " + this.mState);
                    smoothScrollViewStarted = true;
                    this.invalidate();
                }
                break;
            case PULL_TO_REFRESH:
            case REFRESH_FINISH:
            case RESET:
                if (this.getDirection() == Direction.FROM_HEADER) {
                    view = this.mHeaderView;
                    endY = this.getTopHeaderViewReset();
                } else {
                    view = this.mFooterView;
                    endY = this.getTopFooterViewReset();
                }

                if (this.mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), endY)) {
                    this.mLogUtils.i("smoothScrollViewByState:" + view.getTop() + "," + endY + " " + this.mState);
                    smoothScrollViewStarted = true;
                    this.invalidate();
                }
        }

        if (this.mState == State.REFRESHING && !smoothScrollViewStarted) {
            this.notifyRefreshCallback();
        }

    }

    private void setDirection(Direction direction) {
        if (this.mDirection != direction) {
            if (direction != Direction.NONE) {
                if (this.mDirection == Direction.NONE) {
                    this.mDirection = direction;
                    this.mLastDirection = direction;
                    this.mLogUtils.i("setDirection:" + this.mDirection);
                }
            } else {
                this.mDirection = Direction.NONE;
                this.mLogUtils.i("setDirection:" + this.mDirection);
            }

        }
    }

    private int getComsumedDistance(float distance) {
        distance -= distance * this.mComsumeScrollPercent;
        return (int) distance;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = this.getChildCount();
        if (childCount < 3) {
            throw new IllegalArgumentException("you must add one child to SDPullToRefreshView in your xml file");
        } else if (childCount > 3) {
            throw new IllegalArgumentException("you can only add one child to SDPullToRefreshView in your xml file");
        } else {
            this.mRefreshView = this.getChildAt(2);
        }
    }

    private void addLoadingViews() {
        SDPullToRefreshLoadingView headerView = this.onCreateHeaderView();
        if (headerView == null) {
            String headerClassName = this.getResources().getString(R.string.lib_ptr_header_class);
            if (!TextUtils.isEmpty(headerClassName)) {
                headerView = SDPullToRefreshLoadingView.getInstanceByClassName(headerClassName, this.getContext());
            }
        }

        if (headerView == null) {
            headerView = new SimpleTextLoadingView(this.getContext());
        }

        this.setHeaderView((SDPullToRefreshLoadingView) headerView);
        SDPullToRefreshLoadingView footerView = this.onCreateFooterView();
        if (footerView == null) {
            String footerClassName = this.getResources().getString(R.string.lib_ptr_footer_class);
            if (footerClassName != null) {
                footerView = SDPullToRefreshLoadingView.getInstanceByClassName(footerClassName, this.getContext());
            }
        }

        if (footerView == null) {
            footerView = new SimpleTextLoadingView(this.getContext());
        }

        this.setFooterView((SDPullToRefreshLoadingView) footerView);
    }

    protected SDPullToRefreshLoadingView onCreateHeaderView() {
        return null;
    }

    protected SDPullToRefreshLoadingView onCreateFooterView() {
        return null;
    }

    private int getMinWidthInternal() {
        return Build.VERSION.SDK_INT >= 16 ? this.getMinimumWidth() : 0;
    }

    private int getMinHeightInternal() {
        return Build.VERSION.SDK_INT >= 16 ? this.getMinimumHeight() : 0;
    }

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
        this.measureLoadingView(this.mHeaderView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
        this.measureLoadingView(this.mFooterView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
        this.measureChild(this.mRefreshView, widthMeasureSpec, heightMeasureSpec);
        int maxHeight;
        if (widthMode != View.MeasureSpec.EXACTLY) {
            maxHeight = Math.max(this.mHeaderView.getMeasuredWidth(), this.mFooterView.getMeasuredWidth());
            maxHeight = Math.max(maxHeight, this.mRefreshView.getMeasuredWidth());
            maxHeight += this.getPaddingLeft() + this.getPaddingRight();
            maxHeight = Math.max(maxHeight, this.getMinWidthInternal());
            if (widthMode == 0) {
                width = maxHeight;
            } else if (widthMode == View.MeasureSpec.AT_MOST) {
                width = Math.min(maxHeight, width);
            }
        }

        if (heightMode != View.MeasureSpec.EXACTLY) {
            maxHeight = this.mRefreshView.getMeasuredHeight();
            if (maxHeight == 0) {
                maxHeight = Math.max(this.mHeaderView.getMeasuredHeight(), this.mFooterView.getMeasuredHeight());
            }

            maxHeight += this.getPaddingTop() + this.getPaddingBottom();
            maxHeight = Math.max(maxHeight, this.getMinHeightInternal());
            if (heightMode == 0) {
                height = maxHeight;
            } else if (heightMode == View.MeasureSpec.AT_MOST) {
                height = Math.min(maxHeight, height);
            }
        }

        if (needReMeasure) {
            widthMeasureSpecLoadingView = MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            this.measureLoadingView(this.mHeaderView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
            this.measureLoadingView(this.mFooterView, widthMeasureSpecLoadingView, heightMeasureSpecLoadingView);
        }

        this.setMeasuredDimension(width, height);
    }

    private void measureLoadingView(View loadingView, int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams params = loadingView.getLayoutParams();
        loadingView.measure(getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), params.width), getChildMeasureSpec(heightMeasureSpec, 0, params.height));
    }

    private int getTopLayoutHeaderView() {
        int top = this.getTopHeaderViewReset();
        if (this.getDirection() == Direction.FROM_HEADER) {
            if (this.mViewDragHelper.getViewDragState() == 0) {
                switch (this.mState) {
                    case REFRESHING:
                    case REFRESH_SUCCESS:
                    case REFRESH_FAILURE:
                        top += this.mHeaderView.getRefreshHeight();
                }
            } else {
                top = this.mHeaderView.getTop();
            }
        }

        return top;
    }

    private int getTopLayoutFooterView() {
        int top = this.getTopFooterViewReset();
        if (this.getDirection() == Direction.FROM_FOOTER) {
            if (this.mViewDragHelper.getViewDragState() == 0) {
                switch (this.mState) {
                    case REFRESHING:
                    case REFRESH_SUCCESS:
                    case REFRESH_FAILURE:
                        top -= this.mFooterView.getRefreshHeight();
                }
            } else {
                top = this.mFooterView.getTop();
            }
        }

        return top;
    }

    private int getTopLayoutRefreshView() {
        int top = this.getTopAlignTop();
        if (!this.mIsOverLayMode) {
            if (this.mViewDragHelper.getViewDragState() == 0) {
                switch (this.mState) {
                    case REFRESHING:
                    case REFRESH_SUCCESS:
                    case REFRESH_FAILURE:
                        if (this.getDirection() == Direction.FROM_HEADER) {
                            top += this.mHeaderView.getRefreshHeight();
                        } else if (this.getDirection() == Direction.FROM_FOOTER) {
                            top -= this.mFooterView.getRefreshHeight();
                        }
                }
            } else {
                top = this.mRefreshView.getTop();
            }
        }

        return top;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left;
        if (this.mLogUtils.isDebug()) {
            left = this.mViewDragHelper.getViewDragState();
            if (left == 0) {
                this.mLogUtils.i("onLayout " + left + " totalHeight:----------" + this.getHeight());
            } else {
                this.mLogUtils.e("onLayout " + left + " totalHeight:----------" + this.getHeight());
            }
        }

        left = this.getPaddingLeft();
        int top = this.getTopLayoutHeaderView();
        int right = left + this.mHeaderView.getMeasuredWidth();
        int bottom = top + this.mHeaderView.getMeasuredHeight();
        this.mHeaderView.layout(left, top, right, bottom);
        this.mLogUtils.i("HeaderView:" + top + "," + bottom);
        top = this.getTopLayoutRefreshView();
        if (!this.mIsOverLayMode && this.getDirection() == Direction.FROM_HEADER && bottom > top) {
            top = bottom;
        }

        right = left + this.mRefreshView.getMeasuredWidth();
        bottom = top + this.mRefreshView.getMeasuredHeight();
        this.mRefreshView.layout(left, top, right, bottom);
        this.mLogUtils.i("RefreshView:" + top + "," + bottom);
        top = this.getTopLayoutFooterView();
        if (!this.mIsOverLayMode && bottom <= this.getTopAlignBottom() && bottom > top) {
            top = bottom;
        }

        right = left + this.mFooterView.getMeasuredWidth();
        bottom = top + this.mFooterView.getMeasuredHeight();
        this.mFooterView.layout(left, top, right, bottom);
        this.mLogUtils.i("FooterView:" + top + "," + bottom);
        this.mHasOnLayout = true;
        this.runUpdatePositionRunnableIfNeed();
    }

    private void runUpdatePositionRunnableIfNeed() {
        if (this.mHasOnLayout && this.mUpdatePositionRunnable != null) {
            this.post(this.mUpdatePositionRunnable);
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mStopRefreshingRunnable);
        this.mHasOnLayout = false;
        this.mUpdatePositionRunnable = null;
        this.mViewDragHelper.abort();
    }
}
