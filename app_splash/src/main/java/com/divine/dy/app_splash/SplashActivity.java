package com.divine.dy.app_splash;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager2.widget.ViewPager2;

public abstract class SplashActivity extends BaseActivity implements OnSplashItemClickListener, View.OnClickListener {
    //wait time
    private Handler mHandler = new Handler();
    private int count = 3;
    private boolean isJumpToNext;
    protected SPUtils mSPUtils;

    private Runnable mTimer = new Runnable() {
        @Override
        public void run() {
            if (count == 0) {
                btSplashTimer.setText("跳过");
            } else {
                btSplashTimer.setText("跳过(" + count + ")");
            }
            if (count > 0) {
                mHandler.postDelayed(this, 1000);
                count--;
                btSplashTimer.setVisibility(View.VISIBLE);
            } else {
                if (!isJumpToNext) {
                    toNextPage();
                }
            }

        }
    };

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
        boolean fistStart = (boolean) SPUtils.getInstance(this).get(SPKeys.SP_KEY_IS_FIRST_START, true);
        if (!fistStart) {
            ivSplashImage.setVisibility(View.VISIBLE);
            vpSplashGuideImages.setVisibility(View.GONE);
            startTimer();
        } else {
            vpSplashGuideImages.setVisibility(View.VISIBLE);
            ivSplashImage.setVisibility(View.GONE);
            List<Integer> data = new ArrayList<>();
            data.add(R.mipmap.splash_one);
            SplashAdapter adapter = new SplashAdapter(this, data);
            adapter.setOnSplashItemClickListener(this);
            vpSplashGuideImages.setAdapter(adapter);
        }
    }

    private void startTimer() {
        mHandler.post(mTimer);
    }

    @Override
    public void onLastItemClick(View v) {
        SPUtils.getInstance(this).put(SPKeys.SP_KEY_IS_FIRST_START, false);
        toLogin();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.splash_timer) {
            toNextPage();
        }
    }

    public void toNextPage() {
        if (!isJumpToNext) {
            isJumpToNext = true;
        }
        mSPUtils = SPUtils.getInstance(this);
        boolean isLogin = (boolean) mSPUtils.get(SPKeys.SP_KEY_IS_LOGIN, false);
        /**
         * 两种方案：
         * 1、计算上次登录时间与本次登录时间的间隔大于某个值，则需要重新登录
         * 2、记住登录的情况，记录用户名和密码，在首页进行登录接口请求，如果登录失败提示并跳转到登录页
         */
        if (isLogin) {
            toMain();
        } else {
            toLogin();
        }
    }

    public abstract void toLogin();

    public abstract void toMain();

    private ViewPager2 vpSplashGuideImages;
    private ImageView ivSplashImage;
    private Button btSplashTimer;

    public void findView() {
        vpSplashGuideImages = findViewById(R.id.splash_guide_images);
        ivSplashImage = findViewById(R.id.splash_image);
        btSplashTimer = findViewById(R.id.splash_timer);
        btSplashTimer.setOnClickListener(this);
    }

}