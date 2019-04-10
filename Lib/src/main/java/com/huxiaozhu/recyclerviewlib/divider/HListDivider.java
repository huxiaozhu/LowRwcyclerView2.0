package com.huxiaozhu.recyclerviewlib.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.huxiaozhu.recyclerviewlib.adapter.WrapAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class HListDivider extends BaseDivider {

    /**
     * 自定义分割线(高和颜色)
     *
     * @param dividerHeight  分割线高度
     * @param dividerColorId 分割线颜色
     */
    public HListDivider(WrapAdapter adapter, int dividerHeight, int dividerColorId) {
        super(adapter,dividerHeight, dividerColorId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition != head + data + foot - 1) {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawRigth(c, parent);
    }
}
