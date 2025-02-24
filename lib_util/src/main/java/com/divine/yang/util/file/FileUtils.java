package com.divine.yang.util.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                boolean mkdir = file.mkdirs();
                Log.i(TAG, "----- 创建文件夹:" + mkdir + "-" + file.getAbsolutePath());
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                boolean mkdir = file.mkdirs();
                Log.i(TAG, "----- 创建文件夹:" + mkdir + "-" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 递归创建文件
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                boolean createNewFile = file.createNewFile();
                Log.i(TAG, "----- 创建文件:" + createNewFile + "-" + file.getAbsolutePath());
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                boolean createNewFile = file.createNewFile();
                Log.i(TAG, "----- 创建文件:" + createNewFile + "-" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 删除文件，文件夹
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }
    /**
     * 获取该应用的根目录
     */
    public static String getAppPath() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return "";
        }
        File sdRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdRoot.getAbsolutePath());
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    /**
     * 根据文件绝对路径，获取文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        String[] temp = path.replaceAll("\\\\", "/").split("/");
        String fileName = "";
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
        }
        return fileName;
    }

    /**
     * 生成一个2020111021548663(yyyyMMdd+8位随机数)的文件名
     *
     * @return
     */
    public static String makeFileName() {
        String name;
        // 生成8位随机数
        String SYMBOLS = "0123456789";
        Random RANDOM = new SecureRandom();
        char[] nonceChars = new char[8];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        // 生成时间信息
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        // 组合文件名
        name = sf.format(date) + new String(nonceChars);
        return name;
    }

    public static ArrayList<File> iterateFileDir(File sourceFile, ArrayList<File> files) {
        if (null == files) {
            files = new ArrayList<File>();
        }
        if (sourceFile.isDirectory()) {
            File[] listFiles = sourceFile.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                File listFile = listFiles[i];
                iterateFileDir(listFile, files);
            }
        } else {
            files.add(sourceFile);
        }
        return files;
    }

    public static void unzipFile(String zipPtath, String outputDirectory) throws IOException {
        /**
         * 解压assets的zip压缩文件到指定目录
         * @param context上下文对象
         * @param assetName压缩文件名
         * @param outputDirectory输出目录
         * @param isReWrite是否覆盖
         * @throws IOException
         */

        Log.i(TAG, "开始解压的文件： " + zipPtath + "\n" + "解压的目标路径：" + outputDirectory);
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                try {
                    f.delete();
                } catch (Exception e) {
                }
            }
        }

        // 打开压缩文件
        InputStream inputStream = new FileInputStream(zipPtath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            Log.i(TAG, "解压文件 入口 1： " + zipEntry);
            if (!zipEntry.isDirectory()) {  // 如果是一个文件
                // 如果是文件
                String fileName = zipEntry.getName();
                Log.i(TAG, "解压文件 原来 文件的位置： " + fileName);
                Log.i(TAG, "解压文件 的名字： " + fileName);
                file = new File(outputDirectory + File.separator + fileName);  // 放到新的解压的文件路径
                createFile(file);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();

            } else {
                String fileName = zipEntry.getName();
                Log.i(TAG, "解压文件 原来 文件夹的位置： " + fileName);
                createDir(outputDirectory + File.separator + fileName);
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
            Log.i(TAG, "解压文件 入口 2： " + zipEntry);
        }
        zipInputStream.close();
        Log.i(TAG, "解压完成");
    }

    /**
     * 保存文件到sd卡
     *
     * @param filePath
     */
    public static void saveZipFile(InputStream is, long total, String filePath, String name, DownLoadCallBack downLoadCallBack) {

        Log.d("Pcm_total", total + "");
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;

        try {
            File downloadFile = new File(filePath);
            if (!downloadFile.exists()) {
                // downloadFile.createNewFile()
                boolean mkdirSuccess = downloadFile.mkdirs();
                Log.e(TAG, "mkdirSuccess:" + mkdirSuccess);
            }
            File file = new File(filePath, name);
            if (!file.exists()) {
                boolean createFileSuccess = file.createNewFile();
                Log.e(TAG, "createFileSuccess:" + createFileSuccess);
            }
            fos = new FileOutputStream(file);
            long sum = 0;

            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                // 下载中，可以自行加入进度条
                Log.d("PcmD", progress + "");
                if (progress == 100) {
                    downLoadCallBack.callBack();
                } else if (progress < 0) {
                    downLoadCallBack.errorBack();
                }
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }

        /**
         * 下载完成。可以解压
         * 如果是经过加密的zip包，先解密，再解压
         */

    }

    public interface DownLoadCallBack {
        void callBack();

        void errorBack();
    }

}
