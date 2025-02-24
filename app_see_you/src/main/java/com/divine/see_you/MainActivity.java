package com.divine.see_you;

import android.graphics.Bitmap;
import android.webkit.WebView;

import com.divine.serv_webview.WebViewActivity;

public class MainActivity extends WebViewActivity {
    @Override
    public String getImgName() {
        return null;
    }

    @Override
    public String getRouterParams() {
        return null;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onPageLoading() {

    }

    @Override
    public void onLoadResource(WebView view, String url) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

    }
}