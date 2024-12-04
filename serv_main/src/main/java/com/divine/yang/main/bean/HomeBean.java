package com.divine.yang.main.bean;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main.bean
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/12/4
 * Description   :
 */
public class HomeBean {
    private String image, title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "HomeBean{" + "image='" + image + '\'' + ", title='" + title + '\'' + '}';
    }
}
