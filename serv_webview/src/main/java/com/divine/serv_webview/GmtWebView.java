package com.divine.serv_webview;

/**
 * Author: Divine
 * CreateDate: 2021/2/1
 * Describe:
 */
public class GmtWebView {
    // 是否支持页面缩放
    public static boolean supportZoom = false;
    // 是否授予前端页面位置权限
    public static boolean needGPS = false;

    // 是否为开发模式，开发模式可以修改webServerUrl和webServerPath
    public static boolean isDevelop = false;
    // 前端页面服务地址：http://0.0.0.0:9000
    public static String webServerUrl;
    // 首页地址：/inde.html#/
    public static String webServerPath;

    // 极光的推送注册的id
    public static String jPushRegisterId;

}
