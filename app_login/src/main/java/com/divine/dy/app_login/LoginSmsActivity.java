package com.divine.dy.app_login;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.SPUtils;
import com.divine.dy.lib_widget.widget.EditTextWithClean;
import com.divine.dy.lib_widget.widget.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;

public class LoginSmsActivity extends BaseActivity implements LoginView, View.OnClickListener {
    private static String TAG = "LoginSmsActivity";


    private String userNameStr, smsVerStr;

    private SPUtils mSPUtils;
    private Timer mTimer;
    private int timeDelay;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (null != smsVerBtn) {
                timeDelay--;
                handler.sendEmptyMessage(1);
            }
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int what = msg.what;
            if (what == 1) {
                if (timeDelay <= 0) {
                    if (null != mTimer) {
                        smsVerBtn.setTextColor(getResources().getColor(R.color.backgroundColor));
                        smsVerBtn.setText("获取验证码");
                    }
                } else {
                    smsVerBtn.setTextColor(getResources().getColor(R.color.gray_text));
                    smsVerBtn.setText(timeDelay + "s" + "后重新获取");
                }
            }
        }
    };

    private LoginPresenter loginPresenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login_sms;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        findView();
        setListener();
        loginPresenter = new LoginPresenter(this);
        mSPUtils = SPUtils.getInstance(this);
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_sms_get_ver) {
            if (timeDelay <= 0)
                resetTimer();
        } else if (viewId == R.id.login_sms_submit) {
            userNameStr = userName.getText().toString();
            smsVerStr = smsVer.getText().toString();
        }
    }

    @Override
    public void success(String res) {
        Log.e(TAG, "login success:" + res);
        try {
            JSONObject obj = new JSONObject(res);
            int code = obj.optInt("code");
            String msg = obj.optString("msg");
            String token = obj.optString("token");
            if (code == 0) {
                mSPUtils.put(SPKeys.SP_KEY_TOKEN_WEB, token);
                mSPUtils.put(SPKeys.SP_KEY_USER_NAME_WEB, userNameStr);
                //                navigationTo(RouterManager.router_web);
            } else {
                ToastUtils.show(this, "登录失败:" + msg, Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.show(this, "登录失败,请联系管理员。", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void fail(String msg) {
        Log.e(TAG, "login fail:" + msg);
        ToastUtils.show(this, "登录失败,请联系管理员。", Toast.LENGTH_SHORT);
    }

    private void resetTimer() {
        timeDelay = 3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTimer) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    private EditTextWithClean userName, smsVer;
    private TextView smsVerBtn;
    private Button smsLoginBtn;

    private void findView() {
        userName = findViewById(R.id.login_sms_user_name);
        smsVer = findViewById(R.id.login_sms_ver);
        smsVerBtn = findViewById(R.id.login_sms_get_ver);
        smsLoginBtn = findViewById(R.id.login_sms_submit);
    }

    private void setListener() {
        smsVerBtn.setOnClickListener(this);
        smsLoginBtn.setOnClickListener(this);
    }
}