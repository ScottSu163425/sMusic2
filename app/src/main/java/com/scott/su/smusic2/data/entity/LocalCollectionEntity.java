package com.scott.su.smusic2.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * 描述: 本地音乐收藏实体类
 * 作者: Su
 * 日期: 2018/5/11
 */

@Entity(tableName = "collections")
public class LocalCollectionEntity implements Serializable {
    @PrimaryKey
    private long collectionId;
    private String collectionName;
    private String coverPath;
    private String collectionSongIds;


    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
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
}
