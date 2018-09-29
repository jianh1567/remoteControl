package com.wind.control.util;

import android.view.View;

/**
 * 列表 Item 点击事件传递接口
 * Created by Cheny on 2017/12/21.
 */

public interface OnItemClickLisnter<T> {
    void onItemClick(View view, int position, T data);
}
