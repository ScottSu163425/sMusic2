package com.scott.su.smusic2.data.entity;

/**
 * 描述: 主页Tab内容页列表滑动事件
 * 作者: Su
 * 日期: 2018/5/1
 */

public class MainTabListDragEvent {
    private boolean isDragging;

    public MainTabListDragEvent(boolean isDragging) {
        this.isDragging = isDragging;
    }

    public boolean isDragging() {
        return isDragging;
    }
}
