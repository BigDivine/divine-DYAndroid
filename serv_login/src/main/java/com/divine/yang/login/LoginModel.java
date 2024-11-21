package com.divine.yang.login;

import com.divine.yang.base.ApiManager;
import com.divine.yang.base.AppBase;
import com.divine.yang.http.retrofit2.Retrofit2Callback;
import com.divine.yang.http.retrofit2.Retrofit2IModel;
import com.divine.yang.http.retrofit2.RetrofitUtils;

import okhttp3.RequestBody;

/**
 * Author: Divine
 * CreateDate: 2021/2/4
 * Describe:
 */
public class LoginModel extends Retrofit2IModel {
    public void login(String params, Retrofit2Callback callback) {
        //        params = "{\"userName\":\"admin\"," +
        //                "\"userPass\":\"admin\"" +
        //                "}";
        sendRequestPost(AppBase.serverUrl + ApiManager.userLoginApi
                , RetrofitUtils.String2RequestBody(params)
                , callback
        );
    }

    public void smsLogin(String params, Retrofit2Callback callback) {
        String serverUrl = AppBase.serverUrl + ApiManager.userSmsLoginApi;
        RequestBody body = RetrofitUtils.String2RequestBody(params);
        sendRequestPost(serverUrl, body, callback);
    }

    public void getSmsVer(String phone, Retrofit2Callback callback) {
        String serverUrl = AppBase.serverUrl + ApiManager.userGetSmsVerApi;
        RequestBody body = RetrofitUtils.String2RequestBody("{\"userPhone\":\"" + phone + "\"}");
        sendRequestPost(serverUrl, body, callback);
    }

    public void resetPass(String params, Retrofit2Callback<String> callback) {
        String serverUrl = AppBase.serverUrl + ApiManager.userResetPassApi;
        RequestBody body = RetrofitUtils.String2RequestBody(params);
        sendRequestPost(serverUrl, body, callback);
    }

    public void register(String params, Retrofit2Callback callback) {
        String serverUrl = AppBase.serverUrl + ApiManager.userRegisterApi;
        RequestBody body = RetrofitUtils.String2RequestBody(params);
        sendRequestPost(serverUrl, body, callback);
    }
}
