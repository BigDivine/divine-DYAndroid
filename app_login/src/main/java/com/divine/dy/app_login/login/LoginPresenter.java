package com.divine.dy.app_login.login;

import android.util.Log;

import com.divine.dy.app_login.LoginModel;
import com.divine.dy.lib_http.retrofit2.Retrofit2Callback;

/**
 * Author: Divine
 * CreateDate: 2021/2/4
 * Describe:
 */
public class LoginPresenter {
    private LoginModel loginModel;
    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    public void login(String params) {
        Log.e("login", params);
        loginModel.login(params, new Retrofit2Callback<String>() {
            @Override
            public void onSuccess(String response) {
                loginView.loginSuccess(response);
            }

            @Override
            public void onFailed(String msg) {
                loginView.loginFailed(msg);
            }
        });
    }
}
