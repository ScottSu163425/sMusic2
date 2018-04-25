package com.scott.su.common.interfaces;

import com.scott.su.common.entity.PermissionEntity;

import java.util.List;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/4/25 14:46
 */

public interface PermissionCallback {
    void onPermissionGranted(List<PermissionEntity> permissions, boolean allGranted);

    void onPermissionDenied(List<PermissionEntity> permissions, boolean allDenied);
}
