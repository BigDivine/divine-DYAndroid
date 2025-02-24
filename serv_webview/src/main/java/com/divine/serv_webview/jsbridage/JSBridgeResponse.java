package com.divine.serv_webview.jsbridage;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2021/2/24
 * Describe:
 */
public class JSBridgeResponse {
    public String code;
    public String data;
    public String jsonFlag;
    public Object dataObj;

    public JSBridgeResponse(String code, String data) {
        this.code = code;
        this.data = data;
    }

    public JSBridgeResponse(String code, Object dataObj) {
        this.code = code;
        this.dataObj = dataObj;
    }

    //    public JSBridgeResponse(String code, String data,String jsonFlag) {
//        this.code = code;
//        this.data = data;
//        this.jsonFlag = jsonFlag;
//    }
    public String toObjString() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("data", dataObj);
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String toString() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("code", code);

            if (!TextUtils.isEmpty(data)) {
                obj.put("data", data);
            }
            if (null != dataObj) {
                obj.put("data", dataObj);
            }

            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
