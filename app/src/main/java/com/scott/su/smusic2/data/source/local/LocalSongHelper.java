package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.scott.su.common.interfaces.Judgment;
import com.scott.su.common.util.ListUtil;
import com.scott.su.common.util.TimeUtil;
import com.scott.su.smusic2.data.cache.CoverPathCache;
import com.scott.su.smusic2.data.cache.LocalSongEntityCache;
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

            long songId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

            //从缓存中获取
            LocalSongEntity entityFromCache = LocalSongEntityCache.getInstance().get(String.valueOf(songId));
            if (entityFromCache != null) {
                songList.add(entityFromCache);
                continue;
            }

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

            String coverPath = CoverPathCache.getInstance().get(String.valueOf(songId));
            if (TextUtils.isEmpty(coverPath)) {
                coverPath = getAlbumCoverPathByAlbumId(context, String.valueOf(albumId));
                CoverPathCache.getInstance().put(String.valueOf(songId), coverPath);
            }

            localSongEntity.setAlbumCoverPath(coverPath);
            LocalSongEntityCache.getInstance().put(String.valueOf(songId), localSongEntity);

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
    public List<LocalSongEntity> getSongsById(@NonNull Context context, @NonNull final String[] idArr) {
        List<LocalSongEntity> list = new ArrayList<>();

        if (idArr == null || idArr.length == 0) {
            return list;
        }

        List<LocalSongEntity> songs = getAllSongs(context);
        if (ListUtil.isEmpty(songs)) {
            return list;
        }

        list = ListUtil.filter(songs, new Judgment<LocalSongEntity>() {
            @Override
            public boolean test(LocalSongEntity obj) {
                for (int i = 0, n = idArr.length; i < n; i++) {
                    if (idArr[i].equals(obj.getSongId())) {
                        return true;
                    }
                }
                return false;
            }
        });

        return list;
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
        String coverPath = CoverPathCache.getInstance().get(songId);

        if (!TextUtils.isEmpty(coverPath)) {
            return coverPath;
        }

        LocalSongEntity song = getSongById(context, songId);

        if (song == null) {
            return null;
        }

        coverPath = song.getAlbumCoverPath();
        CoverPathCache.getInstance().put(songId, coverPath);
        return coverPath;
    }

    @Override
    public List<LocalSongEntity> searchSong(@NonNull Context context, @NonNull String keyword) {
        List<LocalSongEntity> listAll = getAllSongs(context);
        List<LocalSongEntity> listResult = new ArrayList<>();

        if (listAll != null && !listAll.isEmpty()) {
            for (int i = 0, n = listAll.size(); i < n; i++) {
                LocalSongEntity song = listAll.get(i);

                if (song.getTitle().contains(keyword)
                        || song.getAlbum().contains(keyword)
                        || song.getArtist().contains(keyword)) {
                    listResult.add(song);
                }
            }
        }

        return listResult;
    }

    @Override
    public List<LocalAlbumEntity> searchAlbum(@NonNull Context context, @NonNull String keyword) {
        List<LocalAlbumEntity> listAll = getAllAlbums(context);
        List<LocalAlbumEntity> listResult = new ArrayList<>();

        if (listAll != null && !listAll.isEmpty()) {
            for (int i = 0, n = listAll.size(); i < n; i++) {
                LocalAlbumEntity album = listAll.get(i);

                if (album.getTitle().contains(keyword)
                        || album.getArtist().contains(keyword)) {
                    listResult.add(album);
                }
            }
        }

        return listResult;
    }

}
