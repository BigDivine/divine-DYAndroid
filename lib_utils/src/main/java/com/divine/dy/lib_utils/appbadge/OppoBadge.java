package com.divine.dy.lib_utils.appbadge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;

/**
 * Author: Divine
 * CreateDate: 2021/8/5
 * Describe: 不支持
 *      新款的OPPO仅支持内置应用、微信和QQ显示角标，若要使用角标功能，必须提交申请，审核通过了才能开放，官方给的具体审核标准如下：
 *      申请角标接入规则（应用必须适配OPPO手机，保证角标功能测试通过）
 *          a) 系统应用；
 *          b) 国内外各区域用户量排名Top5的三方即时通讯类应用，且只允许显示即时通信消息类通知（如QQ、微信、facebook、line）；
 *          c) OPPO公司内部费商业化及运营性质的办公类型即时通信应用（如Teamtalk）；
 *          d) 国内外邮件类应用（各区域各属于用户量第一梯队的应用）
 *
 */
public class OppoBadge {

    @Deprecated
    public static boolean setOPPOBadge(int count, Context context) {
        try {
            Bundle extras = new Bundle();
            extras.putInt("app_badge_count", count);
            Uri badgeUri = Uri.parse("content://com.android.badge/badge");
            context.getContentResolver().call(badgeUri, "setAppBadgeCount", String.valueOf(count), extras);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public static boolean setOPPOBadge2(int count, Context context) {
        try {
            Intent intent = new Intent("com.oppo.unsettledevent");
            intent.putExtra("packageName", context.getPackageName());
            intent.putExtra("number", count);
            intent.putExtra("upgradeNumber", count);
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
            if (receivers != null && receivers.size() > 0) {
                context.sendBroadcast(intent);
            } else {
                Bundle extras = new Bundle();
                extras.putInt("app_badge_count", count);
                Uri badgeUri = Uri.parse("content://com.android.badge/badge");
                context.getContentResolver().call(badgeUri, "setAppBadgeCount", null, extras);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
