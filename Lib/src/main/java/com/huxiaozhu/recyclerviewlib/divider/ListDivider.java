package com.huxiaozhu.recyclerviewlib.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huxiaozhu.recyclerviewlib.adapter.WrapAdapter;

public class ListDivider extends BaseDivider{


    /**
     * 自定义分割线(高和颜色)
     *
     * @param adapter
     * @param dividerHeight  分割线高度
     * @param dividerColorId 分割线颜色
     */
    public ListDivider(WrapAdapter adapter, int dividerHeight, int dividerColorId) {
        super(adapter,dividerHeight, dividerColorId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawButtom(c, parent);
    }
}
