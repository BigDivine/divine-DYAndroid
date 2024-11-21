package com.divine.yang.login.register;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.divine.yang.login.LoginActivity;
import com.divine.yang.login.smslogin.SmsLoginExample;
import com.divine.yang.http.retrofit2.CustomResponse;
import com.divine.yang.widget.DialogUtils;
import com.divine.yang.widget.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2021/8/20
 * Describe:
 */
public class RegisterFunction implements RegisterView {
    private static final String TAG = "DY-UserRegister";
    private static RegisterFunction mRegisterFunction;
    private RegisterPresenter presenter;
    private LoginActivity mActivity;

    private RegisterFunction() {
        presenter = new RegisterPresenter(this);
    }

    public static RegisterFunction instance() {
        if (mRegisterFunction == null) {
            synchronized (RegisterFunction.class) {
                if (mRegisterFunction == null) {
                    mRegisterFunction = new RegisterFunction();
                }
            }
        }
        return mRegisterFunction;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = (LoginActivity) mActivity;
    }

    public void getPhoneVer(String userPhone) {
        DialogUtils.showAppLoadingDialog(mActivity, "正在获取验证码");
        presenter.getSmsVer(userPhone);
    }

    public void userRegister(String userName, String userPass, String userPhone, String userPhoneVer) {
        DialogUtils.showAppLoadingDialog(mActivity, "正在注册");
        String params = "{\"userName\":\"" + userName + "\"" +
                ",\"userPass\":\"" + userPass + "\"" +
                ",\"userPhone\":\"" + userPhone + "\"" +
                ",\"userPhoneVer\":\"" + userPhoneVer + "\"" +
                "}";
        presenter.register(params);
    }

    @Override
    public void registerSuccess(String res) {
        Log.e(TAG, "register success:" + res);
        CustomResponse<String> register = new Gson().fromJson(res, CustomResponse.class);
        if (register.code == 0) {
            ToastUtils.showShort(mActivity, "注册成功");
            Message registerMessage = Message.obtain();
            registerMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            registerMessage.arg1 = mActivity.REGISTER_SUCCESS;
            mActivity.mHandler.dispatchMessage(registerMessage);
        } else {
            ToastUtils.showShort(mActivity, "注册失败：" + register.msg);
            Message registerMessage = Message.obtain();
            registerMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
            registerMessage.arg1 = mActivity.REGISTER_FAIL;
            mActivity.mHandler.dispatchMessage(registerMessage);
        }

    }

    @Override
    public void registerFailed(String msg) {
        Log.e(TAG, "register fail:" + msg);
        ToastUtils.showShort(mActivity, "注册失败：" + msg);
        Message registerMessage = Message.obtain();
        registerMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        registerMessage.arg1 = mActivity.REGISTER_FAIL;
        mActivity.mHandler.dispatchMessage(registerMessage);
    }

    @Override
    public void registerGetVerSuccess(String res) {
        Log.e(TAG, "register get ver success:" + res);
        try {
            JSONObject object = new JSONObject(res);
            int code = object.optInt("code");
            if (code == 0) {
                String data = object.optString("data");
                JSONObject dataObj = new JSONObject(data);
                String smsVer = dataObj.optString("smsVer");
                SmsLoginExample.RegisterSmsVer = smsVer;
                Message resetPassMessage = Message.obtain();
                resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                resetPassMessage.arg1 = mActivity.REGISTER_GET_VER_SUCCESS;
                mActivity.mHandler.dispatchMessage(resetPassMessage);
            }else{
                String data = object.optString("msg");
//                JSONObject dataObj = new JSONObject(data);
//                String smsVer = dataObj.optString("smsVer");
//                SmsLoginExample.RegisterSmsVer = smsVer;
                Message resetPassMessage = Message.obtain();
                resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
                resetPassMessage.arg1 = mActivity.REGISTER_GET_VER_FAIL;
                mActivity.mHandler.dispatchMessage(resetPassMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerGetVerFailed(String msg) {
        Log.e(TAG, "register get ver fail:" + msg);
        Message registerMessage = Message.obtain();
        registerMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        registerMessage.arg1 = mActivity.REGISTER_GET_VER_FAIL;
        mActivity.mHandler.dispatchMessage(registerMessage);
    }
}
