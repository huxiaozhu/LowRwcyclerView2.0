package com.huxiaozhu.recyclerviewlib.refach;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

class TouchHelper {
    public static final int EVENT_DOWN = 0;
    public static final int EVENT_LAST = 1;
    private boolean mIsNeedIntercept = false;
    private boolean mIsNeedCosume = false;
    private float mCurrentX;
    private float mCurrentY;
    private float mLastX;
    private float mLastY;
    private float mDownX;
    private float mDownY;
    private float mMoveX;
    private float mMoveY;
    private float mUpX;
    private float mUpY;
    private TouchHelper.Direction mDirection;

    TouchHelper() {
        this.mDirection = TouchHelper.Direction.None;
    }

    public void processTouchEvent(MotionEvent ev) {
        this.mLastX = this.mCurrentX;
        this.mLastY = this.mCurrentY;
        this.mCurrentX = ev.getRawX();
        this.mCurrentY = ev.getRawY();
        switch (ev.getAction()) {
            case 0:
                this.mDownX = this.mCurrentX;
                this.mDownY = this.mCurrentY;
                this.setDirection(TouchHelper.Direction.None);
                break;
            case 1:
                this.mUpX = this.mCurrentX;
                this.mUpY = this.mCurrentY;
                break;
            case 2:
                this.mMoveX = this.mCurrentX;
                this.mMoveY = this.mCurrentY;
        }

        StringBuilder sb = this.getDebugInfo();
        Log.i("TouchHelper", "event " + ev.getAction() + ":" + sb.toString());

    }

    public void setNeedIntercept(boolean needIntercept) {
        this.mIsNeedIntercept = needIntercept;
    }

    public boolean isNeedIntercept() {
        return this.mIsNeedIntercept;
    }

    public void setNeedCosume(boolean needCosume) {
        this.mIsNeedCosume = needCosume;
    }

    public boolean isNeedCosume() {
        return this.mIsNeedCosume;
    }

    public float getDownX() {
        return this.mDownX;
    }

    public float getDownY() {
        return this.mDownY;
    }

    public float getMoveX() {
        return this.mMoveX;
    }

    public float getMoveY() {
        return this.mMoveY;
    }

    public float getUpX() {
        return this.mUpX;
    }

    public float getUpY() {
        return this.mUpY;
    }

