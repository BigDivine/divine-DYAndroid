package com.divine.yang.main.enums;


import androidx.annotation.NonNull;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main.enum
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/12/4
 * Description   :
 */
public enum LISTTYPE {
    //    列表流、瀑布流、卡片流
    LIST("列表流"), FALL("瀑布流"), CARD("卡片流");
    private String listType;

    LISTTYPE(String listType) {
        this.listType = listType;
    }

    @NonNull
    @Override
    public String toString() {
        return this.listType;
    }
}