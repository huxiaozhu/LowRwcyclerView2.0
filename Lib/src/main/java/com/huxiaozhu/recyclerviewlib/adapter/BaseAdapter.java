
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.R;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewViewHolder;
import com.huxiaozhu.recyclerviewlib.callbacks.IEmptyView;
import com.huxiaozhu.recyclerviewlib.callbacks.IFooterView;
import com.huxiaozhu.recyclerviewlib.callbacks.IHeaderView;
import com.huxiaozhu.recyclerviewlib.callbacks.IPullLoading;
import com.huxiaozhu.recyclerviewlib.wedgit.RecyclerDivider;
import com.huxiaozhu.recyclerviewlib.widget.ExpandRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/15.
 * All Rights Reserved by YiZu
 * RecyclerView的适配器的基类
 * 所有的adapter必须继承该类
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewViewHolder> {
    //存放列表数据
    protected List<T> mData;
    //网格默认为2
    protected int mNunColumns = 2;

    protected Context mContext;
    //列表Item的Id
    protected int mLayoutId = 0;
    //存放头部、尾部的ViewId集合
//    private List<Integer> mHeaderView;
//    private List<Integer> mFooterView;

    //EmptyView的Id
    protected int noDataViewId = R.layout.no_data;
    //是否显示EmptyView，默认显示
    protected boolean isShowEmptyView = false;
    //主要来判断RecyclerView有没有数据，false表示recyclerView中有数据
    // (主要和isShowEmptyView配合使用，控制是否显示EmptyView)
    protected boolean isNoData = false;

    //上拉加载从第几个位置加载（位置包括HeaderView和FooterView）
    //默认recyclerView的Itemsize大于等于10的时候上啦加载
    //// TODO: 2017/8/17 上啦加载待优化
    protected int pullLoadingPosition = 10;
    public boolean isLoading = false;

    //HeaderView回掉接口，用来设置HeaderView
    protected IHeaderView mIHeaderView;
    //FooterView回掉接口，用来设置FooterView
    protected IFooterView mIFootView;
    //EmptyView回掉接口,主要用来设置EmptyView
    protected IEmptyView mIEmptyView;
    //PullToLoading回掉接口，主要实现无感上拉刷新
    // ，当绑定最后一个数据的时候会执行该接口的回掉方法
    protected IPullLoading mIPullLoading;

    private ClickLisiner clickLisiner;

    /**
     * 通用的构造方法
     * 这个方法主要是为LowRecyclerView中的所有适配器提供公共的方法和数据
     *
     * @param data
     */
    public BaseAdapter(List<T> data, Context context) {
        if (data != null) {
            mData = data;
        } else {
            mData = new ArrayList<>();
        }
//        if (recyclerView == null) {
//            throw new NullPointerException("RecyclerView is not null");
//        }
//        mRecyclerView = recyclerView;
        this.mContext = context;
        mLayoutId = getItemLayoutId();
//        if (mFooterView == null) {
//            mFooterView = new ArrayList<>();
//        }
//        if (mHeaderView == null) {
//            mHeaderView = new ArrayList<>();
//        }
//        mRecyclerView.setAdapter(this);
    }

    /**
     * @return 必须返回itemView的布局id
     */
    protected abstract @LayoutRes
    int getItemLayoutId();

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
    public BaseViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * 绑定Item数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewViewHolder holder, int position) {
    }


    /**
     * 设置Item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * 设置每个Item的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
     * ********设置HeaderView、FooterView和PullToLoading（上拉加载）***********
     */

