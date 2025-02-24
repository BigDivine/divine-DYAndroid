package com.divine.serv_webview.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.divine.serv_webview.R;
import com.divine.serv_webview.jsbridage.JSBridgeWebViewClient;
import com.divine.yang.base.utils.DialogUtils;
import com.divine.yang.util.network.NetworkUtil;
import com.divine.yang.util.sys.SystemUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Divine
 * CreateDate: 2021/2/23
 * Describe:
 */
public class SchemeHelper {
    private static SchemeHelper mHelper;
    private Activity mActivity;
    private JSBridgeWebViewClient mJSWebViewClient;

    private SchemeHelper(Activity mActivity, JSBridgeWebViewClient mJSWebViewClient) {
        this.mActivity = mActivity;
        this.mJSWebViewClient = mJSWebViewClient;
    }

    public static SchemeHelper getInstance(Activity mActivity, JSBridgeWebViewClient mJSWebViewClient) {
        if (null == mHelper) {
            synchronized (SchemeHelper.class) {
                if (null == mHelper) {
                    mHelper = new SchemeHelper(mActivity, mJSWebViewClient);
                }
            }
        }
        return mHelper;
    }

    public boolean ifResolveScheme(String url) {
        return url.startsWith("tel://")// 电话
                || url.startsWith("tel:")// 电话
                || url.startsWith("sms:")// 短信
                || url.startsWith("mailto://") // 邮件
                || url.startsWith("weixin://") // 微信
                || url.startsWith("alipays://") // 支付宝
                || url.startsWith("alipays") // 支付宝
                || url.startsWith("baidumap://")// 百度地图
                || url.startsWith("bdmap://")// 百度地图
                || url.startsWith("androidamap://")// 高德地图
                || url.startsWith("qqmap://")// 腾讯地图
                || url.startsWith("dianping://");// 大众点评
    }

    // 支付宝scheme
    public boolean isAliPayScheme(String url) {
        return url.startsWith("alipays://") || url.startsWith("alipays");
    }

    // 高德scheme
    public boolean isAMapScheme(String url) {
        return url.startsWith("androidamap://");
    }

    // 高德scheme
    public boolean isBaiduMapScheme(String url) {
        return url.startsWith("baidumap://") || url.startsWith("bdmap://");
    }

    // 高德scheme
    public boolean isQQMapScheme(String url) {
        return url.startsWith("qqmap://");
    }

    public void toApp(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mActivity.startActivity(intent);
    }

    public void resolveScheme(WebView view, String url) throws Exception {
        if (ifResolveScheme(url)) {
            if (isAliPayScheme(url)) {
                // 支付宝scheme，本地拦截处理跳转的，使用Intent.CATEGORY_BROWSABLE跳转
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                mActivity.startActivity(intent);
            } else if (isAMapScheme(url)) {
                // 高德地图scheme，需要手动判断是否安装app
                boolean installed = SystemUtils.isInstalled(mActivity, "com.autonavi.minimap");
                if (installed) {
                    url = URLDecoder.decode(url, "utf-8");
                    toApp(Uri.parse(url));
                } else {
                    DialogUtils.showConfirmDialog(mActivity, "", mActivity.getResources().getString(R.string.dialog_no_amap_msg), (dialog, which) -> {
                        dialog.dismiss();
                    });
                }
            } else {
                // 系统scheme，需要本地拦截处理跳转的
                url = URLDecoder.decode(url, "utf-8");
                toApp(Uri.parse(url));
            }
        } else if (url.startsWith(mJSWebViewClient.kCustomProtocolScheme)) {
            // 处理js调用原生方法交互
            if (url.indexOf(mJSWebViewClient.kQueueHasMessage) > 0) {
                mJSWebViewClient.flushMessageQueue();
            }
        } else if (url.contains("wx.tenpay.com")) {
            // 微信支付
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("Referer", "https://sy.517na.com");
            view.loadUrl(url, extraHeaders);
        } else if (!isAMapScheme(url)) {
            // 处理http和https开头的url
            String finalUrl = url;
            new NetworkUtil.UrlJudgeTask(url, success -> {
                if (success) {
                    view.loadUrl(finalUrl);
                } else {
                    Log.e("SchemeHelper", "url judge error:服务地址有问题，请检查服务地址" );
                }
            }).execute();
        }
    }

    public void resolveSchemeCatch(String url) {
        if (isAliPayScheme(url)) {
            DialogUtils.showConfirmDialog(mActivity, mActivity.getResources().getString(R.string.dialog_no_alipay_msg), mActivity.getResources().getString(R.string.dialog_no_alipay_install_text), (dialog, which) -> {
                Uri alipayUrl = Uri.parse("https://d.alipay.com");
                final Intent intent = new Intent(Intent.ACTION_VIEW, alipayUrl);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                mActivity.startActivity(intent);
            });
        } else if (isAMapScheme(url)) {
            DialogUtils.showConfirmDialog(mActivity, "", mActivity.getResources().getString(R.string.dialog_no_amap_msg), (dialog, which) -> {
                dialog.dismiss();
            });
        } else if (isBaiduMapScheme(url)) {
            DialogUtils.showConfirmDialog(mActivity, "", mActivity.getResources().getString(R.string.dialog_no_bmap_msg), (dialog, which) -> {
                dialog.dismiss();
            });
        } else if (isQQMapScheme(url)) {
            DialogUtils.showConfirmDialog(mActivity, "", mActivity.getResources().getString(R.string.dialog_no_tmap_msg), (dialog, which) -> {
                dialog.dismiss();
            });
        }
    }
}
