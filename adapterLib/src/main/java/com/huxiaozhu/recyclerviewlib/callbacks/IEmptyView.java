
package com.huxiaozhu.recyclerviewlib.callbacks;

import android.view.View;

/**
 * Created by liuxiaozhu on 2017/7/20.
 * All Rights Reserved by YiZu
 * 空数据时的接口回掉
 * 主要将EmptyView抛出adapter交给用户处理
 */

public interface IEmptyView {
    void setEmptyView(View empty);
}
