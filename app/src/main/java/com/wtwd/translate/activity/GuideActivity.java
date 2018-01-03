package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;

/**
 * time:2017/12/28
 * Created by w77996
 */
public class GuideActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_CODE = 0; // 请求码

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
