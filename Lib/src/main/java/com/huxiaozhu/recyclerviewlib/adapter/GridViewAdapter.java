package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.view.ViewGroup;


import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 * GridView的适配器
 */

public abstract class GridViewAdapter<T> extends BaseAdapter<T> {
    public GridViewAdapter(List<T> data, Context context, int spanCount) {
        super(data,context,spanCount);
    }
}
