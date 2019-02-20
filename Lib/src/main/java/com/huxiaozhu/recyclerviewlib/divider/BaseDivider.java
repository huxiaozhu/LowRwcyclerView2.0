
package com.huxiaozhu.recyclerviewlib.divider;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huxiaozhu.recyclerviewlib.adapter.BaseAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.WrapAdapter;

/**
 * Created by chenhui on 2017/4/26.
 * All Rights Reserved by YiZu
 * 通用recyclerView的分割线
 */
public class BaseDivider extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    int mDividerHeight;//分割线高度
    RecyclerView.Adapter mAdapter;
    int mNunColumns;
    int head;
    int foot;
    protected int data;

    /**
     * 自定义分割线(高和颜色)
     *
     * @param dividerHeight 分割线高度
     * @param dividerColorId  分割线颜色
     */
    BaseDivider(WrapAdapter adapter, int dividerHeight, int dividerColorId) {
        mDividerHeight = dividerHeight;

        mAdapter = adapter.getmAdapter();
        if (mAdapter instanceof BaseAdapter) {
            mNunColumns = ((BaseAdapter) mAdapter).getNumColums();
            data = mAdapter.getItemCount();
            head = adapter.getmHeaderView() == null ? 0 : 1;
            foot = adapter.getmFooterView() == null ? 0 : 1;
        }
//        初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColorId);
        //填充
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置分割线偏移量
     * 先调用此方法（设置完后在执行onDraw方法）
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    }

    /**
     * 绘制分割线
     * getItemOffsets方法调用后会调用此方法
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    /**
     * 画divider （底部）
     *
     * @param c
     * @param parent
     */
    void drawButtom(Canvas c, RecyclerView parent) {
        //获取Item的数量
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获取对应poisition对应的item
            final View child = parent.getChildAt(i);
            int left = child.getLeft();
            int right = child.getRight() + mDividerHeight;
            int top = child.getBottom();
            int bottom = top + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * 画divider（右边）
     *
     * @param c
     * @param parent
     */
    void drawRigth(Canvas c, RecyclerView parent) {
        //获取Item的数量
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获取对应poisition对应的item
            final View child = parent.getChildAt(i);
            int top = child.getTop();
            int left = child.getRight();
            int bottom = child.getBottom();
            int right = left + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
