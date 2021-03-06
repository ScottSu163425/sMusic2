package com.scott.su.smusic2.modules.collection.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.common.adapter.BaseRecyclerViewAdapter;
import com.scott.su.common.adapter.BaseRecyclerViewHolder;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.databinding.ItemCollectionBinding;

/**
 * 描述: 收藏列表适配器
 * 作者: su
 * 日期: 2018/6/11
 */

public abstract class MainTabCollectionListAdapter
        extends BaseRecyclerViewAdapter<LocalCollectionEntity, MainTabCollectionListAdapter.VH> {

    abstract void onItemClick(View itemView, ImageView cover, LocalCollectionEntity entity, int position);

    abstract void onMoreClick(View view, LocalCollectionEntity entity, int position);

    public MainTabCollectionListAdapter(Context context) {
        super(context);
    }

    @Override
    protected VH onCreateVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_collection, parent, false));
    }

    @Override
    protected void onBindVH(LocalCollectionEntity entity, @NonNull VH holder, int position) {
        holder.bind(entity, position);
    }

    class VH extends BaseRecyclerViewHolder<LocalCollectionEntity> {
        private ItemCollectionBinding mBinding;


        public VH(View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalCollectionEntity entity, final int position) {
            mBinding.setEntity(entity);

            ImageLoader.load(getContext(), entity.getCoverPath(), mBinding.ivCover,
                    R.drawable.pic_default_cover_album, R.drawable.pic_default_cover_album, true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, mBinding.ivCover, entity, position);
                }
            });

            mBinding.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMoreClick(v, entity, position);
                }
            });

            mBinding.executePendingBindings();
        }
    }


}
