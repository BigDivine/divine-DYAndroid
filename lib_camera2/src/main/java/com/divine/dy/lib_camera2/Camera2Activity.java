package com.divine.dy.lib_camera2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.getpermission.PermissionList;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Camera2Activity extends BaseActivity {
    private String TAG = "D-Camera2Activity";
    private Camera2Fragment mCamera2Fragment;
    private FragmentManager mFm;

    String params = "";

    @Override
    public int getContentViewId() {
        return R.layout.activity_camera2_demo;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        Log.e(TAG, params);
        mCamera2Fragment = new Camera2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("params", params);
        mCamera2Fragment.setArguments(bundle);
        mFm = getSupportFragmentManager();
        FragmentTransaction mFragmentTrans = mFm.beginTransaction();
        mFragmentTrans.add(R.id.camera2_frame_layout, mCamera2Fragment);
        mFragmentTrans.commit();
    }

    @Override
    public void getData() {
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{
                PermissionList.WRITE_EXTERNAL_STORAGE,
                PermissionList.READ_EXTERNAL_STORAGE,
                PermissionList.CAMERA
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCamera2Fragment.result(requestCode, resultCode, data);
    }

    boolean isRotate = false;

    public boolean getIsRotate() {
        return isRotate;
    }

    public boolean setIsRotate(boolean isRotate) {
        return isRotate;
    }

}