
package com.liuxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 */

public abstract class HListViewAdapter<T> extends BaseRecyclerAdapter {
    public HListViewAdapter(List<T> data, Context mContext, @LayoutRes int layoutId) {
        super(data, mContext, layoutId);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public BaseViewHoloder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHoloder(mInflater.inflate(mLayoutId, parent,false), this);
    }

    @Override
    public void onBindViewHolder(BaseViewHoloder holder, int position) {
        //此处将每一个item的position传入adapter
        holder.setmPosition(position);
        setData(holder,position, (T) mData.get(position));
    }
    /**
     * 设置列表数据
     * @param holder
     * @param position
     * @param item
     */
    protected abstract void setData(BaseViewHoloder holder, int position, T item);
}
