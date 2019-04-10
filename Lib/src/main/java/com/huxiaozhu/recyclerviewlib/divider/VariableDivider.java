package com.huxiaozhu.recyclerviewlib.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.huxiaozhu.recyclerviewlib.adapter.VariableAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.WrapAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class VariableDivider extends BaseDivider{


    /**
     * 自定义分割线(高和颜色)
     *
     * @param dividerHeight  分割线高度
     * @param dividerColorId 分割线颜色
     */
    public VariableDivider(WrapAdapter adapter, int dividerHeight, int dividerColorId) {
        super(adapter,dividerHeight, dividerColorId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mAdapter instanceof VariableAdapter) {
            setWDicider(parent.getChildAdapterPosition(view), outRect);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawButtom(c, parent);
        drawRigth(c, parent);
    }

    private void setWDicider(int itemPosition, Rect outRect) {
        if (itemPosition < head) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else if (itemPosition < head + data) {
            outRect.set(0, 0, mDividerHeight, mDividerHeight);
        } else {
//            if (itemPosition != head + data + foot - 1) {
//                outRect.set(0, 0, 0, mDividerHeight);
//            }
            outRect.set(0, 0, 0, mDividerHeight);
        }
    }
}
