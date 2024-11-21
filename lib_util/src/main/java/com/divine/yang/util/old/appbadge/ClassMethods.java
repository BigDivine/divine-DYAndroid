package com.divine.yang.util.old.appbadge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe:
 */
public class ClassMethods {
    public static String getLauncherClassName(Context context) {
        ComponentName launchComponent = getLauncherComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    public static ComponentName getLauncherComponentName(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context
                                                                                            .getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }
}
