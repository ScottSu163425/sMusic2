package com.scott.su.smusic2.modules.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

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

    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_play_cover, container, false);
    }

    @Override
    protected void onInit() {
        ImageView iv = (ImageView) findViewById(R.id.iv_cover);
        LocalSongEntity song = (LocalSongEntity) getArguments().getSerializable(KEY_EXTRA_SONG);

        if (song == null) {
            song = new LocalSongEntity();
        }

        ImageLoader.load(getActivity(), song.getAlbumCoverPath(), iv);
    }

}
