package com.divine.serv_webview.jsbridage;

/**
 * Author: Divine
 * CreateDate: 2021/2/25
 * Describe:
 */
public interface JSRunMethodListener {
    void runMethod(String name,Object data, JSBridgeResponseCallback callback);
}
