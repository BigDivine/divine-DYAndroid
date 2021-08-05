package com.divine.dy.lib_utils.appbadge;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe: 可以通过广播机制直接设置应用角标，且应用在前台和被杀掉后仍可显示。
 */
public class SamsungBadge {
    public static boolean setSamsungBadge(int count, Context context) {
        try {
            String launcherClassName = ClassMethods.getLauncherClassName(context);
            if (TextUtils.isEmpty(launcherClassName)) {
                return false;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
