package com.scott.su.common.manager;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.entity.PermissionEntity;
import com.scott.su.common.interfaces.PermissionCallback;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/6
 */

public class PermissionHelper {
    private static PermissionHelper sInstance;


    public static PermissionHelper getInstance(){
        if (sInstance == null)    {
            synchronized (PermissionHelper.class){
                if ( sInstance == null) {
                    sInstance = new PermissionHelper();
                }
            }
        }
        return sInstance;
    }


    private PermissionHelper(){

    }

    /**
     * 动态请求权限
     *
     * @param permissions
     * @param callback
     */
    public void requestPermissions(@NonNull Activity activity,@NonNull String[] permissions,
                                   @NonNull final PermissionCallback callback) {
        if ((permissions == null) || (permissions.length == 0)) {
            return;
        }

        final int count = permissions.length;   //count > 0 ;
        final List<PermissionEntity> permissionsGranted = new ArrayList<>();
        final List<PermissionEntity> permissionsDenied = new ArrayList<>();

        new RxPermissions(activity)
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    int index = 0;

                    @Override
                    public void accept(com.tbruyelle.rxpermissions2.Permission permission) throws Exception {
                        index++;

                        if (permission.granted) {
                            permissionsGranted.add(new PermissionEntity(permission.name, true, false));
                        } else {
                            permissionsDenied.add(new PermissionEntity(permission.name, false,
                                    permission.shouldShowRequestPermissionRationale));

                            //拒绝权限，并点击“不再询问”
//                            if(permission.shouldShowRequestPermissionRationale){
//
//                            }else {
//
//                            }
                        }

                        if (index == count) {
                            //所有权限请求完毕
                            int countGranted = permissionsGranted.size();

                            if (countGranted == count) {
                                //所有权限都成功获取被授予；
                                callback.onPermissionGranted(permissionsGranted, true);
                            } else if (countGranted == 0) {
                                callback.onPermissionDenied(permissionsDenied, true);
                            } else {
                                callback.onPermissionGranted(permissionsGranted, false);
                                callback.onPermissionDenied(permissionsDenied, false);
                            }
                        }
                    }
                });
    }

}
