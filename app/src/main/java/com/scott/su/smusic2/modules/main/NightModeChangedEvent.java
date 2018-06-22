package com.scott.su.smusic2.modules.main;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class NightModeChangedEvent {
    private boolean on;

    public NightModeChangedEvent(boolean on) {
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }
}