    public void saveDirection() {
        if (this.mDirection == TouchHelper.Direction.None) {
            float dx = this.getDeltaXFrom(0);
            float dy = this.getDeltaYFrom(0);
            if (dx != 0.0F || dy != 0.0F) {
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx < 0.0F) {
                        this.setDirection(TouchHelper.Direction.MoveLeft);
                    } else if (dx > 0.0F) {
                        this.setDirection(TouchHelper.Direction.MoveRight);
                    }
                } else if (dy < 0.0F) {
                    this.setDirection(TouchHelper.Direction.MoveTop);
                } else if (dy > 0.0F) {
                    this.setDirection(TouchHelper.Direction.MoveBottom);
                }

            }
        }
    }

    public void saveDirectionHorizontal() {
        if (this.mDirection != TouchHelper.Direction.MoveLeft && this.mDirection != TouchHelper.Direction.MoveRight) {
            int dx = (int) this.getDeltaXFrom(0);
            if (dx != 0) {
                if (dx < 0) {
                    this.setDirection(TouchHelper.Direction.MoveLeft);
                } else if (dx > 0) {
                    this.setDirection(TouchHelper.Direction.MoveRight);
                }

            }
        }
    }

    public void saveDirectionVertical() {
        if (this.mDirection != TouchHelper.Direction.MoveTop && this.mDirection != TouchHelper.Direction.MoveBottom) {
            int dy = (int) this.getDeltaYFrom(0);
            if (dy != 0) {
                if (dy < 0) {
                    this.setDirection(TouchHelper.Direction.MoveTop);
                } else if (dy > 0) {
                    this.setDirection(TouchHelper.Direction.MoveBottom);
                }

            }
        }
    }

    private void setDirection(TouchHelper.Direction direction) {
        this.mDirection = direction;
        Log.i("TouchHelper", "setDirection:" + direction);
    }

    public TouchHelper.Direction getDirection() {
        return this.mDirection;
    }

    public float getDeltaXFrom(int event) {
        switch (event) {
            case 0:
                return this.mCurrentX - this.mDownX;
            case 1:
                return this.mCurrentX - this.mLastX;
            default:
                return 0.0F;
        }
    }

    public float getDeltaYFrom(int event) {
        switch (event) {
            case 0:
                return this.mCurrentY - this.mDownY;
            case 1:
                return this.mCurrentY - this.mLastY;
            default:
                return 0.0F;
        }
    }

    public double getDegreeXFrom(int event) {
        float dx = this.getDeltaXFrom(event);
        if (dx == 0.0F) {
            return 0.0D;
        } else {
            float dy = this.getDeltaYFrom(event);
            float angle = Math.abs(dy) / Math.abs(dx);
            return Math.toDegrees(Math.atan((double) angle));
        }
    }

    public double getDegreeYFrom(int event) {
        float dy = this.getDeltaYFrom(event);
        if (dy == 0.0F) {
            return 0.0D;
        } else {
            float dx = this.getDeltaXFrom(event);
            float angle = Math.abs(dx) / Math.abs(dy);
            return Math.toDegrees(Math.atan((double) angle));
        }
    }

    public boolean isMoveLeftFrom(int event) {
        return this.getDeltaXFrom(event) < 0.0F;
    }

    public boolean isMoveTopFrom(int event) {
        return this.getDeltaYFrom(event) < 0.0F;
    }

    public boolean isMoveRightFrom(int event) {
        return this.getDeltaXFrom(event) > 0.0F;
    }

    public boolean isMoveBottomFrom(int event) {
        return this.getDeltaYFrom(event) > 0.0F;
    }

    public int getLegalDeltaX(int x, int minX, int maxX, int dx) {
        int future = x + dx;
        int comsume;
        if (this.isMoveLeftFrom(1)) {
            if (future < minX) {
                comsume = minX - future;
                dx += comsume;
            }
        } else if (this.isMoveRightFrom(1) && future > maxX) {
            comsume = future - maxX;
            dx -= comsume;
        }

        return dx;
    }

    public int getLegalDeltaY(int y, int minY, int maxY, int dy) {
        int future = y + dy;
        int comsume;
        if (this.isMoveTopFrom(1)) {
            if (future < minY) {
                comsume = minY - future;
                dy += comsume;
            }
        } else if (this.isMoveBottomFrom(1) && future > maxY) {
            comsume = future - maxY;
            dy -= comsume;
        }

        return dy;
    }

    static void requestDisallowInterceptTouchEvent(View view, boolean disallowIntercept) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    public static boolean isScrollToLeft(View view) {
        return !view.canScrollHorizontally(-1);
    }

    static boolean isScrollToTop(View view) {
        return !view.canScrollVertically(-1);
    }

    public static boolean isScrollToRight(View view) {
        return !view.canScrollHorizontally(1);
    }

    static boolean isScrollToBottom(View view) {
        return !view.canScrollVertically(1);
    }

    private StringBuilder getDebugInfo() {
        StringBuilder sb = (new StringBuilder("\r\n")).append("DownX:").append(this.mDownX).append("\r\n").append("DownY:").append(this.mDownY).append("\r\n").append("MoveX:").append(this.mMoveX).append("\r\n").append("MoveY:").append(this.mMoveY).append("\r\n").append("\r\n").append("DeltaX from down:").append(this.getDeltaXFrom(0)).append("\r\n").append("DeltaY from down:").append(this.getDeltaYFrom(0)).append("\r\n").append("DeltaX from last:").append(this.getDeltaXFrom(1)).append("\r\n").append("DeltaY from last:").append(this.getDeltaYFrom(1)).append("\r\n").append("\r\n").append("DegreeX from down:").append(this.getDegreeXFrom(0)).append("\r\n").append("DegreeY from down:").append(this.getDegreeYFrom(0)).append("\r\n").append("DegreeX from last:").append(this.getDegreeXFrom(1)).append("\r\n").append("DegreeY from last:").append(this.getDegreeYFrom(1)).append("\r\n");
        return sb;
    }

    public enum Direction {
        None,
        MoveLeft,
        MoveTop,
        MoveRight,
        MoveBottom;

        private Direction() {
        }
    }
}
