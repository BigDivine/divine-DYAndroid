package com.divine.yang.login.login;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.divine.yang.login.LoginActivity;
import com.divine.yang.login.LoginBase;
import com.divine.yang.source.SPKeys;
import com.divine.yang.util.sys.SPUtils;
import com.divine.yang.widget.DialogUtils;
import com.divine.yang.widget.ToastUtils;

import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2021/8/23
 * Describe:
 */
public class LoginFunction implements LoginView {
    private static final String TAG = "DY-Login";
    private static LoginFunction mLoginFunction;
    private LoginActivity mActivity;
    private LoginPresenter presenter;
    private String userName, userPass;
    private SPUtils mSPUtils;

    private LoginFunction() {
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
        DialogUtils.showAppLoadingDialog(mActivity, "登录中。。。");
        new Handler().postDelayed(() -> {
            if (DialogUtils.isShowDialog())
                DialogUtils.dismissLoadingDialog();
            Message loginMessage = Message.obtain();
            loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            loginMessage.arg1 = mActivity.LOGIN_SUCCESS;
            mActivity.mHandler.dispatchMessage(loginMessage);
        }, 1000);
    }

    public void userLogin(String userName, String userPass) {
        DialogUtils.showAppLoadingDialog(mActivity, "登录中。。。");
        this.userName = userName;
        this.userPass = userPass;
        String params = "{\"userName\":\"" + userName + "\"" +
                ",\"userPass\":\"" + userPass + "\"" +
                "}";
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
                if (LoginBase.isRememberPwd) {
                    mSPUtils.put(SPKeys.SP_KEY_USER_NAME, userName);
                    mSPUtils.put(SPKeys.SP_KEY_USER_PASS, userPass);
                    mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, true);
                } else if (LoginBase.isRememberUser) {
                    mSPUtils.put(SPKeys.SP_KEY_USER_NAME, userName);
                    mSPUtils.put(SPKeys.SP_KEY_USER_PASS, "");
                    mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
                } else {
                    mSPUtils.put(SPKeys.SP_KEY_USER_NAME, "");
                    mSPUtils.put(SPKeys.SP_KEY_USER_PASS, "");
                    mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
                }
                loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                loginMessage.arg1 = mActivity.LOGIN_SUCCESS;
                mActivity.mHandler.dispatchMessage(loginMessage);
            } else {
                ToastUtils.showShort(mActivity, "登录失败:" + msg + ",请重新登录");
                mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
                loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                loginMessage.arg1 = mActivity.LOGIN_FAIL;
                mActivity.mHandler.dispatchMessage(loginMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(mActivity, "登录失败,请联系管理员。");
            mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
            loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            loginMessage.arg1 = mActivity.LOGIN_FAIL;
            mActivity.mHandler.dispatchMessage(loginMessage);
        }
    }

    @Override
    public void loginFailed(String msg) {
        Log.e(TAG, "login fail:" + msg);
        ToastUtils.showShort(mActivity, "登录失败," + msg);
        mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
        Message loginMessage = Message.obtain();
        loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        loginMessage.arg1 = mActivity.LOGIN_FAIL;
        mActivity.mHandler.dispatchMessage(loginMessage);
    }
}
