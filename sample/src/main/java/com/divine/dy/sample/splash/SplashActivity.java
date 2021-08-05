package com.divine.dy.sample.splash;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.divine.dy.app_login.LoginPresenter;
import com.divine.dy.app_login.LoginView;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.Base64Utils;
import com.divine.dy.lib_utils.sys.SPUtils;
import com.divine.dy.lib_utils.ui.ToastUtils;
import com.divine.dy.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager2.widget.ViewPager2;

public class SplashActivity extends BaseActivity implements OnSplashItemClickListener, LoginView {
    private String params = "";
    private String routerParams;


    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }


    @Override
    public void initView() {
        findView();
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject obj = new JSONObject(params);
                if (obj.has("type")) {
                    String paramsType = obj.optString("type");
                    if (TextUtils.equals(paramsType, "JPush")) {
                        //推送跳转到登录页
                        routerParams = obj.optString("data");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SplashViewPager.setVisibility(View.GONE);
        boolean fistStart =
                (boolean) SPUtils.getInstance(this).get(SPKeys.SP_KEY_IS_FIRST_START, true);
        if (!fistStart) {
            autoLogin();
        } else {
            SplashViewPager.setVisibility(View.VISIBLE);
            List<Integer> data = new ArrayList<>();
            //            data.add(R.mipmap.splash_first);
            //            data.add(R.mipmap.splash_second);
            //            data.add(R.mipmap.splash_third);
            SplashAdapter adapter = new SplashAdapter(this, data);
            adapter.setOnSplashItemClickListener(this);
            SplashViewPager.setAdapter(adapter);
        }

    }

    @Override
    public void onLastItemClick(View v) {
        SPUtils.getInstance(this).put(SPKeys.SP_KEY_IS_FIRST_START, false);
        toLogin();
    }

    protected SPUtils mSPUtils;
    private LoginPresenter loginPresenter;
    private String tenant = "__default_tenant__";
    private String userNameStr, userPassStr;

    public void autoLogin() {
        loginPresenter = new LoginPresenter(this);
        mSPUtils = SPUtils.getInstance(this);
        boolean isLogin = (boolean) mSPUtils.get(SPKeys.SP_KEY_IS_LOGIN, false);
        if (isLogin) {
            userNameStr = (String) mSPUtils.get(SPKeys.SP_KEY_USER_NAME, "");
            userPassStr = (String) mSPUtils.get(SPKeys.SP_KEY_USER_PASS, "");
            //            userPassStr = "";
            if (userNameStr.isEmpty()) {
            }
            if (userPassStr.isEmpty()) {

            } else {

                String params = "{\"username\":\"" + userNameStr + "\"," +
                        "\"pwd\":\"" + userPassStr + "\"," +
                        "\"tenant\":\"" + tenant + "\"," +
                        "\"encrypted\": true" +
                        "}";
                loginPresenter.login(params);
            }
        } else {
            toLogin();
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
                mSPUtils.put(SPKeys.SP_KEY_TOKEN_WEB, token);
                String userNameWeb = Base64Utils.decode(Base64Utils.decode(userNameStr));
                mSPUtils.put(SPKeys.SP_KEY_USER_NAME_WEB, userNameWeb);
                //                if (TextUtils.isEmpty(routerParams)) {
                //                    navigationTo(RouterManager.router_web);
                //                } else {
                //                    navigationTo(RouterManager.router_web, routerParams);
                //                }
                this.finish();
            } else {
                ToastUtils.show(this, "登录失败:" + msg + ",请重新登录", Toast.LENGTH_SHORT);
                mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
                toLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(this, "登录失败,请联系管理员。", Toast.LENGTH_SHORT);
            mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
            toLogin();
        }

    }

    @Override
    public void fail(String msg) {
        ToastUtils.show(this, "登录失败," + msg, Toast.LENGTH_SHORT);
        mSPUtils.put(SPKeys.SP_KEY_IS_LOGIN, false);
        toLogin();
    }

    private void toLogin() {
        //        navigationTo(RouterManager.router_login);
        this.finish();
    }

    private ViewPager2 SplashViewPager;

    public void findView() {
        SplashViewPager = findViewById(R.id.gmt_splash);
    }
}