package com.huxiaozhu.recyclerviewlib.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.FootViewHolder;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.HeadViewHolder;

public final class WrapAdapter extends RecyclerView.Adapter{
    private static final int HEADER = -0x100;
    private static final int FOOTER = -0x101;
    private View mHeaderView=null;
    private View mFooterView=null;
    private View mEmpterView=null;
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
        if (viewType == HEADER||viewType ==FOOTER) {
			if (mAdapter instanceof VariableAdapter) setWaterFall((BaseViewViewHolder) viewHolder);
            return ;
        } else {
            mAdapter.onBindViewHolder(viewHolder, i-(mHeaderView==null?0:1));
        }
    }

    @Override
    public int getItemCount() {
        return (mAdapter != null ? mAdapter.getItemCount():0) +(mFooterView==null?0:1) + (mHeaderView==null?0:1);
    }

	@Override
	public int getItemViewType(int position) {
		int head = mHeaderView == null?0:1;
		int itemCount = mAdapter == null?0:mAdapter.getItemCount();
		if (position<head) return HEADER;
		if (position-head<itemCount)return mAdapter.getItemViewType(position-head);
		return FOOTER;
	}

	public void addHeaderView(View headerView) {
        this.mHeaderView = headerView;
    }

    public void addFooterView(View footerView) {
        this.mFooterView = footerView;
    }

    public void addEmptyView(View emptyView) {
        this.mEmpterView = emptyView;
    }

    public void addAdapter(RecyclerView.Adapter adapter){
        mAdapter = adapter;
    }

	public View getmHeaderView() {
		return mHeaderView;
	}

	public View getmFooterView() {
		return mFooterView;
	}

	public View getmEmpterView() {
		return mEmpterView;
	}

	/**
	 * 瀑布流时将头部最外面的布局设置成铺满
	 *
	 * @param holder
	 */
	private void setWaterFall(BaseViewViewHolder holder) {
		StaggeredGridLayoutManager.LayoutParams clp =
				(StaggeredGridLayoutManager.LayoutParams)holder.getItemView().getLayoutParams();
		if (clp!=null)clp.setFullSpan(true);
	}
}
