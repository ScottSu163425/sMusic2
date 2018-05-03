package com.scott.su.smusic2.data.entity;

/**
 * 描述: 主页Tab内容页列表滑动事件
 * 作者: Su
 * 日期: 2018/5/1
 */

public class MainTabListScrollEvent {
    private boolean isDragging;
    private boolean isSettling;
    private boolean isIdle;


    public MainTabListScrollEvent(boolean isDragging, boolean isSettling, boolean isIdle) {
        this.isDragging = isDragging;
        this.isSettling = isSettling;
        this.isIdle = isIdle;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public boolean isSettling() {
        return isSettling;
    }

    public boolean isIdle() {
        return isIdle;
    }
}
