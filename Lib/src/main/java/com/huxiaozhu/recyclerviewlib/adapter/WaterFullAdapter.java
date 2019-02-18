
package com.huxiaozhu.recyclerviewlib.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.EmptyViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.FootViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.HeadViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.ListViewHolder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 */

public abstract class WaterFullAdapter<T> extends BaseAdapter {
    public WaterFullAdapter(List<T> data, RecyclerView recyclerView, int spanCount) {
        super(data, recyclerView);
        if (spanCount > 2) mNunColumns = spanCount;
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(mNunColumns, StaggeredGridLayoutManager.VERTICAL);
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
            holoder = new EmptyViewHolder(noDataViewId, parent);
        } else {
            if (viewType < 10000) {
                //headerView
                holoder = new HeadViewHolder((Integer) getHeaderView().get(viewType), parent);
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
            setWaterFall(holder);
            if (mIEmptyView != null) {
                mIEmptyView.setEmptyView(holder.getItemView());
            }
        } else {
            if (holder instanceof HeadViewHolder) {
                setWaterFall(holder);
                mIHeaderView.HeaderView(holder.getItemView(), position);
            } else if (holder instanceof ListViewHolder) {
                setData((ListViewHolder) holder, position - getHeaderView().size(), (T) mData.get(position - getHeaderView().size()));
            } else if (holder instanceof FootViewHolder) {
                setWaterFall(holder);
                mIFootView.FooterView(holder.getItemView(), position - getHeaderView().size() - mData.size());
            }
            if (position >= pullLoadingPosition - 1 && mIPullLoading != null) {
                mIPullLoading.PullToLoading();
            }
            setClickLisiner(holder, position);
        }

    }

    /**
     * 瀑布流时将头部最外面的布局设置成铺满
     *
     * @param holder
     */
    private void setWaterFall(BaseViewViewHolder holder) {
        StaggeredGridLayoutManager.LayoutParams clp =
                (StaggeredGridLayoutManager.LayoutParams) holder.getItemView().getLayoutParams();
        clp.setFullSpan(true);
    }

    /**
     * 设置列表数据
     *
     * @param holder
     * @param position
     * @param item
     */
    protected abstract void setData(ListViewHolder holder, int position, T item);
}