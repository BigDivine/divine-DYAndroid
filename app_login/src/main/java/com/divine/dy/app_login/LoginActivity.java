package com.divine.dy.app_login;

import android.content.Intent;
import android.os.Handler;
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
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.getpermission.PermissionList;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.Base64Utils;
import com.divine.dy.lib_utils.sys.SPUtils;
import com.divine.dy.lib_widget.widget.DialogUtils;
import com.divine.dy.lib_widget.widget.EditTextWithClean;
import com.divine.dy.lib_widget.widget.RandomVerificationCodeView;
import com.divine.dy.lib_widget.widget.ToastUtils;
import com.divine.dy.lib_widget.widget.WaveView;

import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2020/11/03
 * Describe: 登录模块界面
 */
public abstract class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, LoginView {
    private final String TAG = "DY-LoginActivity";

    private WaveHelper mWaveHelper1, mWaveHelper2;

    private String userNameStr, userPassStr, userVerCode;
    private boolean isRememberUser;
    private boolean isRememberPWD;
    protected SPUtils mSPUtils;

    private LoginPresenter loginPresenter;
    //是否校验登录，false：不校验，模拟登录；true：校验，正式登录
    protected boolean checkLogin = false;
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
        findView();
        setListener();
        mSPUtils = SPUtils.getInstance(this);
        loginPresenter = new LoginPresenter(this);
        if (LoginBase.needChangeServer) {
            ivLoginChangeServer.setVisibility(View.VISIBLE);
        } else {
            ivLoginChangeServer.setVisibility(View.INVISIBLE);
        }
        vvLoginVerView.setVisibility(LoginBase.needVerifyCode ? View.VISIBLE : View.GONE);
        etLoginUserVer.setVisibility(LoginBase.needVerifyCode ? View.VISIBLE : View.GONE);

