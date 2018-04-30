package com.scott.su.smusic2.modules.main.song;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.adapter.BaseRecyclerViewAdapter;
import com.scott.su.common.adapter.BaseRecyclerViewHolder;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.common.LocalSongEntity;
import com.scott.su.smusic2.databinding.ItemSongMainBinding;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/29
 */

public class MainTabSongListAdapter
        extends BaseRecyclerViewAdapter<LocalSongEntity, MainTabSongListAdapter.VH> {

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(getLayoutInflater().inflate(R.layout.item_song_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getData(position), position);
        holder.setCallback(getCallback());
    }


    static class VH extends BaseRecyclerViewHolder<LocalSongEntity> {
        private ItemSongMainBinding binding;


        public VH(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalSongEntity entity, final int position) {
            //test
            binding.ivCover.setImageResource(R.drawable.ic_default_cover_song);
            binding.tvName.setText("听妈妈的话");
            binding.tvArtist.setText("周杰伦");
            binding.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onItemClick(v, entity, position);
                }
            });
            binding.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onMoreClick(v, entity, position);
                }
            });


            binding.executePendingBindings();
        }

        private Callback mCallback;

        public void setCallback(Callback callback) {
            this.mCallback = callback;
        }

        private Callback getCallback() {
            if (mCallback == null) {
                mCallback = new Callback() {
                    @Override
                    public void onItemClick(View view, LocalSongEntity entity, int position) {

                    }

                    @Override
                    public void onMoreClick(View view, LocalSongEntity entity, int position) {

                    }
                };
            }
            return mCallback;
        }

        public interface Callback {

            void onItemClick(View view, LocalSongEntity entity, int position);

            void onMoreClick(View view, LocalSongEntity entity, int position);
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
                public void onItemClick(View view, LocalSongEntity entity, int position) {

                }

                @Override
                public void onMoreClick(View view, LocalSongEntity entity, int position) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback extends VH.Callback {
    }

}
