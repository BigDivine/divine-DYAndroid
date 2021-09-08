package com.divine.dy.lib_utils.old.sys;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.divine.dy.lib_utils.old.appbadge.HtcBadge;
import com.divine.dy.lib_utils.old.appbadge.HuaweiBadge;
import com.divine.dy.lib_utils.old.appbadge.OtherBadge;
import com.divine.dy.lib_utils.old.appbadge.SamsungBadge;
import com.divine.dy.lib_utils.old.appbadge.SonyBadge;
import com.divine.dy.lib_utils.old.appbadge.ZukBadge;

/**
 * 应用角标工具类
 * Author: Divine
 * CreateDate: 2021/2/23
 * Describe:
 * 6 魅族（不支持） * 官方不支持。
 * 7 360（不支持） * 目前可能仅支持系统应用、微信和QQ，不支持支付宝，未找到设置角标相关文档。
 * 8 锤子（不支持） * 目前仅支持系统应用和微信，甚至不支持QQ。
 * 9 努比亚（不支持） * 目前仅支持系统应用。
 * 10 金立（不支持） * 找不到相关文档。
 * 13 中兴（不支持） * 目前仅支持系统应用和微信，甚至不支持QQ。
 * 17 原生Android（部分支持，无法直接显示数目）
 * Android 8.0及之后的版本Google官方API支持通过发送系统通知的方式设置应用角标，但是不支持显示数量，而是一个小点儿。
 */
public class AppBadgeUtil {
    public static boolean setCount(int count, Context context) {
        if (count >= 0 && context != null) {
            Log.d("BRAND", Build.BRAND);
            switch (Build.BRAND.toLowerCase()) {
                case "huawei":
                case "honor":
                    return HuaweiBadge.setHuaweiBadge(count, context);
                case "samsung":
                    return SamsungBadge.setSamsungBadge(count, context);
                case "lenovo":
                    return ZukBadge.setZukBadge(count, context);
                case "htc":
                    return HtcBadge.setHTCBadge(count, context);
                case "sony":
                    return SonyBadge.setSonyBadge(count, context);
                default:
                    return OtherBadge.setNotificationBadge(count, context);
            }
        } else {
            return false;
        }
    }
}