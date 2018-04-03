
package com.liuxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 */

public abstract class WaterFullAdapter<T> extends BaseRecyclerAdapter {
    public WaterFullAdapter(List<T> data, Context mContext, @LayoutRes int layoutId) {
        super(data, mContext, layoutId);
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
    public BaseViewHoloder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHoloder holoder = null;
        if (viewType == 40000) {
            //没有数据
            holoder = new BaseViewHoloder(mInflater.inflate(noDataViewId,parent,false));
        } else {
            if (viewType < 10000) {
                //headerView
                View headerView = mInflater.inflate((Integer) getHeaderView().get(viewType), parent, false);
                holoder = new BaseViewHoloder(headerView);
            } else if (viewType == 10000) {
                //列表数据
                holoder = new BaseViewHoloder(mInflater.inflate(mLayoutId, parent,false));
            } else if (viewType < 40000) {
                //footerView
                View footerView = mInflater.inflate((Integer) getFooterView().get(viewType - 20000), parent, false);
                holoder = new BaseViewHoloder(footerView);
            }
        }
        return holoder;
    }

    @Override
    public void onBindViewHolder(BaseViewHoloder holder, int position) {
        if (isNoData) {
            setWaterFall(holder);
            if (mIEmptyView != null) {
                mIEmptyView.setEmptyView(holder.getAbroadView());
            }
        } else {
            if (position < getHeaderView().size()) {
                setWaterFall(holder);
                mIHeaderView.HeaderView(holder.getAbroadView(),position);
            } else if (position < mData.size()+getHeaderView().size()) {
                setData(holder, position - getHeaderView().size(), (T) mData.get(position - getHeaderView().size()));
            } else {
                setWaterFall(holder);
                mIFootView.FooterView(holder.getAbroadView(),position-getHeaderView().size()-mData.size());
            }
            if (position >= pullLoadingPosition - 1 && mIPullLoading != null) {
                mIPullLoading.PullToLoading();
            }

        }

    }
    /**
     * 瀑布流时将头部最外面的布局设置成铺满
     *
     * @param holder
     */
    private void setWaterFall(BaseViewHoloder holder) {
        StaggeredGridLayoutManager.LayoutParams clp =
                (StaggeredGridLayoutManager.LayoutParams) holder.getAbroadView().getLayoutParams();
        clp.setFullSpan(true);
    }

    /**
     * 设置列表数据
     * @param holder
     * @param position
     * @param item
     */
    protected abstract void setData(BaseViewHoloder holder, int position, T item);
}
