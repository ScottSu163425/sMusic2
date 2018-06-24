package com.scott.su.smusic2.modules.album;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.ActivityStarter;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.manager.ToastMaker;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.entity.event.CollectionsNeedRefreshEvent;
import com.scott.su.smusic2.databinding.ActivityAlbumDetailBinding;
import com.scott.su.smusic2.modules.collection.select.CollectionSelectDialogFragment;
import com.scott.su.smusic2.modules.common.CommonSongListAdapter;
import com.scott.su.smusic2.modules.common.SongItemPopupMenu;
import com.scott.su.smusic2.modules.play.MusicPlayActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 描述: 专辑详情
 * 作者: su
 * 日期: 2018/6/19
 */

public class AlbumDetailActivity extends BaseActivity {
    private static final String KEY_EXTRA_ALBUM_ID = "KEY_EXTRA_ALBUM_ID";


    public static void start(Context context, String albumId, @Nullable View shareElement) {
        Intent intent = getStartIntent(context, albumId);

        if (shareElement == null) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, new View[]{shareElement});
    }

    public static Intent getStartIntent(Context context, String albumId) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(KEY_EXTRA_ALBUM_ID, albumId);

        return intent;
    }

    private String mAlbumId;
    private LocalAlbumEntity mAlbumEntity;
    private AlbumDetailViewModel mViewModel;
    private ActivityAlbumDetailBinding mBinding;
    private CommonSongListAdapter mSongListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlbumId = getIntent().getStringExtra(KEY_EXTRA_ALBUM_ID);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album_detail);

        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 20);

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSongListAdapter = new CommonSongListAdapter(getActivity());
        mSongListAdapter.setCallback(new CommonSongListAdapter.Callback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position) {
                playSong(entity, position);
            }

            @Override
            public void onMoreClick(View itemView, LocalSongEntity entity, int position) {
                showItemMenu(itemView, entity);
            }
        });
        mBinding.rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rv.setAdapter(mSongListAdapter);

        mViewModel = ViewModelProviders.of(this).get(AlbumDetailViewModel.class);
        mViewModel.setAlbumId(mAlbumId);
        mViewModel.getLiveDataAlbum().observe(this, new Observer<LocalAlbumEntity>() {
            @Override
            public void onChanged(@Nullable LocalAlbumEntity localAlbumEntity) {
                setUpData(localAlbumEntity);
            }
        });
        mViewModel.getLiveDataCollectSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    ToastMaker.showToast(getActivity(), getString(R.string.success_add_into_collection));
                    //更新收藏夹封面
                    EventBus.getDefault().post(new CollectionsNeedRefreshEvent());
                }
            }
        });

        mViewModel.getLiveDataCollectFailMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ToastMaker.showToast(getActivity(), s);
            }
        });

        mViewModel.start();
    }

    private void setUpData(@NonNull LocalAlbumEntity albumEntity) {
        if (albumEntity == null) {
            return;
        }

        mAlbumEntity = albumEntity;
        mSongListAdapter.setData(albumEntity.getAlbumSongs());

        ImageLoader.load(getApplicationContext(), albumEntity.getAlbumCoverPath(), mBinding.ivCover);
        mBinding.toolbar.setTitle(albumEntity.getTitle());
    }

    private void playSong(@NonNull LocalSongEntity song, int position) {
        MusicPlayActivity.start(getActivity(), (ArrayList<LocalSongEntity>) mAlbumEntity.getAlbumSongs(), song,
                position == 0 ? new View[]{mBinding.ivCover} : null);
    }

    private void showItemMenu(View anchor, final LocalSongEntity song) {
        SongItemPopupMenu.show(getActivity(), anchor, song, new CollectionSelectDialogFragment.Callback() {
            @Override
            public void onCollectSelected(LocalCollectionEntity collection) {
                mViewModel.collectSong(collection, song);
            }
        });
    }

}
