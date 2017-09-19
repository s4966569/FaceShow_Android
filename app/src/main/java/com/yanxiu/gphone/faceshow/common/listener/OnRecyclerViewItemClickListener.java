package com.yanxiu.gphone.faceshow.common.listener;

/**
 * RecyclerView的item点击事件
 * Created by 戴延枫 on 2017/9/15.
 */

import com.yanxiu.gphone.faceshow.base.BaseBean;

public interface OnRecyclerViewItemClickListener<T extends BaseBean> {

    void onItemClick(int position,T t);
}
