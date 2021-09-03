package com.divine.dy.app_login.register;

import android.util.Log;

import com.divine.dy.app_login.LoginModel;
import com.divine.dy.lib_http.retrofit2.Retrofit2Callback;

/**
 * Author: Divine
 * CreateDate: 2021/8/20
 * Describe:
 */
public class RegisterPresenter {
    private static final String TAG = "DY-Register";

    private LoginModel mLoginModel;
    private RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        this.mLoginModel = new LoginModel();
    }

    public void register(String params) {
        Log.e("register", params);
        mLoginModel.register(params, new Retrofit2Callback<String>() {
            @Override
            public void onSuccess(String response) {
                registerView.registerSuccess(response);
            }

            @Override
            public void onFailed(String msg) {
                registerView.registerFailed(msg);
            }
        });
    }
    public void getSmsVer(String userPhone) {
        Log.e(TAG, "getSmsVer:" + userPhone);
        mLoginModel.getSmsVer(userPhone, new Retrofit2Callback<String>() {
            @Override
            public void onSuccess(String response) {
                registerView.registerGetVerSuccess(response);
            }

            @Override
            public void onFailed(String msg) {
                registerView.registerGetVerFailed(msg);
            }
        });
    }
}
