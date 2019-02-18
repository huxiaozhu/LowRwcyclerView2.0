
package com.liuxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.ListViewHolder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 */

public abstract class HListViewAdapter<T> extends BaseAdapter {
    public HListViewAdapter(List<T> data, RecyclerView recyclerView) {
        super(data, recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
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
        setData((ListViewHolder) holder,position, (T) mData.get(position));
        setClickLisiner(holder, position);
    }
    /**
     * 设置列表数据
     * @param holder
     * @param position
     * @param item
     */
    protected abstract void setData(ListViewHolder holder, int position, T item);
}
