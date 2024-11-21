package com.divine.yang.camera2.select;

import java.io.Serializable;

/**
 * Author: Divine
 * CreateDate: 2021/9/8
 * Describe:
 */
public class PicBean implements Serializable {
    public String path;
    public String name;

    public PicBean() {

    }

    public PicBean(String path, String name) {
        this.path = path;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            PicBean other = (PicBean) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