        int waveColor = getResources().getColor(R.color.LoginWaveColor);
        wvWaveView1.setWaveColor(waveColor);
        wvWaveView2.setWaveColor(waveColor);
        mWaveHelper1 = new WaveHelper(wvWaveView1, 10000);
        mWaveHelper2 = new WaveHelper(wvWaveView2, 20000);
        mWaveHelper1.start();
        mWaveHelper2.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWaveHelper1)
            mWaveHelper1.cancel();
        if (null != mWaveHelper2)
            mWaveHelper2.cancel();
    }

    @Override
    public void getData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != vvLoginVerView) {
            vvLoginVerView.refresh();
        }
        if (null != mSPUtils)
            setSPInfo();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_submit) {
            loginIn();
        } else if (viewId == R.id.login_submit_test) {
            loginIn();
        } else if (viewId == R.id.login_sms) {
            //            ToastUtils.show(this, "短信登录未开放", Toast.LENGTH_SHORT);
            startActivity(new Intent(this, LoginSmsActivity.class));
        } else if (viewId == R.id.login_change_server) {
            toChangeServer();
        } else if (viewId == R.id.login_ver_view) {
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        if (viewId == R.id.login_remember_name) {
            if (!isChecked) {
                isRememberPWD = false;
                cbLoginRememberPass.setChecked(false);
            }
            isRememberUser = isChecked;
            cbLoginRememberName.setChecked(isChecked);
        } else if (viewId == R.id.login_remember_pass) {
            if (isChecked) {
                isRememberUser = true;
                cbLoginRememberName.setChecked(true);
            }
            isRememberPWD = isChecked;
            cbLoginRememberPass.setChecked(isChecked);
        }
    }

    private void loginIn() {
        DialogUtils.showAppLoadingDialog(this, "登录中。。。");
        try {
            if (!checkLogin) {
                new Handler().postDelayed(() -> {
                    if (DialogUtils.isShowDialog())
                        DialogUtils.dismissLoadingDialog();
                    toMain();
                }, 3000);
            } else {
                userNameStr = etLoginUserName.getText().toString();
                userPassStr = etLoginUserPass.getText().toString();
                if (checkLoginInfo()) {
                    userNameStr = Base64Utils.encode(Base64Utils.encode(userNameStr));
                    userPassStr = Base64Utils.encode(Base64Utils.encode(userPassStr));
                    String params = "{\"username\":\"" + userNameStr + "\"," +
                            "\"pwd\":\"" + userPassStr + "\"," +
                            "\"tenant\":\"" + tenant + "\"," +
                            "\"encrypted\": true" +
                            "}";
                    loginPresenter.login(params);
                }
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
        if (DialogUtils.isShowDialog())
            DialogUtils.dismissLoadingDialog();
    }

    @Override
    public void fail(String msg) {
        Log.e(TAG, "login fail:" + msg);
        ToastUtils.show(this, "登录失败," + msg, Toast.LENGTH_SHORT);
        mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
        if (DialogUtils.isShowDialog())
            DialogUtils.dismissLoadingDialog();
    }

    /**
     * 校验输入框
     *
     * @return
     */
    private boolean checkLoginInfo() {
        userVerCode = etLoginUserVer.getText().toString();
        String trueVerCode = vvLoginVerView.getVerificationCode();
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

    /**
     * 页面数据填充
     */
    private void setSPInfo() {
        try {
            userPassStr = (String) mSPUtils.get(SPKeys.SP_KEY_USER_PASS, "");
            userNameStr = (String) mSPUtils.get(SPKeys.SP_KEY_USER_NAME, "");
            isRememberUser = !TextUtils.isEmpty(userNameStr);
            cbLoginRememberName.setChecked(isRememberUser);
            isRememberPWD = !TextUtils.isEmpty(userPassStr);
            cbLoginRememberPass.setChecked(isRememberPWD);
            if (isRememberPWD) {
                String pass = Base64Utils.decode(Base64Utils.decode(userPassStr));
                String user = Base64Utils.decode(Base64Utils.decode(userNameStr));
                etLoginUserPass.setText(pass);
                etLoginUserName.setText(user);
            } else if (isRememberUser) {
                String user = Base64Utils.decode(Base64Utils.decode(userNameStr));
                etLoginUserPass.setText("");
                etLoginUserName.setText(user);
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
        cbLoginRememberName.setChecked(false);
        cbLoginRememberPass.setChecked(false);
        etLoginUserName.setText("");
        etLoginUserPass.setText("");
        etLoginUserVer.setText("");
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{
                PermissionList.WRITE_EXTERNAL_STORAGE,
                PermissionList.READ_EXTERNAL_STORAGE,
        };
    }

    public abstract void toMain();

    public void toChangeServer() {
        Intent intent = new Intent(this, ChangeServerActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_CODE_CHANGE_SERVER);
    }

    //    private ImageView ivLoginTop;
    private TextView tvLoginTitle;
    private EditTextWithClean etLoginUserName, etLoginUserPass, etLoginUserVer;
    private RandomVerificationCodeView vvLoginVerView;
    private CheckBox cbLoginRememberName, cbLoginRememberPass;
    private Button btLoginSubmit, btLoginSubmitTest, btLoginSms;
    private ImageView ivLoginTitleIcon, ivLoginChangeServer;
    private WaveView wvWaveView1, wvWaveView2;

    private void findView() {
        //        ivLoginTop = findViewById(R.id.login_top);
        ivLoginTitleIcon = findViewById(R.id.login_title_icon);
        tvLoginTitle = findViewById(R.id.login_title);
        etLoginUserName = findViewById(R.id.login_user_name);
        etLoginUserPass = findViewById(R.id.login_user_pass);
        etLoginUserVer = findViewById(R.id.login_user_ver);
        vvLoginVerView = findViewById(R.id.login_ver_view);
        cbLoginRememberName = findViewById(R.id.login_remember_name);
        cbLoginRememberPass = findViewById(R.id.login_remember_pass);
        ivLoginChangeServer = findViewById(R.id.login_change_server);
        btLoginSubmit = findViewById(R.id.login_submit);
        btLoginSubmitTest = findViewById(R.id.login_submit_test);
        btLoginSms = findViewById(R.id.login_sms);
        wvWaveView1 = findViewById(R.id.login_wave1);
        wvWaveView2 = findViewById(R.id.login_wave2);

        tvLoginTitle.setText(LoginBase.loginTitle);
    }

    private void setListener() {
        cbLoginRememberName.setOnCheckedChangeListener(this);
        cbLoginRememberPass.setOnCheckedChangeListener(this);
        btLoginSubmit.setOnClickListener(this);
        btLoginSubmitTest.setOnClickListener(this);
        btLoginSms.setOnClickListener(this);

        if (LoginBase.needChangeServer) {
            ivLoginChangeServer.setOnClickListener(this);
        }
    }
}
