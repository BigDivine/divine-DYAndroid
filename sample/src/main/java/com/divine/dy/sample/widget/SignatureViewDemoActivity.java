package com.divine.dy.sample.widget;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.divine.dy.lib_base.AppConstants;
import com.divine.dy.lib_base.getpermission.PermissionUtil;
import com.divine.dy.lib_widget.widget.SignatureView;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignatureViewDemoActivity extends AppCompatActivity {

    private SignatureView mPathView;
    private Button mBtnClear, mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.divine.dy.lib_widget.R.layout.activity_signature_view_demo_activity);
        mPathView = findViewById(com.divine.dy.lib_widget.R.id.signature_view);
        mBtnClear = findViewById(com.divine.dy.lib_widget.R.id.clear);
        mBtnSave = findViewById(com.divine.dy.lib_widget.R.id.save);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtil.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                       , Manifest.permission.READ_EXTERNAL_STORAGE
                                       , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                                              AppConstants.REQUEST_CODE_PERMISSION);
        }

        //修改背景、笔宽、颜色
        mPathView.setBackColor(Color.WHITE);
        mPathView.setPaintWidth(20);
        mPathView.setPenColor(Color.BLACK);
        //清除
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathView.clear();
                mPathView.setBackColor(Color.WHITE);
                mPathView.setPaintWidth(20);
                mPathView.setPenColor(Color.BLACK);
            }
        });
        //保存
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPathView.getTouched()) {
                    try {
                        mPathView.save(Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc.png", true, 10);
                        setResult(100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignatureViewDemoActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意申请权限
            } else {
                // 用户拒绝申请权限
                Toast.makeText(this, "请同意调起权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}