package com.scott.su.smusic2.data.source.local;

import android.content.Context;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/30
 */

public interface ILocalSongDataSource {
    public List<LocalSongEntity> getAll(Context context);


}
