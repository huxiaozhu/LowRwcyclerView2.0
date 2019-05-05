
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 * 横向滑动的GridView适配器
 */

public abstract class HGridViewAdapter<T> extends BaseAdapter<T> {
    public HGridViewAdapter(List<T> data, Context context, int spanCount) {
        super(data, context,spanCount);
    }
}
