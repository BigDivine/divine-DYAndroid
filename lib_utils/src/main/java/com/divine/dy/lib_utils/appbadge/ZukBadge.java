package com.divine.dy.lib_utils.appbadge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe:联想zuk
 */
public class ZukBadge {
    public static boolean setZukBadge(int count, Context context) {
        try {
            Bundle extra = new Bundle();
            ArrayList<String> ids = new ArrayList<>();
            extra.putStringArrayList("app_shortcut_custom_id", ids);
            extra.putInt("app_badge_count", count);
            Uri contentUri = Uri.parse("content://com.android.badge/badge");
            Bundle bundle = context.getContentResolver().call(contentUri, "setAppBadgeCount", null, extra);
            return bundle != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
