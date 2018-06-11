package com.scott.su.smusic2.modules.main.collection;

import android.content.Context;
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

/**
 * 描述: 收藏列表适配器
 * 作者: su
 * 日期: 2018/6/11
 */

public class MainTabCollectionListAdapter
        extends BaseRecyclerViewAdapter<LocalCollectionEntity, MainTabCollectionListAdapter.VH> {

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
        private ImageView ivCover;
        private TextView tvTitle;
        private View viewMore;


        public VH(View itemView) {
            super(itemView);

            ivCover = (ImageView) findViewById(R.id.iv_cover);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            viewMore = findViewById(R.id.view_more);
        }

        @Override
        public void bind(final LocalCollectionEntity entity, final int position) {
            ImageLoader.load(getContext(), entity.getCoverPath(), ivCover,
                    R.drawable.pic_default_cover_album, R.drawable.pic_default_cover_album, true);
            tvTitle.setText(entity.getCollectionName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onItemClick(v, ivCover, entity, position);
                }
            });

            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onMoreClick(v, entity, position);
                }
            });
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
                public void onItemClick(View itemView, ImageView cover, LocalCollectionEntity entity, int position) {

                }

                @Override
                public void onMoreClick(View view, LocalCollectionEntity entity, int position) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onItemClick(View itemView, ImageView cover, LocalCollectionEntity entity, int position);

        void onMoreClick(View view, LocalCollectionEntity entity, int position);
    }

}
