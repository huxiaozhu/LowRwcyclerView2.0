
package com.liuxiaozhu.recyclerviewlib.callbacks;

import android.view.View;

import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;


/**
 * Created by liuxiaozhu on 2017/5/31.
 * All Rights Reserved by YiZu
 * 子view长按点击事件接口
 */

public interface OnLongClick {

    void onLongClick(int position, View view, BaseViewHoloder holder);
}
