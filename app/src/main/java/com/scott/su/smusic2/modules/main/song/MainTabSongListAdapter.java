package com.scott.su.smusic2.modules.main.song;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scott.su.common.adapter.BaseRecyclerViewAdapter;
import com.scott.su.common.adapter.BaseRecyclerViewHolder;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.ItemSongMainBinding;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/29
 */

public class MainTabSongListAdapter
        extends BaseRecyclerViewAdapter<LocalSongEntity, MainTabSongListAdapter.VH> {

    public MainTabSongListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(getLayoutInflater().inflate(R.layout.item_song_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getData(position), position);
    }


    class VH extends BaseRecyclerViewHolder<LocalSongEntity> {
        private ItemSongMainBinding binding;

        public VH(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalSongEntity entity, final int position) {
            ImageLoader.load(getContext(), entity.getAlbumCoverPath(), binding.ivCover);

            binding.tvName.setText(entity.getTitle());
            binding.tvArtist.setText(entity.getArtist());

            binding.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onItemClick(v, binding.ivCover, entity, position);
                }
            });

            binding.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onMoreClick(v, binding.ivCover, entity, position);
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
                public void onItemClick(View itemView, ImageView cover, LocalSongEntity entity, int position) {

                }

                @Override
                public void onMoreClick(View itemView, ImageView cover, LocalSongEntity entity, int position) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onItemClick(View itemView, ImageView cover, LocalSongEntity entity, int position);

        void onMoreClick(View itemView, ImageView cover, LocalSongEntity entity, int position);
    }

}
