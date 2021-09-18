package com.divine.dy.sample;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.divine.dy.app_login.LoginBase;
import com.divine.dy.lib_base.AppBase;
import com.divine.dy.lib_camera2.Camera2Base;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.SPUtils;

import java.io.File;

/**
 * Author: Divine
 * CreateDate: 2021/8/2
 * Describe:
 */
public class MyApplication extends Application {
    private final String TAG = "DY-application";
    private boolean needChangeServer;
    private boolean needVerifyCode;
    private boolean isDebug;
    private boolean showHint;
    private String serverUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        getMetaData();

        setAppInfo();
        setLoginApp();
        setDebugConfig();
        setCameraConfig();
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void setAppInfo() {
        SPUtils mSPUtils = SPUtils.getInstance(this);
        String server = (String) mSPUtils.get(SPKeys.SP_KEY_SERVER, serverUrl);
        AppBase.serverUrl = server;
        mSPUtils.put(SPKeys.SP_KEY_SERVER, AppBase.serverUrl);
        AppBase.isDebug = isDebug;
        //指定app本地存储目录
        AppBase.appDir = Environment.getExternalStorageDirectory() + File.separator + getPackageName() + File.separator;
        Log.e("dy-application", AppBase.appDir);
    }

    private void setLoginApp() {
        LoginBase.loginTitle = getResources().getString(R.string.app_name);
        LoginBase.needChangeServer = needChangeServer;
        LoginBase.needVerifyCode = needVerifyCode;
    }

    private void setDebugConfig() {
        if (isDebug) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        } else {

        }
    }

    private void setCameraConfig() {
        Camera2Base.showHint = showHint;
        //拍照图片保存路径
        Camera2Base.camera2PicSourceDir = AppBase.appDir + "/camera2/source/";
        //裁剪后图片保存路径
        Camera2Base.camera2PicCropDir = AppBase.appDir + "/camera2/crop/";

    }

    private void getMetaData() {
        needChangeServer = (boolean) getMetaDataByKey("needChangeServer");
        needVerifyCode = (boolean) getMetaDataByKey("needVerifyCode");
        isDebug = (boolean) getMetaDataByKey("isDebug");
        showHint = (boolean) getMetaDataByKey("showHint");
        serverUrl = (String) getMetaDataByKey("serverUrl");
    }

    private Object getMetaDataByKey(String key) {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Object metaData = applicationInfo.metaData.get(key);
            return metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "get metaData failed ;key:" + key);
            return null;
        }
    }
}
