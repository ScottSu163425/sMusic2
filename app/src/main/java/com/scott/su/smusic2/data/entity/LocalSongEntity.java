package com.scott.su.smusic2.data.entity;

import java.io.Serializable;

/**
 * 描述: 本地音乐实体类
 * 作者: Su
 * 日期: 2018/4/29
 */

public class LocalSongEntity implements Serializable{
    private long songId;
    private long albumId;
    private long duration;
    private long size;
    private String title; //歌曲名称
    private String artist; //歌手
    private String album; //专辑
    private String path; //音频文件路径
    private String albumCoverPath; //专辑封面图片路径


    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumCoverPath() {
        return albumCoverPath;
    }

    public void setAlbumCoverPath(String albumCoverPath) {
        this.albumCoverPath = albumCoverPath;
    }
}
