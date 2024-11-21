package com.divine.yang.util.old.appbadge;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe: vivo不支持设置应用角标，也不支持申请
 */
public class VivoBadge {
    @Deprecated
    public static boolean setVivoBadge(int count, Context context) {
        try {
            String launcherClassName = ClassMethods.getLauncherClassName(context);
            if (TextUtils.isEmpty(launcherClassName)) {
                return false;
            }
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            intent.putExtra("className", launcherClassName);
            intent.putExtra("notificationNum", count);
            context.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
