package com.scott.su.smusic2.modules.play;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.BuildConfig;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.FragmentMusicPlayCoverBinding;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/8
 */

public class MusicPlayCoverFragment extends BaseFragment {
    private static final String KEY_EXTRA_SONG = "KEY_EXTRA_SONG";

    public static MusicPlayCoverFragment newInstance(LocalSongEntity song) {
        Bundle args = new Bundle();

        MusicPlayCoverFragment fragment = new MusicPlayCoverFragment();
        args.putSerializable(KEY_EXTRA_SONG, song);
        fragment.setArguments(args);
        return fragment;
    }

    private LocalSongEntity mSongEntity;
    private FragmentMusicPlayCoverBinding mBinding;

    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_play_cover, container, false);
        return mBinding.getRoot();
    }

    @Override
    protected void onInit() {
        mSongEntity = (LocalSongEntity) getArguments().getSerializable(KEY_EXTRA_SONG);

        if (mSongEntity == null) {
            mSongEntity = new LocalSongEntity();
        }

        loadCover();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCover();
    }

    private void loadCover() {
        ImageLoader.load(getActivity(), mSongEntity.getAlbumCoverPath(), mBinding.ivCover,
                R.drawable.pic_default_cover_album, R.drawable.pic_default_cover_album, false);
    }

}
