package com.divine.serv_webview.util;

import android.app.Activity;
import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.divine.serv_webview.GmtWebView;
import com.divine.serv_webview.client.MyWebChromeClient;
import com.divine.serv_webview.client.MyWebViewClient;
import com.divine.serv_webview.client.WebViewInterface;
import com.divine.yang.util.file.FileUtils;

import java.io.File;
/**
 * Author: Divine
 * CreateDate: 2020/8/18
 * Describe:   20250224已经校对完成，当前基本可用
 */
public class WebViewUtils {
    private static MyWebViewClient mWebViewClient;
    private static MyWebChromeClient mWebChromeClient;

    public static void initWebView(Activity activity, WebViewInterface webViewInterface, WebView webview) {
        setWebSettings(webview.getSettings());
        setCookie(webview, true);// 打开cookie
        webview.setHorizontalScrollBarEnabled(false);// 水平不显示滚动条
        webview.setVerticalScrollBarEnabled(false); // 垂直不显示滚动条
        mWebViewClient = new MyWebViewClient(activity, webview, webViewInterface);
        webview.setWebViewClient(mWebViewClient);
        // 20250224已经整理完成，目前为最终版
        mWebChromeClient = new MyWebChromeClient(activity, webViewInterface);
        webview.setWebChromeClient(mWebChromeClient);
    }

    /**
     * 配置websetting
     *
     * @param webSettings
     */
    private static void setWebSettings(WebSettings webSettings) {
        // 调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        // 控制缩放比例，100为不响应系统字体
        webSettings.setTextZoom(100);
        // 设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        ////设置自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 设置允许开启多窗口
        webSettings.supportMultipleWindows();
        webSettings.setSupportMultipleWindows(true);
        // 允许访问文件
        webSettings.setAllowFileAccess(true);
        // 开启javascript
        webSettings.setJavaScriptEnabled(true);
        // 支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 支持手势对页面进行缩放
        webSettings.setBuiltInZoomControls(GmtWebView.supportZoom);
        webSettings.setSupportZoom(GmtWebView.supportZoom);
        // 提高渲染的优先级
        //        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 支持内容重新布局
        //        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // webview中缓存
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //        } else {
        //                    webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //        }
        // sdk升级到34 ，用这个控制缓存策略
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        // sdk升级到34 ，控制缓存的这个方法废弃：    webSettings.setAppCacheEnabled(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        String defaultUserAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(defaultUserAgent + ";only-work");
    }

    private static void setCookie(WebView webView, boolean cookieSupport) {
        // 开启Cookie
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, cookieSupport);
        } else {
            CookieManager.getInstance().setAcceptCookie(cookieSupport);
        }
    }

    // 删除缓存
    public static void clearWebView(Context context) {
        File file = context.getCacheDir().getAbsoluteFile();
        FileUtils.deleteFile(file);
        context.deleteDatabase("webview.db");
        context.deleteDatabase("webviewCache.db");
    }

    public static MyWebViewClient getCurWebViewClient() {
        return mWebViewClient;
    }

    public static MyWebChromeClient getCurWebChromeClient() {
        return mWebChromeClient;
    }
}
