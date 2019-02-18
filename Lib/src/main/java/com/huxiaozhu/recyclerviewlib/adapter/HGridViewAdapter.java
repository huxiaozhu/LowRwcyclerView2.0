
package com.huxiaozhu.recyclerviewlib.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.ListViewHolder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 * 横向滑动的GridView适配器
 */

public abstract class HGridViewAdapter<T> extends BaseAdapter {
    public HGridViewAdapter(List<T> data, RecyclerView recyclerView, int spanCount) {
        super(data, recyclerView);
        if (spanCount > 2) mNunColumns = spanCount;
        GridLayoutManager manager = new GridLayoutManager(mContext, mNunColumns);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public BaseViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mLayoutId, parent);
    }

    @Override
    public void onBindViewHolder(BaseViewViewHolder holder, int position) {
        setData((ListViewHolder) holder, position, (T) mData.get(position));
        setClickLisiner(holder, position);
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
