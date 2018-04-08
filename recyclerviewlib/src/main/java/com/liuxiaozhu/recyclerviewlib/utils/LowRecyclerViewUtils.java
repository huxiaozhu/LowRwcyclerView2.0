
package com.liuxiaozhu.recyclerviewlib.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.liuxiaozhu.recyclerviewlib.adapter.BaseRecyclerAdapter;
import com.liuxiaozhu.recyclerviewlib.adapter.GridViewAdapter;
import com.liuxiaozhu.recyclerviewlib.adapter.HGridViewAdapter;
import com.liuxiaozhu.recyclerviewlib.adapter.HListViewAdapter;
import com.liuxiaozhu.recyclerviewlib.adapter.ListViewAdapter;
import com.liuxiaozhu.recyclerviewlib.adapter.WaterFullAdapter;
import com.liuxiaozhu.recyclerviewlib.wedgit.RecyclerDivider;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 * RecyclerView的工具类（主要设置RecyclerView样式）
 */

public class LowRecyclerViewUtils<T> {
    protected Context mContext;
    protected RecyclerView mRecyclerView;
    protected BaseRecyclerAdapter<T> mAdapter;
    protected int mNunColumns = 2;

    public LowRecyclerViewUtils(RecyclerView recyclerView, int nunColumns, BaseRecyclerAdapter<T> adapter) {
        if (recyclerView == null || adapter == null) {
            throw new RuntimeException("recyclerView或者adapter为空");
        }
        this.mRecyclerView = recyclerView;
        this.mAdapter = adapter;
        //禁止嵌套滑动
//        mRecyclerView.setNestedScrollingEnabled(false);
        mContext = mAdapter.getContext();
        if (mNunColumns > 1) {
            this.mNunColumns = nunColumns;
        }
        setRecyclerStlty();
    }


    protected void setRecyclerStlty() {
        //ListView
        if (mAdapter instanceof ListViewAdapter) {
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            return;
        }
        //GridView
        if (mAdapter instanceof GridViewAdapter) {
            GridLayoutManager manager = new GridLayoutManager(mContext, mNunColumns);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter.getHeaderView().size() > position
                            || position >= mAdapter.getHeaderView().size() + mAdapter.getData().size()
                            || mAdapter.getData().size() + mAdapter.getHeaderView().size() + mAdapter.getFooterView().size() == 0) {
                        return mNunColumns;
                    } else {
                        return 1;
                    }
                }
            });
            mRecyclerView.setLayoutManager(manager);
            return;
        }
        //瀑布流
        if (mAdapter instanceof WaterFullAdapter) {
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(mNunColumns, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            return;
        }
        //水平滑动的ListView
        if (mAdapter instanceof HListViewAdapter) {
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(manager);
            return;
        }
        //横向滑动的GridView
        if (mAdapter instanceof HGridViewAdapter) {
            GridLayoutManager manager = new GridLayoutManager(mContext, mNunColumns);
            manager.setOrientation(GridLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(manager);
            return;
        }
    }

    /**
     * 添加带颜色的分割线
     *
     * @param dividerHeight
     * @param dividerColor
     */
    public void addItemDecoration(int dividerHeight, @ColorRes int dividerColor) {
        if (mAdapter.getHeaderView().size()+mAdapter.getData().size()+mAdapter.getFooterView().size()==0) {
            //没有数据不绘制分割线
        } else {
            mRecyclerView.addItemDecoration(new RecyclerDivider<T>(dividerHeight,
                    mAdapter, mContext.getResources().getColor(dividerColor), mNunColumns));
        }
    }

}
