package com.scott.su.smusic2.data.entity;

import java.io.Serializable;

/**
 * 描述: 本地音乐实体类
 * 作者: Su
 * 日期: 2018/4/29
 */

public class LocalSongEntity implements Serializable {
    private String songId;
    private String albumId;
    private long duration;
    private long fileSize;
    private String title; //歌曲名称
    private String artist; //歌手
    private String album; //专辑
    private String filePath; //音频文件路径
    private String albumCoverPath; //专辑封面图片路径


    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAlbumCoverPath() {
        return albumCoverPath;
    }

    public void setAlbumCoverPath(String albumCoverPath) {
        this.albumCoverPath = albumCoverPath;
    }

    @Override
    public String toString() {
        return "LocalSongEntity{" +
                "songId=" + songId +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", fileSize=" + fileSize +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", filePath='" + filePath + '\'' +
                ", albumCoverPath='" + albumCoverPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalSongEntity) {
            return ((LocalSongEntity) obj).getSongId().equals(getSongId());
        }

        return false;
    }

}
