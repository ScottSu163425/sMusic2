package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.scott.su.common.util.TimeUtil;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/30
 */

public class LocalSongHelper implements ILocalSongDataSource {

    private static LocalSongHelper sInstance;


    public static LocalSongHelper getInstance() {
        if (sInstance == null) {
            synchronized (LocalSongHelper.class) {
                if (sInstance == null) {
                    sInstance = new LocalSongHelper();
                }
            }
        }
        return sInstance;
    }


    private LocalSongHelper() {

    }

    @Override
    public List<LocalSongEntity> getAllSongs(@NonNull Context context) {
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

            //过滤2分钟以下的音频
            if (TimeUtil.getMinutesFromMills(duration) < 2) {
                continue;
            }

            LocalSongEntity localSongEntity = new LocalSongEntity();
            localSongEntity.setSongId(String.valueOf(songId));
            localSongEntity.setTitle(title);
            localSongEntity.setArtist(artist);
            localSongEntity.setAlbum(album);
            localSongEntity.setAlbumId(String.valueOf(albumId));
            localSongEntity.setDuration(duration);
            localSongEntity.setFileSize(size);
            localSongEntity.setFilePath(path);

            //Get and set covert path from LruCache if exists,otherwise com.scott.su.smusic.cache it.
//            String coverPath = CoverPathCache.getInstance().get(albumId + "");
//            if (TextUtils.isEmpty(coverPath)) {
//                coverPath = localAlbumModel.getAlbumCoverPathByAlbumId(context, albumId);
//                CoverPathCache.getInstance().put(albumId + "", coverPath);
//            }

            localSongEntity.setAlbumCoverPath(getAlbumCoverPathByAlbumId(context, String.valueOf(albumId)));

            //Put the whole entity into the com.scott.su.smusic.cache;
//            LocalSongEntityCache.getInstance().put(songId + "", localSongEntity);

            songList.add(localSongEntity);
        }
        cursor.close();

        return songList;
    }

    @Override
    public LocalSongEntity getSongById(@NonNull Context context, @NonNull String songId) {
        if (TextUtils.isEmpty(songId)) {
            return null;
        }

        List<LocalSongEntity> songs = getAllSongs(context);
        if (songs == null || songs.isEmpty()) {
            return null;
        }

        for (LocalSongEntity entity : songs) {
            if (songId.equals(entity.getSongId())) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public List<LocalAlbumEntity> getAllAlbums(Context context) {
        List<LocalAlbumEntity> result = new ArrayList<>();
        List<LocalSongEntity> localSongs = getAllSongs(context);
        Set<String> albumIds = new HashSet<>();
        Map<String, String> albumTitles = new HashMap<>();
        Map<String, String> albumArtists = new HashMap<>();
        Map<String, String> albumCoverPaths = new HashMap<>();
        Map<String, List<LocalSongEntity>> albumSongs = new HashMap<>();


        for (int i = 0, n = localSongs.size(); i < n; i++) {
            LocalSongEntity song = localSongs.get(i);

            albumIds.add(song.getAlbumId());
            albumTitles.put(song.getAlbumId(), song.getAlbum());
            albumArtists.put(song.getAlbumId(), song.getArtist());
            albumCoverPaths.put(song.getAlbumId(), song.getAlbumCoverPath());
        }

        for (String albumId : albumIds) {
            LocalAlbumEntity album = new LocalAlbumEntity();
            album.setAlbumId(String.valueOf(albumId));
            album.setTitle(albumTitles.get(albumId));
            album.setArtist(albumArtists.get(albumId));
            album.setAlbumCoverPath(albumCoverPaths.get(albumId));

            result.add(album);

            albumSongs.put(albumId, new ArrayList<LocalSongEntity>());
        }

        for (int i = 0, n = localSongs.size(); i < n; i++) {
            LocalSongEntity song = localSongs.get(i);
            String albumId = song.getAlbumId();

            albumSongs.get(albumId).add(song);
        }

        for (LocalAlbumEntity album : result) {
            album.setAlbumSongs(albumSongs.get(album.getAlbumId()));
        }

        return result;
    }

    @Override
    public LocalAlbumEntity getAlbum(Context context, @NonNull String albumId) {
        List<LocalAlbumEntity> albums = getAllAlbums(context);

        if (albums != null && !albums.isEmpty()) {
            for (LocalAlbumEntity albumEntity : albums) {
                if (albumId.equals(albumEntity.getAlbumId())) {
                    return albumEntity;
                }
            }
        }

        return null;
    }

    @Override
    public String getAlbumCoverPathByAlbumId(Context context, @NonNull String albumId) {
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

    @Override
    public String getAlbumCoverPathBySongId(Context context, @NonNull String songId) {
        LocalSongEntity song = getSongById(context, songId);

        if (song == null) {
            return null;
        }

        return song.getAlbumCoverPath();
    }

}
