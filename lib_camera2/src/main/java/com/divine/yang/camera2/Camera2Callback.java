package com.divine.yang.camera2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Divine
 * CreateDate: 2021/2/3
 * Describe:
 */
public interface Camera2Callback {

    void singleCallback(String imagePath);

    void multiCallback(ArrayList<String> imagePaths);
}
