package com.divine.dy.app_login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.divine.dy.lib_base.AppConstants;
import com.divine.dy.lib_base.RouterManager;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.getpermission.PermissionList;

import com.divine.dy.app_login.R;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.Base64Utils;
import com.divine.dy.lib_utils.sys.SPUtils;
import com.divine.dy.lib_utils.ui.ToastUtils;
import com.divine.dy.lib_widget.widget.EditTextWithClean;
import com.divine.dy.lib_widget.widget.RandomVerificationCodeView;
import com.divine.dy.lib_widget.widget.WaveView;

import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2020/11/03
 * Describe: 登录模块界面
 */

public abstract class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, LoginView {
    private final String TAG = "LoginActivity";
    private ImageView ivLoginTop;
    private TextView loginTitle;
    private EditTextWithClean userName, userPass, verification;
    private RandomVerificationCodeView verificationView;
    private CheckBox userRemember, passRemember;
    private Button loginBtn, loginSms;
    private ImageView changeServer;

    private String userNameStr, userPassStr, userVerCode;
    private boolean isRememberUser;
    private boolean isRememberPWD;
    protected SPUtils mSPUtils;

    private LoginPresenter loginPresenter;
    protected boolean checkLogin = true;
    private String testUserName = "admin", testUserPass = "admin", tenant = "__default_tenant__";

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        mSPUtils = SPUtils.getInstance(this);
        loginPresenter = new LoginPresenter(this);

        ivLoginTop = findViewById(R.id.iv_login_top);
        loginTitle = findViewById(R.id.login_title);
        userName = findViewById(R.id.user_edt);
        userPass = findViewById(R.id.pwd_edt);
        verification = findViewById(R.id.verification_code);
        verificationView = findViewById(R.id.verification);
        userRemember = findViewById(R.id.remember_user);
        passRemember = findViewById(R.id.remember_pwd);
        changeServer = findViewById(R.id.change_server);
        loginBtn = findViewById(R.id.login_btn);
        //                loginSms = findViewById(R.id.login_sms);
        loginTitle.setText(LoginBase.loginTitle);

