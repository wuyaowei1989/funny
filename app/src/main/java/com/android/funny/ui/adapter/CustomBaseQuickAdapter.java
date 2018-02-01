package com.android.funny.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */

public abstract  class CustomBaseQuickAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public CustomBaseQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        customConvert(helper, item);
    }

    abstract public void customConvert(BaseViewHolder holder, T item);

}