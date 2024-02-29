package com.divine.dy.app_login.smslogin;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.divine.dy.app_login.LoginActivity;
import com.divine.dy.lib_http.retrofit2.CustomResponse;
import com.divine.dy.lib_widget.widget.DialogUtils;
import com.divine.dy.lib_widget.widget.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2021/8/20
 * Describe:
 */
public class SmsLoginFunction implements SmsLoginView {
    private static final String TAG = "DY-SmsLogin";
    private static SmsLoginFunction smsLoginFunction;
    private LoginActivity mActivity;
    private SmsLoginPresenter presenter;

    private SmsLoginFunction() {
        presenter = new SmsLoginPresenter(this);
    }

    public static SmsLoginFunction instance() {
        if (smsLoginFunction == null) {
            synchronized (SmsLoginFunction.class) {
                if (smsLoginFunction == null) {
                    smsLoginFunction = new SmsLoginFunction();
                }
            }
        }
        return smsLoginFunction;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = (LoginActivity) mActivity;
    }

    public void getSmsVer(String userPhone) {
        DialogUtils.showAppLoadingDialog(mActivity, "正在获取验证码");
        presenter.getSmsVer(userPhone);
    }

    public void userSmsLogin(String userPhone, String userPhoneVer) {
        DialogUtils.showAppLoadingDialog(mActivity, "登录中...");
        String params = "{\"userPhone\":" + userPhone + ",\"userPhoneVer\":" + userPhoneVer + "}";
        presenter.smsLogin(params);
    }

    @Override
    public void smsLoginSuccess(String res) {
        Log.e(TAG, "register success:" + res);
        CustomResponse<String> smsLogin = new Gson().fromJson(res, CustomResponse.class);

        if (smsLogin.code == 0) {
            Message loginMessage = Message.obtain();
            loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            loginMessage.arg1 = mActivity.LOGIN_SUCCESS;
            mActivity.mHandler.dispatchMessage(loginMessage);
        } else {
            ToastUtils.showShort(mActivity, "登录失败：" + smsLogin.msg);
            Message loginMessage = Message.obtain();
            loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            loginMessage.arg1 = mActivity.LOGIN_FAIL;
            mActivity.mHandler.dispatchMessage(loginMessage);
        }
    }

    @Override
    public void smsLoginFailed(String msg) {
        Log.e(TAG, "register fail:" + msg);
        ToastUtils.showShort(mActivity, "登录失败：" + msg);
        Message loginMessage = Message.obtain();
        loginMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        loginMessage.arg1 = mActivity.LOGIN_FAIL;
        mActivity.mHandler.dispatchMessage(loginMessage);
    }

    @Override
    public void smsLoginGetVerSuccess(String res) {
        Log.e(TAG, "getVer success:" + res);
        CustomResponse<String> smsLogin = new Gson().fromJson(res, CustomResponse.class);
        try {
            if (smsLogin.code == 0) {
                String data = smsLogin.data;
                JSONObject dataObj = new JSONObject(data);
                String smsVer = dataObj.optString("smsVer");
                SmsLoginExample.SmsVer = smsVer;
                ToastUtils.showShort(mActivity, "获取验证码成功！");
                Message resetPassMessage = Message.obtain();
                resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                resetPassMessage.arg1 = mActivity.GET_VER_SUCCESS;
                mActivity.mHandler.dispatchMessage(resetPassMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort(mActivity, "获取验证码失败");
            Message resetPassMessage = Message.obtain();
            resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            resetPassMessage.arg1 = mActivity.GET_VER_FAIL;
            mActivity.mHandler.dispatchMessage(resetPassMessage);  }
    }

    @Override
    public void smsLoginGetVerFailed(String msg) {
        Log.e(TAG, "getVer fail:" + msg);
        ToastUtils.showShort(mActivity, "获取验证码失败，请重新获取！" + msg);
        Message resetPassMessage = Message.obtain();
        resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        mActivity.mHandler.dispatchMessage(resetPassMessage);
    }
}
