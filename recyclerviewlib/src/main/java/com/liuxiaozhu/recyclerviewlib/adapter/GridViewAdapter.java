package com.liuxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.EmptyViewHolder;
import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.FootViewHolder;
import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.HeadViewHolder;
import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.ListViewHolder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 * GridView的适配器
 */

public abstract class GridViewAdapter<T> extends BaseAdapter {
    public GridViewAdapter(List<T> data, RecyclerView recyclerView, int spanCount) {
        super(data,recyclerView);
        if (spanCount>2) mNunColumns = spanCount;
        final int finalMNunColumns = mNunColumns;
        GridLayoutManager manager = new GridLayoutManager(mContext, finalMNunColumns);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getHeaderView().size() > position
                        || position >= getHeaderView().size() + getData().size()
                        || getData().size() + getHeaderView().size() +getFooterView().size() == 0) {
                    return finalMNunColumns;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public int getItemCount() {
        return setItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return setItemType(position);
    }

    @Override
    public BaseViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewViewHolder holoder = null;
        if (viewType == 40000) {
            //没有数据
            holoder = new EmptyViewHolder(noDataViewId,parent);
        } else {
            if (viewType < 10000) {
                //headerView
                holoder = new HeadViewHolder((Integer) getHeaderView().get(viewType),parent);
            } else if (viewType == 10000) {
                //列表数据
                holoder = new ListViewHolder(mLayoutId, parent);
            } else if (viewType < 40000) {
                //footerView
                holoder = new FootViewHolder((Integer) getFooterView().get(viewType - 20000), parent);
            }
        }
        return holoder;
    }

    @Override
    public void onBindViewHolder(BaseViewViewHolder holder, int position) {
        if (isNoData) {
            if (mIEmptyView != null) {
                mIEmptyView.setEmptyView(holder.getItemView());
            }
        } else {
            if (holder instanceof HeadViewHolder) {
                mIHeaderView.HeaderView(holder.getItemView(), position);
            } else if (holder instanceof ListViewHolder) {
                setData((ListViewHolder) holder, position - getHeaderView().size(), (T) mData.get(position - getHeaderView().size()));
            } else if (holder instanceof FootViewHolder) {
                mIFootView.FooterView(holder.getItemView(),position-getHeaderView().size()-mData.size());
            }
            if (position >= pullLoadingPosition - 1 && mIPullLoading != null) {
                mIPullLoading.PullToLoading();
            }
            setClickLisiner(holder, position);
        }
    }

    /**
     * 设置列表数据
     * @param holder
     * @param position
     * @param item
     */
    protected abstract void setData(ListViewHolder holder, int position, T item);
}
