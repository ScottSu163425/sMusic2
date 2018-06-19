package com.scott.su.smusic2.modules.album;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.ActivityStarter;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.databinding.ActivityAlbumDetailBinding;

/**
 * 描述: 专辑详情
 * 作者: su
 * 日期: 2018/6/19
 */

public class AlbumDetailActivity extends BaseActivity {
    private static final String KEY_EXTRA_ALBUM_ID = "KEY_EXTRA_ALBUM_ID";


    public static void start(Context context, long albumId, @Nullable View shareElement) {
        Intent intent = getStartIntent(context, albumId);

        if (shareElement == null) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, new View[]{shareElement});
    }

    public static Intent getStartIntent(Context context, long albumId) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(KEY_EXTRA_ALBUM_ID, albumId);

        return intent;
    }

    private long mAlbumId;
    private AlbumDetailViewModel mViewModel;
    private ActivityAlbumDetailBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album_detail);

        mAlbumId = getIntent().getLongExtra(KEY_EXTRA_ALBUM_ID, 0);

        mViewModel = ViewModelProviders.of(this).get(AlbumDetailViewModel.class);
        mViewModel.setAlbumId(mAlbumId);
        mViewModel.getLiveDataAlbum().observe(this, new Observer<LocalAlbumEntity>() {
            @Override
            public void onChanged(@Nullable LocalAlbumEntity localAlbumEntity) {
                setUpData(localAlbumEntity);
            }
        });

        mViewModel.start();
    }

    private void setUpData(LocalAlbumEntity albumEntity){
        if (albumEntity == null) {
            return;
        }

        ImageLoader.load(getApplicationContext(),albumEntity.getAlbumCoverPath(),mBinding.ivCover);
    }

}
