package com.divine.serv_webview.util;

import android.Manifest;
import android.os.Build;

/**
 * Project Name  : YBZGMT
 * Package       : com.divine.lib_webview
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/6/26
 * Description   :
 */
public class PermissionUtils {
    public static String[] requestPermissionsArray() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 13系统废弃了READ_EXTERNAL_STORAGE权限，
            // 新增了  READ_MEDIA_IMAGES  READ_MEDIA_VIDEO  READ_MEDIA_AUDIO
            // 这3个新的运行时权限，分别用于控制App对照片、视频、音频的访问。
            // Android13增加了通知权限  POST_NOTIFICATIONS
            // Android14增加了有部分图片权限还是全部图片权限  READ_MEDIA_VISUAL_USER_SELECTED
            return new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            // Android 13系统废弃了READ_EXTERNAL_STORAGE权限，
            // Android13增加了通知权限  POST_NOTIFICATIONS
            // Android 13系统废弃了READ_EXTERNAL_STORAGE权限，
            // 新增了  READ_MEDIA_IMAGES  READ_MEDIA_VIDEO  READ_MEDIA_AUDIO
            // 这3个新的运行时权限，分别用于控制App对照片、视频、音频的访问。
            // Android13增加了通知权限  POST_NOTIFICATIONS
            return new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        } else {
            return new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }
    }
}
