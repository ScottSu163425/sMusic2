package com.scott.su.smusic2.modules.album;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scott.su.common.adapter.BaseRecyclerViewAdapter;
import com.scott.su.common.adapter.BaseRecyclerViewHolder;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.databinding.ItemAlbumMainBinding;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/5/16
 */

public class MainTabAlbumListAdapter
        extends BaseRecyclerViewAdapter<LocalAlbumEntity, MainTabAlbumListAdapter.VH> {

    public MainTabAlbumListAdapter(Context context) {
        super(context);
    }

    @Override
    protected VH onCreateVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VH(getLayoutInflater().inflate(R.layout.item_album_main, parent, false));
    }

    @Override
    protected void onBindVH(LocalAlbumEntity entity, @NonNull VH holder, int position) {
        holder.bind(entity, position);
    }

    class VH extends BaseRecyclerViewHolder<LocalAlbumEntity> {
        private ItemAlbumMainBinding binding;

        public VH(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalAlbumEntity entity, final int position) {
            ImageLoader.load(getContext(), entity.getAlbumCoverPath(), binding.ivCover);

            binding.setEntity(entity);

            binding.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onItemClick(v, binding.ivCover, entity, position);
                }
            });

            binding.executePendingBindings();
        }

    }


    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private Callback getCallback() {
        if (mCallback == null) {
            mCallback = new Callback() {
                @Override
                public void onItemClick(View itemView, ImageView cover, LocalAlbumEntity entity, int position) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onItemClick(View itemView, ImageView cover, LocalAlbumEntity entity, int position);
    }

}
