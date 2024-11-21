package com.divine.yang.base;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Divine
 * CreateDate: 2021/4/30
 * Describe:
 * log日志统计保存
 */
public class LocalLogcat {

    private static LocalLogcat INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    private int mPId;
    private Context context;

    private LocalLogcat(Context context, String dirPath) {
        init(context, dirPath);
        mPId = android.os.Process.myPid();
    }

    public static LocalLogcat getInstance(Context context, String dirPath) {
        if (INSTANCE == null) {
            INSTANCE = new LocalLogcat(context, dirPath);
        }
        return INSTANCE;
    }

    /**
     * 初始化目录
     */
    public void init(Context context, String dirPath) {
        this.context = context;
        // PATH_LOGCAT = Environment.getExternalStorageDirectory().getAbsolutePath() + dirPath;
        PATH_LOGCAT = context.getExternalCacheDir() + dirPath;
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void start() {
        if (mLogDumper == null) {
            mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
        }
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private FileOutputStream out = null;
        private String fileName;

        public LogDumper(String pid, String dir) {
            Date now = new Date(); // 创建一个Date对象，获取当前时间
            // 指定格式化格式
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
            fileName = f.format(now) + ".log";
            Log.e("print", "文件名生成成功：" + fileName);
            mPID = pid;
            try {
                File file = new File(dir, "log-" + fileName);
                Log.e("print", "即将生成文件：" + file.getAbsolutePath());
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    boolean dirCreated = dirFile.mkdirs();
                    if (dirCreated) {
                        if (!file.exists()) {
                            Log.e("print", "文件创建中：" + file.getAbsolutePath());
                            boolean created = file.createNewFile();
                            if (created) {
                                Log.e("print", "文件创建成功：" + file.getAbsolutePath());
                                out = new FileOutputStream(file);
                            } else {
                                Log.e("print", "文件创建失败：" + file.getAbsolutePath());
                            }
                        } else {
                            Log.e("print", "文件已存在：" + file.getAbsolutePath());
                        }
                    }
                } else {
                    Log.e("print", "文件目录已存在：" + dir);
                    if (!file.exists()) {
                        Log.e("print", "文件创建中：" + file.getAbsolutePath());
                        boolean created = file.createNewFile();
                        if (created) {
                            Log.e("print", "文件创建成功：" + file.getAbsolutePath());
                            out = new FileOutputStream(file);
                        } else {
                            Log.e("print", "文件创建失败：" + file.getAbsolutePath());
                        }
                    } else {
                        Log.e("print", "文件已存在：" + file.getAbsolutePath());
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
                Log.e("print:catch", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("print:catch", e.getMessage());
            }

            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             * */

            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息
            // cmds = "logcat -s way";//打印标签过滤信息
            cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";

        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {
                        out.write((new Date().getTime() + "  " + line + "\n").getBytes());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }

            }

        }

    }

}
