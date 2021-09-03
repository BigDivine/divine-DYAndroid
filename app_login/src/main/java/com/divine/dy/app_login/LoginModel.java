package com.divine.dy.app_login;

import com.divine.dy.lib_base.ApiManager;
import com.divine.dy.lib_base.AppBase;
import com.divine.dy.lib_http.retrofit2.Retrofit2Callback;
import com.divine.dy.lib_http.retrofit2.Retrofit2Helper;
import com.divine.dy.lib_http.retrofit2.Retrofit2IModel;
import com.divine.dy.lib_http.retrofit2.Retrofit2Service;
import com.divine.dy.lib_http.retrofit2.RetrofitUtils;
import com.divine.dy.lib_http.retrofit2.RxJava2ResponseTransformer;
import com.divine.dy.lib_http.retrofit2.RxJava2SchedulerTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
