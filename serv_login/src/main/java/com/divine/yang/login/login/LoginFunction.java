package com.divine.yang.login.login;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.divine.yang.base.utils.DialogUtils;
import com.divine.yang.base.utils.ToastUtils;
import com.divine.yang.login.Login;
import com.divine.yang.login.LoginActivity;
import com.divine.yang.util.sys.SPUtils;

import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2021/8/23
 * Describe:
 */
public class LoginFunction implements LoginView {
    private static final String TAG = "DY-Login";
    private Login mLogin;
    private static LoginFunction mLoginFunction;
    private LoginActivity mActivity;
    private LoginPresenter presenter;
    private String userName, userPass;
    private SPUtils mSPUtils;

    private LoginFunction() {
        mLogin = Login.instance();
        presenter = new LoginPresenter(this);
    }

    public static LoginFunction instance() {
        if (mLoginFunction == null) {
            synchronized (LoginFunction.class) {
                if (mLoginFunction == null) {
                    mLoginFunction = new LoginFunction();
                }
            }
        }
        return mLoginFunction;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = (LoginActivity) mActivity;
        mSPUtils = SPUtils.getInstance(mActivity);
    }

    public void userSimulateLogin() {
        DialogUtils.showLoadingDialog(mActivity, "登录中。。。");
        new Handler().postDelayed(() -> {
            if (DialogUtils.isShowDialog()) DialogUtils.dismissLoadingDialog();
            Message loginMessage = Message.obtain();
            loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            loginMessage.arg1 = mActivity.LOGIN_SUCCESS;
            mActivity.mHandler.dispatchMessage(loginMessage);
        }, 1000);
    }

    public void userLogin(String userName, String userPass) {
        DialogUtils.showLoadingDialog(mActivity, "登录中。。。");
        this.userName = userName;
        this.userPass = userPass;
        String params = "{\"userName\":\"" + userName + "\"" + ",\"userPass\":\"" + userPass + "\"" + "}";
        presenter.login(params);
    }

    @Override
    public void loginSuccess(String res) {
        Log.e(TAG, "login success:" + res);
        Message loginMessage = Message.obtain();
        try {
            JSONObject obj = new JSONObject(res);
            int code = obj.optInt("code");
            String msg = obj.optString("msg");
            if (code == 0) {
                if (mLogin.isRememberPwd) {
                    mSPUtils.put(Login.SP_KEY_USER_NAME, userName);
                    mSPUtils.put(Login.SP_KEY_USER_PASS, userPass);
                    mSPUtils.put(Login.SP_KEY_IS_LOGIN, true);
                } else if (mLogin.isRememberUser) {
                    mSPUtils.put(Login.SP_KEY_USER_NAME, userName);
                    mSPUtils.put(Login.SP_KEY_USER_PASS, "");
                    mSPUtils.put(Login.SP_KEY_IS_LOGIN, false);
                } else {
                    mSPUtils.put(Login.SP_KEY_USER_NAME, "");
                    mSPUtils.put(Login.SP_KEY_USER_PASS, "");
                    mSPUtils.put(Login.SP_KEY_IS_LOGIN, false);
                }
                loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                loginMessage.arg1 = mActivity.LOGIN_SUCCESS;
                mActivity.mHandler.dispatchMessage(loginMessage);
            } else {
                ToastUtils.showShort(mActivity, "登录失败:" + msg + ",请重新登录");
                mSPUtils.put(Login.SP_KEY_IS_LOGIN, false);
                loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                loginMessage.arg1 = mActivity.LOGIN_FAIL;
                mActivity.mHandler.dispatchMessage(loginMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(mActivity, "登录失败,请联系管理员。");
            mSPUtils.put(Login.SP_KEY_IS_LOGIN, false);
            loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            loginMessage.arg1 = mActivity.LOGIN_FAIL;
            mActivity.mHandler.dispatchMessage(loginMessage);
        }
    }

    @Override
    public void loginFailed(String msg) {
        Log.e(TAG, "login fail:" + msg);
        ToastUtils.showShort(mActivity, "登录失败," + msg);
        mSPUtils.put(Login.SP_KEY_IS_LOGIN, false);
        Message loginMessage = Message.obtain();
        loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        loginMessage.arg1 = mActivity.LOGIN_FAIL;
        mActivity.mHandler.dispatchMessage(loginMessage);
    }
}
