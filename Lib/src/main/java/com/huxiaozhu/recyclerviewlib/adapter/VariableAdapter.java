
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 */

public abstract class VariableAdapter<T> extends BaseAdapter<T> {
    public VariableAdapter(List<T> data, Context context, int spanCount) {
        super(data, context,spanCount);
    }
}
