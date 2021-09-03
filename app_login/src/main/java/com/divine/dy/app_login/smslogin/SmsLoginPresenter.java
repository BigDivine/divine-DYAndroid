package com.divine.dy.app_login.smslogin;

import android.util.Log;

import com.divine.dy.app_login.LoginModel;
import com.divine.dy.lib_http.retrofit2.Retrofit2Callback;

/**
 * Author: Divine
 * CreateDate: 2021/8/20
 * Describe:
 */
public class SmsLoginPresenter {
    private static final String TAG = "DY-SmsLoginP";
    private LoginModel mLoginModel;
    private SmsLoginView smsLoginView;

    public SmsLoginPresenter(SmsLoginView smsLoginView) {
        this.smsLoginView = smsLoginView;
        this.mLoginModel = new LoginModel();
    }

    public void smsLogin(String params) {
        Log.e(TAG, "smsLogin:" + params);
        mLoginModel.smsLogin(params, new Retrofit2Callback<String>() {
            @Override
            public void onSuccess(String response) {
                smsLoginView.smsLoginSuccess(response);
            }

            @Override
            public void onFailed(String msg) {
                smsLoginView.smsLoginFailed(msg);
            }
        });
    }

    public void getSmsVer(String userPhone) {
        Log.e(TAG, "getSmsVer:" + userPhone);
        mLoginModel.getSmsVer(userPhone, new Retrofit2Callback<String>() {
            @Override
            public void onSuccess(String response) {
                smsLoginView.smsLoginGetVerSuccess(response);
            }

            @Override
            public void onFailed(String msg) {
                smsLoginView.smsLoginGetVerFailed(msg);
            }
        });
    }
}
