package com.divine.yang.login.login;

/**
 * Author: Divine
 * CreateDate: 2021/2/4
 * Describe:
 */
public interface LoginView {
    void loginSuccess(String res);

    void loginFailed(String msg);
}
