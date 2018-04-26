package com.scott.su.common.interfaces;

import com.scott.su.common.entity.PermissionEntity;

import java.util.List;


public interface PermissionCallback {
    void onPermissionGranted(List<PermissionEntity> permissions, boolean allGranted);

    void onPermissionDenied(List<PermissionEntity> permissions, boolean allDenied);
}
