package com.divine.yang.util.old.appbadge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe:
 */
public class HuaweiBadge {
    public static boolean setHuaweiBadge(int count, Context context) {
        try {
            String launchClassName = ClassMethods.getLauncherClassName(context);
            if (TextUtils.isEmpty(launchClassName)) {
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", count);
            Uri badgeUri = Uri.parse("content://com.huawei.android.launcher.settings/badge/");
            context.getContentResolver().call(badgeUri, "change_badge", null, bundle);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
