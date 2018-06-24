package com.scott.su.smusic2.modules.collection.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.interfaces.SimpleCallback;
import com.scott.su.common.manager.ActivityStarter;
import com.scott.su.common.manager.AlertDialogHelper;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.manager.SnackBarMaker;
import com.scott.su.common.manager.ToastMaker;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.entity.event.CollectionsNeedRefreshEvent;
import com.scott.su.smusic2.databinding.ActivityCollectionDetailBinding;
import com.scott.su.smusic2.modules.collection.select.CollectionSelectDialogFragment;
import com.scott.su.smusic2.modules.common.CommonSongListAdapter;
import com.scott.su.smusic2.modules.common.SongItemPopupMenu;
import com.scott.su.smusic2.modules.play.MusicPlayActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 收藏夹详情
 * 作者: Su
 * 日期: 2018/6/22
 */

public class CollectionDetailActivity extends BaseActivity {
    private static final String KEY_EXTRA_COLLECTION_ID = "KEY_EXTRA_COLLECTION_ID";

    public static void start(Context context, String collectionId, @Nullable View[] shareElements) {
        Intent intent = getStartIntent(context, collectionId);

        if (shareElements == null || shareElements.length == 0) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, shareElements);
    }

    public static Intent getStartIntent(Context context, String collectionId) {
        Intent intent = new Intent(context, CollectionDetailActivity.class);
        intent.putExtra(KEY_EXTRA_COLLECTION_ID, collectionId);

        return intent;
    }

    private ActivityCollectionDetailBinding mBinding;
    private CollectionDetailViewModel mViewModel;
    private CommonSongListAdapter mSongListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collection_detail);

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

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FAB可见说明收藏夹不为空
                MusicPlayActivity.start(getActivity(), (ArrayList<LocalSongEntity>) mSongListAdapter.getData(),
                        mSongListAdapter.getData().get(0), new View[]{mBinding.ivCover});
            }
        });

        mViewModel = ViewModelProviders.of(this).get(CollectionDetailViewModel.class);
        mViewModel.setCollectionId(getIntent().getStringExtra(KEY_EXTRA_COLLECTION_ID));
        mViewModel.getLiveDataCollection().observe(this, new Observer<LocalCollectionEntity>() {
            @Override
            public void onChanged(@Nullable LocalCollectionEntity entity) {
                setUpCollection(entity);
            }
        });
        mViewModel.getLiveDataCollectionSongs().observe(this, new Observer<List<LocalSongEntity>>() {
            @Override
            public void onChanged(@Nullable List<LocalSongEntity> localSongEntities) {
                setUpCollectionSongs(localSongEntities);
            }
        });
        mViewModel.getLiveDataRemoveSongSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                EventBus.getDefault().post(new CollectionsNeedRefreshEvent());
            }
        });
        mViewModel.getLiveDataClearSongSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mSongListAdapter.clear();
                ImageLoader.load(getApplicationContext(), "", mBinding.ivCover);
                mBinding.fab.setVisibility(View.GONE);
                EventBus.getDefault().post(new CollectionsNeedRefreshEvent());
            }
        });
        mViewModel.getLiveDataDeleteCollectionSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                EventBus.getDefault().post(new CollectionsNeedRefreshEvent());
                finish();
            }
        });

        mViewModel.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_collect) {
            if (mSongListAdapter.isEmpty()) {
                return true;
            }

            AlertDialogHelper.show(getActivity(), getString(R.string.ask_clear_collection_songs), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.clearSong();
                }
            });

        } else if (id == R.id.action_delete_collect) {
            AlertDialogHelper.show(getActivity(), getString(R.string.ask_delete_collection), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deleteCollection();
                }
            });
        }

        return true;
    }

    private void setUpCollection(@NonNull LocalCollectionEntity collection) {
        ImageLoader.load(getApplicationContext(), collection.getCoverPath(), mBinding.ivCover);

        mBinding.toolbar.setTitle(collection.getCollectionName());
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpCollectionSongs(@NonNull List<LocalSongEntity> songs) {
        mSongListAdapter.setData(songs);
        mBinding.fab.setVisibility(mSongListAdapter.isEmpty() ? View.GONE : View.VISIBLE);

        if (mSongListAdapter.isEmpty()) {
            SnackBarMaker.showSnackBar(mBinding.rv, getString(R.string.empty_collection_song));
        }
    }

    private void playSong(@NonNull LocalSongEntity song, int position) {
        MusicPlayActivity.start(getActivity(), (ArrayList<LocalSongEntity>) mSongListAdapter.getData(), song,
                position == 0 ? new View[]{mBinding.ivCover} : null);
    }

    private void showItemMenu(View anchor, final LocalSongEntity song) {
        SongItemPopupMenu.showForCollection(getActivity(), anchor, song,
                new SimpleCallback<Void>() {
                    @Override
                    public void onCallback(Void aVoid) {
                        mViewModel.removeSong(song);
                    }
                },
                new CollectionSelectDialogFragment.Callback() {
                    @Override
                    public void onCollectSelected(LocalCollectionEntity collection) {
                        mViewModel.collectSong(collection, song);
                    }
                });
    }

}
