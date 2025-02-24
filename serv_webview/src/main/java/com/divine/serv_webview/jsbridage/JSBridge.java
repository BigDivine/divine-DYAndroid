package com.divine.serv_webview.jsbridage;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Divine.Yang
 * @date 2019-06-21
 * @describe webView js调用的原生方法
 */
public class JSBridge {
    private static final String kTag = "JSBridge";
    Map<String, JSBridgeCallback> map = new HashMap<String, JSBridgeCallback>();

    public void addCallback(String key, JSBridgeCallback callback) {
        map.put(key, callback);
    }

    @JavascriptInterface
    public void onResultForScript(String key, String value) {
        Log.i(kTag, "onResultForScript: " + value);
        JSBridgeCallback callback = map.remove(key);
        if (callback != null)
            callback.onReceiveValue(value);
    }
}
