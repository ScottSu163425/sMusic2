package com.scott.su.smusic2.modules.play;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.util.ActivityStarter;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.ActivityMusicPlayBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 音乐播放详情
 * 作者: Su
 * 日期: 2018/5/3
 */

public class MusicPlayActivity extends BaseActivity {
    private static final String KEY_EXTRA_SONG_LIST = "KEY_EXTRA_SONG_LIST";
    private static final String KEY_EXTRA_SONG = "KEY_EXTRA_SONG";


    public static void start(Context context, ArrayList<LocalSongEntity> songList,
                             LocalSongEntity currentSong, @Nullable View[] shareElements) {

        Intent intent = getStartIntent(context, songList, currentSong, shareElements);

        if (shareElements == null || shareElements.length == 0) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, shareElements);
    }

    public static Intent getStartIntent(Context context, ArrayList<LocalSongEntity> songList,
                                        LocalSongEntity currentSong, @Nullable View[] shareElements) {
        Intent intent = new Intent(context, MusicPlayActivity.class);
        intent.putExtra(KEY_EXTRA_SONG_LIST, songList);
        intent.putExtra(KEY_EXTRA_SONG, currentSong);

        return intent;
    }


    private ActivityMusicPlayBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_music_play);


    }


}
