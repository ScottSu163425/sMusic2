package com.scott.su.smusic2.modules.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.SnackBarMaker;
import com.scott.su.common.util.KeyboardUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.entity.event.CollectionsNeedRefreshEvent;
import com.scott.su.smusic2.databinding.ActivitySearchBinding;
import com.scott.su.smusic2.modules.album.AlbumDetailActivity;
import com.scott.su.smusic2.modules.collection.detail.CollectionDetailActivity;
import com.scott.su.smusic2.modules.play.MusicPlayActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/26
 */

public class SearchActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(getStartIntent(context));
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    private ActivitySearchBinding mBinding;
    private SearchViewModel mViewModel;
    private SearchResultListAdapter mResultListAdapter;
    private int mListOffset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        mBinding.viewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.viewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        initResultList();

        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mViewModel.getLiveDataResult().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objects) {
                mResultListAdapter.setData(objects);
                mBinding.rv.scrollTo(0, mListOffset);
                mBinding.viewEmpty.setVisibility(mResultListAdapter.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        mViewModel.start();

        EventBus.getDefault().register(this);
    }

    private void initResultList() {
        mResultListAdapter = new SearchResultListAdapter(this);
        mResultListAdapter.setCallback(new SearchResultListAdapter.Callback() {
            @Override
            public void onItemClickSong(View view, ImageView cover, LocalSongEntity entity, int position) {
                MusicPlayActivity.start(getActivity(), (ArrayList<LocalSongEntity>) mResultListAdapter.getSongList(),
                        entity, new View[]{cover});
            }

            @Override
            public void onItemClickCollection(View view, ImageView cover, LocalCollectionEntity entity, int position) {
                CollectionDetailActivity.start(getActivity(), entity.getCollectionId(), new View[]{cover});
            }

            @Override
            public void onItemClickAlbum(View view, ImageView cover, LocalAlbumEntity entity, int position) {
                AlbumDetailActivity.start(getActivity(), entity.getAlbumId(), cover);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mResultListAdapter.getItemViewType(position);

                if (viewType == SearchResultListAdapter.TYPE_SUBHEAD
                        || viewType == SearchResultListAdapter.TYPE_SONG
                        || viewType == SearchResultListAdapter.TYPE_ALBUM) {

                    return 3;
                }

                return 1;
            }
        });
        mBinding.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                mListOffset = recyclerView.computeVerticalScrollOffset();
            }
        });
        mBinding.rv.setLayoutManager(layoutManager);
        mBinding.rv.setAdapter(mResultListAdapter);
    }

    private void search() {
        String input = mBinding.etSearch.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            SnackBarMaker.showSnackBar(mBinding.etSearch, getString(R.string.error_empty_search));
            return;
        }

        KeyboardUtil.closeKeyboard(getActivity());

        mViewModel.search(input);
    }

    @Subscribe
    public void onEventCollectionNeedRefresh(CollectionsNeedRefreshEvent event) {
        search();
    }

}
