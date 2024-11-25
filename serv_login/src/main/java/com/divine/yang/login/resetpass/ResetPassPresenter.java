package com.divine.yang.login.resetpass;

import com.divine.yang.login.LoginModel;
import com.divine.yang.http.retrofit2.Retrofit2Callback;

/**
 * Author: Divine
 * CreateDate: 2021/8/24
 * Describe:
 */
public class ResetPassPresenter {
    private LoginModel mLoginModel;
    private ResetPassView mResetPassView;

    public ResetPassPresenter(ResetPassView mResetPassView) {
        mLoginModel = new LoginModel();
        this.mResetPassView = mResetPassView;
    }

    public void resetPass(String params) {
        mLoginModel.resetPass(params, new Retrofit2Callback<String>() {
            @Override
            public void onSuccess(String response) {
                mResetPassView.resetPassSuccess(response);
            }

            @Override
            public void onFailed(String msg) {
                mResetPassView.resetPassFailed(msg);
            }
        });
    }
}
