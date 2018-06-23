package com.scott.su.smusic2.modules.collection.select;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.adapter.BaseRecyclerViewAdapter;
import com.scott.su.common.adapter.BaseRecyclerViewHolder;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.databinding.ItemCollectionSelectBinding;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/23
 */

public abstract class CollectionSelectListAdapter
        extends BaseRecyclerViewAdapter<LocalCollectionEntity, CollectionSelectListAdapter.VH> {


    public abstract void onItemClick(View view, LocalCollectionEntity entity, int position);

    public CollectionSelectListAdapter(Context context) {
        super(context);
    }

    @Override
    protected VH onCreateVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_collection_select, parent, false));
    }

    @Override
    protected void onBindVH(LocalCollectionEntity entity, @NonNull VH holder, int position) {
        holder.bind(entity, position);
    }

    class VH extends BaseRecyclerViewHolder<LocalCollectionEntity> {
        private ItemCollectionSelectBinding binding;

        public VH(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalCollectionEntity entity, final int position) {
            ImageLoader.load(getContext(), entity.getCoverPath(), binding.ivCover);

            binding.setEntity(entity);
            binding.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, entity, position);
                }
            });
            binding.executePendingBindings();
        }


    }
}
