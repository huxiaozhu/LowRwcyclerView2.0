
package com.liuxiaozhu.recyclerviewlib.callbacks;

import android.view.View;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;


/**
 * Created by liuxiaozhu on 2017/5/31.
 * All Rights Reserved by YiZu
 * Item长按点击事件的接口
 */

public interface OnItemLongClick {

    void onItemLongClick(int position, View view, BaseViewHoloder holder);
}
