package com.divine.yang.login.smslogin;

/**
 * Author: Divine
 * CreateDate: 2021/8/20
 * Describe:
 */
public interface SmsLoginView {
    void smsLoginSuccess(String res);

    void smsLoginFailed(String msg);

    void smsLoginGetVerSuccess(String res);

    void smsLoginGetVerFailed(String msg);
}
