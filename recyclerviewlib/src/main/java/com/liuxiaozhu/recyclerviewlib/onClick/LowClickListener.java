package com.liuxiaozhu.recyclerviewlib.onClick;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author：Created by liuxiaozhu on 2018/4/3.
 * Email: chenhuixueba@163.com
 * 用来处理Item长按事件
 * 只能监听Item
 */

public class LowClickListener implements RecyclerView.OnItemTouchListener {
    //sdk提供的处理手势检测类
    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    public LowClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        if (listener == null) {
            throw new NullPointerException("OnItemClickListener为空");
        }
        mListener = listener;
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {//这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
                    //单击
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null && mListener != null){
                            mListener.onItemClick(childView,recyclerView.getChildLayoutPosition(childView));
                            return true;
                        }
                        return false;
                    }
                    //长按
                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null && mListener != null){
                            mListener.onItemLongClick(childView,recyclerView.getChildLayoutPosition(childView));
                        }
                    }
                    //双击
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        return false;
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //把事件交给GestureDetector处理
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * item处理接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}
