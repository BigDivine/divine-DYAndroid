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
    private String code;
    private String image, title;
    private int resImage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public int getResImage() {
        return resImage;
    }

    public void setResImage(int resImage) {
        this.resImage = resImage;
    }

    @Override
    public String toString() {
        return "HomeBean{" +
                "code='" + code + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", resImage=" + resImage +
                '}';
    }
}
