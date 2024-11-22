package com.divine.yang.base.base;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.divine.yang.base.AppConstants;
import com.divine.yang.base.LocalLogcat;
import com.divine.yang.base.SecurityCheck;
import com.divine.yang.base.getpermission.PermissionPageUtils;
import com.divine.yang.base.utils.ActivitiesManager;
import com.divine.yang.base.utils.DialogUtils;
import com.divine.yang.base.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by BigDivine on 2020/10/10
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final String TAG = "DY-BaseActivity";
    private final boolean isDebug = true;
    private final String logDirPath = "/logs";
    // activity manager class:activity管理类
    private ActivitiesManager activitiesManager;
    // current time by millisecond:当前时间毫秒数
    private long currentTimeMillis = 0;
    private LocalLogcat mLocalLogcat;
    // need to request permissions dynamically:需要动态申请的权限
    private String[] requestPermissions;

    /**
     * 获取布局的id
     */
    public abstract int getContentViewId();

    /**
     * 页面发出请求，获取页面的数据
     */
    public abstract void getData();

    /**
     * 需要动态获取的权限
     */
    public abstract String[] requestPermissions();

    /**
     * 初始化控件
     */
    public abstract void initView();

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        Log.e(TAG, "onCreateView");
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        activitiesManager = ActivitiesManager.getInstance();
        activitiesManager.addActivity(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // 手机root权限检查
        if (SecurityCheck.isRoot()) {
            DialogUtils.showConfirmDialog(this, "提示", "您的手机处于Root状态，不允许应用APP，请解除Root状态后应用", (dialog, which) -> {
                dialog.dismiss();
                this.finish();
            });
            return;
        }
        // app应用签名校验,通过SHA1来验证
        //        if (!SecurityCheck.signCheck(this)) {
        //            DialogUtils.showConfirmDialog(this
        //                    , "提示"
        //                    , "您的应用签名信息验证失败，不允许使用，请下载官方版本使用"
        //                    , (dialog, which) -> {
        //                        dialog.dismiss();
        //                        this.finish();
        //                    }
        //            );
        //            return;
        //        }
        requestPermissions = requestPermissions();
        if (isDebug) {
            mLocalLogcat = LocalLogcat.getInstance(this, logDirPath);
            // 检查文件权限，用于保存日志文件
            getFilePermission();
        } else {
            requestCustomPermissions();
        }

    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        getData();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onRestart");
        super.onRestart();

    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
        if (isDebug && null != mLocalLogcat && !mLocalLogcat.isRunning()) {
            mLocalLogcat.start();
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        boolean background = !isForeground(this);
        Log.e(TAG, "isBackground:" + background);
        if (background) ToastUtils.showShort(this, "当前应用已转到后台运行");
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (isDebug && null != mLocalLogcat && mLocalLogcat.isRunning()) {
            mLocalLogcat.stop();
        }
    }


    private void requestCustomPermissions() {
        // 获取未授权的权限
        ArrayList<String> deniedPermissionsList = new ArrayList<>();
        for (String permission : requestPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionsList.add(permission);
            }
        }
        int size = deniedPermissionsList.size();
        if (size > 0) {
            String[] deniedPermissions = deniedPermissionsList.toArray(new String[size]);
            // 弹框请求权限
            requestPermissions(deniedPermissions, AppConstants.REQUEST_CODE_PERMISSION);
        } else {
            initView();
        }
    }

    private void getFilePermission() {
        // File filesDir = getFilesDir();
        // File cacheDir = getCacheDir();
        // File externalFilesDir = getExternalFilesDir(null);
        // File externalCacheDir = getExternalCacheDir();
        // String ExternalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Log.i(TAG, "filesDir:" + filesDir + ";cacheDir:" + cacheDir);
        // Log.i(TAG, "externalFilesDir:" + externalFilesDir + ";externalCacheDir:" + externalCacheDir);
        // Log.i(TAG, "ExternalStorageDir:" + ExternalStorageDir);
        // // 您可以通过调用 Environment.getExternalStorageState() 查询该卷的状态。
        // // 如果返回的状态为 MEDIA_MOUNTED，那么您就可以在外部存储空间中读取和写入应用专属文件。
        // // 如果返回的状态为 MEDIA_MOUNTED_READ_ONLY，您只能读取这些文件。
        // boolean isExternalStorageWritable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        // boolean isExternalStorageReadable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
        //         Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        // Log.i(TAG, "isExternalStorageWritable:" + isExternalStorageWritable);
        // Log.i(TAG, "isExternalStorageReadable:" + isExternalStorageReadable);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // Android 10(sdk-29)及以下 可以访问所有文件，并可以在任何目录创建
            // Android 11(sdk-30)~12(sdk-31、32) 可以访问应用专属目录，并可以在专属目录创建
            // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
            // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, AppConstants.REQUEST_CODE_PERMISSION_READ_EXTERNAL);
            } else {
                initView();
            }
        } else {
            // Android 13(sdk-33)~15(sdk-35)READ_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限失效
            // 新增MANAGE_EXTERNAL_STORAGE 可以访问所有文件，可以在任何目录创建
            // <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
            boolean isExternalStorageManager = Environment.isExternalStorageManager();
            if (!isExternalStorageManager) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                initView();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult");
        if (requestCode == AppConstants.REQUEST_CODE_PERMISSION_READ_EXTERNAL) {
            boolean isReadExternalGranted = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isReadExternalGranted = false;
                    break;
                }
            }
            if (isReadExternalGranted) {
                // 已全部授权
                if (isDebug && null != mLocalLogcat && !mLocalLogcat.isRunning()) {
                    mLocalLogcat.start();
                }
                requestCustomPermissions();
            } else {
                Log.e(TAG, "未同意文件权限，不会保存日志文件");
            }
        }
        if (requestCode == AppConstants.REQUEST_CODE_PERMISSION) {
            boolean isAllGranted = true;// 是否全部权限已授权
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                // 已全部授权
                initView();
            } else {
                // 权限有缺失
                new AlertDialog.Builder(this).setMessage("跳转到设置页面允许权限，否则无法正常使用。").setTitle("授权提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionPageUtils.jumpPermissionPage(activitiesManager.currentActivity());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed");
        if (activitiesManager.getActivityStack().size() == 1) {
            if (System.currentTimeMillis() - currentTimeMillis <= 2000) {
                activitiesManager.AppExit(this);
            } else {
                ToastUtils.show(this, "再按一次返回键，退出应用", Toast.LENGTH_SHORT);
                currentTimeMillis = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 判断当前应用在前台
     */
    protected boolean isForeground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if (processes == null) {
            Log.e(TAG, "running app processes is null!");
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo app : processes) {
            if (app.processName.equals(mContext.getPackageName()) && app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    // public void startActivity(Class targetActivity, Bundle bundle) {
    //     Intent intent = new Intent(this, targetActivity);
    //     if (bundle != null) {
    //         intent.putExtra("bundle", bundle);
    //     }
    //     startActivity(intent);
    // }
    //    public void navigationTo(String path) {
    //        ARouter.getInstance()
    //                .build(path)
    //                .navigation(this);
    //    }
    //
    //    public void navigationTo(String path, String params) {
    //        ARouter.getInstance()
    //                .build(path)
    //                .withString("params", params)
    //                .navigation(this);
    //    }
    //
    //    public void navigationTo(String path, int requestCode) {
    //        ARouter.getInstance()
    //                .build(path)
    //                .navigation(this, requestCode);
    //    }
    //
    //    public void navigationTo(String path, String params, int requestCode) {
    //        ARouter.getInstance()
    //                .build(path)
    //                .withString("params", params)
    //                .navigation(this, requestCode);
    //    }
    //
    //    public void navigationTo(String path, NavigationCallback callback) {
    //        ARouter.getInstance()
    //                .build(path)
    //                .navigation(this, callback);
    //    }
    //
    //    public void navigationTo(String path, String params, int requestCode, InterceptorCallback callback) {
    //        ARouter.getInstance()
    //                .build(path)
    //                .withString("params", params)
    //                .navigation(this, requestCode);
    //    }
}
