package com.scott.su.common.activity;


import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.scott.su.common.R;
import com.scott.su.common.entity.PermissionEntity;
import com.scott.su.common.interfaces.PermissionCallback;
import com.scott.su.common.util.FragmentUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 描述: Activity基类
 * 作者: su
 * 日期: 2017/9/21 14:34
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final int sDefaultAnimOpenIn = R.anim.slide_in_right;
    private static final int sDefaultAnimOpenOut = android.R.anim.fade_out;
    private static final int sDefaultAnimCloseIn = R.anim.fade_in;
    private static final int sDefaultAnimCloseOut = R.anim.slide_out_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(provideAnimOpenIn(), provideAnimOpenOut());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(provideAnimCloseIn(), provideAnimCloseOut());
    }

    protected @AnimRes int provideAnimOpenIn(){
        return sDefaultAnimOpenIn;
    }
    protected @AnimRes int provideAnimOpenOut(){
        return sDefaultAnimOpenOut;
    }

    protected @AnimRes int provideAnimCloseIn(){
        return sDefaultAnimCloseIn;
    }

    protected @AnimRes int provideAnimCloseOut(){
        return sDefaultAnimCloseOut;
    }

    protected View getContentView() {
        return this.findViewById(android.R.id.content);
    }

    protected void showFragment(Fragment fragment, @IdRes int containerId) {
        FragmentUtil.show(BaseActivity.this, containerId, fragment, false);
    }

    /**
     * 动态请求权限
     * @param permissions
     * @param callback
     */
    protected void requestPermissions(@NonNull List<String> permissions,
                                      @NonNull final PermissionCallback callback) {
        if ((permissions == null) || (permissions.size() == 0)) {
            return;
        }

        final int count = permissions.size();   //count > 0 ;
        final List<PermissionEntity> permissionsGranted = new ArrayList<>();
        final List<PermissionEntity> permissionsDenied = new ArrayList<>();

        new RxPermissions(BaseActivity.this)
                .requestEach(permissions.toArray(new String[count]))
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
