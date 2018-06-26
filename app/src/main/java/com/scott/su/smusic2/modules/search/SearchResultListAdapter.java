package com.scott.su.smusic2.modules.search;

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
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.ItemAlbumSearchResultBinding;
import com.scott.su.smusic2.databinding.ItemCollectionSearchResultBinding;
import com.scott.su.smusic2.databinding.ItemSongSearchResultBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/26
 */

public class SearchResultListAdapter extends BaseRecyclerViewAdapter<Object, BaseRecyclerViewHolder<Object>> {
    public static final int TYPE_SUBHEAD = 1;
    public static final int TYPE_SONG = 2;
    public static final int TYPE_COLLECTION = 3;
    public static final int TYPE_ALBUM = 4;

    public SearchResultListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUBHEAD) {
            return new VHSubhead(inflater.inflate(R.layout.item_subhead_search_result, parent, false));
        }

        if (viewType == TYPE_SONG) {
            return new VHSong(inflater.inflate(R.layout.item_song_search_result, parent, false));
        }


        if (viewType == TYPE_COLLECTION) {
            return new VHCollection(inflater.inflate(R.layout.item_collection_search_result, parent, false));
        }


        if (viewType == TYPE_ALBUM) {
            return new VHAlbum(inflater.inflate(R.layout.item_album_search_result, parent, false));
        }

        return null;
    }

    @Override
    protected void onBindVH(Object entity, @NonNull BaseRecyclerViewHolder holder, int position) {
        holder.bind(entity, position);
    }

    @Override
    public int getItemViewType(int position) {
        Object entity = getData(position);


        if (entity != null) {
            if (entity instanceof String) {
                return TYPE_SUBHEAD;
            }

            if (entity instanceof LocalSongEntity) {
                return TYPE_SONG;
            }

            if (entity instanceof LocalCollectionEntity) {
                return TYPE_COLLECTION;
            }

            if (entity instanceof LocalAlbumEntity) {
                return TYPE_ALBUM;
            }
        }

        return super.getItemViewType(position);
    }

    class VHSubhead extends BaseRecyclerViewHolder<String> {
        TextView tv;

        public VHSubhead(View itemView) {
            super(itemView);

            tv = (TextView) findViewById(R.id.tv);
        }

        @Override
        public void bind(String entity, int position) {
            tv.setText(entity);
        }
    }

    class VHSong extends BaseRecyclerViewHolder<LocalSongEntity> {
        private ItemSongSearchResultBinding binding;

        public VHSong(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bind(final LocalSongEntity entity, final int position) {
            ImageLoader.load(getContext(), entity.getAlbumCoverPath(), binding.ivCover);

            binding.setEntity(entity);
            binding.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().onItemClickSong(v, binding.ivCover, entity, position);
                }
            });

            binding.executePendingBindings();
        }

    }


    class VHCollection extends BaseRecyclerViewHolder<LocalCollectionEntity> {
        private ItemCollectionSearchResultBinding binding;

        public VHCollection(View itemView) {
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
                    getCallback().onItemClickCollection(v, binding.ivCover, entity, position);
                }
            });

            binding.executePendingBindings();
        }

    }

    class VHAlbum extends BaseRecyclerViewHolder<LocalAlbumEntity> {
        private ItemAlbumSearchResultBinding binding;

        public VHAlbum(View itemView) {
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
                    getCallback().onItemClickAlbum(v, binding.ivCover, entity, position);
                }
            });

            binding.executePendingBindings();
        }

    }

    public List<LocalSongEntity> getSongList() {
        List<LocalSongEntity> list = new ArrayList<>();

        if (!isEmpty()) {
            for (int i = 0, n = getItemCount(); i < n; i++) {
                Object entity = getData(i);

                if (entity instanceof LocalSongEntity) {
                    list.add((LocalSongEntity) entity);
                }
            }
        }

        return list;
    }


    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private Callback getCallback() {
        if (mCallback == null) {
            mCallback = new Callback() {
                @Override
                public void onItemClickSong(View view, ImageView cover, LocalSongEntity entity, int position) {

                }

                @Override
                public void onItemClickCollection(View view, ImageView cover, LocalCollectionEntity entity, int position) {

                }

                @Override
                public void onItemClickAlbum(View view, ImageView cover, LocalAlbumEntity entity, int position) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onItemClickSong(View view, ImageView cover, LocalSongEntity entity, int position);

        void onItemClickCollection(View view, ImageView cover, LocalCollectionEntity entity, int position);

        void onItemClickAlbum(View view, ImageView cover, LocalAlbumEntity entity, int position);

    }

}
