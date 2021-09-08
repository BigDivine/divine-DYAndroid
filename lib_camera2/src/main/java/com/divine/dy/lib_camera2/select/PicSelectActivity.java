package com.divine.dy.lib_camera2.select;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.base.BaseToolbar;
import com.divine.dy.lib_base.base.ToolbarClickListener;
import com.divine.dy.lib_base.getpermission.PermissionList;
import com.divine.dy.lib_camera2.R;
import com.divine.dy.lib_camera2.interfaces.PicSelectListener;
import com.divine.dy.lib_utils.sys.FileUtils;

import java.io.File;
import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public class PicSelectActivity extends BaseActivity implements ToolbarClickListener {

    private ConstraintLayout mPicSelectHeaderLayout;
    private PicSelectConfig mPicSelectConfig;
    private PicSelectFragment mPicSelectFragment;
    private ArrayList<String> mPicSelectImagesResult = new ArrayList<>();

    private BaseToolbar mBaseToolbar;
    PicSelectListener mPicSelectListener = new PicSelectListener() {
        @Override
        public void onSingleImageSelected(String path) {
            PicSelectStaticVariable.mPicSelectImageList.add(path);
            exitPicSelectActivity();
        }

        @Override
        public void onMultiImageSelected(String path) {
            mBaseToolbar.setRightText(String.format("%1$s(%2$d/%3$d)", mPicSelectConfig.btnText, PicSelectStaticVariable.mPicSelectImageList.size(), mPicSelectConfig.maxNum));
        }

        @Override
        public void onMultiImageUnselected(String path) {
            mBaseToolbar.setRightText(String.format("%1$s(%2$d/%3$d)", mPicSelectConfig.btnText, PicSelectStaticVariable.mPicSelectImageList.size(), mPicSelectConfig.maxNum));
        }

        @Override
        public void onCameraShot(File imageFile) {
            if (imageFile != null) {
                PicSelectStaticVariable.mPicSelectImageList.add(imageFile.getAbsolutePath());
                mPicSelectConfig.multiSelect = false; // 多选点击拍照，强制更改为单选
                exitPicSelectActivity();
            }
        }

        @Override
        public void onPreviewChanged(int select, int sum, boolean visible) {
            if (visible) {
                mBaseToolbar.setTitle(select + "/" + sum);
            } else {
                mBaseToolbar.setTitle(mPicSelectConfig.title);
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_pic_select_layout;
    }

    @Override
    public boolean showToolbar() {
        return true;
    }

    @Override
    public void initView() {
        mPicSelectConfig = (PicSelectConfig) getIntent().getSerializableExtra("config");
        mPicSelectFragment = PicSelectFragment.instance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", mPicSelectConfig);
        mPicSelectFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.pic_select_images_frame, mPicSelectFragment, null)
                .commit();
        mPicSelectFragment.setPicSelectListener(mPicSelectListener);
        mBaseToolbar = getBaseToolbar();
        mBaseToolbar.setToolbarClickListener(this);
        mPicSelectHeaderLayout = mBaseToolbar.getHeaderContainLayout();

        if (mPicSelectConfig != null) {
            initConfig();
        }
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据config配置页面
     */
    private void initConfig() {
        if (mPicSelectConfig.backResId != -1) {
            mBaseToolbar.setLeftDrawable(mPicSelectConfig.backResId);
        }
        mPicSelectHeaderLayout.setBackgroundColor(mPicSelectConfig.titleBgColor);
        mBaseToolbar.setTitle(mPicSelectConfig.title);
        mBaseToolbar.setTitleColor(mPicSelectConfig.titleColor);
        mBaseToolbar.setRightBgColor(mPicSelectConfig.btnBgColor);
        mBaseToolbar.setRightTextColor(mPicSelectConfig.btnTextColor);
        if (mPicSelectConfig.multiSelect) {
            if (!mPicSelectConfig.rememberSelected) {
                PicSelectStaticVariable.mPicSelectImageList.clear();
            }
            mBaseToolbar.setRightText(String.format("%1$s(%2$d/%3$d)", mPicSelectConfig.btnText, PicSelectStaticVariable.mPicSelectImageList.size(), mPicSelectConfig.maxNum));
        } else {
            PicSelectStaticVariable.mPicSelectImageList.clear();
            mBaseToolbar.setRightVisible(false);
        }
    }

    @Override
    public void getData() {
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{PermissionList.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("config", mPicSelectConfig);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPicSelectConfig = (PicSelectConfig) savedInstanceState.getSerializable("config");
    }

    @Override
    public void leftClick() {
        onBackPressed();
    }

    @Override
    public void centerClick() {

    }

    @Override
    public void rightClick() {
        if (PicSelectStaticVariable.mPicSelectImageList != null && !PicSelectStaticVariable.mPicSelectImageList.isEmpty()) {
            exitPicSelectActivity();
        } else {
            Toast.makeText(PicSelectActivity.this, "最少选择一张图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPicSelectFragment == null || !mPicSelectFragment.hidePreview()) {
            PicSelectStaticVariable.mPicSelectImageList.clear();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void exitPicSelectActivity() {
        mPicSelectImagesResult.clear();
        mPicSelectImagesResult.addAll(PicSelectStaticVariable.mPicSelectImageList);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("select_pics", mPicSelectImagesResult);
        intent.putExtra("select_pics_bundle", bundle);
        setResult(RESULT_OK, intent);
        if (!mPicSelectConfig.multiSelect) {
            PicSelectStaticVariable.mPicSelectImageList.clear();
        }
        finish();
    }
}
