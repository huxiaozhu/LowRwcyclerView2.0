
package com.huxiaozhu.recyclerviewlib.callbacks;

import android.view.View;

/**
 * Created by liuxiaozhu on 2017/7/19.
 * All Rights Reserved by YiZu
 * HeaderView接口的回掉，将head传到外部交给用户处理
 */

public interface IHeaderView {
    void HeaderView(View head, int position);
}
