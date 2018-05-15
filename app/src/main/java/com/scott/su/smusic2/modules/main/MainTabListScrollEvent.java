package com.scott.su.smusic2.modules.main;

/**
 * 描述: 主页列表滚动事件
 * 作者: su
 * 日期: 2018/5/15
 */

public class MainTabListScrollEvent {
    private boolean mIdle;
    private boolean mDragging;
    private boolean mSettling;


    public MainTabListScrollEvent(boolean idle, boolean dragging, boolean settling) {
        mIdle = idle;
        mDragging = dragging;
        mSettling = settling;
    }

    public boolean isIdle() {
        return mIdle;
    }

    public boolean isDragging() {
        return mDragging;
    }

    public boolean isSettling() {
        return mSettling;
    }

}
