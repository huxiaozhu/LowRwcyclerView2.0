
package com.liuxiaozhu.recyclerviewlib.callbacks;

import android.view.View;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;


/**
 * Created by liuxiaozhu on 2016/12/23.
 * RcyclerView的点击事件传递到adapter里
 */

public interface IRcyclerClickBack {
    void onItemClickBack(int position, View view, BaseViewHoloder holder);
    void onLongItemClickBack(int position, View view, BaseViewHoloder holder);
    void onViewClick(int position, View view, BaseViewHoloder holder);
    void onViewLongClick(int position, View view, BaseViewHoloder holder);
}
