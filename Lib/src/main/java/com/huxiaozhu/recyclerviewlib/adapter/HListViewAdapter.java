
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;


import java.util.List;

/**
 * Created by liuxiaozhu on 2017/7/18.
 * All Rights Reserved by YiZu
 */

public abstract class HListViewAdapter<T> extends BaseAdapter<T> {
    public HListViewAdapter(List<T> data, Context context) {
        super(data, context);
    }
}
