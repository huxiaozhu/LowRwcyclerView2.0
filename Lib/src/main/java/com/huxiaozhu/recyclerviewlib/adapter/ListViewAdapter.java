
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.EmptyViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.FootViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.HeadViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.ListViewHolder;
import com.huxiaozhu.recyclerviewlib.widget.ExpandRecyclerView;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/17.
 * All Rights Reserved by YiZu
 * ListView的适配器
 * 实现功能：
 * 1.添加headerView和FooterView
 * 2.添加上拉加载
 * 3.没有数据时现实默认布局（没有数据）
 */

public abstract class ListViewAdapter<T> extends BaseAdapter {

    protected ListViewAdapter(List<T> data, Context context) {
        super(data, context);
    }

    /**
     * 设置item的数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return setItemCount();
    }

    /**
     * 设置Item的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return setItemType(position);
    }

    /**
     * 创建item的view
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewViewHolder holoder = null;
//        if (viewType == 40000) {
//            //没有数据
//            holoder = new EmptyViewHolder(noDataViewId, parent);
//        } else {
//            if (viewType < 10000) {
//                //headerView
//                holoder = new HeadViewHolder((Integer) getHeaderView().get(viewType), parent);
//            } else if (viewType == 10000) {
                //列表数据
                holoder = new ListViewHolder(mLayoutId, parent);
//            } else if (viewType < 40000) {
//                //footerView
//                holoder = new FootViewHolder((Integer) getFooterView().get(viewType - 20000), parent);
//            }
//        }
        return holoder;
    }

    /**
     * 绑定item的数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewViewHolder holder, int position) {
//        if (isNoData) {
//            if (mIEmptyView != null) {
//                mIEmptyView.setEmptyView(holder.getItemView());
//            }
//        } else {
//            if (holder instanceof HeadViewHolder) {
//                mIHeaderView.HeaderView(holder.getItemView(), position);
//            } else
//                if (holder instanceof ListViewHolder) {
//                setData((ListViewHolder) holder, position - getHeaderView().size(), (T) mData.get(position - getHeaderView().size()));
//            } else if (holder instanceof FootViewHolder) {
//                mIFootView.FooterView(holder.getItemView(), position - getHeaderView().size() - mData.size());
//            }
//            //上拉加载,绑定数据的时候，如果position为
//            if (isLoading) {
//                if (position - getHeaderView().size() >= pullLoadingPosition - 1 && mIPullLoading != null) {
//                    mIPullLoading.PullToLoading();
//                    pullLoadingPosition = pullLoadingPosition + 20;
//                }
//            }
            setData((ListViewHolder) holder,position,(T)mData.get(position));
            setClickLisiner(holder, position);
//        }
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
