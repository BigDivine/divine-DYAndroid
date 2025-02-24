package com.divine.serv_webview.jsbridage;

/**
 * Author: Divine
 * CreateDate: 2021/2/22
 * Describe:
 */
public interface JSBridgeHandler {
    void request(Object data, JSBridgeResponseCallback callback);
}