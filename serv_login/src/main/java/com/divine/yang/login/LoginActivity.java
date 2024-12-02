package com.divine.yang.login;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.base.utils.DialogUtils;
import com.divine.yang.base.utils.ToastUtils;
import com.divine.yang.login.login.LoginFunction;
import com.divine.yang.login.register.RegisterFunction;
import com.divine.yang.login.resetpass.ResetPassFunction;
import com.divine.yang.login.smslogin.SmsLoginExample;
import com.divine.yang.login.smslogin.SmsLoginFunction;
import com.divine.yang.util.sys.SPUtils;
import com.divine.yang.widget.EditTextWithLRIcon;
import com.divine.yang.widget.RandomVerificationCodeView;
import com.divine.yang.widget.WaterMarkView;
import com.divine.yang.widget.WaveView;


/**
 * Author: Divine
 * CreateDate: 2020/11/03
 * Describe: 登录模块界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final String TAG = "DY-LoginActivity";
    private WaveHelper mWaveHelper1, mWaveHelper2;
    protected SPUtils mSPUtils;
    private boolean canGetSmsVer = true;
    private boolean canGetRegisterVer = true;
    public final int GET_VER_WHAT = 1;
    public final int REGISTER_GET_VER_WHAT = 11;
    public final int HIDDEN_DIALOG_WHAT = 2;
    public final int REGISTER_SUCCESS = 1001;
    public final int REGISTER_FAIL = 1002;
    public final int REGISTER_GET_VER_SUCCESS = 1003;
    public final int REGISTER_GET_VER_FAIL = 1004;
    public final int LOGIN_SUCCESS = 2001;
    public final int LOGIN_FAIL = 2002;
    public final int GET_VER_SUCCESS = 20011;
    public final int GET_VER_FAIL = 20012;
    public final int RESET_PASS_SUCCESS = 3001;
    public final int RESET_PASS_FAIL = 3002;
    private Login mLogin;
    private LoginListener mLoginListener;

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == GET_VER_WHAT) {
                if (message.arg1 <= 0) {
                    btLoginSmsGetVer.setText("获取验证码");
                    btLoginSmsGetVer.setTextColor(getColor(com.divine.yang.theme.R.color.BaseThemeColor));
                    canGetSmsVer = true;
                    getSmsVerTimeDelay = 5;
                } else {
                    btLoginSmsGetVer.setText("已获取验证码(" + message.arg1 + "s)");
                    btLoginSmsGetVer.setTextColor(getColor(R.color.gray_text));
                    canGetSmsVer = false;
                    mHandler.postDelayed(getSmsVerRunnable, 1000);
                }
                return true;
            } else if (message.what == REGISTER_GET_VER_WHAT) {
                if (message.arg1 <= 0) {
                    btRegisterUserGetPhoneVer.setText("获取验证码");
                    btRegisterUserGetPhoneVer.setTextColor(getColor(com.divine.yang.theme.R.color.BaseThemeColor));
                    canGetRegisterVer = true;
                    registerGetVerTimeDelay = 5;
                } else {
                    btRegisterUserGetPhoneVer.setText("已获取验证码(" + message.arg1 + "s)");
                    btRegisterUserGetPhoneVer.setTextColor(getColor(R.color.gray_text));
                    canGetRegisterVer = false;
                    mHandler.postDelayed(registerGetVerRunnable, 1000);
                }
                return true;
            } else if (message.what == HIDDEN_DIALOG_WHAT) {
                if (DialogUtils.isShowDialog()) {
                    DialogUtils.dismissLoadingDialog();
                }
                if (message.arg1 == RESET_PASS_SUCCESS) {
                    clearResetPassPanel();
                    startAnimation(resetPass, loginCommon);
                } else if (message.arg1 == REGISTER_SUCCESS) {
                    clearRegisterPanel();
                    startAnimation(register, loginCommon);
                } else if (message.arg1 == GET_VER_SUCCESS) {
                    Log.e(TAG, SmsLoginExample.SmsVer);
                    etLoginUserPhoneVer.setText(SmsLoginExample.SmsVer);
                    startGetSmsVerTimer();
                } else if (message.arg1 == REGISTER_GET_VER_SUCCESS) {
                    Log.e(TAG, SmsLoginExample.RegisterSmsVer);
                    etRegisterUserPhoneVer.setText(SmsLoginExample.RegisterSmsVer);
                    startRegisterGetVerTimer();
                } else if (message.arg1 == LOGIN_SUCCESS) {
                    toHome();
                } else {
                    return false;
                }
                return true;
            }
            return false;
        }
    });

    int getSmsVerTimeDelay = 5;
    Runnable getSmsVerRunnable = () -> {
        Message getSmsVerMessage = new Message();
        getSmsVerMessage.what = GET_VER_WHAT;
        getSmsVerMessage.arg1 = getSmsVerTimeDelay;
        mHandler.dispatchMessage(getSmsVerMessage);
        getSmsVerTimeDelay--;
    };
    int registerGetVerTimeDelay = 5;
    Runnable registerGetVerRunnable = () -> {
        Message getSmsVerMessage = new Message();
        getSmsVerMessage.what = REGISTER_GET_VER_WHAT;
        getSmsVerMessage.arg1 = registerGetVerTimeDelay;
        mHandler.dispatchMessage(getSmsVerMessage);
        registerGetVerTimeDelay--;
    };

    /**
     * 获取手机验证码相关
     */
    public void startGetSmsVerTimer() {
        mHandler.postDelayed(getSmsVerRunnable, 0);
    }

    public void startRegisterGetVerTimer() {
        mHandler.postDelayed(registerGetVerRunnable, 0);
    }

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
        mLogin = Login.instance();
        mLoginListener = mLogin.loginListener;
        findView();
        setListener();

        mSPUtils = SPUtils.getInstance(this);
        if (mLogin.needChangeServer) {
            ivLoginChangeServer.setVisibility(View.VISIBLE);
        } else {
            ivLoginChangeServer.setVisibility(View.INVISIBLE);
        }
        vvLoginVerView.setVisibility(mLogin.needVerifyCode ? View.VISIBLE : View.GONE);
        etLoginUserVer.setVisibility(mLogin.needVerifyCode ? View.VISIBLE : View.GONE);

        int waveColor = getColor(com.divine.yang.theme.R.color.GradientColor);
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
        if (null != mWaveHelper1) mWaveHelper1.cancel();
        if (null != mWaveHelper2) mWaveHelper2.cancel();
    }

    @Override
    public void getData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        createFunctions();

        if (null != vvLoginVerView) {
            vvLoginVerView.refresh();
        }
        if (null != mSPUtils) setSPInfo();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_ver_view) {
            // 用户名登录刷新验证码
        } else if (viewId == R.id.login_submit) {
            // 用户名登录
            if (checkLoginInfo()) {
                    String userNameStr = etLoginUserName.getText().toString();
                    String userPassStr = etLoginUserPass.getText().toString();
                    mLoginFunction.userLogin(userNameStr, userPassStr);
            }
        } else if (viewId == R.id.oauth_login_wx) {
            // 微信登录
        } else if (viewId == R.id.oauth_login_qq) {
            // qq登录
        } else if (viewId == R.id.oauth_login_sina) {
            // 微博登录
        } else if (viewId == R.id.oauth_login_simulate) {
            // 模拟登录
            mLoginFunction.userSimulateLogin();
        } else if (viewId == R.id.login_sms_get_ver) {
            // 手机登录--获取手机短信验证码
            if (canGetSmsVer) {
                String loginSmsPhone = etLoginUserPhone.getText().toString();
                mSmsLoginFunction.getSmsVer(loginSmsPhone);
            }
        } else if (viewId == R.id.login_sms_submit) {
            // 发请求短信登录
            if (checkSmsLoginInfo()) {
                String loginSmsPhone = etLoginUserPhone.getText().toString();
                String loginSmsPhoneVer = etLoginUserPhoneVer.getText().toString();
                mSmsLoginFunction.userSmsLogin(loginSmsPhone, loginSmsPhoneVer);
            }
        } else if (viewId == R.id.register_user_get_phone_ver) {
            // 注册---获取手机短信验证码
            if (canGetRegisterVer) {
                String registerUserPhone = etRegisterUserPhone.getText().toString();
                mRegisterFunction.getPhoneVer(registerUserPhone);
            }
        } else if (viewId == R.id.register_submit) {
            // 发请求注册
            if (checkRegisterInfo()) {
                String registerUserName = etRegisterUserName.getText().toString();
                String registerUserPass = etRegisterUserPass.getText().toString();
                String registerUserPhone = etRegisterUserPhone.getText().toString();
                String registerUserPhoneVer = etRegisterUserPhoneVer.getText().toString();
                mRegisterFunction.userRegister(registerUserName, registerUserPass, registerUserPhone, registerUserPhoneVer);
            }
        } else if (viewId == R.id.reset_pass_submit) {
            // 重置密码确定
            if (checkResetPassInfo()) {
                String user = etResetPassUser.getText().toString();
                String oldPass = etResetPassOld.getText().toString();
                String newPass = etResetPassNew.getText().toString();
                mResetPassFunction.resetPass(user, oldPass, newPass);
            }
        } else if (viewId == R.id.change_server_submit) {
            // 切换服务器地址确定-缓存新的服务地址，并重启应用
            String newServer = etChangeServerHost.getText().toString();
            mSPUtils.put(Login.SP_KEY_SERVER, newServer);
            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // 杀掉以前进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (viewId == R.id.login_register) {
            // 用户注册
            startAnimation(loginCommon, register);
        } else if (viewId == R.id.login_miss_pass) {
            // 忘记密码
            startAnimation(loginCommon, resetPass);
        } else if (viewId == R.id.login_sms) {
            // 切换为短信登录
            startAnimation(loginCommon, loginSms);
        } else if (viewId == R.id.login_change_server) {
            // 切换服务地址
            startAnimation(loginCommon, changeServer);
        } else if (viewId == R.id.login_sms_to_login) {
            // 放弃短信登录，用账号密码登录
            clearSmsLoginPanel();
            startAnimation(loginSms, loginCommon);
        } else if (viewId == R.id.register_to_login) {
            // 放弃注册，直接登录
            clearRegisterPanel();
            startAnimation(register, loginCommon);
        } else if (viewId == R.id.reset_pass_to_login) {
            // 想起密码，不重置，直接登录
            clearResetPassPanel();
            startAnimation(resetPass, loginCommon);
        } else if (viewId == R.id.change_server_to_login) {
            // 取消设置服务器地址
            clearChangeServerPanel();
            startAnimation(changeServer, loginCommon);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        if (viewId == R.id.login_remember_name) {
            if (!isChecked) {
                mLogin.isRememberPwd = false;
                cbLoginRememberPass.setChecked(false);
            }
            mLogin.isRememberUser = isChecked;
            cbLoginRememberName.setChecked(isChecked);
        } else if (viewId == R.id.login_remember_pass) {
            if (isChecked) {
                mLogin.isRememberUser = true;
                cbLoginRememberName.setChecked(true);
            }
            mLogin.isRememberPwd = isChecked;
            cbLoginRememberPass.setChecked(isChecked);
        }
    }

    /**
     * 校验登录输入框
     *
     * @return
     */
    private boolean checkLoginInfo() {
        String userName = etLoginUserName.getText().toString();
        String userPass = etLoginUserPass.getText().toString();
        String userVerCode = etLoginUserVer.getText().toString();
        String trueVerCode = vvLoginVerView.getVerificationCode();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "请输入用户名", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(userPass)) {
            ToastUtils.show(this, "请输入密码", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(userVerCode)) {
            if (mLogin.needVerifyCode) {
                ToastUtils.show(this, "请输入验证码", Toast.LENGTH_SHORT);
                return false;
            } else {
                return true;
            }
        } else if (TextUtils.equals(trueVerCode, userVerCode)) {
            if (mLogin.needVerifyCode) {
                ToastUtils.show(this, "验证码错误，请重新输入", Toast.LENGTH_SHORT);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean checkSmsLoginInfo() {
        String loginPhone = etLoginUserPhone.getText().toString();
        String loginPhoneVer = etLoginUserPhoneVer.getText().toString();
        if (TextUtils.isEmpty(loginPhone)) {
            ToastUtils.show(this, "请输入手机号", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(loginPhoneVer)) {
            ToastUtils.show(this, "请输入验证码", Toast.LENGTH_SHORT);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkRegisterInfo() {
        String registerUserName = etRegisterUserName.getText().toString();
        String registerUserPass = etRegisterUserPass.getText().toString();
        String registerUserPhone = etRegisterUserPhone.getText().toString();
        String registerUserPhoneVer = etRegisterUserPhoneVer.getText().toString();
        if (TextUtils.isEmpty(registerUserName)) {
            ToastUtils.show(this, "请输入用户名", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(registerUserPass)) {
            ToastUtils.show(this, "请输入密码", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(registerUserPhone)) {
            ToastUtils.show(this, "请输入手机号", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(registerUserPhoneVer)) {
            ToastUtils.show(this, "请输入短信验证码", Toast.LENGTH_SHORT);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkResetPassInfo() {
        String user = etResetPassUser.getText().toString();
        String resetPassOld = etResetPassOld.getText().toString();
        String resetPassOld2 = etResetPassOldSecond.getText().toString();
        String resetPassNew = etResetPassNew.getText().toString();
        if (TextUtils.isEmpty(user)) {
            ToastUtils.show(this, "请输入用户名/手机号", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(resetPassOld)) {
            ToastUtils.show(this, "请输入旧密码", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(resetPassOld2)) {
            ToastUtils.show(this, "请再次输入旧密码", Toast.LENGTH_SHORT);
            return false;
        } else if (!TextUtils.equals(resetPassOld, resetPassOld2)) {
            ToastUtils.show(this, "两次旧密码输入不一致", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(resetPassNew)) {
            ToastUtils.show(this, "密码不能为空，请输入新密码", Toast.LENGTH_SHORT);
            return false;
        } else {
            return true;
        }
    }

    private void clearSmsLoginPanel() {
        etLoginUserPhone.setText("");
        etLoginUserPhoneVer.setText("");
    }

    private void clearRegisterPanel() {
        etRegisterUserName.setText("");
        etRegisterUserPass.setText("");
        etRegisterUserPhone.setText("");
        etRegisterUserPhoneVer.setText("");
    }

    private void clearResetPassPanel() {
        etResetPassOld.setText("");
        etResetPassOldSecond.setText("");
        etResetPassNew.setText("");
    }

    private void clearChangeServerPanel() {
        String server = (String) mSPUtils.get(Login.SP_KEY_SERVER, "");
        etChangeServerHost.setText(server);
    }

    /**
     * 页面数据填充
     */
    private void setSPInfo() {
        String userPassStr = (String) mSPUtils.get(Login.SP_KEY_USER_PASS, "");
        String userNameStr = (String) mSPUtils.get(Login.SP_KEY_USER_NAME, "");

        mLogin.isRememberUser = !TextUtils.isEmpty(userNameStr);
        mLogin.isRememberPwd = !TextUtils.isEmpty(userPassStr);

        cbLoginRememberName.setChecked(!TextUtils.isEmpty(userNameStr));
        cbLoginRememberPass.setChecked(!TextUtils.isEmpty(userNameStr));

        if (!TextUtils.isEmpty(userPassStr)) {
            etLoginUserPass.setText(userPassStr);
            etLoginUserName.setText(userNameStr);
        } else if (!TextUtils.isEmpty(userNameStr)) {
            etLoginUserPass.setText("");
            etLoginUserName.setText(userNameStr);
        } else {
            etLoginUserPass.setText("");
            etLoginUserName.setText("");
        }
        String server = (String) mSPUtils.get(Login.SP_KEY_SERVER, "");
        etChangeServerHost.setText(server);
    }

    public void resetUserInfo() {
        mSPUtils.put(Login.SP_KEY_IS_LOGIN, false);
        mSPUtils.put(Login.SP_KEY_USER_NAME, "");
        mSPUtils.put(Login.SP_KEY_USER_PASS, "");
        mLogin.isRememberUser = false;
        mLogin.isRememberPwd = false;
        cbLoginRememberName.setChecked(false);
        cbLoginRememberPass.setChecked(false);
        etLoginUserName.setText("");
        etLoginUserPass.setText("");
        etLoginUserVer.setText("");
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{};
    }

    private WaveView wvWaveView1, wvWaveView2;
    private View loginCommon, loginOauth, loginSms, register, resetPass, changeServer;
    // 用户名登录相关
    private TextView tvLoginTitle;
    private EditTextWithLRIcon etLoginUserName, etLoginUserPass, etLoginUserVer;
    private RandomVerificationCodeView vvLoginVerView;
    private CheckBox cbLoginRememberName, cbLoginRememberPass;
    private Button btLoginRegister, btLoginSubmit, btLoginSms, btLoginMissPass;
    private ImageView ivLoginTitleIcon, ivLoginChangeServer;
    // 授权相关
    private ImageView ivOauthWx, ivOauthQQ, ivOauthSina, ivOauthSimulate;
    // 短信登录相关
    private EditTextWithLRIcon etLoginUserPhone, etLoginUserPhoneVer;
    private Button btLoginSmsGetVer, btLoginSmsSubmit, btLoginSmsToLogin;
    // 注册相关
    private EditTextWithLRIcon etRegisterUserName, etRegisterUserPass, etRegisterUserPhone, etRegisterUserPhoneVer;
    private Button btRegisterSubmit, btRegisterToLogin, btRegisterUserGetPhoneVer;
    // 重置密码相关
    private EditTextWithLRIcon etResetPassUser, etResetPassOld, etResetPassOldSecond, etResetPassNew;
    private Button btResetPassSubmit, btResetPassToLogin;
    // 切换服务地址相关
    private EditTextWithLRIcon etChangeServerHost;
    private Button btChangeServerSubmit, btChangeServerToLogin;

    private void findView() {
        wvWaveView1 = findViewById(R.id.login_wave1);
        wvWaveView2 = findViewById(R.id.login_wave2);
        loginCommon = findViewById(R.id.login_common_layout);
        loginOauth = findViewById(R.id.login_oauth_layout);
        loginSms = findViewById(R.id.login_sms_layout);
        register = findViewById(R.id.register_layout);
        resetPass = findViewById(R.id.reset_pass_layout);
        changeServer = findViewById(R.id.change_server_layout);
        // 用户名登录
        ivLoginTitleIcon = loginCommon.findViewById(R.id.login_title_icon);
        tvLoginTitle = loginCommon.findViewById(R.id.login_title);

        etLoginUserName = loginCommon.findViewById(R.id.login_user_name);
        etLoginUserPass = loginCommon.findViewById(R.id.login_user_pass);
        etLoginUserVer = loginCommon.findViewById(R.id.login_user_ver);

        vvLoginVerView = loginCommon.findViewById(R.id.login_ver_view);
        cbLoginRememberName = loginCommon.findViewById(R.id.login_remember_name);
        cbLoginRememberPass = loginCommon.findViewById(R.id.login_remember_pass);
        ivLoginChangeServer = loginCommon.findViewById(R.id.login_change_server);
        btLoginRegister = loginCommon.findViewById(R.id.login_register);
        btLoginSubmit = loginCommon.findViewById(R.id.login_submit);
        btLoginSms = loginCommon.findViewById(R.id.login_sms);
        btLoginMissPass = loginCommon.findViewById(R.id.login_miss_pass);
        // 授权
        ivOauthWx = loginOauth.findViewById(R.id.oauth_login_wx);
        ivOauthQQ = loginOauth.findViewById(R.id.oauth_login_qq);
        ivOauthSina = loginOauth.findViewById(R.id.oauth_login_sina);
        ivOauthSimulate = loginOauth.findViewById(R.id.oauth_login_simulate);
        // 短信登录
        etLoginUserPhone = loginSms.findViewById(R.id.login_sms_phone);
        etLoginUserPhoneVer = loginSms.findViewById(R.id.login_sms_ver);
        btLoginSmsGetVer = loginSms.findViewById(R.id.login_sms_get_ver);
        btLoginSmsSubmit = loginSms.findViewById(R.id.login_sms_submit);
        btLoginSmsToLogin = loginSms.findViewById(R.id.login_sms_to_login);
        // 注册
        etRegisterUserName = register.findViewById(R.id.register_user_name);
        etRegisterUserPass = register.findViewById(R.id.register_user_pass);
        etRegisterUserPhone = register.findViewById(R.id.register_user_phone);
        etRegisterUserPhoneVer = register.findViewById(R.id.register_user_phone_ver);
        btRegisterSubmit = register.findViewById(R.id.register_submit);
        btRegisterToLogin = register.findViewById(R.id.register_to_login);
        btRegisterUserGetPhoneVer = register.findViewById(R.id.register_user_get_phone_ver);
        // 重置密码
        etResetPassUser = resetPass.findViewById(R.id.reset_pass_user);
        etResetPassOld = resetPass.findViewById(R.id.reset_pass_old);
        etResetPassOldSecond = resetPass.findViewById(R.id.reset_pass_old_second);
        etResetPassNew = resetPass.findViewById(R.id.reset_pass_new);
        btResetPassSubmit = resetPass.findViewById(R.id.reset_pass_submit);
        btResetPassToLogin = resetPass.findViewById(R.id.reset_pass_to_login);
        // 切换服务地址
        etChangeServerHost = changeServer.findViewById(R.id.change_server_host);
        btChangeServerSubmit = changeServer.findViewById(R.id.change_server_submit);
        btChangeServerToLogin = changeServer.findViewById(R.id.change_server_to_login);

        tvLoginTitle.setText(mLogin.loginTitle);

    }

    private void setListener() {
        cbLoginRememberName.setOnCheckedChangeListener(this);
        cbLoginRememberPass.setOnCheckedChangeListener(this);
        btLoginRegister.setOnClickListener(this);
        btLoginSubmit.setOnClickListener(this);
        btLoginSms.setOnClickListener(this);
        btLoginMissPass.setOnClickListener(this);
        ivOauthWx.setOnClickListener(this);
        ivOauthQQ.setOnClickListener(this);
        ivOauthSina.setOnClickListener(this);
        ivOauthSimulate.setOnClickListener(this);

        btLoginSmsGetVer.setOnClickListener(this);
        btLoginSmsSubmit.setOnClickListener(this);
        btLoginSmsToLogin.setOnClickListener(this);

        btRegisterUserGetPhoneVer.setOnClickListener(this);
        btRegisterSubmit.setOnClickListener(this);
        btRegisterToLogin.setOnClickListener(this);

        btResetPassSubmit.setOnClickListener(this);
        btResetPassToLogin.setOnClickListener(this);

        btChangeServerSubmit.setOnClickListener(this);
        btChangeServerToLogin.setOnClickListener(this);

        if (mLogin.needChangeServer) {
            ivLoginChangeServer.setOnClickListener(this);
        }
    }

    private LoginFunction mLoginFunction;
    private SmsLoginFunction mSmsLoginFunction;
    private RegisterFunction mRegisterFunction;
    private ResetPassFunction mResetPassFunction;

    private void createFunctions() {
        mLoginFunction = LoginFunction.instance();
        mSmsLoginFunction = SmsLoginFunction.instance();
        mRegisterFunction = RegisterFunction.instance();
        mResetPassFunction = ResetPassFunction.instance();

        mLoginFunction.setActivity(this);
        mSmsLoginFunction.setActivity(this);
        mRegisterFunction.setActivity(this);
        mResetPassFunction.setActivity(this);
    }

    public void toHome() {
        mLoginListener.toHome(this);
        this.finish();
    }

    // 登录页面板切换动画
    private void startAnimation(View foldView, View UnfoldView) {
        Animation animationFold = AnimationUtils.loadAnimation(this, R.anim.scale_x_fold);
        Animation animationUnfold = AnimationUtils.loadAnimation(this, R.anim.scale_x_unfold);
        animationFold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                foldView.setVisibility(View.GONE);
                foldView.clearAnimation();
                UnfoldView.setVisibility(View.VISIBLE);
                UnfoldView.startAnimation(animationUnfold);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        foldView.startAnimation(animationFold);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 可在该方法中增加水印等
        ViewGroup parent = (ViewGroup) getWindow().getDecorView();
        WaterMarkView wmv = new WaterMarkView(this, null);
        wmv.setTouchPoint(new PointF(event.getX(), event.getY()));
        parent.addView(wmv);
        return super.dispatchTouchEvent(event);
    }
}
