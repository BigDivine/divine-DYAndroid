package com.divine.dy.lib_utils.old.appbadge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe:
 */
public class HtcBadge {
    public static boolean setHTCBadge(int count, Context context) {
        try {
            ComponentName launcherComponentName = ClassMethods.getLauncherComponentName(context);
            if (launcherComponentName == null) {
                return false;
            }
            Intent intent1 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
            intent1.putExtra("com.htc.launcher.extra.COMPONENT", launcherComponentName
                    .flattenToShortString());
            intent1.putExtra("com.htc.launcher.extra.COUNT", count);
            context.sendBroadcast(intent1);

            Intent intent2 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            intent2.putExtra("packagename", launcherComponentName.getPackageName());
            intent2.putExtra("count", count);
            context.sendBroadcast(intent2);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
