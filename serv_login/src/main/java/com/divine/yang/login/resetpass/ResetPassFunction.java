package com.divine.yang.login.resetpass;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.divine.yang.base.utils.DialogUtils;
import com.divine.yang.login.LoginActivity;

/**
 * Author: Divine
 * CreateDate: 2021/8/24
 * Describe:
 */
public class ResetPassFunction implements ResetPassView {
    private static final String TAG = "DY-ResetPass";
    private static ResetPassFunction mResetPassFunction;
    private ResetPassPresenter presenter;
    private LoginActivity mActivity;

    private ResetPassFunction() {
        presenter = new ResetPassPresenter(this);
    }

    public static ResetPassFunction instance() {
        if (mResetPassFunction == null) {
            synchronized (ResetPassFunction.class) {
                if (mResetPassFunction == null) {
                    mResetPassFunction = new ResetPassFunction();
                }
            }
        }
        return mResetPassFunction;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = (LoginActivity) mActivity;
    }

    public void resetPass(String user, String oldPass, String newPass) {
        DialogUtils.showAppLoadingDialog(mActivity, "请稍后...");
        String params = "{\"user\":\"" + user + "\"" +
                ",\"userPassOld\":\"" + oldPass + "\"" +
                ",\"userPassNew\":\"" + newPass + "\"" +
                "}";
        presenter.resetPass(params);
    }

    @Override
    public void resetPassSuccess(String res) {
        Log.e(TAG, "resetPass success:" + res);
        Message resetPassMessage = Message.obtain();
        resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        resetPassMessage.arg1 = mActivity.RESET_PASS_SUCCESS;
        mActivity.mHandler.dispatchMessage(resetPassMessage);
    }

    @Override
    public void resetPassFailed(String message) {
        Log.e(TAG, "resetPass fail:" + message);
        Message resetPassMessage = Message.obtain();
        resetPassMessage.what = mActivity.HIDDEN_DIALOG_WHAT;
        resetPassMessage.arg1 = mActivity.RESET_PASS_FAIL;
        mActivity.mHandler.dispatchMessage(resetPassMessage);
    }
}
