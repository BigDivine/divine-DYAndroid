package com.divine.yang.util.old.appbadge;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe:
 */
public class SonyBadge {
    public static boolean setSonyBadge(int count, Context context) {
        String launcherClassName = ClassMethods.getLauncherClassName(context);
        if (TextUtils.isEmpty(launcherClassName)) {
            return false;
        }
        try {
            //官方给出方法
            ContentValues contentValues = new ContentValues();
            contentValues.put("badge_count", count);
            contentValues.put("package_name", context.getPackageName());
            contentValues.put("activity_name", launcherClassName);
            SonyAsyncQueryHandler asyncQueryHandler = new SonyAsyncQueryHandler(context.getContentResolver());
            Uri badgeUri = Uri.parse("content://com.sonymobile.home.resourceprovider/badge");
            asyncQueryHandler.startInsert(0, null, badgeUri, contentValues);
            return true;
        } catch (Exception e) {
            try {
                //网上大部分使用方法
                Intent intent = new Intent("com.sonyericsson.home.action.UPDATE_BADGE");
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", count > 0);
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());
                context.sendBroadcast(intent);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }
        }
    }

    static class SonyAsyncQueryHandler extends AsyncQueryHandler {
        SonyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }
}