        userRemember.setOnCheckedChangeListener(this);
        passRemember.setOnCheckedChangeListener(this);
        loginBtn.setOnClickListener(this);
        if (LoginBase.needChangeServer) {
            changeServer.setVisibility(View.VISIBLE);
            changeServer.setOnClickListener(this);
        } else {
            changeServer.setVisibility(View.INVISIBLE);
        }
        verificationView.setVisibility(LoginBase.needVerifyCode ? View.VISIBLE : View.GONE);
        verification.setVisibility(LoginBase.needVerifyCode ? View.VISIBLE : View.GONE);
        //        loginSms.setOnClickListener(this);
        WaveView waveView1 = (WaveView) findViewById(R.id.wave1);
        waveView1.setWaveColor(getResources().getColor(R.color.LoginWaveColor));
        mWaveHelper1 = new WaveHelper(waveView1, 10000);
        mWaveHelper1.start();
        WaveView waveView2 = (WaveView) findViewById(R.id.wave2);
        waveView2.setWaveColor(getResources().getColor(R.color.LoginWaveColor));
        mWaveHelper2 = new WaveHelper(waveView2, 20000);
        mWaveHelper2.start();
        WaveView waveView3 = (WaveView) findViewById(R.id.wave3);
        waveView3.setWaveColor(getResources().getColor(R.color.LoginWaveColor));
        mWaveHelper3 = new WaveHelper(waveView3, 30000);
        mWaveHelper3.start();

    }

    WaveHelper mWaveHelper1, mWaveHelper2, mWaveHelper3;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWaveHelper1)
            mWaveHelper1.cancel();
        if (null != mWaveHelper2)
            mWaveHelper2.cancel();
        if (null != mWaveHelper3)
            mWaveHelper3.cancel();
    }

    @Override
    public void getData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != verificationView) {
            verificationView.refresh();
        }
        if (null != mSPUtils)
            setSPInfo();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_btn) {
            loginIn();
            //        } else if (viewId == R.id.login_sms) {
            //            ToastUtils.show(this, "短信登录未开放", Toast.LENGTH_SHORT);
            //            //            startActivity(new Intent(this, LoginSmsActivity.class));
        } else if (viewId == R.id.change_server) {
            toChangeServer();
        } else if (viewId == R.id.verification) {
        }
    }

    public void toChangeServer() {
        Intent intent = new Intent(this, ChangeServerActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_CODE_CHANGE_SERVER);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        if (viewId == R.id.remember_user) {
            if (!isChecked) {
                isRememberPWD = false;
                passRemember.setChecked(false);
            }
            isRememberUser = isChecked;
            userRemember.setChecked(isChecked);
        } else if (viewId == R.id.remember_pwd) {
            if (isChecked) {
                isRememberUser = true;
                userRemember.setChecked(true);
            }
            isRememberPWD = isChecked;
            passRemember.setChecked(isChecked);
        }
    }

    private void loginIn() {
        try {
            userNameStr = userName.getText().toString();
            userPassStr = userPass.getText().toString();
            if (!checkLogin) {
                userNameStr = Base64Utils.encode(Base64Utils.encode(testUserName));
                userPassStr = Base64Utils.encode(Base64Utils.encode(testUserPass));
            }
            if (checkLoginInfo()) {
                userNameStr = Base64Utils.encode(Base64Utils.encode(userNameStr));
                userPassStr = Base64Utils.encode(Base64Utils.encode(userPassStr));
                String params = "{\"username\":\"" + userNameStr + "\"," +
                        "\"pwd\":\"" + userPassStr + "\"," +
                        //                        "\"tenantName\":\"jiuqi\"}";
                        "\"tenant\":\"" + tenant + "\"," +
                        "\"encrypted\": true" +
                        "}";
                loginPresenter.login(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(String res) {
        try {
            JSONObject obj = new JSONObject(res);
            int code = obj.optInt("code");
            String msg = obj.optString("msg");
            String token = obj.optString("token");
            if (code == 0) {
                //                String encodePass = Base64Utils.encode2String(userPassStr);
                if (isRememberPWD) {
                    mSPUtils.put(SPKeys.SP_KEY_USER_NAME, userNameStr);
                    mSPUtils.put(SPKeys.SP_KEY_USER_PASS, userPassStr);
                    mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, true);
                } else if (isRememberUser) {
                    mSPUtils.put(SPKeys.SP_KEY_USER_NAME, userNameStr);
                    mSPUtils.put(SPKeys.SP_KEY_USER_PASS, "");
                    mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
                } else {
                    mSPUtils.put(SPKeys.SP_KEY_USER_NAME, "");
                    mSPUtils.put(SPKeys.SP_KEY_USER_PASS, "");
                    mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
                }
                mSPUtils.put(SPKeys.SP_KEY_TOKEN_WEB, token);
                String userNameWeb = Base64Utils.decode(Base64Utils.decode(userNameStr));
                mSPUtils.put(SPKeys.SP_KEY_USER_NAME_WEB, userNameWeb);

//                navigationTo(RouterManager.router_web);

                this.finish();
            } else {
                ToastUtils.show(this, "登录失败:" + msg + ",请重新登录", Toast.LENGTH_SHORT);
                mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(this, "登录失败,请联系管理员。", Toast.LENGTH_SHORT);
            mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
        }
    }

    @Override
    public void fail(String msg) {
        Log.e(TAG, "login fail:" + msg);
        ToastUtils.show(this, "登录失败," + msg, Toast.LENGTH_SHORT);
        mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
    }

    private boolean checkLoginInfo() {
        userVerCode = verification.getText().toString();
        String trueVerCode = verificationView.getVerificationCode();
        if (TextUtils.isEmpty(userNameStr)) {
            ToastUtils.show(this, "请输入用户名", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(userPassStr)) {
            ToastUtils.show(this, "请输入密码", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(userVerCode)) {
            if (LoginBase.needVerifyCode) {
                ToastUtils.show(this, "请输入验证码", Toast.LENGTH_SHORT);
                return false;
            } else {
                return true;
            }
        } else if (TextUtils.equals(trueVerCode, userVerCode)) {
            if (LoginBase.needVerifyCode) {
                ToastUtils.show(this, "验证码错误，请重新输入", Toast.LENGTH_SHORT);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void setSPInfo() {
        try {
            userPassStr = (String) mSPUtils.get(SPKeys.SP_KEY_USER_PASS, "");
            userNameStr = (String) mSPUtils.get(SPKeys.SP_KEY_USER_NAME, "");
            //            userPassStr = Base64Utils.decode(encodePass);
            isRememberUser = !TextUtils.isEmpty(userNameStr);
            userRemember.setChecked(isRememberUser);
            isRememberPWD = !TextUtils.isEmpty(userPassStr);
            passRemember.setChecked(isRememberPWD);
            if (isRememberPWD) {
                String pass = Base64Utils.decode(Base64Utils.decode(userPassStr));
                String user = Base64Utils.decode(Base64Utils.decode(userNameStr));
                userPass.setText(pass);
                userName.setText(user);
            } else if (isRememberUser) {
                String user = Base64Utils.decode(Base64Utils.decode(userNameStr));
                userPass.setText("");
                userName.setText(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUserInfo() {
        mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
        mSPUtils.put(SPKeys.SP_KEY_USER_NAME, "");
        mSPUtils.put(SPKeys.SP_KEY_USER_PASS, "");
        mSPUtils.put(SPKeys.SP_KEY_TOKEN_WEB, "");
        mSPUtils.put(SPKeys.SP_KEY_USER_NAME_WEB, "");
        isRememberUser = false;
        isRememberPWD = false;
        userRemember.setChecked(false);
        passRemember.setChecked(false);
        userName.setText("");
        userPass.setText("");
        verification.setText("");
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{
                PermissionList.WRITE_EXTERNAL_STORAGE,
                PermissionList.READ_EXTERNAL_STORAGE,
                //                PermissionList.MOUNT_UNMOUNT_FILESYSTEMS
        };
    }
    //    @Override
    //    public boolean onLongClick(View v) {
    //        if (v.getId() == R.id.iv_login_top) {
    //            LoginBase.needVerifyCode = !LoginBase.needVerifyCode;
    //            verificationView.setVisibility(LoginBase.needVerifyCode ? View.VISIBLE : View.GONE);
    //            verification.setVisibility(LoginBase.needVerifyCode ? View.VISIBLE : View.GONE);
    //            return true;
    //        }
    //        return false;
    //    }
}
