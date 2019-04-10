package com.huxiaozhu.recyclerviewlib.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.adapter.viewholder.FootViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.HeadViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public final class WrapAdapter extends RecyclerView.Adapter {
    private static final int HEADER = -0x100;
    private static final int FOOTER = -0x101;
    private View mHeaderView = null;
    private View mFooterView = null;
//    private View mEmpterView = null;
    private RecyclerView.Adapter mAdapter;

    public WrapAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER) {
            return new HeadViewHolder(mHeaderView);
        } else if (viewType == FOOTER) {
            return new FootViewHolder(mFooterView);
        }
        return mAdapter.onCreateViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = getItemViewType(i);
        if (viewType == HEADER) {
            return;
        } else if (viewType == FOOTER) {
            return;
        } else {
            mAdapter.onBindViewHolder(viewHolder, i - (mHeaderView == null ? 0 : 1));
        }
    }

    @Override
    public int getItemCount() {
        return (mAdapter != null ? mAdapter.getItemCount() : 0) + (mFooterView == null ? 0 : 1) + (mHeaderView == null ? 0 : 1);
    }


    @Override
    public int getItemViewType(int position) {
        int head = mHeaderView == null ? 0 : 1;
        int itemCount = mAdapter == null ? 0 : mAdapter.getItemCount();
        if (position < head) return HEADER;
        if (position - head < itemCount) return mAdapter.getItemViewType(position - head);
        return FOOTER;
    }

    public void addHeaderView(View headerView) {
        this.mHeaderView = headerView;
    }

    public void addFooterView(View footerView) {
        this.mFooterView = footerView;
    }

    public void addAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    public View getmHeaderView() {
        return mHeaderView;
    }
    public View getmFooterView() {
        return mFooterView;
    }
    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //瀑布流时将头部最外面的布局设置成铺满
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (holder instanceof HeadViewHolder||holder instanceof FootViewHolder){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }
}
