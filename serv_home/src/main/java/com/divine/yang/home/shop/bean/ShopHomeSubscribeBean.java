package com.divine.yang.home.shop.bean;

import java.util.ArrayList;

/**
 * Author: Divine
 * CreateDate: 2021/10/22
 * Describe:
 */
public class ShopHomeSubscribeBean {
    String img,title,subTitle1,subTitle2,content;
    ArrayList<String> contentImg;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle1() {
        return subTitle1;
    }

    public void setSubTitle1(String subTitle1) {
        this.subTitle1 = subTitle1;
    }

    public String getSubTitle2() {
        return subTitle2;
    }

    public void setSubTitle2(String subTitle2) {
        this.subTitle2 = subTitle2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getContentImg() {
        return contentImg;
    }

    public void setContentImg(ArrayList<String> contentImg) {
        this.contentImg = contentImg;
    }
}
