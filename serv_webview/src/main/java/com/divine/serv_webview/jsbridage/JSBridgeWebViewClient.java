package com.divine.serv_webview.jsbridage;


import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Divine
 * CreateDate: 2021/2/5
 * Describe:
 */

@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public class JSBridgeWebViewClient extends WebViewClient {
    public static final String kCustomProtocolScheme = "wvjbscheme";
    public static final String kQueueHasMessage = "__WVJB_QUEUE_MESSAGE__";

    private static final String kTag = "WVJB";
    private static final String kInterface = kTag + "Interface";
    private static boolean logging = false;
    protected WebView webView;
    protected ArrayList<JSBridgeMessage> startupMessageQueue = null;
    private Map<String, JSBridgeResponseCallback> responseCallbacks = null;
    private Map<String, JSBridgeHandler> messageHandlers = null;
    private long uniqueId = 0;
    private JSBridgeHandler messageHandler;
    private JSBridge myInterface = new JSBridge();
    protected JSRunMethodListener JSRunMethodListener;


    public JSBridgeWebViewClient(WebView webView) {
        this(webView, null);
    }

    public JSBridgeWebViewClient(WebView webView, JSBridgeHandler messageHandler) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.addJavascriptInterface(myInterface, kInterface);
        this.responseCallbacks = new HashMap<String, JSBridgeResponseCallback>();
        this.messageHandlers = new HashMap<String, JSBridgeHandler>();
        this.startupMessageQueue = new ArrayList<JSBridgeMessage>();
        this.messageHandler = messageHandler;
    }

    public void enableLogging() {
        logging = true;
    }

    public void send(Object data) {
        send(data, null);
    }

    public void send(Object data, JSBridgeResponseCallback responseCallback) {
        sendData(data, responseCallback, null);
    }

    public void callHandler(String handlerName) {
        callHandler(handlerName, null, null);
    }

    public void callHandler(String handlerName, Object data) {
        callHandler(handlerName, data, null);
    }

    public void callHandler(String handlerName, Object data,
                            JSBridgeResponseCallback responseCallback) {
        sendData(data, responseCallback, handlerName);
    }

    public void registerHandler(String handlerName, JSBridgeHandler handler) {
        if (handlerName == null || handlerName.length() == 0 || handler == null)
            return;
        messageHandlers.put(handlerName, handler);
    }
    public void flushMessageQueue() {
        String script = "WebViewJavascriptBridge._fetchQueue()";
        executeJavascript(script, new JSBridgeCallback() {
            public void onReceiveValue(String messageQueueString) {
                Log.e("flushMessageQueue", "shouldOverrideUrlLoading-flushMessageQueue:" + messageQueueString);
                if (messageQueueString == null
                        || messageQueueString.length() == 0)
                    return;
                processQueueMessage(messageQueueString);
            }
        });
    }
    private void sendData(Object data, JSBridgeResponseCallback responseCallback,
                          String handlerName) {
        if (data == null && (handlerName == null || handlerName.length() == 0))
            return;
        JSBridgeMessage message = new JSBridgeMessage();
        if (data != null) {
            message.data = data;
        }
        if (responseCallback != null) {
            String callbackId = "objc_cb_" + (++uniqueId);
            responseCallbacks.put(callbackId, responseCallback);
            message.callbackId = callbackId;
        }
        if (handlerName != null) {
            message.handlerName = handlerName;
        }
        queueMessage(message);
    }

    private void queueMessage(JSBridgeMessage message) {
        if (startupMessageQueue != null) {
            startupMessageQueue.add(message);
        } else {
            dispatchMessage(message);
        }
    }

    protected void dispatchMessage(JSBridgeMessage message) {
        String messageJSON = message2JSONObject(message).toString()
                .replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"")
                .replaceAll("'", "\\\\'").replaceAll("\n", "\\\\\n")
                .replaceAll("\r", "\\\\\r").replaceAll("\f", "\\\\\f");

        log("SEND", messageJSON);

        executeJavascript("WebViewJavascriptBridge._handleMessageFromObjC('"
                + messageJSON + "');");
    }

    private JSONObject message2JSONObject(JSBridgeMessage message) {
        JSONObject jo = new JSONObject();
        try {
            if (message.callbackId != null) {
                jo.put("callbackId", message.callbackId);
            }
            if (message.data != null) {
                jo.put("data", message.data);
            }
            if (message.handlerName != null) {
                jo.put("handlerName", message.handlerName);
            }
            if (message.responseId != null) {
                jo.put("responseId", message.responseId);
            }
            if (message.responseData != null) {
                jo.put("responseData", message.responseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    private JSBridgeMessage JSONObject2WVJBMessage(JSONObject jo) {
        JSBridgeMessage message = new JSBridgeMessage();
        try {
            if (jo.has("callbackId")) {
                message.callbackId = jo.getString("callbackId");
            }
            if (jo.has("data")) {
                message.data = jo.get("data");
            }
            if (jo.has("handlerName")) {
                message.handlerName = jo.getString("handlerName");
            }
            if (jo.has("responseId")) {
                message.responseId = jo.getString("responseId");
            }
            if (jo.has("responseData")) {
                message.responseData = jo.get("responseData");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    private void processQueueMessage(String messageQueueString) {
        try {
            JSONArray messages = new JSONArray(messageQueueString);
            for (int i = 0; i < messages.length(); i++) {
                JSONObject jo = messages.getJSONObject(i);
                if (logging)
                    log("RCVD", jo);

                JSBridgeMessage message = JSONObject2WVJBMessage(jo);
                if (message.responseId != null) {
                    JSBridgeResponseCallback responseCallback = responseCallbacks
                            .remove(message.responseId);
                    if (responseCallback != null) {
                        responseCallback.callback(message.responseData);
                    }
                } else {
                    JSBridgeResponseCallback responseCallback = null;
                    if (message.callbackId != null) {
                        final String callbackId = message.callbackId;
                        responseCallback = new JSBridgeResponseCallback() {
                            @Override
                            public void callback(Object data) {
                                JSBridgeMessage msg = new JSBridgeMessage();
                                msg.responseId = callbackId;
                                msg.responseData = data;
                                queueMessage(msg);
                            }
                        };
                    }

                    JSBridgeHandler handler;
                    if (message.handlerName != null) {
                        handler = messageHandlers.get(message.handlerName);
                    } else {
                        handler = messageHandler;
                    }
                    if (handler != null) {
                        handler.request(message.data, responseCallback);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void log(String action, Object json) {
        if (!logging)
            return;
        String jsonString = String.valueOf(json);
        if (jsonString.length() > 500) {
            Log.i(kTag, action + ": " + jsonString.substring(0, 500) + " [...]");
        } else {
            Log.i(kTag, action + ": " + jsonString);
        }
    }

    public void executeJavascript(String script) {
       webView.post(() -> {
        executeJavascript(script, null);
       });
    }

    public void executeJavascript(String script, final JSBridgeCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                    if (callback != null) {
                        if (value != null && value.startsWith("\"")
                                && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1)
                                    .replaceAll("\\\\", "");
                        }
                        callback.onReceiveValue(value);
                    }
                }
            });
        } else {
            if (callback != null) {
                myInterface.addCallback(++uniqueId + "", callback);
                webView.loadUrl("javascript:window." + kInterface
                        + ".onResultForScript(" + uniqueId + "," + script + ")");
            } else {
                webView.loadUrl("javascript:" + script);
            }
        }
    }

    public void setJsRunMethodListener(JSRunMethodListener listener) {
        this.JSRunMethodListener = listener;
    }
}
