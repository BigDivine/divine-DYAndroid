package com.divine.dy.lib_camera2.select;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Divine
 * CreateDate: 2021/9/8
 * Describe:
 */
public class PicFolderBean implements Serializable {
    public String name;
    public String path;
    public PicBean cover;
    public List<PicBean> images;

    public PicFolderBean() {

    }

    @Override
    public boolean equals(Object o) {
        try {
            PicFolderBean other = (PicFolderBean) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
