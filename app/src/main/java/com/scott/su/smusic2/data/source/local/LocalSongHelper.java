package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/30
 */

public class LocalSongHelper {

    /**
     * 获取本地音乐列表
     *
     * @param context
     * @return
     */
    public static List<LocalSongEntity> getLocalSongs(@NonNull Context context) {
        List<LocalSongEntity> songList = new ArrayList<>();

        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return songList;
        }

        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0) {
                continue;
            }

            //Get entity from com.scott.su.smusic.cache directly if it exists in com.scott.su.smusic.cache.
            long songId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
//            LocalSongEntity entityFromCache = LocalSongEntityCache.getInstance().get(songId + "");
//            if (entityFromCache != null) {
//                songEntities.add(entityFromCache);
//                continue;
//            }

            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            LocalSongEntity localSongEntity = new LocalSongEntity();
            localSongEntity.setSongId(songId);
            localSongEntity.setTitle(title);
            localSongEntity.setArtist(artist);
            localSongEntity.setAlbum(album);
            localSongEntity.setAlbumId(albumId);
            localSongEntity.setDuration(duration);
            localSongEntity.setSize(size);
            localSongEntity.setPath(path);

            //Get and set covert path from LruCache if exists,otherwise com.scott.su.smusic.cache it.
//            String coverPath = CoverPathCache.getInstance().get(albumId + "");
//            if (TextUtils.isEmpty(coverPath)) {
//                coverPath = localAlbumModel.getAlbumCoverPathByAlbumId(context, albumId);
//                CoverPathCache.getInstance().put(albumId + "", coverPath);
//            }

            localSongEntity.setAlbumCoverPath(getAlbumCoverPathByAlbumId(context, albumId));

            //Put the whole entity into the com.scott.su.smusic.cache;
//            LocalSongEntityCache.getInstance().put(songId + "", localSongEntity);

            songList.add(localSongEntity);
        }
        cursor.close();

        return songList;
    }

    /**
     * 获取指定专辑封面图片路径
     *
     * @param context
     * @param albumId
     * @return
     */
    public static String getAlbumCoverPathByAlbumId(Context context, long albumId) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToNext();
            path = cursor.getString(0);
            cursor.close();
        }

        //Second way to get the path(Uri) of album cover;
//        path = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId).toString();

        return path;
    }

}
