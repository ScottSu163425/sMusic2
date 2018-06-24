package com.scott.su.smusic2.modules.common;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.adapter.BaseRecyclerViewAdapter;
import com.scott.su.common.adapter.BaseRecyclerViewHolder;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.ItemSongCommonBinding;

/**
 * 描述: 一般歌曲列表适配器
 * 作者: su
 * 日期: 2018/5/14
 */

public class CommonSongListAdapter
        extends BaseRecyclerViewAdapter<LocalSongEntity, CommonSongListAdapter.VH> {


    public CommonSongListAdapter(Context context) {
        super(context);
    }

    @Override
    protected VH onCreateVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_song_common, parent, false));
    }

    @Override
    protected void onBindVH(LocalSongEntity entity, @NonNull VH holder, int position) {
        holder.bind(entity, position);
    }

    class VH extends BaseRecyclerViewHolder<LocalSongEntity> {
        private ItemSongCommonBinding binding;

        public VH(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalSongEntity entity, final int position) {
            binding.setPosition(position);
            binding.setPositionSingleSelected(getSingleSelectedPosition());
            binding.setEntity(entity);
            binding.setCallback(getCallback());

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
                public void onItemClick(View itemView, LocalSongEntity entity, int position) {

                }

                @Override
                public void onMoreClick(View itemView, LocalSongEntity entity, int position) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onItemClick(View itemView, LocalSongEntity entity, int position);

        void onMoreClick(View itemView, LocalSongEntity entity, int position);
    }

}
