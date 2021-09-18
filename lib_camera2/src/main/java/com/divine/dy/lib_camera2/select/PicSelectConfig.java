package com.divine.dy.lib_camera2.select;

import java.io.Serializable;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public class PicSelectConfig implements Serializable {
    /**
     * 包含所有图片的文件夹名称
     */
    public String rootFolderName;
    /**
     * 提交按钮文字
     */
    public String submitText;
    /**
     * 是否多选
     */
    public boolean multiSelect;
    /**
     * 是否记住上次的选中记录(只对多选有效)
     */
    public boolean rememberSelected = true;
    /**
     * 最多选择图片数
     */
    public int maxNum = 9;
    /**
     * 第一个item是否显示相机
     */
    public boolean needCamera;
    /**
     * 返回图标资源
     */
    public int backResId = -1;

    /**
     * 拍照存储路径
     */
    public String filePath;

    /**
     * 裁剪输出大小
     */
    public int aspectX = 1;
    public int aspectY = 1;
    public int outputX = 500;
    public int outputY = 500;

    public PicSelectConfig(Builder builder) {
        this.rootFolderName = builder.rootFolderName;
        this.submitText = builder.submitText;
        this.multiSelect = builder.multiSelect;
        this.rememberSelected = builder.rememberSelected;
        this.maxNum = builder.maxNum;
        this.needCamera = builder.needCamera;
        this.backResId = builder.backResId;
        this.filePath = builder.filePath;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
    }

    public static class Builder implements Serializable {
        private String rootFolderName;
        public String submitText;
        private boolean multiSelect = true;
        private boolean rememberSelected = true;
        private int maxNum = 9;
        private boolean needCamera = true;
        private int backResId = -1;
        private String filePath;
        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        public Builder() {
            rootFolderName = "所有图片";
            submitText = "确定";
        }

        public Builder rootFolderName(String rootFolderName) {
            this.rootFolderName = rootFolderName;
            return this;
        }

        public Builder submitText(String submitText) {
            this.submitText = submitText;
            return this;
        }

        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder rememberSelected(boolean rememberSelected) {
            this.rememberSelected = rememberSelected;
            return this;
        }

        public Builder maxNum(int maxNum) {
            this.maxNum = maxNum;
            return this;
        }

        public Builder needCamera(boolean needCamera) {
            this.needCamera = needCamera;
            return this;
        }

        public Builder backResId(int backResId) {
            this.backResId = backResId;
            return this;
        }

        private Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public PicSelectConfig build() {
            return new PicSelectConfig(this);
        }
    }
}