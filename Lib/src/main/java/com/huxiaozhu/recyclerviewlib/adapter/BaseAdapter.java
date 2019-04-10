
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by liuxiaozhu on 2017/7/15.
 * All Rights Reserved by YiZu
 * RecyclerView的适配器的基类
 * 所有的adapter必须继承该类
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    //存放列表数据
    private List<T> mData;
    //网格默认为2
    private int mNunColumns = 2;

    private Context mContext;
    //列表Item的Id
    private int mLayoutId = 0;

    private ClickLisiner clickLisiner;

    /**
     * 通用的构造方法
     * 这个方法主要是为LowRecyclerView中的所有适配器提供公共的方法和数据
     *
     * @param data
     */
    BaseAdapter(List<T> data, Context context) {
        mData = data != null ? data : (List<T>) new ArrayList<>();
        this.mContext = context;
        mLayoutId = getItemLayoutId();
    }

    BaseAdapter(List<T> data, Context context, int spanCount){
        this(data,context);
        if (spanCount > 2) mNunColumns = spanCount;
    }

    /**
     * @return 必须返回itemView的布局id
     */
    protected abstract @LayoutRes int getItemLayoutId();

    /**
     * *******RecyclerView.Adapter的四个方法，这里复写主要是让子类继承***************
     */

    /**
     * 创建Item的View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(mLayoutId, parent);
    }

    /**
     * 绑定Item数据
     *
     * @param baseViewHolder
     * @param i : position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        setData(baseViewHolder, i, mData.get(i));
        setClickLisiner(baseViewHolder, i);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 设置列表数据
     *
     * @param holder
     * @param position
     * @param item
     */
    public abstract void setData(BaseViewHolder holder, int position, T item);

    /**
     * 设置Item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置每个Item的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 获取Context
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * ***************************adapter公用的方法***************************************
     *  这些方法只能操作除HeaderView和FooterView以外的列表数据，
     *  但是刷新的时候刷新的是整个RecyclerView，包括HeaderView和FooterView
     */

    /**
     * 更新整个recycle
     *
     * @param data 要更新的数据集合不能为空
     */
    public void upData(List<T> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新整个recycle
     */
    public void upData() {
        notifyDataSetChanged();
    }


    /**
     * 删除指定的Item
     *
     * @param position 指的是集合的下标位置
     */
    public void removeData(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 删除全部数据（不包括HeaderView和FooterView）
     */
    public void removeAllData() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 在集合指定位置插入数据
     *
     * @param position 插入的位置的下标
     * @param data     插入的数据
     */
    public void insertData(int position, T data) {
        if (position < getItemCount()) {
            mData.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 列表集合末尾插入数据
     *
     * @param data 插入的数据
     */
    public void insertData(T data) {
        if (data == null) return;
        mData.add(data);
//        notifyItemInserted(mData.size() - 1);
        notifyDataSetChanged();
    }

    /**
     * 列表集合末尾插入数据集合
     *
     * @param data 插入的数据集合
     */
    public void insertDatas(List<T> data) {
        if (data != null) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * *******************************
     * 设置监听
     */
    public interface OnClick {
        void onClick(int position, BaseViewHolder holder);
    }

    public interface OnLongClick {
        void onLongClick(int position, BaseViewHolder holder);
    }

    public interface OnItemClick {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public interface OnItemLongClick {
        void onItemLongClick(int position, BaseViewHolder holder);
    }

    public void setOnClickListener(@IdRes int id, OnClick listener) {
        if (listener == null) throw new NullPointerException("OnClick不能为空");
        if (clickLisiner == null) clickLisiner = new ClickLisiner();
        clickLisiner.setClickId(id);
        clickLisiner.setOnClick(listener);
    }

    public void setOnLongClickListener(@IdRes int id, OnLongClick listener) {
        if (listener == null) throw new NullPointerException("OnClick不能为空");
        if (clickLisiner == null) clickLisiner = new ClickLisiner();
        clickLisiner.setLongClickId(id);
        clickLisiner.setOnLongClick(listener);
    }

    public void setOnItemClickListener(OnItemClick listener) {
        if (listener == null) throw new NullPointerException("OnClick不能为空");
        if (clickLisiner == null) clickLisiner = new ClickLisiner();
        clickLisiner.setOnItemClick(listener);
    }

    public void setOnItenLongClickListener(OnItemLongClick listener) {
        if (listener == null) throw new NullPointerException("OnClick不能为空");
        if (clickLisiner == null) clickLisiner = new ClickLisiner();
        clickLisiner.setOnItemLongClick(listener);
    }

    private class ClickLisiner {
        private OnClick onClick;
        private OnLongClick onLongClick;
        private OnItemClick onItemClick;
        private OnItemLongClick onItemLongClick;
        private int clickId;
        private int longClickId;

        public OnClick getOnClick() {
            return onClick;
        }

        public void setOnClick(OnClick onClick) {
            this.onClick = onClick;
        }

        public OnLongClick getOnLongClick() {
            return onLongClick;
        }

        public void setOnLongClick(OnLongClick onLongClick) {
            this.onLongClick = onLongClick;
        }

        public OnItemClick getOnItemClick() {
            return onItemClick;
        }

        public void setOnItemClick(OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        public OnItemLongClick getOnItemLongClick() {
            return onItemLongClick;
        }

        public void setOnItemLongClick(OnItemLongClick onItemLongClick) {
            this.onItemLongClick = onItemLongClick;
        }

        public int getClickId() {
            return clickId;
        }

        public void setClickId(int clickId) {
            this.clickId = clickId;
        }

        public int getLongClickId() {
            return longClickId;
        }

        public void setLongClickId(int longClickId) {
            this.longClickId = longClickId;
        }
    }

    /**
     * 子view实现
     *
     * @param holder
     * @param position
     */
    protected void setClickLisiner(final BaseViewHolder holder, final int position) {
        if (clickLisiner != null) {
            if (clickLisiner.getOnClick() != null) {
                View view = holder.getView(clickLisiner.clickId);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickLisiner.getOnClick().onClick(position, holder);
                    }
                });
            }
            if (clickLisiner.getOnLongClick() != null) {
                View view = holder.getView(clickLisiner.longClickId);
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        clickLisiner.getOnLongClick().onLongClick(position, holder);
                        return true;
                    }
                });
            }
            if (clickLisiner.getOnItemClick() != null) {
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickLisiner.getOnItemClick().onItemClick(position, holder);
                    }
                });
            }
            if (clickLisiner.getOnLongClick() != null) {
                holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        clickLisiner.getOnLongClick().onLongClick(position, holder);
                        return true;
                    }
                });
            }
        }
    }

	public int getNumColums() {
		return mNunColumns;
	}

}
