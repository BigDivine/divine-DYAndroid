package com.divine.yang.login;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.login
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/11/25
 * Description   :
 */
public class Login {
    public static final String SP_KEY_USER_NAME = "sp_user_name";
    public static final String SP_KEY_USER_PASS = "sp_user_pass";
    public static final String SP_KEY_IS_LOGIN = "sp_is_login";
    public static final String SP_KEY_SERVER = "sp_server";
    public boolean needChangeServer;
    public boolean needVerifyCode;
    public String loginTitle;

    public boolean isRememberPwd;
    public boolean isRememberUser;
    public LoginListener loginListener;
    private static Login mLogin;

    private Login() {
    }

    public static Login instance() {
        if (null == mLogin) {
            synchronized (Login.class) {
                if (null == mLogin) {
                    mLogin = new Login();
                }
            }
        }
        return mLogin;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
