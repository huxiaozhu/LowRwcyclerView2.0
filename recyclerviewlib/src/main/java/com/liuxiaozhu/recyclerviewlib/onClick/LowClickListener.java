package com.liuxiaozhu.recyclerviewlib.onClick;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;

/**
 * Author：Created by liuxiaozhu on 2018/4/3.
 * Email: chenhuixueba@163.com
 * 用来处理Item长按事件
 * 只能监听Item
 */

public abstract class LowClickListener implements RecyclerView.OnItemTouchListener {
    //sdk提供的处理手势检测类
    private GestureDetector mGestureDetector;

    public LowClickListener(Context context, final RecyclerView recyclerView) {
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {//这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
                    //单击
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null){
                            onItemClick(childView,recyclerView.getChildLayoutPosition(childView), (BaseViewHoloder) recyclerView.getChildViewHolder(childView));
                            return true;
                        }
                        return false;
                    }
                    //长按
                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null ){
                            onItemLongClick(childView,recyclerView.getChildLayoutPosition(childView), (BaseViewHoloder) recyclerView.getChildViewHolder(childView));
                        }
                    }
                    //双击
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        return false;
                    }
                });
    }

    protected abstract void onItemClick(View view, int position, BaseViewHoloder holder);

    protected abstract void onItemLongClick(View view, int position, BaseViewHoloder holder);

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
}