//    /**
//     * 获取HeaderView集合
//     *
//     * @return
//     */
//    public List<Integer> getHeaderView() {
//        return mHeaderView;
//    }
//
//    /**
//     * 添加HeaderView集合
//     *
//     * @param headerViewId
//     */
//    public void addHeaderView(List<Integer> headerViewId, IHeaderView IHeaderView) {
//        if (IHeaderView == null && headerViewId == null) {
//            throw new RuntimeException("IIHeaderView或者headerViewId不能为空");
//        }
//        mHeaderView.addAll(headerViewId);
//        this.mIHeaderView = IHeaderView;
//    }
//
//    /**
//     * 添加一个HeaderView
//     *
//     * @param headerViewId
//     */
//    public void addHeaderView(@LayoutRes int headerViewId, IHeaderView IHeaderView) {
//        if (IHeaderView == null) {
//            throw new RuntimeException("IIHeaderView不能为空");
//        }
//        mHeaderView.add(headerViewId);
//        this.mIHeaderView = IHeaderView;
//    }
//
//    /**
//     * 获取FooterView
//     *
//     * @return
//     */
//    public List<Integer> getFooterView() {
//        return mFooterView;
//    }
//
//    /**
//     * 添加FooterView集合
//     *
//     * @param footerViewIdList
//     */
//    public void addFooterView(List<Integer> footerViewIdList, IFooterView IFooterView) {
//        if (IFooterView == null && footerViewIdList == null) {
//            throw new RuntimeException("IIHeaderView或者headerViewId不能为空");
//        }
//        mFooterView.addAll(footerViewIdList);
//        this.mIFootView = IFooterView;
//    }
//
//    /**
//     * 添加单个FooterView
//     *
//     * @param footerViewId
//     */
//    public void addFooterView(@LayoutRes int footerViewId, IFooterView IFooterView) {
//        if (IFooterView == null) {
//            throw new RuntimeException("IIHeaderView不能为空");
//        }
//        mFooterView.add(footerViewId);
//        this.mIFootView = IFooterView;
//    }

    /**
     * 上拉加载,当加载到最后一条Item的时候，会执行接口的回掉方法，
     * 且当最后一个Item的posiotion大于等于PullLoadStartPosiotion才会执行
     *
     * @param pullToLoading 回掉接口
     * @param num           RecyclerView的Item数量大于PullLoadStartPosiotion执行上拉加载
     */
    public void setPullToData(int num, boolean isLoading, IPullLoading pullToLoading) {
        if (pullToLoading != null) {
            this.isLoading = isLoading;
            mIPullLoading = pullToLoading;
            if (num > 1) {
                pullLoadingPosition = num;
            } else {
                throw new RuntimeException("setPullToData()的PullLoadStartPosiotion不能小于0");
            }
        } else {
            throw new RuntimeException("setPullToData()的pullToLoading不能为空");
        }

    }

    /**
     * *****************************设置EmptyView*************************
     */

    /**
     * 设置EmptyView的布局
     *
     * @param layoutId ：布局Id
     */
    public void setEmptyViewId(@LayoutRes int layoutId) {
        noDataViewId = layoutId;
    }

    /**
     * 设置EmptyView
     *
     * @param iEmptyView 接口
     */
    public void setEmptyView(IEmptyView iEmptyView) {
        this.mIEmptyView = iEmptyView;
    }

    /**
     * 设置是否显示EmptyView
     *
     * @param showEmptyView true：显示，false隐藏
     */
    public void setShowEmptyView(boolean showEmptyView) {
        isShowEmptyView = showEmptyView;
    }


    /**
     * ***************************处理RecyclerView返回Item数量以及每个Item返回的类型的公共方法*************
     */

    /**
     * 设置ItemType的类型
     *
     * @param posiotion
     * @return 返回类型说明：
     * 1.0-9999代表添加了HeaderView
     * 2.10000代表列表数据（HeaderView和FooterView之间的数据）
     * 3.20000-29999代表添加了FooterView
     * 4.40000代表显示没有数据的默认布局
     */
    protected int setItemType(int posiotion) {
//        int type = 0;
//        if (isNoData) {
//            //没有数据
//            type = 40000;
//        } else {
//            if (posiotion < mHeaderView.size()) {
//                //添加了HeaderView
//                type = posiotion;
//            } else if (posiotion < mHeaderView.size() + mData.size()) {
//                //
//                type = 10000;
//            } else {
//                if (posiotion < mHeaderView.size() + mData.size() + mFooterView.size()) {
//                    //添加了FooterView
//                    type = (20000 + posiotion - mHeaderView.size() - mData.size());
//                }
//            }
//        }
        return posiotion;
    }

    /**
     * 设置Item的总数
     * 主要是getItemCount（）方法来调用
     *
     * @return ItemCount Irem的数量
     */
    protected int setItemCount() {
        int size;
//        size = mHeaderView.size() + mData.size() + mFooterView.size();
        size =  mData.size();
        if (size == 0 && isShowEmptyView) {
            isNoData = true;
            size = 1;
        }
        return size;
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
        void onClick(int position, BaseViewViewHolder holder);
    }

    public interface OnLongClick {
        void onLongClick(int position, BaseViewViewHolder holder);
    }

    public interface OnItemClick {
        void onItemClick(int position, BaseViewViewHolder holder);
    }

    public interface OnItemLongClick {
        void onItemLongClick(int position, BaseViewViewHolder holder);
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
    protected void setClickLisiner(final BaseViewViewHolder holder, final int position) {
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

//    /**
//     * 分割线
//     * @param dividerHeight
//     * @param dividerColor
//     */
//    public void addItemDecoration(int dividerHeight, @ColorRes int dividerColor) {
//        if (getData().size()==0) {
//            //没有数据不绘制分割线
//        } else {
//            mRecyclerView.addItemDecoration(new RecyclerDivider(dividerHeight,
//                    this, mContext.getResources().getColor(dividerColor), mNunColumns));
//        }
//    }

	public int getNumColums() {
		return mNunColumns;
	}

}
