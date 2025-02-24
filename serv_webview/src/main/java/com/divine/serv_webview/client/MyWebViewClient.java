package com.divine.serv_webview.client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.TextView;

import com.divine.serv_webview.R;
import com.divine.serv_webview.jsbridage.JSBridgeWebViewClient;
import com.divine.serv_webview.util.SchemeHelper;
import com.divine.yang.base.utils.ToastUtils;

/**
 * Author: Divine
 * CreateDate: 2020/8/18
 * Describe:
 */
public class MyWebViewClient extends JSBridgeWebViewClient {
    private final String TAG = "MyWebViewClient";
    private SchemeHelper mSchemeHelper;
    private WebViewInterface webViewInterface;
    private View netErrorLayout;
    private Activity mActivity;
    // 需要在jsbridge中同步增加
    private String[] jsHandleNames = new String[]{
            "openOutLink"
            , "webReload"
            , "jPushSwitch"
            , "getLocation"
            , "setBadge"
            , "saveSession"
            , "postRequest"// post请求
            , "getRequest"// get请求
            , "uploadRequest"// 上传文件请求
            , "downloadFile" // 下载文件
            , "netWorkStatus"// 网络判断
            , "checkVersion"// 检查app版本
            , "updateApp"// 下载app
    };

    public MyWebViewClient(Activity mActivity, WebView webView, WebViewInterface webViewInterface) {
        // 需要支持JS send 方法时
        //        super(webView, new JSBridgeHandler() {
        //            @Override
        //            public void request(Object data, JSBridgeResponseCallback callback) {
        //                Toast.makeText(context, "ObjC Received message from JS:" + data, Toast.LENGTH_LONG).show();
        //                callback.callback("Response for message from ObjC!");
        //            }
        //        });
        // 不需要支持JS send 方法时
        super(webView);
        enableLogging();
        registerJs();
        this.mSchemeHelper = SchemeHelper.getInstance(mActivity, this);
        this.webViewInterface = webViewInterface;
        this.netErrorLayout = webViewInterface.getNetErrorLayout();
        this.mActivity = mActivity;
        Log.e(TAG, "constructor");
    }

    private void registerJs() {
        for (String name : jsHandleNames) {
            registerHandler(name, (data, callback) -> {
                Log.e("JsBridge", "constructor：" + name);
                JSRunMethodListener.runMethod(name, data, callback);
            });
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        // 页面加载失败的时候，显示的错误页面，理论上不应该有错误页面
        String webErrorMsg = mActivity.getResources().getString(R.string.toast_web_url_error_msg);
        ToastUtils.showShort(mActivity, String.format(webErrorMsg, errorCode, description, failingUrl));
        view.loadUrl("about:blank"); // 避免出现默认的错误界面
        netErrorLayout.setVisibility(View.VISIBLE);
        TextView errorMessage = netErrorLayout.findViewById(R.id.net_error_message);
        //        Button btnToSetting = netErrorLayout.findViewById(R.id.to_setting_btn);
        // 断网或者网络连接超时
        switch (errorCode) {
            case ERROR_HOST_LOOKUP:// 服务器寻址失败
            case ERROR_CONNECT:
                errorMessage.setText(mActivity.getResources().getString(R.string.toast_net_error_msg));
                //                btnToSetting.setVisibility(View.GONE);
                break;
            case ERROR_TIMEOUT:
                errorMessage.setText(mActivity.getResources().getString(R.string.toast_net_timeout_msg));
                //                btnToSetting.setVisibility(View.GONE);
                break;
            default:
                errorMessage.setText(mActivity.getResources().getString(R.string.toast_net_unknown_msg));
                //                btnToSetting.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e(TAG, "shouldOverrideUrlLoading:" + url);
        if (null != url) {
            try {
                mSchemeHelper.resolveScheme(view, url);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "shouldOverrideUrlLoading-error:" + e);
                mSchemeHelper.resolveSchemeCatch(url);
                return true;
            }
        } else {
            Log.e(TAG, "shouldOverrideUrlLoading: url is null");
            return false;
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();  // 接受所有网站的证书
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        setSessionData();
        Log.e(TAG, "onLoadResource:" + url);
        webViewInterface.onLoadResource(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        setSessionData();
        Log.e(TAG, "onPageStarted:" + url);
        webViewInterface.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        setSessionData();
        Log.e(TAG, "onPageFinished:" + url);
        if (startupMessageQueue != null) {
            for (int i = 0; i < startupMessageQueue.size(); i++) {
                dispatchMessage(startupMessageQueue.get(i));
            }
            startupMessageQueue = null;
        }
        webViewInterface.onPageFinished(view, url);
        webViewInterface.getNetLoadingLayout().setVisibility(View.GONE);
    }

    private void setSessionData() {
        // 默认session都是前端自己塞值，以下为示例
        // String userName = "usernam"
        // if (!TextUtils.isEmpty(userName)) {
        //     saveSession("user", userName);
        // }
    }

    private void saveLocal(String key, String value) {
        executeJavascript("localStorage.setItem('" + key + "','" + value + "')");
    }

    private void saveSession(String key, String value) {
        executeJavascript("sessionStorage." + key + "='" + value + "'");
    }
}
