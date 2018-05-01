package com.scott.su.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class BaseRecyclerViewHolder<E> extends RecyclerView.ViewHolder {

    public abstract void bind(E entity, int position);

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    protected View findViewById(int id) {
        return itemView.findViewById(id);
    }
}
