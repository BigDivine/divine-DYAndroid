package com.divine.serv_webview.client;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Author: Divine
 * CreateDate: 2020/8/19
 * Describe:
 */
public interface WebViewInterface {
    void showSelectPhotoOrCamera(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams, ValueCallback uploadMsg);

    View getNetErrorLayout();

    View getNetLoadingLayout();

    RelativeLayout getWebViewContainer();

    ProgressBar getWebViewProgressBar();

    View getNewWebPage();

    String getNewWebPageTitle();

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageLoading();

    void onLoadResource(WebView view, String url);

    void onPageFinished(WebView view, String url);
}
