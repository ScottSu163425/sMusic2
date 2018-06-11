package com.scott.su.smusic2.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: 本地音乐收藏实体类
 * 作者: Su
 * 日期: 2018/5/11
 */

public class LocalCollectionEntity implements Serializable {
    private String collectionId;
    private String collectionName;
    private List<String> collectionSongIds;
    private String createTime;
    private String coverPath;


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

    public List<String> getCollectionSongIds() {
        return collectionSongIds;
    }

    public void setCollectionSongIds(List<String> collectionSongIds) {
        this.collectionSongIds = collectionSongIds;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
                ", createTime='" + createTime + '\'' +
                ", coverPath='" + coverPath + '\'' +
                '}';
    }
}
