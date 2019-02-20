package com.huxiaozhu.recyclerviewlib.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.huxiaozhu.recyclerviewlib.adapter.BaseAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.GridViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.HGridViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.HListViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.ListViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.VariableAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.WrapAdapter;
import com.huxiaozhu.recyclerviewlib.divider.BaseDivider;
import com.huxiaozhu.recyclerviewlib.divider.GridDivider;
import com.huxiaozhu.recyclerviewlib.divider.HGridDivider;
import com.huxiaozhu.recyclerviewlib.divider.HListDivider;
import com.huxiaozhu.recyclerviewlib.divider.ListDivider;
import com.huxiaozhu.recyclerviewlib.divider.VariableDivider;

/**
 * 扩展RecyclerView
 */
public class ExpandRecyclerView extends RecyclerView {

    private WrapAdapter mAdapter = new WrapAdapter();

    public ExpandRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 添加HeaderView
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        if (headerView != null) mAdapter.addHeaderView(headerView);
    }

    /**
     * 添加HeaderView
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        if (footerView != null) mAdapter.addFooterView(footerView);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (adapter == null) throw new IllegalArgumentException("Adapter is Null");
        mAdapter.addAdapter(adapter);
        super.setAdapter(mAdapter);
        setManager(adapter);
    }

    private void setManager(final Adapter adapter) {
        final int head = mAdapter.getmHeaderView() == null ? 0 : 1;

        if (adapter instanceof BaseAdapter) {
            if (adapter instanceof ListViewAdapter) {
                //设置recyclerView显示方式
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                setLayoutManager(manager);
            } else if (adapter instanceof HListViewAdapter) {
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                setLayoutManager(manager);
            } else if (adapter instanceof GridViewAdapter) {
                final int finalMNunColumns = ((GridViewAdapter) adapter).getNumColums();
                GridLayoutManager manager = new GridLayoutManager(getContext(), finalMNunColumns);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                //设置头布局，尾布局占满
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position >= head && position < adapter.getItemCount() + head) {
                            return 1;
                        } else {
                            return finalMNunColumns;
                        }
                    }
                });
                setLayoutManager(manager);
            } else if (adapter instanceof HGridViewAdapter) {
                GridLayoutManager manager = new GridLayoutManager(getContext(), ((HGridViewAdapter) adapter).getNumColums());
                manager.setOrientation(GridLayoutManager.HORIZONTAL);
                setLayoutManager(manager);
            } else if (adapter instanceof VariableAdapter) {
                //瀑布流
                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(((VariableAdapter) adapter).getNumColums(), StaggeredGridLayoutManager.VERTICAL);
                manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                setItemAnimator(null);
                setLayoutManager(manager);
            }
        }

    }

    /**
     * 添加分割线
     */
    private void addItemDecoration(int dividerHeight, @ColorRes int dividerColorId) {
        Adapter adapter = mAdapter.getmAdapter();
        BaseDivider decor;
        if (adapter instanceof ListViewAdapter) {
            decor = new ListDivider(mAdapter,dividerHeight, getResources().getColor(dividerColorId));
        } else if (adapter instanceof HListViewAdapter) {
            decor = new HListDivider(mAdapter,dividerHeight, getResources().getColor(dividerColorId));
        } else if (adapter instanceof GridViewAdapter) {
            decor = new GridDivider(mAdapter,dividerHeight, getResources().getColor(dividerColorId));
        } else if (adapter instanceof HGridViewAdapter) {
            decor = new HGridDivider(mAdapter,dividerHeight, getResources().getColor(dividerColorId));
        } else if (adapter instanceof VariableAdapter) {
            decor = new VariableDivider(mAdapter,dividerHeight, getResources().getColor(dividerColorId));
        } else {
            return;
        }
        addItemDecoration(decor);
    }

    public void setAdapter(@Nullable Adapter adapter, int dividerHeight, int dividerColorId) {
        setAdapter(adapter);
        addItemDecoration(dividerHeight,dividerColorId);
    }
}
