package com.divine.dy.app_login.register;

/**
 * Author: Divine
 * CreateDate: 2021/8/20
 * Describe:
 */
public interface RegisterView {
    void registerSuccess(String res);

    void registerFailed(String msg);

    void registerGetVerSuccess(String res);

    void registerGetVerFailed(String msg);
}
