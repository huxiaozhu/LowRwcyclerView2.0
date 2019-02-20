package com.huxiaozhu.recyclerviewlib.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huxiaozhu.recyclerviewlib.adapter.WrapAdapter;

public class GridDivider extends BaseDivider {


    /**
     * 自定义分割线(高和颜色)
     *
     * @param dividerHeight  分割线高度
     * @param dividerColorId 分割线颜色
     */
    public GridDivider(WrapAdapter adapter, int dividerHeight, int dividerColorId) {
        super(adapter,dividerHeight, dividerColorId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        setGrid(parent.getChildAdapterPosition(view), outRect);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawButtom(c, parent);
        drawRigth(c, parent);
    }

    private void setGrid(int position, Rect outRect) {
        //处理头部divider
        if (position < head) {
            //有头部只绘制底部
            outRect.set(0, 0, 0, mDividerHeight);
        } else if (position < head + data) {
            if (isLastColumn(position)) {
//                最后一列右侧不偏移，如果没添加尾部且是最后一行，底部不偏移
//                if (mAdapter.getFooterView().size()==0 && isLastLines(position)) {
//                    outRect.set(0, 0, 0, 0);
//                } else {
//                    outRect.set(0, 0, 0, mDividerHeight);
//                }
                outRect.set(0, 0, 0, mDividerHeight);
            } else if (isLastLines(position)) {
//                最后一行
//                if (mAdapter.getFooterView().size()!=0) {
//                    outRect.set(0, 0, mDividerHeight, mDividerHeight);
//                } else {
//                    outRect.set(0, 0, mDividerHeight, 0);
//                }
                outRect.set(0, 0, mDividerHeight, 0);
            } else {
                outRect.set(0, 0, mDividerHeight, mDividerHeight);
            }
        } else {
//            处理尾部，只绘制底部，最后一行底部不绘制
            if (position == head + data + foot - 1) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mDividerHeight);
            }
        }

    }

    /**
     * 判断是否是最后一列（竖直）
     *
     * @param position
     * @return
     */
    private boolean isLastColumn(int position) {
        int j = position - head;
        int po = j % mNunColumns;
        int i = mNunColumns - 1;
        return po == i;
    }

    /**
     * 判断是否是最后一行（竖直）
     *
     * @param position
     * @return
     */
    private boolean isLastLines(int position) {
        if (position < head + data - (data % mNunColumns)) {
            return false;
        } else {
            return true;
        }
    }

}
