package com.divine.yang.util.old.sys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏相关工具类
 * <p>
 * yzl 2018-11-22
 */
public class StatusBarUtils {
    /**
     * 设置透明状态栏并启用深色文字模式
     */
    public static void setWhiteStatusBar(Activity activity) {
        // Step 1: 设置状态栏透明
        setTransparentStatusBar(activity);

        // Step 2: 启用深色文字模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 原生Android 6.0+方案[1,6](@ref)
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        } else {
            // 厂商定制系统适配
            if (isMIUI()) {
                setMIUIStatusBarDarkMode(activity, true); // 小米适配[3,6](@ref)
            } else if (isFlyme()) {
                setFlymeStatusBarDarkMode(activity.getWindow(), true); // 魅族适配[4,6](@ref)
            }
        }
    }

    /**
     * 设置透明状态栏背景
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTransparentStatusBar(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 小米MIUI状态栏深色模式适配
     */
    public static boolean setMIUIStatusBarDarkMode(Activity activity, boolean darkmode) {
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = activity.getWindow().getClass()
                    .getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            Log.e("StatusBarUtils", "MIUI适配失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 魅族Flyme状态栏深色模式适配
     */
    public static boolean setFlymeStatusBarDarkMode(Window window, boolean dark) {
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");

            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);

            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            value = dark ? (value | bit) : (value & ~bit);

            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            Log.e("StatusBarUtils", "Flyme适配失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断是否为MIUI系统
     */
    private static boolean isMIUI() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    /**
     * 判断是否为Flyme系统
     */
    private static boolean isFlyme() {
        return Build.DISPLAY.toLowerCase().contains("flyme");
    }

    private static String getSystemProperty(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, "");
        } catch (Exception e) {
            return "";
        }
    }
}