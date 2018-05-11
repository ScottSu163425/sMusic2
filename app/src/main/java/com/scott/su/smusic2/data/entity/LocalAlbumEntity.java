package com.scott.su.smusic2.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: 本地音乐专辑实体类
 * 作者: Su
 * 日期: 2018/5/11
 */

public class LocalAlbumEntity implements Serializable {
    private long albumId;
    private String title; //歌曲名称
    private String artist; //歌手
    private String albumCoverPath; //专辑封面图片路径
    private List<LocalSongEntity> albumSongs;

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


    public String getAlbumCoverPath() {
        return albumCoverPath;
    }

    public void setAlbumCoverPath(String albumCoverPath) {
        this.albumCoverPath = albumCoverPath;
    }

    public List<LocalSongEntity> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(List<LocalSongEntity> albumSongs) {
        this.albumSongs = albumSongs;
    }

    @Override
    public String toString() {
        return "LocalAlbumEntity{" +
                "albumId=" + albumId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", albumCoverPath='" + albumCoverPath + '\'' +
                ", albumSongs=" + albumSongs +
                '}';
    }
}
