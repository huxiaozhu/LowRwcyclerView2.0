
package com.huxiaozhu.recyclerviewlib.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHolder;

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

public abstract class ListViewAdapter<T> extends BaseAdapter<T> {

    protected ListViewAdapter(List<T> data, Context context) {
        super(data, context);
    }
}
