package com.divine.serv_webview.client;


import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divine.serv_webview.R;
import com.divine.serv_webview.util.WebViewUtils;
import com.divine.yang.base.utils.DialogUtils;

/**
 * Author: Divine
 * CreateDate: 2020/8/18
 * Describe:
 */
public class MyWebChromeClient extends WebChromeClient {
    private Activity activity;
    private WebViewInterface webViewInterface;
    private ProgressBar progressBar;
    private RelativeLayout container;
    private WebView newWebView;
    public MyWebChromeClient(final Activity activity, final WebViewInterface webViewInterface) {
        this.activity = activity;
        this.webViewInterface = webViewInterface;
        this.container = webViewInterface.getWebViewContainer();
        this.progressBar = webViewInterface.getWebViewProgressBar();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        // 支持覆盖一个新的webview，一般情况不用，跟前端a标签的type相关。第一次的时候不走这个东西
        View newWeb = webViewInterface.getNewWebPage();
        String newWebTitle = webViewInterface.getNewWebPageTitle();
        newWebView = newWeb.findViewById(R.id.new_web_view);
        RelativeLayout newWebViewHeader = newWeb.findViewById(R.id.new_web_header);
        newWebViewHeader.setBackgroundColor(activity.getColor(com.divine.yang.theme.R.color.BaseThemeColor));
        ImageView newBack = newWeb.findViewById(R.id.new_web_back);
        TextView titleView = newWeb.findViewById(R.id.new_web_title);
        titleView.setText(newWebTitle);
        newBack.setOnClickListener((v) -> {
//                    if (newWebView.canGoBack()) {
//                        newWebView.goBack();
//                    } else {
            container.removeView(newWeb);// 把webview加载到activity界面上
//                    }
        });
        WebViewUtils.initWebView(activity, webViewInterface, newWebView);// 初始化webview
        container.addView(newWeb);// 把webview加载到activity界面上
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;// 以下的操作应该就是让新的webview去加载对应的url等操作。
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();
        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {
        if (newWebView != null) {
            container.removeView(newWebView);// 把webview加载到activity界面上
            newWebView = null;
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        // js Alert提示框拦截并修改样式
        DialogUtils.showConfirmDialog(activity, activity.getResources().getString(R.string.dialog_web_title), message, activity.getResources().getString(R.string.dialog_web_confirm_text), (dialog, which) -> {
            result.confirm();
        });
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        // js Confirm提示框拦截并修改样式
        DialogUtils.showConfirmDialog(activity, activity.getResources().getString(R.string.dialog_web_title), message, activity.getResources().getString(R.string.dialog_web_confirm_text), (dialog, which) -> {
            result.confirm();
        }, activity.getResources().getString(R.string.dialog_web_cancel_text), (dialog, which) -> {
            result.cancel();
        });
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        // js Prompt提示框拦截并修改样式
        Toast.makeText(activity, activity.getResources().getString(R.string.toast_web_no_css_msg), Toast.LENGTH_SHORT).show();
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    // For Lollipop 5.0+ Devices
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {
        webViewInterface.showSelectPhotoOrCamera(filePathCallback, fileChooserParams, null);
        return true;
    }

    @Override
    public void onProgressChanged(final WebView webView, final int newProgress) {
        Log.e("webview", "onProgressChanged:" + newProgress);
        // 显示进度条
        progressBar.setProgress(newProgress);
        if (newProgress == 100) {
            // 加载完毕隐藏进度条
            progressBar.setVisibility(View.GONE);
        }
        super.onProgressChanged(webView, newProgress);
    }

    // webview获取位置信息
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

}


