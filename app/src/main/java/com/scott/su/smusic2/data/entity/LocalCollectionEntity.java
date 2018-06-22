package com.scott.su.smusic2.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * 描述: 本地音乐收藏实体类
 * 作者: Su
 * 日期: 2018/5/11
 */

@Entity(tableName = "collections")
public class LocalCollectionEntity implements Serializable {
    private static final String SEPARATOR = "&";

    @PrimaryKey
    @NonNull
    private String collectionId;
    private String collectionName;
    private String coverPath;
    private String collectionSongIds;


    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionSongIds() {
        return collectionSongIds;
    }

    public void setCollectionSongIds(String collectionSongIds) {
        this.collectionSongIds = collectionSongIds;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    @Override
    public String toString() {
        return "LocalCollectionEntity{" +
                "collectionId='" + collectionId + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", collectionSongIds=" + collectionSongIds +
                ", coverPath='" + coverPath + '\'' +
                '}';
    }

    public boolean empty() {
        return TextUtils.isEmpty(getCollectionSongIds());
    }

    public boolean containSong(@NonNull String songId) {
        if (empty()) {
            return false;
        }

        return getCollectionSongIds().contains(songId);
    }

    public String[] getCollectionSongIdArr() {
        if (empty()) {
            return null;
        }

        return getCollectionSongIds().split(SEPARATOR);
    }

    public boolean addSongIntoCollection(@NonNull String songId) {
        if (containSong(songId)) {
            return false;
        }

        setCollectionSongIds(getCollectionSongIds() + SEPARATOR + songId);
        return true;
    }

    public void removreSongs(@NonNull String... songId) {
        for (String id : songId) {
            removeSong(id);
        }
    }

    public boolean removeSong(@NonNull String songId) {
        if (empty()) {
            return false;
        }

        if (!containSong(songId)) {
            return false;
        }

        getCollectionSongIds().replace(songId + SEPARATOR, "");
        return true;
    }

}
