
package com.huxiaozhu.recyclerviewlib.callbacks;

import android.view.View;

/**
 * Created by liuxiaozhu on 2017/7/19.
 * All Rights Reserved by YiZu
 * FooterView接口的回掉，将foot传到外部交给用户处理
 */

public interface IFooterView {
    void FooterView(View foot, int position);
}
